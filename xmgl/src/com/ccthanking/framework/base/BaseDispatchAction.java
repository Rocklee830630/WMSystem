package com.ccthanking.framework.base;

import java.sql.SQLException;

import org.apache.struts.actions.DispatchAction;

public class BaseDispatchAction extends DispatchAction {

	public BaseDispatchAction() {
		super();
	}

	static String Pk_DUPLICATE = "记录已经存在!";
	static String CONSTRAINT_NOT_NULL = "对不起！您插入的数据不完整！请检查您填写的项目是否完整。";
	static String CONSTRAINT_FK_RERERENCE = "对不起！子表已有记录,请先删除子表记录.";
	static String VALUE_TOOLARGE = "对不起！您插入的字符串长度过长！";
	static String INVALID_USERNAME_PASSWORD = "无效的数据库用户或口令！";
	static String DML_LOCKS = "对不起！您操作的记录目前正被其他操作锁定，操作已被取消！";
	static String TIMEOUT_OCCURED = "等待资源发生超时！请尝试重新连接！";
	static String INVALID_NUMBER = "数据类型不匹配，字符类型转换成数字失败！";
	static String FK_NOT_FOUND = "对不起！您输入的数据不合法。可能是新增数据在父表里没有相应记录。";

	public static String handleError(Exception e) {
		String errMessage = null;
		if (e instanceof SQLException) {
			int errId = ((SQLException) e).getErrorCode();
			errMessage = defaultMessage(errId);
			if (errMessage == null) { // not dealed ,we return the default
										// message
				errMessage = e.getMessage();
			}
		} else {
			errMessage = e.getMessage();
		}
		return errMessage;
	}

	protected static String defaultMessage(int id) {
		String userMsg = null;
		if (id == 917)
			return Pk_DUPLICATE;
		if (id == 2292)
			return CONSTRAINT_FK_RERERENCE;
		if (id == 1)
			return Pk_DUPLICATE;
		if (id == 1400)
			return CONSTRAINT_NOT_NULL;
		if (id == 1401)
			return VALUE_TOOLARGE;
		if (id == 1017)
			return INVALID_USERNAME_PASSWORD;
		if (id == 61)
			return DML_LOCKS;
		if (id == 51)
			return TIMEOUT_OCCURED;
		if (id == 1722)
			return INVALID_NUMBER;
		if (id == 2291)
			return FK_NOT_FOUND;
		return null;
	}

}