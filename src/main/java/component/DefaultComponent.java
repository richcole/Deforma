package component;

import com.google.common.collect.Lists;
import functional.FUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static functional.FUtils.asStream;
import static functional.FUtils.instanceOf;

public class DefaultComponent implements Component {

	private List<Component> components = Lists.newArrayList();
	private Optional<Component> parent;

	@Getter
	@Setter
	private boolean enabled = true;

	@Getter
	private Transform localTransform;

	public DefaultComponent() {
		this.localTransform = Transform.IDENTITY;
		this.parent = Optional.empty();
	}

	public DefaultComponent(Transform localTransform) {
		this.localTransform = localTransform;
	}

	@Override
	public <T> Optional<T> getComponent(Class<T> componentClass) {
		return FUtils.castOptional(FUtils.firstThat(components, (c) -> instanceOf(c, componentClass)));
	}

	@Override
	public <T> FUtils.Stream<T> getComponents(Class<T> componentClass) {
		return getComponents().filter(c -> instanceOf(c, componentClass)).map(c -> (T)c);
	}

	public FUtils.Stream<Component> getComponents() {
		return asStream(components);
	}

	@Override
	public Optional<Component> getParent() {
		return FUtils.castOptional(parent);
	}

	@Override
	public <T> void forEachDecendent(Class<T> componentClass, Consumer<T> consumer) {
		asStream(components).forEach(child -> {
			if (child.isEnabled() && instanceOf(child, componentClass)) {
				consumer.accept((T)child);
			}
			child.forEachDecendent(componentClass, consumer);
		});
	}

	@Override
	public Transform getGlobalTransform() {
		return getParent()
			.map((parent) ->
				parent.getGlobalTransform().transform(getLocalTransform()))
			.orElseGet(() ->
				getLocalTransform());
	}

	@Override
	public void setLocalTransform(Transform localTransform) {
		this.localTransform = localTransform;
	}

	@Override
	public void addComponent(Component childComponent) {
		components.add(childComponent);
		childComponent.setParent(this);
	}

	@Override
	public void setParent(Component parent) {
		this.parent = Optional.ofNullable(parent);
	}

}
