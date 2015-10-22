package com.charonchui.framework.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * 网络连接的一些工具类
 */
public class NetworkUtil {

	/**
	 * 判断当前网络是否可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
	}

	/**
	 * 判断当前网络是否是手机网络
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isMobile(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	/**
	 * 判断WIFI是否使用
	 */
	public static boolean isWIFIActivate(Context context) {
		return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
				.isWifiEnabled();
	}

	/**
	 * 修改WIFI状态
	 * 
	 * @param status
	 *            true为开启WIFI，false为关闭WIFI
	 */
//	public static void changeWIFIStatus(Context context, boolean status) {
//		((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
//				.setWifiEnabled(status);
//	}
}
