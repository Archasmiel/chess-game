package engine.time;

public class SimpleTimer {

    private boolean blocked;
    private float time;
    private final float maxTime;

    public SimpleTimer(float maxTime) {
        blocked = false;
        time = 0;
        this.maxTime = maxTime;
    }

    public void reset() {
        time = maxTime;
        blocked = true;
    }

    public void tick(float dt) {
        if (blocked) {
            if (time - dt <= 0) {
                blocked = false;
            } else {
                time -= dt;
            }
        }
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isOpened() {
        return !blocked;
    }
}
