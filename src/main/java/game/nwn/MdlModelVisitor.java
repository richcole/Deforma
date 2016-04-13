package game.nwn;

import game.nwn.readers.MdlAnimation;
import game.nwn.readers.MdlGeometryHeader;
import game.nwn.readers.MdlModel;
import game.nwn.readers.MdlNodeHeader;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class MdlModelVisitor {

	static private Logger logger = LoggerFactory.getLogger(MdlModelVisitor.class);

	MdlModel mdl;

	interface AnimVisitor {
		void preVisit(MdlNodeHeader node, MdlNodeHeader fromNode, double alpha);
		void postVisit(MdlNodeHeader node, MdlNodeHeader fromNode, double alpha);
	}

	interface MeshVisitor {
		void preVisit(MdlNodeHeader node);
		void postVisit(MdlNodeHeader node);
	}

	public MdlModelVisitor(MdlModel mdl) {
		this.mdl = mdl;
	}

	public void visit(MeshVisitor visitor) {
		visit(mdl.getGeometryHeader().getGeometry(), visitor);
	}

	private void visit(MdlNodeHeader node, MeshVisitor visitor) {
		visitor.preVisit(node);
		for(MdlNodeHeader child: node.getChildren()) {
			visit(child, visitor);
		}
		visitor.postVisit(node);
	}

	private void visit(MdlNodeHeader geometry, MdlNodeHeader fromGeom, double alpha, AnimVisitor visitor) {
		visitor.preVisit(geometry, fromGeom, alpha);
		if (fromGeom != null) {
			MdlNodeHeader[] c1 = geometry.getChildren();
			MdlNodeHeader[] c2 = fromGeom.getChildren();
			for (int i = 0; i < c1.length; ++i) {
				boolean found = false;
				for (int j = 0; j < c2.length; ++j) {
					if (c1[i].getName().equals(c2[j].getName())) {
						visit(c1[i], c2[j], alpha, visitor);
						found = true;
						break;
					}
				}
				if (!found) {
					visit(c1[i], null, alpha, visitor);
				}
			}
		} else {
			MdlNodeHeader[] c1 = geometry.getChildren();
			for (int i = 0; i < c1.length; ++i) {
				visit(c1[i], null, alpha, visitor);
			}
		}
		visitor.postVisit(geometry, fromGeom, alpha);
	}

	public Set<Integer> getNumberOfFrames(String name) {
		MdlAnimation anim = mdl.getAnimMap().get(name);
		Set<Integer> numberOfFrames = Sets.newHashSet();
		getNumberOfFrames(anim.getGeometryHeader(), numberOfFrames);
		return numberOfFrames;
	}

	private void getNumberOfFrames(MdlGeometryHeader header, Set<Integer> frames) {
		if (header.getGeometry() != null && header.getGeometry().getPosition() != null) {
			frames.add(header.getGeometry().getPosition().length);
		}
		if (header.getGeometry() != null && header.getGeometry().getOrientation() != null) {
			frames.add(header.getGeometry().getOrientation().length);
		}
		for (MdlNodeHeader child : header.getGeometry().getChildren()) {
			if (child.getOrientation() != null) {
				frames.add(child.getOrientation().length);
			}
			if (child.getPosition() != null) {
				frames.add(child.getPosition().length);
			}
			if (child.getGeomemtryHeader() != null) {
				getNumberOfFrames(child.getGeomemtryHeader(), frames);
			}
		}
	}
}
