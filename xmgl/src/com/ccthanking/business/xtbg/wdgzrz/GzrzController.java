package com.ccthanking.business.xtbg.wdgzrz;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xtbg.wdgzrz.service.impl.GzrzServiceImpl;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/gzrzController")
public class GzrzController {

	@Autowired
	private GzrzServiceImpl gzrzService;
	
	/**
	 * 查看工作日志
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryGzrz")
	@ResponseBody
	public requestJson queryGzrz(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = gzrzService.queryGzrz(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 查看工作日志
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryWorkGzrz")
	@ResponseBody
	public requestJson queryWorkGzrz(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = gzrzService.queryWorkGzrz(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 查看我的历史工作日志
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryMyHistoryRz")
	@ResponseBody
	public requestJson queryMyHistoryRz(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String type = request.getParameter("type");
			String domResult = gzrzService.queryMyHistoryRz(json.getMsg(), user, type);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 修改公告信息：包括添加、修改、删除，分别用1、2、3表示
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "executeGzrz")
	@ResponseBody
	protected requestJson executeGzrz(HttpServletRequest request,requestJson json) {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String operatorSign = request.getParameter("operatorSign");
		String ywid = request.getParameter("ywid");
		try {
			String domResult = this.gzrzService.executeGzrz(json.getMsg(), user, operatorSign, ywid);
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
		String date = request.getParameter("date");
		String domResult = this.gzrzService.queryUnique(date, user);
		return domResult;
	}
}
