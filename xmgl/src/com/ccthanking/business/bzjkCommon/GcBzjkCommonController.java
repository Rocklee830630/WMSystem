package com.ccthanking.business.bzjkCommon;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.bzjkCommon.service.GBzjkCommonService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author zhaiyl
 * @date 2014-12-15
 */
@Controller
@RequestMapping("/bzjk/GcBzjkCommonController")
public class GcBzjkCommonController {

	@Autowired
	private GBzjkCommonService service;
	/**
	 * 开复工链接
	 */
	@RequestMapping(params = "queryList_Kfg")
	@ResponseBody
	public requestJson queryList_Kfg(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryList_Kfg(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * querytcjhgtt
	 */
	@RequestMapping(params = "querytcjhgtt")
	@ResponseBody
	public requestJson querytcjhgtt(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.querytcjhgtt(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 履约保证金管理列表
	 */
	@RequestMapping(params = "queryLybzjList")
	@ResponseBody
	public requestJson queryLybzjList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryLybzjList(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
}
