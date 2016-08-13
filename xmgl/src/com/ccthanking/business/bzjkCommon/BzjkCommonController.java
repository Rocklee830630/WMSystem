package com.ccthanking.business.bzjkCommon;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.bzjkCommon.service.BzjkCommonService;
import com.ccthanking.framework.model.requestJson;
/**
 * 
 * 各部长部门概况链接页面
 * @author 
 *
 */
@Controller
@RequestMapping("/bzjk/bzjkCommonController")
public class BzjkCommonController {
	@Autowired
	private BzjkCommonService service;
/**
 * 查询统筹计划列表
 * @param request
 * @param js
 * @return
 * @throws Exception
 */
	@RequestMapping(params = "queryList")
	@ResponseBody
	public requestJson queryJcjkXx(HttpServletRequest request,requestJson js) throws Exception{
		requestJson j = new requestJson();
		String  domresult = this.service.queryList(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 前期
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryList_Qq")
	@ResponseBody
	public requestJson queryList_Qq(HttpServletRequest request,requestJson js) throws Exception{
		requestJson j = new requestJson();
		String  domresult = this.service.queryList_Qq(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}

	@RequestMapping(params = "queryList_gc_lybzj")
	@ResponseBody
	public requestJson queryList_gc_lybzj(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryList_gc_lybzj(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}

	@RequestMapping(params = "queryList_gc_gcqs")
	@ResponseBody
	public requestJson queryList_gc_gcqs(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryList_gc_gcqs(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}

	@RequestMapping(params = "queryList_gc_gcsx")
	@ResponseBody
	public requestJson queryList_gc_gcsx(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryList_gc_gcsx(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	@RequestMapping(params = "querySJBGList")
	@ResponseBody
	public requestJson querySJBGList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.querySJBGList(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	@RequestMapping(params = "queryGcqsPass")
	@ResponseBody
	public requestJson queryGcqsPass(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryGcqsPass(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 计划编制
	 */
	@RequestMapping(params = "queryJHBZList")
	@ResponseBody
	public requestJson queryJHBZList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryJHBZList(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 计划跟踪
	 */
	@RequestMapping(params = "queryJHGZList")
	@ResponseBody
	public requestJson queryJHGZList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryJHGZList(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 下达库链接
	 */
	@RequestMapping(params = "queryXDKList")
	@ResponseBody
	public requestJson queryXDKList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryXDKList(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 招标需求链接
	 */
	@RequestMapping(params = "queryZBXQList")
	@ResponseBody
	public requestJson queryZBXQList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryZBXQList(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 合同链接
	 */
	@RequestMapping(params = "queryHtList")
	@ResponseBody
	public requestJson queryHtList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.service.queryHtList(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
}
