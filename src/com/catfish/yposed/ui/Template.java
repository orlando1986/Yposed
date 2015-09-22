package com.catfish.yposed.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class Template {
    private Template(Context context) {
        transferFiles(context, "hook.dex");
        transferFiles(context, "libhook.so");
        loadDex(context);
    }

    private void loadDex(Context context) {
        String path = context.getFilesDir().toString();
        String dex = path + "/hook.dex";
        String lib = path + "/libhook.so";
        DexClassLoader cl = new DexClassLoader(dex, context.getCacheDir().toString(), lib, ClassLoader.getSystemClassLoader());
        try {
            Class<?> hm = Class.forName("com.catfish.yposed.HookManager", false, cl);
            Method m = hm.getDeclaredMethod("start", (Class[]) null);
            m.invoke(null);
        } catch (ClassNotFoundException e) {
            Log.e("catfish", e.toString(), e);
        } catch (NoSuchMethodException e) {
            Log.e("catfish", e.toString(), e);
        } catch (IllegalAccessException e) {
            Log.e("catfish", e.toString(), e);
        } catch (IllegalArgumentException e) {
            Log.e("catfish", e.toString(), e);
        } catch (InvocationTargetException e) {
            Log.e("catfish", e.toString(), e);
        }
    }

    private final void transferFiles(Context context, String filename) {
        AssetManager assetManager = context.getAssets();
        try {
            String path = context.getFilesDir() + "/";
            File file = new File(path + filename);
            File data = new File(context.getPackageCodePath());
            if (file.exists() && (file.lastModified() > data.lastModified())) {
                return;
            }
            FileOutputStream fos = new FileOutputStream(path + filename);
            InputStream inputStream = assetManager.open(filename);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
            fos.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("catfish", "transfer files failed", e);
        } catch (Exception e) {
            Log.e("catfish", "transfer files failed", e);
        }
    }
}
