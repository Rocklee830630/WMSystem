package com.ccthanking.business.zlaq.jcxx;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.bdhf.vo.BdwhVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.business.xdxmk.vo.XmxdkVO;
import com.ccthanking.business.zlaq.vo.JcxxVO;
import com.ccthanking.business.zlaq.vo.ZgxxVO;
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
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
import com.ibm.icu.text.SimpleDateFormat;

@Service
public class JcxxServiceImpl  implements JcxxService{
	
	
	private String ywlx_jc=YwlxManager.GC_ZLAQ_JC;
	private String ywlx_zg=YwlxManager.GC_ZLAQ_ZGB;
	Date date=new Date();//获取时间
	SimpleDateFormat y=new SimpleDateFormat("yyyy");//转换格式
	String year=y.format(date);	
	/*	
	 * 普通查询
	 * 
	 */
	@Override
	public String query_jc(String json,User user,String flag,String zt) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				if("0".equals(flag))
				{
					condition+=" and zt>'1' ";					
				}
				if("2".equals(flag))
				{
					condition+=" and zt='5'  ";					
				}
				if("3".equals(zt))
				{
					condition+=" and zt>1 and zt <4";
				}
				if("4".equals(zt)||"5".equals(zt))
				{
					condition+=" and zt="+zt;
				}	
				condition += BusinessUtil.getSJYXCondition("jcb") + BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "select gc_zlaq_jcb_id,GC_JH_SJ_ID, xdkid, xmmc, bdid as bdbh,bdid, bdmc, jclx, jcgm, jcnr, czwt, jcb.jcbm,jcrq, jcdw, kcsjdw, jcb.zt, jcb.ywlx, jcb.sjbh, jcb.bz, jcb.lrr, jcb.lrsj, jcb.lrbm, jcb.lrbmmc, jcb.gxr, jcb.gxsj, jcb.gxbm, jcb.gxbmmc, jcb.sjmj, jcb.sfyx, jcb.jcr, xmbh, isczwt, nd, xmglgs, sgdw, fzr_sgdw SGDWXMJL, jldw, fzr_jldw JLDWZJ, yzdb, jcb.sjdw, fzr_sjdw, fzr_glgs, lxfs_glgs, lxfs_sgdw SGDWXMJLLXDH, lxfs_jldw JLDWZJLXDH, lxfs_sjdw, jcbh,gc_zlaq_zgb_id, jcbid, tzrq, hfrq, fcrq, zgzt, cljy, xgrq,  fcr, hfnr, fcjl,  sjr, hfr, xgqx, zgbh,hfbh,fcbh,fcyj,zjbs,ajbs from GC_ZLAQ_JCB jcb left join  ( select gc_zlaq_zgb_id, jcbid, tzrq, hfrq, fcrq, zgzt, cljy, xgrq,  fcr, hfnr, fcjl,  sjr, hfr, xgqx, zgbh,hfbh,fcbh,lxbs,fcyj  from (select gc_zlaq_zgb_id, jcbid, tzrq, hfrq, fcrq, zgzt, cljy, xgrq,  fcr, hfnr, fcjl,  sjr, hfr, xgqx, zgbh ,hfbh,fcbh,lxbs,fcyj, row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t where sfyx = '1' ) temp where temp.row_flg='1')zgb on gc_zlaq_jcb_id= jcbid ";				
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("ZT", "ZGZT");
				bs.setFieldDic("JCGM", "JCGM");
				bs.setFieldDic("JCLX", "JCLX");
				bs.setFieldDic("XMLX", "XMLX");
				bs.setFieldDic("ND","XMNF");
				bs.setFieldDic("XMSX", "XMSX");
				bs.setFieldDic("ISXD","XMZT");
				bs.setFieldDic("ISBT", "SF");
				bs.setFieldDic("SFYX", "SF");
				bs.setFieldDic("ISCZWT", "SF");
				bs.setFieldDic("FCJL", "FCJL");
				bs.setFieldDic("JCBM", "ZZDW");			
				bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("JCBM", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				//bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
				bs.setFieldTranslater("FZR_GLGS", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("FCR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("FZR_JLDW", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("FZR_SGDW", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldTranslater("LXFS_JLDW", "FS_ORG_PERSON", "ACCOUNT", "SJHM");
				bs.setFieldTranslater("LXFS_SGDW", "FS_ORG_PERSON", "ACCOUNT", "SJHM");
				bs.setFieldTranslater("BDBH", "GC_XMBD", "GC_XMBD_ID", "BDBH");
				/*bs.setFieldDic("XMGLGS", "XMGLGS");*/
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx_jc,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询质量安全检查信息", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			if (conn != null)
				DBUtil.closeConnetion(conn);
		}		
		return domresult;
	}
	/*	
	 * 信息卡列表查询
	 * 
	 */
	@Override
	public String query_jc_xxk(String json,User user,String jclx,String id) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				//condition+=" and  new_jcrq=1 ";
				condition += BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String ndsql="SELECT nd from gc_tcjh_xmxdk WHERE gc_tcjh_xmxdk_id='"+id+"' AND SFYX = '1'";
				String[][] nd = DBUtil.query(conn, ndsql);
				String year=nd[0][0];

				String sql=" select * from ( select gc_zlaq_jcb_id,jcrq,xmmc,bdmc,jcgm,jcnr,czwt,jcb.zt,jcb.sfyx,isczwt,nd,tzrq,xgrq,hfrq,fcrq,zgzt,fcjl,zjbs,ajbs,LRSJ from GC_ZLAQ_JCB jcb left join "+
							" ( select gc_zlaq_zgb_id,  jcbid, tzrq,xgrq, hfrq, fcrq, zgzt,  fcjl from (select gc_zlaq_zgb_id,  jcbid,tzrq,xgrq, hfrq,  fcrq,  zgzt, fcjl, lxbs, row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t   where sfyx = '1') temp where temp.row_flg = '1') zgb "+
							"  on gc_zlaq_jcb_id = jcbid where  ND = '"+year+"' and "+jclx+" and xdkid = '"+id+"' and jcb.sfyx = '1' ) ";
				//modify by zhangbr@ccthanking.com 去掉年度查询条件
				/*String sql=" select * from ( select gc_zlaq_jcb_id,jcrq,xmmc,bdmc,jcgm,jcnr,czwt,jcb.zt,jcb.sfyx,isczwt,nd,tzrq,xgrq,hfrq,fcrq,zgzt,fcjl,zjbs,ajbs,LRSJ from GC_ZLAQ_JCB jcb left join "+
				" ( select gc_zlaq_zgb_id,  jcbid, tzrq,xgrq, hfrq, fcrq, zgzt,  fcjl from (select gc_zlaq_zgb_id,  jcbid,tzrq,xgrq, hfrq,  fcrq,  zgzt, fcjl, lxbs, row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t   where sfyx = '1') temp where temp.row_flg = '1') zgb "+
				"  on gc_zlaq_jcb_id = jcbid where  "+jclx+" and xdkid = '"+id+"' and jcb.sfyx = '1' ) ";*/
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("ZT", "ZGZT");
				bs.setFieldDic("JCGM", "JCGM");
				bs.setFieldDic("JCLX", "JCLX");
				bs.setFieldDic("XMLX", "XMLX");
				bs.setFieldDic("ND","XMNF");
				bs.setFieldDic("XMSX", "XMSX");
				bs.setFieldDic("ISXD","XMZT");
				bs.setFieldDic("ISBT", "SF");
				bs.setFieldDic("SFYX", "SF");
				bs.setFieldDic("ISCZWT", "SF");
				bs.setFieldDic("FCJL", "FCJL");
				bs.setFieldDic("JCBM", "ZZDW");			
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx_jc,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询质量安全检查信息", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			if (conn != null)
				DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//质量安全部概况检查整改信息监控
	@Override
	public String jc_zg_xxtj(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
	
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd="and nd='"+nd+"' ";
			}
			String sql="select * from "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS jc_num FROM GC_ZLAQ_JCB WHERE SFYX = '1' "+sqlnd+" ), "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS zg_sum FROM GC_ZLAQ_JCB WHERE ISCZWT='1' "+sqlnd+" AND SFYX = '1' ), "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS jczl_num FROM GC_ZLAQ_JCB WHERE  jclx='1' "+sqlnd+" AND SFYX = '1'),  "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS jcss_num FROM GC_ZLAQ_JCB WHERE  jcgm='1' "+sqlnd+" AND SFYX = '1' ), "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS zgtz_num FROM GC_ZLAQ_JCB WHERE ZT >1 "+sqlnd+" AND SFYX = '1' ), "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS jcaq_num FROM GC_ZLAQ_JCB WHERE  jclx='2' "+sqlnd+" AND SFYX = '1' ), "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS jcsj_num FROM GC_ZLAQ_JCB WHERE  jcgm='2' "+sqlnd+" AND SFYX = '1'),  "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS zghf_num FROM GC_ZLAQ_JCB WHERE ZT>3 "+sqlnd+" AND SFYX = '1' ), "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS jcjc_num FROM GC_ZLAQ_JCB WHERE  jclx='3' "+sqlnd+" AND SFYX = '1'),  "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS jclx_num FROM GC_ZLAQ_JCB WHERE  jcgm='3' "+sqlnd+" AND SFYX = '1' ), "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS jcyys_num FROM GC_ZLAQ_JCB WHERE  jclx='4' "+sqlnd+" AND SFYX = '1'),  "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS zgfc_num FROM GC_ZLAQ_JCB WHERE ZT=5 "+sqlnd+" AND SFYX = '1' ), "+
					" (  SELECT COUNT(GC_ZLAQ_JCB_ID)AS jczh_num FROM GC_ZLAQ_JCB WHERE  jclx='5' "+sqlnd+" AND SFYX = '1' ), "+
					" (  SELECT  (SELECT COUNT(GC_ZLAQ_JCB_ID) AS zghf_num  FROM GC_ZLAQ_JCB  WHERE ZT>3 "+sqlnd+"  AND SFYX = '1' ) - (SELECT COUNT(GC_ZLAQ_JCB_ID) AS zgfc_num FROM GC_ZLAQ_JCB WHERE ZT = 5  "+sqlnd+" AND SFYX = '1' ) as dfc_sum  from dual )  ," +
					" (  SELECT COUNT(gc_zlaq_zgb_id) as zgcq_num from (select gc_zlaq_zgb_id, jcbid, xgrq, sfyx  from  (select gc_zlaq_zgb_id,jcbid, xgrq,sfyx, row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t where sfyx = '1') temp where temp.row_flg = '1') zgb left join GC_ZLAQ_JCB jcb on gc_zlaq_jcb_id = jcbid where  xgrq < (trunc(SYSDATE)) and zt in (2,3) and jcb.sfyx = '1'  and zgb.sfyx = '1' "+sqlnd+" )";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	
	//各项目公司质量安全情况
	@Override
	public String zlaqqk(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
	
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd="and nd='"+nd+"' ";
			}
			StringBuffer sbSql_xmglgs = new StringBuffer();
			sbSql_xmglgs.append("select row_id ,bmjc from VIEW_YW_ORG_DEPT where extend1='1'");
			String sql = sbSql_xmglgs.toString();
     		String[][] xmglgsArray = DBUtil.query(conn, sql);
     		StringBuffer sbSql = new StringBuffer();
     		if(null !=xmglgsArray && xmglgsArray.length>0 && !Pub.empty(xmglgsArray[0][0])){
     			for(int m=0;m<xmglgsArray.length;m++)
     			{
     				sbSql.append(
					" SELECT DISTINCT '"+xmglgsArray[m][1]+"' as xmglgs,"+
					" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS jc_sum FROM GC_ZLAQ_JCB WHERE SFYX = '1' "+sqlnd+" and xmglgs='"+xmglgsArray[m][0]+"') as jc_sum ,"+
					" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS zg_sum FROM GC_ZLAQ_JCB WHERE ISCZWT='1' "+sqlnd+"  AND SFYX = '1'and xmglgs='"+xmglgsArray[m][0]+"') as zg_sum,"+
					" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS zgtz_num FROM GC_ZLAQ_JCB WHERE ZT >1 "+sqlnd+"  AND SFYX = '1'and xmglgs='"+xmglgsArray[m][0]+"') as zgtz_num,"+
					" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS zghf_num FROM GC_ZLAQ_JCB WHERE ZT>4 "+sqlnd+"  AND SFYX = '1'and xmglgs='"+xmglgsArray[m][0]+"') as zghf_num,"+
					//" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS zghf_num_2 FROM GC_ZLAQ_JCB WHERE ZT=4 "+sqlnd+"  AND SFYX = '1'and xmglgs='"+xmglgsArray[m][0]+"') as zghf_num_2,"+
	                " (SELECT  (SELECT COUNT(GC_ZLAQ_JCB_ID) AS zghf_num  FROM GC_ZLAQ_JCB  WHERE ZT>4 "+sqlnd+" AND SFYX = '1' and xmglgs = '"+xmglgsArray[m][0]+"') - (SELECT COUNT(GC_ZLAQ_JCB_ID) AS zgfc_num FROM GC_ZLAQ_JCB WHERE ZT = 5 "+sqlnd+"  AND SFYX = '1' and xmglgs = '"+xmglgsArray[m][0]+"') from dual) as dfc_sum ,"+
					" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS zgfc_num FROM GC_ZLAQ_JCB WHERE ZT=5 "+sqlnd+"  AND SFYX = '1'and xmglgs='"+xmglgsArray[m][0]+"') as zgfc_num,"+
					" (SELECT COUNT(gc_zlaq_zgb_id) as zgcq_num from (SELECT gc_zlaq_zgb_id, jcbid, xgrq, sfyx  from  (select gc_zlaq_zgb_id,jcbid, xgrq,sfyx,"+
					" row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t where sfyx = '1') temp where temp.row_flg = '1') zgb "+
					" left join GC_ZLAQ_JCB jcb on gc_zlaq_jcb_id = jcbid where  xgrq< (trunc(SYSDATE)) and zt in (2,3) and jcb.sfyx = '1'  and zgb.sfyx = '1' and ND = '"+year+"'and xmglgs='"+xmglgsArray[m][0]+"') as zgcq_num"+
					" from dual UNION ALL ");
     			}	
     		}
     		sbSql.append(
				" SELECT DISTINCT '其它' as xmglgs,"+
				" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS jc_sum FROM GC_ZLAQ_JCB WHERE ND ='"+year+"' AND SFYX = '1' and XMGLGS IS NULL ) as jc_sum ,"+
				" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS zg_sum FROM GC_ZLAQ_JCB WHERE ISCZWT='1' "+sqlnd+"  AND SFYX = '1'and XMGLGS IS NULL ) as zg_sum,"+
				" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS zgtz_num FROM GC_ZLAQ_JCB WHERE ZT >1 "+sqlnd+"  AND SFYX = '1'and XMGLGS IS NULL ) as zgtz_num,"+
				" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS zghf_num FROM GC_ZLAQ_JCB WHERE ZT>4 "+sqlnd+"  AND SFYX = '1'and XMGLGS IS NULL ) as zghf_num,"+
				//" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS zghf_num_2 FROM GC_ZLAQ_JCB WHERE ZT=4 "+sqlnd+"  AND SFYX = '1'and XMGLGS IS NULL ) as zghf_num_2,"+
				" (SELECT  (SELECT COUNT(GC_ZLAQ_JCB_ID) AS zghf_num  FROM GC_ZLAQ_JCB  WHERE ZT > 4 "+sqlnd+" AND SFYX = '1' and xmglgs is null ) - (SELECT COUNT(GC_ZLAQ_JCB_ID) AS zgfc_num FROM GC_ZLAQ_JCB WHERE ZT = 5 "+sqlnd+"  AND SFYX = '1' and xmglgs is null) from dual) as dfc_sum ,"+
				" (SELECT COUNT(GC_ZLAQ_JCB_ID)AS zgfc_num FROM GC_ZLAQ_JCB WHERE ZT=5 "+sqlnd+"  AND SFYX = '1'and XMGLGS IS NULL ) as zgfc_num,"+
				" (SELECT COUNT(gc_zlaq_zgb_id) as zgcq_num from (SELECT gc_zlaq_zgb_id, jcbid, xgrq, sfyx  from  (select gc_zlaq_zgb_id,jcbid, xgrq,sfyx,"+
				" row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t where sfyx = '1') temp where temp.row_flg = '1') zgb "+
				" left join GC_ZLAQ_JCB jcb on gc_zlaq_jcb_id = jcbid where  xgrq < (trunc(SYSDATE)) and zt in (2,3) and jcb.sfyx = '1'  and zgb.sfyx = '1' and ND = '"+year+"'and XMGLGS IS NULL ) as zgcq_num"+
				" from dual");
     		sql = sbSql.toString();
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//涉及整改最多的施工单位(TOP5)
	@Override
	public String sgdw_top(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
	
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd="and nd='"+nd+"' ";
			}
			String sql=" select rownum||'、'||sgdw as sgdw,sgdw_num from(  select dwmc sgdw ,'['||count(dwmc)||']次' as sgdw_num ,count(dwmc) num from GC_ZLAQ_JCB jcb left join GC_CJDW cjdw on jcb.sgdw = cjdw.GC_CJDW_ID "+
					" where jcb.sfyx = '1' and cjdw.sfyx = '1' "+sqlnd+"  and jcb.isczwt=1 group by dwmc order by num desc ) where rownum<6";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	//涉及整改最多的监理单位(TOP5)
	@Override
	public String jldw_top(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
	
			String sqlnd="";
			if(!Pub.empty(nd))
			{
				sqlnd="and nd='"+nd+"' ";
			}
			String sql="select rownum||'、'||jldw as jldw,jldw_num from( select dwmc jldw ,'['||count(dwmc)||']次' as jldw_num,count(dwmc) num  from GC_ZLAQ_JCB jcb left join GC_CJDW cjdw on jcb.jldw = cjdw.GC_CJDW_ID " +
					" where jcb.sfyx = '1' and cjdw.sfyx = '1' "+sqlnd+"  and jcb.isczwt=1  group by dwmc order by num desc) where rownum<6";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	

	//生成检查编号
	@Override
	public String query_jcbh(String jclx,String jcrq,String jc) throws Exception {
		String ny_full,ny,ny_half;
		if(Pub.empty(jcrq))
		{
			Date d=new Date();//获取时间
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//转换格式
			ny_full=sdf.format(d).substring(0,7);
			ny=ny_full.replace("-", "");
			ny_half=ny.substring(2,6);
		}
		else
		{			
			ny_full=jcrq.substring(0,7);
			ny=ny_full.replace("-", "");
			ny_half=ny.substring(2,6); 
		}	
		String result[][] = DBUtil.query("select count(*) + 1 from GC_ZLAQ_JCB where JCLX='"+jclx+"' and to_char(jcrq,'yyyyMM') = '"+ny+"'");
		String jcbh=null;
		if(null!=result&&result.length>0&&!Pub.empty(result[0][0]))
		{	
			jcbh=ny_half+jc+"00"+result[0][0];
			return jcbh;
		}
		else
		{
			return null;			
		}	
	}	
	
		
	/*	
	 * 检查新增
	 * 
	 */
	@Override
	public String insert_jc(String json,User user,String ywid,String flag) throws Exception {
		Connection conn = DBUtil.getConnection();
		JcxxVO xmvo = new JcxxVO();
		TcjhVO tcjhvo= new TcjhVO();
		XmxdkVO xmxdkvo = new XmxdkVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			tcjhvo.setValueFromJson((JSONObject)list.get(0));
			JSONArray xmxdkList = xmxdkvo.doInitJson(json);
			xmxdkvo.setValueFromJson((JSONObject)xmxdkList.get(0));
			EventVO eventVO_jc = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO_jc.getSjbh());
			if(!Pub.empty(ywid))
			{
				xmvo.setGc_zlaq_jcb_id(ywid);
		        // FileUploadService.updateFjztByYwid(conn, ywid);
		        FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2(tcjhvo.getGc_jh_sj_id());//存入计划数据ID
		        fvo.setGlid3(xmvo.getXdkid()); //存入项目ID
		        fvo.setGlid4(tcjhvo.getBdid()); //存入标段ID
		        fvo.setSjbh(xmvo.getSjbh());//事件编号
		        fvo.setYwlx(ywlx_jc);
		        FileUploadService.updateVOByYwid(conn, fvo, ywid,user);
			}
			else
			{				
				ywid=new RandomGUID().toString();
			}	
			xmvo.setGc_zlaq_jcb_id(ywid); //设置主键
			xmvo.setZt("0");
			xmvo.setFzr_jldw(xmxdkvo.getJldwzj());
			xmvo.setLxfs_jldw(xmxdkvo.getJldwzjlxdh());
			xmvo.setFzr_sgdw(xmxdkvo.getSgdwxmjl());
			xmvo.setLxfs_sgdw(xmxdkvo.getSgdwxmjllxdh());
			//判断是否需要整改
			if("1".equals(xmvo.getIsczwt()))
			{
				ZgxxVO zgvo = new ZgxxVO();
				zgvo.setValueFromJson((JSONObject)list.get(0));
				zgvo.setGc_zlaq_zgb_id(new RandomGUID().toString()); //设置主键
				zgvo.setJcbid(xmvo.getGc_zlaq_jcb_id());
				if("1".equals(flag))
				{
					xmvo.setZt("2");//已经发送
				}
				else
				{
					xmvo.setZt("1");//未发送
				}
				zgvo.setLxbs("1");
				BusinessUtil.setInsertCommonFields(zgvo, user);//公共字段插入
				zgvo.setYwlx(ywlx_zg);
				EventVO eventVO = EventManager.createEvent(conn, zgvo.getYwlx(), user);//生成事件
				zgvo.setSjbh(eventVO.getSjbh());
				BaseDAO.insert(conn, zgvo);//插入
			}
			else
			{
				FileUploadVO vo = new FileUploadVO();
				vo.setYwid(ywid);
				vo.setFjlb("0033");
				FileUploadService.deleteByConditionVO(conn, vo);
			}	
			BusinessUtil.setInsertCommonFields(xmvo, user);//公共字段插入
			xmvo.setYwlx(ywlx_jc);
			//FileUploadService.updateFjztByYwid(conn,ywid);
			BaseDAO.insert(conn, xmvo);//插入
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "添加检查整改息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "添加检查整改信息失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zlaq_jcb_id()+"\",\"fieldname\":\"Gc_zlaq_jcb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_jc(jsona,user,"1",null);
		return resultXinXi;
	}

	
	/*	
	 * 检查修改
	 * 
	 */
	@Override
	public String update_jc(String json,User user,String flag,String ywid) throws Exception {
		Connection conn = DBUtil.getConnection();
		ZgxxVO zgvo = new ZgxxVO();
		JcxxVO xmvo = new JcxxVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list_jc = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list_jc.get(0));				
			BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新
			if("0".equals(xmvo.getIsczwt()))
			{
				xmvo.setZt("0");
			}
			else
			{
				xmvo.setZt("1");
			}	
			if("1".equals(flag))
			{
				xmvo.setZt("2");
			}	
			BaseDAO.update(conn, xmvo);
			JSONArray list_zg = zgvo.doInitJson(json);
			zgvo.setValueFromJson((JSONObject)list_zg.get(0));
			if(zgvo.getGc_zlaq_zgb_id()==null||"".equals(zgvo.getGc_zlaq_zgb_id()))
			{
				if("1".equals(xmvo.getIsczwt()))
				{
					zgvo.setGc_zlaq_zgb_id(new RandomGUID().toString()); //设置主键					
					zgvo.setJcbid(xmvo.getGc_zlaq_jcb_id());
					BusinessUtil.setInsertCommonFields(zgvo, user);//公共字段更新
					EventVO eventVO = EventManager.createEvent(conn, zgvo.getYwlx(), user);//生成事件
					zgvo.setSjbh(eventVO.getSjbh());
					zgvo.setLxbs("1");
					BaseDAO.insert(conn, zgvo);
				}
				else
				{
					FileUploadVO vo = new FileUploadVO();
					vo.setYwid(xmvo.getGc_zlaq_jcb_id());
					vo.setFjlb("0033");
					FileUploadService.deleteByConditionVO(conn, vo);
					vo.setYwid(ywid);
					FileUploadService.deleteByConditionVO(conn, vo);
				}	
			}
			else
			{
				if("0".equals(xmvo.getIsczwt()))
				{
					String sql_jc = "DELETE FROM GC_ZLAQ_ZGB WHERE  GC_ZLAQ_ZGB_ID = '"+zgvo.getGc_zlaq_zgb_id()+"'";
					DBUtil.execSql(conn, sql_jc);
					FileUploadVO vo = new FileUploadVO();
					vo.setYwid(xmvo.getGc_zlaq_jcb_id());
					vo.setFjlb("0033");
					FileUploadService.deleteByConditionVO(conn, vo);
					vo.setYwid(ywid);
					FileUploadService.deleteByConditionVO(conn, vo);
				}
				else
				{					
					zgvo.setJcbid(xmvo.getGc_zlaq_jcb_id());
					BusinessUtil.setUpdateCommonFields(zgvo, user);//公共字段更新
					BaseDAO.update(conn, zgvo);			
				}	
			}
			FileUploadVO vo = new FileUploadVO();
			vo.setYwid(xmvo.getGc_zlaq_jcb_id());
			vo.setFjzt("1");
			vo.setGlid2(xmvo.getGc_jh_sj_id());//存入计划数据ID
			vo.setGlid3(xmvo.getXdkid()); //存入项目ID
			vo.setGlid4(xmvo.getBdid()); //存入标段ID	
			vo.setSjbh(xmvo.getSjbh());
			vo.setYwlx(ywlx_jc);
			FileUploadService.updateVOByYwid(conn, vo, ywid,user);
			conn.commit();
			LogManager.writeUserLog(zgvo.getSjbh(), zgvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改检查整改信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(zgvo.getSjbh(), zgvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改检查整改信息失败", user,"","");
		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zlaq_jcb_id()+"\",\"fieldname\":\"Gc_zlaq_jcb_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String resultXinXi=this.query_jc(jsona,user,"1",null);
		return resultXinXi;
	}

	
	//删除检查信息
	@Override
	public String delete_jc(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		JcxxVO xmvo = new JcxxVO(); 
		try {
		conn.setAutoCommit(false);
		JSONArray list_jc = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list_jc.get(0));
		xmvo = (JcxxVO)BaseDAO.getVOByPrimaryKey(conn,xmvo);
		xmvo.setSfyx("0");
		BaseDAO.update(conn,xmvo);
		//String sql_jc = "DELETE FROM GC_ZLAQ_JCB WHERE  GC_ZLAQ_JCB_ID = '"+xmvo.getGc_zlaq_jcb_id()+"'";
		//DBUtil.execSql(conn, sql_jc);
		if("1".equals(xmvo.getZt()))
		{
			ZgxxVO zgvo = new ZgxxVO();
			JSONArray list_zg = zgvo.doInitJson(json);
			zgvo.setValueFromJson((JSONObject)list_zg.get(0));
			zgvo = (ZgxxVO)BaseDAO.getVOByPrimaryKey(conn,zgvo);
			zgvo.setSfyx("0");
			BaseDAO.update(conn,zgvo);
			//String sql_zg = "DELETE FROM GC_ZLAQ_ZGB WHERE  GC_ZLAQ_ZGB_ID = '"+zgvo.getGc_zlaq_zgb_id()+"'";
			//DBUtil.execSql(conn, sql_zg);
		}
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "删除检查信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除检查信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	
	//质量检查饼图	
	@Override
	public String bt_zljc(User user, String id) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json="{querycondition: {conditions: [{} ]}}";
		try {
			String sqlnd="";
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sql = " select * from ( " +
					" SELECT COUNT(GC_ZLAQ_JCB_ID)AS zg_sum FROM GC_ZLAQ_JCB WHERE ISCZWT='1'  and  jclx='1' and xdkid='"+id+"' and ND ='"+year+"' AND SFYX = '1'), " +
					" (select COUNT(GC_ZLAQ_JCB_ID) as zg_wc from GC_ZLAQ_JCB jcb left join GC_ZLAQ_ZGB zgb on jcb.gc_zlaq_jcb_id=zgb.jcbid WHERE ZT=5 and jclx='1' and xdkid='"+id+"' and fcjl=1 and zgb.sfyx=1 and jcb.sfyx=1  and ND ='"+year+"')";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	
	//安全检查饼图	
	@Override
	public String bt_aqjc(User user, String id) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";	
		String json="{querycondition: {conditions: [{} ]}}";
		try {
			String sqlnd="";
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sql = " select * from ( " +
					" SELECT COUNT(GC_ZLAQ_JCB_ID)AS zg_sum FROM GC_ZLAQ_JCB WHERE ISCZWT='1'  and  jclx='2' and xdkid='"+id+"' and ND ="+year+"' AND SFYX = '1'), " +
					" (select COUNT(GC_ZLAQ_JCB_ID) as zg_wc from GC_ZLAQ_JCB jcb left join GC_ZLAQ_ZGB zgb on jcb.gc_zlaq_jcb_id=zgb.jcbid WHERE ZT=5 and jclx='2' and xdkid='"+id+"' and fcjl=1 and zgb.sfyx=1 and jcb.sfyx=1  and ND ='"+year+"')";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	
	
	/* 
	 * 根据输入值从储备库数据表自动模糊匹配项目名称
	 * @see com.ccthanking.business.tcjh.jhgl.service.TcjhService#xmmcAutoComplete(com.ccthanking.framework.model.autocomplete, com.ccthanking.framework.common.User)
	 */
	@Override
	public List<autocomplete> xmmcAutoComplete(autocomplete json,User user) throws Exception {
		List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
		autocomplete ac = new autocomplete();
		String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition(null);
		condition += BusinessUtil.getCommonCondition(user,null);
		String [][] result = DBUtil.query("select distinct xmmc from GC_ZLAQ_JCB jcb" + json.getTablePrefix() + " where " + condition);
        if(null != result&&result.length>0){
        	for(int i =0;i<result.length;i++){
        	  ac = new autocomplete();
              ac.setRegionName(result[i][0]);
              autoResult.add(ac);
        	}
        }
		return autoResult;
	}
	@Override
	public String queryById(String json, HttpServletRequest request)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
			
	        String id = request.getParameter("id");
			if(Pub.empty(id)){
				id = "";
			}
			String sql = "";
			if(!Pub.empty(id)){
				sql = "select gc_zlaq_jcb_id,GC_JH_SJ_ID, xdkid, xmmc, bdid as bdbh,bdid, bdmc, jclx, jcgm, jcnr, czwt, jcb.jcbm,jcrq, jcdw, kcsjdw, jcb.zt, jcb.ywlx, jcb.sjbh, jcb.bz, jcb.lrr, jcb.lrsj, jcb.lrbm, jcb.lrbmmc, jcb.gxr, jcb.gxsj, jcb.gxbm, jcb.gxbmmc, jcb.sjmj, jcb.sfyx, jcb.jcr, xmbh, isczwt, nd, xmglgs, sgdw, fzr_sgdw SGDWXMJL, jldw, fzr_jldw JLDWZJ, yzdb, jcb.sjdw, fzr_sjdw, fzr_glgs, lxfs_glgs, lxfs_sgdw SGDWXMJLLXDH, lxfs_jldw JLDWZJLXDH, lxfs_sjdw, jcbh,gc_zlaq_zgb_id, jcbid, tzrq, hfrq, fcrq, zgzt, cljy, xgrq,  fcr, hfnr, fcjl,  sjr, hfr, xgqx, zgbh,hfbh,fcbh,fcyj,zjbs,ajbs from GC_ZLAQ_JCB jcb left join  ( select gc_zlaq_zgb_id, jcbid, tzrq, hfrq, fcrq, zgzt, cljy, xgrq,  fcr, hfnr, fcjl,  sjr, hfr, xgqx, zgbh,hfbh,fcbh,lxbs,fcyj  from (select gc_zlaq_zgb_id, jcbid, tzrq, hfrq, fcrq, zgzt, cljy, xgrq,  fcr, hfnr, fcjl,  sjr, hfr, xgqx, zgbh ,hfbh,fcbh,lxbs,fcyj, row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t where sfyx = '1' ) temp where temp.row_flg='1')zgb on gc_zlaq_jcb_id= jcbid where jcb.gc_zlaq_jcb_id = '"+id+"'";
		    }
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, null);
			bs.setFieldDic("ZT", "ZGZT");
			bs.setFieldDic("JCGM", "JCGM");
			bs.setFieldDic("JCLX", "JCLX");
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("ND","XMNF");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("ISXD","XMZT");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("SFYX", "SF");
			bs.setFieldDic("ISCZWT", "SF");
			bs.setFieldDic("FCJL", "FCJL");
			bs.setFieldDic("JCBM", "ZZDW");			
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JCBM", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			//bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("FZR_GLGS", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("FCR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("YZDB", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("FZR_JLDW", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("FZR_SGDW", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("LXFS_JLDW", "FS_ORG_PERSON", "ACCOUNT", "SJHM");
			bs.setFieldTranslater("LXFS_SGDW", "FS_ORG_PERSON", "ACCOUNT", "SJHM");
			bs.setFieldTranslater("BDBH", "GC_XMBD", "GC_XMBD_ID", "BDBH");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx_jc,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询质量安全检查信息", user,"","");
		}  catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
