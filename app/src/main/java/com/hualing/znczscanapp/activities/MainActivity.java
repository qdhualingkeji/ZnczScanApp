package com.hualing.znczscanapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hualing.znczscanapp.activities.BaseActivity;
import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.global.GlobalData;
import com.hualing.znczscanapp.model.FunctionType;
import com.hualing.znczscanapp.util.IntentUtil;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {

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
        Intent intent = null;
        switch (v.getId()){
            case R.id.scanBut:
                GlobalData.currentFunctionType = FunctionType.ZHI_JIAN_YUAN;
                break;
            case R.id.kgScanBut:
                GlobalData.currentFunctionType = FunctionType.KU_GUAN;
                break;
            case R.id.pdcxBut:
                GlobalData.currentFunctionType = FunctionType.PAI_DUI_CHA_XUN;
                break;
        }
        IntentUtil.openActivity(this, ScanActivity.class);

        /*
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
    }
}
