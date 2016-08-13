package com.ccthanking.framework.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.common.BusinessUtil;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.impl.CommonDictServiceImpl;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/commonDictController")
public class CommonDictController {
	
	@Autowired
	private CommonDictServiceImpl commonDictService;
	
	@RequestMapping(params = "queryCommonDict")
	@ResponseBody
	public requestJson queryCommonDict(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = commonDictService.queryCommonDict(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "executeCommonDict")
	@ResponseBody
	public requestJson executeCommonDict(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String operatorSign = request.getParameter("operatorSign");
		try {
			String domResult = commonDictService.executeCommonDict(json.getMsg(), user, operatorSign);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "autoComplete")
	@ResponseBody
	public List<autocomplete> autoComplete(HttpServletRequest request, autocomplete json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
		autocomplete ac = new autocomplete();
		String condition = "";
		if(!Pub.empty(json.getMatchInfo())){
			condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
		}
		condition = Pub.empty(condition)?"":" and " + condition;
		String [][] result = DBUtil.query("select DICT_ID,DICT_NAME from FS_COMMON_DICT where 1=1 " + condition + " order by pxh");
        if(null != result){
        	for(int i =0;i<result.length;i++){
        	  ac = new autocomplete();
        	  ac.setRegionCode(result[i][0]);
              ac.setRegionName(result[i][1]);
              autoResult.add(ac);
        	}
        }
		return autoResult;
	}
	
	@RequestMapping(params = "sortOne")
	@ResponseBody
	public requestJson sortOne(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = commonDictService.sortOne(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
}
