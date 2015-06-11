package com.robotech.api;

public class System implements Executable {

    private static byte addr;
    private static byte ctrl;
    private static byte data;
    private static byte intr;

    public static final byte ADDR_CON_OUT = 80;
    public static final byte ADDR_CON_KBD = 81;

    public static final byte CTRL_IDLE = 0;
    public static final byte CTRL_SEND = 1;
    public static final byte CTRL_RECV = 2;

    public static final byte INTR_NONE = 0;
    public static final byte INTR_HACK = 1;
    public static final byte INTR_BUMP = 2;
    public static final byte INTR_SHOT = 2;


    public static void write(byte address, byte data) {
        System.addr = address;
        System.data = data;
        System.ctrl = CTRL_SEND;
    }

    public static void read(byte address, byte data) {
        System.addr = address;
        System.ctrl = CTRL_RECV;
        System.data = data;
    }
}
