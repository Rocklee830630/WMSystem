package com.ccthanking.business.common_yw.ztbtjgl;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.common_yw.vo.ZtbtjglVO;
import com.ccthanking.business.xmcbk.vo.XmcbkVO;
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
public class ZtbtjglServiceImpl implements ZtbtjglService{
	
	private String ywlx=YwlxManager.ZTB_TJGL;
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
				condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
				conn.setAutoCommit(false);
				condition += orderFilter;
				page.setFilter(condition);
				String sql = "select A.GC_JH_SJ_ID jhsjid,nd,xmid,bdid,bdmc,xmmc,bdbh,xmbh,xmbs,pxh, "+
							"(select ZBTJZT as SGZT from GC_ZTB_TJGL C where ZBLX='13' and C.JHSJID=A.GC_JH_SJ_ID and c.sfyx=1) SGZT,"+
							"(select ZBTJZT as JLZT from GC_ZTB_TJGL C where ZBLX='12' and C.JHSJID=A.GC_JH_SJ_ID and c.sfyx=1) JLZT , "+
							"decode(A.xmbs, '1',  (select BDDD from GC_XMBD where GC_XMBD_ID = A.bdid and rownum = 1), (select XMDZ  from GC_TCJH_XMXDK where nd = A.nd  and GC_TCJH_XMXDK_ID = A.XMID and rownum = 1)) as XMDZ"+
							" from GC_JH_SJ A ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("SGZT", "ZBTJZT");
				bs.setFieldDic("JLZT", "ZBTJZT");
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
	 *维护招标条件状态
	 * 
	 */
	@Override
	public String insert(HttpServletRequest request,String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		ZtbtjglVO tjvo= new ZtbtjglVO(); 
		int  flag=Integer.parseInt(request.getParameter("flag"));
		String zt="",lx="";
		switch(flag)
		{
			case 1:
				zt="0";
				lx="12";
			break;
			case 2:
				zt="1";
				lx="12";
			break;
			case 3:
				zt="0";
				lx="13";
			break;
			case 4:
				zt="1";
				lx="13";
			break;
			
		}		
		try {
				conn.setAutoCommit(false);
				JSONArray list = tjvo.doInitJson(json);
				tjvo.setValueFromJson((JSONObject)list.get(0));
				String id=insert_update(tjvo.getJhsjid(),lx);
				if(Pub.empty(id))
				{
					tjvo.setGc_ztb_tjgl_id(new RandomGUID().toString()); //设置主键
					tjvo.setZbtjzt(zt);
					tjvo.setZblx(lx);
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
					tjvo.setGc_ztb_tjgl_id(id);
					tjvo.setZbtjzt(zt);
					tjvo.setZblx(lx);
					BaseDAO.update(conn, tjvo);
					resultVO = tjvo.getRowJson();		
					conn.commit();	
				}	
				LogManager.writeUserLog(tjvo.getSjbh(), tjvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "维护招标条件状态成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(tjvo.getSjbh(), tjvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "维护招标条件状态失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		String jsona="{querycondition: {conditions: [{\"value\":\""+tjvo.getJhsjid()+"\",\"fieldname\":\"Gc_JH_SJ_ID\",\"operation\":\"=\",\"logic\":\"and\"} ,{\"value\":\""+tjvo.getNd()+"\",\"fieldname\":\"A.nd\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
		String result=this.query(request,jsona,user);
		return result;
	}
	//判断插入还是更新
	public static String insert_update(String jhsjid,String lx){
		String sql="select GC_ZTB_TJGL_ID from GC_ZTB_TJGL where  sfyx=1 and zblx="+lx+ " and jhsjid='"+jhsjid+"'";
		String result[][] = DBUtil.query(sql);
		String id="";
		if(null!=result&&result.length>0&&!Pub.empty(result[0][0]))
		{					
			id=result[0][0];
			return  id;//更新
		}
		else
		{			
			return id;//插入
		}
	}
}
