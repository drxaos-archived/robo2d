package com.robotech.game.box;

import org.jbox2d.dynamics.World;

import java.io.Serializable;

public interface Box extends Serializable {

    void setWorld(World world);

    float getX();

    void setX(float x);

    float getY();

    void setY(float y);

    float getAngle();

    void setAngle(float angle);

}
