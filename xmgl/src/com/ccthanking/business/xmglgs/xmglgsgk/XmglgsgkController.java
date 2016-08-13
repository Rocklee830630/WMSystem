package com.ccthanking.business.xmglgs.xmglgsgk;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xmglgs.xmglgsgk.service.XmglgsgkService;
import com.ccthanking.framework.model.requestJson;

/**
 * @author sunjl
 * @time 2014-1-14
 */
@Controller
@RequestMapping("/xmglgsgk/xmglgsgkController")
public class XmglgsgkController {
	
	@Autowired
	private XmglgsgkService xmglgsgkService;

	//工程部——工程进展——工程保函——工程洽商
	@RequestMapping(params = "fzr_ztxx")
	@ResponseBody
	public requestJson fzr_ztxx(final HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		String  resultVO = "";
		resultVO = this.xmglgsgkService.fzr_ztxx(json.getMsg(),request);
		j.setMsg(resultVO);
		return j;

	}		
}
