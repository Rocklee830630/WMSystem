package com.ccthanking.business.zjgl.service.impl;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ccthanking.business.zjgl.service.GcZjglBmgkService;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class GcZjglBmgkServiceImpl implements GcZjglBmgkService {
    private String ywlx = YwlxManager.GC_XMGLGS_ZB;
    private static String SQL_QUERY_BMZFTJ = "select count(*) ZJKX,decode(sum(BCSQ),null,0,sum(BCSQ)) BCSQ  ,decode(sum(CWSHZ),null,0,sum(CWSHZ)) CWSH ,decode(sum(JCHDZ),null,0,sum(JCHDZ)) JCSH from gc_zjgl_tqkmx where to_char(LRSJ,'yyyy')='xxxbmndxxx'";
    private static String SQL_QUERY_BMZ = "select count(*) ZZJKX,decode(sum(BCSQ),null,0,sum(BCSQ)) ZBCSQ  ,decode(sum(CWSHZ),null,0,sum(CWSHZ)) ZCWSH ,decode(sum(JCHDZ),null,0,sum(JCHDZ)) ZJCSH from gc_zjgl_tqkmx";
    private static String SQL_QUERY_ZLR = "select count(*) ZLR from gc_zjgl_tqk";
    private static String SQL_QUERY_LR = "select count(*) LR from gc_zjgl_tqk where QKNF='xxxbmndxxx'";
    private static String SQL_QUERY_ZZSZJ = "select decode(sum(ZFJE),null,0,sum(ZFJE)) ZLJBF ,count(*) ZBS from GC_ZJGL_ZSZJ_ZFZSB";
    private static String SQL_QUERY_ZSZJ = "select decode(sum(ZFJE),null,0,sum(ZFJE)) LJBF ,count(*) BS from GC_ZJGL_ZSZJ_ZFZSB where to_char(ZFRQ,'yyyy')='xxxbmndxxx'";
    private static String SQL_QUERY_ZLVBZJ = "select decode(sum(je),null,0,sum(je)) JE, count(*) B from gc_zjgl_lybzj where sfyx='1'";
    private static String SQL_QUERY_NFLVBZJ = "select decode(sum(je),null,0,sum(je)) NDJE, count(*) NDB from gc_zjgl_lybzj where to_char(JNRQ,'yyyy')='xxxbmndxxx' and sfyx='1'";
    private static String SQL_QUERY_XJNFLVBZJ = "select decode(sum(je),null,0,sum(je)) XJJE,count(*) XJCOUNT from gc_zjgl_lybzj where jnfs='XJ' and to_char(JNRQ,'yyyy')='xxxbmndxxx' and sfyx='1'";
    private static String SQL_QUERY_BHNFLVBZJ = "select decode(sum(je),null,0,sum(je)) BHJE,count(*) BHCOUNT from gc_zjgl_lybzj where jnfs='BH' and to_char(JNRQ,'yyyy')='xxxbmndxxx' and sfyx='1'";
    private static String SQL_QUERY_ZBHLVBZJ = "select decode(sum(je),null,0,sum(je)) ZBHJE,count(*) ZBHBZJ from gc_zjgl_lybzj where jnfs='BH' and sfyx='1'";
    private static String SQL_QUERY_ZXJLVBZJ = "select decode(sum(je),null,0,sum(je)) ZXJJE,count(*) ZXJBZJ from gc_zjgl_lybzj where jnfs='XJ' and sfyx='1'";
    private static String SQL_QUERY_LJFHBZJ = "select decode(sum(je),null,0,sum(je)) LJFHBZJ,count(*) LJFHBZJBS  from gc_zjgl_lybzj where FHQK='1' AND SFYX='1'";
    private static String SQL_QUERY_NFLJFHBZJ = "select decode(sum(je),null,0,sum(je)) NFLJFHBZJ,count(*) NFLJFHBZJBS  from gc_zjgl_lybzj where FHQK='1' and   to_char(FHRQ,'yyyy')='xxxbmndxxx' AND SFYX='1'";
    private static String SQL_QUERY_CLJFHBZJ = "select decode( sum((select decode(sum(je),null,0,sum(je)) from gc_zjgl_lybzj where  to_char(FHRQ,'yyyy')='xxxbmndxxx'   AND SFYX='1')-(select decode(sum(je),null,0,sum(je)) from gc_zjgl_lybzj where FHQK='1' and   to_char(FHRQ,'yyyy')='xxxbmndxxx' AND SFYX='1')),null,0, sum((select decode(sum(je),null,0,sum(je)) from gc_zjgl_lybzj where  to_char(FHRQ,'yyyy')='xxxbmndxxx' AND SFYX='1' )-(select decode(sum(je),null,0,sum(je)) from gc_zjgl_lybzj where FHQK='1' and   to_char(FHRQ,'yyyy')='xxxbmndxxx' AND SFYX='1'))) CE,count(*) CET from gc_zjgl_lybzj WHERE SFYX='1'";
    // 即将
    private static String SQL_QUERY_LYBZJJJSX = "select decode(sum(je),null,0,sum(je)) JJSH ,count(*) JJSHTS from gc_zjgl_lybzj t where to_date(to_char(current_date,'YYYYMMDD'),'YYYYMMDDHH24MISS') < t.bhyxqz and  to_date(to_char(current_date,'YYYYMMDD'),'YYYYMMDDHH24MISS')+30 > t.BHYXQZ  and t.JNFS='BH' and sfyx='1'";
    // 已
    private static String SQL_QUERY_LYBZJYSX = "select decode(sum(je),null,0,sum(je)) BHSX,count(*) SHTS from gc_zjgl_lybzj t where to_date(to_char(current_date,'YYYYMMDD'),'YYYYMMDDHH24MISS')> t.BHYXQZ  and t.JNFS='BH' and sfyx='1'";
    // 年度使用金额
    private static String SQL_QUERY_NDSYJE = "select decode(sum(ZFJE),null,0,sum(ZFJE)) NDSHJE ,COUNT(*) NDSHJESUM from gc_zjgl_zszj_xmzszjqk WHERE to_char(ZFRQ,'yyyy')='xxxbmndxxx'";
    // 累计使用金额
    private static String SQL_QUERY_SYJE = "select decode(sum(ZFJE),null,0,sum(ZFJE)) SHJE ,COUNT(*) SHJESUM from gc_zjgl_zszj_xmzszjqk";
    // 累计拨付余额
    private static String SQL_QUERY_LJBFYE = "select decode(((select decode(sum(ZFJE),null,0,sum(ZFJE)) ZLJBF from GC_ZJGL_ZSZJ_ZFZSB)-(select decode(sum(ZFJE),null,0,sum(ZFJE)) SHJE  from gc_zjgl_zszj_xmzszjqk)),null,0,((select decode(sum(ZFJE),null,0,sum(ZFJE)) ZLJBF from GC_ZJGL_ZSZJ_ZFZSB)-(select decode(sum(ZFJE),null,0,sum(ZFJE)) SHJE  from gc_zjgl_zszj_xmzszjqk))) YE from dual";
    // 根据部门查询信息
//    private static String SQL_QUERY_BMCX = "select GXBMMC XMBH, count(*) KSSJ, decode(sum(BCSQ),null,0,sum(BCSQ)) JSSJ,decode(sum(CWSHZ),null,0,sum(CWSHZ)) ZJLBZ,decode(sum(JCHDZ),null,0,sum(JCHDZ)) ZJLLJWC,decode(round(sum(JCHDZ)/sum(CWSHZ),2)*100,null,0,round(sum(JCHDZ)/sum(CWSHZ),2)*100) CB from gc_zjgl_tqkmx where to_char(LRSJ,'yyyy')='xxxbmndxxx' group by  GXBMMC";
    private static String SQL_QUERY_BMCX = "SELECT vzbcod.sqdw, vzbcod.dept_name, (SELECT COUNT(*) FROM view_zjgl_bmgk_cwb vzbc WHERE vzbcod.sqdw=vzbc.sqdw  xxxbmndxxx) AS KSSJ ,(SELECT SUM(vzbc.bcsq) FROM view_zjgl_bmgk_cwb vzbc WHERE vzbcod.sqdw=vzbc.sqdw  xxxbmndxxx) AS JSSJ,(SELECT SUM(vzbc.cwshz) FROM view_zjgl_bmgk_cwb vzbc WHERE vzbcod.sqdw=vzbc.sqdw  xxxbmndxxx) AS ZJLBZ,(SELECT SUM(vzbc.jchdz) FROM view_zjgl_bmgk_cwb vzbc WHERE vzbcod.sqdw=vzbc.sqdw  xxxbmndxxx) AS ZJLLJWC,ROUND( (SELECT (SUM(vzbc.bcsq)-SUM(vzbc.jchdz))/SUM(vzbc.bcsq) FROM view_zjgl_bmgk_cwb vzbc WHERE vzbcod.sqdw=vzbc.sqdw  xxxbmndxxx ), 2)*100 AS cb FROM view_zjgl_bmgk_cwb_org_dept vzbcod  order by sort asc";
    
    private static String SQL_QUERY_QYLIST_ND = "SELECT fdt.jhsjid,gjs.xmid,gjs.bdid, gjs.xmmc, gjs.bdmc,gjs.bdbh, "
		+" (SELECT sum(bs) FROM view_zjgl_zszj_zfzsb vzzz WHERE vzzz.jhsjid = fdt.jhsjid and vzzz.bfyear IN ('xxxxcYearxxx')) AS bsbf, "
		+" (SELECT sum(zbfje) FROM view_zjgl_zszj_zfzsb vzzz WHERE vzzz.jhsjid = fdt.jhsjid and vzzz.bfyear IN ('xxxxcYearxxx')) AS jebf, "
		+" (SELECT sum(bs) FROM view_zjgl_zszj_xmsyqk vzzx WHERE vzzx.jhsjid = fdt.jhsjid and vzzx.syyear IN ('xxxxcYearxxx')) AS bssy,  "
		+" (SELECT sum(vzzx.zsyje) FROM view_zjgl_zszj_xmsyqk vzzx WHERE vzzx.jhsjid = fdt.jhsjid and vzzx.syyear IN ('xxxxcYearxxx')) AS jesy "
		+" FROM view_zjgl_zszj fdt   "
		+" LEFT JOIN gc_jh_sj gjs    ON fdt.jhsjid = gjs.gc_jh_sj_id ";
    
    private static String SQL_QUERY_QYLIST = "SELECT fdt.jhsjid,gjs.xmid,gjs.bdid, gjs.xmmc, gjs.bdmc,gjs.bdbh, "
		+" (SELECT sum(bs) FROM view_zjgl_zszj_zfzsb vzzz WHERE vzzz.jhsjid = fdt.jhsjid ) AS bsbf, "
		+" (SELECT sum(zbfje) FROM view_zjgl_zszj_zfzsb vzzz WHERE vzzz.jhsjid = fdt.jhsjid ) AS jebf, "
		+" (SELECT sum(bs) FROM view_zjgl_zszj_xmsyqk vzzx WHERE vzzx.jhsjid = fdt.jhsjid ) AS bssy,  "
		+" (SELECT sum(vzzx.zsyje) FROM view_zjgl_zszj_xmsyqk vzzx WHERE vzzx.jhsjid = fdt.jhsjid ) AS jesy "
		+" FROM view_zjgl_zszj fdt   "
		+" LEFT JOIN gc_jh_sj gjs    ON fdt.jhsjid = gjs.gc_jh_sj_id ";
    @Override
    public String queryBmList(String json, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub

        Connection conn = DBUtil.getConnection();
        String domresult = "";

        String sqlString = "";
        String cYear = map.get("nd") == null ? "" : (String) map.get("nd");
        try {

            // String cYear = Pub.getDate("yyyy");
            if (StringUtils.isNotBlank(cYear)) {
                // 组织当前年度数据查询
                sqlString = SQL_QUERY_BMZFTJ.replaceAll("xxxbmndxxx", cYear);
            } else {
                cYear = Pub.getDate("yyyy");
                sqlString = SQL_QUERY_BMZFTJ.replaceAll("xxxbmndxxx", cYear);
            }

            // String preYear = String.valueOf(Integer.parseInt(cYear) - 1);

            // 组织查询条件

            conn.setAutoCommit(false);

            // String sql = SQL_QUERY_QYLIST.replaceAll("xxxxpreYearxxx",
            // preYear);
            // sql = sql.replaceAll("xxxxcYearxxx", cYear);

            BaseResultSet bs = DBUtil.query(conn, sqlString, null);

            bs.setFieldThousand("BCSQ");
            bs.setFieldThousand("CWSH");
            bs.setFieldThousand("JCSH");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryBmZList(String json, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);

            sqlString = SQL_QUERY_BMZ;

            BaseResultSet bs = DBUtil.query(conn, sqlString, null);

            bs.setFieldThousand("ZBCSQ");
            bs.setFieldThousand("ZCWSH");
            bs.setFieldThousand("ZJCSH");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryPCList(String json, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";

        String sqlString = "";
        String cYear = map.get("nd") == null ? "" : (String) map.get("nd");
        try {

            // String cYear = Pub.getDate("yyyy");
            if (StringUtils.isNotBlank(cYear)) {
                // 组织当前年度数据查询
                sqlString = SQL_QUERY_LR.replaceAll("xxxbmndxxx", cYear);
            } else {
                cYear = Pub.getDate("yyyy");
                sqlString = SQL_QUERY_LR.replaceAll("xxxbmndxxx", cYear);
            }
            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryPCZList(String json, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);
            sqlString = SQL_QUERY_ZLR;
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    // 年份征收资金
    public String queryZSZJList(String json, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub

        Connection conn = DBUtil.getConnection();
        String domresult = "";

        String sqlString = "";
        String cYear = map.get("nd") == null ? "" : (String) map.get("nd");
        try {

            // String cYear = Pub.getDate("yyyy");
            if (StringUtils.isNotBlank(cYear)) {
                // 组织当前年度数据查询
                sqlString = SQL_QUERY_ZSZJ.replaceAll("xxxbmndxxx", cYear);
            } else {
                cYear = Pub.getDate("yyyy");
                sqlString = SQL_QUERY_ZSZJ.replaceAll("xxxbmndxxx", cYear);
            }
            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("LJBF");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    // 总征收资金
    public String queryZZSZJList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);
            sqlString = SQL_QUERY_ZZSZJ;
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("ZLJBF");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }

        return domresult;
    }

    @Override
    // 年份履约保证金
    public String queryNFLYBZJList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub

        Connection conn = DBUtil.getConnection();
        String domresult = "";

        String sqlString = "";
        String cYear = map.get("nd") == null ? "" : (String) map.get("nd");
        try {

            // String cYear = Pub.getDate("yyyy");
            if (StringUtils.isNotBlank(cYear)) {
                // 组织当前年度数据查询
                sqlString = SQL_QUERY_NFLVBZJ.replaceAll("xxxbmndxxx", cYear);
            } else {
                cYear = Pub.getDate("yyyy");
                sqlString = SQL_QUERY_NFLVBZJ.replaceAll("xxxbmndxxx", cYear);
            }
            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("NDJE");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }

        return domresult;
    }

    @Override
    // 总履约保证金
    public String queryZLYBZJList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);
            sqlString = SQL_QUERY_ZLVBZJ;
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("JE");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryBHNFLYBZJList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        String cYear = map.get("nd") == null ? "" : (String) map.get("nd");
        try {
            if (StringUtils.isNotBlank(cYear)) {
                // 组织当前年度数据查询
                sqlString = SQL_QUERY_BHNFLVBZJ.replaceAll("xxxbmndxxx", cYear);
            } else {
                cYear = Pub.getDate("yyyy");
                sqlString = SQL_QUERY_BHNFLVBZJ.replaceAll("xxxbmndxxx", cYear);
            }
            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);

            bs.setFieldThousand("BHJE");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }

        return domresult;
    }

    @Override
    public String queryXJNFLYBZJList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        String cYear = map.get("nd") == null ? "" : (String) map.get("nd");
        try {
            if (StringUtils.isNotBlank(cYear)) {
                // 组织当前年度数据查询
                sqlString = SQL_QUERY_XJNFLVBZJ.replaceAll("xxxbmndxxx", cYear);
            } else {
                cYear = Pub.getDate("yyyy");
                sqlString = SQL_QUERY_XJNFLVBZJ.replaceAll("xxxbmndxxx", cYear);
            }
            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);

            bs.setFieldThousand("XJJE");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryZBHLYBZJList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);
            sqlString = SQL_QUERY_ZBHLVBZJ;
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("ZBHJE");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

    @Override
    public String queryZXJLYBZJList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);
            sqlString = SQL_QUERY_ZXJLVBZJ;
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("ZXJJE");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryLJFHLVBZJList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);
            sqlString = SQL_QUERY_LJFHBZJ;
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("LJFHBZJ");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryNFLJFHLVBZJList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        String cYear = map.get("nd") == null ? "" : (String) map.get("nd");
        try {
            if (StringUtils.isNotBlank(cYear)) {
                // 组织当前年度数据查询
                sqlString = SQL_QUERY_NFLJFHBZJ.replaceAll("xxxbmndxxx", cYear);
            } else {
                cYear = Pub.getDate("yyyy");
                sqlString = SQL_QUERY_NFLJFHBZJ.replaceAll("xxxbmndxxx", cYear);
            }
            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("NFLJFHBZJ");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryCLJFHLVBZJList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);
            sqlString = SQL_QUERY_CLJFHBZJ;
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("CE");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }

        return domresult;
    }

    @Override
    // 即将
    public String queryJJSXList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);
            sqlString = SQL_QUERY_LYBZJJJSX;
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("JJSH");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    // 已
    public String queryYSXList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);
            sqlString = SQL_QUERY_LYBZJYSX;
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("BHSX");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryNDSYJEList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        String cYear = map.get("nd") == null ? "" : (String) map.get("nd");
        try {
            if (StringUtils.isNotBlank(cYear)) {
                // 组织当前年度数据查询
                sqlString = SQL_QUERY_NDSYJE.replaceAll("xxxbmndxxx", cYear);
            } else {
                cYear = Pub.getDate("yyyy");
                sqlString = SQL_QUERY_NDSYJE.replaceAll("xxxbmndxxx", cYear);
            }
            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("NDSHJE");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }

        return domresult;
    }

    @Override
    public String querySYJEList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);
            sqlString = SQL_QUERY_SYJE;
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("SHJE");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryLJBFYEList(String msg, User user, HashMap map) throws Exception {
        // TODO Auto-generated method stub
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        try {
            conn.setAutoCommit(false);
            sqlString = SQL_QUERY_LJBFYE;
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("YE");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }

        return domresult;
    }

    @Override
    public String queryBMPXList(String msg, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        String sqlString = "";
        String cYear = map.get("nd") == null ? "" : (String) map.get("nd");
        try {
            if (StringUtils.isNotBlank(cYear)) {
                // 组织当前年度数据查询
                sqlString = SQL_QUERY_BMCX.replaceAll("xxxbmndxxx", "AND vzbc.qknf='"+cYear+"'");
            } else {
                cYear = Pub.getDate("yyyy");
                sqlString = SQL_QUERY_BMCX.replaceAll("xxxbmndxxx", "AND vzbc.qknf='"+cYear+"'");
            }
            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, sqlString, null);
            bs.setFieldThousand("JSSJ");
            bs.setFieldThousand("ZJLBZ");
            bs.setFieldThousand("ZJLLJWC");
            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<部门支付信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }

        return domresult;
    }
    
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
                sqlString = SQL_QUERY_QYLIST_ND.replaceAll("xxxxcYearxxx", cYear);
            } else {
                sqlString = SQL_QUERY_QYLIST;
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
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }
}
