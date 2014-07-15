package com.robotech.military.api;

public interface Robot {

    Chassis getChassis();

    Computer getComputer();

    Radar getRadar();

    Radio getRadio();

    Turret getTurret();

    Extention[] getExtentions();

    Double getEnergy();

    String getUid();

    Long getTime();

    void waitForStep();

    void debug(Object dbg);
}
