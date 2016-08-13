
package com.ccthanking.framework.service.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BusinessMenu;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.MenuVo;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {
	
	@Override
	public String getAllMenu() {
		Connection conn = DBUtil.getConnection();
		String queryDictSql = "SELECT NAME, TITLE, PARENT, ORDERNO, TARGET, LOCATION, " 
				+ "LEVELNO, IMAGE, ALTIMAGE, APP_NAME, LAYERSNO, MEMO, LRR, LRSJ, LRBM, " 
				+ "LRBMMC, GXR, GXSJ, GXBM, GXBMMC, SJMJ, SFYX FROM FS_EAP_MENU " 
				+ "WHERE SFYX='1' ORDER BY ORDERNO";
		JSONArray jsonArr = new JSONArray();
		JSONObject rootJson = new JSONObject();
		rootJson.put("id", "");
		rootJson.put("parentId", "0");
		rootJson.put("name", "菜单树");
		rootJson.put("open", "true");
		rootJson.put("levelno", "0");
		jsonArr.add(rootJson);
		try {
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json = new JSONObject();
				json.put("id",rsMap.get("NAME"));
			    json.put("parentId", rsMap.get("PARENT"));
			    json.put("name", rsMap.get("TITLE"));
			    
			    json.put("orderno", rsMap.get("ORDERNO"));
			    json.put("target", rsMap.get("TARGET"));
			    json.put("location", rsMap.get("LOCATION"));
			    json.put("levelno", rsMap.get("LEVELNO"));
			    jsonArr.add(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonArr.toString();
	}
	
	@Override
	public String executeMenu(String json, User user, String operatorSign)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		int sign = Integer.parseInt(operatorSign);
		try {
			BusinessMenu menuVo = new BusinessMenu();
			conn.setAutoCommit(false);
			JSONArray list = menuVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			
			switch (sign) {
			case 1:
				msg = "添加";
				
				String queryOrderNoSql = "select max(orderno) maxOrderNo from FS_EAP_MENU where parent='" + jsonObj.getString("PARENT") + "'";
				String[][] orderNo = DBUtil.query(conn, queryOrderNoSql);
				int maxOrderNo = Integer.parseInt(orderNo == null ? "1" : orderNo[0][0]) + 1;
				jsonObj.put("ORDERNO", maxOrderNo);
				
				menuVo.setValueFromJson(jsonObj);
				BusinessUtil.setInsertCommonFields(menuVo, user);
				BaseDAO.insert(conn, menuVo);
				resultVo = menuVo.getRowJson();
				conn.commit();
				
				com.ccthanking.framework.coreapp.orgmanage.MenuManager
						.getInstance().synchronize(menuVo.getName(), CacheManager.ADD);
				break;
			case 2:
				msg = "修改";
				menuVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(menuVo, user);
				BaseDAO.update(conn, menuVo);
				resultVo = menuVo.getRowJson();
				conn.commit();
				
				com.ccthanking.framework.coreapp.orgmanage.MenuManager
						.getInstance().synchronize("", 0);
				break;
			case 3:
				msg = "删除";
				// 状态是0表示删除
				jsonObj.put("SFYX", "0");
				menuVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(menuVo, user);
				BaseDAO.update(conn, menuVo);
				resultVo = menuVo.getRowJson();
				conn.commit();

				com.ccthanking.framework.coreapp.orgmanage.MenuManager
						.getInstance().synchronize("", 0);
				break;
			default:
				break;
			}
			
			LogManager.writeUserLog(user.getAccount(), YwlxManager.SYSTEM_MENU,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "菜单信息成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.SYSTEM_MENU,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "菜单信息失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}

	@Override
	public String queryUnique(String menuName, User user) {
		String sql = "SELECT COUNT(1) CNT FROM FS_EAP_MENU WHERE NAME = '" + menuName + "'";
		String[][] cnt = DBUtil.query(sql);
		JSONObject jsonObj = new JSONObject();
		if ("0".equals(cnt[0][0])) {
			jsonObj.put("isUnique", "可以使用");
			jsonObj.put("sign", "0");
		} else {
			jsonObj.put("isUnique", "菜单名称重复，请重新填写");
			jsonObj.put("sign", "1");
		}
		return jsonObj.toString();
	}
	
	@Override
	public String loadAllMenu(String roleId) {
		Connection conn = DBUtil.getConnection();
		String queryDictSql = "SELECT RM.MENU_NAME MENU_NAME, T.NAME, T.TITLE, T.PARENT, " 
				+ " T.ORDERNO, T.TARGET, T.LOCATION, T.CHIEF, T.LEVELNO "
				+ " FROM (SELECT * FROM FS_EAP_MENU WHERE SFYX='1') T LEFT JOIN FS_ORG_ROLE_MENU_MAP RM "
				+ " ON T.NAME = RM.MENU_NAME AND RM.ROLE_ID = '" + roleId + "' ORDER BY T.ORDERNO";
		JSONArray jsonArr = new JSONArray();
		try {
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json = new JSONObject();
				json.put("id",rsMap.get("NAME"));
			    json.put("parentId", rsMap.get("PARENT"));
			    json.put("name", rsMap.get("TITLE"));
			    if (rsMap.get("NAME").equals(rsMap.get("MENU_NAME"))) {
			    	json.put("checked", "true");
			    	json.put("open", "true");
				}
			    jsonArr.add(json);
			}
			
			String resourceSql  ="select "
					+" f.MENU_NAME,FS_PAGE_RESOURCE_ID,NAME,MEMO,URL,PARENT,SFYX,SSFL"
					+" from FS_PAGE_RESOURCE t LEFT JOIN FS_ORG_ROLE_MENU_MAP f"
					+ " ON t.FS_PAGE_RESOURCE_ID = f.MENU_NAME AND f.ROLE_ID = '" + roleId + "'";
			List<Map<String, String>> resourceList = DBUtil.queryReturnList(conn, resourceSql);
			for (int i = 0; i < resourceList.size(); i++) {
				Map<String, String> resourceMap = resourceList.get(i);
				JSONObject json = new JSONObject();
				json.put("id",resourceMap.get("FS_PAGE_RESOURCE_ID"));
			    json.put("parentId", resourceMap.get("PARENT"));
			    json.put("name", resourceMap.get("NAME"));
			    if (resourceMap.get("FS_PAGE_RESOURCE_ID").equals(resourceMap.get("MENU_NAME"))) {
			    	json.put("checked", "true");
			    	json.put("open", "true");
				}
		    	json.put("icon", "/xmgl/img/diy/2.png");
			    jsonArr.add(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonArr.toString();
	}
	
	@Override
	public void awardMenuToRole(String roleId, String[] menuName, User user) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String deleteMenuByRoleSql = "DELETE FROM FS_ORG_ROLE_MENU_MAP WHERE ROLE_ID='" + roleId + "'";
			boolean bDel = DBUtil.exec(conn, deleteMenuByRoleSql);
			
			if (bDel) {
				for (int i = 0; i < menuName.length; i++) {
					String addMenuToRoleSql = "INSERT INTO FS_ORG_ROLE_MENU_MAP(ROLE_ID, MENU_NAME) " 
							+ " VALUES ('" + roleId + "','" + menuName[i] + "')";
					DBUtil.exec(conn, addMenuToRoleSql);
				}
			}
			conn.commit();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.SYSTEM_MENU,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "为角色分配菜单成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			LogManager.writeUserLog(user.getAccount(), YwlxManager.SYSTEM_MENU,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "为角色分配菜单失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
	}

	@Override
	public MenuVo findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuVo> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuVo> find(List<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int remove(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int remove(List<Integer> ids) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(MenuVo bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(MenuVo bean) {
		// TODO Auto-generated method stub
		return 0;
	}
}
