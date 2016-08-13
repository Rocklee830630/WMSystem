package com.ccthanking.business.ztb.bmjk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.ztb.bmjk.service.ZtbBmjkService;
import com.ccthanking.framework.model.requestJson;
/**
 * 招投标合同部部门监控查询类
 * @author zhangbr@ccthanking.com
 *
 */
@Controller
@RequestMapping("/ztbBmjkController")
public class ZtbBmjkController {
	@Autowired
	private ZtbBmjkService service;
	/**
	 * 查询招投标表单信息
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZtbFormInfo")
	@ResponseBody
	public requestJson queryZtbFormInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZtbFormInfo(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询招投标表格信息
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZtbTableInfo")
	@ResponseBody
	public requestJson queryZtbTableInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZtbTableInfo(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDrillingDataZbxqzh")
	@ResponseBody
	public requestJson queryDrillingDataZbxqzh(HttpServletRequest request,requestJson js) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.service.queryDrillingDataZbxqzh(request,js.getMsg());
		j.setMsg(domresult);
		return j;

	}
}
