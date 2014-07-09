package com.robotech.military.api.internal;

import java.io.Serializable;

public class Request {
    public String rid, method;
    public Object[] args;

    public Request(String rid, String method, Object[] args) {
        this.rid = rid;
        this.method = method;
        this.args = args;
    }
}
