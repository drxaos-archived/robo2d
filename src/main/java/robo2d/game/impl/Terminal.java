package robo2d.game.impl;

import robo2d.testbed.devices.DisplayPanel;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Terminal {

    static DisplayPanel display;
    static boolean connected = false;
    static AbstractComputer computer;

    private static Font font = new Font("Monospaced", Font.BOLD, 12);
    private static int fontWidth;
    private static int fontHeight;
    public static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();

    public static void setDisplay(DisplayPanel display) {
        Terminal.display = display;
        display.addKeyListener(keyListener);
    }

    public static final KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            AbstractComputer c = computer;
            if (c != null) {
                c.setStateString("console/input", "" + e.getKeyChar());
                String buf = c.getStateString("console/input/buffer");
                if (buf == null) {
                    buf = "";
                }
                buf += e.getKeyChar();
                if (buf.length() > 10) {
                    buf = buf.substring(buf.length() - 10);
                }
                c.setStateString("console/input/buffer", buf);
            }
        }
    };

    static {
        Thread displayUpdater = new Thread() {
            @Override
            public void run() {
                while (true) {
                    DisplayPanel d = display;
                    AbstractComputer c = computer;
                    if (d != null && c != null) {
                        if (d.img == null) {
                            String text = "X";
                            AffineTransform affinetransform = new AffineTransform();
                            FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
                            fontWidth = (int) (font.getStringBounds(text, frc).getWidth());
                            fontHeight = (int) (font.getStringBounds(text, frc).getHeight());
                            d.img = new BufferedImage(80 * fontWidth, 25 * fontHeight, BufferedImage.TYPE_INT_RGB);
                        }
                        d.display = true;
                        Graphics2D g = (Graphics2D) d.img.getGraphics();
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setColor(Color.BLUE);
                        g.fillRect(0, 0, d.img.getWidth(), d.img.getHeight());
                        g.setColor(Color.WHITE);
                        g.setFont(font);
                        String text = computer.getStateString("console/text");
                        if (text != null && !text.isEmpty()) {
                            int y = 0;
                            for (String line : text.split("\n")) {
                                g.drawString(line, 0, y += fontHeight);
                            }
                        }
                        String video = computer.getStateString("console/video");
                        if (video != null && !video.isEmpty()) {
                            for (String line : video.split("\n")) {
                                BufferedImage img = images.get(line);
                                if (img != null) {
                                    g.drawImage(img, 0, 0, 80 * fontWidth, 25 * fontHeight, null);
                                }
                            }
                        }
                        d.repaint();
                    } else if (d != null) {
                        d.display = false;
                        d.repaint();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                }
            }
        };
        displayUpdater.setDaemon(true);
        displayUpdater.start();
    }

    public static boolean canConnect() {
        return computer != null && computer.canDebug();
    }

    public synchronized static void open(final AbstractComputer computer) {
        Terminal.computer = computer;
    }

    public synchronized static void connect() {
        if (connected) {
            disconnect();
        }
        if (computer != null && computer.canDebug()) {


        }
    }

    public synchronized static void close(AbstractComputer computer) {
        disconnect();
        Terminal.computer = null;
    }

    public synchronized static void disconnect() {
        if (connected) {

            connected = false;
        }
    }
}
