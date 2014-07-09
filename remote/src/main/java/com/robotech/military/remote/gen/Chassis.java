package com.robotech.military.remote.gen;
import com.robotech.military.api.*;
import com.robotech.military.remote.RemoteUtil;
public class Chassis implements com.robotech.military.api.Chassis {
    String rid;
    public Chassis(String rid) {
        this.rid = rid;
    }

    public void setLeftAcceleration(java.lang.Double arg0) {
        RemoteUtil.request(rid, "setLeftAcceleration", arg0);
    }

    public void setRightAcceleration(java.lang.Double arg0) {
        RemoteUtil.request(rid, "setRightAcceleration", arg0);
    }

    public java.lang.Double getRightSpeed() {
        return (java.lang.Double) RemoteUtil.request(rid, "getRightSpeed");
    }

    public java.lang.Boolean isWorking() {
        return (java.lang.Boolean) RemoteUtil.request(rid, "isWorking");
    }

    public java.lang.Double getLeftSpeed() {
        return (java.lang.Double) RemoteUtil.request(rid, "getLeftSpeed");
    }

}
