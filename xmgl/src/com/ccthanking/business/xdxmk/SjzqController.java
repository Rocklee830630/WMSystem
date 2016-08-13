package com.ccthanking.business.xdxmk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xdxmk.service.SjzqService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2013-12-25
 */
@Controller
@RequestMapping("/xdxmk/sjzqController")
public class SjzqController {
	
	@Autowired
	private SjzqService sjzqService;

	@RequestMapping(params = "query_pcxx")
	@ResponseBody
	protected requestJson queryJdzx(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sjzqService.query_pcxx(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "query_tcjh")
	@ResponseBody
	protected requestJson query_tcjh(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sjzqService.query_tcjh(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	
	@RequestMapping(params = "query_kfgl")
	@ResponseBody
	protected requestJson query_kfgl(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sjzqService.query_kfgl(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "query_xxjd")
	@ResponseBody
	protected requestJson query_xxjd(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sjzqService.query_xxjd(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "query_gcqs")
	@ResponseBody
	protected requestJson query_gcqs(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sjzqService.query_gcqs(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "query_lybzj")
	@ResponseBody
	protected requestJson query_lybzj(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sjzqService.query_lybzj(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "query_tqk")
	@ResponseBody
	protected requestJson query_tqk(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sjzqService.query_tqk(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
}
