package com.ccthanking.framework.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;

public class QuickEntryTag extends TagSupport {
	
	private static final long serialVersionUID = -477160046211526881L;

	private String quickEntryHtml() {
		String appPath = ((HttpServletRequest) pageContext.getRequest())
				.getContextPath();
		User user =  (User)((HttpServletRequest) pageContext.getRequest()).getSession().getAttribute(Globals.USER_KEY);
		String text = "";
		try {
			String roleIds = user.getRoleIdInCondition();
			String deptId = user.getDepartment();
			
			// 角色為空，沒有菜單，沒有快捷入口
			if ("".equals(roleIds)) return text;
			
			String quickEntrySql = "SELECT Z.*,QU.URL FROM (SELECT DISTINCT Q.QUICKENTRY_ID, " 
					+ "Q.QUICKENTRY_NAME QUICKNAME," 
					+ "Q.TARGET," 
					+ "Q.IMAGE,"
                    + "Q.ORDERBY_NO " 
					+ "FROM FS_QUICKENTRY Q,FS_QUICKENTRY_MAP QM,FS_ORG_ROLE R "
					+ "WHERE Q.QUICKENTRY_ID=QM.QUICKID "
					+ "AND QM.ROLEID=R.ROLE_ID "
					// 角色列別是快捷入口
					+ "AND (R.ROLETYPE = '1' OR R.ROLETYPE='3') "
					+ "AND QM.ROLEID IN (" + roleIds + ")     ) Z , FS_QUICKENTRY_URL QU "
					+ "WHERE Z.QUICKENTRY_ID=QU.QUICKENTRY_ID " 
					// 人員所在部門
					+ "AND (QU.DEPTID='" + deptId + "' or QU.DEPTID='999') "
					// URL為有效標誌
					+ "AND QU.IS_EFFECT='1' " 
					+ "ORDER BY to_number(Z.ORDERBY_NO)";
			
			QuerySet qs = DBUtil.executeQuery(quickEntrySql, null);
			for (int i = 0; i < qs.getRowCount(); i++) {
				String quickId = qs.getString(i+1, "QUICKENTRY_ID");
				String quickName = qs.getString(i+1, "QUICKNAME");
				String image = qs.getString(i+1, "IMAGE");
				String target = qs.getString(i+1, "TARGET");
				String url = qs.getString(i+1, "URL");
		//		url += account;
				text += "<a href=\"javascript:void(0)\" id=\"" + quickId + "\" onclick=\"popPage('" + target + "','" + url + "','" + quickName + "')\" data-toggle=\"modal\"><img src=\"" + appPath + "/images/" + image + "\" alt=\"" + quickName + "\"><br><span class=\"title\" style='margin-right:13px;'>" + quickName + "</span></a>";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return text;
	}

	private String getResult() throws JspException {

		StringBuffer sb = new StringBuffer();
		sb.append(quickEntryHtml());
		String str = sb.toString();
		sb = null;
		return str;
	}

	public int doEndTag() throws JspTagException {
		try {
			pageContext.getOut().write(getResult());
		}
		// 捕捉异常
		catch (IOException ex) {
			// 抛出新异常
			// throw new JspTagException("错误"};
		} catch (JspException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 值回返
		return EVAL_PAGE;
	}

}
