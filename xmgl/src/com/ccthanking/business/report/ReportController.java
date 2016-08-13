package com.ccthanking.business.report;

/**
 * @author liujs
 */

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.model.requestJson;

/**
 */
@Controller
@RequestMapping("/ReportController")
public class ReportController {

	
	@Autowired
	private ReportService ReportService;
	/**
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryReport")
	@ResponseBody
	public requestJson queryReport(HttpServletRequest request,requestJson json) throws Exception
	{
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.ReportService.queryReport(request);
		j.setMsg(domresult);
		return j;

	}
	//监控
	@RequestMapping(params = "monitor")
	@ResponseBody
	public requestJson monitor(HttpServletRequest request,requestJson json) throws Exception
	{
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.ReportService.monitor(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}
	
	//监控
	@RequestMapping(params = "bm_monitor")
	@ResponseBody
	public requestJson bm_monitor(HttpServletRequest request,requestJson json) throws Exception
	{
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.ReportService.bm_monitor(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}
	
}

