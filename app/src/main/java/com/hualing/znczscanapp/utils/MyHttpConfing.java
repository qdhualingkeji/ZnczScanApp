package com.hualing.znczscanapp.utils;

/**
 * Created by Administrator on 2017/6/15.
 */

public class MyHttpConfing {

    public static final String tag = "TAG-->";

    public static final String zjddxqMenuId="106298257317913";
    public static final String zjbgMenuId="104985599549464";
    public static final String ddrkMenuId="104990439776282";
    //public static final String pdcxMenuId="104809539444749";
    public static final String dqphMenuId="104977020100626";
    public static final String zyddMenuId="104981851938835";//在运订单菜单id
    public static final String baseUrl = "http://121.196.184.205:96/hydrocarbon/";

    /* 用户登录 */
    public static final String login = baseUrl + "api2/auth/token";
    public static final String getMenuBlocks = baseUrl + "api2/meta/menu/get_blocks";
    public static final String zjddxqDtmplNormal= baseUrl + "api2/meta/tmpl/"+zjddxqMenuId+"/dtmpl/normal/";
    public static final String ddrkDtmplNormal= baseUrl + "api2/meta/tmpl/"+ddrkMenuId+"/dtmpl/normal/";
    public static final String zjbgDtmplNormal= baseUrl + "api2/meta/tmpl/"+zjbgMenuId+"/dtmpl/normal/";
    public static final String dqphDtmplNormal=baseUrl + "api2/meta/tmpl/"+dqphMenuId+"/dtmpl/normal/";
    public static final String zyddDtmplNormal=baseUrl + "api2/meta/tmpl/"+zyddMenuId+"/dtmpl/normal/";
    public static final String zyddDtmplRabc=baseUrl + "api2/meta/tmpl/"+zyddMenuId+"/dtmpl/rabc/";
    public static final String dqphEntityListTmpl=baseUrl+"api2/entity/"+dqphMenuId+"/list/tmpl";
    public static final String zjbgEntityListTmpl=baseUrl+"api2/entity/"+zjbgMenuId+"/list/tmpl";
    public static final String zyddEntityListTmpl=baseUrl+"api2/entity/"+zyddMenuId+"/list/tmpl";
    /* 获得订单详情数据 */
    public static final String getZJOrderDetail = baseUrl + "api2/entity/"+zjddxqMenuId+"/detail/";
    public static final String getRKOrderDetail = baseUrl + "api2/entity/"+ddrkMenuId+"/detail/";
    public static final String getDQPHDetail = baseUrl + "api2/entity/"+dqphMenuId+"/detail/";
    public static final String getZYDDDetail = baseUrl + "api2/entity/"+zyddMenuId+"/detail/";
    public static final String getEntityListData=baseUrl+"api2/entity/list/queryKey/data";
    public static final String saveZhiJianBaoGao = baseUrl + "api2/entity/"+zjbgMenuId+"/detail/normal";
    public static final String saveOrderRK = baseUrl + "api2/entity/"+ddrkMenuId+"/detail/normal";
    public static final String initFieldOptions = baseUrl + "api2/meta/dict/field_options";

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



