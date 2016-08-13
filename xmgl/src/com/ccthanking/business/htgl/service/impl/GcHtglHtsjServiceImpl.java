/* ==================================================================
 * 版权：    kcit 版权所有 (c) 2013
 * 文件：    htgl.service.GcHtglHtsjService.java
 * 创建日期： 2013-09-02 上午 08:02:33
 * 功能：    接口：合同数据
 * 所含类:   {包含的类}
 * 修改记录：

 * 日期                        作者                内容
 * ==================================================================
 * 2013-09-02 上午 08:02:33  蒋根亮   创建文件，实现基本功能
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


import com.ccthanking.business.htgl.service.GcHtglHtsjService;
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
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.service.impl.Base1ServiceImpl;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * <p>
 * GcHtglHtsjService.java
 * </p>
 * <p>
 * 功能：合同数据
 * </p>
 * 
 * <p>
 * <a href="GcHtglHtsjService.java.html"><i>查看源代码</i></a>
 * </p>
 * 
 * @author <a href="mailto:jianggl88@163.com">蒋根亮</a>
 * @version 0.1
 * @since 2013-09-02
 * 
 */

@Service
public class GcHtglHtsjServiceImpl extends Base1ServiceImpl<GcHtglHtsjVO, String> implements GcHtglHtsjService {

    private static Logger logger = LoggerFactory.getLogger(GcHtglHtsjServiceImpl.class);

    // // 合同服务接口
    // @Autowired
    // private GcHtglHtService gcHtglHtService;

    // 查询单合同的合同数据.
    private static String SQL_QUERY_ONE = "SELECT ghh.htmc, ghh.htbm, ghh.zhtqdj, ghh.zhtzf, ghh.zwczf, ghh.zbgje, "
            + "gjs.gc_jh_sj_id,gjs.gc_jh_sj_id AS jhsjid, gjs.xmid, gjs.xmmc, gjs.nd, gjs.bdid, gjs.bdmc, ghh.yfdw, ghh.htjqdrq, decode(gjs.bdid,'',gt1.xmdz,gt2.bddd) xmdz,"
            + "ghh.htlx, ghh.htzt, ghh.fbfs, ghh2.id, ghh2.id as htsjid , ghh2.htid, nvl(ghh2.htqdj,0) htqdj, gt1.ISBT, gt1.XMLX ,gt1.XMBH, gt1.xmsx,"
            + "nvl(round(100*ratio_to_report(ghh2.htqdj) over(partition by ghh2.htid),2),0) as gcjgbfb, nvl(ghh2.htjsj,0) htjsj, ghh2.mblx, ghh2.mbid, ghh2.je, ghh2.mbsjlx, ghh2.rq, ghh2.mc, ghh2.bgts, ghh2.htdid, gt2.bdbh,"
            + "ghh2.ywlx, ghh2.sjbh, ghh2.sjmj, ghh2.sfyx, ghh2.bz, ghh2.lrr, ghh2.lrsj, ghh2.lrbm, ghh2.lrbmmc, (SELECT DECODE( ssz, NULL, 0, ssz) FROM GC_PQ_ZXM pq WHERE  ghh2.htid = pq.htid) AS ssz, (SELECT DECODE( sdz, NULL, 0, sdz) FROM GC_PQ_ZXM pq WHERE  ghh2.htid = pq.htid) AS sdz,(SELECT htsx FROM GC_PQ_ZXM pq WHERE  ghh2.htid = pq.htid) AS htsx, "
            + "ghh2.gxr, ghh2.gxsj, ghh2.gxbm, ghh2.gxbmmc, ghh2.sortno, nvl(ghh2.bgje,0) bgje, nvl(ghh2.zxhtj,0) zxhtj, nvl(ghh2.wczf,0) wczf, nvl(ghh2.htzf,0) htzf,decode(ghh2.htqdj*100,0,0,NVL(ROUND(ghh2.htzf/ghh2.htqdj*100,2), 0))  AS ljyzfb,decode(ghh2.htqdj*100,0,0,NVL(ROUND(ghh2.wczf/ghh2.htqdj*100,2), 0))  AS ljwczfb,(SELECT SUM(GCYFK) FROM gc_htgl_ht_htzf z WHERE z.htsjid=ghh2.id) AS GCYFK,    (SELECT SUM(KGZYFK) FROM gc_htgl_ht_htzf z WHERE z.htsjid=ghh2.id) AS KGZYFK ,(SELECT SUM(GCYFK)-SUM(KGZYFK) FROM gc_htgl_ht_htzf z WHERE z.htsjid=ghh2.id) AS WKYFK   "
            + "FROM gc_htgl_htsj ghh2 LEFT JOIN gc_htgl_ht ghh  ON ghh.ID=ghh2.htid " 
            + "LEFT JOIN gc_jh_sj gjs ON gjs.gc_jh_sj_id=ghh2.jhsjid "
            + "LEFT JOIN GC_TCJH_XMXDK gt1 on gjs.xmid = gt1.gc_tcjh_xmxdk_id LEFT JOIN gc_xmbd gt2 on gt2.gc_xmbd_id = gjs.bdid";

    // 更新 总合同签定价
    private static String SQL_UPDATE_ZQKHTJ = "update gc_htgl_ht ghh set ghh.zhtqdj=(select  sum(ghh2.htqdj) from gc_htgl_htsj ghh2 where ghh2.htid=ghh.id ),ghh.zzxhtj=ghh.zbgje+(select  sum(ghh2.htqdj) from gc_htgl_htsj ghh2 where ghh2.htid=ghh.id )  where ghh.id=?";
    // , gzt.zajlfkb=(gzt.zjlqr/gzt.zcsz)*100

    private static String SQL_UPDATE_HTSJ_ZFTZBG = "UPDATE gc_htgl_htsj ghh SET ghh.htzf=(SELECT DECODE( sum(ghhh.zfje), NULL, 0, SUM(ghhh.zfje)) FROM gc_htgl_ht_htzf ghhh WHERE (ghhh.sfdzf = 0 OR sfdzf IS NULL)  AND ghhh.sfyx=1 AND ghh.id=ghhh.htsjid)"
            + " , ghh.wczf=(SELECT DECODE( sum(ghhw.wctzje), NULL, 0, SUM(ghhw.wctzje)) FROM gc_htgl_ht_wctz ghhw WHERE ghhw.sfyx=1 AND ghh.id=ghhw.htsjid) "
            + " , ghh.bgje=(SELECT DECODE( sum(ghhb.bgje), NULL, 0, SUM(ghhb.bgje)) FROM gc_htgl_ht_htbg ghhb WHERE ghhb.sfyx=1 AND ghh.id=ghhb.htsjid) "
            + " WHERE ghh.id=? ";

    private static String SQL_UPDATE_HTSJ_HTJ = "UPDATE gc_htgl_htsj ghh SET ghh.zxhtj =(DECODE(ghh.htqdj, NULL, 0, ghh.htqdj)+DECODE(ghh.bgje, NULL, 0, ghh.bgje)) WHERE ghh.id=? ";

    private static String SQL_QUERY_TZKZDTFX = "select gjs.xmbh,gjs.gc_jh_sj_id as jhsjid,gzl.cssdz ,gjs.xmid,gjs.bdid,gjs.xmmc,gjs.bdmc,decode(gtx.jhztze,null,'null',gtx.jhztze) tcjhndtz, "
            + " decode(gtcbk.JHZTZE,null,0,gtcbk.JHZTZE*10000) jhztz, "
            + " gsc.gs  , "
            + " decode(gtx.gc,null,'null',gtx.gc) tcjhndgctz, "
            + " decode(gtx.zc,null,'null',gtx.zc) tcjhndzctz, "
            + " decode(gtx.qt,null,'null',gtx.qt) tcjhndqttz, "
            + " decode(gtcbk.gc,null,'null',gtcbk.gc) jhgctz, "
            + " decode(gtcbk.zc,null,'null',gtcbk.zc) jhzctz, "
            + " decode(gtcbk.qt,null,'null',gtcbk.qt) jhqttz, "
            + " (select sum(t1.cssdz) from GC_ZJB_LBJB t1 where t1.xmid = gtx.gc_tcjh_xmxdk_id) zxmlbj, "
            + " (select sum(t2.gs) from Gc_Sj_Cbsjpf t2 where t2.xmid = gtx.gc_tcjh_xmxdk_id) zgs, "
            + " (select sum(ZXHTJ) ZXHTJ from gc_htgl_htsj ghh2 where ghh2.xmid = gtx.gc_tcjh_xmxdk_id) zZXHTJ,  "
            + " (select sum(WCZF) WCZF from gc_htgl_htsj ghh2 where ghh2.xmid = gtx.gc_tcjh_xmxdk_id) zWCTZ,   "
            + " (select sum(HTZF) HTZF from gc_htgl_htsj ghh2 where ghh2.xmid = gtx.gc_tcjh_xmxdk_id) zZF, "
            + " (select sum(HTJSJ) HTJS from gc_htgl_htsj ghh2 where ghh2.xmid = gtx.gc_tcjh_xmxdk_id) zJS, "
            + " (select decode(sum(ZXHTJ),null,0,sum(ZXHTJ)) ZXHTJ from gc_htgl_htsj ghh2 where ghh2.jhsjid = gjs.gc_jh_sj_id) ZXHTJ, "
            + " (select decode(sum(WCZF),null,0,sum(WCZF)) WCZF from gc_htgl_htsj ghh2 where ghh2.jhsjid = gjs.gc_jh_sj_id) WCTZ, "
            + " (select decode(sum(HTZF),null,0,sum(HTZF)) HTZF from gc_htgl_htsj ghh2 where ghh2.jhsjid = gjs.gc_jh_sj_id) ZF, "
            + " (select decode(sum(HTJSJ),null,0,sum(HTJSJ)) HTJS from gc_htgl_htsj ghh2 where ghh2.jhsjid = gjs.gc_jh_sj_id) JS "
            + " from gc_jh_sj gjs  "
            + " left join GC_TCJH_XMXDK gtx on gjs.xmid = gtx.gc_tcjh_xmxdk_id  "
            + " left join gc_xmbd gx on gjs.bdid = gx.gc_xmbd_id "
            + " left join GC_ZJB_LBJB gzl on gjs.gc_jh_sj_id = gzl.jhsjid  "
            + " left join gc_tcjh_xmcbk gtcbk on gtcbk.gc_tcjh_xmcbk_id = gtx.xmcbk_id "
            + " left join Gc_Sj_Cbsjpf gsc on gsc.jhsjid = gjs.gc_jh_sj_id  where 1=1 ";
    
    private static String SQL_QUERY_TZKZDTFX_XUJ = "select (select sum(t1.cssdz) from GC_ZJB_LBJB t1 where t1.sjwybh = t.sjwybh) zxmlbj,  "
    		+ " (select sum(t2.gs) from Gc_Sj_Cbsjpf t2 where t2.sjwybh = t.sjwybh) zgs,  "
			+ " (select sum(ZXHTJ) ZXHTJ from gc_htgl_htsj ghh2 where ghh2.jhsjid in (select GC_JH_SJ_ID from gc_jh_sj jhsj where jhsj.sjwybh in (select jhsj1.sjwybh from gc_jh_sj jhsj1 where jhsj1.xmid = t.xmid))) zZXHTJ, "  
			+ " (select sum(WCZF) WCZF from gc_htgl_htsj ghh2 where ghh2.jhsjid in (select GC_JH_SJ_ID from gc_jh_sj jhsj where jhsj.sjwybh in (select jhsj1.sjwybh from gc_jh_sj jhsj1 where jhsj1.xmid = t.xmid))) zWCTZ,    "
			+ " (select sum(HTZF) HTZF from gc_htgl_htsj ghh2 where ghh2.jhsjid in (select GC_JH_SJ_ID from gc_jh_sj jhsj where jhsj.sjwybh in (select jhsj1.sjwybh from gc_jh_sj jhsj1 where jhsj1.xmid = t.xmid))) zZF,  "
			+ " (select sum(HTJSJ) HTJS from gc_htgl_htsj ghh2 where ghh2.jhsjid in (select GC_JH_SJ_ID from gc_jh_sj jhsj where jhsj.sjwybh in (select jhsj1.sjwybh from gc_jh_sj jhsj1 where jhsj1.xmid = t.xmid))) zJS,  "
			+ " (select distinct decode(temp.jhztze,null,'null',temp.jhztze) from GC_TCJH_XMXDK temp where 1=1 nd_sql_  and temp.gc_tcjh_xmxdk_id=t.xmid ) tcjhndtz, "
			+ " (select distinct decode(temp.gc,null,'null',temp.gc) from GC_TCJH_XMXDK temp where 1=1 nd_sql_  and temp.gc_tcjh_xmxdk_id=t.xmid ) tcjhndgctz, "
			+ " (select distinct decode(temp.zc,null,'null',temp.zc) from GC_TCJH_XMXDK temp where 1=1 nd_sql_  and temp.gc_tcjh_xmxdk_id=t.xmid ) tcjhndzctz, "
			+ " (select distinct decode(temp.qt,null,'null',temp.qt) from GC_TCJH_XMXDK temp where 1=1 nd_sql_  and temp.gc_tcjh_xmxdk_id=t.xmid ) tcjhndqttz, "
			+ " (select distinct decode(temp.ztztze,null,0,temp.ztztze*10000) from GC_TCJH_XMXDK temp where 1=1 nd_sql_  and temp.gc_tcjh_xmxdk_id=t.xmid ) jhztz, "
			+ " (select distinct decode(temp.ZTGC,null,0,temp.ZTGC*10000) from GC_TCJH_XMXDK temp where 1=1 nd_sql_  and temp.gc_tcjh_xmxdk_id=t.xmid ) jhgctz, "
			+ " (select distinct decode(temp.ZTZC,null,0,temp.ZTZC*10000) from GC_TCJH_XMXDK temp where 1=1 nd_sql_  and temp.gc_tcjh_xmxdk_id=t.xmid ) jhzctz, "
			+ " (select distinct decode(temp.ZTQT,null,0,temp.ZTQT*10000) from GC_TCJH_XMXDK temp where 1=1 nd_sql_  and temp.gc_tcjh_xmxdk_id=t.xmid ) jhqttz,   "
			+ " t.xmbh,t.gc_jh_sj_id as jhsjid, t.xmid,t.bdid,t.xmmc,t.bdmc,t.cssdz,t.gs,t.ZXHTJ,t.WCTZ,t.ZF,t.JS from  "
			+ " (select t1.*,temp.* from ( "
			+ " select S.SJWYBH s_sjwyhb, "
			+ " decode(sum(gzl.cssdz),null,0,sum(gzl.cssdz)) cssdz, "
			+ " decode(sum(gsc.gs),null,0,sum(gsc.gs)) gs,"
			+ " decode(sum(ZXHTJ),null,0,sum(ZXHTJ)) ZXHTJ, "
			+ " decode(sum(WCZF),null,0,sum(WCZF)) WCTZ, "
			+ " decode(sum(HTZF),null,0,sum(HTZF)) ZF, "
			+ " decode(sum(HTJSJ),null,0,sum(HTJSJ)) JS from GC_JH_SJ S "
			+ " left join GC_HTGL_HTSJ HTSJ on HTSJ.JHSJID=S.GC_JH_SJ_ID "
			+ " left join GC_ZJB_LBJB gzl  on HTSJ.JHSJID = gzl.jhsjid "
			+ " left join Gc_Sj_Cbsjpf gsc on gsc.jhsjid = s.gc_jh_sj_id "
			+ " group by S.SJWYBH) t1, GC_JH_SJ temp where t1.s_sjwyhb=temp.sjwybh  "
			+ "  nd_sql_  xmmc_sql_) t ";

    // 更新合同数据(合同价、支付、合同最新价)
    private static String SQL_UPDATE_HT_ZFXX = "UPDATE gc_htgl_ht ght SET "
            + " ght.zhtzf=(SELECT decode(SUM(ghh.htzf), NULL, 0, SUM(ghh.htzf)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id) "
            + " ,ght.zwczf=(SELECT decode(SUM(ghh.wczf), NULL, 0, SUM(ghh.wczf)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id) "
            + " ,ght.zbgje=(SELECT decode(SUM(ghh.bgje), NULL, 0, SUM(ghh.bgje)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id) "
            + " ,ght.zhtqdj=decode((SELECT decode(SUM(ghh.htqdj), NULL, 0, SUM(ghh.htqdj)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id),0,ght.zhtqdj,(SELECT decode(SUM(ghh.htqdj), NULL, 0, SUM(ghh.htqdj)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id)) "
            + " ,ght.zzxhtj=decode((SELECT decode(SUM(ghh.zxhtj), NULL, 0, SUM(ghh.zxhtj)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id),0,ght.zzxhtj,(SELECT decode(SUM(ghh.zxhtj), NULL, 0, SUM(ghh.zxhtj)) FROM gc_htgl_htsj ghh WHERE ghh.sfyx=1 AND ghh.htid=ght.id)) "
            + " WHERE ght.id=? ";

    // 查询项目概算、拦标价、合同信息
    private static String SQL_QUERY_XMXGJE = " select gtx.gc_tcjh_xmxdk_id XMID,gtx.xmmc,gtx.jhztze ,gtcbk.jhztze*1000 jhztz,  "
            + " (select sum(t1.cssdz) from GC_ZJB_LBJB t1 where t1.xmid = gtx.gc_tcjh_xmxdk_id) xmlbj, "
            + " (select sum(t2.gs) from Gc_Sj_Cbsjpf t2 where t2.xmid = gtx.gc_tcjh_xmxdk_id) gs, "
            + " (select sum(ZXHTJ) ZXHTJ from gc_htgl_htsj ghh2 where ghh2.xmid = gtx.gc_tcjh_xmxdk_id) ZXHTJ,   "
            + " (select sum(WCZF) WCZF from gc_htgl_htsj ghh2 where ghh2.xmid = gtx.gc_tcjh_xmxdk_id) WCTZ,  "
            + " (select sum(HTZF) HTZF from gc_htgl_htsj ghh2 where ghh2.xmid = gtx.gc_tcjh_xmxdk_id) ZF,   "
            + " (select sum(HTJSJ) HTJS from gc_htgl_htsj ghh2 where ghh2.xmid = gtx.gc_tcjh_xmxdk_id) JS  " + " from GC_TCJH_XMXDK gtx  "
            + " left join gc_tcjh_xmcbk gtcbk on gtcbk.xmbh = gtx.xmbh ";

    // 查询项目资金、合同信息
    private static String SQL_QUERY_XMHTZJDT = "SELECT v.*,decode(v.zxhtj,0,0,round(v.wctz/v.zxhtj,4)*100) as tzqdb,decode(v.zxhtj,0,0,round(v.htzf/v.zxhtj,4)*100) as zfqdb FROM VIEW_ZJGL_TZJK v";
    
    //投资信息卡-执行情况
    private static String SQL_QUERY_TZXXK_ZXQK ="select r0.typename,r0.sort_num,decode(r1.gcztz,null,0,r1.gcztz) gcztz,decode(r1.zcztz,null,0,r1.zcztz) zcztz,decode(r1.qtztz,null,0,r1.qtztz) qtztz, "
    		+" decode(r1.hj,null,0,r1.hj) hj,r1.bl from ( select '计财计划' typename,1 sort_num from dual union "
    		+" select '统筹计划' typename,2 sort_num from dual union select '最新合同价' typename,3 sort_num from dual union "
    		+" select '完成投资' typename,4 sort_num from dual union select '支付' typename,5 sort_num from dual ) r0 left join ( " 
    		+"select * from (select 1 sort_num,'计财计划' typename,t1.gc*10000 gcztz,t1.zc*10000 zcztz,t1.qt*10000 qtztz,t1.jhztze*10000 hj ,0 as bl from gc_tcjh_xmcbk t1 left join gc_tcjh_xmxdk t2 on t2.xmcbk_id = t1.gc_tcjh_xmcbk_id where t2.gc_tcjh_xmxdk_id = 'xxxxxmidxxxx' " 
			+" union select 2 sort_num,'统筹计划' typename,t3.gc gcztz,t3.zc zcztz,t3.qt qtztz,t3.jhztze hj ,0 as bl from gc_tcjh_xmxdk t3 where t3.gc_tcjh_xmxdk_id = 'xxxxxmidxxxx' "
			+" union select 3 sort_num,'最新合同价' typename,(select decode(sum(ghh.zxhtj),null,0,sum(zxhtj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where gh.htlx = 'SG' and ghh.xmid = 'xxxxxmidxxxx'  ) gcztz, (select decode(sum(ghh.zxhtj),null,0,sum(zxhtj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where gh.htlx = 'CQ' and ghh.xmid = 'xxxxxmidxxxx'  ) zcztz, "
			+" (select decode(sum(ghh.zxhtj),null,0,sum(zxhtj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where gh.htlx not in('SG','CQ')  and ghh.xmid = 'xxxxxmidxxxx'  ) qtztz,(select decode(sum(ghh.zxhtj),null,0,sum(zxhtj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  ) hj,0 as bl from dual "  
			+" union select 4 sort_num,'完成投资' typename,(select decode(sum(ghh.wczf),null,0,sum(ghh.wczf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where gh.htlx = 'SG' and ghh.xmid = 'xxxxxmidxxxx'  ) gcztz, (select decode(sum(ghh.wczf),null,0,sum(ghh.wczf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where gh.htlx = 'CQ' and ghh.xmid = 'xxxxxmidxxxx'  ) zcztz, "
			+" (select decode(sum(ghh.wczf),null,0,sum(ghh.wczf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where gh.htlx not in('SG','CQ')  and ghh.xmid = 'xxxxxmidxxxx'  ) qtztz,(select decode(sum(ghh.wczf),null,0,sum(ghh.wczf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  ) hj, "
			+" (decode((select decode(sum(ghh.wczf),null,0,sum(ghh.wczf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  ),null,0,round((select decode(sum(ghh.wczf),null,0,sum(ghh.wczf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  )/(select sum(ghh.zxhtj) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  ),4)*100)) bl from dual "  
			+" union select 5 sort_num,'支付' typename,(select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where gh.htlx = 'SG' and ghh.xmid = 'xxxxxmidxxxx'  ) gcztz, (select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where gh.htlx = 'CQ' and ghh.xmid = 'xxxxxmidxxxx'  ) zcztz, "
			+" (select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where gh.htlx not in('SG','CQ')  and ghh.xmid = 'xxxxxmidxxxx'  ) qtztz,(select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  ) hj, "
			+" (decode((select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  ),null,0,round((select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  )/(select sum(ghh.zxhtj) from gc_htgl_htsj ghh left join gc_htgl_ht gh on gh.id = ghh.htid where ghh.xmid = 'xxxxxmidxxxx'  ),4)*100)) bl from dual "
    		+" ) a ) r1 on r0.typename = r1.typename ";
    //投资信息卡-支付情况
    // 已签合同中decode(sum(gh.zhtqdj), null, 0, sum(gh.zhtqdj))原来是decode(sum(ghh.htqdj), null, 0, sum(ghh.htqdj)) update by xhb 2014-04-21
    private static String SQL_QUERY_TZXXK_ZFQK = "select * from (select 2 sort_num, '已支付' lb,(select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='CQ' and ghh.xmid in (xxxxxmidxxxx)) zchts, "
			+" (select decode(sum(ghh.Htzf),null,0,sum(ghh.Htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='CQ' and ghh.xmid in (xxxxxmidxxxx) and gh.htzt > 0) zc, "
			+" (select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='PQ' and ghh.xmid in (xxxxxmidxxxx) and gh.htzt > 0) pqhts, "
			+" (select decode(sum(ghh.Htzf),null,0,sum(ghh.Htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='PQ' and ghh.xmid in (xxxxxmidxxxx) and gh.htzt > 0) pq, "
			+" (select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='SG' and ghh.xmid in (xxxxxmidxxxx) and gh.htzt > 0) sghts, "
			+" (select decode(sum(ghh.Htzf),null,0,sum(ghh.Htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='SG' and ghh.xmid in (xxxxxmidxxxx) and gh.htzt > 0) sg, "
			+" (select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='JL' and ghh.xmid in (xxxxxmidxxxx) and gh.htzt > 0) jlhts, "
			+" (select decode(sum(ghh.Htzf),null,0,sum(ghh.Htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='JL' and ghh.xmid in (xxxxxmidxxxx) and gh.htzt > 0) jl, "
			+" (select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='SJ' and ghh.xmid in (xxxxxmidxxxx) and gh.htzt > 0) sjhts, "
			+" (select decode(sum(ghh.Htzf),null,0,sum(ghh.Htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='SJ' and ghh.xmid in (xxxxxmidxxxx) and gh.htzt > 0) sj, "
			+" (select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx not in('CQ','SG','JL','SJ','PQ') and ghh.xmid in (xxxxxmidxxxx) and gh.htzt > 0) qthts, "
			+" (select decode(sum(ghh.Htzf),null,0,sum(ghh.Htzf)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx not in('CQ','SG','JL','SJ','PQ') and ghh.xmid in (xxxxxmidxxxx) and gh.htzt > 0) qt "
			+" from dual union select 1 sort_num,'已签订（履行中）合同' lb,(select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='CQ' and gh.htzt>0  and ghh.xmid in (xxxxxmidxxxx)) zchts, "  
			+" (select decode(sum(gh.zhtqdj), null, 0, sum(gh.zhtqdj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='CQ' and gh.htzt>0  and ghh.xmid in (xxxxxmidxxxx)) zc,   "
			+" (select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='PQ' and gh.htzt>0  and ghh.xmid in (xxxxxmidxxxx)) pqhts,   "
			+" (select decode(sum(gh.zhtqdj), null, 0, sum(gh.zhtqdj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='PQ' and gh.htzt>0  and ghh.xmid in (xxxxxmidxxxx)) pq, "  
			+" (select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='SG' and gh.htzt>0  and ghh.xmid in (xxxxxmidxxxx)) sghts,   "
			+" (select decode(sum(gh.zhtqdj), null, 0, sum(gh.zhtqdj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='SG' and gh.htzt>0  and ghh.xmid in (xxxxxmidxxxx)) sg, "  
			+" (select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='JL' and gh.htzt>0  and ghh.xmid in (xxxxxmidxxxx)) jlhts,   "
			+" (select decode(sum(gh.zhtqdj), null, 0, sum(gh.zhtqdj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='JL' and gh.htzt>0  and ghh.xmid in (xxxxxmidxxxx)) jl, "  
			+" (select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='SJ' and gh.htzt>0  and ghh.xmid in (xxxxxmidxxxx)) sjhts, "  
			+" (select decode(sum(gh.zhtqdj), null, 0, sum(gh.zhtqdj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx ='SJ' and gh.htzt>0  and ghh.xmid in (xxxxxmidxxxx)) sj, "  
			+" (select count(*) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx not in('CQ','SG','JL','SJ','PQ') and gh.htzt>0  and ghh.xmid in (xxxxxmidxxxx)) qthts,   "
			+" (select decode(sum(gh.zhtqdj), null, 0, sum(gh.zhtqdj)) from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htlx not in('CQ','SG','JL','SJ','PQ') and gh.htzt>0 and ghh.xmid in (xxxxxmidxxxx)) qt from dual) r0";
    //投资信息卡-支付情况统计数据
    private static String SQL_QUERY_TZXXK_ZFQKTJ = "select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) zzf from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htzt>0 and ghh.xmid in ( xxxxxmidxxxx )";
	//	+" select x1.gc_tcjh_xmxdk_id  from gc_tcjh_xmxdk x1 where x1.xmbh in (select x.xmbh from gc_tcjh_xmxdk x where x.gc_tcjh_xmxdk_id in (xxxxxmidxxxx)))";
    
    private static String SQL_QUERY_TZXXK_ZFQKTJ_ZSN = "select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) zzf from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htzt>0 and gh.qdnf<to_char(current_date,'yyyy') and ghh.xmid in ( xxxxxmidxxxx )";
	//	+" select x1.gc_tcjh_xmxdk_id  from gc_tcjh_xmxdk x1 where x1.xmbh in (select x.xmbh from gc_tcjh_xmxdk x where x.gc_tcjh_xmxdk_id in (xxxxxmidxxxx)))";
    
    private static String SQL_QUERY_TZXXK_ZFQKTJ_BND = "select decode(sum(ghh.htzf),null,0,sum(ghh.htzf)) zzf from gc_htgl_htsj ghh left join gc_htgl_ht gh on ghh.htid = gh.id where gh.htzt>0 and gh.qdnf>=to_char(current_date,'yyyy') and ghh.xmid = 'xxxxxmidxxxx'";
	//	+" select x1.gc_tcjh_xmxdk_id  from gc_tcjh_xmxdk x1 where x1.xmbh in (select x.xmbh from gc_tcjh_xmxdk x where x.gc_tcjh_xmxdk_id in (xxxxxmidxxxx)))";
    
    //更新ht表合同签订价
    private static String SQL_UPDATE_HT_JE = "UPDATE GC_HTGL_HT ghh set ghh.zhtqdj=(select sum(ghh2.htqdj) from gc_htgl_htsj ghh2 where ghh2.htid = ghh.id),"
    		+"ghh.zzxhtj=(select sum(ghh3.zxhtj) from gc_htgl_htsj ghh3 where ghh3.htid = ghh.id) where ghh.id = ?";
    
    private static String SQL_QUERY_XJXM = "select g.xj_xmid from gc_tcjh_xmxdk g where g.gc_tcjh_xmxdk_id = ?";
    // 业务类型
    private String ywlx = YwlxManager.GC_HTGL_HT_GC;

    @Override
    public String queryOne(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            QueryConditionList list = RequestUtil.getConditionList(json);

            // ghh.htid需要合同号
            String condition = list == null ? "" : list.getConditionWhere();

            // 组织查询条件
            String orderFilter = RequestUtil.getOrderFilter(json);

            condition += BusinessUtil.getSJYXCondition("ghh2");// 是否有效
            condition += BusinessUtil.getCommonCondition(user, "ghh2");// 预留
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_ONE, page);

            bs.setFieldDic("BGLX", "BGLX");
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式
            bs.setFieldDic("XMLX", "XMLX");// 项目类型
            bs.setFieldDic("XMSX", "XMSX");// 项目属性
            bs.setFieldDic("ISBT", "SF");
            bs.setFieldDic("HTSX", "HTSX");

            // , ghh2.bgje, ghh2.zxhtj, ghh2.wczf, ghh2.htzf

            bs.setFieldThousand("HTQDJ");// 合同签订价(元)
            bs.setFieldThousand("HTJSJ");// 合同结算价或中止价
            // bs.setFieldThousand("JE");//金额
            // bs.setFieldThousand("BGTS");//变更天数
            bs.setFieldThousand("HTZF");// 合同支付
            bs.setFieldThousand("WCZF");// 完成投资
            bs.setFieldThousand("BGJE");// 变更金额
            // bs.setFieldThousand("HTJS");//合同结算
            bs.setFieldThousand("ZXHTJ");// 最新合同价

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<单合同数据>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("单合同数据查询失败!");
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
            condition += BusinessUtil.getSJYXCondition(null);
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);

            conn.setAutoCommit(false);
            String sql = "SELECT t.* FROM GC_HTGL_HTSJ t";
            BaseResultSet bs = DBUtil.query(conn, sql, page);

            bs.setFieldDic("BGLX", "BGLX");
            bs.setFieldDic("HTLX", "HTLX");
            bs.setFieldDic("HTZT", "HTRXZT");// 合同签订状态
            bs.setFieldDic("FBFS", "FBFS");// 发包方式

            bs.setFieldThousand("HTQDJ");// 合同签订价(元)
            bs.setFieldThousand("HTJSJ");// 合同结算价或中止价

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
        GcHtglHtsjVO xmvo = new GcHtglHtsjVO();

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

            // 更新合同数据/合同 信息
            if (xmvo.getId() != null) {
                HashMap map = new HashMap();
                map.put("htsjId", xmvo.getId());
                updateHtsj(json, user, map);
            }

            // 更新合同主相关数据 在上面的方法已经包含此方法的更新
            // DBUtil.executeUpdate(SQL_UPDATE_ZQKHTJ, new Object[] {
            // xmvo.getHtid() });
            // HashMap map1 = new HashMap();
            // map1.put("htId", xmvo.getHtid());
            // gcHtglHtService.updateHtZHSJS(json, user, map1);

            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同数据新增成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + xmvo.getId()
                    + "\",\"fieldname\":\"ghh2.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryOne(jsona, user);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同数据新增失败!");
            LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(), Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同数据新增失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;

    }

    @Override
    public String update(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtsjVO vo = new GcHtglHtsjVO();

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

            // 更新合同数据/合同 信息
            if (vo.getId() != null) {
                HashMap map = new HashMap();
                map.put("htsjId", vo.getId());
                updateHtsj(json, user, map);
            }

            LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user.getOrgDept()
                    .getDeptName() + " " + user.getName() + "合同数据修改成功", user, "", "");

            

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + vo.getId()
                    + "\",\"fieldname\":\"ghh2.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryOne(jsona, user);

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
        GcHtglHtsjVO vo = new GcHtglHtsjVO();
        try {
            conn.setAutoCommit(false);
            JSONArray list = vo.doInitJson(json);
            JSONObject jsonObj = (JSONObject) list.get(0);

            vo.setValueFromJson(jsonObj);
            BaseDAO.delete(conn, vo);

            resultVo = vo.getRowJson();
            conn.commit();

            // 更新合同数据/合同 信息
            if (vo.getId() != null) {
                HashMap map = new HashMap();
                map.put("htsjId", vo.getId());
                updateHtsj(json, user, map);
            }

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

    @Override
    public String updateHtsj(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String resultVO = null;
        GcHtglHtsjVO htsj = null;
        try {
            String htsjId = (String) map.get("htsjId");
            htsj = findById(htsjId);
            conn.setAutoCommit(false);

            DBUtil.executeUpdate(SQL_UPDATE_HTSJ_ZFTZBG, new Object[] { htsj.getId() });
            conn.commit();

           
            DBUtil.executeUpdate(SQL_UPDATE_HTSJ_HTJ, new Object[] { htsj.getId() });
            conn.commit();
            
            DBUtil.executeUpdate(SQL_UPDATE_HT_ZFXX, new Object[] { htsj.getHtid() });
            conn.commit();
            LogManager.writeUserLog(htsj.getSjbh(), htsj.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同数据修改成功", user, "", "");

            String jsona = "{querycondition: {conditions: [{\"value\":\"" + htsj.getId()
                    + "\",\"fieldname\":\"ghh2.id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
            return queryOne(jsona, user);

        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            e.printStackTrace(System.out);
            logger.error("合同数据修改失败!");
            LogManager.writeUserLog(htsj.getSjbh(), htsj.getYwlx(), Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE, user
                    .getOrgDept().getDeptName() + " " + user.getName() + "合同数据修改失败", user, "", "");
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return resultVO;
    }

    @Override
    public String queryTzkzdtfx(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {
            PageManager page = RequestUtil.getPageManager(json);
            if (page == null)
                page = new PageManager();
            String sql = SQL_QUERY_TZKZDTFX_XUJ;
            String nd = (String) map.get("nd");
            String gcNull = (String) map.get("gs");
            String lbjNull = (String) map.get("lbj");
            String xmmc = (String) map.get("xmmc");
            // 组织查询条件
            conn.setAutoCommit(false);
            if (!"".equals(nd)) {
           //     sql += " and  gtx.ND = '" + nd + "' ";
            	sql = sql.replaceAll("nd_sql_", " and temp.ND='" + nd + "'");
            }
            if (!"".equals(xmmc)) {
           //     sql += " and gjs.xmmc like '%" + xmmc + "%' ";
            	sql = sql.replaceAll("xmmc_sql_", " and temp.xmmc like '%" + xmmc + "%'");
            }
            sql = sql.replace("nd_sql_", "");
            sql = sql.replace("xmmc_sql_", "");
            if ("1".equals(gcNull)) {
                sql += " and (select sum(t2.gs) from Gc_Sj_Cbsjpf t2 where t2.xmid = gtx.gc_tcjh_xmxdk_id) is not null ";
            }
            if ("1".equals(lbjNull)) {
                sql += " and (select sum(t1.cssdz) from GC_ZJB_LBJB t1 where t1.xmid = gtx.gc_tcjh_xmxdk_id) is not null ";
            }
            sql += " order by t.XMBH,t.XMBS ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);

            bs.setFieldThousand("TCJHNDTZ");// 统筹计划年度投资
            bs.setFieldThousand("TCJHNDGCTZ");// 统筹计划工程投资
            bs.setFieldThousand("TCJHNDZCTZ");// 统筹计划征拆投资
            bs.setFieldThousand("TCJHNDQTTZ");// 统筹计划其他投资
            bs.setFieldThousand("JHZTZ");// 计划总投资
            bs.setFieldThousand("JHGCTZ");// 计划工程投资
            bs.setFieldThousand("JHZCTZ");// 计划征拆投资
            bs.setFieldThousand("JHQTTZ");// 计划其他投资
            bs.setFieldThousand("GS");// 合同签订价(元)
            bs.setFieldThousand("CSSDZ");// 合同签订价(元)
            bs.setFieldThousand("ZXHTJ");// 合同结算价或中止价
            bs.setFieldThousand("WCTZ");// 合同结算价或中止价
            bs.setFieldThousand("ZF");// 合同结算价或中止价
            bs.setFieldThousand("JS");// 合同结算价或中止价

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<投资控制动态分析数据>", user, "", "");
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
    public String queryXmxgje(String json, User user) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            // 组织查询条件
            PageManager page = RequestUtil.getPageManager(json);
            String condition = RequestUtil.getConditionList(json).getConditionWhere();
            String orderFilter = RequestUtil.getOrderFilter(json);
            condition += BusinessUtil.getCommonCondition(user, null);
            condition += orderFilter;
            if (page == null)
                page = new PageManager();
            page.setFilter(condition);
            conn.setAutoCommit(false);

            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_XMXGJE, null);

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<项目合同信息、拦标价信息、概算信息>", user, "", "");
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
    public String queryXmtzmxfx(String json, User user) throws Exception {

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
            BaseResultSet bs = DBUtil.query(conn, SQL_QUERY_XMHTZJDT, page);

            bs.setFieldThousand("TCTZ");// 统筹计划投资
            bs.setFieldThousand("JHTZ");// 计划总投资
            bs.setFieldThousand("GS"); // 工程总概算
            bs.setFieldThousand("ZXHTJ");// 最新合同价
            bs.setFieldThousand("WCTZ");// 完成投资
            bs.setFieldThousand("HTZF");// 合同支付
            bs.setFieldThousand("HTJS");// 合同结算

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<项目投资控制动态明细分析数据>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("项目投资控制动态明细分析数据!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
    
    @Override
    public String queryTzxxk_zxqk(String json, User user, HashMap map) throws Exception {

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
            
            String sql = SQL_QUERY_TZXXK_ZXQK;
            sql = sql.replace("xxxxxmidxxxx", (String)map.get("xmid"));
            
            conn.setAutoCommit(false);
            BaseResultSet bs = DBUtil.query(conn, sql, page);

            bs.setFieldThousand("GCZTZ");// 工程总投资
            bs.setFieldThousand("ZCZTZ");// 征拆总投资
            bs.setFieldThousand("QTZTZ"); // 其他总投资
            bs.setFieldThousand("HJ");// 合计

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<资金执行情况>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("查询资金执行情况数据!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
    
    @Override
    public String queryTzxxk_zfqk(String json, User user, HashMap map) throws Exception {

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
            
            String sql = SQL_QUERY_TZXXK_ZFQK;
            sql = sql.replace("xxxxxmidxxxx", getAllXm(conn, (String)map.get("xmid")));
            BaseResultSet bs = DBUtil.query(conn, sql, page);

            bs.setFieldThousand("ZC");// 征拆
            bs.setFieldThousand("PQ");// 排迁
            bs.setFieldThousand("SG");// 施工
            bs.setFieldThousand("SJ");// 设计
            bs.setFieldThousand("JL");// 监理
            bs.setFieldThousand("QT");// 其他

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<资金支付情况>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("资金支付情况数据!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
    
    @Override
    public String queryTzxxk_zjzfqktj(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            conn.setAutoCommit(false);
            
            String sql = SQL_QUERY_TZXXK_ZFQKTJ;
            sql = sql.replace("xxxxxmidxxxx", getAllXm(conn, (String)map.get("xmid")));
            BaseResultSet bs = DBUtil.query(conn, sql, null);

            bs.setFieldThousand("ZZF");// 支付金额

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<资金支付情况-总支付>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("资金支付情况数据!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
    
    @Override
    public String queryTzxxk_zjzfqktj_zsn(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            conn.setAutoCommit(false);
            
            String sql = SQL_QUERY_TZXXK_ZFQKTJ_ZSN;
            sql = sql.replace("xxxxxmidxxxx", getAllXm(conn, (String)map.get("xmid")));
            BaseResultSet bs = DBUtil.query(conn, sql, null);

            bs.setFieldThousand("ZZF");// 支付金额

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<资金支付情况-累计至上年>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("资金支付情况数据!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
    
    @Override
    public String queryTzxxk_zjzfqktj_bnd(String json, User user, HashMap map) throws Exception {

        Connection conn = DBUtil.getConnection();
        String domresult = "";
        try {

            conn.setAutoCommit(false);
            
            String sql = SQL_QUERY_TZXXK_ZFQKTJ_BND;
            sql = sql.replace("xxxxxmidxxxx", (String)map.get("xmid"));
            BaseResultSet bs = DBUtil.query(conn, sql, null);

            bs.setFieldThousand("ZZF");// 支付金额

            domresult = bs.getJson();
            LogManager.writeUserLog(null, ywlx, Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS, user.getOrgDept().getDeptName()
                    + " " + user.getName() + "查询<资金支付情况-本年度>", user, "", "");
        } catch (Exception e) {
            DBUtil.rollbackConnetion(conn);
            logger.error("资金支付情况数据!");
            e.printStackTrace(System.out);
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domresult;

    }
    
    //获取所有关联项目
    public String queryGlxm( String id) throws Exception {
    	String[][] res = null;
    	String xmid = xmid = "'"+id+"'";
        try {
        	res = DBUtil.querySql(SQL_QUERY_XJXM, new String[]{id});
           
        	if (res!=null) {
				if(StringUtils.isNotBlank(res[0][0])){
					xmid +=","+ queryGlxm(res[0][0]);
				}
			}
           
        } catch (Exception e) {
            logger.error("资金支付情况数据!");
            e.printStackTrace(System.out);
        } 
        return xmid;

    }
    
    private String getAllXm(Connection conn , String xmid) {
		String xmidList = "";
		try {
			String sql = "select gc_tcjh_xmxdk_id from GC_TCJH_XMXDK xdk where xdk.xmwybh in " 
					+ "(select xmwybh from GC_TCJH_XMXDK x0 where x0.gc_tcjh_xmxdk_id='"+xmid+"')";
			
			String[][] rs = DBUtil.query(conn, sql);
			
			if (rs != null) {
				for (int i = 0; i < rs.length; i++) {
					xmidList += "'" + rs[i][0] + "',";
				}
			}
			
			xmidList = xmidList.endsWith(",") ? xmidList.substring(0, xmidList.lastIndexOf(",")) : xmidList;
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {}
		
		return xmidList;
	}
}
