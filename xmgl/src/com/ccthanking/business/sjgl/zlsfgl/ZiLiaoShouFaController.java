/**
 * @author wangzh
 */
package com.ccthanking.business.sjgl.zlsfgl;

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
@RequestMapping("/ziLiaoShouFaController")
public class ZiLiaoShouFaController {

	
	@Autowired
	private ZiLiaoShouFaService ziLiaoShouFaService;
/**
	 * 接收查询
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
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String sjwybh=request.getParameter("sjwybh");
		String nd=request.getParameter("nd");
		domresult = this.ziLiaoShouFaService.queryConditionZiLiaoShouFa(json.getMsg(),sjwybh,nd,user);
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 导出查询
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJSLQ")
	@ResponseBody
	public requestJson jSLQquery(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String JHSJID=request.getParameter("JHSJID");
		domresult = this.ziLiaoShouFaService.jSLQquery(json.getMsg(),JHSJID,user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "insert")
	@ResponseBody
	protected requestJson insert(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.ziLiaoShouFaService.insert(request,json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "update")
	@ResponseBody
	protected requestJson updatedemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.ziLiaoShouFaService.updatedemo(request,json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "insertLingQu")
	@ResponseBody
	protected requestJson insertLingQu(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.ziLiaoShouFaService.insertLingQu(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updateLingQu")
	@ResponseBody
	protected requestJson updateLingQu(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.ziLiaoShouFaService.updateLingQu(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "queryAllShouQu")
	@ResponseBody
	public requestJson queryAllShouQu(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
        User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String ZLLJSDID=request.getParameter("ZLLJSDID");
		domresult = this.ziLiaoShouFaService.queryConditionShouQu(json.getMsg(),ZLLJSDID,user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "deleteJS")
	@ResponseBody
	protected requestJson deleteJS(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.ziLiaoShouFaService.deleteJS(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "deleteLQ")
	@ResponseBody
	protected requestJson deleteLQ(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.ziLiaoShouFaService.deleteLQ(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}
