package graphics;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

public class Ground implements Drawable {

    public Texture texture;
    public int objectDisplayList;

    final int W = 60, H = 60;
    float[][] heights;

    public void init() {
        try {
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("models/grass/grass.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        heights = new float[W + 1][H + 1];
        for (int i = 0; i < W + 1; i++) {
            for (int j = 0; j < H + 1; j++) {
                heights[i][j] = (float) Math.random() * 3;
            }
        }

        objectDisplayList = GL11.glGenLists(1);
        GL11.glNewList(objectDisplayList, GL11.GL_COMPILE);
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                GL11.glBegin(GL11.GL_QUADS);

                GL11.glNormal3f(0, 0, 1f);
                GL11.glTexCoord2f(0.0f, 0.0f);
                GL11.glVertex3f(i * 10f, j * 10f, heights[i][j]);

                GL11.glNormal3f(0, 0, 1f);
                GL11.glTexCoord2f(0.5f, 0.0f);
                GL11.glVertex3f((i + 1) * 10f, j * 10f, heights[i + 1][j]);

                GL11.glNormal3f(0, 0, 1f);
                GL11.glTexCoord2f(0.5f, 0.5f);
                GL11.glVertex3f((i + 1) * 10f, (j + 1) * 10f, heights[i + 1][j + 1]);

                GL11.glNormal3f(0, 0, 1f);
                GL11.glTexCoord2f(0.0f, 0.5f);
                GL11.glVertex3f(i * 10f, (j + 1) * 10f, heights[i][j + 1]);


                GL11.glEnd();
            }
        }
        GL11.glEndList();


    }

    public void draw() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPushMatrix();
        GL11.glCallList(objectDisplayList);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

}
