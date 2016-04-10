package game.format.xps;

import game.SimpleProgram;
import game.basicgeom.Vector;
import game.events.EventBus;
import game.geom.Geom;
import game.geom.LinesGeom;
import game.geom.MultiLayerMeshGeom;
import game.image.Material;
import game.image.MaterialSource;
import game.mesh.CompiledMesh;
import game.mesh.CompiledMeshList;

import java.util.List;

import com.google.common.collect.Lists;

public class XPSModelBuilder {
	
	private MaterialSource materialSource;
	private Material boneMaterial;
  private EventBus eventBus;

	public XPSModelBuilder(EventBus eventBus, MaterialSource materialSource, Material boneMaterial) {
		this.materialSource = materialSource;
		this.boneMaterial = boneMaterial;
		this.eventBus = eventBus;
	}

	public List<Geom> toMeshGeom(XPSModel model) {
		List<Geom> geoms = Lists.newArrayList();
		LinesGeom boneGeom = new LinesGeom(boneMaterial);
		for(XPSMesh mesh: model.meshes) {
			MultiLayerMeshGeom meshGeom = new MultiLayerMeshGeom();
			
			// FIXME: need to change the structure here
			for(int i=0;i<mesh.numUvLayers;++i) {
				for(XPSTexture texture: mesh.textures) {
					if ( texture.uvLayer == i) {
						meshGeom.addMaterial(materialSource.get(model.root, texture.name));
					}
					break;
				}
			}
			
			for(XPSVertex vertex: mesh.vertices) {
				meshGeom.addVertex(vertex.v, vertex.n);
				for(int i=0;i<mesh.numUvLayers;++i) {
					meshGeom.addTexCoord(i, vertex.st[(2*i)+0], 1.0 - vertex.st[(2*i)+1]);
				}
			}
			
			for(XPSElement element: mesh.elements) {
				meshGeom.addElement(element.a, element.b, element.c);
			}			
			
			geoms.add(meshGeom);
		}
		return geoms;
	}
	
	public LinesGeom getBoneGeom(XPSModel model) {
		LinesGeom boneGeom = new LinesGeom(boneMaterial);
		for(XPSMesh mesh: model.meshes) {
			
			for(XPSBone bone: model.bones) {
				Vector from = Vector.Z;
				for(XPSBone currBone = getParentBone(bone, model.bones); currBone != null; currBone = getParentBone(currBone, model.bones)) {
					from = from.plus(currBone.vector);
				}
				Vector to = from.plus(bone.vector);
				boneGeom.addLine(from, to, 0.01);
			}
			
		}
		return boneGeom;
	}

	private XPSBone getParentBone(XPSBone bone, List<XPSBone> bones) {
		if ( bone.parentIndex >= 0 && bone.parentIndex < bones.size() ) {
			return bones.get(bone.parentIndex);
		}
		return null;
	}

	public CompiledMeshList toCompiledMesh(SimpleProgram simpleProgram, Material material, XPSModel model) {
		return new CompiledMeshList(eventBus, simpleProgram, toMeshGeom(model));
	}
	
	public CompiledMesh getBoneCompiledMesh(SimpleProgram simpleProgram, Material material, XPSModel model) {
		return new CompiledMesh(eventBus, simpleProgram, getBoneGeom(model));
	}
}
