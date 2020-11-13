package custom_components;

import custom_shapes.IShape;
import custom_shapes.MyCircle;
import custom_shapes.MyLine;
import custom_shapes.MyRectangle;
import init.Controller;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.io.Serializable;
import java.util.ArrayList;

public class VectorScene extends Pane{
    // Holds information about action that happens after left clicking the vector scene
    private ClickMode action;
    // Holds all shapes that are currently present in vector scene
    private ArrayList<IShape> content = new ArrayList<>();
    // Colors of object fill and object stroke
    private Color fillColor;
    private Color strokeColor;
    private int strokeWidth;
    // Checks whether user is drawing -> prevents duplicates
    private boolean isDrawing = false;
    // Currently picked shape -> determines what shapes are meant to be edited
    private IShape currentShape;
    //  Current layer where shapes will be placed
    private int layer;


    /**
     *  <p>Initializes custom vector scene component. Adds click and drag listeners, these are used for drawing shapes.</p>
     */
    public VectorScene(){
        action = ClickMode.INTERACT;
        fillColor = Color.color(0, 0, 0, 0);
        strokeColor = Color.BLACK;
        strokeWidth = 1;

        // When user clicks the scene with left button and user is not drawing, a shape is added to both scene and its content.
        setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent m) {
                if(m.getButton() == MouseButton.PRIMARY && !isDrawing && action != ClickMode.INTERACT) {
                    System.out.println("BEGIN DRAW");
                    isDrawing = true;
                    switch (action) {
                        case LINE:
                            newShape(new MyLine(VectorScene.this, layer, m.getX(), m.getY(), strokeColor, strokeWidth));
                            break;
                        case CIRCLE:
                            newShape(new MyCircle(VectorScene.this, layer, m.getX(), m.getY(), 0, strokeColor, fillColor, strokeWidth));
                            break;
                        case RECTANGLE:
                            newShape(new MyRectangle(VectorScene.this, layer, m.getX(), m.getY(), 0, 0, strokeColor, fillColor, strokeWidth));
                            break;
                    }
                } else if(m.getButton() == MouseButton.PRIMARY && isDrawing){
                    System.out.println("END DRAW");
                    isDrawing = false;
                }
                if(m.getButton() == MouseButton.SECONDARY && isDrawing){
                    System.out.println("ABORT DRAW");
                    removeUnfinishedShape();
                }
            }
        });


        // When user moves mouse, coords are updated. If user is currently drawing, shape preview changes
        setOnMouseMoved(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent m) {
                if(isDrawing){
                    switch(action){
                        case INTERACT:
                            break;
                        case LINE:
                        case CIRCLE:
                        case RECTANGLE:
                            currentShape.adjust(m.getX(), m.getY());
                            break;
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
        content.add(shape);
        getChildren().add((Shape) shape);
        currentShape = shape;
    }

    /**
     * <p>If shapes from content are not present on canvas, render them</p>
     */
    private void renderContent(){
        for(IShape shape : content){
            if(!getChildren().contains(shape)){
                getChildren().add((Shape) shape);
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
        if(isDrawing) {
            removeUnfinishedShape();
        }
    }

    /**
     * <p>
     *      Remove shape user is currently drawing from both canvas and content.
     *      Set control vars to init state.
     * </p>
     */
    private void removeUnfinishedShape(){
        isDrawing = false;
        content.remove(currentShape);
        getChildren().remove(currentShape);
        currentShape = null;
    }

    /**
     * Test only -> Will be removed
     */
    public void showContent(){
        for(int i = 0; i < content.size(); i++){
            System.out.println("[" + i + "]\t->\t" + content.get(i));
        }
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

    public ArrayList<IShape> getContent(){
        return content;
    }

    public void setStrokeWidth(int sw){
        strokeWidth = sw;
    }
}

