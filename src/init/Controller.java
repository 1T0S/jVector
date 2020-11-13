package init;

import custom_components.ClickMode;
import custom_components.VectorScene;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import misc.FileOps;

import java.io.IOException;
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
    private Button buttonActionInteract, buttonActionLine, buttonActionCircle, buttonActionRectangle, buttonActionMove;
    @FXML
    private Label labelAction;
    // https://docs.oracle.com/javafx/2/ui_controls/color-picker.htm
    @FXML
    private ColorPicker colorPickerFill, colorPickerStroke;
    @FXML
    BorderPane b;
    @FXML
    private MenuItem menuItemSaveSvg;
    @FXML
    private Spinner spinnerStrokeWidth;

    // On init
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
            When user switches color, listener calls scene's setColor and changes it
            Uses lambda expression instead of anonymous class -> Thanks, Intellij IDEA!
            https://docs.oracle.com/javafx/2/events/handlers.htm
         */
        //noinspection unchecked
        colorPickerFill.setOnAction((EventHandler) t -> scene.setFillColor(colorPickerFill.getValue()));
        //noinspection unchecked
        colorPickerStroke.setOnAction((EventHandler) t -> scene.setStrokeColor(colorPickerStroke.getValue()));
        colorPickerFill.getCustomColors().add(new Color(0, 0, 0, 0));
        colorPickerStroke.getCustomColors().add(new Color(0, 0, 0, 0));

        /*
            This spinner allows user to change width of stroke.
            First I had to create SpinnerValueFactory to set its min value, max value and init value.
            Uses event listener to change canvas stroke when user interacts with the spinner.
         */
        SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 25, 1);
        spinnerStrokeWidth.setValueFactory(svf);
        spinnerStrokeWidth.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
               scene.setStrokeWidth((Integer) spinnerStrokeWidth.getValue());
            }
        });
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
        labelAction.setText("Mode: Interact");
    }

    @FXML
    public void switchActionLine(){
        scene.setClickMode(ClickMode.LINE);
        scene.switchMode();
        labelAction.setText("Mode: Line");
    }

    @FXML
    public void switchActionCircle(){
        scene.setClickMode(ClickMode.CIRCLE);
        scene.switchMode();
        labelAction.setText("Mode: Circle");
    }

    @FXML
    public void switchActionRectangle(){
        scene.setClickMode(ClickMode.RECTANGLE);
        scene.switchMode();
        labelAction.setText("Mode: Rectangle");
    }

    @FXML
    public void switchActionMove(){
        scene.setClickMode(ClickMode.MOVE);
        scene.switchMode();
        labelAction.setText("Mode: Move");
    }

    @FXML
    public void saveSvg() throws IOException {
        FileOps.getSvg(scene);
    }

}