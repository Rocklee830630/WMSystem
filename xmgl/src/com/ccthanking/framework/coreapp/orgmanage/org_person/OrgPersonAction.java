package com.ccthanking.framework.coreapp.orgmanage.org_person;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.ccthanking.framework.Constants;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager;
import com.ccthanking.framework.coreapp.orgmanage.RightManager;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.framework.util.ResponseUtil;

public class OrgPersonAction extends BaseDispatchAction {

	public OrgPersonAction() {
	}

	private Logger logger = com.ccthanking.framework.log.log
			.getLogger("OrgPersonAction");

	public ActionForward insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPowerWriteXml(request, response, "YHGL");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		HashMap tempData = new HashMap();
		insertParas(request, response, tempData);
		Connection conn = DBUtil.getConnection();
		String sql = "INSERT INTO FS_ORG_PERSON (ACCOUNT,PASSWORD,NAME,SEX,DEPARTMENT,PARENT,PERSON_KIND,USER_SN,LEVEL_NAME,SECRET_LEVEL,idcard,certcode,smtp,mailfrom,mailname,mailpsw,USERTEMPLATE,SJHM) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn.setAutoCommit(false);
			String level = OrgDeptManager.getInstance()
					.getDeptByID((String) tempData.get("DEPARTMENT"))
					.getDeptType();
			Object[] objs = { tempData.get("ACCOUNT"),
					Encipher.EncodePasswd((String) tempData.get("PASSWORD")),
					tempData.get("NAME"), tempData.get("SEX"),
					tempData.get("DEPARTMENT"), tempData.get("PARENT"),
					tempData.get("PERSON_KIND"), tempData.get("USER_SN"),
					level, tempData.get("SECRET_LEVEL"),
					tempData.get("IDCARD"), tempData.get("CERTCODE"),
					tempData.get("SMTP"), tempData.get("MAILFROM"),
					tempData.get("MAILNAME"),
					Encipher.EncodePasswd((String) tempData.get("MAILPSW")),
					tempData.get("USERTEMPLATE"), tempData.get("SJHM") };
			DBUtil.executeUpdate(conn, sql, objs);
			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_USER,
					(String) tempData.get("ACCOUNT"), CacheManager.ADD);
			LogManager.writeUserLog(
					"",
					"",
					Globals.OPERATION_TYPE_INSERT,
					LogManager.RESULT_SUCCESS,
					"添加用户 [" + tempData.get("ACCOUNT") + " / "
							+ tempData.get("NAME") + "] 成功", user,"ACCOUNT",tempData.get("ACCOUNT").toString());
			Pub.writeXmlInfoMessage(response, "添加成功！");
		} catch (Exception e) {
			conn.rollback();
			LogManager.writeUserLog(
					"",
					"",
					Globals.OPERATION_TYPE_INSERT,
					LogManager.RESULT_FAILURE,
					"添加用户 [" + tempData.get("ACCOUNT") + " / "
							+ tempData.get("NAME") + "] 失败！"+e.getMessage(), user,null,null);
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPowerWriteXml(request, response, "YHGL");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		HashMap tempData = new HashMap();
		insertParas(request, response, tempData);
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String level = OrgDeptManager.getInstance()
					.getDeptByID((String) tempData.get("DEPARTMENT"))
					.getDeptType();
			String sql = "UPDATE FS_ORG_PERSON SET NAME=? ,SEX=? ,DEPARTMENT=? ,PARENT=? ,"
					+ "PERSON_KIND=? ,USER_SN=? ,LEVEL_NAME=? ,SECRET_LEVEL=?"
					+ ",IDCARD=?,CERTCODE=?,SMTP=?,MAILFROM=?"
					+ ",MAILNAME=?,MAILPSW=?,USERTEMPLATE=?,SJHM=? WHERE ACCOUNT=?";
			Object[] objs = { tempData.get("NAME"), tempData.get("SEX"),
					tempData.get("DEPARTMENT"), tempData.get("PARENT"),
					tempData.get("PERSON_KIND"), tempData.get("USER_SN"),
					level, tempData.get("SECRET_LEVEL"),
					tempData.get("IDCARD"), tempData.get("CERTCODE"),
					tempData.get("SMTP"), tempData.get("MAILFROM"),
					tempData.get("MAILNAME"),
					Encipher.EncodePasswd((String) tempData.get("MAILPSW")),
					tempData.get("USERTEMPLATE"), tempData.get("SJHM"),
					tempData.get("ACCOUNT") };
			DBUtil.executeUpdate(conn, sql, objs);
			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_USER,
					(String) tempData.get("ACCOUNT"), CacheManager.UPDATE);
			LogManager.writeUserLog(
					"",
					"",
					Globals.OPERATION_TYPE_UPDATE,
					LogManager.RESULT_SUCCESS,
					"修改用户 [" + tempData.get("ACCOUNT") + " / "
							+ tempData.get("NAME") + "] 成功", user,"ACCOUNT",tempData.get("ACCOUNT").toString());

			Pub.writeXmlInfoMessage(response, "修改成功！");
		} catch (Exception e) {
			conn.rollback();
			LogManager.writeUserLog(
					"",
					"",
					Globals.OPERATION_TYPE_UPDATE,
					LogManager.RESULT_FAILURE,
					"修改用户 [" + tempData.get("ACCOUNT") + " / "
							+ tempData.get("NAME") + "] 失败！"+e.getMessage(), user,"ACCOUNT",tempData.get("ACCOUNT").toString());

			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPowerWriteXml(request, response, "YHGL");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		HashMap tempData = new HashMap();
		insertParas(request, response, tempData);
		String account = (String) tempData.get("ACCOUNT");
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			// 增加用户注销标志
			String sql = "update fs_org_person t set t.flag='" + Globals.INVALID
					+ "' where t.account='" + account + "'";
			boolean res = DBUtil.execSql(conn, sql);
			// 删除用户对应角色
			sql = "delete fs_org_role_psn_map t where t.person_account='"
					+ account + "'";
			res = res & DBUtil.execSql(conn, sql);
			logger.info("account [" + account + "] 注销...");
			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_USER, account,
					CacheManager.DELETE);
			Pub.writeXmlInfoMessage(response, "操作成功！");
			LogManager.writeUserLog(
					"",
					"",
					Globals.OPERATION_TYPE_DELETE,
					LogManager.RESULT_SUCCESS,
					"删除用户 [" + tempData.get("ACCOUNT") + " / "
							+ tempData.get("NAME") + "] 成功", user,"ACCOUNT",account);

		} catch (Exception e) {
			conn.rollback();
			LogManager.writeUserLog(
					"",
					"",
					Globals.OPERATION_TYPE_DELETE,
					LogManager.RESULT_FAILURE,
					"删除用户 [" + tempData.get("ACCOUNT") + " / "
							+ tempData.get("NAME") + "] 失败！"+e.getMessage(), user,null,null);
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	// 查询结果列表
	public ActionForward queryList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPowerWriteXml(request, response, "YHGL");
		Document doc = RequestUtil.getDocument(request);
		// 按级别过滤
		String deptID = (String) request.getSession().getAttribute("DEPTID");
		String levelName = (String) request.getSession().getAttribute(
				"USERLEVEL");

		String strFilter = "";
		switch (levelName == "" ? 0 : Integer.parseInt(levelName)) {
		case 100:
			strFilter = " substr(DEPARTMENT,0,2)=" + deptID.substring(0, 2);
			break; // 省厅用户
		case 200:
			strFilter = " substr(DEPARTMENT,0,2)=" + deptID.substring(0, 2);
			break; // 市级用户
		case 300:
			strFilter = " substr(DEPARTMENT,0,6)=" + deptID.substring(0, 6);
			break; // 分局用户
		case 330:
			strFilter = " DEPARTMENT = " + deptID;
			break; // 派出所用户和派出所同级别用户
		}

		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String condition = list == null ? "" : list.getConditionWhere();
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		if (strFilter != null)
			condition += " and " + strFilter;
		condition += " and flag='" + Globals.VALID + "' ";
		condition += " ORDER  BY ACCOUNT ";
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "SELECT ACCOUNT,PASSWORD,NAME,SEX,DEPARTMENT,PARENT,PERSON_KIND,USER_SN,LEVEL_NAME,SECRET_LEVEL,IDCARD,CERTCODE,SMTP,MAILFROM,MAILNAME,MAILPSW,USERTEMPLATE,SJHM FROM fs_ORG_PERSON ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("SEX", "XB");
			bs.setFieldOrgDept("DEPARTMENT");
			// bs.setFieldDic("LEVEL_NAME","BMJB");
			ResponseUtil.writeDocumentXml(response, bs.getDocument());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误!");
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	// 给用户授予角色查询人员列表
	public ActionForward grantPsnList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//RightManager.checkPowerWriteXml(request, response, "YHGL");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String condition = list == null ? "" : list.getConditionWhere();
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		condition += " and department like '" + user.getOrgDept().getSsxq() + "%' ";
		condition += " and flag='" + Globals.VALID + "'";
		condition += " ORDER  BY ACCOUNT ";
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "SELECT ACCOUNT,PASSWORD,NAME,SEX,DEPARTMENT,PARENT,PERSON_KIND,USER_SN,LEVEL_NAME FROM FS_ORG_PERSON ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldOrgDept("DEPARTMENT");
			ResponseUtil.writeDocumentXml(response, bs.getDocument());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误!");
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	// 修改密码
	public ActionForward chgPass(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String userid = (String) request.getSession().getAttribute("USERID");
		String oldPass = (String) request.getParameter("oldpass");
		if (oldPass == null)
			oldPass = "";
		String newPass = (String) request.getParameter("newpass");
		if (newPass == null)
			newPass = "";

		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sqlQuery = "select t.password from fs_org_person t where t.account='"
					+ userid + "'";
			String[][] res = DBUtil.query(sqlQuery);
			String password = "";
			if (res != null && res.length > 0)
				password = res[0][0];
			if (password == null)
				password = "";
			// 对数据库中用户密码进行解密
			Encipher encipher = new Encipher();
			password = encipher.DecodePasswd(password);
			if (!password.equals(oldPass)) {
				Pub.writeXmlErrorMessage(response, "旧密码不正确，请重新输入！！");
				return null;
			}
			// 对用户密码进行加密
			newPass = encipher.EncodePasswd(newPass);
			String sql = "update fs_org_person t set t.password='" + newPass
					+ "' where  t.account='" + userid + "'";
			boolean result = DBUtil.exec(conn, sql);
			logger.info("user [" + userid + "] 修改密码...");
			conn.commit();
			// add by wuxp
			CacheManager.broadcastChanges(CacheManager.CACHE_USER, userid,
					CacheManager.UPDATE);
			LogManager
					.writeUserLog("", "", Globals.OPERATION_TYPE_UPDATE,
							LogManager.RESULT_SUCCESS, "用户 [" + userid + " / "
									+ user.getName() + "] 修改密码成功", user,"ACCOUNT",user.getAccount());
			Pub.writeXmlInfoMessage(response, "操作成功！");
		} catch (Exception e) {
			conn.rollback();
			LogManager.writeUserLog("", "", Globals.OPERATION_TYPE_UPDATE,
					LogManager.RESULT_FAILURE,
					"用户 [" + userid + " / " + user.getName() + "] 修改密码失败！"+e.getMessage(),
					user,"ACCOUNT",user.getAccount());
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	// 密码初始化
	public ActionForward initPass(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPowerWriteXml(request, response, "YHGL");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String account = (String) request.getParameter("account");

		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			Encipher encipher = new Encipher();
			String newPass = encipher.EncodePasswd("0000");
			String sql = "update fs_org_person t set t.password='" + newPass
					+ "' where  t.account='" + account + "'";
			DBUtil.exec(conn, sql);
			logger.info("account [" + account + "] 密码初始化...");
			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_USER, account,
					CacheManager.UPDATE);
			LogManager.writeUserLog(
					"",
					"",
					Globals.OPERATION_TYPE_UPDATE,
					LogManager.RESULT_SUCCESS,
					"初始化用户 ["
							+ account
							+ " / "
							+ UserManager.getInstance()
									.getUserByLoginName(account).getName()
							+ "] 密码成功", user,"ACCOUNT",account);
			Pub.writeXmlInfoMessage(response, "操作成功！");
		} catch (Exception e) {
			conn.rollback();
			LogManager.writeUserLog(
					"",
					"",
					Globals.OPERATION_TYPE_UPDATE,
					LogManager.RESULT_FAILURE,
					"初始化用户 ["
							+ account
							+ " / "
							+ UserManager.getInstance()
									.getUserByLoginName(account).getName()
							+ "] 密码失败！"+e.getMessage(), user,"ACCOUNT",account);
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	// 校验用户名是否存在
	public ActionForward checkAccount(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPowerWriteXml(request, response, "YHGL");
		String account = (String) request.getParameter("account");
		String str = "";
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "SELECT ACCOUNT FROM fs_ORG_PERSON WHERE ACCOUNT = '"
					+ account + "' and FLAG = '1'";
			String[][] res = DBUtil.query(sql);
			if (res != null && res.length > 0)
				str = "用户名已存在！";
			else
				str = "用户名可使用！";
			conn.commit();
			Pub.writeMessage(response, str);
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	private void insertParas(HttpServletRequest request,
			HttpServletResponse response, HashMap data) throws Exception {
		SAXReader reader = new SAXReader();
		InputStream in = request.getInputStream();
		Document doc = reader.read(in);

		List queryRoot = doc.selectNodes("//DATAINFO/ROW/*");
		for (int i = 0; i < queryRoot.size(); i++) {
			Element ele = (Element) queryRoot.get(i);
			if (ele.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			String nodeName = ele.getName();
			Object nodeValue = ele.getData();
			if (nodeName != null && !"".equals(nodeName)) {
				data.put(nodeName, nodeValue);
			}
		}
		return;
	}

	public ActionForward saveZrqPerson(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPowerWriteXml(request, response, "ZrqManager");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String zrq = request.getParameter("zrq");
		String personStr = request.getParameter("persons");
		String persons = "('" + personStr.replaceAll(",", "','") + "')";
		String sql = "update fs_org_person set zrq='" + zrq
				+ "' where account in " + persons;
		if (DBUtil.exec(sql)) {
			Pub.writeXmlInfoMessage(response, "成功分配责任区警员。");
			String[] list = personStr.split(",");
			if (list != null)
				for (int i = 0; i < list.length; i++) {
					if (Pub.empty(list[i]))
						continue;
					CacheManager.broadcastChanges(CacheManager.CACHE_USER,
							list[i], CacheManager.UPDATE);
					LogManager.writeUserLog(
							"",
							"",
							Globals.OPERATION_TYPE_UPDATE,
							LogManager.RESULT_SUCCESS,
							"为责任区 ["
									+ zrq
									+ " / "
									+ OrgDeptManager.getInstance()
											.getDeptByID(zrq).getDeptName()
									+ "] " + " 分配负责人 [" + personStr + "] 成功",
							user,null,null);
				}
		} else
			Pub.writeXmlErrorMessage(response, "操作失败！");
		return null;
	}

	public ActionForward deleteZrqPerson(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RightManager.checkPowerWriteXml(request, response, "ZrqManager");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String account = request.getParameter("pid");
		String sql = "update fs_org_person set ZRQ='' where account='" + account
				+ "'";
		if (DBUtil.exec(sql)) {
			Pub.writeXmlInfoMessage(response, "操作成功，解除责任区警员分配。");
			CacheManager.broadcastChanges(CacheManager.CACHE_USER, account,
					CacheManager.UPDATE);
			LogManager.writeUserLog(
					"",
					"",
					Globals.OPERATION_TYPE_UPDATE,
					LogManager.RESULT_SUCCESS,
					"取消用户 ["
							+ account
							+ " / "
							+ UserManager.getInstance()
									.getUserByLoginName(account).getName()
							+ "] 的责任区分配", user,null,null);
		} else
			Pub.writeXmlErrorMessage(response, "操作失败！");
		return null;
	}

	public ActionForward queryZrqList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		RightManager.checkPowerWriteXml(request, response, "ZrqManager");
		Document doc = RequestUtil.getDocument(request);
		// 按级别过滤
		String deptID = (String) request.getSession().getAttribute("DEPTID");
		String levelName = (String) request.getSession().getAttribute(
				"USERLEVEL");

		String strFilter = "";
		switch (levelName == "" ? 0 : Integer.parseInt(levelName)) {
		case 1:
			strFilter = " substr(DEPARTMENT,0,2)=" + deptID.substring(0, 2);
			break; // 省厅用户
		case 2:
			strFilter = " substr(DEPARTMENT,0,4)=" + deptID.substring(0, 4);
			break; // 市级用户
		case 3:
			strFilter = " substr(DEPARTMENT,0,6)=" + deptID.substring(0, 6);
			break; // 分局用户
		case 4:
			strFilter = " DEPARTMENT = " + deptID;
			break; // 派出所用户和派出所同级别用户
		}

		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String condition = " d.ROW_ID=p.ZRQ(+) and d.depttype=8 and "
				+ list.getConditionWhere();
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		// if (strFilter != null)
		// condition += " and " + strFilter;
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "SELECT d.ROW_ID,d.DEPT_NAME,d.DEPT_PARANT_ROWID,p.NAME,p.ACCOUNT,p.DEPARTMENT FROM FS_ORG_DEPT d,FS_ORG_PERSON p";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldOrgDept("DEPARTMENT");
			bs.setFieldOrgDept("DEPT_PARANT_ROWID");
			ResponseUtil.writeDocumentXml(response, bs.getDocument());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误!");
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}
	
	/**
	 * 用户信息查询
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryListPerson(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Document doc = RequestUtil.getDocument(request);
		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String orderFilter = RequestUtil.getOrderFilter(doc);
		String condition = list == null ? "" : list.getConditionWhere();
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		condition += " and flag='" + Globals.VALID + "' ";
		condition += orderFilter;
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "SELECT ACCOUNT,PASSWORD,NAME,SEX,DEPARTMENT,PARENT,PERSON_KIND,USER_SN,LEVEL_NAME,SECRET_LEVEL,IDCARD,CERTCODE,SMTP,MAILFROM,MAILNAME,MAILPSW,USERTEMPLATE,SJHM FROM FS_ORG_PERSON ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("SEX", "XB");
			bs.setFieldOrgDept("DEPARTMENT");
			ResponseUtil.writeDocumentXml(response, bs.getDocument());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误!");
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}
	
	/**
	 * 增加人员
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		HashMap tempData = new HashMap();
		insertParas(request, response, tempData);
		Connection conn = DBUtil.getConnection();
		
		//增加用户名的是否唯一的验证 add by hongpeng.dong at 2013.2.25
		String checksql = "SELECT ACCOUNT FROM FS_ORG_PERSON WHERE ACCOUNT = ? and FLAG = '1'";
		Object[] paras = { tempData.get("ACCOUNT")};
		String[][] res = DBUtil.querySql(conn, checksql, paras);
		if (res != null && res.length > 0){
			Pub.writeXmlInfoMessage(response,  "用户名已存在，请重新添加！");
			return null;
		}
		//~
		
		String sql = "INSERT INTO FS_ORG_PERSON (ACCOUNT,PASSWORD,NAME,SEX,DEPARTMENT,LEVEL_NAME,SJHM) VALUES(?,?,?,?,?,?,?)";
		try {
			conn.setAutoCommit(false);
			String level = OrgDeptManager.getInstance()
					.getDeptByID((String) tempData.get("DEPARTMENT"))
					.getDeptType();
			Object[] objs = { tempData.get("ACCOUNT"),
					Encipher.EncodePasswd((String) tempData.get("PASSWORD")),
					tempData.get("NAME"), tempData.get("SEX"),
					tempData.get("DEPARTMENT"),
					level, tempData.get("SJHM") };
			DBUtil.executeUpdate(conn, sql, objs);
			conn.commit();
			LogManager.writeUserLog("", "",
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + user.getName()+
							"添加用户 [" + tempData.get("ACCOUNT") + " / "
							+ tempData.get("NAME") + "] 成功", user,"ACCOUNT",tempData.get("ACCOUNT").toString());
			Pub.writeXmlInfoMessage(response, "操作成功！");
		} catch (Exception e) {
			conn.rollback();
			LogManager.writeUserLog("", "",
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + user.getName()+
							"添加用户 [" + tempData.get("ACCOUNT") + " / "
							+ tempData.get("NAME") + "] 失败", user,"ACCOUNT",tempData.get("ACCOUNT").toString());
			e.printStackTrace(System.out);
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}
	
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		HashMap tempData = new HashMap();
		insertParas(request, response, tempData);
		Connection conn = DBUtil.getConnection();
		String sql = "update FS_ORG_PERSON set NAME=?,SEX=?,DEPARTMENT=?,LEVEL_NAME=?,SJHM=? where account = ?";
		try {
			conn.setAutoCommit(false);
			String level = OrgDeptManager.getInstance()
					.getDeptByID((String) tempData.get("DEPARTMENT"))
					.getDeptType();
			Object[] objs = {tempData.get("NAME"), tempData.get("SEX"),
					tempData.get("DEPARTMENT"),
					level, tempData.get("SJHM"),tempData.get("ACCOUNT") };
			DBUtil.executeUpdate(conn, sql, objs);
			conn.commit();
			LogManager.writeUserLog("", "",
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + user.getName()+
					"修改用户 [" + tempData.get("ACCOUNT") + " / "
					+ tempData.get("NAME") + "] 成功", user,"ACCOUNT",tempData.get("ACCOUNT").toString());
			Pub.writeXmlInfoMessage(response, "操作成功！");
		} catch (Exception e) {
			conn.rollback();
			LogManager.writeUserLog("", "",
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + user.getName()+
					"修改用户 [" + tempData.get("ACCOUNT") + " / "
					+ tempData.get("NAME") + "] 失败", user,"ACCOUNT",tempData.get("ACCOUNT").toString());
			e.printStackTrace(System.out);
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}
	
	/**
	 * 删除用户
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward disable(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User user = (User) request.getSession().getAttribute(
				com.ccthanking.framework.Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String account = Pub.val(request, "account");
		User tmp = null;
		try {
			conn.setAutoCommit(false);
			String sql = "update fs_org_person set flag = '0' where account = '" + account + "'";
			DBUtil.execUpdateSql(conn, sql);
			conn.commit();
			tmp = UserManager.getInstance().getUserFromDB(account, false);
			Pub.writeMessage(response, "操作成功！");
			LogManager.writeUserLog("", "",
					Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + user.getName()+
					"删除用户 [" + tmp.getAccount() + " / "
					+ tmp.getName() + "] 成功", user,"ACCOUNT",tmp.getAccount());
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, this.handleError(e));
			LogManager.writeUserLog("", "",
					Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + user.getName()+
					"删除用户 [" + tmp.getAccount() + " / "
					+ tmp.getName() + "] 失败", user,"ACCOUNT",tmp.getAccount());
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}
	
	/**
	 * 初始化密码
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward initPassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		
		User user = (User) request.getSession().getAttribute(
				com.ccthanking.framework.Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String account = Pub.val(request, "account");
		String password = Pub.val(request, "password");
		User tmp = null;
		try {
			conn.setAutoCommit(false);
			String sql = "update FS_org_person set password = '"+Encipher.EncodePasswd(password)+"' where account = '" + account + "'";
			DBUtil.execUpdateSql(conn, sql);
			conn.commit();
			tmp = UserManager.getInstance().getUserFromDB(account, false);
			Pub.writeMessage(response, "操作成功！");
			LogManager.writeUserLog("", "",
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + user.getName()+
					"初始用户 [" + tmp.getAccount() + " / "
					+ tmp.getName() + "]的密码 成功", user,"ACCOUNT",tmp.getAccount());
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, this.handleError(e));
			LogManager.writeUserLog("", "",
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + user.getName()+
					"初始用户 [" + tmp.getAccount() + " / "
					+ tmp.getName() + "]的密码 成功", user,"ACCOUNT",tmp.getAccount());
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

}