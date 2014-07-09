package com.robotech.military.remote.gen;
import com.robotech.military.api.*;
import com.robotech.military.remote.RemoteUtil;
public class Radio implements com.robotech.military.api.Radio {
    String rid;
    public Radio(String rid) {
        this.rid = rid;
    }

    public java.lang.Double listen() {
        return (java.lang.Double) RemoteUtil.request(rid, "listen");
    }

    public void broadcast(java.lang.Double arg0) {
        RemoteUtil.request(rid, "broadcast", arg0);
    }

    public void setChannel(java.lang.Double arg0) {
        RemoteUtil.request(rid, "setChannel", arg0);
    }

}
