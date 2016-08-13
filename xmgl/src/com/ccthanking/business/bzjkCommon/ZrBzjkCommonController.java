package com.ccthanking.business.bzjkCommon;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.bzjkCommon.service.ZrBzjkCommonService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author xiahongbo
 * @date 2014-12-12
 */
@Controller
@RequestMapping("/bzjkjm/zrBzjkCommonController")
public class ZrBzjkCommonController {
	@Autowired
	private ZrBzjkCommonService  Service;
	/**
	 * 发现问题统计概况查询
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryFxwtTjgk")
	@ResponseBody
	public requestJson queryFxwtTjgk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.Service.queryFxwtTjgk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 
	 * 查询各领导问题解决情况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZbldChart")
	@ResponseBody
	public requestJson queryZbldChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.Service.queryZbldChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询各部门问题解决情况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZbbmChart")
	@ResponseBody
	public requestJson queryZbbmChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.Service.queryZbbmChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 计划实施项目情况
	 */
	@RequestMapping(params = "queryYkgxmqkColumn2d")
	@ResponseBody
	public requestJson queryYkgxmqkColumn2d(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.Service.queryYkgxmqkColumn2d(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 已开工项目情况
	 */
	@RequestMapping(params = "queryJhssxmColumn2d")
	@ResponseBody
	public requestJson queryJhssxmColumn2d(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.Service.queryJhssxmColumn2d(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 *列表
	 */
	@RequestMapping(params = "queryList")
	@ResponseBody
	public requestJson queryList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.Service.queryList(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 *年度任务
	 */
	@RequestMapping(params = "queryNDRW")
	@ResponseBody
	public requestJson queryNDRW(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.Service.queryNDRW(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 *列表链接
	 */
	@RequestMapping(params = "queryListQqsg")
	@ResponseBody
	public requestJson queryListQqsg(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.Service.queryListQqsg(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryListLx")
	@ResponseBody
	public requestJson queryListLx(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.Service.queryListLx(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "querySjglList")
	@ResponseBody
	public requestJson querySjglList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.Service.querySjglList(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}
