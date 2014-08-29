package com.robotech.military.api;

import java.util.ArrayList;

public class Console {
    IO io;

    public Console(IO io) {
        this.io = io;
    }

    public boolean ready() {
        return true;
    }

    public void set(String data) {
        io.set("console/text", data);
    }

    public String get() {
        return io.get("console/text");
    }

    public void print(String text) {
        String data = get();
        data += text;
        ArrayList<String> strings = new ArrayList<String>();
        for (String s : data.split("\n")) {
            for (String s1 : s.split("(?<=\\G.{80})")) {
                strings.add(s1);
            }
        }
        while (strings.size() > 25) {
            strings.remove(0);
        }
        String res = "";
        for (String s : strings) {
            res += s + "\n";
        }
        set(res);
    }

    public void println(String text) {
        print(text + "\n");
    }

    public void clear() {
        set("");
    }

}
