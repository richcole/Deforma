package game.voxel;

import java.util.List;

import com.google.common.collect.Lists;

public class GLResourceList extends GLResource {
	
	protected List<GLResource> resources = Lists.newArrayList();
	
	public GLResourceList() {
		super();
	}
	
	public void addResource(GLResource resource) {
		resources.add(resource);
	}

	public void init() {
		for(GLResource runable: resources) {
			runable.init();
		}
	}
	
	public void dispose() {
		for(GLResource runable: resources) {
			runable.dispose();
		}
	}
	

}
