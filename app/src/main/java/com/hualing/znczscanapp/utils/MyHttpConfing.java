package com.hualing.znczscanapp.utils;

/**
 * Created by Administrator on 2017/6/15.
 */

public class MyHttpConfing {

    public static final String tag = "TAG-->";

    public static final String ddxqMenuId="104107815608326";
    public static final String zjbgMenuId="104985599549464";
    public static final String baseUrl = "http://121.196.184.205:96/hydrocarbon/";

    /* 用户登录 */
    public static final String login = baseUrl + "api2/auth/token";
    public static final String dtmplNormalByMenuId= baseUrl + "api2/meta/tmpl/"+ddxqMenuId+"/dtmpl/normal/";
    /* 获得订单详情数据 */
    public static final String getOrderDetail = baseUrl + "api2/entity/"+ddxqMenuId+"/detail/";
    public static final String saveZhiJianBaoGao = baseUrl + "api2/entity/"+zjbgMenuId+"/detail/normal";
    /* 用户注册 */
    public static final String registered = baseUrl + "register";
    /* 确认抢单 */
    public static final String confirmQiangDan = baseUrl + "confirmQiangDan";
    /* 确认取货 */
    public static final String confirmQuHuo = baseUrl + "order/itemsToShip";
    /* 获得待取货数据 */
    public static final String getDaiQuHuo = baseUrl + "getDaiQuHuo";
    /* 确认送达 */
    public static final String confirmSongDa = baseUrl + "enterReceipt";
    /* 获得待抢单详情数据 */
    public static final String getDaiQiangDanDetail = baseUrl + "getDaiQiangDanDetail";
    /* 获得待送达数据 */
    public static final String getDaiSongDa = baseUrl + "getDaiSongDa";

}



