/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglTqkmxController.java
 * 创建日期： 2013-09-25 下午 03:01:51
 * 功能：    服务控制类：提请款明细
 * 所含类:   GcZjglTqkmxService
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-25 下午 03:01:51  蒋根亮   创建文件，实现基本功能
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

import com.ccthanking.business.zjgl.service.GcZjglTqkmxService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * <p>
 * GcZjglTqkmxController.java
 * </p>
 * <p>
 * 功能：提请款明细
 * </p>
 * 
 * <p>
 * <a href="GcZjglTqkmxController.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-25
 * 
 */

@Controller
@RequestMapping("/zjgl/gcZjglTqkmxController")
public class GcZjglTqkmxController {

    private static Logger logger = LoggerFactory.getLogger(GcZjglTqkmxController.class);

    @Autowired
    private GcZjglTqkmxService gcZjglTqkmxService;

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
        logger.info("<{}>执行操作【提请款明细查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        
        HashMap map = new HashMap();
        
        String opttype = request.getParameter("opttype");
        map.put("opttype", opttype);
        
        domresult = this.gcZjglTqkmxService.queryCondition(json.getMsg(), user, map);
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
        logger.info("<{}>执行操作【提请款明细新增】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";

        String opttype = request.getParameter("opttype");
        HashMap map = new HashMap();
        map.put("opttype", opttype);
        String id = request.getParameter("id");// 提请款主键
        map.put("id", id);
        String ids = request.getParameter("ids");// 部门明细主键串
        map.put("ids", ids);

        resultVO = this.gcZjglTqkmxService.insert(json.getMsg(), user, map);
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
        logger.info("<{}>执行操作【提请款明细修改】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";

        String opttype = request.getParameter("opttype");
        HashMap map = new HashMap();
        map.put("opttype", opttype);
        String ids = request.getParameter("ids");
        map.put("ids", ids);

        resultVO = this.gcZjglTqkmxService.update(json.getMsg(), user, map);
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
        logger.info("<{}>执行操作【提请款明细删除】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";

        String opttype = request.getParameter("opttype");
        HashMap map = new HashMap();
        map.put("opttype", opttype);
        String ids = request.getParameter("ids");
        map.put("ids", ids);

        resultVO = this.gcZjglTqkmxService.delete(json.getMsg(), user, map);
        j.setMsg(resultVO);
        return j;
    }

}
