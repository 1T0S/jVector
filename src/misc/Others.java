package misc;

import javafx.scene.paint.Color;

public class Others {

    public static String getHtmlColor(Color c){
        return "#" + c.toString().substring(2, c.toString().length() - 2);
    }

    public static boolean isFloat(String s){
        return s.matches("[+-]?([0-9]*[.])?[0-9]+");
    }
}
