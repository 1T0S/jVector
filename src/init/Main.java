package init;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));
        root.getStylesheets().add("styles/styles.css");
        primaryStage.setTitle("Paint 5D");
        //primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1365, 810));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}