package robo2d.game.impl;

import com.robotech.military.api.Program;
import com.robotech.military.api.Robot;
import com.robotech.military.internal.LocalConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class AbstractComputer {

    String cid = "" + (long) (Math.random() * 1000000000);

    Thread program;
    Map<String, String> state = new HashMap<String, String>();
    Socket handlingConnection;

    public void init() {
        setStateString("computer/ready", "true");
    }

    public void update() {
        setStateString("computer/working", program != null ? "true" : "false");
    }

    protected Class compile() {
        File file = ComputerHelper.compile(this);
        if (file == null) {
            return null;
        }
        try {
            // Try this if reloading fails
            //  http://tutorials.jenkov.com/java-reflection/dynamic-class-loading-reloading.html
            URL classUrl = file.toURI().toURL();
            URL[] urls = new URL[]{classUrl};
            ClassLoader ucl = new URLClassLoader(urls, getClass().getClassLoader());
            return ucl.loadClass("Boot");
        } catch (ClassNotFoundException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void startProgram() {
        try {
            if (program == null) {
                final Class code = compile();
                if (code == null || !Program.class.isAssignableFrom(code)) {
                    return;
                }
                program = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Program robotProgram = (Program) code.getConstructor().newInstance();
                            robotProgram.run(new Robot(new LocalConnection(getUid())));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
                program.setDaemon(true);
                program.setPriority(Thread.MIN_PRIORITY);
                program.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopProgram() {
        try {
            if (program != null) {
                program.interrupt();
                program.stop();
                program = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            program = null;
        }
        try {
            if (handlingConnection != null) {
                handlingConnection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            handlingConnection = null;
        }

    }

    public boolean isRunning() {
        return program != null && program.isAlive();
    }

    public void saveFile(String fileName, String content) {
        if (content == null || content.isEmpty()) {
            state.remove("computer/fs/" + fileName);
        } else {
            state.put("computer/fs/" + fileName, content);
        }
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> e : state.entrySet()) {
            if (e.getKey().startsWith("computer/fs/")) {
                buffer.append(e.getKey().replaceFirst("^computer/fs/", "")).append("\n");
            }
        }
        state.put("computer/fs", buffer.toString());
    }

    public String loadFile(String fileName) {
        return state.get("computer/fs/" + fileName);
    }

    public String getStateString(String key) {
        return state.get(key);
    }

    public void setStateString(String key, String value) {
        if (key.startsWith("computer/fs")) {
            saveFile(key, value);
        } else {
            state.put(key, value);
        }
    }

    public double getStateDouble(String key) {
        try {
            return Double.parseDouble(state.get(key));
        } catch (Exception e) {
            return 0;
        }
    }

    public String getUid() {
        return getName() + "-" + cid;
    }

    public String getName() {
        return "COMPUTER";
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

    public void handleConnection(String id, Socket socket, BufferedReader in, PrintWriter out) {
        try {
            if (id.equals(getUid())) {
                if (handlingConnection != null) {
                    try {
                        handlingConnection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                handlingConnection = socket;
                out.println("connected");
                out.flush();
                while (true) {
                    String method = in.readLine();
                    if (method.equals("get")) {
                        String key = unescape(in.readLine());
                        String value = escape(state.get(key));
                        System.out.println("GET [" + getUid() + "] " + key + " -> " + value);
                        out.println(value);
                        out.flush();
                    } else if (method.equals("set")) {
                        String key = unescape(in.readLine());
                        String value = unescape(in.readLine());
                        System.out.println("SET [" + getUid() + "]" + key + " = " + value);
                        state.put(key, value);
                    }
                }
            } else {
                out.println("cannot connect");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
