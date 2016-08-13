/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtWctzController.java
 * 创建日期： 2013-09-05 下午 06:12:53
 * 功能：    服务控制类：合同完成投资
 * 所含类:   GcHtglHtWctzService
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-05 下午 06:12:53  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.htgl.service.GcHtglHtWctzService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;


/**
 * <p> GcHtglHtWctzController.java </p>
 * <p> 功能：合同完成投资 </p>
 *
 * <p><a href="GcHtglHtWctzController.java.html"><i>查看源代码</i></a></p>  
 *
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-05
 * 
 */

@Controller
@RequestMapping("/htgl/gcHtglHtWctzController")
public class GcHtglHtWctzController {

	private static Logger logger = LoggerFactory.getLogger(GcHtglHtWctzController.class);

    @Autowired
    private GcHtglHtWctzService gcHtglHtWctzService;
    
    /**
     * 查询单合同json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryOne")
    @ResponseBody
    public requestJson queryOne(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【单合同支付数据查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcHtglHtWctzService.queryOne(json.getMsg(), user);
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
        logger.info("<{}>执行操作【合同完成投资查询】",user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcHtglHtWctzService.queryCondition(json.getMsg(), user);
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
        logger.info("<{}>执行操作【合同完成投资新增】",user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtWctzService.insert(json.getMsg(), user);
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
        logger.info("<{}>执行操作【合同完成投资修改】",user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtWctzService.update(json.getMsg(), user);
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
        logger.info("<{}>执行操作【合同完成投资删除】",user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtWctzService.delete(json.getMsg(), user);
        j.setMsg(resultVO);
        return j;
    }

}
