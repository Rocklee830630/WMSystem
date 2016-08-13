package com.ccthanking.business.htgl.bmgk;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.htgl.bmgk.service.GcHtBmgkService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @author xiahongbo
 * @date 2014-9-15
 */
@Controller
@RequestMapping("/bmgk/GcHtBmgkController")
public class GcHtBmgkController {

	@Autowired
	private GcHtBmgkService service;
	
	@RequestMapping(params="queryHtglBmlb")
	@ResponseBody
	public requestJson queryHtglBmlb(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = service.queryHtglBmlb(request, json.getMsg());
		j.setMsg(domString);
		return j;
	}
	
	@RequestMapping(params="queryHtglTjgk")
	@ResponseBody
	public requestJson queryHtglTjgk(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = service.queryHtglTjgk(request, json.getMsg());
		j.setMsg(domString);
		return j;
	}
	@RequestMapping(params="queryHtglZbsg")
	@ResponseBody
	public requestJson queryHtglZbsg(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = service.queryHtglZbsg(request, json.getMsg());
		j.setMsg(domString);
		return j;
	}
	@RequestMapping(params="queryHtglZbjl")
	@ResponseBody
	public requestJson queryHtglZbjl(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String domString = service.queryHtglZbjl(request, json.getMsg());
		j.setMsg(domString);
		return j;
	}
	/**
	 * 总工办—设计—招标需求
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZgbsjzbChart")
	@ResponseBody
	public requestJson queryZgbsjzbChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZgbsjzbChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}	
	/**
	 * 总工办—前期—招标需求
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZgbqqzbChart")
	@ResponseBody
	public requestJson queryZgbqqzbChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZgbqqzbChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 总工办—造价部—招标需求
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZgbzjbzbChart")
	@ResponseBody
	public requestJson queryZgbzjbzbChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZgbzjbzbChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 总工办—招投标—招标需求
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZgbztbzbChart")
	@ResponseBody
	public requestJson queryZgbztbzbChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZgbztbzbChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 总工办—排迁—招标需求
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZgbpqzbChart")
	@ResponseBody
	public requestJson queryZgbpqzbChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZgbpqzbChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 总工办—征收—招标需求
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZgbzszbChart")
	@ResponseBody
	public requestJson queryZgbzszbChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZgbzszbChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	
	/**
	 * 总工办—施工—招标需求
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZgbsgzbChart")
	@ResponseBody
	public requestJson queryZgbsgzbChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZgbsgzbChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 总工办—监理—招标需求
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZgbjlzbChart")
	@ResponseBody
	public requestJson queryZgbjlzbChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZgbjlzbChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 总工办—质量安全—招标需求
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZgbzlaqzbChart")
	@ResponseBody
	public requestJson queryZgbzlaqzbChart(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZgbzlaqzbChart(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 招标状态
	 */
	@RequestMapping(params = "queryZbzt")
	@ResponseBody
	protected requestJson queryZbzt(HttpServletRequest request,HttpServletResponse response) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryZbzt(request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 招标合同-招标需求-2D图
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZbglZbxqColumn2d")
	@ResponseBody
	public requestJson queryZbglZbxqColumn2d(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryZbglZbxqColumn2d(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询招标数据链接页面
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZbsjList")
	@ResponseBody
	public requestJson queryZbsjList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryZbsjList(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryHtsjList")
	@ResponseBody
	public requestJson queryHtsjList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryHtsjList(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryZBXQList")
	@ResponseBody
	public requestJson queryZBXQList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String resultVO = this.service.queryZBXQList(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}
