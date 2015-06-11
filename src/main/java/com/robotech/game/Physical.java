package com.robotech.game;

import com.robotech.game.box.Box;

import java.io.Serializable;

public abstract class Physical implements Serializable {

    protected String uid;
    protected float x, y, angle;

    public Physical(String uid) {
        this.uid = uid;
    }

    public abstract Box getBox();

    public abstract void init();

    public abstract void beforeStep(long time);

    public abstract void afterStep(long time);

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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
