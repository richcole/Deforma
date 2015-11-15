package game;

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

	@Override
	public void init() {
		model.init();
	}

	@Override
	public void dispose() {
		model.dispose();
	}

	@Override
	public void ensureInitialized() {
		model.ensureInitialized();
	}

}
