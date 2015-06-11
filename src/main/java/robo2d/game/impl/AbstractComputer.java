package robo2d.game.impl;

import robo2d.game.impl.dynamics.Dynamic;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class AbstractComputer implements Dynamic {

    String cid = "" + (long) (Math.random() * 1000000000);

    protected Class code;

    public void update() {

    }

    @Override
    public void init() {

    }

    public boolean canDebug() {
        return true;
    }

    protected Class compileAndRun() {
        try {
            // Try this if reloading fails
            //  http://tutorials.jenkov.com/java-reflection/dynamic-class-loading-reloading.html
            URL classUrl = null;

            URL[] apiUrls = ((URLClassLoader) this.getClass().getClassLoader()).getURLs();
            URL[] urls = Arrays.copyOf(apiUrls, apiUrls.length + 1);
            urls[urls.length - 1] = classUrl;

            ClassLoader ucl = new URLClassLoader(urls, null);
            Class<?> boot = ucl.loadClass("Boot");
            Class<?> wrapper = ucl.loadClass(ProgramWrapper.class.getCanonicalName());
            wrapper.getMethod("start", Class.class).invoke(null, boot);
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
            stopProgram();
            if (!isRunning()) {
                code = compileAndRun();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopProgram() {
        try {
            if (isRunning()) {
                code.getMethod("stop").invoke(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        try {
            return code != null && ((Boolean) code.getMethod("isWorking").invoke(null)) == true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveFile(String fileName, String content) {
    }

    public String loadFile(String fileName) {
        return "";
    }

    public String getStateString(String key) {
        return "";
    }

    public void setStateString(String key, String value) {
    }

    public double getStateDouble(String key) {
        return 0;
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
}
