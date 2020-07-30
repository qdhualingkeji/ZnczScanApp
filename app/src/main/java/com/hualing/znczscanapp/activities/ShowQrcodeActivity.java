package com.hualing.znczscanapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.global.TheApplication;
import com.hualing.znczscanapp.utils.AsynClient;
import com.hualing.znczscanapp.utils.GsonHttpResponseHandler;
import com.hualing.znczscanapp.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import butterknife.BindView;

public class ShowQrcodeActivity extends BaseActivity {

    private String dqphCode;
    private JSONObject dqphGroupsFieldIdJO,ziDuanNameJO;
    @BindView(R.id.qrcode_iv)
    ImageView qrcodeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        try {
            initZiDuanNameJO();
            int width = (int)(TheApplication.getScreenWidth()/1.3);
            int height=width;
            qrcodeIV.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     *设置字段键名称
     */
    private void initZiDuanNameJO() throws JSONException {
        ziDuanNameJO=new JSONObject();
        ziDuanNameJO.put("二维码字段","二维码");
    }

    @Override
    protected void getDataFormWeb() {
        initQueryKey();
    }

    private void initQueryKey(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.dqphEntityListTmpl, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("initLtmplFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("initLtmplSuccess======",""+rawJsonResponse);

                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String queryKey = jo.getString("queryKey");
                    initDQPHCode(queryKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initDQPHCode(String queryKey){
        RequestParams params = AsynClient.getRequestParams();
        params.put("pageNo","1");
        AsynClient.get(MyHttpConfing.getEntityListData.replaceAll("queryKey",queryKey), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("dQPHCodeFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("dQPHCodeSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONArray entitiesJA = jo.getJSONArray("entities");
                    JSONObject entityJO = (JSONObject)entitiesJA.get(0);
                    dqphCode = entityJO.getString("code");
                    Log.e("dqphCode===",""+dqphCode);

                    initDQPHGroupsFieldId();
                } catch (JSONException e) {
                    Log.e("?????",e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void initDQPHGroupsFieldId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.dqphDtmplNormal, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("dqphFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("dqphSuccess======",""+rawJsonResponse);

                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String config = jo.getString("config");
                    JSONObject configJO = null;
                    configJO = new JSONObject(config);
                    String dtmpl = configJO.getString("dtmpl");
                    JSONObject dtmplJO = new JSONObject(dtmpl);
                    JSONArray groupsJA=new JSONArray(dtmplJO.getString("groups"));
                    JSONObject groupsJO = (JSONObject)groupsJA.get(0);
                    String fields = groupsJO.getString("fields");
                    Log.e("fields===",fields);
                    JSONArray fieldsJA = new JSONArray(fields);

                    dqphGroupsFieldIdJO=new JSONObject();
                    for (int i=0;i<fieldsJA.length();i++) {
                        JSONObject fieldJO = (JSONObject)fieldsJA.get(i);
                        String title = fieldJO.getString("title");
                        String id = fieldJO.getString("id");
                        Log.e("title===",""+title+",id==="+id);
                        dqphGroupsFieldIdJO.put(title,id);
                    }
                    Log.e("dqphGroupsFieldIdJO===",dqphGroupsFieldIdJO.toString());
                    getDQPHDetail();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getDQPHDetail(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getDQPHDetail+dqphCode, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("dqphDetailFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("dqphDetailSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONObject entityJO = jo.getJSONObject("entity");
                    JSONObject fieldMapJO = entityJO.getJSONObject("fieldMap");
                    String qrcodeUrl = toURLString(fieldMapJO.getString(dqphGroupsFieldIdJO.getString(ziDuanNameJO.getString("二维码字段"))));
                    //String qrcodeUrl = toURLString("http://121.196.184.205:96/hydrocarbon/./download-files/bbe2cac353fab4605a1d9f47b4d342bf/二维码图片_司机订单.png");
                    Log.e("qrcodeUrl===",""+qrcodeUrl);
                    initQrcode(qrcodeUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String toURLString(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                try {
                    sb.append(URLEncoder.encode(String.valueOf(c), "utf-8"));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private void initQrcode(final String url){
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    // 设置请求方式和超时时间
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(1000 * 10);
                    conn.connect();
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        //利用消息的方式把数据传送给handler
                        Message msg = handler.obtainMessage();
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    Log.e("error===",""+e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler handler=new Handler(){
        public void handleMessage(Message message) {
            //Bitmap bitmap = (Bitmap) message.obj;
            Bitmap bm=(Bitmap)message.obj;
            qrcodeIV.setImageBitmap(bm);
        }
    };

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_show_qrcode;
    }
}
