package com.ccthanking.business.xmzhxx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.springframework.stereotype.Service;

import com.ccthanking.common.BusinessUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryCondition;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb
 */
@Service
public class XmzhxxServiceImpl {

    public String queryXmzhxx(String json, User user) throws SQLException {
        Connection conn = DBUtil.getConnection();
        String domResult = "";
        try {
            QueryConditionList list = RequestUtil.getConditionList(json);
            String condition = "";
            for (int i = 0; i < list.size(); i++) {
            	QueryCondition qc = list.getQueryCondition(i);
                if ("IS_WC_PQ".equals(qc.getField())) { // 是否完成排迁
                    if ("0".equals(qc.getValue())) {
                    	// 未完成
                    	condition += " and pq_SJ is null ";
					} else {
                    	// 完成
                    	condition += " and pq_SJ is not null ";
					}
                    continue;
				} else if("IS_CQ_PQ".equals(qc.getField())) { // 是否超期排迁
					if ("0".equals(qc.getValue())) {
                    	// 未超期
                    	condition += " and pq_SJ <= pq ";
					} else {
                    	// 超期
                    	condition += " and pq_SJ > pq ";
					}
                    continue;
				} else if("IS_WC_ZQ".equals(qc.getField())) { // 是否完成征拆
					if ("0".equals(qc.getValue())) {
                    	// 未完成
                    	condition += " and zc_sj is null ";
					} else {
                    	// 完成
                    	condition += " and zc_sj is not null ";
					}
                    continue;
				} else if("IS_CQ_ZQ".equals(qc.getField())) { // 是否超期征拆
					if ("0".equals(qc.getValue())) {
                    	// 未完成
                    	condition += " and zc_sj <= zc ";
					} else {
                    	// 完成
                    	condition += " and zc_sj > zc ";
					}
                    continue;
				} else if("ISKG".equals(qc.getField())) { // 是否开工
					if ("0".equals(qc.getValue())) {
                    	// 未开工
                    	condition += " and kgsj_sj IS NULL ";
					} else {
                    	// 已开工
                    	condition += " and kgsj_sj IS NOT NULL ";
					}
                    continue;
				} else if("IS_AQ_KG".equals(qc.getField())) { // 是否按期开工
					if ("0".equals(qc.getValue())) {
                    	// 按期开工
                    	condition += " and kgsj_sj <= kgsj ";
					} else {
                    	// 延期开工
                    	condition += " and kgsj_sj > kgsj ";
					}
                    continue;
				} else if("ISWG".equals(qc.getField())) { // 是否完工
					if ("0".equals(qc.getValue())) {
                    	// 未完工
                    	condition += " and WGSJ_SJ IS NULL ";
					} else {
                    	// 已完工
                    	condition += " and WGSJ_SJ IS NOT NULL ";
					}
                    continue;
				} else if("IS_AQ_WG".equals(qc.getField())) { // 是否按期完工
					if ("0".equals(qc.getValue())) {
                    	// 按期开工
                    	condition += " and WGSJ_SJ <= WGSJ ";
					} else {
                    	// 延期开工
                    	condition += " and WGSJ_SJ > WGSJ ";
					}
                    continue;
				}
                condition += qc.getLogic()+" "+qc.getField()+" "+qc.getOperation()+" '" + qc.getValue() + "' ";
			}
            condition = condition.startsWith("and") ? condition.substring(3, condition.length()).trim() : condition;
            String orderFilter = RequestUtil.getOrderFilter(json);
            
            condition += orderFilter;
            PageManager page = RequestUtil.getPageManager(json);
            page.setFilter(condition);
            // （ 项管公司）
            String querySql = "select * from (SELECT "
                    // 甘特图
            		+ "T.GC_JH_SJ_ID AS GTT,T.PXH,T.XMID,"
                    // 健康状况:需要各节点的计划、实际完成时间和是否需要办理节点，在这里集中获取，由前台处理
                    + "T.XMBH AS JKZK,ISKYPF,ISHPJDS,ISGCXKZ,ISSGXK,ISCBSJPF,ISCQT,ISPQT,ISSGT,ISTBJ,ISCS,ISJLDW,ISSGDW,ISZC,ISPQ,ISJG,ISJS,"
                    + "KGSJ,KGSJ_SJ,WGSJ,WGSJ_SJ,KYPF,KYPF_SJ,HPJDS,HPJDS_SJ,GCXKZ,GCXKZ_SJ,SGXK,SGXK_SJ,CBSJPF,CBSJPF_SJ,CQT,CQT_SJ,"
                    + "PQT,PQT_SJ,SGT,SGT_SJ,TBJ,TBJ_SJ,CS,CS_SJ,JLDW,JLDW_SJ,SGDW,SGDW_SJ,ZC,ZC_SJ,PQ,PQ_SJ,JG,JG_SJ,JS_SJ,XMBS,BDID,"
                    // 项目编号
                    + " T.XMBH,"
                    // 项目名称
                    + " T.XMMC,"
                    // 建设位置
                    + "DECODE(T.XMBS,'1',(SELECT BDDD FROM GC_XMBD WHERE GC_XMBD_ID = T.BDID AND ROWNUM=1),(SELECT XMDZ FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1))AS JSDZ,"
                    // 项目地址图标( ↓ )
                    + "'none3' XXJT,"
                    // 项目管理公司
                    + " (SELECT XMGLGS FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = t.XMID and ROWNUM=1)AS XMGLGS, "
                    // 标段名称
                    + "BDMC AS BDMC,"
                    // 项目性质
                    + "(SELECT XJXJ FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)AS XMXZ,"
                    // 年度目标
                    + "(SELECT JSMB FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)AS NDMB, "
                    // ======================下方几项的查询内容已在上面给出，前台显示为图标，所以不需要重新获取值===========
                    // 工程进展 （已拆迁 已排迁 已开工 已完工 已交/竣工）
                    + "XMZT(T.ISZC,T.ZC_SJ,T.ZC) GC_YCQ, " 
                    
						
					/*	1：不需要办理
						2：按期完成、
						3：未完成未到期
						4：延期完成
						5：到期未完成*/
                    + "(SELECT X.TEMP_PQZT FROM ( SELECT TEMP_PQZT ,A.GC_JH_SJ_ID FROM GC_JH_SJ A , (SELECT MAX(TEMP_PQZT) TEMP_PQZT ,XMID  FROM ( "
                    + "SELECT T.XMID ,CASE T.ISPQ WHEN '0' THEN '1' WHEN '1' THEN DECODE(T.PQ,'', DECODE(T.PQ_SJ,'','3','2'), DECODE(T.PQ_SJ,'',DECODE(SIGN(SYSDATE-T.PQ),1,'5','3'),DECODE(SIGN(T.PQ_SJ-T.PQ),1,'4','2')))  "
                    + "END TEMP_PQZT FROM GC_JH_SJ T  ) A     GROUP BY A.XMID) B WHERE A.XMID=B.XMID AND A.BDID IS NULL   UNION ALL "
                    + "SELECT CASE T.ISPQ WHEN '0' THEN '1' WHEN '1' THEN DECODE(T.PQ,'', DECODE(T.PQ_SJ,'','3','2'), DECODE(T.PQ_SJ,'',DECODE(SIGN(SYSDATE-T.PQ),1,'5','3'),DECODE(SIGN(T.PQ_SJ-T.PQ),1,'4','2')))  "
                    + "END TEMP_PQZT  ,T.GC_JH_SJ_ID FROM GC_JH_SJ T WHERE XMBS = '1'  ) X WHERE T.GC_JH_SJ_ID=X.GC_JH_SJ_ID ) GC_YPQ , " 
                    
					+ "XMZT('999',T.KGSJ_SJ,T.KGSJ) GC_YKG , " 
					+ "XMZT('999',T.WGSJ_SJ,T.WGSJ) GC_YWG , " 
					+ "JJG_XMZT(T.ISJG, T.ISJS, T.JG_SJ, T.JG, T.JS_SJ) GC_YJJG, "
                    
                    // 手续办理 （可研批复 土地划拨 工程规划许可 施工许可）
                    + "XMZT(T.ISKYPF,T.KYPF_SJ,T.KYPF) SXBL_KYPF," 
                    + "XMZT(T.ISHPJDS,T.HPJDS_SJ,T.HPJDS) SXBL_TDHB," 
                    + "XMZT(T.ISGCXKZ,T.GCXKZ_SJ,T.GCXKZ) SXBL_GCGHXK," 
                    + "XMZT(T.ISSGXK,T.SGXK_SJ,T.SGXK) SXBL_SGXK, "
                    
                    // 设计进展 （ 设计任务书 初设批复 拆迁图 排迁图 施工图 设计变更）
                    // 设计任务书
                    + "(select count(r.gc_sjgl_rwsgl_id) from gc_sjgl_rwsgl r where r.sfyx='1' and r.sjwybh =t.sjwybh ) SJ_SJRWS,"
                    

                    + "XMZT(T.ISCBSJPF,T.CBSJPF_SJ,T.CBSJPF) SJ_CSPF," 
                    + "XMZT(T.ISCQT,T.CQT_SJ,T.CQT) SJ_CQT," 
                    + "XMZT(T.ISPQT,T.PQT_SJ,T.PQT) SJ_PQT," 
                    + "XMZT(T.ISSGT,T.SGT_SJ,T.SGT) SJ_SGT,"
                    
                    // 设计变更数
                    + "NVL(DECODE(T.XMBS,'0', " 
                    + "(SELECT COUNT(bg.GC_SJ_SJBG_JS2_ID) FROM GC_SJ_SJBG_JS2 bg WHERE bg.SFYX = '1' AND bg.BGZT = '1' AND " 
                    + "bg.XMID in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh))," 
                    + "(SELECT COUNT(bg.GC_SJ_SJBG_JS2_ID) FROM GC_SJ_SJBG_JS2 bg WHERE bg.SFYX = '1' AND bg.BGZT = '1' AND " 
                    + "bg.sjwybh = T.sjwybh)  ),0) SJ_BGS, "
                    
                    // =============================================================================================

                    // 拦标价（元） 26
                    + "(SELECT CSSDZ FROM GC_ZJB_LBJB LBJ WHERE LBJ.sjwybh=T.sjwybh) LBJ, "
                    // =============================================================================================

                    // 招标进展 （设计单位
                    + "DECODE(T.XMBS,'1',(SELECT SJDW FROM GC_XMBD BD WHERE GC_XMBD_ID = T.BDID)," 
                    + "(SELECT SJDW FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID) ) AS ZB_SJDW, "

                    // 监理单位
                    + "DECODE(T.XMBS,'1',(SELECT JLDW FROM GC_XMBD BD WHERE GC_XMBD_ID = T.BDID),"
                    + "(SELECT JLDW FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID) ) AS ZB_JLDW,"

                    // 施工单位）
                    + "DECODE(T.XMBS,'1',(SELECT SGDW FROM GC_XMBD BD WHERE GC_XMBD_ID = T.BDID)," 
                    + "(SELECT SGDW FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID) ) AS ZB_SGDW, "
                    // =============================================================================================

                    // 排迁信息 （排迁任务数/完成数
                    + "(TO_CHAR(NVL((SELECT COUNT(ZXM.GC_PQ_ZXM_ID) FROM GC_PQ_ZXM ZXM "
                    + "WHERE ZXM.JHSJID = T.GC_JH_SJ_ID GROUP BY T.GC_JH_SJ_ID), 0))|| '/' || "
                    + "TO_CHAR(NVL((SELECT COUNT(ZXM.GC_PQ_ZXM_ID) FROM GC_PQ_ZXM ZXM "
                    + "WHERE ZXM.JHSJID = T.GC_JH_SJ_ID  "
                    + "AND WCSJ IS NOT NULL GROUP BY T.GC_JH_SJ_ID), 0))) PQ_RWS_WCS, "

                    // 总合同金额（元）
                    + "NVL((SELECT SUM(C.ZHTQDJ) FROM GC_PQ_ZXM A, GC_HTGL_HT C "
                    + "WHERE A.JHSJID = T.GC_JH_SJ_ID "
                    + "AND C.ID = A.HTID GROUP BY  T.GC_JH_SJ_ID),0) PQ_ZHTJE, "
                    

                    // 已支付（元））33
                    + "NVL((SELECT SUM(C.ZHTZF) FROM GC_PQ_ZXM A, GC_HTGL_HT C "
                    + "WHERE A.JHSJID = T.GC_JH_SJ_ID "
                    + "AND C.ID = A.HTID GROUP BY  T.GC_JH_SJ_ID),0) PQ_YZF, "
                    

                    // 百分比
                    + "NVL((SELECT TO_CHAR(DECODE(NVL(SUM(ZHTQDJ),0),0,0, "
                    + "ROUND((SUM(C.ZHTZF))/SUM(ZHTQDJ)*100,2)),'FM99999999999990.00') "
                    + "FROM GC_PQ_ZXM A, GC_HTGL_HT C "
                    + "WHERE A.JHSJID = T.GC_JH_SJ_ID "
                    + "AND C.ID = A.HTID GROUP BY  T.GC_JH_SJ_ID),0) PQ_BFB, "
                    // =============================================================================================

                    
                    
                    // 征收信息 （总居民数/已完拆迁 总企业数/已完拆迁 征地面积（m2）/已完征地 ）
                    // （拨入资金（元） 已使用资金（元） 余额（元））39
                    + "(NVL((SELECT SUM(JMHS) FROM GC_ZSB_XXB XXB WHERE XXB.JHSJID = T.GC_JH_SJ_ID AND XXB.SFYX = '1'), 0) || '/' || NVL((SELECT SUM(BCWCJMS) FROM GC_ZSB_JDB JDB WHERE JDB.JHSJID = T.GC_JH_SJ_ID AND JDB.SFYX = '1'), 0) ) ZS_ZJMS_JWCQ , "
                    + "(NVL((SELECT SUM(QYJS) FROM GC_ZSB_XXB XXB WHERE XXB.JHSJID = T.GC_JH_SJ_ID AND XXB.SFYX = '1'), 0) || '/' || NVL((SELECT SUM(BCWCQYS) FROM GC_ZSB_JDB JDB WHERE JDB.JHSJID = T.GC_JH_SJ_ID AND JDB.SFYX = '1'), 0) ) ZS_ZQYS_YWCQ , "
                    + "(NVL((SELECT SUM(ZMJ) FROM GC_ZSB_XXB XXB WHERE XXB.JHSJID = T.GC_JH_SJ_ID AND XXB.SFYX = '1'), 0) || '/' || NVL((SELECT SUM(BCZDMJ) FROM GC_ZSB_JDB JDB WHERE JDB.JHSJID = T.GC_JH_SJ_ID AND JDB.SFYX = '1'), 0) ) ZS_ZDMJ_YWZD, "
                    + "NVL((SELECT SUM(ZJDWJE) FROM GC_ZSB_XXB XXB WHERE XXB.JHSJID = T.GC_JH_SJ_ID AND XXB.SFYX = '1'), 0) ZS_BRZJ, "
        
                    + "NVL((SELECT SUM(HTZF.ZFJE) AS YSYJE  "
                    + "			FROM 	GC_HTGL_HTSJ 	HTSJ, "
                    + "					GC_HTGL_HT_HTZF HTZF, "
                    + "					GC_HTGL_HT      HT "
                    + "			WHERE HTZF.HTSJID = HTSJ.JHSJID  "
                    + "         AND HT.ID = HTSJ.HTID "
                    + "         AND HT.SFYX = '1' AND HT.HTLX = 'CQ' "
                    + "         AND HTZF.SFYX = '1' AND HTZF.SFDZF = '0' "
                    + "         AND HTSJ.SFYX = '1' "
                    + "         AND HTSJ.JHSJID = T.GC_JH_SJ_ID "
                    + "         GROUP BY HTSJ.JHSJID),0) ZS_YSYZJ, "
                 
                    + "(NVL((SELECT SUM(ZJDWJE) FROM GC_ZSB_XXB XXB  "
                    + "			WHERE XXB.JHSJID = T.GC_JH_SJ_ID AND XXB.SFYX = '1'),0)- "
                    + "NVL((SELECT SUM(HTZF.ZFJE) AS YSYJE FROM GC_HTGL_HTSJ HTSJ, "
                    + "         GC_HTGL_HT_HTZF HTZF, "
                    + "         GC_HTGL_HT      HT "
                    + "         WHERE HTZF.HTSJID = HTSJ.JHSJID  "
                    + "         AND HT.ID = HTSJ.HTID "
                    + "         AND HT.SFYX = '1' AND HT.HTLX = 'CQ' "
                    + "         AND HTZF.SFYX = '1' AND HTZF.SFDZF = '0' "
                    + "         AND HTSJ.SFYX = '1' "
                    + "         AND HTSJ.JHSJID = T.GC_JH_SJ_ID "
                    + "         GROUP BY HTSJ.JHSJID),0)) ZS_YE, "

                    // 合同信息 （已签合同数 征收合同（虚拟）金额（元） 排迁合同金额（元） 设计合同金额（元））
                    // （监理合同金额（元） 施工合同金额（元） 其他合同金额（元） 合同总金额（元）
                    // （完成投资（元） % 已支付（元） %）51

                    + " NVL(decode(t.xmbs,0,(select COUNT(DISTINCT htid) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh and vd.htzt>0 ),(select COUNT(DISTINCT htid) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh and vd.htzt>0 )),0) AS HT_JQHTS,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx='CQ'),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh AND vd.htlx='CQ')),0) AS HT_ZSHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx='PQ'),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh AND vd.htlx='PQ')),0) AS HT_PQHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx='SJ'),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh AND vd.htlx='SJ')),0) AS HT_SJHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx='JL'),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh AND vd.htlx='JL')),0) AS HT_JLHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx='SG'),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh AND vd.htlx='SG')),0) AS HT_SGHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx NOT IN ('CQ','PQ','SG','SJ','JL')),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh  AND vd.htlx NOT IN ('CQ','PQ','SG','SJ','JL'))),0) AS HT_QTHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh)),0) AS HT_ZHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.wczf),  NULL, 0, sum(vd.wczf))  from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)),(select decode(sum(vd.wczf),  NULL, 0, sum(vd.wczf))  from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh)),0) AS HT_WCTZ,"
                    + " NVL(decode(t.xmbs,0,(SELECT DECODE(NVL(SUM(htqdj), 0), 0, 0, round(SUM(vd.wczf) / sum(vd.htqdj) * 100, 2)) from view_ht_htsj_jhsj_mx vd WHERE vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)),(SELECT DECODE(NVL(SUM(htqdj), 0), 0, 0, round(SUM(vd.wczf) / sum(vd.htqdj) * 100, 2)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh = t.sjwybh)),0) AS HT_BFB1,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htzt), NULL, 0, sum(vd.htzf)) from view_ht_htsj_jhsj_mx vd WHERE vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)),(select decode(SUM(vd.htzt), null, 0, SUM(vd.htzf)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh)),0) AS HT_YZF,"
                    + " NVL(decode(t.xmbs,0,(select DECODE(NVL(SUM(htqdj), 0), 0, 0, round(SUM(vd.htzf) / sum(vd.htqdj) * 100, 2)) from view_ht_htsj_jhsj_mx vd WHERE vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)),(select DECODE(NVL(SUM(htqdj), 0), 0, 0, round(SUM(vd.htzf) / sum(vd.htqdj) * 100, 2)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh = t.sjwybh)),0) AS HT_BFB2,"
                    

                    // 年度投资估算（万元）--工程
                    + "DECODE(XMBS,'1','',NVL((SELECT GC FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS TZ_GC,"
                    
                    // 年度投资估算（万元）--征拆
                    + "DECODE(XMBS,'1','',NVL((SELECT ZC FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS TZ_ZC,"
                    
                    // 年度投资估算（万元）--其他
                    + "DECODE(XMBS,'1','',NVL((SELECT QT FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS TZ_QT,"
                    
                    // 年度投资估算（万元）--总投资
                    + "DECODE(XMBS,'1','',NVL((SELECT JHZTZE FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS TZ_ZTZ, "
                    //--------------------------总体投资--------------------------------//
                    // 总体投资估算（万元）--工程
                    + "DECODE(XMBS,'1','',NVL((SELECT ZTGC FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS ZTTZ_GC,"
                    
                    // 总体投资估算（万元）--征拆
                    + "DECODE(XMBS,'1','',NVL((SELECT ZTZC FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS ZTTZ_ZC,"
                    
                    // 总体投资估算（万元）--其他
                    + "DECODE(XMBS,'1','',NVL((SELECT ZTQT FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS ZTTZ_QT,"
                    
                    // 总体投资估算（万元）--总投资
                    + "DECODE(XMBS,'1','',NVL((SELECT ZTZTZE FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS ZTTZ_ZTZ, "

                    // 概算信息（元）（工程 征拆 其他 总投资 ）
                    + "NVL(NVL2(BDID,(SELECT NVL(SUM(CBSJ.GCJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.sjwybh=T.sjwybh),(SELECT NVL(SUM(CBSJ.GCJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.XMID in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh))),0)  AS GS_GC, " 
                    + "NVL(NVL2(BDID,(SELECT NVL(SUM(CBSJ.ZCJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.sjwybh=T.sjwybh),(SELECT NVL(SUM(CBSJ.ZCJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.XMID in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh))),0)  AS GS_ZC, " 
                    + "NVL(NVL2(BDID,(SELECT NVL(SUM(CBSJ.QTJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.sjwybh=T.sjwybh),(SELECT NVL(SUM(CBSJ.QTJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.XMID in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh))),0)  AS GS_QT, " 
                    + "NVL(NVL2(BDID,(SELECT NVL(SUM(CBSJ.GS),  0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.sjwybh=T.sjwybh),(SELECT NVL(SUM(CBSJ.GS),  0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.XMID in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh))),0) AS GS_ZTZ, "

                    // 资金支付信息（元）（工程费用 征拆费用 其他费用 总支付费用）
                    + "(SELECT decode(SUM(vz.zfje), NULL, 0, SUM(vz.zfje)) FROM VIEW_GBMJK_XMZHXX_ZJZFXX vz WHERE vz.sjwybh=t.sjwybh AND vz.htlx='SG') AS ZJZF_GCFY,"
                    + "(SELECT decode(SUM(vz.zfje), NULL, 0, SUM(vz.zfje)) FROM VIEW_GBMJK_XMZHXX_ZJZFXX vz WHERE vz.sjwybh=t.sjwybh AND vz.htlx='CQ') AS ZJZF_ZCFY,"
                    + "(SELECT decode(SUM(vz.zfje), NULL, 0, SUM(vz.zfje)) FROM VIEW_GBMJK_XMZHXX_ZJZFXX vz WHERE vz.sjwybh=t.sjwybh AND vz.htlx NOT IN ('SG', 'CQ')) AS ZJZF_QTFY,"
                    + "(SELECT decode(SUM(vz.zfje), NULL, 0, SUM(vz.zfje)) FROM VIEW_GBMJK_XMZHXX_ZJZFXX vz WHERE vz.sjwybh=t.sjwybh) AS ZJZF_ZZFFY,"

                    // // 结算信息（元）（工程金额 征拆金额 其他金额 总结算金额）
                    + "(select decode(sum(vd.htjs), null, 0, sum(vd.htjs)) from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh  AND vd.htlx='SG') AS JS_GCJE,"
                    + "(select decode(sum(vd.htjs), null, 0, sum(vd.htjs)) from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh  AND vd.htlx='CQ') AS JS_ZCJE,"
                    + "(select decode(sum(vd.htjs), null, 0, sum(vd.htjs)) from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh  AND vd.htlx NOT IN ('SG', 'CQ')) AS JS_QTJE,"
                    + "(select decode(sum(vd.htjs), null, 0, sum(vd.htjs)) from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh) AS JS_ZJSJE,ND, "
                    + "(select decode(decode(sum(g1.TBJE), null, 0, sum(g1.TBJE)), 0, 0, ((decode(sum(g1.TBJE), null, 0, sum(g1.TBJE))-decode(sum(g1.CSSDJE), null, 0 , sum(g1.CSSDJE)))/decode(sum(g1.TBJE), null, 0, sum(g1.TBJE)))) from GC_ZJB_JSB g1 where g1.BDID = t.BDID ) SJB"
                    
                    + " from GC_JH_SJ t) xmzhxx" ;
            
            BaseResultSet bs = DBUtil.query(conn, querySql, page);
            // bs.setFieldOrgDept("XMGLGS");
            bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");// 项目管理公司
            bs.setFieldTranslater("ZB_SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");// 设计单位
            bs.setFieldTranslater("ZB_JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");// 监理单位
            bs.setFieldTranslater("ZB_SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");// 施工单位
            bs.setFieldDic("XMXZ", "XMXZ");// 项目性质
            // 拦标价（元）
            bs.setFieldThousand("LBJ");
            // 排迁
            bs.setFieldThousand("PQ_ZHTJE");// 总合同金额（元）
            bs.setFieldThousand("PQ_YZF");// 已支付（元）
            bs.setFieldDecimals("PQ_BFB");// %

            // 年度投资金额翻译
            bs.setFieldThousand("TZ_ZTZ");// 总投资
            bs.setFieldThousand("TZ_GC");// 工程
            bs.setFieldThousand("TZ_ZC");// 征拆
            bs.setFieldThousand("TZ_QT");// 其他
            // 总体投资金额翻译
            bs.setFieldThousand("ZTTZ_ZTZ");// 总投资
            bs.setFieldThousand("ZTTZ_GC");// 工程
            bs.setFieldThousand("ZTTZ_ZC");// 征拆
            bs.setFieldThousand("ZTTZ_QT");// 其他
            // 概算金额翻译
            bs.setFieldThousand("GS_ZTZ");// 总投资
            bs.setFieldThousand("GS_GC");// 工程
            bs.setFieldThousand("GS_ZC");// 征拆
            bs.setFieldThousand("GS_QT");// 其他
            // 征收金额
            bs.setFieldThousand("ZS_BRZJ");// 拨入资金（元）
            bs.setFieldThousand("ZS_YSYZJ");// 已使用资金（元）
            bs.setFieldThousand("ZS_YE");// 余额（元）

            bs.setFieldThousand("HT_ZSHTJE");// 征收合同（虚拟）金额（元）
            bs.setFieldThousand("HT_PQHTJE");// 排迁合同金额（元）
            bs.setFieldThousand("HT_SJHTJE");// 设计合同金额（元）
            bs.setFieldThousand("HT_JLHTJE");// 监理合同金额（元）
            bs.setFieldThousand("HT_SGHTJE");// 施工合同金额（元）
            bs.setFieldThousand("HT_QTHTJE");// 其他合同金额（元）
            bs.setFieldThousand("HT_ZHTJE");// 合同总金额（元）
            bs.setFieldThousand("HT_WCTZ");// 完成投资（元）
            bs.setFieldThousand("HT_YZF");// 已支付（元
            bs.setFieldDecimals("HT_BFB1");// %
            bs.setFieldDecimals("HT_BFB2");// %
            
            bs.setFieldThousand("ZJZF_GCFY");// 资金支付信息（元）--工程费用
            bs.setFieldThousand("ZJZF_ZCFY");// 征拆费用
            bs.setFieldThousand("ZJZF_QTFY");// 其他费用
            bs.setFieldThousand("ZJZF_ZZFFY");// 总支付费用
            bs.setFieldThousand("JS_GCJE");// 结算信息（元）--（工程金额
            bs.setFieldThousand("JS_ZCJE");// 征拆金额
            bs.setFieldThousand("JS_QTJE");// 其他金额
            bs.setFieldThousand("JS_ZJSJE");// 总结算金额

            domResult = bs.getJson();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnetion(conn);
        }
        return domResult;
    }

    public List<autocomplete> xmmcAutoComplete(autocomplete json, User user) throws Exception {
        List<autocomplete> autoResult = new ArrayList<autocomplete>();
        autocomplete ac = new autocomplete();
        String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
        condition += BusinessUtil.getSJYXCondition(null);
        condition += BusinessUtil.getCommonCondition(user, null);
        String[][] result = DBUtil.query("select distinct xmmc from GC_TCJH_XMCBK " + json.getTablePrefix() + " where " + condition);
        if (null != result && result.length > 0) {
            for (int i = 0; i < result.length; i++) {
                ac = new autocomplete();
                ac.setRegionName(result[i][0]);
                autoResult.add(ac);
            }
        }
        return autoResult;
    }

    public List<autocomplete> bdmcAutoComplete(autocomplete json, User user) throws Exception {
        List<autocomplete> autoResult = new ArrayList<autocomplete>();
        autocomplete ac = new autocomplete();
        String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
        condition += BusinessUtil.getSJYXCondition(null);
        condition += BusinessUtil.getCommonCondition(user, null);
        String[][] result = DBUtil.query("select distinct bdmc from gc_xmbd " + json.getTablePrefix() + " where " + condition);
        if (null != result && result.length > 0) {
            for (int i = 0; i < result.length; i++) {
                ac = new autocomplete();
                ac.setRegionName(result[i][0]);
                autoResult.add(ac);
            }
        }
        return autoResult;
    }

	public String queryXmzhxxFenLei(String json, User user) {
		  Connection conn = DBUtil.getConnection();
	        String domResult = "";
	        try {
	            QueryConditionList list = RequestUtil.getConditionList(json);
	            String condition = "";
	            String nd = "";
	            for (int i = 0; i < list.size(); i++) {
	            	QueryCondition qc = list.getQueryCondition(i);
	                if ("IS_WC_PQ".equals(qc.getField())) { // 是否完成排迁
	                    if ("0".equals(qc.getValue())) {
	                    	// 未完成
	                    	condition += " and pq_SJ is null ";
						} else {
	                    	// 完成
	                    	condition += " and pq_SJ is not null ";
						}
	                    continue;
					} else if("IS_CQ_PQ".equals(qc.getField())) { // 是否超期排迁
						if ("0".equals(qc.getValue())) {
	                    	// 未超期
	                    	condition += " and pq_SJ <= pq ";
						} else {
	                    	// 超期
	                    	condition += " and pq_SJ > pq ";
						}
	                    continue;
					} else if("IS_WC_ZQ".equals(qc.getField())) { // 是否完成征拆
						if ("0".equals(qc.getValue())) {
	                    	// 未完成
	                    	condition += " and zc_sj is null ";
						} else {
	                    	// 完成
	                    	condition += " and zc_sj is not null ";
						}
	                    continue;
					} else if("IS_CQ_ZQ".equals(qc.getField())) { // 是否超期征拆
						if ("0".equals(qc.getValue())) {
	                    	// 未完成
	                    	condition += " and zc_sj <= zc ";
						} else {
	                    	// 完成
	                    	condition += " and zc_sj > zc ";
						}
	                    continue;
					} else if("ISKG".equals(qc.getField())) { // 是否开工
						if ("0".equals(qc.getValue())) {
	                    	// 未开工
	                    	condition += " and kgsj_sj IS NULL ";
						} else {
	                    	// 已开工
	                    	condition += " and kgsj_sj IS NOT NULL ";
						}
	                    continue;
					} else if("IS_AQ_KG".equals(qc.getField())) { // 是否按期开工
						if ("0".equals(qc.getValue())) {
	                    	// 按期开工
	                    	condition += " and kgsj_sj <= kgsj ";
						} else {
	                    	// 延期开工
	                    	condition += " and kgsj_sj > kgsj ";
						}
	                    continue;
					} else if("ISWG".equals(qc.getField())) { // 是否完工
						if ("0".equals(qc.getValue())) {
	                    	// 未完工
	                    	condition += " and WGSJ_SJ IS NULL ";
						} else {
	                    	// 已完工
	                    	condition += " and WGSJ_SJ IS NOT NULL ";
						}
	                    continue;
					} else if("IS_AQ_WG".equals(qc.getField())) { // 是否按期完工
						if ("0".equals(qc.getValue())) {
	                    	// 按期开工
	                    	condition += " and WGSJ_SJ <= WGSJ ";
						} else {
	                    	// 延期开工
	                    	condition += " and WGSJ_SJ > WGSJ ";
						}
	                    continue;
					}
					else if("ND".equals(qc.getField())) { // 获得统计年度
						nd = qc.getValue();
	                    //continue;
					}
//	                System.out.println(qc.getLogic()+" "+qc.getField()+" "+qc.getOperation()+"'" + qc.getValue() + "'");

	                condition += qc.getLogic()+" "+qc.getField()+" "+qc.getOperation()+" '" + qc.getValue() + "' ";
				}
	            condition = condition.startsWith("and") ? condition.substring(3, condition.length()).trim() : condition;
	            String orderFilter = RequestUtil.getOrderFilter(json);
	            
	            condition += orderFilter;
	            PageManager page = RequestUtil.getPageManager(json);
	            page.setFilter(condition);
	            // （ 项管公司）
	            String querySql = "select * from (SELECT "
	                    // 甘特图
	            		+ "'0' as LY,'' as XMS,'' as ZXMS,T.GC_JH_SJ_ID AS GTT,T.PXH,T.XMID,"
	                    // 健康状况:需要各节点的计划、实际完成时间和是否需要办理节点，在这里集中获取，由前台处理
	                    + "T.XMBH AS JKZK,ISKYPF,ISHPJDS,ISGCXKZ,ISSGXK,ISCBSJPF,ISCQT,ISPQT,ISSGT,ISTBJ,ISCS,ISJLDW,ISSGDW,ISZC,ISPQ,ISJG,ISJS,"
                    + "KGSJ,KGSJ_SJ,WGSJ,WGSJ_SJ,KYPF,KYPF_SJ,HPJDS,HPJDS_SJ,GCXKZ,GCXKZ_SJ,SGXK,SGXK_SJ,CBSJPF,CBSJPF_SJ,CQT,CQT_SJ,"
                    + "PQT,PQT_SJ,SGT,SGT_SJ,TBJ,TBJ_SJ,CS,CS_SJ,JLDW,JLDW_SJ,SGDW,SGDW_SJ,ZC,ZC_SJ,PQ,PQ_SJ,JG,JG_SJ,JS_SJ,XMBS,BDID,"
                    // 项目编号
                    + " T.XMBH,"
                    // 项目名称
                    + " T.XMMC,"
                    // 建设位置
                    + "DECODE(T.XMBS,'1',(SELECT BDDD FROM GC_XMBD WHERE GC_XMBD_ID = T.BDID AND ROWNUM=1),(SELECT XMDZ FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1))AS JSDZ,"
                    // 项目地址图标( ↓ )
                    + "'none3' XXJT,"
                    // 项目管理公司
                    + " (SELECT XMGLGS FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = t.XMID and ROWNUM=1)AS XMGLGS, "
                    // 标段名称
                    + "BDMC AS BDMC,"
                    // 项目性质
                    + "(SELECT XJXJ FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)AS XMXZ,"
                    // 年度目标
                    + "(SELECT JSMB FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)AS NDMB, "
                    // ======================下方几项的查询内容已在上面给出，前台显示为图标，所以不需要重新获取值===========
                    // 工程进展 （已拆迁 已排迁 已开工 已完工 已交/竣工）
                    + "XMZT(T.ISZC,T.ZC_SJ,T.ZC) GC_YCQ, " 
                    
						
					/*	1：不需要办理
						2：按期完成、
						3：未完成未到期
						4：延期完成
						5：到期未完成*/
                    + "(SELECT X.TEMP_PQZT FROM ( SELECT TEMP_PQZT ,A.GC_JH_SJ_ID FROM GC_JH_SJ A , (SELECT MAX(TEMP_PQZT) TEMP_PQZT ,XMID  FROM ( "
                    + "SELECT T.XMID ,CASE T.ISPQ WHEN '0' THEN '1' WHEN '1' THEN DECODE(T.PQ,'', DECODE(T.PQ_SJ,'','3','2'), DECODE(T.PQ_SJ,'',DECODE(SIGN(SYSDATE-T.PQ),1,'5','3'),DECODE(SIGN(T.PQ_SJ-T.PQ),1,'4','2')))  "
                    + "END TEMP_PQZT FROM GC_JH_SJ T  ) A     GROUP BY A.XMID) B WHERE A.XMID=B.XMID AND A.BDID IS NULL   UNION ALL "
                    + "SELECT CASE T.ISPQ WHEN '0' THEN '1' WHEN '1' THEN DECODE(T.PQ,'', DECODE(T.PQ_SJ,'','3','2'), DECODE(T.PQ_SJ,'',DECODE(SIGN(SYSDATE-T.PQ),1,'5','3'),DECODE(SIGN(T.PQ_SJ-T.PQ),1,'4','2')))  "
                    + "END TEMP_PQZT  ,T.GC_JH_SJ_ID FROM GC_JH_SJ T WHERE XMBS = '1'  ) X WHERE T.GC_JH_SJ_ID=X.GC_JH_SJ_ID ) GC_YPQ , " 
                    
					+ "XMZT('999',T.KGSJ_SJ,T.KGSJ) GC_YKG , " 
					+ "XMZT('999',T.WGSJ_SJ,T.WGSJ) GC_YWG , " 
					+ "JJG_XMZT(T.ISJG, T.ISJS, T.JG_SJ, T.JG, T.JS_SJ) GC_YJJG, "
                    
                    // 手续办理 （可研批复 土地划拨 工程规划许可 施工许可）
                    + "XMZT(T.ISKYPF,T.KYPF_SJ,T.KYPF) SXBL_KYPF," 
                    + "XMZT(T.ISHPJDS,T.HPJDS_SJ,T.HPJDS) SXBL_TDHB," 
                    + "XMZT(T.ISGCXKZ,T.GCXKZ_SJ,T.GCXKZ) SXBL_GCGHXK," 
                    + "XMZT(T.ISSGXK,T.SGXK_SJ,T.SGXK) SXBL_SGXK, "
                    
                    // 设计进展 （ 设计任务书 初设批复 拆迁图 排迁图 施工图 设计变更）
                    // 设计任务书
                    + "(select count(r.gc_sjgl_rwsgl_id) from gc_sjgl_rwsgl r where r.sfyx='1' and r.sjwybh =t.sjwybh ) SJ_SJRWS,"
                    

                    + "XMZT(T.ISCBSJPF,T.CBSJPF_SJ,T.CBSJPF) SJ_CSPF," 
                    + "XMZT(T.ISCQT,T.CQT_SJ,T.CQT) SJ_CQT," 
                    + "XMZT(T.ISPQT,T.PQT_SJ,T.PQT) SJ_PQT," 
                    + "XMZT(T.ISSGT,T.SGT_SJ,T.SGT) SJ_SGT,"
                    
                    // 设计变更数
                    + "NVL(DECODE(T.XMBS,'0', " 
                    + "(SELECT COUNT(bg.GC_SJ_SJBG_JS2_ID) FROM GC_SJ_SJBG_JS2 bg WHERE bg.SFYX = '1' AND bg.BGZT = '1' AND " 
                    + "bg.XMID in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh))," 
                    + "(SELECT COUNT(bg.GC_SJ_SJBG_JS2_ID) FROM GC_SJ_SJBG_JS2 bg WHERE bg.SFYX = '1' AND bg.BGZT = '1' AND " 
                    + "bg.sjwybh = T.sjwybh)  ),0) SJ_BGS, "
                    
                    // =============================================================================================

                    // 拦标价（元） 26
                    + "(SELECT CSSDZ FROM GC_ZJB_LBJB LBJ WHERE LBJ.sjwybh=T.sjwybh) LBJ, "
                    // =============================================================================================

                    // 招标进展 （设计单位
                    + "DECODE(T.XMBS,'1',(SELECT SJDW FROM GC_XMBD BD WHERE GC_XMBD_ID = T.BDID)," 
                    + "(SELECT SJDW FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID) ) AS ZB_SJDW, "

                    // 监理单位
                    + "DECODE(T.XMBS,'1',(SELECT JLDW FROM GC_XMBD BD WHERE GC_XMBD_ID = T.BDID),"
                    + "(SELECT JLDW FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID) ) AS ZB_JLDW,"

                    // 施工单位）
                    + "DECODE(T.XMBS,'1',(SELECT SGDW FROM GC_XMBD BD WHERE GC_XMBD_ID = T.BDID)," 
                    + "(SELECT SGDW FROM GC_TCJH_XMXDK WHERE ND = T.ND AND GC_TCJH_XMXDK_ID = T.XMID) ) AS ZB_SGDW, "
                    // =============================================================================================

                    // 排迁信息 （排迁任务数/完成数
                    + "(TO_CHAR(NVL((SELECT COUNT(ZXM.GC_PQ_ZXM_ID) FROM GC_PQ_ZXM ZXM "
                    + "WHERE ZXM.JHSJID = T.GC_JH_SJ_ID GROUP BY T.GC_JH_SJ_ID), 0))|| '/' || "
                    + "TO_CHAR(NVL((SELECT COUNT(ZXM.GC_PQ_ZXM_ID) FROM GC_PQ_ZXM ZXM "
                    + "WHERE ZXM.JHSJID = T.GC_JH_SJ_ID  "
                    + "AND WCSJ IS NOT NULL GROUP BY T.GC_JH_SJ_ID), 0))) PQ_RWS_WCS, "

                    // 总合同金额（元）
                    + "NVL((SELECT SUM(C.ZHTQDJ) FROM GC_PQ_ZXM A, GC_HTGL_HT C "
                    + "WHERE A.JHSJID = T.GC_JH_SJ_ID "
                    + "AND C.ID = A.HTID GROUP BY  T.GC_JH_SJ_ID),0) PQ_ZHTJE, "
                    

                    // 已支付（元））33
                    + "NVL((SELECT SUM(C.ZHTZF) FROM GC_PQ_ZXM A, GC_HTGL_HT C "
                    + "WHERE A.JHSJID = T.GC_JH_SJ_ID "
                    + "AND C.ID = A.HTID GROUP BY  T.GC_JH_SJ_ID),0) PQ_YZF, "
                    

                    // 百分比
                    + "NVL((SELECT TO_CHAR(DECODE(NVL(SUM(ZHTQDJ),0),0,0, "
                    + "ROUND((SUM(C.ZHTZF))/SUM(ZHTQDJ)*100,2)),'FM99999999999990.00') "
                    + "FROM GC_PQ_ZXM A, GC_HTGL_HT C "
                    + "WHERE A.JHSJID = T.GC_JH_SJ_ID "
                    + "AND C.ID = A.HTID GROUP BY  T.GC_JH_SJ_ID),0) PQ_BFB, "
                    // =============================================================================================

                    
                    
                    // 征收信息 （总居民数/已完拆迁 总企业数/已完拆迁 征地面积（m2）/已完征地 ）
                    // （拨入资金（元） 已使用资金（元） 余额（元））39
                    + "(NVL((SELECT SUM(JMHS) FROM GC_ZSB_XXB XXB WHERE XXB.JHSJID = T.GC_JH_SJ_ID AND XXB.SFYX = '1'), 0) || '/' || NVL((SELECT SUM(BCWCJMS) FROM GC_ZSB_JDB JDB WHERE JDB.JHSJID = T.GC_JH_SJ_ID AND JDB.SFYX = '1'), 0) ) ZS_ZJMS_JWCQ , "
                    + "(NVL((SELECT SUM(QYJS) FROM GC_ZSB_XXB XXB WHERE XXB.JHSJID = T.GC_JH_SJ_ID AND XXB.SFYX = '1'), 0) || '/' || NVL((SELECT SUM(BCWCQYS) FROM GC_ZSB_JDB JDB WHERE JDB.JHSJID = T.GC_JH_SJ_ID AND JDB.SFYX = '1'), 0) ) ZS_ZQYS_YWCQ , "
                    + "(NVL((SELECT SUM(ZMJ) FROM GC_ZSB_XXB XXB WHERE XXB.JHSJID = T.GC_JH_SJ_ID AND XXB.SFYX = '1'), 0) || '/' || NVL((SELECT SUM(BCZDMJ) FROM GC_ZSB_JDB JDB WHERE JDB.JHSJID = T.GC_JH_SJ_ID AND JDB.SFYX = '1'), 0) ) ZS_ZDMJ_YWZD, "
                    + "NVL((SELECT SUM(ZJDWJE) FROM GC_ZSB_XXB XXB WHERE XXB.JHSJID = T.GC_JH_SJ_ID AND XXB.SFYX = '1'), 0) ZS_BRZJ, "
        
                    + "NVL((SELECT SUM(HTZF.ZFJE) AS YSYJE  "
                    + "			FROM 	GC_HTGL_HTSJ 	HTSJ, "
                    + "					GC_HTGL_HT_HTZF HTZF, "
                    + "					GC_HTGL_HT      HT "
                    + "			WHERE HTZF.HTSJID = HTSJ.JHSJID  "
                    + "         AND HT.ID = HTSJ.HTID "
                    + "         AND HT.SFYX = '1' AND HT.HTLX = 'CQ' "
                    + "         AND HTZF.SFYX = '1' AND HTZF.SFDZF = '0' "
                    + "         AND HTSJ.SFYX = '1' "
                    + "         AND HTSJ.JHSJID = T.GC_JH_SJ_ID "
                    + "         GROUP BY HTSJ.JHSJID),0) ZS_YSYZJ, "
                 
                    + "(NVL((SELECT SUM(ZJDWJE) FROM GC_ZSB_XXB XXB  "
                    + "			WHERE XXB.JHSJID = T.GC_JH_SJ_ID AND XXB.SFYX = '1'),0)- "
                    + "NVL((SELECT SUM(HTZF.ZFJE) AS YSYJE FROM GC_HTGL_HTSJ HTSJ, "
                    + "         GC_HTGL_HT_HTZF HTZF, "
                    + "         GC_HTGL_HT      HT "
                    + "         WHERE HTZF.HTSJID = HTSJ.JHSJID  "
                    + "         AND HT.ID = HTSJ.HTID "
                    + "         AND HT.SFYX = '1' AND HT.HTLX = 'CQ' "
                    + "         AND HTZF.SFYX = '1' AND HTZF.SFDZF = '0' "
                    + "         AND HTSJ.SFYX = '1' "
                    + "         AND HTSJ.JHSJID = T.GC_JH_SJ_ID "
                    + "         GROUP BY HTSJ.JHSJID),0)) ZS_YE, "

                    // 合同信息 （已签合同数 征收合同（虚拟）金额（元） 排迁合同金额（元） 设计合同金额（元））
                    // （监理合同金额（元） 施工合同金额（元） 其他合同金额（元） 合同总金额（元）
                    // （完成投资（元） % 已支付（元） %）51

                    + " NVL(decode(t.xmbs,0,(select COUNT(DISTINCT htid) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh and vd.htzt>0 ),(select COUNT(DISTINCT htid) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh and vd.htzt>0 )),0) AS HT_JQHTS,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx='CQ'),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh AND vd.htlx='CQ')),0) AS HT_ZSHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx='PQ'),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh AND vd.htlx='PQ')),0) AS HT_PQHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx='SJ'),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh AND vd.htlx='SJ')),0) AS HT_SJHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx='JL'),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh AND vd.htlx='JL')),0) AS HT_JLHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx='SG'),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh AND vd.htlx='SG')),0) AS HT_SGHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)  AND vd.htlx NOT IN ('CQ','PQ','SG','SJ','JL')),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh  AND vd.htlx NOT IN ('CQ','PQ','SG','SJ','JL'))),0) AS HT_QTHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)),(select decode(sum(vd.htqdj), NULL, 0, sum(vd.htqdj)) from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh)),0) AS HT_ZHTJE,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.wczf),  NULL, 0, sum(vd.wczf))  from view_ht_htsj_jhsj_mx vd WHERE  vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)),(select decode(sum(vd.wczf),  NULL, 0, sum(vd.wczf))  from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh)),0) AS HT_WCTZ,"
                    + " NVL(decode(t.xmbs,0,(SELECT DECODE(NVL(SUM(htqdj), 0), 0, 0, round(SUM(vd.wczf) / sum(vd.htqdj) * 100, 2)) from view_ht_htsj_jhsj_mx vd WHERE vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)),(SELECT DECODE(NVL(SUM(htqdj), 0), 0, 0, round(SUM(vd.wczf) / sum(vd.htqdj) * 100, 2)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh = t.sjwybh)),0) AS HT_BFB1,"
                    + " NVL(decode(t.xmbs,0,(select decode(sum(vd.htzt), NULL, 0, sum(vd.htzf)) from view_ht_htsj_jhsj_mx vd WHERE vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)),(select decode(SUM(vd.htzt), null, 0, SUM(vd.htzf)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh=t.sjwybh)),0) AS HT_YZF,"
                    + " NVL(decode(t.xmbs,0,(select DECODE(NVL(SUM(htqdj), 0), 0, 0, round(SUM(vd.htzf) / sum(vd.htqdj) * 100, 2)) from view_ht_htsj_jhsj_mx vd WHERE vd.xmid in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh)),(select DECODE(NVL(SUM(htqdj), 0), 0, 0, round(SUM(vd.htzf) / sum(vd.htqdj) * 100, 2)) from view_ht_htsj_jhsj_mx vd WHERE vd.sjwybh = t.sjwybh)),0) AS HT_BFB2,"
                    

                    // 年度投资估算（万元）--工程
                    + "DECODE(XMBS,'1','',NVL((SELECT GC FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS TZ_GC,"
                    
                    // 年度投资估算（万元）--征拆
                    + "DECODE(XMBS,'1','',NVL((SELECT ZC FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS TZ_ZC,"
                    
                    // 年度投资估算（万元）--其他
                    + "DECODE(XMBS,'1','',NVL((SELECT QT FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS TZ_QT,"
                    
                    // 年度投资估算（万元）--总投资
                    + "DECODE(XMBS,'1','',NVL((SELECT JHZTZE FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS TZ_ZTZ, "
                    //--------------------------总体投资--------------------------------//
                    // 总体投资估算（万元）--工程
                    + "DECODE(XMBS,'1','',NVL((SELECT ZTGC FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS ZTTZ_GC,"
                    
                    // 总体投资估算（万元）--征拆
                    + "DECODE(XMBS,'1','',NVL((SELECT ZTZC FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS ZTTZ_ZC,"
                    
                    // 总体投资估算（万元）--其他
                    + "DECODE(XMBS,'1','',NVL((SELECT ZTQT FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS ZTTZ_QT,"
                    
                    // 总体投资估算（万元）--总投资
                    + "DECODE(XMBS,'1','',NVL((SELECT ZTZTZE FROM GC_TCJH_XMXDK WHERE GC_TCJH_XMXDK_ID = T.XMID AND ROWNUM=1)/10000,0)) AS ZTTZ_ZTZ, "

                    // 概算信息（元）（工程 征拆 其他 总投资 ）
                    + "NVL(NVL2(BDID,(SELECT NVL(SUM(CBSJ.GCJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.sjwybh=T.sjwybh),(SELECT NVL(SUM(CBSJ.GCJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.XMID in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh))),0)  AS GS_GC, " 
                    + "NVL(NVL2(BDID,(SELECT NVL(SUM(CBSJ.ZCJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.sjwybh=T.sjwybh),(SELECT NVL(SUM(CBSJ.ZCJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.XMID in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh))),0)  AS GS_ZC, " 
                    + "NVL(NVL2(BDID,(SELECT NVL(SUM(CBSJ.QTJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.sjwybh=T.sjwybh),(SELECT NVL(SUM(CBSJ.QTJE),0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.XMID in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh))),0)  AS GS_QT, " 
                    + "NVL(NVL2(BDID,(SELECT NVL(SUM(CBSJ.GS),  0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.sjwybh=T.sjwybh),(SELECT NVL(SUM(CBSJ.GS),  0) FROM GC_SJ_CBSJPF CBSJ WHERE SFYX='1' AND CBSJ.XMID in (select sj.xmid from gc_jh_sj sj where sj.sjwybh=t.sjwybh))),0) AS GS_ZTZ, "

                    // 资金支付信息（元）（工程费用 征拆费用 其他费用 总支付费用）
                    + "(SELECT decode(SUM(vz.zfje), NULL, 0, SUM(vz.zfje)) FROM VIEW_GBMJK_XMZHXX_ZJZFXX vz WHERE vz.sjwybh=t.sjwybh AND vz.htlx='SG') AS ZJZF_GCFY,"
                    + "(SELECT decode(SUM(vz.zfje), NULL, 0, SUM(vz.zfje)) FROM VIEW_GBMJK_XMZHXX_ZJZFXX vz WHERE vz.sjwybh=t.sjwybh AND vz.htlx='CQ') AS ZJZF_ZCFY,"
                    + "(SELECT decode(SUM(vz.zfje), NULL, 0, SUM(vz.zfje)) FROM VIEW_GBMJK_XMZHXX_ZJZFXX vz WHERE vz.sjwybh=t.sjwybh AND vz.htlx NOT IN ('SG', 'CQ')) AS ZJZF_QTFY,"
                    + "(SELECT decode(SUM(vz.zfje), NULL, 0, SUM(vz.zfje)) FROM VIEW_GBMJK_XMZHXX_ZJZFXX vz WHERE vz.sjwybh=t.sjwybh) AS ZJZF_ZZFFY,"

                    // // 结算信息（元）（工程金额 征拆金额 其他金额 总结算金额）
                    + "(select decode(sum(vd.htjs), null, 0, sum(vd.htjs)) from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh  AND vd.htlx='SG') AS JS_GCJE,"
                    + "(select decode(sum(vd.htjs), null, 0, sum(vd.htjs)) from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh  AND vd.htlx='CQ') AS JS_ZCJE,"
                    + "(select decode(sum(vd.htjs), null, 0, sum(vd.htjs)) from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh  AND vd.htlx NOT IN ('SG', 'CQ')) AS JS_QTJE,"
                    + "(select decode(sum(vd.htjs), null, 0, sum(vd.htjs)) from view_ht_htsj_jhsj_mx vd WHERE  vd.sjwybh=t.sjwybh) AS JS_ZJSJE,ND, "
                    + "(select decode(decode(sum(g1.TBJE), null, 0, sum(g1.TBJE)), 0, 0, ((decode(sum(g1.TBJE), null, 0, sum(g1.TBJE))-decode(sum(g1.CSSDJE), null, 0 , sum(g1.CSSDJE)))/decode(sum(g1.TBJE), null, 0, sum(g1.TBJE)))) from GC_ZJB_JSB g1 where g1.BDID = t.BDID ) SJB"
                    
                    + " from GC_JH_SJ t) xmzhxx " ;
	            
	            BaseResultSet bs = DBUtil.query(conn, querySql, page);
	            // bs.setFieldOrgDept("XMGLGS");
	            bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");// 项目管理公司
	            bs.setFieldTranslater("ZB_SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");// 设计单位
	            bs.setFieldTranslater("ZB_JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");// 监理单位
	            bs.setFieldTranslater("ZB_SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");// 施工单位
	            bs.setFieldDic("XMXZ", "XMXZ");// 项目性质
	            // 拦标价（元）
	            bs.setFieldThousand("LBJ");
	            // 排迁
	            bs.setFieldThousand("PQ_ZHTJE");// 总合同金额（元）
	            bs.setFieldThousand("PQ_YZF");// 已支付（元）
	            bs.setFieldDecimals("PQ_BFB");// %

	            // 年度投资金额翻译
	            bs.setFieldThousand("TZ_ZTZ");// 总投资
	            bs.setFieldThousand("TZ_GC");// 工程
	            bs.setFieldThousand("TZ_ZC");// 征拆
	            bs.setFieldThousand("TZ_QT");// 其他
	            // 总体投资金额翻译
	            bs.setFieldThousand("ZTTZ_ZTZ");// 总投资
	            bs.setFieldThousand("ZTTZ_GC");// 工程
	            bs.setFieldThousand("ZTTZ_ZC");// 征拆
	            bs.setFieldThousand("ZTTZ_QT");// 其他
	            // 概算金额翻译
	            bs.setFieldThousand("GS_ZTZ");// 总投资
	            bs.setFieldThousand("GS_GC");// 工程
	            bs.setFieldThousand("GS_ZC");// 征拆
	            bs.setFieldThousand("GS_QT");// 其他
	            // 征收金额
	            bs.setFieldThousand("ZS_BRZJ");// 拨入资金（元）
	            bs.setFieldThousand("ZS_YSYZJ");// 已使用资金（元）
	            bs.setFieldThousand("ZS_YE");// 余额（元）

	            bs.setFieldThousand("HT_ZSHTJE");// 征收合同（虚拟）金额（元）
	            bs.setFieldThousand("HT_PQHTJE");// 排迁合同金额（元）
	            bs.setFieldThousand("HT_SJHTJE");// 设计合同金额（元）
	            bs.setFieldThousand("HT_JLHTJE");// 监理合同金额（元）
	            bs.setFieldThousand("HT_SGHTJE");// 施工合同金额（元）
	            bs.setFieldThousand("HT_QTHTJE");// 其他合同金额（元）
	            bs.setFieldThousand("HT_ZHTJE");// 合同总金额（元）
	            bs.setFieldThousand("HT_WCTZ");// 完成投资（元）
	            bs.setFieldThousand("HT_YZF");// 已支付（元
	            bs.setFieldDecimals("HT_BFB1");// %
	            bs.setFieldDecimals("HT_BFB2");// %
	            
	            bs.setFieldThousand("ZJZF_GCFY");// 资金支付信息（元）--工程费用
	            bs.setFieldThousand("ZJZF_ZCFY");// 征拆费用
	            bs.setFieldThousand("ZJZF_QTFY");// 其他费用
	            bs.setFieldThousand("ZJZF_ZZFFY");// 总支付费用
	            bs.setFieldThousand("JS_GCJE");// 结算信息（元）--（工程金额
	            bs.setFieldThousand("JS_ZCJE");// 征拆金额
	            bs.setFieldThousand("JS_QTJE");// 其他金额
	            bs.setFieldThousand("JS_ZJSJE");// 总结算金额

	            domResult = bs.getJson();
	            
	            //按照子项目建立项目群树形结构
	            String gc_xmflglb_sql = "select id, nd, xmid, xmmc, parent, orderno, ly, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx from gc_xmflglb where nd='"+nd+"' and sfyx = '1' order by orderno";
	            QuerySet qs = DBUtil.executeQuery(gc_xmflglb_sql, null,conn);
	            String json_ = "";
	            if(qs.getRowCount()>0)
	            {
	            	json_ = "{\"response\":";
	            	json_ +="{\"data\":[";
	            	JSONObject resultObject = JSONObject.fromObject(domResult);
	        		String resultObject_txt = resultObject.getString("response");
	        		JSONObject dataObject = JSONObject.fromObject(resultObject_txt);
	        		String dataObject_txt = dataObject.getString("data");
	        	//	JSONObject pageObject = JSONObject.fromObject(resultObject_txt);
	        		String pageObject_txt = resultObject.getString("pages");
	        		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(dataObject_txt);
	        		//设置一个空行的json信息
	        		JSONArray jsonArray_temp = jsonArray;
	        	//	JSONObject empty_json_temp = jsonArray_temp.getJSONObject(0);
	        		JSONObject temp_obj = jsonArray_temp.getJSONObject(0);
	        		String empty_json_txt = temp_obj.toString();
	        		JSONObject empty_json = JSONObject.fromObject(empty_json_txt);
	        		List<Object> arr = empty_json.names();
	    			for (Object name : arr) {
	    				String key = name.toString().toUpperCase();
	    				empty_json.element(key, "");
	    			}
	    			empty_json_txt = empty_json.toString();
	    			JSONObject temp_data = null;
	    			HashMap xm_json_txt = new HashMap();
	    			for(int j=0;j<jsonArray.size();j++)
	    			{
	    				temp_data = jsonArray.getJSONObject(j);
	    				String xmid = temp_data.getString("XMID");
	    				xm_json_txt.put(xmid, temp_data.toString());
	    			}
	    			
	        		
	    			Iterator iter = jsonArray.iterator();
	    			
	    			int xms = 1;
	    			int zxms = 1;

	            	for(int i=0;i<qs.getRowCount();i++)
	            	{
	            		String ly = qs.getString(i+1, "LY");
	            		String parent = qs.getString(i+1, "PARENT");
	            		
	            		if("1".equals(ly)){
	            			JSONObject temp = JSONObject.fromObject(empty_json_txt);
	            			temp.element("XMMC", qs.getString(i+1, "xmmc"));//根项目的json对象
	            			temp.element("LY", "1");//
	            			String xmid = qs.getString(i+1, "XMID");
	            			returnList = new ArrayList<String>();
	            			List<String> childNodes = getChildNodes(qs,xmid);
	            			boolean hasQuerychild = false;
	            			if(childNodes==null) continue; 
	            			Iterator<String> it = childNodes.iterator();
	            			Double tz_gc = 0.00;//年度总投资估算 工程
	            			Double tz_zc =  0.00;//年度总投资估算 征拆
	            			Double tz_qt =  0.00;//年度总投资估算 其他
	            			Double tz_ztz = 0.00;//年度总投资估算 总投资
	            			Double zttz_gc =  0.00;//总体投资估算 工程
	            			Double zttz_zc =  0.00;//总体投资估算 征拆
	            			Double zttz_qt =  0.00;//总体投资估算 其他
	            			Double zttz_ztz =  0.00;//总体投资估算 总投资
	            			Double GS_GC = 0.00;//  概算信息 工程
	            			Double GS_ZC =  0.00;//  概算信息 征拆
	            			Double GS_QT =  0.00;//  概算信息 其他
	            			Double GS_ZTZ =  0.00;//  概算信息 总投资
	            			Double ZJZF_GCFY =  0.00;//资金支付信息 工程费用
	            			Double ZJZF_ZCFY =  0.00;//资金支付信息 征拆费用
	            			Double ZJZF_QTFY =  0.00;//资金支付信息 其他费用
	            			Double ZJZF_ZZFFY =  0.00;//资金支付信息 总支付费用
	            			Double JS_GCJE =  0.00;//结算信息 工程金额
	            			Double JS_ZCJE =  0.00;//结算信息 征拆金额
	            			Double JS_QTJE =  0.00;//结算信息 其他金额
	            			Double JS_ZJSJE =  0.00;//结算信息 总结算金额


	            			while (it.hasNext()) {
	            				String n = (String) it.next();
	            				String xm_txt =(String) xm_json_txt.get(n);
	            				if(Pub.empty(xm_txt)){
	            					hasQuerychild = false;
	            				}else{
	            					hasQuerychild = true;
	            					JSONObject child_json = JSONObject.fromObject(xm_txt);
	            					String tz_gc_c = child_json.getString("TZ_GC");//年度总投资估算 工程
	            					if(!Pub.empty(tz_gc_c)){
	            						Double s =  Double.parseDouble(tz_gc_c);
	            						tz_gc = tz_gc+s;
	            					}
	            					String tz_zc_c = child_json.getString("TZ_ZC");//年度总投资估算 征拆
	            					if(!Pub.empty(tz_zc_c)){
	            						Double s =  Double.parseDouble(tz_gc_c);
	            						tz_zc = tz_zc+s;
	            					}
	            					String tz_qt_c = child_json.getString("TZ_QT");//年度总投资估算 其他
	            					if(!Pub.empty(tz_qt_c)){
	            						Double s =  Double.parseDouble(tz_qt_c);
	            						tz_qt = tz_qt+s;
	            					}
	            					String tz_ztz_c = child_json.getString("TZ_ZTZ");//年度总投资估算 总投资
	            					if(!Pub.empty(tz_ztz_c)){
	            						Double s =  Double.parseDouble(tz_ztz_c);
	            						tz_ztz = tz_ztz+s;
	            					}
	            					String zttz_gc_c = child_json.getString("ZTTZ_GC");//总体投资估算 工程
	            					if(!Pub.empty(zttz_gc_c)){
	            						Double s =  Double.parseDouble(zttz_gc_c);
	            						zttz_gc = zttz_gc+s;
	            					}
	            					String zttz_zc_c = child_json.getString("ZTTZ_ZC");//总体投资估算 征拆
	            					if(!Pub.empty(zttz_zc_c)){
	            						Double s = Double.parseDouble(zttz_zc_c);
	            						zttz_zc = zttz_zc+s;
	            					}
	            					String zttz_qt_c = child_json.getString("ZTTZ_QT");//总体投资估算 其他
	            					if(!Pub.empty(zttz_qt_c)){
	            						Double s =  Double.parseDouble(zttz_qt_c);
	            						zttz_qt = zttz_qt+s;
	            					}
	            					String zttz_ztz_c = child_json.getString("ZTTZ_ZTZ");//总体投资估算 总投资
	            					if(!Pub.empty(zttz_ztz_c)){
	            						Double s =  Double.parseDouble(zttz_ztz_c);
	            						zttz_ztz = zttz_ztz+s;
	            					}
	            					String GS_GC_c = child_json.getString("GS_GC");//概算信息 工程
	            					if(!Pub.empty(GS_GC_c)){
	            						Double s =  Double.parseDouble(GS_GC_c);
	            						GS_GC_c = GS_GC_c+s;
	            					}
	            					String GS_ZC_c = child_json.getString("GS_ZC");//概算信息 征拆
	            					if(!Pub.empty(GS_ZC_c)){
	            						Double s =  Double.parseDouble(GS_ZC_c);
	            						GS_ZC_c = GS_ZC_c+s;
	            					}
	            					String GS_QT_c = child_json.getString("GS_QT");//概算信息 其他
	            					if(!Pub.empty(GS_QT_c)){
	            						Double s =  Double.parseDouble(GS_QT_c);
	            						GS_QT_c = GS_QT_c+s;
	            					}
	            					String GS_ZTZ_c = child_json.getString("GS_ZTZ");//概算信息 	总投资	
	            					if(!Pub.empty(GS_ZTZ_c)){
	            						Double s =  Double.parseDouble(GS_ZTZ_c);
	            						GS_ZTZ_c = GS_ZTZ_c+s;
	            					}
	            					String ZJZF_GCFY_c = child_json.getString("ZJZF_GCFY");//资金支付信息 	工程费用	
	            					if(!Pub.empty(ZJZF_GCFY_c)){
	            						Double s =  Double.parseDouble(ZJZF_GCFY_c);
	            						ZJZF_GCFY_c = ZJZF_GCFY_c+s;
	            					}
	            					String ZJZF_ZCFY_c = child_json.getString("ZJZF_ZCFY");//资金支付信息 	征拆费用
	            					if(!Pub.empty(ZJZF_ZCFY_c)){
	            						Double s =  Double.parseDouble(ZJZF_ZCFY_c);
	            						ZJZF_ZCFY_c = ZJZF_ZCFY_c+s;
	            					}
	            					String ZJZF_QTFY_c = child_json.getString("ZJZF_QTFY");//资金支付信息 	其他费用
	            					if(!Pub.empty(ZJZF_QTFY_c)){
	            						Double s = Double.parseDouble(ZJZF_QTFY_c);
	            						ZJZF_QTFY_c = ZJZF_QTFY_c+s;
	            					}
	            					String ZJZF_ZZFFY_c = child_json.getString("ZJZF_ZZFFY");//资金支付信息 	总支付费用
	            					if(!Pub.empty(ZJZF_ZZFFY_c)){
	            						Double s =  Double.parseDouble(ZJZF_ZZFFY_c);
	            						ZJZF_ZZFFY_c = ZJZF_ZZFFY_c+s;
	            					}
	            					
	            					String JS_GCJE_c = child_json.getString("JS_GCJE");// 结算信息 	工程金额
	            					if(!Pub.empty(JS_GCJE_c)){
	            						Double s =  Double.parseDouble(JS_GCJE_c);
	            						JS_GCJE_c = JS_GCJE_c+s;
	            					}
	            					String JS_ZCJE_c = child_json.getString("JS_ZCJE");// 结算信息 	征拆金额
	            					if(!Pub.empty(JS_GCJE_c)){
	            						Double s =  Double.parseDouble(JS_ZCJE_c);
	            						JS_ZCJE_c = JS_ZCJE_c+s;
	            					}
	            					String JS_QTJE_c = child_json.getString("JS_QTJE");// 结算信息 	其他金额
	            					if(!Pub.empty(JS_QTJE_c)){
	            						Double s = Double.parseDouble(JS_QTJE_c);
	            						JS_QTJE_c = JS_QTJE_c+s;
	            					}
	            					String JS_ZJSJE_c = child_json.getString("JS_ZJSJE");// 结算信息 	总结算金额
	            					if(!Pub.empty(JS_ZJSJE_c)){
	            						Double s =  Double.parseDouble(JS_ZJSJE_c);
	            						JS_ZJSJE_c = JS_ZJSJE_c+s;
	            					}
	            					
	            				}
	            			}
	            			if(tz_gc>-1){//年度总投资估算 工程
	            				temp.element("TZ_GC", String.valueOf(tz_gc));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(tz_gc));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("TZ_GC_SV", sv);
	            			}
	            			if(tz_zc>-1){//年度总投资估算 征拆
	            				temp.element("TZ_ZC", String.valueOf(tz_zc));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(tz_zc));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("TZ_ZC_SV", sv);
	            			}
	            			if(tz_qt>-1){//年度总投资估算 其他
	            				temp.element("TZ_QT", String.valueOf(tz_qt));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(tz_qt));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("TZ_QT_SV", sv);
	            			}
	            			if(tz_ztz>-1){//年度总投资估算 总投资
	            				temp.element("TZ_ZTZ", String.valueOf(tz_ztz));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(tz_ztz));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("TZ_ZTZ_SV", sv);
	            			}
	            			if(zttz_gc>-1){//总体投资估算 工程
	            				temp.element("ZTTZ_GC", String.valueOf(zttz_gc));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(zttz_gc));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("ZTTZ_GC_SV", sv);
	            			}
	            			if(zttz_zc>-1){//总体投资估算 征拆
	            				temp.element("ZTTZ_ZC", String.valueOf(zttz_zc));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(zttz_zc));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("ZTTZ_ZC_SV", sv);
	            			}
	            			if(zttz_zc>-1){//总体投资估算其他
	            				temp.element("ZTTZ_QT", String.valueOf(zttz_qt));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(zttz_qt));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("ZTTZ_QT_SV", sv);
	            			}
	            			if(zttz_ztz>-1){//总体投资估算总投资
	            				temp.element("ZTTZ_ZTZ", String.valueOf(zttz_ztz));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(zttz_ztz));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("ZTTZ_ZTZ_SV", sv);
	            			}
	            			if(GS_GC>-1){//概算信息 工程
	            				temp.element("GS_GC", String.valueOf(GS_GC));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(GS_GC));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("GS_GC_SV", sv);
	            			}
	            			if(GS_ZC>-1){//概算信息 征拆
	            				temp.element("GS_ZC", String.valueOf(GS_ZC));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(GS_ZC));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("GS_ZC_SV", sv);
	            			}
	            			if(GS_QT>-1){//概算信息其他
	            				temp.element("GS_QT", String.valueOf(GS_QT));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(GS_QT));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("GS_QT_SV", sv);
	            			}
	            			if(GS_ZTZ>-1){//概算信息总投资
	            				temp.element("GS_ZTZ", String.valueOf(GS_ZTZ));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(GS_ZTZ));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("GS_ZTZ_SV", sv);
	            			}
	            			if(ZJZF_GCFY>-1){//资金支付信息 工程费用
	            				temp.element("ZJZF_GCFY", String.valueOf(ZJZF_GCFY));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(ZJZF_GCFY));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("ZJZF_GCFY_SV", sv);
	            			}
	            			if(ZJZF_ZCFY>-1){//资金支付信息 征拆费用
	            				temp.element("ZJZF_ZCFY", String.valueOf(ZJZF_ZCFY));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(ZJZF_ZCFY));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("ZJZF_ZCFY_SV", sv);
	            			}
	            			if(ZJZF_QTFY>-1){//资金支付信息 其他费用
	            				temp.element("ZJZF_QTFY", String.valueOf(ZJZF_QTFY));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(ZJZF_QTFY));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("ZJZF_QTFY_SV", sv);
	            			}
	            			if(ZJZF_ZZFFY>-1){//资金支付信息 总支付费用
	            				temp.element("ZJZF_ZZFFY", String.valueOf(ZJZF_ZZFFY));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(ZJZF_ZZFFY));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("ZJZF_ZZFFY_SV", sv);
	            			}
	            			if(JS_GCJE>-1){//结算信息 工程金额
	            				temp.element("JS_GCJE", String.valueOf(JS_GCJE));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(JS_GCJE));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("JS_GCJE_SV", sv);
	            			}
	            			if(JS_ZCJE>-1){//结算信息 征拆金额
	            				temp.element("JS_ZCJE", String.valueOf(JS_ZCJE));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(JS_ZCJE));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("JS_ZCJE_SV", sv);
	            			}
	            			if(JS_QTJE>-1){//结算信息 其他金额
	            				temp.element("JS_QTJE", String.valueOf(JS_QTJE));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(JS_QTJE));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("JS_QTJE_SV", sv);
	            			}
	            			if(JS_ZJSJE>-1){//结算信息 总结算金额
	            				temp.element("JS_ZJSJE", String.valueOf(JS_ZJSJE));
	            				String sv = "";
								sv = Pub.NumberToThousand(String.valueOf(JS_ZJSJE));
								if(!Pub.empty(sv)){
									sv = Pub.NumberAddDec(sv);
								}
								temp.element("JS_ZJSJE_SV", sv);
	            			}
	            			
	            			if(hasQuerychild){
	            				if("0".equals(parent)){
	            					temp.element("XMS", String.valueOf(xms));
	            					xms ++;            					
	            				}
	            				json_ += temp.toString()+",";
	            			}
	            		}else{
	            			
	            			String json_txt = (String)xm_json_txt.get(qs.getString(i+1, "XMID"));
	            			
	            			if(json_txt!=null){
	            				JSONObject row_json = JSONObject.fromObject(json_txt);
	                			row_json.element("ZXMS", String.valueOf(zxms));
	            				json_ +=row_json.toString()+",";
	            				zxms++;
	            			}
	            		}
	            	}
	            	json_ = json_.substring(0, json_.length()-1)+"]},";
	            	json_ +="pages:"+pageObject_txt+"}";
	            		
	            }
	            domResult  = json_;
	            
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            DBUtil.closeConnetion(conn);
	        }
	        return domResult;
	}
	public List<String> getChildNodes(QuerySet qs, String parentid) {
		if(qs == null&&qs.getRowCount()<=0 && parentid == null) return null;
		for(int i=0;i<qs.getRowCount();i++){
			String xmid =  qs.getString(i+1, "XMID");
			String ly =  qs.getString(i+1, "LY");
			String parent = qs.getString(i+1, "PARENT");
			// 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
			if (parentid.equals(parent)) {
				recursionFn(qs, xmid);
			}
			// 二、遍历所有的父节点下的所有子节点
			/*if (node.getParentId()==0) {
				recursionFn(list, node);
			}*/
		}
		return returnList;
	}
	private List<String> returnList = new ArrayList<String>();
    private void recursionFn(QuerySet qs, String node) {
		List<String> childList = getChildList(qs, node);// 得到子节点列表
		if (hasChild(qs, node)) {// 判断是否有子节点
			returnList.add(node);
			Iterator<String> it = childList.iterator();
			while (it.hasNext()) {
				String n = (String) it.next();
				recursionFn(qs, n);
			}
		} else {
			returnList.add(node);
		}
	}
 // 得到子节点列表
  	private List<String> getChildList(QuerySet qs, String node) {
  		List<String> nodeList = new ArrayList<String>();
  		for(int i=0;i<qs.getRowCount();i++){
  			String p = qs.getString(i+1, "PARENT");
  			String xmid =  qs.getString(i+1, "XMID");
  			if (p.equalsIgnoreCase(node) ) {
  				nodeList.add(xmid);
  			}
  		}
  		return nodeList;
  	}

  	// 判断是否有子节点
  	private boolean hasChild(QuerySet qs, String node) {
  		return getChildList(qs, node).size() > 0 ? true : false;
  	}
  	
}
