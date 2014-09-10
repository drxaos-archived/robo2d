package com.robotech.military.api;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class Console implements Serializable {
    IO io;

    public Console(IO io) {
        this.io = io;
    }

    public boolean ready() {
        return true;
    }

    public String getInput() {
        return io.get("console/input");
    }

    public String waitInput() {
        return io.wait("console/input/buffer");
    }

    public void setData(String data) {
        io.set("console/text", data);
    }

    public String getData() {
        return io.get("console/text");
    }

    public void print(String text) {
        if (text == null) {
            return;
        }
        String data = getData();
        if (data == null) {
            data = "";
        }
        data += text;
        ArrayList<String> strings = new ArrayList<String>();
        for (String s : data.split("(?<=\n)")) {
            while (s.length() > (s.endsWith("\n") ? 81 : 80)) {
                String[] split = s.split("(?<=\\G.{80})", 2);
                strings.add(split[0] + "\n");
                s = split[1];
            }
            strings.add(s);
        }
        while (strings.size() > 25) {
            strings.remove(0);
        }
        String res = "";
        for (int i = 0; i < strings.size(); i++) {
            res += strings.get(i);
        }
        setData(res);
    }

    public String read(String prompt) {
        print(prompt);
        String res = "";
        while (true) {
            String s = waitInput();
            if (s == null || s.isEmpty()) {
                continue;
            }
            for (char c : s.toCharArray()) {
                if (c == '\n') {
                    println();
                    return res;
                }
                if (!Character.isISOControl(c)) {
                    res += c;
                    print("" + c);
                }
                if (c == KeyEvent.VK_BACK_SPACE) {
                    if (!res.isEmpty()) {
                        res = res.substring(0, res.length() - 1);
                        String data = getData();
                        data = data.substring(0, data.length() - 1);
                        if (data.endsWith("\n")) {
                            data = data.substring(0, data.length() - 1);
                        }
                        setData(data);
                    }
                }
            }
        }
    }

    public void println() {
        println("");
    }

    public void println(String text) {
        if (text == null) {
            return;
        }
        print(text + "\n");
    }

    public void clear() {
        setData("");
    }

}
