package th;

import graphics.Draw3D;
import graphics.Ground;
import graphics.Robot;
import graphics.Sphere;

public class Main3D {

    static Draw3D draw3D = new Draw3D();

    static float a = 0;

    public static void main(String[] args) {

        draw3D.init();

        final float[] ambience = new float[]{10.0f, 10.0f, 10.0f, 1.0f};
        final float[] diffuse = new float[]{10.0f, 10.0f, 10.0f, 1.0f};
        final float[] position = new float[]{0.0f, 0.0f, 100.0f, 1.0f};
        draw3D.setLight(ambience, diffuse, position, 80);
        final float[] fog = new float[]{0.5f, 0.5f, 0.5f, 1.0f};
        //draw3D.setFog(fog, 0.5f, 400, 500);

        draw3D.addDrawable(new Ground());
        draw3D.addDrawable(new Robot());
        draw3D.addDrawable(new Sphere());

        draw3D.render();

        draw3D.destroy();
    }

}
