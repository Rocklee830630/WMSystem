/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    zjgl.service.GcZjglLybzjService.java
 * 创建日期： 2013-08-30 上午 12:42:04
 * 功能：    接口：履约保证金
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-08-30 上午 12:42:04  蒋根亮   创建文件，实现基本功能
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
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

import com.ccthanking.business.zjgl.vo.GcZjglLybzjVO;
import com.ccthanking.business.zjgl.service.GcZjglLybzjService;
import com.ccthanking.framework.service.impl.Base1ServiceImpl;

/**
 * <p>
 * GcZjglLybzjService.java
 * </p>
 * <p>
 * 功能：履约保证金
 * </p>
 * 
 * <p>
 * <a href="GcZjglLybzjService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-08-30
 * 
 */

@Service
public class GcZjglLybzjServiceImpl extends Base1ServiceImpl<GcZjglLybzjVO, String> implements GcZjglLybzjService {

    private static Logger logger = LoggerFactory.getLogger(GcZjglLybzjServiceImpl.class);

    // 查询履约保证金列表 一条履约保证金可根据桥段信息关联出多条记录
    // private static String SQL_QUERY_LIST =
    // "SELECT   ghh2.htmc, ghh2.htbm, ghh2.yfdw, ghh2.zhtqdj, ghh2.id AS hid, t.*  "
    // + "FROM GC_ZJGL_LYBZJ t LEFT JOIN gc_htgl_ht ghh2 ON t.htid=ghh2.id  ";
    // private static String SQL_QUERY_LIST =
    // "SELECT gjs.xmmc, gjs.bdmc, gjs.nd, t.* FROM gc_zjgl_lybzj t LEFT JOIN gc_jh_sj gjs ON t.jhsjid=gjs.gc_jh_sj_id  ";
    // private static String SQL_QUERY_LIST =
    // "SELECT gjs.xmmc, gjs.bdmc, gjs.nd, ht.htmc, ht.htbm, t.*  FROM gc_zjgl_lybzj t  LEFT JOIN gc_jh_sj gjs    ON t.jhsjid = gjs.gc_jh_sj_id  LEFT JOIN gc_htgl_htsj ghh ON gjs.gc_jh_sj_id=ghh.jhsjid JOIN gc_htgl_ht ht ON ghh.htid=ht.id AND ht.htlx='SG'  ";
    private static String SQL_QUERY_LIST = "SELECT ztb.gc_ztb_sj_id,ztb.ztbmc,ZTB.ZBFS,X.ZBLX,ZTB.ZZBJ,ztb.dsfjgid, gjs.htmc, gjs.htbm,  t.*  FROM gc_zjgl_lybzj t  LEFT JOIN GC_ZTB_SJ ztb  ON t.ztbid = ztb.gc_ztb_sj_id   LEFT JOIN GC_ZTB_XQSJ_YS Y   ON  ZTB.GC_ZTB_SJ_ID = Y.ZTBSJID LEFT JOIN GC_ZTB_XQ X  ON  Y.ZTBXQID = X.GC_ZTB_XQ_ID LEFT JOIN view_htgl_ht_htsj_xm gjs  ON t.ztbid = gjs.ztbid";

    // 工程部提报审批用
    private static String SQL_QUERY_LIST_GCB = "SELECT ztb.gc_ztb_sj_id,ztb.ztbmc,ZTB.ZBFS,X.ZBLX,ZTB.ZZBJ,ztb.dsfjgid, gjs.htmc, gjs.htbm,  t.*  FROM gc_zjgl_lybzj t  LEFT JOIN GC_ZTB_SJ ztb  ON t.ztbid = ztb.gc_ztb_sj_id   LEFT JOIN GC_ZTB_XQSJ_YS Y   ON  ZTB.GC_ZTB_SJ_ID = Y.ZTBSJID LEFT JOIN GC_ZTB_XQ X  ON  Y.ZTBXQID = X.GC_ZTB_XQ_ID LEFT JOIN view_htgl_ht_htsj_xm gjs  ON t.ztbid = gjs.ztbid ";

    // private static String SQL_QUERY_LIST =
    // "SELECT   gjs.xmmc, gjs.bdmc,ghh2.htmc, ghh2.htbm, ghh2.yfdw, ghh.htqdj, ghh.id AS hid, t.*  "
    // +
    // "FROM GC_ZJGL_LYBZJ t LEFT JOIN gc_htgl_ht ghh2 ON t.htid=ghh2.id left JOIN gc_htgl_htsj ghh ON ghh.htid=ghh2.id "
    // + "LEFT JOIN gc_jh_sj gjs ON ghh.jhsjid=gjs.gc_jh_sj_id ";

  
     //查询请款类型显示不同的申请表
    private static  String SQL_QKLX_DIC_VALUE="SELECT dic_code,dic_value FROM fs_dic_tree fdt1 WHERE fdt1.parent_id IN( SELECT fdt.'id' --, fdt.*FROM fs_dic_tree fdt WHERE --fdt.dic_value LIKE '%合同类型%'fdt.dic_code='QKLX') --for update"; 
    
    // 业务类型
    private String ywlx = YwlxManager.GC_ZJGL_LYBZJ;

    @Override
    public String insert(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglLybzjVO vo = new GcZjglLybzjVO();

        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            vo.setValueFromJson((JSONObject) list.get(0));
            // 设置主键
            boolean flagfiles = false;
            String ywid = "";
            if (map.get("ywid") != null) {
                ywid = (String) map.get("ywid");
                if (StringUtils.isNotBlank(ywid)) {
                    flagfiles = true;
                    vo.setId(ywid);
                } else {
                    vo.setId(new RandomGUID().toString()); // 主键
                }
            }
            if("XJ".equals(vo.getJnfs())){
            	vo.setBhlx("");
            }

            vo.setFhqk("0");
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

            if (flagfiles) {
                FileUploadService.updateFjztByYwid(conn, ywid);// 附件处理
            }
            
            String condition = map.get("condition") == null ? "" : (String) map.get("condition");
            Pub.getFlowinf(conn, ywlx, vo.getSjbh(),user,condition);
            
            conn.commit();
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "履约保证金新增成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + vo.getId()
                    + "\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user, map);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("履约保证金新增失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "履约保证金新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcZjglLybzjVO vo = new GcZjglLybzjVO();

        try {
            conn.setAutoCommit(false);

            JSONArray list = vo.doInitJson(json);
            vo.setValueFromJson((JSONObject) list.get(0));

            String opttype = "";
            if (map.get("opttype") != null) {
                opttype = (String) map.get("opttype");
                if ("fh".equals(opttype)) {
                    GcZjglLybzjVO tmp = findById(vo.getId());
                    tmp.setFhrq(vo.getFhrq());
                    tmp.setFhr(vo.getFhr());
                    tmp.setFhqk(vo.getFhqk());

                    BusinessUtil.setUpdateCommonFields(tmp, user);

                    BaseDAO.update(conn, tmp);
                    resultVO = tmp.getRowJson();
                }
            } else {
                vo.setGxr(user.getAccount()); // 更新人
                vo.setGxsj(Pub.getCurrentDate()); // 更新时间
                vo.setGxbm(user.getDepartment());// 更新人单位
                vo.setGxbmmc(user.getOrgDept().getDeptName());// 更新人单位名称
                vo.setYwlx(ywlx);

                vo.setFhqk("0");

                BaseDAO.update(conn, vo);
                
                resultVO = vo.getRowJson();
            }

            conn.commit();
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "履约保证金修改成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + vo.getId()
                    + "\",\"fieldname\":\"t.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user, map);
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("履约保证金修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "履约保证金修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcZjglLybzjVO vo = new GcZjglLybzjVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            BaseDAO.delete(conn, vo);

            resultVo = vo.getRowJson();
            conn.commit();
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "履约保证金删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("履约保证金删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "履约保证金删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }

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
            BaseResultSet bs = null;

            String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
            if (opttype.equals("gcb")) {
                // 工程部查询页面. 需关联GC_ZJB_JSB(结算表) 提报日期, GC_SJGL_JJG(交竣工表) 竣工验收时间
                bs = DBUtil.query(conn, SQL_QUERY_LIST_GCB, page);

            } else {
                bs = DBUtil.query(conn, SQL_QUERY_LIST, page);
            }

            // 设置字典
            bs.setFieldDic("JNFS", "JNFS");// 交纳方式:现金、保函（则需要对方银行和始终日期）
            bs.setFieldDic("FHQK", "SHZT");// 返还情况
            bs.setFieldDic("BHLX", "BHLX");// 保函性质
            bs.setFieldDic("ZBLX", "ZBLX");// 招标类型
            bs.setFieldDic("ZBFS", "ZBFS");// 招标方式
            
            bs.setFieldUserID("BLR");
            // 设置千分位
            bs.setFieldThousand("ZZBJ");// 金额
            bs.setFieldThousand("JE");// 金额
            bs.setFieldTranslater("DSFJGID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
            bs.setFieldTranslater("JNDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
            bs.setFieldTranslater("DFYH", "FS_COMMON_DICT", "DICT_ID", "DICT_NAME");

            bs.setFieldSjbh("SJBH");

            domresult = bs.getJson();

            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<履约保证金>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("履约保证金查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }


	

}
