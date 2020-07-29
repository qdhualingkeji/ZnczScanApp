package com.hualing.znczscanapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.utils.AsynClient;
import com.hualing.znczscanapp.utils.GsonHttpResponseHandler;
import com.hualing.znczscanapp.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class PaiDuiChaXunActivity extends BaseActivity {

    private String zyddCode;
    private JSONObject columnsIdJO,criteriasIdJO,zyddGroupsIdJO,ziDuanNameJO;
    @BindView(R.id.pdh_tv)
    TextView pdhTV;
    @BindView(R.id.prsj_tv)
    TextView prsjTV;

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
        ziDuanNameJO.put("序号字段","序号");
        ziDuanNameJO.put("号码字段","号码");
        ziDuanNameJO.put("排队号字段","排队号");
        ziDuanNameJO.put("订单号字段","订单号");
        ziDuanNameJO.put("排入时间字段","排入时间");
        ziDuanNameJO.put("分类字段","分类");
        ziDuanNameJO.put("状态字段","状态");
        ziDuanNameJO.put("承运车辆字段","承运车辆");
    }

    private void initLtmplAttr(){
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
                    JSONObject jo=new JSONObject(rawJsonResponse);
                    JSONObject ltmplJO = jo.getJSONObject("ltmpl");
                    JSONArray criteriasJA = ltmplJO.getJSONArray("criterias");

                    JSONArray columnsJA = ltmplJO.getJSONArray("columns");

                    initCriteriasId(criteriasJA);
                    initColumnsId(columnsJA);
                    initQueryKey();
                } catch (JSONException e) {
                    Log.e("error===",e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void initCriteriasId(JSONArray criteriasJA) throws JSONException {
        criteriasIdJO=new JSONObject();
        for(int i=0;i<criteriasJA.length();i++){
            JSONObject criteriaJO = (JSONObject)criteriasJA.get(i);
            String title = criteriaJO.getString("title");
            String id = criteriaJO.getString("id");
            //Log.e("title1===",title);
            //Log.e("id1===",id);
            criteriasIdJO.put(title,id);
        }
    }

    @Override
    protected void getDataFormWeb() {
        initLtmplAttr();
        initZYDDQueryKey();
    }

    private void initZYDDQueryKey(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.zyddEntityListTmpl, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("zyddELTFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("zyddELTSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String queryKey = jo.getString("queryKey");
                    initZYDDCode(queryKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initZYDDCode(String queryKey){
        RequestParams params = AsynClient.getRequestParams();
        params.put("pageNo","1");
        AsynClient.get(MyHttpConfing.getEntityListData.replaceAll("queryKey",queryKey), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("zYDDCodeFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("zYDDCodeSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONArray entitiesJA = jo.getJSONArray("entities");
                    JSONObject entitieJO = entitiesJA.getJSONObject(0);
                    zyddCode = entitieJO.getString("code");
                    Log.e("zyddCode===",""+zyddCode);
                    initZYDDGroupsFieldId();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initZYDDGroupsFieldId() {
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.zyddDtmplNormal, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("zyddFail======", "" + rawJsonData + "," + errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("zyddSuccess======", "" + rawJsonResponse);

                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String config = jo.getString("config");
                    JSONObject configJO = null;
                    configJO = new JSONObject(config);
                    String dtmpl = configJO.getString("dtmpl");
                    JSONObject dtmplJO = new JSONObject(dtmpl);
                    JSONArray groupsJA=new JSONArray(dtmplJO.getString("groups"));

                    zyddGroupsIdJO=new JSONObject();
                    for (int i=0;i<groupsJA.length();i++) {
                        JSONObject groupJO = groupsJA.getJSONObject(i);
                        String title = groupJO.getString("title");
                        String id = groupJO.getString("id");
                        Log.e("title===",""+title+",id==="+id);
                        zyddGroupsIdJO.put(title,id);
                    }
                    Log.e("zyddGroupsIdJO===",zyddGroupsIdJO.toString());
                    initCYCLCode();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initCYCLCode(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getZYDDDetail+zyddCode, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("zyddDetailFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("zyddDetailSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONObject entityJO = jo.getJSONObject("entity");
                    JSONObject arrayMapJO = entityJO.getJSONObject("arrayMap");
                    JSONArray cyclJA = arrayMapJO.getJSONArray(zyddGroupsIdJO.getString(ziDuanNameJO.getString("承运车辆字段")));
                    JSONObject cyclJO = cyclJA.getJSONObject(0);
                    String code = cyclJO.getString("code");
                    Log.e("code===",""+code);
                } catch (JSONException e) {
                    Log.e("error===",""+e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void initColumnsId(JSONArray columnsJA) throws JSONException {
        columnsIdJO=new JSONObject();
        for (int i=0;i<columnsJA.length();i++){
            JSONObject columnJO = (JSONObject)columnsJA.get(i);
            String title = columnJO.getString("title");
            String id = columnJO.getString("id");
            //Log.e("title===",""+title+",id==="+id);
            columnsIdJO.put(title,id);
        }
        //Log.e("columnsIdJO===",columnsIdJO.toString());

    }

    private void initQueryKey() throws JSONException {
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.dqphEntityListTmpl, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("pdcxFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("pdcxSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String queryKey = jo.getString("queryKey");

                    initListData(queryKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initListData(String queryKey) {
        RequestParams params = AsynClient.getRequestParams();
        params.put("pageNo","1");
        AsynClient.get(MyHttpConfing.getEntityListData.replaceAll("queryKey",queryKey), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("pdcxDataFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("pdcxDataSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String entities = jo.getString("entities");
                    JSONArray entitiesJA = new JSONArray(entities);
                    JSONObject entityJO = (JSONObject)entitiesJA.get(0);
                    JSONObject cellMapJO = entityJO.getJSONObject("cellMap");
                    String pdh = cellMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("排队号字段")));
                    String prsj = cellMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("排入时间字段")));
                    pdhTV.setText(pdh);
                    prsjTV.setText(prsj);
                } catch (JSONException e) {
                    Log.e("?????",e.getMessage());
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
        return R.layout.activity_pai_dui_cha_xun;
    }
}
