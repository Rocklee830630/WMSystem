package com.ccthanking.framework.coreapp.orgmanage.orgrolepsnmap;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.Role;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.coreapp.orgmanage.OrgRoleManager;
import com.ccthanking.framework.util.Pub;

public class OrgRolePsnMapAction extends BaseDispatchAction {
	
	public OrgRolePsnMapAction() {
	}

	private static org.apache.log4j.Logger logger = org.apache.log4j.LogManager
			.getLogger("SysParaConfigureAction");

	public ActionForward insertOrDelet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String strAdd = request.getParameter("add");
		String strPsnAccount = request.getParameter("person_Account");
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String[] list = strAdd.split("\\|");
			DBUtil.execSql(conn,
					"delete FS_org_role_psn_map where PERSON_ACCOUNT='"
							+ strPsnAccount + "'");
			ps = conn
					.prepareStatement("insert into FS_org_role_psn_map cols(ROLE_NAME,PERSON_ACCOUNT) values(?,?)");
			for (int i = 0; i < list.length; i++) {
				if (Pub.empty(list[i]))
					continue;
				ps.setString(1, list[i]);
				ps.setString(2, strPsnAccount);
				ps.execute();
			}
			conn.commit();
			/*
			 * CacheManager.broadcastChanges(CacheManager.CACHE_USER,
			 * strPsnAccount, CacheManager.MAP_CHANGEED);
			 */

			Pub.writeXmlInfoSaveOK(response);
		} catch (Exception e) {
			conn.rollback();
			logger.error(e);
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

	public ActionForward grantPsn(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String strAdd = request.getParameter("add");
		String strRoleName = request.getParameter("roleName");
		Role role = OrgRoleManager.getInstance().getRole(strRoleName);
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {
			String[] list = strAdd.split("\\|");
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			ps = conn
					.prepareStatement("insert FS_into org_role_psn_map cols(ROLE_NAME,PERSON_ACCOUNT) values(?,?)");
			ps2 = conn
					.prepareStatement("delete FS_org_role_psn_map where ROLE_NAME=? and PERSON_ACCOUNT=?");
			for (int i = 0; i < list.length; i++) {
				if (Pub.empty(list[i]))
					continue;
				ps2.setString(1, strRoleName);
				ps2.setString(2, list[i]);
				ps2.execute();

				ps.setString(1, strRoleName);
				ps.setString(2, list[i]);
				ps.execute();
			}
			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_ROLE,
			role.getName(), CacheManager.MAP_CHANGEED);
			Pub.writeXmlInfoSaveOK(response);
		} catch (Exception e) {
			conn.rollback();
			logger.error(e);
			Pub.writeXmlErrorMessage(response, e.getMessage());
		} finally {
			if (ps != null)
				ps.close();
			ps = null;
			if (ps2 != null)
				ps2.close();
			ps2 = null;
			if (conn != null)
				conn.close();
			conn = null;
		}
		return null;
	}
}