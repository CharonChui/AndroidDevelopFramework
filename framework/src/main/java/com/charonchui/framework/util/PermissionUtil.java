package com.charonchui.framework.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 在需要检查权限的地方调用checkPermissions()方法，然后重写Activity(Fragment).onRequestPermissionsResult
 * 并在该方法中调用本来的onRequestPermissionsResult。
 */
public class PermissionUtil {
    /**
     * 在调用需要权限的功能时使用该方法进行检查。
     *
     * @param activity
     * @param requestCode
     * @param iPermission
     * @param permissions
     */
    public static void checkPermissions(Activity activity, int requestCode, IPermission iPermission, String... permissions) {
        handleRequestPermissions(activity, requestCode, iPermission, permissions);
    }

    public static void checkPermissions(Fragment fragment, int requestCode, IPermission iPermission, String... permissions) {
        handleRequestPermissions(fragment, requestCode, iPermission, permissions);
    }

    public static void checkPermissions(android.app.Fragment fragment, int requestCode, IPermission iPermission, String... permissions) {
        handleRequestPermissions(fragment, requestCode, iPermission, permissions);
    }

    /**
     * 在Actvitiy或者Fragment中重写onRequestPermissionsResult方法后调用该方法。
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param iPermission
     */
    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,
                                                  int[] grantResults, IPermission iPermission) {
        requestResult(activity, requestCode, permissions, grantResults, iPermission);

    }

    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions,
                                                  int[] grantResults, IPermission iPermission) {
        requestResult(fragment, requestCode, permissions, grantResults, iPermission);
    }

    public static void onRequestPermissionsResult(android.app.Fragment fragment, int requestCode, String[] permissions,
                                                  int[] grantResults, IPermission iPermission) {
        requestResult(fragment, requestCode, permissions, grantResults, iPermission);
    }

    public static <T> void requestPermission(T t, int requestCode, String... permission) {
        List<String> permissions = new ArrayList<>();
        for (String s : permission) {
            permissions.add(s);
        }
        requestPermissions(t, requestCode, permissions);
    }

    /**
     * 在检查权限后自己处理权限说明的逻辑后调用该方法，直接申请权限。
     *
     * @param t
     * @param requestCode
     * @param permissions
     * @param <T>
     */
    public static <T> void requestPermission(T t, int requestCode, List<String> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return;
        }
        requestPermissions(t, requestCode, permissions);
    }

    public static boolean checkSelfPermission(Context context, String permission) {
        if (context == null || TextUtils.isEmpty(permission)) {
            throw new IllegalArgumentException("invalidate params: the params is null !");
        }

        context = context.getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (PackageManager.PERMISSION_DENIED == result) {
                return false;
            }
        }

        return true;
    }

    private static <T> void handleRequestPermissions(T t, int requestCode, IPermission iPermission, String... permissions) {
        if (t == null || permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("invalidate params");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Activity activity = getActivity(t);
            List<String> deniedPermissions = getDeniedPermissions(activity, permissions);
            if (deniedPermissions != null && deniedPermissions.size() > 0) {

                List<String> rationalPermissions = new ArrayList<>();
                for (String deniedPermission : deniedPermissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            deniedPermission)) {
                        rationalPermissions.add(deniedPermission);
                    }
                }

                boolean showRational = false;
                if (iPermission != null) {
                    showRational = iPermission.showRational(requestCode);
                }

                if (rationalPermissions.size() > 0 && showRational) {
                    if (iPermission != null) {
                        iPermission.onRational(requestCode, deniedPermissions);
                    }
                } else {
                    requestPermissions(t, requestCode, deniedPermissions);
                }
            } else {
                if (iPermission != null) {
                    iPermission.onGranted(requestCode);
                }
            }
        } else {
            if (iPermission != null) {
                iPermission.onGranted(requestCode);
            }
        }
    }

    @Nullable
    private static <T> Activity getActivity(T t) {
        Activity activity = null;
        if (t instanceof Activity) {
            activity = (Activity) t;
        } else if (t instanceof Fragment) {
            activity = ((Fragment) t).getActivity();
        } else if (t instanceof android.app.Fragment) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                activity = ((android.app.Fragment) t).getActivity();
            }
        }
        return activity;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static <T> void requestPermissions(T t, int requestCode, List<String> deniedPermissions) {
        if (deniedPermissions == null || deniedPermissions.size() == 0) {
            return;
        }
        // has denied permissions
        if (t instanceof Activity) {
            ((Activity) t).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
        } else if (t instanceof Fragment) {
            ((Fragment) t).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
        } else if (t instanceof android.app.Fragment) {
            ((android.app.Fragment) t).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
        }
    }

    private static List<String> getDeniedPermissions(Context context, String... permissions) {
        if (context == null || permissions == null || permissions.length == 0) {
            return null;
        }
        List<String> denyPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkSelfPermission(context, permission)) {
                denyPermissions.add(permission);
            }
        }
        return denyPermissions;
    }


    private static <T> void requestResult(T t, int requestCode, String[] permissions,
                                          int[] grantResults, IPermission iPermission) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.size() > 0) {
            if (iPermission != null) {
                iPermission.onDenied(requestCode);
            }
        } else {
            if (iPermission != null) {
                iPermission.onGranted(requestCode);
            }
        }
    }

}

