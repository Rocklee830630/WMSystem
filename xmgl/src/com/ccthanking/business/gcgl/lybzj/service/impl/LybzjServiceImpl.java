package com.ccthanking.business.gcgl.lybzj.service.impl;

import java.sql.Connection;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ccthanking.business.gcgl.lybzj.service.LybzjService;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class LybzjServiceImpl implements LybzjService {

    // private String ywlx = YwlxManager.GC_GCGL_LVBZJ;

    private String ywlx = YwlxManager.GC_ZJGL_LYBZJ;

    @Override
    public String query(String json, User user) throws Exception {
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            String orderFilter = RequestUtil.getOrderFilter(json);
            if (!StringUtils.isBlank(condition)) {
                condition += " AND t.XMID = t2.XDKID AND t1.GC_TCJH_XMXDK_ID = t2.XDKID ";
            } else {
                condition += " t.XMID = t2.XDKID AND t1.GC_TCJH_XMXDK_ID = t2.XDKID ";
            }
            condition += BusinessUtil.getSJYXCondition("t");
            condition += BusinessUtil.getCommonCondition(user, "t");
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            String sql = "SELECT "
                    + "t1.XMGLGS,t1.ND,t2.JGYSSJ,t.gc_cw_lvbzjb_id,t.xmid,t.htid,t.bdid,t.xmmc,t.htmc,t.bdmc,t.zbdw,t.jnfs,t.htje,t.fhqk,t.fhri,t.blr,t.pzbh,t.jswj,t.bzb,t.ywlx,t.sjbh,t.bz,t.lrr,t.lrsj,t.lrbm,t.lrbmmc,t.gxr,t.gxsj,t.gxbm,t.gxbmmc,t.sjmj,t.sfyx "
                    + "FROM " + "GC_CW_LVBZJB t,GC_SJGL_JJG t2,GC_TCJH_XMXDK t1 ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            // 合同表
            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");

            // 字典
            bs.setFieldDic("JSWJ", "FJSCZT");
            bs.setFieldDic("FHQK", "SF");
            // 日期
            bs.setFieldDateFormat("FHRI", "yyyy-MM-dd");
            bs.setFieldDateFormat("JGYSSJ", "yyyy-MM-dd");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<履约保证金>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

}
