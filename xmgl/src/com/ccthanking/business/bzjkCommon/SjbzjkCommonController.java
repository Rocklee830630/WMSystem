package com.ccthanking.business.bzjkCommon;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.bzjkCommon.service.SjbzjkCommonService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author xiahongbo
 * @date 2014-12-16
 */
@Controller
@RequestMapping("/bzjkjm/SjbzjkCommonController")
public class SjbzjkCommonController {

	@Autowired
	private SjbzjkCommonService service;
	/**
	 * 设计部监控-设计管理的详细页面统计
	 */
	@RequestMapping(params = "querySjglList")
	@ResponseBody
	public requestJson querySjglList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.querySjglList(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}

	/**
	 * 设计部监控-设计管理的详细页面统计
	 */
	@RequestMapping(params = "querySjJJGglList")
	@ResponseBody
	public requestJson querySjJJGglList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.querySjJJGglList(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
}
