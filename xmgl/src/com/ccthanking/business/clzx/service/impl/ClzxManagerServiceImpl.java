package com.ccthanking.business.clzx.service.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.clzx.service.ClzxManagerService;
import com.ccthanking.business.clzx.vo.ClzxRwbMAPVO;
import com.ccthanking.business.clzx.vo.ClzxRwbVO;
import com.ccthanking.business.jhfk.service.JhfkService;
import com.ccthanking.business.jhfk.service.impl.JhfkServiceImpl;
import com.ccthanking.business.jhfk.vo.FkgxVO;
import com.ccthanking.common.BusinessUtil;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;


/**
 * @author xiahongbo
 * @date 2014-9-2
 */
@Service
public class ClzxManagerServiceImpl implements ClzxManagerService {
	
	/*	
	 * 维护新增
	 * 
	 */
	@Override
	public String insert_clzx(HttpServletRequest request,User user) throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		ClzxRwbMAPVO mapvo= new ClzxRwbMAPVO();
		try {
				conn.setAutoCommit(false);
				String ids=request.getParameter("ids");
				String ywid=request.getParameter("ywid");
				String Dsql = "DELETE FROM CLZX_YW_RY_MAP WHERE ywbid   = '"+ywid+"'";
				DBUtil.execSql(conn, Dsql);
				if(ids.length()>0)
				{
					ids=ids.substring(0, ids.length()-1);					
				}	
				String id[]=ids.split(",");
				for(int i=0;i<id.length;i++)
				{
					mapvo.setClzx_yw_ry_map_id(new RandomGUID().toString());
					mapvo.setYwbid(ywid);
					mapvo.setRybid(id[i]);
					BusinessUtil.setInsertCommonFields(mapvo, user);//公共字段插入
					BaseDAO.insert(conn, mapvo);//插入
				}	
				resultVO = mapvo.getRowJson();		
				conn.commit();		
		
				LogManager.writeUserLog(null, null,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_SUCCESS,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "业务配置人员成功", user,"","");
		} catch (Exception e) {
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace(System.out);
			LogManager.writeUserLog(null, null,
					Globals.OPERATION_TYPE_INSERT, LogManager.RESULT_FAILURE,
					user.getOrgDept().getDeptName() + " " + user.getName()
							+ "业务配置人员失败", user,"","");
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return resultVO;
	}

	/**
	 * 获得业务下人员
	 */
	@Override
	public String[][] queryYwry(String id) {
		String sql = "select name,account from CLZX_YW_RY_MAP clzx left join FS_ORG_PERSON  person on person.account=clzx.rybid  where sfyx=1 and ywbid='"+id+"'";
		String[][] personArray = DBUtil.query(sql);
		return personArray;
	}

	/**
	 * 获得业务类型目录
	 */
	@Override
	public String queryClzxgl() {
		
		Connection conn = DBUtil.getConnection();
		String queryDictSql = "select clzx_ywb_id,ywmc,parentid from CLZX_YWB where sfyx=1 and parentid is not null order by parentid asc";
		JSONArray jsonArr = new JSONArray();

		try {
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn,queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json = new JSONObject();
				json.put("id",rsMap.get("CLZX_YWB_ID"));
				if(("0").equals(rsMap.get("PARENTID"))){
					json.put("open", "true");
				}
				/*parentid为空时
				 * if(Pub.empty(rsMap.get("PARENTID"))){
					json.put("open", "true");
				}*/
			    json.put("parentId", rsMap.get("PARENTID"));
			    String name = rsMap.get("YWMC");
			    json.put("name", name);
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
	public void createTask(String ywbid,String filter,User user,Connection conn){
		
		try{
			String ywbSql = "select CLZX_YWB_ID,SFTB from CLZX_YWB where CLZX_YWB_ID='"+ywbid+"'";
			String ywbArr[][] = DBUtil.query(conn, ywbSql);
			//前提是这个类型在业务表中存在，否则无法创建任务
			if(ywbArr!=null && ywbArr.length!=0){
				//查询映射关系表
				//----------------首先处理不需要同步的
				String ywrybSql = "select A.RYBID,A.YWBID " +
						"from CLZX_YW_RY_MAP A " +
						"where A.YWBID ='"+ywbArr[0][0]+"'";
				String ywrybArr[][] = DBUtil.query(conn, ywrybSql);
				if(ywrybArr!=null && ywrybArr.length!=0){
					String glid = new RandomGUID().toString();
					for(int j=0;j<ywrybArr.length;j++){
						int sl = this.getSyRwslByRyid(ywrybArr[j][0], filter,ywbid, conn);
						//如果剩余数量为0，那么可以生成任务，防止
						if(sl!=0){
							String updateSql = "update CLZX_RWB set RWZT='1' where RYID='"+ywrybArr[j][0]+"' and FILTER='"+filter+"' and YWBID='"+ywbid+"'";
							DBUtil.exec(conn, updateSql);
						}
						ClzxRwbVO rwVO = new ClzxRwbVO();
						rwVO.setClzx_rwb_id(new RandomGUID().toString());
						rwVO.setYwbid(ywbid);
						rwVO.setRyid(ywrybArr[j][0]);
						if("0".equals(ywbArr[0][1])){
							//如果不是同步的，那么每次都生成新的关联ID
							glid = new RandomGUID().toString();
						}else{
							
						}
						rwVO.setGlid(glid);
						rwVO.setFilter(filter);
						rwVO.setRwzt("0");//字典：0.未处理，1.已处理
						BusinessUtil.setInsertCommonFields(rwVO,user);
						BaseDAO.insert(conn,rwVO);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//处理中心任务和业务数据公用同一个事物，不再创建新的Connection，也不需要关闭Connection
		}
	}
	/**
	 * 通过人员ID获取剩余任务数量
	 * @param ryid
	 * @param filter
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	private int getSyRwslByRyid(String ryid,String filter,String ywbid,Connection conn) throws Exception{
		int rwsl = 0;
		String sql = "select count(CLZX_RWB_ID) from CLZX_RWB where RYID='"+ryid+"' and FILTER='"+filter+"' and YWBID='"+ywbid+"' and RWZT='0'";
		String arr[][] = DBUtil.query(conn, sql);
		if(arr!=null && arr.length!=0){
			rwsl = Integer.parseInt(arr[0][0]);
		}
		return rwsl;
	}
	@Override
	public void achieveTask(String ywbid,String filter,User user,Connection conn){
		try{
			String rwbSql = "select A.CLZX_RWB_ID,A.GLID,B.SFTB " +
					"from CLZX_RWB A,CLZX_YWB B " +
					"where A.YWBID=B.CLZX_YWB_ID and A.YWBID='"+ywbid+"' and A.FILTER='"+filter+"' and A.RYID='"+user.getAccount()+"' and A.RWZT='0'";
			String rwbArr[][] = DBUtil.query(conn, rwbSql);
			if(rwbArr!=null && rwbArr.length!=0){
				//同一个计划的同一个人的同一个业务，只能查询出一条数据
				String updateSql = "";
				if("1".equals(rwbArr[0][2])){
					//如果需要同步，那么把该任务对应的所有人员的任务状态都同步
					updateSql = "update CLZX_RWB set RWZT='1' where YWBID='"+ywbid+"' and FILTER='"+filter+"' and GLID='"+rwbArr[0][1]+"'";
				}else{
					//如果不需要同步，那么只处理当前人员的状态
					updateSql = "update CLZX_RWB set RWZT='1' where CLZX_RWB_ID='"+rwbArr[0][0]+"'";
				}
				DBUtil.execSql(conn, updateSql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}
	@Override
	public void achieveTaskByJhfk(String fklx,JSONObject rowJson,HttpServletRequest request,Connection conn){
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		try{
			if(rowJson!=null){
				this.achieveTask(fklx, (String)rowJson.get("JHSJID"), user, conn);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}
	@Override
	public String queryYwlx(String json, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String username = user.getAccount();
			
			String sql = "select b.clzx_ywb_id ywbid,b.ywmc,nvl(a.cnt,0) cnt from ( select rwb.YWBID, count(*) cnt, ywb.ywmc "+
						 " from CLZX_RWB rwb, "+
						 "  CLZX_YWB ywb "+
						 " where rwb.RYID = '"+username+"' "+
						 "  and rwb.RWZT = '0' "+
						 "  and rwb.ywbid = ywb.clzx_ywb_id "+
						 "  group by YWBID, ywb.ywmc " +
						 " )a,"+
						 " (select distinct ywb.clzx_ywb_id,ywb.ywmc "+
						 "  from CLZX_RWB rwb, CLZX_YWB ywb "+
						 " where rwb.RYID = '"+username+"'"+
						 " and rwb.ywbid = ywb.clzx_ywb_id)b where a.ywbid(+)=b.clzx_ywb_id"+
						 " order by b.clzx_ywb_id ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	@Override
	public String queryList(String json, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			String ywbid = request.getParameter("yw_code");
			String username = user.getAccount();
			String querySql = "select filter,fkgx.jhzdmc,fkgx.jhsjmc from CLZX_RWB rwb,GC_JH_FKGX fkgx where ywbid='"+ywbid+"' and ryid='"+username+"' and rwb.RWZT='0' and rwb.ywbid=fkgx.fklx" ;
			String[][] rs = DBUtil.query(conn, querySql);
			String filterId = "";
			if(rs==null){
				//------------如果没有记录存在，那么后面的内容都不需要执行了
				return "0";
			}
			for (int i = 0; i < rs.length; i++) {
				filterId += "'"+rs[i][0]+"',";
			}
			filterId = filterId.endsWith(",") ? filterId.substring(0, filterId.lastIndexOf(",")) : filterId;
			// 实际时间节点
			String sjsjjd = "".equals(rs[0][1]) ? "''" : rs[0][1];
			// 计划时间节点
			String jhsjjd = "".equals(rs[0][2]) ? "''" : rs[0][2];
			
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += BusinessUtil.getSJYXCondition(null) + BusinessUtil.getCommonCondition(user, null);
			condition += " and GC_JH_SJ_ID in (" + filterId + ") ";
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			JhfkService jhfk = new JhfkServiceImpl();
//			sjsjjd = jhfk.getFkFkrq(jhsjid, fklx, conn)
			//获取反馈VO
			BaseVO[] bv = null;
			FkgxVO condVO = new FkgxVO();
			condVO.setFklx(ywbid);
			if(condVO!=null&&!condVO.isEmpty()){
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			FkgxVO newvo = null;
			if(bv!=null){
				for(int i=0;i<1;i++){
					newvo = (FkgxVO)bv[i];
				}
			}
			if(newvo==null){
				sjsjjd = "";
			}else{
				sjsjjd = " (select to_char("+newvo.getSjzdmc()+",'yyyy-MM-dd') from "+newvo.getSjbmc()+" where JHSJID=t.GC_JH_SJ_ID and SFYX='1' and  rownum=1) ";
			}
			String queryListSql = "select GC_JH_SJ_ID,XMID,SJWYBH,XMBH,XMMC,BDBH,BDMC,XMGLGS,XMXZ,"+jhsjjd+" JHSJJD, "+sjsjjd+" SJSJJD," +
					"(select count(GC_JH_FKQK_ID) from GC_JH_FKQK F where F.FKLX='"+ywbid+"' and F.JHSJID=t.GC_JH_SJ_ID and F.SFYX='1') FKQK " +
					"from GC_JH_SJ t ";
			BaseResultSet bs = DBUtil.query(conn, queryListSql, page);
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldDic("XMXZ", "XMXZ");
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	@Override
	public String queryListLS(String json, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = DBUtil.getConnection();
		String domResult = "";
		try {
			String ywbid = request.getParameter("yw_code");
			String username = user.getAccount();
			String querySql = "select filter,fkgx.jhzdmc,fkgx.jhsjmc from CLZX_RWB rwb,GC_JH_FKGX fkgx where ywbid='"+ywbid+"' and ryid='"+username+"' and rwb.RWZT='1' and rwb.ywbid=fkgx.fklx" ;
			String[][] rs = DBUtil.query(conn, querySql);
			String filterId = "";
			if(rs==null){
				//------------如果没有记录存在，那么后面的内容都不需要执行了
				return "0";
			}
			for (int i = 0; i < rs.length; i++) {
				filterId += "'"+rs[i][0]+"',";
			}
			filterId = filterId.endsWith(",") ? filterId.substring(0, filterId.lastIndexOf(",")) : filterId;
			// 实际时间节点
			String sjsjjd = "".equals(rs[0][1]) ? "''" : rs[0][1];
			// 计划时间节点
			String jhsjjd = "".equals(rs[0][2]) ? "''" : rs[0][2];
			
			QueryConditionList list = RequestUtil.getConditionList(json);
			String orderFilter = RequestUtil.getOrderFilter(json);
			String condition = list == null ? "" : list.getConditionWhere();
			condition += BusinessUtil.getSJYXCondition("F") + BusinessUtil.getCommonCondition(user, null);
			condition += "and F.FKLX='"+ywbid+"'  and t.gc_jh_sj_id=f.jhsjid and GC_JH_SJ_ID in (" + filterId + ") order by f.lrsj desc";
			condition += orderFilter;
			
			PageManager page = RequestUtil.getPageManager(json);
			page.setFilter(condition);
			JhfkService jhfk = new JhfkServiceImpl();
//			sjsjjd = jhfk.getFkFkrq(jhsjid, fklx, conn)
			//获取反馈VO
			BaseVO[] bv = null;
			FkgxVO condVO = new FkgxVO();
			condVO.setFklx(ywbid);
			if(condVO!=null&&!condVO.isEmpty()){
				bv = (BaseVO [])BaseDAO.getVOByCondition(conn, condVO);
			}
			FkgxVO newvo = null;
			if(bv!=null){
				for(int i=0;i<1;i++){
					newvo = (FkgxVO)bv[i];
				}
			}
			if(newvo==null){
				sjsjjd = "";
			}else{
				sjsjjd = " (select to_char("+newvo.getSjzdmc()+",'yyyy-MM-dd') from "+newvo.getSjbmc()+" where JHSJID=t.GC_JH_SJ_ID and SFYX='1' and  rownum=1) ";
			}
			String queryListSql = "select t.GC_JH_SJ_ID,t.XMID,t.SJWYBH,t.XMBH,t.XMMC,t.BDBH,t.BDMC,t.XMGLGS,t.XMXZ,"+newvo.getJhzdmc()+" Jhzdmc,"+newvo.getJhsjmc()+" JHSJMC,"+jhsjjd+" JHSJJD, "+sjsjjd+" SJSJJD," +
					"GC_JH_FKQK_ID ,f.lrsj CLSJ " +
					"from GC_JH_SJ t,GC_JH_FKQK f ";
			BaseResultSet bs = DBUtil.query(conn, queryListSql, page);
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldDic("XMXZ", "XMXZ");
			domResult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domResult;
	}
	@Override
	public String queryCount(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			String username = user.getAccount();
			
			PageManager page = new PageManager();
			String condition = " ryid='"+username+"' and rwb.RWZT='0' ";
			page.setFilter(condition);

			String querySql = "select count(*) count from CLZX_RWB rwb " ;
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}

	public String queryIsShowClzx(HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute(Globals.USER_KEY);
		Connection conn = null;
		String domString = "";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			String username = user.getAccount();
			
			PageManager page = new PageManager();
			String querySql = "select count(*) cnt from CLZX_YW_RY_MAP where rybid='"+username+"'";
			BaseResultSet bs = DBUtil.query(conn, querySql, page);
			domString = bs.getJson();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	
	public String plfkJh(HttpServletRequest request) {
		Connection conn = null;
		String domString = "0";
		try {
			conn = DBUtil.getConnection();
			String params = request.getParameter("params");
			String[] array = params.split(",");
			
			JhfkService jhfk = new JhfkServiceImpl();
			for (int i = 0; i < array.length; i++) {
				String param = array[i];
				jhfk.doFkjhBatch(request, param, conn);
			}
			
			conn.commit();
			domString = "1";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
}
