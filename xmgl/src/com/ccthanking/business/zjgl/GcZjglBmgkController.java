package com.ccthanking.business.zjgl;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.ccthanking.business.zjgl.service.GcZjglBmgkService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

@Controller
@RequestMapping("/zjgl/gcZjglBmgkController")
public class GcZjglBmgkController {
	@Autowired
	private GcZjglBmgkService gczbService;
	/**
     * 查询区域列表json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryBmList")
    @ResponseBody
    public requestJson queryBmList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";

        HashMap map = new HashMap();

        //项目年度
        String nd = request.getParameter("nd");
        map.put("nd", nd);

        domresult = this.gczbService.queryBmList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }
    

	/**
     * 查询区域列表json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryBmZList")
    @ResponseBody
    public requestJson queryBmZList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        domresult = this.gczbService.queryBmZList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }
    /**
     * 查询区域列表json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryPCZList")
    @ResponseBody
    public requestJson queryPCZList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        
        domresult = this.gczbService.queryPCZList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }
    
    /**
     * 查询区域列表json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryPCList")
    @ResponseBody
    public requestJson queryPCList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        String nd = request.getParameter("nd");
        map.put("nd", nd);
        domresult = this.gczbService.queryPCList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }
    
    /**
     * 查询区域列表json.
     * 总征收资金
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryZZSZJList")
    @ResponseBody
    public requestJson queryZZSZJList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        domresult = this.gczbService.queryZZSZJList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }
    /**
     * 查询区域列表json.
     * 年份征收资金
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryZSZJList")
    @ResponseBody
    public requestJson queryZSZJList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        String nd = request.getParameter("nd");
        map.put("nd", nd);
        domresult = this.gczbService.queryZSZJList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }
    
    /**
     * 查询区域列表json.
     * 总履约保证金
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryZLYBZJList")
    @ResponseBody
    public requestJson queryZLYBZJList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        domresult = this.gczbService.queryZLYBZJList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    
    /**
     * 查询区域列表json.
     * 年份履约保证金
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryNFLYBZJList")
    @ResponseBody
    public requestJson queryNFLYBZJList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        String nd = request.getParameter("nd");
        map.put("nd", nd);
        domresult = this.gczbService.queryNFLYBZJList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    
    /**
     * 查询区域列表json.
     * 年份履约保证金(现金)
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryXJNFLYBZJList")
    @ResponseBody
    public requestJson queryXJNFLYBZJList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        String nd = request.getParameter("nd");
        map.put("nd", nd);
        domresult = this.gczbService.queryXJNFLYBZJList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    /**
     * 查询区域列表json.
     * 年份履约保证金(保函)
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryBHNFLYBZJList")
    @ResponseBody
    public requestJson queryBHNFLYBZJList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        String nd = request.getParameter("nd");
        map.put("nd", nd);
        domresult = this.gczbService.queryBHNFLYBZJList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    
    /**
     * 查询区域列表json.
     * 总履约保证金(保函)
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryZBHLYBZJList")
    @ResponseBody
    public requestJson queryZBHLYBZJList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        domresult = this.gczbService.queryZBHLYBZJList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    /**
     * 查询区域列表json.
     * 总履约保证金(现金)
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryZXJLYBZJList")
    @ResponseBody
    public requestJson queryZXJLYBZJList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        domresult = this.gczbService.queryZXJLYBZJList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    /**
     * 查询区域列表json.
     * 累计返还履约保证金
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryLJFHLVBZJList")
    @ResponseBody
    public requestJson queryLJFHLVBZJList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        domresult = this.gczbService.queryLJFHLVBZJList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    /**
     * 查询区域列表json.
     * 年份累计返还履约保证金
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryNFLJFHLVBZJList")
    @ResponseBody
    public requestJson queryNFLJFHLVBZJList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        String nd = request.getParameter("nd");
        map.put("nd", nd);
        domresult = this.gczbService.queryNFLJFHLVBZJList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    /**
     * 查询区域列表json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryCLJFHLVBZJList")
    @ResponseBody
    public requestJson queryCLJFHLVBZJList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        domresult = this.gczbService.queryCLJFHLVBZJList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    /**
     * 查询区域列表json.
     * 履约保证金保函已失效
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryYSXList")
    @ResponseBody
    public requestJson queryYSXList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        domresult = this.gczbService.queryYSXList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    /**
     * 查询区域列表json.
     * 履约保证金保函即将失效
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryJJSXList")
    @ResponseBody
    public requestJson queryJJSXList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        domresult = this.gczbService.queryJJSXList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    
    /**
     * 查询区域列表json.
     * 征收资金使用金额
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "querySYJEList")
    @ResponseBody
    public requestJson querySYJEList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        domresult = this.gczbService.querySYJEList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    /**
     * 查询区域列表json.
     *  征收资金年度使用金额
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryNDSYJEList")
    @ResponseBody
    public requestJson queryNDSYJEList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        String nd = request.getParameter("nd");
        map.put("nd", nd);
        domresult = this.gczbService.queryNDSYJEList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    /**
     * 查询区域列表json.
     *  累计拨付余额
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryLJBFYEList")
    @ResponseBody
    public requestJson queryLJBFYEList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        String nd = request.getParameter("nd");
        map.put("nd", nd);
        domresult = this.gczbService.queryLJBFYEList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    
    /**
     * 查询区域列表json.
     *  累计拨付余额
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryBMPXList")
    @ResponseBody
    public requestJson queryBMPXList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        HashMap map = new HashMap();
        String nd = request.getParameter("nd");
        map.put("nd", nd);
        domresult = this.gczbService.queryBMPXList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;
    }
    
    /**
     * 查询区域列表json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryQyList")
    @ResponseBody
    public requestJson queryQyList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";

        HashMap map = new HashMap();

        //项目年度
        String nd = request.getParameter("nd");
        map.put("nd", nd);

        domresult = this.gczbService.queryQyList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }
    
}
