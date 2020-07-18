package com.hualing.znczscanapp.activities;

import android.os.Bundle;
import android.view.View;

import com.hualing.znczscanapp.activities.BaseActivity;
import com.hualing.znczscanapp.R;
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

    @OnClick({R.id.scanBut})
    public void onViewClicked(View v){
        IntentUtil.openActivity(this, ScanActivity.class);
    }
}
