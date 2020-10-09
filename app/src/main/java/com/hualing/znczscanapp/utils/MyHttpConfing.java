package com.hualing.znczscanapp.utils;

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
    public static final String baseUrl = "http://121.196.184.205:96/hydrocarbon/";
    //public static final String baseUrl = "http://112.6.41.8:90/hydrocarbon/";

    /* 用户登录 */
    public static final String login = baseUrl + "api2/auth/token";
    public static final String getMenuBlocks = baseUrl + "api2/meta/menu/get_blocks";
    public static final String zjddDtmplNormal= baseUrl + "api2/meta/tmpl/"+zjddMenuId+"/dtmpl/normal/";
    public static final String ddrkDtmplNormal= baseUrl + "api2/meta/tmpl/"+ddrkMenuId+"/dtmpl/normal/";
    public static final String zjbgDtmplNormal= baseUrl + "api2/meta/tmpl/"+zjbgMenuId+"/dtmpl/normal/";
    public static final String dqphDtmplNormal=baseUrl + "api2/meta/tmpl/"+dqphMenuId+"/dtmpl/normal/";
    public static final String wdddDtmplNormal=baseUrl + "api2/meta/tmpl/"+wdddMenuId+"/dtmpl/normal/";
    public static final String wdddDtmplRabc=baseUrl + "api2/meta/tmpl/"+wdddMenuId+"/dtmpl/rabc/";
    public static final String dqphDtmplRabc=baseUrl + "api2/meta/tmpl/"+dqphMenuId+"/dtmpl/rabc/";
    public static final String dqphEntityListTmpl=baseUrl+"api2/entity/"+dqphMenuId+"/list/tmpl";
    public static final String zjbgEntityListTmpl=baseUrl+"api2/entity/"+zjbgMenuId+"/list/tmpl";
    public static final String wdddEntityListTmpl=baseUrl+"api2/entity/"+wdddMenuId+"/list/tmpl";
    public static final String zjddEntityListTmpl=baseUrl+"api2/entity/"+zjddMenuId+"/list/tmpl";
    public static final String ddrkEntityListTmpl=baseUrl+"api2/entity/"+ddrkMenuId+"/list/tmpl";
    /* 获得订单详情数据 */
    public static final String getZJOrderDetail = baseUrl + "api2/entity/"+zjddMenuId+"/detail/";
    public static final String getRKOrderDetail = baseUrl + "api2/entity/"+ddrkMenuId+"/detail/";
    public static final String getDQPHDetail = baseUrl + "api2/entity/"+dqphMenuId+"/detail/";
    public static final String getWDDDDetail = baseUrl + "api2/entity/"+wdddMenuId+"/detail/";
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



