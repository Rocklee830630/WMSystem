package com.ccthanking.business.cjjh;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;

@Controller
@RequestMapping("/gcCjjhController")
public class GcCjjhController {

    private static Logger logger = LoggerFactory.getLogger(GcCjjhController.class);

    @Autowired
    private GcCjjhService GcCjjhService;

    /**
     * 查询项目列表
     * @param json
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(params = "query")
    public requestJson query(final HttpServletRequest request, requestJson json) throws Exception {
		requestJson j = new requestJson();
        String domresult = GcCjjhService.query(request,json.getMsg() );
        j.setMsg(domresult);
        return j;
    }

	//获得工程项目分类树形
	@ResponseBody
	@RequestMapping(params="getProjectfl")
	public void getProjectfl(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		try {
			String menuJson = GcCjjhService.getProjectfl(request);
			response.getWriter().print(menuJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping(params = "add")
	protected requestJson add(HttpServletRequest request,requestJson json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		requestJson j = new requestJson();
		try {
			String domResult = GcCjjhService.add(json.getMsg(), user);
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	@ResponseBody
	@RequestMapping(params = "getNodeDetail")
	protected requestJson getNodeDetail(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		try {
			String domResult = GcCjjhService.getNodeDetail(request,json.getMsg());
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	@ResponseBody
	@RequestMapping(params = "queryXmList")
	protected requestJson queryXmList(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		try {
			String domResult = GcCjjhService.queryXmList(request,json.getMsg());
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	@ResponseBody
	@RequestMapping(params = "saveInfo")
	protected requestJson saveInfo(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		try {
			String domResult = GcCjjhService.saveInfo(request,json.getMsg());
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/*
	 * 根节点维护
	 */
	@ResponseBody
	@RequestMapping(params = "saveRoot")
	protected requestJson saveRoot(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		try {
			String domResult = GcCjjhService.saveRoot(request,json.getMsg());
			j.setMsg(domResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}	@ResponseBody
	@RequestMapping(params = "doDeleteNode")
	protected requestJson doDeleteNode(HttpServletRequest request,requestJson json) throws Exception {
		requestJson j = new requestJson();
		try {
			String domResult = GcCjjhService.doDeleteNode(request,json.getMsg());
			j.setMsg("1");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("0");
		}
		return j;
	}
	/**
	 * 
	 * @param request
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "queryXMbyId")
	@ResponseBody
	public requestJson queryConditiondemo(HttpServletRequest request,requestJson json) throws Exception
	{
		requestJson j = new requestJson();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String  domresult = "";
		domresult = this.GcCjjhService.queryXMbyId(json.getMsg(),user,request);
		j.setMsg(domresult);
		return j;

	}
	@RequestMapping(params = "xmmcAutoQuery")
	@ResponseBody
	public List<autocomplete> xmmcAutoComplete(final HttpServletRequest request,autocomplete match) throws Exception{
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		List<autocomplete> list  = this.GcCjjhService.xmmcAutoComplete(match,user);
		return list;
	}
}
