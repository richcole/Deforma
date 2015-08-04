package art;

import java.util.List;

import com.google.common.collect.Lists;

import game.Model;
import game.gl.GLResource;
import game.math.Matrix;

public class CompiledMeshList extends GLResource implements Model {
	
	private List<CompiledMesh> compiledMeshList = Lists.newArrayList();
	
	public CompiledMeshList(SimpleProgram simpleProgram, List<Mesh> meshList) {
		for(Mesh mesh: meshList) {
			compiledMeshList.add(new CompiledMesh(simpleProgram, mesh));
		}
	}

	@Override
	public void init() {
		for(CompiledMesh compiledMesh: compiledMeshList) {
			compiledMesh.ensureInitialized();
		}
	}

	@Override
	public void render() {
		for(CompiledMesh compiledMesh: compiledMeshList) {
			compiledMesh.render();
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	public void setModelTr(Matrix modelTr) {
		for(CompiledMesh compiledMesh: compiledMeshList) {
			compiledMesh.setModelTr(modelTr);
		}
	}

}
