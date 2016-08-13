/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglTqkbmController.java
 * 创建日期： 2013-09-29 上午 07:31:33
 * 功能：    服务控制类：提请款部门
 * 所含类:   GcZjglTqkbmService
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-29 上午 07:31:33  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.zjgl;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.zjgl.service.GcZjglTqkGcbService;
import com.ccthanking.business.zjgl.service.GcZjglTqkbmService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;


/**
 * <p> GcZjglTqkbmController.java </p>
 * <p> 功能：提请款部门 </p>
 *
 * <p><a href="GcZjglTqkbmController.java.html"><i>查看源代码</i></a></p>  
 *
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-29
 * 
 */

@Controller
@RequestMapping("/zjgl/gcZjglTqkGcbController")
public class GcZjglTqkGcbController {

	private static Logger logger = LoggerFactory.getLogger(GcZjglTqkbmController.class);

    @Autowired
    private GcZjglTqkGcbService gcZjglTqkGcbService;

    /**
     * 查询json
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "query")
    @ResponseBody
    public requestJson queryCondition(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【工程部提请款审批查询】",user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        
        
        
        domresult = this.gcZjglTqkGcbService.queryCondition(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }

    @RequestMapping(params = "updateGcbMxzt")
    @ResponseBody
    protected requestJson updateGcbMxzt(HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【工程部提请款部门明细修改】",user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        String ids = request.getParameter("ids");
        String type = request.getParameter("type");
        if(ids!=null&&!"".equals(ids)){
	        HashMap map = new HashMap();
	        map.put("ids", ids.substring(0,ids.length()-1));
	        map.put("type", type);
	        resultVO = this.gcZjglTqkGcbService.updateGcbMxzt(json.getMsg(), user, map);
	        j.setMsg(resultVO);
        }
        return j;
    }
    
    /**
     * 查询json
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryMx")
    @ResponseBody
    public requestJson queryMx(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【工程部提请款审批明细查询】",user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        
        
        
        domresult = this.gcZjglTqkGcbService.queryGcbTqkMx(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }
}
