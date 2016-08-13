package com.ccthanking.business.wdgl.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.ccthanking.business.wdgl.GcXmflglbVO;
import com.ccthanking.business.wdgl.service.ProjectService;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @auther xhb 
 */
@Service
public class ProjectServiceImpl implements ProjectService {
	
	/* (non-Javadoc)
	 * @see com.ccthanking.business.wdgl.service.impl.ProjectService#getAllProject(java.lang.String)
	 */
	@Override
	public String getAllProject(String prjName) {
		Connection conn = DBUtil.getConnection();
		String condition = "".equals(prjName) ? "" : " WHERE PRJNAME LIKE '%" + prjName + "%'";
		String queryDictSql = "SELECT PRJID, PRJNO, PRJNAME, PRJTYPE FROM TPJ_PROJECT "  + condition + " ORDER BY PRJNO DESC ";
		JSONArray jsonArr = new JSONArray();
		JSONObject rootJson = new JSONObject();
		rootJson.put("id", "0");
		rootJson.put("parentId", "");
		rootJson.put("name", "工程树");
		rootJson.put("open", "true");
		jsonArr.add(rootJson);
		try {
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json = new JSONObject();
				json.put("id",rsMap.get("PRJID"));
			    json.put("parentId", 0);
			    json.put("name", "(" + rsMap.get("PRJNO") + ")" + rsMap.get("PRJNAME"));
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
	public String getProjectfl(String nd) {
		Connection conn = DBUtil.getConnection();
		String condition = "".equals(nd) ? "" : " WHERE nd = '" + nd + "'";
		String queryDictSql = "SELECT ID, XMID, XMMC, PARENT,LY FROM GC_XMFLGLB "  + condition + " ORDER BY ORDERNO  ";
		JSONArray jsonArr = new JSONArray();
		JSONObject rootJson = new JSONObject();
		rootJson.put("id", "0");
		rootJson.put("parentId", "");
		rootJson.put("name", "项目树");
		rootJson.put("open", "true");
		
		jsonArr.add(rootJson);
		try {
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json = new JSONObject();
				json.put("id",rsMap.get("XMID"));
			    json.put("parentId",rsMap.get("PARENT"));
			    json.put("name", rsMap.get("XMMC"));
			    json.put("ly", rsMap.get("LY"));
			    json.put("open", "true");
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
	public String addFl(String parent,String flmc,String operatorSign,String nd,User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		GcXmflglbVO vo = new GcXmflglbVO();
		JSONObject jo = new JSONObject();
	//	String nd = "2014";
		try {
		conn.setAutoCommit(false);
		if("1".equals(operatorSign)){		
			vo.setId(new RandomGUID().toString()); // 主键
			vo.setXmid(new RandomGUID().toString());//xmid
			vo.setParent(parent);
			vo.setLy("1");
			vo.setXmmc(flmc);
			vo.setNd(nd);
			
			int orderno = -1;
			String orderno_s = null;
			String[][] t =null;
			if(!"0".equals(parent)){
				t =  DBUtil.querySql(conn, "select max(orderno) from gc_xmflglb where xmid = '"+parent+"' and nd='"+nd+"'");
			}else{
				t =  DBUtil.querySql(conn, "select max(orderno) from gc_xmflglb where  nd='"+nd+"' ");
			}
			
			if(t!=null&&t.length>0){
				if("".equals(t[0][0]))
					orderno_s = "0";
				else
				orderno_s = t[0][0];
				orderno = Integer.parseInt(orderno_s);
			}
			if(orderno>-1){
				vo.setOrderno(String.valueOf(orderno+1));
				DBUtil.exec(conn, "update gc_xmflglb set orderno=orderno+1 where orderno>"+orderno+" and nd='"+nd+"'");
			}
			//
			vo.setLrr(user.getAccount()); //更新人
			vo.setLrsj(Pub.getCurrentDate()); //更新时间
			vo.setLrbm(user.getDepartment());//录入人单位
			//插入
			BaseDAO.insert(conn, vo);
		}else if("2".equals(operatorSign)){
			String sql = "update gc_xmflglb set xmmc = '"+flmc+"' where xmid = '"+parent+"'";
			DBUtil.exec(conn, sql);
		} if("3".equals(operatorSign)){
			String sql = "delete from gc_xmflglb where xmid = '"+parent+"'";
			String sql_z = "delete from gc_xmflglb where parent = '"+parent+"'";
			int count = 0;
			String[][] childcount = DBUtil.query(conn, "select count(*) from gc_xmflglb where parent = '"+parent+"'");
			if(childcount!=null&&!"".equals(childcount[0][0])){
				count = 1+Integer.parseInt(childcount[0][0]);
			}else{
				count = 1;
			}
			int orderno = -1;
			String orderno_s = null;
			String[][] t =null;
				t =  DBUtil.querySql(conn, "select max(orderno) from gc_xmflglb where xmid='"+parent+"'");
			
			if(t!=null&&t.length>0){
				if("".equals(t[0][0]))
					orderno_s = "0";
				else
				orderno_s = t[0][0];
				orderno = Integer.parseInt(orderno_s);
			}
			String sql_o = "update gc_xmflglb set orderno=orderno-"+count+" where orderno>"+orderno+" and nd='"+nd+"'";
			DBUtil.exec(conn, sql_o);
			DBUtil.exec(conn, sql);
			DBUtil.exec(conn, sql_z);
		}
		
		conn.commit();
		jo.put("flag", "1");
		jo.put("msg", "操作成功！");

		} catch (Exception e) {
			jo.put("flag", "0");
			jo.put("msg", "操作失败："+e.getMessage());
			conn.rollback();
			e.printStackTrace(System.out);
			
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return jo.toString();
	}
	@Override
	public String addZxm(String parent,String ids,String nd,User user) throws SQLException {
		Connection conn = DBUtil.getConnection();
		GcXmflglbVO vo = new GcXmflglbVO();
		JSONObject jo = new JSONObject();
		//String nd = "2014";
		

		try {
		    conn.setAutoCommit(false);
		    String[] id_s = ids.split(",");
		    if(id_s.length>0){
		    	   
	    		    int orderno = -1;
	    		    String orderno_s = null;
					String[][] t =null;
						t =  DBUtil.querySql(conn, "select max(orderno) from gc_xmflglb where xmid='"+parent+"' and nd='"+nd+"' ");
					
					if(t!=null&&t.length>0){
						if("".equals(t[0][0]))
							orderno_s = "0";
						else
						orderno_s = t[0][0];
						orderno = Integer.parseInt(orderno_s);
					}
					int count = 0;
					String[][] c =  DBUtil.querySql(conn, "select count(*) from gc_xmflglb where parent='"+parent+"' and nd='"+nd+"' ");
					if(c!=null&&!"".equals(c[0][0])){
						int tt = Integer.parseInt(c[0][0]);
						count = id_s.length-tt;
					}
					if(orderno>-1){
				    	   DBUtil.exec(conn, "update gc_xmflglb set orderno=orderno+"+count+" where orderno>"+orderno+" and nd='"+nd+"'");
				    }
					 String delete_sql = "delete from GC_XMFLGLB where parent = '"+parent+"' and ly='0'";
		    		    DBUtil.exec(conn, delete_sql);
		    	for(int i = 0;i<id_s.length;i++){
		    		
		    		String xmmc = DBUtil.query(conn, "select xmmc from GC_TCJH_XMXDK where gc_tcjh_xmxdk_id='"+id_s[i]+"'")[0][0];
		    		vo.setId(new RandomGUID().toString()); // 主键
					vo.setXmid(id_s[i]);//xmid
					vo.setParent(parent);
					vo.setLy("0");
					vo.setXmmc(xmmc);
					vo.setNd(nd);
					
					if(orderno>-1){
						vo.setOrderno(String.valueOf(orderno+1+i));
					}
					//
					vo.setLrr(user.getAccount()); //更新人
					vo.setLrsj(Pub.getCurrentDate()); //更新时间
					vo.setLrbm(user.getDepartment());//录入人单位
					//插入
					BaseDAO.insert(conn, vo);
		    	}
		    	
		    }
			
		
		conn.commit();
		jo.put("flag", "1");
		jo.put("msg", "操作成功！");

		} catch (Exception e) {
			jo.put("flag", "0");
			jo.put("msg", "操作失败："+e.getMessage());
			conn.rollback();
			e.printStackTrace(System.out);
			
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return jo.toString();
	}
	
	
	/**
	 * 获得项目文档目录
	 */
	@Override
	public String getDocumentDir(String prjName,String prjId) {
		Connection conn = DBUtil.getConnection();
		String condition = "".equals(prjName) ? "" : " WHERE NAME LIKE '%" + prjName + "%'";
		String queryDictSql = "SELECT ID, NAME,PARENT, FJLB FROM GC_DOCUMENT_DIR "  + condition + " ORDER BY to_number(ID) ASC ";
		JSONArray jsonArr = new JSONArray();
//		JSONObject rootJson = new JSONObject();
//		rootJson.put("id", "0");
//		rootJson.put("parentId", "");
//		rootJson.put("name", "工程树");
//		rootJson.put("open", "true");
//		jsonArr.add(rootJson);
		try {
			List<Map<String, String>> rsList = DBUtil.queryReturnList(conn, queryDictSql);
//			for (int i = 0; i < rsList.size(); i++) {
//				Map<String, String> rsMap = rsList.get(i);
//				JSONObject json = new JSONObject();
//				json.put("id",rsMap.get("PRJID"));
//			    json.put("parentId", 0);
//			    json.put("name", "(" + rsMap.get("PRJNO") + ")" + rsMap.get("PRJNAME"));
//			    jsonArr.add(json);
//			}
			for (int i = 0; i < rsList.size(); i++) {
				Map<String, String> rsMap = rsList.get(i);
				JSONObject json = new JSONObject();
				json.put("id",rsMap.get("ID"));
				if(("0").equals(rsMap.get("PARENT"))){
					json.put("open", "true");
				}
				if(Pub.empty(rsMap.get("PARENT"))){
					json.put("open", "true");
				}
			    json.put("parentId", rsMap.get("PARENT"));
			    String name = rsMap.get("NAME");
			    String fjlb = rsMap.get("FJLB");
			    String htlx = "";
				if("410".equals(rsMap.get("ID"))){	//410表示施工合同
					htlx = "SG";
					fjlb = "0702";
				}else if("407".equals(rsMap.get("ID"))){
					htlx = "JL";
					fjlb = "0702";
				}else if("417".equals(rsMap.get("ID"))){
					htlx = "QT";
					fjlb = "0702";
				}else if("406".equals(rsMap.get("ID"))){
					htlx = "PQ";
					fjlb = "0702";
				}else if("405".equals(rsMap.get("ID"))){
					htlx = "CQ";
					fjlb = "0702";
				}else if("404".equals(rsMap.get("ID"))){
					htlx = "SJ";
					fjlb = "0702";
				}else if("403".equals(rsMap.get("ID"))){
					htlx = "ZBDL";
					fjlb = "0702";
				}else if("402".equals(rsMap.get("ID"))){
					htlx = "ZX";
					fjlb = "0702";
				}else if("401".equals(rsMap.get("ID"))){
					htlx = "QQ";
					fjlb = "0702";
				}
			    if(!Pub.empty(fjlb)){
			    	if(fjlb.indexOf(",")>-1){
			    		fjlb = fjlb.replaceAll(",", "','");
			    		fjlb =  " ('" + fjlb + "')";
			    	}else{
			    		fjlb =  " ('" + fjlb + "')";
			    	}
			    	String sjwybhSql = "select SJWYBH from GC_JH_SJ where XMID='"+prjId+"'";
					String sjwybhArr[][] = DBUtil.query(sjwybhSql);
					String sjwybhStr = "";
					if(sjwybhArr!=null&& sjwybhArr.length!=0){
						for(int x=0;x<sjwybhArr.length;x++){
							sjwybhStr += "'"+sjwybhArr[x][0]+"',";
						}
					}
					String getFjCount = "";
					if(!"".equals(htlx)){
						getFjCount = "select count(1) from fs_fileupload where ywid in (select A.HTID from "+
							"(select distinct HTID from GC_HTGL_HTSJ "+
							"where JHSJID in (select GC_JH_SJ_ID from GC_JH_SJ where SJWYBH in ("+sjwybhStr.substring(0,sjwybhStr.length()-1)+") "+
							"and SFYX='1')) A,GC_HTGL_HT B where A.HTID=B.ID and B.HTLX='"+htlx+"' and B.SFYX='1') and fjlb in "+fjlb+" and FJZT='1'";
					}else{
						if(sjwybhStr!="" && !"".equals(sjwybhStr)){
							sjwybhStr = " or WYBH in ( "+sjwybhStr.substring(0,sjwybhStr.length()-1)+")";
						}
				    	getFjCount = "select count(1) from fs_fileupload where (GLID3='"+prjId+"' "+sjwybhStr+") and fjlb in "+fjlb+" and FJZT='1'";
				    	
					}
					String[][] s = DBUtil.query(conn, getFjCount);
			    	if(s!=null&&s.length>0){
			    		String c = s[0][0];
			    		if(!"0".equals(c)){
			    			name = name+"("+c+")";
			    		}
			    	}
			    }
			    
			    json.put("name", name);
			    json.put("fjlb", fjlb);
			    json.put("queryfjlb", rsMap.get("FJLB"));
//			  
//			    if(list.contains(rsMap.get("NAME"))){
//			    	json.put("checked", "true");
//			    	//json.put("open", "true");
//			    }
//			    if(parList.contains(rsMap.get("NAME"))){
//			    	//json.put("checked", "true");
//			    	json.put("open", "true");
//			    }
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
	public String getChildProject(String json,String parent) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult = "";
		try {

		QueryConditionList list = RequestUtil.getConditionList(json);
		PageManager page =  RequestUtil.getPageManager(json);
		String orderFilter = RequestUtil.getOrderFilter(json);
		String condition = list == null ? "" : list.getConditionWhere();
		
		condition +=" and a.xmid = b.gc_tcjh_xmxdk_id and a.parent ='"+parent+"'";
		
		if (page == null)
			page = new PageManager();
			page.setFilter(condition);
			conn.setAutoCommit(false);
			String sql = "SELECT b.gc_tcjh_xmxdk_id,b.xmbh,b.xmmc,b.xmdz,b.xmlx,b.nd FROM GC_XMFLGLB a,GC_TCJH_XMXDK b";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("XMLX", "XMLX");

			domresult = bs.getJson();

			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		
		} finally {
			if (conn != null)
				conn.close();
		}
		return domresult;
	}
}
