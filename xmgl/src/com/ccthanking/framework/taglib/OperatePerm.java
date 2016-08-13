package com.ccthanking.framework.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;

public class OperatePerm extends TagSupport {
	
	private String url;
	
	public void setUrl(String url){
		this.url = url;
	}
	

	public int doStartTag() throws JspTagException {
		
		int result = SKIP_BODY;
		
		User user = (User) ((HttpServletRequest) pageContext.getRequest())
				.getSession().getAttribute(Globals.USER_KEY);
		
		try {
			if(user.getPersonKind().equals(user.SUPER_USER) || hasResourceRole(user)){
				result = EVAL_BODY_INCLUDE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int doEndTag() throws JspTagException {
		// 值回返
		return EVAL_PAGE;
	}
	
	private boolean hasResourceRole(User user) throws Exception{
		boolean result = false;
		String userRoles = user.getRoleIdInCondition();
		if(StringUtils.isBlank(userRoles)){
			return result;
		}
		String sql = "select  (select URL from FS_PAGE_RESOURCE where FS_PAGE_RESOURCE_ID = t.MENU_NAME) from fs_org_role_menu_map t where role_id in ("+userRoles+")";
		/**
		Object[] para = new Object[1];
		para[0] = userRoles;
		String[][] resource = DBUtil.querySql(sql, para);
		**/
		String[][] resource = DBUtil.query(sql);
		if(null == resource){
			return result;
		}
		for(int i=0;i<resource.length;i++){
			if(StringUtils.isBlank(resource[i][0])){
				continue;
			}
			if(url.equals(resource[i][0])){
				result = true;
				break;
			}
		}
		return result;
	}

}
