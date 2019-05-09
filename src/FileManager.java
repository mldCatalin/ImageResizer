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
    private List<File> imagesList = new ArrayList<>();
    private ImageGUI gui = new ImageGUI();

    FileManager() {
        filesLocation = System.getProperty("user.dir");
        validator = new ImageValidator();
    }

    void processAllImages() {
        var fileManager = new FileManager();
        getLocalImages();
        for (File file : imagesList) {
            var processedImage = new ImageEditEngine(file);//TODO: processedImage e o imagine procesata, sau un ImageEditEngine? I'm confused
            String imageName = "";
            try {
                imageName = fileManager.save((RenderedImage) processedImage.getImg());
            } catch (IOException e) {
                e.printStackTrace();
            }
            gui.displayImage(processedImage.getImg(), imageName);
            gui.setVisible(true);
        }
    }

    private void getLocalImages() {//TODO: you're not actually getting anyting, cause void. maybe loadLocalImages
        Iterator it = FileUtils.iterateFiles(new File(filesLocation), null, false);
        while (it.hasNext()) {
            File file = (File) it.next();
            var fileName = file.getName();
            if (validator.validate(fileName))
                imagesList.add(file);
        }
    }

    private String save(RenderedImage img) throws IOException {
        String path = filesLocation + "\\output\\" + outputCounter++ + ".jpg";
        File outputFile = new File(path);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }
        ImageIO.write(img, "jpg", outputFile);
        return path;
    }
}
