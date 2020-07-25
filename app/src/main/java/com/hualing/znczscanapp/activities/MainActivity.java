package com.hualing.znczscanapp.activities;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.global.GlobalData;
import com.hualing.znczscanapp.global.TheApplication;
import com.hualing.znczscanapp.model.FunctionType;
import com.hualing.znczscanapp.util.IntentUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private JSONObject ziDuanNameJO;
    @BindView(R.id.scanBut)
    CardView scanBut;
    @BindView(R.id.kgScanBut)
    CardView kgScanBut;
    @BindView(R.id.pdcxBut)
    CardView pdcxBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        try {
            initZiDuanNameJO();
            getScreenSize();

            int width = (int) (TheApplication.getScreenWidth()/3.3);
            int height=width+10;
            //Log.e("width===",""+width);
            //Log.e("height===",""+height);
            scanBut.setLayoutParams(new LinearLayout.LayoutParams(width,height));
            kgScanBut.setLayoutParams(new LinearLayout.LayoutParams(width,height));
            pdcxBut.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initZiDuanNameJO() throws JSONException {
        ziDuanNameJO=new JSONObject();
        ziDuanNameJO.put("质检管理字段","质检管理");
        ziDuanNameJO.put("入库管理字段","入库管理");
        ziDuanNameJO.put("我是司机字段","我是司机");
    }

    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        TheApplication.setScreenHeight(display.getHeight());
        TheApplication.setScreenWidth(display.getWidth());
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.scanBut,R.id.kgScanBut,R.id.pdcxBut})
    public void onViewClicked(View v){
        try {
            switch (v.getId()){
                case R.id.scanBut:
                    if(GlobalData.checkQXGroup.contains(ziDuanNameJO.getString("质检管理字段"))) {
                        GlobalData.currentFunctionType = FunctionType.ZHI_JIAN_YUAN;
                        IntentUtil.openActivity(this, ScanActivity.class);
                    }
                    else{
                        MyToast("当前用户无此权限");
                    }
                    break;
                case R.id.kgScanBut:
                    if(GlobalData.checkQXGroup.contains(ziDuanNameJO.getString("入库管理字段"))) {
                        GlobalData.currentFunctionType = FunctionType.KU_GUAN;
                        IntentUtil.openActivity(this, ScanActivity.class);
                    }
                    else{
                        MyToast("当前用户无此权限");
                    }
                    break;
                case R.id.pdcxBut:
                    if(GlobalData.checkQXGroup.contains(ziDuanNameJO.getString("我是司机字段"))) {
                        GlobalData.currentFunctionType = FunctionType.PAI_DUI_CHA_XUN;
                        IntentUtil.openActivity(this, ScanActivity.class);
                    }
                    else{
                        MyToast("当前用户无此权限");
                    }
                    break;
            }

            /*
            Intent intent = null;
            switch (v.getId()){
                case R.id.scanBut:
                    intent = new Intent(MainActivity.this, OrderDetailActivity.class);
                    intent.putExtra("orderCode","108473798673440770");
                    break;
                case R.id.kgScanBut:
                    intent = new Intent(MainActivity.this, OrderRKActivity.class);
                    intent.putExtra("orderCode","108473798673440770");
                    break;
                case R.id.pdcxBut:
                    intent = new Intent(MainActivity.this, PaiDuiChaXunActivity.class);
                    intent.putExtra("orderNum","999999");
                    break;
            }
            startActivity(intent);
            */
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void MyToast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
