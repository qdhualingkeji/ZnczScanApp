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
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.global.GlobalData;
import com.hualing.znczscanapp.global.TheApplication;
import com.hualing.znczscanapp.model.FunctionType;
import com.hualing.znczscanapp.util.AllActivitiesHolder;
import com.hualing.znczscanapp.util.IntentUtil;
import com.hualing.znczscanapp.util.SharedPreferenceUtil;
import com.hualing.znczscanapp.utils.AsynClient;
import com.hualing.znczscanapp.utils.HttpUtil;
import com.hualing.znczscanapp.utils.MyHttpConfing;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private JSONObject ziDuanNameJO;
    @BindView(R.id.changqu_tv)
    TextView changquTV;
    @BindView(R.id.userName_tv)
    TextView userNameTV;
    @BindView(R.id.zhiJian_cv)
    CardView zhiJianCV;
    @BindView(R.id.ruKu_cv)
    CardView ruKuCV;
    @BindView(R.id.pdcxBut)
    CardView pdcxBut;
    @BindView(R.id.csewmBut)
    CardView csewmBut;
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
            zhiJianCV.setLayoutParams(new LinearLayout.LayoutParams(width,height));
            ruKuCV.setLayoutParams(new LinearLayout.LayoutParams(width,height));
            pdcxBut.setLayoutParams(new LinearLayout.LayoutParams(width,height));
            csewmBut.setLayoutParams(new LinearLayout.LayoutParams(width,height));

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
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            mDrawerToggle.syncState();
            mDrawerLayout.addDrawerListener(mDrawerToggle);

            changquTV.setText(GlobalData.changqu);
            userNameTV.setText(GlobalData.userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initZiDuanNameJO() throws JSONException {
        ziDuanNameJO=new JSONObject();
        ziDuanNameJO.put("质检入库字段","质检入库");
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

    @OnClick({R.id.zhiJian_cv,R.id.ruKu_cv,R.id.pdcxBut,R.id.csewmBut,R.id.exitBut})
    public void onViewClicked(View v){
        try {
            switch (v.getId()){
                case R.id.zhiJian_cv:
                    if(GlobalData.checkQXGroup.contains(ziDuanNameJO.getString("质检入库字段"))) {
                        GlobalData.currentFunctionType = FunctionType.ZHI_JIAN_YUAN;
                        //IntentUtil.openActivity(this, ScanActivity.class);
                        IntentUtil.openActivity(this, SelectTypeActivity.class);
                    }
                    else{
                        MyToast("当前用户无此权限");
                    }
                    break;
                case R.id.ruKu_cv:
                    if(GlobalData.checkQXGroup.contains(ziDuanNameJO.getString("质检入库字段"))) {
                        GlobalData.currentFunctionType = FunctionType.KU_GUAN;
                        //IntentUtil.openActivity(this, ScanActivity.class);
                        IntentUtil.openActivity(this, SelectTypeActivity.class);
                    }
                    else{
                        MyToast("当前用户无此权限");
                    }
                    break;
                case R.id.pdcxBut:
                    if(GlobalData.checkQXGroup.contains(ziDuanNameJO.getString("我是司机字段"))) {
                        IntentUtil.openActivity(this, PaiDuiChaXunActivity.class);
                    }
                    else{
                        MyToast("当前用户无此权限");
                    }
                    break;
                case R.id.csewmBut:
                    if(GlobalData.checkQXGroup.contains(ziDuanNameJO.getString("我是司机字段"))) {
                        IntentUtil.openActivity(this, ShowQrcodeActivity.class);
                    }
                    else{
                        MyToast("当前用户无此权限");
                    }
                    break;
                case R.id.exitBut:
                    loginout();
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
                    intent = new Intent(MainActivity.this, DDRKScanActivity.class);
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

    private void loginout(){
        String r = HttpUtil.doDelete(MyHttpConfing.login, "");
        Log.e("result===",r+"");
        SharedPreferenceUtil.logout();
        IntentUtil.openActivity(MainActivity.this,LoginActivity.class);
        AllActivitiesHolder.removeAct(MainActivity.this);
    }

    public void MyToast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
