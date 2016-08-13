package com.ccthanking.business.jhfk.service.impl;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;


import com.ccthanking.business.jhfk.vo.FkgxVO;
import com.ccthanking.business.jjg.vo.JjgwhVO;
import com.ccthanking.business.pqgl.vo.PqInfoVO;
import com.ccthanking.business.qqsx.qqsxgl.GcZgbQqsxVO;
import com.ccthanking.business.sjgl.sj.GcSjVO;
import com.ccthanking.business.tcjh.jhgl.vo.FkqkVO;
import com.ccthanking.business.xmglgs.xxjd.vo.XxjdVO;
import com.ccthanking.business.zjb.lbj.GcZjbLbjbVO;
import com.ccthanking.business.zsb.zsgl.xmb.GcZsbXmbVO;
import com.ccthanking.business.tcjh.jhgl.vo.TcjhVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.util.DateTimeUtil;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;

public class JhfkCommon {
	/**
	 * 获取表主键值
	 * @param conn
	 * @param fkgxVO
	 * @param jhsjid
	 * @return
	 * @throws Exception
	 */
	private String getPkValue(Connection conn,FkgxVO fkgxVO,String jhsjid)throws Exception{
		String s = "";
		String pk = getPrimaryKeys(conn,fkgxVO.getYwbmc());
		String sql = "select "+pk+" from "+fkgxVO.getYwbmc()+" where JHSJID='"+jhsjid+"' and SFYX='1'";
		s = DBUtil.query(conn, sql)[0][0];
		return s;
	}
	/**
	 * 获取表的主键
	 * @param conn
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private String getPrimaryKeys(Connection conn,String tableName)throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseMetaData dmd = null;  
        String pkColumn = "";
		try{
			String sql = "select * from "+tableName+" where 1=2";
			ps = conn.prepareStatement(sql);
			ps.execute();
			rs = ps.executeQuery();
			dmd = conn.getMetaData();
			rs = dmd.getPrimaryKeys(null,"XMGL",tableName);  
			while(rs.next()){  
				pkColumn = rs.getString(4).toUpperCase();  
			}
		}catch(Exception e){
			throw e;
		}finally{
			ps.close();
			rs.close();
		}
		return pkColumn;
	}
	/**
	 * 反射调用
	 * @param conn
	 * @param fklx
	 * @param user
	 * @throws Exception
	 */
	public void callbackMethod(Connection conn,FkqkVO resultVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
		try{
			String fklx = resultVO.getFklx();
			JhfkCommon common = new JhfkCommon();
			Method method = common.getClass().getMethod("doJhfk"+fklx, new Class[]{Connection.class,FkqkVO.class,FkgxVO.class,HttpServletRequest.class});
			method.invoke(common, new Object[]{conn,resultVO,fkgxVO,request});
		}catch(NoSuchMethodException e){
			//如果是没找到方法，那么不需要处理
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 
	 * @param conn
	 * @param jhsjid
	 * @param sjColumnName
	 * @param kzColumnName
	 * @throws Exception
	 */
	public static void doAutoFeedBackXM(Connection conn,String jhsjid,String fklx,HttpServletRequest request) throws Exception{
		FkgxVO condVO = new FkgxVO();
		condVO.setFklx(fklx);
		FkgxVO vo = new FkgxVO();
		vo = (FkgxVO)BaseDAO.getVOByPrimaryKey(conn, condVO);
		String xmSql = "select XMID,XMBS from GC_JH_SJ where GC_JH_SJ_ID='"+jhsjid+"' and SFYX='1'";
		String[][] xmArr = DBUtil.query(conn, xmSql);
		String xmid = xmArr[0][0];
		String xmbs = xmArr[0][1];
		String sqlFkzt = "select count(GC_JH_SJ_ID) from GC_JH_SJ where "+vo.getKzzdmc()+"='1' and "+vo.getJhzdmc()+" is null and XMBS='1' and XMID='"+xmid+"' and SFYX='1'";
		String fkztFlag = DBUtil.query(conn,sqlFkzt)[0][0];
		//这里要判断一下是否所有标段都已反馈时间节点，如果都反馈过了，那么把项目的改成最晚的那个
		//1、所有该反馈时间的标段都已经反馈
		//2、该反馈时间的标段数量不是0
		//3、当前反馈的这条数据不是项目
		if("0".equals(fkztFlag)&&"1".equals(xmbs)){
//			String sqlUpdateXM = "update GC_JH_SJ set "+sjColumnName+"=(select max("+sjColumnName+") " +
//					"from GC_JH_SJ " +
//					"where "+kzColumnName+"='1' " +
//					"and XMBS='1' " +
//					"and XMID='"+xmid+"' and SFYX='1') " +
//					"where XMID='"+xmid+"' and XMBS='0' and SFYX='1'";
//			DBUtil.execSql(conn, sqlUpdateXM);
			String getXmxxSql = "select GC_JH_SJ_ID,(select to_char(max("+vo.getJhzdmc()+"),'yyyy-MM-dd') " +
					"from GC_JH_SJ " +
					"where "+vo.getKzzdmc()+"='1' " +
					"and XMBS='1' " +
					"and XMID='"+xmid+"' and SFYX='1') FKRQ " + 
					"from GC_JH_SJ where XMID='"+xmid+"' and XMBS='0' and SFYX='1'";
			String arr[][] = DBUtil.query(conn, getXmxxSql);
			JSONObject rowJson = new JSONObject();
			rowJson.put("JHSJID", arr[0][0]);
			rowJson.put("FKRQ", arr[0][1]);
			rowJson.put("FLAG", "1");
			JhfkCommon common = new JhfkCommon();
			FkqkVO resultVO = common.doFksj(conn, rowJson, vo, request);
			common.callbackMethod(conn,resultVO,vo,request);
		}
	}
	/**
	 * 
	 * @param conn
	 * @param rowJson
	 * @param fkgxVO
	 * @param user
	 * @param flag
	 * @return
	 * @throws Exception
	 */
	public FkqkVO doFksj(Connection conn,JSONObject rowJson,FkgxVO fkgxVO,HttpServletRequest request) throws Exception{
		FkqkVO fkqkvo = new FkqkVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String pkValue = "";
		String flag = (String)rowJson.get("FLAG");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //-------------------------------------------------获取计划数据VO
		TcjhVO condVO = new TcjhVO();
		condVO.setGc_jh_sj_id((String)rowJson.get("JHSJID"));
		TcjhVO tcjhVO = (TcjhVO) BaseDAO.getVOByPrimaryKey(conn,condVO);
        //-------------------------------------------------获取eventVO
		EventVO eventVO = EventManager.createEvent(conn, fkgxVO.getYwlx(), user);//生成事件
		String sjbh = eventVO.getSjbh();
		Date fkrq = rowJson.has("FKRQ")&&!Pub.empty(rowJson.get("FKRQ").toString())?sdf.parse(rowJson.get("FKRQ").toString()):null;
		String jhsjsj = rowJson.get("FKRQ").toString();
		//-------------------------------------------------反馈情况表（GC_JH_FKQK）插入数据
		String updateFkqkSql = "update GC_JH_FKQK set ZXBZ='0' where JHSJID='"+tcjhVO.getGc_jh_sj_id()+"' and FKLX='"+fkgxVO.getFklx()+"'";
		DBUtil.execSql(conn,updateFkqkSql);
		fkqkvo.setGc_jh_fkqk_id(new RandomGUID().toString());
		fkqkvo.setJhid(tcjhVO.getJhid());
		fkqkvo.setJhsjid(tcjhVO.getGc_jh_sj_id());
		fkqkvo.setXmid(tcjhVO.getXmid());
		fkqkvo.setBdid(tcjhVO.getBdid());
		fkqkvo.setSjbh(sjbh);
		fkqkvo.setFkid(pkValue);
		fkqkvo.setFkrq(fkrq);
		fkqkvo.setYwlx(fkgxVO.getYwlx());
		fkqkvo.setFklx(fkgxVO.getFklx());
		if("1".equals(flag)){
			fkqkvo.setFkrq(fkrq);
			String getLastDateSql = "select max("+fkgxVO.getJhzdmc()+") from GC_JH_SJ " +
					"where XMID=(select XMID from GC_JH_SJ where GC_JH_SJ_ID='"+tcjhVO.getGc_jh_sj_id()+"') and "+fkgxVO.getJhzdmc()+" is not null";
			String arr [][]= DBUtil.query(conn, getLastDateSql);
			if(arr!=null && arr.length>0 && arr[0][0]!=null){
				jhsjsj = sdf.format(DateTimeUtil.parse(arr[0][0], "yyyy-MM-dd"));
			}
			fkqkvo.setBz("标段反馈完毕，系统自动反馈项目时间点");
		}
		fkqkvo.setZxbz("1");
		BusinessUtil.setInsertCommonFields(fkqkvo,user);
		BaseDAO.insert(conn, fkqkvo);
		//------------------------------------------------反馈计划数据表
		String sql = "update GC_JH_SJ set "+fkgxVO.getJhzdmc()+"=to_date('"+jhsjsj+"','yyyy-mm-dd') where GC_JH_SJ_ID='"+tcjhVO.getGc_jh_sj_id()+"'" ;
		DBUtil.execSql(conn, sql);
		return fkqkvo;
	}
	public String doJhfk1001001(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
		String domresult = "";
		PqInfoVO vo = new PqInfoVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			String jhsjid = fkqkVO.getJhsjid();
			String sql = "select count(*) from GC_PQ where JHSJID='"+jhsjid+"' and SFYX='1'";
			String s = DBUtil.query(conn, sql)[0][0];
			vo.setSjsj_pq(fkqkVO.getFkrq());
			vo.setXmid(fkqkVO.getXmid());
			vo.setBdid(fkqkVO.getBdid());
			vo.setJhid(fkqkVO.getJhid());
			vo.setJhsjid(jhsjid);
			if(s=="0"||"0".equals(s)){
				vo.setGc_pq_id(new RandomGUID().toString());
				vo.setYwlx(fkqkVO.getYwlx());
				EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
				vo.setSjbh(event.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
			}else{
				String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
				vo.setGc_pq_id(pkValue);
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
			}
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁计划反馈成功", user,"","");
		} catch (Exception e) {
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "排迁计划反馈失败", user,"","");
			e.printStackTrace(System.out);
			throw e;
		} finally {
		}
		return domresult;
	}
	//上报值反馈回显业务表
	public String doJhfk2001001(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
		String domresult = "";
		GcZjbLbjbVO vo = new GcZjbLbjbVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			String jhsjid = fkqkVO.getJhsjid();
			String sql = "select count(*) from GC_ZJB_LBJB where JHSJID='"+jhsjid+"' and SFYX='1'";
			String s = DBUtil.query(conn, sql)[0][0];
			vo.setSbcszrq(fkqkVO.getFkrq());
			
			if(s=="0"||"0".equals(s)){
				vo.setXmid(fkqkVO.getXmid());
				vo.setBdid(fkqkVO.getBdid());
				vo.setJhid(fkqkVO.getJhid());
				vo.setJhsjid(jhsjid);
				vo.setGc_zjb_lbjb_id(new RandomGUID().toString());
				vo.setYwlx(fkqkVO.getYwlx());
				EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
				vo.setSjbh(event.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
			}else{
				String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
				vo.setGc_zjb_lbjb_id(pkValue);
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
			}
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "上报财审值反馈成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			throw e;
		} finally {
		}
		return domresult;
	}
	//财审反馈回显业务表
	public String doJhfk2001101(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		GcZjbLbjbVO vo = new GcZjbLbjbVO();
		try {
			String jhsjid = fkqkVO.getJhsjid();
			String sql = "select count(*) from GC_ZJB_LBJB where JHSJID='"+jhsjid+"' and SFYX='1'";
			String s = DBUtil.query(conn, sql)[0][0];
			vo.setCssdzrq(fkqkVO.getFkrq());
			
			if(s=="0"||"0".equals(s)){
				vo.setXmid(fkqkVO.getXmid());
				vo.setBdid(fkqkVO.getBdid());
				vo.setJhid(fkqkVO.getJhid());
				vo.setJhsjid(jhsjid);
				vo.setGc_zjb_lbjb_id(new RandomGUID().toString());
				vo.setYwlx(fkqkVO.getYwlx());
				EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
				vo.setSjbh(event.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
			}else{
				String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
				vo.setGc_zjb_lbjb_id(pkValue);
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
			}
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "财审审定值反馈成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			throw e;
		} finally {
		}
		return domresult;
	}
	//概算完成时间反馈回显业务表
	public String doJhfk1007001(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		GcSjVO vo = new GcSjVO();
		try {
			String jhsjid = fkqkVO.getJhsjid();
			String sql = "select count(*) from GC_SJ where JHSJID='"+jhsjid+"' and SFYX='1'";
			String s = DBUtil.query(conn, sql)[0][0];
			vo.setWcsj_ys(fkqkVO.getFkrq());
			if(s=="0"||"0".equals(s)){
				vo.setXmid(fkqkVO.getXmid());
				vo.setBdid(fkqkVO.getBdid());
				vo.setJhid(fkqkVO.getJhid());
				vo.setJhsjid(jhsjid);
				vo.setGc_sj_id(new RandomGUID().toString());
				vo.setYwlx(fkqkVO.getYwlx());
				EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
				vo.setSjbh(event.getSjbh());
				BusinessUtil.setInsertCommonFields(vo,user);
				BaseDAO.insert(conn, vo);
			}else{
				String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
				vo.setGc_sj_id(pkValue);
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
			}
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "概算完成时间反馈成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			throw e;
		} finally {
		}
		return domresult;
	}
	//拆迁图完成时间反馈回显业务表
		public String doJhfk1007003(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			GcSjVO vo = new GcSjVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_SJ where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				vo.setWcsj_cqt(fkqkVO.getFkrq());
				if(s=="0"||"0".equals(s)){
					vo.setXmid(fkqkVO.getXmid());
					vo.setBdid(fkqkVO.getBdid());
					vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_sj_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_sj_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "拆迁图完成时间反馈成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
		//排迁图完成时间反馈回显业务表
		public String doJhfk1007005(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			GcSjVO vo = new GcSjVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_SJ where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				vo.setWcsj_pqt(fkqkVO.getFkrq());
				if(s=="0"||"0".equals(s)){
					vo.setXmid(fkqkVO.getXmid());
					vo.setBdid(fkqkVO.getBdid());
					vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_sj_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_sj_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "排迁图完成时间反馈成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
		//施工图完成时间反馈回显业务表
		public String doJhfk1007007(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			GcSjVO vo = new GcSjVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_SJ where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				vo.setWcsj_sgt_zsb(fkqkVO.getFkrq());
				if(s=="0"||"0".equals(s)){
					vo.setXmid(fkqkVO.getXmid());
					vo.setBdid(fkqkVO.getBdid());
					vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_sj_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_sj_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "施工图完成时间反馈成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
		//交工完成时间反馈回显业务表
		public String doJhfk1007009(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			JjgwhVO vo = new JjgwhVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_SJGL_JJG where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				vo.setJgysrq(fkqkVO.getFkrq());
				if(s=="0"||"0".equals(s)){
					vo.setXmid(fkqkVO.getXmid());
					vo.setBdid(fkqkVO.getBdid());
					vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_sjgl_jjg_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_sjgl_jjg_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "交工完成时间反馈成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
		//竣工完成时间反馈回显业务表
		public String doJhfk1007011(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			JjgwhVO vo = new JjgwhVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_SJGL_JJG where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				vo.setJgyssj(fkqkVO.getFkrq());
				if(s=="0"||"0".equals(s)){
					vo.setXmid(fkqkVO.getXmid());
					vo.setBdid(fkqkVO.getBdid());
					vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_sjgl_jjg_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_sjgl_jjg_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "竣工完成时间反馈成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
		//立项可研完成时间反馈回显业务表
		public String doJhfk1003001(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			GcZgbQqsxVO vo = new GcZgbQqsxVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_ZGB_QQSX where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				vo.setLxkybjsj(fkqkVO.getFkrq());
				vo.setLxfkzt("1");
				if(s=="0"||"0".equals(s)){
					vo.setXdkid(fkqkVO.getXmid());
					//vo.setBdid(fkqkVO.getBdid());
					vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_zgb_qqsx_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_zgb_qqsx_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "立项可研完成时间反馈成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
		//土地审批完成时间反馈回显业务表
		public String doJhfk1003003(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			GcZgbQqsxVO vo = new GcZgbQqsxVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_ZGB_QQSX where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				vo.setTdspbjsj(fkqkVO.getFkrq());
				vo.setTdfkzt("1");
				if(s=="0"||"0".equals(s)){
					vo.setXdkid(fkqkVO.getXmid());
					//vo.setBdid(fkqkVO.getBdid());
					vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_zgb_qqsx_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_zgb_qqsx_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}
				
				//消息推送
				String sql1 = "select XMMC from GC_JH_SJ where GC_JH_SJ_ID='"+jhsjid+"' and SFYX='1'";
				String[][] xmmcs= DBUtil.querySql(sql1);
				//根据ID查YWID和SJBH
				GcZgbQqsxVO vo1 =(GcZgbQqsxVO) BaseDAO.getVOByPrimaryKey(conn, vo);
				PushMessage.push(conn, request, PushMessage.GHSPSX,"项目【"+xmmcs[0][0]+"】的规划审批手续已经办理完成！",null,vo1.getSjbh(),vo1.getYwlx(),vo.getGc_zgb_qqsx_id());
				
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "土地审批完成时间反馈成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
		//规划审批完成时间反馈回显业务表
		public String doJhfk1003005(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			GcZgbQqsxVO vo = new GcZgbQqsxVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_ZGB_QQSX where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				vo.setGhspbjsj(fkqkVO.getFkrq());
				vo.setGhfkzt("1");
				if(s=="0"||"0".equals(s)){
					vo.setXdkid(fkqkVO.getXmid());
					//vo.setBdid(fkqkVO.getBdid());
					vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_zgb_qqsx_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_zgb_qqsx_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}
				
				//消息推送
				String sql1 = "select XMMC from GC_JH_SJ where GC_JH_SJ_ID='"+jhsjid+"' and SFYX='1'";
				String[][] xmmcs= DBUtil.querySql(sql1);
				//根据ID查YWID和SJBH
				GcZgbQqsxVO vo1 =(GcZgbQqsxVO) BaseDAO.getVOByPrimaryKey(conn, vo);
				PushMessage.push(conn, request, PushMessage.GHSPSX,"项目【"+xmmcs[0][0]+"】的规划审批手续已经办理完成！",null,vo1.getSjbh(),vo1.getYwlx(),vo.getGc_zgb_qqsx_id());
				
				
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "规划审批完成时间反馈成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
		//施工许可完成时间反馈回显业务表
		public String doJhfk1003007(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			GcZgbQqsxVO vo = new GcZgbQqsxVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_ZGB_QQSX where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				vo.setSgxkbjsj(fkqkVO.getFkrq());
				vo.setSgfkzt("1");
				if(s=="0"||"0".equals(s)){
					vo.setXdkid(fkqkVO.getXmid());
					//vo.setBdid(fkqkVO.getBdid());
					vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_zgb_qqsx_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_zgb_qqsx_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}
				LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "施工许可完成时间反馈成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
		//开工完成时间反馈回显业务表
		public String doJhfk1008001(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			XxjdVO vo = new XxjdVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_XMGLGS_XXJD where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				String sqlName = "select XMMC,BDMC from GC_JH_SJ where GC_JH_SJ_ID='"+jhsjid+"' ";
				String xmmc = DBUtil.query(conn, sqlName)[0][0];
				String bdmc = DBUtil.query(conn, sqlName)[0][1];
				
				String msgContent = "".equals(bdmc) ? xmmc : (xmmc + "，标段：" + bdmc);
				
				vo.setSjkgsj(fkqkVO.getFkrq());
				if(s=="0"||"0".equals(s)){
					vo.setXmid(fkqkVO.getXmid());
					vo.setBdid(fkqkVO.getBdid());
					//vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_xmglgs_xxjd_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					vo.setXmmc(xmmc);
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_xmglgs_xxjd_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}

				// start 实际开工时间已完，发送相关配置人员信息提醒。    add by xiahongbo by 2014-10-14
				XxjdVO vo1=(XxjdVO) BaseDAO.getVOByPrimaryKey(conn, vo);
				PushMessage.push(conn, request, PushMessage.KGSJ_SJ_FK, "项目："+msgContent+" 。实际开工时间已完，请查看",null,vo1.getSjbh(),vo1.getYwlx(),vo1.getGc_xmglgs_xxjd_id(),"消息提示");
				// end
				LogManager.writeUserLog(fkqkVO.getSjbh(), fkqkVO.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "开工完成时间反馈成功", user,"","");
				vo.setZt("1");
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
		//完工完成时间反馈回显业务表
		public String doJhfk1008013(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			String zdfk = request.getParameter("zdfk") == null ? "" : request.getParameter("zdfk");
			XxjdVO vo = new XxjdVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_XMGLGS_XXJD where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				vo.setSjwgsj(fkqkVO.getFkrq());
				if(s=="0"||"0".equals(s)){
					vo.setXmid(fkqkVO.getXmid());
					vo.setBdid(fkqkVO.getBdid());
					//vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_xmglgs_xxjd_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_xmglgs_xxjd_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}

				// start 实际完工时间已完，发送相关配置人员信息提醒。    add by xiahongbo by 2014-10-14
				if(!"1".equals(zdfk)) {
					String sqlName = "select XMMC,BDMC from GC_JH_SJ where GC_JH_SJ_ID='"+jhsjid+"' ";
					String xmmc = DBUtil.query(conn, sqlName)[0][0];
					String bdmc = DBUtil.query(conn, sqlName)[0][1];
					String msgContent = "".equals(bdmc) ? xmmc : (xmmc + "，标段：" + bdmc);

					XxjdVO vo1=(XxjdVO) BaseDAO.getVOByPrimaryKey(conn, vo);
					PushMessage.push(conn, request, PushMessage.WGSJ_SJ_FK, "项目："+msgContent+" 。实际完工时间已完，请查看",null,vo1.getSjbh(),vo1.getYwlx(),vo1.getGc_xmglgs_xxjd_id(),"消息提示");
				}
				// end
				LogManager.writeUserLog(fkqkVO.getSjbh(), fkqkVO.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "开工完成时间反馈成功", user,"","");
				vo.setZt("1");
				BusinessUtil.setUpdateCommonFields(vo,user);
				BaseDAO.update(conn, vo);
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
		//征收完成时间反馈回显业务表
		public String doJhfk1009001(Connection conn,FkqkVO fkqkVO,FkgxVO fkgxVO,HttpServletRequest request)throws Exception{
			String domresult = "";
			User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
			GcZsbXmbVO vo = new GcZsbXmbVO();
			try {
				String jhsjid = fkqkVO.getJhsjid();
				String sql = "select count(*) from GC_ZSB_XMB where JHSJID='"+jhsjid+"' and SFYX='1'";
				String s = DBUtil.query(conn, sql)[0][0];
				vo.setSjwcrq(fkqkVO.getFkrq());
				if(s=="0"||"0".equals(s)){
					vo.setXmid(fkqkVO.getXmid());
					vo.setBdid(fkqkVO.getBdid());
					//vo.setJhid(fkqkVO.getJhid());
					vo.setJhsjid(jhsjid);
					vo.setGc_zsb_xmb_id(new RandomGUID().toString());
					vo.setYwlx(fkqkVO.getYwlx());
					EventVO event = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
					vo.setSjbh(event.getSjbh());
					BusinessUtil.setInsertCommonFields(vo,user);
					BaseDAO.insert(conn, vo);
				}else{
					String pkValue = this.getPkValue(conn,fkgxVO, jhsjid);
					vo.setGc_zsb_xmb_id(pkValue);
					BusinessUtil.setUpdateCommonFields(vo,user);
					BaseDAO.update(conn, vo);
				}
				LogManager.writeUserLog(fkqkVO.getSjbh(), fkqkVO.getYwlx(),
						Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "征收完成时间反馈成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
				throw e;
			} finally {
			}
			return domresult;
		}
}
