package com.catfish.yposed;

import android.util.Log;

public class HookCallbacks {
    private static final String TAG = "catfish";

    public long victim(int a, long b, char c) {
        Object receiver = HookManager.retrieveReceiver(this, false);
        Log.e(TAG, "hook victim called: " + receiver + ", a=" + a + ", b=" + b + ", c=" + c);
        safe();
        return (Long) HookManager.invokeOriginVirtual("victim", receiver, a, b, c);
    }

    private void safe() {
        Log.e(TAG, "safe", new Exception());
        System.gc();
    }
}
