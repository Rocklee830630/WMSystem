package com.ccthanking.business.jhfk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.jhfk.service.JhfkService;
import com.ccthanking.framework.model.requestJson;

@Controller
@RequestMapping("/jhfk/JhfkController")
public class JhfkController {
	@Autowired
	private JhfkService service;
	/**
	 * 查询表单内容
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryFormInfo")
	@ResponseBody
	public requestJson queryFormInfo(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryFormInfo(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询表单内容
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryTabList")
	@ResponseBody
	public requestJson queryTabList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryTabList(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取排迁计划反馈次数
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getJhfkCounts")
	@ResponseBody
	public requestJson getJhfkCounts(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.getJhfkCounts(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取排迁计划反馈次数
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doJhsjfk")
	@ResponseBody
	public requestJson doJhsjfk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doFkjh(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取排迁计划反馈次数
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getFkFlag")
	@ResponseBody
	public requestJson getFkFlag(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.getFkFlag(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取排迁计划反馈次数
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getFkFkrq")
	@ResponseBody
	public requestJson getFkFkrq(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.getFkFkrq(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取排迁计划反馈次数
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXMFkxx")
	@ResponseBody
	public requestJson queryXMFkxx(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryXMFkxx(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取排迁计划反馈次数
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getFkJhrq")
	@ResponseBody
	public requestJson getFkJhrq(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.getFkJhrq(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取排迁计划反馈次数
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXmxx")
	@ResponseBody
	public requestJson queryXmxx(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryXmxx(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}
