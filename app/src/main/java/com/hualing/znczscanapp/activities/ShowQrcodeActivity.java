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

    private String dqphCode,ssddCode,ssddGroupId;
    private JSONObject dqphGroupsIdJO,dqphGroupsFieldIdJO,ssddJbxxFieldIdJO,ziDuanNameJO;
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
        ziDuanNameJO.put("二维码字段","二维码");
        ziDuanNameJO.put("新字段组字段","新字段组");
        ziDuanNameJO.put("所属订单字段","所属订单");
        ziDuanNameJO.put("基本信息字段","基本信息");
        ziDuanNameJO.put("订单号字段","订单号");
        ziDuanNameJO.put("预装卸重量字段","预装卸重量");
        ziDuanNameJO.put("流向类型字段","流向类型");
        ziDuanNameJO.put("编辑时间字段","编辑时间");
        ziDuanNameJO.put("实际重量字段","实际重量");
        ziDuanNameJO.put("重量差额比字段","重量差额比");
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

                    dqphGroupsIdJO=new JSONObject();
                    for (int i=0;i<groupsJA.length();i++) {
                        JSONObject groupJO = groupsJA.getJSONObject(i);
                        String title = groupJO.getString("title");
                        String id = groupJO.getString("id");
                        Log.e("title===",""+title+",id==="+id);
                        dqphGroupsIdJO.put(title,id);
                    }
                    Log.e("dqphGroupsIdJO===",dqphGroupsIdJO.toString());

                    JSONObject dqphGroupsJO = null;
                    for (int i=0;i<groupsJA.length();i++){
                        if(groupsJA.getJSONObject(i).getString("id").equals(dqphGroupsIdJO.getString(ziDuanNameJO.getString("新字段组字段")))){
                            dqphGroupsJO=groupsJA.getJSONObject(i);
                        }
                        else if(groupsJA.getJSONObject(i).getString("id").equals(dqphGroupsIdJO.getString(ziDuanNameJO.getString("所属订单字段")))){
                            ssddGroupId=groupsJA.getJSONObject(i).getString("id");
                        }
                    }
                    String dqphFields = dqphGroupsJO.getString("fields");
                    Log.e("dqphFields===",dqphFields);
                    JSONArray dqphFieldsJA = new JSONArray(dqphFields);

                    dqphGroupsFieldIdJO=new JSONObject();
                    for (int i=0;i<dqphFieldsJA.length();i++) {
                        JSONObject dqphFieldJO = (JSONObject)dqphFieldsJA.get(i);
                        String title = dqphFieldJO.getString("title");
                        String id = dqphFieldJO.getString("id");
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

    private void initSSDDJBXXFieldId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.dqphDtmplRabc+ssddGroupId, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("ssddFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("ssddSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONObject configJO = jo.getJSONObject("config");
                    JSONObject dtmplJO = configJO.getJSONObject("dtmpl");
                    JSONArray groupsJA = dtmplJO.getJSONArray("groups");
                    JSONObject jbxxGroupJO = null;
                    for(int i=0;i<groupsJA.length();i++){
                        if(groupsJA.getJSONObject(i).getString("title").equals(ziDuanNameJO.getString("基本信息字段"))){
                            jbxxGroupJO=groupsJA.getJSONObject(i);
                            break;
                        }
                    }
                    Log.e("jbxxGroupJO===",""+jbxxGroupJO);
                    ssddJbxxFieldIdJO=new JSONObject();
                    JSONArray jbxxFieldsJA = jbxxGroupJO.getJSONArray("fields");
                    for(int i=0;i<jbxxFieldsJA.length();i++){
                        JSONObject jbxxFieldsJO = jbxxFieldsJA.getJSONObject(i);
                        String title = jbxxFieldsJO.getString("title");
                        String id = jbxxFieldsJO.getString("id");
                        ssddJbxxFieldIdJO.put(title,id);
                    }
                    Log.e("ssddJbxxFieldIdJO===",""+ssddJbxxFieldIdJO);
                    getOrderDetail();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getOrderDetail(){
        RequestParams params = AsynClient.getRequestParams();
        Log.e("ssddCode===",""+ssddCode);
        Log.e("ssddGroupId===",""+ssddGroupId);
        AsynClient.get(MyHttpConfing.getDQPHDetail+ssddCode+"?fieldGroupId="+ssddGroupId, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("orderDetailFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("ordDetSuccess======", "" + rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONObject entityJO = jo.getJSONObject("entity");
                    JSONObject fieldMapJO = entityJO.getJSONObject("fieldMap");
                    String ddh = fieldMapJO.getString(ssddJbxxFieldIdJO.getString(ziDuanNameJO.getString("订单号字段")));
                    String yzxzl = fieldMapJO.getString(ssddJbxxFieldIdJO.getString(ziDuanNameJO.getString("预装卸重量字段")));
                    String lxlx = fieldMapJO.getString(ssddJbxxFieldIdJO.getString(ziDuanNameJO.getString("流向类型字段")));
                    String bjsj = fieldMapJO.getString(ssddJbxxFieldIdJO.getString(ziDuanNameJO.getString("编辑时间字段")));
                    String sjzl = fieldMapJO.getString(ssddJbxxFieldIdJO.getString(ziDuanNameJO.getString("实际重量字段")));
                    String zlceb = fieldMapJO.getString(ssddJbxxFieldIdJO.getString(ziDuanNameJO.getString("重量差额比字段")));
                    ddhTV.setText(ddh);
                    yzxzlTV.setText(yzxzl);
                    lxlxTV.setText(lxlx);
                    bjsjTV.setText(bjsj);
                    sjzlTV.setText(sjzl);
                    zlcebTV.setText(zlceb);
                    /*
                    Log.e("ddh===",""+ddh);
                    Log.e("yzxzl===",""+yzxzl);
                    Log.e("lxlx===",""+lxlx);
                    Log.e("bjsj===",""+bjsj);
                    Log.e("sjzl===",""+sjzl);
                    Log.e("zlceb===",""+zlceb);
                    */
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
                    JSONObject arrayMapJO = entityJO.getJSONObject("arrayMap");
                    ssddCode=arrayMapJO.getJSONArray(ssddGroupId).getJSONObject(0).getString("code");
                    Log.e("ssddCode===",""+ssddCode);

                    JSONObject fieldMapJO = entityJO.getJSONObject("fieldMap");
                    String qrcodeUrl = toURLString(MyHttpConfing.baseUrl+fieldMapJO.getString(dqphGroupsFieldIdJO.getString(ziDuanNameJO.getString("二维码字段"))));
                    //String qrcodeUrl = toURLString("http://121.196.184.205:96/hydrocarbon/./download-files/bbe2cac353fab4605a1d9f47b4d342bf/二维码图片_司机订单.png");
                    Log.e("qrcodeUrl===",""+qrcodeUrl);
                    initQrcode(qrcodeUrl);
                    initSSDDJBXXFieldId();
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
