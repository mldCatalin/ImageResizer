import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Main {
    private final static int WIDTH = 800;
    private final static int HEIGHT = 800;

    public static void main(String[] args) {
        var editEngine = new ImageEditEngine();
        ImageGUI gui = new ImageGUI();

        var fm = new FileManager();
        var files = fm.getLocalImages();
        Iterator iterator = files.iterator();
        while (iterator.hasNext()) {
            var file = (File) iterator.next();
            Image img = ImageEditEngine.openImage(file);
            img = editEngine.resize(WIDTH, HEIGHT, img);
            img = editEngine.Fill(img);
            String imageName = "";
            try {
                imageName = fm.save((RenderedImage) img);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gui.displayImage(img, imageName);
            gui.setVisible(true);
        }
        gui.setVisible(false);
    }
}
