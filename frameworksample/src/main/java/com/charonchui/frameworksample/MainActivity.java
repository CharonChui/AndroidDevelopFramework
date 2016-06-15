package com.charonchui.frameworksample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.fl, new MainFragment()).commit();
    }
}
