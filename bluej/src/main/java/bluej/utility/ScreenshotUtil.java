package bluej.utility;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenshotUtil
{
    protected static BufferedImage grabImage(Component component)
    {
        if (component.getWidth() == 0 || component.getHeight() == 0)
            return null;
        
        BufferedImage image = new BufferedImage(
                component.getWidth(),
                component.getHeight(),
                BufferedImage.TYPE_INT_ARGB
                );
        component.paint( image.getGraphics() );
        return image;
    }
    
    public static void writeImage(File stem, BufferedImage image) throws IOException
    {
        ImageIO.write(image, "png", new File(stem.getAbsolutePath() + ".png"));
    }
    
    /**
     * Takes the screenshot and saves it -- then returns the saved image as a convenience.
     */
    public static BufferedImage screenshotWindow(Component component, File stem) throws IOException
    {
        
        //component.paintAll( image.getGraphics() );
        //component.printAll( image.getGraphics() );
        
        BufferedImage img = grabImage(component);
        writeImage(stem, img);
        return img;
    }

    public static String munge(final String name)
    {
        return name.toLowerCase()
        .replace(" ", "-")
        .replace(".","")
        .replace("(","")
        .replace(")","")
        .replace("/","-");
    }
}
