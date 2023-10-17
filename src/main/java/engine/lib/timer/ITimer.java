package engine.lib.timer;

public interface ITimer {

    void reset();
    void tick(float dt);

    boolean blocked();
    boolean opened();

}
