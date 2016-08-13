/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglTqkbmmxService.java
 * 创建日期： 2013-09-29 上午 07:27:48
 * 功能：    接口：提请款部门明细
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-29 上午 07:27:48  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.zjgl.service.impl;

import java.sql.Connection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ccthanking.business.zjgl.service.GcZjglTqkbmmxService;
import com.ccthanking.business.zjgl.vo.GcZjglTqkbmmxVO;
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
import com.ccthanking.framework.service.impl.Base1ServiceImpl;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * <p>
 * GcZjglTqkbmmxService.java
 * </p>
 * <p>
 * 功能：提请款部门明细
 * </p>
 * 
 * <p>
 * <a href="GcZjglTqkbmmxService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-29
 * 
 */

@Service
public class GcZjglTqkbmmxServiceImpl extends Base1ServiceImpl<GcZjglTqkbmmxVO, String> implements GcZjglTqkbmmxService {

    private static Logger logger = LoggerFactory.getLogger(GcZjglTqkbmmxServiceImpl.class);

    // 业务类型
    private String ywlx = YwlxManager.GC_ZJGL_TQKBMMX;

    // 查询列表
    private static String SQL_QUERY_SQL = "SELECT ghh2.htmc,ghh2.id as htxxid,ghh2.htlx,ghh2.zwczf,ghh.htzf,ghh.wczf,ghh2.zhtzf, "
+" t.id , t.htid ,  t.tqkid , t.dwid , t.dwmc , t.xmmcnr , t.htbm , t.zxhtj , t.bcsq ,t.csz , t.jzqr , t.bmblr , t.bmspr , t.bmtqkmxzt , t.cwblr , t.jcblr , t.cwshz , t.jchdz , t.ywlx ,t.sjbh , t.sjmj , " 
+" t.sfyx , t.bz , t.lrr , t.lrsj ,t.lrbm ,  t.lrbmmc , t.gxr , t.gxsj , t.gxbm , t.gxbmmc , t.sortno , t.jcpch , t.jczfrq , t.jgc , t.kqjgc, "  
+" (case when t.bmtqkmxzt > 4 then t.ybf else nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = t.htid),'0') end) ybf, "
+" (case when t.bmtqkmxzt > 4 then t.ljbf else nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = t.htid),'0')+decode(t.cwshz,null,t.bcsq,t.cwshz) end) ljbf,  "
+" (case when t.bmtqkmxzt > 4 then t.ahtbfb else  trunc(decode(decode(t.zxhtj,null,0,t.zxhtj),0,0,(nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = t.htid),'0')+decode(t.cwshz,null,t.bcsq,t.cwshz))/t.zxhtj),4)*100 end) ahtbfb, "  
+" (case when t.bmtqkmxzt > 4 then t.ajlfkb else trunc(decode(decode(t.jzqr,null,0,t.jzqr),0,0,(nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = t.htid),'0')+decode(t.cwshz,null,t.bcsq,t.cwshz))/t.jzqr),4)*100 end) ajlfkb, " 
+" (select count(*) from gc_zjgl_tqkmx n1 where n1.bmmxid = t.id) CWTQKID,  "
+" nvl((select vsj.sjz from VIEW_ZJGL_TQK_BMMXADD_SJ vsj where vsj.htid = ghh2.id),0) sjz"
+" FROM gc_zjgl_tqkbmmx t LEFT JOIN gc_htgl_htsj ghh ON ghh.ID = t.htid LEFT JOIN gc_htgl_ht ghh2 ON ghh.htid=ghh2.ID";
    
    // 计算总数
    private static String SQL_UPDATE_ZS = "UPDATE gc_zjgl_tqkbm gzt SET gzt.zzxhtj = (select sum(t.zxhtj) from GC_ZJGL_TQKBMMX t WHERE t.tqkid=gzt.ID ), gzt.zybf= (select sum(t.ybf) from GC_ZJGL_TQKBMMX t WHERE t.tqkid=gzt.ID ), gzt.zbcsq= (select sum(t.bcsq) from GC_ZJGL_TQKBMMX t WHERE t.tqkid=gzt.ID ), gzt.zcsz= (select sum(t.csz) from GC_ZJGL_TQKBMMX t WHERE t.tqkid=gzt.ID ), gzt.zjlqr= (select sum(t.JZQR) from GC_ZJGL_TQKBMMX t WHERE t.tqkid=gzt.ID ) WHERE gzt.ID=?";
    // , gzt.zahtbfb=(zybf/zzxhtj)

    // 显示未使用的部门提请款明细
    private String SQL_QUERY_BMMX = "SELECT gzt2.ID, gzt2.htid, gzt2.tqkid, gzt2.dwid, gzt2.dwmc, gzt2.xmmcnr,       gzt2.htbm, gzt2.zxhtj, (case when gzt2.bmtqkmxzt > 4 then gzt2.ybf else nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt2.htid),'0') end )ybf, gzt2.bcsq, (case when gzt2.bmtqkmxzt > 4 then gzt2.ljbf else nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt2.htid),'0')+decode(gzt2.cwshz,null,gzt2.bcsq,gzt2.cwshz) end)ljbf, (case when gzt2.bmtqkmxzt > 4 then gzt2.ahtbfb else trunc(decode(decode(gzt2.zxhtj,null,0,gzt2.zxhtj),0,0,(nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt2.htid),'0')+decode(gzt2.cwshz,null,gzt2.bcsq,gzt2.cwshz))/gzt2.zxhtj),4)*100 end) ahtbfb,       gzt2.csz, gzt2.jzqr, (case when gzt2.bmtqkmxzt > 4 then gzt2.ajlfkb else trunc(decode(decode(gzt2.jzqr,null,0,gzt2.jzqr),0,0,(nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt2.htid),'0')+decode(gzt2.cwshz,null,gzt2.bcsq,gzt2.cwshz))/gzt2.jzqr),4)*100 end) ajlfkb, gzt2.ywlx, gzt2.sjbh, gzt2.sjmj, gzt2.gxsj,gzt2.sfyx, gzt2.bz, gzt2.lrr, gzt2.lrsj, gzt2.lrbm, gzt2.lrbmmc, gzt2.gxr, gzt2.gxbm, gzt2.gxbmmc, gzt2.sortno, gzt.sqdw, gzt.qklx, gzt2.jczfrq, gzt.qknf  FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2";
    
    // 显示未使用的部门提请款明细(工程监理类)
    private String SQL_QUERY_BMMX_GCJL = "SELECT gzt2.ID, gzt2.htid, gzt2.tqkid, gzt2.dwid, gzt2.dwmc, gzt2.xmmcnr,       gzt2.htbm, gzt2.zxhtj, (case when gzt2.bmtqkmxzt >4 then gzt2.ybf else nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt2.htid),'0') end) ybf, gzt2.bcsq, (case when gzt2.bmtqkmxzt >4 then gzt2.ljbf else nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt2.htid),'0')+decode(gzt2.cwshz,null,gzt2.bcsq,gzt2.cwshz) end) ljbf, (case when gzt2.bmtqkmxzt >4 then gzt2.ahtbfb else trunc(decode(decode(gzt2.zxhtj,null,0,gzt2.zxhtj),0,0,(nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt2.htid),'0')+decode(gzt2.cwshz,null,gzt2.bcsq,gzt2.cwshz))/gzt2.zxhtj),4)*100 end ) ahtbfb,       gzt2.csz, gzt2.jzqr, (case when gzt2.bmtqkmxzt >4 then gzt2.ajlfkb else trunc(decode(decode(gzt2.jzqr,null,0,gzt2.jzqr),0,0,(nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt2.htid),'0')+decode(gzt2.cwshz,null,gzt2.bcsq,gzt2.cwshz))/gzt2.jzqr),4)*100 end ) ajlfkb, gzt2.ywlx, gzt2.sjbh, gzt2.sjmj, gzt2.gxsj,gzt2.sfyx, gzt2.bz, gzt2.lrr, gzt2.lrsj, gzt2.lrbm, gzt2.lrbmmc, gzt2.gxr, gzt2.gxbm, gzt2.gxbmmc, gzt2.sortno, gzt.sqdw, gzt.qklx, gzt2.jczfrq, gzt.qknf  FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2, gc_zjgl_tqkgcb gzt3 ";
    // 显示已选择使用了的部门明细
    private static String SQL_QUERY_BMMX_CWB = "SELECT gzt3.ID, gzt3.htid, gzt3.tqkid, gzt3.dwid, gzt3.dwmc, gzt3.xmmcnr,       gzt3.htbm, gzt3.zxhtj, (case when gzt3.bmtqkmxzt > 4 then gzt3.ybf else nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt3.htid),'0') end ) ybf, gzt3.bcsq, (case when gzt3.bmtqkmxzt > 4 then gzt3.ljbf else nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt3.htid),'0')+decode(gzt3.cwshz,null,gzt3.bcsq,gzt3.cwshz) end ) ljbf, (case when gzt3.bmtqkmxzt > 4 then gzt3.ahtbfb else trunc(decode(decode(gzt3.zxhtj,null,0,gzt3.zxhtj),0,0,(nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt3.htid),'0')+decode(gzt3.cwshz,null,gzt3.bcsq,gzt3.cwshz))/gzt3.zxhtj),4)*100 end ) ahtbfb,       gzt3.csz, gzt3.jzqr, (case when gzt3.bmtqkmxzt > 4 then gzt3.ajlfkb else trunc(decode(decode(gzt3.jzqr,null,0,gzt3.jzqr),0,0,(nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = gzt3.htid),'0')+decode(gzt3.cwshz,null,gzt3.bcsq,gzt3.cwshz))/gzt3.jzqr),4)*100 end ) ajlfkb, gzt3.ywlx, gzt3.sjbh, gzt3.sjmj,       gzt3.sfyx, gzt3.bz, gzt3.lrr, gzt3.lrsj, gzt3.lrbm, gzt3.lrbmmc, gzt3.gxr,       gzt3.gxsj, gzt3.gxbm, gzt3.gxbmmc, gzt3.sortno, gzt4.sqdw, gzt4.qklx, gzt4.qknf   FROM gc_zjgl_tqk gzt, gc_zjgl_tqkmx gzt2, gc_zjgl_tqkbmmx gzt3, gc_zjgl_tqkbm gzt4 ";

    // 查询某合同的已拔付 已审批的情况下才是这个值
    private static String SQL_QUERY_YBF = "SELECT sum(gzt2.jchdz) as ybf   FROM gc_zjgl_tqkbmmx gzt2, gc_zjgl_tqkbm gzt WHERE gzt.tqkzt='6' AND gzt2.htid=?";

    // 更新已支付金额
    private static String SQL_UPDATE_YZF = "UPDATE gc_zjgl_tqkbmmx ght   SET ght.ybf=(SELECT decode(SUM(ghh.htzf), NULL, 0, SUM(ghh.htzf)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.htid) WHERE ght.id=?";
    private static String SQL_UPDATE_TQKBM_YZF = "UPDATE gc_zjgl_tqkmx ght  SET ght.ybf = (SELECT decode(SUM(ghh.zfje), NULL, 0, SUM(ghh.zfje)) FROM gc_htgl_ht_htzf ghh  WHERE ghh.sfyx = 1 AND ghh.htsjid = ght.htid) WHERE ght.tqkid=?";

    // private static String

    // 获取计量数据
    private static String SQL_QUERY_JL = "	select b.id,b2.id,b1.gcpc,b2.jhsjid,b3.xmid,b3.nd,b3.jlyf,b3.LJJLSDZ JZQR, "
            + " decode(b4.Cssdje,null,0,b4.Cssdje) csz,decode(b4.Sjsdje,null,0,b4.Sjsdje) sjz  "
            + " from gc_zjgl_tqkbmmx b left join gc_zjgl_tqkbm b1 on b.tqkid = b1.id " + " left join gc_htgl_htsj b2 on b.htid = b2.id "
            + " left join gc_xmglgs_jlb b3 on b2.jhsjid = b3.tcjh_sj_id and b1.qknf = b3.nd and b1.gcpc = to_char(b3.jlyf,'mm') "
            + " left join gc_zjb_jsb b4 on b4.jhsjid = b2.jhsjid ";

    @Override
    public String queryConditionYbfBMMX(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            condition += " AND NOT EXISTS(SELECT * FROM gc_zjgl_zszj_zfzsb gzzz WHERE gzzz.bkxgdid= t.tqkmxid) ";
            String orderFilter = RequestUtil.getOrderFilter(json);
            condition += BusinessUtil.getSJYXCondition(null);
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;

            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            String sql = "SELECT * FROM " + "VIEW_ZJGL_ZSZJ_ZFZSB_TQK t";

            BaseResultSet bs = DBUtil.query(conn, sql, page);

            bs.setFieldThousand("CWSHZ");// 最新合同价
            bs.setFieldThousand("JCHDZ");// 已拔付

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<提请款已拔付明细>", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("提请款部门明细查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryConditionBMMX(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
            if ("bmmx".equals(opttype)) {
                // 查询未使用的部门提交的明细

                PageManager page = RequestUtil.getPageManager(json);
                QueryConditionList list = RequestUtil.getConditionList(json);

                String condition = " NOT EXISTS (SELECT * FROM gc_zjgl_tqkmx WHERE ID=gzt2.id) and gzt.id=gzt2.tqkid AND gzt.tqkzt='4' AND gzt2.bmtqkmxzt !='3' ";
                condition += list == null ? "" : " and " + list.getConditionWhere();

                String orderFilter = RequestUtil.getOrderFilter(json);

                // 增加sql条件
                condition += BusinessUtil.getSJYXCondition("gzt2");
                condition += BusinessUtil.getCommonCondition(user, "gzt2");
                condition += orderFilter;
                if (page == null)
                    page = new PageManager();
                page.setFilter(condition);

                conn.setAutoCommit(false);
                BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_BMMX, page);

                bs.setFieldOrgDept("SQDW");// 申请单位

                bs.setFieldDic("QKLX", "QKLX");// 请款类型
                // bs.setFieldDic("QKNF", "QKNF");// 请款年份
                // bs.setFieldDic("GCPC", "GCPC");// 工程批次
                bs.setFieldDic("TQKZT", "TQKZT");// 状态 新建1审核中2审核通过3审核不通过4

                // 设置千发位表示
                bs.setFieldThousand("ZXHTJ");// 最新合同价
                bs.setFieldThousand("YBF");// 已拔付
                bs.setFieldThousand("BCSQ");// 本次申请拔款
                bs.setFieldThousand("LJBF");// 累计拔付
                bs.setFieldThousand("CSZ");// 财审值
                bs.setFieldThousand("JZQR");// 监理确认计量款
                bs.setFieldThousand("CWSHZ");// 财务审核值
                bs.setFieldThousand("JCHDZ");// 计财核定值

                domresult = bs.getJson();

            } else if ("cwbyxz".equals(opttype)) {
                // 查询财务处已选择

                PageManager page = RequestUtil.getPageManager(json);
                QueryConditionList list = RequestUtil.getConditionList(json);

                String condition = " gzt.id=gzt2.tqkid AND gzt2.bmmxid=gzt3.id AND gzt3.tqkid=gzt4.id ";
                condition += list == null ? "" : " and " + list.getConditionWhere();

                String orderFilter = RequestUtil.getOrderFilter(json);

                // 增加sql条件
                condition += BusinessUtil.getSJYXCondition("gzt2");
                condition += BusinessUtil.getCommonCondition(user, "gzt2");
                condition += orderFilter;
                if (page == null)
                    page = new PageManager();
                page.setFilter(condition);

                conn.setAutoCommit(false);
                BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_BMMX_CWB, page);

                bs.setFieldOrgDept("SQDW");// 申请单位

                bs.setFieldDic("QKLX", "QKLX");// 请款类型
                // bs.setFieldDic("QKNF", "QKNF");// 请款年份
                // bs.setFieldDic("GCPC", "GCPC");// 工程批次
                bs.setFieldDic("TQKZT", "TQKZT");// 状态 新建1审核中2审核通过3审核不通过4

                // 设置千发位表示
                bs.setFieldThousand("ZXHTJ");// 最新合同价
                bs.setFieldThousand("YBF");// 已拔付
                bs.setFieldThousand("BCSQ");// 本次申请拔款
                bs.setFieldThousand("LJBF");// 累计拔付
                bs.setFieldThousand("CSZ");// 财审值
                bs.setFieldThousand("JZQR");// 监理确认计量款
                bs.setFieldThousand("CWSHZ");// 财务审核值
                bs.setFieldThousand("JCHDZ");// 计财核定值

                domresult = bs.getJson();

            }

            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<提请款部门明细>", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("提请款部门明细查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
    
    @Override
    public String queryConditionBMMXdisGcb(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
            if ("bmmx".equals(opttype)) {
                // 查询未使用的部门提交的明细

                PageManager page = RequestUtil.getPageManager(json);
                QueryConditionList list = RequestUtil.getConditionList(json);

                String condition = " NOT EXISTS (SELECT * FROM gc_zjgl_tqkmx WHERE ID=gzt2.id) and gzt.id=gzt2.tqkid AND gzt.tqkzt='4' AND gzt2.bmtqkmxzt !='3' AND gzt.id = gzt3.bmtqkid AND gzt3.tqkztgcb = '1' ";
                condition += list == null ? "" : " and " + list.getConditionWhere();

                String orderFilter = RequestUtil.getOrderFilter(json);

                // 增加sql条件
                condition += BusinessUtil.getSJYXCondition("gzt2");
                condition += BusinessUtil.getCommonCondition(user, "gzt2");
                condition += orderFilter;
                if (page == null)
                    page = new PageManager();
                page.setFilter(condition);

                conn.setAutoCommit(false);
                BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_BMMX_GCJL, page);

                bs.setFieldOrgDept("SQDW");// 申请单位

                bs.setFieldDic("QKLX", "QKLX");// 请款类型
                // bs.setFieldDic("QKNF", "QKNF");// 请款年份
                // bs.setFieldDic("GCPC", "GCPC");// 工程批次
                bs.setFieldDic("TQKZT", "TQKZT");// 状态 新建1审核中2审核通过3审核不通过4

                // 设置千发位表示
                bs.setFieldThousand("ZXHTJ");// 最新合同价
                bs.setFieldThousand("YBF");// 已拔付
                bs.setFieldThousand("BCSQ");// 本次申请拔款
                bs.setFieldThousand("LJBF");// 累计拔付
                bs.setFieldThousand("CSZ");// 财审值
                bs.setFieldThousand("JZQR");// 监理确认计量款
                bs.setFieldThousand("CWSHZ");// 财务审核值
                bs.setFieldThousand("JCHDZ");// 计财核定值

                domresult = bs.getJson();

            } else if ("cwbyxz".equals(opttype)) {
                // 查询财务处已选择

                PageManager page = RequestUtil.getPageManager(json);
                QueryConditionList list = RequestUtil.getConditionList(json);

                String condition = " gzt.id=gzt2.tqkid AND gzt2.bmmxid=gzt3.id AND gzt3.tqkid=gzt4.id ";
                condition += list == null ? "" : " and " + list.getConditionWhere();

                String orderFilter = RequestUtil.getOrderFilter(json);

                // 增加sql条件
                condition += BusinessUtil.getSJYXCondition("gzt2");
                condition += BusinessUtil.getCommonCondition(user, "gzt2");
                condition += orderFilter;
                if (page == null)
                    page = new PageManager();
                page.setFilter(condition);

                conn.setAutoCommit(false);
                BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_BMMX_CWB, page);

                bs.setFieldOrgDept("SQDW");// 申请单位

                bs.setFieldDic("QKLX", "QKLX");// 请款类型
                // bs.setFieldDic("QKNF", "QKNF");// 请款年份
                // bs.setFieldDic("GCPC", "GCPC");// 工程批次
                bs.setFieldDic("TQKZT", "TQKZT");// 状态 新建1审核中2审核通过3审核不通过4

                // 设置千发位表示
                bs.setFieldThousand("ZXHTJ");// 最新合同价
                bs.setFieldThousand("YBF");// 已拔付
                bs.setFieldThousand("BCSQ");// 本次申请拔款
                bs.setFieldThousand("LJBF");// 累计拔付
                bs.setFieldThousand("CSZ");// 财审值
                bs.setFieldThousand("JZQR");// 监理确认计量款
                bs.setFieldThousand("CWSHZ");// 财务审核值
                bs.setFieldThousand("JCHDZ");// 计财核定值

                domresult = bs.getJson();

            }

            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<提请款部门明细>", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("提请款部门明细查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

    @Override
    public String queryYbfByHt(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            String orderFilter = RequestUtil.getOrderFilter(json);
            condition += BusinessUtil.getSJYXCondition(null);
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            String sql = "SELECT * FROM " + "GC_ZJGL_TQKBMMX t";

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_YBF, page);

            bs.setFieldThousand("ZXHTJ");// 最新合同价
            bs.setFieldThousand("YBF");// 已拔付
            bs.setFieldThousand("BCSQ");// 本次申请拔款
            bs.setFieldThousand("LJBF");// 累计拔付
            bs.setFieldThousand("CSZ");// 财审值
            bs.setFieldThousand("JZQR");// 监理确认计量款
            bs.setFieldThousand("CWSHZ");// 财务审核值
            bs.setFieldThousand("JCHDZ");// 计财核定值

            bs.setFieldUserID("BMBLR");
            bs.setFieldUserID("BMSPR");
            bs.setFieldUserID("CWBLR");
            bs.setFieldUserID("JCBLR");

            bs.setFieldDic("BMTQKMXZT", "TQKZT");// 状态

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<提请款部门明细>", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("提请款部门明细查询失败!");
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
            condition += BusinessUtil.getSJYXCondition("t");
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            String sql = "SELECT * FROM " + "GC_ZJGL_TQKBMMX t";

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_SQL, page);

            bs.setFieldThousand("ZXHTJ");// 最新合同价
            bs.setFieldThousand("YBF");// 已拔付
            bs.setFieldThousand("BCSQ");// 本次申请拔款
            bs.setFieldThousand("LJBF");// 累计拔付
            bs.setFieldThousand("CSZ");// 财审值
            bs.setFieldThousand("SJZ");// 审计值
            bs.setFieldThousand("JZQR");// 监理确认计量款
            bs.setFieldThousand("CWSHZ");// 财务审核值
            bs.setFieldThousand("JCHDZ");// 计财核定值
            bs.setFieldThousand("ZWCZF");// 完成投资
            bs.setFieldThousand("ZHTZF");// 合同支付
            bs.setFieldThousand("JGC");// 甲供材
            bs.setFieldThousand("KQJGC");// 扣除甲供材

            bs.setFieldUserID("BMBLR");
            bs.setFieldUserID("BMSPR");
            bs.setFieldUserID("CWBLR");
            bs.setFieldUserID("JCBLR");

            bs.setFieldDic("BMTQKMXZT", "TQKZT");// 状态

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<提请款部门明细>", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("提请款部门明细查询失败!");
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
        GcZjglTqkbmmxVO vo = new GcZjglTqkbmmxVO();

        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            vo.setValueFromJson((JSONObject) list.get(0));
            // 设置主键
            vo.setId(new RandomGUID().toString()); // 主键
            //
            vo.setLrr(user.getAccount()); // 更新人
            vo.setLrsj(Pub.getCurrentDate()); // 更新时间
            vo.setLrbm(user.getDepartment());// 录入人单位
            vo.setLrbmmc(user.getOrgDept().getDeptName());// 录入人单位名称
            vo.setYwlx(ywlx);

            EventVO eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);// 生成事件
            vo.setSjbh(eventVO.getSjbh());

            // 插入
            BaseDAO.insert(conn, vo);
            conn.commit();
            DBUtil.executeUpdate(SQL_UPDATE_ZS, new Object[] { vo.getTqkid() });
            // conn.commit();
            // DBUtil.executeUpdate(SQL_UPDATE_YZF, new Object[] { vo.getId()
            // });

            resultVO = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门明细新增成功", user, "", "");

            // String
            // jsona="{querycondition: {conditions: [{\"value\":\""+vo.getId()+"\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            // return queryCondition(jsona, user);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款部门明细新增失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门明细新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglTqkbmmxVO vo = new GcZjglTqkbmmxVO();

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
            conn.commit();
            DBUtil.executeUpdate(SQL_UPDATE_ZS, new Object[] { vo.getTqkid() });
            // conn.commit();
            // DBUtil.executeUpdate(SQL_UPDATE_YZF, new Object[] { vo.getId()
            // });

            resultVO = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门明细修改成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款部门明细修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门明细修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglTqkbmmxVO vo = new GcZjglTqkbmmxVO();

        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);

            List<String> listrow = new ArrayList<String>();

            String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
            String tqkid = map.get("tqkid") == null ? "" : (String) map.get("tqkid");
            if (StringUtils.isNotBlank(tqkid)) {
                if (opttype.equals("cwb")) {
                    // 财务部 部门提请款综全 批量修改财务值
                    GcZjglTqkbmmxVO tmp = null;
                    boolean flag = false;
                    for (int i = 0; i < list.size(); i++) {
                        flag = true;
                        tmp = new GcZjglTqkbmmxVO();
                        tmp.setValueFromJson((JSONObject) list.get(i));

                        vo = findById(tmp.getId());

                        // vo.setTqkid(tmp.getTqkid());
                        vo.setCwshz(tmp.getCwshz());// 核定值

                        // --------------------------------------------------------------累计拨付、累计拨付比例 start add by xhb 2014-03-12
                        // 当财务审核时，【累计拨付的值】是 【财务审核值（Cwshz）】+【已拨付的值（Ybf）】
                        vo.setLjbf(String.format("%.4f",Double.valueOf(vo.getCwshz())*1.0 + Double.valueOf(vo.getYbf())*1.0));
                        
                        double ljbf = Double.valueOf(vo.getLjbf())*1.0;
                        double htqdj = Double.valueOf(vo.getZxhtj())*1.0;
                        NumberFormat format = NumberFormat.getInstance();
                		format.setMinimumFractionDigits(10);
                		// 【按合同百分比（ahtbfb）】是【累计拨付比例】是【累计拨付（ljbf）】/【合同签订价（htqdj）】
                		String ahtbfb = htqdj==0?"0":String.valueOf(ljbf/htqdj*100);
                        // --------------------------------------------------------------累计拨付、累计拨付比例 end
                        System.out.println(ljbf);
                        System.out.println(String.format("%.4f", ljbf));
                        vo.setAhtbfb(ahtbfb);
                        vo.setBz(tmp.getBz());

                        // vo.setCwblr(user.getAccount());
                        vo.setCwblr(user.getAccount());// 办理人

                        BusinessUtil.setUpdateCommonFields(vo, user);
                        BaseDAO.update(conn, vo);

                        listrow.add(vo.getRowJsonSingle());
                    }
                    if (flag) {
                        conn.commit();
                    }
                } else if (opttype.equals("jcc")) {
                    // 财务部 部门提请款综全 批量修改财务值
                    GcZjglTqkbmmxVO tmp = null;
                    boolean flag = false;
                    for (int i = 0; i < list.size(); i++) {
                        flag = true;
                        tmp = new GcZjglTqkbmmxVO();
                        tmp.setValueFromJson((JSONObject) list.get(i));
                        vo = findById(tmp.getId());
                        vo.setJchdz(tmp.getJchdz());// 核定值
                        vo.setJcpch(tmp.getJcpch());// 计财批次号
                        vo.setJczfrq(tmp.getJczfrq());// 计财支付日期
                        vo.setJcblr(user.getAccount());// 办理人
                        
                        // --------------------------------------------------------------累计拨付、累计拨付比例 start add by xhb 2014-03-12
                        // 当计财核定时，【累计拨付的值】是 【计财核定值（Jchdz）】+【已拨付的值（Ybf）】    
                        vo.setLjbf(String.format("%.4f",Double.valueOf(vo.getJchdz())*1.0 + Double.valueOf(vo.getYbf())*1.0));
                        
                        double ljbf = Double.valueOf(vo.getLjbf())*1.0;
                        double htqdj = Double.valueOf(vo.getZxhtj())*1.0;
                        NumberFormat format = NumberFormat.getInstance();
                		format.setMinimumFractionDigits(10);
                		// 【按合同百分比（ahtbfb）】是【累计拨付比例】是【累计拨付（ljbf）】/【合同签订价（htqdj）】
                		String ahtbfb = String.valueOf(ljbf/htqdj*100);
                        // --------------------------------------------------------------累计拨付、累计拨付比例 end
                        
                        vo.setAhtbfb(ahtbfb);
                        BusinessUtil.setUpdateCommonFields(vo, user);
                        BaseDAO.update(conn, vo);
                        listrow.add(vo.getRowJsonSingle());
                        //更新待支付金额为计财核定值
                        String sql_update_htzf_dzf = "UPDATE gc_htgl_ht_htzf z SET z.zfje="+vo.getJchdz()+"  WHERE htdid = '"+vo.getId()+"'";
                        DBUtil.execUpdateSql(conn, sql_update_htzf_dzf);
                    }
                    if (flag) {
                        conn.commit();
                    }
                }
            }
            // DBUtil.executeUpdate(SQL_UPDATE_YZF, new Object[] { vo.getId()
            // });
            // conn.commit();
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门明细修改成功", user, "", "");
            resultVO = BaseDAO.comprisesResponseData(conn, listrow);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款部门明细修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门明细修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcZjglTqkbmmxVO vo = new GcZjglTqkbmmxVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            BaseDAO.delete(conn, vo);
            conn.commit();
            DBUtil.executeUpdate(SQL_UPDATE_ZS, new Object[] { vo.getTqkid() });

            resultVo = vo.getRowJson();
            conn.commit();
            
            //移除工程部审核数据
            String sql_delete_tqkgcbmx = "delete from gc_zjgl_tqkgcbmx t where t.BMTQKMXID = '"+vo.getId()+"'";
            DBUtil.exec(conn, sql_delete_tqkgcbmx);
            conn.commit();
            
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门明细删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款部门明细删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门明细删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }

    // 格式化日期
    public static Date formatDate(Date date) throws ParseException {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String str = formatDate.format(date);
        return formatDate.parse(str);
    }

    @Override
    public String queryJlsj(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            // PageManager page = RequestUtil.getPageManager(json);
            // String condition =
            // RequestUtil.getConditionList(json).getConditionWhere();
            // String orderFilter = RequestUtil.getOrderFilter(json);
            // condition += BusinessUtil.getSJYXCondition("t");
            // condition += BusinessUtil.getCommonCondition(user, null);
            // condition += orderFilter;
            // if (page == null)
            // page = new PageManager();
            // page.setFilter(condition);

            // conn.setAutoCommit(false);

            // BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_JL, page);

            // bs.setFieldThousand("JZQR");// 监理审定值
            // bs.setFieldThousand("CSZ");// 财审值
            // bs.setFieldThousand("SJZ");// 审计值

            // domresult = bs.getJson();

            if (StringUtils.isNotBlank(json)) {
                // id="+nf+":"+yf+":"+$("#QHTID").val(),
                String[] arr = StringUtils.split(json, ":");
                if (arr.length == 3) {
                    String sqlString = "SELECT  "
                            + "(SELECT jl.JZQR FROM VIEW_ZJGL_TQK_BMMXADD_JLQR_USE jl WHERE jl.htsjid=ghh.id AND jl.nd='" + arr[0]
                            + "' AND jlyf='" + arr[1] + "') AS JZQR, "
                            + "(SELECT js.csz FROM VIEW_ZJGL_TQK_BMMXADD_SJ js WHERE js.htsjid=ghh.id) AS csz, "
                            + "(SELECT js.sjz FROM VIEW_ZJGL_TQK_BMMXADD_SJ js WHERE js.htsjid=ghh.id) AS sjz " + "FROM gc_htgl_htsj ghh "
                            + "WHERE ghh.ID='" + arr[2] + "'";
                    List<Map<String, String>> list = DBUtil.queryReturnList(conn, sqlString);
                    if (list != null && list.size() >= 1) {
                        Map m = list.get(0);
                        return JSONObject.fromObject(m).toString();
                    }
                }
            }

            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<提请款部门明细相关计量数据>", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("提请款部门明细相关计量数据查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
    
    @Override
    public String queryPqsdz(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            // PageManager page = RequestUtil.getPageManager(json);
            // String condition =
            // RequestUtil.getConditionList(json).getConditionWhere();
            // String orderFilter = RequestUtil.getOrderFilter(json);
            // condition += BusinessUtil.getSJYXCondition("t");
            // condition += BusinessUtil.getCommonCondition(user, null);
            // condition += orderFilter;
            // if (page == null)
            // page = new PageManager();
            // page.setFilter(condition);

            // conn.setAutoCommit(false);

            // BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_JL, page);

            // bs.setFieldThousand("JZQR");// 监理审定值
            // bs.setFieldThousand("CSZ");// 财审值
            // bs.setFieldThousand("SJZ");// 审计值

            // domresult = bs.getJson();

            if (StringUtils.isNotBlank(json)) {
                // id="+nf+":"+yf+":"+$("#QHTID").val(),
               
                    String sqlString = "select p.htsx,p.sdz from gc_pq_zxm p  " 
                    				+ " where p.htid =(select t.htid from gc_htgl_htsj t where t.id =  '"+json+"') ";
                    List<Map<String, String>> list = DBUtil.queryReturnList(conn, sqlString);
                    if (list != null && list.size() >= 1) {
                        Map m = list.get(0);
                        return JSONObject.fromObject(m).toString();
                    }
            }

            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<提请款部门明细排迁类相关审定值数据>", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("提请款部门明细排迁类相关审定值数据查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

}
