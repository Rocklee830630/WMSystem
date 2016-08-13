package com.ccthanking.business.qqsx.qqsxgl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
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
public class QianQiShouXuServiceImpl implements QianQiShouXuService {


	String ywlx=YwlxManager.GC_QQSX;
	String ywlxsg=YwlxManager.GC_QQSX_SGXK;
	String ywlxtt=YwlxManager.GC_QQSX_TTSP;
	String ywlxlx=YwlxManager.GC_QQSX_LXKY;
	String ywlxgh=YwlxManager.GC_QQSX_GHSP;
	@Override
	public String queryConditionQianQiShouXu(String json,User user) throws SQLException {
		
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				if(!Pub.empty(condition)){
					condition +=" and a.sjwybh(+)=b.sjwybh  and c.jhsjid(+)=B.Gc_Jh_Sj_Id ";
					
				}else{
					condition="  a.sjwybh(+)=b.sjwybh and c.jhsjid(+)=B.Gc_Jh_Sj_Id  ";
				}
				condition += BusinessUtil.getSJYXCondition("b") + BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
			    page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql="select a.gc_zgb_qqsx_id ,b.sjwybh LXKYFJ,b.sjwybh GHSPFJ,b.sjwybh SGXKFJ,b.sjwybh TTSPFJ,a.xdkid xmid, a.jbdw, a.jer, a.jjsj, a.ywlx, a.sjbh, a.bz, a.lrr, a.lrsj, " +
						"a.lrbm,a.lrbmmc, a.gxr, a.gxsj, a.gxbm, a.gxbmmc, a.sjmj, a.sfyx, a.jhid, a.jjclmx, a.lxfkzt, a.sgfkzt, a.tdfkzt, a.ghfkzt , a.lxsfbl, a.sgsfbl, a.tdsfbl," +
						" a.ghsfbl, a.lxkybjsj, a.lxkybblyy, a.lxkybblsx, a.sgxkbjsj, a.sgxkbblyy, a.sgxkbblsx, a.tdspbjsj, a.tdspbblyy, a.tdspbblsx, a.ghspbjsj,a. ghspbblyy, a.ghspbblsx," +
						"b.xmbh,b.xmmc,b.gc_jh_sj_id jhsjid,b.xmid XDKID, b.bdbh,b.nd ,b.pxh, c.color,b.bdid,b.bdmc, b.kypf jhkypf," +
						"b.sgxk jhsgxk, b.HPJDS jhhpjds,b.GCXKZ jhgcxkz, b.ISKYPF, b.ishpjds, b.isgcxkz, b.issgxk,b.sjwybh,(case b.xmbs when '0' then (select XMDZ from GC_TCJH_XMXDK where nd = b.nd and GC_TCJH_XMXDK_ID = b.XMID and rownum = 1) when '1' then (select BDDD from GC_XMBD where GC_XMBD_ID = b.bdid and rownum = 1) end ) as XMBDDZ  " +
						" from (select * from  gc_zgb_qqsx a where a.sfyx='1') a,gc_jh_sj b  ,( select  (lxfkzt+ghfkzt+tdfkzt+sgfkzt) color,jhsjid " +
						" from (select b.gc_jh_sj_id jhsjid,case when a.lxfkzt=1 then 0 else  case when b.kypf<to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') then 1 else 0 end end lxfkzt," +
						" case  when a.ghfkzt=1 then 0 else case when b.GCXKZ<to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') then 1 else 0 end end ghfkzt, " +
						" case  when a.tdfkzt=1 then 0  else case when b.HPJDS<to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') then 1 else 0 end end tdfkzt," +
						" case when a.sgfkzt=1 then 0  else case when b.SGXK<to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd') then 1 else 0 end end sgfkzt" +
						" from (select * from  gc_zgb_qqsx a where a.sfyx='1') a,gc_jh_sj  b where a.sjwybh(+)=b.sjwybh )) c ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldFileUploadWithWybh("LXKYFJ", "2020,2021,2022,2023,2024");
				bs.setFieldFileUploadWithWybh("GHSPFJ", "0007,0008,0009");
				bs.setFieldFileUploadWithWybh("SGXKFJ", "0015,0016,0017,0018,0019,0020,0021,0022");
				bs.setFieldFileUploadWithWybh("TTSPFJ", "0010,0011,0012,0013,0014,0023");
				
				
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "前期手续查询成功", user,"","");
		
			} catch (Exception e) {
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			return domresult;
	}
	@Override
	public String insert(HttpServletRequest request,String json, User user,String ywid,String xmmc) throws SQLException, Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZgbQqsxVO xmvo = new GcZgbQqsxVO();

		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			
			BusinessUtil.setInsertCommonFields(xmvo, user);
			xmvo.setYwlx(ywlx);
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
			//附件上传
			if(!Pub.empty(ywid)){
				xmvo.setGc_zgb_qqsx_id(ywid);
				
				TcjhVO jhvo=new TcjhVO();
				jhvo.setGc_jh_sj_id(xmvo.getJhsjid());
				jhvo=(TcjhVO) BaseDAO.getVOByPrimaryKey(conn, jhvo);
				
			    FileUploadVO fvo = new FileUploadVO();
		        fvo.setFjzt("1");//更新附件状态
		        fvo.setGlid2(jhvo.getGc_jh_sj_id());//存入计划数据ID
		        fvo.setGlid3(jhvo.getXmid()); //存入项目ID
		        fvo.setGlid4(jhvo.getBdid()); //存入标段ID
		        fvo.setYwlx(ywlx);
		        fvo.setSjbh(eventVO.getSjbh());
		        FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_zgb_qqsx_id(),user);
			}else{
				xmvo.setGc_zgb_qqsx_id(new RandomGUID().toString()); // 主键
			}
			//插入
			BaseDAO.insert(conn, xmvo);
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "增加前期手续信息成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_zgb_qqsx_id()+"\",\"fieldname\":\"Gc_zgb_qqsx_id\",\"operation\":\"=\",\"logic\":\"and\"},{\"value\":\""+xmvo.getJhsjid()+"\",\"fieldname\":\"Gc_Jh_Sj_Id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String result=this.queryConditionQianQiShouXu(jsona,user);
			return result;
	}
	@Override
	public String updatedemo(HttpServletRequest request,String json, User user,String xmmc) throws SQLException,Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcZgbQqsxVO vo = new GcZgbQqsxVO();

		try {
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setUpdateCommonFields(vo, user);
			vo.setYwlx(ywlx);
			
			//根据ID查YWID和SJBH
			GcZgbQqsxVO vo1 =(GcZgbQqsxVO) BaseDAO.getVOByPrimaryKey(conn, vo);
			//附件上传
			TcjhVO jhvo1=new TcjhVO();
			jhvo1.setGc_jh_sj_id(vo.getJhsjid());
			jhvo1=(TcjhVO) BaseDAO.getVOByPrimaryKey(conn, jhvo1);
			FileUploadVO fvo = new FileUploadVO();
			fvo.setFjzt("1");
			fvo.setGlid2(jhvo1.getGc_jh_sj_id());//存入计划数据ID
	        fvo.setGlid3(jhvo1.getXmid()); //存入项目ID
	        fvo.setGlid4(jhvo1.getBdid()); //存入标段ID
	        fvo.setSjbh(vo1.getSjbh());
	        fvo.setYwlx(vo1.getYwlx());
			FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_zgb_qqsx_id(),user);
			//插入
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改前期手续信息成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
		String jsona="{querycondition: {conditions: [{\"value\":\""+vo.getGc_zgb_qqsx_id()+"\",\"fieldname\":\"gc_zgb_qqsx_id\",\"operation\":\"=\",\"logic\":\"and\"},{\"value\":\""+vo.getJhsjid()+"\",\"fieldname\":\"Gc_Jh_Sj_Id\",\"operation\":\"=\",\"logic\":\"and\"}]}}";
		String result=this.queryConditionQianQiShouXu(jsona,user);
		return result;
	}
	@Override
	public String querySxfjzj(HttpServletRequest request, String json,String dfl ) {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition =RequestUtil.getConditionList(json).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
		condition +=" and dfl= "+dfl;
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql="select  gc_qqsx_sxfj_id, fjid,jhsjid SXFJ,SJWYBH, fjlx, dfl, ywbid, whmc, blr, tzbh, blsj, bz, lrr, lrsj, czwt, jhsjid, xmid from GC_QQSX_SXFJ ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			
			if(dfl.equals("0"))
			{
				bs.setFieldFileUpload("SXFJ","2020,2021,2022,2023,2024");
				bs.setFieldTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldDic("FJLX", "LXKYFJLX");
			}
			if(dfl.equals("1"))
			{
				bs.setFieldDic("FJLX", "GHSX");
				bs.setFieldFileUpload("SXFJ", "0007,0008,0009");
				bs.setFieldTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			}
			if(dfl.equals("2"))
			{
				bs.setFieldDic("FJLX", "TDSPSX");
				bs.setFieldFileUpload("SXFJ", "0010,0011,0012,0013,0014,0023");
				bs.setFieldTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			}
			if(dfl.equals("3"))
			{
				bs.setFieldDic("FJLX", "SGXKSX");
				bs.setFieldTranslater("BLR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
				bs.setFieldFileUpload("SXFJ","0015,0016,0017,0018,0019,0020,0021,0022");
			}
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

}
