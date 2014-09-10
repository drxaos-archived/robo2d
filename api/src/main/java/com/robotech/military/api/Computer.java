package com.robotech.military.api;

import java.io.Serializable;

public class Computer implements Serializable {

    IO io;

    public Computer(IO io) {
        this.io = io;
    }

    public void saveFile(String fileName, String content) {
        io.set("computer/fs/" + fileName, content);
    }

    public String loadFile(String fileName) {
        return io.get("computer/fs/" + fileName);
    }

    public String listFiles() {
        return io.get("computer/fs");
    }
}
