/**
 * @author sunjl
 */
package com.ccthanking.business.xmglgs.xmxxgl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xmglgs.xmxxgl.service.XmxxService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 */
@Controller
@RequestMapping("/xmxxController")
public class XmxxController {

	
	@Autowired
	private XmxxService xmxxService;
	
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
		domresult = this.xmxxService.query(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "insert")
	@ResponseBody
	protected requestJson insert(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmxxService.insert(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	
	/**
	 * 根据项目ID查询标段查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryBdById")
	@ResponseBody
	public requestJson queryBdById(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xmxxService.queryBdById(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	

	/**
	 * 更新标段信息json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertBdxx")
	@ResponseBody
	protected requestJson insertBdxx(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmxxService.insertBdxx(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 根据项目ID查询项目
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXdkById")
	@ResponseBody
	public requestJson queryXdkById(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xmxxService.queryXdkById(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
}
