package game;

public class MeshContainer extends AbstractGLResource implements ModelResource {
	
	private ModelResource model;
	private ModelResource lineModel;
	
	MeshContainer() {
	}

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

	@Override
	public void init() {
		if ( lineModel != null ) {
			lineModel.dispose();
		}
		if ( model != null ) {
			model.dispose();
		}
	}

	@Override
	public void dispose() {
		if ( lineModel != null ) {
			lineModel.dispose();
		}
		if ( model != null ) {
			model.dispose();
		}
	}

}
