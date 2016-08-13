package com.ccthanking.business.bgh.bgh;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.bgh.GcBghWtVO;
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
import com.ccthanking.framework.model.requestJson;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


@Service
public class BanGongHuiServiceImpl implements BanGongHuiService {

	private String ywlx=YwlxManager.GC_BGH;
	@Override
	public String querybanGongHuiHuiCi(HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String queryDictSql = "select id ID, parenid PARENT_ID, id DIC_VALUE, '0' HYSJ, '0' CHBM, '0' CHRY,'0' HYDD,'0' HYZC,'0' HYZT " +
				" from (select distinct (to_char(a.lrsj, 'yyyy')) id, '0' parenid  from (select * from  gc_bgh where sfyx='1') a) " +
				" union all " +
				" select a.gc_bgh_id, to_char(a.lrsj, 'yyyy') parenid, a.hc,to_char(HYSJ, 'yyyy-mm-dd HH24:MI:SS'),CHBM,CHRY,HYDD, HYZC, HYZT " +
				" from (select * from  gc_bgh where sfyx='1') a order by hysj ";
		JSONArray jsonArr = new JSONArray();
		try {
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json=new JSONObject();
				json.put("id",rsMap.get("ID"));
			    json.put("parentId", rsMap.get("PARENT_ID"));
			    json.put("name", rsMap.get("DIC_VALUE"));
			    json.put("hysj", rsMap.get("HYSJ"));
			    json.put("chbm", rsMap.get("CHBM"));
			    json.put("chry", rsMap.get("CHRY"));
			    json.put("hydd", rsMap.get("HYDD"));
			    json.put("hyzc", rsMap.get("HYZC"));
			    json.put("hyzt", rsMap.get("HYZT"));
			    Date d=new Date();//获取时间
			    SimpleDateFormat sam_date=new SimpleDateFormat("yyyy");		//转换格式
			    String today=sam_date.format(d);
			  //默认展开父节点 
				if(rsMap.get("ID").equals(today)){
					json.put("selectNode", "true");
					json.put("open", "true");
					
				}
			    jsonArr.add(json);
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonArr.toString();
	}
	@Override
	public String querybanGongHuiList(HttpServletRequest request, String json) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String sfgk = request.getParameter("sfgk");
		try {
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter =" order by xuhao, lrsj desc ";
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(sfgk)){
				if("0".equals(sfgk)){
					condition += " and SFGK='0' ";
				}else if("1".equals(sfgk)){
					condition += " and SFGK='1' ";
				}else if("all".equals(sfgk)){
				}
			}else{
				condition += " and SFGK='1' ";
			}
			condition += orderFilter;
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "select * from ( select a.gc_bgh_wt_id, a.gc_bgh_wt_id fj, a.bghid, a.jhsjid, a.xmid, a.bdid, a.wtbt, a.wtms, a.fqr, a.xwjjsj," +
					" a.zt, a.fqsj, a.shr,a.xuhao, a.shsj, a.shyj, a.bghjl, a.bghhf, a.zzbm, a.zzr, a.zzzxld, a.yqjjsj," +
					" a.dbcs, a.isjj, a.jjsj, a.ywlx, a.sjbh, a.bz, a.lrr, a.lrsj, a.lrbm, a.lrbmmc, " +
					"a.gxr, a.gxsj, a.gxbm, a.gxbmmc, a.sjmj, a.sfyx, a.wtlx,a.FQBM,a.SFGK," +
					"   case  when a.xwjjsj<a.jjsj then 1 else 0  end iscq , " +
					" case when a.dbcs>0 then 1 else 0  end isdu ," +
					"B.gc_bgh_id,to_char(b.HYSJ, 'yyyy-mm-dd HH24:MI:SS') HYSJ, B.hc, B.hydd, B.hyzc, B.chbm, B.hyzt, B.chry,S.XMMC,S.BDMC,A.WTID " +
					" from gc_bgh_wt a,gc_bgh b,(select * from GC_JH_SJ where sfyx='1') S where  a.bghid=b.gc_bgh_id and A.JHSJID=S.GC_JH_SJ_ID(+) and a.sfyx='1' and b.sfyx='1')  ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("XWJJSJ", "yyyy-MM-dd");
		/*	bs.setFieldDateFormat("HYSJ", "yyyy-MM-dd");*/
			bs.setFieldDateFormat("YQJJSJ", "yyyy-MM-dd");
			bs.setFieldDateFormat("JJSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("ZZR");
			bs.setFieldUserID("ZZZXLD");
			bs.setFieldDic("ISJJ", "SF");
			bs.setFieldDic("ZT", "BGHZT");
			bs.setFieldDic("ISCQ", "SF");
			bs.setFieldDic("SFGK", "SF");
			bs.setFieldDic("WTLX", "LX");
			bs.setFieldFileUpload("FJ", "0071");
			bs.setFieldTranslater("FQR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("LRBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("ZZBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("FQBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("SHR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String insertBanGongHui(String json, HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcBghVO xmvo = new GcBghVO();
		GcBghWtVO wtvo = new GcBghWtVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String ywid =request.getParameter("ywid");
		try {
			conn.setAutoCommit(false);
			JSONArray list = xmvo.doInitJson(json);
			xmvo.setValueFromJson((JSONObject)list.get(0));
			wtvo.setValueFromJson((JSONObject)list.get(0));
			BusinessUtil.setInsertCommonFields(xmvo, user);
			xmvo.setYwlx(ywlx);//业务类型
			EventVO eventVO = EventManager.createEvent(conn, xmvo.getYwlx(), user);//生成事件
			xmvo.setSjbh(eventVO.getSjbh());
			//设置主键
			xmvo.setGc_bgh_id(new RandomGUID().toString()); // 主键
			//插入
			BaseDAO.insert(conn, xmvo);
			resultVO = xmvo.getRowJson();
			String idCond = "'"+wtvo.getGc_bgh_wt_id().replaceAll(",","','")+"'";
			//修改议题会次和状态
			String sql="select gc_bgh_wt_id, bghid, jhsjid, xmid, bdid, FQBM,wtbt, wtms, fqr, xwjjsj, zt, fqsj, shr, shsj, shyj, bghjl, bghhf, zzbm, zzr, zzzxld, yqjjsj, dbcs, isjj, jjsj, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, wtlx " +
					"  from gc_bgh_wt where sfyx='1' and zt='1' and GC_BGH_WT_ID in ("+idCond+")";
			String[][] yts=DBUtil.query(sql);
			GcBghWtVO bghwt=new GcBghWtVO();
			if(null!=yts){
				for(int i=0;i<yts.length;i++){
					bghwt.setGc_bgh_wt_id(yts[i][0]);
					GcBghWtVO bghwt1=(GcBghWtVO) BaseDAO.getVOByPrimaryKey(conn, bghwt);
					bghwt1.setZt("2");
					bghwt1.setBghid(xmvo.getGc_bgh_id());
					BusinessUtil.setUpdateCommonFields(bghwt, user);
					BaseDAO.update(conn, bghwt1);
					LogManager.writeUserLog(bghwt1.getSjbh(), bghwt1.getYwlx(),
							Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
							user.getOrgDept().getDeptName() + " " + user.getName()
									+ "办公会问题修改成功", user,"","");
				}
			}
			conn.commit();
			LogManager.writeUserLog(xmvo.getSjbh(), xmvo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "办公会添加成功", user,"","");
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_bgh_id()+"\",\"fieldname\":\"GC_BGH_ID\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.querybanGongHuiList(request,jsona);
			return resultXinXi;
	}
	@Override
	public String updateBanGongHui(String json, HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		GcBghVO xmvo = new GcBghVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn.setAutoCommit(false);
		    JSONArray list = xmvo.doInitJson(json);
		    xmvo.setValueFromJson((JSONObject)list.get(0));
			//设置公共字段
			BusinessUtil.setUpdateCommonFields(xmvo, user);
	     	xmvo.setYwlx(ywlx);//业务类型
	     	//根据ID查YWID和SJBH
	     	GcBghVO vo1=(GcBghVO) BaseDAO.getVOByPrimaryKey(conn, xmvo);
			//修改 
			BaseDAO.update(conn, xmvo);
			resultVO = xmvo.getRowJson();
			conn.commit();
			LogManager.writeUserLog(vo1.getSjbh(), vo1.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "办公会修改成功", user,"","");
	
			} catch (Exception e) {
				DBUtil.rollbackConnetion(conn);
				e.printStackTrace(System.out);
			} finally {
				DBUtil.closeConnetion(conn);
			}
			String jsona="{querycondition: {conditions: [{\"value\":\""+xmvo.getGc_bgh_id()+"\",\"fieldname\":\"gc_bgh_id\",\"operation\":\"=\",\"logic\":\"and\"} ]}}";
			String resultXinXi=this.querybanGongHuiList(request,jsona);
			return resultXinXi;
	}
	@Override
	public String queryHyj( HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String bghid=request.getParameter("bghid");
		String domresult = "";
		try {
			PageManager page=new PageManager();
			String condition = "  SFGK='1' ";
			String orderFilter = " order by a.xuhao, a.lrsj desc ";
		    condition += BusinessUtil.getSJYXCondition("A") + BusinessUtil.getCommonCondition(user,null);
		    condition +=" and a.bghid=b.gc_bgh_id(+) and A.JHSJID=S.GC_JH_SJ_ID(+) and bghid='"+bghid+"' ";
		    condition += orderFilter;
		    page.setFilter(condition);
		    page.setPageRows(1000);
			conn.setAutoCommit(false);
			String sql = " select a.gc_bgh_wt_id, a.gc_bgh_wt_id fj, a.bghid, a.jhsjid, a.xmid, a.bdid, a.wtbt, a.wtms, a.fqr, a.xwjjsj," +
					" a.zt, a.fqsj, a.shr, a.shsj, a.shyj, a.bghjl, a.bghhf, a.zzbm, a.zzr, a.zzzxld, a.yqjjsj," +
					" a.dbcs, a.isjj, a.jjsj, a.ywlx, a.sjbh, a.bz,a.FQBM, a.lrr, a.lrsj, a.lrbm, a.lrbmmc, " +
					"a.gxr, a.gxsj, a.gxbm, a.gxbmmc, a.sjmj, a.sfyx, a.wtlx ,gc_bgh_id, to_char(HYSJ, 'yyyy-mm-dd HH24:MI:SS') HYSJ, hc, hydd, hyzc,chbm, hyzt, chry," +
					" s.xmmc,s.bdmc, case  when a.xwjjsj<a.jjsj then 1 else 0  end iscq  " +
					" from gc_bgh_wt a,(select * from gc_bgh b where sfyx='1') b,(select * from GC_JH_SJ where sfyx='1') S  ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ISJJ", "SF");
			bs.setFieldDic("ZT", "BGHZT");
			bs.setFieldFileUpload("FJ", "0071");
			bs.setFieldDic("ISCQ", "SF");
			bs.setFieldTranslater("FQR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("ZZR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("LRBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("ZZBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("ZZZXLD", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("FQBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("SHR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryZXHC(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String bghid=request.getParameter("bghid");
		String domresult = "";
		String sql=" ";
		try {
			PageManager page=null;
			conn.setAutoCommit(false);
			if(Pub.empty(bghid)){
				 sql = "  select gc_bgh_id, to_char(HYSJ, 'yyyy-mm-dd HH24:MI:SS') HYSJ, hc, hydd, hyzc, chbm, hyzt, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, chry" +
						" from  gc_bgh b,(select WT.bghid,count(WT.GC_BGH_WT_ID) wtsl from GC_BGH_WT WT where WT.SFYX='1' and WT.SFGK='1' group by WT.bghid) w " +
						" where b.sfyx='1' and lrsj=( select MAX(B.LRSJ) from GC_BGH b where b.sfyx='1' )" +
						" and b.GC_BGH_ID=w.bghid and w.wtsl>0" +
						" order by  lrsj desc  ";
			}else{
				 sql = "  select gc_bgh_id, to_char(HYSJ, 'yyyy-mm-dd HH24:MI:SS') HYSJ, hc, hydd, hyzc, chbm, hyzt, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, chry" +
							" from  gc_bgh b,(select WT.bghid,count(WT.GC_BGH_WT_ID) wtsl from GC_BGH_WT WT where WT.SFYX='1' and WT.SFGK='1' group by WT.bghid) w " +
							" where b.sfyx='1' and gc_bgh_id='"+bghid+"' " +
							" and b.GC_BGH_ID=w.bghid and w.wtsl>0" +
							" order by  lrsj desc ";
			}
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
	public String queryBGHWT(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		String sql=" ";
		try {
			PageManager page=null;
			conn.setAutoCommit(false);
				 sql = "  select a.gc_bgh_wt_id,s.xmmc,s.bdmc, a.gc_bgh_wt_id fj, a.bghid, a.jhsjid, a.xmid, a.bdid, a.wtbt, a.wtms, a.fqr, a.xwjjsj," +
					" a.zt, a.fqsj, a.shr, a.shsj, a.shyj, a.bghjl, a.bghhf,a.FQBM, a.zzbm, a.zzr, a.zzzxld, a.yqjjsj," +
					" a.dbcs, a.isjj, a.jjsj, a.ywlx, a.sjbh, a.bz, a.lrr, a.lrsj, a.lrbm, a.lrbmmc, " +
					"a.gxr, a.gxsj, a.gxbm, a.gxbmmc, a.sjmj, a.sfyx, a.wtlx," +
					"B.gc_bgh_id,to_char(b.HYSJ, 'yyyy-mm-dd HH24:MI:SS') HYSJ, B.hc, B.hydd, B.hyzc, B.chbm, B.hyzt, B.chry " +
					" from gc_bgh_wt a,(select * from gc_bgh where sfyx='1') b,(select * from GC_JH_SJ where sfyx='1') S where  a.bghid=b.gc_bgh_id(+) and A.JHSJID=S.GC_JH_SJ_ID(+)   and a.sfyx='1' and a.zt='1' " +
					"order by xuhao asc, lrsj desc  ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ISJJ", "SF");
			bs.setFieldDic("ZT", "BGHZT");
			bs.setFieldDic("ISCQ", "SF");
			bs.setFieldDic("WTLX", "LX");
			bs.setFieldFileUpload("FJ", "0071");
			bs.setFieldTranslater("FQR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("SHR", "FS_ORG_PERSON", "ACCOUNT", "NAME");
			bs.setFieldTranslater("LRBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("FQBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			bs.setFieldTranslater("ZZBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	
	@Override
	public String delete(HttpServletRequest request) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			String gc_bgh_id = request.getParameter("gc_bgh_id");
			
			String updateBghSql = "update gc_bgh set SFYX='0' where gc_bgh_id='"+gc_bgh_id+"'";
			String updateGcBghWtSql = "update GC_BGH_WT set ZT='1' where BGHID='"+gc_bgh_id+"'";
			
			DBUtil.execUpdateSql(conn, updateBghSql);
			DBUtil.execUpdateSql(conn, updateGcBghWtSql);
			
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
			return "0";
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return "1";
	}
	@Override
	public String queryDshyt(HttpServletRequest request, String json) throws Exception{
		// TODO Auto-generated method stub
		String res = "";
		Connection conn = null;
		try{
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			roleCond = " and  ZT='1'";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("A");
			condition +=orderFilter;
			page.setFilter(condition);
			String sql = "  select gc_bgh_wt_id,WTBT,ZT,WTLX from GC_BGH_WT A";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ZT", "BGHZT");
			bs.setFieldDic("WTLX", "LX");
			res = bs.getJson();
		}catch(Exception e){
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		}finally{
			DBUtil.closeConnetion(conn);
		}
		return res;
	}
}


