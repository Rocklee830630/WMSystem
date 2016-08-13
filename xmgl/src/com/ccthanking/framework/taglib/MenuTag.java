package com.ccthanking.framework.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.MenuVo;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.MenuManager;

public class MenuTag extends TagSupport {

	public String getMenuTreeHtml() throws Exception {
		
		User user = (User) ((HttpServletRequest) pageContext.getRequest())
				.getSession().getAttribute(Globals.USER_KEY);
		
		MenuVo[] menus = null;
		String menuTreeHtml = "";
		menus = getAllowedMenus(user,"");//显示第一层菜单
		if(menus!=null&&menus.length>0)
		{
	      for(int i=0;i<menus.length;i++)
	      {
	    	  String icss = menus[i].getImage();
	    	  if(icss==null||"".equalsIgnoreCase(icss))
	    	  {
	    		  icss = "icon-briefcase";
	    	  }
	    	 // menuTreeHtml +="<h3><i class=\"icon-briefcase icon-white\"></i>"+menus[i].getTitle()+"</h3>\r\n";
	    	  menuTreeHtml   +=" <li> <a href=\"#\"> <i class=\""+icss+" icon-white\"></i>  <span class=\"title\">"+menus[i].getTitle()+"</span> </a>\r\n";
	    	  menuTreeHtml +="<ul class=\"sub-menu\" >\r\n";//style=\"height:400px;display:;overflow:auto;\"
	    	  MenuVo[] menus_two =getAllowedMenus(user,menus[i].getName());
	    	 
	    	  //是否隐藏动态二级菜单
	    	  boolean isDynamicsShow = dynamicsShow(menus_two);
	    	  
	    	  if(menus_two!=null&&menus_two.length>0){
	    		  for(int j=0;j<menus_two.length;j++)
	    		  {
	    			  //~暂时处理动态显示菜单部分
	    			  if(isDynamicsShow && menus_two[j].getDynamicShow().equals("1")){
	    				  MenuVo[] menus_three =getAllowedMenus(user,menus_two[j].getName());
	    				  for(MenuVo tmpVo:menus_three){
	    					  menuTreeHtml +="<li> <a href=\"#\"onclick=\"menutree_click('"+tmpVo.getName()+"','"+tmpVo.getLocation()+"','"+tmpVo.getTitle()+"','"+tmpVo.getTarget()+"')\">"+tmpVo.getTitle()+"</a>\r\n";
	    					  menuTreeHtml +="<div></div>\r\n";
	    				  }
	    				  continue;
	    			  }
	    			  //end~
	    			  
	    			 // menuTreeHtml +="<h4><a href=\"#\" onclick=\"menutree_click('"+menus_two[j].getName()+"','"+menus_two[j].getLocation()+"','"+menus_two[j].getTitle()+"','"+menus_two[j].getTarget()+"')\">"+menus_two[j].getTitle()+"</a></h4>\r\n";
	    			  menuTreeHtml +="<li> <a href=\"#\"onclick=\"menutree_click('"+menus_two[j].getName()+"','"+menus_two[j].getLocation()+"','"+menus_two[j].getTitle()+"','"+menus_two[j].getTarget()+"')\">"+menus_two[j].getTitle()+"</a>\r\n";
	    			  MenuVo[] menus_three =getAllowedMenus(user,menus_two[j].getName());
	    			  if(menus_three!=null&&menus_three.length>0){
	    				  menuTreeHtml +=" <ul class=\"sub-menu\" style=\"display:;overflow:auto;\">\r\n";
	    				  for(int k=0;k<menus_three.length;k++)
	    				  {
	    					  menuTreeHtml +=" <li><a href=\"javascript:void(0);\" onclick=\"menutree_click('"+menus_three[k].getName()+"','"+menus_three[k].getLocation()+"','"+menus_three[k].getTitle()+"','"+menus_three[k].getTarget()+"')\">"+menus_three[k].getTitle()+"</a></i>\r\n";
	    				  }
	    				  
	    				  menuTreeHtml +="</ul>\r\n";
	    			  
	    			  }else if(menus_three==null||menus_three.length==0){
	    				  
	    				  menuTreeHtml +="<div></div>\r\n";
	    			  }
	    			  
	    		  }
	    		  
	    	  }
	    	  menuTreeHtml +="</ul>\r\n";
	    	  menuTreeHtml +="</li>\r\n";
	      }
		}
		return menuTreeHtml;
	}

	protected MenuVo[] getAllowedMenus(User user,String parent) throws Exception {
		// 超级用户，看见系统中所有菜单
		if (User.SUPER_USER.equals(user.getPersonKind())) {
			return MenuManager.getInstance().getMenus(parent);
		} else {
			return user.getAllowedMenus(parent);
		}
	}	
	
	
	public int doEndTag() throws JspTagException {
		try {
			// 获取页面输出流，并输出字符串
			String appPath = ((HttpServletRequest) pageContext.getRequest())
					.getContextPath();
			String base = "";
			base +="";
			pageContext.getOut().write(getMenuTreeHtml());
		}
		// 捕捉异常
		catch (IOException ex) {
			// 抛出新异常
			// throw new JspTagException("错误"};
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		// 值回返
		return EVAL_PAGE;
	}
	
	
	/**
	 * 判断动态显示的二级菜单数量，返回是否隐藏动态二级菜单
	 * @param vo
	 * @return
	 */
	private boolean dynamicsShow(MenuVo[] vo){
		boolean result = true;
		int dyNum = 0;
		for(MenuVo tmpVo:vo){
			if(tmpVo.getDynamicShow().equals("1")){
				dyNum ++;
				if(dyNum > 1){
					result = false;
				}
			}
		}
		return result;
	}
	
	
}
