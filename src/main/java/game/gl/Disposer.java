package game.gl;

import game.events.Clock;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Disposer {
  
  private static Logger log = LoggerFactory.getLogger(Disposer.class);
  
  public List<Runnable> actions = Lists.newArrayList();
  
  public Disposer(Clock clock) {
    clock.onTick((e) -> onTick());
  }

  public void dispose(Runnable dispose) {
    synchronized(this) {
      if ( dispose != null ) {
        actions.add(dispose);
      }
    }
  }
  
  public void onTick() {
    List<Runnable> oldActions = null;
    synchronized(this) {
      if ( ! actions.isEmpty() ) {
        oldActions = actions;
        actions = Lists.newArrayList();
      }
    }
    if ( oldActions != null ) {
      for(Runnable action: oldActions) {
        try {
          action.run();
        }
        catch(Exception e) {
          log.error("Error executing dispose action", e);
        }
      }
    }
  }

}
