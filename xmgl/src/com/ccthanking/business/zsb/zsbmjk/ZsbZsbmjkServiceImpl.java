package com.ccthanking.business.zsb.zsbmjk;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;


@Service
public  class ZsbZsbmjkServiceImpl implements ZsbZsbmjkService {
	private String ywlx=YwlxManager.GC_ZS_XM;
	//统计概况查询
	@Override
	public String queryTjgk(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult =""; 
		try {
		conn.setAutoCommit(false);
		String sql = "select (select count(*) from gc_jh_sj jhsj where jhsj.iszc='1') as sjcqxms," +
				"((select count(distinct jhsjid) from gc_zsb_xxb xxb where xxb.sfyx='1')-(select count(distinct jhsjid) from gc_zsb_xxb xxb where xxb.mdwcrq is null and xxb.sfyx='1')) as mdwcxms," +
				"(select count(*) from gc_jh_sj jhsj where jhsj.zc_sj is not null) as wccqxms," +
				"((select count(distinct jhsjid) from gc_zsb_xxb xxb where xxb.sfyx='1' )-(select count(distinct jhsjid) from gc_zsb_xxb xxb where xxb.cdyjrq is null and xxb.sfyx='1')) as cdyjxms," +
				"((select count(distinct jhsjid) from gc_zsb_xxb xxb where xxb.sfyx='1' )-(select count(distinct jhsjid) from gc_zsb_xxb xxb where xxb.tdfwyjrq is null and xxb.sfyx='1')) as tdfwyjxms ," +
				"((select count(distinct jhsjid) from gc_zsb_xxb xxb where xxb.sfyx='1' )-(select count(distinct jhsjid) from gc_zsb_xxb xxb where xxb.qwtrq is null and xxb.sfyx='1')) as wtxyqdwcxms," +
				"(select sum(xxb.xyje) from gc_zsb_xxb xxb where xxb.sfyx='1') as xyjezj,(select sum(xxb.zjdwje) from gc_zsb_xxb xxb where xxb.sfyx='1') as zjdwje," +
				"(select max(xxb.mdwcrq) from gc_zsb_xxb xxb where xxb.sfyx='1') as zxmdwcsj," +
				"(select sum(xxb.jmhs) from gc_zsb_xxb xxb where xxb.sfyx='1') as jmhszj," +
				"(select sum(xxb.qyjs) from gc_zsb_xxb xxb where xxb.sfyx='1') as qyjzj," +
				"(select sum(xxb.jttdmj) from gc_zsb_xxb xxb where xxb.sfyx='1') as jttdzdmjzj," +
				"(select sum(xxb.gytdmj) from gc_zsb_xxb xxb where xxb.sfyx='1') as gytdzdmjzj," +
				"(select sum(xxb.zmj) from gc_zsb_xxb xxb where xxb.sfyx='1') as tdzmjzj," +
				"(select sum(xxb.mdgs) from gc_zsb_xxb xxb where xxb.sfyx='1') as mdgshj," +
				"(select max(jdb.sbrq) from gc_zsb_jdb jdb where jdb.sfyx='1') as zxtbsj " +
				"from dual";
			BaseResultSet bs = DBUtil.query(conn, sql,null);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//居民拆迁进展
	@Override
	public String queryJmcqjz(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		conn.setAutoCommit(false);
		String sql = "select '剩余居民户数' as label," +
					"((select sum(xxb.jmhs) from gc_zsb_xxb xxb where xxb.sfyx='1') - (select sum(jdb.bcwcjms) from gc_zsb_jdb jdb where jdb.sfyx='1')) as value from dual " +
					"union all " +
					"select '已完成居民户数' as label,(select sum(jdb.bcwcjms) from gc_zsb_jdb jdb where jdb.sfyx='1') as value from dual";
		BaseResultSet bs = DBUtil.query(conn, sql,null);
		domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//企业拆迁进展
	public String queryQycqjz(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			conn.setAutoCommit(false);
			String sql = "select '剩余企业家数' as label," +
						"((select sum(xxb.qyjs) from gc_zsb_xxb xxb where xxb.sfyx='1') - (select sum(jdb.bcwcqys) from gc_zsb_jdb jdb where jdb.sfyx='1')) as value from dual " +
						"union all " +
						"select '已完成企业家数' as label,(select sum(jdb.bcwcqys) from gc_zsb_jdb jdb where jdb.sfyx='1') as value from dual";
			BaseResultSet bs = DBUtil.query(conn, sql,null);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//征地拆迁进展
	public String queryZdcqjz(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			conn.setAutoCommit(false);
			String sql = "select '剩余土地面积' as label," +
						"((select sum(xxb.zmj) from gc_zsb_xxb xxb where xxb.sfyx='1') - (select sum(jdb.bczdmj) from gc_zsb_jdb jdb where jdb.sfyx='1')) as value from dual " +
						"union all " +
						"select '已完成土地面积' as label,(select sum(jdb.bczdmj) from gc_zsb_jdb jdb where jdb.sfyx='1') as value from dual";
			BaseResultSet bs = DBUtil.query(conn, sql,null);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//拆迁进展详情
	public String queryCqjzxq(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			conn.setAutoCommit(false);
			String sql = "select dic.dic_value," +
				"nvl(zs.count,'0') as sjcqxms," +
				"nvl((zs.count - md.count),'0') as mdwcxms, " +
				"nvl((zs.count - wccq.count),'0') as wccqxms, " +
				"nvl((zs.count - cd.count),'0') as cdyjxms," +
				"nvl((zs.count - td.count),'0') as tdfwyjxms," +
				"nvl((zs.count - wt.count),'0') as wtxyqdwcxms," +
				"nvl(wtxy.xyje,'0') as xyje, " +
				"nvl(zjdw.zjdwje,'0') as zjdwje " +
				"from " +
				"(select distinct qy,count(distinct jhsjid) as count from gc_zsb_xxb where sfyx='1' group by qy) zs, " +
				"(select zsxm.qy, count(distinct zsxm.jhsjid) as count from " +
					"(select xxb.qy,jhsj.gc_jh_sj_id as jhsjid,jhsj.zc_sj from gc_zsb_xxb xxb,gc_jh_sj jhsj where xxb.jhsjid=jhsj.gc_jh_sj_id and  xxb.sfyx='1') zsxm " +
						"where zsxm.zc_sj is null group by qy) wccq, " +
				"(select distinct qy, count(distinct jhsjid) as count from gc_zsb_xxb xxb where xxb.sfyx='1' and xxb.mdwcrq is null group by qy) md," +
				"fs_dic_tree dic, " +
				"(select distinct qy, count(distinct jhsjid) as count from gc_zsb_xxb xxb where xxb.sfyx='1' and xxb.cdyjrq is null group by qy) cd , " +
				"(select distinct qy, count(distinct jhsjid) as count from gc_zsb_xxb xxb where xxb.sfyx='1' and xxb.tdfwyjrq is null group by qy) td, " +
				"(select distinct qy, count(distinct jhsjid) as count from gc_zsb_xxb xxb where xxb.sfyx='1' and xxb.qwtrq is null group by qy) wt, " +
				"(select xxb.qy,sum(xxb.xyje) as xyje from gc_zsb_xxb xxb where xxb.sfyx='1'group by xxb.qy) wtxy, " +
				"(select xxb.qy,sum(xxb.zjdwje) as zjdwje from gc_zsb_xxb xxb where xxb.sfyx='1'group by xxb.qy) zjdw " +
					"where zs.qy(+)= dic.dic_code and " +
						"wccq.qy(+)=dic.dic_code and " +
						"cd.qy(+)=dic.dic_code and " +
						"md.qy(+)=dic.dic_code and " +
						"td.qy(+)=dic.dic_code and " +
						"wt.qy(+)=dic.dic_code and " +
						"wtxy.qy(+)=dic.dic_code and " +
						"zjdw.qy(+)=dic.dic_code and " +
						"dic.dic_name_code='QY' and dic.parent_id !='0'";
			BaseResultSet bs = DBUtil.query(conn, sql,null);
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

}
