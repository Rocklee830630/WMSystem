package com.ccthanking.business.xdxmk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xdxmk.service.XmscService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2013-7-23
 */
@Controller
@RequestMapping("/xdxmk/xmscController")
public class XmscController {
	
	@Autowired
	private XmscService xmscService;

	@RequestMapping(params = "queryJdzx")
	@ResponseBody
	protected requestJson queryJdzx(final HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmscService.queryJdzx(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "queryXmwgById")
	@ResponseBody
	protected requestJson queryXmwgById(final HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmscService.queryXmwgById(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
	@RequestMapping(params = "queryJdjhjzx")
	@ResponseBody
	protected requestJson queryJdjhjzx(final HttpServletRequest request,requestJson json) throws Exception {
		//User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmscService.queryJdjhjzx(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;
	}
	
}
