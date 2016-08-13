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
import com.ccthanking.framework.service.impl.PushInfoServiceImpl;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/pushInfoController")
public class PushInfoController {
	
	@Autowired
	private PushInfoServiceImpl pushInfoService;
	
	@ResponseBody
	@RequestMapping(params="getPushInfoMenu")
	public void getPushInfoMenu(HttpServletRequest request, HttpServletResponse response) {
		String pushInfoTreeJson = pushInfoService.getPushInfoMenu();
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().print(pushInfoTreeJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@RequestMapping(params = "savePushInfoMap")
	protected requestJson savePushInfoMap(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String id = request.getParameter("id");
		String checkValue = request.getParameter("checkValue");
		pushInfoService.savePushInfoMap(id, checkValue, user);
		requestJson j = new requestJson();
		j.setMsg("");
		return j;
	}
}
