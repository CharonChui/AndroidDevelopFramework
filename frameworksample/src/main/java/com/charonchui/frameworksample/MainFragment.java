package com.charonchui.frameworksample;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.charonchui.framework.util.IPermission;
import com.charonchui.framework.util.PermissionUtil;

import java.util.List;


public class MainFragment extends Fragment implements View.OnClickListener {
    private Button mReqCameraBt;
    private Button mReqContactsBt;
    private Button mReqMoreBt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        findView(view);
        initView();
        return view;
    }

    private void findView(View view) {
        mReqCameraBt = (Button) view.findViewById(R.id.bt_requestCamera);
        mReqContactsBt = (Button) view.findViewById(R.id.bt_requestContacts);
        mReqMoreBt = (Button) view.findViewById(R.id.bt_requestMore);
    }

    private void initView() {
        mReqCameraBt.setOnClickListener(this);
        mReqContactsBt.setOnClickListener(this);
        mReqMoreBt.setOnClickListener(this);
    }


    public static final int REQUEST_CODE_CAMERA = 0;
    public static final int REQUEST_CODE_CONTACTS = 1;
    public static final int REQUEST_CODE_MORE = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(MainFragment.this.getActivity(), "on....", Toast.LENGTH_SHORT).show();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions, grantResults, mPermission);
    }

    public void requestCamera() {
        PermissionUtil.checkPermissions(this, REQUEST_CODE_CAMERA, mPermission, Manifest.permission.CAMERA);
    }

    public void requestReadContacts() {
        PermissionUtil.checkPermissions(this, REQUEST_CODE_CONTACTS, mPermission, Manifest.permission.READ_CONTACTS);
    }

    public void requestMore() {
        PermissionUtil.checkPermissions(this, REQUEST_CODE_MORE, mPermission, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALENDAR, Manifest.permission.CALL_PHONE);
    }

    private void showPermissionTipDialog(final int requestCode, final List<String> permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("I want you permissions");
        builder.setTitle("Hello Permission");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PermissionUtil.requestPermission(MainFragment.this, requestCode, permissions);
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
            Toast.makeText(getActivity(), "onGranted :" + requestCode, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDenied(int requestCode) {
            Toast.makeText(getActivity(), "onDenied :" + requestCode, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_requestCamera:
                requestCamera();
                break;
            case R.id.bt_requestContacts:
                requestReadContacts();
                break;
            case R.id.bt_requestMore:
                requestMore();
                break;
        }
    }
}
