package th;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class ConvOp {
    public static ConvolveOp getGaussianBlurFilter(int radius,
                                                   boolean horizontal) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }

        int size = radius * 2 + 1;
        float[] data = new float[size];

        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            int index = i + radius;
            data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += data[index];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        Kernel kernel = null;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }

    public static void main(String[] args) {

        BufferedImage img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLACK);
        g.setBackground(Color.BLACK);
        g.fillRect(0, 0, 500, 500);
        g.setColor(Color.WHITE);
        g.setBackground(Color.WHITE);
        g.fillRect(250, 250, 20, 40);
        img = getGaussianBlurFilter(8, false).filter(img, null);
        img = getGaussianBlurFilter(8, true).filter(img, null);

        JFrame frame = new JFrame("Convolution");
        frame.setLayout(new BorderLayout(0, 0));
        frame.setSize(500, 500);
        frame.add(new JLabel(new ImageIcon(img)));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.paint(g);
    }
}
