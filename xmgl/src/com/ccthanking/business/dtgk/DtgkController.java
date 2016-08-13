package com.ccthanking.business.dtgk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

@Controller
@RequestMapping("/dtgkController")
public class DtgkController {
	@Autowired
	private DtgkService service;
	
	@RequestMapping(params = "query")
	@ResponseBody
	public requestJson query(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = service.query(json.getMsg(), user, request);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "ztgkRs")
	@ResponseBody
	public requestJson ztgkRs(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = service.ztgkRs(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "ztjzRs")
	@ResponseBody
	public requestJson ztjzRs(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = service.ztjzRs(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "xjxujRs")
	@ResponseBody
	public requestJson xjxujRs(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = service.xjxujRs(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	
	@RequestMapping(params = "queryXx")
	@ResponseBody
	public requestJson queryXx(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String bmjkLx = request.getParameter("bmjkLx");
		String ywlx = request.getParameter("ywlx");
		String nd = request.getParameter("nd");
		String tableName = request.getParameter("tableName");
		String xmglgs = request.getParameter("xmglgs");
		try {
			String domResult = service.queryXx(json.getMsg());
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "queryXmxxBdxx")
	@ResponseBody
	public requestJson queryXmxxBdxx(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String sql = request.getParameter("sql");
		try {
			String domResult = service.queryXmbdxx(json.getMsg(),sql);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
}
