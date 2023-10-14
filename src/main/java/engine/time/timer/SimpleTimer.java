package engine.time.timer;

public class SimpleTimer implements ITimer {

    private boolean blocked;
    private float time;
    private final float maxTime;

    public SimpleTimer(float maxTime) {
        blocked = false;
        time = 0;
        this.maxTime = maxTime;
    }

    @Override
    public void reset() {
        time = maxTime;
        blocked = true;
    }

    @Override
    public void tick(float dt) {
        if (time - dt <= 0) {
            blocked = false;
        } else {
            time -= dt;
        }
    }

    @Override
    public boolean blocked() {
        return blocked;
    }

    @Override
    public boolean opened() {
        return !blocked;
    }
}
