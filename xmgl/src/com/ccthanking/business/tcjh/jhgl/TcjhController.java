package com.ccthanking.business.tcjh.jhgl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.tcjh.jhgl.service.TcjhService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2013-7-31
 */
@Controller
@RequestMapping("/tcjh/tcjhController")
public class TcjhController {
	
	@Autowired
	private TcjhService tcjhService;

	@RequestMapping(params = "insertBgbb")
	@ResponseBody
	protected requestJson insertBgbb(final HttpServletRequest request,requestJson json) throws Exception {
		String xmbh = request.getParameter("xmbh");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.tcjhService.insertBgbb(json.getMsg(),user,xmbh);
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
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.tcjhService.query(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 按计划下达统筹计划json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "xdtcjhByPc")
	@ResponseBody
	protected requestJson xdtcjhByPc(final HttpServletRequest request,String json) throws Exception {
		String jhid = (String) request.getParameter("jhid");
		
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.tcjhService.xdtcjhByPc(jhid,request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 按计划下达统筹计划json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "xdtcjhByXm")
	@ResponseBody
	protected requestJson xdtcjhByXm(final HttpServletRequest request,String json) throws Exception {
		String ids = (String) request.getParameter("ids");
		
		requestJson j = new requestJson();
		String  resultVO = this.tcjhService.xdtcjhByXm(ids,request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "queryMoreXiangMu")
	@ResponseBody
	public requestJson queryMoreXiangMu(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String domresult = this.tcjhService.queryMoreXiangMu(json.getMsg(),request);
		j.setMsg(domresult);
		return j;
	}
	
	/**
	 * 根据ID查询
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
		resultVO = this.tcjhService.queryById(id,user);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 查询下发计划
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXfjh")
	@ResponseBody
	public requestJson queryXfjhCondition(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.tcjhService.queryXfjh(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 根据ID查询甘特图
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "ggtById")
	@ResponseBody
	protected requestJson ggtById(final HttpServletRequest request,String json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String id = (String) request.getParameter("id");
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.tcjhService.ggtById(id,user);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 根据ID查询甘特图形象进度
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "ggtById_xxjd")
	@ResponseBody
	protected requestJson ggtById_xxjd(final HttpServletRequest request,String json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String id = (String) request.getParameter("id");
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.tcjhService.ggtById_xxjd(id,user);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 查询变更信息
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryBgxx")
	@ResponseBody
	public requestJson queryBgxx(final HttpServletRequest request,requestJson json) throws Exception
	{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.tcjhService.queryBgxx(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 查询变更项目
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryBgxm")
	@ResponseBody
	public requestJson queryBgxm(final HttpServletRequest request,requestJson json) throws Exception
	{
		//String bgid = request.getParameter("bgid");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.tcjhService.queryBgxm(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "updatebatchdata")
	@ResponseBody
	protected requestJson updatebatchdata(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String ywid = request.getParameter("YWID");
		String jhid = request.getParameter("JHID");
		String bgsm = request.getParameter("BGSM");
		String tids = request.getParameter("TIDS");
		String  resultVO = "";
		resultVO = this.tcjhService.updatebatchdata(json.getMsg(),user,ywid,jhid,bgsm,tids);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "updatebatchdataNobg")
	@ResponseBody
	protected requestJson updatebatchdataNobg(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String jhid = request.getParameter("JHID");
		String  resultVO = "";
		resultVO = this.tcjhService.updatebatchdataNobg(json.getMsg(),user,jhid);
		j.setMsg(resultVO);
		return j;
	}
	@RequestMapping(params = "xmmcAutoQuery")
	@ResponseBody
	public List<autocomplete> xmmcAutoComplete(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.tcjhService.xmmcAutoComplete(match,user);
		return list;
	}
	
	//根据项目管理公司查询
	@RequestMapping(params = "xmmcAutoQueryByXmglgs")
	@ResponseBody
	public List<autocomplete> xmmcAutoQueryByXmglgs(final HttpServletRequest request,autocomplete match) throws Exception{
		List<autocomplete> list  = this.tcjhService.xmmcAutoQueryByXmglgs(match,request);
		return list;
	}
	
	/**
	 * 查询json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryJh")
	@ResponseBody
	public requestJson queryJh(final HttpServletRequest request,requestJson json) throws Exception
	{
		String jhid = request.getParameter("jhid");
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.tcjhService.queryJh(jhid,user);
		j.setMsg(domresult);
		return j;

	}
}
