package com.robotech.military.api;

import java.io.Serializable;

public interface Program extends Serializable {
    void run(Host robot);
}
