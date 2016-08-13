package com.ccthanking.business.xmcbk.xmwh;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;
@Controller
@RequestMapping("/xmcbk/xmcbkwhController")
public class XmcbkwhController {

	@Autowired
	private XmcbkwhService xmcbkwhService;
	String domresult="";
	String resultVO="";
	/**
	 * 普通查询
	 */
	@RequestMapping(params = "query_cbk")
	@ResponseBody
	public requestJson query_cbk(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.xmcbkwhService.query_cbk(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 项目编号
	 */
	@RequestMapping(params = "query_xmbh")
	@ResponseBody
	public requestJson query_xmbh(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String year =request.getParameter("year");
		domresult = this.xmcbkwhService.query_xmbh(json.getMsg(),user,year);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 校验项目编号
	 */
	@RequestMapping(params = "query_xmbh_jy")
	@ResponseBody
	public requestJson query_xmbh_jy(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String year =request.getParameter("year");
		String xmbh =request.getParameter("xmbh");
		String id=request.getParameter("id");
		domresult = this.xmcbkwhService.query_xmbh_jy(json.getMsg(),user,year,xmbh,id);
		j.setMsg(domresult);
		return j;

	}
	

	/**
	 * 年度查询
	 */
	@RequestMapping(params = "query_xdrq")
	@ResponseBody
	public requestJson query_xdrq(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String pcid=request.getParameter("pcid");
		domresult = this.xmcbkwhService.query_xdrq(json.getMsg(),user,pcid);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 批次查询
	 */
	@RequestMapping(params = "query_xmpc")
	@ResponseBody
	public requestJson query_xmpc(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.xmcbkwhService.query_xmpc(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 查询最新pch
	 */
	@RequestMapping(params = "query_newpch")
	@ResponseBody
	public requestJson query_newpch(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.xmcbkwhService.query_newpch(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 详细信息查询
	 */
	@RequestMapping(params = "query_cbkxx")
	@ResponseBody
	public requestJson query_cbkxx(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String id=request.getParameter("id");
		domresult = this.xmcbkwhService.query_cbkxx(json.getMsg(),user,id);
		j.setMsg(domresult);
		return j;

	}

	
	/**
	 * 通用详细信息查询
	 */
	@RequestMapping(params = "query_cbkxx_ty")
	@ResponseBody
	public requestJson query_cbkxx_ty(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.xmcbkwhService.query_cbkxx_ty(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	
	
	/**
     * @param 维护新增
	 */
	@RequestMapping(params = "insert_cbk")
	@ResponseBody
	protected requestJson insert_cbk(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		resultVO = this.xmcbkwhService.insert_cbk(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
     * @param 维护修改
	 */

	@RequestMapping(params = "update_ckb")
	@ResponseBody
	protected requestJson update_ckb(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String id=request.getParameter("id");
		resultVO = this.xmcbkwhService.update_ckb(json.getMsg(),user,id);
		j.setMsg(resultVO);
		return j;
	}
	
	
	/**
     * @param 排序修改
	 */

	@RequestMapping(params = "update_pxh")
	@ResponseBody
	protected requestJson update_pxh(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		resultVO = this.xmcbkwhService.update_pxh(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	
	//删除检查信息
	@RequestMapping(params = "delete")
	@ResponseBody
	protected requestJson delete(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmcbkwhService.delete(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	
	//项目名称联想查询
	@RequestMapping(params = "xmmcAutoQuery")
	@ResponseBody
	public List<autocomplete> xmmcAutoComplete(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.xmcbkwhService.xmmcAutoComplete(match,user);
		return list;
	}
	
	/**
	 * 通用详细信息查询
	 * SJL 2013-12-19
	 */
	@RequestMapping(params = "queryCbkGz")
	@ResponseBody
	public requestJson queryCbkGz(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.xmcbkwhService.queryCbkGz(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
}
