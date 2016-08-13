/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtHtzfService.java
 * 创建日期： 2013-09-03 上午 09:38:07
 * 功能：    接口：合同数据
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-03 上午 09:38:07  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl.service.impl;

import java.sql.Connection;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.ccthanking.business.htgl.vo.GcHtglHtHtzfVO;
import com.ccthanking.business.htgl.service.GcHtglHtHtzfService;
import com.ccthanking.business.htgl.service.GcHtglHtsjService;
import com.ccthanking.framework.service.impl.Base1ServiceImpl;

/**
 * <p>
 * GcHtglHtHtzfService.java
 * </p>
 * <p>
 * 功能：合同数据
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtHtzfService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-03
 * 
 */

@Service
public class GcHtglHtHtzfServiceImpl extends Base1ServiceImpl<GcHtglHtHtzfVO, String> implements GcHtglHtHtzfService {

    private static Logger logger = LoggerFactory.getLogger(GcHtglHtHtzfServiceImpl.class);

    @Autowired
    private GcHtglHtsjService gcHtglHtsjService;

    // 分组合同支付信息
    private static String SQL_QUERY_ONE = "SELECT  ghhh.zfnf, ghhh.zfyf, ghh.htid, ghhh.zfnf||'-'||ghhh.zfyf AS ny,COUNT(*) AS countYF, decode(SUM(ghhh.zfje), NULL, 0 , SUM(ghhh.zfje)) as sumzfje, decode(SUM(ghhh.yfk), NULL, 0 , SUM(ghhh.yfk)) as sumyfk, decode(SUM(ghhh.kkzh), NULL, 0 , SUM(ghhh.kkzh)) as sumkkzh,(SELECT SUM(zf.zfje) FROM gc_htgl_ht_htzf zf, gc_htgl_htsj htsj  WHERE zf.htsjid=htsj.id AND  htsj.htid=ghh.htid AND TO_DATE(zf.zfnf||zf.zfyf, 'yyyyMm')<=TO_DATE(ghhh.zfnf||ghhh.zfyf, 'yyyyMm')  ) AS ljddyzf  FROM gc_htgl_ht_htzf ghhh, gc_htgl_htsj ghh ";

    // 更新 总合同支付价
    private static String SQL_UPDATE_ZHTZF = "update gc_htgl_ht ghh set ghh.zhtzf=(select sum(zfje) from gc_htgl_ht_htzf ghhh, gc_htgl_htsj ghh2  where ghh2.id= ghhh.htsjid and ghh2.htid=ghh.id )  where ghh.id in (SELECT distinct x.htid  FROM gc_htgl_htsj x, gc_htgl_ht_htzf y WHERE x.id=y.htsjid AND y.id=? )";

    // 更新 合同数据支付
    // 更新 总合同支付价
    private static String SQL_UPDATE_HTSJZF = "update gc_htgl_htsj ghh set ghh.htzf=(select sum(zfje) from gc_htgl_ht_htzf ghhh where ghhh.htsjid=ghh.id )  where ghh.id = ?";

    // 新增合同完成投资记录
    private static String SQL_INSERT_INTO_HT_WCTZ = "insert into gc_htgl_ht_wctz  (id, htsjid, nf, yf, wctzje, wctzlx, ywlx, sfyx, lrr, lrsj, lrbm, lrbmmc, htzfid) SELECT SYS_GUID(), htsjid, zfnf, zfyf, zfje, wctzlx, ywlx, sfyx, lrr, lrsj, lrbm, lrbmmc, id  FROM VIEW_GC_HTGL_HT_HTZF_FORWCTZ WHERE ID=?";
    private static String SQL_UPDATE_HT_WCTZ_BYZF = "update gc_htgl_ht_wctz w   SET        htsjid = (SELECT htsjid FROM VIEW_GC_HTGL_HT_HTZF_FORWCTZ  WHERE ID=w.htzfid), nf = (SELECT nf FROM VIEW_GC_HTGL_HT_HTZF_FORWCTZ  WHERE ID=w.htzfid),       yf = (SELECT yf FROM VIEW_GC_HTGL_HT_HTZF_FORWCTZ  WHERE ID=w.htzfid),       wctzje = (SELECT wctzje FROM VIEW_GC_HTGL_HT_HTZF_FORWCTZ  WHERE ID=w.htzfid),       wctzlx = (SELECT wctzlx FROM VIEW_GC_HTGL_HT_HTZF_FORWCTZ  WHERE ID=w.htzfid),       gxr = (SELECT gxr FROM VIEW_GC_HTGL_HT_HTZF_FORWCTZ  WHERE ID=w.htzfid),       gxsj = (SELECT gxsj FROM VIEW_GC_HTGL_HT_HTZF_FORWCTZ  WHERE ID=w.htzfid),       gxbm = (SELECT gxbm FROM VIEW_GC_HTGL_HT_HTZF_FORWCTZ  WHERE ID=w.htzfid),       gxbmmc = (SELECT gxbmmc FROM VIEW_GC_HTGL_HT_HTZF_FORWCTZ  WHERE ID=w.htzfid)  where id =?";
    private static String SQL_DELETE_HT_WCTZ_BYZF = "DELETE FROM gc_htgl_ht_wctz WHERE htzfid=?";

    // 业务类型
    private String ywlx = YwlxManager.GC_HTGL_HT_HTZF;

    @Override
    public String queryOne(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            QueryConditionList list = RequestUtil.getConditionList(json);

            String condition = " ghhh.htsjid=ghh.ID ";

            // ghh.htid需要合同号
            condition += list == null ? "" : " and " + list.getConditionWhere();

            // 组织查询条件
            String orderFilter = RequestUtil.getOrderFilter(json);

            condition += BusinessUtil.getSJYXCondition("ghhh");// 是否有效
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += " GROUP BY ghhh.zfnf, ghhh.zfyf, ghh.htid ";// 排序
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_ONE, page);
            bs.setFieldThousand("SUMZFJE");
            bs.setFieldThousand("SUMYFK");
            bs.setFieldThousand("SUMKKZH");
            bs.setFieldThousand("LJDDYZF");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同数据支付>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同数据支付查询失败!");
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
            condition += BusinessUtil.getSJYXCondition("T");
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            String sql = "SELECT ghh.htid,ghh.jhsjid,  ghh2.htmc, ghh2.htbm, gjs.xmmc, gjs.bdmc, ghh.ID AS htsjid, "
                    + "t.id,  t.zjzh, t.zfnf, t.zfyf, t.zfrq, t.zfje, t.yfk, t.bcwcgzl, t.blyfk, t.gcyfk, t.fqtk, "
                    + "t.fhbxj, t.kkzh, t.kblk, t.kgzyfk, t.kqtk, t.kbxj, t.sfdzf, t.zfyt, t.dzfrq, t.htdid, t.bz ,ghh.wczf,       ghh.htzf,      " 
                    + " ghh.htqdj,       ghh2.zhtqdj,       decode(ghh.htqdj,0,0,NVL(ROUND(ghh.htzf / ghh.htqdj * 100, 2), 0)) AS ljyzfb,      " 
                    + " decode(ghh.htqdj,0,0,NVL(ROUND(ghh.wczf / ghh.htqdj * 100, 2), 0)) AS ljwczfb, "
                    + " (SELECT SUM(GCYFK)-SUM(KGZYFK) FROM gc_htgl_ht_htzf z WHERE z.htsjid=ghh.id AND z.sfyx=1) AS WKYFK "
                    + " FROM GC_HTGL_HT_HTZF t LEFT JOIN gc_htgl_htsj ghh ON t.htsjid=ghh.ID "
                    + "LEFT JOIN gc_htgl_ht ghh2 ON ghh.htid=ghh2.ID LEFT JOIN gc_jh_sj gjs ON ghh.jhsjid=gjs.gc_jh_sj_id ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            // 合同表
            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");
            // 项目下达库
            // bs.setFieldTranslater("XDKID", "GC_TCJH_XMXDK", "ID", "XMMC");
            // 标段表
            // bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");

            // 设置字典
            // bs.setFieldDic("HTLX", "HTLX");
            // bs.setFieldDic("HTZT", "HTQDZT");// 合同签订状态
            // bs.setFieldDic("FBFS", "FBFS");// 发包方式
            // bs.setFieldDic("BXQDWLX", "BXQDWLX");// 保修期单位：年、季、月、日
            bs.setFieldDic("ZFNF", "XMNF");// 项目年份
            bs.setFieldDic("ZFYF", "YF");// 项目年份

            bs.setFieldDic("ZFYT", "ZFYT");// 项目年份

            // 设置查询条件
            bs.setFieldDateFormat("ZFRQ", "yyyy-MM-dd");// 计量月份
            bs.setFieldThousand("ZFJE");
            bs.setFieldThousand("YFK");
            bs.setFieldThousand("KKZH");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同数据>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同数据查询失败!");
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
        GcHtglHtHtzfVO xmvo = new GcHtglHtHtzfVO();

        try {
            conn.setAutoCommit(false);
            JSONArray list = xmvo.doInitJson(json);
            xmvo.setValueFromJson((JSONObject) list.get(0));
            // 设置主键
            String guid = new RandomGUID().toString();
            xmvo.setId(guid); // 主键
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

            // SQL_INSERT_INTO_HT_WCTZ
            DBUtil.executeUpdate(conn, SQL_INSERT_INTO_HT_WCTZ, new Object[] { guid });
            conn.commit();

            // DBUtil.executeUpdate(SQL_UPDATE_ZHTZF, new Object[] {
            // xmvo.getId() });
            // DBUtil.executeUpdate(SQL_UPDATE_HTSJZF, new Object[] {
            // xmvo.getHtsjid() });
            // 这好像没有任何作用！ 应该是根据类型判断是否写 合同完成投资表 这里的逻辑全是
            // 如果合同属于支付转投资的类型,则同时更新合同投资
            // String sfzfjtz =
            // "SELECT z.id,z.SFZFJTZ  FROM gc_htgl_ht_htzf y left join gc_htgl_htsj x on x.id=y.htsjid left join gc_htgl_ht z on z.id = x.htid where y.id='"
            // + xmvo.getId() + "' ";
            // String[][] results = DBUtil.query(sfzfjtz);
            // if (results.length > 0 && "1".equals(results[0][1])) {
            // String sql =
            // "update gc_htgl_ht ghh set ghh.ZWCZF= (select sum(a.zfje) from gc_htgl_ht_htzf a where a.htsjid='"
            // + xmvo.getHtsjid() + "') where ghh.id = ?";
            // DBUtil.executeUpdate(sql, new Object[] { results[0][0] });
            // sql =
            // "update gc_htgl_htsj ghh set ghh.WCZF= (select sum(a.zfje) from gc_htgl_ht_htzf a where a.htsjid='"
            // + xmvo.getHtsjid() + "') where ghh.id = ?";
            // DBUtil.executeUpdate(sql, new Object[] { xmvo.getHtsjid() });
            // }

            HashMap map = new HashMap();
            map.put("htsjId", xmvo.getHtsjid());
            gcHtglHtsjService.updateHtsj(json, user, map);

            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同数据  合同支付新增成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同数据新增失败!");
            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同数据 合同支付新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtHtzfVO vo = new GcHtglHtHtzfVO();

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

            DBUtil.executeUpdate(conn, SQL_UPDATE_HT_WCTZ_BYZF, new Object[] { vo.getId() });
            conn.commit();

            // DBUtil.executeUpdate(SQL_UPDATE_ZHTZF, new Object[] { vo.getId()
            // });
            // DBUtil.executeUpdate(SQL_UPDATE_HTSJZF, new Object[] {
            // vo.getHtsjid() });
            //
            // // 如果合同属于支付转投资的类型,则同时更新合同投资
            // String sfzfjtz =
            // "SELECT z.id,z.SFZFJTZ  FROM gc_htgl_ht_htzf y left join gc_htgl_htsj x on x.id=y.htsjid left join gc_htgl_ht z on z.id = x.htid where y.id='"
            // + vo.getId() + "' ";
            //
            // String[][] results = DBUtil.query(sfzfjtz);
            //
            // if (results.length > 0 && "1".equals(results[0][1])) {
            // String sql =
            // "update gc_htgl_ht ghh set ghh.ZWCZF= (select sum(a.zfje) from gc_htgl_ht_htzf a where a.htsjid='"
            // + vo.getHtsjid() + "') where ghh.id = ?";
            // DBUtil.executeUpdate(sql, new Object[] { results[0][0] });
            // sql =
            // "update gc_htgl_htsj ghh set ghh.WCZF= (select sum(a.zfje) from gc_htgl_ht_htzf a where a.htsjid='"
            // + vo.getHtsjid()
            // + "') where ghh.id = ?";
            // DBUtil.executeUpdate(sql, new Object[] { vo.getHtsjid() });
            // }
            //
            // conn.commit();

            HashMap map = new HashMap();
            map.put("htsjId", vo.getHtsjid());
            gcHtglHtsjService.updateHtsj(json, user, map);

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同数据修改成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同数据修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同数据修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcHtglHtHtzfVO vo = new GcHtglHtHtzfVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            BaseDAO.delete(conn, vo);

            resultVo = vo.getRowJson();
            conn.commit();

            // 删除完成投资 执行汇总和计算
            DBUtil.executeUpdate(conn, SQL_DELETE_HT_WCTZ_BYZF, new Object[] { vo.getId() });
            conn.commit();

            HashMap map = new HashMap();
            map.put("htsjId", vo.getHtsjid());
            gcHtglHtsjService.updateHtsj(json, user, map);

            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同数据删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同数据删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同数据删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }

}
