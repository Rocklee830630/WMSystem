/**
 * @author wangzh
 */
package com.ccthanking.business.xmglgs.zybgl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.*;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/zhouYueBaoController")
public class ZhouYueBaoController {

	
	@Autowired
	private ZhouYueBaoService zhouYueBaoService;
/**
	 * 查询样例json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryzhouYueBao")
	@ResponseBody
	public requestJson queryConditiondemo(HttpServletRequest request,requestJson json) throws Exception
	{
		
		requestJson j = new requestJson();
		String  domresult = "";
		String shijianid=request.getParameter("shijianid");
		domresult = this.zhouYueBaoService.queryzhouYueBao(json.getMsg(),shijianid);
		j.setMsg(domresult);
		return j;

	}

	@RequestMapping(params = "insert")
	@ResponseBody
	protected requestJson insert(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.zhouYueBaoService.insert(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "insertShiJian")
	@ResponseBody
	protected requestJson insertShiJian(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.zhouYueBaoService.insertShiJian(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryShiJian")
	@ResponseBody
	public requestJson queryShiJian(HttpServletRequest request,requestJson json) throws Exception
	{
		
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhouYueBaoService.queryShiJian(json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "updatedShiJian")
	@ResponseBody
	protected requestJson updatedShiJian(HttpServletRequest request,requestJson json) throws Exception {
	
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.zhouYueBaoService.updatedShiJian(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "update")
	@ResponseBody
	protected requestJson updatedemo(HttpServletRequest request,requestJson json) throws Exception {
		
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.zhouYueBaoService.updatedemo(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryXiangMu")
	@ResponseBody
	public requestJson queryHeTong(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhouYueBaoService.queryConditionXiangMu(json.getMsg());
		j.setMsg(domresult);
		return j;

	}	
	
	@RequestMapping(params = "queryBanLiXiangById")
	@ResponseBody
	protected requestJson queryByIddemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  ywid=request.getParameter("ywid");
	
		requestJson j = new requestJson();
		String  resultVO = "";
		
		resultVO = this.zhouYueBaoService.queryBanLiXiangById(ywid,user);
		j.setMsg(resultVO);
		//mav.setViewName("/jsp/framework/portal/frame");
		return j;
	}


}
