//v20090306-1
//v20090413-2
//v20090531-3
//v20090604-4
package com.ccthanking.framework.coreapp.freequery;

public class FreeQueryProps 
{
	//记录行数限制，这个值可以根据需要重新设置大小，有两个地方用到，但不受影响
	public static final int ROWS_LIMITED_INT = 10000;

	
	//下面所有的值都不可以随意改动，有多处引用了这些数据进行类型判断，在此处对这些类型进行了说明
	
	//FIELD_TYPE表示表fw_user_field中设置的字段类型(style字段)
	public static final String FIELD_TYPE_CHAR_1 = "1";			//字符
	public static final String FIELD_TYPE_IDCARD_2 = "2";		//身份证号
	public static final String FIELD_TYPE_DIC_3 = "3";			//字典
	public static final String FIELD_TYPE_DATE_4 = "4";			//日期
	public static final String FIELD_TYPE_BLOB_5= "5";			//大数据

	//
	public static final String FIELD_KIND_CHAR_1 = "1";			//字符
	public static final String FIELD_KIND_DIC_6 = "6";			//字典
	public static final String FIELD_KIND_CARD_7 = "7";			//
	
	//SET_STYLE表示定义时的可选样式类型
	public static final String SET_STYLE_SET_2 = "2";			//集合（多值）
	public static final String SET_STYLE_AGE_3 = "3";			//年龄
	public static final String SET_STYLE_TYJ_4 = "4";			//同音（简）
	public static final String SET_STYLE_TYQ_5 = "5";			//同音（全）
	public static final String SET_STYLE_RANGE_8 = "8";			//区间
	public static final String SET_STYLE_TREEDIC_11 = "11";		//树形字典
	public static final String SET_STYLE_TABSEL_12 = "12";		//表选
	public static final String SET_STYLE_1518ID_13 = "13";		//兼容15位和18位的身份证号。即输入任意一个号码，可查询两种格式的数据

	//格式化的三类目标
	public static final String FMT_TARGER_NULL_0 = "0";			//对应格式化的“空”
	public static final String FMT_TARGER_VALUE_1 = "1";		//对应格式化的“值”
	public static final String FMT_TARGER_SOURCE_2 = "2";		//对应格式化的“源”

	//FMT_TYPE表示格式化类型
	public static final String FMT_TYPE_TRUNC_1 = "1";			//截取
	public static final String FMT_TYPE_DATE_2 = "2";			//日期
	public static final String FMT_TYPE_CUSTOM_3 = "3";			//自定义
	public static final String FMT_TYPE_ODD0_4 = "4";			//去奇数个0
	public static final String FMT_TYPE_EVEN0_5 = "5";			//去偶数个0
	public static final String FMT_TYPE_FULL0_6 = "6";			//去全部0
	public static final String FMT_TYPE_DIC_7 = "7";			//字典过滤

	//固定值的四个分类
	public static final String FIXED_WSZ_0 = "0";				//未设置，即未选择固定值的情况
	public static final String FIXED_YHZH_1 = "1";				//用户帐户
	public static final String FIXED_YHBM_2 = "2";				//用户部门
	public static final String FIXED_XTSJ_3 = "3";				//系统时间
	public static final String FIXED_MJJB_4 = "4";				//密级级别

	//四个条件类型
	public static final String COND_FILTER_1 = "1";				//过滤条件
	public static final String COND_DYNIMIC_2 = "2";			//动态条件
	public static final String COND_RELATION_3 = "3";			//设置关联
	public static final String COND_FIXED_4 = "4";				//固定值

	//综合查询功能所用到的表，便于该功能在不同的框架中维护
	public static final String MENU = "eap_menu";							//菜单信息
	public static final String USER_TABLE = "fw_user_table";				//业务表信息
	public static final String USER_FIELD = "fw_user_field";				//表的字段信息
	public static final String FREE_QUERY = "fw_free_query";				//定义的查询信息
	public static final String FREE_CUSTOM = "fw_free_custom";				//自定义信息
	public static final String FREE_RELATION = "fw_free_relation";			//表间关联信息
	
	//综合查询常用常量
	public static final String COMBINECONDITION_SHOW = "CS";				//该信息设置到fw_user_field的secret_level字段中，
																			//表示该字段在组合条件查询中显示
}
