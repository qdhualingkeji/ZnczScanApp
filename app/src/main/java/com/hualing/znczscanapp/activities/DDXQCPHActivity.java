package com.hualing.znczscanapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.adapter.SimpleAdapter;
import com.hualing.znczscanapp.utils.AsynClient;
import com.hualing.znczscanapp.utils.GsonHttpResponseHandler;
import com.hualing.znczscanapp.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DDXQCPHActivity extends BaseActivity {

    private List<String> jieLunList;
    private JSONObject columnsIdJO,zjjeColumnsIdJO,columnsFieldIdJO,columnsNameJO,criteriasIdJO,ziDuanNameJO;
    private SimpleAdapter jieLunAdapter;
    private String jielun;
    @BindView(R.id.jieLun_spinner)
    Spinner jieLunSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        try {
            initZiDuanNameJO();
            initJieLunSpinner();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getDataFormWeb() {
        initColumnsFieldId();
    }

    private void initColumnsFieldId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.zjbgDtmplNormal, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("zjbgFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("zjbgSuccess======",""+rawJsonResponse);

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

                    columnsFieldIdJO=new JSONObject();
                    columnsNameJO=new JSONObject();
                    for (int i=0;i<fieldsJA.length();i++) {
                        JSONObject fieldJO = (JSONObject)fieldsJA.get(i);
                        String title = fieldJO.getString("title");
                        String fieldId = fieldJO.getString("fieldId");
                        Log.e("title===",""+title+",fieldId==="+fieldId);
                        String name = fieldJO.getString("name");
                        columnsFieldIdJO.put(title,fieldId);
                        columnsNameJO.put(title,name);
                    }
                    Log.e("columnsFieldIdJO===",columnsFieldIdJO.toString());
                    Log.e("columnsNameJO===",columnsNameJO.toString());

                    initAdapterDataArr(columnsFieldIdJO.getString(ziDuanNameJO.getString("结论字段")),jieLunAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initAdapterDataArr(final String fieldId, final SimpleAdapter adapter){
        RequestParams params = AsynClient.getRequestParams();
        params.put("fieldIds",fieldId);
        AsynClient.get(MyHttpConfing.initFieldOptions, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("FieldOptionsFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("FieldOptions======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String optionsMapStr = jo.getString("optionsMap");
                    JSONObject optionsMapJO = new JSONObject(optionsMapStr);
                    String lxlxJAStr = optionsMapJO.getString(fieldId);
                    JSONArray lxlxJA = new JSONArray(lxlxJAStr);
                    List<String> list = adapter.getList();
                    list.clear();
                    for(int i=0;i<lxlxJA.length();i++){
                        JSONObject lxlxJO=(JSONObject)lxlxJA.get(i);
                        list.add(lxlxJO.getString("value"));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
     *设置字段键名称
     */
    private void initZiDuanNameJO() throws JSONException {
        ziDuanNameJO=new JSONObject();
        ziDuanNameJO.put("订单号字段","订单号");
        ziDuanNameJO.put("预装卸重量字段","预装卸重量");
        ziDuanNameJO.put("流向类型字段","流向类型");
        ziDuanNameJO.put("编辑时间字段","编辑时间");
        ziDuanNameJO.put("二维码字段","二维码");
        ziDuanNameJO.put("实际重量字段","实际重量");
        ziDuanNameJO.put("重量差额比字段","重量差额比");
        ziDuanNameJO.put("结论字段","结论");
        ziDuanNameJO.put("质检结果字段","质检结果");
    }

    private void  initJieLunSpinner(){
        jieLunList=new ArrayList<String>();
        jieLunList.add("");
        jieLunAdapter = new SimpleAdapter(DDXQCPHActivity.this);
        jieLunSpinner.setAdapter(jieLunAdapter);
        jieLunSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jielun=jieLunAdapter.getList().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ddxqcph;
    }
}
