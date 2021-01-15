package custom_components;

import custom_shapes.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import misc.MyPoint;

import java.util.ArrayList;
import java.util.Vector;

public class VectorScene extends Pane {
    // Holds information about action that happens after left clicking the vector scene
    private ClickMode action;
    // Holds all layers that hold shapes
    private ArrayList<ArrayList<IShape>> content = new ArrayList<>();
    // Colors of object fill and object stroke
    private Color fillColor;
    private Color strokeColor;
    private int strokeWidth;
    // Checks whether an action is taking place -> Prevents duplicates and glitches
    private boolean isDrawing = false;
    private boolean isMoving = false;
    private boolean isAdjusting = false;
    private boolean snappingStart = false;
    private boolean snappingEnd = false;
    // Temporary store values of object properties
    private double xTmp, yTmp;
    // Currently picked shape -> determines what shapes are meant to be edited
    private IShape currentShape;
    //  Current layer where shapes will be placed
    private int layer;
    // Checks whether nodes can be moved or not
    private static final String checkMovable = "package custom_shapes";
    // InfoPanes are tied with VectorScenes -> Shapes can be easily adjusted
    private InfoPane infoPane;
    // Holds coordinates of points that are used to create polygon
    private ArrayList<Double> pointMatrix = new ArrayList<>();
    // Holds polygon marks
    private ArrayList<MyPoint> pointList = new ArrayList<>();

    /**
     *  <p>Initializes custom vector scene component. Adds click and drag listeners, these are used for drawing shapes.</p>
     */
    public VectorScene(){
        // Make dynamically added shapes NOT overflow
        setClip(new Rectangle(1080, 720));

        action = ClickMode.INTERACT;
        fillColor = Color.WHITE;
        strokeColor = Color.BLACK;
        strokeWidth = 1;
        xTmp = yTmp = 0;

        // Init 7 layers
        for(int i = 0; i < 7; i++){
            content.add(new ArrayList<IShape>());
        }

        // When user clicks the scene with left button and user is not drawing, a shape is added to both scene and its content.
        setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent m) {
                // Get coords
                double x = m.getX();
                double y = m.getY();
                if(snappingStart && m.getPickResult().getIntersectedNode().getClass().getPackage().toString().equals(checkMovable)){
                    IShape snapTo = (IShape) m.getPickResult().getIntersectedNode();
                    x = snapTo.getCenterX();
                    y = snapTo.getCenterY();
                }
                // Handle actions
                if(action == ClickMode.INTERACT){
                    if(m.getPickResult().getIntersectedNode().getClass().getPackage().toString().equals(checkMovable)){
                        currentShape = (IShape) m.getPickResult().getIntersectedNode();
                        infoPane.setShape(currentShape);
                    }
                } else if(action == ClickMode.POINT && m.getButton() == MouseButton.PRIMARY){
                    pointMatrix.add(m.getX());
                    pointMatrix.add(m.getY());
                    pointList.add(new MyPoint(m.getX(), m.getY()));
                    renderPolygonPoints();
                    infoPane.addCoords(m.getX(), m.getY());
                } else if(action == ClickMode.MOVE){
                    if(m.getButton() == MouseButton.PRIMARY && !isMoving){
                        if(m.getPickResult().getIntersectedNode().getClass().getPackage().toString().equals(checkMovable)){
                            currentShape = (IShape) m.getPickResult().getIntersectedNode();
                            infoPane.setShape(currentShape);
                            xTmp = currentShape.getStartX();
                            yTmp = currentShape.getStartY();
                            System.out.println("BEGIN MOVE");
                            isMoving = true;
                        }
                    } else if(m.getButton() == MouseButton.PRIMARY && isMoving){
                        System.out.println("END MOVE");
                        isMoving = false;
                    } else if(m.getButton() == MouseButton.SECONDARY && isMoving){
                        stopMovingShape();
                    }
                } else if(action == ClickMode.ADJUST){
                        if(m.getButton() == MouseButton.PRIMARY && !isAdjusting){
                            if(m.getPickResult().getIntersectedNode().getClass().getPackage().toString().equals(checkMovable)){
                                System.out.println("START ADJUSTING");
                                currentShape = (IShape) m.getPickResult().getIntersectedNode();
                                infoPane.setShape(currentShape);
                                xTmp = currentShape.getAdjustX();
                                yTmp = currentShape.getAdjustY();
                                isAdjusting = true;
                            }
                        } else if(m.getButton() == MouseButton.PRIMARY && isAdjusting){
                            System.out.println("END ADJUSTING");
                            isAdjusting = false;
                        } else if(m.getButton() == MouseButton.SECONDARY && isAdjusting){
                            stopAdjustingShape();
                        }
                } else if(action == ClickMode.DELETE){
                    if(m.getPickResult().getIntersectedNode().getClass().getPackage().toString().equals(checkMovable)){
                        IShape del = (IShape) m.getPickResult().getIntersectedNode();
                        removeShape(del);
                    }
                }  else if(action == ClickMode.DUPLICATE){
                    System.out.println("DUP");
                    if(m.getPickResult().getIntersectedNode().getClass().getPackage().toString().equals(checkMovable)){
                        IShape toDuplicate = (IShape) m.getPickResult().getIntersectedNode();
                        newShape(toDuplicate.clone());
                    }
                } else {
                    if(m.getButton() == MouseButton.PRIMARY && !isDrawing) {
                        System.out.println("BEGIN DRAW");
                        isDrawing = true;
                        switch (action) {
                            case LINE:
                                newShape(new MyLine(VectorScene.this, layer, x, y, strokeColor, strokeWidth));
                                break;
                            case CIRCLE:
                                newShape(new MyCircle(VectorScene.this, layer, x, y, 0, strokeColor, fillColor, strokeWidth));
                                break;
                            case RECTANGLE:
                                newShape(new MyRectangle(VectorScene.this, layer, x, y, 0, 0, strokeColor, fillColor, strokeWidth));
                                break;
                            case ELLIPSE:
                                newShape(new MyEllipse(VectorScene.this, layer, x, y, 0, 0, strokeColor, fillColor, strokeWidth));
                                break;
                        }
                    } else if(m.getButton() == MouseButton.PRIMARY && isDrawing){
                        System.out.println("END DRAW");
                        isDrawing = false;
                        currentShape = null;
                    }
                    if(m.getButton() == MouseButton.SECONDARY && isDrawing){
                        System.out.println("ABORT DRAW");
                        removeUnfinishedShape();
                    }
                }
            }
        });


        // When user moves mouse, coords are updated. If user is currently drawing, shape preview changes
        setOnMouseMoved(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent m) {
                // Get coords
                double x = m.getX();
                double y = m.getY();
                if(snappingEnd && m.getPickResult().getIntersectedNode().getClass().getPackage().toString().equals(checkMovable)){
                    IShape snapTo = (IShape) m.getPickResult().getIntersectedNode();
                    if(snapTo != currentShape){
                        x = snapTo.getCenterX();
                        y = snapTo.getCenterY();
                    }
                }
                // Check actions
                if(action == ClickMode.INTERACT){

                } else if(action == ClickMode.POINT || action == ClickMode.DUPLICATE){

                } else if(action == ClickMode.MOVE && isMoving){
                    currentShape.move(x, y);
                    infoPane.refresh();
                } else if(action == ClickMode.ADJUST && isAdjusting){
                    currentShape.adjust(x, y);
                    infoPane.refresh();
                }  else{
                    if(isDrawing){
                        currentShape.adjust(x, y);
                    }
                }
            }
        });
    }

    /**
     * <p>Adds shape to scene, content list and currentShape</p>
     * @see #VectorScene
     */
    private void newShape(IShape shape){
        currentShape = shape;
        content.get(layer).add(shape);
        renderContent();
    }

    /**
     * <p>If shapes from content are not present on canvas, render them</p>
     */
    private void renderContent(){
        getChildren().clear();
        for(ArrayList<IShape> arr : content){
            for(IShape shape : arr){
                if(!getChildren().contains(shape)){
                    getChildren().add((Shape) shape);
                }
            }
        }
    }

    /**
     * <p>Removes shape from VectorScene and content</p>
     *
     */
    private void removeShape(IShape shape){
        for(ArrayList<IShape> layer : content){
            layer.remove(shape);
        }
        System.out.println(getChildren().remove(shape));
    }

    /**
     * <p>Changes shape layer on canvas and in ArrayList</p>
     */
    public void changeShapeLayer(IShape shape, int oldLayer){
        // Check whether the shape really was removed -> Prevents duplicates
        if(content.get(oldLayer).remove(shape)){
            content.get(shape.getLayer()).add(shape);
            renderContent();
        }
    }

    /**
     * <p>
     *     If user switches drawMode without finishing drawing the shape, type error is prevented by calling this function.
     * </p>
     * @see #removeUnfinishedShape()
     */
    public void switchMode(){
        if(isDrawing)
            removeUnfinishedShape();
        if(isAdjusting)
            stopAdjustingShape();
        if(isMoving)
            stopMovingShape();
    }

    /**
     * <p>
     *      Remove shape user is currently drawing from both canvas and content.
     *      Set control vars to init state.
     * </p>
     */
    private void removeUnfinishedShape(){
        isDrawing = false;
        content.get(currentShape.getLayer()).remove(currentShape);
        getChildren().remove(currentShape);
        currentShape = null;
    }

    /**
     *     <p> When user right clicks while adjusting shape / clicks on UI item, adjust shape to its previous size </p>
     */
    private void stopAdjustingShape(){
        currentShape.adjust(xTmp, yTmp);
        currentShape = null;
        isAdjusting = false;
    }

    /**
     * <p> When user right clicks while moving shape / clicks on UI item, set shape position to its previous pos</p>
     */
    private void stopMovingShape(){
        currentShape.move(xTmp, yTmp);
        currentShape = null;
        isMoving = false;
    }


    public void addLayer(){
        content.add(new ArrayList<IShape>());
    }

    /**
     * <p>When user clicks on ClearMatrix button, clears all points in memory and remove text from info text area</p>
     */
    public void clearMatrix(){
        pointMatrix.clear();
        removePolygonPoints();
        infoPane.clearPointMatrix();
    }

    /**
     * <p>Removes last 2 coords (x, y) of polygon matrix</p>
     */
    public void clearLastMatrix(){
        if(pointMatrix.size() >= 4){
            pointMatrix.remove(pointMatrix.size() - 1);
            pointMatrix.remove(pointMatrix.size() - 1);
            MyPoint tmp = pointList.get(pointList.size() - 1);
            getChildren().remove(tmp);
            pointList.remove(tmp);
            infoPane.removePointMatrixLine();
        }
    }

    /**
     * <p>Creates polygon from polygonMatrix, clears polygonMatrix</p>
     */
    public void createPolygon(){
        newShape(new MyPolygon(VectorScene.this, pointMatrix, strokeColor, fillColor, strokeWidth, layer));
        clearMatrix();
        removePolygonPoints();
    }

    /**
     * <p>Renders all polygon points on scene, user won't be able to interact with these points or export them</p>
     */
    private void renderPolygonPoints(){
        for(MyPoint point : pointList){
            if(!getChildren().contains(point)){
                getChildren().add(point);
            }
        }
    }

    private void removePolygonPoints(){
        for(MyPoint point : pointList){
            getChildren().remove(point);
        }
        pointList.clear();
    }

    public void setPolygonPointsVisibility(boolean visibility){
        for(MyPoint point : pointList){
            point.setVisible(visibility);
        }
        MyPoint.setDefaultVisibility(visibility);
    }

    public void setPolygonPointsColor(Color c){
        for(MyPoint point: pointList){
            point.setFill(c);
        }
        MyPoint.setDefaultFill(c);
    }

    public void clearAll(){
        getChildren().removeAll();
        for(ArrayList<IShape> arr : content){
            arr.clear();
        }
        switchMode();
    }

    /*
    Getters and setters
    Nothing interesting here
     */
    public void setClickMode(ClickMode act){
        action = act;
    }

    public ClickMode getClickMode(){
        return action;
    }

    public void setFillColor(Color c){
        fillColor = c;
    }

    public void setStrokeColor(Color c){
        strokeColor = c;
    }

    public ArrayList<ArrayList<IShape>> getContent(){
        return content;
    }

    public void setStrokeWidth(int sw){
        strokeWidth = sw;
    }

    public void setLayer(int l){
        layer = l;
        System.out.println(layer);
    }

    public int getLayer(){
        return content.size();
    }

    public void changeSnappingStart(boolean value){
        snappingStart = value;
    }

    public void changeSnappingEnd(boolean value){
        snappingEnd = value;
    }

    public void setInfoPane(InfoPane ip){
        infoPane = ip;
    }


    /*
        IO  -> Garbage code, WIP.
     */
    public void readLine(String[] params){
        MyLine currentLine = new MyLine();
        newShape(currentLine);
        currentLine.setStartX(Double.parseDouble(params[2]));
        currentLine.setStartY(Double.parseDouble(params[4]));
        currentLine.setEndX(Double.parseDouble(params[6]));
        currentLine.setEndY(Double.parseDouble(params[8]));
        currentLine.setOpacity(Double.parseDouble(params[10]));
        currentLine.setStrokeWidth(Double.parseDouble(params[12]));
        currentLine.setStroke(Color.valueOf(params[14]));
        currentLine.setLayer(Integer.parseInt(params[16]));
        currentLine.setRotate(Double.parseDouble(params[18]));
        currentShape = null;
    }

    public void readRectangle(String[] params){
        MyRectangle currentRectangle = new MyRectangle();
        newShape(currentRectangle);
        currentRectangle.setX(Double.parseDouble(params[2]));
        currentRectangle.setY(Double.parseDouble(params[4]));
        currentRectangle.setWidth(Double.parseDouble(params[6]));
        currentRectangle.setHeight(Double.parseDouble(params[8]));
        currentRectangle.setOpacity(Double.parseDouble(params[10]));
        currentRectangle.setStrokeWidth(Double.parseDouble(params[12]));
        currentRectangle.setStroke(Color.valueOf(params[14]));
        currentRectangle.setFill(Color.valueOf(params[16]));
        currentRectangle.setLayer(Integer.parseInt(params[18]));
        currentRectangle.setRotate(Double.parseDouble(params[20]));
        currentShape = null;
    }

    public void readCircle(String[] params){
        MyCircle currentCircle = new MyCircle();
        newShape(currentCircle);
        currentCircle.setCenterX(Double.parseDouble(params[2]));
        currentCircle.setCenterY(Double.parseDouble(params[4]));
        currentCircle.setRadius(Double.parseDouble(params[6]));
        currentCircle.setOpacity(Double.parseDouble(params[8]));
        currentCircle.setStrokeWidth(Double.parseDouble(params[10]));
        currentCircle.setStroke(Color.valueOf(params[12]));
        currentCircle.setFill(Color.valueOf(params[14]));
        currentCircle.setLayer(Integer.parseInt(params[16]));
        currentCircle.setRotate(Double.parseDouble(params[18]));
        currentShape = null;
    }

    public void readEllipse(String[] params){
        MyEllipse currentEllipse = new MyEllipse();
        newShape(currentEllipse);
        currentEllipse.setCenterX(Double.parseDouble(params[2]));
        currentEllipse.setCenterY(Double.parseDouble(params[4]));
        currentEllipse.setRadiusX(Double.parseDouble(params[6]));
        currentEllipse.setRadiusY(Double.parseDouble(params[8]));
        currentEllipse.setOpacity(Double.parseDouble(params[10]));
        currentEllipse.setStrokeWidth(Double.parseDouble(params[12]));
        currentEllipse.setStroke(Color.valueOf(params[14]));
        currentEllipse.setFill(Color.valueOf(params[16]));
        currentEllipse.setLayer(Integer.parseInt(params[18]));
        currentEllipse.setRotate(Double.parseDouble(params[20]));
        currentShape = null;
    }

    public void readPolygon(String[] params){
        MyPolygon currentPolygon = new MyPolygon();
        newShape(currentPolygon);
        // Get points from my format - Ouch
        ArrayList<Double> points = new ArrayList<>();
        String pointsString = params[2];
        while(pointsString.length() != 0){
            System.out.println(pointsString.substring(0, pointsString.indexOf(',')));
            points.add(Double.parseDouble(pointsString.substring(0, pointsString.indexOf(','))));
            pointsString = pointsString.substring(pointsString.indexOf(',') + 1);
        }
        currentPolygon.getPoints().addAll(points);
        currentPolygon.setOpacity(Double.parseDouble(params[4]));
        currentPolygon.setStrokeWidth(Double.parseDouble(params[6]));
        currentPolygon.setStroke(Color.valueOf(params[8]));
        currentPolygon.setFill(Color.valueOf(params[10]));
        currentPolygon.setLayer(Integer.parseInt(params[12]));
        currentPolygon.setRotate(Double.parseDouble(params[14]));
    }
}

