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

    FileManager() {
        filesLocation = System.getProperty("user.dir");
        validator = new ImageValidator();
    }

    List<File> getLocalImages() {
        Iterator it = FileUtils.iterateFiles(new File(filesLocation), null, false);
        List<File> result = new ArrayList<>();
        while (it.hasNext()) {
            File file = (File) it.next();
            var fileName = file.getName();
            if (validator.validate(fileName))
                result.add(file);
        }
        return result;
    }

    String save(RenderedImage img) throws IOException {
        var path = filesLocation + "\\output\\" + outputCounter++ + ".jpg";
        File outputFile = new File(path);
        if (!outputFile.exists()){
            outputFile.mkdirs();
            }
        ImageIO.write(img, "jpg", outputFile);
        return path;
    }
}