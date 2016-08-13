package com.ccthanking.business.sjgl.gs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.fileUpload.service.FileUploadService;
import com.ccthanking.framework.fileUpload.vo.FileUploadVO;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.business.clzx.service.ClzxManagerService;
import com.ccthanking.business.clzx.service.impl.ClzxManagerServiceImpl;
import com.ccthanking.business.sjgl.gs.GcSjCbsjpfService;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.business.sjgl.gs.GcSjCbsjpfVO;
import com.ccthanking.business.sjgl.sj.GcSjVO;
import com.ccthanking.business.zjb.jsgl.GcZjbJsbVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.YwlxManager;
import com.ccthanking.common.vo.EventVO;


@Service
public  class GcSjCbsjpfServiceImpl implements GcSjCbsjpfService {
	private String ywlx=YwlxManager.GC_SJ_CBSJPF;

	@Override
	public String queryGs(HttpServletRequest request,String json) throws Exception {
		Connection conn = DBUtil.getConnection();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String domresult = "";
		try {
		PageManager page = RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = RequestUtil.getConditionList(json).getConditionWhere();
		condition=condition+"and cbsjpf.jhsjid(+) = jhsj.gc_jh_sj_id and jhsj.xmid = xmxdk.gc_tcjh_xmxdk_id";
		condition += BusinessUtil.getSJYXCondition("jhsj") +BusinessUtil.getSJYXCondition("xmxdk") + BusinessUtil.getCommonCondition(user,null);
		condition += orderFilter;
		page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select cbsjpf.sjmj, cbsjpf.GC_SJ_CBSJPF_ID, cbsjpf.cbsjpf, cbsjpf.bz, xmxdk.xmdz, jhsj.nd, jhsj.jhid, jhsj.gc_jh_sj_id as jhsjid, jhsj.xmid, jhsj.bdid,jhsj.xmbh,jhsj.xmmc,jhsj.bdmc,jhsj.pxh from GC_JH_SJ jhsj, GC_SJ_CBSJPF cbsjpf, GC_TCJH_XMXDK xmxdk";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询概算信息成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertGs(HttpServletRequest request,String json,String ywid) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcSjCbsjpfVO xmvo = new GcSjCbsjpfVO();

		try {
			conn.setAutoCommit(false);
			ClzxManagerService cms = new ClzxManagerServiceImpl();
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		//设置主键
		xmvo.setYwlx(ywlx);
		EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
		xmvo.setSjbh(eventVO.getSjbh());
		//设置主键
		if(!Pub.empty(ywid)){
			xmvo.setGc_sj_cbsjpf_id(ywid);
			
		    FileUploadVO fvo = new FileUploadVO();
	        fvo.setFjzt("1");//更新附件状态
	        fvo.setGlid2(xmvo.getJhsjid());//存入计划数据ID	 
	        fvo.setGlid3(xmvo.getXmid()); //存入项目ID
	        fvo.setGlid4(xmvo.getBdid()); //存入标段ID
	        fvo.setYwlx(ywlx);
	        fvo.setSjbh(eventVO.getSjbh());
	        
	        FileUploadService.updateVOByYwid(conn, fvo, xmvo.getGc_sj_cbsjpf_id(),user);
			
		}else{
			xmvo.setGc_sj_cbsjpf_id(new RandomGUID().toString()); // 主键
		}
		
		BusinessUtil.setInsertCommonFields(xmvo, user);
		//插入
		BaseDAO.insert(conn, xmvo);
		resultVO = xmvo.getRowJson();

		//-------------加入处理中心任务生成代码-------------BEGIN---------
		cms.createTask("1007001", xmvo.getJhsjid(), user,conn);
		//-------------加入处理中心任务生成代码-------------END---------
		//初步设计表数据添加
		GcSjVO sjvo=new GcSjVO();
		sjvo.setSjwybh(xmvo.getSjwybh());
		sjvo.setSfyx("1");
		BaseVO[] vs=BaseDAO.getVOByCondition(conn, sjvo);
		if(vs==null||vs.length==0)
		{
			GcSjVO sj = new GcSjVO();
			sj.setGc_sj_id(new RandomGUID().toString()); // 主键
			sj.setJhid(xmvo.getJhid());
			sj.setJhsjid(xmvo.getJhsjid());
			sj.setSjwybh(xmvo.getSjwybh());
			sj.setXmid(xmvo.getXmid());
			sj.setBdid(xmvo.getBdid());
			sj.setNd(xmvo.getNd());
			sj.setWcsj_ys(xmvo.getCbsjpf());
			BusinessUtil.setInsertCommonFields(sj, user);
			sj.setYwlx(YwlxManager.GC_SJ);
			EventVO eventVO1 = EventManager.createEvent(conn, sj.getYwlx(), user);//生成事件
			sj.setSjbh(eventVO1.getSjbh());
			//插入
			BaseDAO.insert(conn, sj);	
		}else{
			for(int i = 0 ;i<vs.length;i++)
			{
				GcSjVO sj1 = (GcSjVO)vs[i];
				sj1.setJhid(xmvo.getJhid());
				sj1.setSjwybh(xmvo.getSjwybh());
				sj1.setJhsjid(xmvo.getJhsjid());
				sj1.setWcsj_ys(xmvo.getCbsjpf());
                BusinessUtil.setUpdateCommonFields(sj1, user);
				BaseDAO.update(conn, sj1);
		}
		}
		
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "插入概算信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "插入概算信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	@Override
	public String updateGs(HttpServletRequest request,String json) throws Exception {
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String gs_sjbh=request.getParameter("gs_sjbh");
		String resultVO = null;
		GcSjCbsjpfVO vo = new GcSjCbsjpfVO();

		try {
			conn.setAutoCommit(false);
		JSONArray list = vo.doInitJson(json);
		vo.setValueFromJson((JSONObject)list.get(0));
		BusinessUtil.setUpdateCommonFields(vo, user);
		
		GcSjCbsjpfVO vo1=(GcSjCbsjpfVO) BaseDAO.getVOByPrimaryKey(conn, vo);

		//-------------加入处理中心任务生成代码-------------BEGIN---------
		ClzxManagerService cms = new ClzxManagerServiceImpl();
		String sql = "select to_char(CBSJPF_SJ,'yyyy-MM-dd') from GC_JH_SJ where GC_JH_SJ_ID='"+vo1.getJhsjid()+"'";
		String arr[][] = DBUtil.query(conn, sql);
		if(arr!=null && arr.length!=0 && vo1.getCbsjpf()!=null){
			String jsrq = DateTimeUtil.getDateTime(vo1.getCbsjpf(),"yyyy-MM-dd");
			if(arr[0][0]!=null && !jsrq.equals(arr[0][0])){
        		cms.createTask("1007001", vo1.getJhsjid(), user,conn);
			}
		}
		//-------------加入处理中心任务生成代码-------------END---------
		FileUploadVO fvo = new FileUploadVO();
     	fvo.setFjzt("1");
     	fvo.setGlid2(vo.getJhsjid());//存入计划数据ID
     	fvo.setGlid3(vo.getXmid()); //存入项目ID
     	fvo.setGlid4(vo.getBdid()); //存入标段ID
     	fvo.setYwlx(ywlx);
     	fvo.setSjbh(vo1.getSjbh());
     	FileUploadService.updateVOByYwid(conn, fvo, vo.getGc_sj_cbsjpf_id(),user);
     	
		//插入
		BaseDAO.update(conn, vo);
		resultVO = vo.getRowJson();
		
		//初步设计表数据添加
		GcSjVO sjvo=new GcSjVO();
		sjvo.setSjwybh(vo.getSjwybh());
		sjvo.setSfyx("1");
		BaseVO[] vs=BaseDAO.getVOByCondition(conn, sjvo);
		if(vs==null||vs.length==0)
		{
				GcSjVO sj = new GcSjVO();
				sj.setGc_sj_id(new RandomGUID().toString()); // 主键
				sj.setJhid(vo.getJhid());
				sj.setJhsjid(vo.getJhsjid());
				sj.setSjwybh(vo.getSjwybh());
				sj.setXmid(vo.getXmid());
				sj.setBdid(vo.getBdid());
				sj.setNd(vo.getNd());
				sj.setWcsj_ys(vo.getCbsjpf());
				BusinessUtil.setInsertCommonFields(sj, user);
				sj.setYwlx(YwlxManager.GC_SJ);
				EventVO eventVO1 = EventManager.createEvent(conn, sj.getYwlx(), user);//生成事件
				sj.setSjbh(eventVO1.getSjbh());
				//插入
				BaseDAO.insert(conn, sj);	
		}else{
			for(int i = 0 ;i<vs.length;i++)
			{
				GcSjVO sj1 = (GcSjVO)vs[i];
				sj1.setJhid(vo.getJhid());
				sj1.setJhsjid(vo.getJhsjid());
				sj1.setSjwybh(vo.getSjwybh());
				sj1.setWcsj_ys(vo.getCbsjpf());
				
                BusinessUtil.setUpdateCommonFields(sj1, user);
				BaseDAO.update(conn, sj1);
		}
		}
		
		
		conn.commit();
		LogManager.writeUserLog(gs_sjbh, YwlxManager.GC_SJ_CBSJPF,
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "修改概算信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(gs_sjbh, YwlxManager.GC_SJ_CBSJPF,
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改概算信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	
	//获取当前时间下的征收进度信息
	@Override
	public String getSx(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String xmid = request.getParameter("xmid");
		String fjlx = request.getParameter("fjlx");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String jhsjid="";
			String jhsjsql = "select gc_jh_sj_id from gc_jh_sj where xmid='"+xmid+"' and sfyx='1'";
			String[][] arr=DBUtil.query(conn, jhsjsql);
			if(arr==null||arr.length==0){
				jhsjid="XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
			}else{
				jhsjid = arr[0][0];
			}
			String sql="";
			if(fjlx.equals("1")){
					sql = "select to_char(qqsx.GHSPBJSJ,'yyyy-mm-dd') from GC_ZGB_QQSX qqsx,GC_JH_SJ jhsj where qqsx.jhsjid(+)=jhsj.gc_jh_sj_id and jhsj.gc_jh_sj_id='"+jhsjid+"' and qqsx.sfyx='1' and jhsj.sfyx='1'";
			}else{
				if(fjlx.equals("2")){
					sql = "select to_char(qqsx.LXKYBJSJ,'yyyy-mm-dd') from GC_ZGB_QQSX qqsx,GC_JH_SJ jhsj where qqsx.jhsjid(+)=jhsj.gc_jh_sj_id and jhsj.gc_jh_sj_id='"+jhsjid+"' and qqsx.sfyx='1' and jhsj.sfyx='1'";
				}else{
					sql = "select to_char(max(blsj),'yyyy-mm_dd') from (select blsj from gc_qqsx_sxfj where fjlx='"+fjlx+"'and sfyx='1' and jhsjid='"+jhsjid+"' order by blsj)";
				}
			}
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

	//获取当前时间下的征收进度信息
	@Override
	public String getBblsx(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String sjwybh = request.getParameter("sjwybh");
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			String sql= "select qqsx.lxkybblsx||','||qqsx.ghspbblsx from gc_zgb_qqsx qqsx,gc_jh_sj jhsj " +
					"where qqsx.jhsjid=jhsj.gc_jh_sj_id and qqsx.sfyx='1' and jhsj.sfyx='1' and jhsj.sjwybh='"+sjwybh+"'  and jhsj.nd="+nd+" and jhsj.bdid is null";
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
	
	//获取当前时间下的前期手续办结时间
	@Override
	public String getQqsx(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		String sjwybh = request.getParameter("sjwybh");
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			//修改概算手续条件查询的表，注释的查找的是前期手续表，下面查询的是统筹计划表
			String sql = "select decode(jhsj.ISKYPF,1,to_char(jhsj.KYPF_SJ,'yyyy-mm-dd'),'no')||','||decode(jhsj.ISGCXKZ,1,to_char(jhsj.GCXKZ_SJ,'yyyy-mm-dd'),'no') from" +
					" gc_jh_sj jhsj where jhsj.sfyx='1' and jhsj.sjwybh='"+sjwybh+"' and jhsj.nd="+nd+" and jhsj.bdid is null";
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
