package custom_shapes;

import custom_components.VectorScene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class MyLine extends Line implements IShape {

    public MyLine(double ox, double oy, Color stroke){
        setStartX(ox);
        setStartY(oy);
        setEndX(ox);
        setEndY(oy);
        setStroke(stroke);
    }

}
