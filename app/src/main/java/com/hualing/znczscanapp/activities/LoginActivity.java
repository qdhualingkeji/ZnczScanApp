package com.hualing.znczscanapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.global.GlobalData;
import com.hualing.znczscanapp.util.AllActivitiesHolder;
import com.hualing.znczscanapp.util.SharedPreferenceUtil;
import com.hualing.znczscanapp.utils.AsynClient;
import com.hualing.znczscanapp.utils.GsonHttpResponseHandler;
import com.hualing.znczscanapp.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.username_et)
    EditText usernameET;
    @BindView(R.id.pwd_et)
    EditText pwdET;

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
        return R.layout.activity_login;
    }

    @OnClick({R.id.loginBtn})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.loginBtn:
                String username = usernameET.getText().toString();
                String pwd = pwdET.getText().toString();
                if(TextUtils.isEmpty(username)){
                    MyToast("请输入用户名");
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    MyToast("请输入密码");
                    return;
                }
                login(username,pwd);
                break;
        }
    }

    /***
     * 用户登录
     * @param username
     * @param password
     */
    private void login(final String username, final String password){
        RequestParams params = AsynClient.getRequestParams();
        //params.put("username", username);
        //params.put("password", password);

        String paramsStr="?username="+username+"&password="+password;
        AsynClient.post(MyHttpConfing.login+paramsStr, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("rawJsonData======",""+rawJsonData);
                MyToast("登录失败");
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse======",""+rawJsonResponse);

                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String status=jo.getString("status");
                    if("suc".equals(status)){
                        String token=jo.getString("token");
                        SharedPreferenceUtil.rememberUser(token,username,password);
                        GlobalData.userName=username;
                        getMenuBlocks();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        AllActivitiesHolder.removeAct(LoginActivity.this);
                    }
                    else
                        MyToast("登录失败");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*
                Gson gson = new Gson();
                RiderEntity riderEntity = gson.fromJson(rawJsonResponse, RiderEntity.class);
                if (riderEntity.getCode() == 100) {
                    RiderEntity.DataBean riderData = riderEntity.getData();
                    GlobalData.riderID = riderData.getRiderID();
                    GlobalData.phone = riderData.getPhone();
                    GlobalData.password = riderData.getPassword();
                    GlobalData.trueName = riderData.getTrueName();

                    SharedPreferenceUtil.rememberRider(phone, password);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    AllActivitiesHolder.removeAct(LoginActivity.this);
                }
                else {
                    MyToast(riderEntity.getMessage());
                }
                */
            }
        });
    }

    private void getMenuBlocks(){
        RequestParams params = AsynClient.getRequestParams();

        AsynClient.post(MyHttpConfing.getMenuBlocks, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("getBlocksFail======",""+rawJsonData);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("getBlocksSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONArray blocksJA = jo.getJSONArray("blocks");
                    String checkQXGroup="";
                    for(int i=0;i<blocksJA.length();i++){
                        JSONObject blockJO=(JSONObject)blocksJA.get(i);
                        String title = blockJO.getString("title");
                        //Log.e("title===",title);
                        checkQXGroup+=","+title;
                    }
                    GlobalData.checkQXGroup=checkQXGroup.substring(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void MyToast(String s) {
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
