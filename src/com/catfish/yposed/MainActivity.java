package com.catfish.yposed;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import dalvik.system.DexClassLoader;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transferFiles("hook.dex");
        transferFiles("libhook.so");
        loadDex();
    }

    private void loadDex() {
        String path = getFilesDir().toString();
        String dex =  path + "/hook.dex";
        String lib =  path + "/libhook.so";
        DexClassLoader cl = new DexClassLoader(dex, getCacheDir().toString(), lib, ClassLoader.getSystemClassLoader());
        try {
            Class<?> hm = Class.forName("com.catfish.HookManager", false, cl);
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

    private final void transferFiles(String filename) {
        AssetManager assetManager = getAssets();
        try {
            String path = getFilesDir() + "/";
            File file = new File(path + filename);
            File data = new File(getPackageCodePath());
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
