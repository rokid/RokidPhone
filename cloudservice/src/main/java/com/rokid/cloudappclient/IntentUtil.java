package com.rokid.cloudappclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.rokid.logger.Logger;

import java.util.List;

/**
 * Created by fanfeng on 2017/10/23.
 */

public class IntentUtil {

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent,0);
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        ResolveInfo searchInfo = resolveInfo.get(0);
        String packageName = searchInfo.serviceInfo.packageName;
        String className = searchInfo.serviceInfo.name;
        Logger.d(" packageName : " + packageName + " className : " + className);
        ComponentName component = new ComponentName(packageName, className);

        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}
