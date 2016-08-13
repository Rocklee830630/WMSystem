package com.ccthanking.business.xmcbk.xmwh;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

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
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class XmcbkwhServiceImpl implements XmcbkwhService{
	
	private String ywlx=YwlxManager.GC_XM_CBK;
	/*	
	 * 普通查询
	 * 
	 */
	@Override
	public String query_cbk(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "select  gc_tcjh_xmcbk_id,xdlx, XMBH, pcid,xmmc, qy, nd, xmlx, xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt,ztztze, ztgc, ztzc, ztqt, xmsx, isbt, pch, isxd, ywlx, sjbh, bz,lrsj,sfyx,XMBM,ZRBM,XMFR,ISNRTJ,WCZTZE,WCGC,WCZC,WCQT,NDMB,ISAMBWC from GC_TCJH_XMCBK ";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldThousand("GC");
				bs.setFieldThousand("ZC");
				bs.setFieldThousand("QT");
				bs.setFieldThousand("JHZTZE");
				bs.setFieldThousand("ZTGC");
				bs.setFieldThousand("ZTZC");
				bs.setFieldThousand("ZTQT");
				bs.setFieldThousand("ZTZTZE");
				bs.setFieldThousand("WCZTZE");
				bs.setFieldThousand("WCGC");
				bs.setFieldThousand("WCZC");
				bs.setFieldThousand("WCQT");
				bs.setFieldDic("XMLX", "XMLX");
				bs.setFieldDic("ND","XMNF");
				bs.setFieldDic("XMSX", "XMSX");
				bs.setFieldDic("ZRBM", "ZRBM");
				bs.setFieldDic("XMFR", "XMFR");
				bs.setFieldDic("ISXD","XMZT");
				bs.setFieldDic("ISNRTJ","SF");
				bs.setFieldDic("ISBT", "SF");
				bs.setFieldDic("SFYX", "SF");
				bs.setFieldDic("QY","QY");
				bs.setFieldDic("PCH","YJPCH");
				bs.setFieldDic("XDLX","XDLX");
				bs.setFieldDic("NDMB","NDMB");
				bs.setFieldDic("ISAMBWC","SF");
				domresult = bs.getJson();
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询项目储备库", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	/*	
	 * 下达日期
	 * 
	 */
	@Override
	public String query_xdrq(String json,User user,String pcid) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				conn.setAutoCommit(false);
				String sql = "select xdrq from GC_CBK_PCB  where GC_CBK_PCB_ID='"+pcid+"'";
				String result[][] = DBUtil.query(conn, sql);
				if(null!=result&&result.length>0)
				{					
					domresult = result[0][0].substring(0, 10);
				}	
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	/*	
	 * 新增时获取项目编号
	 * 
	 */
	@Override
	public String query_xmbh(String json,User user,String year) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {		
			 domresult=year+getnumber("GC_TCJH_XMCBK",3,year).substring(4, 7);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	/*	
	 * 校验项目编号
	 * 
	 */
	@Override
	public String query_xmbh_jy(String json,User user,String year,String xmbh,String id) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "2";
		try {		
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			conn.setAutoCommit(false);
			String sql="";
			if("0".equals(id))
			{
				sql = "select xmbh from GC_TCJH_XMCBK  where xmbh='"+xmbh+"' and nd='"+year+"' and sfyx = '1'";				
			}
			else
			{
				sql = "select xmbh from GC_TCJH_XMCBK  where xmbh='"+xmbh+"' and nd='"+year+"'  and sfyx = '1' and GC_TCJH_XMCBK_ID <> '"+id+"'";			
			}	
			String result[][] = DBUtil.query(conn, sql);
			if(null!=result&&result.length>0)
			{					
				domresult ="1";
			}	
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
		
	/*	
	 * 批次查询
	 * 
	 */
	@Override
	public String query_xmpc(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {	
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition+="  and cbk.pcid = pcb.GC_CBK_PCB_ID and pcb.sfyx='1'";
			condition += BusinessUtil.getSJYXCondition("cbk") + BusinessUtil.getCommonCondition(user,null);
			condition += orderFilter;
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select gc_tcjh_xmcbk_id, pcid,xmbh,cbk.xdlx, xmmc, qy, nd, xmlx, xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt, xmsx, isbt, cbk.pch, pcb.isxd,pxh,  cbk.bz,cbk.lrsj,cbk.sfyx ,GC_CBK_PCB_ID,cbk.XMBM,cbk.ZRBM,cbk.XMFR  from GC_TCJH_XMCBK cbk, GC_CBK_PCB pcb";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX", "XMLX");
			bs.setFieldDic("ND","XMNF");
			bs.setFieldDic("XMSX", "XMSX");
			bs.setFieldDic("ISXD","XMZT");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("ZRBM", "ZRBM");
			bs.setFieldDic("XMFR", "XMFR");
			bs.setFieldDic("SFYX", "SF");
			bs.setFieldThousand("GC");
			bs.setFieldThousand("ZC");
			bs.setFieldThousand("QT");
			bs.setFieldDic("QY","QY");
			bs.setFieldThousand("JHZTZE");

			domresult = bs.getJson();
			LogManager.writeUserLog(null, ywlx,
					Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "查询项目批次", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

		
	/*	
	 * 查询出最新pch
	 * 
	 */
	@Override
	public String query_newpch(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			String sql="select pch from GC_CBK_PCB where sfyx=1 and isxd='1' order by lrsj desc";
			String result[][] = DBUtil.query(conn, sql);
			if(null!=result&&result.length>0&&!Pub.empty(result[0][0]))
			{
				domresult=result[0][0];
			}
			else
			{
				domresult="flag";
			}	
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	
	/*	
	 * 详细信息查询
	 * 
	 */
	@Override
	public String query_cbkxx(String json,User user,String id) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {	
				PageManager page = RequestUtil.getPageManager(json);
				conn.setAutoCommit(false);
				String sql = "select gc_tcjh_xmcbk_id, xdlx,xmbh, xmmc, qy, nd, xmlx, xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt,ztztze, ztgc, ztzc, ztqt, xmsx, isbt, pch, isxd, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx,xmbm,ZRBM,XMFR,ISNRTJ from gc_tcjh_xmcbk where gc_tcjh_xmcbk_id='"+id+"'";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("XMLX", "XMLX");
				bs.setFieldDic("ND","XMNF");
				bs.setFieldDic("XMSX", "XMSX");
				bs.setFieldDic("ISXD","XMZT");
				bs.setFieldDic("ISBT", "SF");
				bs.setFieldDic("SFYX", "SF");
				bs.setFieldThousand("GC");
				bs.setFieldThousand("ZC");
				bs.setFieldThousand("QT");
				bs.setFieldDic("QY","QY");
				bs.setFieldThousand("ZTZTZE");
				bs.setFieldThousand("ZTGC");
				bs.setFieldThousand("ZTZC");
				bs.setFieldThousand("ZTQT");
				bs.setFieldThousand("ZTZTZE");
				bs.setFieldDic("ZRBM", "ZRBM");
				bs.setFieldDic("XMFR", "XMFR");
				bs.setFieldDic("ISNRTJ","SF");
				domresult = bs.getJson();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}


	/*	
	 * 通用详细信息查询
	 * 
	 */
	@Override
	public String query_cbkxx_ty(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {	
				PageManager page = RequestUtil.getPageManager(json);
				String orderFilter = RequestUtil.getOrderFilter(json);
				String condition = RequestUtil.getConditionList(json).getConditionWhere();
				condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user,null);
				condition += orderFilter;
				page.setFilter(condition);
				conn.setAutoCommit(false);
				String sql = "select gc_tcjh_xmcbk_id, xmbh, xmmc, qy, nd, xmlx, xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt,ztztze, ztgc, ztzc, ztqt, xmsx, isbt, pch, isxd, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx,XMBM from gc_tcjh_xmcbk";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldDic("XMLX", "XMLX");
				bs.setFieldDic("ND","XMNF");
				bs.setFieldDic("XMSX", "XMSX");
				bs.setFieldDic("ISXD","XMZT");
				bs.setFieldDic("ISBT", "SF");
				bs.setFieldDic("SFYX", "SF");
				bs.setFieldThousand("GC");
				bs.setFieldThousand("ZC");
				bs.setFieldThousand("QT");
				bs.setFieldDic("QY","QY");
				bs.setFieldThousand("JHZTZE");
				domresult = bs.getJson();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);	
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	
	/*	
	 * 维护新增
	 * 
	 */
	@Override
	public String insert_cbk(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		XmcbkVO xmvo = new XmcbkVO();
		try {
				conn.setAutoCommit(false);
				JSONArray list = xmvo.doInitJson(json);
				xmvo.setValueFromJson((JSONObject)list.get(0));
				xmvo.setGc_tcjh_xmcbk_id(new RandomGUID().toString()); //设置主键
				BusinessUtil.setInsertCommonFields(xmvo, user);//公共字段插入
				xmvo.setYwlx(ywlx);
				//String xmbh=xmvo.getNd() + getLshOnYearMonth("GC_TCJH_XMCBK",3);
				//String xmbh=xmvo.getNd()+getnumber("GC_TCJH_XMCBK",3," and nd="+xmvo.getNd());
				//xmvo.setXmbh(xmbh);
				String bt = xmvo.getIsbt();
				if(bt==null||bt.equals(""))
				{
					xmvo.setIsbt("0");
				}	
				EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
				xmvo.setSjbh(eventVO.getSjbh());
				BaseDAO.insert(conn, xmvo);//插入
				resultVO = xmvo.getRowJson();		
				conn.commit();
		
		
				LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "添加储备库项目成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "添加储备库项目失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	/*	
	 * 维护修改
	 * 
	 */
	@Override
	public String update_ckb(String json,User user,String id) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		XmcbkVO xmvo = new XmcbkVO();

		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			xmvo.setGc_tcjh_xmcbk_id(id); //设置主键
			String bt = xmvo.getIsbt();
			if(bt==null||bt.equals(""))
			{
				xmvo.setIsbt("0");
			}	
			BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新
			BaseDAO.update(conn, xmvo);
			Pub.doSynchronousXmbm(conn, id, "1", xmvo.getXmbm());
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改储备库项目成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改储备库项目失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	//更新排序号信息
	@Override
	public String update_pxh(String json,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		
		String resultVO = "";
		XmcbkVO xmvo =null;
		try {
				conn.setAutoCommit(false);
				XmcbkVO xmvou = new XmcbkVO();
				JSONArray list = xmvou.doInitJson(json);
				List<String> listrow= new ArrayList<String>(); 		
				for(int i=0;i<list.size();i++)
				{
				
					xmvo= new XmcbkVO();
					xmvo.setValueFromJson((JSONObject)list.get(i));	
					BusinessUtil.setUpdateCommonFields(xmvo, user);//公共字段更新	
					//返回的记录存入listrow
					listrow.add(xmvo.getRowJsonSingle());
					//更新操作
					BaseDAO.update(conn, xmvo);
					conn.commit();
					LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
							Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
							user.getOrgDept().getDeptName() + " " + user.getName()
									+ "修改排序号成功", user,"","");

				}
				resultVO = BaseDAO.comprisesResponseData(conn, listrow);
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "修改排序号失败", user,"","");

		} finally {
			if (conn != null) {
				DBUtil.closeConnetion(conn);
			}
		}
		return resultVO;
	}

	
	//生成项目编号
	public static String getnumber(String tablename,int num,String tj){
		String result[][] = DBUtil.query("select max(xmbh) + 1 from "+tablename+"  where  nd="+tj);
		String id=null;
		if(null!=result&&result.length>0&&!Pub.empty(result[0][0]))
		{					
			id = result[0][0];
			int len = id.length();
			if(0 != num && num > len){
				for (int i = 0; i < num - len; i++) {
					id = "0" + id;
				}
			}
		}
		else
		{
			id=tj+"001";
		}
		return id;
	}

	
	//删除项目信息
	@Override
	public String delete(String json, User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		XmcbkVO xmvo = new XmcbkVO(); 
		try {
		conn.setAutoCommit(false);
		JSONArray list = xmvo.doInitJson(json);
		xmvo.setValueFromJson((JSONObject)list.get(0));
		xmvo = (XmcbkVO)BaseDAO.getVOByPrimaryKey(conn,xmvo);
		xmvo.setSfyx("0");
		BaseDAO.update(conn,xmvo);
		conn.commit();
		LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
				Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_SUCCESS,
				user.getOrgDept().getDeptName() + " " + user.getName()
						+ "删除项目信息成功", user,"","");

		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_UPDATE, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "删除项目信息失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}
	

	
	/* 
	 * 根据输入值从储备库数据表自动模糊匹配项目名称
	 * @see com.ccthanking.business.tcjh.jhgl.service.TcjhService#xmmcAutoComplete(com.ccthanking.framework.model.autocomplete, com.ccthanking.framework.common.User)
	 */
	@Override
	public List<autocomplete> xmmcAutoComplete(autocomplete json,User user) throws Exception {
		List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
		autocomplete ac = new autocomplete();
		String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition(null);
		condition += BusinessUtil.getCommonCondition(user,null);
		String [][] result = DBUtil.query("select distinct xmmc from GC_TCJH_XMCBK " + json.getTablePrefix() + " where " + condition);
        if(null != result&&result.length>0){
        	for(int i =0;i<result.length;i++){
        	  ac = new autocomplete();
              ac.setRegionName(result[i][0]);
              autoResult.add(ac);
        	}
        }
		return autoResult;
	}	
	
	/*	
	 * 储备库跟踪
	 * sjl 2013-12-17
	 */
	@Override
	public String queryCbkGz(String json,HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try{
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			condition += "  AND T1.SFYX ='1' AND T2.SFYX ='1' ";
	        condition += BusinessUtil.getCommonCondition(user,"T1");
	        condition += orderFilter;
	        String nd=request.getParameter("nd");
	        String ndtj="";
	        if(!Pub.empty(nd))
	        {
	        	ndtj=" and nd='"+nd+"' ";
	        }	
			if (page == null)
				page = new PageManager();
				page.setFilter(condition);
				
				conn.setAutoCommit(false);
				String sql = "";
				sql = "SELECT "
						+"t1.isnatc,T1.XMBH,T1.XMMC,T1.GC_TCJH_XMXDK_ID XMID,T1.XMDZ,T1.JSNR,T1.JSYY,T1.XMLX,T2.GC AS GC_JH,T2.ZC AS ZC_JH,T2.QT AS QT_JH,T2.JHZTZE AS JHZTZR_JH,T1.XMFR,T.GC_JH_SJ_ID AS KYMC,T2.XMBM," +
						"T2.PCH,(SELECT XDRQ FROM GC_CBK_PCB WHERE GC_CBK_PCB_ID = T2.PCID)AS XDRQ,T1.XMSX,T1.XMGLGS,''AS XMZT,T1.GC AS GC_WC,T1.ZC AS ZC_WC,T1.QT AS QT_WC,T1.JHZTZE AS JHZTZE_WC," +
						"t.kgsj,t.kgsj_sj,t.wgsj,t.wgsj_sj,t.kypf,t.kypf_sj,t.hpjds,t.hpjds_sj,t.gcxkz,t.gcxkz_sj,t.sgxk,t.sgxk_sj,t.cbsjpf,t.cbsjpf_sj,t.cqt,t.cqt_sj,t.pqt,t.pqt_sj,t.sgt,t.sgt_sj,t.tbj,t.tbj_sj,t.cs,t.cs_sj,t.jldw,t.jldw_sj,t.sgdw,t.sgdw_sj,t.zc,t.zc_sj,t.pq,t.pq_sj,t.jg,t.jg_sj"+
						"  FROM GC_TCJH_XMXDK T1  left join GC_TCJH_XMCBK T2 on T1.XMCBK_ID = T2.GC_TCJH_XMCBK_ID left join (select * from gc_jh_sj where xmbs=0  "+ndtj+" and sfyx=1) t on t1.gc_tcjh_xmxdk_id=t.xmid";
				BaseResultSet bs = DBUtil.query(conn, sql, page);
				bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
				//bs.setFieldTranslater("KYMC", "gc_qqsx_sxfj", "JHSJID", "FJLX");//项目管理公司
				bs.setFieldDic("XMXZ", "XMXZ");
				bs.setFieldDic("PCH", "YJPCH");
				bs.setFieldDic("XMLX", "XMLX");
				bs.setFieldDic("XMSX", "XMSX");
				bs.setFieldThousand("GC_JH");
				bs.setFieldThousand("ZC_JH");
				bs.setFieldThousand("QT_JH");
				bs.setFieldThousand("JHZTZR_JH");
				bs.setFieldThousand("GC_WC");
				bs.setFieldThousand("ZC_WC");
				bs.setFieldThousand("QT_WC");
				bs.setFieldThousand("JHZTZE_WC");
				bs.setFieldDateFormat("KGSJ_SJ", "yyyy-MM-dd");
				bs.setFieldDateFormat("WGSJ_SJ", "yyyy-MM-dd");
				domresult = bs.getJson();
				//===============================================
				if(!"0".equals(domresult)){
					String newjsonString = "";
					newjsonString  = "{\"response\":";
					newjsonString +="{\"data\":[";
					JSONObject jsono = JSONObject.fromObject(domresult);
					JSONObject response = (JSONObject) jsono.get("response");
					JSONArray data1 = (JSONArray) response.get("data");
					Iterator iter = data1.iterator();
					JSONObject data = null;
					String str = "";
					int i = 0;
					while (iter.hasNext())
					{
						data = (JSONObject) iter.next();
						String JHSJID = data.getString("KYMC");
						String processdetail = "SELECT WHMC FROM gc_qqsx_sxfj WHERE FJLX ='2024' AND SFYX ='1' AND JHSJID='"+JHSJID+"'";
						QuerySet qs = DBUtil.executeQuery(processdetail, null,conn);
						if(qs.getRowCount()>0){
							for(int k=0;k<qs.getRowCount();k++){
								String kymc = qs.getString(k+1,"WHMC");
								if(!Pub.empty(kymc)){
									str +=kymc+"|";
								}else{
									kymc = "";
								}
							}
						}
						
						if(str.length()>0){
							str = str.substring(0,str.length()-1);
						}
						data.element("KYMC", str);
						str = "";
						data1.remove(i);
						data1.add(i, data);
						i++;
					}
					domresult =jsono.toString();// BaseDAO.EncodeJsString( jsono.toString());
				}
				
				
				
				LogManager.writeUserLog(null, ywlx,
						Globals.OPERATION_TYPE_QUERY, LogManager.RESULT_SUCCESS,
						user.getOrgDept().getDeptName() + " " + user.getName()
								+ "查询<储备库跟踪>成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
}
