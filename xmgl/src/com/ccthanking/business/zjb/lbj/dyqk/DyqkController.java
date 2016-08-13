package com.ccthanking.business.zjb.lbj.dyqk;

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
@RequestMapping("/zjb/lbj/dyqkController")
public class DyqkController {

	
	@Autowired
	private DyqkService Service;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDyqk")
	@ResponseBody
	public requestJson queryConditionDyqk(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		//domresult = this.lbjService.queryConditionLbj(json.getMsg(),user);
        String jhsjid=request.getParameter("jhsjid");
		if(jhsjid==null||"".equals(jhsjid))
		{
			domresult = this.Service.queryConditionDyqk(json.getMsg(),user);
		}
		else{
			String  data="{querycondition: {conditions: [{\"value\":\""+jhsjid+"\",\"fieldname\":\"JHSJID\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			domresult = this.Service.queryConditionDyqk(data,user);
		}
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "insertDyqk")
	@ResponseBody
	protected requestJson insertDyqk(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		String ywid=request.getParameter("ywid");
		resultVO = this.Service.insertDyqk(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateDyqk")
	@ResponseBody
	protected requestJson updateDyqk(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.Service.updateDyqk(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "deleteDyqk")
	@ResponseBody
	protected requestJson deleteDyqk(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.Service.deleteDyqk(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
}

