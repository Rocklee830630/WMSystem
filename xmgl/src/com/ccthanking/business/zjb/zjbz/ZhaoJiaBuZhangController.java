package com.ccthanking.business.zjb.zjbz;

/**
 * @author zhaiyl
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
	@RequestMapping("/zhaoJiaBuZhangController")
	public class ZhaoJiaBuZhangController {
		@Autowired
		private ZhaoJiaBuZhangService zhaoJiaBuZhangService;
	/**
		 * 查询样例json
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(params = "LanBiaoJiaLeiJiTongJi")
		@ResponseBody
		public requestJson lanBiaoJiaTongJi(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zhaoJiaBuZhangService.lanBiaoJiaTongJi(request,user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "weiTuoZiXunGongSiTongJi")
		@ResponseBody
		public requestJson weiTuoZiXunGongSiTongJi(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zhaoJiaBuZhangService.weiTuoZiXunGongSiTongJi(request,user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "shenJianLv")
		@ResponseBody
		public requestJson shenJianLv(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zhaoJiaBuZhangService.shenJianLv(user,nd,request);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "jieSuanTongJi")
		@ResponseBody
		public requestJson jieSuanTongJi(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zhaoJiaBuZhangService.jieSuanTongJi(user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "jieSuanNianDuTongJi")
		@ResponseBody
		public requestJson jieSuanNianDuTongJi(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zhaoJiaBuZhangService.jieSuanNianDuTongJi(user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "songShenYuShenHe")
		@ResponseBody
		public requestJson songShenYuShenHe(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zhaoJiaBuZhangService.songShenYuShenHe(user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "shenHeYuCaiShen")
		@ResponseBody
		public requestJson shenHeYuCaiShen(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zhaoJiaBuZhangService.shenHeYuCaiShen(user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "caiShenYuShenJi")
		@ResponseBody
		public requestJson caiShenYuShenJi(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zhaoJiaBuZhangService.caiShenYuShenJi(user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "shenJiYuSongShen")
		@ResponseBody
		public requestJson shenJiYuSongShen(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.zhaoJiaBuZhangService.shenJiYuSongShen(user,nd);
			j.setMsg(domresult);
			return j;

		}
		@RequestMapping(params = "queryLbj")
		@ResponseBody
		public requestJson queryConditionLbj(HttpServletRequest request,requestJson json) throws Exception
		{
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  domresult = "";
	        
				domresult = this.zhaoJiaBuZhangService.queryConditionLbj(json.getMsg(),user,request);
			j.setMsg(domresult);
			return j;

		}
	}


