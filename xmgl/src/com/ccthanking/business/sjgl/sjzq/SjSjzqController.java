package com.ccthanking.business.sjgl.sjzq;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.sjgl.sjzq.service.SjSjzqService;
import com.ccthanking.framework.model.requestJson;

/**
 * 排迁数据钻取类
 * @author liujs@ccthanking.com
 *
 */
@Controller
@RequestMapping("/sjSjzqController")
public class SjSjzqController {
	@Autowired
	private SjSjzqService service;
	/**
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getValue")
	@ResponseBody
	public requestJson getValue(HttpServletRequest request,requestJson js) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.service.getValue(request,js.getMsg());
		j.setMsg(domresult);
		return j;

	}
	
	
	
	@RequestMapping(params = "getTable")
	@ResponseBody
	public requestJson getTable(HttpServletRequest request) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.service.getTable(request);
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "jcjcValue")
	@ResponseBody
	public requestJson jcjcValue(HttpServletRequest request,requestJson js) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.service.jcjcValue(request,js.getMsg());
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "sjbgValue")
	@ResponseBody
	public requestJson sjbgValue(HttpServletRequest request,requestJson js) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.service.sjbgValue(request,js.getMsg());
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 查询设计综合信息页面
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDrillingDataZh")
	@ResponseBody
	public requestJson queryDrillingDataZh(HttpServletRequest request,requestJson js) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.service.queryDrillingDataZh(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 查询拦标价页面
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDrillingDataLbj")
	@ResponseBody
	public requestJson queryDrillingDataLbj(HttpServletRequest request,requestJson js) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.service.queryDrillingDataLbj(request,js.getMsg());
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 查询交竣工页面
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDrillingDataJjg")
	@ResponseBody
	public requestJson queryDrillingDataJjg(HttpServletRequest request,requestJson js) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.service.queryDrillingDataJjg(request,js.getMsg());
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 查询监测检测页面
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDrillingDataJcjc")
	@ResponseBody
	public requestJson queryDrillingDataJcjc(HttpServletRequest request,requestJson js) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.service.queryDrillingDataJcjc(request,js.getMsg());
		j.setMsg(domresult);
		return j;

	}
	
}
