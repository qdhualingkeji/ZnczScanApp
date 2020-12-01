package com.hualing.znczscanapp.activities;

import android.content.Intent;
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
import com.hualing.znczscanapp.util.AllActivitiesHolder;
import com.hualing.znczscanapp.utils.AsynClient;
import com.hualing.znczscanapp.utils.GsonHttpResponseHandler;
import com.hualing.znczscanapp.utils.MyHttpConfing;
import com.hualing.znczscanapp.widget.TitleBar;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DDXQCPHOldActivity extends BaseActivity {

    private String zjbgCode,zjddCode;
    private JSONObject zjddGroupsFieldsJO,zjjeColumnsIdJO,zjbgGroupsIdJO,zjddGroupsIdJO,zjbgGroupsFieldFieldIdJO,zjbgGroupsFieldNameJO,zjbgCriteriasIdJO,zjddCriteriasIdJO,ziDuanNameJO;
    private SimpleAdapter jieLunAdapter;
    private String jielun;
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.cph_et)
    EditText cphET;
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
            mTitle.setEvents(new TitleBar.AddClickEvents() {
                @Override
                public void clickLeftButton() {
                    AllActivitiesHolder.removeAct(DDXQCPHOldActivity.this);
                }

                @Override
                public void clickRightButton() {

                }
            });

            initZiDuanNameJO();
            initJieLunSpinner();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getDataFormWeb() {
        initZJBGCriteriaId();
        initZJDDCriteriaId();
        initZJBGGroupsFieldId();
    }

    private void initZJBGCriteriaId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListTmpl.replaceAll("menuId",MyHttpConfing.zjbgMenuId), this, params, new GsonHttpResponseHandler() {
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
                    zjbgCriteriasIdJO=new JSONObject();
                    for(int i=0;i<criteriasJA.length();i++){
                        JSONObject criteriaJO = criteriasJA.getJSONObject(i);
                        String title = criteriaJO.getString("title");
                        String id = criteriaJO.getString("id");
                        zjbgCriteriasIdJO.put(title,id);
                    }
                    Log.e("zjbgCriteriasIdJO===",""+zjbgCriteriasIdJO.toString());


                    JSONArray columnsJA = ltmplJO.getJSONArray("columns");
                    zjjeColumnsIdJO=new JSONObject();
                    for(int i=0;i<columnsJA.length();i++){
                        JSONObject columnJO = columnsJA.getJSONObject(i);
                        String title = columnJO.getString("title");
                        String id = columnJO.getString("id");
                        zjjeColumnsIdJO.put(title,id);
                    }
                    Log.e("zjjeColumnsIdJO===",""+zjjeColumnsIdJO.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initZJBGQueryKey() throws JSONException {
        RequestParams params = AsynClient.getRequestParams();
        String cph=cphET.getText().toString();
        //params.put("criteria_"+zjbgCriteriasIdJO.getString(ziDuanNameJO.getString("执行状态字段")),"运输中");
        params.put("criteria_"+zjbgCriteriasIdJO.getString(ziDuanNameJO.getString("车牌号字段")),cph);
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListTmpl.replaceAll("menuId",MyHttpConfing.zjbgMenuId), this, params, new GsonHttpResponseHandler() {
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

                    initZJBGCode(queryKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initZJBGCode(String queryKey) throws JSONException{
        RequestParams params = AsynClient.getRequestParams();
        String cph=cphET.getText().toString();
        //params.put("criteria_"+zjbgCriteriasIdJO.getString(ziDuanNameJO.getString("执行状态字段")),"运输中");
        params.put("criteria_"+zjbgCriteriasIdJO.getString(ziDuanNameJO.getString("车牌号字段")),cph);
        params.put("pageNo","1");
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListData.replaceAll("queryKey",queryKey), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("ZJBGCodeFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("ZJBGCodeSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONArray entitiesJA = jo.getJSONArray("entities");
                    JSONObject entitieJO = entitiesJA.getJSONObject(0);
                    JSONObject cellMapJO = entitieJO.getJSONObject("cellMap");
                    jielun=cellMapJO.getString(zjjeColumnsIdJO.getString(ziDuanNameJO.getString("质检结果字段")));
                    zjbgCode = entitieJO.getString("code");
                    Log.e("jielun===",""+jielun);
                    Log.e("zjbgCode===",""+zjbgCode);
                } catch (JSONException e) {
                    Log.e("error===",""+e.getMessage());
                    e.printStackTrace();
                }

            }
        });
    }

    private void initZJDDCriteriaId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListTmpl.replaceAll("menuId",MyHttpConfing.zjddMenuId), this, params, new GsonHttpResponseHandler() {
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
                    zjddCriteriasIdJO=new JSONObject();
                    for(int i=0;i<criteriasJA.length();i++){
                        JSONObject criteriaJO = criteriasJA.getJSONObject(i);
                        String title = criteriaJO.getString("title");
                        String id = criteriaJO.getString("id");
                        zjddCriteriasIdJO.put(title,id);
                    }
                    Log.e("zjddCriteriasIdJO===",""+zjddCriteriasIdJO.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initZJDDQueryKey() throws JSONException {
        RequestParams params = AsynClient.getRequestParams();
        String cph=cphET.getText().toString();
        params.put("criteria_"+zjddCriteriasIdJO.getString(ziDuanNameJO.getString("车牌号字段")),cph);
        //params.put("criteria_"+zjddCriteriasIdJO.getString(ziDuanNameJO.getString("订单状态字段")),"运输中");
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListTmpl.replaceAll("menuId",MyHttpConfing.zjddMenuId), this, params, new GsonHttpResponseHandler() {
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

                    initZJDDCode(queryKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initZJDDCode(String queryKey){
        RequestParams params = AsynClient.getRequestParams();
        params.put("pageNo","1");
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListData.replaceAll("queryKey",queryKey), this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("ZJDDIdFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("ZJDDIdSuccess======",""+rawJsonResponse);
                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    JSONArray entitiesJA = jo.getJSONArray("entities");
                    JSONObject entitieJO = entitiesJA.getJSONObject(0);
                    zjddCode = entitieJO.getString("code");
                    Log.e("zjddCode===",""+zjddCode);

                    initZJDDGroupsFieldsId();
                } catch (JSONException e) {
                    Log.e("error===",""+e.getMessage());
                    e.printStackTrace();
                }

            }
        });
    }

    private void initZJDDGroupsFieldsId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.doDtmplNormal.replaceAll("menuId",MyHttpConfing.zjddMenuId), this, params, new GsonHttpResponseHandler() {
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

                    zjddGroupsFieldsJO=new JSONObject();
                    for (int i=0;i<fieldsJA.length();i++) {
                        JSONObject fieldJO = (JSONObject)fieldsJA.get(i);
                        String title = fieldJO.getString("title");
                        String id = fieldJO.getString("id");
                        //Log.e("title===",""+title+",id==="+id);
                        zjddGroupsFieldsJO.put(title,id);
                    }
                    Log.e("zjddGroupsFieldsJO===",zjddGroupsFieldsJO.toString());

                    getOrderDetail();
                } catch (JSONException e) {
                    Log.e("???????","???????");
                    e.printStackTrace();
                }
            }
        });
    }

    private void getOrderDetail(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityDetail.replaceAll("menuId",MyHttpConfing.zjddMenuId)+zjddCode, this, params, new GsonHttpResponseHandler() {
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
                        String ddh = fieldMapJO.getString(zjddGroupsFieldsJO.getString(ziDuanNameJO.getString("订单号字段")));
                        String yzxzl=fieldMapJO.getString(zjddGroupsFieldsJO.getString(ziDuanNameJO.getString("预装卸重量字段")));
                        String lxlx = fieldMapJO.getString(zjddGroupsFieldsJO.getString(ziDuanNameJO.getString("流向类型字段")));
                        String bjsj=fieldMapJO.getString(zjddGroupsFieldsJO.getString(ziDuanNameJO.getString("编辑时间字段")));
                        String sjzl=fieldMapJO.getString(zjddGroupsFieldsJO.getString(ziDuanNameJO.getString("实际重量字段")));
                        String zlceb=fieldMapJO.getString(zjddGroupsFieldsJO.getString(ziDuanNameJO.getString("重量差额比字段")));
                        Log.e("订单号===",ddh);
                        Log.e("预装卸重量===",yzxzl);
                        Log.e("流向类型===",lxlx);
                        Log.e("编辑时间===",bjsj);
                        Log.e("二维码===",fieldMapJO.getString(zjddGroupsFieldsJO.getString(ziDuanNameJO.getString("二维码字段"))));
                        Log.e("实际重量===",sjzl);
                        Log.e("重量差额比===",zlceb);

                        ddhTV.setText(ddh);
                        yzxzlTV.setText(yzxzl);
                        lxlxTV.setText(lxlx);
                        bjsjTV.setText(bjsj);
                        sjzlTV.setText(sjzl);
                        zlcebTV.setText(zlceb);

                        jieLunSpinner.setSelection(getValueIndexInList(jielun,jieLunAdapter.getList()));
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

    private void initZJBGGroupsFieldId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.doDtmplNormal.replaceAll("menuId",MyHttpConfing.zjbgMenuId), this, params, new GsonHttpResponseHandler() {
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

                    zjbgGroupsFieldFieldIdJO=new JSONObject();
                    zjbgGroupsFieldNameJO=new JSONObject();
                    for (int i=0;i<fieldsJA.length();i++) {
                        JSONObject fieldJO = (JSONObject)fieldsJA.get(i);
                        String title = fieldJO.getString("title");
                        String fieldId = fieldJO.getString("fieldId");
                        Log.e("title===",""+title+",fieldId==="+fieldId);
                        String name = fieldJO.getString("name");
                        zjbgGroupsFieldFieldIdJO.put(title,fieldId);
                        zjbgGroupsFieldNameJO.put(title,name);
                    }
                    Log.e("zjbgGFFIdJO===",zjbgGroupsFieldFieldIdJO.toString());
                    Log.e("zjbgGFNJO===",zjbgGroupsFieldNameJO.toString());

                    initAdapterDataArr(zjbgGroupsFieldFieldIdJO.getString(ziDuanNameJO.getString("结论字段")),jieLunAdapter);
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
                    String fieldIdJAStr = optionsMapJO.getString(fieldId);
                    JSONArray fieldIdJA = new JSONArray(fieldIdJAStr);
                    List<String> list = adapter.getList();
                    //list.clear();
                    for(int i=0;i<fieldIdJA.length();i++){
                        JSONObject fieldIdJO=(JSONObject)fieldIdJA.get(i);
                        list.add(fieldIdJO.getString("value"));
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
        ziDuanNameJO.put("车牌号字段","车牌号");
        ziDuanNameJO.put("订单状态字段","订单状态");
        ziDuanNameJO.put("执行状态字段","执行状态");
        ziDuanNameJO.put("基本属性组字段","基本属性组");
        ziDuanNameJO.put("基本信息字段","基本信息");
    }

    private void  initJieLunSpinner(){
        jieLunAdapter = new SimpleAdapter(DDXQCPHOldActivity.this);
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

    @OnClick({R.id.cxdd_but,R.id.saveBtn})
    public void onViewClicked(View v){
        switch (v.getId()) {
            case R.id.cxdd_but:
                try {
                    if(checkCPHValue()) {
                        initZJBGQueryKey();
                        initZJDDQueryKey();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.saveBtn:
                try {
                    if(checkDDHValue()) {
                        if(checkJieLunValue()) {
                            saveZhiJianBaoGao();
                        }
                    }
                } catch (JSONException e) {
                    Log.e("error===",""+e.getMessage());
                    e.printStackTrace();
                }
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
        //Log.e("唯一编码===",""+zjbgCode);
        //Log.e("jielun===",""+jielun);
        params.put("唯一编码",zjbgCode);
        params.put(zjbgGroupsFieldNameJO.getString(ziDuanNameJO.getString("结论字段")), jielun);
        //params.put("%fuseMode%",false);
        //params.put("货运订单48[1].$$label$$","关联订单");
        //params.put("货运订单48[1].订单号",ddhTV.getText().toString());
        //data["货运订单48[1].唯一编码"]="337525032";
        //data["货运订单48[1].重量差额比"]=1;
        //data["%fuseMode%"]=false;
        //data["货运订单48.$$flag$$"]=true;

        AsynClient.post(MyHttpConfing.getBaseUrl()+MyHttpConfing.doEntityDetailNormal.replaceAll("menuId",MyHttpConfing.zjbgMenuId), this, params, new GsonHttpResponseHandler() {
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
                        Intent intent = new Intent(DDXQCPHOldActivity.this, MainActivity.class);
                        startActivity(intent);
                        AllActivitiesHolder.removeAct(DDXQCPHOldActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void MyToast(String s) {
        Toast.makeText(DDXQCPHOldActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
