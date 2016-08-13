package com.ccthanking.business.bzjkCommon;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.bzjkCommon.service.QqBzjkCommonService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author xiahongbo
 * @date 2014-12-12
 */
@Controller
@RequestMapping("/bzjkjm/QqBzjkCommonController")
public class QqBzjkCommonController {

	@Autowired
	private QqBzjkCommonService service;
	/**
	 * 立项链接
	 */
	@RequestMapping(params = "queryList_lxky")
	@ResponseBody
	public requestJson queryList_lxky(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryList_lxky(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 土地链接
	 */
	@RequestMapping(params = "queryList_tdsx")
	@ResponseBody
	public requestJson queryList_tdsx(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryList_tdsx(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 规划链接
	 */
	@RequestMapping(params = "queryList_ghsx")
	@ResponseBody
	public requestJson queryList_ghsx(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryList_ghsx(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 施工链接
	 */
	@RequestMapping(params = "queryListQqsg")
	@ResponseBody
	public requestJson queryListQqsg(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryListQqsg(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
}
