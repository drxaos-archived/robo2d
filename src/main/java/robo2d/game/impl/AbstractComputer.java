package robo2d.game.impl;

import com.robotech.military.api.Host;
import com.robotech.military.api.Program;
import com.robotech.military.internal.LocalConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AbstractComputer implements Dynamic {

    String cid = "" + (long) (Math.random() * 1000000000);

    Thread program;
    Map<String, String> state = new HashMap<String, String>();
    Socket handlingConnection;
    final Object waitMon = new Object();

    public void init() {
        setStateString("computer/ready", "true");
        setStateString("console/text", "SYSTEM HALTED");
    }

    public void update() {
        setStateString("computer/working", program != null ? "true" : "false");
    }

    public boolean canDebug() {
        return true;
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

            URL[] apiUrls = ((URLClassLoader) Program.class.getClassLoader()).getURLs();
            URL[] urls = Arrays.copyOf(apiUrls, apiUrls.length + 1);
            urls[urls.length - 1] = classUrl;

            ClassLoader ucl = new URLClassLoader(urls, null/*getClass().getClassLoader()*/);
            Class<?> boot = ucl.loadClass("Boot");
            Class<?> program = ucl.loadClass("com.robotech.military.api.Program");
            if (boot == null || !program.isAssignableFrom(boot)) {
                return null;
            }
            Class<?> wrapper = ucl.loadClass("robo2d.game.impl.ProgramWrapper");
            return wrapper;
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
                program = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setStateString("console/text", getStateString("console/text") + "\nBOOT...");
                            code.getField("uid").set(null, getUid());
                            code.newInstance();
                        } catch (Throwable e) {
                            e.printStackTrace();
                            setStateString("console/text", getStateString("console/text") + "\nSOFTWARE ABORT");
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
        setStateString("console/text", getStateString("console/text") + "\nSYSTEM HALTED");
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
            synchronized (waitMon) {
                state.put(key, value);
                waitMon.notifyAll();
            }
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
                    out.println(value);
                    out.flush();
                } else if (method.equals("set")) {
                    String key = unescape(in.readLine());
                    String value = unescape(in.readLine());
                    state.put(key, value);
                } else if (method.equals("wait")) {
                    String key = unescape(in.readLine());
                    String value = null;
                    synchronized (waitMon) {
                        value = escape(state.get(key));
                        long timeout = System.currentTimeMillis() + 15000;
                        while ((value == null || value.isEmpty()) && System.currentTimeMillis() < timeout) {
                            waitMon.wait(3000);
                            value = escape(state.get(key));
                        }
                        state.put(key, null);
                    }
                    out.println(value);
                    out.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
