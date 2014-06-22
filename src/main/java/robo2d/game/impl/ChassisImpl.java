package robo2d.game.impl;

import robo2d.game.api.Chassis;

public class ChassisImpl implements Chassis {

    Double leftAccel, rightAccel, maxAccel;

    public ChassisImpl(Double maxAccel) {
        this.maxAccel = maxAccel;
    }

    @Override
    public void setLeftAcceleration(Double percent) {
        percent = Math.min(Math.max(percent, 100), -100);
        leftAccel = maxAccel * percent / 100;
    }

    @Override
    public void setRightAcceleration(Double percent) {
        percent = Math.min(Math.max(percent, 100), -100);
        rightAccel = maxAccel * percent / 100;
    }

    @Override
    public Double getLeftSpeed() {
        return null;
    }

    @Override
    public Double getRightSpeed() {
        return null;
    }
}
