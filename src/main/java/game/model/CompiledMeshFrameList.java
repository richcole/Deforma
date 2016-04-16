package game.model;

import java.util.List;

import com.google.common.collect.Lists;

import game.math.Matrix;

public class CompiledMeshFrameList implements TransformRenderable {

	List<CompiledMeshFrame> meshFrameList = Lists.newArrayList();
	double totalFrameTime;

	int currentFrame = 0;
	double t;
	float alpha = 0;
	
	public CompiledMeshFrameList(double totalFrameTime) {
		this.totalFrameTime = totalFrameTime;
	}

	public void add(Frame frame, CompiledMesh compiledMesh) {
		meshFrameList.add(new CompiledMeshFrame(frame, compiledMesh));
	}

	@Override
	public void render(Matrix viewTr, Matrix modelTr) {
		meshFrameList.get(currentFrame).mesh.render(viewTr, modelTr, alpha);
	}

	public void nextFrame() {
		currentFrame = currentFrame + 1;
		if (currentFrame >= meshFrameList.size()) {
			currentFrame = 0;
		}
	}

	private double getEndTime(int i) {
		double endTime;
		if (i + 1 < meshFrameList.size()) {
			endTime = meshFrameList.get(i+1).frame.beginTime;
		}
		else {
			endTime = totalFrameTime;
		}
		return endTime;
	}

	public void advanceSeconds(double dt) {
		t = (t + dt) % totalFrameTime;
		for(int i=0;i < meshFrameList.size(); ++i) {
			CompiledMeshFrame meshFrame = meshFrameList.get(i);
			double beginTime = meshFrame.frame.beginTime;
			double endTime = getEndTime(i);
			if ( t >= beginTime && t < endTime ) {
				alpha = (float)((t - beginTime) / (endTime - beginTime));
				currentFrame = i;
				break;
			}
		}
	}
	
}
