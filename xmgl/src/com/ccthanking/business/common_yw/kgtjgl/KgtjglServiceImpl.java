package com.ccthanking.business.common_yw.kgtjgl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.common_yw.vo.KgtjglVO;
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
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class KgtjglServiceImpl implements KgtjglService{
	
	private String ywlx=YwlxManager.KG_TJGL;
	/*	
	 * 普通查询
	 * 
	 */
	@Override
	public String query(HttpServletRequest request,String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				condition += BusinessUtil.getSJYXCondition("a") + BusinessUtil.getCommonCondition(user,null);
				conn.setAutoCommit(false);
				condition += orderFilter;
				page.setFilter(condition);
				String sql = "select A.GC_JH_SJ_ID jhsjid,nd,a.xmid,bdid,bdmc,xmmc,bdbh,xmbh,xmbs,pxh,KGTJZT, GC_JH_TJGL_ID,C.TBJJG,"+
				"decode(A.xmbs, '1',  (select BDDD from GC_XMBD where GC_XMBD_ID = A.bdid and rownum = 1), "+
				"(select XMDZ  from GC_TCJH_XMXDK where nd = A.nd  and GC_TCJH_XMXDK_ID = A.XMID and rownum = 1)) as XMDZ "+
				"from GC_JH_SJ A  left join GC_JH_TJGL  C on c.jhsjid=a.gc_jh_sj_id";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("KGTJZT", "KGTJZT");
				bs.setFieldDic("TBJJG", "SF");
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询招标条件管理", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	/*	
	 *维护开工条件状态
	 * 
	 */
	@Override
	public String insert(HttpServletRequest request,String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		KgtjglVO tjvo= new KgtjglVO(); 
		String zt=request.getParameter("flag");	
		String nd=request.getParameter("nd");	
		try {
				conn.setAutoCommit(false);
				JSONArray list = tjvo.doInitJson(json);
				tjvo.setValueFromJson((JSONObject)list.get(0));
				if(Pub.empty(tjvo.getGc_jh_tjgl_id()))
				{
					tjvo.setGc_jh_tjgl_id(new RandomGUID().toString()); //设置主键
					tjvo.setKgtjzt(zt);
					BusinessUtil.setInsertCommonFields(tjvo, user);//公共字段插入
					tjvo.setYwlx(ywlx);
					EventVO eventVO = EventManager.createEvent(conn, tjvo.getYwlx(), user);//生成事件
					tjvo.setSjbh(eventVO.getSjbh());
					BaseDAO.insert(conn, tjvo);//插入
					resultVO = tjvo.getRowJson();		
					conn.commit();	
				}
				else
				{
					tjvo = (KgtjglVO)BaseDAO.getVOByPrimaryKey(conn,tjvo);
					tjvo.setKgtjzt(zt);
					BaseDAO.update(conn, tjvo);
					resultVO = tjvo.getRowJson();		
					conn.commit();	
				}	
				LogManager.writeUserLog(tjvo.getSjbh(), tjvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "维护开工条件状态成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(tjvo.getSjbh(), tjvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "维护开工条件状态失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+tjvo.getGc_jh_tjgl_id()+"\",\"fieldname\":\"Gc_jh_tjgl_id\",\"operation\":\"=\",\"logic\":\"and\"} ,{\"value\":\""+nd+"\",\"fieldname\":\"A.nd\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String result=this.query(request,jsona,user);
		return result;
	}
	/*	
	 *维护提报交竣工
	 * 
	 */
	@Override
	public String insert_tb(HttpServletRequest request,String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		KgtjglVO tjvo= new KgtjglVO(); 
		String nd=request.getParameter("nd");	
		try {
				conn.setAutoCommit(false);
				JSONArray list = tjvo.doInitJson(json);
				tjvo.setValueFromJson((JSONObject)list.get(0));
				if(Pub.empty(tjvo.getGc_jh_tjgl_id()))
				{
					tjvo.setGc_jh_tjgl_id(new RandomGUID().toString()); //设置主键
					tjvo.setTbjjg("1");
					BusinessUtil.setInsertCommonFields(tjvo, user);//公共字段插入
					tjvo.setYwlx(ywlx);
					EventVO eventVO = EventManager.createEvent(conn, tjvo.getYwlx(), user);//生成事件
					tjvo.setSjbh(eventVO.getSjbh());
					BaseDAO.insert(conn, tjvo);//插入
					resultVO = tjvo.getRowJson();		
					conn.commit();	
				}
				else
				{
					tjvo = (KgtjglVO)BaseDAO.getVOByPrimaryKey(conn,tjvo);
					tjvo.setTbjjg("1");
					BaseDAO.update(conn, tjvo);
					resultVO = tjvo.getRowJson();		
					conn.commit();	
				}	
				LogManager.writeUserLog(tjvo.getSjbh(), tjvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "维护提报交竣工成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(tjvo.getSjbh(), tjvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "维护提报交竣工失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+tjvo.getGc_jh_tjgl_id()+"\",\"fieldname\":\"Gc_jh_tjgl_id\",\"operation\":\"=\",\"logic\":\"and\"} ,{\"value\":\""+nd+"\",\"fieldname\":\"A.nd\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String result=this.query(request,jsona,user);
		return result;
	}
}
