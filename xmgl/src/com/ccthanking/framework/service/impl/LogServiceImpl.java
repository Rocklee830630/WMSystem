package com.ccthanking.framework.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.Log;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.LogService;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class LogServiceImpl implements LogService {


	/***
	 * 查询用户的登录日志
	 * @author xhb
	 * @param String json 查询条件和排序Json字符串
	 * @param User user 用户实体Bean
	 * @return String 查询的结果集以json串形式返回
	 */
	@Override
	public String queryLogin(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {

			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			String condSQL = " and LOGINSTATUS!='3'  and USERID not in('demo','superman') ";
			condition += condSQL;
			condition += orderFilter;
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String sql = "SELECT distinct  USERID, UERNAME, USERDEPTID, LOGINIP, " 
					+ "LOGINTIME, LOGINSTATUS, LOGOUTTIME, MONTH FROM FS_LOG_USERLOGIN";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("LOGINSTATUS", "DLZT");
			bs.setFieldOrgDept("USERDEPTID");
			bs.setFieldDateFormat("LOGINTIME", "yyyy-MM-dd HH:mm:ss");
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}

	/***
	 * 查询用户的登录日志
	 * @author xhb
	 * @param String json 查询条件和排序Json字符串
	 * @param User user 用户实体Bean
	 * @return String 查询的结果集以json串形式返回
	 */
	@Override
	public ResultSet queryExportLogin(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		ResultSet domResult =null;
		try {

			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			String condSQL = " and LOGINSTATUS!='3'  and USERID not in('demo','superman') ";
			condition += condSQL;
			condition += " and userdeptid=d.row_id ";
			condition += orderFilter;
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String sql = "SELECT LOGINID, userid, UERNAME, d.dept_name USERDEPTID, LOGINIP, " 
					+ "/*to_char(LOGINTIME,'yyyy-MM-dd HH24:mi:ss')*/ LOGINTIME, LOGINSTATUS, LOGOUTTIME, MONTH FROM FS_LOG_USERLOGIN,fs_org_dept d";
			domResult = DBUtil.exportQuery(conn, sql, page);
			//domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	/***
	 * 查询用户的操作日志
	 * @author xhb
	 * @param String json 查询条件和排序Json字符串
	 * @param User user 用户实体Bean
	 * @return String 查询的结果集以json串形式返回
	 */
	@Override
	public String queryLogOperation(String json, User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			
			String condSQL = " and  USERID not in('demo','superman') ";
			condition += condSQL;
			condition += orderFilter;
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			
			String sql = "SELECT OPID, USERID, USERNAME, USERDEPTID, OPERATEIP, " 
					+ "OPERATETIME, YWLX, SJBH, RESULT, LOGINID, MONTH, MEMO, " 
					+ "OPERATETYPE, YWZJZD, USERDEPTNAME, YWZJZ FROM FS_LOG_USEROPERATION";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("RESULT", "CZJG");
			bs.setFieldDic("OPERATETYPE", "CZLX");
			bs.setFieldOrgDept("USERDEPTID");
			
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	
	@Override
	public Log findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Log> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Log> find(List<Integer> ids) {
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
	public int update(Log bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Log bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String queryLogDetail(String json, HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String bg = request.getParameter("begin")!=null&&!"".equals(request.getParameter("begin"))?" and t.LOGINTIME >= to_date('"+request.getParameter("begin")+" 000000"+"', 'YYYY-MM-DD HH24MISS')":"";
			String end = request.getParameter("end")!=null&&!"".equals(request.getParameter("end"))?" and t.LOGINTIME <= to_date('"+request.getParameter("end")+" 235959"+"', 'YYYY-MM-DD HH24MISS')":"";
			String dlType = request.getParameter("dlType");
			String condition = list == null ? "" : list.getConditionWhere();
			PageManager page = RequestUtil.getPageManager(json);
//			condition += orderFilter;
			
			String dlTypeSql = "1".equals(dlType) ? "" : " not ";
			String condSQL = " and  P.DEPARTMENT = D.ROW_ID " +
			"and P.ACCOUNT "+dlTypeSql+" in " + 
/*			"(select P.ACCOUNT " +
			"from FS_LOG_USERLOGIN L, FS_ORG_DEPT D, FS_ORG_PERSON P " +
			"where 1=1 " + bg + end +
			"and D.ROW_ID(+) = P.DEPARTMENT " +
			"and L.USERID(+) = P.ACCOUNT having count(LOGINID) != 0 " +
			"group by P.ACCOUNT, P.NAME, D.DEPT_NAME, D.SORT)" + */
			"(select t.userid " + 
			"FROM FS_LOG_USERLOGIN t " + 
			"where 1=1 " + bg + end +
          	"and LOGINSTATUS != '3' )" + 
			" and p.ACCOUNT not in('demo','superman') and p.flag=1";
			condition += condSQL;
			page.setFilter(condition);
			String sql = "select P.ACCOUNT, P.NAME, D.DEPT_NAME " +
					"from FS_ORG_PERSON P, FS_ORG_DEPT D " ;
					
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("RESULT", "CZJG");
			bs.setFieldDic("OPERATETYPE", "CZLX");
			bs.setFieldOrgDept("USERDEPTID");
			
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
}
