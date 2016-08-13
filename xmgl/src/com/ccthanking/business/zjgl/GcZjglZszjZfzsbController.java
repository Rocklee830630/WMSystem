/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglZszjZfzsbController.java
 * 创建日期： 2013-08-30 上午 12:49:28
 * 功能：    服务控制类：支付征收办
 * 所含类:   GcZjglZszjZfzsbService
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-08-30 上午 12:49:28  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.zjgl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.zjgl.service.GcZjglZszjZfzsbService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * <p>
 * GcZjglZszjZfzsbController.java
 * </p>
 * <p>
 * 功能：支付征收办
 * </p>
 * 
 * <p>
 * <a href="GcZjglZszjZfzsbController.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-08-30
 * 
 */

@Controller
@RequestMapping("/zjgl/gcZjglZszjZfzsbController")
public class GcZjglZszjZfzsbController {

    private static Logger logger = LoggerFactory.getLogger(GcZjglZszjZfzsbController.class);

    @Autowired
    private GcZjglZszjZfzsbService gcZjglZszjZfzsbService;

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
        logger.info("<{}>执行操作【征收资金查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";

        HashMap map = new HashMap();

        //项目年度
        String nd = request.getParameter("nd");
        map.put("nd", nd);

        domresult = this.gcZjglZszjZfzsbService.queryQyList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }

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
        logger.info("<{}>执行操作【支付征收办查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcZjglZszjZfzsbService.queryCondition(json.getMsg(), user);
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
        logger.info("<{}>执行操作【支付征收办新增】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglZszjZfzsbService.insert(json.getMsg(), user);
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
        logger.info("<{}>执行操作【支付征收办修改】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglZszjZfzsbService.update(json.getMsg(), user);
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
        logger.info("<{}>执行操作【支付征收办删除】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglZszjZfzsbService.delete(json.getMsg(), user);
        j.setMsg(resultVO);
        return j;
    }
    
    /**
     * 查询区域列表json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryZjInfo")
    @ResponseBody
    public requestJson queryZjInfo(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【征收资金查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";

        HashMap map = new HashMap();

        //项目年度
        String nd = request.getParameter("nd");
        map.put("nd", nd);

        domresult = this.gcZjglZszjZfzsbService.queryQueryZjInfo(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }
    
    /**
     * 查询合计拨付金额json
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "querySumJe")
    @ResponseBody
    public requestJson querySumJe(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【支付征收办查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcZjglZszjZfzsbService.querySumJe(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }

}
