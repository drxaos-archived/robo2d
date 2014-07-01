package th;

import graphics.Draw3D;
import graphics.Ground;
import graphics.Robot;

public class Main3D {

    static Draw3D draw3D = new Draw3D();

    public static void main(String[] args) {

        draw3D.init();

        final float[] ambience = new float[]{10.0f, 10.0f, 10.0f, 1.0f};
        final float[] diffuse = new float[]{10.0f, 10.0f, 10.0f, 1.0f};
        final float[] position = new float[]{0.0f, 0.0f, 100.0f, 1.0f};
        draw3D.setLight(ambience, diffuse, position, 80);
        //final float[] fog = new float[]{0.5f, 0.5f, 0.5f, 1.0f};
        //draw3D.setFog(fog, 0.5f, 400, 500);

        final Robot robot = new Robot();
        robot.setX(1.5f);
        robot.setY(1.5f);

        draw3D.addDrawable(new Ground());
        draw3D.addDrawable(robot);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    robot.setYRot(robot.getYRot() + 1);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        draw3D.render();

        draw3D.destroy();
    }

}
