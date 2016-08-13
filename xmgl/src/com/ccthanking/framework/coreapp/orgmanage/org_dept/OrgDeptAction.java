package com.ccthanking.framework.coreapp.orgmanage.org_dept;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.ccthanking.framework.Constants;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager;
import com.ccthanking.framework.coreapp.orgmanage.OrgDeptToXml;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.framework.util.ResponseUtil;

public class OrgDeptAction extends BaseDispatchAction {

	public OrgDeptAction() {

	}

	private static org.apache.log4j.Logger logger = org.apache.log4j.LogManager
			.getLogger("OrgDeptAction");

	public ActionForward insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String autoSync = (String) request.getParameter("auto");
		Document doc = RequestUtil.getDocument(request);
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		OrgDeptVO tempVO = new OrgDeptVO();
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			tempVO.setValue((Element) doc.selectSingleNode("/DATAINFO/ROW"));
			BaseDAO.insert(conn, tempVO);
			conn.commit();
			LogManager.writeUserLog("", "", Globals.OPERATION_TYPE_INSERT,
					LogManager.RESULT_SUCCESS, "新增组织机构：" + tempVO.getRowId(),
					user,"ROW_ID",tempVO.getRowId());
			// 同步组织机构xml文件
			if (autoSync != null && autoSync.equals("yes")) {
				String path = getServlet().getServletContext().getRealPath("/");
				OrgDeptToXml.exportAllXml(path, "ZZJG", "'1','2','3','4'");
				// OrgDeptToXml.createCityXml(path + "/dic/city/",
				// tempVO.getOrgDeptRowId());
			}
			Pub.writeXmlInfoSaveOK(response);
			CacheManager.broadcastChanges(CacheManager.CACHE_ORG_DEPT,
					tempVO.getRowId(), CacheManager.ADD);
		} catch (Exception e) {
			conn.rollback();
			logger.error(e);
			Pub.writeXmlErrorMessage(response, handleError(e));
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
		String autoSync = request.getParameter("auto");
		Document doc = RequestUtil.getDocument(request);
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		OrgDeptVO tempVO = new OrgDeptVO();
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			tempVO.setValue((Element) doc.selectSingleNode("/DATAINFO/ROW"));
			BaseDAO.update(conn, tempVO);
			if (autoSync.equals("yes")) {
				String path = getServlet().getServletContext().getRealPath("/");
				OrgDeptToXml.exportAllXml(path, "ZZJG", "'1','2','3','4'");
				// OrgDeptToXml.createCityXml(path + "/dic/city/",
				// tempVO.getOrgDeptRowId());
			}
			conn.commit();
			LogManager.writeUserLog("", "", Globals.OPERATION_TYPE_UPDATE,
					LogManager.RESULT_SUCCESS, "组织机构变更：" + tempVO.getRowId(),
					user,"ROW_ID",tempVO.getRowId());
			Pub.writeXmlInfoUpdateOK(response);
			CacheManager.broadcastChanges(CacheManager.CACHE_ORG_DEPT,
					tempVO.getRowId(), CacheManager.UPDATE);
		} catch (Exception e) {
			conn.rollback();
			logger.error(e);
			Pub.writeXmlErrorMessage(response, e.getMessage());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	// 注销组织机构
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String autoSync = (String) request.getParameter("auto");
		Document doc = RequestUtil.getDocument(request);
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		OrgDeptVO tempVO = new OrgDeptVO();
		Connection conn = DBUtil.getConnection();
		String deptId = "";
		String str = "";
		try {
			conn.setAutoCommit(false);
			tempVO.setValue((Element) doc.selectSingleNode("/DATAINFO/ROW"));
			deptId = tempVO.getRowId();
			String sql = "select count(t.account) from fs_org_person t where t.department='"
					+ deptId + "'";
			String[][] strCount = DBUtil.query(conn, sql);
			if (strCount != null && strCount.length > 0
					&& Integer.parseInt(strCount[0][0]) > 0) {
				str = "该组织机构下有人员，不能删除！";
				Pub.writeXmlInfoMessage(response, str);
			} else {
				sql = "select count(t.row_id) from fs_org_dept t where t.dept_parant_rowid='"
						+ deptId + "'";
				strCount = DBUtil.query(conn, sql);
				if (strCount != null && strCount.length > 0
						&& Integer.parseInt(strCount[0][0]) > 0) {
					str = "该部门下有其他部门，不能删除！";
					Pub.writeXmlInfoMessage(response, str);
				} else {
					// sql =
					// "update org_dept t set t.ACTIVE_FLAG='0' where  t.row_id='"
					// + deptId + "'";
					// boolean res = DBUtil.exec(conn, sql);
					sql = "delete fs_org_dept t where  t.row_id='" + deptId + "'";
					boolean res = DBUtil.exec(conn, sql);
					if (res) {
						conn.commit();
						conn.close();
						conn = null;
						logger.info("组织机构 [" + deptId + "] 删除...");
						str = "操作成功！";
						Pub.writeXmlInfoMessage(response, str);
						LogManager.writeUserLog("", "",
								Globals.OPERATION_TYPE_UPDATE,
								LogManager.RESULT_SUCCESS,
								"组织机构变更：" + tempVO.getRowId(), user,"ROW_ID",tempVO.getRowId());
						CacheManager.broadcastChanges(
								CacheManager.CACHE_ORG_DEPT, deptId,
								CacheManager.DELETE);
						if (autoSync.equals("yes")) {
							String path = getServlet().getServletContext()
									.getRealPath("/");
							OrgDeptToXml.exportAllXml(path, "ZZJG",
									"'1','2','3','4'");
							// OrgDeptToXml.createCityXml(path + "/dic/city/",
							// deptId);
						}
					}
				}
			}
		} catch (Exception e) {
			if (conn != null)
				conn.rollback();
			conn = null;
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}

	public ActionForward queryList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Document doc = RequestUtil.getDocument(request);
		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String condition = list == null ? "" : list.getConditionWhere();
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		condition += " order by  ROW_ID  ASC ";
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "select ROW_ID , DEPT_NAME ,SSXQ, DEPT_PARANT_ROWID , DEPTTYPE, BMJC,ACTIVE_FLAG from fs_ORG_DEPT ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ACTIVE_FLAG", "SF");
			bs.setFieldDic("DEPTTYPE", "BMJB");
			bs.setFieldSimpleXZJG("SSXQ");
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
	 * 查询父子关系,得到导航信息（id 和 部门名称）封装入xml文件
	 */
	public ActionForward navigate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String pid = (String) request.getParameter("pid");

		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sqlNav = "select t.dept_name,t.row_id from fs_org_dept t start with t.row_id="
					+ pid
					+ " connect by t.row_id=prior t.dept_parant_rowid order by t.row_id";
			String navigateString = "";
			String[][] navAl = null;
			navAl = DBUtil.query(sqlNav);
			for (int i = 0; i < navAl.length; i++) {
				navigateString += navAl[i][0] + "|" + navAl[i][1] + "|";
			}
			conn.commit();
			ResponseUtil.writeMessageXml(response, "NAVIGATE", navigateString);
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误!");
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}
	
	private static void writeDeptTree(String id,Element parent){
		Element dept = null;
		String sql = "select row_id,dept_name,dept_parant_rowid from fs_org_dept where dept_parant_rowid = '" + id + "' and active_flag = '1'";
		String[][] result = DBUtil.query(sql);
		if(null == result){return;}
		for (int i = 0; i <= result.length - 1; i++){
			dept = parent.addElement("item").addAttribute("text",result[i][1]).addAttribute("id",result[i][0]).addAttribute("open","1");
			writeDeptTree(result[i][0],dept);
		}
	}
	
	public ActionForward getDeptTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Document document = null;
		try {
			document = DocumentFactory.getInstance().createDocument();
			Element root = document.addElement("tree");
			root.addAttribute("id", "0");
			User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			String sql = "select row_id,dept_name from fs_org_dept where active_flag = '1' and row_id like '" + user.getOrgDept().getSsxq()+ "%' order by depttype";
			String result[][] = DBUtil.query(sql);
			if(null != result && result.length > 0){
				Element dept = root.addElement("item");
				dept.addAttribute("text",result[0][1]).addAttribute("id",result[0][0]).addAttribute("open","1").addAttribute("call","1");//.addAttribute("select","1")
				writeDeptTree(result[0][0],dept);
			}
			
			
			String xml =  document.asXML();
			Pub.writeMessage(response, xml);
			
			return null;
			
		} catch (Exception e) {
			throw e;
		} finally {
			document = null;
		}
	}
	
	public ActionForward insertOrUpdateDept(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String id = Pub.val(request, "id");
		String deptName = Pub.val(request, "deptName");
		String parentId = Pub.val(request, "parentId");
		try {
			String sql = "";
			if(Pub.empty(id)){
				sql = "select depttype from fs_org_dept where row_id = '" + parentId + "'";
				String result[][] = DBUtil.query(conn, sql);
				int level = Integer.parseInt(result[0][0]);
				
				if(6 == level){
					Pub.writeMessage(response, "操作失败，已超出允许的部门最大级别!");
					return null;
				}
				sql = "select nvl(max(to_number(t.id)) + 1,to_number(substr('" +parentId+ "', 0, " +(level+1)*2+ "))+1) from (select substr(row_id,0," +(level+1)*2+ ") id from fs_org_dept where dept_parant_rowid = '" +parentId+ "') t";
				result = DBUtil.query(conn, sql);
				id = result[0][0];
				
				if(id.endsWith("00")){
					Pub.writeMessage(response, "操作失败，同级部门最多不允许超过100个，已超出!");
					return null;
				}
				
				for(int i=id.length();i<12;i++){
					id += "0";
				}
				String ssxq = id.substring(0,4);
				sql = "insert into fs_org_dept(row_id,dept_name,dept_parant_rowid,ssxq,depttype) values(?,?,?,?,?)";
				Object paras[] = new Object[5];
				paras[0] = id;
				paras[1] = deptName;
				paras[2] = parentId;
				paras[3] = ssxq;
				paras[4] = String.valueOf(level+1);
				DBUtil.executeUpdate(conn, sql, paras);
			}else{
				sql = "update fs_org_dept set dept_name = '" + deptName + "' where row_id = '" + id + "'";
				DBUtil.execUpdateSql(conn, sql);
			}
			conn.commit();
			OrgDeptManager.getInstance().reBuildMemory();
			Pub.writeMessage(response, "操作成功！");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeMessage(response, "操作失败！");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}
	
	public ActionForward disable(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String id = Pub.val(request, "id");
		try {
			String sql = "";
			if(Pub.empty(id)){
				Pub.writeMessage(response, "操作失败，没有接收到部门参数！");
				return null;
			}else{
				sql = "update fs_org_dept set active_flag = '0' where row_id = '" + id + "'";
			}
			
			//如果部门下有人员，不允许进行删除
			String hasPersonSql = "select ACCOUNT from fs_org_person where FLAG = '1' and DEPARTMENT = '" + id + "'";
			String result[][] = DBUtil.query(conn, hasPersonSql);
			if(null != result && result.length > 0){
				Pub.writeMessage(response, "操作失败，该部门下还有人员，请对人员进行部门更换或删除后再进行操作！");
				return null;
			}
			//~
			
			DBUtil.execUpdateSql(conn, sql);
			conn.commit();
			OrgDeptManager.getInstance().reBuildMemory();
			Pub.writeMessage(response, "操作成功！");
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
			Pub.writeMessage(response, "操作失败！");
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}
	
}
