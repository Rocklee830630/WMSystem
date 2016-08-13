package com.ccthanking.business.zlaq.jcxx;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;

@Controller
@RequestMapping("/zlaq/jcxxController")
public class JcxxController {

	@Autowired
	private JcxxService JcxxService;
	String domresult="";
	String resultVO="";
	/**
	 * 普通查询
	 */
	@RequestMapping(params = "query_jc")
	@ResponseBody
	public requestJson query_jc(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String flag=request.getParameter("flag");
		String zt=request.getParameter("zt");
		domresult = this.JcxxService.query_jc(json.getMsg(),user,flag,zt);
		j.setMsg(domresult);
		return j;

	}
	
	
	/**
	 * 信息卡列表查询
	 */
	@RequestMapping(params = "query_jc_xxk")
	@ResponseBody
	public requestJson query_jc_xxk(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String jclx=request.getParameter("jclx");
		String id=request.getParameter("id");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.JcxxService.query_jc_xxk(json.getMsg(),user,jclx,id);
		j.setMsg(domresult);
		return j;

	}
	
	//质量安全部概况检查整改信息监控
	@RequestMapping(params = "jc_zg_xxtj")
	@ResponseBody
	public requestJson jc_zg_xxtj(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		domresult = this.JcxxService.jc_zg_xxtj(user,nd);
		j.setMsg(domresult);
		return j;

	}
	
	//各项目公司质量安全情况
	@RequestMapping(params = "zlaqqk")
	@ResponseBody
	public requestJson zlaqqk(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		domresult = this.JcxxService.zlaqqk(user,nd);
		j.setMsg(domresult);
		return j;

	}
	
	//涉及整改最多的施工单位(TOP5)
	@RequestMapping(params = "sgdw_top")
	@ResponseBody
	public requestJson sgdw_top(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		domresult = this.JcxxService.sgdw_top(user,nd);
		j.setMsg(domresult);
		return j;

	}
	
	//涉及整改最多的监理单位(TOP5)
	@RequestMapping(params = "jldw_top")
	@ResponseBody
	public requestJson jldw_top(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String nd=request.getParameter("nd");
		domresult = this.JcxxService.jldw_top(user,nd);
		j.setMsg(domresult);
		return j;

	}

	/**
	 * 生成检查编号
	 */
	@RequestMapping(params = "query_jcbh")
	@ResponseBody
	public requestJson query_jcbh(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String jclx=request.getParameter("jclx");
		String jcrq=request.getParameter("jcrq");
		String jc=request.getParameter("jc");
		domresult = this.JcxxService.query_jcbh(jclx,jcrq,jc);
		j.setMsg(domresult);
		return j;

	}
		

/**
     * @param 维护新增
	 */
	@RequestMapping(params = "insert_jc")
	@ResponseBody
	protected requestJson insert_jc(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String ywid=request.getParameter("ywid");
		String flag=request.getParameter("flag");
		resultVO = this.JcxxService.insert_jc(json.getMsg(),user,ywid,flag);
		j.setMsg(resultVO);
		return j;
}
	
	/**
     * @param 维护修改
	 */

	@RequestMapping(params = "update_jc")
	@ResponseBody
	protected requestJson update_jc(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String flag=request.getParameter("flag");
		String ywid=request.getParameter("ywid");
		resultVO = this.JcxxService.update_jc(json.getMsg(),user,flag,ywid);
		j.setMsg(resultVO);
		return j;
	}
	
	
	//删除检查信息
	@RequestMapping(params = "delete_jc")
	@ResponseBody
	protected requestJson delete_jc(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.JcxxService.delete_jc(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	

	//项目信息质量检查卡饼图
	@RequestMapping(params = "bt_zljc")
	@ResponseBody
	public requestJson bt_zljc(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String id=request.getParameter("id");
		domresult = this.JcxxService.bt_zljc(user,id);
		j.setMsg(domresult);
		return j;

	}
	

	//项目信息安全检查卡饼图
	@RequestMapping(params = "bt_aqjc")
	@ResponseBody
	public requestJson bt_aqjc(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		String id=request.getParameter("id");
		domresult = this.JcxxService.bt_aqjc(user,id);
		j.setMsg(domresult);
		return j;

	}
	
	
	//项目名称联想查询
	@RequestMapping(params = "xmmcAutoQuery")
	@ResponseBody
	public List<autocomplete> xmmcAutoComplete(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.JcxxService.xmmcAutoComplete(match,user);
		return list;
	}
	
	
	/**
	 * 根据ID查询信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryById")
	@ResponseBody
	public requestJson queryById(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.JcxxService.queryById(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
}
