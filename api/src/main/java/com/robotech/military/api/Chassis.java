package com.robotech.military.api;

public interface Chassis {

    void setLeftAcceleration(Double percent);

    void setRightAcceleration(Double percent);

    Double getLeftSpeed();

    Double getRightSpeed();

    Boolean isWorking();
}
