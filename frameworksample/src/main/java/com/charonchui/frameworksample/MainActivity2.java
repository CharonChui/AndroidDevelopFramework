package com.charonchui.frameworksample;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.charonchui.framework.util.IPermission;
import com.charonchui.framework.util.PermissionUtil;

import java.util.List;

public class MainActivity2 extends Activity {

    public static final int REQUEST_CODE_CAMERA = 0;
    public static final int REQUEST_CODE_CONTACTS = 1;
    public static final int REQUEST_CODE_MORE = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults, mPermission);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
    }


    public void requestCamera(View view) {
        PermissionUtil.checkPermissions(this, REQUEST_CODE_CAMERA, mPermission, Manifest.permission.CAMERA);
    }

    public void requestReadContacts(View view) {
        PermissionUtil.checkPermissions(this, REQUEST_CODE_CONTACTS, mPermission, Manifest.permission.READ_CONTACTS);
    }

    public void requestMore(View view) {
        PermissionUtil.checkPermissions(this, REQUEST_CODE_MORE, mPermission, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALENDAR, Manifest.permission.CALL_PHONE);
    }

    private void showPermissionTipDialog(final int requestCode, final List<String> permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("I want you permissions");
        builder.setTitle("Hello Permission");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PermissionUtil.requestPermission(MainActivity2.this, requestCode, permissions);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public IPermission mPermission = new IPermission() {
        @Override
        public void onGranted(int requestCode) {
            Toast.makeText(MainActivity2.this, "onGranted :" + requestCode, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDenied(int requestCode) {
            Toast.makeText(MainActivity2.this, "onDenied :" + requestCode, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRational(int requestCode, List<String> permission) {
            showPermissionTipDialog(requestCode, permission);
        }

        @Override
        public boolean showRational(int requestCode) {
            switch (requestCode) {
                case REQUEST_CODE_MORE:
                    return true;

                default:
                    break;
            }
            return false;
        }
    };
}
