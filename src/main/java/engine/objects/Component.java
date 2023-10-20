package engine.objects;

public abstract class Component {

    protected GameObject gameObject;

    public abstract void update(float dt);

    public void start() {

    }

    public GameObject getGameObject() {
        return gameObject;
    }

}
