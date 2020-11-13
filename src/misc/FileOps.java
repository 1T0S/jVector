package misc;

import custom_components.VectorScene;
import custom_shapes.IShape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class FileOps {
    /**
     * Opens Windows explorer and lets user pick a file
     * @return Return file user has picked. If user picked no file, create new file error.txt and return it.
     */
    public static File showExplorer(){
        Stage stageFileSave =new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file:");
        File picked = fileChooser.showSaveDialog(stageFileSave);
        if(picked == null){
            return new File("error.txt");
        } else{
            return picked;
        }
    }

    /**
     * <p>Saves all shapes that are currently present on VectorScene into .svg</p>
     * @param scene VectorScene whose content is meant to be saved
     */
    public static void getSvg(VectorScene scene) throws IOException {
        FileWriter fw = new FileWriter(showExplorer());
        // Init svg file
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        fw.write("<svg\nversion=\"1.1\"\nheight=\"" + (int) scene.getHeight() + "\"\nwidth=\"" + (int) scene.getWidth() + "\"\n" + ">\n");
        // Iterate throught objects on canvas, save them
        for(IShape shape : scene.getContent()){
            System.out.println(shape.toSvg());
            fw.write(shape.toSvg());
        }
        // End svg file
        fw.write("</svg>");
        fw.close();
    }
}
