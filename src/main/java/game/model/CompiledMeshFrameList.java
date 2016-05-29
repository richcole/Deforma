package game.model;

import java.util.List;

import com.google.common.collect.Lists;

import game.math.Matrix;

public class CompiledMeshFrameList {

	List<CompiledMeshFrame> meshFrameList = Lists.newArrayList();
	double totalFrameTime;

	float alpha = 0;
	
	public CompiledMeshFrameList(double totalFrameTime) {
		this.totalFrameTime = totalFrameTime;
	}

	public void add(Frame frame, CompiledMesh compiledMesh) {
		meshFrameList.add(new CompiledMeshFrame(frame, compiledMesh));
	}

	public void render(Matrix viewTr, Matrix modelTr, int frame, double t) {
		t = t % totalFrameTime;
		meshFrameList.get(frame).mesh.render(viewTr, modelTr, alpha);
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

	public int getFrame(double t) {
		t = t % totalFrameTime;
		for(int i=0;i < meshFrameList.size(); ++i) {
			CompiledMeshFrame meshFrame = meshFrameList.get(i);
			double beginTime = meshFrame.frame.beginTime;
			double endTime = getEndTime(i);
			if ( t >= beginTime && t < endTime ) {
				alpha = (float)((t - beginTime) / (endTime - beginTime));
				return i;
			}
		}
		return 0;
	}
	
}
