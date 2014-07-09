package com.robotech.military.api;

public interface Radio extends Equipment {

    void setChannel(Double frequency);

    void broadcast(Double data);

    Double listen();
}
