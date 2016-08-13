/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglTqkbmmxController.java
 * 创建日期： 2013-09-29 上午 07:27:48
 * 功能：    服务控制类：提请款部门明细
 * 所含类:   GcZjglTqkbmmxService
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-29 上午 07:27:48  蒋根亮   创建文件，实现基本功能
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

import com.ccthanking.business.zjgl.service.GcZjglTqkbmmxService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * <p>
 * GcZjglTqkbmmxController.java
 * </p>
 * <p>
 * 功能：提请款部门明细
 * </p>
 * 
 * <p>
 * <a href="GcZjglTqkbmmxController.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-29
 * 
 */

@Controller
@RequestMapping("/zjgl/gcZjglTqkbmmxController")
public class GcZjglTqkbmmxController {

    private static Logger logger = LoggerFactory.getLogger(GcZjglTqkbmmxController.class);

    @Autowired
    private GcZjglTqkbmmxService gcZjglTqkbmmxService;

    /**
     * 查询json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryYbfBMMX")
    @ResponseBody
    public requestJson queryConditionYbfBMMX(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款已拔付部门明细查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";

        String opttype = request.getParameter("opttype");
        HashMap map = new HashMap();
        map.put("opttype", opttype);

        domresult = this.gcZjglTqkbmmxService.queryConditionYbfBMMX(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 查询json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryBMMX")
    @ResponseBody
    public requestJson queryConditionBMMX(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门明细查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";

        String opttype = request.getParameter("opttype");
        HashMap map = new HashMap();
        map.put("opttype", opttype);

        domresult = this.gcZjglTqkbmmxService.queryConditionBMMX(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }
    
    /**
     * 查询json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryBMMXdisGcb")
    @ResponseBody
    public requestJson queryBMMXdisGcb(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门明细查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";

        String opttype = request.getParameter("opttype");
        HashMap map = new HashMap();
        map.put("opttype", opttype);

        domresult = this.gcZjglTqkbmmxService.queryConditionBMMXdisGcb(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 查询某合同的已拔付.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryYbfByHt")
    @ResponseBody
    public requestJson queryYbfByHt(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门明细查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";

        HashMap map = new HashMap();
        String opttype = request.getParameter("opttype");
        map.put("opttype", opttype);

        String htid = request.getParameter("htid");
        map.put("htid", htid);

        domresult = this.gcZjglTqkbmmxService.queryYbfByHt(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 查询json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "query")
    @ResponseBody
    public requestJson queryCondition(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门明细查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcZjglTqkbmmxService.queryCondition(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 保存数据json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "insert")
    @ResponseBody
    protected requestJson insert(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门明细新增】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglTqkbmmxService.insert(json.getMsg(), user);
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
        logger.info("<{}>执行操作【提请款部门明细修改】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglTqkbmmxService.update(json.getMsg(), user);
        j.setMsg(resultVO);
        return j;
    }

    /**
     * 批量修改记录.
     * 
     * @param request
     * @param json
     * @return
     * @throws Exception
     * @since v1.00
     */
    @RequestMapping(params = "updatebatchdata")
    @ResponseBody
    protected requestJson updatebatchdata(HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门明细批量修改】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";

        HashMap map = new HashMap();
        
        String tqkid = request.getParameter("tqkid");
        map.put("tqkid", tqkid);

        String opttype = request.getParameter("opttype");
        map.put("opttype", opttype);

        resultVO = this.gcZjglTqkbmmxService.update(json.getMsg(), user, map);
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
        logger.info("<{}>执行操作【提请款部门明细删除】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglTqkbmmxService.delete(json.getMsg(), user);
        j.setMsg(resultVO);
        return j;
    }
    
    /**
     * 查询json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryJlsj")
    @ResponseBody
    public requestJson queryJlsj(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门明细相关计量数据查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        String id = request.getParameter("id");
//        String  data="{querycondition: {conditions: [{\"value\":\""+id+"\",\"fieldname\":\"b.id\",\"operation\":\"=\",\"logic\":\"and\"}]}}";
        domresult = this.gcZjglTqkbmmxService.queryJlsj(id, user);
        j.setMsg(domresult);
        return j;

    }
    
    /**
     * 查询json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryPqsdz")
    @ResponseBody
    public requestJson queryPqsdz(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【提请款部门明细排迁类相关审定值数据查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        String id = request.getParameter("id");
//        String  data="{querycondition: {conditions: [{\"value\":\""+id+"\",\"fieldname\":\"b.id\",\"operation\":\"=\",\"logic\":\"and\"}]}}";
        domresult = this.gcZjglTqkbmmxService.queryPqsdz(id, user);
        j.setMsg(domresult);
        return j;

    }

}
