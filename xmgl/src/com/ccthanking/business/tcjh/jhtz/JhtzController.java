package com.ccthanking.business.tcjh.jhtz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Pub;

/**
 * @author xiahongbo
 * @date 2014-8-8
 */

@Controller
@RequestMapping("/jhtzController")
public class JhtzController {
	
	@Autowired
	private JhtzService jhtzService;
	
	public requestJson queryJhtz(HttpServletRequest request, HttpServletResponse response, 
			requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String domString = jhtzService.query(request, user, json.getMsg());
		j.setMsg(domString);
		return j;
	}
	

	@RequestMapping(params = "queryOne")
	@ResponseBody
	public void queryJhtz(HttpServletRequest request, HttpServletResponse response) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		try {
			String domString = jhtzService.queryById(request, user);
			Pub.writeMessage(response, domString, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	protected requestJson saveOrUpdate(final HttpServletRequest request,requestJson json) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String resultVO = this.jhtzService.saveOrUpdate(request, user, json.getMsg());
		j.setMsg(resultVO);
		return j;
	}
}
