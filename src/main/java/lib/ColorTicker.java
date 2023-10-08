package lib;

public class ColorTicker {

    private final RGBAWrapper r;
    private final RGBAWrapper g;
    private final RGBAWrapper b;
    private final RGBAWrapper a;

    public ColorTicker() {
        r = new RGBAWrapper(0.0f);
        g = new RGBAWrapper(0.0f);
        b = new RGBAWrapper(0.0f);
        a = new RGBAWrapper(0.0f);
    }

    private void tickRGBAValue(RGBAWrapper value) {
        value.setA(value.getA() + (value.getB() ? -0.01f : 0.01f));
        if (value.getA() > 1.0f) {
            value.setA1();
            value.switchB();
        } else if (value.getA() <= 0.0f) {
            value.setA0();
            value.switchB();
        }
    }

    public void tickRGBA(int code) {
        switch (code) {
            case 0: tickRGBAValue(r); break;
            case 1: tickRGBAValue(g); break;
            case 2: tickRGBAValue(b); break;
            case 3: tickRGBAValue(a); break;
            default: throw new IllegalStateException("Illegal code");
        }
    }

    public float getR() {
        return r.getA();
    }

    public float getG() {
        return g.getA();
    }

    public float getB() {
        return b.getA();
    }

    public float getA() {
        return a.getA();
    }
}
