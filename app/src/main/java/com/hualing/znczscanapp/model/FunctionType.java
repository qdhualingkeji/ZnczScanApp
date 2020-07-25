package com.hualing.znczscanapp.model;

/**
 * @author 马鹏昊
 * @date {2017-12-19}
 * @des 功能业务线类型
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class FunctionType {

    //未选择
    public static final int NON_SELECTED = -1;

    //质检员模块
    public static final int ZHI_JIAN_YUAN = 30;

    //库管模块
    public static final int KU_GUAN = 31;

    //排队查询
    public static final int PAI_DUI_CHA_XUN = 32;

    //成品入库业务线
    public static final int PRODUCT_IN = 33;

    //成品出库业务线
    public static final int PRODUCT_OUT = 34;

    //物料投料业务线
    public static final int MATERIAL_THROW = 35;

    //物料退库业务线
    public static final int MATERIAL_RETURN = 36;

    //半成品投料业务线
    public static final int HALF_PRODUCT_THROW = 37;

    //半成品退库业务线
    public static final int HALF_PRODUCT_RETURN = 38;

    //追溯业务线
    public static final int DATA_TRACK = 39;

    //审核业务线
    public static final int VERIFY = 40;

    //质检业务线
    public static final int QUALITY_CHECKING = 41;

    //审核不过关修改录入的数据业务线
    public static final int MODIFY_DATA = 42;

    //用户管理线
    public static final int USER_MANAGER = 43;

}
