package com.ccthanking.business.gk;

import java.sql.Connection;

import org.springframework.stereotype.Service;

import com.ccthanking.business.xmcbk.xmwh.XmcbkwhService;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.RequestUtil;
@Service
public class GkServiceImpl  implements GkService{

	/*	
	 * 计划概况报表
	 * 
	 */
	@Override
	public String query_jhgk(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {	
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			conn.setAutoCommit(false);
			String sql = "select sum(decode(c.cqt_sj,'',0,1)) CQT_SJ,sum(decode(c.WGSJ_SJ,'',0,1)) WGSJ_SJ,sum(decode(c.pqt_sj,'',0,1)) pqt_sj,sum(decode(c.KGSJ_SJ,'',0,1)) KGSJ_SJ,count(*) zs,f.dic_value as xmglgs from  gc_jh_sj c ,gc_tcjh_xmxdk d,FS_DIC_TREE f where c.XMID=d.gc_tcjh_xmxdk_id and f.dic_code=d.xmglgs and f.parent_id='1000000000189'  group by d.xmglgs,f.dic_value  union all  select sum(decode(c.cqt_sj,'',0,1)) CQT_SJ,sum(decode(c.WGSJ_SJ,'',0,1)) WGSJ_SJ,sum(decode(c.pqt_sj,'',0,1)) pqt_sj,sum(decode(c.KGSJ_SJ,'',0,1)) KGSJ_SJ,count(*) zs,'合计' dic_value  from  gc_jh_sj c ,gc_tcjh_xmxdk d,FS_DIC_TREE f where c.XMID=d.gc_tcjh_xmxdk_id and f.dic_code=d.xmglgs and f.parent_id='1000000000189'";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			LogManager.writeUserLog(null, null,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询计划概况信息", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	/*	
	 * 统筹计划概况报表
	 * 
	 */
	@Override
	public String query_tcjh(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {	
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			conn.setAutoCommit(false);
			String sql = " select  b.JHPCH JCPC,b.LRSJ XDSJ,sum(decode(A.GC_JH_SJ_ID,'',0,1)) ZS,sum(decode(C.XMSX,'1',1,0))  XMSX,sum(decode(C.ISBT,'1',1,0)) ISBT,sum(decode(C.JHZTZE,'',01,C.JHZTZE)) JHZTZE,sum(decode(f.dic_value,'中豪',1,0)) zhonghao,sum(decode(f.dic_value,'华星',1,0)) huaixng,sum(decode(f.dic_value,'鸿安',1,0)) hongan,sum(decode(f.dic_value,'其它',1,0)) qita from gc_jh_sj a ,gc_jh_zt b ,gc_tcjh_xmxdk c,FS_DIC_TREE f where a.jhid=b.gc_jh_zt_id and a.xmid=c.GC_TCJH_XMXDK_ID and f.dic_code=c.xmglgs and f.parent_id='1000000000189' group by b.JHPCH ,b.LRSJ,f.dic_value";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			LogManager.writeUserLog(null, null,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询统筹计划概况信息", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	
	/*	
	 * 计财计划概况报表
	 * 
	 */
	@Override
	public String query_jcjh(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {	
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			conn.setAutoCommit(false);
			String sql = "select sum(decode(c.cqt_sj,'',0,1)) CQT_SJ,sum(decode(c.WGSJ_SJ,'',0,1)) WGSJ_SJ,sum(decode(c.pqt_sj,'',0,1)) pqt_sj,sum(decode(c.KGSJ_SJ,'',0,1)) KGSJ_SJ,count(*) zs,f.dic_value as xmglgs from  gc_jh_sj c ,gc_tcjh_xmxdk d,FS_DIC_TREE f where c.XMID=d.gc_tcjh_xmxdk_id and f.dic_code=d.xmglgs and f.parent_id='1000000000189'  group by d.xmglgs,f.dic_value  union all  select sum(decode(c.cqt_sj,'',0,1)) CQT_SJ,sum(decode(c.WGSJ_SJ,'',0,1)) WGSJ_SJ,sum(decode(c.pqt_sj,'',0,1)) pqt_sj,sum(decode(c.KGSJ_SJ,'',0,1)) KGSJ_SJ,count(*) zs,'合计' dic_value  from  gc_jh_sj c ,gc_tcjh_xmxdk d,FS_DIC_TREE f where c.XMID=d.gc_tcjh_xmxdk_id and f.dic_code=d.xmglgs and f.parent_id='1000000000189'";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			LogManager.writeUserLog(null, null,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询计财计划下达概况信息", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
}
