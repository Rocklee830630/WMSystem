package com.ccthanking.business.xtbg.sjdx;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xtbg.sjdx.service.SjdxService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/sjdxController")
public class SjdxController {

	@Autowired
	private SjdxService sjdxService;
	
	/**
	 * 查询手机短信，查询所有，已接收，已发送短信。分别用空，receive，sended表示
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "querySms")
	@ResponseBody
	public requestJson querySms(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String mark = request.getParameter("mark") == null ? "" : request.getParameter("mark");
		try {
			String domResult = sjdxService.querySms(json.getMsg(), user, mark);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	

	/**
	 * 操作手机短信
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "executeSms")
	@ResponseBody
	protected requestJson executeSms(HttpServletRequest request,requestJson json) {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String id = request.getParameter("id");
		try {
			String domResult = this.sjdxService.executeSms(json.getMsg(), user, id);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
}
