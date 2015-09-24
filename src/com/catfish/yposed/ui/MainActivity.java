package com.catfish.yposed.ui;

import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.catfish.yposed.HookCallbacks;
import com.catfish.yposed.HookManager;
import com.catfish.yposed.R;

public class MainActivity extends Activity {
    private static final String TAG = "catfish";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HookManager.registerCallbackClass(HookCallbacks.class);
        Method m;
        try {
            m = getClass().getDeclaredMethod("victim", int.class, long.class, char.class);
            HookManager.replaceMethod(m, "victim");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, e.toString());
        }
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick");
        Log.d(TAG, "get: " + victim(1, 123456789098765432l, 'x'));
    }

    private long victim(int a, long b, char c) {
        Log.d(TAG, "victim");
        return b;
    }

}