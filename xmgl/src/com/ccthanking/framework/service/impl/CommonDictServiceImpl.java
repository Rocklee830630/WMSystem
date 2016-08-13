package com.ccthanking.framework.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.CommonDict;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class CommonDictServiceImpl {
	
	
	public String queryCommonDict(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String querySql = "SELECT DICT_ID, DICT_NAME, DICT_CATEGORY,PXH FROM FS_COMMON_DICT";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
//			bs.setFieldTranslater(fieldName, tableName, codeField, valueField);
			
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	
	public String executeCommonDict(String json, User user, String operatorSign)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		int sign = Integer.parseInt(operatorSign);
		try {
			CommonDict commonDict = new CommonDict();
			conn.setAutoCommit(false);
			JSONArray list = commonDict.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			jsonObj.put("UPDATEPERSON", user.getName());
			jsonObj.put("UPDATEDATE", new Date());
			
			switch (sign) {
			case 1:
				msg = "添加";
				jsonObj.put("DICT_ID", new RandomGUID().toString());
				commonDict.setValueFromJson(jsonObj);
				BaseDAO.insert(conn, commonDict);
				
				break;

			case 2:

				msg = "修改";
				commonDict.setValueFromJson(jsonObj);
				BaseDAO.update(conn, commonDict);
				break;
			
			default:
				break;
			}
			
			resultVo = commonDict.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), "",
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "普通字典信息成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), "",
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "普通字典失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	
	public String sortOne(String json, User user)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		try {
			CommonDict commonDict = new CommonDict();
			conn.setAutoCommit(false);
			JSONArray list = commonDict.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			
			//设置其他排序号为2
			String sql = "update FS_COMMON_DICT set pxh = (pxh+1) where pxh <= 20 and DICT_CATEGORY = '" + jsonObj.get("DICT_CATEGORY") +"'";
			DBUtil.exec(conn,sql);
			
			jsonObj.put("UPDATEPERSON", user.getName());
			jsonObj.put("UPDATEDATE", new Date());
			jsonObj.put("PXH","1");
			commonDict.setValueFromJson(jsonObj);
			BaseDAO.update(conn, commonDict);
			resultVo = commonDict.getRowJson();
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), "",
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
					+ msg + "置顶'"+jsonObj.get("DICT_NAME")+"'成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
}
