/**
 * @author zhaiyl
 */
package com.ccthanking.business.bgh.bgh;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.*;

/**
 * @author zhaiyl
 */
@Controller
@RequestMapping("/banGongHuiController")
public class BanGongHuiController {

	
	@Autowired
	private BanGongHuiService banGongHuiService;
/**
	 * json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "querybanGongHui")
	@ResponseBody
	public void querybanGongHui(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String roleId = request.getParameter("str");
		String dept = request.getParameter("dept");
		response.setCharacterEncoding(request.getCharacterEncoding());
		String menuJson = "";
		try {
			menuJson = this.banGongHuiService.querybanGongHuiHuiCi(request);
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(params = "querybanGongHuiList")
	@ResponseBody
	public requestJson querybanGongHuiList(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.banGongHuiService.querybanGongHuiList(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}
	@RequestMapping(params = "insertBanGongHui")
	@ResponseBody
	protected requestJson insertBanGongHui(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.banGongHuiService.insertBanGongHui(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateBanGongHui")
	@ResponseBody
	protected requestJson updateBanGongHui(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.banGongHuiService.updateBanGongHui(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryHyj")
	@ResponseBody
	public requestJson queryHyj(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.banGongHuiService.queryHyj(request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryZXHC")
	@ResponseBody
	public requestJson queryZXHC(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.banGongHuiService.queryZXHC(request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryBGHWT")
	@ResponseBody
	public requestJson queryBGHWT(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.banGongHuiService.queryBGHWT(request);
		j.setMsg(resultVO);
		return j;
	}

	@RequestMapping(params = "delete")
	@ResponseBody
	protected String delete(HttpServletRequest request,requestJson json) throws Exception {
		String resultVO = this.banGongHuiService.delete(request);
		return resultVO;
	}
	@RequestMapping(params = "queryDshyt")
	@ResponseBody
	public requestJson queryDshyt(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.banGongHuiService.queryDshyt(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}
