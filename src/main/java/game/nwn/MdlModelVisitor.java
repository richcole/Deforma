package game.nwn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.nwn.readers.MdlModel;
import game.nwn.readers.MdlNodeHeader;

public class MdlModelVisitor {

	static private Logger logger = LoggerFactory.getLogger(MdlModelVisitor.class);

	interface AnimVisitor {
		void preVisit(MdlNodeHeader node, MdlNodeHeader fromNode, double alpha);
		void postVisit(MdlNodeHeader node, MdlNodeHeader fromNode, double alpha);
	}

	interface MeshVisitor {
		void preVisit(MdlNodeHeader node);
		void postVisit(MdlNodeHeader node);
	}

	public MdlModelVisitor() {
	}

	public void visit(MdlModel mdl, MeshVisitor visitor) {
		visit(mdl.getGeometryHeader().getGeometry(), visitor);
	}

	public void visit(MdlNodeHeader node, MeshVisitor visitor) {
		visitor.preVisit(node);
		for(MdlNodeHeader child: node.getChildren()) {
			visit(child, visitor);
		}
		visitor.postVisit(node);
	}

}
