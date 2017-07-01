package component;

public class SimpleSystemClock implements SimpleClock {
	@Override
	public long now() {
		return System.currentTimeMillis();
	}

	@Override
	public long durationOfSeconds(double i) {
		return (long)(1000 * i);
	}
}
