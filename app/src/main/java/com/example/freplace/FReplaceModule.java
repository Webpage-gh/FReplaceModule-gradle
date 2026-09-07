package com.example.freplace;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FReplaceModule implements IXposedHookLoadPackage {
    private static final String PREF_KEY = "replace_text";
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.hookAllMethods(Activity.class, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                String replace = prefs.getString(PREF_KEY, "FFF");
                replaceAllText(activity.getWindow().getDecorView(), replace);
            }
        });
    }

    private void replaceAllText(View view, String replace) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                replaceAllText(group.getChildAt(i), replace);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setText(replace);
        }
    }
}
