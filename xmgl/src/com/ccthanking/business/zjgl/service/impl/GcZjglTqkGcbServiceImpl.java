/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglTqkbmService.java
 * 创建日期： 2013-09-29 上午 07:31:33
 * 功能：    接口：提请款部门
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-29 上午 07:31:33  蒋根亮   创建文件，实现基本功能
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

import com.ccthanking.business.zjb.jsgl.GcZjbJsbVO;
import com.ccthanking.business.zjgl.service.GcZjglTqkGcbService;
import com.ccthanking.business.zjgl.service.GcZjglTqkbmService;
import com.ccthanking.business.zjgl.vo.GcZjglTqkVO;
import com.ccthanking.business.zjgl.vo.GcZjglTqkbmVO;
import com.ccthanking.business.zjgl.vo.GcZjglTqkgcbVO;
import com.ccthanking.business.zjgl.vo.GcZjglTqkgcbmxVO;
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
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.service.impl.Base1ServiceImpl;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * <p>
 * GcZjglTqkbmService.java
 * </p>
 * <p>
 * 功能：提请款部门
 * </p>
 * 
 * <p>
 * <a href="GcZjglTqkbmService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-29
 * 
 */

@Service
public class GcZjglTqkGcbServiceImpl extends Base1ServiceImpl<GcZjglTqkgcbVO, String> implements GcZjglTqkGcbService {

    private static Logger logger = LoggerFactory.getLogger(GcZjglTqkGcbServiceImpl.class);

    // 业务类型
    private String ywlx = YwlxManager.GC_ZJGL_TQKGCB;

    private static String SQL_UPDATE_TQKGCBZT = "update gc_zjgl_tqkgcb t set t.tqkztgcb = xxxxmxztxxxx where t.id in (select m.gcbtqkid from gc_zjgl_tqkgcbmx m where m.id in (xxxxgcbmxidxxxx)) "
    	+" and (select count(0) from gc_zjgl_tqkgcbmx n where n.gcbtqkid = t.id ) = (select count(0) from gc_zjgl_tqkgcbmx n where n.gcbtqkid = t.id and n.gcbshmxzt = 1)";
    
    // 查询部门提请款及明细情况
    private static String SQL_QUERY_TQKGCBMX = " SELECT (SELECT COUNT(*) FROM gc_zjgl_tqkbmmx gzt WHERE t.id = gzt.tqkid) AS bmjls,       (SELECT sum(gzt.bcsq) FROM gc_zjgl_tqkbmmx gzt WHERE t.id = gzt.tqkid) AS bmze,       (SELECT COUNT(*)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid           AND t.id = gzt.tqkid AND (gzt.bmtqkmxzt='6' or gzt.bmtqkmxzt='7')) AS cwjls,       (SELECT sum(gzt.cwshz)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid           AND t.id = gzt.tqkid AND (gzt.bmtqkmxzt='6' or gzt.bmtqkmxzt='7')) AS cwze,       (SELECT COUNT(*)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid           AND t.id = gzt.tqkid AND gzt.bmtqkmxzt='7') AS jcjls,       (SELECT sum(gzt.jchdz)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid AND t.id = gzt.tqkid  AND gzt.bmtqkmxzt='7') AS jcze, (select n.TQKZTGCB from gc_zjgl_tqkgcb n where n.bmtqkid=t.id) gcbzt, (select n1.id from gc_zjgl_tqkgcb n1 where n1.bmtqkid=t.id) gcbtqkid,     t.*  FROM gc_zjgl_tqkbm t  ";

    private static String SQL_QUERY_GCBBMMX = " select * from VIEW_ZJGL_TQK_GCBBMMX t ";
    
    private static String SQL_UPDATE_TQKGCBMX = "update GC_ZJGL_TQKGCBMX t  set t.GCBSHMXZT=xxxxmxztxxxx where t.id in (xxxxgcbmxidxxxx)";
  
    @Override
    public String queryCondition(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
        	PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            condition += " and t.id in (select g.bmtqkid from gc_zjgl_tqkgcb g)";
            String orderFilter = RequestUtil.getOrderFilter(json);
            // condition += BusinessUtil.getSJYXCondition(null);
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            String sql = SQL_QUERY_TQKGCBMX;
            
            BaseResultSet bs = DBUtil.query(conn, sql, page);

            // 合同表
            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");
            // 项目下达库
            // bs.setFieldTranslater("XDKID", "GC_TCJH_XMXDK", "ID", "XMMC");
            // 标段表
            // bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");

            // 设置字典
            // bs.setFieldDic("SQDW", "SQDW");//申请单位
            // bs.setFieldDic("SJBH", "SJBH");//事件编号
            // bs.setFieldDic("SJMJ", "SJMJ");//数据密级

            // 设置字典
            bs.setFieldDic("GCBZT" , "GCBSHMXZT");// 工程部提请款状态
            bs.setFieldDic("QKLX", "QKLX");
            bs.setFieldDic("TQKZT", "TQKZT");
            //金额
            bs.setFieldThousand("BMZE");
            bs.setFieldThousand("CWZE");
            bs.setFieldThousand("JCZE");
            //bs.setFieldThousand("YBF");
            //bs.setFieldThousand("BCSQ");
            
            
            bs.setFieldSjbh("SJBH");
            // 设置查询条件
            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份
            // bs.setFieldThousand("DYJLSDZ");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<工程部提请款审批>", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("提请款部门查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
    
    @Override
    public String queryGcbTqkMx(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
        	PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            String orderFilter = RequestUtil.getOrderFilter(json);
            // condition += BusinessUtil.getSJYXCondition(null);
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            String sql = SQL_QUERY_GCBBMMX;
            
            BaseResultSet bs = DBUtil.query(conn, sql, page);

            // 合同表
            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");
            // 项目下达库
            // bs.setFieldTranslater("XDKID", "GC_TCJH_XMXDK", "ID", "XMMC");
            // 标段表
            // bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");

            // 设置字典
            // bs.setFieldDic("SQDW", "SQDW");//申请单位
            // bs.setFieldDic("SJBH", "SJBH");//事件编号
            // bs.setFieldDic("SJMJ", "SJMJ");//数据密级

         // 设置字典
            bs.setFieldDic("GCBSHMXZT", "GCBSHMXZT");// 工程部提请款状态
            bs.setFieldDic("HTLX", "HTLX");
            //金额
            bs.setFieldThousand("ZXHTJ");
            bs.setFieldThousand("ZWCZF");
            bs.setFieldThousand("ZHTZF");
            bs.setFieldThousand("YBF");
            bs.setFieldThousand("BCSQ");
            bs.setFieldThousand("JZQR");
            
            
            bs.setFieldSjbh("SJBH");
            // 设置查询条件
            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份
            // bs.setFieldThousand("DYJLSDZ");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<工程部提请款审批>", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("提请款部门查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

  
	@Override
	public String updateGcbMxzt(String json, User user, HashMap map) throws Exception {
		 Connection conn = DBUtil.getConnection();
		 String resultVO = null;
		 GcZjglTqkbmVO vo = new GcZjglTqkbmVO();

		 try {
			 String id = (String) map.get("ids");
			 
			 
			 
			 //判断是通过还是退回
			 String type = (String) map.get("type");
			 
			 conn.setAutoCommit(false);
			 
			 if ("tongguo".equals(type)) {
				 String sql_update_gcbmx = "UPDATE GC_ZJGL_TQKGCB t set t.TQKZTGCB = 1 where t.id in ("+id+")";
				 
				 //更新明细状态
				 DBUtil.exec(conn, sql_update_gcbmx);
				 conn.commit();
				 
				 
			 }else if("tuihui".equals(type)){
				 String sql_update_gcbmx = "UPDATE GC_ZJGL_TQKGCB t set t.TQKZTGCB = 2 where t.id in ("+id+")";
				 
				 //更新明细状态
				 DBUtil.exec(conn, sql_update_gcbmx);
				 conn.commit();
				 
				 //返回给部门处理
				 String sql_update_sql_returnBM = "update gc_zjgl_tqkbm t set t.tqkzt=3 where t.id in (select t2.BMTQKID from gc_zjgl_tqkgcb t2 where t2.id in ("+id+"))";
				 DBUtil.exec(conn, sql_update_sql_returnBM);
				 conn.commit();
			 }
			 
	            
			 LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user.getOrgDept()
	                    .getDeptName() + " " + user.getName() + "提请款部门新增成功", user, "", "");


		 } catch (Exception e) {
			 DBUtil.rollbackConnetion(conn);
			 e.printStackTrace(System.out);
			 logger.error("提请款部门新增失败!");
			 LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user.getOrgDept()
					 .getDeptName() + " " + user.getName() + "提请款部门新增失败", user, "", "");
		 } finally {
			 DBUtil.closeConnetion(conn);
		 }
		 return resultVO;
	}

}
