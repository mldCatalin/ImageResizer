import imagescaling.ResampleFilters;
import imagescaling.ResampleOp;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

//TODO: operatiunile trebuie sa se intample pe membru (img), nu sa tot pasam din stanga in dreapta.
public class ImageEditEngine {

    private final static int WIDTH = 800;
    private final static int HEIGHT = 800;
    private Image img;

//    Image getImg() {
//        return img;
//    }
//    ImageEditEngine() {
//    }
//    ImageEditEngine(File file){
//        openImage(file);
//        resize(WIDTH, HEIGHT, img);
//        fill(img);
//    }

    Image processImage(File file) {
        openImage(file);
        resize(WIDTH, HEIGHT, img);
        fill(img);
        return img;
    }

    public static Image openImage(URL imgUrl) {
        try {
            return ImageIO.read(imgUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
        }
    }

    void openImage(File file) {
        try {
            //A de exemplu aici. de ce intorc spre exterior? ar trebui sa incarc in img.
            this.img = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
            this.img = new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
        }
    }

    void resize(int boundWidth, int boundHeight, Image img) {
        int original_width = ((BufferedImage) img).getWidth();
        int original_height = ((BufferedImage) img).getHeight();
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (new_width > boundWidth) {
            //scale width to fit
            new_width = boundWidth;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > boundHeight) {
            //scale height to fit instead
            new_height = boundHeight;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }
        var resizeFactor = new_height / original_height;

        ResampleOp resizeOp = new ResampleOp(new_width, new_height);
        resizeOp.setFilter(ResampleFilters.getLanczos3Filter());
        //A: sau aici, de ce am acest scaledImage, in loc sa folosesc img?
        BufferedImage scaledImage = resizeOp.filter(toBufferedImage(img), null);
        this.img = scaledImage;
        //return img.getScaledInstance(new_width, new_height, Image.SCALE_AREA_AVERAGING);
    }

    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        // Return the buffered image
        return bimage;
    }

    void fill(Image originalImg) {
        int original_width = originalImg.getWidth(null);
        int original_height = originalImg.getHeight(null);
        if (original_height == original_width)
            this.img = originalImg;
        var max = original_height > original_width ? original_height : original_width;
        BufferedImage img = new BufferedImage(max, max, BufferedImage.TYPE_3BYTE_BGR);
        img.createGraphics();
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, max, max);
        g.drawImage(originalImg, (max - original_width) / 2, (max - original_height) / 2, original_width, original_height, Color.BLACK, null);
        this.img = img;//A: nimeni altcineva nu trebuie sa intoarca img in afara de getImg. altfel sunt 2 functii care fac acelasi lucru
    }
}
