package com.charonchui.framework.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.List;

public class ServiceUtil {

	/**
	 * Check if the sevice is in running mode.
	 * 
	 * @param context
	 * @param cls
	 * @return
	 */
	public static boolean isServiceRunning(Context context, Class<?> cls) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = activityManager
				.getRunningServices(1024);
		for (RunningServiceInfo service : runningServices) {
			if (cls.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
