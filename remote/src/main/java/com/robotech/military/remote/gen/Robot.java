package com.robotech.military.remote.gen;
import com.robotech.military.api.*;
import com.robotech.military.remote.RemoteUtil;
public class Robot implements com.robotech.military.api.Robot {
    String rid;
    public Robot(String rid) {
        this.rid = rid;
    }

    public void debug(java.lang.Object arg0) {
        RemoteUtil.request(rid, "debug", arg0);
    }

    public java.lang.Long getTime() {
        return (java.lang.Long) RemoteUtil.request(rid, "getTime");
    }

    public java.lang.Double getEnergy() {
        return (java.lang.Double) RemoteUtil.request(rid, "getEnergy");
    }

    public void waitForStep() {
        RemoteUtil.request(rid, "waitForStep");
    }

    public com.robotech.military.api.Equipment getEquipment(java.lang.Class arg0) {
        return (com.robotech.military.api.Equipment) RemoteUtil.request(rid, "getEquipment", arg0);
    }

    public java.lang.String getUid() {
        return (java.lang.String) RemoteUtil.request(rid, "getUid");
    }

}
