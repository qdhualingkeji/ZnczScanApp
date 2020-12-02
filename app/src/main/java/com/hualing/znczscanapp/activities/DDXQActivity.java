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

public class DDXQActivity extends BaseActivity {

    private String orderCode="132735811812368398";
    private JSONObject zhcxGroupsIdJO,jbxxFieldsIdJO,yssFieldsIdJO,wlxxFieldsIdJO,fhdwFieldsIdJO,shdwFieldsIdJO,cyclFieldsIdJO,cysjFieldsIdJO,jybgFieldsIdJO,xdzxtzhFieldsIdJO,cysjxtzhFieldsIdJO,phxxFieldsIdJO,pzgbxxFieldsIdJO,mzgbxxFieldsIdJO,ziDuanNameJO;
    @BindView(R.id.jbxx_ddh_tv)
    TextView jbxxDdhTV;
    @BindView(R.id.jbxx_yzxzl_tv)
    TextView jbxxYzxzlTV;
    @BindView(R.id.jbxx_lxlx_tv)
    TextView jbxxLxlxTV;
    @BindView(R.id.jbxx_bjsj_tv)
    TextView jbxxBjsjTV;
    @BindView(R.id.jbxx_sjzl_tv)
    TextView jbxxSjzlTV;
    @BindView(R.id.jbxx_zlceb_tv)
    TextView jbxxZlcebTV;
    @BindView(R.id.jbxx_ddzt_tv)
    TextView jbxxDdztTV;
    @BindView(R.id.jbxx_jhysrq_tv)
    TextView jbxxJhysrqTV;
    @BindView(R.id.jbxx_bz_tv)
    TextView jbxxBzTV;
    @BindView(R.id.jbxx_jszl_tv)
    TextView jbxxJszlTV;
    @BindView(R.id.jbxx_bs_tv)
    TextView jbxxBsTV;
    @BindView(R.id.jbxx_ks_tv)
    TextView jbxxKsTV;
    @BindView(R.id.jbxx_dfgbjz_tv)
    TextView jbxxDfgbjzTV;
    @BindView(R.id.jbxx_dfgbpz_tv)
    TextView jbxxDfgbpzTV;
    @BindView(R.id.jbxx_dfgbmz_tv)
    TextView jbxxDfgbmzTV;
    @BindView(R.id.jbxx_dfbdzp_tv)
    TextView jbxxDfbdzpTV;
    @BindView(R.id.jbxx_dfgbsj_tv)
    TextView jbxxDfgbsjTV;
    @BindView(R.id.yss_gx_tv)
    TextView yssGxTV;
    @BindView(R.id.yss_mc_tv)
    TextView yssMcTV;
    @BindView(R.id.wlxx_gx_tv)
    TextView wlxxGxTV;
    @BindView(R.id.wlxx_mc_tv)
    TextView wlxxMcTV;
    @BindView(R.id.fhdw_gx_tv)
    TextView fhdwGxTV;
    @BindView(R.id.fhdw_dwmc_tv)
    TextView fhdwDwmcTV;
    @BindView(R.id.shdw_gx_tv)
    TextView shdwGxTV;
    @BindView(R.id.shdw_dwmc_tv)
    TextView shdwDwmcTV;
    @BindView(R.id.cycl_gx_tv)
    TextView cyclGxTV;
    @BindView(R.id.cycl_cph_tv)
    TextView cyclCphTV;
    @BindView(R.id.cysj_gx_tv)
    TextView cysjGxTV;
    @BindView(R.id.cysj_xm_tv)
    TextView cysjXmTV;
    @BindView(R.id.cysj_sjh_tv)
    TextView cysjSjhTV;
    @BindView(R.id.cysj_sfzh_tv)
    TextView cysjSfzhTV;

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
        ziDuanNameJO.put("订单号字段","订单号");
        ziDuanNameJO.put("预装卸重量字段","预装卸重量");
        ziDuanNameJO.put("流向类型字段","流向类型");
        ziDuanNameJO.put("编辑时间字段","编辑时间");
        ziDuanNameJO.put("实际重量字段","实际重量");
        ziDuanNameJO.put("重量差额比字段","重量差额比");
        ziDuanNameJO.put("订单状态字段","订单状态");
        ziDuanNameJO.put("计划运输日期字段","计划运输日期");
        ziDuanNameJO.put("备注字段","备注");
        ziDuanNameJO.put("结算重量字段","结算重量");
        ziDuanNameJO.put("包数字段","包数");
        ziDuanNameJO.put("块数字段","块数");
        ziDuanNameJO.put("对方过磅净重字段","对方过磅净重");
        ziDuanNameJO.put("对方过磅皮重字段","对方过磅皮重");
        ziDuanNameJO.put("对方过磅毛重字段","对方过磅毛重");
        ziDuanNameJO.put("对方榜单照片字段","对方榜单照片");
        ziDuanNameJO.put("对方过磅时间字段","对方过磅时间");
        ziDuanNameJO.put("运输商字段","运输商");
        ziDuanNameJO.put("关系字段","relationLabel");
        ziDuanNameJO.put("名称字段","名称");
        ziDuanNameJO.put("物料信息字段","物料信息");
        ziDuanNameJO.put("发货单位字段","发货单位");
        ziDuanNameJO.put("单位名称字段","单位名称");
        ziDuanNameJO.put("收货单位字段","收货单位");
        ziDuanNameJO.put("承运车辆字段","承运车辆");
        ziDuanNameJO.put("车牌号字段","车牌号");
        ziDuanNameJO.put("承运司机字段","承运司机");
        ziDuanNameJO.put("姓名字段","姓名");
        ziDuanNameJO.put("手机号字段","手机号");
        ziDuanNameJO.put("身份证字段","身份证");
        ziDuanNameJO.put("检验报告字段","检验报告");
        ziDuanNameJO.put("结论字段","结论");
        ziDuanNameJO.put("下单者系统账户字段","下单者系统账户");
        ziDuanNameJO.put("实名字段","实名");
        ziDuanNameJO.put("承运司机系统账户字段","承运司机系统账户");
        ziDuanNameJO.put("用户名字段","用户名");
        ziDuanNameJO.put("简述字段","简述");
        ziDuanNameJO.put("排号信息字段","排号信息");
        ziDuanNameJO.put("排队号字段","排队号");
        ziDuanNameJO.put("排入时间字段","排入时间");
        ziDuanNameJO.put("开始叫号时间字段","开始叫号时间");
        ziDuanNameJO.put("状态字段","状态");
        ziDuanNameJO.put("皮重过磅信息字段","皮重过磅信息");
        ziDuanNameJO.put("过磅车辆字段","过磅车辆");
        ziDuanNameJO.put("过磅时间字段","过磅时间");
        ziDuanNameJO.put("过磅重量字段","过磅重量");
        ziDuanNameJO.put("过磅状态字段","过磅状态");
        ziDuanNameJO.put("毛重过磅信息字段","毛重过磅信息");
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
                        else if(ziDuanNameJO.getString("发货单位字段").equals(title)){
                            fhdwFieldsIdJO=new JSONObject();
                            JSONArray  fhdwFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< fhdwFieldsJA.length();j++){
                                JSONObject  fhdwFieldsJO= fhdwFieldsJA.getJSONObject(j);
                                String  fhdwTitle =  fhdwFieldsJO.getString("title");
                                String  fhdwId =  fhdwFieldsJO.getString("id");
                                fhdwFieldsIdJO.put( fhdwTitle, fhdwId);
                            }
                        }
                        else if(ziDuanNameJO.getString("收货单位字段").equals(title)){
                            shdwFieldsIdJO=new JSONObject();
                            JSONArray  shdwFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< shdwFieldsJA.length();j++){
                                JSONObject  shdwFieldsJO= shdwFieldsJA.getJSONObject(j);
                                String  shdwTitle =  shdwFieldsJO.getString("title");
                                String  shdwId =  shdwFieldsJO.getString("id");
                                shdwFieldsIdJO.put( shdwTitle, shdwId);
                            }
                        }
                        else if(ziDuanNameJO.getString("承运车辆字段").equals(title)){
                            cyclFieldsIdJO=new JSONObject();
                            JSONArray  cyclFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< cyclFieldsJA.length();j++){
                                JSONObject  cyclFieldsJO= cyclFieldsJA.getJSONObject(j);
                                String  cyclTitle =  cyclFieldsJO.getString("title");
                                String  cyclId =  cyclFieldsJO.getString("id");
                                cyclFieldsIdJO.put( cyclTitle, cyclId);
                            }
                        }
                        else if(ziDuanNameJO.getString("承运司机字段").equals(title)){
                            cysjFieldsIdJO=new JSONObject();
                            JSONArray  cysjFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< cysjFieldsJA.length();j++){
                                JSONObject  cysjFieldsJO= cysjFieldsJA.getJSONObject(j);
                                String  cysjTitle =  cysjFieldsJO.getString("title");
                                String  cysjId =  cysjFieldsJO.getString("id");
                                cysjFieldsIdJO.put( cysjTitle, cysjId);
                            }
                        }
                        else if(ziDuanNameJO.getString("检验报告字段").equals(title)){
                            jybgFieldsIdJO=new JSONObject();
                            JSONArray  jybgFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< jybgFieldsJA.length();j++){
                                JSONObject  jybgFieldsJO= jybgFieldsJA.getJSONObject(j);
                                String  jybgTitle =  jybgFieldsJO.getString("title");
                                String  jybgId =  jybgFieldsJO.getString("id");
                                jybgFieldsIdJO.put( jybgTitle, jybgId);
                            }
                        }
                        else if(ziDuanNameJO.getString("下单者系统账户字段").equals(title)){
                            xdzxtzhFieldsIdJO=new JSONObject();
                            JSONArray  xdzxtzhFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< xdzxtzhFieldsJA.length();j++){
                                JSONObject  xdzxtzhFieldsJO= xdzxtzhFieldsJA.getJSONObject(j);
                                String  xdzxtzhTitle =  xdzxtzhFieldsJO.getString("title");
                                String  xdzxtzhId =  xdzxtzhFieldsJO.getString("id");
                                xdzxtzhFieldsIdJO.put( xdzxtzhTitle, xdzxtzhId);
                            }
                        }
                        else if(ziDuanNameJO.getString("承运司机系统账户字段").equals(title)){
                            cysjxtzhFieldsIdJO=new JSONObject();
                            JSONArray  cysjxtzhFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< cysjxtzhFieldsJA.length();j++){
                                JSONObject  cysjxtzhFieldsJO= cysjxtzhFieldsJA.getJSONObject(j);
                                String  cysjxtzhTitle =  cysjxtzhFieldsJO.getString("title");
                                String  cysjxtzhId =  cysjxtzhFieldsJO.getString("id");
                                cysjxtzhFieldsIdJO.put( cysjxtzhTitle, cysjxtzhId);
                            }
                        }
                        else if(ziDuanNameJO.getString("排号信息字段").equals(title)){
                            phxxFieldsIdJO=new JSONObject();
                            JSONArray  phxxFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< phxxFieldsJA.length();j++){
                                JSONObject  phxxFieldsJO= phxxFieldsJA.getJSONObject(j);
                                String  phxxTitle =  phxxFieldsJO.getString("title");
                                String  phxxId =  phxxFieldsJO.getString("id");
                                phxxFieldsIdJO.put( phxxTitle, phxxId);
                            }
                        }
                        else if(ziDuanNameJO.getString("皮重过磅信息字段").equals(title)){
                            pzgbxxFieldsIdJO=new JSONObject();
                            JSONArray  pzgbxxFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< pzgbxxFieldsJA.length();j++){
                                JSONObject  pzgbxxFieldsJO= pzgbxxFieldsJA.getJSONObject(j);
                                String  pzgbxxTitle =  pzgbxxFieldsJO.getString("title");
                                String  pzgbxxId =  pzgbxxFieldsJO.getString("id");
                                pzgbxxFieldsIdJO.put( pzgbxxTitle, pzgbxxId);
                            }
                        }
                        else if(ziDuanNameJO.getString("毛重过磅信息字段").equals(title)){
                            mzgbxxFieldsIdJO=new JSONObject();
                            JSONArray  mzgbxxFieldsJA=groupJO.getJSONArray("fields");
                            for(int j=0;j< mzgbxxFieldsJA.length();j++){
                                JSONObject  mzgbxxFieldsJO= mzgbxxFieldsJA.getJSONObject(j);
                                String  mzgbxxTitle =  mzgbxxFieldsJO.getString("title");
                                String  mzgbxxId =  mzgbxxFieldsJO.getString("id");
                                mzgbxxFieldsIdJO.put( mzgbxxTitle, mzgbxxId);
                            }
                        }
                        zhcxGroupsIdJO.put(title,id);
                    }
                    Log.e("zhcxGroupsIdJO===",zhcxGroupsIdJO.toString());
                    Log.e("jbxxFieldsIdJO===",jbxxFieldsIdJO.toString());
                    Log.e("yssFieldsIdJO===",yssFieldsIdJO.toString());
                    Log.e("wlxxFieldsIdJO===",wlxxFieldsIdJO.toString());
                    Log.e("fhdwFieldsIdJO===",fhdwFieldsIdJO.toString());
                    Log.e("shdwFieldsIdJO===",shdwFieldsIdJO.toString());
                    Log.e("cyclFieldsIdJO===",cyclFieldsIdJO.toString());
                    Log.e("cysjFieldsIdJO===",cysjFieldsIdJO.toString());
                    Log.e("jybgFieldsIdJO===",jybgFieldsIdJO.toString());
                    Log.e("xdzxtzhFieldsIdJO===",xdzxtzhFieldsIdJO.toString());
                    Log.e("cysjxtzhFieldsIdJO===",cysjxtzhFieldsIdJO.toString());
                    Log.e("phxxFieldsIdJO===",phxxFieldsIdJO.toString());
                    Log.e("pzgbxxFieldsIdJO===",pzgbxxFieldsIdJO.toString());
                    Log.e("mzgbxxFieldsIdJO===",pzgbxxFieldsIdJO.toString());

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
                    JSONObject fieldMapJO = entityJO.getJSONObject("fieldMap");
                    initJBXXDetail(fieldMapJO);
                    JSONObject arrayMapJO = entityJO.getJSONObject("arrayMap");
                    initYSSDetail(arrayMapJO);
                    initWLXXDetail(arrayMapJO);
                    initFHDWDetail(arrayMapJO);
                    initSHDWDetail(arrayMapJO);
                    initCYCLDetail(arrayMapJO);
                    initCYSJDetail(arrayMapJO);
                    //initJYBGDetail(arrayMapJO);
                    //initXDZXTZHDetail(arrayMapJO);
                    //initCYSJXTZHDetail(arrayMapJO);
                    //initPHXXDetail(arrayMapJO);
                    //initPZGBXXDetail(arrayMapJO);
                    //initMZGBXXDetail(arrayMapJO);
                } catch (JSONException e) {
                    Log.e("error===",""+e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void initJBXXDetail(JSONObject jbxxJO) throws JSONException {
        String ddh=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("订单号字段")));
        String yzxzl=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("预装卸重量字段")));
        String lxlx=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("流向类型字段")));
        String bjsj=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("编辑时间字段")));
        String sjzl=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("实际重量字段")));
        String zlceb=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("重量差额比字段")));
        String ddzt=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("订单状态字段")));
        String jhysrq=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("计划运输日期字段")));
        String bz=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("备注字段")));
        String jszl=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("结算重量字段")));
        String bs=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("包数字段")));
        String ks=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("块数字段")));
        String dfgbjz=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("对方过磅净重字段")));
        String dfgbpz=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("对方过磅皮重字段")));
        String dfgbmz=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("对方过磅毛重字段")));
        String dfbdzp=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("对方榜单照片字段")));
        String dfgbsj=jbxxJO.getString(jbxxFieldsIdJO.getString(ziDuanNameJO.getString("对方过磅时间字段")));
        Log.e("ddh===",ddh);
        Log.e("yzxzl===",yzxzl);
        Log.e("lxlx===",lxlx);
        Log.e("bjsj===",bjsj);
        jbxxDdhTV.setText(ddh);
        jbxxYzxzlTV.setText(yzxzl);
        jbxxLxlxTV.setText(lxlx);
        jbxxBjsjTV.setText(bjsj);
        jbxxSjzlTV.setText(sjzl);
        jbxxZlcebTV.setText(zlceb);
        jbxxDdztTV.setText(ddzt);
        jbxxJhysrqTV.setText(jhysrq);
        jbxxBzTV.setText(bz);
        jbxxJszlTV.setText(jszl);
        jbxxBsTV.setText(bs);
        jbxxKsTV.setText(ks);
        jbxxDfgbjzTV.setText(dfgbjz);
        jbxxDfgbpzTV.setText(dfgbpz);
        jbxxDfgbmzTV.setText(dfgbmz);
        jbxxDfbdzpTV.setText(dfbdzp);
        jbxxDfgbsjTV.setText(dfgbsj);
    }

    private void initYSSDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray yssJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("运输商字段")));
        JSONObject yssJO = yssJA.getJSONObject(0);
        Log.e("yssJO===",""+yssJO.toString());
        String gx=yssJO.getString(ziDuanNameJO.getString("关系字段"));
        String mc=yssJO.getJSONObject("fieldMap").getString(yssFieldsIdJO.getString(ziDuanNameJO.getString("名称字段")));
        Log.e("gx===",gx);
        Log.e("mc===",mc);
        yssGxTV.setText(gx);
        yssMcTV.setText(mc);
    }

    private void initWLXXDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray wlxxJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("物料信息字段")));
        JSONObject wlxxJO = wlxxJA.getJSONObject(0);
        Log.e("wlxxJO===",""+wlxxJO.toString());
        String gx=wlxxJO.getString(ziDuanNameJO.getString("关系字段"));
        String mc=wlxxJO.getJSONObject("fieldMap").getString(wlxxFieldsIdJO.getString(ziDuanNameJO.getString("名称字段")));
        Log.e("mc===",mc);
        wlxxGxTV.setText(gx);
        wlxxMcTV.setText(mc);
    }

    private void initFHDWDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray fhdwJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("发货单位字段")));
        JSONObject fhdwJO = fhdwJA.getJSONObject(0);
        Log.e("fhdwJO===",""+fhdwJO.toString());
        String gx=fhdwJO.getString(ziDuanNameJO.getString("关系字段"));
        String dwmc=fhdwJO.getJSONObject("fieldMap").getString(fhdwFieldsIdJO.getString(ziDuanNameJO.getString("单位名称字段")));
        Log.e("dwmc===",dwmc);
        fhdwGxTV.setText(gx);
        fhdwDwmcTV.setText(dwmc);
    }

    private void initSHDWDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray shdwJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("收货单位字段")));
        JSONObject shdwJO = shdwJA.getJSONObject(0);
        Log.e("shdwJO===",""+shdwJO.toString());
        String gx=shdwJO.getString(ziDuanNameJO.getString("关系字段"));
        String dwmc=shdwJO.getJSONObject("fieldMap").getString(shdwFieldsIdJO.getString(ziDuanNameJO.getString("单位名称字段")));
        Log.e("dwmc===",dwmc);
        shdwGxTV.setText(gx);
        shdwDwmcTV.setText(dwmc);
    }

    private void initCYCLDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray cyclJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("承运车辆字段")));
        JSONObject cyclJO = cyclJA.getJSONObject(0);
        Log.e("cyclJO===",""+cyclJO.toString());
        String gx=cyclJO.getString(ziDuanNameJO.getString("关系字段"));
        String cph=cyclJO.getJSONObject("fieldMap").getString(cyclFieldsIdJO.getString(ziDuanNameJO.getString("车牌号字段")));
        Log.e("cph===",cph);
        cyclGxTV.setText(gx);
        cyclCphTV.setText(cph);
    }

    private void initCYSJDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray cysjJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("承运司机字段")));
        JSONObject cysjJO = cysjJA.getJSONObject(0);
        Log.e("cysjJO===",""+cysjJO.toString());
        String gx=cysjJO.getString(ziDuanNameJO.getString("关系字段"));
        String xm=cysjJO.getJSONObject("fieldMap").getString(cysjFieldsIdJO.getString(ziDuanNameJO.getString("姓名字段")));
        String sjh=cysjJO.getJSONObject("fieldMap").getString(cysjFieldsIdJO.getString(ziDuanNameJO.getString("手机号字段")));
        String sfzh=cysjJO.getJSONObject("fieldMap").getString(cysjFieldsIdJO.getString(ziDuanNameJO.getString("身份证字段")));
        Log.e("xm===",xm);
        Log.e("sjh===",sjh);
        Log.e("sfzh===",sfzh);
        cysjGxTV.setText(gx);
        cysjXmTV.setText(xm);
        cysjSjhTV.setText(sjh);
        cysjSfzhTV.setText(sfzh);
    }

    private void initJYBGDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray jybgJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("检验报告字段")));
        JSONObject jybgJO = jybgJA.getJSONObject(0);
        Log.e("jybgJO===",""+jybgJO.toString());
        String jl=jybgJO.getJSONObject("fieldMap").getString(jybgFieldsIdJO.getString(ziDuanNameJO.getString("结论字段")));
        Log.e("jl===",jl);
    }

    private void initXDZXTZHDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray xdzxtzhJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("下单者系统账户字段")));
        JSONObject xdzxtzhJO = xdzxtzhJA.getJSONObject(0);
        Log.e("xdzxtzhJO===",""+xdzxtzhJO.toString());
        String sm=xdzxtzhJO.getJSONObject("fieldMap").getString(xdzxtzhFieldsIdJO.getString(ziDuanNameJO.getString("实名字段")));
        Log.e("sm===",sm);
    }

    private void initCYSJXTZHDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray cysjxtzhJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("承运司机系统账户字段")));
        JSONObject cysjxtzhJO = cysjxtzhJA.getJSONObject(0);
        Log.e("cysjxtzhJO===",""+cysjxtzhJO.toString());
        String sm=cysjxtzhJO.getJSONObject("fieldMap").getString(cysjxtzhFieldsIdJO.getString(ziDuanNameJO.getString("实名字段")));
        String yhm=cysjxtzhJO.getJSONObject("fieldMap").getString(cysjxtzhFieldsIdJO.getString(ziDuanNameJO.getString("用户名字段")));
        String js=cysjxtzhJO.getJSONObject("fieldMap").getString(cysjxtzhFieldsIdJO.getString(ziDuanNameJO.getString("简述字段")));
        Log.e("sm===",sm);
        Log.e("yhm===",yhm);
        Log.e("js===",js);
    }

    private void initPHXXDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray phxxJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("排号信息字段")));
        Log.e("phxxJA===",""+phxxJA.toString());
        JSONObject phxxJO = phxxJA.getJSONObject(0);
        Log.e("phxxJO===",""+phxxJO.toString());
        String bm=phxxJO.getJSONObject("fieldMap").getString(cysjxtzhFieldsIdJO.getString(ziDuanNameJO.getString("编码字段")));
        String pdh=phxxJO.getJSONObject("fieldMap").getString(cysjxtzhFieldsIdJO.getString(ziDuanNameJO.getString("排队号字段")));
        String prsj=phxxJO.getJSONObject("fieldMap").getString(cysjxtzhFieldsIdJO.getString(ziDuanNameJO.getString("排入时间字段")));
        String ksjhsj=phxxJO.getJSONObject("fieldMap").getString(cysjxtzhFieldsIdJO.getString(ziDuanNameJO.getString("开始叫号时间字段")));
        String zt=phxxJO.getJSONObject("fieldMap").getString(cysjxtzhFieldsIdJO.getString(ziDuanNameJO.getString("状态字段")));
        Log.e("bm===",bm);
        Log.e("pdh===",pdh);
        Log.e("prsj===",prsj);
        Log.e("ksjhsj===",ksjhsj);
        Log.e("zt===",zt);
    }

    private void initPZGBXXDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray pzgbxxJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("皮重过磅信息字段")));
        JSONObject pzgbxxJO = pzgbxxJA.getJSONObject(0);
        Log.e("pzgbxxJO===",""+pzgbxxJO.toString());
        String gbcl=pzgbxxJO.getJSONObject("fieldMap").getString(pzgbxxFieldsIdJO.getString(ziDuanNameJO.getString("过磅车辆字段")));
        String gbsj=pzgbxxJO.getJSONObject("fieldMap").getString(pzgbxxFieldsIdJO.getString(ziDuanNameJO.getString("过磅时间字段")));
        String gbzl=pzgbxxJO.getJSONObject("fieldMap").getString(pzgbxxFieldsIdJO.getString(ziDuanNameJO.getString("过磅重量字段")));
        String gbzt=pzgbxxJO.getJSONObject("fieldMap").getString(pzgbxxFieldsIdJO.getString(ziDuanNameJO.getString("过磅状态字段")));
        Log.e("gbcl===",gbcl);
        Log.e("gbsj===",gbsj);
        Log.e("gbzl===",gbzl);
        Log.e("gbzt===",gbzt);
    }

    private void initMZGBXXDetail(JSONObject arrayMapJO) throws JSONException {
        JSONArray mzgbxxJA = arrayMapJO.getJSONArray(zhcxGroupsIdJO.getString(ziDuanNameJO.getString("毛重过磅信息字段")));
        JSONObject mzgbxxJO = mzgbxxJA.getJSONObject(0);
        Log.e("mzgbxxJO===",""+mzgbxxJO.toString());
        String gbcl=mzgbxxJO.getJSONObject("fieldMap").getString(pzgbxxFieldsIdJO.getString(ziDuanNameJO.getString("过磅车辆字段")));
        String gbsj=mzgbxxJO.getJSONObject("fieldMap").getString(pzgbxxFieldsIdJO.getString(ziDuanNameJO.getString("过磅时间字段")));
        String gbzl=mzgbxxJO.getJSONObject("fieldMap").getString(pzgbxxFieldsIdJO.getString(ziDuanNameJO.getString("过磅重量字段")));
        String gbzt=mzgbxxJO.getJSONObject("fieldMap").getString(pzgbxxFieldsIdJO.getString(ziDuanNameJO.getString("过磅状态字段")));
        Log.e("gbcl===",gbcl);
        Log.e("gbsj===",gbsj);
        Log.e("gbzl===",gbzl);
        Log.e("gbzt===",gbzt);
    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ddxq;
    }
}
