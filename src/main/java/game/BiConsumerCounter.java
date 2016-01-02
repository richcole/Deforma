package game;

import java.util.function.BiConsumer;

public class BiConsumerCounter<S, T> implements BiConsumer<S, T> {
  
  private int count = 0;

  @Override
  public void accept(S t, T u) {
    count += 1;
  }
  
  public int getCount() {
    return count;
  }
  
  public void reset() {
    count = 0;
  }

}
