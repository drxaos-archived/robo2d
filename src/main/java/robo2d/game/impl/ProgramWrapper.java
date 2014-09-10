package robo2d.game.impl;

import javassist.*;
import net.sf.jauvm.vm.GlobalCodeCache;
import net.sf.jauvm.vm.VirtualMachine;

import java.io.InputStream;
import java.io.Serializable;

public class ProgramWrapper {

    public static String uid = "debug";

    public ProgramWrapper() {
        run();
    }

    public void run() {
        try {
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
                    if (cls.equals(System.class)) {
                        return false;
                    }
                    return true;
                }
            });
            Translator translator = new Translator() {
                public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
                }

                public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
                    System.out.println("Loading class: " + classname);
                    CtClass cc = pool.get(classname);
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

            ClassPool pool = new ClassPool(null);
            pool.appendClassPath(new LoaderClassPath(this.getClass().getClassLoader()));

            Loader cl = new Loader(this.getClass().getClassLoader(), pool);
            cl.addTranslator(pool, translator);
            final Class cls = cl.loadClass(ProgramWrapperHelper.class.getCanonicalName());
            VirtualMachine vm = VirtualMachine.create((Runnable) cls.getDeclaredConstructors()[0].newInstance(uid, cl));
            vm.run();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
