package component;

import java.util.function.Consumer;

public interface Painter {
	void paint(UIComponent.PaintContext painter);
}