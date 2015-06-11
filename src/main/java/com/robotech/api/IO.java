package com.robotech.api;

public interface IO {
    void set(String key, String value);

    String get(String key);

    String wait(String key);
}
