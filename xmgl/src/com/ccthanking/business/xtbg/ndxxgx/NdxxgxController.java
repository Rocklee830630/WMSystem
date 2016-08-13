/**
 * @author zhaiyl
 */
package com.ccthanking.business.xtbg.ndxxgx;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.*;

/**
 * @author zhaiyl
 */
@Controller
@RequestMapping("/ndxxgxController")
public class NdxxgxController {

	
	@Autowired
	private NdxxgxService ndxxgxService;
/**
	 * 查询样例json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryNdxxgx")
	@ResponseBody
	public requestJson queryNdxxgx(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.ndxxgxService.queryNdxxgx(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "queryMoreNdxxgx")
	@ResponseBody
	public requestJson queryMoreNdxxgx(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.ndxxgxService.queryMoreNdxxgx(json.getMsg(),request);
		j.setMsg(domresult);
		return j;
	}
	
	@RequestMapping(params = "insertNdxxgx")
	@ResponseBody
	protected requestJson insertNdxxgx(HttpServletRequest request,requestJson json) throws Exception {
		String ywid=request.getParameter("ywid");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.ndxxgxService.insertNdxxgx(json.getMsg(),user,ywid);
		j.setMsg(resultVO);
		return j;
	}
	
	
	@RequestMapping(params = "updateNdxxgx")
	@ResponseBody
	protected requestJson updateNdxxgx(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.ndxxgxService.updateNdxxgx(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}

	@RequestMapping(params = "deleteNdxxgx")
	@ResponseBody
	protected requestJson deleteNdxxgx(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.ndxxgxService.deleteNdxxgx(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}


}
