package com.rokid.voicerec;

import java.util.List;
import java.util.Iterator;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class ActionToComponent {
	public static Intent newIntent(Context ctx, String action) {
		PackageManager pm = ctx.getPackageManager();
		Intent intent = new Intent();
		intent.setAction(action);
		List<ResolveInfo> resolveInfos = pm.queryIntentServices(intent, 0);
		if (resolveInfos == null || resolveInfos.size() == 0) {
			Log.w(TAG, "no match component found for action " + action);
			return null;
		}
		if (resolveInfos.size() > 1) {
			Log.w(TAG, "more than one components match for action " + action);
			traceResolveInfoList(resolveInfos);
			return null;
		}
		ResolveInfo info = resolveInfos.get(0);
		ComponentName cname = new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name);
		intent = new Intent();
		intent.setComponent(cname);
		return intent;
	}

	private static void traceResolveInfoList(List<ResolveInfo> infos) {
		Iterator<ResolveInfo> it = infos.iterator();
		ResolveInfo info;
		while (it.hasNext()) {
			info = it.next();
			Log.i(TAG, info.toString());
		}
	}

	private static final String TAG = "BearKidUtil.ActionToComponent";
}
