package com.catfish.yposed.ui;

import android.util.Log;

import com.catfish.yposed.HookManager;

public class HookCallbacks {
    private static final String TAG = "catfish";

    public long victim1(int a, long b, char c) {
        Object receiver = HookManager.retrieveReceiver(this, false);
        Log.e(TAG, "hook victim called: " + receiver + ", a=" + a + ", b=" + b + ", c=" + c);
        System.gc();
        return (Long) HookManager.invokeOriginVirtual("victim1", receiver, a, b, c);
    }
}
