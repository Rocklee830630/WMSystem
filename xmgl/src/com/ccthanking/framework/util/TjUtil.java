package com.ccthanking.framework.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccthanking.framework.common.DBUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author hongpeng.dong
 *
 */
public class TjUtil {

	/**
	 * 获取List中指定key等于指定value的数据
	 * @param list 包含map数组的结果集，对应DBUtil中List<Map<String, String>> queryReturnList(Connection conn, String sql)方法结果集
	 * @param iKey 数据项
	 * @param iValue	数据值
	 * @return
	 */
	public static List<Map<String, String>> getDataEqual(List<Map<String, String>> list,String iKey,String iValue){
		return getDataDefineEqual(list,iKey,iValue,true);
	}
	
	/**
	 * 获取List中指定key不等于指定value的数据
	 * @param list 包含map数组的结果集，对应DBUtil中List<Map<String, String>> queryReturnList(Connection conn, String sql)方法结果集
	 * @param iKey 数据项
	 * @param iValue	数据值
	 * @return
	 */
	public static List<Map<String, String>> getDataNotEqual(List<Map<String, String>> list,String iKey,String iValue){
		return getDataDefineEqual(list,iKey,iValue,false);
	}
	
	private static List<Map<String, String>> getDataDefineEqual(List<Map<String, String>> list,String iKey,String iValue,boolean equal){
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for(Map<String, String> map : list){
			if(!map.containsKey(iKey) || null == map.get(iKey)){continue;}
			if(equal && map.get(iKey).equals(iValue)){
				result.add(map);
			}else if(!equal && !map.get(iKey).equals(iValue)){
				result.add(map);
			}
		}
		return result;
	}
	
	/**
	 * 获取List中指定key包含指定value的数据，相关于sql中的in
	 * @param list 包含map数组的结果集，对应DBUtil中List<Map<String, String>> queryReturnList(Connection conn, String sql)方法结果集
	 * @param iKey
	 * @param iValues value数组
	 * @return
	 */
	public static List<Map<String, String>> getDataIn(List<Map<String, String>> list,String iKey,String[] iValues){
		return getDataDefineIn(list,iKey,iValues,true);
	}
	
	/**
	 * 获取List中指定key不包含指定value的数据，相关于sql中的 not in
	 * @param list 包含map数组的结果集，对应DBUtil中List<Map<String, String>> queryReturnList(Connection conn, String sql)方法结果集
	 * @param iKey
	 * @param iValues value数组
	 * @return
	 */
	public static List<Map<String, String>> getDataNotIn(List<Map<String, String>> list,String iKey,String[] iValues){
		return getDataDefineIn(list,iKey,iValues,false);
	}
	
	
	public static List<Map<String, String>> getDataDefineIn(List<Map<String, String>> list,String iKey,String[] iValues,boolean in){
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for(Map<String, String> map : list){
			if(!map.containsKey(iKey)){continue;}
			if(in && 0 <= Arrays.binarySearch(iValues,map.get(iKey)) ){
				list.add(map);
			}else if(!in && 0 > Arrays.binarySearch(iValues,map.get(iKey))){
				list.add(map);
			}
		}
		return result;
	}
	
	
	/**
	 * sql中的groupby功能
	 * @param list 包含map数组的结果集，对应DBUtil中List<Map<String, String>> queryReturnList(Connection conn, String sql)方法结果集
	 * @param iKey 分组字段
	 * @return
	 */
	public static Map<String,List> getDataGroupBy(List<Map<String, String>> list,String iKey){
		
		Map<String,List> resultMap = new HashMap<String,List>();
		
		String groupByPer = null;//分组中的单个组
		List<Map<String, String>> perList = null;//分组后单个组对应的数据
		for(Map<String, String> map : list){
			if(!map.containsKey(iKey)){continue;}
			groupByPer = map.get(iKey);
			if(resultMap.containsKey(groupByPer)){
				perList = resultMap.get(groupByPer);
				perList.add(map);
			}else{
				perList = new ArrayList<Map<String, String>>();
				perList.add(map);
				resultMap.put(groupByPer,perList);
			}
		}
		
		return resultMap;
	}
	
	
	/**
	 * 冒泡针对groupby的结果排序（升序），得到排序后的数组
	 * @param iMap 要排序的Map数据
	 * @return
	 */
	public static Object[] sortGroupByData(Map<String,List> iMap){
		
		Object[] result = iMap.keySet().toArray();
		int num = result.length;
		String temp = null;
		
		 for(int i=0;i<num-1;i++){  
		       for(int j=0;j<num-1-i;j++){  
		         if(iMap.get(result[j]).size() >iMap.get(result[j+1]).size()){  
		           temp = (String)result[j];  
		           result[j] = result[j+1];  
		           result[j+1] = temp;  
		         }  
		       }  
		    }  
		
		return result;
	}
	public static JSONObject getConditionByFieldids(String fieldids) throws Exception{
		Connection conn = DBUtil.getConnection();
		JSONObject result = new JSONObject();
		try{
			JSONObject conditions = new JSONObject();
			JSONArray condArr = new JSONArray();
			JSONObject condCommonObj = new JSONObject();
			condCommonObj.put("value", "1000");
			condCommonObj.put("fieldname", "rownum");
			condCommonObj.put("operation", "<=");
			condCommonObj.put("logic", "and");
			condArr.add(condCommonObj);
			if(!Pub.empty(fieldids)){
				JSONObject condObj = new JSONObject();
				String sql = "select FS_DEPT_MONITOR_ELEMENT_ID,FIELDID,FIELDTITLE,FIELDNAME,OPERATION,FIELDVALUE,FIELDTYPE,ORDERNUM,CLASS,DISPLAY " +
						"from FS_DEPT_MONITOR_ELEMENT ";
				List<Map<String, String>> wtList = new ArrayList<Map<String, String>>();
				if(fieldids.indexOf("@")!=-1){
					fieldids = fieldids.replace("@", "','");
					String execSql = sql + "where FIELDID in ('"+fieldids+"')";
					wtList = DBUtil.queryReturnList(conn, execSql);
				}else{
					String execSql = sql + "where FIELDID='"+fieldids+"'";
					wtList = DBUtil.queryReturnList(conn, execSql);
				}
				if(wtList!=null){
					for(int j=0;j<wtList.size();j++){
						condObj.put("value",wtList.get(j).get("FIELDVALUE"));
						condObj.put("fieldname",wtList.get(j).get("FIELDNAME"));
						condObj.put("operation",wtList.get(j).get("OPERATION"));
						condObj.put("fieldtype",wtList.get(j).get("FIELDTYPE") );
						condObj.put("logic","and");
						condArr.add(condObj);
					}
				}
			}
			conditions.put("conditions", condArr);
			result.put("querycondition", conditions);
		}catch(Exception e){
			e.printStackTrace();
			conn.rollback();
			DBUtil.closeConnetion(conn);
		}finally{
			DBUtil.closeConnetion(conn);
		}
		return result;
	}
}
