package com.ccthanking.business.zlaq.zlaqgk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.gcgl.gcbgk.GcbgkService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;


@Controller
@RequestMapping("/zlaqgk/zlaqgkController")
public class ZlaqgkController {

	
	@Autowired
	private ZlaqgkService ZlaqgkService;
	
	//数据钻取
	@RequestMapping(params = "zlaq_gk")
	@ResponseBody
	public requestJson zlaq_gk(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		//String xmid =request.getParameter("xmid");
		String sqlname =request.getParameter("sqlname");
		String nd = request.getParameter("nd");
		String xmglgs = request.getParameter("xmglgs");
		domresult = this.ZlaqgkService.zlaq_gk(json.getMsg(),user,nd,sqlname,xmglgs);
		j.setMsg(domresult);
		return j;

	}

}
