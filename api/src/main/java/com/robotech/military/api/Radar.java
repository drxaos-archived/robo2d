package com.robotech.military.api;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Radar extends Remote {

    public static enum Type {
        UNKNOWN,
        EMPTY,
        WALL,
        ENEMY_BOT,
        MATE_BOT,
        ME
    }

    public static class LocatorScanData implements Serializable {
        public Type pixel;
        public double distance;
        public double angle;

        public LocatorScanData(Type pixel, double distance, double angle) {
            this.pixel = pixel;
            this.distance = distance;
            this.angle = angle;
        }
    }

    public static class SatelliteScanData implements Serializable {

        public Type[][] image;
        public double accuracy;
        public int centerX, centerY;

        public SatelliteScanData(Type[][] image, double accuracy, int centerX, int centerY) {
            this.image = image;
            this.accuracy = accuracy;
            this.centerX = centerX;
            this.centerY = centerY;
        }
    }

    Boolean satelliteRequest(Point center, double accuracy) throws RemoteException;

    SatelliteScanData getSatelliteResponse() throws RemoteException;

    void clearSatelliteResponse() throws RemoteException;

    LocatorScanData locate(double angle) throws RemoteException;

    Double getAngle() throws RemoteException;

    Point getPosition() throws RemoteException;

}
