package gp.utils;

public interface Repeat {
    static void of(int numTimes, Runnable runnable) {
        for (int i = 0; i < numTimes; i++) {
            runnable.run();
        }
    }
}
