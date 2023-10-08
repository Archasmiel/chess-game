package lib;

public class RGBAWrapper {

    private float a;
    private boolean b;

    public RGBAWrapper(float a) {
        this.a = a;
        this.b = false;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public void setA1() {
        this.a = 1.0f;
    }

    public void setA0() {
        this.a = 0.0f;
    }

    public boolean getB() {
        return b;
    }

    public void switchB() {
        this.b = !this.b;
    }
}
