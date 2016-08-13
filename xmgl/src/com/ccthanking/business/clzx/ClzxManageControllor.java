package com.ccthanking.business.clzx;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.clzx.service.ClzxManagerService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Pub;

/**
 * @author xiahongbo
 * @date 2014-9-2
 */
@Controller
@RequestMapping("/clzx/clzxManageControllor")
public class ClzxManageControllor {

	@Autowired
	private ClzxManagerService service;
	/**
     * @param 插入业务人员
	 */
	@RequestMapping(params = "insert_clzx")
	@ResponseBody
	protected requestJson insert_clzx(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String resultVO = this.service.insert_clzx(request,user);
		j.setMsg(resultVO);
		return j;
	}
		
	@RequestMapping(params = "queryClzxgl")
	@ResponseBody
	public void queryClzxgl(HttpServletRequest request, HttpServletResponse response) {
		
		response.setCharacterEncoding("UTF-8");
		String menuJson = service.queryClzxgl();
		try {
			System.out.println(menuJson);
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(params = "queryYwlx")
	@ResponseBody
	public requestJson queryYwlx(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		try {
			String domResult = service.queryYwlx(json.getMsg(), request);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "queryList")
	@ResponseBody
	public requestJson queryList(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		try {
			String domResult = service.queryList(json.getMsg(), request);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	@RequestMapping(params = "queryListLS")
	@ResponseBody
	public requestJson queryListLS(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		try {
			String domResult = service.queryListLS(json.getMsg(), request);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params="queryCount")
	@ResponseBody
	public requestJson queryCount(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		try {
			String domString = service.queryCount(request);
			j.setMsg(domString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params="queryIsShowClzx")
	@ResponseBody
	public requestJson queryIsShowClzx(HttpServletRequest request, requestJson json) {
		requestJson j = new requestJson();
		try {
			String domString = service.queryIsShowClzx(request);
			j.setMsg(domString);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params="plfkJh")
	@ResponseBody
	public String plfkJh(HttpServletRequest request, HttpServletResponse response, requestJson json) {
		String domString = null;
		try {
			domString = service.plfkJh(request);
			Pub.writeMessage(response, domString, "UTF-8");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return domString;
	}
}
