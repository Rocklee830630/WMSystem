package com.ccthanking.framework.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.impl.DictServiceImpl;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/dictController")
public class DictController {

	@Autowired
	private DictServiceImpl dictServiceImpl;
	
	@ResponseBody
	@RequestMapping(params="getAllDict")
	public void getAllDict(HttpServletRequest request, HttpServletResponse response) {
		String dictJson = dictServiceImpl.getDictByParentId("0");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().print(dictJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@RequestMapping(params="getCustomDict")
	public void getCustomDict(HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		String dictJson = dictServiceImpl.getCustomDictByParentId("0", type);
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().print(dictJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@RequestMapping(params="getDicsByParent")
	public requestJson getDicsByParent(HttpServletRequest request, requestJson json)throws Exception {
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = this.dictServiceImpl.getDicsByParent(json.getMsg(),user);
		j.setMsg(domresult);
		return j;
	}
	
	@RequestMapping(params = "insertOrUpdate")
	@ResponseBody
	protected requestJson insertOrUpdate(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String type = request.getParameter("type");
		
		JSONObject resultJson = null;
		if ("1".equals(type)) {
			resultJson = this.dictServiceImpl.insertOrUpdateSystemDict(json.getMsg(),user);
		} else {
			resultJson = this.dictServiceImpl.insertOrUpdate(json.getMsg(),user);
		}
		
		JSONObject jsonCondition = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		if(null != resultJson) {
			jsonCondition.put("value", resultJson.getString("PARENT_ID"));
			jsonCondition.put("fieldname", "PARENT_ID");
			jsonCondition.put("operation", "=");
			jsonCondition.put("logic", "and");
			jsonArray.add(jsonCondition);
			jsonCondition.put("value", resultJson.getString("ID"));
			jsonCondition.put("fieldname", "ID");
			jsonCondition.put("operation", "=");
			jsonCondition.put("logic", "and");
			jsonArray.add(jsonCondition);
		}
		jsonCondition = new JSONObject();
		jsonCondition.put("conditions", jsonArray);
		jsonCondition.put("querycondition", jsonCondition);
		jsonCondition.remove("conditions");
		String domresult = this.dictServiceImpl.getDicsByParent(jsonCondition.toString(),user);
		j.setMsg(domresult);
		return j;
	}
	

	@ResponseBody
	@RequestMapping(params = "checkUnique")
	protected String checkUnique(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String parent_id = request.getParameter("parent_id");
		String dict_code = request.getParameter("dict_code");
		String domResult = this.dictServiceImpl.checkUnique(parent_id, dict_code, user);
		return domResult;
	}

	@ResponseBody
	@RequestMapping(params = "deleteSystemDict")
	protected requestJson deleteSystemDict(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		
		JSONObject resultJson = this.dictServiceImpl.deleteSystemDict(json.getMsg(),user);
		
		JSONObject jsonCondition = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		if(null != resultJson) {
			jsonCondition.put("value", resultJson.getString("PARENT_ID"));
			jsonCondition.put("fieldname", "PARENT_ID");
			jsonCondition.put("operation", "=");
			jsonCondition.put("logic", "and");
			jsonArray.add(jsonCondition);
			jsonCondition.put("value", resultJson.getString("ID"));
			jsonCondition.put("fieldname", "ID");
			jsonCondition.put("operation", "=");
			jsonCondition.put("logic", "and");
			jsonArray.add(jsonCondition);
		}
		jsonCondition = new JSONObject();
		jsonCondition.put("conditions", jsonArray);
		jsonCondition.put("querycondition", jsonCondition);
		jsonCondition.remove("conditions");
		String domresult = this.dictServiceImpl.getDicsByParent(jsonCondition.toString(),user);
		j.setMsg(domresult);
		return j;
	}
}
