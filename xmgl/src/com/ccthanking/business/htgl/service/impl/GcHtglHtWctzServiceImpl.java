/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtWctzService.java
 * 创建日期： 2013-09-05 下午 06:12:53
 * 功能：    接口：合同完成投资
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-05 下午 06:12:53  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl.service.impl;

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
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

import com.ccthanking.business.htgl.vo.GcHtglHtWctzVO;
import com.ccthanking.business.htgl.service.GcHtglHtWctzService;
import com.ccthanking.framework.service.impl.Base1ServiceImpl;

/**
 * <p>
 * GcHtglHtWctzService.java
 * </p>
 * <p>
 * 功能：合同完成投资
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtWctzService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-05
 * 
 */

@Service
public class GcHtglHtWctzServiceImpl extends Base1ServiceImpl<GcHtglHtWctzVO, String> implements GcHtglHtWctzService {

    private static Logger logger = LoggerFactory.getLogger(GcHtglHtWctzServiceImpl.class);

    // 并合同信息查询.
    private static String SQL_QUERY_LIST = "SELECT ghh.htbm, ghh.htmc, ghh.htlx, ghh.fbfs,ghh.htzt, ghh.zhtqdj, t.*"
            + "  FROM gc_htgl_ht_wctz t LEFT JOIN gc_htgl_ht ghh ON t.htsjid=ghh.ID";

    // 查询单合同分组完成投资情况
    private static String SQL_QUERY_ONE_YEARMONTH = "SELECT ghhw.nf, ghhw.yf, ghhw.nf||'-'||ghhw.yf AS NY, sum(ghhw.wctzje) AS sumwczje FROM gc_htgl_ht_wctz ghhw, gc_htgl_htsj ghh ";

    private static String SQL_QUERY_ON_HTDETAIL = "SELECT ghh.htlx, ghh.fbfs,ghh.htzt, ghh.zhtqdj, t.id, t.nf, t.yf, "
            + "t.wctzje, t.wctzlx, t.bz, gjs.xmmc, gjs.bdmc, ghh.htmc,ghh.htbm, ghh2.htid, t.htsjid, ghh2.jhsjid "
            + "FROM gc_htgl_ht_wctz t LEFT JOIN gc_htgl_htsj ghh2 ON t.htsjid=ghh2.ID "
            + "LEFT join gc_htgl_ht ghh ON ghh.ID=ghh2.htid LEFT JOIN  gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid";

    // 更新 总合同完成投资
    private static String SQL_UPDATE_ZWCZF = "update gc_htgl_ht ghh set ghh.zwczf=(SELECT sum(ghhw.wctzje)  FROM gc_htgl_ht_wctz ghhw, gc_htgl_htsj ghh2 WHERE ghhw.htsjid=ghh2.id AND ghh2.htid=ghh.id) where ghh.id in (SELECT distinct y.htid FROM gc_htgl_ht_wctz x, gc_htgl_htsj y WHERE x.htsjid=y.id AND x.id=? )";
    
    //更新 合同数据完成支付
    private static String SQL_UPDATE_HTSJ_WCZF = "update gc_htgl_htsj ght set ght.wczf =(select decode(sum(gh.wctzje),null,0,sum(gh.wctzje)) from gc_htgl_ht_wctz gh where gh.sfyx=1 and gh.htsjid = ght.id) where ght.id= ?";

    // 业务类型
    private String ywlx = YwlxManager.GC_HTGL_HT_WCTZ;

    @Override
    public String queryOne(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            QueryConditionList list = RequestUtil.getConditionList(json);

            String condition = " ghhw.htsjid=ghh.ID ";

            // ghh.htid需要合同号
            condition += list == null ? "" : " and " + list.getConditionWhere();

            // 组织查询条件
            String orderFilter = RequestUtil.getOrderFilter(json);

            condition += BusinessUtil.getSJYXCondition("ghhw");// 是否有效
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += " GROUP BY ghhw.nf, ghhw.yf ";// 排序
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_ONE_YEARMONTH, page);

            // 设置字典
            bs.setFieldDic("BGLX", "BGLX");
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式

            bs.setFieldDic("WCTZLX", "ZFYT");// 完成投资类型==支付用途

            bs.setFieldThousand("SUMWCZJE");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同完成投资>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同完成投资查询失败!");
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
            condition += BusinessUtil.getSJYXCondition("t");
            condition += BusinessUtil.getCommonCondition(user, "t");
            condition += orderFilter;

            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_ON_HTDETAIL, page);

            // 设置字典
            bs.setFieldDic("BGLX", "BGLX");
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式

            bs.setFieldDic("WCTZLX", "ZFYT");// 完成投资类型==支付用途

            bs.setFieldThousand("WCTZJE");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同完成投资>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同完成投资查询失败!");
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
        GcHtglHtWctzVO xmvo = new GcHtglHtWctzVO();

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

            DBUtil.executeUpdate(SQL_UPDATE_ZWCZF, new Object[] { xmvo.getId() });
            conn.commit();
            
            DBUtil.executeUpdate(SQL_UPDATE_HTSJ_WCZF, new Object[] { xmvo.getHtsjid()});
            conn.commit();
            
            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同完成投资新增成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同完成投资新增失败!");
            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同完成投资新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }
    
    @Override
    public String insertFromJL(User user, String jhsjid,String opttype ,HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtWctzVO xmvo = new GcHtglHtWctzVO();

        try {
            conn.setAutoCommit(false);
            
            // 设置主键
            xmvo.setId(new RandomGUID().toString()); // 主键
            //根据JHSJID查询对应的施工合同的htsjid
            String sql = "select distinct(ghh.id) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id  where gh.htlx='SG' and ghh.jhsjid = '"+jhsjid+"'";
            String[][] results = DBUtil.query(sql);
            if(results!=null&&results[0]!=null&&results[0][0]!=null){
            	xmvo.setHtsjid(results[0][0]);
            }
            if(!"d".equals(opttype)){
	            if(map.get("NF")!=null){
	            	xmvo.setNf((String)map.get("NF"));		//支付年份
	            }
	            if(map.get("YF")!=null){
	            	xmvo.setYf((String)map.get("YF"));		//支付月份
	            }
	            if(map.get("JE")!=null){
	            	xmvo.setWctzje((String)map.get("JE"));	//支付金额
	            }
	            xmvo.setWctzlx("3");		//完成支付类型： 其他
	            //
            
            	//如果是删除不需要执行这些操作
            	xmvo.setLrr(user.getAccount()); // 更新人
                xmvo.setLrsj(Pub.getCurrentDate()); // 更新时间
                xmvo.setLrbm(user.getDepartment());// 录入人单位
                xmvo.setLrbmmc(user.getOrgDept().getDeptName());// 录入人单位名称
                xmvo.setYwlx(ywlx);
            }
            
            if("c".equals(opttype)){
            	//新增
                EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);// 生成事件
                xmvo.setSjbh(eventVO.getSjbh());
                // 插入
                BaseDAO.insert(conn, xmvo);
                conn.commit();
            }else{
            	//修改（根据年份、月份、合同数据ID）
            	sql = " select w.id from gc_htgl_ht_wctz w where w.htsjid='"+xmvo.getHtsjid()+"' ";
            	
                if(map.get("NF")!=null){
                	sql += " and w.NF = '"+(String)map.get("NF")+"' ";
                }
                if(map.get("YF")!=null){
                	sql += " and w.YF = '"+(String)map.get("YF")+"'";
                }
                results = DBUtil.query(sql);	//查询对应的完成投资主键ID
            	for (String[] wctzRow : results) {
            		xmvo.setId(wctzRow[0]);
            		if("u".equals(opttype)){	//更新
            			BaseDAO.update(conn, xmvo);
            		}else if("d".equals(opttype)){	//删除
            			BaseDAO.delete(conn, xmvo);
            		}
                	conn.commit();
				}
            	
            }

            
            resultVO = xmvo.getRowJson();

            DBUtil.executeUpdate(SQL_UPDATE_ZWCZF, new Object[] { xmvo.getId() });
            conn.commit();
            
            DBUtil.executeUpdate(SQL_UPDATE_HTSJ_WCZF, new Object[] { xmvo.getHtsjid()});
            conn.commit();
            
            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同完成投资(通过计量)新增成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同完成投资(通过计量)新增失败!");
            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同完成投资(通过计量)新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtWctzVO vo = new GcHtglHtWctzVO();

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

            DBUtil.executeUpdate(SQL_UPDATE_ZWCZF, new Object[] { vo.getId() });
            conn.commit();
            
            DBUtil.executeUpdate(SQL_UPDATE_HTSJ_WCZF, new Object[] { vo.getHtsjid()});
            conn.commit();
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同完成投资修改成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同完成投资修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同完成投资修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcHtglHtWctzVO vo = new GcHtglHtWctzVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            BaseDAO.delete(conn, vo);

            resultVo = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同完成投资删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同完成投资删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同完成投资删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }

}
