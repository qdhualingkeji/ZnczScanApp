package com.hualing.znczscanapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class DDRKCPHActivity extends BaseActivity {

    private String ddrkCode;
    private JSONObject groupsIdJO,groupsFieldsJO,groupsFieldsFieldIdJO,groupsFieldsNameJO,ddrkColumnsIdJO,ddrkCriteriasIdJO,ziDuanNameJO;
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
            initZXZTSpinner();
            initRKZTSpinner();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getDataFormWeb() {
        initDDRKCriteriaId();
        initGroupsFieldsId();
    }

    private void initDDRKCriteriaId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.ddrkEntityListTmpl, this, params, new GsonHttpResponseHandler() {
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
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.ddrkEntityListTmpl, this, params, new GsonHttpResponseHandler() {
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
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getEntityListData.replaceAll("queryKey", queryKey), this, params, new GsonHttpResponseHandler() {
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

                    getOrderDetail();
                } catch (JSONException e) {
                    Log.e("error===", "" + e.getMessage());
                    e.printStackTrace();
                }

            }
        });
    }

    private void initGroupsFieldsId(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.ddrkDtmplNormal, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("ddrkDNFail======",""+rawJsonData+","+errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("ddrkDNSuccess======",""+rawJsonResponse);

                try {
                    JSONObject jo = new JSONObject(rawJsonResponse);
                    String config = jo.getString("config");
                    JSONObject configJO = new JSONObject(config);
                    String dtmpl = configJO.getString("dtmpl");
                    JSONObject dtmplJO = new JSONObject(dtmpl);
                    JSONArray groupsJA=new JSONArray(dtmplJO.getString("groups"));

                    groupsIdJO=new JSONObject();
                    for (int i=0;i<groupsJA.length();i++) {
                        JSONObject groupJO = groupsJA.getJSONObject(i);
                        String title = groupJO.getString("title");
                        String id = groupJO.getString("id");
                        Log.e("title===",""+title+",id==="+id);
                        groupsIdJO.put(title,id);
                    }
                    Log.e("groupsIdJO===",groupsIdJO.toString());

                    JSONObject groupsJO = null;
                    for (int i=0;i<groupsJA.length();i++){
                        if(groupsJA.getJSONObject(i).getString("id").equals(groupsIdJO.getString(ziDuanNameJO.getString("基本信息字段")))){
                            groupsJO=groupsJA.getJSONObject(i);
                            break;
                        }
                    }
                    String fields = groupsJO.getString("fields");
                    JSONArray fieldsJA = new JSONArray(fields);

                    groupsFieldsJO=new JSONObject();
                    groupsFieldsFieldIdJO=new JSONObject();
                    groupsFieldsNameJO=new JSONObject();
                    for (int i=0;i<fieldsJA.length();i++) {
                        JSONObject fieldJO = (JSONObject)fieldsJA.get(i);
                        String title = fieldJO.getString("title");
                        String id = fieldJO.getString("id");
                        String fieldId = fieldJO.getString("fieldId");
                        String name = fieldJO.getString("name");
                        //Log.e("title===",""+title+",id==="+id);
                        groupsFieldsJO.put(title,id);
                        groupsFieldsFieldIdJO.put(title,fieldId);
                        groupsFieldsNameJO.put(title,name);
                    }
                    Log.e("groupsFieldsJO===",groupsFieldsJO.toString());
                    Log.e("groupsFFIdJO===",groupsFieldsFieldIdJO.toString());
                    Log.e("groupsFieldsNameJO===",groupsFieldsNameJO.toString());

                    initAdapterDataArr(groupsFieldsFieldIdJO.getString(ziDuanNameJO.getString("流向类型字段")),lxlxAdapter);
                    initAdapterDataArr(groupsFieldsFieldIdJO.getString(ziDuanNameJO.getString("执行状态字段")),zxztAdapter);
                    initAdapterDataArr(groupsFieldsFieldIdJO.getString(ziDuanNameJO.getString("入库状态字段")),rkztAdapter);
                } catch (JSONException e) {
                    Log.e("???????","???????");
                    e.printStackTrace();
                }
            }
        });
    }

    private void getOrderDetail(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getBaseUrl()+MyHttpConfing.getRKOrderDetail+ddrkCode, this, params, new GsonHttpResponseHandler() {
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
                        String ddh = fieldMapJO.getString(groupsFieldsJO.getString(ziDuanNameJO.getString("订单号字段")));
                        String yzxzl=fieldMapJO.getString(groupsFieldsJO.getString(ziDuanNameJO.getString("预装卸重量字段")));
                        String bjsj=fieldMapJO.getString(groupsFieldsJO.getString(ziDuanNameJO.getString("编辑时间字段")));
                        String sjzl=fieldMapJO.getString(groupsFieldsJO.getString(ziDuanNameJO.getString("实际重量字段")));
                        String zlceb=fieldMapJO.getString(groupsFieldsJO.getString(ziDuanNameJO.getString("重量差额比字段")));
                        String lxlx = fieldMapJO.getString(groupsFieldsJO.getString(ziDuanNameJO.getString("流向类型字段")));
                        String zxzt = fieldMapJO.getString(groupsFieldsJO.getString(ziDuanNameJO.getString("执行状态字段")));
                        String rkzt = fieldMapJO.getString(groupsFieldsJO.getString(ziDuanNameJO.getString("入库状态字段")));
                        Log.e("订单号===",ddh);
                        Log.e("预装卸重量===",yzxzl);
                        Log.e("编辑时间===",bjsj);
                        Log.e("二维码===",fieldMapJO.getString(groupsFieldsJO.getString(ziDuanNameJO.getString("二维码字段"))));
                        Log.e("实际重量===",sjzl);
                        Log.e("重量差额比===",zlceb);
                        Log.e("流向类型===",lxlx);
                        Log.e("执行状态===",zxzt);
                        Log.e("入库状态===",rkzt);

                        ddhTV.setText(ddh);
                        yzxzlTV.setText(yzxzl);
                        bjsjTV.setText(bjsj);
                        sjzlTV.setText(sjzl);
                        zlcebTV.setText(zlceb);

                        lxlxSpinner.setSelection(getValueIndexInList(lxlx,lxlxAdapter.getList()));
                        zxztSpinner.setSelection(getValueIndexInList(lxlx,zxztAdapter.getList()));
                        rkztSpinner.setSelection(getValueIndexInList(lxlx,rkztAdapter.getList()));
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
        ziDuanNameJO.put("执行状态字段","执行状态");
        ziDuanNameJO.put("入库状态字段","入库状态");
        ziDuanNameJO.put("实际重量字段","实际重量");
        ziDuanNameJO.put("重量差额比字段","重量差额比");
        ziDuanNameJO.put("计划运输日期字段","计划运输日期");
        ziDuanNameJO.put("出入库时间字段","出入库时间");
        ziDuanNameJO.put("二维码字段","二维码");
        ziDuanNameJO.put("车牌号字段","车牌号");
        ziDuanNameJO.put("基本信息字段","基本信息");
    }

    private void  initLLLXSpinner(){
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

    private void  initZXZTSpinner(){
        zxztAdapter = new SimpleAdapter(DDRKCPHActivity.this);
        zxztSpinner.setAdapter(zxztAdapter);
        zxztSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zxzt=zxztAdapter.getList().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void  initRKZTSpinner(){
        rkztAdapter =new SimpleAdapter(DDRKCPHActivity.this);
        rkztSpinner.setAdapter(rkztAdapter);
        rkztSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rkzt=rkztAdapter.getList().get(position);
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

    @OnClick({R.id.cxdd_but,R.id.jhysrq_tv,R.id.crkrq_tv,R.id.crksj_tv,R.id.saveBtn})
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
            case R.id.jhysrq_tv:
                View jhysrqVI = LayoutInflater.from(this).inflate(R.layout.date_select,null);
                final DatePicker jhysrqDP = jhysrqVI.findViewById(R.id.datePicker);
                new AlertDialog.Builder(this).setView(jhysrqVI)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String dateStr = jhysrqDP.getYear() + "-" + (jhysrqDP.getMonth() + 1) + "-" + jhysrqDP.getDayOfMonth();
                                jhysrqTV.setText(dateStr);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.crkrq_tv:
                View crkrqVI = LayoutInflater.from(this).inflate(R.layout.date_select,null);
                final DatePicker crkrqDP = crkrqVI.findViewById(R.id.datePicker);
                new AlertDialog.Builder(this).setView(crkrqVI)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String dateStr = crkrqDP.getYear() + "-" + (crkrqDP.getMonth() + 1) + "-" + crkrqDP.getDayOfMonth();
                                crkrqTV.setText(dateStr);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            case R.id.crksj_tv:
                View crksjVI = LayoutInflater.from(this).inflate(R.layout.time_select, null);
                final TimePicker crksjTP = crksjVI.findViewById(R.id.timePicker);
                new AlertDialog.Builder(this).setView(crksjVI)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String dateStr = crksjTP.getCurrentHour() + ":" + crksjTP.getCurrentMinute()+":00";
                                crksjTV.setText(dateStr);
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            case R.id.saveBtn:
                try {
                    if(checkDDHValue()) {
                        if(checkLLLXValue()) {
                            if(checkZXZTValue()) {
                                if(checkRKZTValue()) {
                                    if(checkJHYSRQValue()) {
                                        if(checkCRKRQValue()) {
                                            if(checkCRKSJValue()) {
                                                saveOrderRK();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    Log.e("error===",""+e.getMessage());
                    e.printStackTrace();
                }
                break;
        }
    }

    private void saveOrderRK() throws JSONException {
        RequestParams params = AsynClient.getRequestParams();
        //params.put("唯一编码", "109221979828920322");
        params.put("唯一编码", ddrkCode);
        params.put(groupsFieldsNameJO.getString(ziDuanNameJO.getString("预装卸重量字段")), yzxzlTV.getText().toString());
        params.put(groupsFieldsNameJO.getString(ziDuanNameJO.getString("实际重量字段")), sjzlTV.getText().toString());
        params.put(groupsFieldsNameJO.getString(ziDuanNameJO.getString("重量差额比字段")), zlcebTV.getText().toString());
        params.put(groupsFieldsNameJO.getString(ziDuanNameJO.getString("流向类型字段")), lxlx);
        params.put(groupsFieldsNameJO.getString(ziDuanNameJO.getString("执行状态字段")), zxzt);
        params.put(groupsFieldsNameJO.getString(ziDuanNameJO.getString("入库状态字段")), rkzt);
        params.put(groupsFieldsNameJO.getString(ziDuanNameJO.getString("计划运输日期字段")), jhysrqTV.getText().toString());
        params.put(groupsFieldsNameJO.getString(ziDuanNameJO.getString("出入库时间字段")), crkrqTV.getText().toString()+" "+crksjTV.getText().toString());
        /*
        params.put("二维码", "");
        params.put("企业客户信息37.$$flag$$", "true");
        params.put("车辆管理63.$$flag$$", "true");
        params.put("企业客户信息81.$$flag$$", "true");
        params.put("司机信息43.$$flag$$", "true");
        params.put("系统用户80.$$flag$$", "true");
        params.put("系统用户74.$$flag$$", "true");
        */
        AsynClient.post(MyHttpConfing.getBaseUrl()+MyHttpConfing.saveOrderRK, this, params, new GsonHttpResponseHandler() {
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
                        MyToast("审核完毕");
                        Intent intent = new Intent(DDRKCPHActivity.this, MainActivity.class);
                        startActivity(intent);
                        AllActivitiesHolder.removeAct(DDRKCPHActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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


    private boolean checkLLLXValue(){
        if(TextUtils.isEmpty(lxlx)||lxlxAdapter.NO_SELECTED.equals(lxlx)){
            MyToast("请选择流向类型");
            return false;
        }
        else
            return true;
    }

    private boolean checkZXZTValue(){
        if(TextUtils.isEmpty(zxzt)||zxztAdapter.NO_SELECTED.equals(zxzt)){
            MyToast("请选择执行状态");
            return false;
        }
        else
            return true;
    }

    private boolean checkRKZTValue(){
        if(TextUtils.isEmpty(rkzt)||rkztAdapter.NO_SELECTED.equals(rkzt)){
            MyToast("请选择入库状态");
            return false;
        }
        else
            return true;
    }

    private boolean checkJHYSRQValue(){
        String jhysrq = jhysrqTV.getText().toString();
        if(TextUtils.isEmpty(jhysrq)||"null".equals(jhysrq)||"请选择计划运输日期".equals(jhysrq)){
            MyToast("请选择计划运输日期");
            return false;
        }
        else
            return true;
    }

    private boolean checkCRKRQValue(){
        String crkrq = crkrqTV.getText().toString();
        if(TextUtils.isEmpty(crkrq)||"null".equals(crkrq)||"请选择出入库日期".equals(crkrq)){
            MyToast("请选择出入库日期");
            return false;
        }
        else
            return true;
    }

    private boolean checkCRKSJValue(){
        String crksj = crksjTV.getText().toString();
        if(TextUtils.isEmpty(crksj)||"null".equals(crksj)||"请选择出入库时间".equals(crksj)){
            MyToast("请选择出入库时间");
            return false;
        }
        else
            return true;
    }

    public void MyToast(String s) {
        Toast.makeText(DDRKCPHActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
