package com.ccthanking.business.wdgl;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.wdgl.service.ProjectService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/projectController")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	
	@ResponseBody
	@RequestMapping(params="getChildProject")
	public requestJson getChildProject(HttpServletRequest request,requestJson json) throws Exception{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String parent = request.getParameter("parent") == null ? "" : request.getParameter("parent");
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = projectService.getChildProject(json.getMsg(),parent);
		j.setMsg(domresult);
		return j;
		
	}
	
	@ResponseBody
	@RequestMapping(params="getAllProject")
	public void getAllProject(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String prjName = request.getParameter("prjName") == null ? "" : request.getParameter("prjName");
		String menuJson = projectService.getAllProject(prjName);
		try {
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@ResponseBody
	@RequestMapping(params="getDocumentDir")
	public void getDocumentDir(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String prjName = request.getParameter("prjName") == null ? "" : request.getParameter("prjName");
		String prjId = request.getParameter("prjId") == null ? "" : request.getParameter("prjId");
		String menuJson = projectService.getDocumentDir(prjName,prjId);
		try {
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//获得工程项目分类树形
	@ResponseBody
	@RequestMapping(params="getProjectfl")
	public void getProjectfl(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		String prjName = request.getParameter("prjName") == null ? "" : request.getParameter("prjName");
		String nd = request.getParameter("nd") == null ? "" : request.getParameter("nd");
		String menuJson = projectService.getProjectfl(nd);
		try {
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//新增工程项目分类
	@ResponseBody
	@RequestMapping(params="addFl")
	public void addFl(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		response.setCharacterEncoding("UTF-8");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String parent = request.getParameter("parent") == null ? "" : request.getParameter("parent");
		String flmc = request.getParameter("flmc") == null ? "" : request.getParameter("flmc");
		String operatorSign = request.getParameter("operatorSign") == null ? "" : request.getParameter("operatorSign");
		String nd  =request.getParameter("nd") == null ? "" : request.getParameter("nd");
		

		String menuJson = projectService.addFl(parent,flmc,operatorSign,nd,user);
		try {
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//新增子项目
		@ResponseBody
		@RequestMapping(params="addZxm")
		public void addZxm(HttpServletRequest request, HttpServletResponse response) throws SQLException {
			response.setCharacterEncoding("UTF-8");
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String parent = request.getParameter("parent") == null ? "" : request.getParameter("parent");
			String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
			String nd = request.getParameter("nd") == null ? "" : request.getParameter("nd");

			String menuJson = projectService.addZxm(parent,ids,nd,user);
			try {
				response.getWriter().print(menuJson);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
}
