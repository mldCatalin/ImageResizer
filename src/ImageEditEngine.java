import imagescaling.ResampleFilters;
import imagescaling.ResampleOp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

/**
 * @author Catalin Moldovan
 */

public class ImageEditEngine {

    public ImageEditEngine() {
    }

    public static Image openImage(URL imgUrl) {
        try {
            return ImageIO.read(imgUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
        }
    }

    public static Image openImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
            return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
        }
    }

    public Image resize(int boundWidth, int boundHeight, Image img) {
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
//        while (resizeFactor < 0.5) {
//            img = img.getScaledInstance(original_width / 2, original_height / 2, Image.SCALE_AREA_AVERAGING);
//            original_height = original_height / 2;
//            original_width = original_width / 2;
//            resizeFactor = new_height / original_height;
//        }
        ResampleOp resizeOp = new ResampleOp(new_width, new_height);
        ;
        resizeOp.setFilter(ResampleFilters.getLanczos3Filter());
        BufferedImage scaledImage = resizeOp.filter(toBufferedImage(img), null);
        return scaledImage;
        //return img.getScaledInstance(new_width, new_height, Image.SCALE_AREA_AVERAGING);
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
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

    public Image Fill(Image originalImg) {
        int original_width = originalImg.getWidth(null);
        int original_height = originalImg.getHeight(null);
        if (original_height == original_width)
            return originalImg;
        var max = original_height > original_width ? original_height : original_width;
        BufferedImage img = new BufferedImage(max, max, BufferedImage.TYPE_3BYTE_BGR);
        img.createGraphics();
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, max, max);
        g.drawImage(originalImg, (max - original_width) / 2, (max - original_height) / 2, original_width, original_height, Color.BLACK, null);
        return img;
    }


}



