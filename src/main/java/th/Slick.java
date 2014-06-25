package th;

import org.newdawn.slick.*;
import slick2d.NativeLoader;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Slick extends BasicGame {
    public Slick(String gamename) {
        super(gamename);
    }

    int mx, my;
    boolean md;

    @Override
    public void init(GameContainer gc) throws SlickException {
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        mx = gc.getInput().getMouseX();
        my = gc.getInput().getMouseY();
        md = gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
        if (gc.getInput().isKeyPressed(org.lwjgl.input.Keyboard.KEY_RCONTROL)) {
            System.out.println("R-CTRL");
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.setColor(md ? Color.red : Color.white);
        g.drawString("Howdy!", mx, my);
    }

    public static void main(String[] args) {
        NativeLoader.load("build/natives");

        try {
            AppGameContainer appgc;
            Slick game = new Slick("Simple Slick Game");
            appgc = new AppGameContainer(game);
            appgc.setDisplayMode(640, 480, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Slick.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}