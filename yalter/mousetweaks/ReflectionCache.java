/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.util.ReportedException
 */
package yalter.mousetweaks;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

public class ReflectionCache {
    private HashMap<String, Class> classes = new HashMap();
    private HashMap<String, Method> methods = new HashMap();
    private HashMap<String, Field> fields = new HashMap();

    public boolean isInstance(Object obj, String name) {
        Class clazz = this.classes.get(name);
        return clazz.isInstance(obj);
    }

    public Object invokeMethod(Object obj, String name, Object ... args) throws InvocationTargetException {
        Method method = this.methods.get(name);
        try {
            return method.invoke(obj, args);
        }
        catch (IllegalAccessException e) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)e, (String)"Invoking method in MouseTweaks' reflection");
            throw new ReportedException(crashreport);
        }
    }

    public Object getFieldValue(Object obj, String name) {
        Field field = this.fields.get(name);
        try {
            return field.get(obj);
        }
        catch (IllegalAccessException e) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)e, (String)"Getting field value in MouseTweaks' reflection");
            throw new ReportedException(crashreport);
        }
    }

    public void setFieldValue(Object obj, String name, Object value) {
        Field field = this.fields.get(name);
        try {
            field.set(obj, value);
        }
        catch (IllegalAccessException e) {
            CrashReport crashreport = CrashReport.makeCrashReport((Throwable)e, (String)"Setting field value in MouseTweaks' reflection");
            throw new ReportedException(crashreport);
        }
    }

    void storeClass(String name, Class clazz) {
        this.classes.put(name, clazz);
    }

    void storeMethod(String name, Method method) {
        this.methods.put(name, method);
    }

    void storeField(String name, Field field) {
        this.fields.put(name, field);
    }
}

