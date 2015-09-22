package com.catfish.yposed.ui;

import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
            m = getClass().getDeclaredMethod("victim", (Class[])null);
            HookManager.replaceMethod(m, "victim1");
        } catch (NoSuchMethodException e) {
            Log.d(TAG, e.toString());
        }
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick");
        victim();
    }

    private void victim() {
        Log.d(TAG, "victim");
    }
}
