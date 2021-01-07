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
        Stage stageFileSave = new Stage();
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
     * @throws IOException
     */
    public static void getSvg(VectorScene scene) throws IOException {
        FileWriter fw = new FileWriter(showExplorer());
        // Init svg file
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        fw.write("<svg\nversion=\"1.1\"\nheight=\"" + (int) scene.getHeight() + "\"\nwidth=\"" + (int) scene.getWidth() + "\"\n" + ">\n");
        // Iterate throught objects on canvas, save them
        for(ArrayList<IShape> arr : scene.getContent()){
            for(IShape shape : arr){
                System.out.println(shape.toSvg());
                fw.write(shape.toSvg());
            }
        }
        // End svg file
        fw.write("</svg>");
        fw.close();
    }

    /**
     * <p>Saves all shapes that are currently on canvas to .jvgf format</p>
     * @param scene VectorScene whose content is meant to be saved
     * @throws IOException
     */
    public static void getJvgf(VectorScene scene) throws IOException{
        FileWriter fw = new FileWriter(showExplorer());
        fw.write("LAYERS " + scene.getLayer() + "\n");
        for(ArrayList<IShape> arr : scene.getContent()){
            for(IShape shape : arr){
                System.out.println(shape.toJvgf());
                fw.write(shape.toJvgf());
            }
        }
        fw.close();
    }

    /**
     *
     * @return Number of scene layers
     * @throws IOException
     */
    public static int readJvgf(VectorScene scene) throws IOException {
        File f = showExplorer();
        if(f.length() == 0){
            return 7;
        } else{
            // Init
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            // Get layer info
            String layerInfo = br.readLine();
            int layer = Integer.parseInt(layerInfo.split(" ")[1]);
            // Start reading shapes
            String line = "";
            while((line = br.readLine()) != null){
                String[] params = line.split(" ");
                switch(params[0]){
                    case "LINE":
                        scene.readLine(params);
                        break;
                    case "RECTANGLE":
                        scene.readRectangle(params);
                        break;
                    case "CIRCLE":
                        scene.readCircle(params);
                        break;
                    case "ELLIPSE":
                        scene.readEllipse(params);
                        break;
                }
            }
            return layer;
        }
    }
}
