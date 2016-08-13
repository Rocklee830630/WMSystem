package com.ccthanking.business.xtbg.ggtz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.xtbg.ggtz.service.GgtzService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.model.responseJson;
import com.ccthanking.framework.util.Pub;

/**
 * @auther xhb 
 */
@Controller
@RequestMapping("/ggtzController")
public class GgtzController {

	@Autowired
	private GgtzService ggtzService;
	

	/**
	 * 查询公告信息
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryGgtz")
	@ResponseBody
	public requestJson queryGgtz(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = ggtzService.queryGgtz(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/**
	 * 查询首页更多公告信息
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "queryMoreGg")
	@ResponseBody
	public requestJson queryMoreGg(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = ggtzService.queryMoreGg(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 阅读公告，每次浏览此公告，阅读次数加一
	 * @param json 页面传进来的对象json
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "readGgtz")
	@ResponseBody
	public requestJson readGgtz(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = ggtzService.readGgtz(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "readMainGg")
	@ResponseBody
	public String readMainGg(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		String ggid = request.getParameter("ggid");
		String ydcs = request.getParameter("ydcs");
		JSONObject jsonObj = new JSONObject();
		try {
			ggtzService.readGgtz(ggid, ydcs, user);
			jsonObj.put("sign", "0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObj.toString();
	}

	/**
	 * 修改公告信息：包括添加、修改、删除
	 * @param request
	 * @param json 页面传进来的对象json
	 * @return
	 */
	@RequestMapping(params = "executeGg")
	@ResponseBody
	public requestJson executeGg(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		String operatorSign = request.getParameter("operatorSign");
		String ywid = request.getParameter("ywid");
		String sffb = request.getParameter("sffb");
		try {
			String domResult = ggtzService.executeGg(json.getMsg(), user, operatorSign,request, ywid,sffb);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	@RequestMapping(params = "cqsh")
	@ResponseBody
	public String cqsh(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		String ggid = request.getParameter("ggid");
		String sjbh = request.getParameter("sjbh");
		String ywlx = request.getParameter("ywlx");


		JSONObject jsonObj = new JSONObject();
		try {
			ggtzService.cqsh(ggid,sjbh,ywlx, user, request);
			jsonObj.put("sign", "0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObj.toString();
	}

	@RequestMapping(params = "doggsh")
	@ResponseBody
	public void doggsh(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	   	User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		 String id = (String)request.getParameter("id");
		 String shyj =  (String)request.getParameter("shyj");
		 String shjg =  (String)request.getParameter("shjg");
		 String taskid = (String)request.getParameter("taskid");
		 String sjbh = (String)request.getParameter("sjbh");
		 
			String ywid = request.getParameter("ywid");
			String zt = request.getParameter("zt");
		 try {
				//ggtzService.doggsh(id,shyj,shjg,taskid,sjbh, user, request);
			 ggtzService.doggsh(ywid,"",zt,"","", user, request);
			} catch (Exception e) {
				e.printStackTrace();
			}		 JSONObject jo = new JSONObject();
		 jo.put("success", "1");
     	 jo.put("message", "审核通过！");
         Pub.writeMessage(response,jo.toString(),"UTF-8");
	}
	
	
	
	@RequestMapping(params = "publishGg")
	@ResponseBody
	public String publishGg(HttpServletRequest request, requestJson json) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		String ggid = request.getParameter("ggid");
		JSONObject jsonObj = new JSONObject();
		try {
			ggtzService.publishGg(ggid, user, request);
			jsonObj.put("sign", "0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObj.toString();
	}
	
	
	@RequestMapping(params = "frameGgtz")
	@ResponseBody
	public responseJson frameGgtz(final HttpServletRequest request) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		responseJson j = new responseJson();
		String message = ggtzService.frameGgtz(user);
		j.setMsg(message);
		j.setSuccess(true);
		return j;
	}
}
