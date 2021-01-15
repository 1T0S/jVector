package custom_shapes;

import custom_components.VectorScene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import misc.Others;

import java.util.ArrayList;


public class MyPolygon extends Polygon implements IShape {
    private int layer;
    private VectorScene scene;
    private ArrayList<Double> coordinations;

    public MyPolygon(VectorScene sc, ArrayList<Double> coords, Color stroke, Color fill, int strokeWidth, int l){
        scene = sc;
        coordinations = coords;
        setFill(fill);
        setStroke(stroke);
        setStrokeWidth(strokeWidth);
        getPoints().addAll(coords);
        layer = l;
    }

    public MyPolygon(){}

    @Override
    public void move(double ox, double oy) {
        System.out.println("NOPE");
    }

    @Override
    public void adjust(double mx, double my) {
        System.out.println("NOPE!");
    }

    @Override
    public String toSvg(){
        String stroke = Others.getHtmlColor((Color) getStroke());
        String fill = Others.getHtmlColor((Color) getFill());
        StringBuilder pointsString = new StringBuilder();
        for(int i = 0; i < getPoints().size() - 1; i += 2){
            pointsString.append(getPoints().get(i)).append(" ").append(getPoints().get(i + 1)).append(",");
        }
        return "<polygon points=\" " + pointsString + "\" fill-opacity=\"" + getOpacity() + "\" stroke-width=\"" +
                (int) getStrokeWidth() + "\" stroke=\"" + stroke + "\" fill=\"" + fill +
                "\" transform=\"rotate(" + getRotate() + "," + getCenterX() + "," + getCenterY() + ")\" />\n";
    }

    @Override
    public String toJvgf(){
        String stroke = Others.getHtmlColor((Color) getStroke());
        String fill = Others.getHtmlColor((Color) getFill());
        String points = "";
        for(double point : getPoints()){
            points += point + ",";
        }
        return "POLYGON points " + points + " fill_opacity " + getOpacity() + " stroke_width " +
                getStrokeWidth() + " stroke_color " + stroke + " fill " + fill + " layer " + layer + " rotate " + getRotate() + "\n";
    }

    public MyPolygon clone(){
        System.out.println("ADDED");
        return new MyPolygon(scene, coordinations, (Color) getStroke(), (Color) getFill(), (int) getStrokeWidth(), layer);
    }

    @Override
    public double getCenterX() {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        for(int i = 0; i < getPoints().size() - 1; i += 2){
            double tmp = getPoints().get(i);
            if(tmp > maxX){
                maxX = tmp;
            }
            if(tmp < minX){
                minX = tmp;
            }
        }
        return (minX + maxX) / 2;
    }

    @Override
    public double getCenterY(){
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for(int i = 1; i < getPoints().size() - 1; i += 2){
            double tmp = getPoints().get(i);
            if(tmp > maxY){
                maxY = tmp;
            }
            if(tmp < minY){
                minY = tmp;
            }
        }
        return (minY + maxY) / 2;
    }

    @Override
    public double getStartX() {
        return getPoints().get(0);
    }

    @Override
    public double getStartY() {
        return getPoints().get(1);
    }

    @Override
    public void setStartX(double x) {
        System.out.println("NOPE");
    }

    @Override
    public void setStartY(double y) {
        System.out.println("NOPE");
    }

    @Override
    public double getAdjustX() {
        return 0;
    }

    @Override
    public double getAdjustY() {
        return 0;
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
