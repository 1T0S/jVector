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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class VectorScene extends Pane {
    // Holds information about action that happens after left clicking the vector scene
    private ClickMode action;
    // Holds all shapes that are currently present in vector scene
    public ArrayList<IShape> content = new ArrayList<>();
    // Colors of object fill and object stroke
    private Color fillColor;
    private Color strokeColor;
    // Checks whether user is drawing -> prevents duplicates
    private boolean isDrawing = false;
    // Currently picked shape -> determines what shapes are meant to be edited
    private IShape currentShape;


    /**
     *  <p>Initializes custom vector scene component. Adds click and drag listeners, these are used for drawing shapes.</p>
     */
    public VectorScene(){
        action = ClickMode.LINE;
        fillColor = Color.BLUE;
        strokeColor = Color.GREEN;

        // When user clicks the scene with left button and user is not drawing, a shape is added to both scene and its content.
        // When user clicks the scene with right button and user is drawing, the draw mode ends.
        setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent m) {
                if(m.getButton() == MouseButton.PRIMARY && !isDrawing){
                    isDrawing = true;
                    switch(action){
                        case INTERACT:
                            break;
                        case LINE:
                            newShape(new MyLine(m.getX(), m.getY(), strokeColor));
                            break;
                    }
                }
                if(m.getButton() == MouseButton.SECONDARY && isDrawing){
                    isDrawing = false;
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
                            ((MyLine) currentShape).setEndX(m.getX());
                            ((MyLine) currentShape).setEndY(m.getY());
                            break;
                    }
                }
            }
        });
    }

    /**
     * <p>Changes action state</p>
     * @param act Shape that will be drawn after user left clicks the scene
     */
    public void changeClickMode(ClickMode act){
        action = act;
    }

    /**
     * <p>Adds shape to scene, content list and currentShape</p>
     * @see #VectorScene
     */
    public void newShape(IShape shape){
        content.add(shape);
        getChildren().add((Shape) shape);
        currentShape = shape;
    }

    /**
     * Test only -> Will be removed
     */
    public void showContent(){
        for(int i = 0; i < content.size(); i++){
            System.out.println("[" + i + "]\t->\t" + content.get(i));
        }
    }
}

