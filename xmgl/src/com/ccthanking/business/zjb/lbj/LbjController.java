package com.ccthanking.business.zjb.lbj;

/**
 * @author zhaiyl
 */

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/zjb/lbj/lbjController")
public class LbjController {

	
	@Autowired
	private LbjService lbjService;
	/**
	 * 结算list
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryLbj")
	@ResponseBody
	public requestJson queryConditionLbj(HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		//domresult = this.lbjService.queryConditionLbj(json.getMsg(),user);
        String sjwybh=request.getParameter("sjwybh");
        String nd=request.getParameter("nd");
        String IsNull=request.getParameter("IsNull");
        
		if(sjwybh==null||"".equals(sjwybh))
		{
			domresult = this.lbjService.queryConditionLbj(json.getMsg(),user,IsNull);
		}
		else{
			String  data="{querycondition: {conditions: [{\"value\":\""+sjwybh+"\",\"fieldname\":\"jhsj.sjwybh\",\"operation\":\"=\",\"logic\":\"and\"},{\"value\":\""+nd+"\",\"fieldname\":\"jhsj.nd\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			domresult = this.lbjService.queryConditionLbj(data,user,IsNull);
		}
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "insertLbj")
	@ResponseBody
	protected requestJson insertLbj(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.lbjService.insertLbj(request,json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateLbj")
	@ResponseBody
	protected requestJson updateLbj(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.lbjService.updateLbj(request,json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateZt")
	@ResponseBody
	protected requestJson updateZt(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.lbjService.updateZt(request,json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
}

