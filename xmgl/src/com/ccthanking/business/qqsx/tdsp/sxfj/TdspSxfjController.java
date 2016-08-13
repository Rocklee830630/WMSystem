package com.ccthanking.business.qqsx.tdsp.sxfj;

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
import com.ccthanking.business.qqsx.tdsp.sxfj.TdspSxfjService;
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
@RequestMapping("/qqsx/tdsp/sxfjController")
public class TdspSxfjController {

	
	@Autowired
	private TdspSxfjService sxfjService;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */

	//查询
	@RequestMapping(params = "queryXmxx")
	@ResponseBody
	public requestJson queryXmxx(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		String jhsjid=request.getParameter("jhsjid");
		if(Pub.empty(jhsjid)){
			domresult = this.sxfjService.queryXmxx(request,json.getMsg());
		}else{
			String  data="{querycondition: {conditions: [{\"value\":\""+jhsjid+"\",\"fieldname\":\"jhsj.gc_jh_sj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			domresult = this.sxfjService.queryXmxx(request,data);
		}
		j.setMsg(domresult);
		return j;
	}
	
	//查询
	@RequestMapping(params = "querySxfj")
	@ResponseBody
	public requestJson querySxfj(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.sxfjService.querySxfj(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}
	
	//添加
	@RequestMapping(params = "insertSxfj")
	@ResponseBody
	protected requestJson insertSxfj(HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		String ywid=request.getParameter("ywid");
		resultVO = this.sxfjService.insertSxfj(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	//修改
	@RequestMapping(params = "updateSxfj")
	@ResponseBody
	protected requestJson updateSxfj(HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sxfjService.updateSxfj(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	//反馈功能
	@RequestMapping(params = "feedbackQqsx")
	@ResponseBody
	protected requestJson feedbackQqsx(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sxfjService.feedbackQqsx(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	//删除
	@RequestMapping(params = "deleteSxfj")
	@ResponseBody
	protected requestJson deleteSxfj(HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sxfjService.deleteSxfj(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	//获取个数
	@RequestMapping(params = "getCounts")
	@ResponseBody
	protected requestJson getCounts(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sxfjService.getCounts(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	//获取时间
	@RequestMapping(params = "getDate")
	@ResponseBody
	protected requestJson getDate(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sxfjService.getDate(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}

