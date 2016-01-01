package game.events;

public interface EventCallback<T> {
  void accept(T t);
}
