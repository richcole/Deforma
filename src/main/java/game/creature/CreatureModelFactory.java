package game.creature;

import game.controllers.InputProcessor;
import game.events.Clock;
import game.events.EventBus;
import game.gl.GLFactory;
import game.image.CachingImageProvider;
import game.model.AnimSet;
import game.model.CompiledAnimSet;
import game.model.CompiledMesh;
import game.model.CompiledMeshFrameList;
import game.model.CompiledMeshProgram;
import game.model.CompiledTexture;
import game.model.CompositeImage;
import game.model.MeshFrame;
import game.model.MeshFrameList;
import game.model.UniformBindingPool;
import game.nwn.NwnMeshConverter;
import game.nwn.readers.KeyReader;
import game.view.View;

import java.util.Map.Entry;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatureModelFactory implements Function<String, CreatureModel> {

	private static Logger log = LoggerFactory
			.getLogger(CreatureModelFactory.class);

	private GLFactory glFactory;
	private UniformBindingPool bindingPool;
	private CompiledMeshProgram program;
	private CachingImageProvider imageProvider;
	private KeyReader keyReader;

	public CreatureModelFactory(GLFactory glFactory,
			UniformBindingPool bindingPool, CompiledMeshProgram program,
			View view, EventBus eventBus, Clock clock,
			InputProcessor inputProcessor, CachingImageProvider imageProvider,
			KeyReader keyReader) {
		this.glFactory = glFactory;
		this.bindingPool = bindingPool;
		this.program = program;
		this.imageProvider = imageProvider;
		this.keyReader = keyReader;
	}

	public CreatureModel apply(String modelName) {
		AnimSet animSet = new NwnMeshConverter()
				.convertToMeshFrameList(keyReader.getModel(modelName));
		CompositeImage compositeImage = new CompositeImage(imageProvider);

		for (Entry<String, MeshFrameList> animEntry : animSet) {
			log.info("Anim name: " + animEntry.getKey());
			for (MeshFrame meshFrame : animEntry.getValue().getMeshFrameList()) {
				compositeImage.addAll(meshFrame.mesh.imageList);
			}
		}
		CompiledTexture compiledTexture = new CompiledTexture(glFactory,
				compositeImage, false);
		CompiledAnimSet compiledAnimSet = new CompiledAnimSet();

		for (Entry<String, MeshFrameList> animEntry : animSet) {
			String animName = animEntry.getKey();
			MeshFrameList meshFrameList = animEntry.getValue();
			CompiledMeshFrameList compiledMeshFrameList = new CompiledMeshFrameList(
					meshFrameList.getTotalFrameTime());
			for (MeshFrame meshFrame : meshFrameList.getMeshFrameList()) {
				CompiledMesh compiledMesh = new CompiledMesh(glFactory,
						bindingPool, program, compiledTexture, meshFrame.mesh);
				compiledMeshFrameList.add(meshFrame.frame, compiledMesh);
			}
			compiledAnimSet.put(animName, compiledMeshFrameList);
		}

		return new CreatureModel(compiledAnimSet);
	}
}
