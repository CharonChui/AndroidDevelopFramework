package com.charonchui.framework.config;

/**
 * Created by Administrator on 2015/10/22.
 */
public class FrameworkBuildConfig {

    /**
     * If is in the debug mode.
     */
    private static boolean debug = true;

    public static void setDebugMode(boolean debugMode) {
        debug = debugMode;
    }

    public static boolean isDebugMode() {
        return debug;
    }

}
