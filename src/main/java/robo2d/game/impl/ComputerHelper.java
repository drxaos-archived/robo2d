package robo2d.game.impl;

import bluej.compiler.CompileObserver;
import bluej.compiler.CompilerAPICompiler;
import bluej.compiler.Diagnostic;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ComputerHelper {

    public static synchronized void saveToDisk(ComputerImpl computer, File path) {
        try {
            FileUtils.deleteDirectory(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        path.mkdirs();
        File templateDir = new File("templates/bluej");
        try {
            FileUtils.copyDirectory(templateDir, path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, String> e : computer.getMemory().entrySet()) {
            File file = new File(path, e.getKey());
            try {
                FileUtils.write(file, e.getValue());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static synchronized void loadFromDisk(ComputerImpl computer, File path) {
        for (File file : FileUtils.listFiles(path, new String[]{"java", "txt"}, true)) {
            try {
                String content = FileUtils.readFileToString(file);
                computer.getMemory().put(file.getName(), content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileUtils.deleteDirectory(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized File compile(ComputerImpl computer) {
        final File compileDir = new File("tmp/" + computer.robot.getUid());
        saveToDisk(computer, compileDir);

        CompilerAPICompiler compiler = new CompilerAPICompiler();
        compiler.setDestDir(compileDir);

        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader) cl).getURLs();
        File[] classPath = new File[urls.length];
        for (int i = 0; i < urls.length; i++) {
            classPath[i] = new File(urls[i].getFile());
        }

        compiler.setClasspath(classPath);
        compiler.setBootClassPath(null);

        List<String> options = new ArrayList<String>();
        options.add(0, "-source");
        options.add(1, "1.6");

        Collection<File> files = FileUtils.listFiles(compileDir, new String[]{"java", "txt"}, true);
        CompileObserver observer = new CompileObserver() {
            @Override
            public void startCompile(File[] files) {
                System.out.println("Compilation started: " + compileDir.getName());
            }

            @Override
            public boolean compilerMessage(Diagnostic diagnostic) {
                System.out.println("Compilation message: " + diagnostic.getFileName() + ":" + diagnostic.getStartLine() + " " + diagnostic.getMessage());
                return true;
            }

            @Override
            public void endCompile(File[] files, boolean b) {
                System.out.println("Compilation " + (b ? "success" : "fail") + ": " + compileDir.getName());
            }
        };
        boolean compiled = compiler.compile(files.toArray(new File[files.size()]), observer, true, options, Charset.defaultCharset());
        return compiled ? compileDir : null;
    }

}
