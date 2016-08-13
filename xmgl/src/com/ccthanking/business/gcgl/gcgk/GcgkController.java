package com.ccthanking.business.gcgl.gcgk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.gcgl.gcgk.service.GcgkService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2014-1-14
 */
@Controller
@RequestMapping("/gcgk/gcgkController")
public class GcgkController {
	
	@Autowired
	private GcgkService gcgkService;

	//工程部——工程进展——工程保函——工程洽商
	@RequestMapping(params = "gcb_ztgk")
	@ResponseBody
	public requestJson gcb_ztgk(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.gcgkService.gcb_ztgk(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}

	/**
	 * 工程部-进度管理-列表
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params="gcJdglLb")
	@ResponseBody
	public requestJson gcJdglLb(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = gcgkService.gcJdglLb(json.getMsg(), request);
		j.setMsg(domString);
		return j;
	}

	/**
	 * 工程部-工程计量及支付-统筹概况
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params="gcGcjlzfTcgk")
	@ResponseBody
	public requestJson gcGcjlzfTcgk(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = gcgkService.gcGcjlzfTcgk(json.getMsg(), request);
		j.setMsg(domString);
		return j;
	}
	
	/**
	 * 工程部-工程计量及支付-总体情况
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params="gcGcjlzfZtqk")
	@ResponseBody
	public requestJson gcGcjlzfZtqk(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = gcgkService.gcGcjlzfZtqk(json.getMsg(), request);
		j.setMsg(domString);
		return j;
	}
	
	/**
	 * 工程部-工程管理-统筹概况
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params="gcGcglTcgk")
	@ResponseBody
	public requestJson gcGcglTcgk(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = gcgkService.gcGcglTcgk(json.getMsg(), request);
		j.setMsg(domString);
		return j;
	}
	
	/**
	 * 工程部-履约保证金-统筹概况
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params="gcLybzjTcgk")
	@ResponseBody
	public requestJson gcLybzjTcgk(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = gcgkService.gcLybzjTcgk(json.getMsg(), request);
		j.setMsg(domString);
		return j;
	}
	
	/**
	 * 工程部-工程甩项-统筹概况
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params="gcGcsxTcgk")
	@ResponseBody
	public requestJson gcGcsxTcgk(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = gcgkService.gcGcsxTcgk(json.getMsg(), request);
		j.setMsg(domString);
		return j;
	}
	
	/**
	 * 工程部-工程洽商-图形分析
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params="gcGcqsTxfx")
	@ResponseBody
	public requestJson gcGcqsTxfx(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = gcgkService.gcGcqsTxfx(json.getMsg(), request);
		j.setMsg(domString);
		return j;
	}
	
	/**
	 * 工程部-工程洽商-统筹概况
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "gcGcqsTcgk")
	@ResponseBody
	public requestJson gcGcqsTcgk(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = gcgkService.gcGcqsTcgk(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "queryJdgl1")
	@ResponseBody
	public requestJson queryJdgl1(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = gcgkService.queryJdgl1(request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "queryJdgl2")
	@ResponseBody
	public requestJson queryJdgl2(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = gcgkService.queryJdgl2(request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 工程部-进度管理-统筹概况
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "gcJdglTcgk")
	@ResponseBody
	public requestJson gcJdglTcgk(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = gcgkService.gcJdglTcgk(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 履约保证金列表
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "gcLybzjLb")
	@ResponseBody
	public requestJson gcLybzjLb(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = gcgkService.gcLybzjLb(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
}
