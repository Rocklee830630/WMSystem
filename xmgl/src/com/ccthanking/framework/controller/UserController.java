/**
 * @author wangzh
 */
package com.ccthanking.framework.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.model.responseJson;
import com.ccthanking.framework.service.UserService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/userController")
public class UserController {

	
	@Autowired
	private UserService userService;

	/**
	 * @param 用户注销
	 * @return org.springframework.web.servlet.ModelAndView
	 * @throws Exception 
	 */
	@RequestMapping(params = "logout", method = RequestMethod.POST)
	protected ModelAndView logout(final HttpServletRequest request,
			final ModelAndView mav)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		if (user != null) {
			com.ccthanking.framework.log.LogManager.writeLogoutLog(user);
		}
		request.getSession().invalidate();
		mav.setViewName("index");
		return mav;

	}

	/**
	 * @param 用户登录验证
	 * @param username
	 * @param password
	 * @return org.springframework.web.servlet.ModelAndView
	 * @throws Exception 
	 */
	@RequestMapping(params = "login", method = RequestMethod.POST)
	protected ModelAndView login(final HttpServletRequest request, final ModelAndView mav,HttpServletResponse response,
			@RequestParam(value = "username") final String username, @RequestParam(value = "password") final String password) throws Exception {
		User user = this.userService.getUserByUsernameAndPassword(username,password);
		if (user != null) {
			request.getSession().setAttribute(Globals.USER_KEY, user);
			request.getSession().setAttribute("userId", user.getAccount());
			request.getSession().setAttribute("DEPTID", user.getOrgDept().getDeptID());
			request.getSession().setAttribute("DEPTNAME", user.getOrgDept().getDept_Name());
			String requestIp = getIpAddr(request);
			if(!Pub.empty(requestIp)){
				user.setIP(requestIp);
			}
			LogManager.writeLoginLog(user, LogManager.LOGIN_STATUS_SUCCESS);
			mav.setViewName("/jsp/framework/portal/frame");
			return mav;
		}else
		{
			request.setAttribute("error", "用户名不存在或密码不正确");
			mav.setViewName("/index");
			return mav;
		}
		
	}	
	
	/**
	 * @param 用户登录验证
	 * @param username
	 * @param password
	 * @return org.springframework.web.servlet.ModelAndView
	 * @throws Exception 
	 */
	@RequestMapping(params = "login", method = RequestMethod.GET)
	protected ModelAndView loginGet(final HttpServletRequest request, final ModelAndView mav) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		if (user != null) {
			mav.setViewName("/jsp/framework/portal/frame");
			return mav;
		}else
		{
			mav.setViewName("/index");
			return mav;
		}
		
	}	
	
	@RequestMapping(params = "checkvalid")
	@ResponseBody
	public responseJson checkvalid(final HttpServletRequest request,requestJson js) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		responseJson j = new responseJson();
		
		//String menuTreeHtml = menuService.getMenuTreeHtml(user);
		
		//j.setMsg(menuTreeHtml);
		j.setSuccess(true);
		return j;
	}

	/**
     * @param 保存用户信息
	 * @return org.springframework.web.servlet.ModelAndView
	 */
	@RequestMapping(params = "/reg", method = RequestMethod.POST)
	@ResponseBody
	protected Map<String, Object> regPost(final HttpServletRequest reqeuest,
			@RequestParam(value = "username") final String username, @RequestParam(value = "password") final String password,
			@RequestParam(value = "realName") final String realName, @RequestParam(value = "email") final String email,
			@RequestParam(value = "sex") final Integer sex) {
		Map<String, Object> model = new HashMap<String, Object>();
	/*	User user = new User();
		user.setLastLoginIp(reqeuest.getRemoteAddr());
		long now = System.currentTimeMillis();
		user.setLastLoginTime(now);
		user.setLoginTimes(0);
		user.setLogonIp(reqeuest.getRemoteAddr());
		user.setPasswrod(password);
		user.setRealName(realName);
		user.setRegTime(now);
		user.setRole(0);
		user.setStatus(0);
		user.setUserName(username);
		user.setUserSex(sex);
		model.put("result", this.userService.insert(user));*/
		return model;
	}
	@RequestMapping(params = "insertdemo")
	@ResponseBody
	protected requestJson insertdemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.userService.insertdemo(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updatedemo")
	@ResponseBody
	protected requestJson updatedemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.userService.updatedemo(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "print", method = RequestMethod.POST)
	protected requestJson print(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "tabHtml") final String tabHtml) throws Exception {
	   
		
		requestJson j = new requestJson();
	//	String  resultVO = "";
	//	resultVO = this.userService.updatedemo(json.getMsg(),user);
	//	j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询样例json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "querydemo")
	@ResponseBody
	public requestJson querydemo(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.userService.queryConditiondemo(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "queryUser")
	@ResponseBody
	public requestJson queryUser(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = userService.queryUser(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	
	@RequestMapping(params = "executeUser")
	@ResponseBody
	protected requestJson executeUser(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String id = request.getParameter("id");
		String update = request.getParameter("update");
		try {
			String domResult = this.userService.executeUser(json.getMsg(), user, id, update);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "queryUnique")
	@ResponseBody
	protected String queryUnique(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String account = request.getParameter("account");
		String domResult = this.userService.queryUnique(account, user);
		return domResult;
	}
	
	@RequestMapping(params = "awardRoleToUser")
	@ResponseBody
	protected requestJson awardRoleToUser(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String account = request.getParameter("account");
		String checkValue = request.getParameter("checkValue");
				
		String[] roleNameAndId = "".equals(checkValue) ? new String[0] : checkValue.split(",");
		userService.awardRoleToUser(account, roleNameAndId, user);
		requestJson j = new requestJson();
		j.setMsg("");
		return j;
	}
	@RequestMapping(params="loadAllUser")
	@ResponseBody
	public void loadAllUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding(request.getCharacterEncoding());
		String menuJson = "";
		try {
			menuJson = this.userService.loadAllUser(request);
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(params = "awardRoleToPerson")
	@ResponseBody
	protected requestJson awardRoleToPerson(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String roleid = request.getParameter("roleid");
		String rolename = request.getParameter("rolename");
		String checkValue = request.getParameter("checkValue");
				
		String[] accountNameAndId = "".equals(checkValue) ? new String[0] : checkValue.split(",");
		userService.awardRoleToPerson(roleid,rolename, accountNameAndId, user);
		requestJson j = new requestJson();
		j.setMsg("");
		return j;
	}
	
	
	@RequestMapping(params = "modifyPassword")
	@ResponseBody
	protected requestJson modifyPassword(HttpServletRequest request,requestJson json)
			throws Exception {
		
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		JSONArray list = new BaseVO().doInitJson(json.getMsg());
		JSONObject jsonObj = (JSONObject) list.get(0);
		
		String oldPass = jsonObj.getString("oldpassword");
		if (oldPass == null)
			oldPass = "";
		String newPass = jsonObj.getString("newpassword");
		if (newPass == null)
			newPass = "";
		requestJson j = new requestJson();
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sqlQuery = "select t.password from fs_org_person t where t.account='"
					+ user.getAccount() + "'";
			String[][] res = DBUtil.query(sqlQuery);
			String password = "";
			if (res != null && res.length > 0){
				password = Encipher.DecodePasswd(res[0][0]);
			}
			if (password == null)
				password = "";
			
			if(!oldPass.equals(password)){
				j.setPrompt("输入的旧密码不正确!");
				throw new Exception("输入的旧密码不正确!");
			}
			newPass = Encipher.EncodePasswd(newPass);
			
			String sql = "update fs_org_person t set t.password='" + newPass
					+ "' where  t.account='" + user.getAccount() + "'";
			boolean result = DBUtil.exec(conn, sql);
			conn.commit();
			CacheManager.broadcastChanges(CacheManager.CACHE_USER, user.getAccount(),
					CacheManager.UPDATE);
			LogManager
					.writeUserLog("", "", Globals.OPERATION_TYPE_UPDATE,
							LogManager.RESULT_SUCCESS, "用户 [" + user.getAccount() + " / "
									+ user.getName() + "] 修改密码成功", user,"ACCOUNT",user.getAccount());
			j.setMsg("");
			return j;
		} catch (Exception e) {
			conn.rollback();
			LogManager.writeUserLog("", "", Globals.OPERATION_TYPE_UPDATE,
					LogManager.RESULT_FAILURE,
					"用户 [" + user.getAccount() + " / " + user.getName() + "] 修改密码失败！"+e.getMessage(),
					user,"ACCOUNT",user.getAccount());
			e.printStackTrace(System.out);
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
			
		}
		return j;
	}
	/**
	 * 查询用户头像和签名列表
	 * @param request
	 * @param js
	 */
	@RequestMapping(params = "queryUserFile")
	@ResponseBody
	public requestJson queryUserFile(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = userService.queryUserFile(request,js);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获得单位下所有人员
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="loadDeptUser")
	@ResponseBody
	public void loadDeptUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding(request.getCharacterEncoding());
		String menuJson = "";
		try {
			menuJson = this.userService.loadDeptUser(request);
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(params = "resetPw")
	@ResponseBody
	public requestJson resetPw(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		String account=request.getParameter("account");
		String  domresult = "";
		domresult = this.userService.resetPw(account);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "personInfo")
	@ResponseBody
	protected requestJson personInfo(HttpServletRequest request,requestJson json)
			throws Exception {
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.userService.personInfo(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}
	public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
	
}
