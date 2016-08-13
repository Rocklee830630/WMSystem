/**
 * @author wangzh
 */
package com.ccthanking.business.ztb.zbgl;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.ztb.zbxq.ZhaoBiaoXuQiuService;
import com.ccthanking.business.ztb.zbxq.ZhaoBiaoXuQiuServiceImpl;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/ZhaotoubiaoController")
public class ZhaotoubiaoController {

	
	@Autowired
	private ZhaotoubiaoService zhaotoubiaoService;
/**
	 * 查询样例json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZhaotoubiao")
	@ResponseBody
	public requestJson queryConditiondemo(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.zhaotoubiaoService.queryConditionZhaotoubiao(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "insertZhaotoubiao")
	@ResponseBody
	protected requestJson insertdemo(HttpServletRequest request,requestJson json) throws Exception {
		String ZTBXQID=request.getParameter("ZTBXQID");
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.zhaotoubiaoService.insertdemo(json.getMsg(),user,ZTBXQID);
		j.setMsg(resultVO);
		//mav.setViewName("/jsp/framework/portal/frame");
		return j;
	}
	
	
	@RequestMapping(params = "updateZhaotoubiao")
	@ResponseBody
	protected requestJson updatedemo(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.zhaotoubiaoService.updatedemo(json.getMsg(),user);
		j.setMsg(resultVO);
		//mav.setViewName("/jsp/framework/portal/frame");
		return j;
	}
	
	@RequestMapping(params = "updateZhaotoubiaoZT")
	@ResponseBody
	protected requestJson updateZhaotoubiaoZT(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.zhaotoubiaoService.updateZhaotoubiaoZT(json.getMsg(),user);
		j.setMsg(resultVO);
		//mav.setViewName("/jsp/framework/portal/frame");
		return j;
	}
	
	@RequestMapping(params = "queryZhaotoubiaoxuqiu")
	@ResponseBody
	public requestJson queryConditionZhaobiaoxuqiu(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.zhaotoubiaoService.queryConditionZhaobiaoxuqiu(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "queryZhaoBiaoXiangMu")
	@ResponseBody
	public requestJson queryConditionZhaobiaoxiangmu(HttpServletRequest request,requestJson json) throws Exception
	{
		String ZTBSJID=request.getParameter("ZTBSJID");
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.zhaotoubiaoService.queryConditionZhaobiaoxiangmu(json.getMsg(),user,ZTBSJID);
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "queryZhongbiaojia")
	@ResponseBody
	public requestJson queryZhongbiaojia(HttpServletRequest request,requestJson json) throws Exception
	{
		String ZTBSJID=request.getParameter("ZTBSJID");
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.zhaotoubiaoService.queryZhongbiaojia(json.getMsg(),user,ZTBSJID);
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "updateZhongbiaojia")
	@ResponseBody
	protected requestJson updateZhongbiaojia(HttpServletRequest request,requestJson json) throws Exception {
		
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.zhaotoubiaoService.updateZhongbiaojia(json.getMsg(),user);
		j.setMsg(resultVO);
		//mav.setViewName("/jsp/framework/portal/frame");
		return j;
	}
	
	@RequestMapping(params = "queryZtbsptg")
	@ResponseBody
	public requestJson queryZtbsptg(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.zhaotoubiaoService.queryZtbsptg(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "deleteZtbsj")
	@ResponseBody
	public requestJson deleteZtbsj(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaotoubiaoService.deleteZtbsj(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}

	@RequestMapping(params = "getWhfxmCount")
	@ResponseBody
	public requestJson getWhfxmCount(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaotoubiaoService.getWhfxmCount(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}
	
	@RequestMapping(params = "queryZxmListWithXqid")
	@ResponseBody
	public requestJson queryZxmListWithXqid(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaotoubiaoService.queryZxmListWithXqid(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}
	@RequestMapping(params = "queryLanBiaoJia")
	@ResponseBody
	public requestJson queryLanBiaoJia(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaotoubiaoService.queryLanBiaoJia(request);
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 根据主键获取
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getFormValueByID")
	@ResponseBody
	public requestJson getFormValueByID(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		ZhaoBiaoXuQiuService zhaobiaoxuqiuService = new ZhaoBiaoXuQiuServiceImpl();
		String resultVO = zhaobiaoxuqiuService.getFormValueByID(request,js.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 校验是否存在已启动招标的需求
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "doCheck")
	@ResponseBody
	public requestJson doCheck(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaotoubiaoService.doCheck(request);
		j.setMsg(domresult);
		return j;
	}
	/**
	 * 
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "getxmZJECount")
	@ResponseBody
	public requestJson getxmZJECount(HttpServletRequest request,requestJson js) throws Exception {
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaotoubiaoService.getxmZJECount(request);
		j.setMsg(domresult);
		return j;
	}
	
}
