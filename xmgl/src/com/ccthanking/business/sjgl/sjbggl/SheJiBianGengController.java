/**
 * @author wangzh
 */
package com.ccthanking.business.sjgl.sjbggl;

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
@RequestMapping("/sheJiBianGengService")
public class SheJiBianGengController {

	
	@Autowired
	private SheJiBianGengService sheJiBianGengService;
/**
	 * 查询样例json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryAllLList")
	@ResponseBody
	public requestJson queryConditiondemo(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		String sjwybh=request.getParameter("sjwybh");
		String nd=request.getParameter("nd");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.sheJiBianGengService.queryConditionSheJiBianGeng(json.getMsg(),sjwybh,nd,user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "insert")
	@ResponseBody
	protected requestJson insert(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		String ywid=request.getParameter("ywid");
		resultVO = this.sheJiBianGengService.insert(request,json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "update")
	@ResponseBody
	protected requestJson updatedemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sheJiBianGengService.updatedemo(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "insertLingQu")
	@ResponseBody
	protected requestJson insertLingQu(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sheJiBianGengService.insertLingQu(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateLingQu")
	@ResponseBody
	protected requestJson updateLingQu(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sheJiBianGengService.updateLingQu(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "queryAllShouQu")
	@ResponseBody
	public requestJson queryAllShouQu(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		String SJBGID=request.getParameter("SJBGID");
        User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		domresult = this.sheJiBianGengService.queryConditionShouQu(json.getMsg(),SJBGID,user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "deleteJS")
	@ResponseBody
	protected requestJson deleteJS(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sheJiBianGengService.deleteJS(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "deleteLQ")
	@ResponseBody
	protected requestJson deleteLQ(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.sheJiBianGengService.deleteLQ(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}
