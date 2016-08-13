package com.ccthanking.business.pqgl.sjzq;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.pqgl.sjzq.service.PqSjzqService;
import com.ccthanking.framework.model.requestJson;

/**
 * 排迁数据钻取类
 *
 */
@Controller
@RequestMapping("/sjzq/pqSjzqController")
public class PqSjzqController {
	@Autowired
	private PqSjzqService service;
	/**
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDrillingDataZxm")
	@ResponseBody
	public requestJson queryDrillingDataZxm(HttpServletRequest request,requestJson js) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.service.queryDrillingDataZxm(request,js.getMsg());
		j.setMsg(domresult);
		return j;

	}
	/**
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDrillingDataZh")
	@ResponseBody
	public requestJson queryDrillingDataZh(HttpServletRequest request,requestJson js) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.service.queryDrillingDataZh(request,js.getMsg());
		j.setMsg(domresult);
		return j;

	}
	
}
