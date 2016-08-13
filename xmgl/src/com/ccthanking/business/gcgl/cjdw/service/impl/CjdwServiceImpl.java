package com.ccthanking.business.gcgl.cjdw.service.impl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.gcgl.cjdw.service.CjdwService;
import com.ccthanking.business.gcgl.cjdw.vo.CjdwVO;
import com.ccthanking.business.zjb.jsgl.GcZjbJsbVO;
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
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class CjdwServiceImpl implements CjdwService {
	
	private String ywlx = YwlxManager.GC_CJDW;

	@Override
	public String query(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		PageManager page = RequestUtil.getPageManager(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		String orderFilter = RequestUtil.getOrderFilter(json);
        condition += BusinessUtil.getSJYXCondition(null);
        condition += BusinessUtil.getCommonCondition(user,null);
        if(!Pub.empty(condition)){
			condition +=" AND gc_cjdw_id=b.cjdw(+) ";
			
		}else{
			condition="   gc_cjdw_id=b.cjdw(+) ";
		}
        condition += orderFilter;
      
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"gc_cjdw_id, dwbh, dwmc, dwlx, clsj, dz, dh, qyzz, qyxz, fzr, fzrdh, ywfw, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx,zbcs,zs,cjdw " +
					"FROM " +
					"GC_CJDW,( select count(*) zs ,dwid cjdw from (" +
					" select  dwid,xmid from ( " +
					" select * from ( select (case xmbs" +
					"    when '0' then " +
					"    (select sgdw " +
					"             from GC_TCJH_XMXDK " +
					"             where gc_tcjh_xmxdk_id = t.xmid " +
					"                AND SFYX = '1') " +
					"            when '1' then " +
					"             (select sgdw " +
					"                from GC_XMBD " +
					"                where GC_XMBD_ID = t.bdid " +
					"                   AND SFYX = '1') " +
					"            end) dwid ,t.* " +
					"       from GC_JH_SJ t, GC_TCJH_XMXDK T1 " +
					"      where T.XMID = T1.GC_TCJH_XMXDK_ID " +
					"       and t.sfyx = '1' " +
					"       and t1.sfyx = '1') where dwid is not null) group by  xmid,dwid) group by dwid) b";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			//字典
			bs.setFieldDic("DWLX", "JGLB");
			bs.setFieldDic("QYXZ", "QYXZ");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<参建单位>", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	
	@Override
	public String insert(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		EventVO eventVO = null;
		CjdwVO vo = new CjdwVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			if(Pub.empty(vo.getGc_cjdw_id())){
				vo.setGc_cjdw_id(new RandomGUID().toString()); 
				vo.setYwlx(ywlx);
				vo.setDwmc(vo.getDwmc().replaceAll(" ",""));
			    eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			    vo.setSjbh(eventVO.getSjbh());
			    BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), ywlx,
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "新增<参建单位>成功", user,"","");
			}else{
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
				LogManager.writeUserLog(vo.getSjbh(), ywlx,
						Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "修改<参建单位>成功", user,"","");
			}
		
			resultVO = vo.getRowJson();
			conn.commit();
			
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String queryByMingChen(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String dwmc=request.getParameter("dwmc");
		String dwlx=request.getParameter("dwlx");
		String id=request.getParameter("id");
		dwmc=dwmc.replaceAll(" ", "");
		String dwmcsql=" ";
		if(!Pub.empty(dwmc))
		{
			dwmcsql=" and  dwmc='"+dwmc+"' and dwlx="+dwlx+" ";
		}
		if(!Pub.empty(id))
		{
			dwmcsql=dwmcsql+"and gc_cjdw_id not in('"+id+"')";
		}
		try {
			
			PageManager page = new PageManager();
			conn.setAutoCommit(false);
			String sql = " SELECT count(*) zs " +
					"FROM " +
					"GC_CJDW where 1=1 "+dwmcsql+ " and sfyx='1' ";
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
	public String queryZtbList(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		PageManager page = RequestUtil.getPageManager(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		String orderFilter = RequestUtil.getOrderFilter(json);
        condition += BusinessUtil.getSJYXCondition("Z");
        condition += BusinessUtil.getCommonCondition(user,"Z");
        condition +=" AND Z.GC_ZTB_SJ_ID=H.ZTBID(+) ";
        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT " +
					"ZTBMC,ZBBH,ZZBJ,ZBTZSBH,KBRQ,HTMC,HTBM,ZHTQDJ " +
					"FROM " +
					"GC_ZTB_SJ Z,(select * from GC_HTGL_HT where SFYX='1') H ";
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldThousand("ZZBJ");
			bs.setFieldThousand("ZHTQDJ");
			//字典
			//bs.setFieldDic("DWLX", "JGLB");
			//bs.setFieldDic("QYXZ", "QYXZ");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<中标信息>", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}



	@Override
	public String queryXMXXList(String json, HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		String cjdwid=request.getParameter("cjdwid");
		String domresult = "";
		try {

		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition += " and t.xmid = t1.gc_tcjh_xmxdk_id and t.sfyx = '1')T where t.sgdwid='"+cjdwid+"' or t.jldwid='"+cjdwid+"'";
       /* condition += BusinessUtil.getSJYXCondition("t");*/
        condition += BusinessUtil.getCommonCondition(user,"t");
        condition += orderFilter;
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			
			conn.setAutoCommit(false);
			String sql = "";
			sql = "select * from " +
					"(SELECT (case xmbs  when '0' then  (select sgdw  from GC_TCJH_XMXDK  where gc_tcjh_xmxdk_id = t.xmid   AND SFYX = '1')" +
					"  when '1' then (select sgdw from GC_XMBD where GC_XMBD_ID = t.bdid AND SFYX = '1') end) SGDWID," +
					" (case xmbs  when '0' then  (select jldw   from GC_TCJH_XMXDK  where gc_tcjh_xmxdk_id = t.xmid   AND SFYX = '1') " +
					" when '1' then (select jldw  from GC_XMBD   where GC_XMBD_ID = t.bdid   AND SFYX = '1')  end) JLDWID,"
					+" decode(  t.xmbs,'1',(select BDDD from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1),(select XMDZ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1)) as XMDZ," +
					"  decode (t.xmbs,'1',(select JSGM from GC_XMBD where GC_XMBD_ID = t.bdid and rownum = 1),(select JSNR from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum = 1)) as JSNR," +
					"  (select JSMB from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum=1) as JSMB," +
					"  (select XJXJ from GC_TCJH_XMXDK where nd = t.nd and GC_TCJH_XMXDK_ID = t.XMID and rownum=1) as XMXZ, " +
					" t1.wdd,"+
					"t.gc_jh_sj_id, t.jhid, t.nd, t.xmid, t.bdid, t.xmbh, t.bdbh,t.xmmc,t.xmglgs," +
					" t.bdmc, t.pxh, t.kgsj, t.kgsj_sj, t.kgsj_bz, t.wgsj, t.wgsj_sj, t.wgsj_bz, t.kypf, t.kypf_sj, t.kypf_bz, t.hpjds, t.hpjds_sj, t.hpjds_bz, t.gcxkz, t.gcxkz_sj, t.gcxkz_bz, t.sgxk, t.sgxk_sj, t.sgxk_bz, t.cbsjpf, t.cbsjpf_sj, t.cbsjpf_bz, t.cqt, t.cqt_sj, t.cqt_bz, t.pqt, t.pqt_sj, t.pqt_bz, t.sgt, t.sgt_sj, t.sgt_bz, t.tbj, t.tbj_sj, t.tbj_bz, t.cs, t.cs_sj, t.cs_bz, t.jldw, t.jldw_sj, t.jldw_bz, t.sgdw, t.sgdw_sj, t.sgdw_bz, t.zc, t.zc_sj, t.zc_bz, t.pq, t.pq_sj, t.pq_bz, t.jg, t.jg_sj, t.jg_bz, t.xmsx, t.ywlx, t.sjbh, t.bz, t.lrr, t.lrsj, t.lrbm, t.lrbmmc, t.gxr, t.gxsj, t.gxbm, t.gxbmmc, t.sjmj, t.sfyx, t.xflx, t.isxf, t.iskypf, t.ishpjds, t.isgcxkz, t.issgxk, t.iscbsjpf, t.iscqt, t.ispqt, t.issgt, t.istbj, t.iscs, t.isjldw, t.issgdw, t.iszc, t.ispq, t.isjg, t.xmbs,t.iskgsj,t.iswgsj"+
					" FROM " +
					" GC_JH_SJ t,GC_TCJH_XMXDK t1 " ;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldDic("ISKGSJ", "SF");
			bs.setFieldDic("ISWGSJ", "SF");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询<统筹计划数据>成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}



	@Override
	public String deleteCJDW(String json, User user) {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		CjdwVO vo = new CjdwVO();
		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			vo.setYwlx(ywlx);
			//置成失效
	        vo.setSfyx("0");
			//插入
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除参见单位成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return resultVO;
	}
	
}
