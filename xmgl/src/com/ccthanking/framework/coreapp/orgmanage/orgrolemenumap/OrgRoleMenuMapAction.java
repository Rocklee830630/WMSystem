package com.ccthanking.framework.coreapp.orgmanage.orgrolemenumap;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Document;

import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

public class OrgRoleMenuMapAction extends BaseDispatchAction {

	public OrgRoleMenuMapAction() {
	}

	private static org.apache.log4j.Logger logger = org.apache.log4j.LogManager
			.getLogger("SysParaConfigureAction");

	public ActionForward insertOrDelet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Document doc = RequestUtil.getDocument(request);
		// String strAdd = request.getParameter("add");
		String strAdd = doc.selectSingleNode("/data/add").getText();
		//String strRoleName = request.getParameter("roleName");
		String strRoleName = doc.selectSingleNode("/data/rolename").getText();
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String[] list = strAdd.split("\\|");
			DBUtil.execSql(conn, "delete FS_org_role_menu_map where ROLE_NAME='"
					+ strRoleName + "'");
			ps = conn
					.prepareStatement("insert into FS_org_role_menu_map cols(ROLE_NAME,MENU_NAME) values(?,?)");
			for (int i = 0; i < list.length; i++) {
				if (Pub.empty(list[i]))
					continue;
				ps.setString(1, strRoleName);
				ps.setString(2, list[i]);
				ps.execute();
			}
			ps.close();
			ps = null;
			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_ROLE,
			strRoleName, CacheManager.MAP_CHANGEED);
			Pub.writeXmlInfoSaveOK(response);
		} catch (Exception e) {
			conn.rollback();
			logger.error(e);
			e.printStackTrace();
			Pub.writeXmlErrorMessage(response, e.getMessage());
		} finally {
			if (ps != null)
				ps.close();
			ps = null;
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}
}