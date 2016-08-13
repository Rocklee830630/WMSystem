package com.ccthanking.business.cjjh;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

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
import com.ccthanking.framework.model.autocomplete;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class GcCjjhServiceImpl implements GcCjjhService {

	private String ywlx=YwlxManager.GC_CJJH;
	@Override
	public String query(HttpServletRequest request,String json) {
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			/**
			conn = DBUtil.getConnection();
			String sql = "select gc_cjjh_id, jwxm, wbxm, xh, mxs, zxsm, xmmc, xmjswz, xmjsnrgm, jh_gc, jh_zq, jh_qt, jh_xj, xmjsjd, nd_gc, nd_zq, nd_qt, nd_xj, nd_jsmb, zrbm, xmfr, xmlx, parentid, NODELEVEL from gc_cjjh order by to_number(gc_cjjh_id)";
			
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, sql);
			
			for(Map<String, String> map : rsList) {
				JSONObject jsonObj = JSONObject.fromObject(map);
				array.add(jsonObj);
			}
			*/
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			roleCond = " and  C.XMID = B.GC_TCJH_XMCBK_ID(+) AND C.GC_CJJH_ID = A.JD(+)  and c.PXH_SJ is not null ";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("C") + BusinessUtil.getCommonCondition(user,null);
			condition +=orderFilter;
			page.setFilter(condition);
			String sql ="SELECT C.GC_CJJH_ID, " +
					" C.ND, " +
					" C.JWXM, " +
					" C.WBXM, " +
					" C.XH, " +
					" C.MXS, " +
					" C.ZXSM, c.XMXZ, " +
					" C.XMMC, " +
					" B.XMDZ XMJSWZ, " +
					" B.JSNR XMJSNRGM, " +
					" NVL(DECODE(A.JD, NULL, B.ZTGC, A.ZTGC), 0) JH_GC, " +
					" NVL(DECODE(A.JD, NULL, B.ZTZC, A.ZTZC), 0) JH_ZQ, " +
					" NVL(DECODE(A.JD, NULL, B.ZTQT, A.ZTQT), 0) JH_QT, " +
					" NVL(DECODE(A.JD, NULL, B.ZTZTZE, A.ZTZTZE), 0) JH_XJ, " +
					" NVL(DECODE(A.JD, NULL, B.GC, A.GC), 0) ND_GC, " +
					" NVL(DECODE(A.JD, NULL, B.ZC, A.ZC), 0) ND_ZQ, " +
					" NVL(DECODE(A.JD, NULL, B.QT, A.QT), 0) ND_QT, " +
					" NVL(DECODE(A.JD, NULL, B.JHZTZE, A.JHZTZE), 0) ND_XJ, " +
					" C.XMJSJD, " +
					" B.Ndmb, " +
					" B.ZRBM, " +
					" B.XMFR, " +
					" B.XMLX, " +
					" PARENTID, " +
					" NODELEVEL " +
					" FROM (SELECT NVL(SUM(B.JHZTZE), 0) JHZTZE, " +
					" NVL(SUM(B.GC), 0) GC, " +
					" NVL(SUM(B.ZC), 0) ZC, " +
					" NVL(SUM(B.QT), 0) QT, " +
					" NVL(SUM(B.ZTGC), 0) ZTGC, " +
					" NVL(SUM(B.ZTZTZE), 0) ZTZTZE, " +
					" NVL(SUM(B.ZTZC), 0) ZTZC, " +
					" NVL(SUM(B.ZTQT), 0) ZTQT, " +
					" JD " +
					" FROM (SELECT A.*, " +
					" LEVEL, " +
					" CONNECT_BY_ISLEAF, " +
					" CONNECT_BY_ROOT GC_CJJH_ID JD " +
					" FROM GC_CJJH A " +
					" WHERE A.ISXM = '1' and SFYX='1' " +
					" START WITH A.GC_CJJH_ID IN " +
					" (SELECT A.GC_CJJH_ID " +
					" FROM GC_CJJH A " +
					" WHERE A.ISXM = '0') " +
					" CONNECT BY PRIOR A.GC_CJJH_ID = A.PARENTID) A, " +
					" GC_TCJH_XMCBK B " +
					" WHERE A.XMID = B.GC_TCJH_XMCBK_ID(+) " +
					" GROUP BY JD) A, " +
					" GC_CJJH C, " +
					" GC_TCJH_XMCBK B ";
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");		
			bs.setFieldThousand("JH_XJ");	//计划总投资-小计
			bs.setFieldThousand("JH_GC");	//计划总投资-工程
			bs.setFieldThousand("JH_ZQ");	//计划总投资-征地排迁
			bs.setFieldThousand("JH_QT");	//计划总投资-其他
			bs.setFieldThousand("ND_GC");	//年度计划投资-工程
			bs.setFieldThousand("ND_ZQ");	//年度计划投资-工程
			bs.setFieldThousand("ND_QT");	//年度计划投资-工程
			bs.setFieldThousand("ND_XJ");	//年度计划投资-工程
			bs.setFieldThousand("ND_XJ");	//年度计划投资-工程
			bs.setFieldDic("ZRBM", "ZRBM");
			bs.setFieldDic("NDMB", "NDMB");
			bs.setFieldDic("XMFR", "XMFR");
			bs.setFieldDic("XMJSJD", "XMJSJD");
			bs.setFieldDic("XMXZ", "XMXZ");
			
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	

	@Override
	public String getProjectfl(HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String ISXM =request.getParameter("ISXM");
		String sqlIsxm=" ";
		if(!Pub.empty(ISXM)){
			sqlIsxm=" and ISXM ='"+ISXM+"' ";
		}
		String nd=request.getParameter("nd");
		String queryDictSql =  "SELECT T.GC_CJJH_ID, ND, XMSX,T.PARENTID,ISXM, T.XMMC || '(' || DECODE(T.XMS, NULL, 0, T.XMS) || ')' XMMC,NODELEVEL, XMS, PXH_SJ" +
							   " FROM (SELECT T.GC_CJJH_ID, " +
							   "ND, " +
						       "T.PARENTID, " +
						       "T.XMMC, " +
						       "T.NODELEVEL, " +
						       "NVL((SELECT COUNT(*) PARENTID " +
						          "FROM GC_CJJH B " +
						         "WHERE B.PARENTID = T.GC_CJJH_ID " +
						          " AND SFYX = '1' " +
						          " AND B.ISXM = '1' " +
						        " GROUP BY PARENTID),'0') XMS,PXH_SJ,XMSX,isxm " +
						  "FROM GC_CJJH T " +
						 "WHERE SFYX = '1'and nd='"+nd+"' "+ sqlIsxm +") t ORDER BY PXH_SJ ";
		JSONArray jsonArr = new JSONArray();
		String openID = request.getParameter("id");
		try {
			String defaultList[][] = null;
			if(!Pub.empty(openID)){
				String queryDefaultSql = "select GC_CJJH_ID from GC_CJJH a  start with GC_CJJH_ID='"+openID+"' connect by GC_CJJH_ID = prior PARENTID ";
				defaultList = DBUtil.query(conn, queryDefaultSql); 
			}
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json = new JSONObject();
				json.put("id",rsMap.get("GC_CJJH_ID"));
			    json.put("parentId",rsMap.get("PARENTID"));
			    json.put("name", rsMap.get("XMMC"));
			    json.put("level", rsMap.get("NODELEVEL"));
			    json.put("nd", rsMap.get("ND"));
			    json.put("xmsx", rsMap.get("XMSX"));
			    json.put("isxm", rsMap.get("ISXM"));
			    if(("0".equals(rsMap.get("NODELEVEL"))||"1".equals(rsMap.get("NODELEVEL"))||"2".equals(rsMap.get("NODELEVEL")))){
			    	//打开本年的根节点
			    	json.put("open", "true");
			    }
			    if(defaultList!=null){
			    	for(int x=0;x<defaultList.length;x++){
			    		if(defaultList[x][0].equals(rsMap.get("GC_CJJH_ID"))){
			    			json.put("open", "true");
			    		}
			    	}
			    }
			    jsonArr.add(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return jsonArr.toString();
	}
	@Override
	public String add(String json, User user) {
		Connection conn = DBUtil.getConnection();
		String resultVo = null;
		try {
			GcCjjhVO gcCjjhVO = new GcCjjhVO();
			conn.setAutoCommit(false);
			
			JSONArray list = gcCjjhVO.doInitJson(json);
			JSONObject jsonObj = (JSONObject)list.get(0);
			int maxOrderNo = getNewPkValue(conn);
			jsonObj.put("GC_CJJH_ID", maxOrderNo);
			
			gcCjjhVO.setValueFromJson(jsonObj);
			BusinessUtil.setInsertCommonFields(gcCjjhVO, user);
			BaseDAO.insert(conn, gcCjjhVO);
			resultVo = gcCjjhVO.getRowJson();
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVo;
	}
	private int getNewPkValue(Connection conn){
		String queryOrderNoSql = "select max(to_number(GC_CJJH_ID)) maxOrderNo from GC_CJJH ";
		String[][] orderNo = DBUtil.query(conn, queryOrderNoSql);
		int maxOrderNo = Integer.parseInt(orderNo == null ? "1" : orderNo[0][0]) + 1;
		return maxOrderNo;
	}

	@Override
	public String update(String json, User user) {
		return null;
	}
	@Override
	public String queryNodeList(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			roleCond = " ";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("A") + BusinessUtil.getCommonCondition(user,null);
			 if(!Pub.empty(condition)){
					condition +=" and  a.xmid=b.gc_tcjh_xmcbk_id(+)  ";
					
				}else{
					condition="   a.xmid=b.gc_tcjh_xmcbk_id (+)";
				}
			condition +=orderFilter;
			page.setFilter(condition);
			String sql = " select a.gc_cjjh_id, a. jwxm, a. wbxm, a. xh, a. mxs, a. zxsm, a. xmmc, a. xmjswz, a. xmjsnrgm, a. jh_gc, a. jh_zq, a. jh_qt, " +
						 " a.jh_xj, a. xmjsjd, a. nd_gc, a. nd_zq, a. nd_qt, a. nd_xj, a. nd_jsmb, a. zrbm, a. xmfr, a. xmlx, a. parentid, a. nodelevel," +
						 "(select XMMC from GC_CJJH where GC_CJJH_ID=a.PARENTID and rownum=1) PARENTNAME," +
						 " a.sfyx, a. xmid, a. nd , a. pxh, a. pxh_sj, a.xmsx, a. isxm,b.xmbh,b.xmdz,a.DXZX ,a.XMXZ,a.TZLX, a.SSXZ from gc_cjjh a,  gc_tcjh_xmcbk b ";
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldDic("ISXM", "SF");
			bs.setFieldDic("DXZX", "SF");
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String getNodeDetail(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		String treeNodeID = request.getParameter("id");
		try {
			domresult = Pub.makeQueryConditionByID(treeNodeID, "A.GC_CJJH_ID");
			domresult = this.queryNodeList(request, domresult);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryXmList(HttpServletRequest request,String json) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		String domresult = "";
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			roleCond = " ";
			condition +=roleCond;
			condition += BusinessUtil.getSJYXCondition("T") + BusinessUtil.getCommonCondition(user,null);
			if(!Pub.empty(condition)){
				condition +=" AND  t.gc_tcjh_xmcbk_id not in  (select t.xmid from GC_CJJH t where t.sfyx='1' and t.xmid is not null) ";
				
			}else{
				condition="  t.gc_tcjh_xmcbk_id not in  (select t.xmid from GC_CJJH t where t.sfyx='1' and t.xmid is not null)";
			}
			condition +=orderFilter;
			page.setFilter(condition);
			String sql = "select gc_tcjh_xmcbk_id XMID, xmbh, xmmc, pxh, qy, nd, xmlx, xmdz, jsnr, jsyy, jsrw, jsbyx, jhztze, gc, zc, qt, xmsx, isbt, pch, xdlx, isxd, ywlx, sjbh, bz, lrr, lrsj, lrbm, lrbmmc, gxr, gxsj, gxbm, gxbmmc, sjmj, sfyx, pcid, spxxid, ztztze, ztgc, ztzc, ztqt, xmbm, cbk_xmbm from gc_tcjh_xmcbk T ";
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDateFormat("LRSJ", "yyyy-MM-dd");
			bs.setFieldOrgDept("LRBM");
			bs.setFieldUserID("LRR");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String saveInfo(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		GcCjjhVO vo = new GcCjjhVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		String logStr = "两委一局新增节点成功";
		try {
			boolean newFlag = false;
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			if(Pub.empty(vo.getGc_cjjh_id())){
				vo.setGc_cjjh_id(new RandomGUID().toString());
				vo.setSfyx("1");
				newFlag = true;
			}
			//特殊的排序号
			String defaultValue="00000000000000000000000000000000000";
			int nodeLevel = Integer.parseInt(vo.getNodelevel());
			String sjpxh = "";
			if("root".equals(vo.getParentid())){
				sjpxh = "1"+defaultValue.substring(0,(((nodeLevel+1)*2)-vo.getPxh().length()))+vo.getPxh();
			}else{
				String sql = "select PXH_SJ,NODELEVEL from GC_CJJH where SFYX='1' and GC_CJJH_ID='"+vo.getParentid()+"'";
				String arr[][] = DBUtil.query(conn, sql);
				String parentPxh = arr[0][0];
				int parentLevel = Integer.parseInt(arr[0][1]);
				sjpxh = parentPxh.substring(0,(parentLevel*2)+1) +defaultValue.substring(0,((2-vo.getPxh().length())))+vo.getPxh();
			}
			sjpxh = sjpxh+defaultValue.substring(0,15-sjpxh.length());
			vo.setPxh_sj(sjpxh);
		/*	if(!Pub.empty(vo.getXmid())){
				GcXmflglbVO flVO = new GcXmflglbVO();
				String queryFlSql = "select XMID,ID,PARENT from GC_XMFLGLB where SFYX='1' and XMID='"+vo.getXmid()+"'";
				String count[][] = DBUtil.query(conn, queryFlSql);
				flVO.setNd(vo.getNd());
				flVO.setXmid(vo.getXmid());
				flVO.setXmmc(vo.getXmmc());
				flVO.setParent(vo.getParentid());
				flVO.setOrderno(vo.getPxh_sj());
				flVO.setLy("1");
				flVO.setId(vo.getGc_cjjh_id());
				if(count==null || count.length==0){
					BusinessUtil.setInsertCommonFields(flVO,user);
					BaseDAO.insert(conn, flVO);
				}else{
					BusinessUtil.setUpdateCommonFields(flVO,user);
					BaseDAO.update(conn, flVO);
				}
				//对父节点处理
				String queryParentSql = "select GC_CJJH_ID,ND,XMMC,PARENTID,PXH_SJ,decode(XMID,null,'0','1') LY," +
						"case when GC_CJJH_ID in (select ID from GC_XMFLGLB) then '1' else '0' end as EXISTFLAG,NODELEVEL " +
						"from GC_CJJH where SFYX='1' and NODELEVEL>0 and NODELEVEL<"+vo.getNodelevel()+ " "+
						"connect by prior PARENTID=GC_CJJH_ID start with GC_CJJH_ID='"+vo.getGc_cjjh_id()+"'";
				String arr[][] = DBUtil.query(conn, queryParentSql);
				if(arr!=null && arr.length!=0){
					for(int x=0;x<arr.length;x++){
						GcXmflglbVO parentFlVO = new GcXmflglbVO();
						parentFlVO.setId(arr[x][0]);
						parentFlVO.setNd(arr[x][1]);
						parentFlVO.setXmmc(arr[x][2]);
						if("1".equals(arr[x][7])){
							parentFlVO.setParent("0");
						}else{
							parentFlVO.setParent(arr[x][3]);
						}
						parentFlVO.setOrderno(arr[x][4]);
						parentFlVO.setLy(arr[x][5]);
						parentFlVO.setXmid(arr[x][0]);
						if("0".equals(arr[x][6])){
							BusinessUtil.setInsertCommonFields(parentFlVO,user);
							BaseDAO.insert(conn, parentFlVO);
						}else{
							BusinessUtil.setUpdateCommonFields(parentFlVO,user);
							BaseDAO.update(conn, parentFlVO);
						}
					}
				}
			}*/
			if(newFlag){
				BusinessUtil.setInsertCommonFields(vo, user);
				vo.setYwlx(ywlx);//业务类型
				EventVO eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
				vo.setSjbh(eventVO.getSjbh());
				BaseDAO.insert(conn, vo);
				logStr = "两委一局新增节点成功";
			}else{
				BusinessUtil.setUpdateCommonFields(vo, user);
				BaseDAO.update(conn, vo);
				logStr = "两委一局修改节点成功";
			}
			
			// 城建計劃在新增的时候，需要同步到储备库中  start add by xiahongbo
			String xmid = vo.getXmid();
			if (!Pub.empty(xmid)) {
				String udpateSql = "UPDATE GC_TCJH_XMCBK SET XMSX='"+vo.getSsxz()+"' WHERE GC_TCJH_XMCBK_ID='"+xmid+"'";
				String updateSql1= "UPDATE GC_TCJH_XMXDK SET XMSX='"+vo.getSsxz()+"' WHERE XMCBK_ID='"+xmid+"'";
				DBUtil.execUpdateSql(conn, udpateSql);
				DBUtil.execUpdateSql(conn, updateSql1);
			}
			// 城建計劃在新增的时候，需要同步到储备库中  end
			
			conn.commit();
			domresult = Pub.makeQueryConditionByID(vo.getGc_cjjh_id(), "A.GC_CJJH_ID");
			domresult = this.queryNodeList(request, domresult);
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ logStr, user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}

	@Override
	public String saveRoot(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		GcCjjhVO vo = new GcCjjhVO();
		User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			JSONArray list = vo.doInitJson(json);
			vo.setValueFromJson((JSONObject)list.get(0));
			boolean iscf=rootpd(vo.getNd());
			if(iscf&&Pub.empty(vo.getGc_cjjh_id()))
			{
				return "1";
			}
			if(Pub.empty(vo.getGc_cjjh_id()))
			{
				vo.setGc_cjjh_id(new RandomGUID().toString());
				vo.setSfyx("1");
				vo.setParentid("root");
				vo.setNodelevel("0");
				//特殊的排序号
				String defaultValue="00000000000000000000000000000000000";
				int nodeLevel = Integer.parseInt(vo.getNodelevel());
				String sjpxh = "";
				sjpxh = "1"+defaultValue.substring(0,(((nodeLevel+1)*2)-vo.getPxh().length()))+vo.getPxh();
				sjpxh = sjpxh+defaultValue.substring(0,15-sjpxh.length());
				vo.setPxh_sj(sjpxh);
				BusinessUtil.setInsertCommonFields(vo, user);
				vo.setYwlx(ywlx);//业务类型
				EventVO eventVO = EventManager.createEvent(conn, vo.getYwlx(), user);//生成事件
				vo.setSjbh(eventVO.getSjbh());
				BaseDAO.insert(conn, vo);
			}
			else
			{
				BusinessUtil.setUpdateCommonFields(vo, user);//公共字段更新
				BaseDAO.update(conn, vo);
			}	
			conn.commit();
			domresult = Pub.makeQueryConditionByID(vo.getGc_cjjh_id(), "A.GC_CJJH_ID");
			domresult = this.queryNodeList(request, domresult);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}


	
	@Override
	public String doDeleteNode(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		GcCjjhVO vo = new GcCjjhVO();
		String id = request.getParameter("id");
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			vo.setGc_cjjh_id(id);
			vo.setSfyx("0");
			BaseDAO.update(conn, vo);
			conn.commit();
			LogManager.writeUserLog(vo.getSjbh(), vo.getYwlx(),
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "两委一局删除节点成功", user,"","");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			DBUtil.rollbackConnetion(conn);
			throw e;
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}


	@Override
	public String queryXMbyId(String json, User user,HttpServletRequest request) {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {
			String cjjhid=request.getParameter("cjjhid");
			PageManager page = RequestUtil.getPageManager(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
		    condition += BusinessUtil.getSJYXCondition("A") + BusinessUtil.getCommonCondition(user,null);
		    /*condition += BusinessUtil.getSJYXCondition("B") + BusinessUtil.getCommonCondition(user,null);*/
		    if(!Pub.empty(condition)){
				condition +=" and  a.xmid=b.gc_tcjh_xmcbk_id(+) and a.isxm='1' and a.parentid='"+cjjhid+"' ";
				
			}else{
				condition="   a.xmid=b.gc_tcjh_xmcbk_id(+) and a.isxm='1' and a.parentid='"+cjjhid+"'";
			}
			condition += orderFilter;
			
		    page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = 	" select a.gc_cjjh_id, a. jwxm, a. wbxm, a. xh, a. mxs, a. zxsm, a. xmmc, a. xmjswz, a. xmjsnrgm, a. jh_gc, a. jh_zq, a. jh_qt, " +
							" a.jh_xj, a. xmjsjd, a. nd_gc, a. nd_zq, a. nd_qt, a. nd_xj, a. nd_jsmb, a. zrbm, a. xmfr, a. xmlx, a. parentid, a. nodelevel," +
							" a.sfyx, a. xmid, a. nd cjnd, a. pxh, a. pxh_sj, a.xmsx, a. isxm,b.xmbh,b.nd,b.xmdz from gc_cjjh a, gc_tcjh_xmcbk b  ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("ISXM", "SF");
			bs.setFieldDic("XMSX", "XMSX");
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	/* 
	 * 根据输入值从计划数据表自动模糊匹配项目名称
	 * @see com.ccthanking.business.tcjh.jhgl.service.TcjhService#xmmcAutoComplete(com.ccthanking.framework.model.autocomplete, com.ccthanking.framework.common.User)
	 */
	@Override
	public List<autocomplete> xmmcAutoComplete(autocomplete json,User user) throws Exception {
		List<autocomplete> autoResult = new ArrayList<autocomplete>(); 
		autocomplete ac = new autocomplete();
		String condition = RequestUtil.getConditionList(json.getMatchInfo()).getConditionWhere();
		condition += BusinessUtil.getSJYXCondition(json.getTablePrefix());
		condition += BusinessUtil.getCommonCondition(user,json.getTablePrefix());
		String [][] result = DBUtil.query("select distinct xmmc from GC_CJJH " + json.getTablePrefix() + " where " + condition);
		if(null != result){
        	for(int i =0;i<result.length;i++){
        	  ac = new autocomplete();
              ac.setRegionName(result[i][0]);
              autoResult.add(ac);
        	}
        }
		return autoResult;
	}
	
	//判断相同年度是否有两个root复
	public static boolean rootpd(String nd)
	{
		String sql="select GC_CJJH_ID from GC_CJJH where sfyx=1 and parentid='root' and nd='"+nd+"'";
		String result[][] = DBUtil.query(sql);
		if(null!=result&&result.length>0)
		{
			return true;
		}	
		else
		{
			return false;			
		}	
	}
}
