package com.ccthanking.business.xmcbk.bmgk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xmcbk.bmgk.service.XmcbkBmgkService;
import com.ccthanking.framework.model.requestJson;
/**
 * 
 * 项目储备库部门概况页面
 * @author zhangbr@ccthanking.com
 *
 */
@Controller
@RequestMapping("/bmgk/XmcbkBmgkController")
public class XmcbkBmgkController {
	@Autowired
	private XmcbkBmgkService service;
	/**
	 * 按所属地区（建委项目）
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryTableAssdq")
	@ResponseBody
	public requestJson queryTableAssdq(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryTableAssdq(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 按所属地区
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryTableAssdqAll")
	@ResponseBody
	public requestJson queryTableAssdqAll(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryTableAssdqAll(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 责任部门
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryTableAzrbm")
	@ResponseBody
	public requestJson queryTableAzrbm(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryTableAzrbm(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 项目类型
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryTableAxmlx")
	@ResponseBody
	public requestJson queryTableAxmlx(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryTableAxmlx(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 总投资
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryTableAztz")
	@ResponseBody
	public requestJson queryTableAztz(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryTableAztz(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 	//政府——年度投资
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZf_NdChart")
	@ResponseBody
	public requestJson queryZf_NdChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZf_NdChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 	//市场化——年度投资
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "querySch_NdChart")
	@ResponseBody
	public requestJson querySch_NdChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.querySch_NdChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 	//两委一局饼图3
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryLwyjChart")
	@ResponseBody
	public requestJson queryLwyjChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryLwyjChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 市场化——年度投资
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhtzTjgk")
	@ResponseBody
	public requestJson queryJhtzTjgk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJhtzTjgk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 	//政府市场化
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(params = "queryZf_Sch")
	@ResponseBody
	public requestJson queryZf_Sch(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZf_Sch(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 	储备库统计概况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryCbkTjgk")
	@ResponseBody
	public requestJson queryCbkTjgk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryCbkTjgk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 	计划下达统计概况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhxdTjgk")
	@ResponseBody
	public requestJson queryJhxdTjgk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJhxdTjgk(request,js.getMsg());
		j.setMsg(resultVO);
		System.out.println(j.getMsg());
		return j;
	}
	/**
	 * 	计划下达年初计划
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhxdNcjhChart")
	@ResponseBody
	public requestJson queryJhxdNcjhChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJhxdNcjhChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 	计划下达追加计划
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhxdZjjhChart")
	@ResponseBody
	public requestJson queryJhxdZjjhChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJhxdZjjhChart(request);
		j.setMsg(resultVO);
		return j;
	}

	/**
	 * 	计划下达追加计划
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhzxZjjhChart")
	@ResponseBody
	public requestJson queryJhzxZjjhChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJhzxZjjhChart(request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 	计划下达追加计划
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhzxTjgk")
	@ResponseBody
	public requestJson queryJhzxTjgk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryJhzxTjgk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}

	/**
	 * “两委一局”计划->政府投资
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryTabLwyjZf")
	@ResponseBody
	public requestJson queryTabLwyjZf(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryTabLwyjZf(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * “两委一局”计划->市场投资
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryTabLwyjSc")
	@ResponseBody
	public requestJson queryTabLwyjSc(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryTabLwyjSc(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 计划执行--年初计划--政府投资
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhzxNcjhZftzChart")
	@ResponseBody
	public requestJson queryJhzxNcjhZftzChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryJhzxNcjhZftzChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 计划执行--年初计划--市场投资
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhzxNcjhSctzChart")
	@ResponseBody
	public requestJson queryJhzxNcjhSctzChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryJhzxNcjhSctzChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 计划下达——年初计划
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhxdNcjh")
	@ResponseBody
	public requestJson queryJhxdNcjh(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryJhxdNcjh(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 计划下达——追加计划
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhxdZjjh")
	@ResponseBody
	public requestJson queryJhxdZjjh(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryJhxdZjjh(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryList")
	@ResponseBody
	public requestJson queryJcjkXx(HttpServletRequest request,requestJson js) throws Exception{
		requestJson j = new requestJson();
		String  domresult = this.service.queryList(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 查询开完工结算情况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryKwgjs")
	@ResponseBody
	public requestJson queryKwgjs(HttpServletRequest request,requestJson js) throws Exception{
		requestJson j = new requestJson();
		String  domresult = this.service.queryKwgjs(request,js.getMsg());
		j.setMsg(domresult);
		return j;
	}
	
}
