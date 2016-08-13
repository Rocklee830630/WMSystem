package com.ccthanking.business.qqsx.sxfj;

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
import com.ccthanking.business.qqsx.sxfj.GcQqsxSxfjService;
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
@RequestMapping("/qqsx/sxfjController")
public class GcQqsxSxfjController {

	
	@Autowired
	private GcQqsxSxfjService sxfjService;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "querySxfj")
	@ResponseBody
	public requestJson queryConditionGhsp(requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.sxfjService.queryConditionSxfj(json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "insertSxfj")
	@ResponseBody
	protected requestJson insertGhsp(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sxfjService.insertSxfj(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateSxfj")
	@ResponseBody
	protected requestJson updateGhsp(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sxfjService.updateSxfj(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryBanLiXiangById")
	@ResponseBody
	protected requestJson queryByIddemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  ywid=request.getParameter("ywid");
		requestJson j = new requestJson();
		String  resultVO = "";
		
		resultVO = this.sxfjService.queryBanLiXiangById(ywid,user);
		j.setMsg(resultVO);
		//mav.setViewName("/jsp/framework/portal/frame");
		return j;
	}
	
}

