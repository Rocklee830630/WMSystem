package com.ccthanking.business.zjb.bmgk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.zjb.bmgk.service.ZjBmgkService;
import com.ccthanking.framework.model.requestJson;

@Controller
@RequestMapping("/bmgk/ZjBmgkController")
public class ZjBmgkController {
	@Autowired
	private ZjBmgkService service;
	/**
	 * 结算相关数字的超链接页面查询
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJsljList")
	@ResponseBody
	public requestJson queryJsljList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJsljList(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询造价编制统计概况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZjbzTjgk")
	@ResponseBody
	public requestJson queryZjbzTjgk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZjbzTjgk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询工程結算-统计概况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryGcjsTjgk")
	@ResponseBody
	public requestJson queryGcjsTjgk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryGcjsTjgk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 查询工程結算-图形分析
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryGcjsTxfx")
	@ResponseBody
	public requestJson queryGcjsTxfx(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryGcjsTxfx(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询造价分析管理统计概况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryFxglTjgk")
	@ResponseBody
	public requestJson queryFxglTjgk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryFxglTjgk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询造价分析管理超概情况分布饼图
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryFxglCgfbChart")
	@ResponseBody
	public requestJson queryFxglCgfbChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryFxglCgfbChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询造价分析管理超概情况分布饼图
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryFxglCgfbTable")
	@ResponseBody
	public requestJson queryFxglCgfbTable(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryFxglCgfbTable(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryGcjsList")
	@ResponseBody
	public requestJson queryGcjsList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryGcjsList(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 造价链接
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryList_zjbz")
	@ResponseBody
	public requestJson queryList_zjbz(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryList_zjbz(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 造价列表
	 */
	@RequestMapping(params = "queryJslbljList")
	@ResponseBody
	public requestJson queryJslbljList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJslbljList(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}
