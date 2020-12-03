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

    public MyRectangle(){}


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
        originX = ox;
        originY = oy;
        adjust(ox + getWidth(), oy + getHeight());
        System.out.println("WORK IN PROGRESS");
    }

    @Override
    public String toSvg(){
        String stroke = Others.getHtmlColor((Color) getStroke());
        String fill = Others.getHtmlColor((Color) getFill());
        return "<rect x=\"" + getX() + "\" y=\"" + getY() + "\" width=\"" + (int) getWidth() +
                "\" height=\"" + getHeight() + "\" fill-opacity=\"" + (int) getOpacity() + "\" stroke-width=\"" +
                (int) getStrokeWidth() + "\" stroke=\"" + stroke + "\" fill=\"" + fill + "\" />\n";
    }

    public String toJvgf(){
        String stroke = Others.getHtmlColor((Color) getStroke());
        String fill = Others.getHtmlColor((Color) getFill());
        return "RECTANGLE origin_x " + getX() + " origin_y " + getY() + " width " + getWidth() + " height " + getHeight() +
                " fill_opacity " + getOpacity() + " stroke_width " + getStrokeWidth() + " stroke_color " + stroke + " fill " + fill + " layer " + layer + "\n";
    }

    // Getters
    public int getLayer(){
        return layer;
    }
    public void setLayer(int l){
        layer = l;
    }

    public double getCenterX(){
        return getX() + (getWidth() / 2);
    }

    public double getCenterY(){
        return getY() + (getHeight() / 2);
    }

    public double getStartX(){
        return getX();
    }

    public double getStartY(){
        return getY();
    }

    public double getAdjustX(){
        if(originX > getX()){
            return originX - getWidth();
        } else{
            return originX + getWidth();
        }
    }

    public double getAdjustY(){
        if(originY > getY()){
            return originY - getHeight();
        } else{
            return originY + getHeight();
        }
    }
}
