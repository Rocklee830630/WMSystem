package com.ccthanking.business.wttb;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.wttb.service.WttbService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Pub;

@Controller
@RequestMapping("/wttb/wttbController")
public class WttbController {

	@Autowired
	private WttbService service;
	
	@RequestMapping(params = "insertInfo")
	@ResponseBody
	public requestJson insertInfo(HttpServletRequest request,requestJson js) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.insertInfo(js.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 有些数据无法同信息表的数据一起查询出来，所以添加这个方法，根据问题编号查询
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "querySomeData")
	@ResponseBody
	public requestJson querySomeData(HttpServletRequest request,requestJson js) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String wtid = request.getParameter("id");
		String  resultVO = "";
		resultVO = this.service.querySomeData(wtid,user,null);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 退回问题提报
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doSendBack")
	@ResponseBody
	public requestJson doSendBack(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doSendBack(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 撤销问题提报
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doRevoke")
	@ResponseBody
	public requestJson doRevoke(HttpServletRequest request,requestJson js) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doRevoke(js.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 完成批复意见
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doSugg")
	@ResponseBody
	public requestJson doSugg(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doSugg(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 问题发起人确认
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doConfirm")
	@ResponseBody
	public requestJson doConfirm(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doConfirm(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 问题催办
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doUrge")
	@ResponseBody
	public requestJson doUrge(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doUrge(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 补充批复意见
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doSupplySugg")
	@ResponseBody
	public requestJson doSupplySugg(HttpServletRequest request,requestJson js) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doSupplySugg(js.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 转发
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doTransfer")
	@ResponseBody
	public requestJson doTransfer(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doTransfer(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	
	/**
	 * 查询接收人列表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryPsnList")
	@ResponseBody
	public requestJson queryPsnList(HttpServletRequest request,requestJson js) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryPsnList(js.getMsg(),user,null);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 发送问题提报
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "sendReport")
	@ResponseBody
	public requestJson sendReport(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.sendReport(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 发送问题提报
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doResend")
	@ResponseBody
	public requestJson doResend(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doResend(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 发起人查询问题提报信息主表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryInfoFqr")
	@ResponseBody
	public requestJson queryInfoFqr(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryInfoFqr(js.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 接收人查询问题提报信息主表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryInfoJsr")
	@ResponseBody
	public requestJson queryInfoJsr(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryInfoJsr(js.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 办理人查询问题提报信息主表
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryInfoBlr")
	@ResponseBody
	public requestJson queryInfoBlr(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryInfoBlr(js.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询已处理的问题
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryInfoYcl")
	@ResponseBody
	public requestJson queryInfoYcl(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryInfoYcl(js.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询流转历史
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryLzls")
	@ResponseBody
	public requestJson queryLzls(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryLzls(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询批复情况
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryPfqk")
	@ResponseBody
	public requestJson queryPfqk(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryPfqk(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取提示颜色
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getColor")
	@ResponseBody
	public requestJson getColor(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.getColor(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 部长确认发送
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doConfirmSend")
	@ResponseBody
	public requestJson doConfirmSend(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doConfirmSend(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 通过问题主键查询问题信息
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryInfoByWtid")
	@ResponseBody
	public requestJson queryInfoByWtid(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String wtid = request.getParameter("wtid");
		String  resultVO = "";
		resultVO = this.service.queryInfoByWtid(request,wtid);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询部门领导
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryLeader")
	@ResponseBody
	public requestJson queryLeader(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryLeader(request,j.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询部门领导
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryLeaderFlag")
	@ResponseBody
	public requestJson queryLeaderFlag(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryLeaderFlag(request,j.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询当前人对当前问题的办理角色
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryBlrjs")
	@ResponseBody
	public requestJson queryBlrjs(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.queryBlrjs(request,j.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询当前人有多少个需要办理的问题，包括主送和抄送，但是已经解决的不算
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getWtCounts")
	@ResponseBody
	public requestJson getWtCounts(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.getWtCounts(request,j.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询当前人有多少个需要办理的问题，包括主送和抄送，但是已经解决的不算
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getWtList")
	@ResponseBody
	public requestJson getWtList(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.getWtList(request,j.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询当前人有多少个需要办理的问题，包括主送和抄送，但是已经解决的不算
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getUserNameByAccount")
	@ResponseBody
	public requestJson getUserNameByAccount(HttpServletRequest request,requestJson js) throws Exception {
		String account = request.getParameter("account");
		String userName = Pub.getUserNameByLoginId(account);
		requestJson j = new requestJson();
		j.setMsg(userName);
		return j;
	}
	/**
	 * 问题提报删除方法
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "deleteWttb")
	@ResponseBody
	public requestJson deleteWttb(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.deleteWttb(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 提交到会议中心
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doSendToHyzx")
	@ResponseBody
	public requestJson doSendToHyzx(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.doSendToHyzx(js.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 获取消息接收人
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getAccepter")
	@ResponseBody
	public requestJson getAccepter(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.service.getAccepter(js.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
}
