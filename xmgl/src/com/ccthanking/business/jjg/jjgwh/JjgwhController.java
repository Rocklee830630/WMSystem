package com.ccthanking.business.jjg.jjgwh;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.bdhf.bdwh.BdwhService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;


@Controller
@RequestMapping("/jjg/jjgwhController")
public class JjgwhController {
	
	@Autowired
	private JjgwhService JjgwhService;
	String domresult="";
	String resultVO="";
	/**
	 * 普通查询
	 */
	@RequestMapping(params = "query_jjg")
	@ResponseBody
	public requestJson query_jjg(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String iswg=request.getParameter("iswg");
		String tbjjg=request.getParameter("tbjjg");
		String jhsjid=request.getParameter("jhsjid");
		if(jhsjid==null||"".equals(jhsjid))
		{
			domresult = this.JjgwhService.query_jjg(json.getMsg(),user,iswg,tbjjg);
		}
		else{
			String  data="{querycondition: {conditions: [{\"value\":\""+jhsjid+"\",\"fieldname\":\"sj.gc_jh_sj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			domresult = this.JjgwhService.query_jjg(data,user,iswg,tbjjg);
		}
		j.setMsg(domresult);
		return j;

	}
	//项目信息查询
	@RequestMapping(params = "query_xmxx")
	@ResponseBody
	public requestJson queryHeTong(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.JjgwhService.query_xmxx(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}

	
	/**
     * @param 新增
	 */
	@RequestMapping(params = "insert_jjg")
	@ResponseBody
	protected requestJson insert_jjg(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String ywid = request.getParameter("ywid");
		resultVO = this.JjgwhService.insert_jjg(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
}
	


	/**
     * @param 维护修改
	 */

	@RequestMapping(params = "update_jjg")
	@ResponseBody
	protected requestJson update_jjg(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		resultVO = this.JjgwhService.update_jjg(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
}


}
