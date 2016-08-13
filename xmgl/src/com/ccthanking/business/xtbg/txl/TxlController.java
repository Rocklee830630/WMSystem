package com.ccthanking.business.xtbg.txl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.business.xtbg.txl.service.TxlService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/txlController")
public class TxlController {

	@Autowired
	private TxlService txlService;
	
	/**
	 * 查询个人通讯录
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryPrivateTxl")
	@ResponseBody
	public requestJson queryPrivateTxl(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = txlService.queryPrivateTxl(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	

	/**
	 * 修改个人通讯录：包括添加、修改、删除，分别用1、2、3表示
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "executePrivateTxl")
	@ResponseBody
	protected requestJson executePrivateTxl(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String id = request.getParameter("id");
		String exeSign = request.getParameter("exeSign");
		try {
			String domResult = this.txlService.executePrivateTxl(json.getMsg(), user, id, exeSign);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 查询公共通讯录
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryPublicTxl")
	@ResponseBody
	public requestJson queryPublicTxl(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = txlService.queryPublicTxl(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	

	/**
	 * 修改公共通讯录：包括添加、修改、删除，分别用1、2、3表示
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "executePublicTxl")
	@ResponseBody
	protected requestJson executePublicTxl(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String id = request.getParameter("id");
		String exeSign = request.getParameter("exeSign");
		try {
			String domResult = this.txlService.executePublicTxl(json.getMsg(), user, id, exeSign);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	

	@RequestMapping(params = "queryTxlGroup")
	@ResponseBody
	public requestJson queryTxlGroup(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String isPublic = request.getParameter("isPublic");
		try {
			String domResult = txlService.queryTxlGroup(json.getMsg(), user, isPublic);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 修改公共通讯录（组）：包括添加、修改、删除，分别用1、2、3表示
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "executeTxlGroup")
	@ResponseBody
	public requestJson executeTxlGroup(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String id = request.getParameter("id");
		String isPublic = request.getParameter("isPublic");
		try {
			String domResult = this.txlService.executeTxlGroup(json.getMsg(), user, id, isPublic);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "deleteTxlGroup")
	@ResponseBody
	public requestJson deleteTxlGroup(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = this.txlService.deleteTxlGroup(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
}
