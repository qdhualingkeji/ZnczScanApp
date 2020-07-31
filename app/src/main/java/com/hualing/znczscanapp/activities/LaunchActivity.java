package com.hualing.znczscanapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.global.GlobalData;
import com.hualing.znczscanapp.util.AllActivitiesHolder;
import com.hualing.znczscanapp.util.IntentUtil;
import com.hualing.znczscanapp.util.SharedPreferenceUtil;
import com.hualing.znczscanapp.utils.AsynClient;
import com.hualing.znczscanapp.utils.GsonHttpResponseHandler;
import com.hualing.znczscanapp.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class LaunchActivity extends BaseActivity {

    private static final long DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (SharedPreferenceUtil.ifHasLocalUser()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toLogin();
                        }
                    });
                }else {
                    IntentUtil.openActivity(LaunchActivity.this,LoginActivity.class);
                    AllActivitiesHolder.removeAct(LaunchActivity.this);
                }
            }
        },DELAY);
    }

    private void toLogin(){
        RequestParams params = AsynClient.getRequestParams();
        final String username = SharedPreferenceUtil.getUser()[1];
        final String password = SharedPreferenceUtil.getUser()[2];
        String paramsStr="?username="+username+"&password="+password;
        AsynClient.post(MyHttpConfing.login+paramsStr, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {

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
                        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        IntentUtil.openActivity(LaunchActivity.this,LoginActivity.class);
                    }
                    AllActivitiesHolder.removeAct(LaunchActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_launch;
    }
}
