package com.hualing.znczscanapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.util.AllActivitiesHolder;
import com.hualing.znczscanapp.utils.AsynClient;
import com.hualing.znczscanapp.utils.GsonHttpResponseHandler;
import com.hualing.znczscanapp.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailActivity extends BaseActivity {

    private String orderCode;
    private JSONObject columnsIdJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        orderCode = getIntent().getStringExtra("orderCode");
        Toast.makeText(this, orderCode, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void getDataFormWeb() {
        initColumnsId();
    }

    private void initColumnsId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.dtmplNormalByMenuId, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("rawJsonData2======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse2======",""+rawJsonResponse);

                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String config = jo.getString("config");
                    JSONObject configJO = new JSONObject(config);
                    String dtmpl = configJO.getString("dtmpl");
                    JSONObject dtmplJO = new JSONObject(dtmpl);
                    JSONArray groupsJA=new JSONArray(dtmplJO.getString("groups"));
                    JSONObject groupsJO = (JSONObject)groupsJA.get(1);
                    //Log.e("group===",""+groupsJO.toString());
                    String fields = groupsJO.getString("fields");
                    JSONArray fieldsJA = new JSONArray(fields);

                    columnsIdJO=new JSONObject();
                    for (int i=0;i<fieldsJA.length();i++) {
                        JSONObject fieldJO = (JSONObject)fieldsJA.get(i);
                        String title = fieldJO.getString("title");
                        String id = fieldJO.getString("id");
                        //Log.e("title===",""+title+",id==="+id);
                        columnsIdJO.put(title,id);
                    }
                    Log.e("columnsIdJO===",columnsIdJO.toString());
                } catch (JSONException e) {
                    Log.e("???????","???????");
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
        return R.layout.activity_order_detail;
    }
}
