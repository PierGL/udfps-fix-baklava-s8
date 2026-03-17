package com.example.udfpsinjector;

import android.content.res.Resources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("android")) return;

        // Hook per le coordinate: X=876, Y=2100
        XposedHelpers.findAndHookMethod(Resources.class, "getIntArray", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                int id = (Integer) param.args[0];
                String name = ((Resources) param.thisObject).getResourceEntryName(id);
                if ("config_udfps_sensor_props".equals(name)) {
                    param.setResult(new int[]{876, 2100, 110});
                }
            }
        });

        // Fix Anti-Black Screen (SDK 36 Compliant)
        XposedHelpers.findAndHookMethod(Resources.class, "getBoolean", int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                int id = (Integer) param.args[0];
                String name = ((Resources) param.thisObject).getResourceEntryName(id);
                if ("config_udfps_supports_hbm_control".equals(name)) {
                    param.setResult(false);
                }
            }
        });
    }
}
