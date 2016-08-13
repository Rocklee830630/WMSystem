/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglZszjZfzsbService.java
 * 创建日期： 2013-08-30 上午 12:49:28
 * 功能：    接口：支付征收办
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-08-30 上午 12:49:28  蒋根亮   创建文件，实现基本功能
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
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.stereotype.Service;

import com.ccthanking.business.zjgl.service.GcZjglZszjZfzsbService;
import com.ccthanking.business.zjgl.vo.GcZjglZszjZfzsbVO;
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
import com.ccthanking.framework.util.QueryCondition;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * <p>
 * GcZjglZszjZfzsbService.java
 * </p>
 * <p>
 * 功能：支付征收办
 * </p>
 * 
 * <p>
 * <a href="GcZjglZszjZfzsbService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-08-30
 * 
 */

@Service
public class GcZjglZszjZfzsbServiceImpl extends Base1ServiceImpl<GcZjglZszjZfzsbVO, String> implements GcZjglZszjZfzsbService {

    private static Logger logger = LoggerFactory.getLogger(GcZjglZszjZfzsbServiceImpl.class);

    // 业务类型
    private String ywlx = YwlxManager.GC_ZJGL_ZSZJ_ZFZSB;

    // 按所有区域查询
    // private static String SQL_QUERY_QYLIST =
    // "SELECT fdt.dic_code, fdt.dic_value, (SELECT COUNT(*) FROM gc_zjgl_zszj_zfzsb gzzz WHERE gzzz.qy=fdt.dic_code) AS bsbf, (SELECT sum(zfje) FROM gc_zjgl_zszj_zfzsb gzzz WHERE gzzz.qy=fdt.dic_code ) AS zjebf , (SELECT zfrq FROM gc_zjgl_zszj_zfzsb gzzz WHERE gzzz.qy=fdt.dic_code AND ROWNUM=1 ) AS zxsjbf ,(SELECT COUNT(*) FROM gc_zjgl_zszj_xmzszjqk gzzx WHERE gzzx.qy=fdt.dic_code) AS bssy,(SELECT sum(zfje) FROM gc_zjgl_zszj_xmzszjqk gzzx WHERE gzzx.qy=fdt.dic_code) AS zjesy,(SELECT zfrq FROM gc_zjgl_zszj_xmzszjqk gzzx WHERE gzzx.qy=fdt.dic_code AND ROWNUM = 1) AS zxsjsy, ((SELECT sum(zfje)  FROM gc_zjgl_zszj_zfzsb gzzz WHERE gzzz.qy = fdt.dic_code)-(SELECT sum(zfje)  FROM gc_zjgl_zszj_xmzszjqk gzzx WHERE gzzx.qy = fdt.dic_code) ) AS ye FROM fs_dic_tree fdt ";
    // private static String SQL_QUERY_QYLIST =
    // "SELECT fdt.jhsjid,       gjs.xmid,       gjs.bdid,        gjs.xmmc,       gjs.bdmc,       gjs.bdbh,       (SELECT sum(bs) FROM view_zjgl_zszj_zfzsb vzzz WHERE vzzz.jhsjid = fdt.jhsjid and vzzz.bfyear IN('xxxxpreYearxxx', 'xxxxcYearxxx')) AS bsbf, (SELECT sum(zbfje) FROM view_zjgl_zszj_zfzsb vzzz WHERE vzzz.jhsjid = fdt.jhsjid and vzzz.bfyear IN('xxxxpreYearxxx')) AS zjebf,        (SELECT sum(zbfje) FROM view_zjgl_zszj_zfzsb vzzz WHERE vzzz.jhsjid = fdt.jhsjid and vzzz.bfyear IN('xxxxcYearxxx')) AS zxsjbf,      (SELECT sum(bs) FROM view_zjgl_zszj_xmsyqk vzzx WHERE vzzx.jhsjid = fdt.jhsjid and vzzx.syyear IN('xxxxpreYearxxx', 'xxxxcYearxxx')) AS bssy,       (SELECT sum(vzzx.zsyje) FROM view_zjgl_zszj_xmsyqk vzzx WHERE vzzx.jhsjid = fdt.jhsjid and vzzx.syyear IN('xxxxpreYearxxx')) AS zjesy,         (SELECT sum(vzzx.zsyje) FROM view_zjgl_zszj_xmsyqk vzzx WHERE vzzx.jhsjid = fdt.jhsjid and vzzx.syyear IN('xxxxcYearxxx')) AS zxsjsy,       ((SELECT SUM(zbfje) FROM view_zjgl_zszj_zfzsb vzzz WHERE vzzz.jhsjid=fdt.jhsjid and vzzz.bfyear IN('xxxxpreYearxxx', 'xxxxcYearxxx'))-(SELECT SUM(vzzx.zsyje) FROM view_zjgl_zszj_xmsyqk vzzx WHERE vzzx.jhsjid=fdt.jhsjid AND vzzx.syyear IN('xxxxpreYearxxx', 'xxxxcYearxxx'))) AS ye FROM view_zjgl_zszj fdt  LEFT JOIN gc_jh_sj gjs    ON fdt.jhsjid = gjs.gc_jh_sj_id ";
    private static String SQL_QUERY_QYLIST = "SELECT fdt.jhsjid,       gjs.xmid,       gjs.bdid,       gjs.xmmc,       gjs.bdmc,       gjs.bdbh,       (SELECT sum(bs)          FROM view_zjgl_zszj_zfzsb vzzz         WHERE vzzz.jhsjid = fdt.jhsjid           and vzzz.bfyear IN ('xxxxcYearxxx')) AS bsbf,       (SELECT sum(zbfje)          FROM view_zjgl_zszj_zfzsb vzzz         WHERE vzzz.jhsjid = fdt.jhsjid           and vzzz.bfyear IN ('xxxxcYearxxx')) AS zjebf,       (SELECT sum(zbfje)          FROM view_zjgl_zszj_zfzsb vzzz         WHERE vzzz.jhsjid = fdt.jhsjid) AS zxsjbf,       (SELECT sum(bs)          FROM view_zjgl_zszj_xmsyqk vzzx         WHERE vzzx.jhsjid = fdt.jhsjid           and vzzx.syyear IN ('xxxxcYearxxx')) AS bssy,       (SELECT sum(vzzx.zsyje)          FROM view_zjgl_zszj_xmsyqk vzzx         WHERE vzzx.jhsjid = fdt.jhsjid           and vzzx.syyear IN ('xxxxcYearxxx')) AS zjesy,       (SELECT sum(vzzx.zsyje)          FROM view_zjgl_zszj_xmsyqk vzzx         WHERE vzzx.jhsjid = fdt.jhsjid) AS zxsjsy,       ((SELECT DECODE(SUM(zbfje), null ,0,SUM(zbfje))    FROM view_zjgl_zszj_zfzsb vzzz          WHERE vzzz.jhsjid = fdt.jhsjid) -       (SELECT DECODE( SUM(vzzx.zsyje), null ,0,SUM(vzzx.zsyje))           FROM view_zjgl_zszj_xmsyqk vzzx          WHERE vzzx.jhsjid = fdt.jhsjid)) AS ye,(case gjs.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = gjs.nd and GC_TCJH_XMXDK_ID = gjs.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = gjs.bdid and rownum = 1) end ) as XMBDDZ  FROM view_zjgl_zszj fdt  LEFT JOIN gc_jh_sj gjs    ON fdt.jhsjid = gjs.gc_jh_sj_id ";
    private static String SQL_QUERY_ZJINFO = "SELECT fdt.jhsjid, gjs.xmid, gjs.bdid, gjs.xmmc, gjs.bdmc, gjs.bdbh,  ( SELECT SUM (zbfje) FROM view_zjgl_zszj_zfzsb vzzz WHERE vzzz.jhsjid = fdt.jhsjid ) AS zxsjbf, ( SELECT SUM (vzzx.zsyje) FROM view_zjgl_zszj_xmsyqk vzzx WHERE vzzx.jhsjid = fdt.jhsjid ) AS zxsjsy, (SELECT DECODE (SUM(bs), NULL, 0, SUM(bs))    FROM view_zjgl_zszj_zfzsb vzzz         WHERE vzzz.jhsjid = fdt.jhsjid        ) AS bsbf, (SELECT DECODE ( SUM (bs), NULL, 0, SUM (bs) )          FROM view_zjgl_zszj_xmsyqk vzzx         WHERE vzzx.jhsjid = fdt.jhsjid        ) AS bssy, ( ( SELECT DECODE ( SUM (zbfje), NULL, 0, SUM (zbfje)) FROM view_zjgl_zszj_zfzsb vzzz WHERE vzzz.jhsjid = fdt.jhsjid ) -  ( SELECT DECODE ( SUM (vzzx.zsyje), NULL, 0, SUM (vzzx.zsyje) ) FROM view_zjgl_zszj_xmsyqk vzzx WHERE vzzx.jhsjid = fdt.jhsjid )) AS ye FROM view_zjgl_zszj fdt LEFT JOIN gc_jh_sj gjs ON fdt.jhsjid = gjs.gc_jh_sj_id";
    
    @Override
    public String queryQyList(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // String cYear = Pub.getDate("yyyy");
            String sqlString = "";
            String cYear = map.get("nd") == null ? "" : (String) map.get("nd");
            if (StringUtils.isNotBlank(cYear)) {
                // 组织当前年度数据查询
                sqlString = SQL_QUERY_QYLIST.replaceAll("xxxxcYearxxx", cYear);
            } else {
                cYear = Pub.getDate("yyyy");
                sqlString = SQL_QUERY_QYLIST.replaceAll("xxxxcYearxxx", cYear);
            }

            // String preYear = String.valueOf(Integer.parseInt(cYear) - 1);

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            QueryConditionList list = RequestUtil.getConditionList(json);

            String condition = "";// fdt.parent_id='1000000000243' ";

            // ghh.htid需要合同号
            condition = list.getConditionWhere();

            // 组织查询条件
            String orderFilter = RequestUtil.getOrderFilter(json);

            // condition += BusinessUtil.getSJYXCondition("fdt");// 是否有效
            condition += BusinessUtil.getCommonCondition(user, null);
            // condition += " GROUP BY xx ";// 排序
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            // String sql = SQL_QUERY_QYLIST.replaceAll("xxxxpreYearxxx",
            // preYear);
            // sql = sql.replaceAll("xxxxcYearxxx", cYear);

            BaseResultSet bs = DBUtil.query(conn, sqlString, page);

            bs.setFieldThousand("ZJEBF");
            bs.setFieldThousand("ZJESY");
            bs.setFieldThousand("ZXSJBF");
            bs.setFieldThousand("ZXSJSY");
            bs.setFieldThousand("YE");

            // 设置字典
            // bs.setFieldDic("BGLX", "BGLX");
            // bs.setFieldDic("HTLX", "HTLX");
            // bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            // bs.setFieldDic("FBFS", "FBFS");// 发包方式
            // bs.setFieldDic("WCTZLX", "ZFYT");// 完成投资类型==支付用途

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<征收资金>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("征收资金查询失败!");
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
        GcZjglZszjZfzsbVO xmvo = new GcZjglZszjZfzsbVO();

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
                    .getOrgDept().getDeptName() + " " + user.getName() + "支付征收办新增成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + xmvo.getId()
                    + "\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user);
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("支付征收办新增失败!");
            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "支付征收办新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglZszjZfzsbVO vo = new GcZjglZszjZfzsbVO();

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
                    .getDeptName() + " " + user.getName() + "支付征收办修改成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("支付征收办修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "支付征收办修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcZjglZszjZfzsbVO vo = new GcZjglZszjZfzsbVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            BaseDAO.delete(conn, vo);

            resultVo = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "支付征收办删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("支付征收办删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "支付征收办删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }

    @Override
    public String queryCondition(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            // String condition =
            // RequestUtil.getConditionList(json).getConditionWhere();
            StringBuilder condition = new StringBuilder(" 1=1 ");
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
            String sql = "SELECT vzzzt.tqkmxid, vzzzt.qkmc, gjs.xmmc, gjs.bdmc, gjs.xmbh, gjs.xmid, gjs.bdid, t.*  FROM  GC_ZJGL_ZSZJ_ZFZSB t LEFT JOIN gc_jh_sj gjs ON t.jhsjid=gjs.gc_jh_sj_id LEFT JOIN view_zjgl_zszj_zfzsb_tqk vzzzt ON t.bkxgdid=vzzzt.tqkmxid ";
            // String sql =
            // "SELECT vzzzt.tqkmxid, vzzzt.qkmc, xmmc, bdmc, xmid, bdid, t.* FROM GC_ZJGL_ZSZJ_ZFZSB t LEFT JOIN view_zjgl_zszj_zfzsb_tqk vzzzt  ON t.bkxgdid = vzzzt.tqkmxid ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            // 合同表
            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");
            // 项目下达库
            // bs.setFieldTranslater("XDKID", "GC_TCJH_XMXDK", "ID", "XMMC");
            // 标段表
            // bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");

            // 设置字典
            bs.setFieldDic("QY", "QY");
            // bs.setFieldDic("HTZT", "HTQDZT");// 合同签订状态
            // bs.setFieldDic("FBFS", "FBFS");// 发包方式
            // bs.setFieldDic("BXQDWLX", "BXQDWLX");// 保修期单位：年、季、月、日
            // bs.setFieldDic("QDNF", "XMNF");// 项目年份

            // 设置查询条件
            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份
            bs.setFieldThousand("ZFJE");
            bs.setFieldUserID("JBR");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<支付征收办>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("支付征收办查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

	@Override
	public String queryQueryZjInfo(String json, User user) throws Exception {
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
            // String sql = "SELECT * FROM " + "GC_ZJGL_TQK t";
            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_ZJINFO, page);


            bs.setFieldThousand("ZXSJBF");// 总最新合同价
            bs.setFieldThousand("ZXSJSY");// 总最新合同价
            bs.setFieldThousand("YE");// 总最新合同价
            
            


            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<提请款>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("提请款查询失败!");
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
	            // String condition =
	            // RequestUtil.getConditionList(json).getConditionWhere();
	            StringBuilder condition = new StringBuilder(" 1=1 ");
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
	            String sql = "SELECT sum(t.ZFJE) ZFJE FROM  GC_ZJGL_ZSZJ_ZFZSB t LEFT JOIN gc_jh_sj gjs ON t.jhsjid=gjs.gc_jh_sj_id LEFT JOIN view_zjgl_zszj_zfzsb_tqk vzzzt ON t.bkxgdid=vzzzt.tqkmxid ";
	            // String sql =
	            // "SELECT vzzzt.tqkmxid, vzzzt.qkmc, xmmc, bdmc, xmid, bdid, t.* FROM GC_ZJGL_ZSZJ_ZFZSB t LEFT JOIN view_zjgl_zszj_zfzsb_tqk vzzzt  ON t.bkxgdid = vzzzt.tqkmxid ";
	            BaseResultSet bs = DBUtil.query(conn, sql, page);
	            // 合同表
	            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");
	            // 项目下达库
	            // bs.setFieldTranslater("XDKID", "GC_TCJH_XMXDK", "ID", "XMMC");
	            // 标段表
	            // bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");

	            // 设置字典
	            bs.setFieldDic("QY", "QY");
	            // bs.setFieldDic("HTZT", "HTQDZT");// 合同签订状态
	            // bs.setFieldDic("FBFS", "FBFS");// 发包方式
	            // bs.setFieldDic("BXQDWLX", "BXQDWLX");// 保修期单位：年、季、月、日
	            // bs.setFieldDic("QDNF", "XMNF");// 项目年份

	            // 设置查询条件
	            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份
	            bs.setFieldThousand("ZFJE");

	            domresult = bs.getJson();
	            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
	                    + " " + user.getName() + "查询<支付征收办>", user, "", "");
	        } catch (Exception e) {
	            DBUtil.rollbackConnetion(conn);
	            logger.error("支付征收办查询失败!");
	            e.printStackTrace(System.out);
	        } finally {
	            DBUtil.closeConnetion(conn);
	        }
	        return domresult;

	    }
}
