package com.ccthanking.business.zsb.zsbmjk;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
public class ZengChaiBuZhangServiceImpl implements ZengChaiBuZhangService {

	// private String ywlx=YwlxManager.GC_ZJB_JS;

	@Override
	public String queryTongJiGaiKuang(User user, String nd) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sqlnd = "";
			if (!Pub.empty(nd)) {
				sqlnd = "and jhsj.nd='" + nd + "'";
			}
			String sql = "with zcxx as " +
					"( select jhsj.gc_jh_sj_id,xxb.jhsjid as xxb_jhsjid,xmb.jhsjid as xmb_jhsjid," +
					"sum(decode(xxb.mdwcrq,null,1,0)) as mdwcrq_mark,max(xxb.mdwcrq) as mdwcrq,min(jhsj.zc_sj) as zc_sj,min(xmb.cdsjsj) as cdyjrq," +
					"sum(decode(xxb.tdfwyjrq,null,1,0)) as tdfwyjrq, sum(decode(xxb.qwtrq,null,1,0)) as qwtrq,sum(xxb.xyje) as xyje," +
					"sum(xxb.zjdwje) as zjdwje,sum(xxb.jmhs) as jmhs,sum(xxb.qyjs) as qyjs, " +
					"sum(xxb.jttdmj) as jttdmj,sum(xxb.gytdmj) as gytdmj,sum(xxb.zmj) as zmj," +
					"sum(xxb.mdgs) as mdgs,max(jdb.sbrq) as sbrq from " +
					"(select * from gc_jh_sj where sfyx='1' and iszc='1') jhsj," +
					"(select * from gc_zsb_xxb where sfyx='1' ) xxb, " +
					"(select * from gc_zsb_jdb where sfyx='1') jdb, " +
					"(select * from gc_zsb_xmb where sfyx='1') xmb " +
					"where xmb.jhsjid(+)=jhsj.gc_jh_sj_id and xxb.jhsjid(+)=jhsj.gc_jh_sj_id and jdb.zdxxid(+)=xxb.gc_zsb_xxb_id and jhsj.bdid is null "+sqlnd+" group by gc_jh_sj_id,xxb.jhsjid,xmb.jhsjid ) " +
							"select count(gc_jh_sj_id) as sjcqxms, " +
							"sum(decode(mdwcrq_mark,0,1,0)) as mdwcxms, " +
							"count(decode(zc_sj,null,null,gc_jh_sj_id)) as wccqxms, " +
							"count(decode(cdyjrq,null,null,xmb_jhsjid)) as cdyjxms, " +
							"sum(decode(tdfwyjrq,0,1,0)) as tdfwyjxms, " +
							"sum(decode(qwtrq,0,1,0)) as wtxyqdwcxms, " +
							"nvl(sum(xyje), 0) as xyjezj, nvl(sum(zjdwje), 0) as zjdwje, " +
							"max(mdwcrq) as zxmdwcsj, nvl(sum(jmhs),0) as jmhszj, " +
							"nvl(sum(qyjs),0) as qyjzj, nvl(sum(jttdmj),0) as jttdzdmjzj, " +
							"nvl(sum(gytdmj),0) as gytdzdmjzj, nvl(sum(zmj),0) as tdzmjzj," +
							"nvl(sum(mdgs),0) as mdgshj, max(sbrq) as zxtbsj from zcxx";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("XYJEZJ");
			bs.setFieldThousand("ZJDWJE");
			bs.setFieldThousand("MDGSHJ");
			bs.setFieldThousand("JMHSZJ");
			bs.setFieldThousand("QYJZJ"); //
			bs.setFieldThousand("JTTDZDMJZJ"); // 集体涂点征地面积总计
			bs.setFieldThousand("GYTDZDMJZJ"); // 国有土地征地面积总计
			bs.setFieldThousand("TDZMJZJ"); // 土地总面积总计
			bs.setFieldThousand("MDGSHJ"); // 摸底估算合计
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);

		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String juMingChaiQianJinZhan(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			String sqlnd = "";
			if (!Pub.empty(nd)) {
				sqlnd = " and jhsj.nd='" + nd + "'";
			}
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "with jmxx as "
					+ "( select sum(xxb.jmhs) as jmhs,sum(jdb.bcwcjms) as bcwcjms "
					+ "from (select * from gc_jh_sj where sfyx='1' and iszc='1') jhsj, "
					+ "(select * from gc_zsb_xxb where sfyx='1') xxb, "
					+ "(select * from gc_zsb_jdb where sfyx='1') jdb "
					+ "where xxb.jhsjid(+)=jhsj.gc_jh_sj_id and jdb.zdxxid(+)=xxb.gc_zsb_xxb_id "
					+ sqlnd
					+ ") "
					+ "select '剩余居民户数' as label,nvl((sum(jmhs)-sum(bcwcjms)),0) as value from jmxx "
					+ "union all select '已完成居民户数' as label,nvl(sum(bcwcjms),0) as value from jmxx ";
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
	public String moDiXiangQing(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String json = "{querycondition: {conditions: [{} ]}}";
		try {
			String sqlnd = "";
			if (!Pub.empty(nd)) {
				sqlnd = " and jhsj.nd='" + nd + "'";
			}
			PageManager page = new PageManager();
			// String orderFilter = RequestUtil.getOrderFilter(json);
			// String condition =
			// RequestUtil.getConditionList(json).getConditionWhere();
			conn.setAutoCommit(false);
			String sql = "select  (select max(xxb.mdwcrq) from gc_zsb_xxb xxb  ,gc_jh_sj jhsj where xxb.jhsjid=jhsj.gc_jh_sj_id and xxb.sfyx = '1' "
					+ sqlnd
					+ ") as zxmdwcsj,"
					+ " (select sum(xxb.jmhs)  "
					+ " from gc_zsb_xxb xxb  ,gc_jh_sj jhsj where xxb.jhsjid=jhsj.gc_jh_sj_id and xxb.sfyx = '1' "
					+ sqlnd
					+ ") as jmhszj,"
					+ " (select sum(xxb.qyjs) from gc_zsb_xxb xxb  ,gc_jh_sj jhsj where xxb.jhsjid=jhsj.gc_jh_sj_id  and xxb.sfyx = '1' "
					+ sqlnd
					+ ") as qyjzj, "
					+ "   (select sum(xxb.jttdmj) from gc_zsb_xxb xxb  ,gc_jh_sj jhsj where xxb.jhsjid=jhsj.gc_jh_sj_id and xxb.sfyx = '1' "
					+ sqlnd
					+ ") as jttdzdmjzj, "
					+ "  (select sum(xxb.gytdmj) from gc_zsb_xxb xxb  ,gc_jh_sj jhsj where xxb.jhsjid=jhsj.gc_jh_sj_id and xxb.sfyx = '1' "
					+ sqlnd
					+ ") as gytdzdmjzj, "
					+ "  (select sum(xxb.zmj) from gc_zsb_xxb xxb  ,gc_jh_sj jhsj where xxb.jhsjid=jhsj.gc_jh_sj_id and xxb.sfyx = '1' "
					+ sqlnd
					+ ") as tdmjzj, "
					+ "  (select sum(xxb.mdgs) from gc_zsb_xxb xxb  ,gc_jh_sj jhsj where xxb.jhsjid=jhsj.gc_jh_sj_id  and xxb.sfyx = '1' "
					+ sqlnd
					+ ") as mdgshj, "
					+ "  (select max(jdb.sbrq) from gc_zsb_jdb jdb  ,gc_jh_sj jhsj where xxb.jhsjid=jhsj.gc_jh_sj_id and jdb.sfyx = '1' "
					+ sqlnd + ") as zxtbsj from dual ";
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
	public String qiYeChaiQianJinZhan(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			String sqlnd = "";
			if (!Pub.empty(nd)) {
				sqlnd = "and jhsj.nd='" + nd + "'";
			}
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "with qyxx as "
					+ "(select sum(xxb.qyjs) as qyjs, sum(jdb.bcwcqys) as bcwcqys "
					+ "from (select * from gc_jh_sj where sfyx = '1' and iszc='1') jhsj, "
					+ "(select * from gc_zsb_xxb where sfyx = '1') xxb, "
					+ "(select * from gc_zsb_jdb where sfyx = '1') jdb "
					+ "where xxb.jhsjid(+) = jhsj.gc_jh_sj_id and jdb.zdxxid(+) = xxb.gc_zsb_xxb_id "
					+ sqlnd
					+ ") "
					+ "select '剩余企业家数' as label, nvl((sum(qyjs) - sum(bcwcqys)), 0) as value from qyxx "
					+ "union all select '已完成企业家数' as label, nvl(sum(bcwcqys), 0) as value from qyxx";
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
	public String zhengDiMianJiJinZhan(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			String sqlnd = "";
			if (!Pub.empty(nd)) {
				sqlnd = "and jhsj.nd='" + nd + "'";
			}
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sql = "with zdxx as "
					+ "(select sum(xxb.zmj) as zmj, sum(jdb.bczdmj) as bczdmj "
					+ "from (select * from gc_jh_sj where sfyx = '1' and iszc='1') jhsj, "
					+ "(select * from gc_zsb_xxb where sfyx = '1') xxb, "
					+ "(select * from gc_zsb_jdb where sfyx = '1') jdb "
					+ "where xxb.jhsjid(+) = jhsj.gc_jh_sj_id and jdb.zdxxid(+) = xxb.gc_zsb_xxb_id "
					+ sqlnd
					+ ") "
					+ "select '剩余土地面积' as label, nvl((sum(zmj) - sum(bczdmj)), 0) as value from zdxx "
					+ "union all select '已完成土地面积' as label, nvl(sum(bczdmj), 0) as value from zdxx";
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
	public String zhengChaiJinZhan(User user, String nd) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

			String sqlnd = "";
			if (!Pub.empty(nd)) {
				sqlnd = "and jhsj.nd='" + nd + "'";
			}
			PageManager page = new PageManager();
			page.setPageRows(100);
			conn.setAutoCommit(false);
			String sql = "with zcxx as "
					+ "(select xxb.qy,count(distinct xxb.jhsjid) as sjcqxms, "
					+ "count(distinct decode(xxb.mdwcrq,null,xxb.jhsjid,null)) as no_md, "
					+ "count(distinct decode(jhsj.zc_sj,null,jhsj.gc_jh_sj_id,null)) as no_wccq, "
					+ "count(distinct decode(xxb.cdyjrq,null,xxb.jhsjid,null)) as no_cdyj, "
					+ "count(distinct decode(xxb.tdfwyjrq,null,xxb.jhsjid,null)) as no_tdfwyj, "
					+ "count(distinct decode(xxb.qwtrq,null,xxb.jhsjid,null)) as no_wtxyqdwc, "
					+ "sum(xxb.xyje) as xyje, sum(xxb.zjdwje) as zjdwje "
					+ "from (select * from gc_jh_sj where sfyx='1' and iszc='1' and bdid is null) jhsj, "
					+ "(select * from gc_zsb_xxb where sfyx='1') xxb, "
					+ "(select * from fs_dic_tree where dic_name_code = 'QY' and parent_id != '0' and sfyx='1') dic "
					+ "where xxb.jhsjid(+)=jhsj.gc_jh_sj_id and xxb.qy is not null "
					+ sqlnd
					+ " group by xxb.qy) "
					+ "select dic.dic_value,nvl(zcxx.sjcqxms,0) as sjcqxms,"
					+ "nvl(zcxx.sjcqxms-zcxx.no_md,0) as mdwcxms, "
					+ "nvl(zcxx.sjcqxms-zcxx.no_wccq,0) as wccqxms, "
					+ "nvl(zcxx.sjcqxms-zcxx.no_cdyj,0) as cdyjxms,"
					+ "nvl(zcxx.sjcqxms-zcxx.no_tdfwyj,0) as tdfwyjxms,"
					+ "nvl(zcxx.sjcqxms-zcxx.no_wtxyqdwc,0) as wtxyqdwcxms,"
					+ "nvl(zcxx.xyje,0) as xyje,nvl(zcxx.zjdwje,0) as zjdwje from "
					+ "(select * from fs_dic_tree where sfyx='1' and dic_name_code = 'QY' and parent_id != '0' and sfyx='1') dic ,zcxx where zcxx.qy(+)=dic.dic_code order by dic.pxh asc";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("XYJE");
			bs.setFieldThousand("ZJDWJE");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);

		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

}
