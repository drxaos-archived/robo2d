package com.robotech.military.internal;

import com.robotech.military.api.IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LocalConnection implements IO {
    String id;
    Socket socket;
    PrintWriter writer;
    BufferedReader reader;

    public LocalConnection(String id) {
        this.id = id;
    }

    private synchronized void reset() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("localhost", 4455));
                socket.getChannel();
                writer = new PrintWriter(socket.getOutputStream());
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer.println(id);
                String answer = reader.readLine();
                if (answer.equals("connected")) {
                    throw new ConnectionError("handshake error: " + answer);
                }
            } catch (IOException e) {
                throw new ConnectionError(e);
            }
        }
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\r", "\\r").replace("\n", "\\n");
    }

    private String unescape(String s) {
        return s.replace("\\r", "\r").replace("\\n", "\n").replace("\\\\", "\\");
    }

    @Override
    public synchronized void set(String key, String value) {
        writer.println("set");
        writer.println(escape(key));
        writer.println(escape(value));
    }

    @Override
    public synchronized String get(String key) {
        for (int i = 0; true; i++) {
            writer.println("get");
            writer.println(escape(key));
            try {
                return unescape(reader.readLine());
            } catch (IOException e) {
                if (i > 10) {
                    throw new ConnectionError(e);
                }
                reset();
            }
        }
    }
}
