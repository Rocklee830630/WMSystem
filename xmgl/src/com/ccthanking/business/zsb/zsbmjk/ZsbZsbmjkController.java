package com.ccthanking.business.zsb.zsbmjk;

/**
 * @author liujs
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

	@Controller
	@RequestMapping("/zengChaiBuZhangController")
	public class ZsbZsbmjkController {
		@Autowired
		private ZengChaiBuZhangService zengChaiBuZhangService;
	/**
		 * 查询样例json
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(params = "tongJiGaiKuang")
		@ResponseBody
		public requestJson queryTongJiGaiKuang(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zengChaiBuZhangService.queryTongJiGaiKuang(user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "juMingChaiQianJinZhan")
		@ResponseBody
		public requestJson juMingChaiQianJinZhan(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zengChaiBuZhangService.juMingChaiQianJinZhan(user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "qiYeChaiQianJinZhan")
		@ResponseBody
		public requestJson qiYeChaiQianJinZhan(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zengChaiBuZhangService.qiYeChaiQianJinZhan(user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "zhengDiMianJiJinZhan")
		@ResponseBody
		public requestJson zhengDiMianJiJinZhan(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zengChaiBuZhangService.zhengDiMianJiJinZhan(user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "zhengChaiJinZhan")
		@ResponseBody
		public requestJson zhengChaiJinZhan(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zengChaiBuZhangService.zhengChaiJinZhan(user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "moDiXiangQing")
		@ResponseBody
		public requestJson moDiXiangQing(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zengChaiBuZhangService.moDiXiangQing(user,nd);
			j.setMsg(domresult);
			return j;

		}

	}


