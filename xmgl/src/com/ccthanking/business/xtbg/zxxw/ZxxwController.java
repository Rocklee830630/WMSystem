package com.ccthanking.business.xtbg.zxxw;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xtbg.zxxw.service.ZxxwService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/zxxwController")
public class ZxxwController {

	@Autowired
	private ZxxwService zxxwService;

	/**
	 * 查询新闻信息
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryXw")
	@ResponseBody
	public requestJson queryXw(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = zxxwService.queryXw(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/**
	 * 查询首页更多新闻信息
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryMoreXw")
	@ResponseBody
	public requestJson queryMoreXw(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = zxxwService.queryMoreXw(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}


	/**
	 * 修改新闻信息：包括添加、修改、删除
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "executeXw")
	@ResponseBody
	public requestJson executeXw(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String operatorSign = request.getParameter("operatorSign");
		String ywid = request.getParameter("ywid");
		try {
			String domResult = zxxwService.executeXw(json.getMsg(), user, operatorSign, ywid);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
}
