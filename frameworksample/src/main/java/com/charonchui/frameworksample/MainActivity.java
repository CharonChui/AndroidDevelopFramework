package com.charonchui.frameworksample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.charonchui.framework.util.ActivityUtil;
import com.charonchui.framework.util.LogUtil;
import com.charonchui.framework.util.ToastUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtil.e("@@@", "" + ActivityUtil.getScreenHeight(this));
        ToastUtil.makeText(this, "hello world", Toast.LENGTH_SHORT).show();
    }


}
