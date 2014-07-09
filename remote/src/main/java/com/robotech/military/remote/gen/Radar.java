package com.robotech.military.remote.gen;
import com.robotech.military.api.*;
import com.robotech.military.remote.RemoteUtil;
public class Radar implements com.robotech.military.api.Radar {
    String rid;
    public Radar(String rid) {
        this.rid = rid;
    }

    public com.robotech.military.api.Radar.SatelliteScanData getSatelliteResponse() {
        return (com.robotech.military.api.Radar.SatelliteScanData) RemoteUtil.request(rid, "getSatelliteResponse");
    }

    public void clearSatelliteResponse() {
        RemoteUtil.request(rid, "clearSatelliteResponse");
    }

    public java.lang.Boolean satelliteRequest(com.robotech.military.api.Point arg0, double arg1) {
        return (java.lang.Boolean) RemoteUtil.request(rid, "satelliteRequest", arg0, arg1);
    }

    public com.robotech.military.api.Radar.LocatorScanData locate(double arg0) {
        return (com.robotech.military.api.Radar.LocatorScanData) RemoteUtil.request(rid, "locate", arg0);
    }

    public com.robotech.military.api.Point getPosition() {
        return (com.robotech.military.api.Point) RemoteUtil.request(rid, "getPosition");
    }

    public java.lang.Double getAngle() {
        return (java.lang.Double) RemoteUtil.request(rid, "getAngle");
    }

}
