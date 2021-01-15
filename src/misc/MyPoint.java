package misc;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MyPoint extends Circle {
    private static Color fill = Color.RED;
    private static boolean visibility = true;

    public MyPoint(double xCoord, double yCoord){
        setCenterX(xCoord);
        setCenterY(yCoord);
        setRadius(5);
        setFill(fill);
        setVisible(visibility);
        setStrokeWidth(0);
    }

    public static void setDefaultVisibility(boolean v){
        visibility = v;
    }

    public static void setDefaultFill(Color c){
        fill = c;
    }


}
