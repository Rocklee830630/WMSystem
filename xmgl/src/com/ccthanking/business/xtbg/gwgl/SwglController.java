package com.ccthanking.business.xtbg.gwgl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xtbg.gwgl.service.SwglService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/swglController")
public class SwglController {

	@Autowired
	private SwglService swglService;
	
	/**
	 * 收文信息查询
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "querySw")
	@ResponseBody
	public requestJson querySw(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = swglService.querySw(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 修改公告信息：包括添加、修改、删除
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "executeSw")
	@ResponseBody
	public requestJson executeSw(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String operatorSign = request.getParameter("operatorSign");
		String ywid = request.getParameter("ywid");
		try {
			String domResult = swglService.executeSw(json.getMsg(), user, operatorSign, ywid);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "queryLs")
	@ResponseBody
	public requestJson queryLs(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = swglService.queryLs(json.getMsg(), user);
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
		String domResult = this.swglService.queryUnique(request, user);
		return domResult;
	}
	
	@RequestMapping(params = "querySwbh")
	@ResponseBody
	protected String querySwbh(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String swlb = request.getParameter("swlb");
		String domResult = this.swglService.querySwbh(swlb, user);
		return domResult;
	}
	
	@RequestMapping(params = "deleteSw")
	@ResponseBody
	protected String deleteSw(HttpServletRequest request,requestJson json) throws Exception {
		return this.swglService.deleteSw(request.getParameter("swid"));
	}
}
