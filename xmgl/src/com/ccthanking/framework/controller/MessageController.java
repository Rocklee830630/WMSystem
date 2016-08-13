package com.ccthanking.framework.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.model.responseJson;
import com.ccthanking.framework.service.MenuService;
import com.ccthanking.framework.service.MessageService;
import com.ccthanking.framework.common.User;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/messageController")
public class MessageController {
	
	@Autowired
	private MessageService messageService;
//	@Autowired
//	private MenuService menuService;
	
	@RequestMapping(params = "getMessage")
	@ResponseBody
	public responseJson getMessage(final HttpServletRequest request) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		responseJson j = new responseJson();
		String message = messageService.getMessage(user);
		j.setMsg(message);
		j.setSuccess(true);
		return j;
	}

	@RequestMapping(params = "getUserMessage")
	@ResponseBody
	public requestJson getUserMessage(HttpServletRequest request,requestJson json) throws Exception
	{

		
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.messageService.getUserMessage(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "changeZt")
	@ResponseBody
	public requestJson changeZt(HttpServletRequest request,requestJson json) throws Exception
	{

		
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.messageService.changeZt(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;

	}


}
