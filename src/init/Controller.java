package init;

import custom_components.ClickMode;
import custom_components.VectorScene;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private HBox hboxBottom, hboxTop;
    @FXML
    private VBox vboxLeft;
    @FXML
    private AnchorPane paneRight;
    @FXML
    private MenuBar menu;
    @FXML
    private VectorScene scene = new VectorScene();
    @FXML
    private Button buttonShowContent, buttonActionInteract, buttonActionLine, buttonActionCircle;
    // https://docs.oracle.com/javafx/2/ui_controls/color-picker.htm
    @FXML
    private ColorPicker colorPickerFill, colorPickerStroke;
    @FXML
    BorderPane b;

    // On init
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
            When user switches color, listener calls scene's setColor and changes it
            Uses lambda expression instead of anonymous class -> Thanks, Intellij IDEA!
         */
        //noinspection unchecked
        colorPickerFill.setOnAction((EventHandler) t -> scene.setFillColor(colorPickerFill.getValue()));
        //noinspection unchecked
        colorPickerStroke.setOnAction((EventHandler) t -> scene.setStrokeColor(colorPickerStroke.getValue()));
        colorPickerFill.getCustomColors().add(new Color(0, 0, 0, 0));
        colorPickerStroke.getCustomColors().add(new Color(0, 0, 0, 0));
    }

    /**
     * Test only -> Will be removed
     */
    @FXML
    public void showContent(){
        scene.showContent();
        for(Node n : b.getChildren()){
            System.out.println(n);
        }
    }

    @FXML
    public void switchActionInteract(){
        scene.setClickMode(ClickMode.INTERACT);
        scene.switchMode();
    }

    @FXML
    public void switchActionLine(){
        scene.setClickMode(ClickMode.LINE);
        scene.switchMode();
    }

    @FXML
    public void switchActionCircle(){
        scene.setClickMode(ClickMode.CIRCLE);
        scene.switchMode();
    }

    @FXML
    public void switchActionRectangle(){
        scene.setClickMode(ClickMode.RECTANGLE);
        scene.switchMode();
    }

    @FXML
    public void switchActionMove(){
        scene.setClickMode(ClickMode.MOVE);
        scene.switchMode();
    }

}