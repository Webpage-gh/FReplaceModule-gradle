package com.example.freplace

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class FReplaceModule : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        // Hook on Activity's onResume to modify UI after it's inflated
        XposedBridge.hookAllMethods(Activity::class.java, "onResume", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                val activity = param?.thisObject as? Activity ?: return
                replaceAllText(activity.window.decorView)
            }
        })
    }

    private fun replaceAllText(view: View) {
        when (view) {
            is ViewGroup -> {
                for (i in 0 until view.childCount) {
                    replaceAllText(view.getChildAt(i))
                }
            }
            is TextView -> {
                view.text = "FFF"
            }
        }
    }
}
