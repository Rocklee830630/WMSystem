package com.ccthanking.business.qqsx.xmxxk;

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
@RequestMapping("/qqsx/xmxxkController")
public class XmxxkController {

	
	@Autowired
	private XmxxkService xmxxkService;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */

	//查询
	@RequestMapping(params = "exist")
	@ResponseBody
	public requestJson exist(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult ;
		domresult = this.xmxxkService.exist(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}
	//手续信息查询
	@RequestMapping(params = "querySxxx")
	@ResponseBody
	public requestJson querySxxx(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xmxxkService.querySxxx(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}
}

