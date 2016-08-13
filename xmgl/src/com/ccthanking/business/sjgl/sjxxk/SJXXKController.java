package com.ccthanking.business.sjgl.sjxxk;

/**
 * @author zhaiyl
 */


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ccthanking.framework.model.*;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/sjgl/sj/sjxxkController")
public class SJXXKController {

	@Autowired
	private SJXXKService Service;

	@RequestMapping(params = "xiangMuXinXi")
	@ResponseBody
	public requestJson queryTongJiGaiKuang(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		domresult = this.Service.xiangMuXinXi(user, request);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "queryTongCJH")
	@ResponseBody
	public requestJson queryTongCJH(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		domresult = this.Service.queryTongCJH(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "queryJiHuaWanCheng")
	@ResponseBody
	public requestJson queryJiHuaWanCheng(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		domresult = this.Service.queryJiHuaWanCheng(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "queryJianCeJianCe")
	@ResponseBody
	public requestJson queryJianCeJianCe(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		domresult = this.Service.queryJianCeJianCe(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "queryJiaoJunGong")
	@ResponseBody
	public requestJson queryJiaoJunGong(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		domresult = this.Service.queryJiaoJunGong(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "queryHeTongXinXi")
	@ResponseBody
	public requestJson queryHeTongXinXi(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		domresult = this.Service.queryHeTongXinXi(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;

	}
	
}
