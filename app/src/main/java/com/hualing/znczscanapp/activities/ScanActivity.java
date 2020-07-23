package com.hualing.znczscanapp.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.util.AllActivitiesHolder;
import com.hualing.znczscanapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {

    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.zxingview)
    ZXingView mZxingview;

    //授权请求码
    private static final int MY_PERMISSIONS_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        mTitle.setRightButtonEnable(false);
        mTitle.setEvents(new TitleBar.AddClickEvents() {
            @Override
            public void clickLeftButton() {
                AllActivitiesHolder.removeAct(ScanActivity.this);
            }

            @Override
            public void clickRightButton() {

            }
        });

        //6.0以上先授权
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission4Version6Up();
        }

        mZxingview.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //已打开权限
        startScan();
    }

    private void startScan() {
        mZxingview.startCamera();
        //        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mZxingview.showScanRect();
        mZxingview.startSpot();
    }

    @Override
    protected void onStop() {
        mZxingview.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZxingview.onDestroy();
        super.onDestroy();
    }

    /**
     * 6.0以上版本权限授权
     */
    private void checkPermission4Version6Up() {
        //检测权限授权（针对6.0以上先安装后检查权限的情况）
        List<String> permissionsList = new ArrayList<>();
        String[] permissions = null;
        //        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        //            permissionsList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.CAMERA);
        }
        if (permissionsList.size() != 0) {
            permissions = new String[permissionsList.size()];
            for (int i = 0; i < permissionsList.size(); i++) {
                permissions[i] = permissionsList.get(i);
            }
            //此句调起权限授权框
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * 授权结束后回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    // Permission Denied
                    new AlertDialog.Builder(this)
                            .setMessage("您已拒绝了授权申请，无法使用扫码功能，是否去系统设置中打开权限?")
                            .setPositiveButton("去打开", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent localIntent = new Intent();
                                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                    } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
                                        localIntent.setAction(Intent.ACTION_VIEW);
                                        localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                        localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                                    }
                                    startActivity(localIntent);
                                }
                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭此页，无法使用扫描功能
                            AllActivitiesHolder.removeAct(ScanActivity.this);
                        }
                    }).create().show();
                    //                    Toast.makeText(TheApplication.getContext(), "您已拒绝了授权申请，无法使用扫码功能，请通过授权或者手动在系统设置中打开权限", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //授权通过开始扫描(异步问题导致onStart里面的启动扫描不管用,所以需要再次开启)
            startScan();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_scan;
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        try {
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            JSONObject jo = new JSONObject("{'订单编码':'107928501035606018'}");
            String orderCode = jo.getString("订单编码");
            Log.e("orderCode==",""+orderCode);
            Intent intent = new Intent(ScanActivity.this, OrderDetailActivity.class);
            intent.putExtra("orderCode",orderCode);
            startActivity(intent);
            AllActivitiesHolder.removeAct(ScanActivity.this);
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }
}
