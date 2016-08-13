package com.ccthanking.business.sjgl.gs;

/**
 * @author 
 */

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/sjgl/gs/gsController")
public class GcSjCbsjpfController {

	
	@Autowired
	private GcSjCbsjpfService Service;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryGs")
	@ResponseBody
	public requestJson queryGs(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.Service.queryGs(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "insertGs")
	@ResponseBody
	protected requestJson insertGs(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		String ywid=request.getParameter("ywid");
		resultVO = this.Service.insertGs(request,json.getMsg(),ywid);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateGs")
	@ResponseBody
	protected requestJson updateGs(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.Service.updateGs(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "getSx")
	@ResponseBody
	public requestJson getSx(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.Service.getSx(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "getBblsx")
	@ResponseBody
	public requestJson getBblsx(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.Service.getBblsx(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "getQqsx")
	@ResponseBody
	public requestJson getQqsx(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.Service.getQqsx(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
}

