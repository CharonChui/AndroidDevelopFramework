package com.charonchui.framework.util;

import java.util.List;

public interface IPermission {
    void onGranted(int requestCode);

    void onDenied(int requestCode);

    void onRational(int requestCode, List<String> permissions);

    /**
     * 是否需要提示用户该权限的作用，提示后需要再调用requestPermission()方法来申请。
     *
     * @return true 为提示，false为不提示
     */
    boolean showRational(int requestCode);
}
