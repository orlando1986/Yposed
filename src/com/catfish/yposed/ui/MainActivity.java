package com.catfish.yposed.ui;

import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.catfish.yposed.HookCallbacks;
import com.catfish.yposed.HookManager;
import com.catfish.yposed.R;

public class MainActivity extends Activity {
    private static final String TAG = "catfish";
    private static boolean isHooked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isHooked) {
            isHooked = true;
            HookManager.registerCallbackClass(HookCallbacks.class);
            try {
                Method m = getClass().getDeclaredMethod("victim", int.class, long.class, char.class);
                HookManager.replaceMethod(m, "victim");
            } catch (NoSuchMethodException e) {
                Log.e(TAG, e.toString());
            }
        }

        ((TextView) findViewById(R.id.text)).setText(victim(1, 1234567890987654321l, 'c'));
    }

    private String victim(int a, long b, char c) {
        Log.d(TAG, "victim: " + this);
        return "test";
    }

}