package custom_shapes;

import custom_components.VectorScene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;


public class MyPolygon extends Polygon implements IShape {
    private int layer;
    private VectorScene scene;

    public MyPolygon(VectorScene sc, ArrayList<Double> coords, Color stroke, Color fill, int strokeWidth, int l){
        scene = sc;
        setFill(fill);
        setStroke(stroke);
        setStrokeWidth(strokeWidth);
        getPoints().addAll(coords);
        layer = l;
    }

    @Override
    public void move(double ox, double oy) {

    }

    @Override
    public void adjust(double mx, double my) {

    }

    @Override
    public String toSvg() {
        return null;
    }

    @Override
    public String toJvgf() {
        return null;
    }

    @Override
    public double getCenterX() {
        return 0;
    }

    @Override
    public double getCenterY() {
        return 0;
    }

    @Override
    public double getStartX() {
        return 0;
    }

    @Override
    public double getStartY() {
        return 0;
    }

    @Override
    public void setStartX(double x) {

    }

    @Override
    public void setStartY(double y) {

    }

    @Override
    public double getAdjustX() {
        return 0;
    }

    @Override
    public double getAdjustY() {
        return 0;
    }

    @Override
    public int getLayer() {
        return layer;
    }

    @Override
    public void setLayer(int i) {
        layer = i;
    }
}
