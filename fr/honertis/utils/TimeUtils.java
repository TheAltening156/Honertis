package fr.honertis.utils;

public class TimeUtils {
	public long lastMs = System.currentTimeMillis();
	
	public boolean check(float ms) {
		return lastMs >= ms;
	}
	
	public void reset() {
		lastMs = System.currentTimeMillis();
	}
	
	public boolean hasTimeElapsed(long time, boolean reset) {
		if (System.currentTimeMillis() - lastMs > time) {
			if (reset) reset();
			return true;
		}
		return false;
	}
	
	public long getCurrentTimeMillis() {
		return System.nanoTime() / 1000000L;
	}
}
