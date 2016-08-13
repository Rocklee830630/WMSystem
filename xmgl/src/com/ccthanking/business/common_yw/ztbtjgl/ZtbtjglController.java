package com.ccthanking.business.common_yw.ztbtjgl;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
@Controller
@RequestMapping("/common_yw/ztbtjgl/ZtbtjglController")
public class ZtbtjglController {

	@Autowired
	private ZtbtjglService ZtbtjglService;
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
		domresult = this.ZtbtjglService.query(request,json.getMsg(),user);
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
		resultVO = this.ZtbtjglService.insert(request,json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
}
