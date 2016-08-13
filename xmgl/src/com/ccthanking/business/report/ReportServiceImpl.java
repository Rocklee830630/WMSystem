package com.ccthanking.business.report;

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class ReportServiceImpl implements ReportService {
	String lrbm = "";

	@Override
	public String queryReport(HttpServletRequest request) throws Exception {
		// User user = (User)
		// request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String condition = " where ";
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		if (Pub.empty(start)) {
			if (Pub.empty(end)) {
				condition += "to_char(lrsj,'yyyy-mm-dd')<=to_char(sysdate,'yyyy-mm-dd') ";
			} else {
				condition += "lrsj<=to_date('" + end + "','yyyy-mm-dd') ";
			}
		} else {
			if (Pub.empty(end)) {
				condition += "lrsj>=to_date('" + start + "','yyyy-mm-dd') ";
			} else {
				condition += "lrsj<=to_date('" + end
						+ "','yyyy-mm-dd') and lrsj>=to_date('" + start
						+ "','yyyy-mm-dd') ";
			}
		}
		String domresult = "";
		String zsxx = "select lrsj, lrr, lrbm, gxsj, gxr from gc_zsb_jdb union all select lrsj, lrbm, lrr, gxsj, gxr from gc_zsb_xmb union all select lrsj, lrr, lrbm, gxsj, gxr from gc_zsb_xxb";
		String qqsx = "select lrsj,lrr,gxsj,gxr,lrbm from gc_qqsx_sxfj union all select lrsj,lrr,gxsj,gxr,lrbm from gc_zgb_qqsx";
		String sjxx = "select lrsj,lrr,gxsj,gxr,lrbm from gc_sj_bgsf_js union all select lrsj,lrr,gxsj,gxr,lrbm from gc_sj_cbsjpf union all select lrsj,lrr,gxsj,gxr,lrbm from gc_sj_cbsjpf union all select lrsj,lrr,gxsj,gxr,lrbm from gc_sj_sjbg_js2 union all select lrsj,lrr,gxsj,gxr,lrbm from  gc_sj_xgzl_lq union all select lrsj,lrr,gxsj,gxr,lrbm from gc_sj_zlsf_js union all select lrsj,lrr,gxsj,gxr,lrbm from gc_sjgl_jjg union all select lrsj,lrr,gxsj,gxr,lrbm from gc_sj";
		String zjxx = "select lrsj,lrr,gxsj,gxr,lrbm from gc_zjb_dyqk union all select lrsj,lrr,gxsj,gxr,lrbm from gc_zjb_lbjb union all select lrsj,lrr,gxsj,gxr,lrbm from gc_zjb_jsb";
		String zlaq = "select lrsj,lrr,gxsj,gxr,lrbm from gc_zlaq_jcb union all select lrsj,lrr,gxsj,gxr,lrbm from gc_zlaq_zgb";
		String xmgl = "select lrsj,lrr,gxsj,gxr,lrbm from GC_XMGLGS_JLB union all select lrsj,lrr,gxsj,gxr,lrbm from GC_XMGLGS_ZBB union all select lrsj,lrr,gxsj,gxr,lrbm from GC_GCGL_GCQS union all select lrsj,lrr,gxsj,gxr,lrbm from GC_GCB_KFG union all select lrsj,lrr,gxsj,gxr,lrbm from gc_zlaq_jcb union all select lrsj,lrr,gxsj,gxr,lrbm from gc_zlaq_zgb union all select lrsj,lrr,gxsj,gxr,lrbm from GC_XMGLGS_XXJD union all select lrsj,lrr,gxsj,gxr,lrbm from GC_XMGLGS_XXJD_JHBZ union all select lrsj,lrr,gxsj,gxr,lrbm from GC_XMGLGS_XXJD_JHFK ";
		String[][] arr = { { zsxx, "100000000005" }, { qqsx, "100000000014" },
				{ sjxx, "100000000016" }, { zjxx, "100000000004" },
				{ zlaq, "100000000006" }, { xmgl, "100000000010" } };
		String queryCondition = "";
		for (int i = 0; i < arr.length; i++) {
			queryCondition = queryCondition
					+ ""
					+ "select '"
					+ arr[i][1]
					+ "' as bmmc,yw.ywsl,yw.ywmx,yw.ywlj,yw.ywljmx,decode(fw.fwsl,'',0,fw.fwsl) as fwsl,decode(fw.fwlj,'',0,fw.fwlj) as fwlj,decode(tzgg.tzsl,'',0,tzgg.tzsl) as tzsl,decode(tzgg.tzlj,'',0,tzgg.tzlj) as tzlj from "
					+ "(select lrbm,count(decode(to_char(lrsj,'mm-dd'),to_char(sysdate,'mm-dd'),lrsj,'')) as ywsl,count(lrsj) as ywlj, "
					+
					// 业务输入情况明细（本日）
					"(select wmsys.wm_concat( distinct (select to_char(name) from fs_org_person where account = lrr) ||':'|| count(decode(to_char(lrsj,'mm-dd'),to_char(sysdate,'mm-dd'),lrsj,''))) "
					+ "from ("
					+ arr[i][0]
					+ ") where lrbm='"
					+ arr[i][1]
					+ "' group by lrr) as ywmx, "
					+
					// 业务输入情况明细（累计）
					"(select wmsys.wm_concat( distinct (select to_char(name) from fs_org_person where account = lrr) ||':'|| count(lrsj)) "
					+ "from ("
					+ arr[i][0]
					+ ") "
					+ condition
					+ " and lrbm='"
					+ arr[i][1]
					+ "' group by lrr) as ywljmx "
					+ "from ("
					+ arr[i][0]
					+ ") "
					+ condition
					+ " and lrbm='"
					+ arr[i][1]
					+ "' group by lrbm) yw,"
					+
					// 发文信息
					"(select count(decode(to_char(lrsj,'mm-dd'),to_char(sysdate,'mm-dd'),fwid,'')) as fwsl,count(lrsj) as fwlj,lrbm as fwbm "
					+ "from XTBG_GWGL_FWGL "
					+ condition
					+ " and lrbm='"
					+ arr[i][1]
					+ "' group by lrbm ) fw,"
					+
					// 通知公告信息
					"(select count(decode(to_char(lrsj,'mm-dd'),to_char(sysdate,'mm-dd'),ggid,'')) as tzsl,count(lrsj) as tzlj,lrbm as tzbm "
					+ "from XTBG_XXZX_GGTZ " + condition + " and lrbm='"
					+ arr[i][1] + "' group by lrbm ) tzgg "
					+ "where fw.fwbm(+)=yw.lrbm and tzgg.tzbm(+)=yw.lrbm";
			if (i < arr.length - 1)
				queryCondition += " union all ";
		}
		try {
			PageManager page = new PageManager();
			// page.setFilter(condition);
			conn.setAutoCommit(false);

			String sql = queryCondition;

			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldTranslater("BMMC", "FS_ORG_DEPT", "ROW_ID", "DEPT_NAME");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	public String monitor(HttpServletRequest request, String json)
			throws Exception {
		// User user = (User)
		// request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		Statement stmt = null;
		String end = request.getParameter("end");
		String start = request.getParameter("start");
		String dic = request.getParameter("dic");

		String endCondition = null;
		String dateCondition = "";
		// String dicCondition="";
		// 部门查询条件
		// dicCondition=Pub.empty(dic)?"":(" where tab.lrbm in ("+dic+")");
		// 时间查询条件
		if (Pub.empty(end)) {
			dateCondition = Pub.empty(start) ? ""
					: (" and to_char(lrsj,'yyyy-mm-dd')>='" + start + "'");
			endCondition = "sysdate";
		} else {
			dateCondition = Pub.empty(start) ? (" and to_char(lrsj,'yyyy-mm-dd')<='"
					+ end + "'")
					: (" and  to_char(lrsj,'yyyy-mm-dd') between '" + start
							+ "' and '" + end + "'");
			endCondition = "to_date('" + end + "','yyyy-mm-dd')";
		}
		try {
			PageManager page = RequestUtil.getPageManager(json);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String exitTable = "select table_name from user_tables where table_name='TEM_TABLE'";
			String exit[][] = DBUtil.querySql(conn, exitTable);
			if (exit == null || exit.length == 0) {
				// 创建临时表
				String table = "create global temporary table tem_table(LRR VARCHAR2(30),LRSJ DATE,LRBM VARCHAR2(30),SORT NUMBER) on commit delete rows";
				stmt.executeUpdate(table);
			}
			// 定义所有的表
			String tab = "select TABLE_NAME from user_tab_columns where column_name='LRR' and table_name like 'GC_%'";
			String tab_name[][] = DBUtil.querySql(conn, tab);
			String insert = "";
			for (int i = 0; i < tab_name.length; i++) {
				insert = "insert into tem_table(lrsj,lrr,lrbm,sort)(select lrsj,lrr,lrbm,(select sort from FS_ORG_DEPT where row_id=lrbm) from "
						+ tab_name[i][0]
						+ " where lrbm is not null and lrbm !='200000000000'"
						+ dateCondition + ") ";
				stmt.executeUpdate(insert);
			}
			// 查询语句，这里过滤超级管理员部门以及时间问题
			String query_sql = "select tab.lrbm as bmmc,sort,"
					+ "count(lrsj) as LJZL,"
					+ "wmsys.wm_concat(distinct (select to_char(name) from fs_org_person where account = lrr)||':'||(select count(a.lrsj) from tem_table a where a.lrr=tab.lrr)) as LJGR,"
					+ "(select wmsys.wm_concat(distinct (select to_char(name) from fs_org_person where account = now.lrr)||':'||(select sum(decode(to_char(b.lrsj,'yyyy-mm-dd'),to_char("
					+ endCondition
					+ ",'yyyy-mm-dd'),1,0)) from tem_table b where b.lrr=now.lrr) )from tem_table now where to_char(now.lrsj,'yyyy-mm-dd')=to_char("
					+ endCondition
					+ ",'yyyy-mm-dd') and now.lrbm(+)=tab.lrbm group by now.lrbm) as JRGR,"
					+ "sum(decode(to_char(tab.lrsj,'yyyy-mm-dd'),to_char("
					+ endCondition + ",'yyyy-mm-dd'),1,0)) as JRZL  "
					+ "from tem_table tab group by lrbm,sort";
			// 主要查询语句，部门部分建议在这里修改
			// 部门名称查询
			String bm = "";
			String bm_sql = "";
			bm = Pub.empty(dic) ? "" : ("dept.row_id as");
			bm_sql = Pub.empty(dic) ? ("(" + query_sql + ") order by sort asc")
					: ("("
							+ query_sql
							+ ") lrqk,FS_ORG_DEPT dept where lrqk.bmmc(+)=dept.row_id and dept.row_id in ("
							+ dic + ") order by dept.sort asc");
			String sql = "select "
					+ bm
					+ " bmmc, decode(ljzl,null,0,ljzl) as ljzl,decode(ljgr,null,'—',ljgr) as ljgr,decode(jrgr,null,'—',jrgr) as jrgr,decode(jrzl,null,0,jrzl) as jrzl from "
					+ bm_sql + "";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldTranslater("BMMC", "FS_ORG_DEPT", "ROW_ID", "DEPT_NAME");
			domresult = bs.getJson();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	public String bm_monitor(HttpServletRequest request, String json)
			throws Exception {
		// User user = (User)
		// request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		Statement stmt = null;
		String end = request.getParameter("end");
		String start = request.getParameter("start");
		String dic = request.getParameter("dic");

		String endCondition = null;
		String dateCondition = "";
		String dicCondition = "";
		String dlTimeCondition = "";
		String lcTimeCondition = "";
		String cjTimeCondition = "";
		String lrTimeCondition = "";
		String fbTimeCondition = "";
		// 部门查询条件
		dicCondition = Pub.empty(dic) ? " and dept.row_id not in ('100000000008','100000000000','100000000007')"
				: (" and dept.row_id in (" + dic + ") ");
		// 业务表时间查询条件
		if (Pub.empty(end)) {
			dateCondition = Pub.empty(start) ? ""
					: (" and to_char(lrsj,'yyyy-mm-dd')>='" + start + "'");
			dlTimeCondition = Pub.empty(start) ? ""
					: (" and LOGINTIME >= TO_DATE('"+start+"000000', 'YYYY-MM-DDHH24MISS') ");
			lcTimeCondition = Pub.empty(start) ? ""
					: (" and CREATETIME >= TO_DATE('"+start+"000000', 'YYYY-MM-DDHH24MISS') ");
			cjTimeCondition = Pub.empty(start) ? ""
					: (" and CJSJ >= TO_DATE('"+start+"000000', 'YYYY-MM-DDHH24MISS') ");
			lrTimeCondition = Pub.empty(start) ? ""
					: (" and A.LRSJ >= TO_DATE('"+start+"000000', 'YYYY-MM-DDHH24MISS') ");
			fbTimeCondition = Pub.empty(start) ? ""
					: (" and FBSJ >= TO_DATE('"+start+"000000', 'YYYY-MM-DDHH24MISS') ");
			endCondition = "sysdate";
		} else {
			dateCondition = Pub.empty(start) ? (" and to_char(lrsj,'yyyy-mm-dd')<='"
					+ end + "'")
					: (" and  to_char(lrsj,'yyyy-mm-dd') between '" + start
							+ "' and '" + end + "'");
			endCondition = "to_date('" + end + "','yyyy-mm-dd')";

			dlTimeCondition = Pub.empty(start) ? (" and LOGINTIME <= TO_DATE('"+end+"235959', 'YYYY-MM-DDHH24MISS') ")
					: (" and LOGINTIME >= TO_DATE('"+start+"000000', 'YYYY-MM-DDHH24MISS') and LOGINTIME <= TO_DATE('"+end+"235959', 'YYYY-MM-DDHH24MISS') ");
			lcTimeCondition = Pub.empty(start) ? (" and CREATETIME <= TO_DATE('"+end+"235959', 'YYYY-MM-DDHH24MISS') ")
					: (" and CREATETIME >= TO_DATE('"+start+"000000', 'YYYY-MM-DDHH24MISS') and CREATETIME <= TO_DATE('"+end+"235959', 'YYYY-MM-DDHH24MISS') ");
			cjTimeCondition = Pub.empty(start) ? (" and CJSJ <= TO_DATE('"+end+"235959', 'YYYY-MM-DDHH24MISS') ")
					: (" and CJSJ >= TO_DATE('"+start+"000000', 'YYYY-MM-DDHH24MISS') and CJSJ <= TO_DATE('"+end+"235959', 'YYYY-MM-DDHH24MISS') ");
			lrTimeCondition = Pub.empty(start) ? (" and A.LRSJ <= TO_DATE('"+end+"235959', 'YYYY-MM-DDHH24MISS') ")
					: (" and A.LRSJ >= TO_DATE('"+start+"000000', 'YYYY-MM-DDHH24MISS') and A.LRSJ <= TO_DATE('"+end+"235959', 'YYYY-MM-DDHH24MISS') ");
			fbTimeCondition = Pub.empty(start) ? (" and FBSJ <= TO_DATE('"+end+"235959', 'YYYY-MM-DDHH24MISS') ")
					: (" and FBSJ >= TO_DATE('"+start+"000000', 'YYYY-MM-DDHH24MISS') and FBSJ <= TO_DATE('"+end+"235959', 'YYYY-MM-DDHH24MISS') ");
		}
		// 操作日志时间查询条件
		String operaDate = "";
		if (Pub.empty(end)) {
			operaDate = Pub.empty(start) ? ""
					: (" and to_char(OPERATETIME,'yyyy-mm-dd')>='" + start + "'");
		} else {
			operaDate = Pub.empty(start) ? (" and to_char(OPERATETIME,'yyyy-mm-dd')<='"
					+ end + "'")
					: (" and  to_char(OPERATETIME,'yyyy-mm-dd') between '"
							+ start + "' and '" + end + "'");
		}

		try {
			PageManager page = RequestUtil.getPageManager(json);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String exitTable = "select table_name from user_tables where table_name='TEM_MONITOR_TABLE'";
			String exit[][] = DBUtil.querySql(conn, exitTable);
			if (exit == null || exit.length == 0) {
				// 创建临时表
				String table = "create global temporary table tem_monitor_table(LRR VARCHAR2(30),LRSJ DATE,LRBM VARCHAR2(30),YWMK VARCHAR2(1000)) on commit delete rows";
				stmt.executeUpdate(table);
			}
			// 定义所有的业务表
			String sj_table[][] = {
					// 设计
//					{ "\'概算\'", "gc_sj_cbsjpf" },
//					{ "\'设计任务书\'", "GC_SJGL_RWSGL" },
//					{ "\'交竣工\'", "GC_SJGL_JJG" },
//					{ "\'监测检测\'", "GC_SJ_BGSF_JS" },
//					{ "\'设计变更\'", "GC_SJ_SJBG_JS2" },
//					{ "\'设计文件管理\'", "GC_SJ_ZLSF_JS" },
					{ "\'设计相关资料领取\'", "GC_SJ_XGZL_LQ" },
					{"\'设计任务书新增\'","GC_SJGL_RWSGL"},//已处理
					// 质量安全
					{ "\'质量安全信息-检查\'", "GC_ZLAQ_JCB" },
					// 项目管理公司
//					{ "\'工程计量\'", "GC_XMGLGS_JLB" },
//					{ "\'工程洽商\'", "GC_GCGL_GCQS" },
//					{ "\'开复工令\'", "GC_GCB_KFG" },
//					{ "\'形象进度\'", "GC_XMGLGS_XXJD_JHBZ" },
					// 排迁
//					{ "\'排迁外业信息\'", "GC_PQ_ZXM" },
					// 招投标
					{ "\'部门招标需求新增\'", "GC_ZTB_XQ" },//已处理
//					{ "\'招投标管理\'", "GC_ZTB_SJ" },
					// 征收
//					{ "\'征收任务\'", "GC_ZSB_XXB" },
//					{ "\'征收进度\'", "GC_ZSB_JDB" },
					// 结算、拦标价
					{ "\'拦标价新增\'", "GC_ZJB_LBJB" },//已处理
//					{ "\'结算管理\'", "GC_ZJB_JSB" },
					// 项目储备库
					{ "\'项目储备库新增\'", "gc_tcjh_xmcbk" },
					{ "\'工程周报管理\'", "gc_xmglgs_zbb" },
					// 标段划分
					{ "\'标段划分新增\'", "gc_xmbd" },//已处理
					// 部门合同管理
					{ "\'部门合同新增\'", "gc_htgl_ht" } };//已处理
			String insert = "";
			for (int i = 0; i < sj_table.length; i++) {
				insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select lrsj,lrr,lrbm,to_char("
						+ sj_table[i][0]
						+ ") from "
						+ sj_table[i][1]
						+ " where lrbm is not null and lrbm !='100000000000' and sfyx='1' "
						+ dateCondition + ") ";
				stmt.executeUpdate(insert);
			}
			//修改储备库项目成功
			String xdcbkxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'项目储备库修改' from FS_LOG_USEROPERATION where memo like '%修改储备库项目成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(xdcbkxg_insert);
			//删除项目信息成功
			String xdcbksc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'项目储备库删除' from FS_LOG_USEROPERATION where memo like '%删除项目信息成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(xdcbksc_insert);
			//两委一局新增节点成功
			String lwyjxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'两委一局新增节点' from FS_LOG_USEROPERATION where memo like '%两委一局新增节点成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(lwyjxz_insert);
			//两委一局修改节点成功
			String lwyjxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'两委一局修改节点' from FS_LOG_USEROPERATION where memo like '%两委一局修改节点成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(lwyjxg_insert);
			//两委一局删除节点成功
			String lwyjsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'两委一局删除节点' from FS_LOG_USEROPERATION where memo like '%两委一局删除节点成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(lwyjsc_insert);
			//下达项目成功
			String cbkxdxm_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'两委一局下达项目' from FS_LOG_USEROPERATION where memo like '%下达项目成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(cbkxdxm_insert);
			//更新<统筹计划数据>成功
			String tcjhbz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'统筹计划编制' from FS_LOG_USEROPERATION where memo like '%更新<统筹计划数据>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tcjhbz_insert);
			//添加<版本项目>成功
			String tcjhbzbb_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'统筹计划保存版本编制' from FS_LOG_USEROPERATION where memo like '%添加<版本项目>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tcjhbzbb_insert);
			//更新<下达项目库>成功
			String xdxmkwh_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'项目下达库维护' from FS_LOG_USEROPERATION where memo like '%更新<下达项目库>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(xdxmkwh_insert);
			//<按计划下达>成功
			String xdxmkxdajh_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'项目下达库按计划下达' from FS_LOG_USEROPERATION where memo like '%<按计划下达>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(xdxmkxdajh_insert);
			//<按项目下达>成功
			String xdxmkxdaxm_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'项目下达库按项目下达' from FS_LOG_USEROPERATION where memo like '%<按项目下达>成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(xdxmkxdaxm_insert);
			//<按计划下达统筹>成功
			String jhxfapc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'下达库按批次下发' from FS_LOG_USEROPERATION where memo like '%<按计划下达统筹>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(jhxfapc_insert);
			//<按项目下达统筹>成功
			String jhxfaxm_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'下达库按项目下发' from FS_LOG_USEROPERATION where memo like '%<按项目下达统筹>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(jhxfaxm_insert);
			//添加土地手续内容成功
			String tdspxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'土地审批新增' from FS_LOG_USEROPERATION where memo like '%添加土地手续内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tdspxz_insert);
			//修改土地手续内容成功
			String tdspxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'土地审批修改' from FS_LOG_USEROPERATION where memo like '%修改土地手续内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tdspxg_insert);
			//删除土地手续内容成功
			String tdspsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'土地审批删除' from FS_LOG_USEROPERATION where memo like '%删除土地手续内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tdspsc_insert);
			//土地审批手续部门反馈成功
			String tdspbmfk_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'土地审批部门反馈' from FS_LOG_USEROPERATION where memo like '%土地审批手续部门反馈成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tdspbmfk_insert);
			//添加规划审批办理手续的附件内容成功
			String ghspxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'规划审批新增' from FS_LOG_USEROPERATION where memo like '%添加规划审批办理手续的附件内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(ghspxz_insert);
			//修改规划审批办理手续的附件内容成功
			String ghspxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'规划审批修改' from FS_LOG_USEROPERATION where memo like '%修改规划审批办理手续的附件内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(ghspxg_insert);
			//删除规划审批办理手续的附件内容成功
			String ghspsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'规划审批删除' from FS_LOG_USEROPERATION where memo like '%删除规划审批办理手续的附件内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(ghspsc_insert);
			//规划审批手续部门反馈成功
			String ghspbmfk_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'规划审批部门反馈' from FS_LOG_USEROPERATION where memo like '%规划审批手续部门反馈成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(ghspbmfk_insert);
			//添加立项可研办理手续的附件内容成功
			String lxkyxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'立项可研新增' from FS_LOG_USEROPERATION where memo like '%添加立项可研办理手续的附件内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(lxkyxz_insert);
			//修改立项可研办理手续的附件内容成功
			String lxkyxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'立项可研修改' from FS_LOG_USEROPERATION where memo like '%修改立项可研办理手续的附件内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(lxkyxg_insert);
			//删除立项可研办理手续的附件内容成功
			String lxkysc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'立项可研删除' from FS_LOG_USEROPERATION where memo like '%删除立项可研办理手续的附件内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(lxkysc_insert);
			//立项可研手续部门反馈成功
			String lxkybmfk_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'立项可研部门反馈' from FS_LOG_USEROPERATION where memo like '%立项可研手续部门反馈成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(lxkybmfk_insert);
			//添加施工许可办理手续的附件内容成功
			String sgxkxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'施工许可新增' from FS_LOG_USEROPERATION where memo like '%添加施工许可办理手续的附件内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sgxkxz_insert);
			//修改施工许可办理手续的附件内容成功
			String sgxkxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'施工许可修改' from FS_LOG_USEROPERATION where memo like '%修改施工许可办理手续的附件内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sgxkxg_insert);
			//删除施工许可手续内容成功
			String sgxksc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'施工许可删除' from FS_LOG_USEROPERATION where memo like '%删除施工许可手续内容成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sgxksc_insert);
			//施工许可手续部门反馈成功
			String sgxkbmfk_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'施工许可部门反馈' from FS_LOG_USEROPERATION where memo like '%施工许可手续部门反馈成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sgxkbmfk_insert);
			
			//履约保证金新增成功
			String lybzjxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'履约保证金新增' from FS_LOG_USEROPERATION where memo like '%履约保证金新增成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(lybzjxz_insert);
			//履约保证金修改成功
			String lybzjxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'履约保证金修改' from FS_LOG_USEROPERATION where memo like '%履约保证金修改成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(lybzjxg_insert);
			//履约保证金删除成功
			String lybzjsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'履约保证金删除' from FS_LOG_USEROPERATION where memo like '%履约保证金删除成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(lybzjsc_insert);
			//提请款新增成功
			String tqkxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'提请款新增' from FS_LOG_USEROPERATION where memo like '%提请款新增成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tqkxz_insert);
			//提请款修改成功
			String tqkxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'提请款修改' from FS_LOG_USEROPERATION where memo like '%提请款修改成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tqkxg_insert);
			//提请款删除成功
			String tqksc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'提请款删除' from FS_LOG_USEROPERATION where memo like '%提请款删除成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tqksc_insert);
			//提请款明细新增成功
			String tqkmxxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'提请款明细新增' from FS_LOG_USEROPERATION where memo like '%提请款明细新增成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tqkmxxz_insert);
			//提请款明细修改成功
			String tqkmxxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'提请款明细修改' from FS_LOG_USEROPERATION where memo like '%提请款明细修改成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tqkmxxg_insert);
			//提请款明细删除成功
			String tqkmxsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'提请款明细删除' from FS_LOG_USEROPERATION where memo like '%提请款明细删除成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(tqkmxsc_insert);
			//插入概算信息成功
			String gsxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'概算新增' from FS_LOG_USEROPERATION where memo like '%插入概算信息成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(gsxz_insert);
			//修改概算信息成功
			String gsxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'概算修改' from FS_LOG_USEROPERATION where memo like '%修改概算信息成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(gsxg_insert);
			//资料收取新增成功
			String sjwjxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'设计文件新增' from FS_LOG_USEROPERATION where memo like '%资料收取新增成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sjwjxz_insert);
			//资料收取修改成功
			String sjwjxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'设计文件修改' from FS_LOG_USEROPERATION where memo like '%资料收取修改成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sjwjxg_insert);
			//删除设计文件管理（接收）信息成功
			String sjwjsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'设计文件删除' from FS_LOG_USEROPERATION where memo like '%删除设计文件管理（接收）信息成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sjwjsc_insert);
			//设计变更新增成功
			String sjbgxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'设计变更新增' from FS_LOG_USEROPERATION where memo like '%设计变更添加成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sjbgxz_insert);
			//设计变更领取成功
			String sjbgxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'设计变更修改' from FS_LOG_USEROPERATION where memo like '%设计变更修改成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sjbgxg_insert);
			//删除变更管理（接收）信息成功
			String sjbgsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'设计变更删除' from FS_LOG_USEROPERATION where memo like '%删除变更管理（接收）信息成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sjbgsc_insert);
			//修改标段信息成功
			String bdhfxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'标段划分修改' from FS_LOG_USEROPERATION where memo like '%修改标段信息成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(bdhfxg_insert);
			
//			// 前期手续
//			String qq_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select lrsj,lrr,lrbm,(case dfl when '0' then '立项可研' when '1' then '规划审批'  when '2' then '土地审批'  when '3' then '施工许可' end) from gc_qqsx_sxfj where lrbm is not null and lrbm !='100000000000' and sfyx='1' "
//					+ dateCondition + ") ";
//			stmt.executeUpdate(qq_insert);
//			// 统筹计划编制
//			String tcjh_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select lrsj,lrr,lrbm,'统筹计划编制' from gc_jh_sj where lrbm is not null and lrbm !='100000000000' and sfyx='1' and bdid is null "
//					+ dateCondition + ") ";
//			stmt.executeUpdate(tcjh_insert);
			//排迁子项目添加成功
			String pqrwxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'排迁任务新增' from FS_LOG_USEROPERATION where memo like '%排迁子项目添加成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(pqrwxz_insert);
			//排迁子项目修改成功
			String pqrwxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'排迁任务修改' from FS_LOG_USEROPERATION where memo like '%排迁子项目修改成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(pqrwxg_insert);
			//排迁子项目删除成功
			String pqrwsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'排迁任务删除' from FS_LOG_USEROPERATION where memo like '%排迁子项目删除成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(pqrwsc_insert);
			//排迁进展剩余添加成功
			String pqjzxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'排迁进展新增' from FS_LOG_USEROPERATION where memo like '%排迁进展剩余添加成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(pqjzxz_insert);
			//排迁进展反馈修改成功
			String pqjzxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'排迁进展修改' from FS_LOG_USEROPERATION where memo like '%排迁进展反馈修改成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(pqjzxg_insert);
			//排迁进展反馈删除成功
			String pqjzsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'排迁进展删除' from FS_LOG_USEROPERATION where memo like '%排迁进展反馈删除成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(pqjzsc_insert);
			// 排迁内业
			String pqny_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'排迁内业信息' from FS_LOG_USEROPERATION where memo like '%排迁业内情况操作成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(pqny_insert);
			//插入征收详细信息数据成功
			String zsrwxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'征收任务新增' from FS_LOG_USEROPERATION where memo like '%插入征收详细信息数据成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(zsrwxz_insert);
			//修改征收详细信息数据成功
			String zsrwxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'征收任务修改' from FS_LOG_USEROPERATION where memo like '%修改征收详细信息数据成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(zsrwxg_insert);
			//删除征收详细信息数据成功
			String zsrwsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'征收任务删除' from FS_LOG_USEROPERATION where memo like '%删除征收详细信息数据成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(zsrwsc_insert);
			//插入征收进度信息数据成功
			String zsjzxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'征收进展新增' from FS_LOG_USEROPERATION where memo like '%插入征收进度信息数据成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(zsjzxz_insert);
			//修改征收进度信息数据成功
			String zsjzxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'征收进展修改' from FS_LOG_USEROPERATION where memo like '%修改征收进度信息数据成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(zsjzxg_insert);
			//删除征收进度信息数据成功
			String zsjzsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'征收进展删除' from FS_LOG_USEROPERATION where memo like '%删除征收进度信息数据成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(zsjzsc_insert);
			
			// 设计任务书修改
			String sjrwqxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'设计任务书修改' from FS_LOG_USEROPERATION where memo like '%修改设计任务书成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sjrwqxg_insert);
			// 设计任务书删除
			String sjrwqsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'设计任务书删除' from FS_LOG_USEROPERATION where memo like '%删除设计任务书成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sjrwqsc_insert);
			//招标需求修改成功
			String zbxqxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'招标需求修改' from FS_LOG_USEROPERATION where memo like '%招标需求修改成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(zbxqxg_insert);
			//招标需求删除成功
			String zbxqsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'招标需求删除' from FS_LOG_USEROPERATION where memo like '%招标需求删除成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(zbxqsc_insert);
			//启动招标需求成功
			String qdzbxq_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'启动招标' from FS_LOG_USEROPERATION where memo like '%启动招标需求成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(qdzbxq_insert);
			//招投标管理修改成功
			String ztbglxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'招投标管理' from FS_LOG_USEROPERATION where memo like '%招投标管理修改成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(ztbglxg_insert);
			//计划数据子中标价修改成功
			String jehf_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'招标金额划分' from FS_LOG_USEROPERATION where memo like '%计划数据子中标价修改成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(jehf_insert);
			//合同信息修改成功
			String bmhtxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'部门合同修改' from FS_LOG_USEROPERATION where memo like '%合同信息修改成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(bmhtxg_insert);
			//合同信息删除成功
			String bmhtsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'部门合同删除' from FS_LOG_USEROPERATION where memo like '%合同信息删除成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(bmhtsc_insert);
			
			//审计项目纳入计划成功
			String sjnrxm_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'审计纳入项目' from FS_LOG_USEROPERATION where memo like '%项目纳入计划成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sjnrxm_insert);
			//审计信息修改成功
			String sjxxxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select distinct OPERATETIME,USERID,USERDEPTID,'审计信息维护' from FS_LOG_USEROPERATION where memo like '%审计信息修改成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sjxxxg_insert);
			//审计移除成功
			String sjycxm_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'审计移除项目' from FS_LOG_USEROPERATION where memo like '%审计移除成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(sjycxm_insert);
			//新增<工程甩项>成功
			String gcsxsz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'工程甩项新增' from FS_LOG_USEROPERATION where memo like '%新增<工程甩项>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(gcsxsz_insert);
			//更新<工程甩项>成功
			String gcsxsg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'工程甩项修改' from FS_LOG_USEROPERATION where memo like '%更新<工程甩项>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(gcsxsg_insert);
			//工程甩项删除成功
			String gcsxsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'工程甩项删除' from FS_LOG_USEROPERATION where memo like '%工程甩项删除成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(gcsxsc_insert);
			//新增<工程计量>成功
			String gcjlxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'工程计量新增' from FS_LOG_USEROPERATION where memo like '%新增<工程计量>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(gcjlxz_insert);
			//修改<工程计量>成功
			String gcjlxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'工程计量修改' from FS_LOG_USEROPERATION where memo like '%修改<工程计量>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(gcjlxg_insert);
			//工程计量删除成功
			String gcjlsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'工程计量删除' from FS_LOG_USEROPERATION where memo like '%工程计量删除成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(gcjlsc_insert);
			//新增<工程洽商>成功
			String gcqsxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'工程洽商新增' from FS_LOG_USEROPERATION where memo like '%新增<工程洽商>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(gcqsxz_insert);
			//更新<工程洽商>成功
			String gcqsxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'工程洽商修改' from FS_LOG_USEROPERATION where memo like '%更新<工程洽商>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(gcqsxg_insert);
			//工程洽商删除成功
			String gcqssc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'工程洽商删除' from FS_LOG_USEROPERATION where memo like '%工程洽商删除成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(gcqssc_insert);
			//新增<开复工令>成功
			String kfglxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'开复工令新增' from FS_LOG_USEROPERATION where memo like '%新增<开复工令>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(kfglxz_insert);
			//修改<开复工令>成功
			String kfglxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'开复工令修改' from FS_LOG_USEROPERATION where memo like '%修改<开复工令>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(kfglxg_insert);
			//开复工令删除成功
			String kfglsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'开复工令删除' from FS_LOG_USEROPERATION where memo like '%开复工令删除成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(kfglsc_insert);
			//新增<形象进度-计划编制>成功
			String xxjdxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'形象进度新增' from FS_LOG_USEROPERATION where memo like '%新增<形象进度-计划编制>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(xxjdxz_insert);
			//修改<形象进度-计划编制>成功
			String xxjdxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'形象进度修改' from FS_LOG_USEROPERATION where memo like '%修改<形象进度-计划编制>成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(xxjdxg_insert);
			//形象进度编制删除成功
			String xxjdsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'形象进度删除' from FS_LOG_USEROPERATION where memo like '%形象进度编制删除成功' "
					+ operaDate + ") ";
			stmt.executeUpdate(xxjdsc_insert);
			
			//提请款部门明细新增成功
			String bmtqkmxxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'部门提请款明细新增' from FS_LOG_USEROPERATION where memo like '%提请款部门明细新增成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(bmtqkmxxz_insert);
			//提请款部门明细修改成功
			String bmtqkmxxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'部门提请款明细修改' from FS_LOG_USEROPERATION where memo like '%提请款部门明细修改成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(bmtqkmxxg_insert);
			//提请款部门明细删除成功
			String bmtqkmxsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'部门提请款明细删除' from FS_LOG_USEROPERATION where memo like '%提请款部门明细删除成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(bmtqkmxsc_insert);
			//提请款部门新增成功
			String bmtqkxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'部门提请款新增' from FS_LOG_USEROPERATION where memo like '%提请款部门新增成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(bmtqkxz_insert);
			//提请款部门修改成功
			String bmtqkxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'部门提请款修改' from FS_LOG_USEROPERATION where memo like '%提请款部门修改成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(bmtqkxg_insert);
			//提请款部门删除成功
			String bmtqksc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'部门提请款删除' from FS_LOG_USEROPERATION where memo like '%提请款部门删除成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(bmtqksc_insert);
			//结算文件管理添加成功
			String jswjglxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'结算文件新增' from FS_LOG_USEROPERATION where memo like '%结算文件管理添加成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(jswjglxz_insert);
			//结算文件管理修改成功
			String jswjglxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'结算文件修改' from FS_LOG_USEROPERATION where memo like '%结算文件管理修改成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(jswjglxg_insert);
			//添加交竣工成功
			String jjgxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'交竣工新增' from FS_LOG_USEROPERATION where memo like '%添加交竣工成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(jjgxz_insert);
			//修改交竣工成功
			String jjgxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'交竣工修改' from FS_LOG_USEROPERATION where memo like '%修改交竣工成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(jjgxg_insert);
			//插入报告收发（接收）信息成功
			String jcjcxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'监测检测新增' from FS_LOG_USEROPERATION where memo like '%插入报告收发（接收）信息成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(jcjcxz_insert);
			//修改报告收发（接收）信息成功
			String jcjcxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'监测检测修改' from FS_LOG_USEROPERATION where memo like '%修改报告收发（接收）信息成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(jcjcxg_insert);
			//删除监测检测（接收）信息成功
			String jcjcsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'监测检测删除' from FS_LOG_USEROPERATION where memo like '%删除监测检测（接收）信息成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(jcjcsc_insert);
			
			//提报结算状态添加成功
			String tbjs_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'提报结算' from FS_LOG_USEROPERATION where memo like '%提报结算状态添加成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(tbjs_insert);
			//维护招标条件状态成功
			String whzbtj_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'招标条件维护' from FS_LOG_USEROPERATION where memo like '%维护招标条件状态成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(whzbtj_insert);
			//维护开工条件状态成功
			String whkgtj_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'开工条件维护' from FS_LOG_USEROPERATION where memo like '%维护开工条件状态成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(whkgtj_insert);
			//修改拦标价信息成功
			String lbjxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'拦标价修改' from FS_LOG_USEROPERATION where memo like '%修改拦标价信息成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(lbjxg_insert);
			//插入拦标价答疑成功
			String lbjdyxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'拦标价答疑新增' from FS_LOG_USEROPERATION where memo like '%插入拦标价答疑成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(lbjdyxz_insert);
			//修改拦标价答疑成功
			String lbjdyxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'拦标价答疑修改' from FS_LOG_USEROPERATION where memo like '%修改拦标价答疑成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(lbjdyxg_insert);
			//删除拦标价答疑成功
			String lbjdysc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'拦标价答疑删除' from FS_LOG_USEROPERATION where memo like '%删除拦标价答疑成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(lbjdysc_insert);
			//结算管理添加成功
			String jsglxz_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'结算管理新增' from FS_LOG_USEROPERATION where memo like '%结算管理添加成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(jsglxz_insert);
			//结算管理修改成功
			String jsglxg_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'结算管理修改' from FS_LOG_USEROPERATION where memo like '%结算管理修改成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(jsglxg_insert);
			//删除结算成功
			String jsglsc_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'结算管理删除' from FS_LOG_USEROPERATION where memo like '%删除结算成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(jsglsc_insert);
			//结算金额提交成功
			String jsgltj_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select OPERATETIME,USERID,USERDEPTID,'结算金额提交到合同' from FS_LOG_USEROPERATION where memo like '%结算金额提交成功' "
				+ operaDate + ") ";
			stmt.executeUpdate(jsgltj_insert);
			
			// 质量安全检查
			String zlaq_insert = "insert into tem_monitor_table(lrsj,lrr,lrbm,ywmk)(select lrsj,lrr,lrbm,(case LXBS when '1' then '质量安全信息-整改' when '2' then '质量安全信息-回复'  when '3' then '质量安全信息-复查' end) from GC_ZLAQ_ZGB where lrbm is not null and lrbm !='100000000000' and sfyx='1' "
					+ dateCondition + ") ";
			stmt.executeUpdate(zlaq_insert);
			// 查询语句
			/**
			String sql = "with "
					+ "lrzl as (select lrbm,count(lrsj) as ljzl,nvl(sum(decode(to_char(lrsj,'yyyy-mm-dd'),to_char("
					+ endCondition
					+ ",'yyyy-mm-dd'),1,0)),0) as jrzl from tem_monitor_table group by lrbm) , "
					+ "rymx as (select lrbm,wmsys.wm_concat(ljmx) as ljrymx,wmsys.wm_concat(jrmx) as jrrymx from "
					+ "(select lrbm,(select to_char(name) from fs_org_person where account = lrr)||':'||count(lrsj) as ljmx,"
					+ "(select (select to_char(name) from fs_org_person where account = lrr)||':'||count(lrsj) from tem_monitor_table tab1 where to_char(lrsj,'yyyy-mm-dd')=to_char("
					+ endCondition
					+ ",'yyyy-mm-dd') and tab1.lrr(+)=tab.lrr group by lrbm,lrr) as jrmx "
					+ "from tem_monitor_table tab group by lrbm,lrr) group by lrbm) , "
					+ "lrmx as ( select lrbm,wmsys.wm_concat(ljmx) as ljmx,jrmx as jrmx from "
					+ "(select lrbm,ywmk||':'||count(lrsj) as ljmx, "
					+ "(select wmsys.wm_concat(ywmk || ':' || count(lrsj)) jrmx from tem_monitor_table tab1 where to_char(lrsj,'yyyy-mm-dd')=to_char("
					+ endCondition
					+ ",'yyyy-mm-dd') and tab1.lrbm=tab.lrbm group by lrbm,ywmk) as jrmx "
					+ "from tem_monitor_table tab group by lrbm,ywmk) group by lrbm,jrmx) "
					+ "select dept.dept_name as bmmc,dept.row_id as bmid,nvl(ljzl,0) as ljzl,nvl(jrzl,0) as jrzl,decode(jrzl,0,'—',null,'—',jrrymx) as jrrymx,decode(ljzl,0,'—',null,'—',ljrymx) as ljrymx,decode(ljzl,0,'—',null,'—',ljmx) as ljmx,decode(jrzl,0,'—',null,'—',jrmx) as jrmx "
					+ "from fs_org_dept dept,rymx,lrmx,lrzl "
					+ "where rymx.lrbm(+)=dept.row_id and lrmx.lrbm(+)=dept.row_id and lrzl.lrbm(+)=dept.row_id "
					+ dicCondition + "order by sort asc";
					*/
			String sql = "select DEPT.BMJC BMMC,P.NAME LRR,decode(ZS.YWZS,null,0,ZS.YWZS) YWZS," +
					" decode(MX.YWMX,null,'',MX.YWMX) YWMX,nvl(DLCS,0) DLCS " +
					" ,decode(FQLCS,null,0,FQLCS) FQLCS" +
					" ,decode(JSLCS,null,0,JSLCS) JSLCS " +
					" ,decode(CLLCS,null,0,CLLCS) CLLCS " +
					" ,decode(FQWTS,null,0,FQWTS) FQWTS " +
					" ,decode(JSWTS,null,0,JSWTS) JSWTS " +
					" ,decode(CLWTS,null,0,CLWTS) CLWTS " +
					" ,decode(LLGGS,null,0,LLGGS) LLGGS " +
					" ,decode(JSGGS,null,0,JSGGS) JSGGS " +
					" from " +
					" (select LRR,wmsys.wm_concat(A.YWS) YWMX from " +
					" (select LRR,YWMK||':'||count(YWMK) YWS from tem_monitor_table group by LRR,YWMK order by YWMK) A " +
					" group by LRR  order by YWS) MX," +
					" (select LRR,count(YWMK) YWZS from tem_monitor_table group by LRR) ZS," +
					" (SELECT COUNT(A.LOGINID) dlcs, A.USERID " +
					" FROM FS_LOG_USERLOGIN A " +
					" WHERE LOGINSTATUS != '3' " + dlTimeCondition +
					"  GROUP BY USERID) D," +
					"(select count(distinct processoid) FQLCS," +
					" cjrid USERID" +
					" from ap_processinfo ap, ap_processconfig t" +
					" where ap.eventid = t.sjbh(+) " + lcTimeCondition +
					" group by cjrid) F," +
					" (select count(distinct processoid) JSLCS,USERID from (" +
					" select t.id processoid,DBRYID USERID" +
					" from ap_task_schedule t, ap_processinfo a" +
					" where RWZT = '01' " + cjTimeCondition +
					" and t.spbh = a.processoid" +
					" union all" +
					" select ap.processoid," +
					" af.actor USERID" +
					" from ap_processinfo ap," +
					" ap_processconfig t," +
					" (select processoid, closetime,actor" +
					" from ap_processdetail" +
					" where state = '11') af" +
					" where ap.eventid = t.sjbh(+)" +lcTimeCondition+
					" and (ap.processoid in (select processoid" +
					" from ap_processdetail t, AP_PROCESSTYPE P" +
					" where t.processtypeoid = p.processtypeoid" +
					" and p.processtype != 4" +
					" and t.state = '11'))" +
					" and ap.processoid = af.processoid" +
					" union all" +
					" select i.processoid," +
					" s.wcrid USERID" +
					" from ap_processinfo i," +
					" (select t.sjbh, t.wcrid, max(t.wcsj) wcsj" +
					" from ap_task_schedule t" +
					" group by t.sjbh, t.wcrid) s" +
					" where i.eventid = s.sjbh" +
					" and (i.operationoid = '2486' or i.operationoid = '2487')" +lcTimeCondition +
					" )group by USERID) G," +
					"(select count(distinct processoid) CLLCS,USERID  from (" +
					" select ap.processoid," +
					" af.actor USERID" +
					" from ap_processinfo ap," +
					" ap_processconfig t," +
					" (select processoid, closetime,actor" +
					" from ap_processdetail" +
					" where state = '11') af" +
					" where ap.eventid = t.sjbh(+)" +lcTimeCondition +
					" and (ap.processoid in (select processoid" +
					" from ap_processdetail t, AP_PROCESSTYPE P" +
					" where t.processtypeoid = p.processtypeoid" +
					" and p.processtype != 4" +
					" and t.state = '11'))" +
					" and ap.processoid = af.processoid" +
					" union all" +
					" select i.processoid," +
					" s.wcrid USERID" +
					" from ap_processinfo i," +
					" (select t.sjbh, t.wcrid, max(t.wcsj) wcsj" +
					" from ap_task_schedule t" +
					" group by t.sjbh, t.wcrid) s" +
					" where i.eventid = s.sjbh" +
					" and (i.operationoid = '2486' or i.operationoid = '2487')" +lcTimeCondition +
					" )group by USERID ) H, " +
					"(select count(distinct WTTB_INFO_ID) FQWTS ,LRR USERID from WTTB_INFO  " +
					" where SJZT in ('2','3','4','6') " +dateCondition +
					" and SFYX='1'" +
					" group by LRR) I," +
					"(select count(distinct WTTB_INFO_ID) JSWTS,B.JSR USERID from WTTB_INFO A,WTTB_LZLS B " +
					" where A.WTTB_INFO_ID=B.WTID " +
					" and A.SFYX='1'" +
					" and A.LRR!=B.JSR" +
					" and A.SJZT in ('2','3','4','6') " +
					" and B.BLRJS in ('1','2','3','4')" +lrTimeCondition +
					" group by B.JSR) J," +
					" (select count(distinct WTTB_INFO_ID) CLWTS,B.JSR USERID from WTTB_INFO A,WTTB_LZLS B" +
					" where A.WTTB_INFO_ID=B.WTID" +
					" and A.SFYX='1'" +
					" and A.SJZT in ('3','6')" +
					" and B.BLRJS in ('1','2','3','4')" +lrTimeCondition +
					" group by B.JSR) K," +
					"(SELECT SUM(DECODE(G.SFYD, '1', 1, 0)) LLGGS," +
					" COUNT(G.FBID) JSGGS," +
					" G.JSR_ACCOUNT USERID" +
					" FROM XTBG_XXZX_GGTZ_FB G" +
					" WHERE G.SFYX = '1' " +fbTimeCondition +
					" GROUP BY G.JSR_ACCOUNT) L," +
					" FS_ORG_DEPT DEPT," +
					" FS_ORG_PERSON P" +
					" where MX.LRR=ZS.LRR(+) " +
					" and MX.LRR(+)=P.ACCOUNT" +
					" and P.ACCOUNT(+)=D.USERID "+
					" and P.DEPARTMENT=DEPT.ROW_ID(+) " +
					" and D.USERID=F.USERID(+) " +
					" and D.USERID=G.USERID(+)" +
					" and D.USERID=H.USERID(+)" +
					" and D.USERID=I.USERID(+)" +
					" and D.USERID=J.USERID(+)" +
					" and D.USERID=K.USERID(+)" +
					" and D.USERID=L.USERID(+)" +
					" and P.ACCOUNT!='superman'" +
					" and P.ACCOUNT!='demo' " +dicCondition+
					" order by DEPT.SORT asc,to_number(P.SORT) asc,P.ACCOUNT asc";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

}
