package com.ccthanking.business.gk;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;
@Controller
@RequestMapping("/gk/gkController")
public class GkController {

	@Autowired
	private GkService gkService;
	String domresult="";
	String resultVO="";
	/**
	 * 计划概况查询
	 */
	@RequestMapping(params = "query_jhgk")
	@ResponseBody
	public requestJson query_jhgk(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		resultVO = this.gkService.query_jhgk(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 统筹计划概况查询
	 */
	@RequestMapping(params = "query_tcjh")
	@ResponseBody
	public requestJson query_tcjh(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		resultVO = this.gkService.query_tcjh(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}

	
	/**
	 * 计划概况查询
	 */
	@RequestMapping(params = "query_jcjh")
	@ResponseBody
	public requestJson query_jcjh(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		resultVO = this.gkService.query_jcjh(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}

}
