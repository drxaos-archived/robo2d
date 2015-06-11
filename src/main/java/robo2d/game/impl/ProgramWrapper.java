package robo2d.game.impl;

import com.robotech.military.api.Executable;
import net.sf.jauvm.vm.GlobalCodeCache;
import net.sf.jauvm.vm.VirtualMachine;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ProgramWrapper {

    public static VirtualMachine vm;

    static {
        GlobalCodeCache.setCodeLoader(new GlobalCodeCache.CodeLoader() {
            @Override
            public InputStream getBytecodeStream(Class<?> cls) {
                if (cls.getCanonicalName().startsWith("java.")) {
                    return null;
                }
                if (cls.getCanonicalName().startsWith("javax.")) {
                    return null;
                }
                if (cls.getCanonicalName().startsWith("com.robotech.military.internal.")) {
                    return null;
                }
                return super.getBytecodeStream(cls);
            }

            @Override
            public boolean checkAccess(Class cls) {
                System.out.println("Using class: " + cls.getCanonicalName());
                if (Executable.class.isAssignableFrom(cls)) {
                    return false;
                }
                return true;
            }
        });
    }

    public static boolean isWorking() {
        return vm != null;
    }

    public static void start(Class cls) {
        try {
            vm = VirtualMachine.create(cls);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void step() {
        try {
            if (vm.run(1)) {
                stop();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void stop() {
        vm = null;
    }

    public static String save() {
        try {
            return vm.getFullState();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static void load(String state) {
        try {
            vm = VirtualMachine.create(new ByteArrayInputStream(Base64.decodeBase64(state)));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


}
