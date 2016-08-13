package com.ccthanking.business.zjb.sjgl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.*;

/**
 * @author zhaiyl
 */
@Controller
@RequestMapping("/shenJiGuanliController")
public class ShenJiGuanLiController {
	@Autowired
	private ShenJiGuanLiService shenJiGuanLiService;
	
	@RequestMapping(params = "querysjList")
	@ResponseBody
	public requestJson querysjList(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.shenJiGuanLiService.querysjList(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "queryJS")
	@ResponseBody
	public requestJson queryJS(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.shenJiGuanLiService.queryJS(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "querysjxx")
	@ResponseBody
	public requestJson querysjxx(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.shenJiGuanLiService.querysjxx(request,user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "insert_jz")
	@ResponseBody
	protected requestJson insert_jz(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
	
		resultVO = this.shenJiGuanLiService.insert_sj(request,json.getMsg() );
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "update_js")
	@ResponseBody
	protected requestJson update_js(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
	
		resultVO = this.shenJiGuanLiService.update_js(request,json.getMsg() );
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "update_jss")
	@ResponseBody
	protected requestJson update_jss(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
	
		resultVO = this.shenJiGuanLiService.update_jss(request,json.getMsg() );
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateJSById")
	@ResponseBody
	protected requestJson updateJSById(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
	
		resultVO = this.shenJiGuanLiService.updateJSById(request );
		j.setMsg(resultVO);
		return j;
	}

}
