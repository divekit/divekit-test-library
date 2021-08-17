package thkoeln.st.springtestlib.specification.diagram.elements;

public class Point {

    private float x;
    private float y;


    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point add(Point p) {
        this.x += p.getX();
        this.y += p.getY();
        return this;
    }

    public Point scale(float scale) {
        this.x *= scale;
        this.y *= scale;
        return this;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
