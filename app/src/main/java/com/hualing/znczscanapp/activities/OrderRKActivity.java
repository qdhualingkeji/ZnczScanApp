package com.hualing.znczscanapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.zxing.common.StringUtils;
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

public class OrderRKActivity extends BaseActivity {

    private String orderCode;
    List<String> lxlxList,zxztList,rkztList;
    private JSONObject columnsIdJO,columnsFieldIdJO,columnsNameJO,ziDuanNameJO;
    private SimpleAdapter lxlxAdapter,zxztAdapter,rkztAdapter;
    private String lxlx,zxzt,rkzt;
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
    }

    private void initColumnsId(){
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
                    JSONObject configJO = new JSONObject(config);
                    String dtmpl = configJO.getString("dtmpl");
                    JSONObject dtmplJO = new JSONObject(dtmpl);
                    JSONArray groupsJA=new JSONArray(dtmplJO.getString("groups"));
                    JSONObject groupsJO = (JSONObject)groupsJA.get(0);
                    //Log.e("group===",""+groupsJO.toString());
                    String fields = groupsJO.getString("fields");
                    JSONArray fieldsJA = new JSONArray(fields);

                    columnsIdJO=new JSONObject();
                    columnsFieldIdJO=new JSONObject();
                    columnsNameJO=new JSONObject();
                    for (int i=0;i<fieldsJA.length();i++) {
                        JSONObject fieldJO = (JSONObject)fieldsJA.get(i);
                        String title = fieldJO.getString("title");
                        String id = fieldJO.getString("id");
                        String fieldId = fieldJO.getString("fieldId");
                        String name = fieldJO.getString("name");
                        Log.e("title===",""+title+",fieldId==="+fieldId+",id==="+id);
                        columnsIdJO.put(title,id);
                        columnsFieldIdJO.put(title,fieldId);
                        columnsNameJO.put(title,name);
                    }
                    Log.e("columnsIdJO===",columnsIdJO.toString());
                    Log.e("columnsFieldIdJO===",columnsFieldIdJO.toString());
                    Log.e("columnsNameJO===",columnsNameJO.toString());

                    initAdapterDataArr(columnsFieldIdJO.getString(ziDuanNameJO.getString("流向类型字段")),lxlxAdapter);
                    initAdapterDataArr(columnsFieldIdJO.getString(ziDuanNameJO.getString("执行状态字段")),zxztAdapter);
                    initAdapterDataArr(columnsFieldIdJO.getString(ziDuanNameJO.getString("入库状态字段")),rkztAdapter);

                    getOrderDetail();
                } catch (JSONException e) {
                    Log.e("???????","???????");
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

    private void getOrderDetail(){
        RequestParams params = AsynClient.getRequestParams();
        AsynClient.get(MyHttpConfing.getRKOrderDetail+orderCode, this, params, new GsonHttpResponseHandler() {
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
                        String zxzt=fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("执行状态字段")));
                        String rkzt=fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("入库状态字段")));
                        String sjzl=fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("实际重量字段")));
                        String zlceb=fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("重量差额比字段")));
                        String jhysrq=fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("计划运输日期字段")));
                        String crksj=fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("出入库时间字段")));
                        Log.e("订单号===",ddh);
                        Log.e("预装卸重量===",yzxzl);
                        Log.e("流向类型===",lxlx);
                        Log.e("编辑时间===",bjsj);
                        Log.e("执行状态===",zxzt);
                        Log.e("入库状态===",rkzt);
                        Log.e("二维码===",fieldMapJO.getString(columnsIdJO.getString(ziDuanNameJO.getString("二维码字段"))));
                        Log.e("实际重量===",sjzl);
                        Log.e("重量差额比===",zlceb);
                        Log.e("出入库时间===",crksj);

                        ddhTV.setText(ddh);
                        yzxzlTV.setText(yzxzl);
                        bjsjTV.setText(bjsj);
                        sjzlTV.setText(sjzl);
                        zlcebTV.setText(zlceb);

                        lxlxSpinner.setSelection(getValueIndexInList(lxlx,lxlxAdapter.getList()));
                        zxztSpinner.setSelection(getValueIndexInList(zxzt,zxztAdapter.getList()));
                        rkztSpinner.setSelection(getValueIndexInList(rkzt,rkztAdapter.getList()));

                        jhysrqTV.setText(jhysrq);
                        if(crksj!=null) {
                            String[] crksjArr = crksj.split(" ");
                            crkrqTV.setText(crksjArr[0]);
                            crksjTV.setText(crksjArr[1]);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("??????",""+e.getMessage());
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
        ziDuanNameJO.put("执行状态字段","执行状态");
        ziDuanNameJO.put("入库状态字段","入库状态");
        ziDuanNameJO.put("实际重量字段","实际重量");
        ziDuanNameJO.put("重量差额比字段","重量差额比");
        ziDuanNameJO.put("计划运输日期字段","计划运输日期");
        ziDuanNameJO.put("出入库时间字段","出入库时间");
        ziDuanNameJO.put("二维码字段","二维码");
    }

    private void  initLLLXSpinner(){
        lxlxList=new ArrayList<String>();
        lxlxList.add("");
        lxlxAdapter = new SimpleAdapter(OrderRKActivity.this);
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
        zxztList=new ArrayList<String>();
        zxztList.add("");
        zxztAdapter = new SimpleAdapter(OrderRKActivity.this);
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
        rkztList=new ArrayList<String>();
        rkztList.add("");
        rkztAdapter =new SimpleAdapter(OrderRKActivity.this);
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

    @OnClick({R.id.jhysrq_tv,R.id.crkrq_tv,R.id.crksj_tv,R.id.saveBtn})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.jhysrq_tv:
                View jhysrqVI = LayoutInflater.from(this).inflate(R.layout.date_select, null);
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
                View v1 = LayoutInflater.from(this).inflate(R.layout.time_select, null);
                final TimePicker crksjTP = v1.findViewById(R.id.timePicker);
                crksjTP.setIs24HourView(true);
                new AlertDialog.Builder(this).setView(v1)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String dateStr = crksjTP.getCurrentHour() + ":" + crksjTP.getCurrentMinute()+":00";
                                crksjTV.setText(dateStr);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.saveBtn:
                try {
                    saveOrderRK();
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
        params.put("唯一编码", orderCode);
        params.put(columnsNameJO.getString(ziDuanNameJO.getString("预装卸重量字段")), yzxzlTV.getText().toString());
        params.put(columnsNameJO.getString(ziDuanNameJO.getString("实际重量字段")), sjzlTV.getText().toString());
        params.put(columnsNameJO.getString(ziDuanNameJO.getString("重量差额比字段")), zlcebTV.getText().toString());
        params.put(columnsNameJO.getString(ziDuanNameJO.getString("流向类型字段")), lxlx);
        params.put(columnsNameJO.getString(ziDuanNameJO.getString("执行状态字段")), zxzt);
        params.put(columnsNameJO.getString(ziDuanNameJO.getString("入库状态字段")), rkzt);
        params.put(columnsNameJO.getString(ziDuanNameJO.getString("计划运输日期字段")), jhysrqTV.getText().toString());
        params.put(columnsNameJO.getString(ziDuanNameJO.getString("出入库时间字段")), crkrqTV.getText().toString()+" "+crksjTV.getText().toString());
        /*
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
