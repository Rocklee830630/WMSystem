/**
 * @author
 */
package com.ccthanking.business.xmglgs.xxjd.tbjs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @author 
 */
@Controller
@RequestMapping("/tiBaoJieSuanController")
public class TiBaoJieSuanController {
	@Autowired
	private TiBaoJieSuanService tiBaoJieSuanService;
	@RequestMapping(params = "queryTiBaoJieSuan")
	@ResponseBody
	public requestJson queryTiBaoJieSuan(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.tiBaoJieSuanService.queryTiBaoJieSuan(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "insertTBJSZT")
	@ResponseBody
	protected requestJson insertTBJSZT(HttpServletRequest request,requestJson json) throws Exception {
		String ywid=request.getParameter("ywid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.tiBaoJieSuanService.insertTBJSZT(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
	}
	
}
