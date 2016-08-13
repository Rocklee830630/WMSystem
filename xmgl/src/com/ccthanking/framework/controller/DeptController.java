package com.ccthanking.framework.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.DeptService;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/deptController")
public class DeptController {

	@Autowired
	DeptService deptService;
	
	/**
	 * 查询部门信息
	 */
	@RequestMapping(params = "queryDept")
	@ResponseBody
	public requestJson queryDept(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = deptService.queryDept(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return j;
	}

	@RequestMapping(params = "executeDept")
	@ResponseBody
	protected requestJson executeDept(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String id = request.getParameter("id");
		String operatorSign = request.getParameter("operatorSign");
		try {
			String domResult = this.deptService.executeDept(json.getMsg(), user, operatorSign);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	@ResponseBody
	@RequestMapping(params="loadAllDept")
	public void loadAllDept(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String roleId = request.getParameter("str");
		String dept = request.getParameter("dept");
		response.setCharacterEncoding(request.getCharacterEncoding());
		String menuJson = "";
		try {
			menuJson = this.deptService.loadAllDept(roleId,dept);
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(params = "leaderSave")
	@ResponseBody
	protected requestJson leaderSave(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = this.deptService.leaderSave(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
}
