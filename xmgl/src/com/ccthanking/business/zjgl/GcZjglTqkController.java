/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglTqkController.java
 * 创建日期： 2013-09-25 下午 02:36:20
 * 功能：    服务控制类：提请款
 * 所含类:   GcZjglTqkService
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-25 下午 02:36:20  蒋根亮   创建文件，实现基本功能
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.zjgl.service.GcZjglTqkService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * <p>
 * GcZjglTqkController.java
 * </p>
 * <p>
 * 功能：提请款
 * </p>
 * 
 * <p>
 * <a href="GcZjglTqkController.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-25
 * 
 */

@Controller
@RequestMapping("/zjgl/gcZjglTqkController")
public class GcZjglTqkController {

    private static Logger logger = LoggerFactory.getLogger(GcZjglTqkController.class);

    @Autowired
    private GcZjglTqkService gcZjglTqkService;

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
        logger.info("<{}>执行操作【提请款查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcZjglTqkService.queryCondition(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 申请前同步当前时间的数据到提请款明细表.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "syncdata")
    @ResponseBody
    public requestJson syncTqk(final HttpServletRequest request, @RequestParam String tqkid) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【同步提请款数据入明细表】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcZjglTqkService.synaData(null, tqkid, user);
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
        logger.info("<{}>执行操作【提请款新增】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";

        HashMap map = new HashMap();
        String ywid = request.getParameter("ywid");
        map.put("ywid", ywid);

        String condition = request.getParameter("condition");
        map.put("condition", condition);
        String tqkid = request.getParameter("tqkid");// insert into 部门提请款ID
                                                     // 需将本ID的明细全部插入明细表
        map.put("tqkid", tqkid);

        resultVO = this.gcZjglTqkService.insert(json.getMsg(), user, map);
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
        logger.info("<{}>执行操作【提请款修改】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";

        HashMap map = new HashMap();

        String opttype = request.getParameter("opttype");
        map.put("opttype", opttype);

        resultVO = this.gcZjglTqkService.update(json.getMsg(), user, map);
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
        logger.info("<{}>执行操作【提请款删除】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglTqkService.delete(json.getMsg(), user);
        j.setMsg(resultVO);
        return j;
    }

}
