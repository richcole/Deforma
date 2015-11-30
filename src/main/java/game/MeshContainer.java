package game;

public class MeshContainer implements ModelResource {
	
	private ModelResource model;
	private ModelResource lineModel;
	
	@Override
	public void render(Matrix tr) {
		if ( model != null ) {
			model.render(tr);
		}
		if ( lineModel != null ) {
			lineModel.render(tr);
		}
	}

	public void setModel(ModelResource model) {
		this.model = model;
	}

	public void setLineModel(ModelResource lineModel) {
		this.lineModel = lineModel;
	}

}
