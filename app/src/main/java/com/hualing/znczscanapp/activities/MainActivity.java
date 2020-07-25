package com.hualing.znczscanapp.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
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
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

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

            mToolBar.setTitle(getResources().getString(R.string.app_name));//设置Toolbar标题
            //        mToolBar.setTitle("二维码追溯-员工模式");//设置Toolbar标题
            mToolBar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
            setSupportActionBar(mToolBar);
            //        mToolBar.setOnMenuItemClickListener(onMenuItemClick);
            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //创建返回键，并实现打开关/闭监听
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    Log.e("aaaaaaaaa","aaaaaaaaaa");
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            mDrawerToggle.syncState();
            mDrawerLayout.addDrawerListener(mDrawerToggle);
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
