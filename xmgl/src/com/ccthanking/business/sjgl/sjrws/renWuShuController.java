package com.ccthanking.business.sjgl.sjrws;

/**
 * @author zhaiyl
 */

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.business.zjb.lbj.LbjService;
import com.ccthanking.framework.service.UserService;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.framework.model.*;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/renWuShuController")
public class renWuShuController {

	
	@Autowired
	private renWuShuService renWuShuService;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryRenWuShu")
	@ResponseBody
	public requestJson queryConditionDyqk(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.renWuShuService.queryRenWuShu(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "insertRenWuShu")
	@ResponseBody
	protected requestJson insertRenWuShu(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.renWuShuService.insertRenWuShu(json.getMsg(),user,request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateRenWuShu")
	@ResponseBody
	protected requestJson updateRenWuShu(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.renWuShuService.updateRenWuShu(json.getMsg(),user ,request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "deleteRenWuShu")
	@ResponseBody
	protected requestJson deleteRenWuShu(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.renWuShuService.deleteRenWuShu(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
}

