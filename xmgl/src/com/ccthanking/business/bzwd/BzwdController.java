package com.ccthanking.business.bzwd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.bzwd.service.BzwdService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;


@Controller
@RequestMapping("/BzwdController")
public class BzwdController {
	@Autowired
	private BzwdService service;
	/**
	 * 查询标准文档树形列表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryWdTree")
	@ResponseBody
	public void queryWdTree(HttpServletRequest request,HttpServletResponse response) throws Exception {
		requestJson j = new requestJson();
		String encode = request.getCharacterEncoding();
		String  resultVO = "";
		resultVO = this.service.queryWdTree(request,j.getMsg());
		response.setCharacterEncoding(encode);
		response.getWriter().print(resultVO);
	}
	/**
	 * 
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertBzwd")
	@ResponseBody
	protected requestJson insertBzwd(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.insertBzwd(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateBzwd")
	@ResponseBody
	protected requestJson updateBzwd(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.updateBzwd(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryOneById")
	@ResponseBody
	protected requestJson queryOneById(HttpServletRequest request,HttpServletResponse response) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryOneById(request);
		j.setMsg(resultVO);
		return j;
	}
}
