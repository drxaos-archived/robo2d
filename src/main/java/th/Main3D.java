package th;

import graphics.Draw3D;
import graphics.Ground;
import graphics.Robot;

public class Main3D {

    static Draw3D draw3D = new Draw3D();

    public static void main(String[] args) {

        draw3D.init();

        float[] ambience = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
        float[] diffuse = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        float[] position = new float[]{1000.0f, 0.0f, 1000.0f, 1.0f};
        draw3D.setLight(ambience, diffuse, position, 80);

//        draw3D.addDrawable(new Ground());
        draw3D.addDrawable(new Robot());

        draw3D.render();

        draw3D.destroy();
    }

}
