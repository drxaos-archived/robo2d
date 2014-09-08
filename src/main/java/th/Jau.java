package th;

import javassist.*;
import net.sf.jauvm.vm.GlobalCodeCache;
import net.sf.jauvm.vm.VirtualMachine;

import java.io.InputStream;
import java.io.Serializable;

public class Jau {

    public static void main(String[] args) throws Throwable {

        GlobalCodeCache.setCodeLoader(new GlobalCodeCache.CodeLoader() {
            @Override
            public InputStream getBytecodeStream(Class<?> cls) {
                if (cls.getCanonicalName().startsWith("java.lang.")) {
                    return null;
                }
                if (cls.getCanonicalName().startsWith("java.io.")) {
                    return null;
                }
                if (cls.getCanonicalName().equals(Api.class.getCanonicalName())) {
                    return null;
                }
                return super.getBytecodeStream(cls);
            }

            @Override
            public boolean checkAccess(Class cls) {
                if (cls.equals(System.class)) {
                    return false;
                }
                //System.out.println("Using class: " + cls.getCanonicalName());
                return true;
            }
        });
        Translator translator = new Translator() {
            public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
            }

            public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
                CtClass cc = pool.get(classname);
                //System.out.println("Loading class: " + classname);
                if (cc.isFrozen()) {
                    return;
                }
                for (CtClass ic : cc.getInterfaces()) {
                    if (ic.getName().equals(Serializable.class.getCanonicalName())) {
                        return;
                    }
                }
                cc.addInterface(pool.get(Serializable.class.getCanonicalName()));
            }
        };

        ClassPool pool = ClassPool.getDefault();
        Loader cl = new Loader();
        cl.addTranslator(pool, translator);
        Class runCls = cl.loadClass(Test123.class.getCanonicalName());

        VirtualMachine vm = VirtualMachine.create((Runnable) runCls.newInstance());

        for (int i = 0; i < 1000000; i++) {
            if (vm.run(1)) {
                break;
            } else {
                System.out.println("VM step " + i + " snapshot size: " + vm.getFullState().length());
            }
        }
    }

}
