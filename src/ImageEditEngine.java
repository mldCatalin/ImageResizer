import imagescaling.ResampleFilters;
import imagescaling.ResampleOp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class ImageEditEngine {
    private final static int WIDTH = 800;
    private final static int HEIGHT = 800;
    private Image img;

    Image editImage(File file) {
        openImage(file);
        resize();
        fill();
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

    private void openImage(File file) {
        try {
            this.img = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
            this.img = new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
        }
    }

    private void resize() {
        int original_width = ((BufferedImage) this.img).getWidth();
        int original_height = ((BufferedImage) this.img).getHeight();
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (new_width > WIDTH) {
            //scale width to fit
            new_width = WIDTH;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > HEIGHT) {
            //scale height to fit instead
            new_height = HEIGHT;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }
        ResampleOp resizeOp = new ResampleOp(new_width, new_height);
        resizeOp.setFilter(ResampleFilters.getLanczos3Filter());
        this.img = resizeOp.filter(toBufferedImage(), null);
    }

    private BufferedImage toBufferedImage() {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bImage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        // Return the buffered image
        return bImage;
    }

    private void fill() {
        int original_width = this.img.getWidth(null);
        int original_height = this.img.getHeight(null);
        if (original_height == original_width)
            return;
        var max = original_height > original_width ? original_height : original_width;
        BufferedImage bufferedImage = new BufferedImage(max, max, BufferedImage.TYPE_3BYTE_BGR);
        bufferedImage.createGraphics();
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, max, max);
        g.drawImage(this.img, (max - original_width) / 2, (max - original_height) / 2, original_width, original_height, Color.BLACK, null);
        this.img = bufferedImage;
    }
}
