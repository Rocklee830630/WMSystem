package com.ccthanking.business.htgl.bmgk.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.ccthanking.business.htgl.bmgk.service.GcHtBmgkService;
import com.ccthanking.common.BzjkCommon;
import com.ccthanking.framework.CommonChart.showchart.chart.ChartUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;

/**
 * @author xiahongbo
 * @date 2014-9-15
 */
@Service
public class GcHtBmgkServiceImpl implements GcHtBmgkService {
	
	private static String propertiesFileName = "com.ccthanking.properties.business.bmjk.bmjk_zbht";
	private static String propertyFileName = "com.ccthanking.properties.business.bzjklj.ztb_xx";
	@Override
	public String queryHtglBmlb(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			page.setPageRows(100);
			
			String nd = request.getParameter("nd");
			String sql = Pub.getPropertiesString(propertiesFileName, "HTGL_BMLB");
			sql = sql.replaceAll("%HTGL_BMLB_ND%", nd);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	
	@Override
	public String queryHtglTjgk(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String nd = request.getParameter("nd");
			String sql = Pub.getPropertiesString(propertiesFileName, "HTGL_TJGK");
			nd = Pub.empty(nd)?"":" and t.qdnf='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd);
			
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String queryHtglZbsg(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String nd = request.getParameter("nd");
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_SGZB_TXFX");			
			String nd1 = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			String nd2 = Pub.empty(nd)?"":" TO_CHAR(C.LRSJ,'YYYY') = '"+nd+"'";
			String nd3=Pub.empty(nd)?"":"AND T.ND < '"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd1);
			sql=sql.replaceAll("%Condition_2%",nd2);
			sql=sql.replaceAll("%Condition_3%",nd3);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String queryHtglZbjl(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = null;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			PageManager page = new PageManager();
			String nd = request.getParameter("nd");
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_JLZB_TXFX");
			String nd1 = Pub.empty(nd)?"":" and t.ND='"+nd+"'";
			String nd2 = Pub.empty(nd)?"":" TO_CHAR(C.LRSJ,'YYYY') = '"+nd+"'";
			String nd3=Pub.empty(nd)?"":"AND T.ND < '"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd1);
			sql=sql.replaceAll("%Condition_2%",nd2);
			sql=sql.replaceAll("%Condition_3%",nd3);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domString = bs.getJson();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	//总工办—设计—招标需求
	@Override
	public String queryZgbsjzbChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_ZBXQ_SJ");		
			String nd2 = Pub.empty(nd)?"":" c.nd = '"+nd+"'";
			sql=sql.replaceAll("%Condition_2%",nd2);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = domresult.replaceAll("&lt;", "<");
			HashMap chartMap=new HashMap();
			chartMap.put("pieRadius","30");			
			domresult = ChartUtil.makePieEChartJsonString(domresult, chartMap, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//总工办—前期—招标需求
	@Override
	public String queryZgbqqzbChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");	
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_ZBXQ_QQ");
			String nd2 = Pub.empty(nd)?"":" c.nd = '"+nd+"'";
			sql=sql.replaceAll("%Condition_2%",nd2);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = domresult.replaceAll("&lt;", "<");
			HashMap chartMap=new HashMap();
			chartMap.put("pieRadius","30");			
			domresult = ChartUtil.makePieEChartJsonString(domresult, chartMap, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//总工办—造价部—招标需求
	@Override
	public String queryZgbzjbzbChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");	
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_ZBXQ_ZJB");
			String nd2 = Pub.empty(nd)?"":" c.nd = '"+nd+"'";
			sql=sql.replaceAll("%Condition_2%",nd2);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = domresult.replaceAll("&lt;", "<");
			HashMap chartMap=new HashMap();
			chartMap.put("pieRadius","30");			
			domresult = ChartUtil.makePieEChartJsonString(domresult, chartMap, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//总工办—招投标—招标需求
	@Override
	public String queryZgbztbzbChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");	
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_ZBXQ_ZTB");
			String nd2 = Pub.empty(nd)?"":" c.nd = '"+nd+"'";
			sql=sql.replaceAll("%Condition_2%",nd2);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = domresult.replaceAll("&lt;", "<");
			HashMap chartMap=new HashMap();
			chartMap.put("pieRadius","30");			
			domresult = ChartUtil.makePieEChartJsonString(domresult, chartMap, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//总工办—排迁—招标需求
	@Override
	public String queryZgbpqzbChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");	
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_ZBXQ_PQ");
			String nd2 = Pub.empty(nd)?"":" c.nd = '"+nd+"'";
			sql=sql.replaceAll("%Condition_2%",nd2);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = domresult.replaceAll("&lt;", "<");
			HashMap chartMap=new HashMap();
			chartMap.put("pieRadius","30");			
			domresult = ChartUtil.makePieEChartJsonString(domresult, chartMap, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	//总工办—征收—招标需求
	@Override
	public String queryZgbzszbChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");	
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_ZBXQ_ZS");
			String nd2 = Pub.empty(nd)?"":" c.nd = '"+nd+"'";
			sql=sql.replaceAll("%Condition_2%",nd2);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = domresult.replaceAll("&lt;", "<");
			HashMap chartMap=new HashMap();
			chartMap.put("pieRadius","30");			
			domresult = ChartUtil.makePieEChartJsonString(domresult, chartMap, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//总工办—施工—招标需求
	@Override
	public String queryZgbsgzbChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");	
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_ZBXQ_XMGLGS_SGZB");
			String nd2 = Pub.empty(nd)?"":" c.nd = '"+nd+"'";
			sql=sql.replaceAll("%Condition_2%",nd2);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = domresult.replaceAll("&lt;", "<");
			HashMap chartMap=new HashMap();
			chartMap.put("pieRadius","30");			
			domresult = ChartUtil.makePieEChartJsonString(domresult, chartMap, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//总工办—监理—招标需求
	@Override
	public String queryZgbjlzbChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");	
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_ZBXQ_XMGLGS_JLZB");
			String nd2 = Pub.empty(nd)?"":" c.nd = '"+nd+"'";
			sql=sql.replaceAll("%Condition_2%",nd2);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = domresult.replaceAll("&lt;", "<");
			HashMap chartMap=new HashMap();
			chartMap.put("pieRadius","30");			
			domresult = ChartUtil.makePieEChartJsonString(domresult, chartMap, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	//总工办—质量安全—招标需求
	@Override
	public String queryZgbzlaqzbChart(HttpServletRequest request, String json)
			throws Exception {
		Connection conn = null;
		String domresult = "";
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String nd=request.getParameter("nd");	
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_ZBXQ_ZLAQ");
			String nd2 = Pub.empty(nd)?"":" c.nd = '"+nd+"'";
			sql=sql.replaceAll("%Condition_2%",nd2);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			domresult = domresult.replaceAll("&lt;", "<");
			HashMap chartMap=new HashMap();
			chartMap.put("pieRadius","30");			
			domresult = ChartUtil.makePieEChartJsonString(domresult, chartMap, null);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	// 招标状态
	@Override
	public String queryZbzt(HttpServletRequest request) throws Exception {
		Connection conn = DBUtil.getConnection();
		String domresult ="";
		PageManager page=new PageManager();
		String nd = request.getParameter("nd");
		String ndtj="";
		try {
			conn.setAutoCommit(false);
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_ZBXQ_LB");
			String nd1 = Pub.empty(nd)?"":" and c.ND='"+nd+"'";
			sql=sql.replaceAll("%Condition%", nd1);
			BaseResultSet bs = DBUtil.query(conn, sql,page);
			/*bs.setFieldDic("ZBFS", "ZBFS");*/
			domresult = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	
	public String queryZbglZbxqColumn2d(HttpServletRequest request, String json) {
		Connection conn = null;
		String domresult = "";
		String nd = request.getParameter("nd");
		try {
			conn = DBUtil.getConnection();
			PageManager page = new PageManager();
			String sql = Pub.getPropertiesString(propertiesFileName, "ZBGL_ZBXQ_Column2D");
			//拼接年度查询条件
			
			String nd2 = Pub.empty(nd)?"":" c.nd = '"+nd+"'";
			sql = sql.replaceAll("%Condition_2%", nd2);
			
			page.setPageRows(1000);
			conn.setAutoCommit(false);
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			domresult = bs.getJson();
			HashMap<String, Object> rowMap = new HashMap<String, Object>();
			rowMap.put("BREAKNUM", "every");
			rowMap.put("TOTALNUM", "3");
			rowMap.put("LINKFUNCTION", "javascript:zbxqcol");
			List paraList = new ArrayList(); 
			paraList.add("XH");
			paraList.add("LIEID");
			rowMap.put("LINKPARAM", paraList);
			// 覆盖默认属性，显示图例
			HashMap<String, Object> charMap = new HashMap<String, Object>();
			charMap.put("showlegend", "1");
			charMap.put("showvalues", "0");

			List list = new ArrayList();
			list.add(ChartUtil.chartWarnColor5);
			list.add(ChartUtil.chartColor4);
			list.add(ChartUtil.chartWarnColor1);
			rowMap.put("COLOR", list);
			domresult = ChartUtil.makeMSStackedColumn2DChartJsonString(domresult, charMap, rowMap);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domresult;
	}
	@Override
	public String queryZbsjList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		String tiaojian = request.getParameter("tiaojian");
		//页面查询条件施工和监理模糊查询
		String sgjddw= request.getParameter("SGJLDW");
		
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			String orderFilter = RequestUtil.getOrderFilter(json);
			//页面查询条件施工和监理模糊查询
			if(!Pub.empty(sgjddw)){
				condition=condition+" and (sg.dwmc like '%"+sgjddw+"%' or jl.dwmc like '%"+sgjddw+"%') ";
			}
			PageManager page = RequestUtil.getPageManager(json);
			String ndSql2 = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			
			String arr[] = proKey.split("\\*\\*");
			//子查询properties的路径
			//页面sql--arr[0]是链接到哪个页面，例如统筹计划、储备库等调用不同的sql的名称
			String querySql = Pub.getPropertiesSqlValue(propertyFileName, arr[0]);
			String condiSql = Pub.getPropertiesSqlValue(propertyFileName,arr[2]);
			//子查询sqlarr[1]是子查询sql的名称同bzfieldname
			if(!Pub.empty(tiaojian)&&!"null".equals(tiaojian)){
				condiSql=condiSql.replaceAll("%queryTiaojian%", tiaojian);
			}
			//替换查询条件
//			querySql = querySql.replaceAll("%queryCondition%", condition);
			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%ndCondition2%", ndSql2);
			querySql += condition;
			querySql += orderFilter;
			BaseResultSet bs = DBUtil.query(conn, querySql, page);

			bs.setFieldFileUpload("BJ","0015");//报建手续附件
			bs.setFieldDic("XMXZ", "XMXZ");
			bs.setFieldDic("ISBT", "SF");
			bs.setFieldDic("JLZBFS", "ZBFS");
			bs.setFieldDic("SGZBFS", "ZBFS");
			bs.setFieldThousand("JHGCTZ");
			bs.setFieldThousand("JHZCTZ");
			bs.setFieldThousand("JHQTTZ");
			bs.setFieldThousand("JHZTZE");
			bs.setFieldTranslater("XMGLGS", "FS_ORG_DEPT", "ROW_ID", "BMJC");//项目管理公司
			bs.setFieldTranslater("SGZBDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("JLZBDW", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String queryHtsjList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		String tiaojian = request.getParameter("tiaojian");
		//页面查询条件施工和监理模糊查询
		String sgjddw= request.getParameter("SGJLDW");
		
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			//页面查询条件施工和监理模糊查询
			if(!Pub.empty(sgjddw)){
				condition=condition+" and (sg.dwmc like '%"+sgjddw+"%' or jl.dwmc like '%"+sgjddw+"%') ";
			}
			PageManager page = RequestUtil.getPageManager(json);
			String ndSql = Pub.empty(nd) ? "" : " and t.qdnf='"+nd+"'";
			String ndSqlHt = Pub.empty(nd) ? "" : " and ht.qdnf='"+nd+"'";
			String ndSqlZb = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			String arr[] = proKey.split("\\*\\*");
			//子查询properties的路径
			//页面sql--arr[0]是链接到哪个页面，例如统筹计划、储备库等调用不同的sql的名称
			String querySql = Pub.getPropertiesSqlValue(propertyFileName, arr[0]);
			if(arr.length>4 && arr[4]!=null){
				//处理语句中同时存在以招投标和合同为查询条件的情况
				if("MULTI".equals(arr[4])){
					String condiSqlHT = Pub.getPropertiesSqlValue(propertyFileName,arr[2]+"_"+arr[3]+"_HT");
					querySql = querySql.replaceAll("%condition_HT%", condiSqlHT);
					String condiSqlZBSJ = Pub.getPropertiesSqlValue(propertyFileName,arr[2]+"_"+arr[3]+"_ZBSJ");
					querySql = querySql.replaceAll("%condition_ZBSJ%", condiSqlZBSJ);
				}else if("HT".equals(arr[4])){
					String condiSqlHT = Pub.getPropertiesSqlValue(propertyFileName,arr[2]+"_"+arr[3]+"_HT");
					querySql = querySql.replaceAll("%condition_HT%", condiSqlHT);
					querySql = querySql.replaceAll("%condition_ZBSJ%", "null");
				}else if("ZBSJ".equals(arr[4])){
					String condiSqlHT = Pub.getPropertiesSqlValue(propertyFileName,arr[2]+"_"+arr[3]+"_ZBSJ");
					querySql = querySql.replaceAll("%condition_ZBSJ%", condiSqlHT);
					querySql = querySql.replaceAll("%condition_HT%", "null");
				}else if("COMMON".equals(arr[4])){
					String lrbm = "";
					switch(Integer.parseInt(arr[3])){
						case 5:
							lrbm = "and ht.lrbm in('100000000016')";
							break;
						case 8:
							lrbm = "and ht.lrbm in('100000000014')";
							break;
						case 9:
							lrbm = "and ht.lrbm in('100000000015')";
							break;
						case 10:
							lrbm = "and ht.lrbm in('100000000005')";
							break;
						case 11:
							lrbm = "and ht.lrbm in('100000000017')";
							break;
						case 12:
							lrbm = "and ht.lrbm in('100000000004')";
							break;
						case 13:
							lrbm = "and ht.lrbm in('100000000009')";
							break;
						case 14:
							lrbm = "and ht.lrbm in('100000000006')";
							break;
					}
					String htzt = "";
					if("HTGL_BMLB_YINGQDHT".equals(arr[2])){
						htzt = " and ht.htzt in('-1','0','1','2') ";
					}else if("HTGL_BMLB_YIQDHT".equals(arr[2])){
						htzt = " and ht.htzt in('1','2') ";
					}else if("HTGL_BMLB_DQDHT".equals(arr[2])){
						htzt = " and ht.htzt in('-1','0') ";
					}
					String condiSqlHT = Pub.getPropertiesSqlValue(propertyFileName,"HTGL_BMLB_HT_COMMON");
					condiSqlHT = condiSqlHT.replaceAll("%lrbmCondition%", lrbm);
					condiSqlHT = condiSqlHT.replaceAll("%htztCondition%", htzt);
					querySql = querySql.replaceAll("%condition_ZBSJ%", "null");
					querySql = querySql.replaceAll("%condition_HT%", condiSqlHT);
				}
			}else{
				//替换合同语句
				String condiSql = Pub.getPropertiesSqlValue(propertyFileName,arr[2]);
				querySql = querySql.replaceAll("%condition_HT%", condiSql);
				querySql = querySql.replaceAll("%condition_ZBSJ%", "null");
			}
			//替换查询条件
			querySql = querySql.replaceAll("%Condition%", ndSql);
			querySql = querySql.replaceAll("%htCondition%", ndSqlHt);
			querySql = querySql.replaceAll("%zbCondition%", ndSqlZb);
			
			querySql += condition;
			BaseResultSet bs = DBUtil.query(conn, querySql, page);

			bs.setFieldDic("ZBLX", "ZBLX");
			bs.setFieldDic("ZBFS", "ZBFS");
			bs.setFieldDic("HTLX", "HTLX");
			bs.setFieldDic("HTZT", "HTRXZT");
			bs.setFieldThousand("ZZBJ");
			bs.setFieldThousand("ZHTQDJ");
			bs.setFieldTranslater("DSFJGID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			bs.setFieldTranslater("YFID", "GC_CJDW", "GC_CJDW_ID", "DWMC");
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
	@Override
	public String queryZBXQList(HttpServletRequest request, String json) {
		Connection conn = null;
		String domString = "";
		String nd = request.getParameter("nd");
		String proKey = request.getParameter("proKey");
		String tiaojian = request.getParameter("tiaojian");
		//页面查询条件施工和监理模糊查询
		String sgjddw= request.getParameter("SGJLDW");
		String arr[] = proKey.split("\\*\\*");
		String lie=arr[2];
		String hang=arr[3];
		String bmCondition="";
		String zblxCondition="";
		String xqztCondition="";
		String ndCondition="";
		String xqNDCondition="";
		String htlxCondition="";
		String htztCondition="";
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			if(lie.equals("HTGL_BMLB_ZBXQ")){
				//zblxCondition="";
				xqztCondition=" and XQZT in ('3','5','6') ";
			}
			if(lie.equals("HTGL_BMLB_LXCXZ")){
				//zblxCondition="";	
				xqztCondition=" and XQZT in ('5') ";
			}
			if(lie.equals("HTGL_BMLB_YWC")){
				//zblxCondition="";
				xqztCondition=" and XQZT in ('6') ";
			}
//			//合同判断
//			if(lie.equals("HTGL_BMLB_YINGQDHT")){
//				htztCondition=" and XQZT in ('-1','0','1','2') ";
//			}
//			if(lie.equals("HTGL_BMLB_YIQDHT")){
//				htztCondition=" and XQZT in ('1','2') ";
//			}
//			if(lie.equals("HTGL_BMLB_DQDHT")){
//				htztCondition=" and XQZT in ('-1','0') ";
//			}
			
			if(hang.equals("1")){
				bmCondition=" AND T.lrbm in(select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003') ";
				zblxCondition="and zblx in ('12','13','14','15','16','17','18','11','21')";
				
			}
			if(hang.equals("2")){
				bmCondition="  AND T.lrbm in(select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003')  ";
				zblxCondition="and zblx in ('12')";
			}
			if(hang.equals("3")){
				bmCondition=" AND T.lrbm in(select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003') ";
				zblxCondition="and zblx in ('13')";
			}
			if(hang.equals("4")){
				bmCondition=" AND T.lrbm in(select ROW_ID from fs_org_dept where EXTEND1='1' or ROW_ID='100000000003') ";
				zblxCondition="and zblx in ('14','15','16','17','18','11','21')";
//				htlxCondition=" and ht.htlx in ('ZJWT','XY','QT','QQ','PQ','JC','DJ','CQ','BC','LDLH') ";
			}
			if(hang.equals("5")){
				bmCondition=" AND T.LRBM = '100000000016' ";
			}
			if(hang.equals("6")){
				bmCondition=" AND T.LRBM = '100000000016'  ";
				zblxCondition="and zblx in ('11')";
//				htlxCondition=" and ht.htlx in ('SJ') ";
			}
			if(hang.equals("7")){
				bmCondition=" AND T.LRBM = '100000000016' ";
				zblxCondition="and zblx in ('12','13','14','15','16','17','18','21')";
//				htlxCondition=" and ht.htlx in ('ZJWT','XY','SG','QT','QQ','PQ','JL','JC','DJ','CQ','BC','LDLH') ";
			}
			if(hang.equals("8")){
				bmCondition=" AND T.LRBM = '100000000014' ";
			}
			if(hang.equals("9")){
				bmCondition=" AND T.LRBM = '100000000015' ";
			}
			if(hang.equals("10")){
				bmCondition=" AND T.LRBM = '100000000005' ";
			}
			if(hang.equals("11")){
				bmCondition=" AND T.LRBM = '100000000017' ";
			}
			if(hang.equals("12")){
				bmCondition=" AND T.LRBM = '100000000004' ";
			}
			if(hang.equals("13")){
				bmCondition=" AND T.LRBM = '100000000009' ";
			}
			if(hang.equals("14")){
				bmCondition=" AND T.LRBM = '100000000006' ";
			}
			ndCondition = Pub.empty(nd) ? "" : " and t.nd='"+nd+"'";
			xqNDCondition = Pub.empty(nd) ? "" : " and X.nd='"+nd+"'";
		
			
			//前台查询条件
			String condition =RequestUtil.getConditionList(json).getConditionWhere();
			if(!Pub.empty(condition)){
				condition=" and  "+condition;
			}
			//页面查询条件施工和监理模糊查询
			if(!Pub.empty(sgjddw)){
				condition=condition+" and (sg.dwmc like '%"+sgjddw+"%' or jl.dwmc like '%"+sgjddw+"%') ";
			}
			PageManager page = RequestUtil.getPageManager(json);
		
			
			
			
			//子查询properties的路径
			//页面sql--arr[0]是链接到哪个页面，例如统筹计划、储备库等调用不同的sql的名称
			String querySql = Pub.getPropertiesSqlValue("com.ccthanking.properties.business.bzjklj.lj_list", arr[0]);
			String condiSql = Pub.getPropertiesSqlValue(propertyFileName,"HTGL_BMLB_ZBXQ");
			
			condiSql= condiSql.replaceAll("%bmCondition%", bmCondition);
			condiSql= condiSql.replaceAll("%zblxCondition%", zblxCondition);
			condiSql= condiSql.replaceAll("%xqztCondition%", xqztCondition);
			condiSql= condiSql.replaceAll("%ndCondition%", ndCondition);
//			condiSql= querySql.replaceAll("%htlxCondition%", htlxCondition);
//			condiSql= querySql.replaceAll("%htztCondition%", htztCondition);
			querySql = querySql.replaceAll("%condition_sql%", condiSql);
			querySql = querySql.replaceAll("%xndCondition%", xqNDCondition);
			querySql = querySql.replaceAll("%queryCondition%", condition);
			//替换查询条件
			//querySql = querySql.replaceAll("%Condition%", ndSql);
//			querySql += condition;
			BaseResultSet bs = DBUtil.query(conn, querySql, page);

			bs.setFieldDic("ZBLX", "ZBLX");//招标类型
			bs.setFieldDic("ZBFS", "ZBFS");//招标方式
			bs.setFieldDic("XQZT", "SF");//招标方式
			bs.setFieldDic("XQZT", "XQZT");//垫资方式
			bs.setFieldDic("TBJFS", "TBBJFS");//投标报价方式
			bs.setFieldThousand("YSE");//预算额
			bs.setFieldSjbh("sjbh");//事件编号
			bs.setFieldTranslater("JLDWID", "GC_CJDW", "GC_CJDW_ID", "DWMC"); // 监理单位
			bs.setFieldTranslater("LRBM", "FS_ORG_DEPT", "ROW_ID", "BMJC");	// 录入部门
			domString = bs.getJson();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.closeConnetion(conn);
		}
		return domString;
	}
}
