package com.ccthanking.business.zsb.bzjkxx;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.model.requestJson;

/**
 * @auther zhaiyl
 */
@Controller
@RequestMapping("/zsxxController")
public class ZsbzxxController {

	@Autowired
	private ZsbzxxServiceImpl zsbzxxServiceImpl;
	
	@RequestMapping(params = "queryZsxx")
	@ResponseBody
	public requestJson queryZsxx(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		String nd = request.getParameter("nd");
		String lujing = request.getParameter("lujing");
		String mingchen = request.getParameter("mingchen");
		String tiaojian = request.getParameter("tiaojian");
		try {
			String domResult = zsbzxxServiceImpl.queryZsxx(json.getMsg(), lujing,mingchen,tiaojian,nd);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
}
