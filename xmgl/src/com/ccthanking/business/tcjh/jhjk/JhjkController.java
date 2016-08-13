package com.ccthanking.business.tcjh.jhjk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.model.requestJson;
/**
 * 
 * 项目储备库部门概况页面
 * @author zhangbr@ccthanking.com
 *
 */
@Controller
@RequestMapping("/tcjh/jhjk/JhjkController")
public class JhjkController {
	@Autowired
	private JhjkService service;

	/**
	 * 	//项目分类
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXMFLChart")
	@ResponseBody
	public requestJson queryXMFLChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryXMFLChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 	//计划编制
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJHBZChart")
	@ResponseBody
	public requestJson queryJHBZChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJHBZChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}	/**
	 * 按项目法人
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXMFR")
	@ResponseBody
	public requestJson queryXMFR(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryXMFR(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 责任部门
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZRBM")
	@ResponseBody
	public requestJson queryZRBM(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZRBM(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 项目进度
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXMJD")
	@ResponseBody
	public requestJson queryXMJD(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryXMJD(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 按项目类型
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXMLX")
	@ResponseBody
	public requestJson queryXMLX(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryXMLX(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 项目性质
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXMXZ")
	@ResponseBody
	public requestJson queryXMXZ(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryXMXZ(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 计划跟踪
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhgzColumn2d")
	@ResponseBody
	public requestJson queryJhgzColumn2d(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryJhgzColumn2d(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 计划项目项
	 */
	@RequestMapping(params = "queryJhxms")
	@ResponseBody
	public requestJson queryJhxms(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryJhxms(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 计划概况
	 */
	@RequestMapping(params = "queryJhgk")
	@ResponseBody
	public requestJson queryJhgk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryJhgk(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 计划编制统计概况查询
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhbzTjgk")
	@ResponseBody
	public requestJson queryJhbzTjgk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryJhbzTjgk(request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "queryJhgzlb")
	@ResponseBody
	public requestJson queryJhgzlb(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryJhgzlb(request, js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}
