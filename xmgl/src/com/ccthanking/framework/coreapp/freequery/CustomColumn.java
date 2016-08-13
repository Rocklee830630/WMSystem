//v20071212-1
//v20090110-2
//v20090306-3
package com.ccthanking.framework.coreapp.freequery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.ccthanking.framework.common.DBUtil;

public class CustomColumn {
	
	public CustomColumn(){
		
	}
	
	public ArrayList getCustomColumnList(ArrayList p_PartDispList,String p_QueryID,String p_UserName){
		   
		   ArrayList  alColumnList = new ArrayList();
		   Connection conn = DBUtil.getConnection();
		   Statement stmt = null;
		   String strSelectColumns = "";   
		    try {
				String strSQL = "SELECT columnset FROM "+FreeQueryProps.FREE_CUSTOM+" WHERE queryid='"
					          + p_QueryID + "' AND account='" + p_UserName + "'";
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(strSQL);
				if (rs.next())
					strSelectColumns = rs.getString(1);
			    if (strSelectColumns == ""){
			    	for(int i = 0; i < p_PartDispList.size(); i++){
			    		alColumnList.add(String.valueOf(i));
			    	}
			    }
			    else{
			    	String[] arrColumn;// 0=0;3=1; 0:false 1:true;
			    	arrColumn = strSelectColumns.split(";");
			    	if (p_PartDispList.size() == arrColumn.length){
				    	for (int i = 0 ; i < arrColumn.length;i++){
				    		
				    		String[] arrColInfo = arrColumn[i].split("=");
				    		String strColIndex = arrColInfo[0];
				    		String strColStatus = arrColInfo[1];
				    		int intColIndex = Integer.parseInt(strColIndex); 
				    		if (intColIndex >= p_PartDispList.size()){
				    			arrColInfo = null;
				    			continue;
				    		}
				    		
							if ("1".equals(strColStatus))
								alColumnList.add(String.valueOf(intColIndex));
							arrColInfo = null;
				    	}
			    	}
			    	else{
			    		for(int i = 0; i < p_PartDispList.size(); i++){
			    			alColumnList.add(String.valueOf(i));
				    	}
			    	}
			    	arrColumn = null;
			    }
		    }
		    catch(SQLException e){
		    	e.printStackTrace();
		    }
		    finally{
		    	try {
					if (stmt != null)
						stmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
		   
		   return alColumnList; 
	   }
}
