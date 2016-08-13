package com.ccthanking.framework.controller;

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
import com.ccthanking.framework.service.RoleService;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/roleController")
public class RoleController {
	
	@Autowired
	RoleService roleService;
	
	/**
	 * 查询角色信息
	 */
	@RequestMapping(params = "queryRole")
	@ResponseBody
	public requestJson queryRole(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = roleService.queryRole(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return j;
	}

	@RequestMapping(params = "executeRole")
	@ResponseBody
	protected requestJson executeRole(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String id = request.getParameter("id");
		String operatorSign = request.getParameter("operatorSign");
		try {
			String domResult = roleService.executeRole(json.getMsg(), user, operatorSign);
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
		String roleName = request.getParameter("roleName");
		String domResult = roleService.queryUnique(roleName, user);
		return domResult;
	}
	/**
	 * 查询角色树
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "queryRoleTree")
	@ResponseBody
	public void queryRoleTree(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String encode = request.getCharacterEncoding();
		String  resultVO = "";
		resultVO = this.roleService.queryRoleTree(request,j.getMsg());
		response.setCharacterEncoding(encode);
		response.getWriter().print(resultVO);
	}

	/**
	 * 查询角色树
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "queryRoleByTreeNode")
	@ResponseBody
	public requestJson queryRoleByTreeNode(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.roleService.queryRoleByTreeNode(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 角色移入树形节点
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doMoveInTreeNode")
	@ResponseBody
	public requestJson doMoveInTreeNode(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.roleService.doMoveInTreeNode(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 角色移入树形节点
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doMoveOutTreeNode")
	@ResponseBody
	public requestJson doMoveOutTreeNode(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.roleService.doMoveOutTreeNode(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取已存在的角色ID
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getExistsNodeID")
	@ResponseBody
	public requestJson getExistsNodeID(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.roleService.getExistsNodeID(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
}
