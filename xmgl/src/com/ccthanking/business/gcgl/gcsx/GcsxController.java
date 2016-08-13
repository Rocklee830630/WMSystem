package com.ccthanking.business.gcgl.gcsx;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.gcgl.gcsx.service.GcsxService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2013-8-12
 */
@Controller
@RequestMapping("/gcsx/gcsxController")
public class GcsxController {
	
	@Autowired
	private GcsxService GcsxService;

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
		requestJson j = new requestJson();
		String  domresult = "";
		domresult = this.GcsxService.query(json.getMsg(),request);
		j.setMsg(domresult);
		return j;

	}
	
	/**
	 * 保存数据json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "insert")
	@ResponseBody
	protected requestJson insert(final HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.GcsxService.insert(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	/**
	 * 项目名称自动查询
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "xmmcAutoCompleteByYw")
	@ResponseBody
	public List<autocomplete> xmmcAutoCompleteByYw(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.GcsxService.xmmcAutoCompleteByYw(match,user);
		return list;
	}
	
	/**
	 * 删除方法
	 * @param request
	 * @param js
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public requestJson deletePqzxm(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.GcsxService.delete(request,json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
	
}
