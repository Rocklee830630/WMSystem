/**
 * @author wangzh
 */
package com.ccthanking.business.zjb.jsgl;

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

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.*;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/jieSuanGuanliController")
public class JieSuanController {

	
	@Autowired
	private JieSuanService jieSuanService;
/**
	 * 查询样例json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJieSuandemo")
	@ResponseBody
	public requestJson queryConditiondemo(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.jieSuanService.queryConditionJieSuan(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params="queryhtxx")
	@ResponseBody
	public requestJson queryhtxx(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		//String  data="{querycondition: {conditions: [{\"value\":\"100\",\"fieldname\":\"rownum\",\"operation\":\"<=\",\"logic\":\"and\"} ]}}";
		domresult = this.jieSuanService.queryhtxx(json.getMsg(),user);
		j.setMsg(domresult);
		return j;
		
	}
	@RequestMapping(params="queryhtxxzs")
	@ResponseBody
	public requestJson queryhtxxzs(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String  data="{querycondition: {conditions: [{\"value\":\"100\",\"fieldname\":\"rownum\",\"operation\":\"<=\",\"logic\":\"and\"} ]}}";
		domresult = this.jieSuanService.queryhtxxzs(data,user);
		j.setMsg(domresult);
		return j;
		
	}
	@RequestMapping(params = "insertJieSuan")
	@ResponseBody
	protected requestJson insertdemo(HttpServletRequest request,requestJson json) throws Exception {
		String ywid=request.getParameter("ywid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jieSuanService.insertdemo(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
	}
	
	
	@RequestMapping(params = "updateJieSuan")
	@ResponseBody
	protected requestJson updatedemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jieSuanService.updatedemo(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "queryHeTong")
	@ResponseBody
	public requestJson queryHeTong(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.jieSuanService.queryConditionHeTong(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}	
	
	
	@RequestMapping(params = "deleteJS")
	@ResponseBody
	protected requestJson deleteDyqk(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jieSuanService.deleteDyqk(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 更改合同状态
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "updateHeTongZT")
	@ResponseBody
	protected requestJson updateHeTongZT(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.jieSuanService.updateHeTongZT(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}

}
