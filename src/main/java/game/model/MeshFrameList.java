package game.model;

import java.util.List;

import com.google.common.collect.Lists;

public class MeshFrameList {
	List<MeshFrame> meshFrameList = Lists.newArrayList();
	double totalFrameTime;
	
	public MeshFrameList(double totalFrameTime) {
		this.totalFrameTime = totalFrameTime;
	}

	public void add(MeshFrame meshFrame) {
		meshFrameList.add(meshFrame);
		if ( meshFrameList.size() == 1 ) {
			meshFrame.mesh.p2 = meshFrame.mesh.p1;
		}
		else {
			MeshFrame prevFrame = meshFrameList.get(meshFrameList.size()-2);
			prevFrame.mesh.p2 = meshFrame.mesh.p1;
			meshFrame.mesh.p2 = meshFrameList.get(0).mesh.p1;
		}
	}
	
	public List<MeshFrame> getMeshFrameList() {
		return meshFrameList;
	}
	
	public double getTotalFrameTime() {
		return totalFrameTime;
	}
 	
}
