package com.ccthanking.business.gcgl.gcbgk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;



@Controller
@RequestMapping("/gcbgk/gcbgkController")
public class GcbgkController {
	
	@Autowired
	private GcbgkService GcbgkService;
	
	//年度洽商数
	@RequestMapping(params = "gcqs_zq_nd")
	@ResponseBody
	public requestJson gcqs_zq_nd(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		//String xmid =request.getParameter("xmid");
		String xmglgs =request.getParameter("xmglgs");
		String nd = request.getParameter("nd");
		domresult = this.GcbgkService.gcqs_zq_nd(json.getMsg(),user,nd,xmglgs);
		j.setMsg(domresult);
		return j;

	}

	
	//年度洽商数
	@RequestMapping(params = "gcqs_zq_by")
	@ResponseBody
	public requestJson gcqs_zq_by(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		//String xmid =request.getParameter("xmid");
		//String bdid =request.getParameter("bdid");
		String xmglgs =request.getParameter("xmglgs");
		String nd = request.getParameter("nd");
		domresult = this.GcbgkService.gcqs_zq_by(json.getMsg(),user,nd,xmglgs);
		j.setMsg(domresult);
		return j;

	}
	
}
