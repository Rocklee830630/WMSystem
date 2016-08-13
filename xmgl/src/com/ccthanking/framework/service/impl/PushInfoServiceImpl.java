package com.ccthanking.framework.service.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;

/**
 * @auther xhb 
 */
@Service
public class PushInfoServiceImpl {
	
	public String getPushInfoMenu() {
		Connection conn = null;
		String sql = "SELECT OPERATOR_NO,OPERATOR_NAME,CATEGORY_ID FROM FS_PUSHINFO WHERE IS_EFFECT='1' ORDER BY ORDERBY_NO";
		JSONArray jsonArray = new JSONArray();
		try {
			conn = DBUtil.getConnection();
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, sql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id", rsMap.get("OPERATOR_NO"));
				jsonObj.put("parentId", rsMap.get("CATEGORY_ID"));
				jsonObj.put("name", rsMap.get("OPERATOR_NAME"));
				jsonArray.add(jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonArray.toString();
	}
	
	public void savePushInfoMap(String operator_no, String array, User user) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			String deleteSql = "DELETE FROM FS_PUSHINFO_PSN WHERE OPERATOR_NO='" + operator_no + "'";
			DBUtil.execSql(conn, deleteSql);
			
			String[] arrayList = "".equals(array) ? new String[0] : array.split(",");
			for (int i = 0; i < arrayList.length; i++) {
				String userId = arrayList[i].split("=")[0];
				String deptId = arrayList[i].split("=")[1];
				
				String insertSql = "INSERT INTO FS_PUSHINFO_PSN (OPERATOR_NO, USERID, DEPTID) " 
						+ "VALUES ('" + operator_no + "','" + userId + "','" + deptId + "')";
				DBUtil.execSql(conn, insertSql);
			}
			conn.commit();

			com.ccthanking.framework.coreapp.orgmanage.PushPersonManager.getInstance().reBuildMemory();
			
			LogManager.writeUserLog(user.getAccount(), YwlxManager.SYSTEM_PUSHINFO,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "为用户分配推送信息角色成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.SYSTEM_PUSHINFO,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "为用户分配推送信息角色失败", user, "", "");
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
	}
}
