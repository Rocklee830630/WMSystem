

package com.ccthanking.framework.service;

import com.ccthanking.framework.common.MenuVo;
import com.ccthanking.framework.common.User;



public interface MenuService extends BaseService<MenuVo> {

	/**
	 * @author wangzh
	 * @memo生成菜单数html代码
	 * @return string
	 * @throws Exception 
	 */
	//String getMenuTreeHtml(final User user) throws Exception;
	public String getAllMenu();
	
	public String executeMenu(String json, User user, String operatorSign)
			throws Exception;
	
	public String queryUnique(String menuName, User user);
	
	public String loadAllMenu(String roleId);
	
	public void awardMenuToRole(String roleId, String[] menuName, User user) throws Exception;
}
