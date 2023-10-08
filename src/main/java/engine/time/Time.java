package engine.time;

public class Time {

    private static Time INSTANCE;
    private long startTime;

    private Time() {

    }

    public static float getTime() {
        return (float)((System.nanoTime() - Time.get().getStartTime()) * 1E-9);
    }

    public static Time get() {
        if (Time.INSTANCE == null) {
            Time.INSTANCE = new Time();
            Time.INSTANCE.startTime = System.nanoTime();
        }
        return Time.INSTANCE;
    }

    public long getStartTime() {
        return startTime;
    }

}
