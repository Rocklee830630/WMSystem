package com.ccthanking.business.gcgl.gcjl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.gcgl.gcjl.service.GcjlService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2013-8-7
 */
@Controller
@RequestMapping("/gcjl/gcjlController")
public class GcjlController {
	
	@Autowired
	private GcjlService gcjlService;

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
		domresult = this.gcjlService.query(json.getMsg(),request);
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
		resultVO = this.gcjlService.insert(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 根据合同ID查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryByTcjhId")
	@ResponseBody
	public requestJson queryByHtid(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.gcjlService.queryByTcjhId(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 根据年度查询最大月份json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryMaxMonth")
	@ResponseBody
	public requestJson queryMaxMonth(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.gcjlService.queryMaxMonth(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 统计查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryTjxx")
	@ResponseBody
	public requestJson queryTjxx(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.gcjlService.queryTjxx(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 判断所选日期是否已存在json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDate")
	@ResponseBody
	public requestJson queryDate(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.gcjlService.queryDate(json.getMsg(),request);
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
		resultVO = this.gcjlService.delete(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
}
