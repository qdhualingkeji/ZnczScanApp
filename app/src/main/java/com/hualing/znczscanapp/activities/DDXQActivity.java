package com.hualing.znczscanapp.activities;

import android.os.Bundle;
import android.util.Log;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.utils.AsynClient;
import com.hualing.znczscanapp.utils.GsonHttpResponseHandler;
import com.hualing.znczscanapp.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DDXQActivity extends BaseActivity {

    private String orderCode="132735811812368398";
    private JSONObject zhcxGroupsIdJO,jbxxFieldsIdJO,yssFieldsIdJO,wlxxFieldsIdJO,ziDuanNameJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        try {
            initZiDuanNameJO();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     *设置字段键名称
     */
    private void initZiDuanNameJO() throws JSONException {
        ziDuanNameJO=new JSONObject();
        ziDuanNameJO.put("基本信息字段","基本信息");
        ziDuanNameJO.put("运输商字段","运输商");
        ziDuanNameJO.put("名称字段","名称");
        ziDuanNameJO.put("物料信息字段","物料信息");
    }

    @Override
    protected void getDataFormWeb() {
        initZHCXGroupsId();
    }
    private void initZHCXGroupsId() {
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.doDtmplNormal.replaceAll("menuId",MyHttpConfing.bqglDdcxZhcxMenuId), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("zhcxFail======", "" + rawJsonData + "," + errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("zhcxSuccess======", "" + rawJsonResponse);

                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String config = jo.getString("config");
                    JSONObject configJO = null;
                    configJO = new JSONObject(config);
                    String dtmpl = configJO.getString("dtmpl");
                    JSONObject dtmplJO = new JSONObject(dtmpl);
                    JSONArray groupsJA=new JSONArray(dtmplJO.getString("groups"));

                    zhcxGroupsIdJO=new JSONObject();
                    for (int i=0;i<groupsJA.length();i++) {
                        JSONObject groupJO = groupsJA.getJSONObject(i);
                        String title = groupJO.getString("title");
                        String id = groupJO.getString("id");
                        Log.e("title===",""+title+",id==="+id);
                        if(ziDuanNameJO.getString("基本信息字段").equals(title)){
                            jbxxFieldsIdJO=new JSONObject();
                            JSONArray jbxxFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j<jbxxFieldsJA.length();j++){
                                JSONObject jbxxFieldsJO=jbxxFieldsJA.getJSONObject(j);
                                String jbxxTitle = jbxxFieldsJO.getString("title");
                                String jbxxId = jbxxFieldsJO.getString("id");
                                jbxxFieldsIdJO.put(jbxxTitle,jbxxId);
                            }
                        }
                        else if(ziDuanNameJO.getString("运输商字段").equals(title)){
                            yssFieldsIdJO=new JSONObject();
                            JSONArray  yssFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< yssFieldsJA.length();j++){
                                JSONObject  yssFieldsJO= yssFieldsJA.getJSONObject(j);
                                String  yssTitle =  yssFieldsJO.getString("title");
                                String  yssId =  yssFieldsJO.getString("id");
                                yssFieldsIdJO.put( yssTitle, yssId);
                            }
                        }
                        else if(ziDuanNameJO.getString("物料信息字段").equals(title)){
                            wlxxFieldsIdJO=new JSONObject();
                            JSONArray  wlxxFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< wlxxFieldsJA.length();j++){
                                JSONObject  wlxxFieldsJO= wlxxFieldsJA.getJSONObject(j);
                                String  wlxxTitle =  wlxxFieldsJO.getString("title");
                                String  wlxxId =  wlxxFieldsJO.getString("id");
                                wlxxFieldsIdJO.put( wlxxTitle, wlxxId);
                            }
                        }
                        zhcxGroupsIdJO.put(title,id);
                    }
                    Log.e("zhcxGroupsIdJO===",zhcxGroupsIdJO.toString());
                    Log.e("jbxxFieldsIdJO===",jbxxFieldsIdJO.toString());
                    Log.e("yssFieldsIdJO===",yssFieldsIdJO.toString());
                    Log.e("wlxxFieldsIdJO===",wlxxFieldsIdJO.toString());

                    initZHCXDetail();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initZHCXDetail(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityDetail.replaceAll("menuId",MyHttpConfing.bqglDdcxZhcxMenuId)+orderCode, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("zhcxDetailFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("zhcxDetailSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONObject entityJO = jo.getJSONObject("entity");
                    JSONObject arrayMapJO = entityJO.getJSONObject("arrayMap");
                    initYSSDetail(arrayMapJO);
                } catch (JSONException e) {
                    Log.e("error===",""+e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void initYSSDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray yssJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("运输商字段")));
        JSONObject yssJO = yssJA.getJSONObject(0);
        Log.e("yssJO===",""+yssJO.toString());
        String mc=yssJO.getJSONObject("fieldMap").getString(yssFieldsIdJO.getString(ziDuanNameJO.getString("名称字段")));
        Log.e("mc===",mc);
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ddxq;
    }
}
