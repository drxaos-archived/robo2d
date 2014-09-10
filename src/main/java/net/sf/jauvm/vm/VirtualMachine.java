/**
 * Copyright (c) 2005 Nuno Cruces
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

package net.sf.jauvm.vm;

import net.sf.jauvm.vm.insn.Insn;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class VirtualMachine implements Serializable {
    private int cp;
    private Frame frame;
    private Insn[] insns;
    private ExcptHandler[] excpts;
    private StackTraceElement[] trace;
    private transient String fullState = "";

    VirtualMachine() {
    }

    public static VirtualMachine create(Class<? extends Runnable> cls) throws Throwable {
        if (cls == null) throw new NullPointerException();
        Method method = cls.getMethod("run");
        MethodCode code = GlobalCodeCache.get(cls, "run()V");
        return new VirtualMachine(new Throwable().getStackTrace(), method, code, cls.newInstance());
    }

    public static VirtualMachine create(final Runnable run) throws Throwable {
        if (run == null) throw new NullPointerException();
        Class<?> cls = run.getClass();
        Method method = cls.getMethod("run");
        MethodCode code = GlobalCodeCache.get(cls, "run()V");
        return new VirtualMachine(new Throwable().getStackTrace(), method, code, run);
    }

    public static VirtualMachine create(InputStream in) throws Throwable {
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        if (o instanceof VirtualMachine) {
            return (VirtualMachine) o;
        } else {
            throw new IOException("object class is [" + o.getClass().getCanonicalName() + "]");
        }
    }

    public VirtualMachine(StackTraceElement[] trace, Method method, MethodCode code, Object... params) {
        this.setFrame(Frame.newBootstrapFrame(method, code, params));
        this.trace = trace;
    }

    public String getFullState() {
        return fullState;
    }

    public void run() throws Throwable {
        run(-1);
    }

    public boolean run(long cycles) throws Throwable {
        while (frame != null) {
            try {
                synchronized (this) {
                    Insn insn = insns[cp++];

                    insn.execute(this);

                    try {
                        ByteArrayOutputStream s = new ByteArrayOutputStream();
                        save(s);
                        s.close();
                        fullState = s.toString();
                    } catch (Exception e) {
                        // ?
                    }
                }
                if (cycles > 0) {
                    cycles--;
                }
                if (cycles == 0) {
                    return false;
                }
            } catch (StackTracedException e) {
                Throwable t = e.getCause();
                fillInStackTrace(t, t.getStackTrace());
                if (!findHandler(t)) throw t;
            } catch (Throwable t) {
                fillInStackTrace(t);
                if (!findHandler(t)) throw t;
            }
        }
        return true;
    }

    public void save(OutputStream out) throws IOException {
        synchronized (this) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(this);
            } catch (NotSerializableException e) {
                throw new VirtualMachineException("Instance of illegal class [" + e.getMessage() + "] at " + getPointer(), e);
            }
        }
    }

    public StackTraceElement getPointer() {
        if (frame.getMethod() != null) {
            Method m = frame.getMethod();
            MethodCode c = frame.getCode();
            return new StackTraceElement(m.getDeclaringClass().getName(), m.getName(),
                    c.source, LineNumber.getLine(c.lines, cp));
        } else {
            Constructor m = frame.getConstructor();
            MethodCode c = frame.getCode();
            return new StackTraceElement(m.getDeclaringClass().getName(), "<init>",
                    c.source, LineNumber.getLine(c.lines, cp));
        }
    }

    private boolean findHandler(Throwable thrwbl) {
        while (frame != null) {
            for (ExcptHandler excpt : excpts) {
                if (excpt.start < cp && cp <= excpt.end && (excpt.cls == null || excpt.cls.get().isInstance(thrwbl))) {
                    cp = excpt.handler;
                    frame = frame.getMutableCopy();
                    frame.popAll();
                    frame.pushObject(thrwbl);
                    return true;
                }
            }
            setCp(frame.getRet());
            setFrame(frame.getParent());
        }
        return false;
    }

    private void fillInStackTrace(Throwable thrwbl, StackTraceElement... st) {
        List<StackTraceElement> lst = new ArrayList<StackTraceElement>();
        lst.addAll(Arrays.asList(st));
        int cp = this.cp;
        Frame frame = this.frame;
        while (frame != null) {
            MethodCode c = frame.getCode();
            if (frame.getMethod() != null) {
                Method m = frame.getMethod();
                lst.add(new StackTraceElement(m.getDeclaringClass().getName(), m.getName(),
                        c.source, LineNumber.getLine(c.lines, cp)));
            } else {
                Constructor m = frame.getConstructor();
                lst.add(new StackTraceElement(m.getDeclaringClass().getName(), m.getName(),
                        c.source, LineNumber.getLine(c.lines, cp)));
            }
            cp = frame.getRet();
            frame = frame.getParent();
        }
        lst.addAll(Arrays.asList(trace));
        thrwbl.setStackTrace(lst.toArray(trace));
    }


    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
        if (frame == null) return;
        insns = frame.getCode().insns;
        excpts = frame.getCode().excpts;
    }

    public Insn getInsn() {
        return insns[cp];
    }
}
