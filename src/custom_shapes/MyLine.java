package custom_shapes;

import custom_components.ClickMode;
import custom_components.VectorScene;
import init.Controller;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import misc.Others;
import java.io.Serializable;

public class MyLine extends Line implements IShape{
    private VectorScene scene;
    private int layer;

    public MyLine(VectorScene sc, int l, double ox, double oy, Color stroke, int strokeWidth){
        scene = sc;
        layer = l;
        setStartX(ox);
        setStartY(oy);
        setEndX(ox);
        setEndY(oy);
        setStroke(stroke);
        setStrokeWidth(strokeWidth);
        System.out.println(scene.getHeight());
    }

    public MyLine(){}

    @Override
    public void adjust(double mx, double my) {
        setEndX(mx);
        setEndY(my);
    }

    @Override
    public void move(double ox, double oy){
        double diffX = getStartX() - getEndX();
        double diffY = getStartY() - getEndY();
        setStartX(ox);
        setStartY(oy);
        setEndX(ox - diffX);
        setEndY(oy - diffY);
    }

    // This bad boiii converts line to .svg element, spaghetti code
    @Override
    public String toSvg(){
        // Convert String returned by java 0xRRGGBBOO into #RRGGBB, which is needed in svg
        String stroke = Others.getHtmlColor((Color) getStroke());
        return "<line x1=\"" + (int) getStartX() + "\" y1=\"" + (int) getStartY() + "\" x2=\"" + (int) getEndX() +
                "\" y2=\"" + (int) getEndY() + "\" fill-opacity=\"" + (int) getOpacity() + "\" stroke-width=\"" +
                (int) getStrokeWidth() + "\" stroke=\"" + stroke + "\"/>\n";
    }

    public String toJvgf(){
        String stroke = Others.getHtmlColor((Color) getStroke());
        return "LINE begin_x " + getStartX() + " begin_y " + getStartY() + " end_x " + getEndX() + " end_y " + getEndY() +
                " fill_opacity " + getOpacity() + " stroke_width " + getStrokeWidth() + " stroke_color " + stroke + " layer " + layer + "\n";
    }

    // Getters
    public int getLayer(){
        return layer;
    }
    public void setLayer(int l){
        layer = l;
    }
    
    public double getCenterX(){
        double diff = Math.abs(getStartX() - getEndX()) / 2;
        return (getStartX() > getEndX()) ? getEndX() + diff : getStartX() + diff;
    }
    
    public double getCenterY(){
        double diff = Math.abs(getStartY() - getEndY()) / 2 ;
        return (getStartY() > getEndY()) ? getEndY() + diff : getStartY() + diff;
    }

    public double getAdjustX(){
        return getEndX();
    }

    public double getAdjustY(){
        return getEndY();
    }
}
