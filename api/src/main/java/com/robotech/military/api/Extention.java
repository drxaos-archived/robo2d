package com.robotech.military.api;

import java.io.Serializable;

public interface Extention {
    Serializable invoke(String method, Serializable... args);
}
