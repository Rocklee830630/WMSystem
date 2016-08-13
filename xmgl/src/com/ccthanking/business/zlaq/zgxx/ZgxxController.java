package com.ccthanking.business.zlaq.zgxx;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;

@Controller
@RequestMapping("/zlaq/zgxxController")
public class ZgxxController {

	@Autowired
	private ZgxxService ZgxxService;
	String domresult="";
	String resultVO="";
	/**
	 * 普通查询
	 */
	@RequestMapping(params = "query_zg")
	@ResponseBody
	public requestJson query_zg(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String flag="";
		domresult = this.ZgxxService.query_zg(json.getMsg(),user,flag);
		j.setMsg(domresult);
		return j;

	}
	/**
    * @param 通知新增
	 */
	@RequestMapping(params = "insert_zg")
	@ResponseBody
	protected requestJson insert_zg(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String ywid = request.getParameter("ywid");
		resultVO = this.ZgxxService.insert_zg(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
}

	/**
     * @param 通知修改
	 */

	@RequestMapping(params = "update_zg")
	@ResponseBody
	protected requestJson update_zg(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String ywid=request.getParameter("ywid");
		resultVO = this.ZgxxService.update_zg(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
}


	/**
    * @param 回复新增
	 */
	@RequestMapping(params = "insert_hf")
	@ResponseBody
	protected requestJson insert_hf(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String ywid = request.getParameter("ywid");
		String flag = request.getParameter("flag");
		resultVO = this.ZgxxService.insert_hf(json.getMsg(),user,ywid,flag);
		j.setMsg(resultVO);
		return j;
}

	
	/**
	 * @param 回复修改
	 */

	@RequestMapping(params = "update_hf")
	@ResponseBody
	protected requestJson update_hf(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String flag = request.getParameter("flag");
		String ywid = request.getParameter("ywid");
		resultVO = this.ZgxxService.update_hf(json.getMsg(),user,ywid,flag);
		j.setMsg(resultVO);
		return j;
}


	/**
    * @param 复查新增
	 */
	@RequestMapping(params = "insert_fc")
	@ResponseBody
	protected requestJson insert_fc(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String ywid = request.getParameter("ywid");
		resultVO = this.ZgxxService.insert_fc(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
}

	
	/**
	 * @param 复查修改
	 */

	@RequestMapping(params = "update_fc")
	@ResponseBody
	protected requestJson update_fc(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String ywid = request.getParameter("ywid");
		resultVO = this.ZgxxService.update_fc(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
}
	
	
	//项目名称联想查询
	@RequestMapping(params = "xmmcAutoQuery")
	@ResponseBody
	public List<autocomplete> xmmcAutoComplete(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.ZgxxService.xmmcAutoComplete(match,user);
		return list;
	}
}
