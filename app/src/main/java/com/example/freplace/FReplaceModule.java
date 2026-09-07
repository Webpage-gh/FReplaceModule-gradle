package com.example.freplace;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FReplaceModule implements IXposedHookLoadPackage {
    private static final String PREF_KEY = "replace_text";
    private static final String MODULE_PACKAGE = "com.example.freplace";
    private static final Uri CONTENT_URI = Uri.parse("content://com.example.freplace.provider/config");

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
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

                // 通过 ContentProvider 读取配置
                String replace = loadConfigFromProvider(activity);
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

                // 通过 ContentProvider 读取配置
                String replace = loadConfigFromProvider(activity);
                replaceAllText(activity.getWindow().getDecorView(), replace);
            }
        });
    }
    
    private String loadConfigFromProvider(Activity activity) {
        try {
            ContentResolver resolver = activity.getContentResolver();
            Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int valueIndex = cursor.getColumnIndex("value");
                if (valueIndex != -1) {
                    String value = cursor.getString(valueIndex);
                    cursor.close();
                    return value;
                }
                cursor.close();
            }
        } catch (Exception e) {
            XposedBridge.log("FReplaceModule: Failed to load config from ContentProvider: " + e.getMessage());
        }
        return "FFF"; // 默认值
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
