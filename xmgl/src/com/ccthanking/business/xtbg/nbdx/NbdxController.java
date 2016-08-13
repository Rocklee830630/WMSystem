package com.ccthanking.business.xtbg.nbdx;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xtbg.nbdx.service.NbdxService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/nbdxController")
public class NbdxController {
	
	@Autowired
	private NbdxService nbdxService;

	/**
	 * 查询内部短信
	 * @param request
	 * @param json 页面传过来的json
	 * @return
	 */
	@RequestMapping(params = "queryNbdx")
	@ResponseBody
	public requestJson queryNbdx(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = nbdxService.queryNbdx(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	

	/**
	 * 修改内部短信方法，包括添加（1）、修改（2）、删除（3）
	 * @param request
	 * @param json 页面传过来的json
	 * @return
	 */
	@RequestMapping(params = "executeNbdx")
	@ResponseBody
	public requestJson executeNbdx(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String operatorSign = request.getParameter("operatorSign");
		try {
			String domResult = nbdxService.executeNbdx(request,json.getMsg(), user, operatorSign);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
}
