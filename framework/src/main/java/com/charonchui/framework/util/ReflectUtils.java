package com.charonchui.framework.util;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static final String CLASSNAME_ICONTENTPROVIDER = "android.content.IContentProvider";
    public static final String CLASSNAME_IMOUNTSERVICE_STUB = "android.os.storage.IMountService$Stub";
    public static final String CLASSNAME_PAGEAGEPARSE_PACKAGE = "android.content.pm.PackageParser$Package";
    public static final String CLASSNAME_PAGEAGEPARSE = "android.content.pm.PackageParser";
    public static final String CLASSNAME_THUMBNAILUTILS = "android.media.ThumbnailUtils";

    public static final String CLASSNAME_THREADS = "android.provider.Telephony$Threads";
    public static final String CLASSNAME_IPACKAGESTATSOBSERVER = "android.content.pm.IPackageStatsObserver";
    public static final String CLASSNAME_IPACKAGEDATAOBSERVER = "android.content.pm.IPackageDataObserver";
    public static final String CLASSNAME_IPACKAGEINSTALLOBERVER = "android.content.pm.IPackageInstallObserver";

    public static final String CLASSNAME_AUDIOSYSTEM = "android.media.AudioSystem";

    public static final String CLASSNAME_PACKAGEMANAGER = "android.content.pm.PackageManager";
    public static final String CLASSNAME_IPACKAGEMANAGER_STUB = "android.content.pm.IPackageManager$Stub";
    public static final String CLASSNAME_APPLICATIONINFO = "android.content.pm.ApplicationInfo";
    public static final String CLASSNAME_PACKAGEINFO = "android.content.pm.PackageInfo";

    public static final String CLASSNAME_IPACKAGEMANAGER = "android.content.pm.IPackageManager";
    public static final String CLASSNAME_IPACKAGEDELETEOBSERVER = "android.content.pm.IPackageDeleteObserver";
    public static final String CLASSNAME_PROCESS = "android.os.Process";

    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     *
     * @param object         : 子类对象
     * @param methodName     : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @return 父类中的方法对象
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected, default)
     *
     * @param object         : 子类对象
     * @param methodName     : 父类中的方法名
     * @param parameterTypes : 父类中的方法参数类型
     * @param parameters     : 父类中的方法参数
     * @return 父类中方法的执行结果
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        //根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象
        Method method = getDeclaredMethod(object, methodName, parameterTypes);

        //抑制Java对方法进行检查,主要是针对私有方法而言
        method.setAccessible(true);
        try {
            if (null != method) {
                //调用object 的 method 所代表的方法，其方法的参数是 parameters
                return method.invoke(object, parameters);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @return 父类中的属性对象
     */
    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @param value     : 将要设置的值
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        //抑制Java对其的检查
        field.setAccessible(true);
        try {
            //将 object 中 field 所代表的值 设置为 value
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @return : 父类中的属性值
     */

    public static Object getFieldValue(Object object, String fieldName) {
        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象
        Field field = getDeclaredField(object, fieldName);
        //抑制Java对其的检查
        field.setAccessible(true);
        try {
            //获取 object 中 field 所代表的属性值
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class classForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(className);
        }
    }

    public static Object stubAsInterface(String clazz, IBinder binder) {
        return stubAsInterface(classForName(clazz), binder);
    }

    public static Object stubAsInterface(Class clazz, IBinder binder) {
        try {
            return clazz.getDeclaredMethod("asInterface", IBinder.class)
                    .invoke(null, binder);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method getMethod(String calssName, String methodName, Class<?>... paremerters) {
        Method method = null;
        try {
            Class<?> mClass = Class.forName(calssName);
            method = mClass.getDeclaredMethod(methodName, paremerters);
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    public static Method getMethod(Class<?> cls, String methodName, Class<?> paramTypes[]) {
        Method method = null;
        if (methodName != null && methodName.length() > 0) {
            try {
                method = cls.getMethod(methodName, paramTypes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return method;
    }

    public static Object invoke(Object receiver, Method method, Object... paremeters) {
        Object result = null;
        try {
            result = method.invoke(receiver, paremeters);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getSystemProperties(String propertyName, String defaultValue) {
        String result = defaultValue;
        try {
            Class<?> className = Class.forName("android.os.SystemProperties");
            Method method = className.getDeclaredMethod("get", String.class, String.class);
            result = (String) method.invoke(null, propertyName, defaultValue);
        } catch (ClassNotFoundException | SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Context getApplicationContext() {
        Context context = null;
        try {
            Class<?> clazz = Class.forName("android.app.ActivityThread");
            Method method = clazz.getDeclaredMethod("currentApplication", new Class<?>[]{});
            context = (Context) method.invoke(null, new Object[]{});
        } catch (ClassNotFoundException | SecurityException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return context;
    }

    public static Constructor getObjectConstructor(String className, Class... paramsTypes) {
        try {
            return Class.forName(className).getConstructor(paramsTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return prepareField(obj.getClass(), fieldName).get(obj);
    }

    public static void setField(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        prepareField(obj.getClass(), fieldName).set(obj, value);
    }

    public static int getIntField(Object object, String fieldName) {
        try {
            return object.getClass()
                    .getDeclaredField(fieldName)
                    .getInt(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getObjectField(Object object, String fieldName) {
        try {
            return object.getClass()
                    .getDeclaredField(fieldName)
                    .get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getObjectFieldNoDeclared(Object object, String fieldName) {
        try {
            return object.getClass()
                    .getField(fieldName)
                    .get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    // Hide Constants Helper
    public static int getStaticIntField(String className, String fieldName) {
        try {
            return Class.forName(className)
                    .getDeclaredField(fieldName)
                    .getInt(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getStaticObjectField(String className, String fieldName) {
        try {
            return Class.forName(className)
                    .getDeclaredField(fieldName)
                    .get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStaticStringField(String className, String fieldName) {
        return (String) getStaticObjectField(className, fieldName);
    }

    private static Field prepareField(Class<?> c, String fieldName) throws NoSuchFieldException {
        while (c != null) {
            try {
                Field f = c.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } finally {
                c = c.getSuperclass();
            }
        }
        throw new NoSuchFieldException();
    }

    public static void windowDismissed(InputMethodManager imm, IBinder iBinder) {
        try {
            imm.getClass().getMethod("windowDismissed", IBinder.class).invoke(imm, iBinder);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void modifyPushPriority(Object object, String filedName, Object filedValue) {
        Class<?> classType = object.getClass();
        Field field;
        try {
            field = classType.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(object, filedValue);
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void modifyPushBigContentView(Object object, String filedName, Object filedValue) {
        Class<?> classType = object.getClass();
        Field field;
        try {
            field = classType.getDeclaredField(filedName);
            field.setAccessible(true);// 设置安全检查，访问私有成员变量必须
            field.set(object, filedValue);
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
