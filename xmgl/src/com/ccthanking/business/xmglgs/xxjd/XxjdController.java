package com.ccthanking.business.xmglgs.xxjd;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xmglgs.xxjd.service.XxjdService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2013-10-9
 */
@Controller
@RequestMapping("/xxjd/xxjdController")
public class XxjdController {
	
	@Autowired
	private XxjdService xxjdService;

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
		domresult = this.xxjdService.query(json.getMsg(),request);
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
		resultVO = this.xxjdService.insert(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 查询形象进度记录，根据计划数据ID
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryByJhid")
	@ResponseBody
	public requestJson queryByJhid(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xxjdService.queryByJhid(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	
	/**
	 * 查询计划编制表单内容
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhbzByJhid")
	@ResponseBody
	public requestJson queryJhbz(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xxjdService.queryJhbzByJhid(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 保存计划编制数据
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertJhbz")
	@ResponseBody
	protected requestJson insertJhbz(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xxjdService.insertJhbz(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 查询计划反馈表单内容
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhbzByJhfkid")
	@ResponseBody
	public requestJson queryJhbzByJhfkid(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xxjdService.queryJhbzByJhfkid(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 保存计划反馈数据
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertJhfk")
	@ResponseBody
	protected requestJson insertJhfk(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xxjdService.insertJhfk(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	
	/**
	 * 查询自定义节点根据计划编制ID
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZdyjdByJhbzid")
	@ResponseBody
	public requestJson queryZdyjdByJhbzid(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xxjdService.queryZdyjdByJhbzid(json.getMsg(),request);
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
		resultVO = this.xxjdService.delete(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}

	/**
	 * 根据ID查询甘特图
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "ggtById")
	@ResponseBody
	protected requestJson ggtById(final HttpServletRequest request,String json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String id = (String) request.getParameter("id");
		requestJson j = new requestJson();
		String  resultVO = this.xxjdService.ggtById(id,user);
		j.setMsg(resultVO);
		return j;
	}
}
