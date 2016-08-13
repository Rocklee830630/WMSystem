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
public class GcZjglTqkbmServiceImpl extends Base1ServiceImpl<GcZjglTqkbmVO, String> implements GcZjglTqkbmService {

    private static Logger logger = LoggerFactory.getLogger(GcZjglTqkbmServiceImpl.class);

    // 业务类型
    private String ywlx = YwlxManager.GC_ZJGL_TQKBM;

    // 更新部门明细状态
    private static String SQL_UPDATE_MXZT = "UPDATE gc_zjgl_tqkbmmx t SET t.bmtqkmxzt='xxxxbmtqkmxztxxx' WHERE t.tqkid=?";

    // 查询部门提请款及明细情况
    private static String SQL_QUERY_ANDBMMX = "SELECT (SELECT COUNT(*) FROM gc_zjgl_tqkbmmx gzt WHERE t.id = gzt.tqkid) AS bmjls,       (SELECT sum(gzt.bcsq) FROM gc_zjgl_tqkbmmx gzt WHERE t.id = gzt.tqkid) AS bmze,       (SELECT COUNT(*)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid           AND t.id = gzt.tqkid AND (gzt.bmtqkmxzt='6' or gzt.bmtqkmxzt='7')) AS cwjls,       (SELECT sum(gzt.cwshz)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid           AND t.id = gzt.tqkid AND (gzt.bmtqkmxzt='6' or gzt.bmtqkmxzt='7')) AS cwze,       (SELECT COUNT(*)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid           AND t.id = gzt.tqkid AND gzt.bmtqkmxzt='7') AS jcjls,       (SELECT sum(gzt.jchdz)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid AND t.id = gzt.tqkid  AND gzt.bmtqkmxzt='7') AS jcze,       t.*  FROM gc_zjgl_tqkbm t ";
    private static String SQL_QUERY_ANDCWMX_ZH = "SELECT (SELECT COUNT(*) FROM gc_zjgl_tqkbmmx gzt WHERE t.id = gzt.tqkid) AS bmjls,       (SELECT sum(gzt.bcsq) FROM gc_zjgl_tqkbmmx gzt WHERE t.id = gzt.tqkid) AS bmze,       (SELECT COUNT(*)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid           AND t.id = gzt.tqkid ) AS cwjls,       (SELECT sum(gzt.cwshz)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid           AND t.id = gzt.tqkid ) AS cwze,       (SELECT COUNT(*)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid           AND t.id = gzt.tqkid AND gzt.bmtqkmxzt='7') AS jcjls,       (SELECT sum(gzt.jchdz)          FROM gc_zjgl_tqkbmmx gzt, gc_zjgl_tqkmx mx         WHERE gzt.id = mx.bmmxid AND t.id = gzt.tqkid  AND gzt.bmtqkmxzt='7') AS jcze,       t.*  FROM gc_zjgl_tqkbm t ";

    private static String SQL_QUERY_BMTQKZH_JOIN_GCB = " SELECT * FROM VIEW_ZJGL_TQK_BMTQKZH t";
    
    private static String SQL_DELETE_TQKGCB_BY_BMTQKID = "delect from GC_ZJGL_TQKGCB g where g.BMTQKID = ?";
    private static String SQL_DELETE_TQKGCBMX_BY_BMTQKID = "delete from GC_ZJGL_TQKGCBMX g where g.GCBTQKID = (select g1.ID from GC_ZJGL_TQKGCB g1 where g1.BMTQKID = ?)";
    
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

            String sql = SQL_QUERY_ANDBMMX;
            if (map != null) {
                String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
                if (opttype.equals("tqkzh")) {
                    // 提请款综合菜单过来请求
                    sql = SQL_QUERY_BMTQKZH_JOIN_GCB;
                }
            }

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
            bs.setFieldDic("QKLX", "QKLX");// 请款类型
            bs.setFieldDic("TQKZT", "TQKZT");// 提请款状态

            bs.setFieldOrgDept("SQDW");
            bs.setFieldSjbh("SJBH");

            bs.setFieldUserID("BMBLRID");
            bs.setFieldUserID("CWBLRID");
            bs.setFieldUserID("JCBLRID");

            bs.setFieldThousand("ZZXHTJ");// 总最新合同价
            bs.setFieldThousand("ZYBF");// 总已拔付
            bs.setFieldThousand("ZBCSQ");// 总本次申请
            bs.setFieldThousand("ZLJBF");// 总累计拔付
            bs.setFieldThousand("ZCSZ");// 总财审值
            bs.setFieldThousand("ZJLQR");// 总监理确认款

            bs.setFieldThousand("BMZE");// 总监理确认款
            bs.setFieldThousand("CWZE");// 总监理确认款
            bs.setFieldThousand("JCZE");// 总监理确认款

            bs.setFieldOrgDept("SQDW");// 申请单位代码转名称

            
            bs.setFieldUserID("BMBLRID");
            // 设置查询条件
            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份
            // bs.setFieldThousand("DYJLSDZ");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<提请款部门>", user, "", "");

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
    public String insert(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglTqkbmVO vo = new GcZjglTqkbmVO();

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
            conn.commit();
            
            
            
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门新增成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + vo.getId()
                    + "\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user, null);

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

    @Override
    public String update(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglTqkbmVO vo = new GcZjglTqkbmVO();

        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            vo.setValueFromJson((JSONObject) list.get(0));
            
            String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
            boolean flag = false;
            String upsqlString = "";
            if (opttype.equals("bm")) {
                flag = true;
                // vo.setTqkzt("1");
                // upsqlString =
                // "UPDATE gc_zjgl_tqkbmmx t SET t.bmtqkmxzt='1', t.cwblr='"+user.getAccount()+"' WHERE t.tqkid='"+vo.getId()+"'";
                upsqlString = "UPDATE gc_zjgl_tqkbmmx t SET t.bmtqkmxzt='" + vo.getTqkzt() + "', t.cwblr='" + user.getAccount()
                        + "' WHERE t.tqkid=?";
                
                if("4".equals(vo.getTqkzt())){
                		vo.setBmshsftg("1");
                		//如果是工程类/监理类提请款，同时添加工程部审批数据
                      /*  if("15".equals(vo.getQklx())||"16".equals(vo.getQklx())){
                        	//清空原有的工程部数据（对于回退的提请款）
                        	//工程部审核明细数据
                        	String delete_gcbtqkmx = "delete from gc_zjgl_tqkgcbmx t3 where t3.GCBTQKID = (select t.id from gc_zjgl_tqkgcb t where t.BMTQKID = '"+vo.getId()+"')";
                        	DBUtil.exec(conn, delete_gcbtqkmx);
                        	conn.commit();
                        	//工程部审核数据
                        	String delete_gcbtqk = "delete from gc_zjgl_tqkgcb t where t.BMTQKID ='"+vo.getId()+"'";                        	
                        	DBUtil.exec(conn, delete_gcbtqk);
                        	conn.commit();
                        	//明细中是否有施工/监理合同
                        	String query_tqkbmmx = " select t.id,t2.htlx from gc_zjgl_tqkbmmx t left join gc_htgl_htsj t1 on t.htid = t1.id left join gc_htgl_ht t2 on t2.id = t1.htid where t.tqkid = ? and t2.htlx in ('SG','JL')";
                        	String[][] res = DBUtil.querySql(query_tqkbmmx, new Object[]{vo.getId()});
                        	
                        	if(res!=null&&res.length!=0){
                        		String tqkGcbId = insertTQKGCB(user, vo, conn);
                        		for (String[] enty : res) {
                            		
                            		GcZjglTqkgcbmxVO gcbmx = new GcZjglTqkgcbmxVO();
                                	gcbmx.setId(new RandomGUID().toString());
                                	gcbmx.setBmtqkmxid(enty[0]);
                                	gcbmx.setGcbtqkid(tqkGcbId);
                                	gcbmx.setGcbshmxzt("0");
                                	gcbmx.setLrr(user.getAccount()); // 更新人
                                	gcbmx.setLrsj(Pub.getCurrentDate()); // 更新时间
                                	gcbmx.setLrbm(user.getDepartment());// 录入人单位
                                	gcbmx.setLrbmmc(user.getOrgDept().getDeptName());// 录入人单位名称
                                	gcbmx.setYwlx(YwlxManager.GC_ZJGL_TQKGCBMX);

                                    EventVO eventgcbVO = EventManager.createEvent(conn, gcbmx.getYwlx(), user);// 生成事件
                                    gcbmx.setSjbh(eventgcbVO.getSjbh());
                                    
                                    BaseDAO.insert(conn, gcbmx);
                                    conn.commit();
            					}
                        	}
                        	
                        	
                        }*/
                }
                
                
                
            } else if (opttype.equals("cwb")) {
                flag = true;
                // vo.setTqkzt("3");
                // upsqlString =
                // "UPDATE gc_zjgl_tqkbmmx t SET t.bmtqkmxzt='3', t.jcblr='"+user.getAccount()+"' WHERE t.tqkid='"+vo.getId()+"'";
                upsqlString = "UPDATE gc_zjgl_tqkbmmx t SET t.bmtqkmxzt='" + vo.getTqkzt() + "', t.jcblr='" + user.getAccount()
                        + "' WHERE t.tqkid=?";
            } else if (opttype.indexOf("bmimport") != -1) {
                // 部门导入
                flag = false;

                String[] tmp_arr_String = StringUtils.split(opttype, ":");
                if (tmp_arr_String.length > 1) {
                    String insertIntoString = "insert into gc_zjgl_tqkbmmx (id, htid, tqkid, dwid, dwmc, xmmcnr, htbm, zxhtj, ybf, bcsq, ljbf, ahtbfb, csz, jzqr, ajlfkb, bmblr, bmtqkmxzt, ywlx, sjmj, sfyx, bz, lrr, lrsj, lrbm, lrbmmc, sortno) "
                            + "SELECT SYS_GUID(), htid, '"
                            + tmp_arr_String[1]
                            + "', dwid, dwmc, xmmcnr, htbm, zxhtj, ybf, bcsq, ljbf, ahtbfb, csz, jzqr, ajlfkb, '"
                            + user.getAccount()
                            + "', '1', ywlx, sjmj, sfyx, bz, '"
                            + user.getAccount()
                            + "', sysdate, lrbm, lrbmmc, sortno "
                            + "FROM gc_zjgl_tqkbmmx WHERE tqkid='" + vo.getId() + "'";
                    DBUtil.execSql(conn, insertIntoString);
                    conn.commit();
                }
                BusinessUtil.setUpdateCommonFields(vo, user);
            } else {
                vo.setYwlx(ywlx);
                BusinessUtil.setUpdateCommonFields(vo, user);
            }

            // 插入
            BaseDAO.update(conn, vo);
            resultVO = vo.getRowJson();
            
            if (flag) {
                // 更新明细状态
                // String upsql = SQL_UPDATE_MXZT.replaceAll("xxxxbmtqkmxztxxx",
                // vo.getTqkzt());
                // upsql = upsql.replaceAll("xxxxxtqkidxxxx", vo.getId());
                DBUtil.executeUpdate(upsqlString, new Object[] { vo.getId() });
            }

            conn.commit();

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门修改成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + vo.getId()
                    + "\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user, null);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款部门修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcZjglTqkbmVO vo = new GcZjglTqkbmVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            BaseDAO.delete(conn, vo);

            resultVo = vo.getRowJson();
            conn.commit();
            
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款部门删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款部门删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }

	@Override
	public String deleteTqk(String json, User user) {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZjglTqkbmVO vo = new GcZjglTqkbmVO();

		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			vo.setYwlx(ywlx);
			//置成失效
	        vo.setSfyx("0");
			//修改
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除提请款成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return resultVO;
	}
	
	private String insertTQKGCB(User user, GcZjglTqkbmVO vo, Connection conn) throws Exception{
		
		
		//如果是工程类/监理类的提请款，需提交工程部审核
        GcZjglTqkgcbVO gcbVo = new GcZjglTqkgcbVO();
        try {
        	 gcbVo.setId(new RandomGUID().toString());
             gcbVo.setTqkztgcb("0");
             gcbVo.setBmtqkid(vo.getId());
             gcbVo.setLrr(user.getAccount()); // 更新人
             gcbVo.setLrsj(Pub.getCurrentDate()); // 更新时间
             gcbVo.setLrbm(user.getDepartment());// 录入人单位
             gcbVo.setLrbmmc(user.getOrgDept().getDeptName());// 录入人单位名称
             gcbVo.setYwlx(YwlxManager.GC_ZJGL_TQKGCB);
             
             EventVO eventGcbVO = EventManager.createEvent(conn, gcbVo.getYwlx(), user);// 生成事件
             gcbVo.setSjbh(eventGcbVO.getSjbh());
             gcbVo.setYwlx(YwlxManager.GC_ZJGL_TQKGCB);
             
             //插入
             BaseDAO.insert(conn, gcbVo);
             conn.commit();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("提请款工程部添加!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "提请款工程部添加", user, "", "");
		}
       
        return gcbVo.getId();
       
	}

	@Override
	public String updateGcbMxzt(String json, User user, HashMap map) throws Exception {
		 Connection conn = DBUtil.getConnection();
		 String resultVO = null;
		 GcZjglTqkbmVO vo = new GcZjglTqkbmVO();

		 try {
			 String id = (String) map.get("id");
			 conn.setAutoCommit(false);
	          
			 String sql_update_gcbmx = "update GC_ZJGL_TQKGCBMX t  set t.GCBSHMXZT=1 where t.id in ("+id+")";

			 DBUtil.exec(conn, sql_update_gcbmx);
			 conn.commit();
	            
			 LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user.getOrgDept()
	                    .getDeptName() + " " + user.getName() + "提请款部门新增成功", user, "", "");

			 String jsona = "{querycondition: {conditions: [{\"value\":\"" + vo.getId()
	                    + "\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			 return queryCondition(jsona, user, null);

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
