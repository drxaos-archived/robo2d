package com.robotech.military.remote.gen;
import com.robotech.military.api.*;
import com.robotech.military.remote.RemoteUtil;
public class Turret implements com.robotech.military.api.Turret {
    String rid;
    public Turret(String rid) {
        this.rid = rid;
    }

    public void fire() {
        RemoteUtil.request(rid, "fire");
    }

}
