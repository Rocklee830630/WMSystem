package com.ccthanking.business.xdxmk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xdxmk.service.XdxmkService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Pub;

/**
 * @author sunjl
 * @time 2013-7-23
 */
@Controller
@RequestMapping("/xdxmk/xdxmkController")
public class XdxmkController {
	
	@Autowired
	private XdxmkService xdxmkService;

	@RequestMapping(params = "insert")
	@ResponseBody
	protected requestJson insert(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xdxmkService.insert(json.getMsg(),user,request);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query")
	@ResponseBody
	public requestJson queryCondition(final HttpServletRequest request,requestJson json) throws Exception
	{
		String ids = request.getParameter("ids");
		if(Pub.empty(ids)){
			ids = "";
		}else{
			ids = ids.substring(0,ids.length()-1);
		}
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.query(json.getMsg(),user,ids,request);
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXx")
	@ResponseBody
	public requestJson queryXx(final HttpServletRequest request,requestJson json) throws Exception
	{
		
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.queryXx(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_bd")
	@ResponseBody
	public requestJson query_bd(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.query_bd(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_bdxj")
	@ResponseBody
	public requestJson query_bdxj(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.query_bdxj(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;
	}
	
	/**
	 * 查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "query_countbd")
	@ResponseBody
	public requestJson query_countbd(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.query_countbd(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;
	}
	
	/**
	 * 下达统筹计划json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertJhAxm")
	@ResponseBody
	protected requestJson insertJhAxm(final HttpServletRequest request,requestJson json) throws Exception {
		String ids = request.getParameter("ids");
		String ywid = request.getParameter("ywid");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xdxmkService.insertJhAxm(json.getMsg(),user,ids,ywid);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 根据ID查询�json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryById")
	@ResponseBody
	protected requestJson queryById(final HttpServletRequest request,String json) throws Exception {
		String id = (String) request.getParameter("id");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xdxmkService.queryById(id,user);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 添加计划
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertJhApc")
	@ResponseBody
	protected requestJson insertJhApc(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xdxmkService.insertJhApc(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 添加待结转信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertDjz")
	@ResponseBody
	protected requestJson insertDjz(final HttpServletRequest request,requestJson json) throws Exception {
		String ids = (String) request.getParameter("ids");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xdxmkService.insertDjz(json.getMsg(),user,ids);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 查询结转json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryDjz")
	@ResponseBody
	public requestJson queryDjz(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.queryDjz(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}

	/**
	 * 项目名称自动查询
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "xmmcAutoCompleteToXmxdk")
	@ResponseBody
	public List<autocomplete> xmmcAutoCompleteToXmxdk(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.xdxmkService.xmmcAutoCompleteToXmxdk(match,user);
		return list;
	}
	
	/**
	 * 查询最大批次号
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryMaxPch")
	@ResponseBody
	public requestJson queryMaxPch(final HttpServletRequest request) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String xflx = request.getParameter("xflx");
		String nd = request.getParameter("nd");
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.queryMaxPch(xflx,nd,user);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 查询合同信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryHTSJ")
	@ResponseBody
	public requestJson queryHTSJ(final HttpServletRequest request,requestJson json) throws Exception
	{
		String htlx = request.getParameter("htlx");
		String id = request.getParameter("id");
		String bmid = request.getParameter("bmid");
		
		Map map = new HashMap<String, String>();
		map.put("id", id);
		map.put("htlx", htlx);
		map.put("bmid", bmid);
		
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.queryHTSJ(json.getMsg(),user,map);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 查询数量、金额
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryHtSum")
	@ResponseBody
	public requestJson queryHtSum(final HttpServletRequest request,requestJson json) throws Exception
	{
		String htlx = request.getParameter("htlx");
		String id = request.getParameter("id");
		String bmid = request.getParameter("bmid");
		
		Map map = new HashMap<String, String>();
		map.put("id", id);
		map.put("htlx", htlx);
		map.put("bmid", bmid);
		
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.queryHtSum(json.getMsg(),user,map);
		j.setMsg(domresult);
		return j;

	}
	//==================================================新增审批对应方法====================================================
	/**
	 * 查询统筹计划批次表,下发类型区分，只过滤未下发到统筹计划的批次
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJhpc")
	@ResponseBody
	public requestJson queryJhpc(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.queryJhpc(json.getMsg(),request);
		j.setMsg(domresult);
		return j;
	}
	
	/**
	 * 新增计划_审批
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertJhsp")
	@ResponseBody
	protected requestJson insertJhsp(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xdxmkService.insertJhsp(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 根据批次号查询数据
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXmxxByPch")
	@ResponseBody
	public requestJson queryXmxxByPch(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.queryXmxxByPch(json.getMsg(),request);
		j.setMsg(domresult);
		return j;
	}
	
	/**
	 * 新增下达库审批表
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertXmxdkSp")
	@ResponseBody
	protected requestJson insertXmxdkSp(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xdxmkService.insertXmxdkSp(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 删除下达库审批表
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "deleteXdkxmSp")
	@ResponseBody
	protected requestJson deleteXdkxmSp(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xdxmkService.deleteXdkxmSp(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 根据事件编号查询下达库审批表，用于领导审批时查看
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXmxxSp")
	@ResponseBody
	public requestJson queryXmxxSp(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.queryXmxxSp(json.getMsg(),request);
		j.setMsg(domresult);
		return j;
	}
	
	/**
	 * 新增下达库审批表按项目
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertXmxdSpAxm")
	@ResponseBody
	protected requestJson insertXmxdSpAxm(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xdxmkService.insertXmxdSpAxm(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	
	/**
	 * 查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryApc")
	@ResponseBody
	public requestJson queryApc(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.queryApc(json.getMsg(),request);
		j.setMsg(domresult);
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
	public requestJson verificationXmnd(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.verificationXmnd(json.getMsg(),request);
		j.setMsg(domresult);
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
	public requestJson xmSptg(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.xdxmkService.xmSptg(json.getMsg(),request);
		j.setMsg(domresult);
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
		resultVO = this.xdxmkService.deleteSp(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
}
