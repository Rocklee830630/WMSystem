package com.ccthanking.business.xdxmk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xdxmk.service.WttbXmscService;
import com.ccthanking.framework.model.requestJson;

@Controller
@RequestMapping("/wttb/WttbXmscController")
public class WttbXmscController {
	@Autowired
	private WttbXmscService service;
	/**
	 * 查询问题提报统计概况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWttbTjgk")
	@ResponseBody
	public requestJson queryWttbTjgk(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtTjgk(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题提报统计概况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWtxzfbChart")
	@ResponseBody
	public requestJson queryWtxzfbChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtxzfbChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题提报统计概况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWtlbfbChart")
	@ResponseBody
	public requestJson queryWtlbfbChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtlbfbChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题提报统计概况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJjqkfbChart")
	@ResponseBody
	public requestJson queryJjqkfbChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJjqkfbChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题最多项目列表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryInfoTable")
	@ResponseBody
	public requestJson queryInfoTable(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryInfoTable(js.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题最多项目列表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDrillingData")
	@ResponseBody
	public requestJson queryDrillingData(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryDrillingData(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
}
