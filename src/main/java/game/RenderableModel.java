package game;

import game.basicgeom.Matrix;

public class RenderableModel implements RenderableResource {
	
	private ModelResource model;
	private Matrix modelTr;

	public RenderableModel(ModelResource model, Matrix modelTr) {
		super();
		this.modelTr = modelTr;
		this.model = model;
	}

	public RenderableModel(ModelResource model) {
		this(model, Matrix.IDENTITY);
	}

	@Override
	public void render() {
		model.render(modelTr);
	}

	public void setModelTr(Matrix modelTr) {
		this.modelTr = modelTr;
	}

}
