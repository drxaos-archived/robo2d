package robo2d.testbed.devices;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class DisplayPanel extends JPanel {

    public BufferedImage img;
    public boolean display = false;

    public DisplayPanel() {
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (display && img != null) {
            int w = img.getWidth(), h = img.getHeight();
            h = (int) (1.0f * this.getWidth() * h / w);
            w = this.getWidth();
            if (h > this.getHeight()) {
                w = (int) (1.0f * this.getHeight() * w / h);
                h = this.getHeight();
            }
            g.drawImage(img,
                    (this.getWidth() - w) / 2, (this.getHeight() - h) / 2,
                    w, h, null);
        }
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        getRootPane().getParent().addKeyListener(l);
    }
}
