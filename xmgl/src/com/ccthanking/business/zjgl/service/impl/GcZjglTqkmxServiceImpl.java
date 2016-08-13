/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglTqkmxService.java
 * 创建日期： 2013-09-25 下午 03:34:59
 * 功能：    接口：提请款明细
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-25 下午 03:34:59  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.zjgl.service.impl;

import java.sql.Connection;
import java.util.HashMap;

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

import com.ccthanking.business.zjgl.vo.GcZjglTqkmxVO;
import com.ccthanking.business.zjgl.service.GcZjglTqkmxService;
import com.ccthanking.framework.service.impl.Base1ServiceImpl;

/**
 * <p>
 * GcZjglTqkmxService.java
 * </p>
 * <p>
 * 功能：提请款明细
 * </p>
 * 
 * <p>
 * <a href="GcZjglTqkmxService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-25
 * 
 */

@Service
public class GcZjglTqkmxServiceImpl extends Base1ServiceImpl<GcZjglTqkmxVO, String> implements GcZjglTqkmxService {

    private static Logger logger = LoggerFactory.getLogger(GcZjglTqkmxServiceImpl.class);

    // 业务类型
    private String ywlx = YwlxManager.GC_ZJGL_TQKMX;

    // 查询列表 提请款明细
    // private static String SQL_QUERY_SQL =
    // "SELECT vztd.dbf, ghh2.htmc, t.ID, t.htid, t.tqkid, t.bmmxid, t.dwid, t.dwmc, t.xmmcnr,       t.htbm, t.zxhtj, t.ybf, t.bcsq, t.ljbf, t.ahtbfb, t.csz, t.jzqr, t.ajlfkb,   t.ywlx, t.sjbh, t.sjmj, t.sfyx, t.bz, t.lrr, t.lrsj,       t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sortno, gzt.bmblr,       gzt.bmspr, gzt.bmtqkmxzt, gzt.cwblr, gzt.jcblr, DECODE(gzt.cwshz, NULL, gzt.bcsq, gzt.cwshz) AS cwshz, gzt.jchdz  FROM gc_zjgl_tqkmx t JOIN gc_zjgl_tqkbmmx gzt ON t.bmmxid=gzt.ID LEFT JOIN gc_htgl_htsj ghh    ON ghh.ID = t.htid  LEFT JOIN gc_htgl_ht ghh2    ON ghh.htid = ghh2.ID  LEFT JOIN view_zjgl_tqk_dbf vztd on vztd.htsjid=ghh.ID";
    																																									// 累计拨付向gc_zjgl_tqkbmmx取值
    private static String SQL_QUERY_SQL = "SELECT vztd.dbf, ghh2.htmc, t.ID, t.htid, t.tqkid, t.bmmxid, t.dwid, t.dwmc, t.xmmcnr,       t.htbm, t.zxhtj, (case when gzt.bmtqkmxzt > 4 then gzt.ybf else nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = t.htid),'0') end ) ybf, t.bcsq,  (case when gzt.bmtqkmxzt > 4 then gzt.ljbf else nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = t.htid),'0')+decode(gzt.cwshz,null,t.bcsq,gzt.cwshz) end ) ljbf, (case when gzt.bmtqkmxzt > 4 then gzt.ahtbfb else trunc(decode(decode(t.zxhtj,null,0,t.zxhtj),0,0,(nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = t.htid),'0')+decode(gzt.cwshz,null,t.bcsq,gzt.cwshz))/t.zxhtj),4)*100 end ) ahtbfb, t.csz, t.jzqr,  (case when gzt.bmtqkmxzt > 4 then gzt.ajlfkb else trunc(decode(decode(t.jzqr,null,0,t.jzqr),0,0,(nvl((select t1.ybf from view_tqkbmxx_ht_ybf t1 where t1.htid = t.htid),'0')+decode(gzt.cwshz,null,t.bcsq,gzt.cwshz))/t.jzqr),4)*100 end )ajlfkb,   t.ywlx, t.sjbh, t.sjmj, t.sfyx, gzt.bz, t.lrr, t.lrsj,       t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sortno, gzt.bmblr,       gzt.bmspr, gzt.bmtqkmxzt, gzt.cwblr, gzt.jcblr,  gzt.cwshz, gzt.jchdz,gzt.jcpch, gzt.jczfrq,gzt.jgc,nvl((select js.sjz from VIEW_ZJGL_TQK_BMMXADD_SJ js where js.htid = ghh.htid),0) sjz,nvl((select t.htjsj from gc_htgl_htsj t where t.id = ghh.htid),0) jsz  FROM gc_zjgl_tqkmx t JOIN gc_zjgl_tqkbmmx gzt ON t.bmmxid=gzt.ID LEFT JOIN gc_htgl_htsj ghh    ON ghh.ID = t.htid  LEFT JOIN gc_htgl_ht ghh2    ON ghh.htid = ghh2.ID  LEFT JOIN view_zjgl_tqk_dbf vztd on vztd.htsjid=ghh.ID";
    // 财部编辑sql 将本次申请拨款 赋为 财务审核值 gzt.bcsq AS cwshz
    // private static String SQL_QUERY_SQL_BCSQBK_TO_CWSHZ =
    // "SELECT vztd.dbf, ghh2.htmc, t.ID, t.htid, t.tqkid, t.bmmxid, t.dwid, t.dwmc, t.xmmcnr,       t.htbm, t.zxhtj, t.ybf, t.bcsq, t.ljbf, t.ahtbfb, t.csz, t.jzqr, t.ajlfkb,   t.ywlx, t.sjbh, t.sjmj, t.sfyx, t.bz, t.lrr, t.lrsj,       t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sortno, gzt.bmblr,       gzt.bmspr, gzt.bmtqkmxzt, gzt.cwblr, gzt.jcblr, gzt.bcsq AS cwshz, gzt.jchdz  FROM gc_zjgl_tqkmx t JOIN gc_zjgl_tqkbmmx gzt ON t.bmmxid=gzt.ID LEFT JOIN gc_htgl_htsj ghh    ON ghh.ID = t.htid  LEFT JOIN gc_htgl_ht ghh2    ON ghh.htid = ghh2.ID  LEFT JOIN view_zjgl_tqk_dbf vztd on vztd.htsjid=ghh.ID";
    private static String SQL_QUERY_SQL_BCSQBK_TO_CWSHZ = "SELECT vztd.dbf, ghh2.htmc, t.ID, t.htid, t.tqkid, t.bmmxid, t.dwid, t.dwmc, t.xmmcnr,        "
    		 +" t.htbm, t.zxhtj, (case when gzt.bmtqkmxzt > 4 then gzt.ybf else nvl((select v1.ybf from view_tqkbmxx_ht_ybf v1 where v1.htid = gzt.htid),'0') end) ybf, t.bcsq,   "
    		 +" (case when gzt.bmtqkmxzt > 4 then gzt.ljbf else nvl((select v1.ybf from view_tqkbmxx_ht_ybf v1 where v1.htid = gzt.htid),'0')+decode(gzt.cwshz,null,t.bcsq,gzt.cwshz) end ) ljbf,   "
    		 +" (case when gzt.bmtqkmxzt > 4 then gzt.ahtbfb else trunc(decode(decode(gzt.zxhtj,null,0,gzt.zxhtj),0,0,(nvl((select v1.ybf from view_tqkbmxx_ht_ybf v1 where v1.htid = gzt.htid),'0')+decode(gzt.cwshz,null,gzt.bcsq,gzt.cwshz))/gzt.zxhtj),4)*100 end ) ahtbfb, t.csz, t.jzqr,   "
    		 +" (case when gzt.bmtqkmxzt > 4 then gzt.ajlfkb else trunc(decode(decode(t.jzqr,null,0,gzt.jzqr),0,0,(nvl((select v1.ybf from view_tqkbmxx_ht_ybf v1 where v1.htid = gzt.htid),'0')+decode(gzt.cwshz,null,gzt.bcsq,gzt.cwshz))/gzt.jzqr),4)*100 end ) ajlfkb,   t.ywlx, t.sjbh, t.sjmj, t.sfyx, t.bz, t.lrr, t.lrsj, "       
    		 +" t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sortno, gzt.bmblr,       gzt.bmspr, gzt.bmtqkmxzt, gzt.cwblr, gzt.jcblr, gzt.cwshz, gzt.jchdz,gzt.jcpch,gzt.jczfrq, gzt.jgc ,  nvl((select js.sjz from VIEW_ZJGL_TQK_BMMXADD_SJ js where js.htid = gzt.htid),0) sjz, " 
    		 +" nvl((select t.htjsj from gc_htgl_htsj t where t.id = gzt.htid),0) jsz   FROM gc_zjgl_tqkmx t JOIN gc_zjgl_tqkbmmx gzt ON t.bmmxid=gzt.ID   LEFT JOIN gc_htgl_htsj ghh    ON ghh.ID = t.htid "   
    		 +" LEFT JOIN gc_htgl_ht ghh2    ON ghh.htid = ghh2.ID  LEFT JOIN view_zjgl_tqk_dbf vztd on vztd.htsjid=ghh.ID";
    // 计财 审定设置 将财务审核值赋予计财审核值
    // private static String SQL_QUERY_SQL_CWSHZ_TO_JCHDZ =
    // "SELECT vztd.dbf, ghh2.htmc, t.ID, t.htid, t.tqkid, t.bmmxid, t.dwid, t.dwmc, t.xmmcnr,       t.htbm, t.zxhtj, t.ybf, t.bcsq, t.ljbf, t.ahtbfb, t.csz, t.jzqr, t.ajlfkb,   t.ywlx, t.sjbh, t.sjmj, t.sfyx, t.bz, t.lrr, t.lrsj,       t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sortno, gzt.bmblr,       gzt.bmspr, gzt.bmtqkmxzt, gzt.cwblr, gzt.jcblr, gzt.cwshz,  DECODE(gzt.jchdz, NULL, gzt.cwshz, gzt.jchdz) AS jchdz  FROM gc_zjgl_tqkmx t JOIN gc_zjgl_tqkbmmx gzt ON t.bmmxid=gzt.ID LEFT JOIN gc_htgl_htsj ghh    ON ghh.ID = t.htid  LEFT JOIN gc_htgl_ht ghh2    ON ghh.htid = ghh2.ID  LEFT JOIN view_zjgl_tqk_dbf vztd on vztd.htsjid=ghh.ID";
    																																												// 累计拨付向gc_zjgl_tqkbmmx取值
    private static String SQL_QUERY_SQL_CWSHZ_TO_JCHDZ = "SELECT vztd.dbf, ghh2.htmc, t.ID, t.htid, t.tqkid, t.bmmxid, t.dwid, t.dwmc, t.xmmcnr,       t.htbm, t.zxhtj, gzt.ybf, t.bcsq, gzt.ljbf, gzt.ahtbfb, t.csz, t.jzqr, gzt.ajlfkb,   t.ywlx, t.sjbh, t.sjmj, t.sfyx, t.bz, t.lrr, t.lrsj,       t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sortno, gzt.bmblr,       gzt.bmspr, gzt.bmtqkmxzt, gzt.cwblr, gzt.jcblr, gzt.cwshz,  gzt.jchdz ,gzt.jcpch,gzt.jczfrq, gzt.jgc FROM gc_zjgl_tqkmx t JOIN gc_zjgl_tqkbmmx gzt ON t.bmmxid=gzt.ID LEFT JOIN gc_htgl_htsj ghh    ON ghh.ID = t.htid  LEFT JOIN gc_htgl_ht ghh2    ON ghh.htid = ghh2.ID  LEFT JOIN view_zjgl_tqk_dbf vztd on vztd.htsjid=ghh.ID";

    // 删除提 请款明细
    private static String SQL_DEL_SQL = "DELETE FROM gc_zjgl_tqkmx WHERE ID=?";
    // 更新部门明细状态为退回
    private static String SQL_UPDATE_SQL = "UPDATE gc_zjgl_tqkbmmx SET bmtqkmxzt = '3', gxsj = SYSDATE  WHERE ID=?";

    // 语句将部门明细插入明细表CWSHZ
//    private static String SQL_INSERT_INTO = "insert into gc_zjgl_tqkmx  (id, htid, tqkid, bmmxid, dwid, dwmc, xmmcnr, htbm, zxhtj, ybf, bcsq, ljbf, ahtbfb, csz, jzqr, ajlfkb, ywlx, sjbh, sjmj, sfyx, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc,sortno) select id, htid, 'xxxxxxtqkidxxxxxx', ID,  dwid, dwmc, xmmcnr, htbm, zxhtj, (SELECT decode(SUM(ghh.zfje), NULL, 0, SUM(ghh.zfje)) FROM gc_htgl_ht_htzf ghh  WHERE ghh.sfyx = 1 AND ghh.htsjid = htid) as ybf, bcsq, ((SELECT decode(SUM(ghh.zfje), NULL, 0, SUM(ghh.zfje)) FROM gc_htgl_ht_htzf ghh  WHERE ghh.sfyx = 1 AND ghh.htsjid = htid)+bcsq) as ljbf, ahtbfb, csz, jzqr, ajlfkb,  ywlx, sjbh, sjmj, sfyx, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sortno from gc_zjgl_tqkbmmx WHERE ID IN(xxxxxidsxxxxxx)";
    																																																																																																																																																		// 【累计拨付的值】是 【财务审核值（Cwshz）】+【已拨付的值（Ybf）】
    private static String SQL_INSERT_INTO = "insert into gc_zjgl_tqkmx  (id, htid, tqkid, bmmxid, dwid, dwmc, xmmcnr, htbm, zxhtj, ybf, bcsq, ljbf, ahtbfb, csz, jzqr, ajlfkb, ywlx, sjbh, sjmj, sfyx, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc,sortno) select id, htid, 'xxxxxxtqkidxxxxxx', ID,  dwid, dwmc, xmmcnr, htbm, zxhtj, (SELECT decode(SUM(ghh.zfje), NULL, 0, SUM(ghh.zfje)) FROM gc_htgl_ht_htzf ghh  WHERE ghh.sfyx = 1 AND ghh.htsjid = htid) as ybf, bcsq, ((SELECT decode(SUM(ghh.zfje), NULL, 0, SUM(ghh.zfje)) FROM gc_htgl_ht_htzf ghh  WHERE ghh.sfyx = 1 AND ghh.htsjid = htid)+CWSHZ) as ljbf, ahtbfb, csz, jzqr, ajlfkb,  ywlx, sjbh, sjmj, sfyx, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sortno from gc_zjgl_tqkbmmx WHERE ID IN(xxxxxidsxxxxxx)";

    // 更新一些总金额
    private static String SQL_UPDATE_ZS = "UPDATE gc_zjgl_tqk gzt SET gzt.zzxhtj = (select sum(t.zxhtj) from GC_ZJGL_TQKMX t WHERE t.tqkid=gzt.ID ), gzt.zybf= (select sum(t.ybf) from GC_ZJGL_TQKMX t WHERE t.tqkid=gzt.ID ), gzt.zbcsq= (select sum(t.bcsq) from GC_ZJGL_TQKMX t WHERE t.tqkid=gzt.ID ), gzt.zcsz= (select sum(t.csz) from GC_ZJGL_TQKMX t WHERE t.tqkid=gzt.ID ), gzt.zjlqr= (select sum(t.JZQR) from GC_ZJGL_TQKMX t WHERE t.tqkid=gzt.ID ) WHERE gzt.ID=?";

    @Override
    public String queryCondition(String json, User user, HashMap map) throws Exception {

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

            String doSql = "";
            String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
            if (opttype.equals("tocwinsertedit")) {
                // 需要把 CWSHZ 在insert into 我状态下 财务审核值 设置为有值的状态
                doSql = SQL_QUERY_SQL_BCSQBK_TO_CWSHZ;
            } else if (opttype.equals("jcedit")) {
                doSql = SQL_QUERY_SQL_CWSHZ_TO_JCHDZ;
            } else {
                // 原值查出
                doSql = SQL_QUERY_SQL;
            }
            BaseResultSet bs = DBUtil.query(conn, doSql, page);
            // 合同表
            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");
            // 项目下达库
            // bs.setFieldTranslater("XDKID", "GC_TCJH_XMXDK", "ID", "XMMC");
            // 标段表
            // bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");

            // 设置字典
            // bs.setFieldDic("SJBH", "SJBH");//事件编号
            // bs.setFieldDic("SJMJ", "SJMJ");//数据密级

            // 设置查询条件
            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份
            // bs.setFieldThousand("DYJLSDZ");

            // 数字千分位
            bs.setFieldThousand("ZXHTJ");// 最新合同价
            bs.setFieldThousand("YBF");// 已拔付
            bs.setFieldThousand("BCSQ");// 本次申请拔款
            bs.setFieldThousand("LJBF");// 累计拔付
            bs.setFieldThousand("CSZ");// 财审值
            bs.setFieldThousand("SJZ");// 审计值值
            bs.setFieldThousand("JSZ");// 合同结算价
            bs.setFieldThousand("JZQR");// 监理确认计量款
            bs.setFieldThousand("JGC");// 甲供材

            if (opttype.equals("tocwedit")) {
                // 需要把 CWSHZ 不读为千分为 因为页面多行编辑number时有错， 必段为数字
            } else if (opttype.equals("tocwinsertedit")) {
                // 需要把 CWSHZ 财务审核值 设置为有值的状态
            } else if (opttype.equals("jcedit")) {
                // 需要把 JCHDZ 财务审核值 设置为有值的状态 非千分住
                bs.setFieldThousand("CWSHZ");// 财务审核值
            } else {
                // 原值查出
                bs.setFieldThousand("CWSHZ");// 财务审核值
                bs.setFieldThousand("JCHDZ");// 计财核定值
            }

            bs.setFieldThousand("DBF");// 待拔付

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<提请款明细>", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("提请款明细查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

    @Override
    public String insert(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglTqkmxVO vo = new GcZjglTqkmxVO();

        try {
            conn.setAutoCommit(false);

            String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
            if (opttype.equals("iinto")) {
                // 通过sql语句 insert into
                String ids = (String) map.get("ids");
                // String[] idss = StringUtils.split(ids, ",");
                // StringBuilder sBuilder = new StringBuilder();
                // for (String string : idss) {
                // sBuilder.append("");
                // }

                ids = ids.replaceAll(",", "','");
                ids = "'" + ids;
                ids = ids.substring(0, ids.length() - 2);

                String id = (String) map.get("id");

                String insql = SQL_INSERT_INTO.replaceAll("xxxxxxtqkidxxxxxx", id);
                insql = insql.replaceAll("xxxxxidsxxxxxx", ids);

                if (StringUtils.isNotBlank(id)) {
                    // DBUtil.executeUpdate(SQL_INSERT_INTO, new Object[] { id,
                    // ids });
                    DBUtil.execUpdateSql(conn, insql);
                }
            } else {
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
                DBUtil.executeUpdate(SQL_UPDATE_ZS, new Object[] { vo.getTqkid() });
            }

            resultVO = vo.getRowJson();
            conn.commit();
            if (opttype.equals("iinto")) {
                String id = (String) map.get("id");
                DBUtil.executeUpdate(SQL_UPDATE_ZS, new Object[] { id });
                conn.commit();
            }

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款明细新增成功", user, "", "");

            // String
            // jsona="{querycondition: {conditions: [{\"value\":\""+vo.getId()+"\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            // return queryCondition(jsona, user);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款明细新增失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款明细新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglTqkmxVO vo = new GcZjglTqkmxVO();

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

            DBUtil.executeUpdate(SQL_UPDATE_ZS, new Object[] { vo.getTqkid() });

            resultVO = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款明细修改成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款明细修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款明细修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcZjglTqkmxVO vo = new GcZjglTqkmxVO();
        try {
            conn.setAutoCommit(false);

            String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
            if (opttype.equals("delonly")) {
                // 仅移除本部门提交的记录中的某条
                String id = (String) map.get("ids");
                vo = findById(id);
                if (StringUtils.isNotBlank(id)) {
                    DBUtil.executeUpdate(SQL_DEL_SQL, new Object[] { id });
                    
                }
            } else if (opttype.equals("delandreturn")) {
                // 移除本部门提交的记录中的某条 并更新本条为退回状态
                String id = (String) map.get("ids");
                vo = findById(id);
                if (StringUtils.isNotBlank(id)) {
                	
                	//如果所有明细都被退回则退回部门提请款信息
                    String sql_return_tqkbm = "select count(*) from gc_zjgl_tqkbmmx t   where tqkid = "
                    	+"(select tqkid from gc_zjgl_tqkbmmx where id = "
               +"(select bmmxid from gc_zjgl_tqkmx "
               +" where id = '"+vo.getId()+"')) and t.bmtqkmxzt<>3";
                    String[][] sql_res = DBUtil.querySql(sql_return_tqkbm);
                    if(sql_res!=null&&"1".equals(sql_res[0][0])){
                    	String sql_reTqkBm = " update gc_zjgl_tqkbm t set t.tqkzt =3 where t.id = (select t2.tqkid from gc_zjgl_tqkbmmx t2 where t2.id = '"+vo.getBmmxid()+"')";
                    	DBUtil.execSql(sql_reTqkBm);
                    }
                	
                    DBUtil.executeUpdate(SQL_DEL_SQL, new Object[] { id });
                    // 更新部门明细的状态 BMTQKMXZT 3
                    DBUtil.executeUpdate(SQL_UPDATE_SQL, new Object[] { id });
                    String sql = "update gc_zjgl_tqkgcbmx t set t.GCBSHMXZT=3 where t.BMTQKMXID=?";
                    DBUtil.executeUpdate(sql, new Object[]{ id });
                    
                    
                    //sql = "update gc_zjgl_tqkgcb t set t.TQKZTGCB=3 where t.ID=(select h.GCBTQKID from gc_zjgl_tqkgcbmx h where h.BMTQKMXID = ?)";
                    //DBUtil.executeUpdate(sql, new Object[]{ id });
                }
            } else {
                JSONArray list = vo.doInitJson(json);
                JSONObject jsonObj = (JSONObject) list.get(0);
                vo.setValueFromJson(jsonObj);

                BaseDAO.delete(conn, vo);
            }

            resultVo = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款明细删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款明细删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款明细删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }
    public static void main(String[] args) {
		String s = "323983899";
		System.out.println(Pub.MoneyFormat(s));
	}
}
