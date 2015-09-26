package com.catfish.yposed;

import android.util.Log;

public class HookCallbacks {
    private static final String TAG = "catfish";

    public String victim(int a, long b, char c) {
        Object receiver = HookManager.retrieveReceiver(this, false);
        Log.e(TAG, "hook victim called: " + receiver + ", a=" + a + ", b=" + b + ", c=" + c);
        safe();
        String result = (String) HookManager.invokeOrigin("victim", receiver, a, b, c);
        return result + " SUCCESS";
    }

    private void safe() {
        Log.e(TAG, "safe");
        System.gc();
    }
}
