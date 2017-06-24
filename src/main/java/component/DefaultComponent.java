package component;

import com.google.common.collect.Lists;
import functional.FUtils;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static functional.FUtils.asStream;
import static functional.FUtils.instanceOf;

public class DefaultComponent implements Component {

	private List<Component> components = Lists.newArrayList();
	private Optional<Component> parent;

	@Getter
	private Transform localTransform;
	@Getter
	private Transform invLocalTransform;

	public DefaultComponent() {
		this.localTransform = Transform.IDENTITY;
		this.invLocalTransform = Transform.IDENTITY;
		this.parent = Optional.empty();
	}

	public DefaultComponent(Transform localTransform) {
		this.localTransform = localTransform;
		this.invLocalTransform = localTransform.invert();
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
			if (instanceOf(child, componentClass)) {
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
	public Transform getInvGlobalTransform() {
		return getParent()
			.map((parent) ->
				getInvLocalTransform().transform(parent.getInvGlobalTransform()))
			.orElseGet(() ->
				getInvLocalTransform());
	}

	@Override
	public void setLocalTransform(Transform localTransform) {
		this.localTransform = localTransform;
		this.invLocalTransform = localTransform.invert();
	}

	@Override
	public void setInvLocalTransform(Transform invLocalTransform) {
		this.invLocalTransform = invLocalTransform;
		this.localTransform = invLocalTransform.invert();
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
