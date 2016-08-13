package com.ccthanking.business.sjgl.sj;

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
import com.ccthanking.business.sjgl.sj.GcSjService;
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
@RequestMapping("/sjgl/sj/sjController")
public class GcSjController {

	
	@Autowired
	private GcSjService Service;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "querySj")
	@ResponseBody
	public requestJson querySj(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
        String jhsjid=request.getParameter("jhsjid");
		if(jhsjid==null||"".equals(jhsjid))
		{
			domresult = this.Service.querySj(request,json.getMsg());
		}
		else{
			String  data="{querycondition: {conditions: [{\"value\":\""+jhsjid+"\",\"fieldname\":\"jhsj.gc_jh_sj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			domresult = this.Service.querySj(request,data);
		}
		//domresult = this.Service.queryConditionSj(json.getMsg(),user);
		j.setMsg(domresult);
		return j;
	}
	
	@RequestMapping(params = "Feedback")
	@ResponseBody
	protected requestJson updateXmb(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.Service.Feedback(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}

	@RequestMapping(params = "gsFeedback")
	@ResponseBody
	protected requestJson gsFeedback(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.Service.gsFeedback(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}

