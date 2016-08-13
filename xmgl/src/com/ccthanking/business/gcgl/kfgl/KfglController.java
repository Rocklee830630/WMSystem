package com.ccthanking.business.gcgl.kfgl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.gcgl.kfgl.service.KfglService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2013-8-8
 */
@Controller
@RequestMapping("/kfgl/kfglController")
public class KfglController {
	
	@Autowired
	private KfglService kfglService;

	/**
	 * 查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query")
	@ResponseBody
	public requestJson queryCondition(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.kfglService.query(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 查询项目下达库json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXdxmk")
	@ResponseBody
	public requestJson queryXdxmk(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.kfglService.queryXdxmk(json.getMsg(), request);
		j.setMsg(domresult);
		return j;

	}
	
	
	/**
	 * 保存数据json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insert")
	@ResponseBody
	protected requestJson insert(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.kfglService.insert(json.getMsg(), request);
		j.setMsg(resultVO);
		return j;
	}
	
	
	/**
	 * 根据开复工ID查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryKfgById")
	@ResponseBody
	public requestJson queryKfgById(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.kfglService.queryKfgById(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 修改数据json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "update")
	@ResponseBody
	protected requestJson update(final HttpServletRequest request,requestJson json) throws Exception {
		
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.kfglService.update(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 新增状态数据json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertZt")
	@ResponseBody
	protected requestJson insertZt(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.kfglService.insertZt(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 根据统筹计划ID查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryKfglByXm")
	@ResponseBody
	public requestJson queryKfglByXm(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.kfglService.queryKfglByXm(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 删除方法
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public requestJson deletePqzxm(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.kfglService.delete(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}
