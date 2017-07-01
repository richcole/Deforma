package component;

import functional.FUtils;

import java.util.Optional;
import java.util.function.Consumer;

public interface Component {
	<T> Optional<T> getComponent(Class<T> componentClass);
	<T> FUtils.Stream<T> getComponents(Class<T> componentClass);
	Optional<Component> getParent();

	<T> void forEachDecendent(Class<T> componentClass, Consumer<T> consumer);

	Transform getLocalTransform();
	void setLocalTransform(Transform localTransform);

	Transform getGlobalTransform();

	default void init(Scene scene) {};
	default void update(Scene scene) {};

	void addComponent(Component childComponent);
	void setParent(Component parent);

	boolean isEnabled();
	void setEnabled(boolean isEnabled);
}
