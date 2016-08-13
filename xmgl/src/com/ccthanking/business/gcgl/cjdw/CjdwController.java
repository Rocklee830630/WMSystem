package com.ccthanking.business.gcgl.cjdw;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.gcgl.cjdw.service.CjdwService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2013-8-9
 */
@Controller
@RequestMapping("/cjdw/cjdwController")
public class CjdwController {
	
	@Autowired
	private CjdwService cjdwService;

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
		domresult = this.cjdwService.query(json.getMsg(),request);
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
		resultVO = this.cjdwService.insert(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 判断单位名称是否重复
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryByMingChen")
	@ResponseBody
	public requestJson queryByMingChen(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.cjdwService.queryByMingChen(request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 根据单位ID查询中标信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZtbList")
	@ResponseBody
	public requestJson queryZtbList(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.cjdwService.queryZtbList(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 根据单位ID查询项目信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXMXXList")
	@ResponseBody
	public requestJson queryXMXXList(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.cjdwService.queryXMXXList(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "deleteCJDW")
	@ResponseBody
	protected requestJson deleteCJDW(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.cjdwService.deleteCJDW(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
}
