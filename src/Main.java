import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        ImageGUI gui = new ImageGUI();

        var fileManager = new FileManager();
        var files = fileManager.getLocalImages();
        Iterator iterator = files.iterator();
        while (iterator.hasNext()) {
            var file = (File) iterator.next();
            //TODO: uitandu-ma mai jos vad ca fac un ImageEditEngine(file) si apoi din editEngine.getImg() direct imi scoate rezultatul. 
            //This is kind of confusing. Ce s-a intamplat si unde de e gata deja imaginea? Simt ca nu a fost transparent fata de mine si this makes me uneasy.
            var editEngine = new ImageEditEngine();
            Image processedImage = editEngine.processImage(file);
            String imageName = "";
            try {
                imageName = fileManager.save((RenderedImage) processedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gui.displayImage(processedImage, imageName);
            gui.setVisible(true);
        }
        System.exit(0);
    }
}
