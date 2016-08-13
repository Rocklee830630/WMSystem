package com.ccthanking.business.zsb.zsjd;

/**
 * @author liujs
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
import com.ccthanking.business.zsb.zsjd.GcZsbJdbService;
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
@RequestMapping("/zsb/jdb/jdbController")
public class GcZsbJdbController {

	
	@Autowired
	private GcZsbJdbService jdbService;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJdb")
	@ResponseBody
	public requestJson queryJdb(HttpServletRequest request,requestJson json) throws Exception
	{
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.jdbService.queryJdb(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}

	@RequestMapping(params = "insertJdb")
	@ResponseBody
	protected requestJson insertJdb(HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jdbService.insertJdb(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateJdb")
	@ResponseBody
	protected requestJson updateJdb(HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jdbService.updateJdb(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	//插入完成后基础进度信息查询
	@RequestMapping(params = "queryJdxx")
	@ResponseBody
	public requestJson queryJdxx(HttpServletRequest request,requestJson json) throws Exception
	{
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.jdbService.queryJdxx(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	
	//删除功能
	@RequestMapping(params = "deleteJdxx")
	@ResponseBody
	protected requestJson deleteJdxx(HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jdbService.deleteJdxx(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	//查看当前时间是否有数据信息、
	@RequestMapping(params = "getZsjd")
	@ResponseBody
	public requestJson getJhfkCounts(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jdbService.getZsjd(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}

