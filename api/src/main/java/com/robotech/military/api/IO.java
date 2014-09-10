package com.robotech.military.api;

import java.io.Serializable;

public interface IO extends Serializable {
    void set(String key, String value);

    String get(String key);

    String wait(String key);
}
