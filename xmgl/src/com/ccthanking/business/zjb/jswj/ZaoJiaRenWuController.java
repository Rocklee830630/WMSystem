/**
 * 
 */
package com.ccthanking.business.zjb.jswj;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.business.zjb.jsgl.JieSuanService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.*;

@Controller
@RequestMapping("/zaoJiaRenWuController")
public class ZaoJiaRenWuController {
	@Autowired
	private ZaoJiaRenWuService zaoJiaRenWuService;
	
	@RequestMapping(params = "queryZaoJiaRenWu")
	@ResponseBody
	public requestJson queryZaoJiaRenWu(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.zaoJiaRenWuService.queryZaoJiaRenWu(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "insertZaoJiaRenWu")
	@ResponseBody
	protected requestJson insertZaoJiaRenWu(HttpServletRequest request,requestJson json) throws Exception {
		String ywid=request.getParameter("ywid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.zaoJiaRenWuService.insertZaoJiaRenWu(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
	}
	
	
	@RequestMapping(params = "updateZaoJiaRenWu")
	@ResponseBody
	protected requestJson updateZaoJiaRenWu(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.zaoJiaRenWuService.updateZaoJiaRenWu(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
}
