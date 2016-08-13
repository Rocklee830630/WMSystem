/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglZszjXmzszjqkService.java
 * 创建日期： 2013-08-30 上午 12:52:21
 * 功能：    接口：项目征收资金情况
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-08-30 上午 12:52:21  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.zjgl.service.impl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryCondition;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

import com.ccthanking.business.zjgl.vo.GcZjglZszjXmzszjqkVO;
import com.ccthanking.business.zjgl.service.GcZjglZszjXmzszjqkService;
import com.ccthanking.framework.service.impl.Base1ServiceImpl;

/**
 * <p>
 * GcZjglZszjXmzszjqkService.java
 * </p>
 * <p>
 * 功能：项目征收资金情况
 * </p>
 * 
 * <p>
 * <a href="GcZjglZszjXmzszjqkService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-08-30
 * 
 */

@Service
public class GcZjglZszjXmzszjqkServiceImpl extends Base1ServiceImpl<GcZjglZszjXmzszjqkVO, String> implements GcZjglZszjXmzszjqkService {

    private static Logger logger = LoggerFactory.getLogger(GcZjglZszjXmzszjqkServiceImpl.class);

    // 按项目分组资金生收情况
    private static String SQL_QUERY_GROUP = "SELECT t.xmid, gjs.xmmc,gjs.xmbh, sum(t.zfje) AS zfje , COUNT(*) AS zfcs FROM gc_zjgl_zszj_xmzszjqk t LEFT JOIN gc_jh_sj gjs ON t.xmid=gjs.gc_jh_sj_id ";

    // 查询列表
    private static String SQL_QUERY_SQL = "SELECT gjs.xmmc, gjs.xmbh, gjs.bdmc, t.*  FROM gc_zjgl_zszj_xmzszjqk t, gc_jh_sj gjs ";
    // 业务类型
    private String ywlx = YwlxManager.GC_ZJGL_ZSZJ_XMZSZJQK;

    @Override
    public String queryConditionGroup(String json, User user) throws Exception {
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            QueryConditionList list = RequestUtil.getConditionList(json);

            String condition = list == null ? "" : list.getConditionWhere();

            String orderFilter = RequestUtil.getOrderFilter(json);

            // 增加sql条件
            condition += "";
            condition += BusinessUtil.getSJYXCondition("t");
            condition += BusinessUtil.getCommonCondition(user, "t");
            condition += " GROUP BY t.xmid, gjs.xmmc, gjs.xmbh ";// 分组
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_GROUP, page);

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<项目征收资金分组情况>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("项目征收资金情况查询失败!");
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
            StringBuilder condition = new StringBuilder(" t.jhsjid=gjs.gc_jh_sj_id  ");
            QueryConditionList condiList = RequestUtil.getConditionList(json);
            if (condiList.size() > 0) {
                for (int i = 0; i < condiList.size(); i++) {
                    QueryCondition condi = condiList.getQueryCondition(i);
                    if ("ZFRQ".equalsIgnoreCase(condi.getField()) && StringUtils.isNotBlank(condi.getValue())) {
                        condition.append("  and to_char(ZFRQ, 'YYYY') ='" + condi.getValue() + "' ");
                    } else if ("ZFRQB".equalsIgnoreCase(condi.getField()) && StringUtils.isNotBlank(condi.getValue())) {
                        condition.append("  AND t.zfrq " + condi.getOperation() + " TO_DATE('" + condi.getValue() + "', '"
                                + condi.getFieldFormat() + "') ");
                    } else if ("ZFRQE".equalsIgnoreCase(condi.getField()) && StringUtils.isNotBlank(condi.getValue())) {
                        condition.append("  AND t.zfrq " + condi.getOperation() + " TO_DATE('" + condi.getValue() + "', '"
                                + condi.getFieldFormat() + "') ");
                    } else {
                        condition.append("  AND " + condi.getField() + " " + condi.getOperation() + " '" + condi.getValue() + "' ");
                    }
                }
            }

            String orderFilter = RequestUtil.getOrderFilter(json);
            condition.append(BusinessUtil.getSJYXCondition("t"));
            condition.append(BusinessUtil.getCommonCondition(user, null));
            condition.append(orderFilter);
            if (page == null)
                page = new PageManager();
            page.setFilter(condition.toString());

            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_SQL, page);

            bs.setFieldThousand("ZFJE");
            bs.setFieldDic("QY", "QY");// 请款类型

            bs.setFieldUserID("LXR");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<项目征收资金情况>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("项目征收资金情况查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

    @Override
    public String queryXdxmk(String json, User user, String yjxm) throws Exception {
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            String orderFilter = RequestUtil.getOrderFilter(json);
            if (!StringUtils.isBlank(yjxm)) {
                condition += " AND t.XMSX = '" + yjxm + "' ";
            }
            if (!StringUtils.isBlank(condition)) {
                condition += " AND t.jhid = t1.GC_JH_ZT_ID AND t.xmbh = t2.xmbh and t1.sfyx='1' AND t1.ISXF='1' ";
            } else {
                condition += " t.jhid = t1.GC_JH_ZT_ID AND t.xmbh = t2.xmbh and t1.sfyx='1' AND t1.ISXF='1' ";
            }
            condition += BusinessUtil.getSJYXCondition("t");
            condition += BusinessUtil.getCommonCondition(user, "t");
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            String sql = "SELECT "
                    + "t.gc_jh_sj_id,t.xmbh,t.xmmc,t.nd,t.bdbh,t.bdmc,t.XMXZ,t.XMSX,t.JHID,"
                    + "t1.GC_JH_ZT_ID,t1.ND as jhnd,t1.JHMC,t1.ISXF,"
                    + "t2.GC_TCJH_XMXDK_ID,t2.XMGLGS,t2.FZR_GLGS,t2.xmdz,t2.xmlx,t2.YZDB,t2.SGDW,t2.FZR_SGDW,t2.JLDW,t2.FZR_JLDW,t2.SJDW,t2.FZR_SJDW,t2.JSNR,t2.WGRQ "
                    + "FROM " + "GC_JH_SJ t,GC_JH_ZT t1,GC_TCJH_XMXDK t2";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            bs.setFieldDic("ISXF", "SF");
            bs.setFieldDic("XMSX", "XMSX");
            bs.setFieldDic("XMGLGS", "XMGLGS");
            // 日期
            bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
            domresult = bs.getJson();
        } catch (Exception e) {
            e.printStackTrace(System.out);

        } finally {
            if (conn != null)
                conn.close();
        }
        return domresult;
    }

    @Override
    public String insert(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglZszjXmzszjqkVO xmvo = new GcZjglZszjXmzszjqkVO();

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
                    .getOrgDept().getDeptName() + " " + user.getName() + "项目征收资金情况新增成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + xmvo.getId()
                    + "\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("项目征收资金情况新增失败!");
            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "项目征收资金情况新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglZszjXmzszjqkVO vo = new GcZjglZszjXmzszjqkVO();

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
                    .getDeptName() + " " + user.getName() + "项目征收资金情况修改成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + vo.getId()
                    + "\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("项目征收资金情况修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "项目征收资金情况修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcZjglZszjXmzszjqkVO vo = new GcZjglZszjXmzszjqkVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            BaseDAO.delete(conn, vo);

            resultVo = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "项目征收资金情况删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("项目征收资金情况删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "项目征收资金情况删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }

    @Override
    public String queryXdxmk(String json, HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            boolean fullQuery = false;
            if (condition.indexOf("bmtqkmxzt") != -1) {
                fullQuery = true;
            }
            String orderFilter = RequestUtil.getOrderFilter(json);
            /**
             * if(!StringUtils.isBlank(yjxm)){ condition +=
             * " AND t.XMSX = '"+yjxm+"' "; }
             * if(!StringUtils.isBlank(condition)){ condition +=
             * " AND t.xmid = t2.GC_TCJH_XMXDK_ID and t1.sfyx='1' AND t1.ISXF='1' "
             * ; }else{ condition +=
             * " t.jhid = t1.GC_JH_ZT_ID AND t.xmbh = t2.xmbh and t1.sfyx='1' AND t1.ISXF='1' "
             * ; }
             **/
            if (fullQuery) {
                condition += " AND t.xmid = t2.GC_TCJH_XMXDK_ID AND t.gc_jh_sj_id= vzzzt.jhsjid ";
            } else {
                condition += " AND t.xmid = t2.GC_TCJH_XMXDK_ID ";
            }
            condition += BusinessUtil.getSJYXCondition("t");
            condition += BusinessUtil.getCommonCondition(user, "t");
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            String sql = "SELECT "
                    + "t.GC_JH_SJ_ID,t.JHID,t.xmbs,t.ND,t.XMID,t.BDID,t.XMBH,t.BDBH,t.XMMC,t.BDMC,t.XMXZ,t.PXH,t.KGSJ,t.KGSJ_SJ,t.KGSJ_BZ,t.WGSJ,t.WGSJ_SJ,t.WGSJ_BZ,t.KYPF,t.KYPF_SJ,t.KYPF_BZ,t.HPJDS,t.HPJDS_SJ,t.HPJDS_BZ,t.GCXKZ,t.GCXKZ_SJ,t.GCXKZ_BZ,t.SGXK,t.SGXK_SJ,t.SGXK_BZ,t.CBSJPF,t.CBSJPF_SJ,t.CBSJPF_BZ,t.CQT,t.CQT_SJ,t.CQT_BZ,t.PQT,t.PQT_SJ,t.PQT_BZ,t.SGT,t.SGT_SJ,t.SGT_BZ,t.TBJ,t.TBJ_SJ,t.TBJ_BZ,t.CS,t.CS_SJ,t.CS_BZ,t.JLDW jh_jldw,t.JLDW_SJ,t.JLDW_BZ,t.SGDW jh_sgdw,t.SGDW_SJ,t.SGDW_BZ,t.ZC,t.ZC_SJ,t.ZC_BZ,t.PQ,t.PQ_SJ,t.PQ_BZ,t.JG,t.JG_SJ,t.JG_BZ,t.XMSX,t.YWLX,t.SJBH,t.BZ,t.LRR,t.LRSJ,t.LRBM,t.LRBMMC,t.GXR,t.GXSJ,t.GXBM,t.GXBMMC,t.SJMJ,t.SFYX,t.XFLX,t.ISXF,t2.GC_TCJH_XMXDK_ID,t2.XMGLGS,t2.FZR_GLGS,t2.xmlx,t2.YZDB,t2.SJDW,t2.FZR_SJDW,t2.JSNR,t2.WGRQ,t2.LXFS_GLGS,t2.LXFS_SJDW,t2.LXFS_YZDB, "
                    + "(case xmbs when '0' then (select sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t.bdid) end) sgdw, "
                    + "(case xmbs when '0' then (select fzr_sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select sgdwfzr from GC_XMBD where GC_XMBD_ID = t.bdid) end) FZR_SGDW, "
                    + "(case xmbs when '0' then (select jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select jldw from GC_XMBD where GC_XMBD_ID = t.bdid) end) JLDW, "
                    + "(case xmbs when '0' then (select fzr_jldw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select jldwfzr from GC_XMBD where GC_XMBD_ID = t.bdid) end) FZR_JLDW, "
                    + "(case xmbs when '0' then (select lxfs_sgdw from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select sgdwfzrlxfs from GC_XMBD where GC_XMBD_ID = t.bdid) end) LXFS_SGDW, "
                    + "(case xmbs when '0' then (select LXFS_JLDW from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id = t.xmid) when '1' then (select jldwfzrlxfs from GC_XMBD where GC_XMBD_ID = t.bdid) end) LXFS_JLDW, "
                    + "(case t.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1) end ) XMDZ ";

            if (fullQuery) {
                sql += "FROM " + "GC_JH_SJ t,GC_TCJH_XMXDK t2, view_zjgl_zszj_zfzsb_tqk vzzzt ";
            } else {
                sql += "FROM " + "GC_JH_SJ t,GC_TCJH_XMXDK t2 ";
            }

            BaseResultSet bs = DBUtil.query(conn, sql, page);
            bs.setFieldDic("ISXF", "SF");
            bs.setFieldDic("XMSX", "XMSX");
            bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");// 项目管理公司
            bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
            bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
            bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
            bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");// 业主代表
            bs.setFieldTranslater("FZR_GLGS", "FS_ORG_PERSON", "ACCOUNT", "NAME");// 项目管理公司负责人
            // 日期
            bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
            domresult = bs.getJson();
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String querySumJe(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            StringBuilder condition = new StringBuilder(" t.jhsjid=gjs.gc_jh_sj_id  ");
            QueryConditionList condiList = RequestUtil.getConditionList(json);
            if (condiList.size() > 0) {
                for (int i = 0; i < condiList.size(); i++) {
                    QueryCondition condi = condiList.getQueryCondition(i);
                    if ("ZFRQ".equalsIgnoreCase(condi.getField()) && StringUtils.isNotBlank(condi.getValue())) {
                        condition.append("  and to_char(ZFRQ, 'YYYY') ='" + condi.getValue() + "' ");
                    } else if ("ZFRQB".equalsIgnoreCase(condi.getField()) && StringUtils.isNotBlank(condi.getValue())) {
                        condition.append("  AND t.zfrq " + condi.getOperation() + " TO_DATE('" + condi.getValue() + "', '"
                                + condi.getFieldFormat() + "') ");
                    } else if ("ZFRQE".equalsIgnoreCase(condi.getField()) && StringUtils.isNotBlank(condi.getValue())) {
                        condition.append("  AND t.zfrq " + condi.getOperation() + " TO_DATE('" + condi.getValue() + "', '"
                                + condi.getFieldFormat() + "') ");
                    } else {
                        condition.append("  AND " + condi.getField() + " " + condi.getOperation() + " '" + condi.getValue() + "' ");
                    }
                }
            }

            String orderFilter = RequestUtil.getOrderFilter(json);
            condition.append(BusinessUtil.getSJYXCondition("t"));
            condition.append(BusinessUtil.getCommonCondition(user, null));
            condition.append(orderFilter);
            if (page == null)
                page = new PageManager();
            page.setFilter(condition.toString());

            String sql = " SELECT sum(t.ZFJE) ZFJE  FROM gc_zjgl_zszj_xmzszjqk t, gc_jh_sj gjs ";
            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, sql, page);

            bs.setFieldThousand("ZFJE");
            bs.setFieldDic("QY", "QY");// 请款类型

            bs.setFieldUserID("LXR");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<项目征收资金情况>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("项目征收资金情况查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

    @Override
    public String queryCommon(String msg, User user, HashMap map) {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {
            String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
            if (opttype.equals("xmbd")) {
                // 根据jhsjid 查询项目 标段信息 直接返回json串
                Map<String, Object> m_cond = (Map<String, Object>) map.get("cond");
                String id = (String) m_cond.get("jhsjid");
                String sqlString = "SELECT * FROM gc_jh_sj WHERE gc_jh_sj_id='" + id + "'";
                if (StringUtils.isNotBlank(id)) {
                    List<Map<String, String>> list = DBUtil.queryReturnList(conn, sqlString);
                    if (list != null && list.size() >= 1) {
                        Map m = list.get(0);
                        return JSONObject.fromObject(m).toString();
                    }
                }
            }else if (opttype.equals("pq")) {
            	// 根据htid 查询关联的排迁子任务信息
                Map<String, Object> m_cond = (Map<String, Object>) map.get("cond");
                String htid = (String) m_cond.get("htid");
                String sqlString = "SELECT pq.*,(SELECT f.DIC_VALUE FROM FS_DIC_TREE f where f.parent_id =  ( select id from fs_dic_tree f1 where f1.dic_code='GXLB') and f.dic_code = pq.gxlb) gxlb_sv FROM gc_pq_zxm pq WHERE htid='" + htid + "'";
                if (StringUtils.isNotBlank(htid)) {
                    List<Map<String, String>> list = DBUtil.queryReturnList(conn, sqlString);
                    if (list != null && list.size() >= 1) {
                        Map m = list.get(0);
                        return JSONObject.fromObject(m).toString();
                    }
                }
            }else if (opttype.equals("lybzj")) {
            	// 根据htid 查询关联的排迁子任务信息
                Map<String, Object> m_cond = (Map<String, Object>) map.get("cond");
                String htid = (String) m_cond.get("htid");
                String sqlString = "select ly.*,c.dwmc from gc_zjgl_lybzj ly left join GC_CJDW c on c.gc_cjdw_id = ly.jndw WHERE ly.htid='" + htid + "'";
                if (StringUtils.isNotBlank(htid)) {
                    List<Map<String, String>> list = DBUtil.queryReturnList(conn, sqlString);
                    if (list != null && list.size() >= 1) {
                        Map m = list.get(0);
                        return JSONObject.fromObject(m).toString();
                    }
                }
            }	

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("项目征收资金情况   queryCommon查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }
}
