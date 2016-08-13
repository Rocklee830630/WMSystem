/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglTqkService.java
 * 创建日期： 2013-09-25 下午 02:36:20
 * 功能：    接口：提请款
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-25 下午 02:36:20  蒋根亮   创建文件，实现基本功能
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

import com.ccthanking.business.zjgl.service.GcZjglTqkService;
import com.ccthanking.business.zjgl.vo.GcZjglTqkVO;
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
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * <p>
 * GcZjglTqkService.java
 * </p>
 * <p>
 * 功能：提请款
 * </p>
 * 
 * <p>
 * <a href="GcZjglTqkService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-25
 * 
 */

@Service
public class GcZjglTqkServiceImpl extends Base1ServiceImpl<GcZjglTqkVO, String> implements GcZjglTqkService {

    private static Logger logger = LoggerFactory.getLogger(GcZjglTqkServiceImpl.class);

    // 业务类型
    private String ywlx = YwlxManager.GC_ZJGL_TQK;

    // 语句将部门明细插入明细表
    // 更新ybf 已拨付：合同支付+待支付，也就是计财实际审定已拨付的金额+建管中心已审批但计财还未反馈审定值的统计
    // 在财务提请款都需要用这个值
    // private static String SQL_INSERT_INTO =
    // "insert into gc_zjgl_tqkmx (id, htid, tqkid, bmmxid, dwid, dwmc, xmmcnr, htbm, zxhtj, ybf, bcsq, ljbf, ahtbfb, csz, jzqr, ajlfkb, cwshz, jchdz, ywlx, sjbh, sjmj, sfyx, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sortno) SELECT id, htid, 'xxxxxxtqkidxxxxxx', id, dwid, dwmc, xmmcnr, htbm, zxhtj, (SELECT decode(SUM(ghh.zfje), NULL, 0, SUM(ghh.zfje)) FROM gc_htgl_ht_htzf ghh  WHERE ghh.sfyx = 1 AND ghh.htsjid = htid) as ybf, bcsq, ((SELECT decode(SUM(ghh.zfje), NULL, 0, SUM(ghh.zfje)) FROM gc_htgl_ht_htzf ghh  WHERE ghh.sfyx = 1 AND ghh.htsjid = htid)+bcsq) as ljbf, ahtbfb, csz, jzqr, ajlfkb, cwshz, jchdz, ywlx, sjbh, sjmj, sfyx, bz, 'xxxxlrrxxxxxxxxx', sysdate, 'xxxxxxlrbmxxxxxx', lrbmmc, gxr, gxsj, gxbm, gxbmmc, sortno from gc_zjgl_tqkbmmx WHERE NOT EXISTS(SELECT * FROM gc_zjgl_tqkmx t WHERE t.id=gc_zjgl_tqkbmmx.id) and tqkid ='xxxxxidsxxxxxx' AND bmtqkmxzt!='3' ";
    // 【累计拨付的值】是 【财务审核值（Cwshz）】+【已拨付的值（Ybf）】
    private static String SQL_INSERT_INTO = "insert into gc_zjgl_tqkmx (id, htid, tqkid, bmmxid, dwid, dwmc, xmmcnr, htbm, zxhtj, ybf, bcsq, ljbf, ahtbfb, csz, jzqr, ajlfkb, cwshz, jchdz, ywlx, sjbh, sjmj, sfyx, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sortno) SELECT id, htid, 'xxxxxxtqkidxxxxxx', id, dwid, dwmc, xmmcnr, htbm, zxhtj, (SELECT decode(SUM(ghh.zfje), NULL, 0, SUM(ghh.zfje)) FROM gc_htgl_ht_htzf ghh  WHERE ghh.sfyx = 1 AND ghh.htsjid = htid) as ybf, bcsq, ((SELECT decode(SUM(ghh.zfje), NULL, 0, SUM(ghh.zfje)) FROM gc_htgl_ht_htzf ghh  WHERE ghh.sfyx = 1 AND ghh.htsjid = htid)+CWSHZ) as ljbf, ahtbfb, csz, jzqr, ajlfkb, cwshz, jchdz, ywlx, sjbh, sjmj, sfyx, bz, 'xxxxlrrxxxxxxxxx', sysdate, 'xxxxxxlrbmxxxxxx', lrbmmc, gxr, gxsj, gxbm, gxbmmc, sortno from gc_zjgl_tqkbmmx WHERE NOT EXISTS(SELECT * FROM gc_zjgl_tqkmx t WHERE t.id=gc_zjgl_tqkbmmx.id) and tqkid ='xxxxxidsxxxxxx' AND bmtqkmxzt!='3' order by LRSJ desc";

    // 更新部门明细状态为已审批能过
    private static String SQL_UPDATE_BMMXZT = "UPDATE gc_zjgl_tqkbmmx g SET g.ybf = g.ybf + g.jchdz,g.bmtqkmxzt='xxxxxZtxxxxx' WHERE ID IN(SELECT gzt.id FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx gzt2 WHERE gzt.id=gzt2.bmmxid  and gzt2.tqkid='xxdfidxxxx')";
    
    private static String SQL_UPDATE_BMMXZT_SZSPZ = "UPDATE gc_zjgl_tqkbmmx g SET g.ybf = g.ybf + g.CWSHZ,g.bmtqkmxzt='xxxxxZtxxxxx' WHERE ID IN(SELECT gzt.id FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx gzt2 WHERE gzt.id=gzt2.bmmxid  and gzt2.tqkid='xxdfidxxxx')";
    
    private static String SQL_UPDATE_TQKBMZT = "UPDATE gc_zjgl_tqkbm tt SET tt.tqkzt='xxxxxtqkztxxxx' WHERE tt.id IN(SELECT gzt.id  FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2, gc_zjgl_tqkmx gzt3 WHERE gzt3.bmmxid=gzt2.id AND gzt2.tqkid=gzt.id AND gzt3.tqkid='xxxxxxzzzxxxxx')";
    // 合同支付
    private static String SQL_INSERT_INTO_HT_HTZF = "insert into gc_htgl_ht_htzf  (id, htsjid, zfnf, zfyf, zfrq, zfje, yfk, sfdzf, htdid, lrr, lrsj) SELECT SYS_GUID(),  gzt.htid, gzt3.qknf, gzt3.yf, gzt3.bzrq, gzt.cwshz,  gzt.bcsq, '1', gzt.id,'xxxxlrrxxx', SYSDATE    FROM gc_zjgl_tqkbmmx gzt,       gc_htgl_htsj    ghh,       gc_zjgl_tqkmx gzt2,       gc_zjgl_tqk   gzt3 WHERE gzt.id = gzt2.bmmxid   AND gzt3.id = gzt2.tqkid   and gzt.htid = ghh.id AND gzt3.id=?";
    // 合同支付表是否待支付
    private static String SQL_UPATE_HT_HTZF = "UPDATE gc_htgl_ht_htzf z SET z.zfje=(SELECT t.jchdz FROM gc_zjgl_tqkbmmx t WHERE t.id=z.htdid), z.sfdzf='0'  WHERE htdid IN (  SELECT gzt.id   FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx gzt2 WHERE gzt.id=gzt2.bmmxid AND gzt2.tqkid='xxxtqkidxxx')";
    
    // 完成投资 非施工合同全部计入完成投资
    // private static String SQL_INSERT_INTO_HT_WCTZ =
    // "insert into gc_htgl_ht_wctz  (id, htsjid, nf, yf, wctzje, lrr, lrsj )SELECT SYS_GUID(), gzt.htid, gzt3.qknf, gzt3.yf, gzt.jchdz, 'xxxxlrrxxxx', SYSDATE   FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx gzt2, gc_zjgl_tqk gzt3, gc_htgl_htsj ghh, gc_htgl_ht ghh2 WHERE gzt.id=gzt2.bmmxid AND gzt2.tqkid=gzt3.id AND gzt.htid=ghh.id AND ghh.htid=ghh2.id AND ghh2.htlx!='SG' AND gzt3.id=?";
    private static String SQL_INSERT_INTO_HT_WCTZ = "insert into gc_htgl_ht_wctz (id, htsjid, nf, yf, wctzje, lrr, lrsj, lrbm, htzfid) SELECT SYS_GUID(), z.htsjid, z.zfnf, z.zfyf, gzt.jchdz, 'userid', SYSDATE,'departid', z.id  FROM  gc_htgl_ht_htzf z, gc_htgl_ht ghh, gc_htgl_htsj ghh2, gc_zjgl_tqkbmmx gzt WHERE z.htsjid=ghh2.id AND ghh2.htid=ghh.id AND z.htdid = gzt.id AND (ghh.sfzfjtz='1' OR  ghh.sfzfjtz is null )  and   z.htdid in (  SELECT gzt.id   FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx gzt2 WHERE gzt.id=gzt2.bmmxid AND gzt2.tqkid=?)";

    // 更新合同数据合同支付及完成投资金额
    private static String SQL_UPDATE_HTSJ = "UPDATE gc_htgl_htsj j SET htzf=(SELECT sum(ghhh.zfje) FROM gc_htgl_ht_htzf ghhh WHERE ghhh.htsjid=j.id AND ghhh.sfdzf='0'), wczf=(SELECT SUM(ghhw.wctzje) FROM gc_htgl_ht_wctz ghhw WHERE ghhw.htsjid=j.id ) WHERE j.id IN (SELECT  gzt2.htid FROM gc_zjgl_tqkmx gzt, gc_zjgl_tqkbmmx gzt2 WHERE gzt.bmmxid=gzt2.id AND gzt.tqkid=?)";
    // 更新合同信息
    private static String SQL_UPDATE_HT = "UPDATE gc_htgl_ht t SET  zhtzf =(SELECT SUM(j.htzf) FROM gc_htgl_htsj j WHERE t.id=j.htid),  zwczf = (SELECT SUM(j.wczf) FROM gc_htgl_htsj j WHERE t.id=j.htid) WHERE t.id IN(SELECT DISTINCT ghh.htid FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx gzt2, gc_htgl_htsj ghh WHERE gzt.id=gzt2.bmmxid AND gzt.htid=ghh.id AND gzt2.tqkid=?)";

    // 查询财务部提请款明细
    private static String SQL_QUERY_MXZK = "SELECT (SELECT COUNT(*) FROM gc_zjgl_tqkmx gzt WHERE t.id = gzt.tqkid) AS cwjls,       (SELECT SUM(gzt2.cwshz)          FROM gc_zjgl_tqkmx gzt, gc_zjgl_tqkbmmx gzt2         WHERE gzt2.id = gzt.bmmxid           and t.id = gzt.tqkid) AS cwze,       (SELECT COUNT(*) FROM gc_zjgl_tqkmx gzt, gc_zjgl_tqkbmmx gzt2 WHERE t.id = gzt.tqkid AND gzt.bmmxid=gzt2.id AND gzt2.bmtqkmxzt='7') AS jcjls,        (SELECT SUM(gzt2.jchdz)          FROM gc_zjgl_tqkmx gzt, gc_zjgl_tqkbmmx gzt2         WHERE gzt2.id = gzt.bmmxid          and t.id = gzt.tqkid AND gzt2.bmtqkmxzt='7') AS jcze,     t.*,	fcd.dict_name  FROM GC_ZJGL_TQK t  left join fs_common_dict fcd on fcd.dict_id = t.rzzt";

    @Override
    public String queryCondition(String json, User user) throws Exception {

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
            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_MXZK, page);

            // 设置字典
            bs.setFieldDic("QKLX", "QKLX");// 请款类型
            bs.setFieldDic("QKNF", "XMNF");// 请款年份-->项目年份 XMNF
            // bs.setFieldDic("GCPC", "PCH");// 工程批次-->批次号 PCH
            bs.setFieldDic("TQKZT", "TQKZT");// -->审批结果 SPJG

            // 设置查询条件
            bs.setFieldThousand("ZZXHTJ");// 总最新合同价
            bs.setFieldThousand("ZYBF");// 总已拔付
            bs.setFieldThousand("ZBCSQ");// 总本次申请
            bs.setFieldThousand("ZLJBF");// 总累计拔付
            bs.setFieldThousand("ZCSZ");// 总财审值
            bs.setFieldThousand("ZJLQR");// 总监理确认款

            bs.setFieldThousand("CWZE");// 总监理确认款
            bs.setFieldThousand("JCZE");// 总监理确认款

            bs.setFieldOrgDept("SQDW");
            bs.setFieldSjbh("SJBH");

            bs.setFieldUserID("CWBLRID");
            bs.setFieldUserID("JCBLRID");
            bs.setFieldUserID("CWSHRID");

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
    public String insert(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglTqkVO vo = new GcZjglTqkVO();

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
            resultVO = vo.getRowJson();

            // 新增审批流程相关信息.
            String condition = map.get("condition") == null ? "" : (String) map.get("condition");
            Pub.getFlowinf(conn, ywlx, vo.getSjbh(), user, condition);

            conn.commit();

            String tqkid = map.get("tqkid") == null ? "" : (String) map.get("tqkid");
            if (StringUtils.isNotBlank(tqkid)) {
                // 将部门提请款的所有明细，插入财务提醒
                String insql = SQL_INSERT_INTO.replaceAll("xxxxxidsxxxxxx", tqkid);
                insql = insql.replaceAll("xxxxxxtqkidxxxxxx", vo.getId());
                insql = insql.replaceAll("xxxxlrrxxxxxxxxx", user.getAccount());
                insql = insql.replaceAll("xxxxxxlrbmxxxxxx", user.getDepartment());

                DBUtil.execUpdateSql(conn, insql);
                conn.commit();
            }

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款新增成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + vo.getId()
                    + "\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款新增失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglTqkVO vo = new GcZjglTqkVO();

        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            vo.setValueFromJson((JSONObject) list.get(0));

            BusinessUtil.setUpdateCommonFields(vo, user);

            String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
            if (opttype.equals("spz")) {
                // 设置为审批状态
                // 更新部门明细 为审批中状态
                // SQL_UPDATE_BMMXZT
                String updatebmmx = SQL_UPDATE_BMMXZT_SZSPZ.replaceAll("xxxxxZtxxxxx", vo.getTqkzt());
                updatebmmx = updatebmmx.replaceAll("xxdfidxxxx", vo.getId());
                DBUtil.execUpdateSql(conn, updatebmmx);
                // 更新部门提请款状态为 审批中 所有明细相关联的部门提请款
                String updatebmzt = SQL_UPDATE_TQKBMZT.replaceAll("xxxxxtqkztxxxx", vo.getTqkzt());
                updatebmzt = updatebmzt.replaceAll("xxxxxxzzzxxxxx", vo.getId());
                DBUtil.execUpdateSql(conn, updatebmzt);
            } else if (opttype.equals("sptg")) {
                // 更新部门明细 为审批通过状态
                // SQL_UPDATE_BMMXZT
                String updatebmmx = SQL_UPDATE_BMMXZT_SZSPZ.replaceAll("xxxxxZtxxxxx", vo.getTqkzt());
                updatebmmx = updatebmmx.replaceAll("xxdfidxxxx", vo.getId());
                DBUtil.execUpdateSql(conn, updatebmmx);
                // 更新部门提请款状态为 已审批 所有明细相关联的部门提请款
                String updatebmzt = SQL_UPDATE_TQKBMZT.replaceAll("xxxxxtqkztxxxx", vo.getTqkzt());
                updatebmzt = updatebmzt.replaceAll("xxxxxxzzzxxxxx", vo.getId());
                DBUtil.execUpdateSql(conn, updatebmzt);

                // 测试写入 合同支付表
                String iinto = SQL_INSERT_INTO_HT_HTZF.replaceAll("xxxxlrrxxx", user.getAccount());
                DBUtil.executeUpdate(iinto, new Object[] { vo.getId() });
            } else if (opttype.equals("bf")) {
                // 拔付 7 已拔付
                // 更新合同支付表 更新部门提请款表 更新提请款表

                // 更新部门明细 为审批通过状态
                // SQL_UPDATE_BMMXZT
                String updatebmmx = SQL_UPDATE_BMMXZT.replaceAll("xxxxxZtxxxxx", vo.getTqkzt());
                updatebmmx = updatebmmx.replaceAll("xxdfidxxxx", vo.getId());
                DBUtil.execUpdateSql(conn, updatebmmx);
                // 更新部门提请款状态为 已审批 所有明细相关联的部门提请款
                String updatebmzt = SQL_UPDATE_TQKBMZT.replaceAll("xxxxxtqkztxxxx", vo.getTqkzt());
                updatebmzt = updatebmzt.replaceAll("xxxxxxzzzxxxxx", vo.getId());
                DBUtil.execUpdateSql(conn, updatebmzt);

                // 更新合同支付
                String updatehtzf = SQL_UPATE_HT_HTZF.replaceAll("xxxtqkidxxx", vo.getId());
                DBUtil.execUpdateSql(conn, updatehtzf);

                // 写入完成投资 除施工合同 FROM gc_htgl_ht_htzf z, gc_htgl_ht ghh,
                // gc_htgl_htsj ghh2 WHERE z.htsjid=ghh2.id AND ghh2.htid=ghh.id
                // AND (ghh.sfzfjtz='1' OR ghh.sfzfjtz is null ) and z.htdid in
                String iinto = SQL_INSERT_INTO_HT_WCTZ.replaceAll("userid", user.getAccount());
                iinto = iinto.replaceAll("departid", user.getDepartment());
                DBUtil.executeUpdate(iinto, new Object[] { vo.getId() });
                conn.commit();
                // 更新合同数据
                DBUtil.executeUpdate(SQL_UPDATE_HTSJ, new Object[] { vo.getId() });
                conn.commit();
                // 更新合同信息
                DBUtil.executeUpdate(SQL_UPDATE_HT, new Object[] { vo.getId() });
            }

            // 更新
            BaseDAO.update(conn, vo);
            resultVO = vo.getRowJson();

            conn.commit();
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款修改成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcZjglTqkVO vo = new GcZjglTqkVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            BaseDAO.delete(conn, vo);

            resultVo = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }

    @Override
    public String synaData(String json, String tqkid, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        try {

            // logger.info("同步当前拔付数据到明细数据上{}", user.getAccount());
            conn.setAutoCommit(false);
            String upsql = "UPDATE gc_zjgl_tqkbmmx t SET t.ybf=(SELECT ybf FROM VIEW_TQKBMXX_SYNCDATA WHERE ID=t.id), t.ljbf=(SELECT ljbf FROM VIEW_TQKBMXX_SYNCDATA WHERE ID=t.id), t.ahtbfb=(SELECT ahtbfb FROM VIEW_TQKBMXX_SYNCDATA WHERE ID=t.id) WHERE t.id IN(SELECT ID FROM VIEW_TQKBMXX_SYNCDATA t WHERE t.tqkid =?)";
            DBUtil.executeUpdate(conn, upsql, new Object[] { tqkid });

            conn.commit();

            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款同步数据成功", user, "", "");
            
            return "true";
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款同步数据失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款同步数据失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }

        return "false";
    }

}
