package com.charonchui.framework.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApkUtils {

    private final static String TAG = "ApkUtils";

    // 已安装的所有应用
    private static final int APP_TYPE_ALL = 0;
    // 已安装的非系统应用
    private static final int APP_TYPE_NON_SYSTEM = 1;
    // 已安装的系统应用
    private static final int APP_TYPE_SYSTEM = 2;


    public static final int APK_INSTALLED = 0;
    public static final int APK_UNINSTALLED = -2;
    public static final int APK_UPGRADE = 1;
    public static final int APK_DOWNGRADE = -1;


    public static int getInstallState(PackageInfo info, int tagretVersionCode) {
        int ret;
        if (info == null) {
            ret = ApkUtils.APK_UNINSTALLED;
        } else if (info.versionCode == tagretVersionCode) {
            ret = ApkUtils.APK_INSTALLED;
        } else if (info.versionCode < tagretVersionCode) {
            ret = ApkUtils.APK_UPGRADE;
        } else
            ret = ApkUtils.APK_DOWNGRADE;
        return ret;
    }

    public static PackageInfo getInstalledApp(Context context, PackageManager pm, String pname) {
        PackageInfo info = null;

        pm = context.getPackageManager();
        try {
            info = pm.getPackageInfo(pname, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return info;
    }

    public static String getInstalledAppNameByPackageInfo(Context context, PackageManager pm,
                                                          PackageInfo info) {
        if (pm == null) {
            pm = context.getPackageManager();
        }
        return pm.getApplicationLabel(info.applicationInfo).toString();
    }

    public static List<PackageInfo> getInstalledApps(Context context) {
        PackageManager pm = context.getPackageManager();
        return getInstalledApps(context, pm);
    }

    public static List<PackageInfo> getInstalledApps(Context context, PackageManager pm) {
        return getAllApps(context, pm, APP_TYPE_ALL);
    }

    public static List<PackageInfo> getInstalledNonSystemApps(Context context, PackageManager pm) {
        return getAllApps(context, pm, APP_TYPE_NON_SYSTEM);
    }

    public static List<PackageInfo> getInstalledSystemApps(Context context, PackageManager pm) {
        return getAllApps(context, pm, APP_TYPE_SYSTEM);
    }

    private static List<PackageInfo> getAllApps(Context context, PackageManager pm, int appType) {
        List<PackageInfo> pkgs = new ArrayList<>();

        List<PackageInfo> infos = pm
                .getInstalledPackages(PackageManager.GET_DISABLED_COMPONENTS);
        switch (appType) {
            case APP_TYPE_ALL:
                for (PackageInfo info : infos) {
                    pkgs.add(info);
                }
                break;

            case APP_TYPE_NON_SYSTEM:
                for (PackageInfo info : infos) {
                    if (!isSystemApp(info.applicationInfo)) {
                        pkgs.add(info);
                    }
                }
                break;

            case APP_TYPE_SYSTEM:
                for (PackageInfo info : infos) {
                    if (isSystemApp(info.applicationInfo)) {
                        pkgs.add(info);
                    }
                }
                break;
        }
        return pkgs;
    }

    public static List<String> getInstalledAppPkgs(Context context) {
        List<String> pkgs = new ArrayList<String>();

        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(0);
        for (PackageInfo pi : packages) {
            if (!ApkUtils.isSystemApp(pi.applicationInfo)) {
                pkgs.add(pi.packageName);
            }
        }

        return pkgs;
    }

    public static List<PackageInfo> getInstalledPackages(PackageManager pm, int flags) {
        List<PackageInfo> infos = pm.getInstalledPackages(flags);
        return infos;
    }


    public static boolean isApkInstalled(Context context, String packageName) {
        if (packageName == null) {
            return false;
        }
        if (packageName.equals(context.getPackageName())) {
            return true;
        }
        boolean result = false;

        try {
            result = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_DISABLED_COMPONENTS) != null;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean isApkInstalled(Context context, String packageName, String versionCode) {
        if (packageName == null) {
            return false;
        }
        if (packageName.equals(context.getPackageName())) {
            return true;
        }
        boolean result = false;

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_DISABLED_COMPONENTS);
            if (info == null) {
                return false;
            }
            result = String.valueOf(info.versionCode).equals(versionCode);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean isValidApk(String apkPath, Context context) {
        boolean bRet = false;
        try {
            if (FileUtil.isFileExist(apkPath)) {
                PackageManager pm = context.getPackageManager();
                PackageInfo pkInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_CONFIGURATIONS);
                bRet = pkInfo != null;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return bRet;
    }

    public static void uninstall(Context mContext, String packageName) {
        Intent intent;
        Uri uri;
        try {
            uri = Uri.fromParts("package", packageName, null);
            intent = new Intent(Intent.ACTION_DELETE, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static boolean openApp(final Context context, final String packageName) {
        Intent mIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            context.startActivity(mIntent);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSystemApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
            if (info.publicSourceDir.startsWith("data/dataapp") || info.publicSourceDir.startsWith("/data/dataapp")) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static String getAppSignatureMd5(Context context, String packname) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(packname,
                    PackageManager.GET_SIGNATURES);
            byte[] signature = info.signatures[0].toByteArray();
            String hash = Md5Utils.md5LowerCase(Arrays.toString(signature));
            return hash;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    private static String[] getPackageSignatures(Context context, String apkPath) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_SIGNATURES);
            if (info != null && info.signatures != null && info.signatures.length > 0) {
                byte[] signature = info.signatures[0].toByteArray();
                String hash = HashUtils.getHash(Arrays.toString(signature)).toLowerCase();
                String packageName = info.applicationInfo.packageName;
                return new String[]{hash, packageName};
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object parsePackage(String archiveFilePath, int flags) {
        // 这是一个Package 解释器, 是隐藏的
        try {
            Object packageParser;
            // 构造函数的参数只有一个, apk文件的路径
            if (Build.VERSION.SDK_INT >= 21) {//android 5.0系统改方法做了改变
                packageParser = ReflectUtils.getObjectConstructor(ReflectUtils.CLASSNAME_PAGEAGEPARSE)
                        .newInstance();
            } else {
                packageParser = ReflectUtils.getObjectConstructor(ReflectUtils.CLASSNAME_PAGEAGEPARSE, String.class)
                        .newInstance(archiveFilePath);
            }
            // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            final File sourceFile = new File(archiveFilePath);
            // 这里就是解析了, 四个参数,
            // 源文件File,
            // 目的文件路径(看Android安装器源码, 用的是源文件路径, 但名字却是destFileName)
            // 显示, DisplayMetrics metrics
            // flags, 这个标记类型，比如PackageManager.GET_SIGNATURES表示需要签名信息

            Object pkg;
            if (Build.VERSION.SDK_INT >= 21) {
                Method parsePackageMethod = packageParser.getClass().getMethod("parsePackage", File.class, int.class);
                pkg = parsePackageMethod.invoke(packageParser, sourceFile, flags);
            } else {
                Method parsePackageMethod = packageParser.getClass().getMethod("parsePackage", File.class, String.class, DisplayMetrics.class, int.class);
                pkg = parsePackageMethod.invoke(packageParser, sourceFile, archiveFilePath, metrics, flags);
            }
            if (pkg == null) {
                Log.d(TAG, "---parsePackage is null------;;sourceFile=" + sourceFile.getAbsolutePath());
                return null;
            }
            //这里只取出而不校验，如果要校验，第二个参数传0
            if (Build.VERSION.SDK_INT >= 21) {
                Method collectCertificates = packageParser.getClass().getDeclaredMethod("collectCertificates", ReflectUtils.classForName(ReflectUtils.CLASSNAME_PAGEAGEPARSE_PACKAGE), File.class, int.class);
                collectCertificates.setAccessible(true);
                collectCertificates.invoke(packageParser, pkg, sourceFile, 1);
            } else {
                Method collectCertificates = packageParser.getClass().getDeclaredMethod("collectCertificates", ReflectUtils.classForName(ReflectUtils.CLASSNAME_PAGEAGEPARSE_PACKAGE), int.class);
                collectCertificates.invoke(packageParser, pkg, 1);
            }
            return pkg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

        // 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
        // PackageInfo pkgInfo=null;
        // try {
        // pkgInfo=PackageParser.generatePackageInfo(pkg, null, flags,0,0);
        // } catch (Exception e) {
        // // TODO: handle exception
        // Log.e(TAG, "--generatePackageInfo error--");
        // return null;
        // }
        // return pkgInfo;
    }

    public static String[] getApkFileSignatureAndPackageName(Context context, String apkPath) {
        try {
            String[] packageSignatureAndName = getPackageSignatures(context, apkPath);
            if (packageSignatureAndName != null) {
                return packageSignatureAndName;
            }

            //这种方式在nexus 5 android 5.0上会卡住。
            Object pkg = null;
            try {
                //package太大，有可能导致oom
                pkg = parsePackage(apkPath, PackageManager.GET_SIGNATURES);
            } catch (OutOfMemoryError e) {
                pkg = null;
            }
            if (pkg == null) {
                return null;
            }
            Signature[] sigsApk = getApkSignature(pkg, apkPath);
            if (sigsApk != null && sigsApk.length > 0) {
                byte[] signature = sigsApk[0].toByteArray();
                String hash = HashUtils.getHash(Arrays.toString(signature)).toLowerCase();
                String packageName = (String) ReflectUtils.getObjectFieldNoDeclared(ReflectUtils.getField(pkg, "applicationInfo"), "packageName");

                return new String[]{hash, packageName};
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Signature[] getApkSignature(Object pkg, String apkPath) {

        Signature[] sigs = new Signature[0];
        try {
            sigs = (Signature[]) ReflectUtils.getField(pkg, "mSignatures");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (sigs == null) {
            return null;// 有一些系统应用获取不到签名信息（比如google电子市场），sigs会为空，所以做特别处理，以防报错
        }
        if (sigs.length <= 0) {
            return null;
        }
        return sigs;
    }

    public static String[] getApkFileSignatureAndPackageNameEx(Context context, String apkPath) {
        try {
            Object info2 = parsePackage(apkPath, PackageManager.GET_SIGNATURES);
            if (info2 != null) {
                Signature[] sig = (Signature[]) ReflectUtils.getField(info2, "mSignatures");
                if (sig == null || sig.length == 0) {
                    return null;
                }

                byte[] signature = sig[0].toByteArray();
                String hash = HashUtils.getHash(Arrays.toString(signature)).toLowerCase();
                String packageName = (String) ReflectUtils.getField(info2, "packageName");
                return new String[]{hash, packageName};
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }


    public static boolean isValidAppPackageName(String pkg) {// 检查包名是否合法
        String regex = "^[a-zA-Z_]\\w*(\\.[a-zA-Z_]\\w*)*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pkg);
        return m.matches();
    }


    public static String getApkFileLable(Context ctx, String path) {

        String PATH_PackageParser = "android.content.pm.PackageParser";
        String PATH_AssetManager = "android.content.res.AssetManager";
        try {
            String apkPath = path;

            Class pkgParserCls = Class.forName(PATH_PackageParser);
            Class[] typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Constructor pkgParserCt;
            if (Build.VERSION.SDK_INT >= 21) {
                pkgParserCt = pkgParserCls.getConstructor();
            } else {
                pkgParserCt = pkgParserCls.getConstructor(typeArgs);
            }

            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            Object pkgParser;
            if (Build.VERSION.SDK_INT >= 21) {
                pkgParser = pkgParserCt.newInstance();
            } else {
                pkgParser = pkgParserCt.newInstance(valueArgs);
            }

            Log.d("DownloadUtils", "pkgParser:" + pkgParser.toString());

            DisplayMetrics metrics = new DisplayMetrics();

            metrics.setToDefaults();

            Method pkgParser_parsePackageMtd;
            if (Build.VERSION.SDK_INT >= 21) {
                typeArgs = new Class[2];
                typeArgs[0] = File.class;
                typeArgs[1] = int.class;
            } else {
                typeArgs = new Class[4];
                typeArgs[0] = File.class;
                typeArgs[1] = String.class;
                typeArgs[2] = DisplayMetrics.class;
                typeArgs[3] = Integer.TYPE;
            }
            pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",
                    typeArgs);

            if (Build.VERSION.SDK_INT >= 21) {
                valueArgs = new Object[2];
                valueArgs[0] = new File(apkPath);
                valueArgs[1] = 0;
            } else {
                valueArgs = new Object[4];
                valueArgs[0] = new File(apkPath);
                valueArgs[1] = apkPath;
                valueArgs[2] = metrics;
                valueArgs[3] = 0;
            }

            Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);

            Field appInfoFld = pkgParserPkg.getClass().getDeclaredField("applicationInfo");

            ApplicationInfo info = (ApplicationInfo) appInfoFld.get(pkgParserPkg);

            Log.d("DownloadUtils", "pkg:" + info.packageName + " uid=" + info.uid);

            Class assetMagCls = Class.forName(PATH_AssetManager);

            Constructor assetMagCt = assetMagCls.getConstructor((Class[]) null);

            Object assetMag = assetMagCt.newInstance((Object[]) null);
            typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath",
                    typeArgs);
            valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
            Resources res = ctx.getResources();

            typeArgs = new Class[3];
            typeArgs[0] = assetMag.getClass();
            typeArgs[1] = res.getDisplayMetrics().getClass();
            typeArgs[2] = res.getConfiguration().getClass();

            Constructor resCt = Resources.class.getConstructor(typeArgs);

            valueArgs = new Object[3];
            valueArgs[0] = assetMag;
            valueArgs[1] = res.getDisplayMetrics();
            valueArgs[2] = res.getConfiguration();

            res = (Resources) resCt.newInstance(valueArgs);

            CharSequence label = null;
            if (info.labelRes != 0) {
                label = res.getText(info.labelRes);
            }

            Log.d("DownloadUtils", "label=" + label);
            return label.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-1";
    }

    public static final String[] coreApks = {"com.android.phone", "com.android.inputmethod.latin", "android", "com.android.bluetooth", "com.android.certinstaller",
            "com.android.sidekick", "com.google.android.gsf", "com.google.android.partnersetup", "com.android.htmlviewer",
            "com.android.wallpaper.livepicker", "com.android.stk", "com.android.providers.userdictionary", "com.android.packageinstaller",
            "com.android.providers.telocation", "com.android.email", "com.android.providers.telephony", "com.android.calculator2",
            "com.android.providers.contacts", "com.android.browser", "com.android.monitor", "com.android.soundrecorder", "com.android.providers.media",
            "com.android.launcher", "com.android.calendar", "com.android.providers.calendar", "com.android.defcontainer", "com.android.settings",
            "com.android.providers.settings", "com.android.deskclock", "com.android.providers.drm", "com.android.providers.applications",
            "com.android.contacts", "com.android.gallery", "com.google.android.location", "com.android.fileexplorer", "com.android.updater",
            "com.android.providers.downloads.ui", "com.android.providers.downloads", "com.android.mms", "com.android.server.vpn",
            "com.android.providers.subscribedfeeds", "com.android.thememanager", "com.android.systemui", "com.android.wallpaper", "com.google.android.gm",
            "com.google.android.backup", "com.google.android.syncadapters.calendar", "com.google.android.syncadapters.contacts",
            "com.android.vending.updater", "com.android.vending", "com.google.android.feedback", "com.google.android.street", "com.android.setupwizard",
            "com.google.android.googlequicksearchbox", "com.google.android.apps.uploader", "com.android.camera",
            "com.google.android.apps.genie.geniewidget", "com.android.music", "com.android.musicvis", "com.google.android.voicesearch",
            "com.noshufou.android.su", "com.qihoo.root", "com.lbe.security.miui", "com.lbe.security.su", "com.lbe.security.shuame", "eu.chainfire.supersu",
            "com.miui.uac", "com.android.protips"};

}
