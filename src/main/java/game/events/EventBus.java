package game.events;

import java.util.function.Consumer;

import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class EventBus {
  
  static class ObjectEventType {
    
    private Object object;
    private Class<?> eventType;
    
    public ObjectEventType(Object object, Class<?> eventType) {
      super();
      this.object = object;
      this.eventType = eventType;
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(object, eventType);
    }

    @Override
    public boolean equals(Object otherObject) {
      if ( otherObject instanceof ObjectEventType ) {
        ObjectEventType other = (ObjectEventType) otherObject;
        return Objects.equal(object, other.object) && Objects.equal(eventType, other.eventType);
      }
      return false;
    }
  }
  
  private Multimap<ObjectEventType, Consumer<? extends Event>> actionMap = ArrayListMultimap.create();

  public <T extends Event> void onEvent(Object subject, Class<T> eventType, Consumer<T> action) {
    ObjectEventType objectEventType = new ObjectEventType(subject, eventType);
    actionMap.put(objectEventType, action);
  }
  
  public <T extends Event> void emit(Object subject, T event) {
    ObjectEventType objectEventType = new ObjectEventType(subject, event.getClass());
    for(Consumer<?> action: actionMap.get(objectEventType)) {
      @SuppressWarnings("unchecked")
      Consumer<T> eventAction = (Consumer<T>) action;
      eventAction.accept(event);
    }
  }
}
