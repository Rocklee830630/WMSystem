package com.ccthanking.framework;

/**
 *
 * <p> </p>
 * <p>Description: 系统全局常量定义</p>
 * @version 1.0
 */

public class Globals{
	
	public Globals() {
	}

	// 全局系统对象的 key
	// buffer data in servlet context
	public static final String DBPM_INSTANCE = "GLOBAL.DBPM";
	public static final String BUFFER_KEY_CONTEXT = "GLOBAL.BUFFER_KEY_CONTEXT";
	public static final String BUFFER_KEY_CONTEXT_DEPT = "GLOBAL.DEPT";
	public static final String BUFFER_KEY_CONTEXT_USER = "GLOBAL.USER";
	public static final String BUFFER_KEY_CONTEXT_MENU = "GLOBAL.MENU";
	public static final String BUFFER_KEY_CONTEXT_ROLE = "GLOBAL.ROLE";
	public static final String BUFFER_KEY_CONTEXT_DICS = "GLOBAL.DICS";
	public static final String BUFFER_KEY_CONTEXT_PARA = "GLOBAL.PARA";
	public static final String BUFFER_KEY_CONTEXT_SPELL = "GLOBAL.SPELL";

	// session 中的对象 key
	public static final String USER_KEY = "SESSION.USERINFO"; // session 中的用户对象
	public static final String DEPT_KEY = "SESSION.DEPTINFO"; // session 中的部门对象

	// 应用中的常量约定
	public static final String KEY_ERROR_MESSAGE = "ERRMESSAGE"; // 错误信息标签名称
	public static final String KEY_WHEN_ERROR_GOTO = "WHEN_ERROR_GO_TO"; // 发生错误时跳转页面
	public static final String KEY_ERROR_ACTION = "ERROR_ACTION"; // 发生错误时的执行脚本代码
	public static final String KEY_SUCCESS_MESSAGE = "KEY_SUCCESS_MESSAGE";
	public static final String KEY_WHEN_SUCCESS_GOTO = "KEY_WHEN_SUCCESS_GOTO";
	public static final String KEY_SUCCESS_ACTION = "KEY_SUCCESS_ACTION";

	/****************************** 以下常量定义为审批流使用 ***************************/

	// 以下为事件状态常量
	public static final String EVENT_STATE_REGISTER = "0"; //登记完成
	public static final String EVENT_STATE_APPROVE = "1"; // 审批中
	public static final String EVENT_STATE_APPROVE_SUCCESS = "2"; // 审批通过
	public static final String EVENT_STATE_APPROVE_BREAK = "3"; // 审批不通过
	public static final String EVENT_STATE_APPROVE_AFFIRM = "4"; // 不通过确认
	public static final String EVENT_STATE_CANCELED = "5"; // 事件注销
	public static final String EVENT_STATE_FINISHED = "6"; // 事件完成
	public static final String EVENT_STATE_ROLLBACK = "7"; // 审批退回

	public static final String AP_LINK_URL = "AP_LINK_URL"; // 审批页面参数key
	public static final String PRINT_URL = "PRINT_URL"; // 打印任务处理页面的参数key
	public static final String AFFIRM_URL = "AFFIRM_URL"; // 确认任务页面参数key
	public static final String GENERAL_TASK_URL = "GENERAL_TASK_URL"; // 普通任务处理页面的参数配置的
																		// key
	public static final String DEFAULT_TASK_PROCESS_URL = "DEFAULT_TASK_PROCESS_URL"; // 默认任务处理页面参数
																						// key

	public static final String PRINT_DBDW = "PRINT_DBDW"; // 打印代办单位参数key，下同
	public static final String AP_LINK_DBDW = "AP_LINK_DBDW";
	public static final String AFFIRM_DBDW = "AFFIRM_DBDW";
	public static final String GENERAL_TASK_DBDW = "GENERAL_TASK_DBDW";
	public static final String DEFAULT_TASK_DBDW = "DEFAULT_TASK_DBDW";
	/****************************** 以上常量定义为审批流使用 ***************************/

	// 操作类型
	public static final String OPERATION_TYPE_INSERT = "1"; // 登记
	public static final String OPERATION_TYPE_DELETE = "2"; // 撤销
	public static final String OPERATION_TYPE_UPDATE = "3"; // 变更
	public static final String OPERATION_TYPE_AFFIRM = "4"; // 确认
	public static final String OPERATION_TYPE_ARCHIVE = "5"; // 归档
	public static final String OPERATION_TYPE_APPROVE = "6"; // 审批
	public static final String OPERATION_TYPE_PRINT = "7"; // 打印
	public static final String OPERATION_TYPE_QUERY = "8"; // 查询

	// 是否有效
	public static final String VALID = "1"; // 有效
	public static final String INVALID = "0"; // 无效

	// 最大查询记录限制数量和默认每页显示记录数量
	public static final int MAX_RECORD_LIMITED = 1000;
	public static final int DEFAULT_ROWS_PERPAGE = 10;

	public static final String DOC_KEY = "docxml";
	public static final String YWLX_KEY = "ywlx";
	public static final String PAGE_KEY = "page";
	public static final String SQL_KEY = "sql";
	

	/***************** 收发文状态 FS_DICT_TREE 中ID为9000000000181 add by xhb *******************/
	/* 登记 */
	public static final String OA_GWGL_DJ = "001";
	/* 办理中 */
	public static final String OA_GWGL_BLZ = "002";
	/* 办完 */
	public static final String OA_GWGL_BW = "003";
	/* 审批中断 */
	public static final String OA_GWGL_SPZD = "004";
	/* 办结 */
	public static final String OA_GWGL_BJ = "005";
	/* 确认 */
	public static final String OA_GWGL_QR = "006";
}