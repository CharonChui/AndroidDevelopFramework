package com.charonchui.framework.util;

import android.util.Log;

import com.charonchui.framework.config.FrameworkBuildConfig;


/**
 * Utility to print logs.
 * This is controlled by FrameworkBuildConfig.isDebugMode(),
 * you should change the debug mode to let it print logs or not.
 *
 * @author CharonChui
 */
public class LogUtil {
    /**
     * If print log here.
     */
    private static int LOG_LEVEL = FrameworkBuildConfig.isDebugMode() ? 6 : 1;

    private static final int VERBOSE = 5;
    private static final int DEBUG = 4;
    private static final int INFO = 3;
    private static final int WARN = 2;
    private static final int ERROR = 1;

    public static void v(String tag, String msg) {
        if (LOG_LEVEL > VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_LEVEL > DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LOG_LEVEL > INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVEL > WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_LEVEL > ERROR) {
            Log.e(tag, msg);
        }
    }
}
