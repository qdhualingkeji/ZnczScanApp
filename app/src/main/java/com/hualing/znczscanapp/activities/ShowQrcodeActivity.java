package com.hualing.znczscanapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    private String wdddCode;
    private JSONObject wdddJbxxFieldIdJO,wdddCriteriasIdJO,ziDuanNameJO;
    @BindView(R.id.qrcode_iv)
    ImageView qrcodeIV;
    @BindView(R.id.ddh_tv)
    TextView ddhTV;
    @BindView(R.id.yzxzl_tv)
    TextView yzxzlTV;
    @BindView(R.id.lxlx_tv)
    TextView lxlxTV;
    @BindView(R.id.bjsj_tv)
    TextView bjsjTV;
    @BindView(R.id.sjzl_tv)
    TextView sjzlTV;
    @BindView(R.id.zlceb_tv)
    TextView zlcebTV;

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
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            qrcodeIV.setLayoutParams(layoutParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     *设置字段键名称
     */
    private void initZiDuanNameJO() throws JSONException {
        ziDuanNameJO=new JSONObject();
        ziDuanNameJO.put("执行状态字段","执行状态");
        ziDuanNameJO.put("基本信息字段","基本信息");
        ziDuanNameJO.put("订单号字段","订单号");
        ziDuanNameJO.put("预装卸重量字段","预装卸重量");
        ziDuanNameJO.put("流向类型字段","流向类型");
        ziDuanNameJO.put("编辑时间字段","编辑时间");
        ziDuanNameJO.put("二维码字段","二维码");
        ziDuanNameJO.put("实际重量字段","实际重量");
        ziDuanNameJO.put("重量差额比字段","重量差额比");
    }

    @Override
    protected void getDataFormWeb() {
        initWDDDLtmplAttr();
    }

    private void initWDDDLtmplAttr() {
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.wdddEntityListTmpl, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("initWDDDLFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("initWDDDLSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo=new JSONObject(rawJsonResponse);
                    JSONObject ltmplJO = jo.getJSONObject("ltmpl");
                    JSONArray criteriasJA = ltmplJO.getJSONArray("criterias");

                    JSONArray columnsJA = ltmplJO.getJSONArray("columns");

                    initWDDDCriteriasId(criteriasJA);
                    initWDDDQueryKey();
                } catch (JSONException e) {
                    Log.e("error===",e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void initWDDDCriteriasId(JSONArray criteriasJA) throws JSONException {
        wdddCriteriasIdJO=new JSONObject();
        for(int i=0;i<criteriasJA.length();i++){
            JSONObject criteriaJO = (JSONObject)criteriasJA.get(i);
            String title = criteriaJO.getString("title");
            String id = criteriaJO.getString("id");
            Log.e("title1===",title);
            Log.e("id1===",id);
            wdddCriteriasIdJO.put(title,id);
        }
    }

    private void initWDDDQueryKey() throws JSONException {
        RequestParams params = AsynClient.getRequestParams();
        String paramsStr="?criteria_"+wdddCriteriasIdJO.getString(ziDuanNameJO.getString("执行状态字段"))+"=待确认,排队中,待化验,待一检上磅,一检下磅,待入库,入库完成,待二检上磅,待离厂,一检称重中,二检称重中,编辑中,运输中";
        AsynClient.get(MyHttpConfing.wdddEntityListTmpl+paramsStr, this, params, new GsonHttpResponseHandler() {
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
                    initWDDDCode(queryKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initWDDDCode(String queryKey){
        RequestParams params = AsynClient.getRequestParams();
        params.put("pageNo","1");
        AsynClient.get(MyHttpConfing.getEntityListData.replaceAll("queryKey",queryKey), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("wDDDCodeFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("wDDDCodeSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONArray entitiesJA = jo.getJSONArray("entities");
                    JSONObject entityJO = (JSONObject)entitiesJA.get(0);
                    wdddCode = entityJO.getString("code");
                    Log.e("wdddCode===",""+wdddCode);

                    initWDDDGroupsId();
                } catch (JSONException e) {
                    Log.e("?????",e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void initWDDDGroupsId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.wdddDtmplNormal, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("wdddFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("wdddSuccess======",""+rawJsonResponse);

                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String config = jo.getString("config");
                    JSONObject configJO = null;
                    configJO = new JSONObject(config);
                    String dtmpl = configJO.getString("dtmpl");
                    JSONObject dtmplJO = new JSONObject(dtmpl);
                    JSONArray groupsJA=new JSONArray(dtmplJO.getString("groups"));

                    JSONObject wdddGroupsIdJO=new JSONObject();
                    for (int i=0;i<groupsJA.length();i++) {
                        JSONObject groupJO = groupsJA.getJSONObject(i);
                        String title = groupJO.getString("title");
                        String id = groupJO.getString("id");
                        Log.e("title===",""+title+",id==="+id);
                        wdddGroupsIdJO.put(title,id);
                    }
                    Log.e("wdddGroupsIdJO===",wdddGroupsIdJO.toString());

                    JSONObject wdddGroupsJO = null;
                    for (int i=0;i<groupsJA.length();i++){
                        if(groupsJA.getJSONObject(i).getString("id").equals(wdddGroupsIdJO.getString(ziDuanNameJO.getString("基本信息字段")))){
                            wdddGroupsJO=groupsJA.getJSONObject(i);
                            break;
                        }
                    }
                    String wdddFields = wdddGroupsJO.getString("fields");
                    Log.e("wdddFields===",wdddFields);
                    JSONArray wdddFieldsJA = new JSONArray(wdddFields);

                    wdddJbxxFieldIdJO=new JSONObject();
                    for (int i=0;i<wdddFieldsJA.length();i++) {
                        JSONObject dqphFieldJO = (JSONObject)wdddFieldsJA.get(i);
                        String title = dqphFieldJO.getString("title");
                        String id = dqphFieldJO.getString("id");
                        Log.e("title===",""+title+",id==="+id);
                        wdddJbxxFieldIdJO.put(title,id);
                    }
                    Log.e("wdddJbxxFieldIdJO===",wdddJbxxFieldIdJO.toString());
                    getWDDDJBXX();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getWDDDJBXX() {
        RequestParams params = AsynClient.getRequestParams();
        Log.e("wdddCode===",""+wdddCode);
        AsynClient.get(MyHttpConfing.getWDDDDetail+wdddCode, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("wdddDetailFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("wdddDetailSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONObject entityJO = jo.getJSONObject("entity");
                    JSONObject fieldMapJO = entityJO.getJSONObject("fieldMap");
                    String ddh = fieldMapJO.getString(wdddJbxxFieldIdJO.getString(ziDuanNameJO.getString("订单号字段")));
                    String yzxzl = fieldMapJO.getString(wdddJbxxFieldIdJO.getString(ziDuanNameJO.getString("预装卸重量字段")));
                    String lxlx = fieldMapJO.getString(wdddJbxxFieldIdJO.getString(ziDuanNameJO.getString("流向类型字段")));
                    String bjsj = fieldMapJO.getString(wdddJbxxFieldIdJO.getString(ziDuanNameJO.getString("编辑时间字段")));
                    String sjzl = fieldMapJO.getString(wdddJbxxFieldIdJO.getString(ziDuanNameJO.getString("实际重量字段")));
                    String zlceb = fieldMapJO.getString(wdddJbxxFieldIdJO.getString(ziDuanNameJO.getString("重量差额比字段")));
                    String qrcodeUrl = toURLString(MyHttpConfing.baseUrl+fieldMapJO.getString(wdddJbxxFieldIdJO.getString(ziDuanNameJO.getString("二维码字段"))));

                    Log.e("订单号===",""+ddh);
                    Log.e("预装卸重量===",""+yzxzl);
                    Log.e("流向类型===",""+lxlx);
                    Log.e("编辑时间===",""+bjsj);
                    Log.e("实际重量===",""+sjzl);
                    Log.e("重量差额比===",""+zlceb);
                    Log.e("二维码===",""+qrcodeUrl);

                    initQrcode(qrcodeUrl);

                    ddhTV.setText(ddh);
                    yzxzlTV.setText(yzxzl);
                    lxlxTV.setText(lxlx);
                    bjsjTV.setText(bjsj);
                    sjzlTV.setText(sjzl);
                    zlcebTV.setText(zlceb);
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
