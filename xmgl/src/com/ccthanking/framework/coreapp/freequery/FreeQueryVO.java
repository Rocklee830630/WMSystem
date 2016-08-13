//v20071212-1
//v20090110-2
//v20090306-3
package com.ccthanking.framework.coreapp.freequery;

/*
 * author : michael wang
 * create time : 2006.06.01
 * you are free to copy,modify,rewrite this code,but please you reserve author's name.
 * 
 * version 1.0 2006.06.01 create
 *         1.1 2007.05.03 apply for jwzh 
 */

	 
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.ccthanking.framework.Constants;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;


public class FreeQueryVO {
	private String strDeptID = "";
    private String strUserID = "";
    private String strUserName = "";
    private String strTableLevel = "";
    private String strTableTitle = "";
    private String strMemo = "";
    private String strHtmlXML = "";
    private String strTableXML = "";
    private String strFieldXML = "";
    private String strRelationXML = "";
    private String strFilterXML = "";
    private String strTitle = "";
    private String strMenu = "";
    private String strParentMenu = "";
    private String strImageXML = "";
    private String strJoinXML = "";
    
    public FreeQueryVO()
    {
    	
    }
    
    public String getDeptID() {
		return strDeptID;
	}

	public String getFieldXML() {
		return strFieldXML;
	}

	public String getFilterXML() {
		return strFilterXML;
	}

	public String getHtmlXML() {
		return strHtmlXML;
	}

	public String getMemo() {
		return strMemo;
	}

	public String getRelationXML() {
		return strRelationXML;
	}

	public String getTableLevel() {
		return strTableLevel;
	}

	public String getTableTitle() {
		return strTableTitle;
	}

	public String getTableXML() {
		return strTableXML;
	}

	public String getUserID() {
		return strUserID;
	}

	public String getUserName() {
		return strUserName;
	}
	
	public String getTitle() {
		return strTitle;
	}

	public String getMenuName()
	{
		return strMenu;
	}
	
	public String getParentMenuName()
	{
		return strParentMenu;
	}
	
	public String getImageXML()
	{
		return strImageXML;
	}
	
	public String getJoinXML(){
		return strJoinXML;
	}
	
//	public void setDeptID(String p_DeptID) {
//		strDeptID = p_DeptID;
//	}
//
//	public void setFieldXML(String p_FieldXML) {
//		strFieldXML = p_FieldXML;
//	}
//
//	public void setFilterXML(String p_FilterXML) {
//		strFilterXML = p_FilterXML;
//	}
//
//	public void setHtmlXML(String p_HtmlXML) {
//		strHtmlXML = p_HtmlXML;
//	}
//
//	public void setMemo(String p_Memo) {
//		strMemo = p_Memo;
//	}
//

//	public void setRelationXML(String p_RelationXML) {
//		strRelationXML = p_RelationXML;
//	}
//
//	public void setTableLevel(String p_TableLevel) {
//		strTableLevel = p_TableLevel;
//	}
//
//	public void setTableTitle(String p_TableTitle) {
//		strTableTitle = p_TableTitle;
//	}
//
//	public void setTableXML(String p_TableXML) {
//		strTableXML = p_TableXML;
//	}
//
//	public void setUserID(String p_UserID) {
//		strUserID = p_UserID;
//	}
//
//	public void setUserName(String p_UserName) {
//		strUserName = p_UserName;
//	}
//
//	public void setTitle(String p_Title) {
//		strTitle = p_Title;
//	}
	
//	public void setMenuName(String p_MenuName) {
//		strMenu = p_MenuName;
//	}
//
//	public void setParentMenuName(String p_ParentMenuName) {
//		strParentMenu = p_ParentMenuName;
//	}	
	

	public void executeInsert(Connection p_Conn,Object[] p_ArrFieldValue,
			                  String p_BlobInfo,String p_StrSEQ)
			throws Exception {
		java.sql.PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		OutputStream os = null;
		Object objValue;
		String strInsert = " INSERT INTO "+FreeQueryProps.FREE_QUERY+"(id, deptid,userid, username ,tablelevel" 
		    + ",tableinfo,tabletitle,createtime,memo,hasrelate) VALUES(" + p_StrSEQ 
            + ",?,?,?,?,EMPTY_BLOB(),?,sysdate,?,?)";
        String strUpdateBlob = "SELECT tableinfo FROM "+FreeQueryProps.FREE_QUERY+" WHERE id = " + p_StrSEQ;

		try {
			ps = p_Conn.prepareStatement(strInsert);
			for (int i = 0; i < p_ArrFieldValue.length; i++) {
				objValue = p_ArrFieldValue[i];
				if (objValue != null)
					ps.setObject(i + 1, objValue);
				else
					ps.setNull(i + 1, java.sql.Types.VARCHAR);
			}
			ps.execute();
			stmt = p_Conn.createStatement();
			
			rs = stmt.executeQuery(strUpdateBlob);
			if (rs.next()) {
				// weblogic 处理BLOB类型字段
				// weblogic.jdbc.common.OracleBlob dbBlob;
				// dbBlob = (weblogic.jdbc.common.OracleBlob) rs.getBlob(1);
				// 其它处理BLOB类型字段
				oracle.sql.BLOB dbBlob = null;
				dbBlob = (oracle.sql.BLOB) rs.getBlob(1);

				os = dbBlob.getBinaryOutputStream();
//				os.write(p_BlobInfo.getBytes("utf-8"));
				os.write(p_BlobInfo.getBytes("GBK"));
				os.close();
			}
		} catch (java.sql.SQLException e) {
			System.out.println(strUpdateBlob);
			e.printStackTrace();
			throw e;
		} finally {
			if (os != null)
				os.close();
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
	}

	public void executeUpdate(Connection p_Conn,Object[] p_ArrFieldValue,
                              String p_BlobInfo,String p_StrSEQ)
			throws Exception {
		java.sql.PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		OutputStream os = null;
		Object objValue;

		String strUpdate = "UPDATE "+FreeQueryProps.FREE_QUERY+" SET deptid = ?,userid= ? ,username = ?,tablelevel = ?" 
	 	    + ",tableinfo = EMPTY_BLOB(),tabletitle = ?, memo = ? ,hasrelate = ?" 
	 	    + " WHERE id = " + p_StrSEQ;
        String strUpdateBlob = "SELECT tableinfo FROM "+FreeQueryProps.FREE_QUERY+" WHERE id = " + p_StrSEQ;
		try {
			ps = p_Conn.prepareStatement(strUpdate);
			for (int i = 0; i < p_ArrFieldValue.length; i++) {
				objValue = p_ArrFieldValue[i];
				if (objValue != null)
					ps.setObject(i + 1, objValue);
				else
					ps.setNull(i + 1, java.sql.Types.VARCHAR);
			}
			ps.execute();
			stmt = p_Conn.createStatement();
			rs = stmt.executeQuery(strUpdateBlob);
			if (rs.next()) {
				// weblogic 处理BLOB类型字段
				// weblogic.jdbc.common.OracleBlob dbBlob;
				// dbBlob = (weblogic.jdbc.common.OracleBlob) rs.getBlob(1);
				// 其它处理BLOB类型字段
				oracle.sql.BLOB dbBlob = null;
				dbBlob = (oracle.sql.BLOB) rs.getBlob(1);

				os = dbBlob.getBinaryOutputStream();
				try {
//					os.write(p_BlobInfo.getBytes("utf-8"));
					os.write(p_BlobInfo.getBytes("GBK"));
					os.close();
				} catch (IOException ex) {
					ex.printStackTrace();
					throw ex;
				}
			}
		} catch (java.sql.SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (os != null)
				os.close();
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
	}
	
	public String getFieldList(Connection conn, List list)
    {
        String strResult = "";
        String condition = " t1.table_id = t2.table_id ";
		Iterator itor = list.iterator();

		StringBuffer tempBuffer = new StringBuffer();// AND t1.table_id IN (
		while (itor.hasNext()) {
			Element row = (Element) itor.next();
			if (tempBuffer.length() > 0)
				tempBuffer.append(",");
			tempBuffer.append("'" + row.element("TID").getText() + "'");
		}
		if (tempBuffer.length() > 0)
			condition += " AND t1.table_id IN (" + tempBuffer.toString() + ") ";
		else
			condition += " AND 1=2 ";
		condition += " AND rownum < " + Constants.MAX_RECORD_LIMITED
				+ " ORDER BY tid ";
          
        PageManager page = new PageManager();
        page.setPageRows(Constants.MAX_RECORD_LIMITED);
        page.setFilter(condition);

        Document domresult = null;
        try
        {
//            String sql = "SELECT t1.owner,t1.table_name tid,t1.column_name cid,t1.comments cname,t1.data_type,"
//                + "t1.data_length,t1.data_precision,t1.data_scale,t1.dic_table,t1.dic_code,"
//                + "t1.key_field,t1.value_field,t1.style,t1.primarykey pk,t1.indexname ind,t1.nullable,"
//                + "t2.comments tname,t1.dic_file"
//                + " FROM fw_free_table t2,fw_free_field t1 ";
           String sql = "SELECT t2.table_id tid, t2.table_owner owner,t2.table_name tname,t2.table_comment tnote"
	 	              + ",t1.field_name fname,t1.field_comment fnote,t1.field_type"
	 	              + ",t1.style,t1.date_format fmt"
	 	              + ",t1.dict_tbl dic_table,t1.dic_file,t1.dict_field dic_code,t1.code_field key_field,t1.content_field_name value_field"
	 	              + ",t1.is_codefield c,t1.is_nchar n,t1.is_partition p "
	                  + " FROM "+FreeQueryProps.USER_TABLE+" t2,"+FreeQueryProps.USER_FIELD+" t1 ";

            BaseResultSet bs = DBUtil.query(conn, sql,page);
            domresult = bs.getDocument();
            strResult = domresult.asXML();
            bs.Close();
        }
        catch (Exception e)
        {
        	 System.out.println("conditon="+condition);
            e.printStackTrace(System.out);
        }
        domresult = null;

        return strResult;
    }
	
	private String toGBK(String p_Value){
		String strResult = "";
		try {
			byte[] arrTemp = p_Value.getBytes();
			strResult = new String(arrTemp, "gbk");
			arrTemp = null;
		} catch (UnsupportedEncodingException e) {
			strResult = "";
			e.printStackTrace();
		}
		return strResult;
	}
	
	public boolean getFreeQuery(String p_ID)
    {
   		boolean bResult = false;
        String strSQL = "SELECT deptid,userid,username,tablelevel,tableinfo,tabletitle,memo  " +
        		"FROM "+FreeQueryProps.FREE_QUERY+" WHERE id = " + p_ID;

        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = DBUtil.getConnection();
        strHtmlXML = "";
        
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(strSQL);

            if (rs.next())
            {
                strDeptID = rs.getString(1);
                strUserID = rs.getString(2);
                strUserName = rs.getString(3);
                strTableLevel = rs.getString(4);
                strTableTitle = rs.getString(6);
                strMemo = rs.getString(7);

                Blob dbBlob;
                dbBlob = rs.getBlob(5);
                int length = (int) dbBlob.length();
                if (length < 12)
                {
                	try
                    {
                        if (rs != null)
                            rs.close();
                        if (stmt != null)
                            stmt.close();
                        if (conn != null)
                            conn.close();
                        conn = null;
                        bResult = false;
                    }
                    catch (java.sql.SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                	return bResult;
                }
                byte[] buffer = dbBlob.getBytes(1, length);
                String strTableInfo = "";
                strTableInfo = new String(buffer);

                Document doc = null;
                try
                {
                    doc = DocumentHelper.parseText(strTableInfo);
                }
                catch (DocumentException ex1)
                {
                	ex1.printStackTrace();
                	buffer = null;
                	doc = null;
                	try
                    {
                        if (rs != null)
                            rs.close();
                        if (stmt != null)
                            stmt.close();
                        if (conn != null)
                            conn.close();
                        conn = null;
                        bResult = false;
                    }
                    catch (java.sql.SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                	return bResult;
                }
                org.dom4j.Element root = doc.getRootElement();
                strTitle = root.attributeValue("title");
                if (strTitle != null)
                  strTitle = toGBK(strTitle);
                org.dom4j.Element menuNode = root.element("MENU");
                if (menuNode != null){
                	strMenu = menuNode.getText();
                	if (strMenu != null)
                	  strMenu = toGBK(strMenu);
                	strParentMenu = menuNode.attributeValue("parent");
                	if (strParentMenu != null)
                	  strParentMenu = toGBK(strParentMenu);
                }
                	
                Node listTables = doc.selectSingleNode("//FREE/TABLES");
                strTableXML = listTables.asXML();
                if (strTableXML != null)
                  strTableXML = toGBK(strTableXML);
                strFieldXML = getFieldList(conn,doc.selectNodes("//FREE/TABLES/ROW"));

                Node listRelation = doc.selectSingleNode("//FREE/RELATIONS");
                strRelationXML = listRelation.asXML();
                if (strRelationXML != null)
                   strRelationXML = toGBK(strRelationXML);

                Node listFilters = doc.selectSingleNode("//FREE/FILTERS");
                strFilterXML = listFilters.asXML();
                if  (strFilterXML != null)
                  strFilterXML = toGBK(strFilterXML);
                
                
                Node listImage = doc.selectSingleNode("//FREE/IMAGESET");
                if (listImage != null){
                	strImageXML = listImage.asXML();
                	if (strImageXML != null)
                	  strImageXML = toGBK(strImageXML);
                }
                
                Node listJoin = doc.selectSingleNode("//FREE/JOINS");
                if (listJoin != null){
                   strJoinXML = listJoin.asXML();
                   if (strJoinXML != null)
                     strJoinXML = toGBK(strJoinXML);
                }
                
                Node listHtmls = doc.selectSingleNode("//FREE/TJB");
                strHtmlXML = listHtmls.asXML();
                strHtmlXML = strHtmlXML.replaceAll("&lt;", "<");
                strHtmlXML = strHtmlXML.replaceAll("&gt;", ">");
                strHtmlXML = strHtmlXML.replaceAll("&amp;", "&");
                strHtmlXML = strHtmlXML.substring(12, strHtmlXML.length() - 14);
                strHtmlXML = toGBK(strHtmlXML);
                
                buffer = null;
                doc = null;

            }
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
        }

        try
        {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
            conn = null;
            bResult = true;
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
        }

        return bResult;
    }
	
	public String getFreeQueryDefine(String id)
    {

        String strResult = "";
//        String strSQL = "SELECT deptid,userid,username,tablelevel,tableinfo,tabletitle,memo  "
        String strSQL = "SELECT tableinfo  "
        	          +	"FROM "+FreeQueryProps.FREE_QUERY+" WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtil.getConnection();
        try
        {
            ps = conn.prepareStatement(strSQL);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next())
            {
//                strDeptID = rs.getString(1);
//                strUserID = rs.getString(2);
//                strUserName = rs.getString(3);
//                strTableLevel = rs.getString(4);
//                strTableTitle = rs.getString(6);
//                strMemo = rs.getString(7);
                Blob dbBlob;
//                dbBlob = rs.getBlob(5);
                dbBlob = rs.getBlob(1);
                int length = (int) dbBlob.length();
                if (length < 12){
                	try
                    {
                        if (rs != null)
                            rs.close();
                        if (ps != null)
                        	ps.close();
                        if (conn != null)
                            conn.close();
                        conn = null;
                    }
                    catch (java.sql.SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                    throw new SQLException("");
                }
                byte[] buffer = dbBlob.getBytes(1, length);
                strResult = new String(buffer);
                try {
					strResult = new String(buffer,"GBK");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				buffer = null;
            }
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
        }

        try
        {
            if (rs != null)
                rs.close();
            if (ps != null)
            	ps.close();
            if (conn != null)
                conn.close();
            conn = null;
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
        }
        return strResult;
    }
	
	public void doRelationTable(Connection p_Conn, String p_QueryID,
			ArrayList p_RelationList) throws Exception {
		
		if (p_RelationList.size() > 0){
			
			PreparedStatement ps = null;
			Statement stmt = null;

			String strInsertSQL = " INSERT INTO "+FreeQueryProps.FREE_RELATION+"(relationid, queryid,tableid, fieldname,relationname,queryname)"
					            + " VALUES(seq_common_serival_number.nextval,"+ p_QueryID + ",?,?,?,?)";
			String strDeleteSQL = "DELETE FROM "+FreeQueryProps.FREE_RELATION+" WHERE queryid = '" + p_QueryID + "'";

			try {
				stmt = p_Conn.createStatement();
				stmt.executeUpdate(strDeleteSQL);
				ps = p_Conn.prepareStatement(strInsertSQL);
				
				for(int i = 0; i < p_RelationList.size();i++){
					
					QueryJoinVO tmpVO = (QueryJoinVO) p_RelationList.get(i);
					String tempTID = tmpVO.getTableID();
		        	String tempField = tmpVO.getFieldName();
		        	String tempRName = tmpVO.getRelationName();
		        	String tmepQName = tmpVO.getQueryName();
		        	
		        	ps.setString(1, tempTID);
		        	ps.setString(2, tempField);
		        	ps.setString(3, tempRName);
		        	ps.setString(4, tmepQName);
					
		        	ps.addBatch();
				}
				
				ps.executeBatch();

				
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (ps != null)
					ps.close();
				if (stmt != null)
					stmt.close();
			}
			
		}
		
	}
	
	public void buildMenu(Connection p_Conn, String p_MenuID,String p_MenuTitle,
			String p_ParentID,String p_Layersno,String p_QueryID) throws Exception {
//			String p_ParentID,String p_Layersno,String p_Location) throws Exception {
		
			
			PreparedStatement ps = null;
			PreparedStatement psTable = null;
			ResultSet rs = null;

			String strInsertSQL = " INSERT INTO "+FreeQueryProps.MENU+"(title,parent, layersno,target,location,orderno,name )"
					            + " VALUES(?,?,?,'pagearea',?,?,?)";
			String strUpdateSQL = " UPDATE "+FreeQueryProps.MENU+" SET title = ?,parent = ?,layersno = ?,target = 'pagearea' ,location = ?, orderno = ?" 
					            + " WHERE name = ?";
			String strSelect = " SELECT 1 FROM "+FreeQueryProps.MENU+" WHERE name = ?";

			try {
				ps = p_Conn.prepareStatement(strSelect);
				ps.setString(1, p_MenuID);
				
				rs = ps.executeQuery();
				if (rs.next()){
					psTable = p_Conn.prepareStatement(strUpdateSQL);
				}
				else{
					psTable = p_Conn.prepareStatement(strInsertSQL);
				}
				
				rs.close();
//				String strLocation = "FrameWork/DefineQueryAction.do?method=doFreeQuery&id=" + p_QueryID;
				String strLocation = "FrameWork/DefineQueryAction.do?method=doSeekExec&id=" + p_QueryID;
				
				psTable.setString(1, p_MenuTitle);
				psTable.setString(2, p_ParentID);
				psTable.setString(3, p_Layersno);
				psTable.setString(4, strLocation);
				psTable.setString(5, p_QueryID);
				psTable.setString(6, p_MenuID);
				
				psTable.execute();
				
			
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (psTable != null)
					psTable.close();
			}
			
		}
		
}
