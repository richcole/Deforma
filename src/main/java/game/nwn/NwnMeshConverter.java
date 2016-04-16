package game.nwn;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Stack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import game.math.Matrix;
import game.math.Quaternion;
import game.math.Utils;
import game.math.Vector;
import game.model.Frame;
import game.model.AnimSet;
import game.model.Mesh;
import game.model.MeshFrame;
import game.model.MeshFrameList;
import game.nwn.readers.MdlAnimation;
import game.nwn.readers.MdlFace;
import game.nwn.readers.MdlMeshHeader;
import game.nwn.readers.MdlModel;
import game.nwn.readers.MdlNodeHeader;

public class NwnMeshConverter {
	
	static class FrameCollector implements MdlModelVisitor.MeshVisitor {
		Set<Double> beginTimes = Sets.newHashSet();
		double      length = 0;
		
		@Override
		public void preVisit(MdlNodeHeader node) {
			addTimings(node.getPositionTimings());
			addTimings(node.getOrientationTimings());
		}

		private void addTimings(float[] ts) {
			if ( ts != null ) {
				for(float t: ts) {
					addTiming(t);
				}
			}
		}

		private void addTiming(double beginTime) {
			this.length = Math.max(this.length, beginTime);
			beginTimes.add(beginTime);
		}

		@Override
		public void postVisit(MdlNodeHeader node) {
		}
		
		public List<Frame> getFrames() {
			List<Frame> frames = Lists.newArrayList();
			Double[] sortedBeginTimes = beginTimes.toArray(new Double[beginTimes.size()]);
			Arrays.sort(sortedBeginTimes);
			for(int i=0; i<sortedBeginTimes.length-1; ++i) {
				frames.add(new Frame(sortedBeginTimes[i], sortedBeginTimes[i+1]));
			}
			if ( sortedBeginTimes.length > 0 ) {
				frames.add(new Frame(sortedBeginTimes[sortedBeginTimes.length-1], length));
			}
			return frames;
		};
	}
	
	static class AnimDescCollector implements MdlModelVisitor.MeshVisitor {
		
		private Map<String, MdlNodeHeader> animNodeMap = Maps.newHashMap();

		public AnimDescCollector() {
		}

		@Override
		public void preVisit(MdlNodeHeader node) {
			animNodeMap.put(node.getName(), node);
		}

		@Override
		public void postVisit(MdlNodeHeader node) {
		}
		
	}
	
	static class BTrCollector implements MdlModelVisitor.MeshVisitor {

		private AnimDescCollector anim;
		private Frame frame;
		private Stack<MdlNodeHeader> path = new Stack<MdlNodeHeader>();
		private List<Matrix> bTr = Lists.newArrayList();

		public BTrCollector(AnimDescCollector anim, Frame frame) {
			this.anim = anim;
			this.frame = frame;
		}

		@Override
		public void preVisit(MdlNodeHeader node) {
			path.push(node);
			Matrix ltr = Matrix.IDENTITY;
			for(MdlNodeHeader pNode: path) {
				MdlNodeHeader animNode = anim.animNodeMap.get(pNode.getName());
				if ( pNode.getPosition() != null ) {
					ltr = ltr.times(Matrix.translate(pNode.getPosition()[0]));
				}
				if ( animNode != null && animNode.getPositionTimings() != null) {
					int frameIndex = getTimingIndex(frame, animNode.getPositionTimings());
					Vector tp;
					if ( frameIndex+1 < animNode.getPositionTimings().length ) {
						double alpha = 1.0 - getAlpha(frame, frameIndex, animNode.getPositionTimings());
						tp = Utils.lerp(alpha, animNode.getPosition()[frameIndex], animNode.getPosition()[frameIndex+1]);
					}
					else {
						tp = animNode.getPosition()[frameIndex];
					}
					ltr = ltr.times(Matrix.translate(tp));
				}
				if ( pNode.getOrientation() != null ) {
					ltr = ltr.times(pNode.getOrientation()[0].toMatrix());
				}
				if ( animNode != null && animNode.getOrientationTimings() != null ) {
					int frameIndex = getTimingIndex(frame, animNode.getOrientationTimings());
					Quaternion q;
					if ( frameIndex+1 < animNode.getOrientationTimings().length ) {
						double alpha = 1.0 - getAlpha(frame, frameIndex, animNode.getOrientationTimings());
						q = Utils.lerp(alpha, animNode.getOrientation()[frameIndex], animNode.getOrientation()[frameIndex+1]);
					}
					else {
						q = animNode.getOrientation()[frameIndex];
					}
					ltr = ltr.times(q.toMatrix());
				}
			}
			bTr.add(ltr.transpose());
		}

		private double getAlpha(Frame frame, int frameIndex, float[] ts) {
			return (frame.beginTime - ts[frameIndex]) / (ts[frameIndex+1] - ts[frameIndex]);
		}

		@Override
		public void postVisit(MdlNodeHeader node) {
			path.pop();
		}
			
		private int getTimingIndex(Frame frame, float[] positionTimings) {
			for(int i=0; i<positionTimings.length; ++i) {
				if ( frame.beginTime >= positionTimings[i] && (i == (positionTimings.length - 1) || frame.beginTime <= positionTimings[i+1] )) {
					return i;
				}
			}
			return 0;
		}
		
		public List<Matrix> getBTr() {
			return bTr;
		}

	}

	static class PlaneCollector implements MdlModelVisitor.MeshVisitor {
		private List<Vector> p = Lists.newArrayList();
		private List<Vector> n = Lists.newArrayList();
		private List<Vector> t = Lists.newArrayList();
		private List<Integer> e = Lists.newArrayList();
		private List<Integer> i = Lists.newArrayList();
		private List<Integer> b = Lists.newArrayList();

		private Lexicon boneLexicon = new Lexicon();
		private Lexicon imageLexicon = new Lexicon();
		
		private Stack<MdlNodeHeader> path = new Stack<MdlNodeHeader>();

		public PlaneCollector() {
		}
		
		@Override
		public void preVisit(MdlNodeHeader node) {
			MdlMeshHeader mhdr = node.getMeshHeader();
			int boneIndex = boneLexicon.getIndex(node.getName());
			path.push(node);			
			addFaces(mhdr, boneIndex);			
		}

		private void addFaces(MdlMeshHeader mhdr, int boneIndex) {
			if ( mhdr != null && mhdr.getRender() == 1) {
				for(MdlFace face: mhdr.getFaces()) {
					for(int localTextureIndex=0; localTextureIndex<mhdr.getTextureCount(); ++localTextureIndex) {
						String texture = mhdr.getTextures()[localTextureIndex];
						Integer textureIndex = imageLexicon.getIndex(texture);
						for(int vertexIndex: face.getVertex()) {
							e.add(p.size());
							Vector pos = mhdr.getVertices()[vertexIndex];
							p.add(pos);
							n.add(face.getPlaneNormal());
							t.add(mhdr.getTexturePoints()[0][vertexIndex]);
							i.add(textureIndex);
							b.add(boneIndex);
						}
					}
				}
			}
		}

		@Override
		public void postVisit(MdlNodeHeader node) {
			path.pop();
		}
		
		private Mesh getMesh(List<Matrix> btr) {
			Mesh mesh = new Mesh(p.size(), e.size());
			
			List<Vector> np = Lists.newArrayList();
			for(int i=0;i<p.size();++i) {
				np.add(btr.get(b.get(i)).transpose().times(p.get(i)));
			}
			
			mesh.p1 = Utils.toDoubleArray3(np);
			mesh.n = Utils.toDoubleArray3(n);
			mesh.t = Utils.toDoubleArray2(t);
			mesh.i = Utils.toIntArray(i);
			mesh.e = Utils.toIntArray(e);
			mesh.imageList = imageLexicon.toList();
			return mesh;
		}
	}
	
	public AnimSet convertToMeshFrameList(MdlModel model) {
		
		AnimSet animSet = new AnimSet();
		
		for(String animName: model.getAnimMap().keySet()) {
			MdlAnimation animNode = model.getAnimMap().get(animName);
			double animLength = animNode.getLength();
			MdlNodeHeader animGeometry = animNode.getGeometryHeader().getGeometry();
	
			MdlModelVisitor modelVisitor = new MdlModelVisitor();
	
			PlaneCollector planeCollector = new PlaneCollector();
			modelVisitor.visit(model.getGeometryHeader().getGeometry(), planeCollector);
			
			FrameCollector frameCollector = new FrameCollector();
			modelVisitor.visit(animGeometry, frameCollector);
			List<Frame> frames = frameCollector.getFrames();
	
			AnimDescCollector animCollector = new AnimDescCollector();
			modelVisitor.visit(animGeometry, animCollector);
	
			MeshFrameList meshFrameList = new MeshFrameList(animLength);
			for(Frame frame: frames) {
				BTrCollector btrCollector = new BTrCollector(animCollector, frame);
				modelVisitor.visit(model.getGeometryHeader().getGeometry(), btrCollector);
				meshFrameList.add(new MeshFrame(planeCollector.getMesh(btrCollector.getBTr()), frame));
			}
			
			animSet.put(animName, meshFrameList);
		}
		
		return animSet;
	}
}
