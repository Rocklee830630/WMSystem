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
@RequestMapping("/zjgl/gcZjglTqkbmController")
public class GcZjglTqkbmController {

	private static Logger logger = LoggerFactory.getLogger(GcZjglTqkbmController.class);

    @Autowired
    private GcZjglTqkbmService gcZjglTqkbmService;

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
        logger.info("<{}>执行操作【提请款部门查询】",user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        
        String opttype = request.getParameter("opttype");
        HashMap map = new HashMap();
        map.put("opttype", opttype);
        
        domresult = this.gcZjglTqkbmService.queryCondition(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 保存数据json
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "insert")
    @ResponseBody
    protected requestJson insert(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门新增】",user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglTqkbmService.insert(json.getMsg(), user);
        j.setMsg(resultVO);
        return j;
    }

    /**
     * 修改记录.
     * 
     * @param request
     * @param json
     * @return
     * @throws Exception
     * @since v1.00
     */
    @RequestMapping(params = "update")
    @ResponseBody
    protected requestJson update(HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门修改】",user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        
        String opttype = request.getParameter("opttype");
        HashMap map = new HashMap();
        map.put("opttype", opttype);
        
        resultVO = this.gcZjglTqkbmService.update(json.getMsg(), user, map);
        j.setMsg(resultVO);
        return j;
    }

    /**
     * 删除记录.
     * 
     * @param request
     * @param json
     * @return
     * @throws Exception
     * @since v1.00
     */
    @RequestMapping(params = "delete")
    @ResponseBody
    public requestJson delete(HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门删除】",user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglTqkbmService.delete(json.getMsg(), user);
        j.setMsg(resultVO);
        return j;
    }
   /**
    * 删除提请款
    * @param request
    * @param json
    * @return 
    * @throws Exception
    */
    @RequestMapping(params = "deleteTqk")
    @ResponseBody
    public requestJson deleteTqk(HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglTqkbmService.deleteTqk(json.getMsg(), user);
        j.setMsg(resultVO);
        return j;
    }
    
    @RequestMapping(params = "updateGcbMxzt")
    @ResponseBody
    protected requestJson updateGcbMxzt(HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门修改】",user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        String ids = request.getParameter("ids");
        HashMap map = new HashMap();
        map.put("ids", ids);
        resultVO = this.gcZjglTqkbmService.updateGcbMxzt(json.getMsg(), user, map);
        j.setMsg(resultVO);
        return j;
    }
}
