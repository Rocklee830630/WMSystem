/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtController.java
 * 创建日期： 2013-08-28 下午 11:00:58
 * 功能：    服务控制类：合同信息
 * 所含类:   GcHtglHtService
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-08-28 下午 11:00:58  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccthanking.business.htgl.service.GcHtglHtService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Pub;

/**
 * <p>
 * GcHtglHtController.java
 * </p>
 * <p>
 * 功能：合同信息
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtController.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-08-28
 * 
 */

@Controller
@RequestMapping("/htgl/gcHtglHtController")
public class GcHtglHtController {

    private static Logger logger = LoggerFactory.getLogger(GcHtglHtController.class);

    @Autowired
    private GcHtglHtService gcHtglHtService;

    /**
     * 查询合同json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "querySgHt")
    @ResponseBody
    public requestJson querySgHt(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.debug("<{}>执行操作【施工类合同信息查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcHtglHtService.querySgHt(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 查询合同json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryHt")
    @ResponseBody
    public requestJson queryHt(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.debug("<{}>执行操作【合同信息查询4选择】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcHtglHtService.queryHt(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }
    
    /**
     * 查询合同json.
     * 针对部门提请款进行的查询，
     * 条件为履行状态、或结算状态内且支付比未到100%的合同
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryHtOnlyTqkbm")
    @ResponseBody
    public requestJson queryHtzjxxOnlyTqkbm(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.debug("<{}>执行操作【合同信息查询4选择】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcHtglHtService.queryHtOnlyTqkbm(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 根据计划数据ID查询单位名称及ID.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryDwxxByJhid")
    @ResponseBody
    public requestJson queryDwxxByJhid(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【单位信息查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";

        HashMap map = new HashMap();
        String opttype = request.getParameter("opttype");
        map.put("opttype", opttype);

        String htid = request.getParameter("htid");
        map.put("htid", htid);

        domresult = this.gcHtglHtService.queryOther(json.getMsg(), user, map);
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
        logger.info("<{}>执行操作【合同信息查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        String id = request.getParameter("id");
        if (Pub.empty(id)) {
            domresult = this.gcHtglHtService.queryCondition(json.getMsg(), user);
        } else {
            String data = "{querycondition: {conditions: [{\"value\":\"" + id
                    + "\",\"fieldname\":\"ghh.id\",\"operation\":\"=\",\"logic\":\"and\"}]}}";
            domresult = this.gcHtglHtService.queryCondition(data, user);
        }
        // domresult = this.gcHtglHtService.queryCondition(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }
    
    /**
     * 部门合同补存招投标查询
     * @param request
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryConditionForZtb")
    @ResponseBody
    public requestJson queryConditionForZtb(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【合同信息查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = this.gcHtglHtService.queryConditionForZtb(json.getMsg(), user, request);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 对合同主体补存招投标信息
     * @param request
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "bcZtbxx")
    @ResponseBody
    public String bcZtbxx(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【合同补录招投标信息】", user.getName());
        String domresult = this.gcHtglHtService.bcZtbxx(request);
        return domresult;

    }

    /**
     * 查询合同信息json
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryBySJBH")
    @ResponseBody
    public requestJson queryBySJBH(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【合同信息查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcHtglHtService.queryBySJBH(json.getMsg(), user);
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
        logger.info("<{}>执行操作【合同信息新增】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";

        // 附件带上业务主键
        HashMap map = new HashMap();
        String ywid = request.getParameter("ywid");
        map.put("ywid", ywid);

        String xmxxfrom = request.getParameter("xmxxfrom");
        map.put("xmxxfrom", xmxxfrom);
        String ids = request.getParameter("ids");
        map.put("ids", ids);

        // 项目ID
        String xmids = request.getParameter("xmids");
        map.put("xmids", xmids);

        // 标段ID
        String bdids = request.getParameter("bdids");
        map.put("bdids", bdids);

        // 金额
        String jes = request.getParameter("jes");
        map.put("jes", jes);
        
        // 排迁子任务ID
        String pqzxmid = request.getParameter("pqzxmid");
        map.put("pqzxmid", pqzxmid);

        // 履约保证金
        String bzjId = request.getParameter("bzjId");
        map.put("bzjId", bzjId);

        // 其它操作
        String opttype = request.getParameter("opttype");
        map.put("opttype", opttype);

        String condition = request.getParameter("condition");
        map.put("condition", condition);
        
        resultVO = this.gcHtglHtService.insert(json.getMsg(), user, map);
        j.setMsg(resultVO);

        // this.gcHtglHtService.updateHtZHSJS(json.getMsg(), user, map2);

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
        logger.info("<{}>执行操作【合同信息修改】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        if (StringUtils.isNotBlank(request.getParameter("id"))) {
            // 仅更新合同状态字段
            // 独特更新
            String id = request.getParameter("id");
            String opttype = request.getParameter("opttype");
            resultVO = this.gcHtglHtService.update(json.getMsg(), user, id, opttype);
        } else {
        	
            HashMap map = new HashMap();

            String xmxxfrom = request.getParameter("xmxxfrom");
            map.put("xmxxfrom", xmxxfrom);
            String ids = request.getParameter("ids");
            map.put("ids", ids);

            // 项目ID
            String xmids = request.getParameter("xmids");
            map.put("xmids", xmids);

            // 标段ID
            String bdids = request.getParameter("bdids");
            map.put("bdids", bdids);

            // 排迁子任务ID
            String pqzxmid = request.getParameter("pqzxmid");
            map.put("pqzxmid", pqzxmid);

            // 履约保证金
            String bzjId = request.getParameter("bzjId");
            map.put("bzjId", bzjId);

            // 标段变更
            String bdChange = request.getParameter("bdChange");
            map.put("bdChange", bdChange);
            
            // 金额
            String jes = request.getParameter("jes");
            map.put("jes", jes);
            
            resultVO = this.gcHtglHtService.update(json.getMsg(), user,map);
        }
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
        logger.info("<{}>执行操作【合同信息删除】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtService.delete(json.getMsg(), user);
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
    @RequestMapping(params = "updateHtztToYJS")
    @ResponseBody
    public requestJson updateHtztToYJS(HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        String htid = request.getParameter("htid");
        logger.info("<{}>执行操作【合同信息删除】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtService.updateHtztToYJS(json.getMsg(), user, htid);
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
    @RequestMapping(params = "updateHtZHSJS")
    @ResponseBody
    public requestJson updateHtsj(HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【合同数据更新】", user.getName());

        HashMap map = new HashMap();
        String htId = request.getParameter("htId");
        map.put("htId", htId);

        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtService.updateHtZHSJS(json.getMsg(), user, map);
        j.setMsg(resultVO);
        return j;
    }

    /**
     * 查询合同造价信息
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryHtzjxx")
    @ResponseBody
    public requestJson queryHtzjxx(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【合同数据查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcHtglHtService.queryHtzjxx(json.getMsg(), user);
        j.setMsg(domresult);
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
    @RequestMapping(params = "updateHtjs")
    @ResponseBody
    protected requestJson updateHtjs(HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【合同jie'suan信息修改】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtService.updateHtjs(json.getMsg(), user);
        j.setMsg(resultVO);
        return j;
    }

    /**
     * 修改合同编号
     * 
     * @param request
     * @param json
     * @return
     * @throws Exception
     * @since v1.00
     */
    @RequestMapping(params = "updateHtbh")
    @ResponseBody
    protected requestJson updateHtbh(HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【合同编号信息修改】", user.getName());
        requestJson j = new requestJson();
        String resultVO = "";
        resultVO = this.gcHtglHtService.updateHtbh(json.getMsg(), user, request);
        j.setMsg(resultVO);
        return j;
    }

    /**
     * 查询部门合同概况by HTLX
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryHtgk")
    @ResponseBody
    public requestJson queryHtgk(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【合同信息查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        domresult = this.gcHtglHtService.queryCondition(json.getMsg(), user);
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
    @RequestMapping(params = "queryHtList")
    @ResponseBody
    public requestJson queryHtList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【征收资金查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";

        HashMap map = new HashMap();

        // 项目年度
        String nd = request.getParameter("nd");
        map.put("nd", nd);

        domresult = this.gcHtglHtService.queryHtList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 查询投资控制动态分析_合同列表json.
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryTzjkHtList")
    @ResponseBody
    public requestJson queryTzjkHtList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【征收资金查询】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";

        HashMap map = new HashMap();

        // 项目年度
        String nd = request.getParameter("nd");
        map.put("nd", nd);

        domresult = this.gcHtglHtService.queryTzjkHtList(json.getMsg(), user, map);
        j.setMsg(domresult);
        return j;

    }

    /**
     * 合同编码查询--自动完成.<br/>
     * 
     * @param request
     * @param match
     * @return
     * @throws Exception
     * @since v1.00
     */
    @RequestMapping(params = "htbmAutoComplete")
    @ResponseBody
    public List<autocomplete> htbmAutoComplete(final HttpServletRequest request, autocomplete match) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        HashMap map = new HashMap();
        List<autocomplete> list = this.gcHtglHtService.htbmAutoQuery(match, user, map);
        return list;
    }

    /**
     * 查询履约保证金信息.<br/>
     * 
     * @param request
     * @param match
     * @return
     * @throws Exception
     * @since v1.00
     */
    @RequestMapping(params = "queryZtbSjrws")
    @ResponseBody
    public String queryZtbLybzj(final HttpServletRequest request, autocomplete match) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【履约保证金查询】", user.getName());
        HashMap map = new HashMap();
        String domresult = "";
        map.put("ztbId", request.getParameter("ztbId"));
        map.put("jhsjids", request.getParameter("jhsjids"));
        domresult = this.gcHtglHtService.queryZtbSjrws(user, map);
        return domresult;
    }

    /**
     * 查询履约保证金信息by HTLX
     * 
     * @param json
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "queryLybzjList")
    @ResponseBody
    public requestJson queryLybzjList(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【履约保证金查询】", user.getName());
        requestJson j = new requestJson();
        HashMap map = new HashMap();
        String domresult = "";
        map.put("ztbId", request.getParameter("ztbId"));
        domresult = this.gcHtglHtService.queryLybzjList(json.getMsg(), user, map);
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
    @RequestMapping(params = "queryIndex")
    @ResponseBody
    public requestJson queryIndex(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        logger.info("<{}>执行操作【合同信息查询-合同综合管理页面】", user.getName());
        requestJson j = new requestJson();
        String domresult = "";
        Map map = new HashMap();
        map.put("wcrid", request.getParameter("wcrid"));
        map.put("kssj", request.getParameter("kssj"));
        map.put("jssj", request.getParameter("jssj"));
        map.put("htid", request.getParameter("htid"));
        domresult = this.gcHtglHtService.queryConditionForIndex(json.getMsg(), user, map);
       
        // domresult = this.gcHtglHtService.queryCondition(json.getMsg(), user);
        j.setMsg(domresult);
        return j;

    }
    
    @ResponseBody
    @RequestMapping(params = "queryIsCf")
    public String queryIsCf(final HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    	String json = null;
    	try {
    		json = gcHtglHtService.queryIsCf(user, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return json;
    }
    
    @RequestMapping(params = "deleteHTxm")
    @ResponseBody
    public requestJson deleteHTxm(final HttpServletRequest request, requestJson json) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        requestJson j = new requestJson();
        String domresult = "";
        this.gcHtglHtService.deleteHTxm(json.getMsg(), user, request);
        j.setMsg(domresult);
        return j;

    }
}
