package graphics;

import objimp.ObjImpScene;
import org.lwjgl.opengl.GL11;

public class Sphere extends ObjImpScene implements Drawable {
    @Override
    public void init() {
        load("models/sphere/sphere.obj");
    }

    @Override
    public void draw() {
        GL11.glPushMatrix();
        GL11.glTranslatef(300, 300, 100);
        GL11.glScalef(80, 80, 80);
        super.draw();
        GL11.glPopMatrix();
    }
}
