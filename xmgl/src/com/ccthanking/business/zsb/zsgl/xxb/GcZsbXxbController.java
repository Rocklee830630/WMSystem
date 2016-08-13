package com.ccthanking.business.zsb.zsgl.xxb;

/**
 * @author liujs
 */

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.business.zsb.zsgl.xxb.GcZsbXxbService;
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
@RequestMapping("/zsb/xxb/xxbController")
public class GcZsbXxbController {

	
	@Autowired
	private GcZsbXxbService xxbService;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXxb")
	@ResponseBody
	public requestJson queryXxb(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		
		String xxbid=request.getParameter("xxbid");
		if(xxbid==null||"".equals(xxbid))
		{
			domresult = this.xxbService.queryXxb(request,json.getMsg());
		}
		else{
			String  data="{querycondition: {conditions: [{\"value\":\""+xxbid+"\",\"fieldname\":\"GC_ZSB_XXB_ID\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			domresult = this.xxbService.queryXxb(request,data);
		}
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "insertXxb")
	@ResponseBody
	protected requestJson insertXxb(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xxbService.insertXxb(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateXxb")
	@ResponseBody
	protected requestJson updateXxb(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xxbService.updateXxb(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateXXB")
	@ResponseBody
	protected requestJson updateXXB(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xxbService.updateXXB(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	//删除功能
	@RequestMapping(params = "deleteZsxx")
	@ResponseBody
	protected requestJson deleteZsxx(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xxbService.deleteZsxx(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	//项目名称联想查询
	@RequestMapping(params = "xmmcAutoQuery")
	@ResponseBody
	public List<autocomplete> xmmcAutoComplete(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.xxbService.xmmcAutoComplete(request,match);
		return list;
	}
}

