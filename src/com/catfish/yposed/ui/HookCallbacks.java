package com.catfish.yposed.ui;

import com.catfish.yposed.HookManager;

import android.util.Log;

public class HookCallbacks {
    private static final String TAG = "catfish";

    public void victim1(Object receiver) {
        Log.e(TAG, "hook victim called: " + receiver);
        System.gc();
//        HookManager.invokeOriginVirtual("victim1", receiver, (Class[]) null);
    }
}
