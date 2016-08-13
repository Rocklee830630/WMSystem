package com.ccthanking.business.sjgl.bgsf;

/**
 * @author 
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
import com.ccthanking.business.sjgl.bgsf.GcSjBgsfJsService;
import com.ccthanking.framework.service.UserService;
import com.ccthanking.framework.util.Pub;
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
@RequestMapping("/sjgl/jcjc/jcjcController")
public class GcSjBgsfJsController {

	
	@Autowired
	private GcSjBgsfJsService Service;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "querySf")
	@ResponseBody
	public requestJson querySf(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		String jhsjid=request.getParameter("jhsjid");
		if(Pub.empty(jhsjid)){
			domresult = this.Service.querySf(request,json.getMsg());
		}else{
			String  data="{querycondition: {conditions: [{\"value\":\""+jhsjid+"\",\"fieldname\":\"jhsj.gc_jh_sj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			domresult = this.Service.querySf(request,data);
		}
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "queryJs")
	@ResponseBody
	public requestJson queryJs(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.Service.queryJs(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "insertSf")
	@ResponseBody
	protected requestJson insertSf(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.Service.insertSf(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateSf")
	@ResponseBody
	protected requestJson updateSf(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.Service.updateSf(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "querySfxx")
	@ResponseBody
	public requestJson querySfxx(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		//String ZLLJSD=request.getParameter("ZLLJSD");
		domresult = this.Service.querySfxx(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "insertSfxx")
	@ResponseBody
	protected requestJson insertSfxx(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.Service.insertSfxx(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateSfxx")
	@ResponseBody
	protected requestJson updateSfxx(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.Service.updateSfxx(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "deleteJS")
	@ResponseBody
	protected requestJson deleteJS(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.Service.deleteJS(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "deleteLQ")
	@ResponseBody
	protected requestJson deleteLQ(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.Service.deleteLQ(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
}

