package com.hualing.znczscanapp.utils;

import android.util.Log;

import com.hualing.znczscanapp.global.GlobalData;
import com.hualing.znczscanapp.util.SharedPreferenceUtil;

/**
 * Created by Administrator on 2017/6/15.
 */

public class MyHttpConfing {

    public static final String tag = "TAG-->";

    public static final String zjddMenuId="113345755488276";
    public static final String zjbgMenuId="113345755488274";
    public static final String ddrkMenuId="104990439776282";
    //public static final String pdcxMenuId="104809539444749";
    public static final String dqphMenuId="104977020100626";
    public static final String wdddMenuId="104981851938837";//我的订单菜单id

    public static final String bqglWgjcYbwjMenuId="121024849682450";//一磅外检菜单id
    public static final String bqglWgjcEbwjMenuId="128740148158480";//二磅外检菜单id

    public static final String zjtgActionId="121012354850818";//质检通过actionId
    public static final String ywczxActionId="121027374653442";//已完成装卸actionId
    //public static final String baseUrl = "http://121.196.184.205:96/hydrocarbon/";
    //public static final String baseUrl = "http://112.6.41.8:90/hydrocarbon/";

    public static String getBaseUrl(){
        //Log.e("ip===",""+SharedPreferenceUtil.getChangquIp());
        return "http://"+ SharedPreferenceUtil.getChangquIp()+"/hydrocarbon/";
    }

    /* 用户登录 */
    //public static final String login = baseUrl + "api2/auth/token";
    public static final String login = "api2/auth/token";
    public static final String getMenuBlocks = "api2/meta/menu/get_blocks";
    public static final String zjddDtmplNormal= "api2/meta/tmpl/"+zjddMenuId+"/dtmpl/normal/";
    public static final String ddrkDtmplNormal= "api2/meta/tmpl/"+ddrkMenuId+"/dtmpl/normal/";
    public static final String zjbgDtmplNormal= "api2/meta/tmpl/"+zjbgMenuId+"/dtmpl/normal/";
    public static final String dqphDtmplNormal=getBaseUrl() + "api2/meta/tmpl/"+dqphMenuId+"/dtmpl/normal/";
    public static final String wdddDtmplNormal="api2/meta/tmpl/"+wdddMenuId+"/dtmpl/normal/";
    public static final String wdddDtmplRabc="api2/meta/tmpl/"+wdddMenuId+"/dtmpl/rabc/";
    public static final String dqphDtmplRabc=getBaseUrl() + "api2/meta/tmpl/"+dqphMenuId+"/dtmpl/rabc/";
    public static final String dqphEntityListTmpl="api2/entity/"+dqphMenuId+"/list/tmpl";
    public static final String zjbgEntityListTmpl="api2/entity/"+zjbgMenuId+"/list/tmpl";
    public static final String wdddEntityListTmpl="api2/entity/"+wdddMenuId+"/list/tmpl";
    public static final String zjddEntityListTmpl="api2/entity/"+zjddMenuId+"/list/tmpl";
    public static final String ddrkEntityListTmpl="api2/entity/"+ddrkMenuId+"/list/tmpl";
    /* 获得订单详情数据 */
    public static final String getZJOrderDetail = "api2/entity/"+zjddMenuId+"/detail/";
    public static final String getRKOrderDetail = "api2/entity/"+ddrkMenuId+"/detail/";
    public static final String getDQPHDetail = getBaseUrl() + "api2/entity/"+dqphMenuId+"/detail/";
    public static final String getWDDDDetail = "api2/entity/"+wdddMenuId+"/detail/";
    public static final String saveZhiJianBaoGao = "api2/entity/"+zjbgMenuId+"/detail/normal";
    public static final String saveOrderRK = "api2/entity/"+ddrkMenuId+"/detail/normal";
    public static final String initFieldOptions = "api2/meta/dict/field_options";

    public static final String zjtgAction="api2/entity/"+bqglWgjcYbwjMenuId+"/action/"+zjtgActionId;
    public static final String ywczxAction="api2/entity/"+bqglWgjcEbwjMenuId+"/action/"+ywczxActionId;
    public static final String getEntityListTmpl="api2/entity/menuId/list/tmpl";
    public static final String getEntityListData="api2/entity/list/queryKey/data";

    /* 用户注册 */
    public static final String registered = getBaseUrl() + "register";
    /* 确认抢单 */
    public static final String confirmQiangDan = getBaseUrl() + "confirmQiangDan";
    /* 确认取货 */
    public static final String confirmQuHuo = getBaseUrl() + "order/itemsToShip";
    /* 获得待取货数据 */
    public static final String getDaiQuHuo = getBaseUrl() + "getDaiQuHuo";
    /* 确认送达 */
    public static final String confirmSongDa = getBaseUrl() + "enterReceipt";
    /* 获得待抢单详情数据 */
    public static final String getDaiQiangDanDetail = getBaseUrl() + "getDaiQiangDanDetail";
    /* 获得待送达数据 */
    public static final String getDaiSongDa = getBaseUrl() + "getDaiSongDa";

}



