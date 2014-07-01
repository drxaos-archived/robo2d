package graphics;

import objimp.ObjImpScene;
import org.lwjgl.opengl.GL11;

public class Robot extends ObjImpScene implements Drawable {
    public float yRot;
    float x, y, z;
    float right = 0, left = 0, front = 0, back = 0, top = 0, bottom = 0;
    float scaleW, scaleL, scaleH;

    @Override
    public void init() {
        load("models/bot/robot.obj");
        for (float[] floats : _vertexList) {
            if (floats[2] < back) {
                back = floats[2];
            }
            if (floats[2] > front) {
                front = floats[2];
            }
            if (floats[0] < right) {
                right = floats[0];
            }
            if (floats[0] > left) {
                left = floats[0];
            }
            if (floats[1] < bottom) {
                bottom = floats[1];
            }
            if (floats[1] > top) {
                top = floats[1];
            }
        }
        scaleW = 0.5f / Math.max(left, Math.abs(right));
        scaleL = 0.5f / Math.max(front, Math.abs(back));
        scaleH = Math.max(scaleL, scaleW);
    }

    @Override
    public void draw() {
        GL11.glPushMatrix();
        GL11.glTranslatef(x + (front + back) * scaleL, y + (left + right) * scaleW, z - bottom * scaleH);
        GL11.glRotatef(90, 1, 0, 0);
        GL11.glRotatef(yRot + 90, 0, 1, 0);
        GL11.glScalef(scaleW, scaleL, scaleH);
        super.draw();
        GL11.glPopMatrix();
    }

    public float getYRot() {
        return yRot;
    }

    public void setYRot(float yRot) {
        this.yRot = yRot;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
