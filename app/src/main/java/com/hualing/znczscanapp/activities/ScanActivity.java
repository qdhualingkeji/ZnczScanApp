package com.hualing.znczscanapp.activities;

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
import com.hualing.znczscanapp.global.GlobalData;
import com.hualing.znczscanapp.model.FunctionType;
import com.hualing.znczscanapp.util.AllActivitiesHolder;
import com.hualing.znczscanapp.utils.AsynClient;
import com.hualing.znczscanapp.utils.GsonHttpResponseHandler;
import com.hualing.znczscanapp.utils.MyHttpConfing;
import com.hualing.znczscanapp.widget.TitleBar;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {

    private JSONObject ziDuanNameJO,ybwjCriteriasIdJO,ebwjCriteriasIdJO;
    private String noOrderMsg="没有找到匹配订单";
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

        try {
            initZiDuanNameJO();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
     *设置字段键名称
     */
    private void initZiDuanNameJO() throws JSONException {
        ziDuanNameJO=new JSONObject();
        ziDuanNameJO.put("订单号字段","订单号");
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
        initYbwjCriteriaId();
        initEbwjCriteriaId();
    }

    private void initYbwjCriteriaId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListTmpl.replaceAll("menuId",MyHttpConfing.bqglWgjcYbwjMenuId), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {

                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("criteriaIdFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("criteriaIdSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONObject ltmplJO = jo.getJSONObject("ltmpl");
                    JSONArray criteriasJA = ltmplJO.getJSONArray("criterias");
                    ybwjCriteriasIdJO=new JSONObject();
                    for(int i=0;i<criteriasJA.length();i++){
                        JSONObject criteriaJO = criteriasJA.getJSONObject(i);
                        String title = criteriaJO.getString("title");
                        String id = criteriaJO.getString("id");
                        ybwjCriteriasIdJO.put(title,id);
                    }
                    Log.e("ybwjCritIdJO===",""+ybwjCriteriasIdJO.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initEbwjCriteriaId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListTmpl.replaceAll("menuId",MyHttpConfing.bqglWgjcEbwjMenuId), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {

                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("criteriaIdFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("criteriaIdSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONObject ltmplJO = jo.getJSONObject("ltmpl");
                    JSONArray criteriasJA = ltmplJO.getJSONArray("criterias");
                    ebwjCriteriasIdJO=new JSONObject();
                    for(int i=0;i<criteriasJA.length();i++){
                        JSONObject criteriaJO = criteriasJA.getJSONObject(i);
                        String title = criteriaJO.getString("title");
                        String id = criteriaJO.getString("id");
                        ebwjCriteriasIdJO.put(title,id);
                    }
                    Log.e("ebwjCritIdJO===",""+ebwjCriteriasIdJO.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initYbwjQueryKey(String orderNum, final String orderCode) throws JSONException {
        RequestParams params = AsynClient.getRequestParams();
        //Log.e("criteriaId===",criteriasIdJO.getString(ziDuanNameJO.getString("订单号字段")));
        //params.put("criteria_105935359844361","DD109221984123887616");
        params.put("criteria_"+ybwjCriteriasIdJO.getString(ziDuanNameJO.getString("订单号字段")),orderNum);
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListTmpl.replaceAll("menuId",MyHttpConfing.bqglWgjcYbwjMenuId), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("queryKeyFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("queryKeySuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String queryKey = jo.getString("queryKey");
                    //Log.e("queryKey======",""+queryKey);

                    checkYbwjOrderExist(queryKey,orderCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initEbwjQueryKey(String orderNum, final String orderCode) throws JSONException {
        RequestParams params = AsynClient.getRequestParams();
        //Log.e("criteriaId===",criteriasIdJO.getString(ziDuanNameJO.getString("订单号字段")));
        //params.put("criteria_105935359844361","DD109221984123887616");
        params.put("criteria_"+ebwjCriteriasIdJO.getString(ziDuanNameJO.getString("订单号字段")),orderNum);
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListTmpl.replaceAll("menuId",MyHttpConfing.bqglWgjcEbwjMenuId), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("queryKeyFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("queryKeySuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String queryKey = jo.getString("queryKey");
                    //Log.e("queryKey======",""+queryKey);

                    checkEbwjOrderExist(queryKey,orderCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 验证一磅外检订单是否存在
     * @param queryKey
     * @param orderCode
     */
    private void checkYbwjOrderExist(String queryKey, final String orderCode){
        RequestParams params = AsynClient.getRequestParams();
        params.put("pageNo","1");
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListData.replaceAll("queryKey",queryKey), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("Fail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("Success======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONArray entitiesJA = jo.getJSONArray("entities");
                    if(entitiesJA.length()==0){
                        MyToast(noOrderMsg);
                        Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                        startActivity(intent);
                        AllActivitiesHolder.removeAct(ScanActivity.this);
                    }
                    else{
                        doAction(MyHttpConfing.doAction.replaceAll("menuId",MyHttpConfing.bqglWgjcYbwjMenuId).replaceAll("actionId",MyHttpConfing.zjtgActionId),orderCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 验证二磅外检订单是否存在
     * @param queryKey
     * @param orderCode
     */
    private void checkEbwjOrderExist(String queryKey, final String orderCode){
        RequestParams params = AsynClient.getRequestParams();
        params.put("pageNo","1");
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListData.replaceAll("queryKey",queryKey), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("Fail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("Success======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONArray entitiesJA = jo.getJSONArray("entities");
                    if(entitiesJA.length()==0){
                        MyToast(noOrderMsg);
                        Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                        startActivity(intent);
                        AllActivitiesHolder.removeAct(ScanActivity.this);
                    }
                    else{
                        doAction(MyHttpConfing.doAction.replaceAll("menuId",MyHttpConfing.bqglWgjcEbwjMenuId).replaceAll("actionId",MyHttpConfing.ywczxActionId),orderCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
            //Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            Intent intent = null;
            //JSONObject jo = new JSONObject("{'订单编码':'108473798673440770','订单号':'999999','司机身份证':'12345678'}");
            JSONObject jo = new JSONObject(result);
            switch (GlobalData.currentFunctionType){
                case FunctionType.ZHI_JIAN_YUAN:
                    /*
                    intent = new Intent(ScanActivity.this, DDXQScanOldActivity.class);
                    intent.putExtra("orderCode",jo.getString("订单编码"));//订单化验那边既要查询订单详情，也要根据订单号查询质检报告id，因此两个参数都要传过去
                    intent.putExtra("orderNum",jo.getString("订单号"));
                    */
                    initYbwjQueryKey(jo.getString("订单号"),jo.getString("订单编码"));
                    break;
                case FunctionType.KU_GUAN:
                    /*
                    intent = new Intent(ScanActivity.this, DDRKScanOldActivity.class);
                    intent.putExtra("orderCode",jo.getString("订单编码"));
                    */
                    initEbwjQueryKey(jo.getString("订单号"),jo.getString("订单编码"));
                    //doAction(MyHttpConfing.ywczxAction,jo.getString("订单编码"));
                    break;
                    /*
                case FunctionType.PAI_DUI_CHA_XUN:
                    intent = new Intent(ScanActivity.this, PaiDuiChaXunActivity.class);
                    intent.putExtra("orderNum",jo.getString("订单号"));
                    break;
                    */
            }
            startActivity(intent);
            AllActivitiesHolder.removeAct(ScanActivity.this);
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void doAction(final String actionUrl, final String codes){
        RequestParams params = AsynClient.getRequestParams();
        params.put("codes",codes);
        AsynClient.post(MyHttpConfing.getBaseUrl()+actionUrl, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("actionFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("actionSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String status=jo.getString("status");
                    if("suc".equals(status)){
                        Intent intent = null;
                        if(actionUrl.contains(MyHttpConfing.zjtgActionId)) {
                            MyToast("质检完毕");
                        }
                        else{
                            MyToast("入库完毕");
                        }
                        intent = new Intent(ScanActivity.this, DDXQActivity.class);
                        intent.putExtra("orderCode",codes);
                        startActivity(intent);
                        AllActivitiesHolder.removeAct(ScanActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void MyToast(String s) {
        Toast.makeText(ScanActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }
}
