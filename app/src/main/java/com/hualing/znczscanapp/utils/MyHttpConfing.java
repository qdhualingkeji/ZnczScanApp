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
    public static final String doEntityDetailNormal = "api2/entity/menuId/detail/normal";
    public static final String getEntityDetail = "api2/entity/menuId/detail/";
    public static final String doMetaTmplDtmplRabc="api2/meta/tmpl/menuId/dtmpl/rabc/";
    public static final String doDtmplNormal= "api2/meta/tmpl/menuId/dtmpl/normal/";
    public static final String doAction="api2/entity/menuId/action/actionId";
    public static final String getEntityListTmpl="api2/entity/menuId/list/tmpl";
    public static final String getEntityListData="api2/entity/list/queryKey/data";
    public static final String initFieldOptions = "api2/meta/dict/field_options";

}



