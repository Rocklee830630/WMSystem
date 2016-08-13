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
public class QuickEntryServiceImpl {
	
	public String getQuickEntryTree(String roleId) {
		Connection conn = null;
		String sql = "SELECT T.ID, T.NAME, T.PARENTID,M.ROLEID FROM " 
				+ "(SELECT CATEGORY_ID ID,CATEGORY_NAME NAME, '0' PARENTID " 
				+ "FROM FS_QUICKENTRY GROUP BY CATEGORY_ID,CATEGORY_NAME " 
				+ "UNION ALL " 
				+ "SELECT QUICKENTRY_ID ID, TITLE NAME, CATEGORY_ID PARENTID " 
				+ "FROM FS_QUICKENTRY) T ,FS_QUICKENTRY_MAP M "
				+ "WHERE T.ID=M.QUICKID(+) AND M.ROLEID(+)='" + roleId + "' ORDER BY PARENTID";
		JSONArray jsonArray = new JSONArray();
		try {
			conn = DBUtil.getConnection();
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, sql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id", rsMap.get("ID"));
				jsonObj.put("parentId", rsMap.get("PARENTID"));
				jsonObj.put("name", rsMap.get("NAME"));
				if (!"".equals(rsMap.get("ROLEID"))) {
					jsonObj.put("checked", "true");
					jsonObj.put("open", "true");
				}
				jsonArray.add(jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonArray.toString();
	}
	
	public void awardQuickEntryToRole(String roleId, String quickIdArray, User user) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String deleteMenuByRoleSql = "DELETE FROM FS_QUICKENTRY_MAP WHERE ROLEID='" + roleId + "'";
			DBUtil.exec(conn, deleteMenuByRoleSql);
			
			String[] quickId =  "".equals(quickIdArray) ? new String[0] : quickIdArray.split(",");
			for (int i = 0; i < quickId.length; i++) {
				String addMenuToRoleSql = "INSERT INTO FS_QUICKENTRY_MAP(ROLEID, QUICKID) " 
						+ " VALUES ('" + roleId + "','" + quickId[i] + "')";
				DBUtil.exec(conn, addMenuToRoleSql);
			}
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.SYSTEM_ROLE,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "为角色分配快捷入口成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.SYSTEM_ROLE,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "为角色分配快捷入口失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
	}
}
