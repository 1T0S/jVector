package custom_shapes;

import custom_components.ClickMode;
import custom_components.VectorScene;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import misc.Others;

import java.io.Serializable;

public class MyRectangle extends Rectangle implements IShape{
    private VectorScene scene;
    private int layer;
    // These are needed because rectangles can only be draw in one direction (for some reason), so middle point needs to be adjusted
    private double originX, originY;

    public MyRectangle(VectorScene sc, int l, double ox, double oy, double width, double height, Color stroke, Color fill, int strokeWidth){
        scene = sc;
        layer = l;
        setX(ox);
        setY(oy);
        originX = ox;
        originY = oy;
        setHeight(height);
        setWidth(width);
        setFill(fill);
        setStroke(stroke);
        setStrokeWidth(strokeWidth);

        // Placeholder -> When user clicks, something happens
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(scene.getClickMode() == ClickMode.INTERACT){
                    setRotate(getRotate() + 20);
                }
            }
        });
    }


    @Override
    public void adjust(double mx, double my){
        if(mx < originX){
            setX(originX + (mx - originX));
            setWidth(Math.abs(getX() - originX));
        } else{
            setX(originX);
            setWidth(Math.abs(mx - originX));
        }
        if(my < originY){
            setY(originY + (my - originY));
            setHeight(Math.abs(getY() - originY));
        } else{
            setY(originY);
            setHeight(Math.abs(my - originY));
        }
        System.out.println("X: " + getX() + "\tY: " + getY() + "\nWidth: " + getWidth() + "\tHeight: " + getHeight() + "\n");
    }

    @Override
    public void move(double ox, double oy){

    }

    @Override
    public String toSvg(){
        String stroke = Others.getHtmlColor((Color) getStroke());
        String fill = Others.getHtmlColor((Color) getFill());
        return "<rect x=\"" + getX() + "\" y=\"" + getY() + "\" width=\"" + (int) getWidth() +
                "\" height=\"" + getHeight() + "\" fill-opacity=\"" + (int) getOpacity() + "\" stroke-width=\"" +
                (int) getStrokeWidth() + "\" stroke=\"" + stroke + "\" fill=\"" + fill + "\" />\n";
    }
}
