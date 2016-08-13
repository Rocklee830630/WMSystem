package com.ccthanking.business.zjb.zjbz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.service.UserService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class ZhaoJiaBuZhangServiceImpl implements ZhaoJiaBuZhangService {
	 //委托咨询单位 项目数比率
	public String sqlxmbd(String nd)
	{
		String sql="select xmid, jsb.cssdje,jsb.TBJE,YZSDJE,SJSDJE  from (select * from gc_jh_sj jhsj where jhsj.xmid not in (select distinct (xmid)" +
			" from gc_jh_sj jhsj where jhsj.bdid is not null)) jhsj, gc_zjb_jsb jsb  where jhsj.gc_jh_sj_id = jsb.jhsjid and jsb.SFWT = '1'  and jsb.sfyx = '1' " +
			" and jhsj.sfyx = '1' "+nd+" " +
			" union all" +
			" select distinct (xmid), sum(jsb.cssdje),sum(jsb.TBJE),sum(YZSDJE),sum(SJSDJE) from (select * from gc_jh_sj a where a.xmid  in  " +
			"( select XMID from gc_jh_sj jhsj where jhsj.bdid is not null) ) jhsj, gc_zjb_jsb jsb " +
			"where jsb.jhsjid = jhsj.gc_jh_sj_id and jsb.SFWT = '1' and  jsb.sfyx = '1' and jhsj.sfyx = '1' "+nd+"   group by xmid";
		return sql;
	}
	@Override
	public String lanBiaoJiaTongJi(HttpServletRequest request,User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String lj=request.getParameter("lj");
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
	
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd="and jhsj.nd='"+nd+"'";
			}
			 //累计编制拦标价项目数（有财审审定值的项目数（带标段的是有一个标段有财神审定值 项目数+1））
			//2013年12月19日 改为按照计数规则统计 （计划数据表中的cs_sj判断）
			String sql=	"( select count(XMID) ljbzlbj from ( select  distinct XMID from gc_jh_sj jhsj where jhsj.sfyx='1' and jhsj.iscs='1' and jhsj.cs_sj is not null "+sqlnd+" ) )a " +		
					//财审值共计（财审审计值包括项目和标段） 
					",(select nvl(sum(CSSDZ),0) cszgj from GC_ZJB_LBJB lbj,gc_jh_sj jhsj  where lbj.sfyx='1' and jhsj.sfyx='1'  and jhsj.iscs = '1' and lbj.jhsjid=jhsj.gc_jh_sj_id  "+sqlnd+" ) b," +
					//已完招标项目数(有拦标价和完成招标的项目数（有标段的有一个标段完成招标项目+1）)
					"(select count(*) ywzbxm from  (select distinct(jhsj.xmid) from  GC_ZJB_LBJB lbj,GC_JH_SJ jhsj,(select * from gc_ztb_jhsj b where b.gc_ztb_jhsj_id in (select max(b.gc_ztb_jhsj_id) " +
					"from gc_ztb_xq a, gc_ztb_jhsj b where b.xqid = a.GC_ZTB_XQ_ID and a.sfyx='1' and b.sfyx='1' and a.ZBLX = '14' group by b.jhsjid)) c, gc_ztb_xq d," +
					"  gc_ztb_xqsj_ys e, gc_ztb_sj f where   lbj.jhsjid = jhsj.GC_JH_SJ_ID " +
					" and c.xqid = d.gc_ztb_xq_id and d.gc_ztb_xq_id = e.ztbxqid and e.ztbsjid = f.gc_ztb_sj_id and jhsj.gc_jh_sj_id = c.jhsjid " +
					" and jhsj.sfyx = '1'  and d.sfyx='1' and e.sfyx='1' and f.sfyx='1'  and lbj.sfyx='1'  and c.sfyx='1'  and f.dsfjgid is not null and jhsj.iscs = '1'and jhsj.cs_sj is not null "+sqlnd+" )) c" +
					//中标价 //差额（差额：2013年12月19日 改为取消去绝对值）      财审值-中标价 //差额百分比   （财审值-中标价）/财审值  （已完招标项目和标段数）
					",( select nvl(sum(c.ZZBJ),0) zbj ,nvl(sum(lbj.CSSDZ)-sum(c.ZZBJ),0) ce,to_char(nvl(round(decode(sum(lbj.CSSDZ),0,0,((abs(sum(lbj.CSSDZ) - sum(c.ZZBJ))) / sum(lbj.CSSDZ)) * 100), 2),0), 'fm99999999999990.00') cebfb" +
					" from GC_ZJB_LBJB lbj,  GC_JH_SJ jhsj, (select *  from gc_ztb_jhsj b where b.gc_ztb_jhsj_id in (select max(b.gc_ztb_jhsj_id)" +
					"   from gc_ztb_xq a, gc_ztb_jhsj b   where b.xqid = a.GC_ZTB_XQ_ID  and a.sfyx='1' and b.sfyx='1'  and a.ZBLX = '14'   group by b.jhsjid)) c," +
					" gc_ztb_xq d, gc_ztb_xqsj_ys e, gc_ztb_sj f where   lbj.jhsjid = jhsj.GC_JH_SJ_ID " +
					" and c.xqid = d.gc_ztb_xq_id and d.gc_ztb_xq_id = e.ztbxqid and e.ztbsjid = f.gc_ztb_sj_id  and jhsj.gc_jh_sj_id = c.jhsjid " +
					" and jhsj.sfyx = '1' and d.sfyx='1' and e.sfyx='1' and f.sfyx='1'  and lbj.sfyx='1'  and c.sfyx='1' and f.dsfjgid is not null and jhsj.iscs = '1'and jhsj.cs_sj is not null "+sqlnd+" ) d "; 
			String sql1="select ljbzlbj,cszgj,ywzbxm,zbj,ce ,cebfb from  "+sql ;
			String sql2="select  ljbzlbj ndljbzlbj,cszgj ndcszgj,ywzbxm ndywzbxm,zbj ndzbj,ce ndce ,cebfb ndcebfb from  "+sql ;
			
			String sqlz=" ";
			if(!Pub.empty(lj))
			{
				sqlz=sql1;
			}
			else
			{
				sqlz=sql2;
			}
			
			BaseResultSet bs = DBUtil.query(conn, sqlz, page);
			bs.setFieldThousand("CSZGJ");
			bs.setFieldThousand("XYJEZJ");
			bs.setFieldThousand("ZBJ");
			bs.setFieldThousand("CE");
			bs.setFieldThousand("NDCSZGJ");
			bs.setFieldThousand("ZJDWJE");
			bs.setFieldThousand("NDZBJ");
			bs.setFieldThousand("NDCE");
			
			bs.setFieldDecimals ("CEBFB");
			bs.setFieldDecimals ("NDZXCEBL");
			bs.setFieldDecimals ("NDCEBFB");
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String shenJianLv(User user, String nd,HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		String zxgs=request.getParameter("zxgs");
		String sql1="";
		String sqlIszxgs="";
		try {
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd=" and jhsj.nd='"+nd+"'";
			}
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			//咨询公司审
			if(zxgs.endsWith("1")){
				sqlIszxgs="  and lbj.zxgs is not null  ";
			}else{
				sqlIszxgs="  and lbj.zxgs is  null  ";
			}
			
			String sqlbfb="select lbj.xmid, lbj.cssdz,lbj.sbcsz,nvl(round(decode(sbcsz,0,0,abs((sbcsz-cssdz))/sbcsz*100),2),0) bfb  from (select * from gc_jh_sj jhsj    where jhsj.xmid not in   (select distinct (xmid)   from gc_jh_sj jhsj     where jhsj.bdid is not null)) jhsj," +
						" GC_ZJB_LBJB  lbj where jhsj.gc_jh_sj_id = lbj.jhsjid   and jhsj.iscs = '1'  "+sqlIszxgs+"  and jhsj.cs_sj is not null and lbj.sfyx = '1' and jhsj.sfyx = '1' "+sqlnd+" " +
						//--标段的项目" +
						" union all select distinct(lbj.xmid), sum(cssdz),sum(sbcsz),nvl(round(decode(sum(sbcsz),0,0,abs((sum(sbcsz)-sum(cssdz)))/sum(sbcsz)*100),2),0) bfb " +
						" from  (select * from gc_jh_sj a  where a.xmid  in ( select XMID from gc_jh_sj jhsj where jhsj.bdid is not null) ) jhsj, GC_ZJB_LBJB  lbj" +
						" where lbj.jhsjid = jhsj.gc_jh_sj_id  and jhsj.iscs = '1' "+sqlIszxgs+"   and jhsj.cs_sj is not null and  lbj.sfyx = '1'   and jhsj.sfyx = '1' "+sqlnd+" group by lbj.xmid";
			
			//报给咨询公司的项目数
			if(zxgs.endsWith("1")){
				 sql1= "select nvl(a.value,0) value, b.dic_value label,b.dic_code CODE from ( select count(dangci) value, dangci label" +
						" from (select decode(sign(bfb - 2.9) + sign(bfb - 9.9) + sign(bfb - 14.9) +  sign(bfb - 19.9),-4, '0-3%', -2, '3%-10%', 0,'10%-15%',2,'15%-20%',  4,    '20%以上', 0) dangci," +
						"  bfb from ("+sqlbfb+"))" +
						" group by dangci ) a,(select t.dic_value ,t.dic_code, t.id from FS_DIC_TREE t where t.parent_id='9990000000332' ) b  where a.label(+)=b.dic_value order by b.id";
			}else{//直接上报的值项目数
				sql1 = "select nvl(a.value,0) value, b.dic_value label,b.dic_code CODE from ( select count(dangci) value, dangci label" +
						" from (select decode(sign(bfb - 6.9) + sign(bfb - 9.9) + sign(bfb - 14.9) +  sign(bfb - 19.9),-4, '0-7%', -2, '7%-10%', 0,'10%-15%',2,'15%-20%',  4,    '20%以上', 0) dangci," +
						"  bfb from ("+sqlbfb+"))" +
						" group by dangci ) a,(select t.dic_value ,t.dic_code, t.id from FS_DIC_TREE t where t.parent_id='9990000000338' ) b  where a.label(+)=b.dic_value order by b.id";
			}
			BaseResultSet bs = DBUtil.query(conn, sql1, page);
			domresult = bs.getJson();
			HashMap chartMap = new HashMap();
			chartMap.put("WARNING", "WARNING");
		    HashMap linkMap = new HashMap();
		    if(zxgs.endsWith("1")){
		    	linkMap.put("LINKFUNCTION", "javascript:zxgsLbj");
		    }else{
		    	linkMap.put("LINKFUNCTION", "javascript:zjsbLbj");
		    }
			
			List paraList = new ArrayList(); 
			paraList.add("CODE");
			linkMap.put("LINKPARAM", paraList);
			domresult = ChartUtil.makePieChartJsonString(domresult, chartMap, linkMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String jieSuanTongJi(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
	
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd="and jhsj.nd='"+nd+"'";
			}
			//--累计结算合同
			String sql1="  select count(*)  LJJSHT from gc_zjb_jsb  where JSZT='6' and sfyx='1' ";
			//--送审值共计(上报值de 金额)
			String sql2=" select nvl(sum(TBJE),0) SSZGJ from gc_zjb_jsb where sfyx='1' and JSZT='6'  ";
			//--审计值共计
			String sql3=" select nvl(sum(SJSDJE),0) SJZGJ from gc_zjb_jsb where sfyx='1' and JSZT='6'   ";
			//--核减(审计值-送审值)  核减百分比(审计值-送审值)/送审值 (2013年12月19日 改为（送审值-审计值）/送审值)
			String sql4=" select nvl(sum(TBJE)-sum(SJSDJE),0) HJ,to_char(decode(nvl(sum(TBJE),0),0,0,round((nvl(sum(TBJE),0)-nvl(sum(SJSDJE),0))/sum(TBJE)*100,2)),'fm99999999999990.00')  HJBL " +
					" from gc_zjb_jsb  where sfyx='1'and  JSZT='6'  ";
			String sql=" select LJJSHT, SSZGJ ,SJZGJ, HJ,HJBL from ("+sql1+") a,("+sql2+") b ,("+sql3+") c,("+sql4+") d ";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("SSZGJ");
			bs.setFieldThousand("SJZGJ");
			bs.setFieldThousand("HJ");
			bs.setFieldDecimals("HJBL"); 
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String shenJiYuSongShen(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd=" and jhsj.nd='"+nd+"'";
			}
			PageManager page = new PageManager();
			String sqlxm=this.sqlxmbd(sqlnd);
			conn.setAutoCommit(false);
			String sql = " select nvl(a.value,0) value, b.dic_value label from (select count(dangci)  value,dangci label from (select " +
					" decode(sign(bfb-4.9)+sign(bfb-9.9)+sign(bfb-14.9)+sign(bfb-19.9),  -4,  '0-5%',   -2, '5%-10%', 0, '10%-15%',   2, '15%-20%', 4, '20%以上',0) dangci,bfb" +
					" from (select  a.bc bfb  from (select decode(nvl(TBJE,0),0,0,nvl(round((nvl(TBJE,0)-nvl(SJSDJE,0))/TBJE,2),0)) bc" +			//2013年12月19日 改为：(上报值-审计值)/上报值
					" from ("+sqlxm+")) a)) group by dangci ) a,(select t.dic_value ,t.id from FS_DIC_TREE t where t.parent_id='9990000000300' ) b  where a.label(+)=b.dic_value order by b.id ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String caiShenYuShenJi(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd=" and jhsj.nd='"+nd+"'";
			}
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sqlxm=this.sqlxmbd(sqlnd);
			String sql = " select nvl(a.value,0) value, b.dic_value label  from (select count(dangci)  value,dangci label from (select " +
					" decode(sign(bfb-4.9)+sign(bfb-9.9)+sign(bfb-14.9)+sign(bfb-19.9),  -4,  '0-5%',   -2, '5%-10%', 0, '10%-15%',   2, '15%-20%', 4, '20%以上',0) dangci,bfb" +
					" from (select  a.bc bfb  from (select decode(nvl(CSSDJE,0),0,0,nvl(round((nvl(CSSDJE,0)-nvl(SJSDJE,0))/CSSDJE,2),0)) bc" +				//2013年12月19日 改为：(财审值-审计值)/财审值
					" from ("+sqlxm+") ) a)) group by dangci  ) a,(select t.id,t.dic_value from FS_DIC_TREE t where t.parent_id='9990000000300' ) b  where a.label(+)=b.dic_value  order by b.id ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String shenHeYuCaiShen(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd=" and jhsj.nd='"+nd+"'";
			}
			String sqlxm=this.sqlxmbd(sqlnd);
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "select nvl(a.value,0) value, b.dic_value label  from ( select count(dangci)  value,dangci  label from (select " +
					" decode(sign(bfb-4.9)+sign(bfb-9.9)+sign(bfb-14.9)+sign(bfb-19.9),  -4,  '0-5%',   -2, '5%-10%', 0, '10%-15%',   2, '15%-20%', 4, '20%以上',0) dangci,bfb" +
					" from (select  a.bc bfb  from (select decode(nvl(YZSDJE,0),0,0,nvl(round((nvl(YZSDJE,0)-nvl(CSSDJE,0))/YZSDJE,2),0)) bc" +			//2013年12月19日 改为审核值-财审值/审核值
					" from ("+sqlxm+")) a)) group by dangci ) a,(select t.dic_value,t.id from FS_DIC_TREE t where t.parent_id='9990000000300' ) b  where a.label(+)=b.dic_value order by b.id ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String songShenYuShenHe(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd=" and jhsj.nd='"+nd+"'";
			}
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sqlxm=this.sqlxmbd(sqlnd);
			String sql = "select nvl(a.value,0) value, b.dic_value label  from ( select count(dangci)  value,dangci label  from (select " +
					"decode(sign(bfb-4.9)+sign(bfb-9.9)+sign(bfb-14.9)+sign(bfb-19.9),  -4,  '0-5%',   -2, '5%-10%', 0, '10%-15%',   2, '15%-20%', 4, '20%以上',0) dangci,bfb" +
					" from (select  a.bc bfb  from (select decode(nvl(TBJE,0),0,0,nvl(round((nvl(TBJE,0)-nvl(YZSDJE,0))/TBJE,2),0)) bc " +		//2013年12月19日 改为上报值-审核值/上报值
					" from ("+sqlxm+")) a)) group by dangci) a,(select t.dic_value,t.id from FS_DIC_TREE t where t.parent_id='9990000000300' ) b  where a.label(+)=b.dic_value order by b.id ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String jieSuanNianDuTongJi(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
	
			String sqlnd="";
			String sqljs=" ";
			
			if(!Pub.empty(nd))
			{
				sqlnd="and jhsj.nd='"+nd+"'";
				sqljs=" and jhsj.nd='"+nd+"' or ( GHH.sfyx = '1' and ghh.htlx = 'SG' and jhsj.wgsj_sj is not null and jhsj.nd < ('"+nd+"')  and jhsj.gc_jh_sj_id not in " +
						" (select jsb.jhsjid from gc_zjb_jsb  jsb   )  and ghh.htzt ='1'   )";
			}
			
			//--年度具备结算合同(已经完工的数)
			String sql1="  SELECT count(distinct(ghh.id)) NDJSHT FROM gc_htgl_ht ghh left JOIN gc_htgl_htsj ghh2  ON ghh.ID = ghh2.htid" +
					" LEFT JOIN gc_jh_sj jhsj  ON jhsj.gc_jh_sj_id = ghh2.jhsjid  left join gc_zjb_jsb jsb    on jsb.jhsjid=jhsj.gc_jh_sj_id " +
					" where rownum <= '100'  and GHH.sfyx = '1'  and jhsj.wgsj_sj is not null" +
					" and ghh.htlx = 'SG'  and ghh.htzt in ('1','2') " +sqljs+"   ";
			//已完成结算
			String sql2="  select count(*) YWCJS from gc_zjb_jsb jsb ,gc_jh_sj jhsj  where  jsb.jhsjid=jhsj.gc_jh_sj_id and  jsb.sfyx='1' and JSZT='6'  "+sqlnd+"  ";
			//
			//--完成率：
			String sql3=" select to_char(decode(NDJSHT, 0, 0, round(YWCJS / NDJSHT*100, 2)),'fm99999999999990.00') JSWCL from ( "+sql2+") a," +
					"  ("+sql1+") b ";
			//---正在结算合同
			String sql4="  select count(*) ZZJSHTGS from gc_zjb_jsb jsb  ,gc_jh_sj jhsj  where " +
					" jsb.jhsjid=jhsj.gc_jh_sj_id and  jsb.sfyx='1'  and jsb.jszt in (0,1,2,3,4,5) "+sqlnd+" ";
			//--年度送审值
			String sql5="  select nvl(sum(TBJE),0) NDSSZ from gc_zjb_jsb jsb ,gc_jh_sj jhsj  where jsb.sfyx='1' and jhsj.sfyx='1' and  jsb.jhsjid=jhsj.gc_jh_sj_id "+sqlnd+" ";
			//财审值差比%(2013年12月19日 改为上报值-财审值)
			String sql6=" select nvl(sum(CSSDJE),0) JSCSZ,to_char(decode(nvl(sum(TBJE),0),0,0,round((nvl(sum(TBJE),0)-nvl(sum(CSSDJE),0))/sum(TBJE)*100,2)),'fm99999999999990.00') JSCSZBL " +
					"  from gc_zjb_jsb jsb ,gc_jh_sj jhsj  where jsb.sfyx='1' and jhsj.sfyx='1' and  jsb.jhsjid=jhsj.gc_jh_sj_id "+sqlnd+"    ";
			//审计值 差比%(2013年12月19日 改为上报值-审计值)
			String sql7=" select nvl(sum(SJSDJE),0) JSSJZ,to_char(decode(nvl(sum(TBJE),0),0,0,round((nvl(sum(TBJE),0)-nvl(sum(SJSDJE),0))/sum(TBJE)*100,2)),'fm99999999999990.00') JSSJZBL " +
					"  from gc_zjb_jsb jsb ,gc_jh_sj jhsj  where jsb.sfyx='1' and jhsj.sfyx='1' and  jsb.jhsjid=jhsj.gc_jh_sj_id "+sqlnd+"   ";
			//核减(审计值-送审值)(2013年12月19日 改为上报值-审计值)
			String sql8=" select nvl(sum(TBJE)-sum(SJSDJE),0) JSHJ ,to_char(decode(nvl(sum(TBJE),0),0,0,round((nvl(sum(TBJE),0)-nvl(sum(SJSDJE),0))/sum(TBJE)*100,2)),'fm99999999999990.00')  JSHJBL " +
					"  from gc_zjb_jsb  jsb ,gc_jh_sj jhsj  where jsb.sfyx='1' and jhsj.sfyx='1' and  jsb.jhsjid=jhsj.gc_jh_sj_id "+sqlnd+"    ";
			//-委托咨询单位审核项目数(造价表是否委托为是)
			String sql9="  select count(distinct(jhsj.xmid))  WTZXDW from gc_zjb_jsb   jsb ,gc_jh_sj jhsj  where jsb.sfyx='1' and jhsj.sfyx='1' and  jsb.jhsjid=jhsj.gc_jh_sj_id and  jsb.SFWT='1'  "+sqlnd+"  ";
			//上报值  ,审核值：0.00(元)(0.00%),财审值：0.00(元)(差比0.00%)
			
			String sql13="  " +//2013年12月19日 审核值百分比改为（上报值-审核值）/上报值
					"select nvl(sum(TBJE),0) JSSSZ,nvl(sum(YZSDJE),0) JSSHZ, to_char(decode(nvl(sum(TBJE),0),0,0,round((nvl(sum(TBJE),0)-nvl(sum(YZSDJE),0))/sum(TBJE)*100,2)),'fm99999999999990.00') JSSHZBL" +
					//2013年12月19日 财审值百分比改为（上报值-财审值）/上报值
					" , nvl(sum(CSSDJE),0) WTJSCSZ ,to_char(decode(nvl(sum(TBJE),0),0,0,round((nvl(sum(TBJE),0)-nvl(sum(CSSDJE),0))/sum(TBJE)*100,2)),'fm99999999999990.00') WTJSCSZBL  from (" +
					" select xmid, jsb.cssdje,jsb.TBJE,YZSDJE  from (select * from gc_jh_sj jhsj where jhsj.xmid not in (select distinct (xmid)" +
					"  from gc_jh_sj jhsj where jhsj.bdid is not null)) jhsj, gc_zjb_jsb jsb  where jhsj.gc_jh_sj_id = jsb.jhsjid and jsb.SFWT = '1'  and jsb.sfyx = '1' "+sqlnd+"    " +
					" and jhsj.sfyx = '1' " +
					//--有标段的项目
					" union all select distinct (xmid), sum(jsb.cssdje),sum(jsb.TBJE),sum(YZSDJE) from (select * from gc_jh_sj a where a.xmid  in " +
					"( select XMID from gc_jh_sj jhsj where jhsj.bdid is not null) ) jhsj,  gc_zjb_jsb jsb where jsb.jhsjid = jhsj.gc_jh_sj_id and jsb.SFWT = '1' and  jsb.sfyx = '1' and jhsj.sfyx = '1'  "+sqlnd+"  " +
					"   group by xmid) ";
			
			
			String sql=" select NDJSHT, YWCJS ,JSWCL, ZZJSHTGS,NDSSZ,JSCSZ,JSCSZBL,JSSJZ,JSSJZBL,JSHJ,JSHJBL,WTZXDW,JSSSZ,JSSHZ,JSSHZBL,WTJSCSZ ,WTJSCSZBL from ("+sql1+") a,("+sql2+") b ,("+sql3+") c, " +
					" ("+sql4+") d,("+sql5+") e,("+sql6+") f,("+sql7+") g,("+sql8+") h,("+sql9+") i,("+sql13+") g ";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("NDSSZ");
			bs.setFieldThousand("JSCSZ");
			bs.setFieldThousand("JSSJZ");
			bs.setFieldThousand("JSHJ");
			bs.setFieldThousand("JSSSZ");
			bs.setFieldThousand("JSSHZ");
			bs.setFieldThousand("WTJSCSZ");
			bs.setFieldDecimals("JSCSZBL"); 
			bs.setFieldDecimals("JSSJZBL"); 
			bs.setFieldDecimals("JSHJBL"); 
			bs.setFieldDecimals("JSSHZBL"); 
			bs.setFieldDecimals("WTJSCSZBL"); 
			bs.setFieldDecimals("JSWCL"); 
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String weiTuoZiXunGongSiTongJi(HttpServletRequest request,User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String lj=request.getParameter("lj");
		try {
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd=" and jhsj.nd='"+nd+"'";
			}
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			//委托咨询单位编制项目数(有委托咨询公司的项目数，有标段的项目 有一个标段有项目就+1)
			//上报值财审值共计 (有委托咨询单位的项目和标段的上报值财审值总数)
			//审减 （有委托咨询单位的项目和标段的 上报财审值减去财审审定值）
			//审减 百分比（有委托咨询单位的项目和标段的 （上报财审值-财审审定值）/上报财审值
			String sql1 = "select count(distinct(S.XMID)) WTXYQDWCXMS,nvl(sum(SBCSZ),0) ZXSBCSZ ,nvl(sum(SBCSZ)-sum(CSSDZ),0) ZXSJ, to_char( nvl(decode(sum(SBCSZ), '0', '0',round((sum(SBCSZ) - sum(CSSDZ)) / sum(SBCSZ)*100, 2)),0),'fm99999999999990.00') ZXCEBL " +
					"from GC_ZJB_LBJB L,GC_JH_SJ S where L.JHSJID=S.GC_JH_SJ_ID and L.SFYX='1' and L.ZXGS is not null and S.sfyx = '1' and S.iscs = '1' and S.cs_sj is not null";
			String sql2 = "select count(distinct(S.XMID)) NDWTXYQDWCXMS,nvl(sum(SBCSZ),0) NDZXSBCSZ ,nvl(sum(SBCSZ)-sum(CSSDZ),0) NDZXSJ, to_char( nvl(decode(sum(SBCSZ), '0', '0',round((sum(SBCSZ) - sum(CSSDZ)) / sum(SBCSZ)*100, 2)),0),'fm99999999999990.00') NDZXCEBL " +
					"from GC_ZJB_LBJB L,GC_JH_SJ S where L.JHSJID=S.GC_JH_SJ_ID and L.SFYX='1' and L.ZXGS is not null and S.sfyx = '1' and S.iscs = '1' and S.cs_sj is not null and S.ND='"+lj+"' ";
			
			String sql=" ";
			if(Pub.empty(lj))
			{
   				sql=sql1;
			}
			else
			{
				sql=sql2;
			}

			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("ZXSBCSZ");
			bs.setFieldThousand("ZXSJ");
			bs.setFieldThousand("NDZXSBCSZ");
			bs.setFieldThousand("NDZXSJ");
			bs.setFieldDecimals ("ZXCEBL");
			bs.setFieldDecimals ("NDZXCEBL");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryConditionLbj(String json, User user,HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String sqlnd="";
		String sqliszxgs="";
		String nd=request.getParameter("nd");
		String qujian=request.getParameter("qujian");
		String iszxgs=request.getParameter("iszxgs");
		int begin = 0;
		int end=0;
		if("1".equals(qujian)){
			begin=0;
			end=3;
		}if("2".equals(qujian)){
			begin=3;
			end=10;
		}if("3".equals(qujian)){
			begin=10;
			end=15;
		}if("4".equals(qujian)){
			begin=15;
			end=20;
		}if("5".equals(qujian)){
			begin=20;
			end=100000000;
		}if("6".equals(qujian)){
			begin=0;
			end=7;
		}if("7".equals(qujian)){
			begin=7;
			end=10;
		}
		
		if(!Pub.empty(nd))
		{
			sqlnd=" and jhsj.nd='"+nd+"'";
		}
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		//咨询公司审
		if(iszxgs.equals("1")){
			sqliszxgs=" and lbj.zxgs is not null  ";
		}else{
		//直接审
			sqliszxgs=" and lbj.zxgs is  null  ";
		}
		condition += "and lbj.jhsjid(+)=jhsj.GC_JH_SJ_ID  and c.xqid=d.gc_ztb_xq_id(+) and d.gc_ztb_xq_id=e.ztbxqid(+) and e.ztbsjid=f.gc_ztb_sj_id(+) and jhsj.gc_jh_sj_id=c.jhsjid(+) " +
				  "  and jhsj.iscs = '1' and jhsj.cs_sj is not null "+sqliszxgs+"  and lbj.xmid in (" +
				 //查出符合条件的xmid
				 "  select xmid from (select lbj.xmid,lbj.cssdz,  lbj.sbcsz," +
				 "  nvl(round(decode(sbcsz,  0, 0,abs((sbcsz - cssdz)) / sbcsz * 100), 2),0) bfb" +
				 "  from (select * from gc_jh_sj jhsj  where jhsj.xmid not in" +
				 "  (select distinct (xmid)  from gc_jh_sj jhsj where jhsj.bdid is not null)) jhsj, GC_ZJB_LBJB lbj" +
				 "  where jhsj.gc_jh_sj_id = lbj.jhsjid  and jhsj.iscs = '1'  "+sqliszxgs+"   and jhsj.cs_sj is not null" +
				 "  and lbj.sfyx = '1'  and jhsj.sfyx = '1' "+sqlnd+" " +
				 "  union all select distinct (lbj.xmid),sum(cssdz), sum(sbcsz), " +
				 "  nvl(round(decode(sum(sbcsz),0, 0, abs((sum(sbcsz) - sum(cssdz))) /sum(sbcsz) * 100), 2),0) bfb" +
				 "  from (select * from gc_jh_sj a where a.xmid in (select XMID from gc_jh_sj jhsj where jhsj.bdid is not null)) jhsj," +
				 "  GC_ZJB_LBJB lbj where lbj.jhsjid = jhsj.gc_jh_sj_id  and jhsj.iscs = '1' and jhsj.cs_sj is not null "+sqliszxgs+"  " +
				 "  and lbj.sfyx = '1'  and jhsj.sfyx = '1' "+sqlnd+"  group by lbj.xmid)where bfb>="+begin+" and bfb<"+end+" )";
	
		condition += BusinessUtil.getSJYXCondition("jhsj") + BusinessUtil.getCommonCondition(user,null);
		condition += BusinessUtil.getSJYXCondition("lbj") + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select gc_zjb_lbjb_id, jhsj.GC_JH_SJ_ID as jhsjid, jhsj.xmbh, jhsj.jhid, jhsj.nd, jhsj.xmsx, jhsj.pxh, jhsj.xmmc, jhsj.bdmc, jhsj.xmid, jhsj.bdbh,jhsj.bdid," +
						 " lbj.csbgbh, lbj.txqsj, lbj.zbsj, lbj.tzjjsj, lbj.zxgsj, lbj.sgfajs, lbj.zbwjjs, lbj.zxgsrq, lbj.jgbcsrq, lbj.sbcsz, lbj.czswrq, lbj.cssdz, lbj.sjz, lbj.sjbfb, lbj.zdj, lbj.bz,lbj.SBCSZRQ,lbj.CSSDZRQ,  " +
						 "(select max(dyqk.twrq) from GC_ZJB_DYQK dyqk where dyqk.jhsjid(+)=jhsj.GC_JH_SJ_ID and dyqk.sfyx='1') as YWRQ ," +
						 "(select max(dyqk.dyrq) from GC_ZJB_DYQK dyqk where dyqk.jhsjid(+)=jhsj.GC_JH_SJ_ID and dyqk.sfyx='1') as HFRQ, decode(lbj.ZXGS,'', f.dsfjgid,lbj.ZXGS)  ZXGS,f.kbrq, d.lrsj  " +
						 " from GC_ZJB_LBJB lbj,GC_JH_SJ jhsj,( select * from gc_ztb_jhsj  b where b.gc_ztb_jhsj_id in(select max(b.gc_ztb_jhsj_id) from gc_ztb_xq a ,gc_ztb_jhsj b where b.xqid=a.GC_ZTB_XQ_ID and a.ZBLX ='14' group by b.jhsjid)) c" +
						 ",gc_ztb_xq d,gc_ztb_xqsj_ys e,gc_ztb_sj f ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("SBCSZ"); 
			bs.setFieldThousand("CSSDZ"); 
			bs.setFieldThousand("SJZ"); 
			bs.setFieldThousand("ZDJ");
			 bs.setFieldTranslater("ZXGS", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}


	}


