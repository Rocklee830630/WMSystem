/**
 * @author zhaiyl
 */
package com.ccthanking.business.bgh;

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
@RequestMapping("/banGongHuiWenTiController")
public class BanGongHuiWenTiController {

	
	@Autowired
	private BanGongHuiWenTiService banGongHuiWenTiService;
/**
	 * json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "querybanGongHuiWen")
	@ResponseBody
	public requestJson querybanGongHuiWen(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.banGongHuiWenTiService.querybanGongHuiWen(json.getMsg(),user);
		j.setMsg(domresult);
		return j;
	}
	@RequestMapping(params = "insertBanGongHuiWenTi")
	@ResponseBody
	protected requestJson insertBanGongHuiWenTi(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.banGongHuiWenTiService.insertBanGongHuiWenTi(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateBanGongHuiWenTi")
	@ResponseBody
	protected requestJson updateBanGongHuiWenTi(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.banGongHuiWenTiService.updateBanGongHuiWenTi(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "deleteGongHuiWenTi")
	@ResponseBody
	protected requestJson deleteGongHuiWenTi(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.banGongHuiWenTiService.deleteGongHuiWenTi(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updatebatchdata")
	@ResponseBody
	protected requestJson updatebatchdata(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.banGongHuiWenTiService.updatebatchdata(json.getMsg(),user,request);
		j.setMsg(resultVO);
		return j;
	}
}
