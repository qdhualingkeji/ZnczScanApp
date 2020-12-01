package com.hualing.znczscanapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hualing.znczscanapp.R;
import com.hualing.znczscanapp.adapter.SimpleAdapter;
import com.hualing.znczscanapp.util.AllActivitiesHolder;
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

/*
*这个类是扫码后查询订单详情，用来化验订单是否合格的
* 现在系统改进优化了，可以一键质检通过，这个类暂时不用了
*/
public class DDXQScanActivity extends BaseActivity {

    private String zjbgCode,orderCode,orderNum;
    private JSONObject zjddGroupsFieldsIdJO,zjjeColumnsIdJO,zjbgGroupsIdJO,zjddGroupsIdJO,columnsFieldIdJO,columnsNameJO,criteriasIdJO,ziDuanNameJO;
    private SimpleAdapter jieLunAdapter;
    private String jielun;
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
    @BindView(R.id.jieLun_spinner)
    Spinner jieLunSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        try {
            orderCode = getIntent().getStringExtra("orderCode");
            orderNum = getIntent().getStringExtra("orderNum");
            Toast.makeText(this, orderCode, Toast.LENGTH_LONG).show();
            initZiDuanNameJO();
            initJieLunSpinner();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getDataFormWeb() {
        initCriteriaId();
        initZJDDGroupsFieldsId();
        initZJBGGroupsFieldsId();
    }

    private void initCriteriaId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.zjbgEntityListTmpl, this, params, new GsonHttpResponseHandler() {
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
                    criteriasIdJO=new JSONObject();
                    for(int i=0;i<criteriasJA.length();i++){
                        JSONObject criteriaJO = criteriasJA.getJSONObject(i);
                        String title = criteriaJO.getString("title");
                        String id = criteriaJO.getString("id");
                        criteriasIdJO.put(title,id);
                    }
                    Log.e("criteriasIdJO===",""+criteriasIdJO.toString());


                    JSONArray columnsJA = ltmplJO.getJSONArray("columns");
                    zjjeColumnsIdJO=new JSONObject();
                    for(int i=0;i<columnsJA.length();i++){
                        JSONObject columnJO = columnsJA.getJSONObject(i);
                        String title = columnJO.getString("title");
                        String id = columnJO.getString("id");
                        zjjeColumnsIdJO.put(title,id);
                    }
                    Log.e("zjjeColumnsIdJO===",""+zjjeColumnsIdJO.toString());
                    initQueryKey();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initQueryKey() throws JSONException {
        RequestParams params = AsynClient.getRequestParams();
        //Log.e("criteriaId===",criteriasIdJO.getString(ziDuanNameJO.getString("订单号字段")));
        //params.put("criteria_105935359844361","DD109221984123887616");
        params.put("criteria_"+criteriasIdJO.getString(ziDuanNameJO.getString("订单号字段")),orderNum);
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.zjbgEntityListTmpl, this, params, new GsonHttpResponseHandler() {
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

                    initZJBGId(queryKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initZJBGId(String queryKey){
        RequestParams params = AsynClient.getRequestParams();
        params.put("pageNo","1");
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListData.replaceAll("queryKey",queryKey), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("initZJBGIdFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("initZJBGIdSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONArray entitiesJA = jo.getJSONArray("entities");
                    JSONObject entitieJO = entitiesJA.getJSONObject(0);
                    JSONObject cellMapJO = entitieJO.getJSONObject("cellMap");
                    jielun=cellMapJO.getString(zjjeColumnsIdJO.getString(ziDuanNameJO.getString("质检结果字段")));
                    zjbgCode = entitieJO.getString("code");
                } catch (JSONException e) {
                    Log.e("error===",""+e.getMessage());
                    e.printStackTrace();
                }

            }
        });
    }

    private void initZJDDGroupsFieldsId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.zjddDtmplNormal, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("ddxqDNFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("ddxqDNSuccess======",""+rawJsonResponse);

                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String config = jo.getString("config");
                    JSONObject configJO = new JSONObject(config);
                    String dtmpl = configJO.getString("dtmpl");
                    JSONObject dtmplJO = new JSONObject(dtmpl);
                    JSONArray groupsJA=new JSONArray(dtmplJO.getString("groups"));

                    zjddGroupsIdJO=new JSONObject();
                    for (int i=0;i<groupsJA.length();i++) {
                        JSONObject groupJO = groupsJA.getJSONObject(i);
                        String title = groupJO.getString("title");
                        String id = groupJO.getString("id");
                        Log.e("title===",""+title+",id==="+id);
                        zjddGroupsIdJO.put(title,id);
                    }
                    Log.e("zjddGroupsIdJO===",zjddGroupsIdJO.toString());

                    JSONObject groupsJO = null;
                    for (int i=0;i<groupsJA.length();i++){
                        if(groupsJA.getJSONObject(i).getString("id").equals(zjddGroupsIdJO.getString(ziDuanNameJO.getString("基本信息字段")))){
                            groupsJO=groupsJA.getJSONObject(i);
                            break;
                        }
                    }

                    String fields = groupsJO.getString("fields");
                    JSONArray fieldsJA = new JSONArray(fields);

                    zjddGroupsFieldsIdJO=new JSONObject();
                    for (int i=0;i<fieldsJA.length();i++) {
                        JSONObject fieldJO = (JSONObject)fieldsJA.get(i);
                        String title = fieldJO.getString("title");
                        String id = fieldJO.getString("id");
                        //Log.e("title===",""+title+",id==="+id);
                        zjddGroupsFieldsIdJO.put(title,id);
                    }
                    Log.e("zjddGroupsFieldsIdJO===",zjddGroupsFieldsIdJO.toString());

                    getOrderDetail();
                } catch (JSONException e) {
                    Log.e("???????","???????");
                    e.printStackTrace();
                }
            }
        });
    }

    private void initZJBGGroupsFieldsId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.zjbgDtmplNormal, this, params, new GsonHttpResponseHandler() {
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

                    zjbgGroupsIdJO=new JSONObject();
                    for (int i=0;i<groupsJA.length();i++) {
                        JSONObject groupJO = groupsJA.getJSONObject(i);
                        String title = groupJO.getString("title");
                        String id = groupJO.getString("id");
                        Log.e("title===",""+title+",id==="+id);
                        zjbgGroupsIdJO.put(title,id);
                    }
                    Log.e("zjbgGroupsIdJO===",zjbgGroupsIdJO.toString());

                    JSONObject groupsJO = null;
                    for (int i=0;i<groupsJA.length();i++){
                        if(groupsJA.getJSONObject(i).getString("id").equals(zjbgGroupsIdJO.getString(ziDuanNameJO.getString("基本属性组字段")))){
                            groupsJO=groupsJA.getJSONObject(i);
                            break;
                        }
                    }
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
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.initFieldOptions, this, params, new GsonHttpResponseHandler() {
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
                    //list.clear();
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

    private void getOrderDetail(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getZJOrderDetail+orderCode, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("rawJsonData3======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse3======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String status = jo.getString("status");
                    if("suc".equals(status)){
                        String entity = jo.getString("entity");
                        JSONObject entityJO = new JSONObject(entity);
                        String fieldMapStr = entityJO.getString("fieldMap");
                        JSONObject fieldMapJO = new JSONObject(fieldMapStr);
                        String ddh = fieldMapJO.getString(zjddGroupsFieldsIdJO.getString(ziDuanNameJO.getString("订单号字段")));
                        String yzxzl=fieldMapJO.getString(zjddGroupsFieldsIdJO.getString(ziDuanNameJO.getString("预装卸重量字段")));
                        String lxlx = fieldMapJO.getString(zjddGroupsFieldsIdJO.getString(ziDuanNameJO.getString("流向类型字段")));
                        String bjsj=fieldMapJO.getString(zjddGroupsFieldsIdJO.getString(ziDuanNameJO.getString("编辑时间字段")));
                        String sjzl=fieldMapJO.getString(zjddGroupsFieldsIdJO.getString(ziDuanNameJO.getString("实际重量字段")));
                        String zlceb=fieldMapJO.getString(zjddGroupsFieldsIdJO.getString(ziDuanNameJO.getString("重量差额比字段")));
                        Log.e("订单号===",ddh);
                        Log.e("预装卸重量===",yzxzl);
                        Log.e("流向类型===",lxlx);
                        Log.e("编辑时间===",bjsj);
                        Log.e("二维码===",fieldMapJO.getString(zjddGroupsFieldsIdJO.getString(ziDuanNameJO.getString("二维码字段"))));
                        Log.e("实际重量===",sjzl);
                        Log.e("重量差额比===",zlceb);

                        ddhTV.setText(ddh);
                        yzxzlTV.setText(yzxzl);
                        lxlxTV.setText(lxlx);
                        bjsjTV.setText(bjsj);
                        sjzlTV.setText(sjzl);
                        zlcebTV.setText(zlceb);

                        jieLunSpinner.setSelection(getValueIndexInList(jielun,jieLunAdapter.getList()),true);
                    }
                } catch (JSONException e) {
                    Log.e("error===",""+e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private int getValueIndexInList(String value,List<String> list){
        int index=0;
        for(int i=0;i<list.size();i++){
            if(value.equals(list.get(i))){
                index=i;
                break;
            }
        }
        return index;
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
        ziDuanNameJO.put("基本属性组字段","基本属性组");
        ziDuanNameJO.put("基本信息字段","基本信息");
    }

    private void  initJieLunSpinner(){
        jieLunAdapter = new SimpleAdapter(DDXQScanActivity.this);
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

    @OnClick({R.id.saveBtn})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.saveBtn:
                try {
                    if(checkJieLunValue()) {
                        saveZhiJianBaoGao();
                    }
                } catch (JSONException e) {
                    Log.e("error===",""+e.getMessage());
                    e.printStackTrace();
                }
                break;
        }
    }

    private boolean checkJieLunValue(){
        if(TextUtils.isEmpty(jielun)||jieLunAdapter.NO_SELECTED.equals(jielun)){
            MyToast("请选择结论");
            return false;
        }
        else
            return true;
    }

    private void saveZhiJianBaoGao() throws JSONException {
        RequestParams params = AsynClient.getRequestParams();
        params.put("唯一编码",zjbgCode);
        params.put(columnsNameJO.getString(ziDuanNameJO.getString("结论字段")), jielun);
        //params.put("%fuseMode%",false);
        //params.put("货运订单48[1].$$label$$","关联订单");
        //params.put("货运订单48[1].订单号",ddhTV.getText().toString());
        //data["货运订单48[1].唯一编码"]="337525032";
        //data["货运订单48[1].重量差额比"]=1;
        //data["%fuseMode%"]=false;
        //data["货运订单48.$$flag$$"]=true;
        AsynClient.post(MyHttpConfing.getBaseUrl()+MyHttpConfing.saveZhiJianBaoGao, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("rawJsonData4======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse4======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                String status=jo.getString("status");
                    if("suc".equals(status)){
                        MyToast("质检完毕，"+jielun);
                        Intent intent = new Intent(DDXQScanActivity.this, MainActivity.class);
                        startActivity(intent);
                        AllActivitiesHolder.removeAct(DDXQScanActivity.this);
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
        return R.layout.activity_ddxq_scan;
    }

    public void MyToast(String s) {
        Toast.makeText(DDXQScanActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
