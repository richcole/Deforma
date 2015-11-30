package game;

import java.util.List;

import com.google.common.collect.Lists;

public class CompiledMeshList implements ModelResource {
	
	private List<CompiledMesh> compiledMeshList = Lists.newArrayList();
	
	public CompiledMeshList(SimpleProgram simpleProgram, List<Geom> geomList) {
		for(Geom mesh: geomList) {
			compiledMeshList.add(new CompiledMesh(simpleProgram, mesh));
		}
	}

	@Override
	public void render(Matrix modelTr) {
		for(CompiledMesh compiledMesh: compiledMeshList) {
			compiledMesh.render(modelTr);
		}
	}

	public List<CompiledMesh> getMeshList() {
		return compiledMeshList;
	}
}
