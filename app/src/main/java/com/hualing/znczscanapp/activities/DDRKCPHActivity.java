package com.hualing.znczscanapp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import butterknife.OnClick;

public class DDRKCPHActivity extends BaseActivity {

    private String ddrkCode;
    List<String> lxlxList,zxztList,rkztList;
    private JSONObject ddrkColumnsIdJO,ddrkCriteriasIdJO,ziDuanNameJO;
    private SimpleAdapter lxlxAdapter,zxztAdapter,rkztAdapter;
    private String lxlx,zxzt,rkzt;
    @BindView(R.id.cph_et)
    EditText cphET;
    @BindView(R.id.ddh_tv)
    TextView ddhTV;
    @BindView(R.id.yzxzl_tv)
    TextView yzxzlTV;
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
    @BindView(R.id.jhysrq_tv)
    TextView jhysrqTV;
    @BindView(R.id.crkrq_tv)
    TextView crkrqTV;
    @BindView(R.id.crksj_tv)
    TextView crksjTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        try {
            initZiDuanNameJO();
            initLLLXSpinner();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getDataFormWeb() {
        initDDRKCriteriaId();
    }

    private void initDDRKCriteriaId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.ddrkEntityListTmpl, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("criteriaIdFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("criteriaIdSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONObject ltmplJO = jo.getJSONObject("ltmpl");
                    JSONArray criteriasJA = ltmplJO.getJSONArray("criterias");
                    ddrkCriteriasIdJO=new JSONObject();
                    for(int i=0;i<criteriasJA.length();i++){
                        JSONObject criteriaJO = criteriasJA.getJSONObject(i);
                        String title = criteriaJO.getString("title");
                        String id = criteriaJO.getString("id");
                        ddrkCriteriasIdJO.put(title,id);
                    }
                    Log.e("ddrkCriteriasIdJO===",""+ddrkCriteriasIdJO.toString());


                    JSONArray columnsJA = ltmplJO.getJSONArray("columns");
                    ddrkColumnsIdJO=new JSONObject();
                    for(int i=0;i<columnsJA.length();i++){
                        JSONObject columnJO = columnsJA.getJSONObject(i);
                        String title = columnJO.getString("title");
                        String id = columnJO.getString("id");
                        ddrkColumnsIdJO.put(title,id);
                    }
                    Log.e("ddrkColumnsIdJO===",""+ddrkColumnsIdJO.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initDDRKQueryKey() throws JSONException {
        RequestParams params = AsynClient.getRequestParams();
        String cph=cphET.getText().toString();
        params.put("criteria_"+ddrkCriteriasIdJO.getString(ziDuanNameJO.getString("执行状态字段")),"运输中");
        params.put("criteria_"+ddrkCriteriasIdJO.getString(ziDuanNameJO.getString("车牌号字段")),cph);
        AsynClient.get(MyHttpConfing.ddrkEntityListTmpl, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("queryKeyFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("queryKeySuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String queryKey = jo.getString("queryKey");

                    initDDRKCode(queryKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initDDRKCode(String queryKey) {
        RequestParams params = AsynClient.getRequestParams();
        params.put("pageNo", "1");
        AsynClient.get(MyHttpConfing.getEntityListData.replaceAll("queryKey", queryKey), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("DDRKFail======", "" + rawJsonData + "," + errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("DDRKSuccess======", "" + rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONArray entitiesJA = jo.getJSONArray("entities");
                    JSONObject entitieJO = entitiesJA.getJSONObject(0);
                    JSONObject cellMapJO = entitieJO.getJSONObject("cellMap");
                    lxlx = cellMapJO.getString(ddrkColumnsIdJO.getString(ziDuanNameJO.getString("流向类型字段")));
                    ddrkCode = entitieJO.getString("code");
                    Log.e("lxlx===", "" + lxlx);
                    Log.e("ddrkCode===", "" + ddrkCode);
                } catch (JSONException e) {
                    Log.e("error===", "" + e.getMessage());
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
        ziDuanNameJO.put("车牌号字段","车牌号");
    }

    private void  initLLLXSpinner(){
        lxlxList=new ArrayList<String>();
        lxlxList.add("");
        lxlxAdapter = new SimpleAdapter(DDRKCPHActivity.this);
        lxlxSpinner.setAdapter(lxlxAdapter);
        lxlxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lxlx=lxlxAdapter.getList().get(position);
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
        return R.layout.activity_ddrkcph;
    }

    @OnClick({R.id.cxdd_but,R.id.saveBtn})
    public void onViewClicked(View v){
        switch (v.getId()) {
            case R.id.cxdd_but:
                try {
                    if(checkCPHValue()) {
                        initDDRKQueryKey();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.saveBtn:
                /*
                try {
                    if(checkDDHValue()) {
                        //saveZhiJianBaoGao();
                    }
                } catch (JSONException e) {
                    Log.e("error===",""+e.getMessage());
                    e.printStackTrace();
                }
                */
                break;
        }
    }

    private boolean checkCPHValue(){
        String cph=cphET.getText().toString();
        if(TextUtils.isEmpty(cph)){
            MyToast("请输入车牌号");
            return false;
        }
        else
            return true;
    }

    private boolean checkDDHValue(){
        String ddh=ddhTV.getText().toString();
        if(TextUtils.isEmpty(ddh)){
            MyToast("请输入订单号");
            return false;
        }
        else
            return true;
    }

    public void MyToast(String s) {
        Toast.makeText(DDRKCPHActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
