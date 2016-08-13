package com.ccthanking.framework.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.impl.QuickEntryServiceImpl;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/quickEntryController")
public class QuickEntryController {
	
	@Autowired
	private QuickEntryServiceImpl quickEntryService;
	
	@ResponseBody
	@RequestMapping(params="getQuickEntryTree")
	public void getQuickEntryTree(HttpServletRequest request, HttpServletResponse response) {
		String roleId = request.getParameter("roleId");
		String treeJson = quickEntryService.getQuickEntryTree(roleId);
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().print(treeJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(params = "awardQuickEntryToRole")
	@ResponseBody
	protected requestJson awardQuickEntryToRole(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String roleId = request.getParameter("roleId");
		String val = request.getParameter("val");
				
		quickEntryService.awardQuickEntryToRole(roleId, val, user);
		requestJson j = new requestJson();
		j.setMsg("");
		return j;
	}
}
