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

        // Placeholder -> When user clicks, something happens
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(scene.getClickMode() == ClickMode.INTERACT){
                    setStrokeWidth(getStrokeWidth() + 1);
                }
            }
        });
    }

    public MyLine(){}

    @Override
    public void adjust(double mx, double my) {
        setEndX(mx);
        setEndY(my);
    }

    @Override
    public void move(double ox, double oy){
        /*
        double diffX = getStartX() - getEndX();
        double diffY = getStartY() - getEndY();
        setStartX(ox);
        setStartY(oy);
        setEndX(ox - diffX);
        setEndY(oy - diffY);
        */
        System.out.println("WORK IN PROGRESS");
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
}
