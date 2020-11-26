package custom_shapes;

import custom_components.ClickMode;
import custom_components.VectorScene;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import misc.Others;

import java.io.Serializable;

public class MyCircle extends Circle implements IShape{
    private VectorScene scene;
    private int layer;

    public MyCircle(VectorScene sc, int l, double ox, double oy, double r, Color stroke, Color fill, int strokeWidth){
        scene = sc;
        layer = l;
        setCenterX(ox);
        setCenterY(oy);
        setRadius(r);
        setStroke(stroke);
        setFill(fill);
        setStrokeWidth(strokeWidth);

        // Placeholder -> When user clicks, something happens
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(scene.getClickMode() == ClickMode.INTERACT){
                    setRadius(getRadius() - 10);
                }
            }
        });
    }

    public MyCircle(){}

    /**
     * <p>Sets radius of circle by alculating Pythagorean theorem</p>
     * @param mx Mouse coordinate x
     * @param my Mouse coordinate y
     */
    @Override
    public void adjust(double mx, double my){
        double radius = Math.sqrt(Math.pow(getCenterX() - mx, 2) + Math.pow(getCenterY() - my, 2));
        setRadius(radius);
    }

    @Override
    public void move(double ox, double oy){
        setCenterX(ox);
        setCenterY(oy);
    }

    @Override
    public String toSvg(){
        String stroke = Others.getHtmlColor((Color) getStroke());
        String fill = Others.getHtmlColor((Color) getFill());
        return "<circle cx=\"" + (int) getCenterX() +"\" cy=\"" + (int) getCenterY() + "\" r=\"" + (int) getRadius() +
                "\" fill-opacity=\"" + (int) getOpacity() + "\" stroke-width=\"" +
                (int) getStrokeWidth() + "\" stroke=\"" + stroke + "\" fill=\"" + fill + "\" />\n";
    }

    public String toJvgf(){
        String stroke = Others.getHtmlColor((Color) getStroke());
        String fill = Others.getHtmlColor((Color) getFill());
        return "CIRCLE center_x " + getCenterX() + " center_y " + getCenterY() + " radius " + getRadius() + " fill_opacity " +
                getOpacity() + " stroke_width " + getStrokeWidth() + " stroke_color " + stroke + " fill " + fill + " layer " + layer + "\n";
    }

    // Getters
    public int getLayer(){
        return layer;
    }
    public void setLayer(int l){
        layer = l;
    }
}
