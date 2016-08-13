package com.ccthanking.business.common_yw.kgtjgl;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
@Controller
@RequestMapping("/common_yw/kgtjgl/kgtjglController")
public class KgtjglController {

	@Autowired
	private KgtjglService KgtjglService;
	String domresult="";
	String resultVO="";
	/**
	 * 普通查询
	 */
	@RequestMapping(params = "query")
	@ResponseBody
	public requestJson query(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.KgtjglService.query(request,json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	/**
     * @param 
	 */
	@RequestMapping(params = "insert")
	@ResponseBody
	protected requestJson insert(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		resultVO = this.KgtjglService.insert(request,json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	/**
     * @param 
	 */
	@RequestMapping(params = "insert_tb")
	@ResponseBody
	protected requestJson insert_tb(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		resultVO = this.KgtjglService.insert_tb(request,json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
}
