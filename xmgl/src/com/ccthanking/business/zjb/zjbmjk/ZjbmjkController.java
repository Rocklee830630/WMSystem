package com.ccthanking.business.zjb.zjbmjk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.model.requestJson;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/zjbmjkController")
public class ZjbmjkController {

	@Autowired
	private ZjbmjkServiceImpl zjbmjkServiceImpl;
	
	/**
	 * 累计结算合同
	 * @param request
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "queryLjjsht")
	@ResponseBody
	public requestJson queryLjjsht(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String nd = request.getParameter("nd");
		String zjhtlx = request.getParameter("zjhtlx");
		try {
			String domResult = zjbmjkServiceImpl.queryLjjsht(json.getMsg(), nd, zjhtlx);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	@RequestMapping(params = "queryLbj")
	@ResponseBody
	public requestJson queryLbj(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String nd = request.getParameter("nd");
		String lujing = request.getParameter("lujing");
		String mingchen = request.getParameter("mingchen");
		String tiaojian = request.getParameter("tiaojian");
		try {
			String domResult = zjbmjkServiceImpl.queryLbj(json.getMsg(), lujing,mingchen,tiaojian,nd);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	@RequestMapping(params = "queryzjxx")
	@ResponseBody
	public requestJson queryzjxx(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String nd = request.getParameter("nd");
		String lujing = request.getParameter("lujing");
		String mingchen = request.getParameter("mingchen");
		String tiaojian = request.getParameter("tiaojian");
		try {
			String domResult = zjbmjkServiceImpl.queryzjxx(json.getMsg(), lujing,mingchen,tiaojian,nd);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
}
