package com.robotech.military.remote;

import com.robotech.military.api.Equipment;
import com.robotech.military.api.Robot;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Generator {
    public static void main(String[] args) {
        List<Class> classes = new ArrayList<Class>(Arrays.asList(Equipment.EQUIPMENT));
        classes.add(Robot.class);

        File dir = new File("remote/src/main/java/com/robotech/military/remote/gen/");
        dir.mkdirs();

        for (Class c : classes) {
            StringBuilder b = new StringBuilder();

            b.append("package com.robotech.military.remote.gen;\n");
            b.append("import com.robotech.military.api.*;\n");
            b.append("import com.robotech.military.remote.RemoteUtil;\n");
            b.append("public class " + c.getSimpleName() + " implements " + c.getCanonicalName() + " {\n");
            b.append("    String rid;\n");
            b.append("    public "+c.getSimpleName()+"(String rid) {\n");
            b.append("        this.rid = rid;\n");
            b.append("    }\n");
            b.append("\n");

            for (Method method : c.getDeclaredMethods()) {
                List<String> paramsM = new ArrayList<String>();
                List<String> paramsF = new ArrayList<String>();
                paramsF.add("");
                Class<?>[] parameterTypes = method.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    paramsM.add(parameterTypes[i].getCanonicalName() + " arg" + i);
                    paramsF.add("arg" + i);
                }
                b.append("    public " + method.getReturnType().getCanonicalName() + " " + method.getName() + "(" + String.join(", ", paramsM) + ") {\n");
                String returnType = method.getReturnType().getCanonicalName();
                if(returnType.equals("void")){
                    b.append("        RemoteUtil.request(rid, \"" + method.getName() + "\"" + String.join(", ", paramsF) + ");\n");
                }else {
                    b.append("        return (" + method.getReturnType().getCanonicalName() + ") RemoteUtil.request(rid, \"" + method.getName() + "\"" + String.join(", ", paramsF) + ");\n");
                }
                b.append("    }\n");
                b.append("\n");
            }
            b.append("}\n");

            File file = new File("remote/src/main/java/com/robotech/military/remote/gen/" + c.getSimpleName() + ".java");
            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write(b.toString().getBytes());
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
