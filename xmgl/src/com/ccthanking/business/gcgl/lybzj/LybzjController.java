package com.ccthanking.business.gcgl.lybzj;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.gcgl.lybzj.service.LybzjService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2013-8-8
 */
@Controller
@RequestMapping("/lybzj/lybzjController")
public class LybzjController {
	
	@Autowired
	private LybzjService lybzjService;

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
		domresult = this.lybzjService.query(json.getMsg(),user);
		j.setMsg(domresult);
		return j;

	}
	
}
