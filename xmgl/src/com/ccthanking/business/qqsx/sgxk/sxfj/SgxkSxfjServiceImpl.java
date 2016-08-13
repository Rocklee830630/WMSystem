package com.ccthanking.business.qqsx.sgxk.sxfj;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.clzx.service.ClzxManagerService;
import com.ccthanking.business.clzx.service.impl.ClzxManagerServiceImpl;
import com.ccthanking.business.qqsx.qqsxgl.GcZgbQqsxVO;
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
public  class SgxkSxfjServiceImpl implements SgxkSxfjService {
	private String ywlx=YwlxManager.GC_QQSX_SGXK;

	@Override
	public String queryXmxx(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition += " and qqsx.SJWYBH(+)=jhsj.SJWYBH and sx.SJWYBH(+) = jhsj.SJWYBH";
		condition +=  BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql="select distinct JHSJ.XMBS,jhsj.pxh,jhsj.lrsj,jhsj.xmbh,jhsj.bdbh,jhsj.xmmc,jhsj.bdmc,qqsx.gc_zgb_qqsx_id as ywbid,qqsx.xdkid,jhsj.xmid,jhsj.bdid,jhsj.gc_jh_sj_id as jhsjid,jhsj.nd,qqsx.SGXKBJSJ as bjsj, qqsx.SGXKBBLSX as bblsx," +
					"sx.BJFK, sx.QTSXFK, sx.ZLJDFK, sx.AQJDFK,sx.ZJGLFK,sx.STQFK, sx.ZFJCFK,sx.SGXKFK ,sx.CZWT, " +
					"sx.BJ , sx.QTSX , sx.ZLJD , sx.AQJD ,jhsj.SJWYBH, sx.ZJGL , sx.STQ , sx.ZFJC , sx.SGXK,(case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ " +
					"from (select SJWYBH, " +
					"max(decode(fjlx, '0015', jhsjid, '')) BJ," +
					"max(decode(fjlx, '0015', blsj, '')) BJFK, " +
					"max(decode(fjlx, '0016', jhsjid, '')) QTSX, " +
					"max(decode(fjlx, '0016', blsj, '')) QTSXFK, " +
					"max(decode(fjlx, '0017', jhsjid, '')) ZLJD, " +
					"max(decode(fjlx, '0017', blsj, '')) ZLJDFK, " +
					"max(decode(fjlx, '0018', jhsjid, '')) AQJD," +
					"max(decode(fjlx, '0018', blsj, '')) AQJDFK, " +
					"max(decode(fjlx, '0019', jhsjid, '')) ZJGL, " +
					"max(decode(fjlx, '0019', blsj, '')) ZJGLFK, " +
					"max(decode(fjlx, '0020', jhsjid, '')) STQ, " +
					"max(decode(fjlx, '0020', blsj, '')) STQFK, " +
					"max(decode(fjlx, '0021', jhsjid, '')) ZFJC," +
					"max(decode(fjlx, '0021', blsj, '')) ZFJCFK, " +
					"max(decode(fjlx, '0022', jhsjid, '')) SGXK," +
					"max(decode(fjlx, '0022', blsj, '')) SGXKFK, " +
					
					"(select czwt from " +
					"(select czwt,SJWYBH, " +
					"row_number() over(partition by SJWYBH order by blsj desc) as line " +
					"from GC_QQSX_SXFJ  where dfl = '0' and sfyx = '1') sx2 " +
					
					"where line = '3' and sx2.SJWYBH(+)=sx1.SJWYBH) as czwt from GC_QQSX_SXFJ sx1" +
					" where sfyx='1' group by sjwybh) sx,  (select * from GC_JH_SJ where sfyx='1')  jhsj,(select * from Gc_Zgb_Qqsx where sfyx='1') qqsx " ;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("BBLSX", "SGXKSX");
			bs.setFieldFileUpload("BJ", "0015");
			bs.setFieldFileUpload("QTSX", "0016");
			bs.setFieldFileUpload("ZLJD", "0017");
			bs.setFieldFileUpload("AQJD", "0018");
			bs.setFieldFileUpload("ZJGL", "0019");
			bs.setFieldFileUpload("STQ", "0020");
			bs.setFieldFileUpload("ZFJC", "0021");
			bs.setFieldFileUpload("SGXK", "0022");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询施工许可数据", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	@Override
	public String querySxfj(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition =RequestUtil.getConditionList(json).getConditionWhere();
		condition += " and DFL='3'";
		condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		
		page.setFilter(condition);
			conn.setAutoCommit(false);
			//String sql="select  gc_qqsx_sxfj_id, sxfj.fjid, sxfj.fjlx, sxfj.dfl, sxfj.ywbid, sxfj.whmc, sxfj.blr, sxfj.tzbh, sxfj.blsj, sxfj.bz, sxfj.lrr, sxfj.lrsj, sxfj.czwt, sxfj.jhsjid, sxfj.xmid, sxfj.sjbh, sxfj.ywlx,jhsj.bdid from GC_QQSX_SXFJ sxfj,gc_jh_sj jhsj";
			String sql="select  gc_qqsx_sxfj_id, fjid, fjlx, dfl,ywbid, SJWYBH,whmc, blr, tzbh, blsj, bz, lrr, lrsj, czwt, jhsjid, xmid, sjbh, ywlx from GC_QQSX_SXFJ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("FJLX", "SGXKSX");
			bs.setFieldTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询施工手续办理信息", user,"","");
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertSxfj(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		String ywid=request.getParameter("ywid");
		String bdid=request.getParameter("bdid");
		SgxkSxfjVO xmvo = new SgxkSxfjVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		if(Pub.empty(ywid))
		{
			ywid=new RandomGUID().toString();
		}
		EventVO eventVO = EventManager.createEvent(conn, ywlx, user);//生成事件
		//插入
		BusinessUtil.setInsertCommonFields(xmvo, user);
		xmvo.setGc_qqsx_sxfj_id(ywid); // 主键
		xmvo.setSjbh(eventVO.getSjbh());
		xmvo.setYwlx(ywlx);
		xmvo.setDfl("3");
		BaseDAO.insert(conn, xmvo);
		resultVO = xmvo.getRowJson();

		//附件信息
		FileUploadVO vo = new FileUploadVO();
		vo.setYwid(xmvo.getJhsjid());
		vo.setGlid1(ywid);
		vo.setGlid2(xmvo.getJhsjid());
		vo.setGlid3(xmvo.getXmid());
		vo.setGlid4(bdid);
		vo.setSjbh(eventVO.getSjbh());
		vo.setYwlx(ywlx);
		vo.setFjzt("1");
		vo.setFjlb(xmvo.getFjlx());
		FileUploadService.updateVOByYwid(conn,vo,xmvo.getGc_qqsx_sxfj_id(),user);
				
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "添加施工许可办理手续的附件内容成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "添加施工许可办理手续的附件内容失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String updateSxfj(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String bdid=request.getParameter("bdid");
		String resultVO = null;
		SgxkSxfjVO vo = new SgxkSxfjVO();
		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		BusinessUtil.setUpdateCommonFields(vo, user);
		//附件操作
		FileUploadVO fvo = new FileUploadVO();
		fvo.setFjzt("1");
		fvo.setYwid(vo.getJhsjid());
		fvo.setGlid1(vo.getGc_qqsx_sxfj_id());
		fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
		fvo.setGlid3(vo.getXmid()); //存入项目ID
		fvo.setGlid4(bdid);			//存入标段ID
		fvo.setFjlb(vo.getFjlx());
		FileUploadService.updateVOByGlid1(conn, fvo, vo.getGc_qqsx_sxfj_id(),user);
		
		//修改操作
		BaseDAO.update(conn, vo);
		resultVO = vo.getRowJson();

		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), ywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "修改施工许可办理手续的附件内容成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改施工许可办理手续的附件内容失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	
	public String feedbackQqsx(HttpServletRequest request, String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String qqsxid="";
		String sjwybh=request.getParameter("sjwybh");
		String jhsjid=request.getParameter("jhsjid");
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZgbQqsxVO vo = new GcZgbQqsxVO();
		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));

		String jhsjsql = "select GC_ZGB_QQSX_ID,SJBH from GC_ZGB_QQSX where sjwybh='"+sjwybh+"' and sfyx='1'";
		String[][] arr=DBUtil.query(conn, jhsjsql);
		if(arr==null||arr.length==0){
			qqsxid="";
		}else{
			qqsxid = arr[0][0];
		}
		
		if(Pub.empty(qqsxid)){
			BusinessUtil.setInsertCommonFields(vo, user);
			vo.setGc_zgb_qqsx_id(new RandomGUID().toString()); // 主键
			vo.setSgsfbl("1");
			vo.setJhsjid(jhsjid);
			vo.setYwlx(YwlxManager.GC_QQSX);
			vo.setSjwybh(sjwybh);
			EventVO eventVO1 = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
			vo.setSjbh(eventVO1.getSjbh());
			//插入
			BaseDAO.insert(conn, vo);
		}else{
			BusinessUtil.setUpdateCommonFields(vo, user);
			vo.setGc_zgb_qqsx_id(qqsxid);
			vo.setSgsfbl("1");
			vo.setJhsjid(jhsjid);
			vo.setSjbh(arr[0][1]);
			vo.setYwlx(YwlxManager.GC_QQSX);
	
			//插入
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();
		}
		//-------------加入处理中心任务生成代码-------------BEGIN---------
		ClzxManagerService cms = new ClzxManagerServiceImpl();
		cms.createTask("1003007", jhsjid, user,conn);
		//-------------加入处理中心任务生成代码-------------END---------
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "施工许可手续部门反馈成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "施工许可手续部门反馈失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	@Override
	public String deleteSxfj(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String fjlb=request.getParameter("fjlb");
		String resultVO = null;
		SgxkSxfjVO vo = new SgxkSxfjVO();
		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		//修改
		BusinessUtil.setUpdateCommonFields(vo, user);
		vo.setSfyx("0");

		BaseDAO.update(conn, vo);
		resultVO = vo.getRowJson();

		//删除手续，附件删除
		FileUploadVO condVO=new FileUploadVO();
		String del_id=vo.getGc_qqsx_sxfj_id();
		condVO.setGlid1(del_id);
		condVO.setFjlb(fjlb);
		FileUploadService.deleteByConditionVO(conn,condVO);
		
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), ywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "删除施工许可手续内容成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除施工许可手续内容失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	//计算个数
	@Override
	public String getCounts(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String fjlx=request.getParameter("fjlx");
		String sjwybh=request.getParameter("sjwybh");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql = "select distinct count(distinct(FJLX)) from GC_QQSX_SXFJ where FJLX in("+fjlx+") and sjwybh='"+sjwybh+"'and sfyx='1'";
			String[][] resArr = DBUtil.query(conn, sql);
			if(resArr!=null){
				domresult = resArr[0][0];
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//计算个数
		@Override
		public String getDate(HttpServletRequest request, String json)
				throws Exception {
			Connection conn = null;
			String domresult = "";
			String sjwybh=request.getParameter("sjwybh");
			try {
				conn = DBUtil.getConnection();
				conn.setAutoCommit(false);
				String sql = "select to_char(BLSJ,'YYYY-MM-DD') from (select BLSJ from GC_QQSX_SXFJ where sjwybh='"+sjwybh+"' and sfyx='1' and dfl='3' order by blsj desc) where rownum='1'";
				String[][] resArr = DBUtil.query(conn, sql);
				if(resArr!=null){
					domresult = resArr[0][0];
				}
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace(System.out);
				DBUtil.rollbackConnetion(conn);
				throw e;
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
//作废
		@Override
		public String querySxfjzj(HttpServletRequest request, String json,String jhsjid) {
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			Connection conn = DBUtil.getConnection();
			String domresult = "";
			try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			condition += " and DFL='3'  and b.jhsjid=c.gc_jh_sj_id  and b.jhsjid ='"+jhsjid+"'";
			condition += BusinessUtil.getSJYXCondition("C") + BusinessUtil.getCommonCondition(user,null);
			condition += orderFilter;
			
			page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql="select  b.gc_qqsx_sxfj_id,b.jhsjid SXFJ, b.fjid,b.fjlx, b.dfl, b.ywbid, b.whmc,b.blr, b.tzbh, b.blsj, b.bz, b.lrr, b.lrsj," +
						" b.czwt,b.jhsjid,c.xmid, c.bdmc,C.XMMC  from  GC_QQSX_SXFJ b,gc_jh_sj  c ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("FJLX", "SGXKSX");
				bs.setFieldTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldFileUpload("SXFJ","0015,0016,0017,0018,0019,0020,0021,0022");
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询施工许可手续办理附件", user,"","");
				
			} catch (Exception e) {
				e.printStackTrace(System.out);
			
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
		}
}
