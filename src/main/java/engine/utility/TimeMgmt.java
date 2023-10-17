package engine.utility;

public class TimeMgmt {

    private static TimeMgmt INSTANCE;
    private long startTime;

    private TimeMgmt() {

    }

    public static float getTime() {
        return (float)((System.nanoTime() - TimeMgmt.get().getStartTime()) * 1E-9);
    }

    public static TimeMgmt get() {
        if (TimeMgmt.INSTANCE == null) {
            TimeMgmt.INSTANCE = new TimeMgmt();
            TimeMgmt.INSTANCE.startTime = System.nanoTime();
        }
        return TimeMgmt.INSTANCE;
    }

    public long getStartTime() {
        return startTime;
    }

}
