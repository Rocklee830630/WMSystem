package com.ccthanking.business.xmcbk.xmxd;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
@Controller
@RequestMapping("/xmcbk/xmcbkxdController")
public class XmcbkxdController {
	@Autowired
	private XmcbkxdService xmcbkxdService;
	String  domresult = "";
	/**
	 * 查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_jzxd")
	@ResponseBody
	public requestJson query_jzxd(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String ids=request.getParameter("ids");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.xmcbkxdService.query_jzxd(json.getMsg(),user,ids);
		j.setMsg(domresult);
		return j;

	}
	

	/**
	 * 最大pch查询
	 */
	@RequestMapping(params = "query_maxpch")
	@ResponseBody
	public requestJson query_maxpch(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String xdlx=request.getParameter("xdlx");
		String xdnf=request.getParameter("xdnf");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.xmcbkxdService.query_maxpch(json.getMsg(),user,xdlx,xdnf);
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 暂存查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_zc")
	@ResponseBody
	public requestJson query_zc(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.xmcbkxdService.query_zc(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 审批信息询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_sp_info")
	@ResponseBody
	public requestJson query_sp_info(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.xmcbkxdService.query_sp_info(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 审批项目查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_sp")
	@ResponseBody
	public requestJson query_sp(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		String  spxxid=request.getParameter("spxxid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.xmcbkxdService.query_sp(json.getMsg(),user,spxxid);
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 更新审批状态son
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "update_spzt")
	@ResponseBody
	public requestJson update_spzt(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		String  sjbh=request.getParameter("sjbh");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.xmcbkxdService.update_spzt(json.getMsg(),user,sjbh);
		j.setMsg(domresult);
		return j;

	}
	/**
     * @param 插入审批信息
	 * @return org.springframework.web.servlet.ModelAndView
	 */
	@RequestMapping(params = "insert_sp_info")
	@ResponseBody
	protected requestJson insert_sp_info(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmcbkxdService.insert_sp_info(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	/**
     * @param 插入审批项目
	 * @return org.springframework.web.servlet.ModelAndView
	 */
	@RequestMapping(params = "insert_sp")
	@ResponseBody
	protected requestJson insert_sp(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		String ids=request.getParameter("ids");
		String spxxid=request.getParameter("spxxid");
		resultVO = this.xmcbkxdService.insert_sp(json.getMsg(),user,ids,spxxid);
		j.setMsg(resultVO);
		return j;
	}
	/**
     * @param 插入下达项目
	 * @return org.springframework.web.servlet.ModelAndView
	 */
	@RequestMapping(params = "insert_xd")
	@ResponseBody
	protected requestJson insert_xd(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		//ids是即将下达的项目数据的主键字符串，以逗号分隔，当数据量较大时，以get方式传值会失败，改为通过json对象获取
//		String ids=request.getParameter("ids");
		String msg = json.getMsg();
		JSONObject obj = JSONObject.fromObject(msg);
		JSONObject response = (JSONObject)obj.get("response");
		JSONArray data = (JSONArray)response.get("data");
		JSONObject row = (JSONObject)data.get(0);
		String ids = (String)row.get("ids");
		resultVO = this.xmcbkxdService.insert_xd(json.getMsg(),user,ids,request);
		j.setMsg(resultVO);
		return j;
	}
	/**
     * @param 暂存插入
	 * @return org.springframework.web.servlet.ModelAndView
	 */
	@RequestMapping(params = "insert_zc")
	@ResponseBody
	protected requestJson insert_zc(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		String ids=request.getParameter("ids");
		String dczlx=request.getParameter("dczlx");
		resultVO = this.xmcbkxdService.insert_zc(json.getMsg(),user,ids,dczlx);
		j.setMsg(resultVO);
		return j;
	}
/*		
	/**
     * @param 结转插入
	 * @return org.springframework.web.servlet.ModelAndView
	 */

	@RequestMapping(params = "insert_jz")
	@ResponseBody
	protected requestJson insert_jz(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		String ids=request.getParameter("ids");
		String year=request.getParameter("year");
		resultVO = this.xmcbkxdService.insert_jz(json.getMsg(),user,ids,year);
		j.setMsg(resultVO);
		return j;
	}
	/**
     * @param 审批信息修改
	 * @return org.springframework.web.servlet.ModelAndView
	 */
	@RequestMapping(params = "update_sp_info")
	@ResponseBody
	protected requestJson update_sp_info(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		String ywid=request.getParameter("ywid");
		resultVO = this.xmcbkxdService.update_sp_info(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	/**
     * @param 删除审批项目
	 * @return org.springframework.web.servlet.ModelAndView
	 */
	@RequestMapping(params = "delete_sp")
	@ResponseBody
	protected requestJson delete_sp(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		String cbkid=request.getParameter("cbkid");
		resultVO = this.xmcbkxdService.delete_sp(json.getMsg(),user,cbkid);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 通过
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "xmSptg")
	@ResponseBody
	protected requestJson xmSptg(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmcbkxdService.xmSptg(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 验证项目年度是否统一
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "verificationXmnd")
	@ResponseBody
	protected requestJson verificationXmnd(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmcbkxdService.verificationXmnd(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 删除审批
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "deleteSp")
	@ResponseBody
	protected requestJson deleteSp(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmcbkxdService.deleteSp(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
}
