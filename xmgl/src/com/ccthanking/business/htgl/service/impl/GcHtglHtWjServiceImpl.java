/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtWjService.java
 * 创建日期： 2013-09-03 上午 09:40:06
 * 功能：    接口：合同文件
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-03 上午 09:40:06  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl.service.impl;

import java.sql.Connection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

import com.ccthanking.business.htgl.vo.GcHtglHtWjVO;
import com.ccthanking.business.htgl.service.GcHtglHtWjService;
import com.ccthanking.framework.service.impl.Base1ServiceImpl;



/**
 * <p> GcHtglHtWjService.java </p>
 * <p> 功能：合同文件 </p>
 *
 * <p><a href="GcHtglHtWjService.java.html"><i>查看源代码</i></a></p>  
 *
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-03
 * 
 */

@Service
public class GcHtglHtWjServiceImpl extends Base1ServiceImpl<GcHtglHtWjVO, String> implements GcHtglHtWjService {

	private static Logger logger = LoggerFactory.getLogger(GcHtglHtWjServiceImpl.class);
	
	 // 并合同信息查询.
    private static String SQL_QUERY_LIST = "SELECT ghh.htbm, ghh.htmc, ghh.htlx, ghh.fbfs,ghh.htzt, ghh.zhtqdj, t.*"
            + "  FROM gc_htgl_ht_wj t LEFT JOIN gc_htgl_ht ghh ON t.htsjid=ghh.ID";
    
	// 业务类型
    private String ywlx = YwlxManager.GC_HTGL_HT_WJ;

    @Override
    public String queryCondition(String json, User user) throws Exception {
    
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            String orderFilter = RequestUtil.getOrderFilter(json);

            if (!StringUtils.isBlank(condition)) {
            }
            condition += BusinessUtil.getSJYXCondition("ghh");
            condition += BusinessUtil.getCommonCondition(user, "ghh");
            condition += orderFilter;

            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_LIST, page);

            // 设置字典
            bs.setFieldDic("BGLX", "BGLX");
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式


            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同文件>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同文件查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
    
    @Override
    public String insert(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtWjVO xmvo = new GcHtglHtWjVO();

        try {
            conn.setAutoCommit(false);
            JSONArray list = xmvo.doInitJson(json);
            xmvo.setValueFromJson((JSONObject) list.get(0));
            // 设置主键
            xmvo.setId(new RandomGUID().toString()); // 主键
            //
            xmvo.setLrr(user.getAccount()); // 更新人
            xmvo.setLrsj(Pub.getCurrentDate()); // 更新时间
            xmvo.setLrbm(user.getDepartment());// 录入人单位
            xmvo.setLrbmmc(user.getOrgDept().getDeptName());// 录入人单位名称
            xmvo.setYwlx(ywlx);

            EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);// 生成事件
            xmvo.setSjbh(eventVO.getSjbh());

            // 插入
            BaseDAO.insert(conn, xmvo);
            resultVO = xmvo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同文件新增成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同文件新增失败!");
            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同文件新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtWjVO vo = new GcHtglHtWjVO();

        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            vo.setValueFromJson((JSONObject) list.get(0));
            // 设置主键
            /*
             * vo.setId(new RandomGUID().toString()); // 主键
             *//* vo.setId("20FC7EF9-696D-6398-15C8-A77F2C4DFC02"); */
            //
            vo.setGxr(user.getAccount()); // 更新人
            vo.setGxsj(Pub.getCurrentDate()); // 更新时间
            vo.setGxbm(user.getDepartment());// 更新人单位
            vo.setGxbmmc(user.getOrgDept().getDeptName());// 更新人单位名称
            vo.setYwlx(ywlx);

            // 插入
            BaseDAO.update(conn, vo);
            resultVO = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同文件修改成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同文件修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同文件修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcHtglHtWjVO vo = new GcHtglHtWjVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            BaseDAO.delete(conn, vo);

            resultVo = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同文件删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同文件删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同文件删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }
    
}
