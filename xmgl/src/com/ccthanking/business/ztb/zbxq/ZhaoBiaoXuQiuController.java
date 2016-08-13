/**
 * @author wangzh
 */
package com.ccthanking.business.ztb.zbxq;


import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.*;

/**
 * @author wangzh
 */
@Controller
@RequestMapping("/ZhaoBiaoXuQiuController")
public class ZhaoBiaoXuQiuController {

	
	@Autowired
	private ZhaoBiaoXuQiuService zhaobiaoxuqiuService;
	
	/**
	 * 查询招投标json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZhaobiaoxuqiu")
	@ResponseBody
	public requestJson queryConditiondemo(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaobiaoxuqiuService.queryConditionZhaobiaoxuqiu(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 查询招投标json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryConditionZhaobiaoxuqiuNd")
	@ResponseBody
	public requestJson queryConditionZhaobiaoxuqiuNd(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  domresult = this.zhaobiaoxuqiuService.queryConditionZhaobiaoxuqiuNd(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}

	@RequestMapping(params = "updatebatchdataNobg")
	@ResponseBody
	protected requestJson updatebatchdataNobg(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = this.zhaobiaoxuqiuService.updatebatchdataNobg(json.getMsg(),user);
		j.setMsg(resultVO);
		return j;
	}
	/**
	 * 增加招标需求
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insertZhaobiaoxuqiu")
	@ResponseBody
	protected requestJson insertdemo(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.zhaobiaoxuqiuService.insertdemo(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 查询需求下的项目
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryZhaoBiaoXiangMu")
	@ResponseBody
	public requestJson queryZhaoBiaoXiangMu(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.zhaobiaoxuqiuService.queryZhaoBiaoXiangMu(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "queryMoreXiangMu")
	@ResponseBody
	public requestJson queryMoreXiangMu(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.zhaobiaoxuqiuService.queryMoreXiangMu(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	/**
	 * 修改招投标
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "updateZhaobiaoxuqiu")
	@ResponseBody
	public requestJson updateZhaobiaoxuqiu(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String jhsjids=request.getParameter("jhsjids");
		String  domresult = "";
		domresult = this.zhaobiaoxuqiuService.updateZhaobiaoxuqiu(request,json.getMsg(),user,jhsjids);
		j.setMsg(domresult);
		return j;

	}
	
	@RequestMapping(params = "queryConditionZhaotoubiao")
	@ResponseBody
	public requestJson queryConditionZhaotoubiao(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaobiaoxuqiuService.queryConditionZhaotoubiao(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "doQdzb")
	@ResponseBody
	public requestJson doQdzb(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaobiaoxuqiuService.doQdzb(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}
	@RequestMapping(params = "doSubmit")
	@ResponseBody
	public requestJson doSubmit(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaobiaoxuqiuService.doSubmit(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}
	@RequestMapping(params = "deleteZtbxq")
	@ResponseBody
	public requestJson deleteZtbxq(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaobiaoxuqiuService.deleteZtbxq(request,json.getMsg());
		j.setMsg(domresult);
		return j;
	}

	@RequestMapping(params = "getCountZbcs")
	@ResponseBody
	public requestJson getCountZbcs(HttpServletRequest request,requestJson json) throws Exception{
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.zhaobiaoxuqiuService.getCountZbcs(request,json.getMsg());
		j.setMsg(domresult);
		return j;

	}
}
