/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglZszjXmzszjqkController.java
 * 创建日期： 2013-08-30 上午 12:52:21
 * 功能：    服务控制类：项目征收资金情况
 * 所含类:   GcZjglZszjXmzszjqkService
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-08-30 上午 12:52:21  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.zjgl;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.ccthanking.business.zjgl.service.GcZjglZszjXmzszjqkService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;

/**
 * <p>
 * GcZjglZszjXmzszjqkController.java
 * </p>
 * <p>
 * 功能：项目征收资金情况
 * </p>
 * 
 * <p>
 * <a href="GcZjglZszjXmzszjqkController.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-08-30
 * 
 */

@Controller
@RequestMapping("/zjgl/gcZjglZszjXmzszjqkController")
public class GcZjglZszjXmzszjqkController {

    private static Logger logger = LoggerFactory.getLogger(GcZjglZszjXmzszjqkController.class);

    @Autowired
    private GcZjglZszjXmzszjqkService gcZjglZszjXmzszjqkService;

    /**
     * 查询项目下达库json
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryXdxmkZszj")
    @ResponseBody
    public requestJson queryXdxmkZszj(final HttpServletRequest request, requestJson json) throws Exception {
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcZjglZszjXmzszjqkService.queryXdxmk(json.getMsg(), request);
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
    @RequestMapping(params = "queryGroup")
    @ResponseBody
    public requestJson queryGroup(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【项目征收资金情况查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcZjglZszjXmzszjqkService.queryConditionGroup(json.getMsg(), user);
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
        logger.info("<{}>执行操作【项目征收资金情况查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcZjglZszjXmzszjqkService.queryCondition(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 查询项目下达库json
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryXdxmk")
    @ResponseBody
    public requestJson queryXdxmk(final HttpServletRequest request, requestJson json) throws Exception {
        String yjxm = request.getParameter("yjxm");
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcZjglZszjXmzszjqkService.queryXdxmk(json.getMsg(), user, yjxm);
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
        logger.info("<{}>执行操作【项目征收资金情况新增】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglZszjXmzszjqkService.insert(json.getMsg(), user);
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
        logger.info("<{}>执行操作【项目征收资金情况修改】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglZszjXmzszjqkService.update(json.getMsg(), user);
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
        logger.info("<{}>执行操作【项目征收资金情况删除】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcZjglZszjXmzszjqkService.delete(json.getMsg(), user);
        j.setMsg(resultVO);
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
        domresult = this.gcZjglZszjXmzszjqkService.querySumJe(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 提供一些公共查询，返回json格式数据. <br/>
     * 自动获取以"qc_"开头的参数
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryCommon")
    @ResponseBody
    public requestJson queryCommon(final HttpServletRequest request, requestJson json, @RequestParam("opttype") String opttype)
            throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【queryCommon查询】", user.getName());
        requestJson j = new requestJson();

        TreeMap<String, Object> map1 = (TreeMap<String, Object>) WebUtils.getParametersStartingWith(request, "qc_");

        HashMap map = new HashMap();
        map.put("opttype", opttype);
        map.put("cond", map1);

        String domresult = "";
        domresult = this.gcZjglZszjXmzszjqkService.queryCommon(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }

}
