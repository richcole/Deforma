package game.nwn;

import java.util.List;
import java.util.Stack;

import com.google.common.collect.Lists;

import game.math.Matrix;
import game.math.Utils;
import game.math.Vector;
import game.model.Mesh;
import game.nwn.readers.MdlFace;
import game.nwn.readers.MdlMeshHeader;
import game.nwn.readers.MdlModel;
import game.nwn.readers.MdlNodeHeader;

public class NwnMeshConverter {

	static class PlaneCollector implements MdlModelVisitor.MeshVisitor {
		List<Vector> p = Lists.newArrayList();
		List<Vector> n = Lists.newArrayList();
		List<Vector> t = Lists.newArrayList();
		List<Integer> e = Lists.newArrayList();
		List<Integer> i = Lists.newArrayList();
		Lexicon imageLexicon = new Lexicon();
		
		Stack<Matrix> trs = new Stack<Matrix>();
		Matrix tr = Matrix.IDENTITY;

		@Override
		public void preVisit(MdlNodeHeader node) {
			MdlMeshHeader mhdr = node.getMeshHeader();
			trs.push(tr);

			Matrix ltr = Matrix.IDENTITY;
			if ( node.getPosition() != null ) {
				ltr = ltr.times(Matrix.translate(node.getPosition()[0]));
			}
			if ( node.getOrientation() != null ) {
				ltr = ltr.times(node.getOrientation()[0].toMatrix());
			}
			tr = tr.times(ltr);
			if ( mhdr != null && mhdr.getRender() == 1) {
				for(MdlFace face: mhdr.getFaces()) {
					for(int localTextureIndex=0; localTextureIndex<mhdr.getTextureCount(); ++localTextureIndex) {
						String texture = mhdr.getTextures()[localTextureIndex];
						Integer textureIndex = imageLexicon.getIndex(texture);
						for(int vertexIndex: face.getVertex()) {
							e.add(p.size());
							Vector pos = mhdr.getVertices()[vertexIndex];
							p.add(tr.times(pos));
							n.add(face.getPlaneNormal());
							t.add(mhdr.getTexturePoints()[0][vertexIndex]);
							i.add(textureIndex);
						}
					}
				}
			}
		}

		@Override
		public void postVisit(MdlNodeHeader node) {
			tr = trs.pop();
		}
		
		private Mesh getMesh() {
			Mesh mesh = new Mesh(p.size(), e.size());
			mesh.p = Utils.toDoubleArray3(p);
			mesh.n = Utils.toDoubleArray3(n);
			mesh.t = Utils.toDoubleArray2(t);
			mesh.i = Utils.toIntArray(i);
			mesh.e = Utils.toIntArray(e);
			mesh.b = new int[p.size()];
			mesh.imageList = imageLexicon.toList();
			return mesh;
		}
	}

	public Mesh convert(MdlModel model) {
		PlaneCollector planeCollector = new PlaneCollector();
		MdlModelVisitor visitor = new MdlModelVisitor(model);
		visitor.visit(planeCollector);
		return planeCollector.getMesh();
	}

}
