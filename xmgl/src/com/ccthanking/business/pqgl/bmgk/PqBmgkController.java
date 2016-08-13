package com.ccthanking.business.pqgl.bmgk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.pqgl.bmgk.service.PqBmgkService;
import com.ccthanking.framework.model.requestJson;

/**
 * 排迁部门监控查询类
 * @author zhangbr@ccthanking.com
 *
 */
@Controller
@RequestMapping("/pqBmjkController")
public class PqBmgkController {
	@Autowired
	private PqBmgkService service;
	/**
	 * 查询排迁统计管理
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryPqTjgk")
	@ResponseBody
	public requestJson queryZtbFormInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryPqTjgk(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询排迁统计管理
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryPqRwxx")
	@ResponseBody
	public requestJson queryPqRwxx(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryPqRwxx(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询排迁统计管理
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryPqChart")
	@ResponseBody
	public requestJson queryPqChart(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryPqChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询排迁统计管理
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryPqNyxx")
	@ResponseBody
	public requestJson queryPqNyxx(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryPqNyxx(request);
		j.setMsg(resultVO);
		return j;
	}
}
