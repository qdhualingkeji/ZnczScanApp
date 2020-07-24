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

public class PaiDuiChaXunActivity extends BaseActivity {

    private JSONObject columnsIdJO,ziDuanNameJO;

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
        ziDuanNameJO.put("排入时间字段","排入时间");
        ziDuanNameJO.put("分类字段","分类");
        ziDuanNameJO.put("状态字段","状态");
    }

    @Override
    protected void getDataFormWeb() {
        initColumnsId();
    }

    private void initColumnsId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.pdcxEntityListTmpl, this, params, new GsonHttpResponseHandler() {
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
                    String ltmpl = jo.getString("ltmpl");
                    JSONObject ltmplJO = new JSONObject(ltmpl);
                    String columns = ltmplJO.getString("columns");
                    JSONArray columnsJA=new JSONArray(columns);

                    columnsIdJO=new JSONObject();
                    for (int i=0;i<columnsJA.length();i++){
                        JSONObject columnJO = (JSONObject)columnsJA.get(i);
                        String title = columnJO.getString("title");
                        String id = columnJO.getString("id");
                        Log.e("title===",""+title+",id==="+id);
                        columnsIdJO.put(title,id);
                    }
                    Log.e("columnsIdJO===",columnsIdJO.toString());

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
        params.put("pageSize","100");
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
                    for (int i=0;i<entitiesJA.length();i++){
                        JSONObject entityJO = (JSONObject)entitiesJA.get(i);
                        JSONObject cellMapJO = entityJO.getJSONObject("cellMap");
                        Log.e("===",columnsIdJO.getString(ziDuanNameJO.getString("排队号字段")));
                        String pdh = cellMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("排队号字段")));
                        //cellMapJO.getString("");
                        //cellMapJO.getString("");
                        //cellMapJO.getString("");
                        Log.e("pdh===",pdh);
                    }
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
