package com.ccthanking.business.bdhf.bdwh;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

@Controller
@RequestMapping("/bdhf/bdwhController")
public class BdwhController {
	@Autowired
	private BdwhService bdwhService;
	String domresult="";
	String resultVO="";
	/**
	 * 普通查询
	 */
	@RequestMapping(params = "queryList")
	@ResponseBody
	public requestJson querydemo(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.bdwhService.queryList(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	

	/**
	 * 点击项目列表查询标段
	 */
	@RequestMapping(params = "querydemo_bd")
	@ResponseBody
	public requestJson querydemo_bd(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String xmid=request.getParameter("xmid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.bdwhService.querydemo_bd(json.getMsg(),xmid,user);
		j.setMsg(domresult);
		return j;

	}


	
	/**
     * @param 新增标段
	 */
	@RequestMapping(params = "insertdemo")
	@ResponseBody
	protected requestJson insertdemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String flag =request.getParameter("flag");
		resultVO = this.bdwhService.insertdemo(json.getMsg(),user,flag,request);
		j.setMsg(resultVO);
		return j;
	}
	
	
	/**
     * @param 维护修改
	 */

	@RequestMapping(params = "update_bdbd")
	@ResponseBody
	protected requestJson update_bdbd(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		resultVO = this.bdwhService.update_bdbd(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
     * @param 根据标段ID查询标段信息
	 */

	@RequestMapping(params = "queryBdxxByBdid")
	@ResponseBody
	protected requestJson queryBdxxByBdid(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		resultVO = this.bdwhService.queryBdxxByBdid(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}

}
