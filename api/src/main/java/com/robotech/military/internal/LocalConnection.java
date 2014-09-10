package com.robotech.military.internal;

import com.robotech.military.api.IO;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LocalConnection implements IO, Serializable {
    String id;
    transient Socket socket;
    transient PrintWriter writer;
    transient BufferedReader reader;

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
                writer.flush();
                String answer = reader.readLine();
                if (!answer.equals("connected")) {
                    throw new ConnectionError("handshake error: " + answer);
                }
            } catch (IOException e) {
                throw new ConnectionError(e);
            }
        }
    }

    private String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\r", "\\r").replace("\n", "\\n");
    }

    private String unescape(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        return s.replace("\\r", "\r").replace("\\n", "\n").replace("\\\\", "\\");
    }

    @Override
    public synchronized void set(String key, String value) {
        for (int i = 0; true; i++) {
            try {
                writer.println("set");
                writer.println(escape(key));
                writer.println(escape(value));
                writer.flush();
                return;
            } catch (Exception e) {
                if (i > 10) {
                    throw new ConnectionError(e);
                }
                reset();
            }
        }
    }

    @Override
    public synchronized String get(String key) {
        for (int i = 0; true; i++) {
            try {
                writer.println("get");
                writer.println(escape(key));
                writer.flush();
                return unescape(reader.readLine());
            } catch (Exception e) {
                if (i > 10) {
                    throw new ConnectionError(e);
                }
                reset();
            }
        }
    }

    @Override
    public synchronized String wait(String key) {
        for (int i = 0; true; i++) {
            try {
                writer.println("wait");
                writer.println(escape(key));
                writer.flush();
                return unescape(reader.readLine());
            } catch (Exception e) {
                if (i > 10) {
                    throw new ConnectionError(e);
                }
                reset();
            }
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
