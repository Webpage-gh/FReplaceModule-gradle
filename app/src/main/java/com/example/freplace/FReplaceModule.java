package com.example.freplace;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FReplaceModule implements IXposedHookLoadPackage {
    private static final String PREF_KEY = "replace_text";
    private static final String PREFS_NAME = "freplace_prefs";
    private static final String MODULE_PACKAGE = "com.example.freplace";
    private XSharedPreferences xsp;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 初始化 XSharedPreferences，使用正确的 SharedPreferences 名称
        xsp = new XSharedPreferences(MODULE_PACKAGE, PREFS_NAME);
        xsp.makeWorldReadable();

        // 避免 Hook 自身应用
        if (lpparam.packageName.equals(MODULE_PACKAGE)) {
            return;
        }

        // Hook onCreate 用于首次打开时的文本替换
        XposedBridge.hookAllMethods(Activity.class, "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                
                // 再次检查避免 Hook 自身应用的 Activity
                if (activity.getPackageName().equals(MODULE_PACKAGE)) {
                    return;
                }

                // 使用 XSharedPreferences 读取配置
                xsp.reload();
                String replace = xsp.getString(PREF_KEY, "FFF");
                // 使用 post 延迟执行，确保 contentView 已加载
                activity.getWindow().getDecorView().post(() -> 
                    replaceAllText(activity.getWindow().getDecorView(), replace));
            }
        });

        // Hook onResume 用于返回页面时的刷新
        XposedBridge.hookAllMethods(Activity.class, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity) param.thisObject;
                
                // 再次检查避免 Hook 自身应用的 Activity
                if (activity.getPackageName().equals(MODULE_PACKAGE)) {
                    return;
                }

                // 使用 XSharedPreferences 读取配置
                xsp.reload();
                String replace = xsp.getString(PREF_KEY, "FFF");
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
