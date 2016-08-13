package com.ccthanking.business.xtbg.gwgl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xtbg.gwgl.service.impl.FwglServiceImpl;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/fwglController")
public class FwglController {

	@Autowired
	private FwglServiceImpl fwglService;
	
	/**
	 * 发收文信息查询
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryFw")
	@ResponseBody
	public requestJson queryFw(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = fwglService.queryFw(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	/**
	 * 修改发文信息：包括添加、修改、删除
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "executeFw")
	@ResponseBody
	public requestJson executeFw(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String operatorSign = request.getParameter("operatorSign");
		String ywid = request.getParameter("ywid");
		try {
			String domResult = fwglService.executeFw(json.getMsg(), user, operatorSign, ywid);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	
	/**
	 * 查询历史
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "queryLs")
	@ResponseBody
	public requestJson queryLs(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = fwglService.queryLs(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 查询历史，根据当前用户
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "queryLsByPerson")
	@ResponseBody
	public requestJson queryLsByPerson(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = fwglService.queryLsByPerson(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/**
	 * 删除
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "deleteFw")
	@ResponseBody
	protected String deleteFw(HttpServletRequest request,requestJson json) throws Exception {
		return this.fwglService.deleteFw(request.getParameter("fwid"));
	}
	
}
