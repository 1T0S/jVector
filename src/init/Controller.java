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
import javafx.scene.shape.Rectangle;
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
    private Button buttonActionInteract, buttonActionLine, buttonActionCircle, buttonActionRectangle, buttonActionMove,
            buttonAddLayer, buttonAdjust;
    @FXML
    private CheckBox checkboxSnappingStart, checkboxSnappingEnd;
    @FXML
    private Label labelAction;
    // https://docs.oracle.com/javafx/2/ui_controls/color-picker.htm
    @FXML
    private ColorPicker colorPickerFill, colorPickerStroke;
    @FXML
    private BorderPane b;
    @FXML
    private MenuItem menuItemSaveSvg, menuItemSaveJvgf;
    @FXML
    private Spinner spinnerStrokeWidth, spinnerCurrentLayer;

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
        SpinnerValueFactory<Integer> svfStrokeWidth = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 25, 1);
        spinnerStrokeWidth.setValueFactory(svfStrokeWidth);
        spinnerStrokeWidth.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
               scene.setStrokeWidth((Integer) spinnerStrokeWidth.getValue());
            }
        });

        // Layer spinner -> Determines on which layers are objects initialized
        SpinnerValueFactory<Integer> svfCurrentLayer = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 6, 0);
        spinnerCurrentLayer.setValueFactory(svfCurrentLayer);
        spinnerCurrentLayer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scene.setLayer((Integer) spinnerCurrentLayer.getValue());
            }
        });
    }

    @FXML
    public void switchActionInteract(){
        scene.switchMode();
        scene.setClickMode(ClickMode.INTERACT);
        labelAction.setText("Mode: Interact");
    }

    @FXML
    public void switchActionAdjust(){
        scene.switchMode();
        scene.setClickMode(ClickMode.ADJUST);
        labelAction.setText("Mode: Adjust");
    }

    @FXML
    public void switchActionLine(){
        scene.switchMode();
        scene.setClickMode(ClickMode.LINE);
        labelAction.setText("Mode: Line");
    }

    @FXML
    public void switchActionCircle(){
        scene.switchMode();
        scene.setClickMode(ClickMode.CIRCLE);
        labelAction.setText("Mode: Circle");
    }

    @FXML
    public void switchActionRectangle(){
        scene.switchMode();
        scene.setClickMode(ClickMode.RECTANGLE);
        labelAction.setText("Mode: Rectangle");
    }

    @FXML
    public void switchActionMove(){
        scene.switchMode();
        scene.setClickMode(ClickMode.MOVE);
        labelAction.setText("Mode: Move");
    }

    @FXML
    public void addLayer(){
        System.out.println("Layer added");
        scene.addLayer();
        SpinnerValueFactory.IntegerSpinnerValueFactory vf = (SpinnerValueFactory.IntegerSpinnerValueFactory) spinnerCurrentLayer.getValueFactory();
        vf.setMax(vf.getMax() + 1);
    }

    @FXML
    public void saveSvg() throws IOException {
        FileOps.getSvg(scene);
    }

    @FXML
    public void saveJvgf() throws IOException {
        FileOps.getJvgf(scene);
    }

    @FXML
    public void openJvgf() throws IOException{
        int layers = FileOps.readJvgf(scene);
        SpinnerValueFactory.IntegerSpinnerValueFactory vf = (SpinnerValueFactory.IntegerSpinnerValueFactory) spinnerCurrentLayer.getValueFactory();
        while(layers > vf.getMax()){
            addLayer();
        }
    }

    @FXML
    public void changeSnappingStart(){
        scene.changeSnappingStart(checkboxSnappingStart.isSelected());
    }

    @FXML
    public void changeSnappingEnd(){
        scene.changeSnappingEnd(checkboxSnappingEnd.isSelected());
    }

}