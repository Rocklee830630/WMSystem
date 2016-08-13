package com.ccthanking.framework.coreapp.freetable;

/*****************************************************************************
 * Free table class. for html and java(jsp/servlet) application with Database
 * operation. (oracle / based on sql)
 * Version 1.0
 * Author: leo Leo.Chou decay@163.com
 *         michael wang wyanguo@163.com
 * create time : 2006.06.01
 * You can copy and/or use and/or modify this program free,but please reserve
 * the segment above. Please mail me if you have any question, Good day!
 *
 * notice:
 * if you do not use weblogic ,you must modify  executeInsert and executeUpdate
 * function to do blob data type
 * you must modify variable header21,replace "freetable" with your web applica-
 * tion name
 *****************************************************************************
 */

import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.ccthanking.framework.util.*;
import org.dom4j.*;
import org.dom4j.io.*;
import java.io.*;
import com.ccthanking.framework.common.*;
import java.sql.*;
import com.ccthanking.framework.base.*;
import com.ccthanking.framework.Constants;

public class FreeTableAction
    extends BaseDispatchAction
{

    public FreeTableAction()
    {
    }

//// michael add begin

    public String strID = "";
    public String strDeptID = "";
    public String strUserID = "";
    public String strUserName = "";
    public String strTableLevel = "";
    public String strTableInfo = "";
    public String strTableTitle = "";
    public String strMemo = "";
    public String strTableXML = "";
    public String strFieldXML = "";
    public String strRelationXML = "";
    public String strFilterXML = "";
    public String strHtmlXML = "";
//    public String strTitle = "";

    private String strErrorMessage;

    private String executeSEQ(Connection conn, String seqName)
    { //throws java.sql.SQLException {
        String strResult = "";
//        String strSEQClause = "select seq_free_stat.nextval from dual";
        String strSEQClause = "select " + seqName + ".nextval from dual";

        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            stmt = conn.createStatement();

            rs = stmt.executeQuery(strSEQClause);

            if (rs.next())
            {
                strResult = rs.getString(1);
            }
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            strErrorMessage = ex.toString();
        }

        try
        {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            strErrorMessage = ex.toString();
        }

        return strResult;
    }

    private void executeInsert(Connection conn, String strInsertSQL,
                               Object[] arrFieldValue)

    {
        java.sql.PreparedStatement ps = null;
        Statement stmt = null;
        ResultSet rs = null;
        Object objValue;

        try
        {
            ps = conn.prepareStatement(strInsertSQL);

            for (int i = 0; i < arrFieldValue.length; i++)
            {

                objValue = arrFieldValue[i];
                if (objValue != null)
                    ps.setObject(i + 1, objValue);
                else
                    ps.setNull(i + 1, java.sql.Types.VARCHAR);

            }

            ps.execute();

        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            strErrorMessage = ex.toString();
        }

        try
        {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            strErrorMessage = ex.toString();
        }

    }

    private void executeInsert(Connection conn, String strInsertSQL,
                               String strUpdateBlobSQL, Object[] arrFieldValue,
                               String blobInfo)

    {
        java.sql.PreparedStatement ps = null;
        Statement stmt = null;
        ResultSet rs = null;
        Object objValue;

        try
        {
            ps = conn.prepareStatement(strInsertSQL);

            for (int i = 0; i < arrFieldValue.length; i++)
            {

                objValue = arrFieldValue[i];
                if (objValue != null)
                    ps.setObject(i + 1, objValue);
                else
                    ps.setNull(i + 1, java.sql.Types.VARCHAR);

            }

            ps.execute();

            stmt = conn.createStatement();

            rs = stmt.executeQuery(strUpdateBlobSQL);

            if (rs.next())
            {
//weblogic ����BLOB�����ֶ�
//                weblogic.jdbc.common.OracleBlob dbBlob;
//                dbBlob = (weblogic.jdbc.common.OracleBlob) rs.getBlob(1);
//������BLOB�����ֶ�
                oracle.sql.BLOB dbBlob = null;
                dbBlob = (oracle.sql.BLOB) rs.getBlob(1);

                OutputStream os = dbBlob.getBinaryOutputStream();
                try
                {
                    os.write(blobInfo.getBytes());
                    os.close();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    strErrorMessage = ex.toString();
                }
            }
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            strErrorMessage = ex.toString();
        }

        try
        {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            strErrorMessage = ex.toString();
        }

    }

    private void insertBlob(Connection conn, String strUpdateBlobSQL,
                            String blobInfo)

    {
        Statement stmt = null;
        ResultSet rs = null;
        Object objValue;

        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(strUpdateBlobSQL);
            if (rs.next())
            {
//weblogic ����BLOB�����ֶ�
//                weblogic.jdbc.common.OracleBlob dbBlob;
//                dbBlob = (weblogic.jdbc.common.OracleBlob) rs.getBlob(1);
//������BLOB�����ֶ�
                oracle.sql.BLOB dbBlob = null;
                dbBlob = (oracle.sql.BLOB) rs.getBlob(1);

                OutputStream os = dbBlob.getBinaryOutputStream();
                try

                {
                    os.write(blobInfo.getBytes());
                    os.close();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    strErrorMessage = ex.toString();
                }
            }
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            strErrorMessage = ex.toString();
        }

        try
        {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            strErrorMessage = ex.toString();
        }
    }

    private void executeUpdate(Connection conn, String strUpdateSQL,
                               String strUpdateBlobSQL, Object[] arrFieldValue,
                               String blobInfo)
    {
        java.sql.PreparedStatement ps = null;
        Statement stmt = null;
        ResultSet rs = null;
        Object objValue;

        try
        {
            ps = conn.prepareStatement(strUpdateSQL);

            for (int i = 0; i < arrFieldValue.length; i++)
            {

                objValue = arrFieldValue[i];
                if (objValue != null)
                    ps.setObject(i + 1, objValue);
                else
                    ps.setNull(i + 1, java.sql.Types.VARCHAR);

            }

            ps.execute();

            stmt = conn.createStatement();

            rs = stmt.executeQuery(strUpdateBlobSQL);

            if (rs.next())
            {
//weblogic ����BLOB�����ֶ�
//              weblogic.jdbc.common.OracleBlob dbBlob;
//             dbBlob = (weblogic.jdbc.common.OracleBlob) rs.getBlob(1);
//������BLOB�����ֶ�
                oracle.sql.BLOB dbBlob = null;
                dbBlob = (oracle.sql.BLOB) rs.getBlob(1);

                OutputStream os = dbBlob.getBinaryOutputStream();
                try
                {
                    os.write(blobInfo.getBytes());
                    os.close();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    strErrorMessage = ex.toString();
                }

            }
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            strErrorMessage = ex.toString();
        }

        try
        {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            strErrorMessage = ex.toString();
        }

    }

    private void doXMLToTable(Connection conn, List objList, String strID,
                              String strPID)
    {

        org.dom4j.Element objElement;
        Object objValue[] = new Object[7];
        objValue[0] = strID;
        objValue[6] = strPID;
        String strInsert = "insert into FW_FREE_STAT_DATA (ID,X,Y,COLSPAN,ROWSPAN,INFO,PID) values(?,?,?,?,?,?,?)";
        for (int i = 0; i < objList.size(); i++)
        {
            objElement = (org.dom4j.Element) objList.get(i);
            objValue[1] = objElement.attributeValue("X");
            objValue[2] = objElement.attributeValue("Y");
            objValue[3] = objElement.attributeValue("COLSPAN");
            objValue[4] = objElement.attributeValue("ROWSPAN");
            objValue[5] = objElement.getText();

            executeInsert(conn, strInsert, objValue);
        }
    }

    public ActionForward saveTableData(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response)
        throws
        Exception
    {
        Document doc = RequestUtil.getDocument(request);
        String docstr = (String) request.getSession().getAttribute("condinfo");
        Document domresult = null;
        OutputStream os = null;

        Node infoNode = doc.selectSingleNode("//RESULT/INFO");
        List dataList = doc.selectNodes("//RESULT/DATA/CELL");

        Element root = doc.getRootElement();

        org.dom4j.Attribute attID = root.attribute("ID");
        strID = attID == null ? "" : attID.getValue();

        String strColumns = root.attributeValue("COLUMNS");

        strErrorMessage = "";
        Connection conn = DBUtil.getConnection("p3");
        try
        {
            conn.setAutoCommit(false);
            getFreeStatContent(strID);
            String strSEQ = executeSEQ(conn, "seq_common_serival_number");

            Object objFieldValue[] = new Object[6];

            objFieldValue[0] = Pub.getUserDepartment(request);
            objFieldValue[1] = Pub.getUserAccount(request);
            objFieldValue[2] = Pub.getUserName(request);
            objFieldValue[3] = strTableLevel;
            objFieldValue[4] = strTableTitle;
            objFieldValue[5] = strColumns;

            String strInsert = " insert into fw_free_stat_info(ID, PID,DEPTID,USERID ,USERNAME ,TABLELEVEL,TABLEINFO,TABLETITLE,EXECTIME,COLUMNS,CONDINFO)";
            strInsert = strInsert + " values(" + strSEQ + "," + strID +
                ",?,?,?,?,EMPTY_BLOB(),?,sysdate,?,EMPTY_BLOB())";
            String strUpdateBlob =
                "select TABLEINFO from fw_free_stat_info where ID = " +
                strSEQ;
            String strUpdateBlobCondInfo =
                "select CONDINFO from fw_free_stat_info where id=" + strSEQ;
            if ("".equals(strErrorMessage))
            {

                strTableInfo = infoNode.asXML();
                executeInsert(conn, strInsert, strUpdateBlob, objFieldValue,
                              strTableInfo);
                if (!Pub.empty(docstr))
                    this.insertBlob(conn, strUpdateBlobCondInfo, docstr);
                if ("".equals(strErrorMessage))
                    doXMLToTable(conn, dataList, strSEQ, strID);
            }

            String strTitle = "";
            String strMsg = "";
            if ("".equals(strErrorMessage))
            {
                strTitle = "MESSAGE";
                strMsg = "����ɹ���";
                conn.commit();
            }
            else
            {
                strTitle = "ERRMESSAGE";
                strMsg = "�������\n" + strErrorMessage;
                conn.rollback();
            }

            domresult = WriteXmlMessage(domresult, strMsg, strTitle);
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();

        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            try
            {
                conn.rollback();
            }
            catch (SQLException ex1)
            {
            }
            domresult = WriteXmlMessage(domresult, "�������\n" + ex.getMessage(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        finally
        {
            if (conn != null)

            {
                conn.close();
            }
            conn = null;
        }

        return null;
    }

    public ActionForward saveTableDefine(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
        throws
        Exception
    {
        Document doc = RequestUtil.getDocument(request);
        Document domresult = null;
        OutputStream os = null;

        strUserID = Pub.getUserAccount(request);
        strUserName = Pub.getUserName(request);
        strDeptID = Pub.getUserDepartment(request);

        Element root = doc.getRootElement();

        org.dom4j.Attribute attID = root.attribute("ID");
        strID = attID == null ? null : attID.getValue();

        Element tableTitleNode = root.element("TABLETITLE");
        Element memoNode = root.element("MEMO");

        if (tableTitleNode != null)
        {
            org.dom4j.Attribute attLevel = tableTitleNode.attribute("level");
            strTableLevel = attLevel != null ? attLevel.getValue() : null;
            strTableTitle = tableTitleNode.getText();
        }

        strMemo = memoNode != null ? memoNode.getText() : null;

        strErrorMessage = "";
        Connection conn = DBUtil.getConnection("p3");
        try
        {

            conn.setAutoCommit(false);

            String strInsert;
            String strUpdate;
            String strUpdateBlob;

            Object objFieldValue[] = new Object[6];

            objFieldValue[0] = strDeptID;
            objFieldValue[1] = strUserID;
            objFieldValue[2] = strUserName;
            objFieldValue[3] = strTableLevel;
            objFieldValue[4] = strTableTitle;
            objFieldValue[5] = strMemo;

            if (strID == null)
            {
                String strSEQ = executeSEQ(conn, "seq_common_serival_number");
                strInsert = " insert into fw_free_stat(ID, DEPTID,USERID ,USERNAME ,TABLELEVEL,TABLEINFO,TABLETITLE,CREATETIME,MEMO)";
                strInsert = strInsert + " values(" + strSEQ +
                    ",?,?,?,?,EMPTY_BLOB(),?,sysdate,?)";
                strUpdateBlob =
                    "select TABLEINFO from fw_free_stat where ID = " +
                    strSEQ;

                if ("".equals(strErrorMessage))
                {
                    root.addAttribute("ID", strSEQ);
                    strTableInfo = doc.asXML();

                    executeInsert(conn, strInsert, strUpdateBlob, objFieldValue,
                                  strTableInfo);
                }
            }
            else
            {
                strUpdate = "update fw_free_stat set DEPTID = ?,USERID = ? ,USERNAME = ?,TABLELEVEL = ?,TABLEINFO = EMPTY_BLOB(),TABLETITLE = ?,CREATETIME = sysdate ,MEMO = ?";
                strUpdate = strUpdate + " where ID = " + strID;

                strUpdateBlob =
                    "select TABLEINFO from fw_free_stat where ID = " +
                    strID;

                strTableInfo = doc.asXML();
                executeUpdate(conn, strUpdate, strUpdateBlob, objFieldValue,
                              strTableInfo);
            }
            String strTitle = "";
            String strMsg = "";
            if ("".equals(strErrorMessage))
            {
                strTitle = "MESSAGE";
                strMsg = "����ɹ���";
                conn.commit();
            }
            else
            {
                strTitle = "ERRMESSAGE";
                strMsg = "�������\n" + strErrorMessage;
                conn.rollback();
            }

            domresult = WriteXmlMessage(domresult, strMsg, strTitle);
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();

        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            try
            {
                conn.rollback();
            }
            catch (SQLException ex1)
            {
            }
            domresult = WriteXmlMessage(domresult, "�������\n" + ex.getMessage(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        finally
        {
            if (conn != null)
                conn.close();
            conn = null;
        }

        return null;
    }

    public ActionForward delFreeStat(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
        throws
        Exception
    {
        Document doc = RequestUtil.getDocument(request);

        Document domresult = null;
        OutputStream os = null;
        String strSQL = "";

        Element root = doc.getRootElement();

        strErrorMessage = "";

        org.dom4j.Attribute attID = root.attribute("ID");
        String strID = attID == null ? null : attID.getValue();
        if (strID == null)
        {
            strErrorMessage = "��ݲ�����";
        }
        else
        {
            strSQL = " delete from fw_free_stat where id = " + strID;
        }

        Connection conn = DBUtil.getConnection("p3");
        try
        {
            if ("".equals(strErrorMessage))
            {
                DBUtil.execSql(conn, strSQL);
                conn.commit();
            }

            String strTitle = "";
            String strMsg = "";
            if ("".equals(strErrorMessage))
            {
                strTitle = "MESSAGE";
                strMsg = "ɾ����ݳɹ���";
            }
            else
            {
                strTitle = "ERRMESSAGE";
                strMsg = "�������\n" + strErrorMessage;
            }

            domresult = WriteXmlMessage(domresult, strMsg, strTitle);
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();

        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
            domresult = WriteXmlMessage(domresult, "�������\n" + ex.getMessage(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        finally
        {
            if (conn != null)
                conn.close();
            conn = null;
        }

        return null;
    }

    private String getFieldList(Connection conn, List list)
    {
        String strResult = "";
        String owner = null;
        String condition =
            " t1.table_name=t2.table_name and t1.table_name in (";
        Iterator itor = list.iterator();
        while (itor.hasNext())
        {
            Element row = (Element) itor.next();
            condition += "'" + row.element("TID").getText() + "',";
            owner = row.element("OWNER").getText();
        }
        condition += "'') and t1.owner='" + owner + "' and rownum < " +
            Constants.MAX_RECORD_LIMITED
            + " order by tid ";
        PageManager page = new PageManager();
        page.setPageRows(Constants.MAX_RECORD_LIMITED);
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        try
        {
            String sql = "select t1.owner,t1.table_name tid,t1.column_name cid,t1.comments cname,t1.data_type,"
                +
                "t1.data_length,t1.data_precision,t1.data_scale,t1.dic_table,t1.dic_code,"

                + "t1.key_field,t1.value_field,t1.style,t1.primarykey pk,t1.indexname ind,t1.nullable,"
                + "t2.comments tname,t1.dic_file"
                + " from free_table t2,free_field t1 ";
            BaseResultSet bs = DBUtil.query(conn,
                                            sql,
                                            page);
            domresult = bs.getDocument();
            strResult = domresult.asXML();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }

        return strResult;
    }

    public boolean getFreeStat()
    {

        boolean bResult = false;
        String strSQL = "SELECT deptid,userid,username,tablelevel,tableinfo,tabletitle,memo  from fw_free_stat where id = " +
            strID;

        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = DBUtil.getConnection("p3");

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
                    strHtmlXML = "";
                    return false;
                }
                byte[] buffer = dbBlob.getBytes(1, length);
                strTableInfo = new String(buffer);

                Document doc = null;
                try
                {
                    doc = DocumentHelper.parseText(strTableInfo);
                }
                catch (DocumentException ex1)
                {
                }
                org.dom4j.Element root = doc.getRootElement();
//                strTitle = root.attributeValue("title");

                Node listTables = doc.selectSingleNode("//FREE/TABLES");
                strTableXML = listTables.asXML();
                strFieldXML = getFieldList(conn,
                                           doc.selectNodes(
                                               "//FREE/TABLES/RESPONSE/RESULT/ROW"));

                Node listRelation = doc.selectSingleNode("//FREE/RELATIONS");
                strRelationXML = listRelation.asXML();

                Node listFilters = doc.selectSingleNode("//FREE/FILTERS");
                strFilterXML = listFilters.asXML();
                Node listHtmls = doc.selectSingleNode("//FREE/TJB");
                strHtmlXML = listHtmls.asXML();
                strHtmlXML = strHtmlXML.replaceAll("&lt;", "<");
                strHtmlXML = strHtmlXML.replaceAll("&gt;", ">");
                strHtmlXML = strHtmlXML.replaceAll("&amp;", "&");
                strHtmlXML = strHtmlXML.substring(12, strHtmlXML.length() - 14);

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

    public boolean getFreeStatContent(String strID)
    {

        boolean bResult = false;
        String strSQL = "SELECT deptid,userid,username,tablelevel,tabletitle  from fw_free_stat where id = " +
            strID;

        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = DBUtil.getConnection("p3");

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
                strTableTitle = rs.getString(5);
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

    public String getFreeStatInfo(String id)
    {

        String strResult = "";
        String strSQL = "SELECT tableinfo  FROM fw_free_stat_info WHERE id = " +
            id;
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = DBUtil.getConnection("p3");
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(strSQL);
            if (rs.next())
            {
                Blob dbBlob;
                dbBlob = rs.getBlob(1);
                int length = (int) dbBlob.length();
                if (length < 12)
                    return "";
                byte[] buffer = dbBlob.getBytes(1, length);
                String strHtmlXML = new String(buffer);

                strHtmlXML = strHtmlXML.replaceAll("&lt;", "<");
                strHtmlXML = strHtmlXML.replaceAll("&gt;", ">");
                strHtmlXML = strHtmlXML.replaceAll("&amp;", "&");
                strHtmlXML = strHtmlXML.substring(13, strHtmlXML.length() - 15);
                strResult = strHtmlXML;

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
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
        }
        return strResult;
    }

    public String getFreeStat(String id)
    {

        String strResult = "";
        String strSQL = "SELECT deptid,userid,username,tablelevel,tableinfo,tabletitle,memo  from fw_free_stat where id = " +
            id;
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = DBUtil.getConnection("p3");
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(strSQL);
            if (rs.next())
            {
//                strDeptID = rs.getString(1);
//                strUserID = rs.getString(2);
//                strUserName = rs.getString(3);
//                strTableLevel = rs.getString(4);
//                strTableTitle = rs.getString(6);
//                strMemo = rs.getString(7);
                Blob dbBlob;
                dbBlob = rs.getBlob(5);
                int length = (int) dbBlob.length();
                if (length < 12)
                    throw new SQLException("");
                byte[] buffer = dbBlob.getBytes(1, length);
                strResult = new String(buffer);
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
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
        }
        return strResult;
    }

    public String getFreeStatInfoCondition(String id)
    {

        String strResult = "";
        String strSQL = "SELECT condinfo from fw_free_stat_info where id = '" +
            id + "'";
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = DBUtil.getConnection("p3");
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(strSQL);
            if (rs.next())
            {
                Blob dbBlob;
                dbBlob = rs.getBlob(5);
                int length = (int) dbBlob.length();
                if (length < 12)
                    throw new SQLException("");
                byte[] buffer = dbBlob.getBytes(1, length);
                strResult = new String(buffer);
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
        }
        catch (java.sql.SQLException ex)
        {
            ex.printStackTrace();
        }
        return strResult;
    }

    public ActionForward getFreeStatInfoList(ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
        throws
        Exception
    {
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String strPID = request.getParameter("pid");
        String condition = list == null ? "1=1" : list.getConditionWhere();
        condition += " and pid = " + strPID + " and rownum < " +
            Constants.MAX_RECORD_LIMITED;
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection("p3");
        try
        {
            conn.setAutoCommit(false);
            String sql =
                "select tabletitle,username,deptid,exectime,columns,id from fw_free_stat_info";
            BaseResultSet bs = DBUtil.query(conn,
                                            sql,
                                            page);
            bs.setFieldOrgDept("deptid");
            bs.setFieldDateFormat("exectime", "yyyy-MM-dd");
            domresult = bs.getDocument();
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            domresult = WriteXmlMessage(domresult, "������󣡣�" + e.toString(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        finally
        {
            if (conn != null)
                conn.close();
            conn = null;
        }
        return null;
    }

    public ActionForward delFreeStatInfo(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
        throws
        Exception
    {
        Document doc = RequestUtil.getDocument(request);

        Document domresult = null;
        OutputStream os = null;
        String strInfoSQL = "";
        String strDataSQL = "";

        Element root = doc.getRootElement();

        strErrorMessage = "";

        org.dom4j.Attribute attID = root.attribute("ID");
        String strID = attID == null ? null : attID.getValue();
        if (strID == null)
        {
            strErrorMessage = "��ݲ�����";
        }
        else
        {
            strInfoSQL = " delete from fw_free_stat_info where id = " + strID;
            strDataSQL = " delete from fw_free_stat_data where id = " + strID;
        }

        Connection conn = DBUtil.getConnection("p3");
        try
        {
            if ("".equals(strErrorMessage))
            {
                conn.setAutoCommit(false);
                DBUtil.execSql(conn, strInfoSQL);
                DBUtil.execSql(conn, strDataSQL);
                conn.commit();
            }

            String strTitle = "";
            String strMsg = "";
            if ("".equals(strErrorMessage))
            {
                strTitle = "MESSAGE";
                strMsg = "ɾ����ݳɹ���";
            }
            else
            {
                strTitle = "ERRMESSAGE";
                strMsg = "�������\n" + strErrorMessage;
            }

            domresult = WriteXmlMessage(domresult, strMsg, strTitle);
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();

        }
        catch (java.sql.SQLException ex)
        {
            conn.rollback();
            ex.printStackTrace();
            domresult = WriteXmlMessage(domresult, "�������\n" + ex.getMessage(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        finally
        {
            if (conn != null)
                conn.close();
            conn = null;
        }

        return null;
    }

/////////michael add end

    public ActionForward getFreeStatList(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
        throws
        Exception
    {
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String condition = list == null ? "1=1" : list.getConditionWhere();

        condition += " and rownum < " + Constants.MAX_RECORD_LIMITED;
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);

        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection("p3");
        try
        {
            conn.setAutoCommit(false);
            String sql = "select tabletitle,username,deptid,createtime,lastdate,decode(tablelevel,'1','����','2','����','3','�쵼') as tablelevel,id from fw_free_stat";
            BaseResultSet bs = DBUtil.query(conn,
                                            sql, page);
            bs.setFieldOrgDept("deptid");
            bs.setFieldDateFormat("createtime", "yyyy-MM-dd");
            bs.setFieldDateFormat("lastdate", "yyyy-MM-dd");
            domresult = bs.getDocument();
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }

        catch (Exception e)
        {
            e.printStackTrace(System.out);
            domresult = WriteXmlMessage(domresult, "������󣡣�" + e.toString(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        finally
        {
            if (conn != null)
                conn.close();
            conn = null;
        }
        return null;
    }

    public ActionForward getFieldList(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response)
        throws
        Exception
    {
        Document doc = RequestUtil.getDocument(request);
        String owner = null;
        String condition =
            " t1.table_name=t2.table_name and t1.table_name in (";
        List list = doc.selectNodes("//ROW");
        Iterator itor = list.iterator();
        while (itor.hasNext())
        {
            Element row = (Element) itor.next();
            condition += "'" + row.element("TID").getText() + "',";
            owner = row.element("OWNER").getText();
        }
        condition += "'') and t1.owner='" + owner + "' and rownum < " +
            Constants.MAX_RECORD_LIMITED
            + " order by tid ";
        PageManager page = new PageManager();
        page.setPageRows(Constants.MAX_RECORD_LIMITED);
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection("p3");
        try
        {
            conn.setAutoCommit(false);
            String sql = "select t1.owner,t1.table_name tid,t1.column_name cid,t1.comments cname,t1.data_type,"
                +
                "t1.data_length,t1.data_precision,t1.data_scale,t1.dic_table,t1.dic_code,"
                + "t1.key_field,t1.value_field,t1.style,t1.primarykey pk,t1.indexname ind,t1.nullable,"
                + "t2.comments tname,t1.dic_file ,t1.code_field "
                + " from free_table t2,free_field t1 ";
            BaseResultSet bs = DBUtil.query(conn,
                                            sql,
                                            page);
            domresult = bs.getDocument();
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            domresult = WriteXmlMessage(domresult, "������󣡣�" + e.toString(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        finally
        {
            if (conn != null)
                conn.close();
            conn = null;
        }
        return null;
    }

    public ActionForward getTableList(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response)
        throws
        Exception
    {
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String condition = list == null ? "1=1" : list.getConditionWhere();
        condition = condition.toUpperCase();
        condition += " and rownum < " + Constants.MAX_RECORD_LIMITED;
        if (page == null)
            page = new PageManager();
        page.setPageRows(Constants.MAX_RECORD_LIMITED);
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection("p3");
        try
        {
            conn.setAutoCommit(false);
            String sql = "select table_name tid,owner,"
                + "comments tname from free_table ";
            BaseResultSet bs = DBUtil.query(conn,
                                            sql,
                                            page);
            domresult = bs.getDocument();
            os = response.getOutputStream();
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            domresult = WriteXmlMessage(domresult, "������󣡣�" + e.toString(),
                                        "ERRMESSAGE");
            if (os == null)
            {
                os = response.getOutputStream();
            }
            XMLWriter writer = new XMLWriter(os);
            writer.write(domresult);
            os.flush();
        }
        finally
        {
            if (conn != null)
                conn.close();
            conn = null;
        }
        return null;
    }

///////////////////////////////////////////////////////////////////////////

    private String doQuery(Document doc, HttpServletRequest request)
        throws
        Exception
    {
        String res = null;

        request.getSession().setAttribute("condinfo",
                                          doc.getRootElement().
                                          element("FILTERS").asXML());
        QueryTable table = new QueryTable();
        table.setStyle(table.STYLE_SHOW_EXCEL);
        table.setAllcols(128);
        String[][] data = doAnalyze(doc, table, request);
        table.setData(data);
        for (int i = 0; i < data.length; i++)
        {
            for (int j = 0; j < data[i].length; j++)
            {
                if (data[i][j] == null)
                    data[i][j] = "0";
            }
        }
        //format output
        //table.addTitleMemo("ͳ�����ڣ�" + Pub.getDate("yyyy-MM-dd"));
//        table.addTitleMemo("ͳ���ˣ�"+user.getUserName());
        table.addTailMemo("��ע��[������� " + Pub.getDate("yyyy-MM-dd")+"]");
        table.setCellLink(true);
        res = table.listdetail();
        return res;
    }

    private String[][] doAnalyze(Document doc, QueryTable table,
                                 HttpServletRequest request)
        throws Exception
    {
        Element root = doc.getRootElement();
        table.setTitle(root.attribute("title").getStringValue());
        table.setBodyStyleSheet("CURSOR: hand; table-layout:fixed;");
        List xList = root.selectNodes("//X/CELL");
        List yList = root.selectNodes("//Y/CELL");
        int rows = 0, cols = 0;
        int xOff = 0, yOff = 0;
        ArrayList condArr = new ArrayList();
        int index = 0;
        ArrayList xarr = new ArrayList();
        for (int i = 0; i < xList.size(); i++)
        {
            Element element = (Element) xList.get(i);
            String value = element.attribute("value") == null ? null :
                element.attribute("value").getStringValue();
            if (i == 0)
            {
                xOff = Pub.toInt(element.attribute("x").getStringValue());
                cols += xOff;
                table.setLockIndex(xOff);
                Element hnode = root.element("H");
                String hvalue = "��";
                if (hnode != null)
                {
                    if (hnode.attribute("line").getStringValue().equals("true"))
                        hvalue = "id=" + hnode.attribute("id").getStringValue() +
                            ":"
                            + hnode.attribute("value1").getStringValue()
                            + "////" + hnode.attribute("value2").getStringValue();
                    else
                        hvalue = hnode.attribute("value").getStringValue();
                }
                for (int k = 0; k < cols; k++)
                {
                    table.addField(index, hvalue,
                                   0, // ��������� 0,�ַ�1������
                                   index + 1, // ��ʾλ�ã��� 1 ��ʼ
                                   0, // �п�ȣ�0 Ϊ�Զ�����
                                   null, // ͳ��ָ�� "sum:0",num:0,avg:2  ||  ָ��:����
                                   index + 1, // ��������ţ��� 1 ��ʼ����group by �е�˳��
                                   null // ���ӵ�����λ��
                        );
                    index++;
                }
            }
            if (this.empty(value))
                break;
//            ArrayList arr = doAnalyzeX(element, yList, table, cols, true);
            ArrayList arr = doAnalyzeX(element,request);
            if (arr != null)
                xarr.addAll(arr);
            for (int j = 0; j < arr.size(); j++)
            {
                Condition cond = (Condition) arr.get(j);
                table.addField(index, cond.title[0],
                               0, // ��������� 0,�ַ�1������
                               index + 1, // ��ʾλ�ã��� 1 ��ʼ
                               0, // �п�ȣ�0 Ϊ�Զ�����
                               "sum:0", // ͳ��ָ�� "sum:0",num:0,avg:2  ||  ָ��:����
                               0, // ��������ţ��� 1 ��ʼ����group by �е�˳��
                               null // ���ӵ�����λ��
                    );
                index++;
                table.setAllcols(index);
            }
        }
        ArrayList yarr = new ArrayList();
        for (int j = 0; j < yList.size(); j++)
        {
            Element element = (Element) yList.get(j);
            if (j == 0)
            {
                yOff = Pub.toInt(element.attribute("y").getStringValue());
            }
            String value = element.attribute("value") == null ? null :
                element.attribute("value").getStringValue();
            if (this.empty(value))
                break;
//            ArrayList arr = doAnalyzeY(element, yList, table, cols, true);
            ArrayList arr = doAnalyzeY(element,request);
            if (yarr != null)
                yarr.addAll(arr);
        }
        List tList = root.selectNodes("//TABLES/RESPONSE/RESULT/ROW");
        String tableStr = "";
        for (int i = 0; i < tList.size(); i++)
        {
            Element row = (Element) tList.get(i);
            if (i == 0)
            {
                tableStr += row.element("OWNER").getText() + "." +
                    row.element("TID").getText();
            }
            else
                tableStr += "," + row.element("OWNER").getText() + "." +
                    row.element("TID").getText();
        }

        List rList = root.selectNodes("//RELATIONS/RELATION");
        String relationStr = "(";
        for (int i = 0; i < rList.size(); i++)
        {
            Element relation = (Element) rList.get(i);
            String logic = relation.attribute("value").getStringValue();
            if (i == 0)
            {
                relationStr +=
                    relation.element("FIELD1").element("ROW").element("OWNER").
                    getText()
                    + "." +
                    relation.element("FIELD1").element("ROW").element("TID").
                    getText()
                    + "." +
                    relation.element("FIELD1").element("ROW").element("CID").
                    getText()
                    + " " + (logic.equals("=(+)") ? "=" : logic) + " "
                    +
                    relation.element("FIELD2").element("ROW").element("OWNER").
                    getText()
                    + "." +
                    relation.element("FIELD2").element("ROW").element("TID").
                    getText()
                    + "." +
                    relation.element("FIELD2").element("ROW").element("CID").
                    getText() + (logic.equals("=(+)") ? "(+)" : "");
            }
            else
            {
                relationStr += " and " +
                    relation.element("FIELD1").element("ROW").element("OWNER").
                    getText()
                    + "." +
                    relation.element("FIELD1").element("ROW").element("TID").
                    getText()
                    + "." +
                    relation.element("FIELD1").element("ROW").element("CID").
                    getText()
                    + " " + (logic.equals("=(+)") ? "=" : logic) + " "
                    +
                    relation.element("FIELD2").element("ROW").element("OWNER").
                    getText()
                    + "." +
                    relation.element("FIELD2").element("ROW").element("TID").
                    getText()
                    + "." +
                    relation.element("FIELD2").element("ROW").element("CID").
                    getText() + (logic.equals("=(+)") ? "(+)" : "");
            }
        }
        relationStr += ")";
        if (relationStr.length() < 3)
            relationStr = "";
        String filterStr = getFilterString(root);
        request.getSession().setAttribute("xcondition", xarr);
        request.getSession().setAttribute("ycondition", yarr);
        request.getSession().setAttribute("tablestr", tableStr);
        request.getSession().setAttribute("relationstr", relationStr);
        request.getSession().setAttribute("filterstr", filterStr);
        request.getSession().setAttribute("tabledefinedoc", doc);
        return doExecute(root, table, xOff, xarr, yarr, tableStr, relationStr,
                         filterStr);
    }

    private String getFilterString(String condition)
        throws Exception
    {
        Document doc = DocumentHelper.parseText(condition);
        return getFilterString(doc.getRootElement());
    }

    private String getFilterString(Element root)
        throws Exception
    {
        List rList = root.selectNodes("//FILTERS/FILTER");
        String str = "(";
        for (int i = 0; i < rList.size(); i++)
        {
            Element filter = (Element) rList.get(i);
            String code = filter.element("CODE") == null ? "" :
                filter.element("CODE").getText();
            String value = filter.element("VALUE").getText();
            if(Pub.empty(value)) continue;
            //// process format string here...
            String format = filter.element("FORMAT") == null ? null :
                filter.element("FORMAT").getText();
            if (format != null && format.equals("undefined"))
                format = null;
            if (format == null)
                format = "YYYYMMDDHH24MISS";
            //// process format string end.
            if (empty(code))
                code = value;
            String logic = filter.element("LOGIC").getText();
            String operate = filter.element("OPERATE").getText();
            String cname = filter.element("ROW").element("OWNER").getText()
                + "." + filter.element("ROW").element("TID").getText()
                + "." + filter.element("ROW").element("CID").getText();
            int type = Pub.toInt(filter.element("ROW").element("STYLE").getText());
            Element additional = filter.element("ADDITIONAL");
            Element formatnode = additional == null ? null :
                additional.element("FORMAT");
            int formattype = formatnode == null ? 0 :
                Pub.toInt(formatnode.attribute("type").getStringValue());
            if (i > 0)
            {
                str += " " + logic + " ";
            }
            switch (formattype)
            {
                case 1:
                    switch (type)
                    {

                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        default:
                            int start = Pub.toInt(formatnode.element("START").
                                                  getText());
                            int end = Pub.toInt(formatnode.element("END").
                                                getText());
                            if (end > 0)
                                code = "substr('" + code + "'," + start + "," +
                                    end +
                                    ")";
                            else if (end == 0 && start > 0)
                                code = "substr('" + code + "'," + start + ")";
                            else
                                code = "'" + code + "'";
                            str += cname + " " + operate + " " + code;
                            break;
                        case 4:
                        case 5:
                            if (type == 4)
                            {
                                if (operate.equals("="))
                                {
                                    str += "(" + cname + " >= to_date('" + code +
                                        "000000','" + format + "')"
                                        + " and " + cname + " <= to_date('" +
                                        code + "235959','" + format + "'))";
                                }
                                else if (operate.equals("!=") ||
                                         operate.equals("<>"))
                                {
                                    str += "(" + cname + " < to_date('" + code +
                                        "000000','" + format + "')"
                                        + " or " + cname + " > to_date('" +
                                        code + "235959','" + format + "'))";
                                }
                                else
                                    str += cname + " " + operate + " to_date('" +
                                        code + "','" + format + "')";
                            }
                            else if (type == 5)
                            {
                                //datetime
                            }
                            break;
                    }
                    break;
                case 2:

                    //format = formatnode.getText();
                    if (type == 4)
                    {
                        if (operate.equals("="))
                        {
                            str += "(" + cname + " >= to_date('" + code +
                                "000000','" + format + "')"
                                + " and " + cname + " <= to_date('" + code +
                                "235959','" + format + "'))";
                        }
                        else if (operate.equals("!=") || operate.equals("<>"))
                        {
                            str += "(" + cname + " < to_date('" + code +
                                "000000','" + format + "')"
                                + " or " + cname + " > to_date('" + code +
                                "235959','" + format + "'))";
                        }
                        else
                            str += cname + " " + operate + " to_date('" + code +
                                "','" + format + "')";
                    }
                    else if (type == 5)
                    {
                        //datetime
                    }
                    else
                    {
                        str += cname + " " + operate + " '" + code + "'";
                    }
                    break;
                case 3:
                    format = formatnode.getText();
                    if (!empty(format))
                        code = format.replaceAll("\\?", code);
                    else
                        code = "'" + code + "'";
                    str += cname + " " + operate + " " + code;
                    break;
                case 0:
                default:
                    str += cname + " " + operate + " '" + code + "'";
                    break;
            }
        }
        str += ")";
        if (str.length() < 3)
            str = "";
        return str;
    }

    private String getFilterStringByTableList(Element root, String tabList)
    {
        if (empty(tabList))
            return "";
        String list[] = tabList.split(",");
        List rList = root.selectNodes("//FILTERS/FILTER");
        String str = "(";
        for (int i = 0; i < rList.size(); i++)
        {
            Element filter = (Element) rList.get(i);
            String code = filter.element("CODE") == null ? "" :
                filter.element("CODE").getText();
            String value = filter.element("VALUE").getText();
            if(Pub.empty(value)) continue;
            //// process format string here...
            String format = filter.element("FORMAT") == null ? null :
                filter.element("FORMAT").getText();
            if (format != null && format.equals("undefined"))
                format = null;
            if (format == null)
                format = "YYYYMMDDHH24MISS";
            //// process format string end.
            if (empty(code))
                code = value;
            String logic = filter.element("LOGIC").getText();
            String operate = filter.element("OPERATE").getText();
            String cname = filter.element("ROW").element("OWNER").getText()
                + "." + filter.element("ROW").element("TID").getText()
                + "." + filter.element("ROW").element("CID").getText();
            if (list != null)
            {
                boolean f1 = false;
                for (int n = 0; n < list.length; n++)
                {
                    if (!Pub.empty(list[n]))
                    {
                        if (cname.indexOf(list[n].trim()) >= 0)
                        {
                            f1 = true;
                            break;
                        }
                    }
                }
                if (!f1)
                    continue;
            }
            int type = Pub.toInt(filter.element("ROW").element("STYLE").getText());
            Element additional = filter.element("ADDITIONAL");
            Element formatnode = additional == null ? null :
                additional.element("FORMAT");
            int formattype = formatnode == null ? 0 :
                Pub.toInt(formatnode.attribute("type").getStringValue());
            if (str.length() > 3)
            {
                str += " " + logic + " ";
            }
            switch (formattype)
            {
                case 1:
                    switch (type)
                    {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        default:
                            int start = Pub.toInt(formatnode.element("START").
                                                  getText());
                            int end = Pub.toInt(formatnode.element("END").
                                                getText());
                            if (end > 0)
                                code = "substr('" + code + "'," + start + "," +
                                    end +
                                    ")";
                            else if (end == 0 && start > 0)
                                code = "substr('" + code + "'," + start + ")";
                            else
                                code = "'" + code + "'";
                            str += cname + " " + operate + " " + code;
                            break;
                        case 4:

                        case 5:
                            if (type == 4)
                            {
                                if (operate.equals("="))
                                {
                                    str += "(" + cname + " >= to_date('" + code +
                                        "000000','" + format + "')"
                                        + " and " + cname + " <= to_date('" +
                                        code + "235959','" + format + "'))";
                                }
                                else if (operate.equals("!=") ||
                                         operate.equals("<>"))
                                {
                                    str += "(" + cname + " < to_date('" + code +
                                        "000000','" + format + "')"
                                        + " or " + cname + " > to_date('" +
                                        code + "235959','" + format + "'))";
                                }
                                else
                                    str += cname + " " + operate + " to_date('" +
                                        code + "','" + format + "')";
                            }
                            else if (type == 5)
                            {
                                //datetime
                            }
                            break;
                    }
                    break;
                case 2:

                    //format = formatnode.getText();
                    if (type == 4)
                    {
                        if (operate.equals("="))
                        {
                            str += "(" + cname + " >= to_date('" + code +
                                "000000','" + format + "')"
                                + " and " + cname + " <= to_date('" + code +
                                "235959','" + format + "'))";
                        }
                        else if (operate.equals("!=") || operate.equals("<>"))
                        {
                            str += "(" + cname + " < to_date('" + code +
                                "000000','" + format + "')"
                                + " or " + cname + " > to_date('" + code +
                                "235959','" + format + "'))";
                        }
                        else
                            str += cname + " " + operate + " to_date('" + code +
                                "','" + format + "')";
                    }
                    else if (type == 5)
                    {
                        //datetime
                    }
                    else
                    {
                        str += cname + " " + operate + " '" + code + "'";
                    }
                    break;
                case 3:
                    format = formatnode.getText();
                    if (!empty(format))
                        code = format.replaceAll("\\?", code);
                    else
                        code = "'" + code + "'";
                    str += cname + " " + operate + " " + code;
                    break;
                case 0:
                default:
                    str += cname + " " + operate + " '" + code + "'";
                    break;
            }
        }
        str += ")";
        if (str.length() < 3)
            str = "";
        return str;
    }

    private String getRelationStringByTableList(Element root, String tabList)
    {
        if (this.empty(tabList))
            return "";
        String[] list = tabList.split(",");
        List rList = root.selectNodes("//RELATIONS/RELATION");
        String relationStr = "(";
        for (int i = 0; i < rList.size(); i++)
        {
            Element relation = (Element) rList.get(i);
            String logic = relation.attribute("value").getStringValue();
            String c1 = relation.element("FIELD1").element("ROW").element(
                "OWNER").
                getText()
                + "." +
                relation.element("FIELD1").element("ROW").element("TID").
                getText()
                + "." +
                relation.element("FIELD1").element("ROW").element("CID").
                getText();
            String c2 = relation.element("FIELD2").element("ROW").element(
                "OWNER").
                getText()
                + "." +
                relation.element("FIELD2").element("ROW").element("TID").
                getText()
                + "." +
                relation.element("FIELD2").element("ROW").element("CID").
                getText();
            if (list != null)
            {
                boolean f1 = false, f2 = false;
                for (int n = 0; n < list.length; n++)
                {
                    if (!Pub.empty(list[n]))
                    {
                        if (c1.indexOf(list[n].trim()) >= 0)
                            f1 = true;
                        if (c2.indexOf(list[n].trim()) >= 0)
                            f2 = true;
                        if (f1 && f2)
                            break;
                    }
                }
                if (!f1 || !f2)
                    continue;
            }
            if (relationStr.length() < 3)
            {
                relationStr += c1 + " " + (logic.equals("=(+)") ? "=" : logic) +
                    " "
                    + c2 + (logic.equals("=(+)") ? "(+)" : "");
            }
            else
            {
                relationStr += " and " + c1 + " " +
                    (logic.equals("=(+)") ? "=" : logic)
                    + " " + c2 + (logic.equals("=(+)") ? "(+)" : "");
            }
        }
        relationStr += ")";
        if (relationStr.length() < 3)
            relationStr = "";
        return relationStr;
    }

    private String[][] doExecute(Element root, QueryTable table, int xOffset,
                                 ArrayList xArr, ArrayList yArr, String tabStr,
                                 String relationStr, String filterStr)
        throws
        Exception
    {
        String id = root.attribute("id") == null ? "" :
            root.attribute("id").getStringValue();
        if (id == null || id.equals("null"))
            id = "";
        String sql = "";
        String[] tablist = tabStr.split(",");

        String[][] data = new String[yArr.size()][xOffset + xArr.size()];
        table.setData(data);
        int xOff = xOffset;
        for (int i = 0; i < xArr.size(); i++)
        {
            Condition xCond = (Condition) xArr.get(i);
            ArrayList sArr = xCond.summ;
            int yOff = 0;
            for (int j = 0; j < yArr.size(); j++)
            {
                Condition yCond = (Condition) yArr.get(j);
                Condition sCond = null;
                for (int k = 0; k < sArr.size(); k++)
                {
                    Condition summ = (Condition) sArr.get(k);
                    if (summ.y == yCond.y)
                    {
                        sCond = summ;
                        break;
                    }
                }
                if (sCond != null)
                {
                    ArrayList lockAreaList = getLockAreaList(yCond, xOff);
                    tabStr = "";
                    boolean alltab = true;
                    for (int p = 0; p < tablist.length; p++)
                    {
                        if (this.empty(tablist[p]))
                            continue;
                        if ( (sCond.colname + yCond.cond +
                              xCond.cond).indexOf(tablist[p].trim()) < 0)
                        {
                            alltab = false;
                            continue;
                        }
                        if (tabStr.length() <= 1)
                            tabStr += tablist[p];

                        else
                            tabStr += "," + tablist[p];
                    }
                    String relation = "";
                    String filter = "";
                    if (!this.empty(relationStr))
                        relation = this.getRelationStringByTableList(root,
                            tabStr);
                    if (!this.empty(filterStr))
                        filter = this.getFilterStringByTableList(root,
                            tabStr);
                    sql = "select " + sCond.colname + " from " + tabStr
                        + " where " + yCond.cond + " and " + xCond.cond;
                    if (!this.empty(relation)) // && alltab)
                        sql += " and " + relation;
                    if (!this.empty(filter))
                        sql += " and " + filter;
                    String[][] list = DBUtil.query(sql);
                    for (int z = 0; z < (list == null ? 1 : list.length); z++)
                    {
                        for (int p = 0; p < xOffset; p++)
                        {
                            if (p < lockAreaList.size())
                            {
                                data[z + yOff][p] = (String) lockAreaList.get(p);
                            }
                            else
                                data[z +
                                    yOff][p] = (String) lockAreaList.get(
                                        lockAreaList.
                                        size() - 1);
                        }
                    }
                    if (list != null)
                    {
                        for (int x = 0; x < list.length; x++)
                        {
                            for (int y = 0; y < list[x].length; y++)
                            {
                                if (sCond.detail != null)
                                {
                                    table.fields[y +
                                        xOff].setColLink(
                                            "/" + Pub.PREFIX +
                                            "/FrameWork/FreeTableAction.do?method=showDetail&id=" +
                                            id + "&x=" +
                                            i + "&");
                                }
                                data[x + yOff][y + xOff] = list[x][y];
                            }
                        }
                        yOff += list.length;
                    }
                    else
                    {
                        yOff += 1;
                    }
                }
            }
            xOff += 1;
        }
        return data;
    }

    private ArrayList getLockAreaList(Condition yCond, int xOff)
    {
        ArrayList list = new ArrayList();
        if (yCond != null && xOff > 0)
        {
            if (yCond.title.length < xOff)
            {
                for (int i = 0; i < yCond.title.length; i++)
                {
                    list.add(yCond.title[i]);
                }
                for (int i = yCond.title.length; i <= xOff; i++)
                {
                    list.add(yCond.title[yCond.title.length - 1]);
                }
            }
            else if (yCond.title.length == xOff)
            {
                for (int i = 0; i < yCond.title.length; i++)
                {
                    list.add(yCond.title[i]);
                }
            }
            else if (yCond.title.length > xOff)
            {
                for (int i = 0; i < xOff; i++)
                {
                    list.add(yCond.title[i]);
                }
            }
        }
        return list;
    }

    private String getLockAreaStr(Condition yCond, int xOff)
    {
        String str = "";
        if (yCond != null && xOff > 0)
        {
            if (yCond.title.length < xOff)
            {
                for (int i = 0; i < yCond.title.length; i++)
                {
                    str += "'" + yCond.title[i] + "'";
                }
                for (int i = yCond.title.length; i <= xOff; i++)
                {
                    str += "'" + yCond.title[yCond.title.length - 1] + "'";
                }
            }
            else if (yCond.title.length == xOff)
            {
                for (int i = 0; i < yCond.title.length; i++)
                {
                    str += "'" + yCond.title[i] + "'";
                }
            }
            else if (yCond.title.length > xOff)
            {
                for (int i = 0; i < xOff; i++)
                {
                    str += "'" + yCond.title[i] + "'";
                }
            }
        }
        return str.replaceAll("''", "','");
    }

//    private ArrayList doAnalyzeY(Element element, List yList, QueryTable table,
//                                 int cols, boolean exec)
    private ArrayList doAnalyzeY(Element element,HttpServletRequest request)
        throws Exception
    {
        int index = Pub.toInt(element.attribute("y").getStringValue());
        String title = element.attribute("value") == null ? "" :
            element.attribute("value").getStringValue();
        ArrayList arr = new ArrayList();
        ArrayList cList = getCellConditions(element,request);
        List xList = element.selectNodes("CELL");
        if (xList == null || xList.size() == 0)
        {
            if (cList != null)
                for (int i = 0; i < cList.size(); i++)
                {
                    Condition cond = (Condition) cList.get(i);
                    cond.index = index;
                    arr.add(cond);
                }
        }
        else
        {
            for (int i = 0; i < xList.size(); i++)
            {
                Element cell = (Element) xList.get(i);
                index = Pub.toInt(cell.attribute("y").getStringValue());
                title = element.attribute("value") == null ? "" :
                    element.attribute("value").getStringValue();
                if (cell.attribute("value") == null ||
                    cell.attribute("value").equals(""))
                    continue;
//                ArrayList sqlarr = doAnalyzeY(cell, yList, table, cols, true);
                ArrayList sqlarr = doAnalyzeY(cell,request);
                if (cList != null)
                    for (int j = 0; j < cList.size(); j++)
                    {
                        Condition currcond = (Condition) cList.get(j);
                        for (int k = 0; k < sqlarr.size(); k++)
                        {
                            Condition oldcond = (Condition) sqlarr.get(k);
                            Condition cond = new Condition();
                            cond.index = oldcond.index;
                            cond.x = oldcond.x;
                            cond.y = oldcond.y;
                            cond.cond = new String(oldcond.cond);
                            cond.title = new String[currcond.title.length +
                                oldcond.title.length];
                            for (int n = 0; n < currcond.title.length; n++)
                            {
                                cond.title[n] = currcond.title[n];
                            }
                            int p = 0;
                            for (int n = currcond.title.length;
                                 n < cond.title.length; n++, p++)
                            {
                                cond.title[n] = oldcond.title[p];
                            }
                            cond.cond = currcond.cond + " and " + cond.cond;
                            arr.add(cond);
                        }
                    }
            }
        }
        return arr;
    }

//    private ArrayList doAnalyzeX(Element element, List yList, QueryTable table,
//                                 int cols, boolean exec)
    private ArrayList doAnalyzeX(Element element,HttpServletRequest request)
        throws Exception
    {
        int index = Pub.toInt(element.attribute("x").getStringValue());
        String title = element.attribute("value") == null ? "" :
            element.attribute("value").getStringValue();
        ArrayList arr = new ArrayList();
        ArrayList cList = getCellConditions(element,request);
        List xList = element.selectNodes("CELL");
        if (xList == null || xList.size() == 0)
        {
            ArrayList sArr = getCellSummarys(element);
            if (cList != null)
                for (int i = 0; i < cList.size(); i++)
                {
                    Condition cond = (Condition) cList.get(i);
                    cond.summ = sArr;
                    cond.index = index;
                    arr.add(cond);
                }
        }
        else
        {
            for (int i = 0; i < xList.size(); i++)
            {
                Element cell = (Element) xList.get(i);
                index = Pub.toInt(cell.attribute("x").getStringValue());
                title = element.attribute("value") == null ? "" :
                    element.attribute("value").getStringValue();
                if (cell.attribute("value") == null ||
                    cell.attribute("value").equals(""))
                    continue;
//                ArrayList sqlarr = doAnalyzeX(cell, yList, table, cols, true);
                ArrayList sqlarr = doAnalyzeX(cell,request);
                if (cList != null)
                    for (int j = 0; j < cList.size(); j++)
                    {
                        Condition currcond = (Condition) cList.get(j);
                        for (int k = 0; k < sqlarr.size(); k++)
                        {
                            Condition oldcond = (Condition) sqlarr.get(k);
                            Condition cond = new Condition();
                            cond.index = oldcond.index;
                            cond.x = oldcond.x;
                            cond.y = oldcond.y;
                            cond.cond = new String(oldcond.cond);
                            cond.title[0] = new String(oldcond.title[0]);
                            cond.cond = currcond.cond + " and " + cond.cond;
                            cond.title[0] = currcond.title[0] + "->" +
                                cond.title[0];
                            cond.summ = oldcond.summ;
                            arr.add(cond);
                        }
                    }
            }
        }
        return arr;
    }

    private ArrayList getCellSummarys(Element cell)
        throws Exception
    {
        ArrayList arr = null;
        Element summ = cell.element("SUMM");
        if (summ == null)
            return null;
        List xList = summ.selectNodes("CELL");
        if (xList == null || xList.size() == 0)
            return null;
        arr = new ArrayList(xList.size());
        for (int i = 0; i < xList.size(); i++)
        {
            Element summcell = (Element) xList.get(i);
            String str = summcell.element("DETAIL") == null ? null :
                summcell.element("DETAIL").asXML();
            if (summcell.attribute("kind") == null)
            {
                Condition cond = new Condition();
                cond.x = Pub.toInt(summcell.attribute("x").getStringValue());
                cond.y = Pub.toInt(summcell.attribute("y").getStringValue());
                cond.colname = "count(1)";
                cond.detail = str;
                arr.add(cond);
            }
            else
            {
                Condition cond = getSummary(summcell);
                cond.detail = str;
                arr.add(cond);
            }
        }
        return arr;
    }

    private Condition getSummary(Element cell)
        throws Exception
    {
        if (cell == null)
            return null;
        String exp = (cell.attribute("exp") == null ||
                      Pub.empty(cell.attribute("exp").getStringValue())) ?
            "[1]" : cell.attribute("exp").getStringValue();
        String sum = (cell.attribute("summary") == null ||
                      Pub.empty(cell.attribute("summary").getStringValue())) ?
            "count" : cell.attribute("summary").getStringValue();
        List rList = cell.selectNodes("SUMMARY/ROW");
        for (int i = 1; i <= rList.size(); i++)
        {
            Element row = (Element) rList.get(i - 1);
            String col = row.element("OWNER").getText() + "."
                + row.element("TID").getText() + "."
                + row.element("CID").getText();
            exp = exp.replaceAll("\\[" + i + "\\]", col);
        }
        Condition cond = new Condition();
        cond.x = Pub.toInt(cell.attribute("x").getStringValue());
        cond.y = Pub.toInt(cell.attribute("y").getStringValue());
        cond.colname = sum + "(" + exp + ")";
        return cond;
    }

    private ArrayList getCellConditions(Element cell,HttpServletRequest request)
        throws Exception
    {
        ArrayList arr = null;
        if (cell != null)
        {
            Element cond = cell.element("COND");
            if (cond == null)
                return getDefaultConditions(cell);
            if (cond.attribute("type").getValue().equals("static"))
            {
                List rows = cond.selectNodes("ROW");
                if (rows != null && rows.size() > 0)
                {
                    arr = new ArrayList(rows.size());
                    int found = -1;
                    for (int i = 0; i < rows.size(); i++)
                    {
                        Element row = (Element) rows.get(i);
                        Condition condition = getCondition(cell, row);
                        if (condition != null &&
                            condition.cond.indexOf("����") >= 0)
                        {
                            found = i;
                        }
                        arr.add(condition);
                    }
                    if (found > -1)
                    {
                        String cstr = "";
                        Condition otherCond = null;
                        for (int i = 0; i < arr.size(); i++)
                        {
                            Condition condition = (Condition) arr.get(i);
                            if (i != found)
                            {
                                if (condition.cond.length() > 3)
                                {
                                    if (cstr.length() < 3)
                                        cstr += " NOT" + condition.cond;
                                    else
                                        cstr += " AND NOT" + condition.cond;
                                }
                            }
                            else
                            {
                                otherCond = condition;
                            }
                        }
                        otherCond.cond = cstr;
                    }
                }
            }
            else if (cond.attribute("type").getValue().equals("dynamic"))
            {
                String sql = cond.getText();
//              [�û��ʺ�][�û�����][ϵͳʱ��][�ܼ�����]
                sql = sql.replaceAll("\\[�û��ʺ�\\]","\'"+Pub.getUserAccount(request)+"'");
                sql = sql.replaceAll("\\[�û�����\\]","'"+Pub.getUserDepartment(request)+"'");
                sql = sql.replaceAll("\\[ϵͳʱ��\\]","sysdate");
                sql = sql.replaceAll("\\[�ܼ�����\\]",Pub.getUserSecrecyLevel(request));
                String[][] vlist = DBUtil.query(sql);
                if(vlist == null)
                    return null;
                arr = new ArrayList(vlist.length);
                String col = getColName(cell);
                for(int i=0,len=vlist.length;i<len;i++)
                {
                    Condition condObj = new Condition();
                    condObj.colname = col;
                    String str = " (";
                    String logic = "=";
                    String value = formatConditionValue(cell, vlist[i][0]);
                    str += col + " " + logic + " " + value;
                    condObj.cond = str + ") ";
                    if (!Pub.empty(vlist[i][1]))
                        condObj.title[0] = vlist[i][1];
                    else
                        condObj.title[0] = "";
                    condObj.x = Pub.toInt(cell.attribute("x").getStringValue());
                    condObj.y = Pub.toInt(cell.attribute("y").getStringValue());
                    arr.add(condObj);
                }
            }
        }
        return arr;
    }

    private Condition getCondition(Element cell, Element row)
        throws Exception
    {
        if (row == null || cell == null)
            return null;
        Condition cond = new Condition();
        String col = getColName(cell);
        cond.colname = col;
        String str = " (";
        List clist = row.selectNodes("C");
        if (clist != null && clist.size() > 0)
        {
            for (int i = 0; i < clist.size(); i++)
            {
                Element c = (Element) clist.get(i);
                String logic = c.element("LOGIC").getText();
                if(logic != null && (logic.toUpperCase().trim().equals("IS NULL")
                || logic.toUpperCase().trim().equals("IS NOT NULL")))
                {
                    if (str.length() < 3)
                        str += col + " " + logic + " ";
                    else
                        str += " or " + col + " " + logic + " ";
                }
                else if (c.element("VALUE") != null)
                {
                    String value = c.element("VALUE").attribute("code") == null ?
                        c.element("VALUE").getText() :
                        c.element("VALUE").attribute("code").getStringValue();
                    value = formatConditionValue(cell, value);
                    if (str.length() < 3)
                        str += col + " " + logic + " " + value;
                    else
                        str += " or " + col + " " + logic + " " + value;
                }
                else if (c.element("VMIN") != null)
                {
                    String vmin = c.element("VMIN").attribute("code") == null ?
                        c.element("VMIN").getText() :
                        c.element("VMIN").attribute("code").getStringValue();
                    String vmax = c.element("VMAX").attribute("code") == null ?
                        c.element("VMAX").getText() :
                        c.element("VMAX").attribute("code").getStringValue();
                    vmin = formatConditionValue(cell, vmin);
                    vmax = formatConditionValue(cell, vmax);
                    if (logic.toUpperCase().equals("AND"))
                    {
                        if (str.length() < 3)
                            str += col + ">=" + vmin + " and " + col + "<=" +
                                vmax;
                        else
                            str += " or " + col + ">=" + vmin + " and " + col +
                                "<=" + vmax;
                    }
                    else
                    {
                        if (str.length() < 3)
                            str += col + "<=" + vmin + " or " + col + ">=" +

                                vmax;
                        else
                            str += " or " + col + "<=" + vmin + " or " + col +
                                ">=" + vmax;
                    }
                }
//                else
//                {
//                    String value = c.element("VALUE").attribute("code") == null ?
//                        c.element("VALUE").getText() :
//                        c.element("VALUE").attribute("code").getStringValue();
//                    value = formatConditionValue(cell, value);
//                    if (str.length() < 3)
//                        str += col + " " + logic + " " + value;
//                    else
//                        str += " or " + col + " " + logic + " " + value;
//                }
            }
        }
        else if (clist == null || clist.size() == 0)
        {
            if (row.element("DSCR") != null &&
                row.element("DSCR").getText().indexOf("����") >= 0)
            {
                str += "����";
            }
        }
        cond.cond = str + ") ";
        if (row.element("DSCR") != null && row.element("DSCR").getText() != null)
            cond.title[0] = row.element("DSCR").getText().replaceAll("->", ":");
        else
            cond.title[0] = "";
        cond.x = Pub.toInt(cell.attribute("x").getStringValue());
        cond.y = Pub.toInt(cell.attribute("y").getStringValue());
        return cond;
    }

    private String getColName(Element cell)
        throws Exception
    {
        return cell.element("ROW").element("OWNER").getText() + "."
            + cell.element("ROW").element("TID").getText() + "."
            + cell.element("ROW").element("CID").getText();
    }

    private String formatConditionValue(Element cell, String value)
        throws
        Exception
    {
        String type = cell.element("ROW").element("DATA_TYPE").getText();
        if (type.toUpperCase().equals("DATE"))
        {
            value = "to_date('" + value + "','YYYYMMDDHH24MISS')";
        }
        else if (type.toUpperCase().equals("NUMBER"))
            ;
        else
            value = "'" + value + "'";
        return value;
    }

    private ArrayList getDefaultConditions(Element cell)
        throws Exception
    {
        if (cell == null)
            return null;
        ArrayList res = new ArrayList();
        String kind = cell.attribute("kind") == null ? "" :
            cell.attribute("kind").getStringValue();
        if (!kind.toUpperCase().equals("DIC"))
        { //δ���õĿ�����...�������������...��ȫ��ȡֵ����Ч����
            Condition cond = new Condition();
            cond.colname = getColName(cell);
            cond.x = Pub.toInt(cell.attribute("x").getStringValue());
            cond.y = Pub.toInt(cell.attribute("y").getStringValue());
            cond.cond = " ";
            cond.title[0] = cell.attribute("value") == null ? "" :
                cell.attribute("value").getStringValue().replaceAll("->", ":");
            res.add(cond);
        }
        else
        {
            try
            {
                String dic_table = cell.element("ROW").element("DIC_TABLE").
                    getText();
                String dic_code = cell.element("ROW").element("DIC_CODE").
                    getText();
                String key_field = cell.element("ROW").element("KEY_FIELD").
                    getText();
                String value_field = cell.element("ROW").element("VALUE_FIELD").
                    getText();
                String code_field = cell.element("ROW").element("CODE_FIELD").
                    getText();
                if (Pub.empty(code_field))
                    return res;
                String sql = "select " + key_field + "," + value_field
                    + " from " + dic_table
                    + " where " + code_field + "='" + dic_code + "'"
                    + " and rownum <=" + Pub.MAX_DEFAULT_CONDITIONS
                    + " order by " + key_field;
                String list[][] = DBUtil.query(sql);
                if (list != null)
                {
                    for (int i = 0; i < list.length; i++)
                    {
                        Condition cond = new Condition();
                        cond.colname = getColName(cell);
                        cond.x = Pub.toInt(cell.attribute("x").getStringValue());
                        cond.y = Pub.toInt(cell.attribute("y").getStringValue());
                        cond.cond = " (" + cond.colname + "='" + list[i][0] +
                            "') ";
                        cond.title[0] = list[i][1];
                        res.add(cond);
                    }
                }
            }
            catch (Exception e)
            {}
        }
        return res;
    }

    private void setDynamicValue(Document doc, Document doccond,
                                 HttpServletRequest request)
        throws Exception
    {
        Element root = doc.getRootElement();
        Element rootcond = doccond.getRootElement();
        List cList = rootcond.elements("CONDITION");
        List rList = root.selectNodes("//FILTERS/FILTER");
        for (int i = 0, j = 0; i < rList.size(); i++)
        {
            Element filter = (Element) rList.get(i);
////////////////////////////////////
            String code = filter.element("CODE") == null ? "" :
                filter.element("CODE").getText();
            String value = filter.element("VALUE").getText();
            String format = filter.element("FORMAT") == null ? null :
                filter.element("FORMAT").getText();
            String operate = filter.element("OPERATE").getText();
            String cname = filter.element("ROW").element("OWNER").getText()
                + "." + filter.element("ROW").element("TID").getText()
                + "." + filter.element("ROW").element("CID").getText();
            int fieldtype = Pub.toInt(filter.element("ROW").element("STYLE").
                                      getText());
////////////////////////////////////
            Element addition = filter.element("ADDITIONAL");
            if (addition != null)
            {
                Element type = addition.element("TYPE");
                int fixed = Pub.toInt(type.attribute("fixed").
                                      getStringValue());
                if (type.attribute("value").getStringValue().equals("2"))
                { //dynamic

                    Element condition = (Element) cList.get(j);
                    filter.element("CODE").setText(condition.element("CODE").
                        getText());
                    filter.element("VALUE").setText(condition.element("VALUE").
                        getText());
                    filter.element("OPERATE").setText(condition.element(
                        "OPERATE").getText());
                    j++;
                }
                else if (fixed > 0)
                { //�̶� ��������
                    switch (fixed)
                    {
                        case 1:
                            filter.element("VALUE").setText(Pub.getUserAccount(
                                request));
                            break;
                        case 2:
                            filter.element("VALUE").setText(Pub.
                                getUserDepartment(request));
                            filter.element("CODE").setText(Pub.
                                getUserDepartment(request));
                            break;
                        case 3:
                            filter.element("CODE").setText(Pub.getDate(
                                "yyyyMMdd"));
                            filter.element("VALUE").setText(Pub.getDate(
                                "yyyyMMdd"));
                            break;
                        case 4:
                            filter.element("VALUE").setText(Pub.
                                getUserSecrecyLevel(request));
                            filter.element("CODE").setText(Pub.
                                getUserSecrecyLevel(request));
                            break;
                        default:
                            break;
                    }
                }
                //filter.element("FORMAT").setText(addition.element("FORMAT").getText());
//                    Element addformat = addition.element("FORMAT");
//                    if (addformat.attribute("type").getStringValue().equals(
//                        "2"));

            }
        }
    }

    private void setFixedValue(Document doc, HttpServletRequest request)
        throws Exception
    {
        Element root = doc.getRootElement();
        List rList = root.selectNodes("//FILTERS/FILTER");
        for (int i = 0, j = 0; i < rList.size(); i++)
        {
            Element filter = (Element) rList.get(i);
            String code = filter.element("CODE") == null ? "" :
                filter.element("CODE").getText();
            String value = filter.element("VALUE").getText();
            String format = filter.element("FORMAT").getText();
            String operate = filter.element("OPERATE").getText();
            String cname = filter.element("ROW").element("OWNER").getText()
                + "." + filter.element("ROW").element("TID").getText()
                + "." + filter.element("ROW").element("CID").getText();
            int fieldtype = Pub.toInt(filter.element("ROW").element("STYLE").
                                      getText());
            Element addition = filter.element("ADDITIONAL");
            if (addition != null)
            {
                Element type = addition.element("TYPE");
                int fixed = Pub.toInt(type.attribute("fixed").
                                      getStringValue());
                if (fixed > 0)
                { //�̶� ��������
                    switch (fixed)
                    {
                        case 1:
                            filter.element("VALUE").setText(Pub.getUserAccount(
                                request));
                            break;
                        case 2:
                            filter.element("VALUE").setText(Pub.
                                getUserDepartment(request));
                            filter.element("CODE").setText(Pub.
                                getUserDepartment(request));
                            break;
                        case 3:
                            filter.element("CODE").setText(Pub.getDate(
                                "yyyyMMdd"));
                            filter.element("VALUE").setText(Pub.getDate(
                                "yyyyMMdd"));
                            break;
                        case 4:
                            filter.element("VALUE").setText(Pub.
                                getUserSecrecyLevel(request));
                            filter.element("CODE").setText(Pub.
                                getUserSecrecyLevel(request));
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public static ArrayList parseDynamicCondition(HttpSession session)
    {
        Document doc = (Document) session.getAttribute("FREE_TABLE_DOC");
        try
        {
            ArrayList res = null;
            Element root = doc.getRootElement();
            List rList = root.selectNodes("//FILTERS/FILTER");
            for (int i = 0; i < rList.size(); i++)
            {
                Element filter = (Element) rList.get(i);
////////////////////////////////////

                String code = filter.element("CODE") == null ? "" :
                    filter.element("CODE").getText();
                String value = filter.element("VALUE").getText();
                String format = filter.element("FORMAT") == null ? null :
                    filter.element("FORMAT").getText();
                String logic = filter.element("LOGIC").getText();
                String operate = filter.element("OPERATE").getText();
                String cname = filter.element("ROW").element("OWNER").getText()
                    + "." + filter.element("ROW").element("TID").getText()
                    + "." + filter.element("ROW").element("CID").getText();
                int fieldtype = Pub.toInt(filter.element("ROW").element("STYLE").
                                          getText());
                String dicfile = filter.element("ROW").element("DIC_FILE").
                    getText();
////////////////////////////////////
                Element addition = filter.element("ADDITIONAL");
                if (addition != null)
                {
                    Element type = addition.element("TYPE");
                    int fixed = Pub.toInt(type.attribute("fixed").
                                          getStringValue());
                    if (type.attribute("value").getStringValue().equals("2"))
                    {
                        Hashtable tab = new Hashtable();
                        tab.put("fieldname", cname);
                        tab.put("fieldmemo",
                                filter.element("ROW").element("CNAME").getText());
                        tab.put("fieldkind", "" + fieldtype);
                        tab.put("dicfile", dicfile);
                        tab.put("condindex", "" + i);
                        List list = addition.elements("DYNAMICLOGIC");
                        String dlogic = "";
                        String defaultLogic = null;
                        for (int j = 0; j < list.size(); j++)
                        {
                            Element dylogic = (Element) list.get(j);
                            dlogic += "<option value='" +
                                dylogic.attribute("op").getStringValue() + "'>"
                                + dylogic.getText() + "</option>\n";
                            if (j == 0)
                                defaultLogic = dylogic.attribute("op").
                                    getStringValue();
                        }
                        tab.put("dynamiclogic", dlogic);
                        tab.put("defaultLogic", defaultLogic);
                        tab.put("defaultvalue", value);
                        tab.put("defaultcode", code);
                        Element addformat = addition.element("FORMAT");
                        if (addformat.attribute("type").getStringValue().equals(
                            "2"))
                            tab.put("fieldformat", addformat.getText());
                        if (res == null)
                            res = new ArrayList();
                        res.add(tab);
                    }
                }
            }
            return res;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private boolean hasDynamicCondition(Document doc, String id,
                                        HttpServletRequest req)
        throws Exception
    {
        Element root = doc.getRootElement();
        List rList = root.selectNodes("//FILTERS/FILTER");
        boolean found = false;
        for (int i = 0; i < rList.size(); i++)
        {
            Element filter = (Element) rList.get(i);
            Element addition = filter.element("ADDITIONAL");
            if (addition != null)
            {
                Element type = addition.element("TYPE");
                if (type.attribute("value").getStringValue().equals("2"))
                {
                    found = true;
                    req.getSession().setAttribute("FREE_TABLE_DOC", doc);
                    break;
                }
            }
        }
        return found;
    }

    private boolean hasFixedCondition(Document doc, String id,
                                      HttpServletRequest req)
        throws Exception
    {
        Element root = doc.getRootElement();
        List rList = root.selectNodes("//FILTERS/FILTER");
        boolean found = false;
        for (int i = 0; i < rList.size(); i++)
        {
            Element filter = (Element) rList.get(i);
            Element addition = filter.element("ADDITIONAL");
            if (addition != null)
            {
                Element type = addition.element("TYPE");
                if (Pub.toInt(type.attribute("fixed").getStringValue()) > 0)
                {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    public ActionForward doQueryFreeTable(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response)
        throws
        Exception
    {
        try
        {
            String id = request.getParameter("id");
            if (id == null)
                throw new Exception("error get definition!");
            request.getSession().setAttribute("freeid", id);
            request.getSession().setAttribute("action", "execute");
            String docstr = this.getFreeStat(id);
            Document doc = DocumentHelper.parseText(docstr);
            if (hasDynamicCondition(doc, id, request))
                return mapping.findForward("condition");
            else if (hasFixedCondition(doc, id, request))
                setFixedValue(doc, request);
            doc.getRootElement().addAttribute("id", id);
            String res = doQuery(doc, request);
            writeExecute(res, doc, request, response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return mapping.findForward("error");
        }
        return null;
    }

    public ActionForward showDetail(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
        throws
        Exception
    {
        Document docdefine = (Document) request.getSession().getAttribute(
            "tabledefinedoc");
        Element rootdef = docdefine == null ? null : docdefine.getRootElement();
        ArrayList xarr = (ArrayList) request.getSession().getAttribute(
            "xcondition");
        ArrayList yarr = (ArrayList) request.getSession().getAttribute(
            "ycondition");
        String tabstr = (String) request.getSession().getAttribute("tablestr");
        String relationStr = (String) request.getSession().getAttribute(
            "relationstr");
        String filter = (String) request.getSession().getAttribute("filterstr");
        String condinfo = (String) request.getSession().getAttribute("condinfo");

        String stat_id = (String) request.getSession().getAttribute(
            "stat_info_id");
        String id = Pub.val(request, "id");
        if (Pub.empty(condinfo))
        {
            if (!Pub.empty(stat_id))
            {
                String sql = "select id,pid from fw_free_stat_info where id='" +
                    stat_id + "'";
                String[][] list = DBUtil.querySql(sql);
                if (list != null)
                {
                    id = list[0][1];
                }
                condinfo = this.getFreeStatInfoCondition(stat_id);
            }
        }

        int x = Pub.toInt( (String) request.getParameter("x"));
        int y = Pub.toInt( (String) request.getParameter("y"));
        int pnum = Pub.toInt( (String) request.getParameter("p"));
        int rnum = Pub.toInt( (String) request.getParameter("r"));
        int count = Pub.toInt( (String) request.getParameter("c"));

        if (!Pub.empty(id) && xarr == null)
        {
            docdefine = DocumentHelper.parseText(this.getFreeStat(id));
            rootdef = docdefine.getRootElement();
            List xList = rootdef.selectNodes("//X/CELL");
            List yList = rootdef.selectNodes("//Y/CELL");
            int rows = 0, cols = 0;
            int xOff = 0, yOff = 0;
            ArrayList condArr = new ArrayList();
            int index = 0;
            xarr = new ArrayList();
            for (int i = 0; i < xList.size(); i++)
            {
                Element element = (Element) xList.get(i);
                String value = element.attribute("value").getStringValue();
                if (i == 0)
                {
                    xOff = Pub.toInt(element.attribute("x").getStringValue());
                    cols += xOff;
                }
                if (this.empty(value))
                    break;
                ArrayList arr = doAnalyzeX(element,request);
                if (arr != null)
                    xarr.addAll(arr);
            }
            yarr = new ArrayList();
            for (int j = 0; j < yList.size(); j++)
            {
                Element element = (Element) yList.get(j);
                if (j == 0)
                {
                    yOff = Pub.toInt(element.attribute("y").getStringValue());
                }
                String value = element.attribute("value").getStringValue();
                if (this.empty(value))
                    break;
                ArrayList arr = doAnalyzeY(element,request);
                if (yarr != null)
                    yarr.addAll(arr);
            }
            List tList = rootdef.selectNodes("//TABLES/RESPONSE/RESULT/ROW");
            tabstr = "";
            for (int i = 0; i < tList.size(); i++)
            {
                Element row = (Element) tList.get(i);
                if (i == 0)
                {
                    tabstr += row.element("OWNER").getText() + "." +
                        row.element("TID").getText();
                }
                else
                    tabstr += "," + row.element("OWNER").getText() + "." +
                        row.element("TID").getText();
            }
            List rList = rootdef.selectNodes("//RELATIONS/RELATION");
            relationStr = "(";
            for (int i = 0; i < rList.size(); i++)
            {
                Element relation = (Element) rList.get(i);
                String logic = relation.attribute("value").getStringValue();
                if (i == 0)
                {
                    relationStr +=
                        relation.element("FIELD1").element("ROW").element(
                            "OWNER").
                        getText()
                        + "." +
                        relation.element("FIELD1").element("ROW").element("TID").
                        getText()
                        + "." +
                        relation.element("FIELD1").element("ROW").element("CID").
                        getText()
                        + " " + (logic.equals("=(+)") ? "=" : logic) + " "
                        +
                        relation.element("FIELD2").element("ROW").element(
                            "OWNER").
                        getText()
                        + "." +
                        relation.element("FIELD2").element("ROW").element("TID").
                        getText()
                        + "." +
                        relation.element("FIELD2").element("ROW").element("CID").
                        getText() + (logic.equals("=(+)") ? "(+)" : "");
                }
                else
                {
                    relationStr += " and " +
                        relation.element("FIELD1").element("ROW").element(
                            "OWNER").
                        getText()
                        + "." +
                        relation.element("FIELD1").element("ROW").element("TID").
                        getText()
                        + "." +
                        relation.element("FIELD1").element("ROW").element("CID").
                        getText()
                        + " " + (logic.equals("=(+)") ? "=" : logic) + " "
                        +
                        relation.element("FIELD2").element("ROW").element(
                            "OWNER").
                        getText()
                        + "." +
                        relation.element("FIELD2").element("ROW").element("TID").
                        getText()
                        + "." +
                        relation.element("FIELD2").element("ROW").element("CID").
                        getText() + (logic.equals("=(+)") ? "(+)" : "");
                }
            }
            relationStr += ")";
            if (relationStr.length() < 3)
                relationStr = "";
            filter = getFilterString(rootdef);
        }

        if (!Pub.empty(condinfo))
        {
            filter = getFilterString(condinfo);
        }

        request.getSession().setAttribute("xcondition", xarr);
        request.getSession().setAttribute("ycondition", yarr);
        request.getSession().setAttribute("tablestr", tabstr);
        request.getSession().setAttribute("relationstr", relationStr);
        request.getSession().setAttribute("filterstr", filter);
        request.getSession().setAttribute("condinfo", condinfo);
        request.getSession().setAttribute("tabledefinedoc", docdefine);

        if (pnum == 0)
            pnum = 1;
        Condition xcond = (Condition) xarr.get(x);
        Condition ycond = (Condition) yarr.get(y);
        Condition scond = null;
        ArrayList sArr = xcond.summ;
        for (int k = 0; k < sArr.size(); k++)
        {
            Condition summ = (Condition) sArr.get(k);
            if (summ.y == ycond.y)
            {
                scond = summ;
                break;
            }
        }
//        if (scond.detail != null)
        Document doc = DocumentHelper.parseText(scond.detail);
        OutputStream os = null;
        String res = "";
        Connection conn = null;
        try
        {
            conn = DBUtil.getConnection("p3");
            String sql = "select ";
            List rows = doc.selectNodes("//ROW");
            for (int i = 0; i < rows.size(); i++)
            {
                Element row = (Element) rows.get(i);
                if ("4".equals(row.element("STYLE").getText())
                    || row.element("DATA_TYPE").getText().equals("DATE"))
                {
                    String format = "yyyy-mm-dd hh24:mi:ss";
                    if (row.attribute("format") != null)
                    {
                        if (row.attribute("format").getStringValue() != null)
                        {
                            format = row.attribute("format").getStringValue();
                        }
                    }
                    sql += "to_char(" + row.element("OWNER").getText() + "."
                        + row.element("TID").getText() + "."
                        + row.element("CID").getText() + ",'" + format + "'),";
                }
                else
                    sql += row.element("OWNER").getText() + "."
                        + row.element("TID").getText() + "."
                        + row.element("CID").getText() + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            String[] tablist = tabstr.split(",");
            tabstr = "";
            boolean alltab = true;
            for (int p = 0; p < tablist.length; p++)
            {
                if (Pub.empty(tablist[p]))
                    continue;
                if (sql.indexOf(tablist[p]) < 0)
                {
                    alltab = false;
                    continue;
                }
                if (tabstr.length() <1)
                    tabstr += tablist[p];
                else
                    tabstr += "," + tablist[p];
            }
            sql += " from " + tabstr + " where " + ycond.cond + " and " +
                xcond.cond;
            if (!this.empty(relationStr))
                relationStr = this.getRelationStringByTableList(rootdef, tabstr);
            if (!this.empty(filter))
                filter = this.getFilterStringByTableList(rootdef, tabstr);
            if (!this.empty(relationStr)) // && alltab)
                sql += " and " + relationStr;

            if (!this.empty(filter))
                sql += " and " + filter;
            String sqlcount = "select count(1) from " + tabstr + " where " +
                ycond.cond + " and " + xcond.cond;
            if (!this.empty(relationStr))
                sqlcount += " and " + relationStr;
            if (!this.empty(filter))
                sqlcount += " and " + filter;
            if (count <= 0)
                count = Pub.toInt(DBUtil.getSignalValue(conn, sqlcount));
            Element root = doc.getRootElement();
            if (root.attribute("rnum") != null)
                rnum = Pub.toInt(root.attribute("rnum").getStringValue());
            if (rnum <= 0)
                rnum = 20;
            PageManager page = new PageManager();
            page.setCurrentPage(pnum);
            page.setPageRows(rnum);
            page.setCountRows(count);
            page.setCountPage( (int) Math.ceil( (count + 0.00001) /
                                               (rnum + 0.00001)));
            sql = DBUtil.getSqlPage(sql, page);
            QueryTable table = new QueryTable(conn, sql);
            table.setStyle(table.STYLE_SHOW_EXCEL);
            table.setAllcols(rows.size());
            table.setShowTail(false);
            table.addTailMemo("�� [" + pnum + "] ҳ��[" +
                              (table.getData() == null ? 0 :
                               table.getData().length) + "] ��",
                              "�� [" + page.getCountPage() + "] ҳ [" + count +
                              "] ����¼��ÿҳ ["
                              + rnum + "] ��");
            table.setShowTailMemoBorder(true);
            table.setTitle("��ϸ��Ϣ");
            table.addTitleMemo("��");
            for (int i = 0; i < rows.size(); i++)
            {
                Element row = (Element) rows.get(i);
                table.addField(i, row.attribute("name") == null ?
                               row.element("CNAME").getText() :
                               row.attribute("name").getStringValue()
                               ,
                               0, // ��������� 0,�ַ�1������
                               i + 1, // ��ʾλ�ã��� 1 ��ʼ
                               0, // �п�ȣ�0 Ϊ�Զ�����
                               null, // ͳ��ָ�� "sum:0",num:0,avg:2  ||  ָ��:����
                               0, // ��������ţ��� 1 ��ʼ����group by �е�˳��
                               null // ���ӵ�����λ��
                    );
                if (!Pub.empty(row.element("DIC_CODE").getText()))
                    table.setFieldDicCode(i, row.element("DIC_CODE").getText());
                else if (!Pub.empty(row.element("DIC_TABLE").getText()))
                    table.setFieldDicCode(i, row.element("DIC_TABLE").getText());
            }
            table.setCellLink(false);

            res = table.listdetail();
            request.getSession().setAttribute("key_free_stat_detail".
                                              toUpperCase(), res);
            os = response.getOutputStream();
            os.write(this.header1.getBytes());
            os.write("��ϸ��Ϣ".getBytes());
            os.write(this.header21.getBytes());
            os.write(request.getContextPath().getBytes());
            os.write(this.header22.getBytes());
            os.write(request.getContextPath().getBytes());
            os.write(this.header3.getBytes());
            os.write(res.getBytes());
            os.write(this.tail1.getBytes());
            os.write("key_free_stat_detail".toUpperCase().getBytes());
            os.write(this.tail2.getBytes());
            os.write("FreeTableDetail".getBytes());
            os.write(this.tail3.getBytes());
            os.write(this.getPageStr(page).getBytes());
            os.write(this.tail5.getBytes());
            os.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return mapping.findForward("error");
        }
        finally
        {
            if (conn != null)
                conn.close();
            conn = null;
        }
        return null;

    }

    private String getPageStr(PageManager page)
    {
        String res = "";
        if (page == null)
            return "";
        int p = page.getCurrentPage();
        int r = page.getPageRows();
        int count = page.getCountRows();
        int pages = page.getCountPage();
        res += "<script language=\"JavaScript\">\n"
            + "<!--\n"
            + "var p = " + p + ";\n"
            + "var r = " + r + ";\n"
            + "var count = " + count + ";\n"
            + "var pages = " + pages + ";\n"
            + "-->\n</script>\n"
            + "<div style=\"display:none;\"><form method=\"POST\" name=\"pageform\" id=\"pageform\">\n"
            + "<input type=\"hidden\" name=\"p\"/>\n"
            + "<input type=\"hidden\" name=\"r\"/>\n"
            + "<input type=\"hidden\" name=\"c\"/>\n</form></div>\n";
        if (pages > 1)
        {
            if (p == 1)
            {
                res += "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#F��  ҳ\" onclick=\"doFirstPage();\" disabled=\"true\">\n";
                res += "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#S��һҳ\" onclick=\"doPrevPage();\" disabled=\"true\">\n";
            }
            else
            {
                res += "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#F��  ҳ\" onclick=\"doFirstPage();\">\n";
                res += "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#S��һҳ\" onclick=\"doPrevPage();\">\n";
            }
            if (p == pages)
            {
                res += "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#N��һҳ\" onclick=\"doNextPage();\" disabled=\"true\">\n";
                res += "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#Lβ��ҳ\" onclick=\"doLastPage();\" disabled=\"true\">\n";
            }
            else
            {
                res += "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#N��һҳ\" onclick=\"doNextPage();\" >\n";
                res += "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#Lβ��ҳ\" onclick=\"doLastPage();\" >\n";
            }
        }
        return res;
    }

    public ActionForward doRunDynamic(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response)
        throws
        Exception
    {
        try
        {
            String docstr = Pub.val(request, "txtXML");
            String action = request.getParameter("action");
            String freeid = request.getParameter("freeid");
            Document dynamic = DocumentHelper.parseText(docstr);
            Document doc = (Document) request.getSession().getAttribute(
                "FREE_TABLE_DOC");
            if (doc == null ||
                action != null && action.equals("execute") && !Pub.empty(freeid))
            {
                doc = DocumentHelper.parseText(this.getFreeStat(freeid));
            }
            if (doc == null || dynamic == null)
                throw new Exception("null here!");
            this.setDynamicValue(doc, dynamic, request);
            String res = doQuery(doc, request);
            if (action != null && action.equals("preview"))
                writePreview(res, doc, request, response);
            else if (action != null && action.equals("execute"))
                writeExecute(res, doc, request, response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return mapping.findForward("error");
        }
        return null;
    }

    private void writePreview(String res, Document doc,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Exception
    {
        OutputStream os = null;
        request.getSession().setAttribute("key_free_stat_preview".
                                          toUpperCase(), res);
        os = response.getOutputStream();
        os.write(this.header1.getBytes());
        os.write(doc.getRootElement().attribute("title").getStringValue().
                 getBytes());
        os.write(this.header21.getBytes());
        os.write(request.getContextPath().getBytes());
        os.write(this.header22.getBytes());
        os.write(request.getContextPath().getBytes());
        os.write(this.header3.getBytes());
        os.write(res.getBytes());
        os.write(this.tail1.getBytes());
        os.write("key_free_stat_preview".toUpperCase().getBytes());
        os.write(this.tail2.getBytes());
        os.write("FreeTableDownload".getBytes());
        os.write(this.tail3.getBytes());
        os.write(this.tail5.getBytes());
        os.flush();
        os.close();
    }

    private void writeExecute(String res, Document doc,
                              HttpServletRequest request,
                              HttpServletResponse response)
        throws Exception
    {
        OutputStream os = null;
        String id = request.getParameter("freeid");
        if (id == null)
        {
            id = (String) request.getSession().getAttribute("freeid");
            if (id == null || id.equals(""))
            {
                id = doc.getRootElement().attribute("id") == null ? "" :
                    doc.getRootElement().attribute("id").getStringValue();
            }
        }
        request.getSession().setAttribute("key_free_stat_preview".
                                          toUpperCase(), res);
        os = response.getOutputStream();
        os.write(this.header1.getBytes());
        os.write(doc.getRootElement().attribute("title").getStringValue().
                 getBytes());
        os.write(this.header21.getBytes());
        os.write(request.getContextPath().getBytes());
        os.write(this.header22.getBytes());
        os.write(request.getContextPath().getBytes());
        os.write(this.header3.getBytes());
        os.write(res.getBytes());
        os.write(this.tail1.getBytes());
        os.write("key_free_stat_preview".toUpperCase().getBytes());
        os.write(this.tail2.getBytes());
        os.write("FreeTableDownload".getBytes());
        os.write(this.tail3.getBytes());
        os.write(this.tail41.getBytes());
        os.write(id.getBytes());
        os.write(this.tail42.getBytes());
        os.write(this.tail5.getBytes());
        os.flush();
        os.close();
    }

    public ActionForward doPreview(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws
        Exception
    {
        try
        {
            request.getSession().setAttribute("action", "preview");
            request.getSession().setAttribute("freeid", "");
            String docstr = request.getParameter("txtXML");
            //docstr = new String(docstr.getBytes("ISO8859_1"), "GB2312");
            Document doc = DocumentHelper.parseText(docstr);
            if (hasDynamicCondition(doc, null, request))
                return mapping.findForward("condition");
            else if (hasFixedCondition(doc, null, request))
                setFixedValue(doc, request);
            String res = null;
            res = doQuery(doc, request);
            writePreview(res, doc, request, response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return mapping.findForward("error");
        }
        return null;
    }

    public Document WriteXmlMessage(Document doc, String strMessage,
                                    String strTagName)
    {
        try
        {
            Element root = null;
            if (doc == null)
            {
                doc = DocumentFactory.getInstance().createDocument();

                root = doc.addElement("RESPONSE");
                root.addAttribute("errorcode", "0");
                root.addAttribute("code", "0");
                Element resultRoot = root.addElement("RESULT");

                resultRoot.addAttribute("currentpagenum", String.valueOf(1));
                resultRoot.addAttribute("recordsperpage", String.valueOf(12));

                resultRoot.addAttribute("totalpage", String.valueOf(0));
                resultRoot.addAttribute("countrows", String.valueOf(0));
            }
            else
            {
                root = doc.getRootElement();
            }
            if (root != null && strMessage != null && strTagName != null)
            {
                root.addElement(strTagName).addText(strMessage);
            }
        }
        catch (Exception e)
        {
            System.out.println("ZqzCzrkZqzEventAction.WriteXmlMessage(Document doc,String strMessage,String strTagName):" +
                               e.toString());
        }
        return doc;
    }

    public static boolean empty(String s)
    {
        if (s == null || s.trim().equals(""))
            return true;
        else
            return false;
    }

    private static String header1 =
        "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" "
        + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" "
        + "xmlns:x=\"urn:schemas-microsoft-com:office:excel\" "
        + "xmlns=\"http://www.w3.org/TR/REC-html40\">\n"
        + "<head>\n"
        + "<title>";
    private static String header21 = "</title>\n"
        +
        "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=GBK\">\n"
        + "<meta name=ProgId content=Excel.Sheet>\n"
        + "<!--[if !mso]>\n"
        + "<style>\n"
        + "v\\:* {behavior:url(#default#VML);}\n"
        + "o\\:* {behavior:url(#default#VML);}\n"
        + "x\\:* {behavior:url(#default#VML);}\n"
        + ".shape {behavior:url(#default#VML);}\n"
        + "</style>\n"
        + "<![endif]--><!--[if gte mso 9]><xml>\n"
        + " <o:DocumentProperties>\n"
        + "  <o:Author>leo oss@tom.com</o:Author>\n"
        + "  <o:LastAuthor>leo</o:LastAuthor>\n"
        + "  <o:Created>2006-08-29T08:22:54Z</o:Created>\n"
        + "  <o:LastSaved>2006-08-29T08:25:10Z</o:LastSaved>\n"
        + "  <o:Company>neusoft.com</o:Company>\n"
        + "  <o:Version>11.5606</o:Version>\n"
        + " </o:DocumentProperties>\n"
        + "</xml><![endif]-->\n"
        + "<style>\n"
        + "<!--\n"
        + "a {\n"
        + "font-size:          9pt;\n"
        + "color:              navy;\n"
        + "text-decoration:    none;\n"
        + "}\n"
        + "a:hover {\n"
        + "font-size:          9pt;\n"
        + "color:              darkorange;\n"
        + "text-decoration:    underline;\n"
        + "}\n"
        + ".Button {\n"
        + "behavior:         url(\"/" + Pub.PREFIX +
        "/js/commonjs/xButton.htc\");\n"
        + "}\n"
        + "@media print {\n"
        + "   .noprint {display:none;}\n"
        + "-->\n"
        + "</style>\n"
        + "<SCRIPT language=\"javascript\" src=\"";
    private static String header22 =
        "/jsp/framework/freetable/FreeTable.js\"></SCRIPT>\n"
        + "<Script language=\"JavaScript\">\n"
        + "<!--\n"
        + "function doDownload(key,file)\n"
        + "{\n"
        + "    document.all.download.src = \"";
    private static String header3 =
        "/jsp/framework/freetable/download.jsp?key=\"+key+\"&file=\"+file;\n"
        + "}\n"
        + "-->\n"
        + "</Script>\n"
        + "</head>\n"
        +
        "<body onLoad=\"doInitFreeTable();\" onResize=\"doRefreshLine();\">\n";
    private static String tail1 = "<p><br>\n"
        + "<table align=\"center\" class=\"noprint\">\n"
        + "<tr><td>\n"
        + "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#P��  ӡ\" onclick=\"window.print()\">\n"
        + "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#D��  ��\" onclick=\"doDownload('";
    private static String tail2 = "','";
    private static String tail3 = "');\">\n";
    private static String tail41 = "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#S��  ��\" onclick=\"doSaveFreeSataResult('";
    private static String tail42 = "');\">\n";
    private static String tail5 = "<INPUT type=\"button\" name=\"cmdSubmit\" class=\"Button\" value=\"#R��  ��\" onclick=\"history.back();\"></td></tr>\n"
        + "</table>\n"
        + "<iframe id=\"download\" name=\"download\" class=\"noprint\" style=\"display: none;\"></iframe>\n"
        + "</body>\n"
        + "</html>\n";

}

class Condition
    implements Cloneable, java.io.Serializable
{
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch (CloneNotSupportedException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public String detail;
    public int index;
    public int x, y;
    public String title[] = new String[1];
    public String colname;
    public String cond;
    public ArrayList summ;
}
