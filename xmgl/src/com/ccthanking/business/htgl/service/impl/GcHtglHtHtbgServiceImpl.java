/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtHtbgService.java
 * 创建日期： 2013-09-05 下午 06:13:49
 * 功能：    接口：合同变更
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-05 下午 06:13:49  蒋根亮   创建文件，实现基本功能
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
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

import com.ccthanking.business.htgl.vo.GcHtglHtHtbgVO;
import com.ccthanking.business.htgl.service.GcHtglHtHtbgService;
import com.ccthanking.framework.service.impl.Base1ServiceImpl;

/**
 * <p>
 * GcHtglHtHtbgService.java
 * </p>
 * <p>
 * 功能：合同变更
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtHtbgService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-05
 * 
 */

@Service
public class GcHtglHtHtbgServiceImpl extends Base1ServiceImpl<GcHtglHtHtbgVO, String> implements GcHtglHtHtbgService {

    private static Logger logger = LoggerFactory.getLogger(GcHtglHtHtbgServiceImpl.class);

    // 并合同信息查询.
    private static String SQL_QUERY_LIST = "SELECT ghh.htbm, ghh.htmc, ghh.htlx, ghh.fbfs,ghh.htzt, ghh.zhtqdj, t.*"
            + "  FROM gc_htgl_ht_htbg t LEFT JOIN gc_htgl_ht ghh ON t.htsjid=ghh.ID";

    private static String SQL_QUERY_LISTONE = "SELECT ghh.htbm, ghh.htmc, ghh.htlx, ghh.fbfs,ghh.htzt, ghh.zhtqdj, "
            + "gjs.xmmc, gjs.bdmc, ghh2.htid, ghh2.jhsjid,t.id, t.htsjid, t.bglx, t.bgmc, t.bgje, t.bgts, t.bgrq, t.bgly "
            + "FROM gc_htgl_ht_htbg t LEFT JOIN gc_htgl_htsj ghh2 ON t.htsjid=ghh2.ID "
            + "LEFT JOIN gc_htgl_ht ghh ON ghh2.htid=ghh.ID LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid";

    // 业务类型
    private String ywlx = YwlxManager.GC_HTGL_HT_HTBG;

    // 更新 总合同变更金额
    private static String SQL_UPDATE_ZBGJE = "update gc_htgl_ht ghh set ghh.zbgje=(SELECT SUM(ghhh.bgje)  FROM gc_htgl_ht_htbg ghhh, gc_htgl_htsj ghh2 WHERE ghhh.htsjid=ghh2.id AND ghh2.htid=ghh.id) where ghh.id in (SELECT y.htid FROM  gc_htgl_ht_htbg x, gc_htgl_htsj y WHERE x.htsjid=y.id AND x.id=? )";

    @Override
    public String queryOne(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            QueryConditionList list = RequestUtil.getConditionList(json);

            // ghh.htid需要合同号
            String condition = list == null ? "" : list.getConditionWhere();

            // 组织查询条件
            String orderFilter = RequestUtil.getOrderFilter(json);

            condition += BusinessUtil.getSJYXCondition("t");// 是否有效
            condition += BusinessUtil.getCommonCondition(user, "t");// 预留
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_LISTONE, page);

            // 设置字典
            bs.setFieldDic("BGLX", "BGLX");
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldThousand("BGJE");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同变更>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同变更查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

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

            // 设置查询条件
            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份
            // bs.setFieldThousand("DYJLSDZ");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同变更>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同变更查询失败!");
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
        GcHtglHtHtbgVO xmvo = new GcHtglHtHtbgVO();

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

            DBUtil.executeUpdate(SQL_UPDATE_ZBGJE, new Object[] { xmvo.getId() });

            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同变更新增成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同变更新增失败!");
            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同变更新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtHtbgVO vo = new GcHtglHtHtbgVO();

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
            // EventVO eventVO = EventManager.createEvent(conn, vo.getYwlx(),
            // user);// 生成事件
            // vo.setSjbh(eventVO.getSjbh());

            // 插入
            BaseDAO.update(conn, vo);
            resultVO = vo.getRowJson();
            conn.commit();

            DBUtil.executeUpdate(SQL_UPDATE_ZBGJE, new Object[] { vo.getId() });

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同变更修改成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同变更修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同变更修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcHtglHtHtbgVO vo = new GcHtglHtHtbgVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            BaseDAO.delete(conn, vo);

            resultVo = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同变更删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同变更删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同变更删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }

}
