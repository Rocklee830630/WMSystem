package com.ccthanking.framework.spflow;

import com.ccthanking.framework.common.*;

import java.sql.Connection;
import com.ccthanking.common.vo.ApWsTypzVO;
import com.ccthanking.framework.wsdy.*;
import com.ccthanking.framework.util.*;
import com.ccthanking.framework.coreapp.aplink.*;
import java.util.*;

/*
 该配置类是为了处理
 不同的级别的
 */
public class WsConfigManager {
	/*
	 * modified by songxb@2007-12-05 添加参数condition
	 * 当同一业务类型，存在相同角色的多条记录时，作为条件唯一定位一条记录
	 */
	public static java.util.Collection getOperationSteps(String ywlx,
			User user, String condition) throws Exception {
		if (Pub.empty(ywlx) || user == null) {
			throw new Exception("不满足调用条件！");
		}
		Connection conn = DBUtil.getConnection();
		Collection steps = null;
		try {
			ApWsTypzVO typzvo = getDefaultConfig(ywlx, user, condition);// modified
																				// by
																				// songxb@2007-12-05
																				// 增加了condition参数的重载函数
			if (typzvo != null) {
				String operationOID = typzvo.getOperationoid();
				com.ccthanking.framework.coreapp.aplink.Process process = ProcessTypeMgr
						.getProcessByOperationOID(conn, operationOID);
				steps = process.getSteps();
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			if (conn != null)
				conn.close();
		}
		return steps;

	}

	public static ApWsTypzVO getDefaultConfig(String ywlx, User user)
			throws Exception {
		String ws_template_id = null;
		String operationoid = null;
		String userLevel = user.getLevelName();
		String userRolelist = user.getRoleListString();
		String rolename = null;
		// 流程查询语句增加根据部门过滤的条件addby zhangj 09-07-22
		OrgDept dept = user.getOrgDept();
		String sqlQuery = "select ws_template_id,rolename,DEPT_LEVEL,OPERATIONOID from ap_ws_typz where ywlx= ? and deptid = ?";
		ApWsTypzVO typzvo = null;
		Object[] objs = new Object[3];
		objs[0] = ywlx;
		objs[1] = dept.getDeptID();
		Connection conn = DBUtil.getConnection();
		try {
			QuerySet qs = DBUtil.executeQuery(sqlQuery, objs, conn);
			// 根据部门id查找流程，如没有继续向上一级 addby zhangj 09-07-22
			while (qs.getRowCount() <= 0) {
				dept = dept.getParent();
				objs[1] = dept.getDeptID();
				qs = DBUtil.executeQuery(sqlQuery, objs, conn);
				if (objs[1].equals("100000000000")) {
					break;
				}
			}
			// addby zhangj end 09-07-22
			if (qs.getRowCount() > 0) {
				for (int i = 0; i < qs.getRowCount(); i++) {
					rolename = qs.getString(i + 1, "ROLENAME");
					if (userRolelist.indexOf(rolename) >= 0) {
						typzvo = new ApWsTypzVO();
						typzvo.setWs_template_id(qs.getString(i + 1,
								"ws_template_id"));
						typzvo.setOperationoid(qs.getString(i + 1,
								"OPERATIONOID"));
						typzvo.setDept_level(qs.getString(i + 1, "DEPT_LEVEL"));
						typzvo.setRolename(qs.getString(i + 1, "ROLENAME"));
						break;
					}
				}
			}
		} catch (Exception E) {
			System.out.println(E);
		} finally {
			if (conn != null)
				conn.close();
		}

		return typzvo;
	}

	/*
	 * add by songxb@2007-12-05 getDefaultConfig()的重载函数 添加参数condition
	 * 当同一业务类型，存在相同角色的多条记录时，作为条件唯一定位一条记录
	 */
	public static ApWsTypzVO getDefaultConfig(String ywlx, User user,
			String condition) throws Exception {
		String ws_template_id = null;
		String operationoid = null;
		//String userLevel = user.getLevelName();
		String userRolelist = user.getRoleListIdString();
		String rolename = null;
		OrgDept dept = user.getOrgDept();
		// 流程查询语句增加根据部门过滤的条件addby zhangj 09-07-22
		String sqlQuery = "select ws_template_id,rolename,DEPT_LEVEL,OPERATIONOID from ap_ws_typz where ywlx= ? and deptid = ?";
		ApWsTypzVO typzvo = null;
		Object[] objs = new Object[2];
		objs[0] = ywlx;
		//objs[1] = userLevel;
		objs[1] = dept.getDeptID();
		// add by songxb@2007-12-05 对condition做校验
		if (condition != null && condition != "undefined"
				&& condition.length() > 0) {
			sqlQuery += " and condition = '" + condition + "' ";
		}
		Connection conn = DBUtil.getConnection();
		try {
			QuerySet qs = DBUtil.executeQuery(sqlQuery, objs, conn);
			// 根据部门id查找流程，如没有继续向上一级 addby zhangj start 09-07-22
			while (qs.getRowCount() <= 0) {
				dept = dept.getParent();
				if(dept==null)
					break;
				objs[1] = dept.getDeptID();
				qs = DBUtil.executeQuery(sqlQuery, objs, conn);
				if (objs[1].equals("100000000000")) {
					break;
				}
			}
			// addby zhangj end 09-07-22
			if (qs.getRowCount() > 0) {
				for (int i = 0; i < qs.getRowCount(); i++) {
					rolename = qs.getString(i + 1, "ROLENAME");
					//if (userRolelist.indexOf(rolename) >= 0) {
						typzvo = new ApWsTypzVO();
						typzvo.setWs_template_id(qs.getString(i + 1,
								"ws_template_id"));
						typzvo.setOperationoid(qs.getString(i + 1,
								"OPERATIONOID"));
						typzvo.setDept_level(qs.getString(i + 1, "DEPT_LEVEL"));
						typzvo.setRolename(qs.getString(i + 1, "ROLENAME"));
						break;
					//}
				}
			}
		} catch (Exception E) {
			System.out.println(E);
		} finally {
			if (conn != null)
				conn.close();
		}

		return typzvo;
	}

	public static String getWsTemplateId(String ywlx, User user)
			throws Exception {

		String ws_template_id = null;
		String userLevel = user.getLevelName();
		// 流程查询语句增加根据部门过滤的条件addby zhangj 09-07-22
		String sqlQuery = "select ws_template_id from za_zfba_jcxx_ws_typz where ywlx= ? and dept_level = ?  and deptid = ?";
		OrgDept dept = user.getOrgDept();
		Object[] objs = new Object[3];
		objs[0] = ywlx;
		objs[1] = userLevel;
		objs[2] = dept.getDeptID();

		Connection conn = DBUtil.getConnection();

		try {

			QuerySet ID = DBUtil.executeQuery(sqlQuery, objs, conn);
			// 根据部门id查找流程，如没有继续向上一级 addby zhangj 09-07-22
			while (ID.getRowCount() <= 0) {
				dept = dept.getParent();
				objs[2] = dept.getDeptID();
				ID = DBUtil.executeQuery(sqlQuery, objs, conn);
				if (objs[2].equals("310000000000")) {
					break;
				}
			}// addby zhangj end 09-07-22
			if (ID.getRowCount() > 0) {
				ws_template_id = ID.getString(1, "ws_template_id");
			}

		} catch (Exception E) {
			System.out.println(E);
		} finally {
			if (conn != null)
				conn.close();
		}
		return ws_template_id;
	}
}