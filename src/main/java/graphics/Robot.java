package graphics;

import objimp.ObjImpScene;

public class Robot extends ObjImpScene implements Drawable {
    @Override
    public void init() {
        load("models/bot/robot.obj");
    }
}
