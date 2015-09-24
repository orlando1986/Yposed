package com.catfish.yposed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class HookManager {
    private static final String TAG = "catfish";
    private static Class<?> sCallbackClass = null;
    private static Map<String, Method> sMethodCache = new HashMap<String, Method>();

    static {
        System.loadLibrary("hook");

        String lib = "";
        try {
            Class<?> properties = Class.forName("android.os.SystemProperties");
            Method get = properties.getDeclaredMethod("get", String.class, String.class);
            lib = (String) get.invoke(null, "persist.sys.dalvik.vm.lib", "");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.toString());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.toString());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.toString());
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.toString());
        }

        int version = -1;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            // dalvik vm
        } else if (android.os.Build.VERSION.RELEASE.startsWith("4.4")
                && "libart.so".equals(lib)) {
            version = 0;
        } else if (android.os.Build.VERSION.RELEASE.startsWith("5.0")) {
            version = 1;
        } else if (android.os.Build.VERSION.RELEASE.startsWith("5.1")) {
            version = 2;
        }
        initVM(version);
    }

    public static void registerCallbackClass(Class<?> callback) {
        if (sCallbackClass != null) {
            throw new RuntimeException("CallbackClass has been registered");
        }
        sCallbackClass = callback;
    }

    public static void replaceMethod(Method origin, String proxy) {
        if (sCallbackClass == null) {
            throw new NullPointerException("CallbackClass hasn't been registered yet");
        }

        Method[] ms = sCallbackClass.getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().equals(proxy)) {
                if (sMethodCache.get(proxy) != null) {
                    throw new IllegalArgumentException("hook " + proxy + " duplicated");
                }
                sMethodCache.put(proxy, m);
                hookYposedMethod(origin, m, Modifier.isStatic(origin.getModifiers()));
                return;
            }
        }
        throw new IllegalArgumentException("didn't find " + proxy + " in " + sCallbackClass);
    }

    public static Object invokeOriginVirtual(String methodName, Object receiver, Object... args) {
        Method m = sMethodCache.get(methodName);
        if (methodName == null) {
            throw new RuntimeException(methodName + " has not been used to hook, please verify");
        }
        try {
            m.setAccessible(true);
            return m.invoke(receiver, args);
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.toString());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.toString());
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public static Object invokeOriginStatic(String methodName, Object... args) {
        Method m = sMethodCache.get(methodName);
        if (methodName == null) {
            throw new RuntimeException(methodName + " has not been used to hook, please verify");
        }
        try {
            m.setAccessible(true);
            return m.invoke(null, args);
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.toString());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.toString());
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    public static Object retrieveReceiver(Object thiz, boolean isStatic) {
        if (isStatic) {
            return null;
        }
        return (Object) thiz;
    }

    private static native void hookYposedMethod(Method origin, Method proxy, boolean isStatic);

    private static native void initVM(int version);
}
