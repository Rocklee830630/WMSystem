package com.ccthanking.business.xdxmk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xdxmk.service.JhbmjkService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2013-12-9
 */
@Controller
@RequestMapping("/jhbmjk/jhbmjkController")
public class JhbmjkController {
	
	@Autowired
	private JhbmjkService jhbmjkService;

	@RequestMapping(params = "query_jhps")
	@ResponseBody
	protected requestJson queryJdzx(final HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jhbmjkService.queryJdzx(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "query_lxxmps")
	@ResponseBody
	protected requestJson query_lxxmps(final HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jhbmjkService.query_lxxmps(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "query_ybxms")
	@ResponseBody
	protected requestJson query_ybxms(final HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jhbmjkService.query_ybxms(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "query_wc")
	@ResponseBody
	protected requestJson query_wc(final HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jhbmjkService.query_wc(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "query_wbz")
	@ResponseBody
	protected requestJson query_wbz(final HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jhbmjkService.query_wbz(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "query_wtqk")
	@ResponseBody
	protected requestJson query_wtqk(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String reuslt = this.jhbmjkService.query_wtqk(json.getMsg(),request);
		j.setMsg(reuslt);
		return j;
	}
	@RequestMapping(params = "query_wtqk_qt")
	@ResponseBody
	protected requestJson query_wtqk_qt(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String reuslt = this.jhbmjkService.query_wtqk_qt(json.getMsg(),request);
		j.setMsg(reuslt);
		return j;
	}
}
