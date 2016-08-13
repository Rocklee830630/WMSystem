/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtService.java
 * 创建日期： 2013-09-02 上午 08:08:02
 * 功能：    接口：合同信息
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-02 上午 08:08:02  蒋根亮   创建文件，实现基本功能
 *
 * ==================================================================
 */
package com.ccthanking.business.htgl.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ccthanking.business.htgl.service.GcHtglHtService;
import com.ccthanking.business.htgl.vo.GcHtglHtVO;
import com.ccthanking.business.htgl.vo.GcHtglHtsjVO;
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
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.service.impl.Base1ServiceImpl;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * <p>
 * GcHtglHtService.java
 * </p>
 * <p>
 * 功能：合同信息
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-02
 * 
 */

@Service
public class GcHtglHtServiceImpl extends Base1ServiceImpl<GcHtglHtVO, String> implements GcHtglHtService {

    private static Logger logger = LoggerFactory.getLogger(GcHtglHtServiceImpl.class);

    // 业务类型
    private static String ywlx = YwlxManager.GC_HTGL_HT;

    // 查询合同列表
    private static String SQL_QUERY_HT_LIST = "SELECT ghh.ID AS htid, ghh.qdnf, ghh.htmc, ghh.htbm, ghh.htlx, "
            + "ghh.zhtqdj,ghh.yfid, ghh2.id as htsjid, gjs.gc_jh_sj_id AS jhsjid, gjs.xmid,gjs.xmmc, gjs.xmxz, gjs.nd, gjs.bdid,"
            + "gjs.bdmc,ghh.yfdw, ghh2.htqdj, ghh.htjqdrq, ghh2.wczf, ghh2.htzf   FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2 "
            + "ON ghh.ID=ghh2.htid LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid";

    // 查询施工类合同列表 施工、监理单位 --标段监理单位必须唯一 即一个标段只能唯一在一个监理合同里 现有不唯一情况，所以用join ，应该用子查询,
    // 且VIEW_HTGL_JLHT_JHSJID_YFDW 的join 要去掉
    // --,(SELECT htyf.yfid FROM VIEW_HTGL_JLHT_JHSJID_YFDW htyf WHERE
    // htyf.jhsjid = gjs.gc_jh_sj_id) AS jldwid
    // --,(SELECT htyf.yfdw FROM VIEW_HTGL_JLHT_JHSJID_YFDW htyf WHERE
    // htyf.jhsjid = gjs.gc_jh_sj_id) AS jldwmc
    private static String SQL_QUERY_SGHT_LIST = "SELECT ghh.ID AS htid, ghh.qdnf, ghh.htmc, ghh.htbm, "
            + "ghh.zhtqdj, ghh2.id as htsjid, gjs.gc_jh_sj_id AS jhsjid, gjs.xmid,gjs.xmbh,gjs.xmmc, gjs.xmxz, gjs.nd, gjs.bdid,"
            + "gjs.bdmc , ghh2.htqdj, ghh.htjqdrq  , ghh.htlx AS xmlx, ghh.yfid AS sgdwid  ,ghh.yfdw   AS sgdwmc, htyf.yfid AS jldwid, htyf.yfdw AS jldwmc "
            + "FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2 "
            + "ON ghh.ID=ghh2.htid LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid "
            + "LEFT JOIN VIEW_HTGL_JLHT_JHSJID_YFDW htyf on htyf.jhsjid = gjs.gc_jh_sj_id ";

    // 合同列表查询
    private static String SQL_QUERY_HT = "SELECT ghh.*,ghh2.id as htsjid,gjs.gc_jh_sj_id AS jhsjid,gjs.xmid,"
            + "gjs.xmmc,gjs.xmxz,gjs.nd,gjs.bdid,gjs.bdmc,ghh2.htqdj "
            + "FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2  ON ghh.ID=ghh2.htid "
            + "LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid";

    // 合同列表查询
    // private static String SQL_QUERY_HT1 =
    // "SELECT distinct (ghh.zhtqdj+ghh.zbgje) AS zzxhtjdo, ghh.* ,gzs.ztbmc,gpz.GXLB,gpz.ZXMMC,gpz.GC_PQ_ZXM_ID "
    // + "FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2  ON ghh.ID=ghh2.htid "
    // + "LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid "
    // + "LEFT JOIN GC_ZTB_SJ gzs ON gzs.gc_ztb_sj_id = ghh.ztbid "
    // + "LEFT JOIN GC_PQ_ZXM gpz on gpz.htid = ghh.id ";
    // 合同列表查询 排迁信息导致查询不出结果
    private static String SQL_QUERY_HT1 = "SELECT distinct (case when ghh.htzt > 1 then ghh.zzxhtj else ghh.zhtqdj+ghh.zbgje end) AS zzxhtjdo, ghh.* ,gzl.je bzj_jnje,gzl.jnrq bzj_jnrq,gzs.ztbmc,gzx.zblx,gzs.ZKXS,DECODE(ghh.htlx, 'PQ', (SELECT gpz.gxlb FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS GXLB                 ,DECODE(ghh.htlx, 'PQ', (SELECT gpz.ZXMMC FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS ZXMMC                 ,DECODE(ghh.htlx, 'PQ', (SELECT gpz.GC_PQ_ZXM_ID FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS GC_PQ_ZXM_ID  "
            + "FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2  ON ghh.ID=ghh2.htid "
            + "LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid "
            + "LEFT JOIN GC_ZTB_SJ gzs ON gzs.gc_ztb_sj_id = ghh.ztbid " 
            + "LEFT JOIN GC_ZTB_XQSJ_YS gzxy ON gzs.gc_ztb_sj_id=gzxy.ZTBSJID "
            + "LEFT JOIN GC_ZTB_XQ gzx ON gzxy.ZTBXQID=gzx.GC_ZTB_XQ_ID "
            + "LEFT JOIN GC_ZJGL_LYBZJ gzl on gzl.htid = ghh.id ";
    
    private static String SQL_QUERY_HT2 = "SELECT distinct (case when ghh.htzt > 1 then ghh.zzxhtj else ghh.zhtqdj+ghh.zbgje end) AS zzxhtjdo, ghh.* ,gzl.je bzj_jnje,gzl.jnrq bzj_jnrq,gzs.ztbmc,gzx.zblx,DECODE(ghh.htlx, 'PQ', (SELECT gpz.gxlb FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS GXLB                 ,DECODE(ghh.htlx, 'PQ', (SELECT gpz.ZXMMC FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS ZXMMC                 ,DECODE(ghh.htlx, 'PQ', (SELECT gpz.GC_PQ_ZXM_ID FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS GC_PQ_ZXM_ID  "
        + "FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2  ON ghh.ID=ghh2.htid "
        + "LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid "
        + "LEFT JOIN GC_ZTB_SJ gzs ON gzs.gc_ztb_sj_id = ghh.ztbid " 
        + "LEFT JOIN GC_ZTB_XQSJ_YS gzxy ON gzs.gc_ztb_sj_id=gzxy.ZTBSJID "
        + "LEFT JOIN GC_ZTB_XQ gzx ON gzxy.ZTBXQID=gzx.GC_ZTB_XQ_ID "
        + "LEFT JOIN GC_ZJGL_LYBZJ gzl on gzl.htid = ghh.id ";

    // 合同 信息查询(包含履约保障金)
    private static String SQL_QUERY_HT_LYBZJ = "SELECT distinct (ghh.zhtqdj+ghh.zbgje) AS zzxhtjdo, ghh.* ,gzs.ztbmc,t2.dwmc bzj_JNDW,t.jnrq bzj_Jnrq,t.je bzj_jnje,t.jnfs bzj_jnfs,t.blr,DECODE(ghh.htlx, 'PQ', (SELECT gpz.gxlb FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS GXLB                 ,DECODE(ghh.htlx, 'PQ', (SELECT gpz.ZXMMC FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS ZXMMC                 ,DECODE(ghh.htlx, 'PQ', (SELECT gpz.GC_PQ_ZXM_ID FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS GC_PQ_ZXM_ID  "
            + "FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2  ON ghh.ID=ghh2.htid "
            + "LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid "
            + "LEFT JOIN GC_ZTB_SJ gzs ON gzs.gc_ztb_sj_id = ghh.ztbid "
            + "LEFT JOIN gc_zjgl_lybzj t ON t.jhsjid=ghh2.jhsjid "
            + "LEFT JOIN GC_CJDW t2 on t2.gc_cjdw_id = t.jndw";

    // 从招投标新增标段信息
    // private static String SQL_INSERINTO_FROM_ZTB =
    // "insert into gc_htgl_htsj  (id, jhsjid, xmid, bdid, htid, htqdj) SELECT SYS_GUID(),gzj.jhsjid, c.xmid, c.bdid, 'xxxxhtidxxxx', gzj.zzbj FROM gc_ztb_xqsj_ys gzxy, gc_ztb_jhsj gzj,gc_jh_sj c, GC_TCJH_XMXDK p WHERE gzxy.ztbxqid=gzj.xqid AND c.xmid = p.gc_tcjh_xmxdk_id  AND c.gc_jh_sj_id=gzj.jhsjid AND gzxy.ztbsjid='xxxxxztbidxxxxx'";
    // 从招标信息新增合同时，添加录入人的信息 add by xiahongbo by 2014-11-04
    private static String SQL_INSERINTO_FROM_ZTB = "insert into gc_htgl_htsj(id, jhsjid, xmid, bdid, htid, htqdj,GCJGBFB,ZXHTJ,LRR,LRSJ,LRBM,LRBMMC) SELECT SYS_GUID(),gzj.jhsjid, gjs.xmid, gjs.bdid, 'xxxxhtidxxxx', gzj.zzbj,case when gzs.zzbj=0 then 0 else round(gzj.zzbj / gzs.zzbj * 100, 2) end ,gzj.zzbj, 'xxxxxLRRxxxxx',sysdate,'xxxxxLRBMxxxxx','xxxxxLRBMMCxxxxx' FROM gc_ztb_sj gzs, gc_ztb_xqsj_ys gzxy, gc_ztb_jhsj gzj, gc_jh_sj gjs WHERE gzs.gc_ztb_sj_id=gzxy.ztbsjid AND gzxy.ztbxqid=gzj.xqid AND gjs.gc_jh_sj_id=gzj.jhsjid and gzs.gc_ztb_sj_id = gzj.zbid AND gzs.gc_ztb_sj_id='xxxxxztbidxxxxx'";

    private static String UPDATE_XMID_BDID_FROM_JHSJID_DXMHT = "UPDATE gc_htgl_htsj gh SET xmid =(SELECT gjs.xmid FROM gc_jh_sj gjs WHERE gjs.gc_jh_sj_id=gh.jhsjid), gh.bdid =(SELECT gjs.bdid FROM gc_jh_sj gjs WHERE gjs.gc_jh_sj_id=gh.jhsjid)  WHERE gh.htid=?";

    private static String UPDATE_XMID_BDID_FROM_JHSJID = "UPDATE gc_htgl_htsj gh SET xmid =(SELECT gjs.xmid FROM gc_jh_sj gjs WHERE gjs.gc_jh_sj_id=gh.jhsjid), gh.bdid =(SELECT gjs.bdid FROM gc_jh_sj gjs WHERE gjs.gc_jh_sj_id=gh.jhsjid), gh.htqdj = (decode((SELECT gjs.bdid FROM gc_jh_sj gjs WHERE gjs.gc_jh_sj_id=gh.jhsjid),null,(select xd.jhztze from gc_tcjh_xmxdk xd where gh.xmid = xd.gc_tcjh_xmxdk_id),(select bd.gcztfy from gc_xmbd bd where gh.bdid=bd.gc_xmbd_id)))  WHERE gh.htid=?";

    // 单项目合同 合同数据签订价 = 合同表 总签定价 价格百分比=100
    private static String UPDATE_DXMHT_HTSJ_HTQDJ = "UPDATE gc_htgl_htsj sj SET sj.htqdj=(SELECT ht.zhtqdj FROM gc_htgl_ht ht WHERE ht.id=sj.htid ), sj.gcjgbfb=100 WHERE sj.htid=?";
    
    //单项目合同合同结算
    private static String UPDATE_DXMHT_HTSJ_HTJS = "UPDATE gc_htgl_htsj sj SET sj.ZXHTJ=?,sj.HTJSJ=?,sj.HTJS=? WHERE sj.htid=?";

    // 根据数据ID查询是否存在施工合同， 如果不存在则查询招投标需求中获取单位信息
    private static String SQL_QUERY_DWXX_HT = "SELECT yfdw, yfid  FROM view_htgl_ht_htsj_xm gzj ";// WHERE
                                                                                                  // gzj.jhsjid=?
    // private static String SQL_QUERY_DWXX_ZTB =
    // "SELECT gzs.dsfjgid AS yfid , gc.dwmc AS yfdw  FROM gc_ztb_xq gzx, gc_ztb_jhsj gzj,gc_ztb_xqsj_ys gzxy, gc_ztb_sj gzs, gc_cjdw gc   WHERE gzx.gc_ztb_xq_id=gzj.xqid AND gzxy.ztbxqid=gzx.gc_ztb_xq_id AND gzs.gc_ztb_sj_id=gzxy.ztbsjid  AND gzs.dsfjgid=gc.gc_cjdw_id  AND gzj.jhsjid=?";
    private static String SQL_QUERY_DWXX_ZTB = "SELECT yfid, yfdw FROM VIEW_DWXX_ZTb gzj ";

    // 查询某合同的已拔付 已审批的情况下才是这个值
    // private static String SQL_QUERY_YBF =
    // "SELECT sum(gzt2.jchdz) as ybf   FROM gc_zjgl_tqkbmmx gzt2, gc_zjgl_tqkbm gzt WHERE gzt.tqkzt='6' AND gzt2.htid=?";
    private static String SQL_QUERY_YBF = "select ybf, htid from view_tqkbmxx_ht_ybf gzj";
    // 合同支付>新增 本月完成投资
    private static String SQL_QUERY_BYWCTZ = "SELECT DYYFK, TCJH_SJ_ID, nd, jlyf,gck1_dy, gck1_lj,gck_dy,gck_lj FROM VIEW_HTZF_JLB_BYWCTZ ghh2 ";

    // 更新合同数据(合同价、支付、合同最新价)
    private static String SQL_UPDATE_HT_ZFXX = "UPDATE gc_htgl_ht ght SET "
            + " ght.zhtzf=(SELECT decode(SUM(ghh.htzf), NULL, 0, SUM(ghh.htzf)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id) "
            + " ,ght.zwczf=(SELECT decode(SUM(ghh.wczf), NULL, 0, SUM(ghh.wczf)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id) "
            + " ,ght.zbgje=(SELECT decode(SUM(ghh.bgje), NULL, 0, SUM(ghh.bgje)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id) "
            + " ,ght.zhtqdj=decode((SELECT decode(SUM(ghh.htqdj), NULL, 0, SUM(ghh.htqdj)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id),0,ght.zhtqdj,(SELECT decode(SUM(ghh.htqdj), NULL, 0, SUM(ghh.htqdj)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id)) "
            + " ,ght.zzxhtj=decode((SELECT decode(SUM(ghh.zxhtj), NULL, 0, SUM(ghh.zxhtj)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id),0,ght.zzxhtj,(SELECT decode(SUM(ghh.zxhtj), NULL, 0, SUM(ghh.zxhtj)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id)) "
            + " WHERE ght.id=? ";

    // 更新GC_PQ_ZXM ，表里有HTID
    private static String UPDATE_GC_PQ_ZXM_HTID = "UPDATE GC_PQ_ZXM SET htid = ?,htsx=? WHERE gc_pq_zxm_id=?";

    // 删除合同信息 合同支付 合同变更 完成投资 附件
    String DEL_SQL_FS_FILEUPLOAD = "DELETE FROM fs_fileupload WHERE ywid=?";
    String DEL_SQL_GC_HTGL_HT_HTZF = "DELETE FROM gc_htgl_ht_htzf WHERE htsjid IN(SELECt id FROM  gc_htgl_htsj ghh WHERE ghh.htid=?)";
    String DEL_SQL_GC_HTGL_HT_WCTZ = "DELETE FROM gc_htgl_ht_wctz WHERE htsjid IN(SELECt id FROM  gc_htgl_htsj ghh WHERE ghh.htid=?)";
    String DEL_SQL_GC_HTGL_HT_HTBG = "DELETE FROM gc_htgl_ht_htbg WHERE htsjid IN(SELECt id FROM  gc_htgl_htsj ghh WHERE ghh.htid=?)";
    String DEL_SQL_GC_HTGL_HTSJ = "DELETE FROM gc_htgl_htsj WHERE htid=?";
    // 如果是排迁类合同， 则把htid置空
    private static String DEL_SQL_GC_PQ_ZXM = "UPDATE GC_PQ_ZXM SET htid = '' WHERE htid=?";

    // 如果关联履约保证金，把htid置空
    private static String DEL_SQL_GC_ZJGL_LYBZJ = "UPDATE GC_ZJGL_LYBZJ G SET G.HTID = '' WHERE G.HTID = ?";
    // 查询合同造价信息
    private static String SQL_QUERY_HTZJXX = "select  DECODE ( gzl.cssdz, NULL, 0, gzl.cssdz ) cssdz,gzs.zzbj,ghh.zzxhtj,ghh.zhtjs from gc_htgl_ht ghh  "
            + "  left join gc_ztb_sj gzs on gzs.gc_ztb_sj_id = ghh.ztbid "
            + "  left join gc_ztb_xqsj_ys gzxy on gzs.gc_ztb_sj_id = gzxy.ztbsjid "
            + " left join gc_ztb_jhsj gzj on gzxy.ztbxqid = gzj.xqid " + " left join gc_zjb_lbjb gzl on gzl.jhsjid = gzj.jhsjid ";

    private static String SQL_QUERY_QYLIST = "select ghh.htlx,count(0) ZQDJ,sum(ghh.zhtqdj) zzhtqdj ,sum(ghh.zwczf) zzwczf ,sum(ghh.zhtzf) zzhtzf , "
            + "decode(sum(ghh.zhtqdj),0,0,round((sum(ghh.zwczf)/sum(ghh.zhtqdj)),4)*100) ztzqdb, "
            + "decode(sum(ghh.zhtqdj),0,0,round((sum(ghh.zhtzf)/sum(ghh.zhtqdj)),4)*100) zzfqdb, "
            + "(select count(0) from gc_htgl_ht ht1 where ht1.htlx = ghh.htlx and ht1.qdnf='xxxhtndxxx') ndhtqds,  "
            + "decode((select sum(ht2.zhtqdj) from gc_htgl_ht ht2 where ht2.htlx = ghh.htlx and ht2.qdnf='xxxhtndxxx'),NULL,0,(select sum(ht2.zhtqdj) from gc_htgl_ht ht2 where ht2.htlx = ghh.htlx and ht2.qdnf='xxxhtndxxx')) ndzhtqd,  "
            + "decode((select sum(ht3.zwczf) from gc_htgl_ht ht3 where ht3.htlx = ghh.htlx and ht3.qdnf='xxxhtndxxx'),NULL,0,(select sum(ht3.zwczf) from gc_htgl_ht ht3 where ht3.htlx = ghh.htlx and ht3.qdnf='xxxhtndxxx')) ndzhttz,  "
            + "decode((select sum(ht4.zhtzf) from gc_htgl_ht ht4 where ht4.htlx = ghh.htlx and ht4.qdnf='xxxhtndxxx'),NULL,0,(select sum(ht4.zhtzf) from gc_htgl_ht ht4 where ht4.htlx = ghh.htlx and ht4.qdnf='xxxhtndxxx')) ndzhtzf,  "
            + "decode((select sum(ht5.zhtqdj) from gc_htgl_ht ht5 where ht5.htlx = ghh.htlx and ht5.qdnf='xxxhtndxxx'),0,0,(select round((sum(ht5.zwczf)/sum(ht5.zhtqdj)),2)*100 from gc_htgl_ht ht5 where ht5.htlx = ghh.htlx and ht5.qdnf='xxxhtndxxx')) ndtzqdb, "
            + "decode((select sum(ht6.zhtqdj) from gc_htgl_ht ht6 where ht6.htlx = ghh.htlx and ht6.qdnf='xxxhtndxxx'),0,0,(select round((sum(ht6.zhtzf)/sum(ht6.zhtqdj)),2)*100 from gc_htgl_ht ht6 where ht6.htlx = ghh.htlx and ht6.qdnf='xxxhtndxxx')) ndzfqdb   "
            + "from gc_htgl_ht ghh group by ghh.htlx ";

    private static String SQL_QUERY_HT_BD = "select gx.gc_xmbd_id from gc_htgl_ht ht left join gc_htgl_htsj ghh on ht.id = ghh.htid left join gc_jh_sj gjs on ghh.jhsjid = gjs.gc_jh_sj_id left join GC_TCJH_XMXDK gtx on gtx.gc_tcjh_xmxdk_id = gjs.xmid left join gc_xmbd gx on ghh.bdid = gx.gc_xmbd_id where ghh.jhsjid is not null and gx.gc_xmbd_id is not null and ht.id = ?";

    private static String SQL_QUERY_HT_XM = "select gtx.gc_tcjh_xmxdk_id from gc_htgl_ht ht left join gc_htgl_htsj ghh on ht.id = ghh.htid left join gc_jh_sj gjs on ghh.jhsjid = gjs.gc_jh_sj_id left join GC_TCJH_XMXDK gtx on gtx.gc_tcjh_xmxdk_id = gjs.xmid left join gc_xmbd gx on ghh.bdid = gx.gc_xmbd_id where ghh.jhsjid is not null and gx.gc_xmbd_id is null and ht.id = ?";

    // 修改合同状态为已结算
    private static String SQL_UPDATE_HTZT_YJS = "UPDATE GC_HTGL_ HT WHERE NOT EXSITS (select * from gc_htgl_htsj ghh1 left join gc_htgl_ht ghh2 on ghh1.htid = ghh2.id where ghh2.id = ? and ghh1.htjs is null) AND ID = ?";

    // 查询投资控制动态分析_合同列表
    private static String SQL_QUERY_TZJK = "SELECT  DECODE(v.zzhtqdj,0,0, ROUND((v.zzwczf / v.zzhtqdj) * 100, 2)) AS zzwczfbl, DECODE(v.zzhtqdj,0,0,  ROUND((v.zzhtzf / v.zzhtqdj) * 100, 2)) AS zzhtzfbl,v.* FROM VIEW_HT_HTSJ_JHSJ v ";

    // 查询单个合同信息
    private static String SQL_QUERYONE_HT = "SELECT distinct (ghh.zhtqdj+ghh.zbgje) AS zzxhtjdo, ghh.* ,gzs.ztbmc,DECODE(ghh.htlx, 'PQ', (SELECT gpz.gxlb FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS GXLB                 ,DECODE(ghh.htlx, 'PQ', (SELECT gpz.ZXMMC FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS ZXMMC                 ,DECODE(ghh.htlx, 'PQ', (SELECT gpz.GC_PQ_ZXM_ID FROM GC_PQ_ZXM gpz WHERE gpz.htid=ghh.id), '') AS GC_PQ_ZXM_ID  "
            + "FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2  ON ghh.ID=ghh2.htid "
            + "LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid " + "LEFT JOIN GC_ZTB_SJ gzs ON gzs.gc_ztb_sj_id = ghh.ztbid ";

    // 查询履约保证金信息
    private static String SQL_QUERY_LYBZJ = "select v.id,v.jhsjid,v.xmid,v.bdid,v.je,v.jnrq,v2.dwmc,v.jnfs,v1.xmmc,v1.bdmc from gc_zjgl_lybzj v "
            + " left join gc_jh_sj v1 on v1.gc_jh_sj_id = v.jhsjid left join gc_cjdw v2 on v2.gc_cjdw_id = v.jndw ";

    // 查询设计任务书
    private static String SQL_QUERY_SJRWS_BYJHSJID = "select gjs.bdmc,gjs.bdid,(select count(*) from GC_SJGL_RWSGL t where t.jhsjid = gjs.gc_jh_sj_id and t.rwslx = 2) zs from gc_jh_sj gjs where gjs.gc_jh_sj_id in (xxxxjhsjidsxxxx) ";

    private static String SQL_QUERY_SJRWS_BYZTB = "select gjs.bdmc,gjs.bdid,(select count(*) from GC_SJGL_RWSGL t where t.jhsjid = gjs.gc_jh_sj_id and t.rwslx = 2) zs from gc_jh_sj gjs where gjs.gc_jh_sj_id in (SELECT gzj.jhsjid FROM gc_ztb_sj gzs, gc_ztb_xqsj_ys gzxy, gc_ztb_jhsj gzj, gc_jh_sj gjs WHERE gzs.gc_ztb_sj_id=gzxy.ztbsjid AND gzxy.ztbxqid=gzj.xqid AND gjs.gc_jh_sj_id=gzj.jhsjid AND gzs.gc_ztb_sj_id='xxxxxztbidxxxxx') ";

    @Override
    public String querySgHt(String json, User user) throws Exception {
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            PageManager page = RequestUtil.getPageManager(json);
            QueryConditionList list = RequestUtil.getConditionList(json);

            String condition = " ghh.sfyx='1' and gjs.sfyx='1' and ghh2.sfyx='1' and ghh.htlx='SG' and gjs.wgsj_sj is not null and  NOT EXISTS (SELECT * FROM GC_ZJB_JSB j WHERE j.htid=ghh2.ID and j.sfyx='1') ";
            condition += list == null ? "" : " and " + list.getConditionWhere();

            String orderFilter = RequestUtil.getOrderFilter(json);

            condition += BusinessUtil.getSJYXCondition("ghh");
            condition += BusinessUtil.getCommonCondition(user, "ghh");
            condition += orderFilter;

            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            // TODO 需要补充完成施工监理数据信息.
            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_SGHT_LIST, page);

            bs.setFieldDic("XMLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldDic("QDNF", "XMNF");// 项目年份
            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同

            // 日期
            bs.setFieldDateFormat("HTJQDRQ", "yyyy-MM-dd");

            bs.setFieldThousand("ZHTQDJ");// 总合同签订价(元)
            bs.setFieldThousand("HTQDJ");// 合同签订价(元)

            domresult = bs.getJson();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String queryHt(String json, User user) throws Exception {
        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            String orderFilter = RequestUtil.getOrderFilter(json);

            if (!StringUtils.isBlank(condition)) {
            }
            condition += BusinessUtil.getSJYXCondition("ghh");
            condition += BusinessUtil.getCommonCondition(user, "ghh");
            condition += orderFilter;

            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            // String sql =
            // "SELECT ghh.ID AS htid, ghh2.id as htsjid, ghh2.xmbh,ghh2.bdid, ghh.htmc, ghh.htbm, ghh.yfdw, ghh2.htqdj, ghh.htjqdrq, gjs.xmmc, gjs.bdmc"
            // +
            // "  FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2 ON ghh.ID=ghh2.htid LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid ";

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_HT_LIST, page);

            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldDic("QDNF", "XMNF");// 项目年份
            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同

            // 日期
            bs.setFieldDateFormat("HTJQDRQ", "yyyy-MM-dd");

            bs.setFieldThousand("ZHTQDJ");// 总合同签订价(元)
            bs.setFieldThousand("HTQDJ");// 合同签订价(元)
            bs.setFieldThousand("HTZF");// 标段合同支付(元)
            bs.setFieldThousand("WCZF");// 标段完成支付(元)

            bs.setFieldTranslater("YFID", "GC_CJDW", "GC_CJDW_ID", "DWMC");

            domresult = bs.getJson();
        } catch (Exception e) {
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
            // condition += BusinessUtil.getSJYXCondition(null);
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_HT1, page);
            // 合同表
            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");
            // 项目下达库
            // bs.setFieldTranslater("XDKID", "GC_TCJH_XMXDK", "ID", "XMMC");
            // 标段表
            // bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");

            // 设置字典
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldDic("BXQDWLX", "BXQDW");// 保修期单位：年、季、月、日
            bs.setFieldDic("QDNF", "XMNF");// 项目年份
            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同
            bs.setFieldDic("SFZFJTZ", "SF");// 是否支付即投资
            bs.setFieldDic("SFDXMHT", "SF");// 是否单项目
            bs.setFieldDic("GXLB", "GXLB");// 是否单项目
            bs.setFieldDic("JFDW", "JFDW");// 甲方单位

            // 设置查询条件
            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份

            bs.setFieldThousand("BZJ_JNJE");// 履约保证金
            bs.setFieldThousand("YHSJE");// 印花税金额(元)
            bs.setFieldThousand("BXJE");// 保修金额
            bs.setFieldThousand("ZHTQDJ");// 总合同签订价(元)
            bs.setFieldThousand("ZHTZF");// 总合同支付
            bs.setFieldThousand("ZWCZF");// 总完成投资
            bs.setFieldThousand("ZBGJE");// 总变更金额
            bs.setFieldThousand("ZHTJS");// 总合同结算
            bs.setFieldThousand("ZZXHTJ");// 总最新合同价
            bs.setFieldThousand("ZZXHTJDO");// 总最新合同价 计算出

            bs.setFieldSjbh("SJBH");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同信息查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
    
    /**
     * 部门合同补存招投标查询
     * @param request
     * @param json
     * @return
     * @throws Exception
     */
    @Override
    public String queryConditionForZtb(String json, User user, HttpServletRequest request) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {
        	String sfztb = request.getParameter("sfztb");
        	

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            String orderFilter = RequestUtil.getOrderFilter(json);
            // condition += BusinessUtil.getSJYXCondition(null);
            condition += BusinessUtil.getCommonCondition(user, null);
            if("0".equals(sfztb)) {
                condition += " and ghh.ZTBID is null ";
            } else if("1".equals(sfztb)) {
                condition += " and ghh.ZTBID is not null ";
            }
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_HT1, page);
            // 合同表
            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");
            // 项目下达库
            // bs.setFieldTranslater("XDKID", "GC_TCJH_XMXDK", "ID", "XMMC");
            // 标段表
            // bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");

            // 设置字典
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldDic("BXQDWLX", "BXQDW");// 保修期单位：年、季、月、日
            bs.setFieldDic("QDNF", "XMNF");// 项目年份
            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同
            bs.setFieldDic("SFZFJTZ", "SF");// 是否支付即投资
            bs.setFieldDic("SFDXMHT", "SF");// 是否单项目
            bs.setFieldDic("GXLB", "GXLB");// 是否单项目
            bs.setFieldDic("JFDW", "JFDW");// 甲方单位

            // 设置查询条件
            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份

            bs.setFieldThousand("BZJ_JNJE");// 履约保证金
            bs.setFieldThousand("YHSJE");// 印花税金额(元)
            bs.setFieldThousand("BXJE");// 保修金额
            bs.setFieldThousand("ZHTQDJ");// 总合同签订价(元)
            bs.setFieldThousand("ZHTZF");// 总合同支付
            bs.setFieldThousand("ZWCZF");// 总完成投资
            bs.setFieldThousand("ZBGJE");// 总变更金额
            bs.setFieldThousand("ZHTJS");// 总合同结算
            bs.setFieldThousand("ZZXHTJ");// 总最新合同价
            bs.setFieldThousand("ZZXHTJDO");// 总最新合同价 计算出

            bs.setFieldSjbh("SJBH");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同信息查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }
    
    @Override
    public String queryConditionForIndex(String json, User user,Map<String, String> map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String htid = map.get("htid");
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            String orderFilter = RequestUtil.getOrderFilter(json);
            // condition += BusinessUtil.getSJYXCondition(null);
            condition += BusinessUtil.getCommonCondition(user, null);
            
            String wcrid = map.get("wcrid");
            if(StringUtils.isNotBlank(wcrid)){
            	String sql_w = "select ats.sjbh from ap_task_schedule ats where ats.ywlx = '700101' and ats.wcrid = '"+wcrid+"'"; 
            	if(StringUtils.isNotBlank(map.get("kssj"))){
            		sql_w += " and ats.wcsj >= to_date('"+map.get("kssj")+"','yyyy-mm-dd') ";
            	}
            	if(StringUtils.isNotBlank(map.get("jssj"))){
            		sql_w += " and ats.wcsj <= to_date('"+map.get("jssj")+"','yyyy-mm-dd') ";
            	}
            	condition += " and ghh.sjbh in ("+sql_w+") ";
            }
            
            if(StringUtils.isNotBlank(htid)) {
            	condition += " and ghh.id='"+htid+"'";
            }
            
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_HT2, page);
            // 合同表
            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");
            // 项目下达库
            // bs.setFieldTranslater("XDKID", "GC_TCJH_XMXDK", "ID", "XMMC");
            // 标段表
            // bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");

            // 设置字典
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldDic("BXQDWLX", "BXQDW");// 保修期单位：年、季、月、日
            bs.setFieldDic("QDNF", "XMNF");// 项目年份
            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同
            bs.setFieldDic("SFZFJTZ", "SF");// 是否支付即投资
            bs.setFieldDic("SFDXMHT", "SF");// 是否单项目
            bs.setFieldDic("GXLB", "GXLB");// 是否单项目
            bs.setFieldDic("JFDW", "JFDW");// 甲方单位

            // 设置查询条件
            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份

            bs.setFieldThousand("BZJ_JNJE");// 履约保证金
            bs.setFieldThousand("YHSJE");// 印花税金额(元)
            bs.setFieldThousand("BXJE");// 保修金额
            bs.setFieldThousand("ZHTQDJ");// 总合同签订价(元)
            bs.setFieldThousand("ZHTZF");// 总合同支付
            bs.setFieldThousand("ZWCZF");// 总完成投资
            bs.setFieldThousand("ZBGJE");// 总变更金额
            bs.setFieldThousand("ZHTJS");// 总合同结算
            bs.setFieldThousand("ZZXHTJ");// 总最新合同价
            bs.setFieldThousand("ZZXHTJDO");// 总最新合同价 计算出

            bs.setFieldSjbh("SJBH");

            long s1 = System.currentTimeMillis();
            domresult = bs.getJson();
            long s2 = System.currentTimeMillis();
            System.out.println((s2-s1)+"ms");
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同信息查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

    @Override
    public String queryBySJBH(String json, User user) throws Exception {

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

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_HT_LYBZJ, page);
            // 合同表
            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");
            // 项目下达库
            // bs.setFieldTranslater("XDKID", "GC_TCJH_XMXDK", "ID", "XMMC");
            // 标段表
            // bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");

            // 设置字典
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldDic("BXQDWLX", "BXQDW");// 保修期单位：年、季、月、日
            bs.setFieldDic("QDNF", "XMNF");// 项目年份
            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同
            bs.setFieldDic("BZJ_JNFS", "BZJ_JNFS");// 履约保证金缴纳方式

            bs.setFieldDic("GXLB", "GXLB");

            // 设置查询条件
            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份

            bs.setFieldThousand("YHSJE");// 印花税金额(元)
            bs.setFieldThousand("BXJE");// 保修金额
            bs.setFieldThousand("ZHTQDJ");// 总合同签订价(元)
            bs.setFieldThousand("ZHTZF");// 总合同支付
            bs.setFieldThousand("ZWCZF");// 总完成投资
            bs.setFieldThousand("ZBGJE");// 总变更金额
            bs.setFieldThousand("ZHTJS");// 总合同结算
            bs.setFieldThousand("ZZXHTJ");// 总最新合同价
            bs.setFieldThousand("ZZXHTJDO");// 总最新合同价 计算出

            bs.setFieldTranslater("JNDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");

            bs.setFieldSjbh("SJBH");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同信息查询失败!");
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
        GcHtglHtVO vo = new GcHtglHtVO();

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
                    ywid = new RandomGUID().toString();
                    vo.setId(ywid); // 主键
                }
            }

            String htqdj = "";
            if (map.get("htqdj") != null) {
                htqdj = (String) map.get("htqdj");
            }

            BusinessUtil.setInsertCommonFields(vo, user);
            vo.setYwlx(ywlx);

            EventVO eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);// 生成事件
            vo.setSjbh(eventVO.getSjbh());

            // 插入
            BaseDAO.insert(conn, vo);
            conn.commit();
            String condition = map.get("condition") == null ? "" : (String) map.get("condition");
            Pub.getFlowinf(conn, ywlx, vo.getSjbh(), user, condition);

            resultVO = vo.getRowJson();

            if (flagfiles) {
            	//--记录附件新增数据项
    			FileUploadVO fvo = new FileUploadVO();
    			fvo.setSjbh(vo.getSjbh());	//存入时间编号
				fvo.setYwlx(vo.getYwlx());	//存入时间编号
				fvo.setFjzt("1");
    			FileUploadService.updateVOByYwid(conn, fvo, ywid, user);
    			//--附件数据项更新完毕
            }

            // 增加标段信息

            String xmxxfrom = (String) map.get("xmxxfrom");
            if (xmxxfrom.equals("chosexm") && !"2".equals(vo.getSfdxmht())) {
                // 自主选择项目得来且不是无项目合同

                String jhsjids = (String) map.get("ids");
                String[] jhsjid = jhsjids.split(",");
                boolean flag = false;
                boolean flagsfdxmht = false;// 单项目合同
                if ("1".equals(vo.getSfdxmht()) || (jhsjid != null && jhsjid.length == 1)) {
                    flagsfdxmht = true;
                }
                for (int i = 0; i < jhsjid.length; i++) {
                    if (StringUtils.isNotBlank(jhsjid[i])) {
                        GcHtglHtsjVO xmvo = new GcHtglHtsjVO();
                        xmvo.setJhsjid(jhsjid[i]);
                        xmvo.setId(new RandomGUID().toString()); // 主键
                        xmvo.setHtid(ywid);
                        // xmvo.setXmid(xmid[i]);
                        // xmvo.setBdid(bdid[i]);
                        if (flagsfdxmht) {
                            xmvo.setHtqdj(vo.getZhtqdj());
                        }
                        BusinessUtil.setInsertCommonFields(xmvo, user);// 公共字段更新
                        xmvo.setYwlx(YwlxManager.GC_HTGL_HT_GC);// 业务类型
                        // 插入
                        BaseDAO.insert(conn, xmvo);

                        flag = true;
                    }
                }
                if (flag) {
                    if (flagsfdxmht) {
                        // 更新合同数据的项目及标段信息 根据jhsjid，单项目合同不更新合同签订价
                        DBUtil.executeUpdate(conn, UPDATE_XMID_BDID_FROM_JHSJID_DXMHT, new Object[] { vo.getId() });
                        conn.commit();
                    } else {
                        // 多项目合同， 更新合同签订价
                        DBUtil.executeUpdate(conn, UPDATE_XMID_BDID_FROM_JHSJID, new Object[] { vo.getId() });
                        conn.commit();
                    }

                }
                // DBUtil.executeUpdate(conn, SQL_UPDATE_HT_ZFXX, new Object[] {
                // vo.getId() });
            } else if (xmxxfrom.equals("choseztb")) {
                // 招投标处得来

                // 组织sql新增
                String insql = SQL_INSERINTO_FROM_ZTB.replaceAll("xxxxhtidxxxx", ywid);
                insql = insql.replaceAll("xxxxxztbidxxxxx", vo.getZtbid());

                insql = insql.replaceAll("xxxxxLRRxxxxx", user.getAccount());
                insql = insql.replaceAll("xxxxxLRBMxxxxx", user.getDepartment());
                insql = insql.replaceAll("xxxxxLRBMMCxxxxx", user.getOrgDept().getBmjc());
                System.out.println("insert:"+insql);
                // 施工合同,更新履约保证金信息
                if ("SG".equals(vo.getHtlx())) {
                    if (map.get("bzjId") != null) {
                        String bzjId = (String) map.get("bzjId");
                        String sql = " update gc_zjgl_lybzj set htid = '" + vo.getId() + "' where id = '" + bzjId + "'";
                        DBUtil.execUpdateSql(conn, sql);
                        conn.commit();
                    }
                }
                // 根据招投标插入合同数据信息
                if (StringUtils.isNotBlank(vo.getZtbid())) {
                    DBUtil.execUpdateSql(conn, insql);
                    conn.commit();
                }

            }
            conn.commit();

            // 如果是排迁类合同 则需新主合同ID至GC_PQ_ZXM ，表里有HTID
            String pqzxmid = map.get("pqzxmid") == null ? "" : (String) map.get("pqzxmid");
            if (StringUtils.isNotBlank(pqzxmid) && "PQ".equals(vo.getHtlx())) {
                DBUtil.executeUpdate(conn, UPDATE_GC_PQ_ZXM_HTID, new Object[] { vo.getId(), ((JSONObject) list.get(0)).get("HTSX"),
                        pqzxmid.trim() });
                conn.commit();
            }

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息新增成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同信息新增失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtVO vo = new GcHtglHtVO();

        try {
            String bdChange = map.get("bdChange") != null ? (String) map.get("bdChange") : "";

            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            vo.setValueFromJson((JSONObject) list.get(0));

            // 清理合同数据以及关联信息
            updateHtxxClear(vo.getId(), user, bdChange);

            String pqzxmid = (String) ((JSONObject) list.get(0)).get("GC_PQ_ZXM_ID");
            BusinessUtil.setUpdateCommonFields(vo, user);

            BaseDAO.update(conn, vo);
            resultVO = vo.getRowJson();
            conn.commit();

            boolean flagsfdxmht = false;// 单项目合同
            if ("1".equals(vo.getSfdxmht())) {
                flagsfdxmht = true;
            }
           
            // 增加标段信息

            String xmxxfrom = map.get("xmxxfrom") == null ? "" : (String) map.get("xmxxfrom");
            if (xmxxfrom.equals("chosexm")) {
                // 自主选择项目得来且不是无项目合同
                String jhsjids = (String) map.get("ids");

                String[] jhsjid = jhsjids.split(",");

                for (int i = 0; i < jhsjid.length; i++) {
                    if (StringUtils.isNotBlank(jhsjid[i])) {
                        GcHtglHtsjVO xmvo = new GcHtglHtsjVO();
                        xmvo.setJhsjid(jhsjid[i]);
                        xmvo.setId(new RandomGUID().toString()); // 主键
                        xmvo.setHtid(vo.getId());
                        if (flagsfdxmht) {
                            xmvo.setHtqdj(vo.getZhtqdj());
                        }
                        BusinessUtil.setInsertCommonFields(xmvo, user);// 公共字段更新
                        xmvo.setYwlx(YwlxManager.GC_HTGL_HT_GC);// 业务类型
                        // 插入
                        BaseDAO.insert(conn, xmvo);
                    }
                }
                if (flagsfdxmht) {
                    // 更新合同数据的项目及标段信息 根据jhsjid，单项目合同不更新合同签订价
                    DBUtil.executeUpdate(conn, UPDATE_XMID_BDID_FROM_JHSJID_DXMHT, new Object[] { vo.getId() });
                    conn.commit();
                } else {
                    // 多项目合同， 更新合同签订价
                    DBUtil.executeUpdate(conn, UPDATE_XMID_BDID_FROM_JHSJID, new Object[] { vo.getId() });
                    conn.commit();
                }
            } else if (xmxxfrom.equals("choseztb")&&"true".equals(bdChange)) {
                // 招投标处得来

                // 组织sql新增
                String insql = SQL_INSERINTO_FROM_ZTB.replaceAll("xxxxhtidxxxx", vo.getId());
                insql = insql.replaceAll("xxxxxztbidxxxxx", vo.getZtbid());

                insql = insql.replaceAll("xxxxxLRRxxxxx", user.getAccount());
                insql = insql.replaceAll("xxxxxLRBMxxxxx", user.getDepartment());
                insql = insql.replaceAll("xxxxxLRBMMCxxxxx", user.getOrgDept().getBmjc());
                System.out.println("update:"+insql);
                // 施工合同,更新履约保证金信息
                if ("SG".equals(vo.getHtlx())) {
                    if (map.get("bzjId") != null) {
                        String bzjId = (String) map.get("bzjId");
                        String sql = " update gc_zjgl_lybzj set htid = '" + vo.getId() + "' where id = '" + bzjId + "'";
                        DBUtil.execUpdateSql(conn, sql);
                        conn.commit();
                    }
                }
                // 根据招投标插入合同数据信息
                if (StringUtils.isNotBlank(vo.getZtbid())) {
                    DBUtil.execUpdateSql(conn, insql);
                    conn.commit();
                }

            }
            conn.commit();

            // 如果是排迁类合同 则需新主合同ID至GC_PQ_ZXM ，表里有HTID
            if (StringUtils.isNotBlank(pqzxmid) && "PQ".equals(vo.getHtlx())) {

                DBUtil.executeUpdate(conn, UPDATE_GC_PQ_ZXM_HTID, new Object[] { vo.getId(), ((JSONObject) list.get(0)).get("HTSX"),
                        pqzxmid.trim() });
                conn.commit();
            }

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息修改成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同信息修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    // 仅更新合同状态字段 不做其它的
    @Override
    public String update(String json, User user, String id, String opttype) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtVO vo = new GcHtglHtVO();
        try {
            conn.setAutoCommit(false);

            vo = findById(id);
            if ("cqsp".equals(opttype)) {
                vo.setHtzt("-2");
            } else if ("cqsptg".equals(opttype)) {
                vo.setHtzt("-1");
            } else if ("bmtjsp".equals(opttype)) {
                vo.setHtzt("0");

                // 合同提交到招标合同部，发送相关配置人员信息提醒。    add by xiahongbo by 2014-10-14
                PushMessage.push(conn, null, PushMessage.HT_TJHT, "合同【"+vo.getHtmc()+"】已提交到合同部门，请签订审核此合同。", "&htid="+id, vo.getSjbh(), vo.getYwlx(), "", "合同已提交，请查看");
            } else if ("htbqdlx".equals(opttype)) {
                vo.setHtzt("1");

                // 移过来的 update处
                // 配置回写乙方单位的sql语句
                String update_bd_yfdw = "";
                String update_xm_yfdw = "";
                if ("SG".equals(vo.getHtlx())) {
                    update_bd_yfdw = " update gc_xmbd set SGDW= ? ,SGDWFZR=(select wmsys.wm_concat(FZR) from gc_cjdw where gc_cjdw_id in (?)),SGDWFZRLXFS=(select wmsys.wm_concat(FZRDH) from gc_cjdw where gc_cjdw_id in (?)) where GC_XMBD_ID in ("
                            + SQL_QUERY_HT_BD + ")";
                    update_xm_yfdw = " update GC_TCJH_XMXDK set SGDW = ? ,FZR_SGDW=(select wmsys.wm_concat(FZR) from gc_cjdw where gc_cjdw_id in (?)),LXFS_SGDW=(select wmsys.wm_concat(FZRDH) from gc_cjdw where gc_cjdw_id in (?)) where GC_TCJH_XMXDK_ID in ("
                            + SQL_QUERY_HT_XM + ")";
                } else if ("JL".equals(vo.getHtlx())) {
                    update_bd_yfdw = " update gc_xmbd set JLDW= ? ,JLDWFZR=(select wmsys.wm_concat(FZR) from gc_cjdw where gc_cjdw_id in (?)),JLDWFZRLXFS=(select wmsys.wm_concat(FZRDH) from gc_cjdw where gc_cjdw_id in (?)) where GC_XMBD_ID in ("
                            + SQL_QUERY_HT_BD + ")";
                    update_xm_yfdw = " update GC_TCJH_XMXDK set JLDW = ? ,FZR_JLDW=(select wmsys.wm_concat(FZR) from gc_cjdw where gc_cjdw_id in (?)),LXFS_JLDW=(select wmsys.wm_concat(FZRDH) from gc_cjdw where gc_cjdw_id in (?)) where GC_TCJH_XMXDK_ID in ("
                            + SQL_QUERY_HT_XM + ")";
                } else if ("SJ".equals(vo.getHtlx())) {
                    update_bd_yfdw = " update gc_xmbd set SJDW= ? ,SJDWFZR=(select wmsys.wm_concat(FZR) from gc_cjdw where gc_cjdw_id in (?)),SJDWFZRLXFS=(select wmsys.wm_concat(FZRDH) from gc_cjdw where gc_cjdw_id in (?)) where GC_XMBD_ID in ("
                            + SQL_QUERY_HT_BD + ")";
                    update_xm_yfdw = " update GC_TCJH_XMXDK set SJDW = ? ,FZR_SJDW=(select wmsys.wm_concat(FZR) from gc_cjdw where gc_cjdw_id in (?)),LXFS_SJDW=(select wmsys.wm_concat(FZRDH) from gc_cjdw where gc_cjdw_id in (?)) where GC_TCJH_XMXDK_ID in ("
                            + SQL_QUERY_HT_XM + ")";
                }else if("PQ".equals(vo.getHtlx())){
                	//招标合同部合同审核时，检查是否有审定值，如果有审定值，更改合同状态为“已结算”
                	String sql_update_pq = " update gc_htgl_ht g set g.htzt='-2' where g.id=? and (select sdz from gc_pq_zxm p where p.htid = g.id and p.htsx='2') is not null";
                	DBUtil.executeUpdate(conn, sql_update_pq, new String[]{vo.getId()});
                	conn.commit();
                }
                // 回写项目/标段的乙方单位
                if (!"".equals(update_bd_yfdw) && !"".equals(update_xm_yfdw)) {
                    // 乙方单位
                    String dwmc = "";
                    String dwmcWhere = "";
                    dwmc += vo.getYfid();
                    dwmcWhere += "'" + vo.getYfid() + "'";
                    if (vo.getYf2id() != null && !"".equals(vo.getYf2id())) {
                        dwmc += "," + vo.getYf2id();
                        dwmcWhere += "'" + vo.getYf2id() + "'";
                    }
                    if (vo.getYf3id() != null && !"".equals(vo.getYf3id())) {
                        dwmc += "," + vo.getYf3id();
                        dwmcWhere += "'" + vo.getYf3id() + "'";
                    }

                    DBUtil.executeUpdate(conn, update_bd_yfdw, new Object[] { dwmc, dwmcWhere, dwmcWhere, vo.getId() });
                    DBUtil.executeUpdate(conn, update_xm_yfdw, new Object[] { dwmc, dwmcWhere, dwmcWhere, vo.getId() });
                    conn.commit();
                }

                // 合同已审核，发送录入人员信息提醒。    add by xiahongbo by 2014-10-14
                String lrr = vo.getLrr();
                String[] person = new String[1];
                person[0] = lrr;
                PushMessage.push(conn, null, PushMessage.HT_QDSH, "合同【"+vo.getHtmc()+"】已签订审核，请查看。", "url", vo.getSjbh(), vo.getYwlx(), "", person, "合同已签订审核，请查看");

            } else if ("htjs".equals(opttype)) {
                //合同结束
                vo.setHtzt("3");
                vo.setHtjsrq(new Date());//实际结束日期
                BusinessUtil.setUpdateCommonFields(vo, user);
            }else if ("htzz".equals(opttype)) {
                //合同中止
                vo.setHtzt("4");
                
                GcHtglHtVO tmp = new GcHtglHtVO();
                
                JSONArray list = tmp.doInitJson(json);
                tmp.setValueFromJson((JSONObject) list.get(0));
                String HTZZLY=tmp.getHtzzly(); //HTZZLY 合同中止理由
                vo.setHtzzly(HTZZLY);
                
                BusinessUtil.setUpdateCommonFields(vo, user);
            }

            // 插入
            BaseDAO.update(conn, vo);
            resultVO = vo.getRowJson();
            conn.commit();

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息修改成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + vo.getId()
                    + "\",\"fieldname\":\"ghh.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同信息修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String delete(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVo = null;
        GcHtglHtVO vo = new GcHtglHtVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            
            String sql = "select sjzt from fs_event where sjbh='"+vo.getSjbh()+"'";
            String[][] rs = DBUtil.query(conn, sql);
            String sjzt = rs == null ? "0" : rs[0][0];

            // 新建状态的合同才可以删除
            if (StringUtils.isNotBlank(vo.getHtzt()) && ("-3".equals(vo.getHtzt()) || "7".equals(sjzt))) {

                Object[] objs = new Object[] { vo.getId() };
                DBUtil.executeUpdate(conn, DEL_SQL_FS_FILEUPLOAD, objs);
                DBUtil.executeUpdate(conn, DEL_SQL_GC_HTGL_HT_HTZF, objs);
                DBUtil.executeUpdate(conn, DEL_SQL_GC_HTGL_HT_WCTZ, objs);
                DBUtil.executeUpdate(conn, DEL_SQL_GC_HTGL_HT_HTBG, objs);
                DBUtil.executeUpdate(conn, DEL_SQL_GC_HTGL_HTSJ, objs);
                DBUtil.executeUpdate(conn, DEL_SQL_GC_PQ_ZXM, objs);
                DBUtil.executeUpdate(conn, DEL_SQL_GC_ZJGL_LYBZJ, objs);

                BaseDAO.delete(conn, vo);

                resultVo = vo.getRowJson();
                conn.commit();
            }

            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息删除成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同信息删除失败!");
            LogManager.writeUserLog(user.getAccount(), ywlx, Globals.OPERATION_TYPE_DELETE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息删除失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVo;

    }

    @Override
    public String queryOther(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            String orderFilter = RequestUtil.getOrderFilter(json);
            // condition += BusinessUtil.getSJYXCondition("t");
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            BaseResultSet bs = null;

            String opttype = map.get("opttype") == null ? "" : (String) map.get("opttype");
            if (opttype.equals("yfdw")) {
                // 工程部查询页面. 需关联GC_ZJB_JSB(结算表) 提报日期, GC_SJGL_JJG(交竣工表) 竣工验收时间

                bs = DBUtil.query(conn, SQL_QUERY_DWXX_HT, page);
                int count = bs.getPage().getCountRows();
                if (count == 0) {
                    bs = DBUtil.query(conn, SQL_QUERY_DWXX_ZTB, page);
                }
            } else if (opttype.equals("ybf")) {
                String htid = map.get("htid") == null ? "" : (String) map.get("htid");

                bs = DBUtil.query(conn, SQL_QUERY_YBF, page);

            } else if (opttype.equals("bywctz")) {
                // 合同支付>新增 本月完成投资
                // String htid = map.get("htid") == null ? "" : (String)
                // map.get("htid");
                // String[] idStrings = StringUtils.split(htid, ",");

                bs = DBUtil.query(conn, SQL_QUERY_BYWCTZ, page);
            }

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

    @Override
    public String updateHtztToYJS(String json, User user, String id) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtVO vo = new GcHtglHtVO();
        try {
            conn.setAutoCommit(false);

            vo = findById(id);

            DBUtil.executeUpdate(SQL_UPDATE_HTZT_YJS, new String[] { vo.getId(), vo.getId() });
            vo.setHtzt("2");

            // 插入
            BaseDAO.update(conn, vo);
            resultVO = vo.getRowJson();
            conn.commit();

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同结算状态修改成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + vo.getId()
                    + "\",\"fieldname\":\"ghh.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同结算状态修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同结算状态修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String updateHtZHSJS(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtVO ht = null;
        try {
            String htId = (String) map.get("htId");
            ht = findById(htId);
            conn.setAutoCommit(false);
            DBUtil.executeUpdate(SQL_UPDATE_HT_ZFXX, new Object[] { ht.getId() });
            conn.commit();
            LogManager.writeUserLog(ht.getSjbh(), ht.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + ht.getId()
                    + "\",\"fieldname\":\"ghh2.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同信息修改失败!");
            LogManager.writeUserLog(ht.getSjbh(), ht.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;
    }

    @Override
    public String queryHtzjxx(String json, User user) throws Exception {
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
            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_HTZJXX, page);

            bs.setFieldThousand("CSSDZ");// 拦标价
            bs.setFieldThousand("ZZBJ");// 中标价
            bs.setFieldThousand("ZZXHTJ");// 最新合同价
            bs.setFieldThousand("ZHTJS");// 合同结算价

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同造价信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同造价信息查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }
    
    @Override
    public String queryHtOnlyTqkbm(String json, User user) throws Exception {
    	 Connection conn = DBUtil.getConnection();
         String domresult = "";
         try {

             PageManager page = RequestUtil.getPageManager(json);
             String condition = RequestUtil.getConditionList(json).getConditionWhere();
             
             condition += " and (ghh.htzt = '1' or (ghh.htzt = '2' and ghh2.htzf < ghh2.zxhtj)) ";
             
             String orderFilter = RequestUtil.getOrderFilter(json);

             if (!StringUtils.isBlank(condition)) {
             }
             condition += BusinessUtil.getSJYXCondition("ghh");
             condition += BusinessUtil.getCommonCondition(user, "ghh");
             condition += orderFilter;

             if (page == null)
                 page = new PageManager();
             page.setFilter(condition);

             conn.setAutoCommit(false);
             // String sql =
             // "SELECT ghh.ID AS htid, ghh2.id as htsjid, ghh2.xmbh,ghh2.bdid, ghh.htmc, ghh.htbm, ghh.yfdw, ghh2.htqdj, ghh.htjqdrq, gjs.xmmc, gjs.bdmc"
             // +
             // "  FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2 ON ghh.ID=ghh2.htid LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid ";

             BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_HT_LIST, page);

             bs.setFieldDic("HTLX", "HTLX");
             bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
             bs.setFieldDic("FBFS", "FBFS");// 发包方式
             bs.setFieldDic("QDNF", "XMNF");// 项目年份
             bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同

             // 日期
             bs.setFieldDateFormat("HTJQDRQ", "yyyy-MM-dd");

             bs.setFieldThousand("ZHTQDJ");// 总合同签订价(元)
             bs.setFieldThousand("HTQDJ");// 合同签订价(元)
             bs.setFieldThousand("HTZF");// 标段合同支付(元)
             bs.setFieldThousand("WCZF");// 标段完成支付(元)

             bs.setFieldTranslater("YFID", "GC_CJDW", "GC_CJDW_ID", "DWMC");

             domresult = bs.getJson();
         } catch (Exception e) {
             e.printStackTrace(System.out);
         } finally {
             DBUtil.closeConnetion(conn);
         }
         return domresult;
    }

    @Override
    public String updateHtjs(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtVO vo = new GcHtglHtVO();

        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            vo.setValueFromJson((JSONObject) list.get(0));
            String pqzxmid = (String) ((JSONObject) list.get(0)).get("GC_PQ_ZXM_ID");
            BusinessUtil.setUpdateCommonFields(vo, user);
            vo.setHtzt("2");
            vo.setZzxhtj(vo.getZhtjs());
            BaseDAO.update(conn, vo);
            resultVO = vo.getRowJson();
            conn.commit();

            vo = findById(vo.getId());
            
            boolean flagsfdxmht = false;// 单项目合同
            if ("1".equals(vo.getSfdxmht())) {
                flagsfdxmht = true;
            }
            if (flagsfdxmht) {
                DBUtil.executeUpdate(conn, UPDATE_DXMHT_HTSJ_HTQDJ, new Object[] { vo.getId() });
                conn.commit();
                
                DBUtil.executeUpdate(conn, UPDATE_DXMHT_HTSJ_HTJS, new Object[] { vo.getZhtjs(),vo.getZhtjs(),vo.getZhtjs(),vo.getId() });
                conn.commit();
            } else {
                // 如没有入口从其它地方直接更新htsj里面的htjs字段, 需要根据标段的价格页分比自动计算
                // TODO 这里需要完成
            }

            // 如果是排迁类合同 则需新主合同ID至GC_PQ_ZXM ，表里有HTID

            if (StringUtils.isNotBlank(pqzxmid) && "PQ".equals(vo.getHtlx())) {
                DBUtil.executeUpdate(conn, UPDATE_GC_PQ_ZXM_HTID, new Object[] { vo.getId(), pqzxmid.trim() });
                conn.commit();
            }
            if("PQ".equals(vo.getHtlx())){
            	String sql = "select p.htsx,decode(p.sdz,null,'',p.sdz) from gc_pq_zxm p where p.htid = ?";
            	String[][] results = DBUtil.querySql(conn, sql, new String[]{vo.getId()});
            	if(results!=null){
            		if("1".equals(results[0][0])&&"".equals(results[0][2])){
            			sql = "update gc_pq_zxm p set p.sdz = '"+vo.getZzxhtj()+"' where p.htid = '"+vo.getId()+"'";
            			DBUtil.exec(conn, sql);
            			conn.commit();
            		}
            	}
            }
            

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息修改成功", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同信息修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String updateHtbh(String json, User user, HttpServletRequest request) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtVO vo = new GcHtglHtVO();
        GcHtglHtVO vo_ = new GcHtglHtVO();

        String stepindex = request.getParameter("stepindex");
        String oid = request.getParameter("oid");
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            vo.setValueFromJson((JSONObject) list.get(0));
            vo_.setId(vo.getId());
            vo_.setHtbm(vo.getHtbm());
            vo_.setJfdw(vo.getJfdw());
            vo_.setJfid(vo.getJfid());

            BaseDAO.update(conn, vo_);
            // 将合同业务中的合同编码，同步到文书中
            if ("43485".equals(oid)) {// 43485表示的是新的合同会签单，wsid为28，会签单会签顺序改变了
                if("11".equals(stepindex)) {// 第十一步骤是合同编码
                    DBUtil.exec(conn, "update ap_process_ws set DOMAIN_VALUE='" + vo.getHtbm()
                            + "' where WS_TEMPLATE_ID='28' and FIELDNAME='Fld464' and SJBH ='" + vo.getSjbh() + "'");
                }
			} else if("43463".equals(oid)) {// 43463表示的是老的合同会签单，wsid为5
				if("3".equals(stepindex)) {// 第三步骤是合同编码
                    DBUtil.exec(conn, "update ap_process_ws set DOMAIN_VALUE='" + vo.getHtbm()
                            + "' where WS_TEMPLATE_ID='5' and FIELDNAME='Fld464' and SJBH ='" + vo.getSjbh() + "'");
                }
			}/* else if("2".equals(stepindex)) {// 第二步骤是落实核算主题（甲方单位）
                DBUtil.exec(conn, "update ap_process_ws set DOMAIN_VALUE='" + vo.getJfid()
                        + "' where WS_TEMPLATE_ID='5' and FIELDNAME='Fld467' and SJBH ='" + vo.getSjbh() + "' and lrsj is not null");

                String id = new RandomGUID().toString();
                String insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM,SPYLB)"+
              		  	" values('"+id+"','5','5','','"+vo.getJfdw()+"','','Fld467','','"+stepindex+"','1','','"+vo.getSjbh()+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"','') ";
                DBUtil.execUpdateSql(conn, insertSql);
            }*/    /* 不在这里处理落实核算主题了，在TaskBo.java里面hthqd方法处理 add by xiahb on 2014-4-3 */
            resultVO = vo.getRowJson();
            conn.commit();

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息修改成功", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同信息修改失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同信息修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String queryHtList(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // String cYear = Pub.getDate("yyyy");
            String sqlString = "";
            String cYear = map.get("nd") == null ? "" : (String) map.get("nd");
            if (StringUtils.isNotBlank(cYear)) {
                // 组织当前年度数据查询
                sqlString = SQL_QUERY_QYLIST.replaceAll("xxxhtndxxx", cYear);
            } else {
                cYear = Pub.getDate("yyyy");
                sqlString = SQL_QUERY_QYLIST.replaceAll("xxxhtndxxx", cYear);
            }

            // String preYear = String.valueOf(Integer.parseInt(cYear) - 1);

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            QueryConditionList list = RequestUtil.getConditionList(json);

            conn.setAutoCommit(false);

            // String sql = SQL_QUERY_QYLIST.replaceAll("xxxxpreYearxxx",
            // preYear);
            // sql = sql.replaceAll("xxxxcYearxxx", cYear);

            BaseResultSet bs = DBUtil.query(conn, sqlString, page);

            bs.setFieldDic("HTLX", "HTLX");

            bs.setFieldThousand("ZZHTQDJ");
            bs.setFieldThousand("ZZWCZF");
            bs.setFieldThousand("ZZHTZF");
            bs.setFieldThousand("ZXSJSY");
            bs.setFieldThousand("NDZHTQD");
            bs.setFieldThousand("NDZHTTZ");
            bs.setFieldThousand("NDZHTZF");

            // 设置字典
            // bs.setFieldDic("BGLX", "BGLX");
            // bs.setFieldDic("HTLX", "HTLX");
            // bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            // bs.setFieldDic("FBFS", "FBFS");// 发包方式
            // bs.setFieldDic("WCTZLX", "ZFYT");// 完成投资类型==支付用途

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同信息>", user, "", "");
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
    public String updateHtHtqjjs(User user, String htid, String htsjId, HashMap map) throws Exception {
        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtVO ht = null;
        boolean flag = false; // 记录htsjId参数传入时是否为空
        try {
            String htjsj = (String) map.get("htjsj");
            String update_htsj = "update gc_htgl_htsj set   htjs='"+htjsj+"',HTJSJ = '" + htjsj + "' where id = ?";
            String update_ht = "update gc_htgl_ht set  ZZXHTJ='"+htjsj+"', ZHTJS = '" + htjsj + "' where id = ? ";
            String update_htzt = "update gc_htgl_ht set  htzt='2' where id = ? and htzt=1 ";
            // 是否存在合同签订价
            if (map.get("htqdj") != null && !"".equals((String) map.get("htqdj"))) {
                update_htsj = "update gc_htgl_htsj set HTQDJ = '" + ((String) map.get("htqdj")) + "' ,htjs='"+htjsj+"', HTJSJ = '" + htjsj
                        + "' where  id = ?";
                update_ht = "update gc_htgl_ht set ZHTQDJ = '" + (String) map.get("htqdj") + "' , ZHTJS = '" + htjsj + "' where id = ? ";
            }

            String[][] results = null;
            // 根据合同数据ID是否为空 获取ht-htsj的列表
            if (StringUtils.isBlank(htsjId)) {
                results = DBUtil
                        .query(" select ghh.id,ghh2.htlx from gc_htgl_htsj ghh left join gc_htgl_ht ghh2 on ghh.htid = ghh2.id where ghh.htid = '"
                                + htid + "'");
                htsjId = results[0][0];
            } else {
                results = DBUtil
                        .query(" select ghh.id,ghh2.htlx,ghh2.id htid from gc_htgl_htsj ghh left join gc_htgl_ht ghh2 on ghh.htid = ghh2.id where ghh.htid = "
                                + "(select ghh.htid from gc_htgl_htsj ghh left join gc_htgl_ht ghh2 on ghh.htid = ghh2.id where ghh.id = '"
                                + htsjId + "')");
                htid = results[0][2];
                flag = true; // htsjId参数传入时不为空
            }

            conn.setAutoCommit(false);
            // 是否只有一条合同数据信息
            if (results.length == 1) {
            	//如果是单项目合同，则同时更新htjs的结算价和最新合同价
                // 判断是否是排迁合同
                //if ("PQ".equals(results[0][1])) {
                update_htsj = "update gc_htgl_htsj set ZXHTJ = '" + htjsj + "' ,htjs='"+htjsj+"', HTJSJ = '" + htjsj
                            + "' where id = ?";
                update_ht = "update gc_htgl_ht set  ZHTJS = '" + htjsj + "',zzxhtj='" + htjsj + "' where id = ?";

                //}
                // 更新合同的合同结算价和合同签订价
                DBUtil.executeUpdate(update_ht, new String[] { htid });
                // 更新合同的合同状态
                DBUtil.executeUpdate(update_htzt, new String[] { htid });
                // 更新合同数据的合同结算价和合同签订价
                DBUtil.executeUpdate(update_htsj, new String[] { htsjId });
                

            } else {
                // 如果合同数据集合不唯一,根据传入htid 或者 htsjId更新 两张表
                if (flag){
                    // 更新合同的合同结算价和合同签订价
                    DBUtil.executeUpdate(update_htsj, new String[] { htsjId });
                }else{
                    // 更新合同的合同结算价和合同签订价
                    DBUtil.executeUpdate(update_ht, new String[] { htid });
	                // 更新合同的合同状态
	                DBUtil.executeUpdate(update_htzt, new String[] { htid });
                }
            }

            conn.commit();

            ht = findById(htid);

            resultVO = ht.getRowJson();

            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "合同签订价、合同结算价修改成功", user, "", "");
            String jsona = "{querycondition: {conditions: [{\"value\":\"" + htid
                    + "\",\"fieldname\":\"ghh2.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryCondition(jsona, user);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同信息修改失败!");
            LogManager.writeUserLog(ht.getSjbh(), ht.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同签订价、合同结算价修改成功修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;
    }

    @Override
    public String queryTzjkHtList(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            String orderFilter = RequestUtil.getOrderFilter(json);
            // condition += BusinessUtil.getSJYXCondition("t");
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_TZJK, page);

            bs.setFieldDic("HTLX", "HTLX");

            bs.setFieldThousand("ZHTQDJ");
            bs.setFieldThousand("ZZHTQDJ");
            bs.setFieldThousand("ZZWCZF");
            bs.setFieldThousand("ZZHTZF");
            bs.setFieldThousand("ZZHTJS");

            domresult = bs.getJson();

            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<投资控制动态分析>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("投资控制动态分析查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public List<autocomplete> htbmAutoQuery(autocomplete json, User user, HashMap map) throws Exception {

        List<autocomplete> autoResult = new ArrayList<autocomplete>();
        autocomplete ac = new autocomplete();
        String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
        // String htbm = (String) map.get("htbm");
        // if (StringUtils.isNotBlank(htbm)) {
        // condition += " AND T9.XMGLGS = '" + htbm + "' ";
        // }

        condition += BusinessUtil.getSJYXCondition("");
        condition += BusinessUtil.getCommonCondition(user, "");

        String[][] result = DBUtil.query("SELECT htbm, id FROM gc_htgl_ht  " + json.getTablePrefix() + " WHERE htbm IS NOT NULL and "
                + condition);
        if (null != result) {
            for (int i = 0; i < result.length; i++) {
                ac = new autocomplete();
                ac.setRegionName(result[i][0]);
                // ac.setRegionCode(result[i][1]);
                autoResult.add(ac);
            }
        }
        return autoResult;
    }

    public String querySj(String json, User user) throws Exception {

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

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERYONE_HT, page);
            // 合同表
            // bs.setFieldTranslater("HTID", "合同表", "ID", "NAME");
            // 项目下达库
            // bs.setFieldTranslater("XDKID", "GC_TCJH_XMXDK", "ID", "XMMC");
            // 标段表
            // bs.setFieldTranslater("BDID", "GC_XMBD", "GC_XMBD_ID", "BDMC");

            // 设置字典
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldDic("BXQDWLX", "BXQDW");// 保修期单位：年、季、月、日
            bs.setFieldDic("QDNF", "XMNF");// 项目年份
            bs.setFieldDic("SFXNHT", "SF");// 是否虚拟合同
            bs.setFieldDic("SFZFJTZ", "SF");// 是否支付即投资
            bs.setFieldDic("SFDXMHT", "SF");// 是否单项目
            bs.setFieldDic("GXLB", "GXLB");// 是否单项目

            // 设置查询条件
            // bs.setFieldDateFormat("JLRQ", "yyyy-MM");// 计量月份

            bs.setFieldThousand("YHSJE");// 印花税金额(元)
            bs.setFieldThousand("BXJE");// 保修金额
            bs.setFieldThousand("ZHTQDJ");// 总合同签订价(元)
            bs.setFieldThousand("ZHTZF");// 总合同支付
            bs.setFieldThousand("ZWCZF");// 总完成投资
            bs.setFieldThousand("ZBGJE");// 总变更金额
            bs.setFieldThousand("ZHTJS");// 总合同结算
            bs.setFieldThousand("ZZXHTJ");// 总最新合同价
            bs.setFieldThousand("ZZXHTJDO");// 总最新合同价 计算出
            bs.setFieldThousand("TEST1");// 总最新合同价 计算出

            bs.setFieldSjbh("SJBH");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<合同信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("合同信息查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

    @Override
    public String queryZtbSjrws(User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {
            String sql = "";
            // 组织查询条件
            if (map.get("ztbId") != null) {
                String ztbId = (String) map.get("ztbId");
                sql = SQL_QUERY_SJRWS_BYZTB;
                sql = sql.replace("xxxxxztbidxxxxx", ztbId);
            } else if (map.get("jhsjids") != null) {
                String jhsjids = (String) map.get("jhsjids");
                sql = SQL_QUERY_SJRWS_BYJHSJID;
                sql = sql.replace("xxxxjhsjidsxxxx", jhsjids.substring(0, jhsjids.length() - 1));
            }

            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, sql, null);

            bs.setFieldThousand("JE");

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<设计任务书信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("设计任务书信息查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }

    @Override
    public String queryLybzjList(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            condition += " and v.jhsjid in (SELECT gzj.jhsjid FROM gc_ztb_sj gzs, gc_ztb_xqsj_ys gzxy, gc_ztb_jhsj gzj, gc_jh_sj gjs WHERE gzs.gc_ztb_sj_id=gzxy.ztbsjid  AND gzxy.ztbxqid=gzj.xqid AND gjs.gc_jh_sj_id=gzj.jhsjid AND gzs.gc_ztb_sj_id='"
                    + (String) map.get("ztbId") + "') ";
            String orderFilter = RequestUtil.getOrderFilter(json);
            // condition += BusinessUtil.getSJYXCondition("t");
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_LYBZJ, page);

            bs.setFieldDic("JNFS", "JNFS");

            bs.setFieldThousand("JE");

            domresult = bs.getJson();

            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<履约保证金信息>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("履约保证金信息查询失败!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;
    }

    @Override
    public String updateHtxxClear(String id, User user, String bdChange) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtVO vo = new GcHtglHtVO();

        try {
        	if(bdChange.equals("")){
        		return resultVO;
        	}
        	
            conn.setAutoCommit(false);
            vo = findById(id);
            
            if ("true".equals(bdChange) || "2".equals(vo.getSfdxmht())) {
                // 如果项目有变动或是无项目合同删除所有合同数据信息
                String del_htsj_sql = "delete from gc_htgl_htsj where htid=? ";
                DBUtil.executeUpdate(conn, del_htsj_sql, new Object[] { vo.getId() });
                conn.commit();
            }

            // 移除合同对排迁子任务关联关系
            String update_pq_clear_sql = "update GC_PQ_ZXM SET htid = '',htsx='' WHERE htid=?";
            DBUtil.executeUpdate(conn, update_pq_clear_sql, new Object[] { vo.getId() });
            conn.commit();

            // 移除合同对履约保证金关联
            String update_lybzj_clear_sql = "update gc_zjgl_lybzj set htid = '' where htid = '" + vo.getId() + "'";
            DBUtil.execUpdateSql(conn, update_lybzj_clear_sql);
            conn.commit();

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同数据清理成功(合同修改前准备动作)", user, "", "");

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同数据清理失败!");
            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同数据清理失败(合同修改前准备动作)", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }
    
    public String queryIsCf(User user, HttpServletRequest request) throws Exception {
    	Connection conn = null;
    	JSONObject json = new JSONObject();
    	try {
			conn = DBUtil.getConnection();
			
			String htidSql = "".equals(request.getParameter("htid")) ? "" : "and a.id <> '"+request.getParameter("htid")+"' ";
			String ids = request.getParameter("ids");
			String[] jhsjids = ids.split(",");

			for (String jhsjid : jhsjids) {
				String sql = "select count(*) cnt, decode(sj.xmbs,'0',sj.xmmc,'1',sj.bdmc) xmmc " 
						+ "from gc_htgl_ht a, gc_htgl_htsj b,gc_jh_sj sj " 
						+ "where a.id = b.htid and a.htlx = 'SG' " 
						+ "and b.jhsjid = sj.gc_jh_sj_id " 
						+ "and b.jhsjid = '"+jhsjid+"' " 
						+ htidSql
						+ "group by decode(sj.xmbs,'0',sj.xmmc,'1',sj.bdmc) ";
				
				String[][] rs = DBUtil.query(conn, sql);
				
				if(rs != null && !"0".equals(rs[0][0])) {
					json.put("isDouble", "1");
					json.put("xmmc", rs[0][1]);
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
    	return json.toString();
    }
    
    /**
     * 对合同主体补存招投标信息
     * @param request
     * @param json
     * @return
     * @throws Exception
     */
    public String bcZtbxx(HttpServletRequest request) throws Exception {
    	Connection conn = null;
    	JSONObject obj = null;
    	try {
    		conn = DBUtil.getConnection();
    		obj = new JSONObject();
    		conn.setAutoCommit(false);
    		
			String htid = request.getParameter("htid");
			String zbsjid = request.getParameter("zbsjid");
			
			String htSql = "select htsj.jhsjid from gc_htgl_htsj htsj where htsj.htid='"+htid+"'";
			String zbSql = "select b.jhsjid from GC_ZTB_JHSJ b where ZBID = '"+zbsjid+"'";
			
			String[][] htRs = DBUtil.query(conn, htSql);
			String[][] zbRs = DBUtil.query(conn, zbSql);
			
			int xiangtongCnt = 0;
			
			int htxmCnt = htRs == null ? 0 : htRs.length;
			int ztxmCnt = zbRs == null ? 0 : zbRs.length;
			
			String updateHtSql = "update gc_htgl_ht ht set ht.ztbid='"+zbsjid+"' where ht.id='"+htid+"'";
			// 当合同和招标没有项目的时候。
			if(htxmCnt == 0 && ztxmCnt == 0) {
				obj.put("msg", "回写招标信息成功!");
				obj.put("error", "0");
				
				DBUtil.execSql(conn, updateHtSql);
			} else if(htxmCnt == ztxmCnt) {
				for (int i = 0; i < htxmCnt; i++) {
					System.out.println(htRs[i][0]);
					for (int j = 0; j < ztxmCnt; j++) {
						if(htRs[i][0].equals(zbRs[j][0])) {
							xiangtongCnt++;
							continue;
						}
						System.out.println(zbRs[j][0]);
					}
				}

				// 当合同项目和招标项目一致时
				if(htxmCnt == xiangtongCnt) {
					obj.put("msg", "回写招标信息成功。");
					obj.put("error", "0");
					
					DBUtil.execSql(conn, updateHtSql);
				} else {
					obj.put("msg", "合同项目和招标项目不一致");
					obj.put("error", "1");
				}
			} else {
				
				if(htxmCnt == 0){//如果合同项目数为0，使用招投标项目
					 // 组织sql新增
	                String insql = SQL_INSERINTO_FROM_ZTB.replaceAll("xxxxhtidxxxx", htid);
	                insql = insql.replaceAll("xxxxxztbidxxxxx", zbsjid);
	                // 根据招投标插入合同数据信息
	                if (StringUtils.isNotBlank(zbsjid)) {
	                    DBUtil.execUpdateSql(conn, insql);
	                    //conn.commit();
	                }
	                DBUtil.execSql(conn, updateHtSql);
				}else{
					obj.put("msg", "合同内项目数量和招标内项目数量不一致");
					obj.put("error", "1");
				}
			}
			
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
    	return obj.toString();
    }
    
    @Override
    public void deleteHTxm(String json, User user, HttpServletRequest request) throws Exception {
        Connection conn = DBUtil.getConnection();
        String htid = request.getParameter("htid");
        try {
            conn.setAutoCommit(false);
            Object [] paras = new Object[1];
            paras[0] = htid;
            DBUtil.executeUpdate(conn, DEL_SQL_GC_HTGL_HTSJ, paras);
            conn.commit();

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
    }
    
}
