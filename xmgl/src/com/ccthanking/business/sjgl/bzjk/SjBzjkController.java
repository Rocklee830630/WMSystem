package com.ccthanking.business.sjgl.bzjk;

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
	@RequestMapping("sjbzgkController")
	public class SjBzjkController {
		@Autowired
		private SjBzjkService SjBzjkService;
		/**
		 * 施工进展情况
		 */
		@RequestMapping(params = "queryTjgk")
		@ResponseBody
		protected requestJson queryTjgk(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.queryTjgk(request);
			j.setMsg(resultVO);
			return j;
		}
		/**
		 * 拆迁图
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(params = "queryCQT")
		@ResponseBody
		protected requestJson queryCQT(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.queryCQT(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "queryPQT")
		@ResponseBody
		protected requestJson queryPQT(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.queryPQT(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "querySGT")
		@ResponseBody
		protected requestJson querySGT(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.querySGT(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "querySJBG")
		@ResponseBody
		protected requestJson querySJBG(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.querySJBG(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "queryJCJC")
		@ResponseBody
		protected requestJson queryJCJC(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.queryJCJC(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "queryJJG")
		@ResponseBody
		protected requestJson queryJJG(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.queryJJG(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "queryZJXX")
		@ResponseBody
		protected requestJson queryZJXX(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.queryZJXX(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "querySJXM")
		@ResponseBody
		protected requestJson querySJXM(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.querySJXM(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "querySJBD")
		@ResponseBody
		protected requestJson querySJBD(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.querySJBD(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "querySJY")
		@ResponseBody
		protected requestJson querySJY(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.querySJY(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "queryJCLX")
		@ResponseBody
		protected requestJson queryJCLX(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.queryJCLX(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "querysjxms")
		@ResponseBody
		protected requestJson querysjxms(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.querysjxms(request);
			j.setMsg(resultVO);
			return j;
		}
		@RequestMapping(params = "queryzlsf")
		@ResponseBody
		protected requestJson queryzlsf(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.SjBzjkService.queryzlsf(request);
			j.setMsg(resultVO);
			return j;
		}
		
	}


