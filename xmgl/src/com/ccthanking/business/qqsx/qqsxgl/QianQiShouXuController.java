/**
 * @author wangzh
 */
package com.ccthanking.business.qqsx.qqsxgl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.*;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/qianQiShouXuController")
public class QianQiShouXuController {

	
	@Autowired
	private QianQiShouXuService qianQiShouXuService;
/**
	 * 查询样例json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryQainQiShouXu")
	@ResponseBody
	public requestJson queryConditiondemo(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.qianQiShouXuService.queryConditionQianQiShouXu(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "insert")
	@ResponseBody
	protected requestJson insert(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String ywid=request.getParameter("ywid");
		String xmmc=request.getParameter("xmmc");
		String  resultVO = "";
		resultVO = this.qianQiShouXuService.insert(request,json.getMsg(),user,ywid,xmmc);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "update")
	@ResponseBody
	protected requestJson updatedemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String xmmc=request.getParameter("xmmc");
		String  resultVO = "";
		resultVO = this.qianQiShouXuService.updatedemo(request,json.getMsg(),user,xmmc);
		j.setMsg(resultVO);
		return j;
	}
	//查询
	@RequestMapping(params = "querySxfjzj")
	@ResponseBody
	public requestJson querySxfjzj(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String dfl=request.getParameter("dfl");
		String  domresult = "";
		domresult = this.qianQiShouXuService.querySxfjzj(request,json.getMsg(),dfl );
		j.setMsg(domresult);
		return j;
	}
	/*@RequestMapping(params = "queryXiangMu")
	@ResponseBody
	public requestJson queryHeTong(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.qianQiShouXuService.queryConditionXiangMu(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "insertlxky")
	@ResponseBody
	protected requestJson insertlxky(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.qianQiShouXuService.insertlxky(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "insertsgxk")
	@ResponseBody
	protected requestJson insertsgxk(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.qianQiShouXuService.insertsgxk(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "inserttdsp")
	@ResponseBody
	protected requestJson inserttdsp(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.qianQiShouXuService.inserttdsp(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "insertghsp")
	@ResponseBody
	protected requestJson insertghsp(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.qianQiShouXuService.insertghsp(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "queryLXKY")
	@ResponseBody
	public requestJson queryLXKY(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.qianQiShouXuService.queryLXKY(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "querySGXK")
	@ResponseBody
	public requestJson querySGXK(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.qianQiShouXuService.querySGXK(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "queryTDSP")
	@ResponseBody
	public requestJson queryTDSP(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.qianQiShouXuService.queryTDSP(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "queryGHSP")
	@ResponseBody
	public requestJson queryGHSP(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.qianQiShouXuService.queryGHSP(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
*/
}
