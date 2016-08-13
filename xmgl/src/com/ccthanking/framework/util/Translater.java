package com.ccthanking.framework.util;

import java.sql.Connection;
import java.util.HashMap;

import com.ccthanking.framework.common.DBUtil;

public class Translater {
	private String tableName;
	private String codeField;
	private String valueField;
	private HashMap ids=new HashMap();
	public Translater(String tableName, String codeField, String valueField) {
		super();
		this.tableName = tableName;
		this.codeField = codeField;
		this.valueField = valueField;
	}
	
	public String translate(String code) {
		// TODO Auto-generated method stub
		String result=code;
		if (ids.containsKey(code)){
			result=(String)ids.get(code);		
		}else{
			Connection conn = DBUtil.getConnection();
			try{
				String sql="select "+valueField+" from "+tableName+" t where t."+codeField+"='"+code+"'";
				String[][] list=new String[0][0];
			    list = DBUtil.querySql(conn, sql);
			    if (list!=null && list.length>0){
			    	result=list[0][0].replaceAll(" ", "");//处理掉数据中可能存在的空格
			    	ids.put(code, result);
			    }			    					
			}catch(Exception e){
				result=code;
				e.printStackTrace();
			}finally{				
				try{
					if (conn!=null)
						conn.close();
				}catch(Exception e){
					e.printStackTrace();
				}				
			}	
		}
			
		
		return result;
	}
}
