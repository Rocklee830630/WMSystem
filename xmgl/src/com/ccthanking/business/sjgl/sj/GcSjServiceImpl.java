package com.ccthanking.business.sjgl.sj;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.tcjh.jhgl.vo.FkqkVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public  class GcSjServiceImpl implements GcSjService {
	private String ywlx=YwlxManager.GC_SJ;
	
	@Override
	public String querySj(HttpServletRequest request,String json) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition =RequestUtil.getConditionList(json).getConditionWhere();
		condition += " and sfgs.xmid(+) = jhsj.xmid  and gcsj.SJWYBH(+) = jhsj.SJWYBH" +
				" and jjg.jhsjid(+) = jhsj.gc_jh_sj_id" +
				"  and sjwj.SJWYBH(+) = jhsj.SJWYBH" +
				"  and cbsj.SJWYBH(+) = jhsj.SJWYBH" +
				"  and sjbg.SJWYBH(+) = jhsj.SJWYBH" +
				"  and gsjh.xmid(+) = jhsj.xmid" +
				"  and bd.jhsjid(+) = jhsj.gc_jh_sj_id" +
				"  and xm.jhsjid(+) = jhsj.gc_jh_sj_id";
		condition += BusinessUtil.getSJYXCondition("jhsj")+BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select jhsj.xmbs, jhsj.SJWYBH ,jhsj.pxh,jhsj.xmbh,jhsj.xmmc,jhsj.bdmc,jhsj.bdbh,jhsj.xmid,jhsj.bdid,jhsj.nd,jhsj.jhid,jhsj.sjbh as jh_sjbh,jhsj.gc_jh_sj_id as jhsjid,sjbg.bg,sjbg.BGFY ZBGFY," +
						 "sjwj.jbgh,jhsj.cbsjpf_sj,jhsj.cqt_sj,jhsj.sgt_sj,jhsj.pqt_sj,jhsj.iscbsjpf,jhsj.iscqt,jhsj.ispqt,jhsj.issgt," +
						 "jhsj.cbsjpf_bz,jhsj.cqt_bz,jhsj.pqt_bz,jhsj.sgt_bz,jhsj.jg_bz,jhsj.js_sj,jhsj.jg_sj," +
						 "nvl(jhsj.jg_sj,jhsj.js_sj) as jjgsj, jjg.JGYSSJ as jungsj,jjg.JGYSRQ as jiaogsj," +
						 //计划信息
						 "gsjh.JHGS,gsjh.sjgs,gsjh.GSJWYBH,jhsj.CQT as JHCQT,jhsj.PQT as JHPQT,jhsj.SGT as JHSGT,jhsj.JG as JHJG,gcsj.sjbh as sj_sjbh," +
						 "gcsj.gc_sj_id,gcsj.wcsj_kcbg,gcsj.wcsj_cqt,gcsj.wcsj_pqt,gcsj.wcsj_sgt_ssb,gcsj.wcsj_sgt_zsb,gcsj.wcsj_ys," +
						 "cbsj.ssjs,cbsj.sse,cbsj.bz as bz_gs,cbsj.cbsjpf,cbsj.pfnr,cbsj.gs,cbsj.gys,cbsj.gc_sj_cbsjpf_id,cbsj.gs_sjbh,cbsj.gcje,cbsj.zcje,cbsj.qtje,cbsj.ys_gcje,cbsj.ys_zcje,cbsj.ys_qtje, " +				//初步設計批覆修改爲概算，此處的兩條信息针对批复内容和概算
						 "nvl2(jhsj.bdid, bd.sjdw, xm.sjdw) as sjdw,nvl2(jhsj.bdid, bd.sgdw, xm.sgdw) as sgdw,nvl2(jhsj.bdid, bd.jldw, xm.jldw) as jldw ," +
						 "   ( select count(*)  ccrws from GC_SJGL_RWSGL t   where jhsj.SJWYBH=t.SJWYBH and  t.rwslx='1' and t.sfyx='1') ccrws ," +
						 " ( select count(*)  shrws from GC_SJGL_RWSGL t   where jhsj.SJWYBH=t.SJWYBH and  t.rwslx='2' and t.sfyx='1' )  shrws ,"+
						 //是否可以概算字段start
						 " (case when sfgs.gh='true' and sfgs.lx='true' then 'true' else 'false' end ) as sfjtgs, "+
						 " (case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ " + 
						 //end
						 " from Gc_Jh_Sj jhsj,(select * from Gc_Sj where sfyx='1') gcsj," +
						 //是否可以概算start
						 "(select xmid, (case when ISGCXKZ='1' and GCXKZ_SJ is not null then 'true' when ISGCXKZ='0' then 'true' else 'false' end ) as gh," +
						 "(case when ISKYPF='1' and KYPF_SJ is not null then 'true' when ISKYPF='0' then 'true' else 'false' end) as lx " +
						 "from gc_jh_sj where sfyx='1' and bdid is null) sfgs,"+
						 //是否可以概算 end
						 "(select * from GC_SJGL_JJG jjg where jjg.sfyx='1') jjg,"+
						 "(select max(decode(tzlb,'0',JSRQ,'')) as jbgh,SJWYBH from GC_SJ_ZLSF_JS where sfyx='1'  group by SJWYBH) sjwj," +
						 "(select sj.SJWYBH,sj.wcsj_ys,cbsjpf.bz,cbsjpf.gc_sj_cbsjpf_id,cbsjpf.cbsjpf,cbsjpf.pfnr,cbsjpf.sse,cbsjpf.ssjs,cbsjpf.gcje,cbsjpf.zcje,cbsjpf.qtje,cbsjpf.gs,nvl(cbsjpf.gs,cbsjpf.sse) as gys,cbsjpf.xmid,cbsjpf.sjbh as gs_sjbh,cbsjpf.ys_gcje,cbsjpf.ys_zcje,cbsjpf.ys_qtje from Gc_Sj_Cbsjpf cbsjpf,(select * from Gc_Sj where sfyx='1') sj where sj.SJWYBH=cbsjpf.SJWYBH and cbsjpf.sfyx='1') cbsj," +
						 "(select count(*) as bg,sum(BGFY) BGFY,SJWYBH from gc_sj_sjbg_js2 where sfyx='1' and bgzt='1' group by SJWYBH) sjbg," +
						 //添加初步设计计划时间显示
						 "(select jhsj.SJWYBH as GSJWYBH,jhsj.xmid, jhsj.cbsjpf as JHGS,jhsj.cbsjpf_sj as sjgs from  GC_JH_SJ jhsj where jhsj.bdid is null and jhsj.sfyx='1') gsjh," +
						 //施工监理单位查询
						 "(select xmbd.sjdw,xmbd.sgdw,xmbd.jldw, jhsj.gc_jh_sj_id as jhsjid from gc_xmbd xmbd, gc_jh_sj jhsj where xmbd.gc_xmbd_id = jhsj.bdid and xmbd.sfyx = '1') bd, " +
						 "(select xdk.sjdw,xdk.sgdw,xdk.jldw, jhsj.gc_jh_sj_id as jhsjid from gc_tcjh_xmxdk xdk, gc_jh_sj jhsj where xdk.gc_tcjh_xmxdk_id = jhsj.xmid and jhsj.bdid is null and xdk.sfyx = '1') xm " ;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("GYS");
			bs.setFieldThousand("ZBGFY");
			bs.setFieldTranslater("SGDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("SJDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询设计信息成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String Feedback(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String result = null;
		String sjbh=request.getParameter("sjbh");
		String jhywlx=YwlxManager.GC_JH_SJ;
		String lb=request.getParameter("lb");
		String jhid=request.getParameter("jhid");
		String sjid=request.getParameter("sjid");
		String fkywlx;
		String bz="";
		TcjhVO vo = new TcjhVO();
		GcSjVO sj = new GcSjVO();
		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		BusinessUtil.setUpdateCommonFields(vo, user);
		String sjExist="select gc_sj_id,sjbh from gc_sj where sfyx='1' and  jhsjid='"+vo.getGc_jh_sj_id()+"'";
		
		String exist[][]=DBUtil.querySql(sjExist);
		//操作设计表时间选项
		int b =Integer.parseInt(lb);
		switch(b){
		case 1:
			sj.setWcsj_cqt(vo.getCqt_sj());
			break;
		case 2:
			sj.setWcsj_sgt_zsb(vo.getSgt_sj());
			break;
		case 3:
			sj.setWcsj_pqt(vo.getPqt_sj());
			break;
		case 4:
			sj.setWcsj_ys(vo.getCbsjpf_sj());
			break;
		default:
			break;
		}
		if(exist==null||exist.length==0){
			String xmid=request.getParameter("xmid");
			String bdid=request.getParameter("bdid");
			String nd=request.getParameter("nd");
			sj.setGc_sj_id(new RandomGUID().toString()); // 主键
			sjid=sj.getGc_sj_id();
			sj.setJhid(jhid);
			sj.setJhsjid(vo.getGc_jh_sj_id());
			sj.setXmid(xmid);
			sj.setBdid(bdid);
			sj.setNd(nd);
			BusinessUtil.setInsertCommonFields(sj, user);
			sj.setYwlx(YwlxManager.GC_SJ);
			EventVO eventVO1 = EventManager.createEvent(conn, sj.getYwlx(), user);//生成事件
			sj.setSjbh(eventVO1.getSjbh());
			sjbh=eventVO1.getSjbh();
			//插入
			BaseDAO.insert(conn, sj);
		}else{
			sj.setGc_sj_id(exist[0][0]);
			sjid=exist[0][0];
			sjbh=exist[0][1];
			//修改统筹计划表
			BaseDAO.update(conn, sj);
		}
		BaseDAO.update(conn, vo);
		//--操作反馈情况表
		int a =Integer.parseInt(lb);
		switch(a){
		case 1:
			fkywlx=YwlxManager.GC_SJ_BGSF_JS_CQT;
			bz=vo.getCqt_bz();
			break;
		case 2:
			fkywlx=YwlxManager.GC_SJ_BGSF_JS_SGT;
			bz=vo.getSgt_bz();
			break;
		case 3:
			fkywlx=YwlxManager.GC_SJ_BGSF_JS_PQT;
			bz=vo.getPqt_bz();
			break;
		case 4:
			fkywlx=YwlxManager.GC_SJ_CBSJPF;
			bz=vo.getCbsjpf_bz();
			break;
		case 5:
			fkywlx=YwlxManager.GC_SJGL_JJG;
			bz=vo.getJg_bz();
			break;
		default:
			fkywlx=YwlxManager.GC_SJ;
			break;
		}
		FkqkVO fkvo = new FkqkVO();
		fkvo.setGc_jh_fkqk_id(new RandomGUID().toString()); // 主键
		fkvo.setJhid(jhid);
		fkvo.setFkid(sjid);
		fkvo.setJhsjid(vo.getGc_jh_sj_id());
		fkvo.setSjbh(sjbh);
		fkvo.setFkrq(Pub.getCurrentDate());
		fkvo.setYwlx(fkywlx);
		fkvo.setBz(bz);
		BusinessUtil.setInsertCommonFields(fkvo,user);
		BaseDAO.insert(conn, fkvo);
		
		result = vo.getRowJson();
		conn.commit();
		LogManager.writeUserLog(sjbh, jhywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "反馈设计信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(sjbh, jhywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "反馈设计信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_jh_sj_id()+"\",\"fieldname\":\"gc_jh_sj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String update_result =this.querySj(request,jsona);
		return update_result;
	}

	@Override
	public String gsFeedback(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String xmid=request.getParameter("xmid");
		String result = null;
		TcjhVO vo = new TcjhVO();

		try {
		conn.setAutoCommit(false);
		
		String jhsjid="";
		String jhsjsql = "select gc_jh_sj_id from gc_jh_sj where xmid='"+xmid+"' and sfyx='1'";
		String[][] arr=DBUtil.query(conn, jhsjsql);
		if(arr==null||arr.length==0){
			jhsjid="XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
		}else{
			jhsjid = arr[0][0];
		}
		
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		BusinessUtil.setUpdateCommonFields(vo, user);
		vo.setGc_jh_sj_id(jhsjid);
		vo.setYwlx(YwlxManager.GC_JH_SJ);
		EventVO eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
		vo.setSjbh(eventVO.getSjbh());

		//插入
		BaseDAO.update(conn, vo);
		result = vo.getRowJson();
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "反馈设计信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "反馈设计信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_jh_sj_id()+"\",\"fieldname\":\"gc_jh_sj_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String update_result =this.querySj(request,jsona);
		return update_result;
	}
}
