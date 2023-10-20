package engine.utility;

public class GameTime {

    private static GameTime INSTANCE;
    private long startTime;

    private GameTime() {

    }

    public static float getTime() {
        return (float)((System.nanoTime() - GameTime.get().getStartTime()) * 1E-9);
    }

    public static GameTime get() {
        if (GameTime.INSTANCE == null) {
            GameTime.INSTANCE = new GameTime();
            GameTime.INSTANCE.startTime = System.nanoTime();
        }
        return GameTime.INSTANCE;
    }

    public long getStartTime() {
        return startTime;
    }

}
