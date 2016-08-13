package com.ccthanking.framework.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.coreapp.orgmanage.org_dept.OrgDeptVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.service.DeptService;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class DeptServiceImpl implements DeptService {
	
	/* (non-Javadoc)
	 * @see com.ccthanking.framework.service.impl.DeptService#queryDept(java.lang.String, com.ccthanking.framework.common.User)
	 */
	@Override
	public String queryDept(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += " AND ACTIVE_FLAG='1' ";
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String sql = "SELECT ROW_ID, DEPT_NAME, DEPT_PARANT_ROWID, ACTIVE_FLAG, " 
					+ "SSXQ, DEPTTYPE, BMJC, PDM, PM, CUS_DEPT_LEVEL, JZ, JGLB,EXTEND1,FZR,FGZR,YBZR,SORT FROM FS_ORG_DEPT";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldOrgDept("DEPT_PARANT_ROWID");
			bs.setFieldUserID("FZR");
			bs.setFieldUserID("FGZR");
			bs.setFieldUserID("YBZR");
			bs.setFieldDic("EXTEND1", "SFSXMGLGS");
			
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	/* (non-Javadoc)
	 * @see com.ccthanking.framework.service.impl.DeptService#executeDept(java.lang.String, com.ccthanking.framework.common.User, java.lang.String)
	 */
	@Override
	public String executeDept(String json, User user, String operatorSign)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		int sign = Integer.parseInt(operatorSign);
		try {
			OrgDeptVO deptVo = new OrgDeptVO();
			conn.setAutoCommit(false);
			JSONArray list = deptVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			
			switch (sign) {
			case 1:
				msg = "添加";
				
				String maxSql = "select max(t.row_id) from FS_ORG_DEPT t";
				String[][] maxRowId = DBUtil.query(conn, maxSql);
				long max_row_id = Long.parseLong((maxRowId == null || Pub.empty(maxRowId[0][0])) ? "100000000000" : maxRowId[0][0]);
				
				max_row_id++;
				
				jsonObj.put("ROW_ID", max_row_id);
				// 上级部门
		//		jsonObj.put("DEPT_PARANT_ROWID", "100000000000");
				// 0：无效，1：有效
				jsonObj.put("ACTIVE_FLAG", "1");
				
				deptVo.setValueFromJson(jsonObj);
				BaseDAO.insert(conn, deptVo);
				resultVo = deptVo.getRowJson();
				conn.commit();

				com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager
						.getInstance().synchronize(jsonObj.getString("ROW_ID"), CacheManager.ADD);
				break;

			case 2:

				msg = "修改";
				deptVo.setValueFromJson(jsonObj);
				BaseDAO.update(conn, deptVo);
				resultVo = deptVo.getRowJson();
				conn.commit();

				com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager
						.getInstance().synchronize(jsonObj.getString("ROW_ID"), CacheManager.UPDATE);
				break;
				
			case 3:

				msg = "删除";
				// 状态是0表示删除
				jsonObj.put("ACTIVE_FLAG", "0");
				deptVo.setValueFromJson(jsonObj);
				BaseDAO.update(conn, deptVo);
				resultVo = deptVo.getRowJson();
				conn.commit();

				com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager
						.getInstance().synchronize(jsonObj.getString("ROW_ID"), CacheManager.DELETE);
				break;
				
			default:
				break;
			}
			
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_NBDX,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "部门信息成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_NBDX,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "部门信息失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}

	@Override
	public OrgDeptVO findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrgDeptVO> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrgDeptVO> find(List<Integer> ids) {
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
	public int update(OrgDeptVO bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(OrgDeptVO bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String loadAllDept(String s,String dept) throws Exception {
		Connection conn = DBUtil.getConnection();
		String queryDictSql = "select row_id NAME,bmjc TITLE,DEPT_PARANT_ROWID PARENT,FZR,FGZR,YBZR from FS_ORG_DEPT t" ;
		 if(!Pub.empty(dept)){
			 queryDictSql  +=" where row_id ='"+dept+"' ";
		 }
				queryDictSql +=	" order by sort asc";
		JSONArray jsonArr = new JSONArray();
		try {
			List list = new ArrayList();
			if(!Pub.empty(s)){
				String arrs[] = s.split(",");
				for(String arr:arrs){
					list.add(arr);
				}
			}
			List parList=new ArrayList();
			if(!Pub.empty(s)){
				String sql="select distinct DEPT_PARANT_ROWID from FS_ORG_DEPT where row_id in("+s+")";
				String bmArr[][]=DBUtil.query(conn, sql);
				if(bmArr.length>0){
					for (int a=0;a<bmArr.length;a++){
						parList.add(bmArr[a][0]);
					}
				}
			}
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json = new JSONObject();
				json.put("id",rsMap.get("NAME"));
				if(("0").equals(rsMap.get("PARENT"))){
					json.put("open", "true");
				}
				if(Pub.empty(rsMap.get("PARENT"))){
					json.put("open", "true");
				}
			    json.put("parentId", rsMap.get("PARENT"));
			    json.put("name", rsMap.get("TITLE"));
			    json.put("fzr", rsMap.get("FZR"));
			    String fzr = rsMap.get("FZR");
			    String fzrName = "";
			    if(!Pub.empty(fzr)){
			    	json.put("fzrName",Pub.getUserNameByLoginId(fzr));
			    }else{
			    	json.put("fzrName","");
			    }
			    json.put("fgzr", rsMap.get("FGZR"));
			    String fgzr = rsMap.get("FGZR");
			    String fgzrName = "";
			    if(!Pub.empty(fgzr)){
			    	json.put("fgzrName",Pub.getUserNameByLoginId(fgzr));
			    }else{
			    	json.put("fgzrName","");
			    }
			    if(list.contains(rsMap.get("NAME"))){
			    	json.put("checked", "true");
			    	//json.put("open", "true");
			    }
			    if(parList.contains(rsMap.get("NAME"))){
			    	//json.put("checked", "true");
			    	json.put("open", "true");
			    }
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
	public String leaderSave(String json, User user)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		try {
			OrgDeptVO deptVo = new OrgDeptVO();
			conn.setAutoCommit(false);
			JSONArray list = deptVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			deptVo.setValueFromJson(jsonObj);
			BaseDAO.update(conn, deptVo);
			resultVo = deptVo.getRowJson();
			conn.commit();
			com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager
					.getInstance().synchronize(jsonObj.getString("ROW_ID"), CacheManager.UPDATE);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_NBDX,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "领导信息成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.OA_TXGL_NBDX,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "领导信息失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	
}
