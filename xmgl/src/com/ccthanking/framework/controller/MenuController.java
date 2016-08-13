package com.ccthanking.framework.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.log;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.model.responseJson;
import com.ccthanking.framework.service.MenuService;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/menuController")
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	@RequestMapping(params = "menutree")
	@ResponseBody
	public responseJson menutree(final HttpServletRequest request,requestJson js) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		responseJson j = new responseJson();
		
		String menuTreeHtml ="";// menuService.getMenuTreeHtml(user);
		
		j.setMsg(menuTreeHtml);
		j.setSuccess(true);
		return j;
	}

	@ResponseBody
	@RequestMapping(params="getAllMenu")
	public void getAllMenu(HttpServletRequest request, HttpServletResponse response) {
		String menuJson = menuService.getAllMenu();
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@RequestMapping(params = "executeMenu")
	@ResponseBody
	protected requestJson executeMenu(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String operatorSign = request.getParameter("operatorSign");
		try {
			String domResult = menuService.executeMenu(json.getMsg(), user, operatorSign);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "queryUnique")
	@ResponseBody
	protected String queryUnique(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String menuName = request.getParameter("menuName");
		String domResult = menuService.queryUnique(menuName, user);
		return domResult;
	}
	
	@ResponseBody
	@RequestMapping(params="loadAllMenu")
	public void loadAllMenu(HttpServletRequest request, HttpServletResponse response) {
		String roleId = request.getParameter("roleId");
		String menuJson = menuService.loadAllMenu(roleId);
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@RequestMapping(params = "awardMenuToRole")
	@ResponseBody
	protected requestJson awardMenuToRole(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String roleId = request.getParameter("roleId");
		String val = request.getParameter("val");
				
		String[] menuName = "".equals(val) ? new String[0] : val.split(",");
		menuService.awardMenuToRole(roleId, menuName, user);
		requestJson j = new requestJson();
		j.setMsg("");
		return j;
	}
	
	public void refreshMenuCache() {
		Logger lg = log.getLogger(MenuController.class);
		lg.info("重新载入菜单信息...");
		try {
			com.ccthanking.framework.coreapp.orgmanage.MenuManager.getInstance().reBuildMemory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
