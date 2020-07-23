package com.hualing.znczscanapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.util.AllActivitiesHolder;
import com.hualing.znczscanapp.utils.AsynClient;
import com.hualing.znczscanapp.utils.GsonHttpResponseHandler;
import com.hualing.znczscanapp.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderRKActivity extends BaseActivity {

    private String orderCode;
    private String[] lxlxArr,zxztArr,rkztArr;
    private JSONObject columnsIdJO,columnsFieldIdJO,ziDuanNameJO;
    private ArrayAdapter<String> lxlxAdapter;
    private ArrayAdapter<String> zxztAdapter;
    private ArrayAdapter<String> rkztAdapter;
    private String lxlx,zxzt,rkzt;
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
    @BindView(R.id.lxlx_spinner)
    Spinner lxlxSpinner;
    @BindView(R.id.zxzt_spinner)
    Spinner zxztSpinner;
    @BindView(R.id.rkzt_spinner)
    Spinner rkztSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        try {
            orderCode = getIntent().getStringExtra("orderCode");
            initZiDuanNameJO();
            initLLLXSpinner();
            initZXZTSpinner();
            initRKZTSpinner();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getDataFormWeb() {
        initColumnsId();
        initColumnsFieldId();
    }

    private void initColumnsId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.ddxqDtmplNormal, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("rawJsonData5======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse5======",""+rawJsonResponse);

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

                    getOrderDetail();
                } catch (JSONException e) {
                    Log.e("???????","???????");
                    e.printStackTrace();
                }
            }
        });
    }

    private void initColumnsFieldId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.ddrkDtmplNormal, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("ddrkFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("ddrkSuccess======",""+rawJsonResponse);

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
                    for (int i=0;i<fieldsJA.length();i++) {
                        JSONObject fieldJO = (JSONObject)fieldsJA.get(i);
                        String title = fieldJO.getString("title");
                        String fieldId = fieldJO.getString("fieldId");
                        Log.e("title===",""+title+",fieldId==="+fieldId);
                        columnsFieldIdJO.put(title,fieldId);
                    }
                    Log.e("columnsFieldIdJO===",columnsFieldIdJO.toString());

                    initAdapterDataArr(columnsFieldIdJO.getString(ziDuanNameJO.getString("流向类型字段")),lxlxArr,lxlxAdapter);
                    initAdapterDataArr(columnsFieldIdJO.getString(ziDuanNameJO.getString("执行状态字段")),zxztArr,zxztAdapter);
                    initAdapterDataArr(columnsFieldIdJO.getString(ziDuanNameJO.getString("入库状态字段")),rkztArr,rkztAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initAdapterDataArr(final String fieldId, final String[] dataArr, final ArrayAdapter<String> adapter){
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
                    for(int i=0;i<lxlxJA.length();i++){
                        JSONObject lxlxJO=(JSONObject)lxlxJA.get(i);
                        dataArr[i]=lxlxJO.getString("value");
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getOrderDetail(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getOrderDetail+orderCode, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("rawJsonData6======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse6======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String status = jo.getString("status");
                    if("suc".equals(status)){
                        String entity = jo.getString("entity");
                        JSONObject entityJO = new JSONObject(entity);
                        String fieldMapStr = entityJO.getString("fieldMap");
                        JSONObject fieldMapJO = new JSONObject(fieldMapStr);
                        String ddh = fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("订单号字段")));
                        String yzxzl=fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("预装卸重量字段")));
                        String lxlx = fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("流向类型字段")));
                        String bjsj=fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("编辑时间字段")));
                        String sjzl=fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("实际重量字段")));
                        String zlceb=fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("重量差额比字段")));
                        Log.e("订单号===",ddh);
                        Log.e("预装卸重量===",yzxzl);
                        Log.e("流向类型===",lxlx);
                        Log.e("编辑时间===",bjsj);
                        Log.e("二维码===",fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("二维码字段"))));
                        Log.e("实际重量===",sjzl);
                        Log.e("重量差额比===",zlceb);

                        ddhTV.setText("订单号:"+ddh);
                        yzxzlTV.setText("预装卸重量:"+yzxzl);
                        lxlxTV.setText("流向类型:"+lxlx);
                        bjsjTV.setText("编辑时间:"+bjsj);
                        sjzlTV.setText("实际重量:"+sjzl);
                        zlcebTV.setText("重量差额比:"+zlceb);
                    }
                } catch (JSONException e) {
                    Log.e("??????",""+e.getMessage());
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
        ziDuanNameJO.put("执行状态字段","执行状态");
        ziDuanNameJO.put("入库状态字段","入库状态");
        ziDuanNameJO.put("实际重量字段","实际重量");
        ziDuanNameJO.put("重量差额比字段","重量差额比");
        ziDuanNameJO.put("计划运输日期字段","计划运输日期");
        ziDuanNameJO.put("出入库时间字段","出入库时间");
        ziDuanNameJO.put("二维码字段","二维码");
    }

    private void  initLLLXSpinner(){
        lxlxArr=new String[2];
        lxlxArr[0]="";
        lxlxAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lxlxArr);
        lxlxSpinner.setAdapter(lxlxAdapter);
        lxlxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lxlx=lxlxArr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void  initZXZTSpinner(){
        zxztArr=new String[3];
        zxztArr[0]="";
        zxztAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,zxztArr);
        zxztSpinner.setAdapter(zxztAdapter);
        zxztSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zxzt=zxztArr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void  initRKZTSpinner(){
        rkztArr=new String[3];
        rkztArr[0]="";
        rkztAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,rkztArr);
        rkztSpinner.setAdapter(rkztAdapter);
        rkztSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rkzt=rkztArr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick({R.id.saveBtn})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.saveBtn:
                saveOrderRK();
                break;
        }
    }

    private void saveOrderRK(){
        RequestParams params = AsynClient.getRequestParams();
        params.put("唯一编码", "108473798673440770");
        params.put("预装卸重量", yzxzlTV.getText().toString());
        params.put("实际重量", sjzlTV.getText().toString());
        params.put("重量差额比", zlcebTV.getText().toString());
        params.put("流向类型", lxlx);
        params.put("执行状态", zxzt);
        params.put("入库状态", rkzt);
        /*
        params.put("计划运输日期", "");
        params.put("出入库时间", "");
        params.put("二维码", "");
        params.put("企业客户信息37.$$flag$$", "true");
        params.put("车辆管理63.$$flag$$", "true");
        params.put("企业客户信息81.$$flag$$", "true");
        params.put("司机信息43.$$flag$$", "true");
        params.put("系统用户80.$$flag$$", "true");
        params.put("系统用户74.$$flag$$", "true");
        */
        AsynClient.post(MyHttpConfing.saveOrderRK, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("rawJsonData7======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse7======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String status=jo.getString("status");
                    if("suc".equals(status)){
                        Intent intent = new Intent(OrderRKActivity.this, MainActivity.class);
                        startActivity(intent);
                        AllActivitiesHolder.removeAct(OrderRKActivity.this);
                    }
                } catch (JSONException e) {
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
        return R.layout.activity_order_rk;
    }
}
