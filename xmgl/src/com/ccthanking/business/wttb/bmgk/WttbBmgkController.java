package com.ccthanking.business.wttb.bmgk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.wttb.bmgk.service.WttbBmgkService;
import com.ccthanking.framework.model.requestJson;

@Controller
@RequestMapping("/wttb/WttbBmgkController")
public class WttbBmgkController {
	@Autowired
	private WttbBmgkService service;
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
	@RequestMapping(params = "queryWttbTjgkNew")
	@ResponseBody
	public requestJson queryWttbTjgkNew(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWttbTjgkNew(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题性质分布图表
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
	 * 查询满意程度分布图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWtmycdChart")
	@ResponseBody
	public requestJson queryWtmycdChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtmycdChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题性质分布图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWtcqqkChart")
	@ResponseBody
	public requestJson queryWtcqqkChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtcqqkChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题解决程度图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWtjjcdChart")
	@ResponseBody
	public requestJson queryWtjjcdChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtjjcdChart(request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 查询问题解决程度图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWtjjcdEChart")
	@ResponseBody
	public requestJson queryWtjjcdEChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtjjcdEChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题日期分布图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWtrqfbChart")
	@ResponseBody
	public requestJson queryWtrqfbChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtrqfbChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题类别分布图表
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
	 * 查询问题类别分布详细图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWtlbfbxxChart")
	@ResponseBody
	public requestJson queryWtlbfbxxChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtlbfbxxChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询主办部门图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZbbmChart")
	@ResponseBody
	public requestJson queryZbbmChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZbbmChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询主办部门图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZbbmEChart")
	@ResponseBody
	public requestJson queryZbbmEChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZbbmEChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询主办部门满意程度图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZbbmMycdChart")
	@ResponseBody
	public requestJson queryZbbmMycdChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZbbmMycdChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询主办领导图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZbldChart")
	@ResponseBody
	public requestJson queryZbldChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZbldChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询主办领导图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZbldEChart")
	@ResponseBody
	public requestJson queryZbldEChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZbldEChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题提出图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWttcChart")
	@ResponseBody
	public requestJson queryWttcChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWttcChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题提出图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWttcbtChart")
	@ResponseBody
	public requestJson queryWttcbtChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWttcbtChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询问题提出图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWtjsbtChart")
	@ResponseBody
	public requestJson queryWtjsbtChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtjsbtChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询主办部门图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryNdwttbqkChart")
	@ResponseBody
	public requestJson queryNdwttbqkChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryNdwttbqkChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询主办部门图表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryNdwttbqkEChart")
	@ResponseBody
	public requestJson queryNdwttbqkEChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryNdwttbqkEChart(request);
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
	@RequestMapping(params = "queryWtzdxm")
	@ResponseBody
	public requestJson queryWtzdxm(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryWtzdxm(request);
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
	@RequestMapping(params = "queryYcsjzcwt")
	@ResponseBody
	public requestJson queryYcsjzcwt(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryYcsjzcwt(request);
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
	@RequestMapping(params = "queryJjwtzdbm")
	@ResponseBody
	public requestJson queryJjwtzdbm(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJjwtzdbm(request);
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
	@RequestMapping(params = "queryJjxlzgbm")
	@ResponseBody
	public requestJson queryJjxlzgbm(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJjxlzgbm(request);
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
	/**
	 * 查询问题最多项目列表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDrillingDataJh")
	@ResponseBody
	public requestJson queryDrillingDataJh(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryDrillingDataJh(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}
