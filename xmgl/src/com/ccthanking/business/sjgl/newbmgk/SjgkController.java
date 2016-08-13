package com.ccthanking.business.sjgl.newbmgk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.sjgl.newbmgk.service.SjgkService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author xiahongbo
 * @date 2014-9-20
 */
@Controller
@RequestMapping("/sjgk/sjgkController")
public class SjgkController {

	@Autowired
	private SjgkService service;
	//图形分析
	@RequestMapping(params="queryTxfx")
	@ResponseBody
	public requestJson queryTxfx(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = service.queryTxfx(request, json.getMsg());
		j.setMsg(domString);
		return j;
	}
	//桥梁类
	@RequestMapping(params="queryQll")
	@ResponseBody
	public requestJson queryQll(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = service.queryQll(request, json.getMsg());
		j.setMsg(domString);
		return j;
	}
	//道路类
	@RequestMapping(params="queryDll")
	@ResponseBody
	public requestJson queryDll(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = service.queryDll(request, json.getMsg());
		j.setMsg(domString);
		return j;
	}
	
	/**
	 * 设计部门-设计管理-统筹概况
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params="querySjglTcgk")
	@ResponseBody
	public requestJson querySjglTcgk(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = service.querySjglTcgk(request, json.getMsg());
		j.setMsg(domString);
		return j;
	}
	
	/**
	 * 设计部门-交（竣）工验收管理-统筹概况
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params="queryJgjvgTcgk")
	@ResponseBody
	public requestJson queryJgjvgTcgk(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = service.queryJgjvgTcgk(request, json.getMsg());
		j.setMsg(domString);
		return j;
	}
}
