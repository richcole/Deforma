package game.voxel;

public class MeshContainer implements Model {
	
	Model model;
	private Model lineModel;
	
	MeshContainer() {
	}

	@Override
	public void render() {
		if ( model != null ) {
			model.render();
		}
		if ( lineModel != null ) {
			lineModel.render();
		}
	}

	@Override
	public void setModelTr(Matrix tr) {
	}
	
	public void setModel(Model model) {
		this.model = model;
	}

	public void setLineModel(Model lineModel) {
		this.lineModel = lineModel;
	}

}
