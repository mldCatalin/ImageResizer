import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class FileManager {
    private String filesLocation;
    private ImageValidator validator;
    private int outputCounter;
    private List<File> imageFiles = new ArrayList<>();
    private ImageGUI gui = new ImageGUI();

    FileManager() {
        filesLocation = System.getProperty("user.dir");
        validator = new ImageValidator();
    }

    void processAllImages() {
        loadLocalImages();
        for (File file : imageFiles) {
            var editEngine = new ImageEditEngine();
            var processedImage = editEngine.editImage(file);
            String processedImageName = "";
            try {
                processedImageName = save((RenderedImage) processedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gui.displayImage(processedImage, processedImageName);
            gui.setVisible(true);
        }
    }

    private void loadLocalImages() {
        Iterator it = FileUtils.iterateFiles(new File(filesLocation), null, false);
        while (it.hasNext()) {
            File file = (File) it.next();
            var fileName = file.getName();
            if (validator.validate(fileName))
                imageFiles.add(file);
        }
    }

    private String save(RenderedImage img) throws IOException {
        String path = filesLocation + "\\output\\" + outputCounter++ + ".jpg";
        File outputFile = new File(path);
        outputFile.mkdirs();
        ImageIO.write(img, "jpg", outputFile);
        return path;
    }
}
