package custom_shapes;

import custom_components.VectorScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import misc.Others;

public class MyEllipse extends Ellipse implements IShape {
    private int layer;
    private VectorScene scene;

    public MyEllipse(VectorScene sc, int l, double ox, double oy, double rx, double ry, Color stroke, Color fill, int strokeWidth) {
        scene = sc;
        layer = l;
        setCenterX(ox);
        setCenterY(oy);
        setRadiusX(rx);
        setRadiusY(ry);
        setStroke(stroke);
        setFill(fill);
        setStrokeWidth(strokeWidth);
    }

    public MyEllipse(){}

    @Override
    public void move(double ox, double oy) {
        setCenterX(ox);
        setCenterY(oy);
    }

    @Override
    public void adjust(double mx, double my) {
        setRadiusX(Math.abs(getCenterX() - mx));
        setRadiusY(Math.abs(getCenterY() - my));
    }

    @Override
    public String toSvg() {
        String stroke = Others.getHtmlColor((Color) getStroke());
        String fill = Others.getHtmlColor((Color) getFill());
        return "<ellipse cx=\"" + (int) getCenterX() + "\" cy=\"" + (int) getCenterY() + "\" rx=\"" + (int) getRadiusX() +
                "\" ry=\"" + (int) getRadiusY() + "\" fill-opacity=\"" + getOpacity() + "\" stroke-width=\"" +
                (int) getStrokeWidth() + "\" stroke=\"" + stroke + "\" fill=\"" + fill +
                "\" transform=\"rotate(" + getRotate() + "," + getCenterX() + "," + getCenterY() + ")\" />\n";
    }

    @Override
    public String toJvgf() {
        String stroke = Others.getHtmlColor((Color) getStroke());
        String fill = Others.getHtmlColor((Color) getFill());
        return "ELLIPSE center_x " + getCenterX() + " center_y " + getCenterY() + " radius_x " + getRadiusX() + " radius_y " + getRadiusY() + " fill_opacity " +
                getOpacity() + " stroke_width " + getStrokeWidth() + " stroke_color " + stroke + " fill " + fill + " layer " + layer + " rotate " + getRotate() + "\n";
    }

    public MyEllipse clone(){
        MyEllipse e = new MyEllipse(scene, layer, getCenterX(), getCenterY(), getRadiusX(), getRadiusY(), (Color) getStroke(), (Color) getFill(), (int) getStrokeWidth());
        e.setRotate(getRotate());
        e.setOpacity(getOpacity());
        return e;
    }

    @Override
    public double getStartX() {
        return getCenterX();
    }

    @Override
    public double getStartY() {
        return getCenterY();
    }

    @Override
    public void setStartX(double x) {
        setCenterX(x);
    }

    @Override
    public void setStartY(double y) {
        setCenterY(y);
    }

    @Override
    public double getAdjustX() {
        return getCenterX() + getRadiusX();
    }

    @Override
    public double getAdjustY() {
        return getCenterY() + getRadiusY();
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
