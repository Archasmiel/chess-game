package engine.time;

import java.util.ArrayList;

public class Timers extends ArrayList<ITimer> {

    public Timers() {

    }

    public void tick(float dt) {
        this.forEach(e -> {
            if (e.blocked()) {
                e.tick(dt);
            }
        });
    }

}
