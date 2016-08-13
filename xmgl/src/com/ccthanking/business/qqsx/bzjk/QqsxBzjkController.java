package com.ccthanking.business.qqsx.bzjk;

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
	@RequestMapping("qqsx/bzgkController")
	public class QqsxBzjkController {
		@Autowired
		private QqsxBzjkService QqsxBzjkService;
		/**
		 * 各手续情况
		 */
		@RequestMapping(params = "querySx")
		@ResponseBody
		protected requestJson querySx(HttpServletRequest request,HttpServletResponse response) throws Exception {
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.QqsxBzjkService.querySx(request);
			j.setMsg(resultVO);
			return j;
		}

		@RequestMapping(params = "querySxZxtColumn2d")
		@ResponseBody
		public requestJson querySxZxtColumn2d(HttpServletRequest request,requestJson js) throws Exception {
			requestJson j = new requestJson();
			String resultVO = this.QqsxBzjkService.querySxZxtColumn2d(request,js.getMsg());
			j.setMsg(resultVO);
			return j;
		}
		/**
		 * 施工进展情况
		 */
		@RequestMapping(params = "querySg")
		@ResponseBody
		protected requestJson querySg(HttpServletRequest request,HttpServletResponse response) throws Exception {
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.QqsxBzjkService.querySg(request);
			j.setMsg(resultVO);
			return j;
		}

		@RequestMapping(params = "querySgZxtColumn2d")
		@ResponseBody
		public requestJson querySgZxtColumn2d(HttpServletRequest request,requestJson js) throws Exception {
			requestJson j = new requestJson();
			String resultVO = this.QqsxBzjkService.querySgZxtColumn2d(request,js.getMsg());
			j.setMsg(resultVO);
			return j;
		}
	
		/**
		 * 立项进展情况
		 */
		@RequestMapping(params = "queryLx")
		@ResponseBody
		protected requestJson queryLx(HttpServletRequest request,HttpServletResponse response) throws Exception {
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.QqsxBzjkService.queryLx(request);
			j.setMsg(resultVO);
			return j;
		}

		@RequestMapping(params = "queryLxZxtColumn2d")
		@ResponseBody
		public requestJson queryLxZxtColumn2d(HttpServletRequest request,requestJson js) throws Exception {
			requestJson j = new requestJson();
			String resultVO = this.QqsxBzjkService.queryLxZxtColumn2d(request,js.getMsg());
			j.setMsg(resultVO);
			return j;
		}
	
		/**
		 * 土地进展情况
		 */
		@RequestMapping(params = "queryTd")
		@ResponseBody
		protected requestJson queryTd(HttpServletRequest request,HttpServletResponse response) throws Exception {
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.QqsxBzjkService.queryTd(request);
			j.setMsg(resultVO);
			return j;
		}

		@RequestMapping(params = "queryTdZxtColumn2d")
		@ResponseBody
		public requestJson queryTdZxtColumn2d(HttpServletRequest request,requestJson js) throws Exception {
			requestJson j = new requestJson();
			String resultVO = this.QqsxBzjkService.queryTdZxtColumn2d(request,js.getMsg());
			j.setMsg(resultVO);
			return j;
		}
		
		/**
		 * 规划进展情况
		 */
		@RequestMapping(params = "queryGh")
		@ResponseBody
		protected requestJson queryGh(HttpServletRequest request,HttpServletResponse response) throws Exception {
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.QqsxBzjkService.queryGh(request);
			j.setMsg(resultVO);
			return j;
		}

		@RequestMapping(params = "queryGhZxtColumn2d")
		@ResponseBody
		public requestJson queryGhZxtColumn2d(HttpServletRequest request,requestJson js) throws Exception {
			requestJson j = new requestJson();
			String resultVO = this.QqsxBzjkService.queryGhZxtColumn2d(request,js.getMsg());
			j.setMsg(resultVO);
			return j;
		}
		/**
		 * 时间信息查询
		 */
		@RequestMapping(params = "queryDate")
		@ResponseBody
		protected requestJson queryDate(HttpServletRequest request,HttpServletResponse response) throws Exception {
			requestJson j = new requestJson();
			String  resultVO = "";
			resultVO = this.QqsxBzjkService.queryDate(request);
			j.setMsg(resultVO);
			return j;
		}
		/**
		 * 计数信息查询
		 */
		@RequestMapping(params = "queryCount")
		@ResponseBody
		protected requestJson queryCount(HttpServletRequest request,HttpServletResponse response) throws Exception {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			requestJson j = new requestJson();
			String nd=request.getParameter("nd");
			String  resultVO = "";
			resultVO = this.QqsxBzjkService.queryCount(user,nd);
			j.setMsg(resultVO);
			return j;
		}
		
		/**
		 * 整体情况
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(params = "queryZtqk")
		@ResponseBody
		protected requestJson queryZtqk(HttpServletRequest request,HttpServletResponse response) throws Exception {
			requestJson j = new requestJson();
			String resultVO = this.QqsxBzjkService.queryZtqk(request);
			j.setMsg(resultVO);
			return j;
		}
		/**
		 * 导视图1
		 */
		@RequestMapping(params = "queryQqsx_DST1")
		@ResponseBody
		protected requestJson queryQqsx_DST1(HttpServletRequest request,HttpServletResponse response) throws Exception {
			requestJson j = new requestJson();
			String resultVO = this.QqsxBzjkService.queryQqsx_DST1(request);
			j.setMsg(resultVO);
			return j;
		}
		/**
		 * 导视图2
		 */
		@RequestMapping(params = "queryQqsx_DST2")
		@ResponseBody
		protected requestJson queryQqsx_DST2(HttpServletRequest request,HttpServletResponse response) throws Exception {
			requestJson j = new requestJson();
			String resultVO = this.QqsxBzjkService.queryQqsx_DST2(request);
			j.setMsg(resultVO);
			return j;
		}
	}


