/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtsjController.java
 * 创建日期： 2013-09-02 上午 08:02:33
 * 功能：    服务控制类：合同数据
 * 所含类:   GcHtglHtsjService
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-02 上午 08:02:33  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.htgl.service.GcHtglHtsjService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Pub;

/**
 * <p>
 * GcHtglHtsjController.java
 * </p>
 * <p>
 * 功能：合同数据
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtsjController.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-02
 * 
 */

@Controller
@RequestMapping("/htgl/gcHtglHtsjController")
public class GcHtglHtsjController {

    private static Logger logger = LoggerFactory.getLogger(GcHtglHtsjController.class);

    @Autowired
    private GcHtglHtsjService gcHtglHtsjService;

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
        logger.info("<{}>执行操作【单合同数据查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcHtglHtsjService.queryOne(json.getMsg(), user);
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
        logger.info("<{}>执行操作【合同数据查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        String id = request.getParameter("id");
        if(Pub.empty(id)){
        	domresult = this.gcHtglHtsjService.queryCondition(json.getMsg(), user);
        }else{
	        String  data="{querycondition: {conditions: [{\"value\":\""+id+"\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"}]}}";
	        domresult = this.gcHtglHtsjService.queryCondition(data, user);
        }
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
        logger.info("<{}>执行操作【合同数据新增】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtsjService.insert(json.getMsg(), user);
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
        logger.info("<{}>执行操作【合同数据修改】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtsjService.update(json.getMsg(), user);
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
        logger.info("<{}>执行操作【合同数据删除】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtsjService.delete(json.getMsg(), user);
        j.setMsg(resultVO);
        return j;
    }
    
    /**
     * 更新合同信息记录.
     * 
     * @param request
     * @param json
     * @return
     * @throws Exception
     * @since v1.00
     */
    @RequestMapping(params = "updateHtsj")
    @ResponseBody
    public requestJson updateHtsj(HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【合同数据更新】", user.getName());
        
        
        HashMap map = new HashMap();
        String htsjId = request.getParameter("htsjId");
        map.put("htsjId", htsjId);
        
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtsjService.updateHtsj(json.getMsg(), user, map);
        j.setMsg(resultVO);
        return j;
    }
    
    /**
    * 查询json
    * 
    * @param json
    * @return
    * @throws Exception
    */
   @RequestMapping(params = "queryTzkzdtfx")
   @ResponseBody
   public requestJson queryTzkzdtfx(final HttpServletRequest request, requestJson json) throws Exception {
       User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
       logger.info("<{}>执行操作【投资控制动态分析查询】", user.getName());
       requestJson j = new requestJson();
       
       HashMap map = new HashMap();
       if(request.getParameter("gs")!=null){
    	   map.put("gs", request.getParameter("gs"));
       }
       if(request.getParameter("lbj")!=null){
    	   map.put("lbj", request.getParameter("lbj"));
       }
       if(request.getParameter("xmmc")!=null){
    	   map.put("xmmc", request.getParameter("xmmc"));
       }else{
    	   map.put("xmmc", "");
       }
       if(request.getParameter("nd")!=null){
    	   map.put("nd", request.getParameter("nd"));
       }else{
    	   map.put("nd", (new SimpleDateFormat("yyyy")).format(new Date()));
       }
       
       String domresult = "";
       domresult = this.gcHtglHtsjService.queryTzkzdtfx(json.getMsg(), user,map);
       j.setMsg(domresult);
       return j;

   }
   
   /**
    * 查询项目的合同信息、拦标价信息、概算信息
    * 
    * @param json
    * @return
    * @throws Exception
    */
   @RequestMapping(params = "queryXmxgje")
   @ResponseBody
   public requestJson querySumJe(final HttpServletRequest request, requestJson json) throws Exception {
       User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
       logger.info("<{}>执行操作【查询项目的合同信息、拦标价信息、概算信息】", user.getName());
       requestJson j = new requestJson();
       String domresult = "";
       domresult = this.gcHtglHtsjService.queryXmxgje(json.getMsg(), user);
       j.setMsg(domresult);
       return j;

   }
   
   /**
    * 获取项目投资明细分析数据
    * 
    * @param json
    * @return
    * @throws Exception
    */
   @RequestMapping(params = "queryXmtzmxfx")
   @ResponseBody
   public requestJson queryXmtzmxfx(final HttpServletRequest request, requestJson json) throws Exception {
       User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
       logger.info("<{}>执行操作【查询项目投资明细分析数据】", user.getName());
       requestJson j = new requestJson();
       String domresult = "";
       domresult = this.gcHtglHtsjService.queryXmtzmxfx(json.getMsg(), user);
       j.setMsg(domresult);
       return j;

   }
   
   /**
    * 获取项目投资明细分析数据
    * 
    * @param json
    * @return
    * @throws Exception
    */
   @RequestMapping(params = "queryTzxxk_zfqk")
   @ResponseBody
   public requestJson queryTzxxk_zfqk(final HttpServletRequest request, requestJson json) throws Exception {
       User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
       logger.info("<{}>执行操作【查询资金支付情况数据】", user.getName());
       
       HashMap map = new HashMap();
       //年度
       String nd = request.getParameter("nd");
       map.put("nd", nd);
       //项目id
       String xmid = request.getParameter("xmid");
       map.put("xmid", xmid);
       
       requestJson j = new requestJson();
       String domresult = "";
       domresult = this.gcHtglHtsjService.queryTzxxk_zfqk(json.getMsg(), user, map);
       j.setMsg(domresult);
       return j;

   }
   
   /**
    * 获取项目投资总支付情况
    * 
    * @param json
    * @return
    * @throws Exception
    */
   @RequestMapping(params = "queryTzxxk_zjzfqktj")
   @ResponseBody
   public requestJson queryTzxxk_zjzfqktj(final HttpServletRequest request, requestJson json) throws Exception {
       User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
       logger.info("<{}>执行操作【查询资金支付情况数据】", user.getName());
       
       HashMap map = new HashMap();
       //项目id
       String xmid = request.getParameter("xmid");
       map.put("xmid", xmid);
       
       requestJson j = new requestJson();
       String domresult = "";
       domresult = this.gcHtglHtsjService.queryTzxxk_zjzfqktj(json.getMsg(), user, map);
       j.setMsg(domresult);
       return j;

   }
   
   /**
    * 获取项目投资总支付情况
    * 
    * @param json
    * @return
    * @throws Exception
    */
   @RequestMapping(params = "queryTzxxk_zjzfqktj_zsn")
   @ResponseBody
   public requestJson queryTzxxk_zjzfqktj_zsn(final HttpServletRequest request, requestJson json) throws Exception {
       User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
       logger.info("<{}>执行操作【查询资金支付情况数据】", user.getName());
       
       HashMap map = new HashMap();
       //项目id
       String xmid = request.getParameter("xmid");
       map.put("xmid", xmid);
       
       requestJson j = new requestJson();
       String domresult = "";
       domresult = this.gcHtglHtsjService.queryTzxxk_zjzfqktj_zsn(json.getMsg(), user, map);
       j.setMsg(domresult);
       return j;

   }
   
   /**
    * 获取项目投资总支付情况
    * 
    * @param json
    * @return
    * @throws Exception
    */
   @RequestMapping(params = "queryTzxxk_zjzfqktj_bnd")
   @ResponseBody
   public requestJson queryTzxxk_zjzfqktj_bnd(final HttpServletRequest request, requestJson json) throws Exception {
       User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
       logger.info("<{}>执行操作【查询资金支付情况数据】", user.getName());
       
       HashMap map = new HashMap();
       //项目id
       String xmid = request.getParameter("xmid");
       map.put("xmid", xmid);
       
       requestJson j = new requestJson();
       String domresult = "";
       domresult = this.gcHtglHtsjService.queryTzxxk_zjzfqktj_bnd(json.getMsg(), user, map);
       j.setMsg(domresult);
       return j;

   }
   
   /**
    * 获取项目投资明细分析数据
    * 
    * @param json
    * @return
    * @throws Exception
    */
   @RequestMapping(params = "queryTzxxk_zxqk")
   @ResponseBody
   public requestJson queryTzxxk_zxqk(final HttpServletRequest request, requestJson json) throws Exception {
       User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
       logger.info("<{}>执行操作【查询资金执行情况数据】", user.getName());
       
       HashMap map = new HashMap();
       //年度
       String nd = request.getParameter("nd");
       map.put("nd", nd);
       //项目id
       String xmid = request.getParameter("xmid");
       map.put("xmid", xmid);
       
       requestJson j = new requestJson();
       String domresult = "";
       domresult = this.gcHtglHtsjService.queryTzxxk_zxqk(json.getMsg(), user, map);
       j.setMsg(domresult);
       return j;

   }
}
