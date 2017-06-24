package component;

import component.gl.GlEngine;

public interface RenderTarget extends Component {
	void bind(GlEngine glEngine);
	void preRender(GlEngine glEngine);
	void postRender(GlEngine glEngine);
}
