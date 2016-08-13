package com.ccthanking.business.lcgl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;


@Controller
@RequestMapping("/lcglController")
public class LcglController {
	@Autowired
	private LcglService lcglService;
	
	@RequestMapping(params = "queryFlowApply")
	@ResponseBody
	protected requestJson queryFlowApply(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = this.lcglService.queryLcInfo(request, json.getMsg());
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "queryFlowApplyByPerson")
	@ResponseBody
	protected requestJson queryFlowApplyByPerson(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = this.lcglService.queryFlowApplyByPerson(request, json.getMsg(),user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/*
	 * 查询流程配置表
	 */
	@RequestMapping(params = "queryProcessType")
	@ResponseBody
	protected requestJson queryProcessType(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = this.lcglService.queryProcessType(request, json.getMsg());
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	
	/*
	 * 查询流程节点表
	 */
	@RequestMapping(params = "queryProcessStep")
	@ResponseBody
	protected requestJson queryProcessStep(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = this.lcglService.queryProcessStep(request, json.getMsg());
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/*
	 * 保存流程节点表
	 */
	@RequestMapping(params = "saveProcess")
	@ResponseBody
	protected requestJson saveProcess(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = this.lcglService.saveProcess(request, json.getMsg(),user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/*
	 * 保存流程节点表
	 */
	@RequestMapping(params = "deleteProcess")
	@ResponseBody
	protected requestJson deleteProcess(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = this.lcglService.deleteProcess(json.getMsg(),user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/*
	 * 查询流程节点表
	 */
	@RequestMapping(params = "queryProcessWs")
	@ResponseBody
	protected requestJson queryProcessWs(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String processoid = request.getParameter("processoid");
		try {
			String domResult = this.lcglService.queryProcessWs(request, processoid);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/*
	 * 保存文书表
	 */
	@RequestMapping(params = "saveApwstypz")
	@ResponseBody
	protected requestJson saveApwstypz(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = this.lcglService.saveApwstypz(request, json.getMsg(),user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/*
	 * 查询流程待办信息
	 */

	@RequestMapping(params = "queryProcessTask")
	@ResponseBody
	protected requestJson queryProcessTask(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String processoid = request.getParameter("processoid");
		try {
			String[] domResult = this.lcglService.queryProcessTask(request, processoid);
			j.setMsg(domResult[0]);
			j.setPrompt(domResult[1]);
		    j.setObj((String)domResult[2]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	
}
