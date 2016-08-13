package com.ccthanking.business.cwjk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.model.requestJson;
/**
 * 
 * 财务监控
 * @author zhangbr@ccthanking.com
 *
 */
@Controller
@RequestMapping("/cwjk/CwjkController")
public class CwjkController {
	@Autowired
	private CwjkService service;

	/**
	 * 	//基本情况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJbqk")
	@ResponseBody
	public requestJson queryJbqk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJbqk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 投资完成及支付情况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZfqkColumn2d")
	@ResponseBody
	public requestJson queryZfqkColumn2d(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryZfqkColumn2d(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}

}
