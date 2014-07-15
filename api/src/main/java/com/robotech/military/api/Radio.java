package com.robotech.military.api;

public interface Radio {

    void setChannel(Double frequency);

    void broadcast(Double data);

    Double listen();
}
