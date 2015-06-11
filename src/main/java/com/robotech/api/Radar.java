package com.robotech.api;

public interface Radar {
    byte TYPE_UNKNOWN = 0;
    byte TYPE_WALL = 1;
    byte TYPE_MOVING_UNIT = 2;
    byte TYPE_STATIC_UNIT = 3;
    byte TYPE_FLAG = 4;
    byte TYPE_BUILDING = 5;
    byte TYPE_EMPTY = 100;

    byte PARTY_NONE = 0;
    byte PARTY_RED = 1;
    byte PARTY_GREEN = 2;
    byte PARTY_BLUE = 3;
    byte PARTY_YELLLOW = 4;
}
