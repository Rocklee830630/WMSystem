package com.ccthanking.business.wttb.bmgk;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import com.ccthanking.common.BusinessUtil;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.framework.util.TjUtil;

/**
 * @author hongpeng.dong
 * 
 */
public class Tj_Wttb {

	// 问题提报正常统计使用数据
	public static final String WTTB_NORMAL_SQL = 
		"select distinct I.wttb_info_id, I.jhsjid, I.wtlx, I.wtbt, I.yjsj, I.sjsj, I.sjzt, I.wtxz, I.cqbz, I.lrr, I.lrsj, I.lrbm, I.lrbmmc, I.gxr, I.gxsj, I.gxbm, I.gxbmmc, I.ywlx, I.sjbh, I.sjmj, I.sfyx,I.xmmc,I.bdmc " +
		",'' BLQK,L.JSR,XWWCSJ,L.JSBM ,P.NAME as LRRMC,CBCS,D1.EXTEND1 D1EXTEND1,D2.EXTEND1 D2EXTEND1,decode(JHSJID,null,'0','1') SJXM,HFQK,MYCD, " +
		"case when D2.ROW_ID='100000000000' and L.JSR not in (select ACCOUNT from VIEW_ZXZR) then '1' else '0' end as CJGJS, " +
		"case when D2.EXTEND1='1' and L.JSR not in (select ACCOUNT from VIEW_ZXZR) then '1' else '0' end as XMGLGSJS," +
		"case when D2.EXTEND1!='1' and L.JSR not in (select ACCOUNT from VIEW_ZXZR) then '1' else '0' end as ZXGBMJS," +
		"case when L.JSR in (select ACCOUNT from VIEW_ZXZR) then '1' else '0' end as ZXLDJS " +
		" from WTTB_INFO I,(select WTID,JSR,JSBM,XWWCSJ,HFQK,MYCD " +
		"from WTTB_LZLS L " +
		"where L.BLRJS = '1') L,FS_ORG_PERSON P,FS_ORG_DEPT D1,FS_ORG_DEPT D2 ";
	public static final String WTTB_COND_SQL = " and I.SFYX='1' and I.WTTB_INFO_ID=L.WTID(+) and I.LRR=P.ACCOUNT(+) and D1.ROW_ID=I.LRBM and D2.ROW_ID=L.JSBM and I.SJZT in ('2','3','4','6')";
	
	/**
	 * 获取问题提报全部正常有效数据
	 */
	public static List<Map<String, String>> getWttbData(String fieldids) {
		String strSql = "";
		try{
			JSONObject condObj = TjUtil.getConditionByFieldids(fieldids);
			String json = condObj.toString();
			PageManager page = RequestUtil.getPageManager(json);
			String condition = RequestUtil.getConditionList(json).getConditionWhere();
			String roleCond = "";
			//发起人查询
			String sql = WTTB_NORMAL_SQL;
			roleCond = WTTB_COND_SQL;
			condition +=roleCond;
			page.setFilter(condition);
			strSql = sql + " where " + condition ;
//			System.out.println("strSql>>"+strSql);
		}catch(Exception e){
			
		}
		return getData(strSql,null);
	}
	
	/**
	 * 按年度获取问题提报全部正常有效数据
	 */
	public static List<Map<String, String>> getWttbYearData(String year) {
		String[] paras = {year};
		return getData(WTTB_NORMAL_SQL + " and to_char(LRSJ,'yyyy') = ?",paras);
	}
	
	
	
	
	@SuppressWarnings("finally")
	public static List<Map<String, String>> getData(String sql,String paras[]) {
		List<Map<String, String>> wtList = new ArrayList<Map<String, String>>();
		try {
			
			wtList = DBUtil.queryReturnList(DBUtil.getConnection(), sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			return wtList;
		}
	}
	@SuppressWarnings("finally")
	public static List<Map<String, String>> getDataByConnection(String sql,JSONObject obj) {
		List<Map<String, String>> wtList = new ArrayList<Map<String, String>>();
		try {
			
			wtList = DBUtil.queryReturnList(DBUtil.getConnection(), sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			return wtList;
		}
	}
	public static String getConditionTj(List<Map<String, String>> data,String title,String code,String codeType){
		String result = "";
		try{
			if(null == data || 0 == data.size()){return result;}
			Map<String,List> groupData = TjUtil.getDataGroupBy(data, code);
			Object maps[] = TjUtil.sortGroupByData(groupData);
			if(null == maps || 0 == maps.length){return result;}
			result = "<div class='span3'><span class='title'>"+title+"：</span>";
			
			for (int i=0;i<maps.length;i++) {
				   String key = (String)maps[i];
				   result += "<span>";
				   result += Pub.getValueByCode(code, key);
				   result += "（"+Integer.toString(groupData.get(key).size())+"）";
				   result += "</span>";
			 }
			result += "</div>";
		}catch(Exception e){
			
		}finally{
			return result;
		}
	}
	
	

}

