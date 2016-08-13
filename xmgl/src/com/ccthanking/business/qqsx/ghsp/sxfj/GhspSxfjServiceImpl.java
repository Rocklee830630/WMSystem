package com.ccthanking.business.qqsx.ghsp.sxfj;

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
public  class GhspSxfjServiceImpl implements GhspSxfjService {
	private String ywlx=YwlxManager.GC_QQSX_GHSP;

	@Override
	public String queryXmxx(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition += " and qqsx.sjwybh(+)=jhsj.sjwybh and sx.sjwybh(+) = jhsj.sjwybh and jhsj.bdid is null";
		condition +=  BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql="select distinct jhsj.pxh,jhsj.xmbh,jhsj.lrsj,jhsj.xmmc,qqsx.gc_zgb_qqsx_id as ywbid,qqsx.xdkid,jhsj.xmid,jhsj.gc_jh_sj_id as jhsjid,jhsj.nd,qqsx.ghspbjsj as bjsj, qqsx.ghspbblsx as bblsx," +
						"sx.XZYJS,sx.JSXZXMYJS,sx.JSYDGHXKZ,jhsj.SJWYBH,sx.YDXKZ,sx.JSGCGHXKZ,sx.GCXKZ," +
						"sx.CZWT,(case jhsj.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = jhsj.nd and GC_TCJH_XMXDK_ID = jhsj.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = jhsj.bdid and rownum = 1) end ) as XMBDDZ " +
						"from (select sjwybh, max(decode(fjlx, '0007', jhsjid, '')) JSXZXMYJS," +
								"max(decode(fjlx, '0007', blsj, '')) XZYJS, " +
								"max(decode(fjlx, '0008', jhsjid, '')) JSYDGHXKZ, " +
								"max(decode(fjlx, '0008', blsj, '')) YDXKZ, " +
								"max(decode(fjlx, '0009', jhsjid, '')) JSGCGHXKZ, " +
								"max(decode(fjlx, '0009', blsj, '')) GCXKZ, " +
								"(select czwt from " +
									"(select czwt,sjwybh, " +
									"row_number() over(partition by SJWYBH order by blsj desc) as line " +
									"from GC_QQSX_SXFJ  where dfl = '1' and sfyx = '1') sx2 " +
									"where line = '1' and sx2.sjwybh(+)=sx1.sjwybh) as czwt from GC_QQSX_SXFJ sx1" +
								" where sfyx='1' group by sjwybh) sx, (select * from GC_JH_SJ where sfyx='1')  jhsj,(select * from Gc_Zgb_Qqsx where sfyx='1') qqsx " ;
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("BBLSX", "GHSX");
			bs.setFieldFileUpload("JSXZXMYJS", "0007");
			bs.setFieldFileUpload("JSYDGHXKZ", "0008");
			bs.setFieldFileUpload("JSGCGHXKZ", "0009");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询规划审批数据", user,"","");
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
		condition += " and DFL='1'";
		condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql="select  gc_qqsx_sxfj_id, fjid, fjlx, dfl, ywbid,SJWYBH, whmc, blr, tzbh, blsj, bz, lrr, lrsj, czwt, jhsjid, xmid,sjbh,ywlx from GC_QQSX_SXFJ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("FJLX", "GHSX");
			bs.setFieldTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询规划审批手续办理附件", user,"","");
			
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
		GhspSxfjVO xmvo = new GhspSxfjVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		if(Pub.empty(ywid))
		{
			ywid=new RandomGUID().toString();
		}
		//生成事件
		EventVO eventVO = EventManager.createEvent(conn, ywlx, user);

		//插入
		BusinessUtil.setInsertCommonFields(xmvo, user);
		xmvo.setYwlx(ywlx);
		xmvo.setSjbh(eventVO.getSjbh());
		xmvo.setGc_qqsx_sxfj_id(ywid);
		xmvo.setDfl("1");
		BaseDAO.insert(conn, xmvo);
		resultVO = xmvo.getRowJson();
		
		//设置主键
		FileUploadVO vo = new FileUploadVO();
		vo.setYwid(xmvo.getJhsjid());
		vo.setGlid1(ywid);
		vo.setGlid2(xmvo.getJhsjid());
		vo.setGlid3(xmvo.getXmid());
		vo.setSjbh(eventVO.getSjbh());
		vo.setYwlx(ywlx);
		vo.setFjzt("1");
		vo.setFjlb(xmvo.getFjlx());
		FileUploadService.updateVOByYwid(conn,vo,xmvo.getGc_qqsx_sxfj_id(),user);
		
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "添加规划审批办理手续的附件内容成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "添加规划审批办理手续的附件内容失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String updateSxfj(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GhspSxfjVO vo = new GhspSxfjVO();
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
			fvo.setFjlb(vo.getFjlx());
			FileUploadService.updateVOByGlid1(conn, fvo, vo.getGc_qqsx_sxfj_id(),user);
			
			//修改操作
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();

			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "修改规划审批办理手续的附件内容成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改规划审批办理手续的附件内容失败", user,"","");
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
			//生成事件
			EventVO eventVO = EventManager.createEvent(conn, YwlxManager.GC_QQSX, user);
			vo.setGc_zgb_qqsx_id(new RandomGUID().toString()); // 主键
			vo.setSjbh(eventVO.getSjbh());
			vo.setGhsfbl("1");
			vo.setJhsjid(jhsjid);
			vo.setSjwybh(sjwybh);
			vo.setYwlx(YwlxManager.GC_QQSX);
			BusinessUtil.setInsertCommonFields(vo, user);
			//插入
			BaseDAO.insert(conn, vo);
		}else{
			//设置主键
			vo.setGc_zgb_qqsx_id(arr[0][0]);
			vo.setSjbh(arr[0][1]);
			BusinessUtil.setUpdateCommonFields(vo, user);
			//修改
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();
		}
		//-------------加入处理中心任务生成代码-------------BEGIN---------
		ClzxManagerService cms = new ClzxManagerServiceImpl();
		cms.createTask("1003005", jhsjid, user,conn);
		//-------------加入处理中心任务生成代码-------------END---------
		conn.commit();
		LogManager.writeUserLog(vo.getSjbh(), YwlxManager.GC_QQSX,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "规划审批手续部门反馈成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "规划审批手续部门反馈失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	@Override
	public String deleteSxfj(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String fjlb=request.getParameter("fjlb");
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GhspSxfjVO vo = new GhspSxfjVO();
		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		//设置主键
		BusinessUtil.setUpdateCommonFields(vo, user);
		vo.setSfyx("0");
		
		//插入
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
						+ "删除规划审批办理手续的附件内容成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(vo.getSjbh(), ywlx,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除规划审批办理手续的附件内容失败", user,"","");
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
				String sql = "select to_char(BLSJ,'YYYY-MM-DD') from (select BLSJ from GC_QQSX_SXFJ where sjwybh='"+sjwybh+"' and sfyx='1' and dfl='1' order by blsj desc) where rownum='1'";
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
}
