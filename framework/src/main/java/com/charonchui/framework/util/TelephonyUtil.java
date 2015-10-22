package com.charonchui.framework.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class TelephonyUtil {

    public static String getIMEI(Context context) {
        if (context == null) {
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;

    }

    public static String getPhoneNumber(Context context) {
        if (context == null) {
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getLine1Number();
        return imei;

    }
}
