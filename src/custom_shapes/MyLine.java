package custom_shapes;

import custom_components.ClickMode;
import custom_components.VectorScene;
import init.Controller;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class MyLine extends Line implements IShape {
    private VectorScene scene;
    private int layer;

    public MyLine(VectorScene sc, int l, double ox, double oy, Color stroke){
        scene = sc;
        layer = l;
        setStartX(ox);
        setStartY(oy);
        setEndX(ox);
        setEndY(oy);
        setStroke(stroke);
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

    @Override
    public void adjust(double mx, double my) {
        setEndX(mx);
        setEndY(my);
    }

    @Override
    public void move(double ox, double oy){

    }


}
