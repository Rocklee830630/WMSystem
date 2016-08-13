package com.ccthanking.framework.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.RoleTreeVO;
import com.ccthanking.framework.common.RoleVO;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.service.RoleService;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class RoleServiceImpl implements RoleService {
	
	/* (non-Javadoc)
	 * @see com.ccthanking.framework.service.impl.RoleService#queryRole(java.lang.String, com.ccthanking.framework.common.User)
	 */
	@Override
	public String queryRole(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += " AND SFYX='1' ";
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String sql = "SELECT ROLE_ID, NAME, S_MEMO, LEVEL_NAME, DEPTID, YWLX, SJBH, BZ, " 
					+ "LRR, LRSJ, LRBM, LRBMMC, GXR, GXSJ, GXBM, GXBMMC, SJMJ, SFYX,ROLETYPE,PARENTROLEID FROM FS_ORG_ROLE";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldOrgDept("DEPTID");
			bs.setFieldUserID("GXR");
			bs.setFieldDic("ROLETYPE", "JSLB");
			
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	/* (non-Javadoc)
	 * @see com.ccthanking.framework.service.impl.RoleService#executeRole(java.lang.String, com.ccthanking.framework.common.User, java.lang.String)
	 */
	@Override
	public String executeRole(String json, User user, String operatorSign)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String msg = null;
		String resultVo = null;
		int sign = Integer.parseInt(operatorSign);
		try {
			RoleVO roleVo = new RoleVO();
			conn.setAutoCommit(false);
			JSONArray list = roleVo.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			
			switch (sign) {
			case 1:
				msg = "添加";
				jsonObj.put("ROLE_ID", new RandomGUID().toString());
				
				roleVo.setValueFromJson(jsonObj);
				BusinessUtil.setInsertCommonFields(roleVo, user);
				BaseDAO.insert(conn, roleVo);
				resultVo = roleVo.getRowJson();
				conn.commit();

				com.ccthanking.framework.coreapp.orgmanage.OrgRoleManager
						.getInstance().synchronize(jsonObj.getString("ROLE_ID"), CacheManager.ADD);
				break;

			case 2:

				msg = "修改";
				roleVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(roleVo, user);
				BaseDAO.update(conn, roleVo);
				resultVo = roleVo.getRowJson();
				conn.commit();
				
				com.ccthanking.framework.coreapp.orgmanage.OrgRoleManager
						.getInstance().synchronize(jsonObj.getString("ROLE_ID"), CacheManager.UPDATE);
				break;
				
			case 3:

				msg = "删除";
				// 状态是0表示删除
				jsonObj.put("SFYX", "0");
				roleVo.setValueFromJson(jsonObj);
				BusinessUtil.setUpdateCommonFields(roleVo, user);
				BaseDAO.update(conn, roleVo);
				resultVo = roleVo.getRowJson();
				conn.commit();

				com.ccthanking.framework.coreapp.orgmanage.OrgRoleManager
						.getInstance().synchronize(jsonObj.getString("ROLE_ID"), CacheManager.DELETE);
				break;
				
			default:
				break;
			}
			
			LogManager.writeUserLog(user.getAccount(), YwlxManager.SYSTEM_ROLE,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "角色信息成功", user, "", "");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(user.getAccount(), YwlxManager.SYSTEM_ROLE,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ msg + "角色信息失败", user, "", "");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	
	@Override
	public String queryUnique(String roleName, User user) {
		String sql = "SELECT COUNT(1) CNT FROM FS_ORG_ROLE WHERE NAME = '" + roleName + "'";
		String[][] cnt = DBUtil.query(sql);
		JSONObject jsonObj = new JSONObject();
		if ("0".equals(cnt[0][0])) {
			jsonObj.put("isUnique", "可以使用");
			jsonObj.put("sign", "0");
		} else {
			jsonObj.put("isUnique", "角色名称重复，请重新填写");
			jsonObj.put("sign", "1");
		}
		return jsonObj.toString();
	}

	@Override
	public String queryRoleTree(HttpServletRequest request, String jsonString) {
		Connection conn = null;
//		String s = request.getParameter("str");
//		String parentid = request.getParameter("parentid");
		JSONArray jsonArr = new JSONArray();
		try {
			conn = DBUtil.getConnection();
			String queryDictSql = "select FS_ORG_ROLE_TREE_ID,NODEID,NODENAME,PARENTID,NODELEVEL from FS_ORG_ROLE_TREE t  where NODELEVEL<3" +
					" order by SORT";
//			List list = new ArrayList();
//			List parentList = new ArrayList();
//			if(!Pub.empty(s)){
//				String arrs[] = s.split(",");
//				for(String arr:arrs){
//					String checkedParentSql = "select FS_ORG_ROLE_TREE from FS_ORG_ROLE_TREE where account='"+arr+"'";
//					String parentArr[][] = DBUtil.query(conn, checkedParentSql);
//					if(parentArr!=null){
//						list.add(parentArr[0][0]);
//					}
//					list.add(arr);
//				}
//			}
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json = new JSONObject();
				json.put("id",rsMap.get("FS_ORG_ROLE_TREE_ID"));
				json.put("parentId", rsMap.get("PARENTID"));
				json.put("name", rsMap.get("NODENAME"));
				json.put("NODELEVEL", rsMap.get("NODELEVEL"));
				json.put("SORT", rsMap.get("SORT"));
				json.put("NODEID",rsMap.get("NODEID"));
				//默认展开父节点 
				json.put("selectNode", "true");
				json.put("open", "true");
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
	public String queryRoleByTreeNode(HttpServletRequest request,String json) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		String tid = request.getParameter("tid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			roleCond = " and R.ROLE_ID=L.NODEID ";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("R") + BusinessUtil.getCommonCondition(user,null);
			condition +=orderFilter;
			page.setFilter(condition);
			condition += orderFilter;
			page.setFilter(condition);
			
			String sql = "SELECT ROLE_ID, NAME, S_MEMO, LEVEL_NAME, DEPTID, YWLX, SJBH, BZ, " 
					+ "LRR, LRSJ, LRBM, LRBMMC, GXR, GXSJ, GXBM, GXBMMC, SJMJ, SFYX,ROLETYPE,PARENTROLEID,L.FS_ORG_ROLE_TREE_ID" +
					" FROM FS_ORG_ROLE R," +
					" FS_ORG_ROLE_TREE L ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldOrgDept("DEPTID");
			bs.setFieldUserID("GXR");
			bs.setFieldDic("ROLETYPE", "JSLB");
			
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	@Override
	public String doMoveInTreeNode(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		RoleTreeVO vo = new RoleTreeVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String sjbh = "";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONObject jsono = JSONObject.fromObject(json);
			vo.setNodeid((String)jsono.get("NODEID"));
			vo.setParentid((String)jsono.get("PARENTID"));
			String IDList = vo.getNodeid();
			String IDArr[] = IDList.split(",");
			if(IDArr!=null&&IDArr.length!=0){
				//获取同级别节点中的最大排序号
				String sqlSort = "select max(sort) from FS_ORG_ROLE_TREE where PARENTID='"+vo.getParentid()+"'";
				String arrSort[][] = DBUtil.query(conn, sqlSort);
				int maxSort = arrSort==null||"".equals(arrSort[0][0])?0:Integer.parseInt(arrSort[0][0]);
				//获取同级别节点中的最大排序号
				String sqlLevel = "select NODELEVEL from FS_ORG_ROLE_TREE where FS_ORG_ROLE_TREE_ID='"+vo.getParentid()+"'";
				String arrLevel[][] = DBUtil.query(conn, sqlLevel);
				String nodeLevel = arrLevel==null?"2":arrLevel[0][0];
				RoleTreeVO newVO = new RoleTreeVO();
				for(int i=0;i<IDArr.length;i++){
					maxSort++;
					newVO.setFs_org_role_tree_id(new RandomGUID().toString());
					newVO.setNodeid(IDArr[i]);
					newVO.setNodename("");
					newVO.setParentid(vo.getParentid());
					newVO.setSort(String.valueOf(maxSort));
					newVO.setNodelevel(String.valueOf(Integer.parseInt(nodeLevel)+1));
					BaseDAO.insert(conn, newVO);
				}
			}
			EventVO event = EventManager.createEvent(conn, YwlxManager.SYSTEM_ROLE_TREE, user);//生成事件
			sjbh = event.getSjbh();
			LogManager.writeUserLog(sjbh, YwlxManager.SYSTEM_ROLE_TREE,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "加入角色类别成功", user,"","");
			conn.commit();
			domresult = Pub.makeQueryConditionByID(vo.getParentid(), "FS_ORG_ROLE_TREE_ID");
			domresult = this.queryRoleByTreeNode(request, domresult);
		} catch (Exception e) {
			LogManager.writeUserLog(sjbh, YwlxManager.SYSTEM_ROLE_TREE,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "加入角色类别失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String doMoveOutTreeNode(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		RoleTreeVO vo = new RoleTreeVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String sjbh = "";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			//获取事件编号
			EventVO event = EventManager.createEvent(conn, YwlxManager.SYSTEM_ROLE_TREE, user);//生成事件
			sjbh = event.getSjbh();
			//删业务数据
			RoleTreeVO delVO = new RoleTreeVO();
			delVO.setFs_org_role_tree_id(vo.getFs_org_role_tree_id());
			BaseDAO.delete(conn, delVO);
			LogManager.writeUserLog(sjbh, YwlxManager.SYSTEM_ROLE_TREE,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "加入角色类别成功", user,"","");
			conn.commit();
		} catch (Exception e) {
			LogManager.writeUserLog(sjbh, YwlxManager.SYSTEM_ROLE_TREE,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "加入角色类别失败", user,"","");
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	//获取已存在的角色ID
	public String getExistsNodeID(HttpServletRequest request, String json)throws Exception{
		Connection conn = null;
		String domresult = "";
		String parentID = request.getParameter("parentID");
		String nodeID = "";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql = "select NODEID from FS_ORG_ROLE_TREE where PARENTID='"+parentID+"'";
			String arr[][] = DBUtil.query(conn, sql);
			
			if(arr!=null&&arr.length!=0){
				for(int i=0;i<arr.length;i++){
					nodeID += arr[i][0]+",";
				}
			}
			if(nodeID!=""){
				nodeID = nodeID.substring(0,nodeID.length()-1);
			}
			String rootSql = "select NODEID " +
					" from FS_ORG_ROLE_TREE " +
					" where NODELEVEL='1' " +
					" start with FS_ORG_ROLE_TREE_ID='"+parentID+"' " +
					" connect by  prior PARENTID = FS_ORG_ROLE_TREE_ID";
			String rootArr[][] = DBUtil.query(conn, rootSql);
			String type = rootArr==null?"":rootArr[0][0];
			JSONObject obj = new JSONObject();
			obj.put("NODEID", nodeID);
			obj.put("TYPE", type);
			domresult = obj.toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
