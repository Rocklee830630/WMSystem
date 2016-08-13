package com.ccthanking.business.zsb.zsgl.xmb;

/**
 * @author liujs
 */

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.business.zsb.zsgl.xmb.GcZsbXmbService;
import com.ccthanking.framework.service.UserService;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.framework.model.*;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/zsb/xmb/xmbController")
public class GcZsbXmbController {

	
	@Autowired
	private GcZsbXmbService xmbService;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXmb")
	@ResponseBody
	public requestJson queryConditionXmb(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xmbService.queryConditionXmb(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "insertXmb")
	@ResponseBody
	protected requestJson insertXmb(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		//String qy=request.getParameter("qy");
		String  resultVO = "";
		resultVO = this.xmbService.insertXmb(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateXmb")
	@ResponseBody
	protected requestJson updateXmb(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmbService.updateXmb(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryJdxx")
	@ResponseBody
	public requestJson queryJdxx(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xmbService.queryJdxx(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "query")
	@ResponseBody
	public requestJson queryXmb(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xmbService.queryXmb(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 新增进展反馈信息
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doJhsjfk")
	@ResponseBody
	public requestJson doJhsjfk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmbService.doJhsjfk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取计划反馈状态
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getZt")
	@ResponseBody
	public requestJson getZt(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmbService.getZt(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}

