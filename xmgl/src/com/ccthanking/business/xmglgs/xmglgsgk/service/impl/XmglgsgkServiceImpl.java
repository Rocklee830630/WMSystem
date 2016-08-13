package com.ccthanking.business.xmglgs.xmglgsgk.service.impl;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xmglgs.xmglgsgk.service.XmglgsgkService;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;


@Service
public class XmglgsgkServiceImpl implements XmglgsgkService {
	
	//项目管理公司总体概况
	@Override
	public String fzr_ztxx(String json,HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String time=Pub.getDate("yyyy-MM-dd", new Date());//当前日期
		String month=time.substring(5, 7);	
		json="{querycondition: {conditions: [{} ]}}";
		try {
      
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			OrgDept dept = user.getOrgDept();
			String deptId = request.getParameter("xmglgs");
			if(null == deptId){
				deptId = dept.getDeptID();
			}
			String sqlnd="",sqlnd_xdk="",sqlnd_tcjh="",sqlnd_bd="",sqlnd_t="",sqlnd_qknf="";
			String nd = request.getParameter("nd");
			if(!Pub.empty(nd))
			{
				sqlnd="and nd='"+nd+"' ";
				sqlnd_xdk="and xdk.nd='"+nd+"' ";
				sqlnd_tcjh="and tcjh.nd='"+nd+"' ";
				sqlnd_bd="and bd.nd='"+nd+"' ";
				sqlnd_t="and t.nd='"+nd+"' ";
				sqlnd_qknf=" and gzt.qknf='"+nd+"' ";
			}
			String sql="select "+
					//项目公司负责人
					"(select wmsys.wm_concat(to_char(name)) from FS_ORG_PERSON where account in(select fzr from VIEW_YW_ORG_DEPT where row_id='"+deptId+"')) as XMGS_FZR, "+
					//项目负责人
					"(select wmsys.wm_concat(to_char(name)) from FS_ORG_PERSON where account in(select fzr_glgs  from GC_TCJH_XMXDK where  sfyx='1' and isnatc='1' "+sqlnd+"  and xmglgs='"+deptId+"')) as XM_FZR, "+
					//所辖项目数
					"(select nvl(count(GC_JH_SJ_ID),0) from GC_JH_SJ t where t.sfyx = '1' and t.nd = '"+nd+"' and t.xmglgs = '"+deptId+"' and t.xmbs = '0') as XM_SUM, "+
					//标段数
					"(select nvl(count(GC_JH_SJ_ID),0) from GC_JH_SJ t where t.sfyx = '1' and t.nd = '"+nd+"' and t.xmglgs = '"+deptId+"' and (t.xmbs = '1' or isnobdxm='1')) AS BD_SUM,"+  
					//工程总投资
					"(select nvl(trunc(sum(JHZTZE) / 100000000, 2),0) from GC_JH_SJ t left join GC_TCJH_XMXDK xdk on t.xmid = xdk.gc_tcjh_xmxdk_id where xdk.sfyx = '1' and t.sfyx = '1' and t.nd = '"+nd+"' and t.xmglgs = '"+deptId+"') AS ZTZ_SUM,"+  				
					//已开工项目
					"(select nvl(count(gc_jh_sj_id), 0) from GC_JH_SJ t where t.sfyx = '1' and nd = '"+nd+"' and t.xmbs = '0' and ((iskgsj  ='1' and KGSJ_SJ IS NOT NULL) or (iskgsj = '0'))  and t.xmglgs = '"+deptId+"') as kgxm_sum,"+
					//标段数
					"(select nvl(count(gc_jh_sj_id), 0) from GC_JH_SJ t where t.sfyx = '1' and nd = '"+nd+"' and (t.xmbs = '1' or isnobdxm = '1') and ((iskgsj  ='1' and KGSJ_SJ IS NOT NULL) or (iskgsj = '0'))  and t.xmglgs = '"+deptId+"') as kgbd_sum,"+   				
					//已完工项目
					"(select nvl(count(gc_jh_sj_id), 0) from GC_JH_SJ t where t.sfyx = '1' and nd = '"+nd+"' and t.xmbs = '0' and ((iswgsj  ='1' and WGSJ_SJ IS NOT NULL) or (iswgsj = '0'))  and t.xmglgs = '"+deptId+"') as wgxm_sum ,"+ 
					//标段数
					"(select nvl(count(gc_jh_sj_id), 0) from GC_JH_SJ t where t.sfyx = '1' and nd = '"+nd+"' and (t.xmbs = '1' or isnobdxm = '1') and ((iswgsj  ='1' and WGSJ_SJ IS NOT NULL) or (iswgsj = '0'))  and t.xmglgs = '"+deptId+"') as wgbd_sum,"+  
					//未反馈项目
					"(select nvl(count(GC_JH_SJ_ID),0) from GC_JH_SJ where sfyx='1' and xmbs = '0' and iswgsj ='1' and WGSJ is not null and WGSJ_SJ is null and nd ='"+nd+"' and xmglgs = '"+deptId+"' ) as wfkxm_sum,"+
					//标段数
					"(select nvl(count(GC_JH_SJ_ID),0) from GC_JH_SJ where sfyx='1' and (xmbs = '1' or ISNOBDXM = '1') and iswgsj ='1' and WGSJ is not null and WGSJ_SJ is null and nd='"+nd+"' and xmglgs = '"+deptId+"') as wfkbd_sum," +  				
					//进度拖延项目
					"(select nvl(count(GC_JH_SJ_ID),0) from gc_jh_sj t where nd='"+nd+"' and sfyx = '1' and xmbs = '0' and xmglgs='"+deptId+"' and ((t.iskgsj = '1' and t.kgsj is not null and t.kgsj_sj is not null and trunc(kgsj_sj) > trunc(kgsj)) or (t.iswgsj = '1' and t.wgsj is not null and t.wgsj_sj is not null and trunc(wgsj_sj) > trunc(wgsj)))) as yzyqxm,"+
					//标段数
					"(select nvl(count(GC_JH_SJ_ID),0) from gc_jh_sj t where nd='"+nd+"' and sfyx = '1' and (xmbs = '1' or ISNOBDXM = '1') and xmglgs = '"+deptId+"' and ((t.iskgsj = '1' and t.kgsj is not null and t.kgsj_sj is not null and trunc(kgsj_sj) > trunc(kgsj)) or (t.iswgsj = '1' and t.wgsj is not null and t.wgsj_sj is not null and trunc(wgsj_sj) > trunc(wgsj)))) as yzyqbd,"+
					//本年度完成合计
					"(SELECT nvl(SUM(dyjlsdz),'0') FROM GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id = tcjh.gc_jh_sj_id WHERE jl.sfyx = '1'and tcjh.sfyx=1 and tcjh.nd='"+nd+"'  AND jl.xmglgs = '"+deptId+"')  as bn_sum ,"+
					//累计完成合计
					"(select nvl(sum(ljjlsdz),'0') from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id where jl.sfyx=1 and tcjh.sfyx=1 and jlyf=(select max(jlyf) from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id where tcjh.nd=(select max(tcjh.nd) from GC_XMGLGS_JLB jl left join gc_jh_sj tcjh on jl.tcjh_sj_id=tcjh.gc_jh_sj_id)) and tcjh.xmglgs = '"+deptId+"' ) as LJWC_SUM ,"+  
					//开工令办理项目数
					"(select nvl(count(gc_jh_sj_id),0) from GC_GCB_KFG,gc_jh_sj t1 where t1.nd = '"+nd+"' and t1.sfyx = '1' and kgfg = '0' and t1.gc_jh_sj_id = TCJHSJID and t1.xmglgs='"+deptId+"' ) as kgl_sum,"+  
					//审批完成数
					"(select nvl(count(gc_jh_sj_id),0) from gc_gcb_kfg t2,gc_jh_sj t1,fs_event e where t1.gc_jh_sj_id = t2.tcjhsjid and t2.sfyx = '1' and t1.nd = '"+nd+"' and kgfg = '0' and e.sjbh = t2.sjbh and e.sjzt = '2' and t1.xmglgs='"+deptId+"') as kglsp_sum, "+  
					//复工令办理项目数
					"(select nvl(count(gc_jh_sj_id),0) from GC_GCB_KFG,gc_jh_sj t1 where t1.nd = '"+nd+"' and t1.sfyx = '1' and kgfg = '1' and gc_jh_sj_id = TCJHSJID and t1.xmglgs='"+deptId+"') as fgl_sum,"+    
					//停工令办理项目数
					"(select nvl(count(gc_jh_sj_id),0) from GC_GCB_KFG,gc_jh_sj t1 where t1.nd = '"+nd+"' and t1.sfyx = '1' and kgfg = '2' and gc_jh_sj_id = TCJHSJID and t1.xmglgs = '"+deptId+"') as tgl_sum ,"+
					//本周工程量完成合计			
					"(select to_char(max(kssj),'YYYY-MM-DD') max_kssj  from GC_XMGLGS_ZBB zb left join GC_TCJH_XMXDK xdk on xdk.gc_tcjh_xmxdk_id=zb.xdkid where rownum <= '1000' and zb.xmglgs='"+deptId+"'  and zb.sfyx='1' and xdk.sfyx='1'  "+sqlnd_xdk+" ) as max_kssj, "+
					//本月累计完成合计
					"(select to_char(max(jlyf),'YYYY-MM') jlyf from GC_XMGLGS_JLB where sfyx='1' and xmglgs='"+deptId+"' and nd ='"+nd+"' ) as jlyf, "+
					//已提请款次数
					"(select COUNT(*) ytqk_sum FROM gc_zjgl_tqkbm gzt WHERE gzt.sqdw='"+deptId+"' and gzt.qknf='"+nd+"'  and gzt.sfyx = '1'  ) as ytqk_sum, "+
					//完成审批次数
					"(select COUNT(distinct gzt2.ID) FROM gc_zjgl_tqk gzt2, gc_zjgl_tqkmx gzt3, gc_zjgl_tqkbmmx gzt1, gc_zjgl_tqkbm gzt WHERE gzt2.id = gzt3.tqkid AND gzt3.bmmxid = gzt1.id AND gzt1.tqkid=gzt.id  AND gzt2.tqkzt = 6   AND gzt.sqdw='"+deptId+"'   "+sqlnd_qknf+" and gzt.sfyx = '1'  ) as spwc_sum, "+
					//已拨付数
					"(select COUNT(distinct gzt2.ID) FROM gc_zjgl_tqk gzt2, gc_zjgl_tqkmx gzt3, gc_zjgl_tqkbmmx gzt1, gc_zjgl_tqkbm gzt WHERE gzt2.id = gzt3.tqkid AND gzt3.bmmxid = gzt1.id AND gzt1.tqkid=gzt.id AND gzt2.tqkzt = 7  AND gzt.sqdw='"+deptId+"'   "+sqlnd_qknf+" and gzt.sfyx = '1'  ) as ybf_sum, "+
					//提请款总额
					"(select nvl(sum(gzt2.bcsq) ,'0') FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2 WHERE gzt.id=gzt2.tqkid AND  gzt.sqdw='"+deptId+"' and gzt.qknf='"+nd+"' and gzt.sfyx = '1'  ) as tqkze, "+
					//其中审批完成涉及总额
					"(select nvl(sum(gzt2.bcsq) ,'0') FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2 WHERE gzt.id=gzt2.tqkid AND gzt2.bmtqkmxzt='6' and gzt.sqdw='"+deptId+"' and gzt.qknf='"+nd+"' and gzt.sfyx = '1'  ) as spwcje, "+
					//财务审批总额
					"(select nvl(sum(gzt2.cwshz) ,'0') FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2 WHERE gzt.id=gzt2.tqkid AND gzt2.bmtqkmxzt='6' and gzt.sqdw='"+deptId+"' and gzt.qknf = '"+nd+"' and gzt.sfyx = '1'  ) as cwspje, "+
					//审计审定总额
					"(select nvl(sum(gzt2.csz) ,'0') FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2 WHERE gzt.id=gzt2.tqkid AND gzt2.bmtqkmxzt='6' and gzt.sqdw='"+deptId+"' and gzt.qknf='"+nd+"' and gzt.sfyx = '1'  ) as sjsde, "+
					//拨付总额
					"(select nvl(sum(gzt2.jchdz) ,'0') FROM gc_zjgl_tqkbm gzt, gc_zjgl_tqkbmmx gzt2 WHERE gzt.id=gzt2.tqkid AND gzt2.bmtqkmxzt='7' and gzt.sqdw='"+deptId+"' and gzt.qknf='"+nd+"' and gzt.sfyx = '1'  ) as bfze, "+
					//年度工程洽商数
					"(select count(GC_GCGL_GCQS_ID) from GC_GCGL_GCQS qs,gc_jh_sj jh where qs.sfyx='1' and jh.nd='"+nd+"' and jh.gc_jh_sj_id = qs.jhsjid and jh.xmglgs='"+deptId+"') as ytj_sum,"+
					//本月提请数
					"(select count(GC_GCGL_GCQS_ID) from GC_GCGL_GCQS gcqs,gc_jh_sj jh  where gcqs.sfyx='1' and to_char(gcqs.qstcrq,'yyyy-mm')='"+nd+"-"+month+"' and jh.nd='"+nd+"' and jh.xmglgs='"+deptId+"' and jh.gc_jh_sj_id = gcqs.jhsjid  ) as bytj_sum, "+
					//接到整改单数
					"(select COUNT(GC_ZLAQ_JCB_ID) FROM GC_ZLAQ_JCB WHERE ZT >1  and xmglgs='"+deptId+"' and nd='"+nd+"' AND SFYX = '1') as zgtz_num, "+
					//已回复
					"(select COUNT(GC_ZLAQ_JCB_ID) FROM GC_ZLAQ_JCB WHERE ZT>3 and xmglgs='"+deptId+"' and nd='"+nd+"' AND SFYX = '1') as zghf_num, "+
					//复查已通过
					"(select COUNT(GC_ZLAQ_JCB_ID) FROM GC_ZLAQ_JCB jcb left join GC_ZLAQ_ZGB zgb  on jcb.gc_zlaq_jcb_id=zgb.jcbid WHERE ZT=5 and zgb.sfyx='1' and  fcjl='1'  and xmglgs='"+deptId+"' and nd = '"+nd+"'  AND jcb.SFYX = '1') as zgfc_num, "+
					//超期未回复
					"(select COUNT(gc_zlaq_zgb_id) from (select gc_zlaq_zgb_id, jcbid, xgrq, sfyx  from  (select gc_zlaq_zgb_id,jcbid, xgrq,sfyx, row_number() OVER(PARTITION BY jcbid ORDER BY lxbs desc) as row_flg from GC_ZLAQ_ZGB t where sfyx = '1') temp where temp.row_flg = '1') zgb left join GC_ZLAQ_JCB jcb on gc_zlaq_jcb_id = jcbid where  xgrq < (trunc(SYSDATE)) and zt in(2,3) and jcb.sfyx = '1' and zt>1  and xmglgs='"+deptId+"'  and zgb.sfyx = '1' and nd='"+nd+"' ) as zgcq_num  "+
					" from dual ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);	
			System.out.println("====:"+sql);
			bs.setFieldThousand("ZTZ_SUM");
			bs.setFieldThousand("BN_SUM");
			bs.setFieldThousand("LJWC_SUM");
			bs.setFieldThousand("TQKZE");
			bs.setFieldThousand("SPWCJE");
			bs.setFieldThousand("CWSPJE");
			bs.setFieldThousand("SJSDE");
			bs.setFieldThousand("BFZE");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
}
