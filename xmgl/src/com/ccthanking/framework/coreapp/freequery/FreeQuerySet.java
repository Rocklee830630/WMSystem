//v20071212-1
package com.ccthanking.framework.coreapp.freequery;

/*
 * author : michael wang
 * create time : 2007.04.08
 * you are free to copy,modify,rewrite this code,but please you reserve author's name.
 *
 * version 1.0 2007.04.08 create
 *         1.1 2007.05.03 apply for jwzh
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.util.Pub;

public class FreeQuerySet {


	private FieldVO arrFieldVO[];
	private String arrData[][];
	private int columnCount;
	private int recordCount;

	private ArrayList alColumnList = new ArrayList();

	public FreeQuerySet()
	{
		columnCount = 0;
		recordCount = 0;
	}

//	public void addColumn(String p_ColumnName){
//
//		alColumnList.add(p_ColumnName);
//
//	}

    public FieldVO[] getFields()
    {
        return this.arrFieldVO;
    }

    public String[][] getData()
    {
        return this.arrData;
    }

	public void addColumn(FieldVO p_VO){

		alColumnList.add(p_VO);

	}

	public void clearColumn(){

		alColumnList.clear();

	}

	public String[][] getQueryData(Connection conn, String sql)
    {
        if (sql == null)
            return null;
        if (conn == null)
            return null;
        ResultSet rs = null;
        Statement stmt = null;
        ResultSetMetaData md = null;
        ArrayList aList = new ArrayList();
        int rows = 0, cols;
        arrFieldVO = null;
        arrData = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            md = rs.getMetaData();
            cols = md.getColumnCount();

            arrFieldVO = new FieldVO[md.getColumnCount()];

            for (int i = 0; i < md.getColumnCount(); i++)
            {
            	FieldVO tmpFieldInfo;
            	if ( i < alColumnList.size() ){
            		tmpFieldInfo = (FieldVO) alColumnList.get(i);
            	}
            	else{
            		tmpFieldInfo = new FieldVO();
            	    tmpFieldInfo.setName(md.getColumnName(i+1));
            	    tmpFieldInfo.setType("1");
            	}
            	tmpFieldInfo.setIndex(i);
//            	tmpFieldInfo.colType = md.getColumnTypeName(i+1);
            	arrFieldVO[i] = tmpFieldInfo;
            }

            while (rs.next())
            {
                String[] row = new String[md.getColumnCount() + 1];
                for (int i = 0; i < md.getColumnCount(); i++)
                {
                	int columnType = md.getColumnType(i + 1);
                	if ( columnType == Types.DATE || columnType == Types.TIME || columnType == Types.TIMESTAMP){
               		    java.sql.Timestamp dateValue = rs.getTimestamp(i + 1);
    		    	    if (dateValue != null){
    		    		   java.util.Date saveDate = dateValue;
    		    		   java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
    		    		   row[i]= dateFormat.format(saveDate);
    		    	    }
    		    	}
                	else if (columnType == Types.BLOB || columnType == Types.CLOB)
                		row[i]= "";
                	else
                       row[i] = rs.getString(i + 1);
                    row[i] = row[i] == null ? "" : row[i];
                }
                aList.add(row);
            }
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
        }
        catch (Exception e)
        {

            try
            {
            	arrFieldVO = null;
            	arrData = null;
            	aList.clear();
            	aList = null;
            	columnCount = 0;
        		recordCount = 0;
                if (rs != null)
                {
                    rs.close();
                    rs = null;
                }
                if (stmt != null)
                {
                    stmt.close();
                    stmt = null;
                }
                e.printStackTrace(System.out);
            }
            catch (Exception ee)
            {
                return null;
            }
            return null;
        }

        rows = aList.size();
        if (rows == 0 || cols == 0)
        {
        	arrFieldVO = null;
        	arrData = null;
            aList.clear();
            aList = null;
            columnCount = 0;
    		recordCount = 0;
            return null;
        }

        columnCount = cols;
        recordCount = rows;


        arrData = new String[rows][cols];
        for (int i = 0; i < rows; i++)
        {
            Object[] row = (Object[]) aList.toArray()[i];
            for (int j = 0; j < cols; j++)
            {
            	arrData[i][j] = (String) row[j];
            }
        }
        aList.clear();
        aList = null;
        return arrData;
    }

	public boolean execQuery(String p_SQL,String p_DS)
	{
		boolean bResult = false;
		Connection conn = null;

		if (!"".equals(p_DS))
			conn = DBUtil.getConnection(p_DS);
		else
			conn = DBUtil.getConnection();

        bResult = execQuery(conn,p_SQL);
        try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {

		}
		return bResult;
	}

	public boolean execQuery(Connection conn, String sql)
    {
		boolean bStatus = false;
        if (sql == null)
            return bStatus;
        if (conn == null)
            return bStatus;
        ResultSet rs = null;
        Statement stmt = null;
        ResultSetMetaData md = null;
        ArrayList aList = new ArrayList();
        int rows = 0, cols;
        arrFieldVO = null;
        arrData = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            md = rs.getMetaData();
            cols = md.getColumnCount();

            arrFieldVO = new FieldVO[md.getColumnCount()];

            for (int i = 0; i < md.getColumnCount(); i++)
            {
            	FieldVO tmpFieldInfo;
            	if ( i < alColumnList.size() ){
            		tmpFieldInfo = (FieldVO) alColumnList.get(i);
            	}
            	else{
            		tmpFieldInfo = new FieldVO();
            	    tmpFieldInfo.setName(md.getColumnName(i+1));
            	    tmpFieldInfo.setType("1");
            	}
            	tmpFieldInfo.setIndex(i);
//            	tmpFieldInfo.colType = md.getColumnTypeName(i+1);
            	arrFieldVO[i] = tmpFieldInfo;
            }

            while (rs.next())
            {
                String[] row = new String[md.getColumnCount() + 1];
                for (int i = 0; i < md.getColumnCount(); i++)
                {
                	int columnType = md.getColumnType(i + 1);
                	if ( columnType == Types.DATE || columnType == Types.TIME || columnType == Types.TIMESTAMP){
               		    java.sql.Timestamp dateValue = rs.getTimestamp(i + 1);
    		    	    if (dateValue != null){
    		    		   java.util.Date saveDate = dateValue;
    		    		   java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
    		    		   row[i]= dateFormat.format(saveDate);
    		    	    }
    		    	}
                	else if (columnType == Types.BLOB || columnType == Types.CLOB)
                		row[i]= "";
                	else
                       row[i] = rs.getString(i + 1);
                    row[i] = row[i] == null ? "" : row[i];
                    //add by wuxp �˴������ⲿ���Դ����Ϣ����
                    if(!Pub.empty(arrFieldVO[i].getDicTable()))
                    {
                        if(!Pub.empty(arrFieldVO[i].getKeyField())&&!Pub.empty(arrFieldVO[i].getValueField()) )
                        {
                            String sqlDic="SELECT "+arrFieldVO[i].getValueField()+" FROM "+arrFieldVO[i].getDicTable()+" WHERE "+arrFieldVO[i].getKeyField()+" = ?";
                            QuerySet qs = DBUtil.executeQuery(sqlDic,new Object[]{row[i]},conn);
                            if(qs.getRowCount()>0)
                            {
                                row[i]=qs.getString(1,1);
                            }
                        }
                    }
                    //add by wuxp
                }
                aList.add(row);
            }
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
        }
        catch (Exception e)
        {
            try
            {
            	arrFieldVO = null;
            	arrData = null;
            	aList.clear();
            	aList = null;
            	columnCount = 0;
        		recordCount = 0;
                if (rs != null)
                {
                    rs.close();
                    rs = null;
                }
                if (stmt != null)
                {
                    stmt.close();
                    stmt = null;
                }
                e.printStackTrace(System.out);
            }
            catch (Exception ee)
            {
                return bStatus;
            }
            return bStatus;
        }

        rows = aList.size();
        if (rows == 0 || cols == 0)
        {
        	arrFieldVO = null;
        	arrData = null;
            aList.clear();
            aList = null;
            columnCount = 0;
    		recordCount = 0;
            return bStatus;
        }

        columnCount = cols;
        recordCount = rows;

        arrData = new String[rows][cols];
        for (int i = 0; i < rows; i++)
        {
            Object[] row = (Object[]) aList.toArray()[i];
            for (int j = 0; j < cols; j++)
            {
            	arrData[i][j] = (String) row[j];
            }
        }
        bStatus = true;
        aList.clear();
        aList = null;
        return bStatus;
    }

	public String getValue(int p_Row,int p_Col){

		String strResult = null;

		if (p_Row < 0 || p_Row >= recordCount)
			return strResult;
		if (p_Col < 0 || p_Col >= columnCount)
			return strResult;
		if (arrData != null)
		  strResult = arrData[p_Row][p_Col];

        return strResult;
	}


	public String getValue(int p_Row,String p_Field)
	{
        String strResult = null;

		if (p_Row < 0 || p_Row >= recordCount)
			return strResult;

		int column = -1;
		if (arrFieldVO != null) {
			for (int i = 0; i < columnCount; i++) {
				if (p_Field.equalsIgnoreCase(arrFieldVO[i].getField())) {
					column = arrFieldVO[i].getIndex();
					break;
				}
			}
		}

		if (arrData != null && column >=0){
			strResult = arrData[p_Row][column];
		}

		return strResult;
	}

	public int getColumns(){
		return columnCount;
	}

	public int getRows(){
		return recordCount;
	}
}
