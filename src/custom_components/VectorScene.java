package custom_components;

import custom_shapes.IShape;
import custom_shapes.MyCircle;
import custom_shapes.MyLine;
import custom_shapes.MyRectangle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.util.ArrayList;

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

    /**
     *  <p>Initializes custom vector scene component. Adds click and drag listeners, these are used for drawing shapes.</p>
     */
    public VectorScene(){
        // Make dynamically added shapes NOT overflow
        setClip(new Rectangle(1080, 720));

        action = ClickMode.INTERACT;
        fillColor = Color.color(0, 0, 0, 0);
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

                } else if(action == ClickMode.MOVE){
                    if(m.getButton() == MouseButton.PRIMARY && !isMoving){
                        if(m.getPickResult().getIntersectedNode().getClass().getPackage().toString().equals(checkMovable)){
                            currentShape = (IShape) m.getPickResult().getIntersectedNode();
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
                } else{
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

                } else if(action == ClickMode.MOVE && isMoving){
                    currentShape.move(x, y);
                } else if(action == ClickMode.ADJUST && isAdjusting){
                    currentShape.adjust(x, y);
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
        currentShape = null;
    }

    public void readCircle(String params[]){
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
        currentShape = null;
    }
}

