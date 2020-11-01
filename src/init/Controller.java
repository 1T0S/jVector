package init;

import custom_components.VectorScene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller {
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
    private Button buttonShowContent;

    /**
     * Test only -> Will be removed
     */
    @FXML
    public void showContent(){
        scene.showContent();
    }
}