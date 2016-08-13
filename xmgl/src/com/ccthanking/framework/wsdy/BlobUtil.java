package com.ccthanking.framework.wsdy;

import java.sql.*;
import java.sql.Blob;
import java.lang.*;
import oracle.sql.*;
import oracle.jdbc.driver.OracleResultSet;
import com.ccthanking.framework.common.OPException;
import com.ccthanking.framework.common.DBUtil;

public class BlobUtil
{
    String m_query;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    BLOB blob = null;

    //增加多媒体信息(入口)
    /**
     * dbtype - 所连接数据库类型
     * tablename - 要进行操作的表名称
     *            exp. "pp_blob"
         * para[][] - 用于传递要插入的字段名称、数据类型和字段值;para[][0]-字段名称;para[][1]-字段类型;para[][2]-字段值
     *            注意:不包括多媒体字段
     *            exp. para[0][0]="fjbh";para[0][1]="string";para[0][2]="001";
     * blobname - 要进行操作的多媒体字段名称
                  exp. "multimedia"
     * cond[][] - 用于传递定位到多媒体字段所需查询条件的字段名称、数据类型和字段值;cond[][0]-字段名称;cond[][1]-字段类型;cond[][2]-字段值
     * filebuffer - 预插入多媒体字段的内容
     */
    public String insert(String dbtype, String tablename, String[][] para,
                         String blobname, String[][] cond, byte[] filebuffer)
        throws Exception
    {

        Connection conn = DBUtil.getConnection();

        String m_returnValue = "";
        String m_strsql = "";
        String m_strsql2 = "";
        int m_int = 0;
        String[][] m_para;
        String[][] m_cond;
        try{
            if (dbtype.trim().equalsIgnoreCase("sqlserver"))
            { //数据库为sqlserver2000
                m_int = para.length;
                //构造SQL语句
                m_strsql = "INSERT INTO " + tablename + " (";
                for (int i = 0; i < m_int; i++)
                    m_strsql = m_strsql + para[i][0] + ",";
                m_strsql += blobname + ") VALUES (";
                for (int i = 0; i < m_int; i++)
                    m_strsql = m_strsql + "?,";
                m_strsql += "?)";
                //构造参数数组
                m_para = new String[m_int][2];
                for (int i = 0; i < m_int; i++)
                {
                    m_para[i][0] = para[i][1];
                    m_para[i][1] = para[i][2];
                }

                //调用sqlserver2000专用方法
                m_returnValue = insertForSqlServer(conn, m_strsql, m_para,
                    filebuffer);
            }
            else
            { //数据库为oracle817
                //构造SQL语句m_strsql
                m_int = para.length;
                m_strsql = "INSERT INTO " + tablename + " (";
                for (int i = 0; i < m_int; i++)
                    m_strsql = m_strsql + para[i][0] + ",";
                m_strsql += blobname + ") VALUES (";
                for (int i = 0; i < m_int; i++)
                    m_strsql = m_strsql + "?,";
                m_strsql += "?)";
                //构造参数数组m_para
                m_para = new String[m_int][2];
                for (int i = 0; i < m_int; i++)
                {
                    m_para[i][0] = para[i][1];
                    m_para[i][1] = para[i][2];
                }
                //构造SQL语句m_strsql2
                m_int = cond.length;
                m_strsql2 = "SELECT " + blobname + " FROM " + tablename +
                    " WHERE ";
                for (int i = 0; i < m_int; i++)
                    if (i != m_int - 1)
                        m_strsql2 = m_strsql2 + cond[i][0] + "=? AND ";
                    else
                        m_strsql2 = m_strsql2 + cond[i][0] + "=? ";
                        //构造参数数组m_cond
                m_cond = new String[m_int][2];
                for (int i = 0; i < m_int; i++)
                {
                    m_cond[i][0] = cond[i][1];
                    m_cond[i][1] = cond[i][2];
                }

                m_returnValue = insertForOracle(conn, m_strsql, m_para,
                                                m_strsql2,
                                                m_cond, filebuffer);
            }
        }catch(Exception e){

        }finally{
            if(conn!=null)
                conn.close();
        }
        return m_returnValue;
    }

    //增加多媒体信息(sqlserver2000专用)
    /**
     * conn - 数据库连接
     * sql_1 - 第一条sql语句
         *         exp. "insert into testblob (sid,author,multimedia) values(?,?,?)"
     * para[][] - 用于传递sql_1中要插入的字段值和数据类型;para[][0]-字段类型;para[][1]-字段值
     * filebuffer - 预插入多媒体字段的内容
     */
    private String insertForSqlServer(Connection conn, String sql_1,
                                      String[][] para, byte[] filebuffer)
        throws OPException
    {
        String result = "";
        try {
            //**********对含有blob字段的表进行插入-begin**********/
            //onn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql_1);
            for (int i = 0; i < para.length; i++) {
                if (para[i][0].equalsIgnoreCase("string"))
                    pstmt.setString(i + 1, para[i][1]);
                else if (para[i][0].equalsIgnoreCase("int"))
                    pstmt.setInt(i + 1, Integer.parseInt(para[i][1]));
            }
            pstmt.setBytes(para.length + 1, filebuffer);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt.close();
            //conn.commit();
            //***********对含有blob字段的表进行插入-end***********/
            result = "0";
        }
        catch (SQLException e) {
            result = "-1";
            throw new OPException(e.getMessage());
        }
        catch (Exception e) {
            result = "-1";
            throw new OPException(e.getMessage());
            //e.printStackTrace();
        }
        return (result);
    }

    //增加多媒体信息(oracle817专用)
    /**
     * conn - 数据库连接
     * sql_1 - 第一条sql语句，用于插入除BLOB外的其它字段值
     * para[][] - 用于传递sql_1中要插入的字段值和数据类型;para[][0]-字段类型;para[][1]-字段值
         *            exp. "insert into testblob (sid,author,multimedia) values(?,?,?)"
     * sql_2 - 第二条sql语句，用于插入BLOB的字段值
         *            exp. "select multimedia from testblob where sid=? for update";
     * cond[][] - 用于传递sql_2中查询条件的字段值和数据类型;cond[][0]-字段类型;cond[][1]-字段值
     * filebuffer - 预插入BLOB字段的内容
     */
    private String insertForOracle(Connection conn, String sql_1,
                                   String[][] para, String sql_2,
                                   String[][] cond, byte[] filebuffer)
        throws OPException
    {

        String result = "";
        try {
            //**********对含有blob字段的表进行插入-begin**********/
            blob = BLOB.empty_lob();
            //conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql_1);
            for (int i = 0; i < para.length; i++) {
                if (para[i][0].equalsIgnoreCase("string"))
                    pstmt.setString(i + 1, para[i][1]);
                else if (para[i][0].equalsIgnoreCase("int"))
                    pstmt.setInt(i + 1, Integer.parseInt(para[i][1]));
                else if (para[i][0].equalsIgnoreCase("timestamp"))
                    pstmt.setTimestamp(i + 1, Timestamp.valueOf(para[i][1]));
                else if (para[i][0].equalsIgnoreCase("date"))
                    pstmt.setDate(i + 1, Date.valueOf(para[i][1]));
            }
            pstmt.setBlob(para.length + 1, blob);
            pstmt.executeUpdate();
            pstmt.close();

            pstmt = conn.prepareStatement(sql_2);
            for (int i = 0; i < cond.length; i++) {
                if (cond[i][0].equalsIgnoreCase("string"))
                    pstmt.setString(i + 1, cond[i][1]);
                else if (cond[i][0].equalsIgnoreCase("int"))
                    pstmt.setInt(i + 1, Integer.parseInt(cond[i][1]));
                else if (cond[i][0].equalsIgnoreCase("timestamp"))
                    pstmt.setTimestamp(i + 1, Timestamp.valueOf(cond[i][1]));
                else if (cond[i][0].equalsIgnoreCase("date"))
                    pstmt.setDate(i + 1, Date.valueOf(cond[i][1]));
            }
            //rset = (OracleResultSet)pstmt.executeQuery();
            rset = pstmt.executeQuery();
            if (rset.next()) {
                //blob =((OracleResultSet)rset).getBLOB(1);
                blob = (BLOB) rset.getBlob(1);
                blob.putBytes(1, filebuffer);
            }

            pstmt.close();
            //conn.commit();
            //***********对含有blob字段的表进行插入-end***********/
            result = "0";
        }
        catch (SQLException e) {
            result = "-1";
            throw new OPException(e.getMessage());
        }
        catch (Exception e) {
            result = "-1";
            throw new OPException(e.getMessage());
            //e.printStackTrace();
        }
        return (result);
    }

    //提取多媒体信息(入口)
    /**
     * dbtype - 所连接数据库类型
     * conn - 数据库连接
     * tablename - 要进行操作的表名称
     *            exp. "pp_blob"
     * blobname - 要进行操作的多媒体字段名称
                  exp. "multimedia"
     * cond[][] - 用于传递定位到多媒体字段所需查询条件的字段名称、数据类型和字段值;cond[][0]-字段名称;cond[][1]-字段类型;cond[][2]-字段值
     */
    public byte[] get(String dbtype, String tablename, String blobname,
                      String[][] cond)
        throws Exception
    {
        Connection conn = DBUtil.getConnection();
        String m_strsql = "";
        int m_int = 0;
        //构造SQL语句
        m_int = cond.length;
        byte[] byte_temp = null;
        try{
                m_strsql = "SELECT " + blobname + " FROM " + tablename + " WHERE ";
                for (int i = 0; i < m_int; i++) {
                    if (i != m_int - 1) {
                        if (cond[i][1].equalsIgnoreCase("string"))
                            m_strsql = m_strsql + cond[i][0] + "='" + cond[i][2] +
                                "' AND ";
                        else if (cond[i][1].equalsIgnoreCase("int"))
                            m_strsql = m_strsql + cond[i][0] + "=" + cond[i][2] +
                                " AND ";
                    }
                    else {
                        if (cond[i][1].equalsIgnoreCase("string"))
                            m_strsql = m_strsql + cond[i][0] + "='" + cond[i][2] + "'";
                        else if (cond[i][1].equalsIgnoreCase("int"))
                            m_strsql = m_strsql + cond[i][0] + "=" + cond[i][2];
                    }
                }
                if (dbtype.trim().equalsIgnoreCase("longrow")) {
                    byte_temp = getLongRaw(conn, m_strsql);
                }else if (dbtype.trim().equalsIgnoreCase("sqlserver")){
                    byte_temp = getForSqlServer(conn, m_strsql);
                }else{
                    byte_temp = getForOracle(conn, m_strsql);
                }
        }catch(Exception e){

        }finally{
            if(conn!=null)
                conn.close();
        }
        return byte_temp;
    }

    //提取多媒体信息(sqlserver2000专用)
    /**
     * conn - 数据库连接
     * strSql - 用于查询多媒体信息的Sql语句
     *         exp. "select multimedia from testblob where sid='test.txt'"
     */
    private byte[] getForSqlServer(Connection conn, String strSql)
        throws OPException
    {
        Statement stmt = null;
        ResultSetMetaData rset_meta = null;
        try {
            //conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rset = (ResultSet) stmt.executeQuery(strSql);
            rset_meta = (ResultSetMetaData) rset.getMetaData();
            if (rset.next()) {
                byte[] buffer = rset.getBytes(rset_meta.getColumnName(1));
                stmt.close();
                //conn.commit();
                return buffer;
            }
            return null;
        }
        catch (Exception e) {
            throw new OPException(e.getMessage());
            //e.printStackTrace();
        }
    }

    //提取多媒体信息(oracle817专用)
    /**
     * conn - 数据库连接
     * strSql - 用于查询多媒体信息的Sql语句
              exp. "select multimedia from testblob where sid='test.txt'"
     */
    private byte[] getForOracle(Connection conn, String strSql)
        throws OPException
    {
        Statement stmt = null;
        ResultSetMetaData rset_meta = null;
        int length = 0;
        try {
            blob = BLOB.empty_lob();
            //conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rset = (ResultSet) stmt.executeQuery(strSql);
            rset_meta = (ResultSetMetaData) rset.getMetaData();
            if (rset.next()) {
                Blob objBlob = rset.getBlob(rset_meta.getColumnName(1));
                //songxb@2008-02-29
                if(objBlob==null){
                    return new byte[0];
                }
                length = (int) objBlob.length();
                byte[] buffer = objBlob.getBytes(1, length);
                stmt.close();

                return buffer;
            }
            return null;
        }
        catch (Exception e) {
            throw new OPException(e.getMessage());
        }finally{
            try {
                if (stmt != null) {
                    stmt.close();
                }
               // if (conn != null) {
               //     conn.close();
               // }
            }
            catch (SQLException ex) {
            }

        }
    }

    //更新多媒体信息(在线编辑专用-入口)
    /**
     * dbtype - 所连接数据库类型
     * conn - 数据库连接
     * tablename - 要进行操作的表名称
     *            exp. "pp_blob"
     * blobname - 要进行操作的多媒体字段名称
                  exp. "multimedia"
     * cond[][] - 用于传递定位到多媒体字段所需查询条件的字段名称、数据类型和字段值;cond[][0]-字段名称;cond[][1]-字段类型;cond[][2]-字段值
     */
    public String updateEd(String dbtype, String tablename, String blobname,
                           String[][] cond, byte[] filebuffer)
        throws Exception
    {
        Connection conn = DBUtil.getConnection();
        String m_returnValue = "";
        String m_strsql = "";
        String[][] param;
        int m_int = 0;
        try{
            if (dbtype.trim().equalsIgnoreCase("sqlserver"))
            { //数据库为sqlserver2000
                //构造SQL语句
                m_int = cond.length;
                m_strsql = "UPDATE " + tablename + " SET " + blobname +
                    "=? WHERE ";
                for (int i = 0; i < m_int; i++)
                {
                    if (i != m_int - 1)
                    {
                        m_strsql = m_strsql + cond[i][0] + "=? AND ";
                    }
                    else
                    {
                        m_strsql = m_strsql + cond[i][0] + "=?";
                    }
                }
                param = new String[m_int][2];
                for (int i = 0; i < m_int; i++)
                {
                    param[i][0] = cond[i][1];
                    param[i][1] = cond[i][2];
                }
                m_returnValue = updateEdForSqlServer(conn, m_strsql, param,
                    filebuffer);
            }
            else
            {
                //构造SQL语句
                m_int = cond.length;
                m_strsql = "SELECT " + blobname + " FROM " + tablename +
                    " WHERE ";
                for (int i = 0; i < m_int; i++)
                {
                    if (i != m_int - 1)
                    {
                        m_strsql = m_strsql + cond[i][0] + "=? AND ";
                    }
                    else
                    {
                        m_strsql = m_strsql + cond[i][0] + "=? ";
                    }
                }
                m_strsql += "FOR UPDATE";
                param = new String[m_int][2];
                for (int i = 0; i < m_int; i++)
                {
                    param[i][0] = cond[i][1];
                    param[i][1] = cond[i][2];
                }
                m_returnValue = updateEdForOracle(conn, m_strsql, param,
                                                  filebuffer);
            }
        }catch(Exception e){

        }finally{
            if(conn!=null){
                conn.close();
            }
        }
        return m_returnValue;
    }
    /**
     * 更新blob字段
     * @param sql
     * @param filebuffer
     */
    public void updateBlob(Connection conn,String sql, byte[] filebuffer)
        throws SQLException
    {
        PreparedStatement pstmt = null;
        try {
            BLOB blob = BLOB.empty_lob();
            pstmt = conn.prepareStatement(sql);
            ResultSet rset = pstmt.executeQuery();
            if (rset.next()) {
                blob = (BLOB) rset.getBlob(1);
                blob.putBytes(1, filebuffer);
            }
        }
        catch (SQLException ex) {
           // conn.rollback();
            ex.printStackTrace();
        }finally{
                if(rset!=null){
                    rset.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }

        }

    }

    //更新多媒体信息(在线编辑专用-sqlserver2000专用)
    /**
     * conn - 数据库连接
     * sql_2 - sql语句，用于更新多媒体的字段值
     *            exp. "UPDATE pp_blob SET multimedia=? WHERE fjbh=?";
     * cond[][] - 用于传递sql_2中查询条件的字段值和数据类型;cond[][0]-字段类型;cond[][1]-字段值
     * filebuffer - 预插入多媒体字段的内容
     */
    private String updateEdForSqlServer(Connection conn, String sql_2,
                                        String[][] cond, byte[] filebuffer)
        throws OPException
    {

        String result = "";
        try {
            //**********对含有blob字段的表进行插入-begin**********/
            //conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql_2);
            pstmt.setBytes(1, filebuffer);
            for (int i = 0; i < cond.length; i++) {
                if (cond[i][0].equalsIgnoreCase("string"))
                    pstmt.setString(i + 2, cond[i][1]);
                else if (cond[i][0].equalsIgnoreCase("int"))
                    pstmt.setInt(i + 2, Integer.parseInt(cond[i][1]));
            }
            for (int i = 0; i < cond.length; i++) {
                System.out.println("cond[" + i + "][1]:" + cond[i][1] + "@");
            }
            pstmt.executeUpdate();
            pstmt.close();
            //conn.commit();
            //***********对含有blob字段的表进行插入-end***********/
            result = "0";
        }
        catch (SQLException e) {
            result = "-1";
            e.printStackTrace();
            throw new OPException(e.getMessage());
        }
        catch (Exception e) {
            result = "-1";
            e.printStackTrace();
            throw new OPException(e.getMessage());
        }
        return (result);
    }

    //更新多媒体信息(在线编辑专用-oracle817专用)
    /**
     * conn - 数据库连接
     * sql_2 - 第二条sql语句，用于更新BLOB的字段值
         *            exp. "select multimedia from testblob where sid=? for update";
     * cond[][] - 用于传递sql_2中查询条件的字段值和数据类型;cond[][0]-字段类型;cond[][1]-字段值
     * filebuffer - 预插入BLOB字段的内容
     */
    private String updateEdForOracle(Connection conn, String sql_2,
                                     String[][] cond, byte[] filebuffer)
        throws OPException
    {

        String result = "";
        try {
            //**********对含有blob字段的表进行插入-begin**********/
            blob = BLOB.empty_lob();
            //conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql_2);
            for (int i = 0; i < cond.length; i++) {
                if (cond[i][0].equalsIgnoreCase("string"))
                    pstmt.setString(i + 1, cond[i][1]);
                else if (cond[i][0].equalsIgnoreCase("int"))
                    pstmt.setInt(i + 1, Integer.parseInt(cond[i][1]));
            }
            rset = pstmt.executeQuery();
            if (rset.next()) {
                blob = (BLOB) rset.getBlob(1);
                blob.putBytes(1, filebuffer);
            }
            pstmt.close();
            //conn.commit();
            //***********对含有blob字段的表进行插入-end***********/
            result = "0";
        }
        catch (SQLException e) {
            result = "-1";
            throw new OPException(e.getMessage());
        }
        catch (Exception e) {
            result = "-1";
            throw new OPException(e.getMessage());
            //e.printStackTrace();
        }
        return (result);
    }

    //更新多媒体信息(入口)[未测试，系统中尚未用到此方法]
    /**
     * dbtype - 所连接数据库类型
     * conn - 数据库连接
     * tablename - 要进行操作的表名称
     *            exp. "pp_blob"
         * para[][] - 用于传递要更新的字段名称、数据类型和字段值;para[][0]-字段名称;para[][1]-字段类型;para[][2]-字段值
     *            注意:多媒体字段放在最后
     *            exp. para[0][0]="fjbh";para[0][1]="string";para[0][2]="001";
     * cond[][] - 用于传递定位到多媒体字段所需查询条件的字段名称、数据类型和字段值;cond[][0]-字段名称;cond[][1]-字段类型;cond[][2]-字段值
     * filebuffer - 预插入多媒体字段的内容
     */
    public String update(String dbtype, String tablename, String[][] para,
                         String[][] cond, byte[] filebuffer)
        throws Exception
    {

        Connection conn =  DBUtil.getConnection();
        String m_returnValue = "";
        String m_strsql = "";
        String m_strsql2 = "";
        String[][] param;
        String[][] conditon;
        int m_int = 0;
        try{
            if (dbtype.trim().equalsIgnoreCase("sqlserver"))
            { //数据库为sqlserver2000
                //构造SQL语句
                m_int = para.length;
                m_strsql = "UPDATE " + tablename + " SET ";
                for (int i = 0; i < m_int; i++)
                {
                    if (i != m_int - 1)
                    {
                        m_strsql = m_strsql + para[i][0] + "=?,";
                    }
                    else
                    {
                        m_strsql = m_strsql + para[i][0] + "=?";
                    }
                }
                m_int = cond.length;
                m_strsql = m_strsql + " WHERE ";
                for (int i = 0; i < m_int; i++)
                {
                    if (i != m_int - 1)
                    {
                        m_strsql = m_strsql + cond[i][0] + "=? AND ";
                    }
                    else
                    {
                        m_strsql = m_strsql + cond[i][0] + "=?";
                    }
                }
                m_int = para.length + cond.length;
                param = new String[m_int][2];
                for (int i = 0; i < para.length; i++)
                {
                    param[i][0] = para[i][1];
                    param[i][1] = para[i][2];
                }
                for (int i = para.length; i < cond.length; i++)
                {
                    param[i][0] = cond[i][1];
                    param[i][1] = cond[i][2];
                }
                m_returnValue = updateForSqlServer(conn, m_strsql, param,
                    filebuffer);
            }
            else
            { //数据库为oracle817
                //构造SQL语句m_strsql
                m_int = para.length;
                m_strsql = "UPDATE " + tablename + " SET ";
                for (int i = 0; i < m_int - 1; i++)
                {
                    if (i != m_int - 2)
                    {
                        m_strsql = m_strsql + cond[i][0] + "=?,";
                    }
                    else
                    {
                        m_strsql = m_strsql + cond[i][0] + "=?";
                    }
                }
                m_int = cond.length;
                m_strsql = m_strsql + " WHERE ";
                for (int i = 0; i < m_int; i++)
                {
                    if (i != m_int - 1)
                    {
                        m_strsql = m_strsql + cond[i][0] + "=? AND ";
                    }
                    else
                    {
                        m_strsql = m_strsql + cond[i][0] + "=?";
                    }
                }
                param = new String[para.length - 1][2];
                for (int i = 0; i < para.length - 1; i++)
                {
                    param[i][0] = para[i][1];
                    param[i][1] = para[i][2];
                }
                //构造SQL语句m_strsql2
                m_int = cond.length;
                m_strsql2 = "SELECT " + para[para.length - 1][0] + " FROM " +
                    tablename + " WHERE ";
                for (int i = 0; i < m_int; i++)
                {
                    if (i != m_int - 1)
                    {
                        m_strsql2 = m_strsql2 + cond[i][0] + "=? AND ";
                    }
                    else
                    {
                        m_strsql2 = m_strsql2 + cond[i][0] + "=? ";
                    }
                }
                conditon = new String[cond.length][2];
                for (int i = 0; i < cond.length; i++)
                {
                    conditon[i][0] = cond[i][1];
                    conditon[i][1] = cond[i][2];
                }
                m_returnValue = updateForOracle(conn, m_strsql, param,
                                                m_strsql2,
                                                conditon, filebuffer);
            }
        }catch(Exception e){

        }finally{
            if(conn!=null)
                conn.close();
        }
        return m_returnValue;
    }

    //更新多媒体信息(sqlserver2000专用)
    /**
     * conn - 数据库连接
     * sql_2 - sql语句，用于更新多媒体的字段值
         *            exp. "UPDATE testblob SET sid=?,author=?,multimedia=? WHERE sid=?";
     * cond[][] - 用于传递sql_2中查询条件的字段值和数据类型;cond[][0]-字段类型;cond[][1]-字段值
     * filebuffer - 预插入多媒体字段的内容
     */
    private String updateForSqlServer(Connection conn, String sql_2,
                                      String[][] cond, byte[] filebuffer)
        throws OPException
    {

        String result = "";
        try {
            //**********对含有blob字段的表进行插入-begin**********/
            //conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql_2);
            for (int i = 0; i < cond.length; i++) {
                if (cond[i][0].equalsIgnoreCase("string"))
                    pstmt.setString(i + 1, cond[i][1]);
                else if (cond[i][0].equalsIgnoreCase("int"))
                    pstmt.setInt(i + 1, Integer.parseInt(cond[i][1]));
                else if (cond[i][0].equalsIgnoreCase("multimedia"))
                    pstmt.setBytes(i + 1, filebuffer);
            }
            for (int i = 0; i < cond.length; i++) {
                System.out.println("cond[" + i + "][1]:" + cond[i][1] + "@");
            }
            pstmt.executeUpdate();
            pstmt.close();
            //***********对含有blob字段的表进行插入-end***********/
            result = "0";
        }
        catch (SQLException e) {
            result = "-1";
            e.printStackTrace();
            throw new OPException(e.getMessage());
        }
        catch (Exception e) {
            result = "-1";
            e.printStackTrace();
            throw new OPException(e.getMessage());
        }
        return (result);
    }

    //更新多媒体信息(oracle817专用)
    /**
     * conn - 数据库连接
     * sql_1 - 第一条sql语句，用于更新除BLOB外的其它字段值
     * para[][] - 用于传递sql_1中要更新的字段值和数据类型;para[][0]-字段类型;para[][1]-字段值
     *            exp. "update testblob set sid=?,author=? where sid=?";
     * sql_2 - 第二条sql语句，用于更新BLOB的字段值
         *            exp. "select multimedia from testblob where sid=? for update";
     * cond[][] - 用于传递sql_2中查询条件的字段值和数据类型;cond[][0]-字段类型;cond[][1]-字段值
     * filebuffer - 预插入BLOB字段的内容
     */
    public String updateForOracle(Connection conn, String sql_1,
                                  String[][] para, String sql_2,
                                  String[][] cond, byte[] filebuffer)
        throws OPException
    {

        String result = "";
        try {
            //**********对含有blob字段的表进行插入-begin**********/
            //conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql_2);
            for (int i = 0; i < cond.length; i++) {
                if (cond[i][0].equalsIgnoreCase("string"))
                    pstmt.setString(i + 1, cond[i][1]);
                else if (cond[i][0].equalsIgnoreCase("int"))
                    pstmt.setInt(i + 1, Integer.parseInt(cond[i][1]));
                else if (cond[i][0].equalsIgnoreCase("multimedia"))
                    pstmt.setBytes(i + 1, filebuffer);
            }
            pstmt.executeUpdate();
            pstmt.close();

            pstmt = conn.prepareStatement(sql_1);
            for (int i = 0; i < para.length; i++) {
                if (para[i][0].equalsIgnoreCase("string"))
                    pstmt.setString(i + 1, para[i][1]);
                else if (para[i][0].equalsIgnoreCase("int"))
                    pstmt.setInt(i + 1, Integer.parseInt(para[i][1]));
            }
            pstmt.executeUpdate();
            pstmt.close();

            //conn.commit();
            //***********对含有blob字段的表进行插入-end***********/
            result = "0";
        }
        catch (SQLException e) {
            result = "-1";
            throw new OPException(e.getMessage());
        }
        catch (Exception e) {
            result = "-1";
            throw new OPException(e.getMessage());
            //e.printStackTrace();
        }finally{

        }
        return (result);
    }

    //提取多媒体信息(long raw类型)
    /**
     * conn - 数据库连接
     * strSql - 用于查询多媒体信息的Sql语句
               exp. "select multimedia from testblob where sid='test.txt'"
     */
    public byte[] getLongRaw(Connection conn, String strSql)
        throws OPException
    {
        Statement stmt = null;
        ResultSetMetaData rset_meta = null;
        try {
            RAW raw = null;
            //conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rset = (ResultSet) stmt.executeQuery(strSql);
            rset_meta = (ResultSetMetaData) rset.getMetaData();
            if (rset.next()) {
                byte[] buffer = rset.getBytes(rset_meta.getColumnName(1)); //raw.getBytes();
                stmt.close();

                //conn.commit();
                return buffer;
            }
            return null;
        }
        catch (Exception e) {
            throw new OPException(e.getMessage());
        }
    }

    //提取多媒体信息(入口)
    /**
     */
    public byte[] get(String dbtype, String sql)
        throws Exception
    {
        Connection conn = null;
        byte[] byte_temp = null;
        try{
            conn = DBUtil.getConnection();
            if (dbtype.trim().equalsIgnoreCase("longrow"))
            {
                byte_temp = getLongRaw(conn, sql);
            }else if (dbtype.trim().equalsIgnoreCase("sqlserver")){

                    byte_temp = getForSqlServer(conn, sql);
            }else{
                    byte_temp = getForOracle(conn, sql);
            }
        }catch(Exception e){

        }finally{
            if(conn != null)
                conn.close();
        }
        return byte_temp;
    }



}