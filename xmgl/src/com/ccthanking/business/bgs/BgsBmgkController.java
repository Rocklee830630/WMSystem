package com.ccthanking.business.bgs;

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
import com.ccthanking.framework.util.Pub;

	@Controller
	@RequestMapping("/BgsBmgk")
	public class BgsBmgkController {
		@Autowired
		private BgsBmgkService bgsBmgkService;
	/**
		 * 查询统筹计划json
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(params = "TongChouJiHuaQuery")
		@ResponseBody
		public requestJson lanBiaoJiaTongJi(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			String nd=request.getParameter("nd");
			domresult = this.bgsBmgkService.TongChouJiHuaQuery(request,user);
			j.setMsg(domresult);
			return j;

		}
		/**
		 * 查询发文情况json
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(params = "faWenQingKuang")
		@ResponseBody
		public requestJson faWenQingKuang(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			domresult = this.bgsBmgkService.faWenQingKuang(request,user);
			j.setMsg(domresult);
			return j;

		}
		/**
		 * 查询收文情况json
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(params = "shouWenQingKuang")
		@ResponseBody
		public requestJson shouWenQingKuang(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			domresult = this.bgsBmgkService.shouWenQingKuang(request,user);
			j.setMsg(domresult);
			return j;

		}
		/**
		 * 查询流程执行情况json
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(params = "liuChengQingKuang")
		@ResponseBody
		public requestJson liuChengQingKuang(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			domresult = this.bgsBmgkService.liuChengQingKuang(request,user);
			j.setMsg(domresult);
			return j;

		}
		/**
		 * 存在问题情况情况json
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(params = "cunZaiWenTiQingKuang")
		@ResponseBody
		public requestJson cunZaiWenTiQingKuang(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			domresult = this.bgsBmgkService.cunZaiWenTiQingKuang(request,user);
			j.setMsg(domresult);
			return j;

		}
		/**
		 * 存在问题情况情况json
		 * @param json
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(params = "tongChouJiHuaQingKuang")
		@ResponseBody
		public requestJson tongChouJiHuaQingKuang(HttpServletRequest request,HttpServletResponse response) throws Exception
		{
			requestJson j = new requestJson();
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String  domresult = "";
			domresult = this.bgsBmgkService.tongChouJiHuaQingKuang(request,user);
			j.setMsg(domresult);
			return j;

		}
		
	}


