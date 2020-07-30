package com.hualing.znczscanapp.activities;

import android.os.Bundle;
import android.view.View;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.global.GlobalData;
import com.hualing.znczscanapp.model.FunctionType;
import com.hualing.znczscanapp.util.IntentUtil;

import butterknife.OnClick;

public class SelectTypeActivity extends BaseActivity {

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
        return R.layout.activity_select_type;
    }

    @OnClick({R.id.scanBut,R.id.cphcxBut})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.scanBut:
                IntentUtil.openActivity(this, ScanActivity.class);
                break;
            case R.id.cphcxBut:
                switch (GlobalData.currentFunctionType){
                    case FunctionType.ZHI_JIAN_YUAN:
                        IntentUtil.openActivity(this, DDXQCPHActivity.class);
                        break;
                    case FunctionType.KU_GUAN:
                        IntentUtil.openActivity(this, DDRKCPHActivity.class);
                        break;
                }
                break;
        }
    }
}
