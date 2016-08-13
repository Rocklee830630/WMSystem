package com.ccthanking.framework.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccthanking.framework.common.dataset.DataItemImpl;
import com.ccthanking.framework.common.dataset.DataObjImpl;
import com.ccthanking.framework.common.dataset.QuerySetImpl;
import com.ccthanking.framework.common.datasource.DBConnectionManager;
import com.ccthanking.framework.util.Pub;

/**
 * 公用函数类
 * @author wangzh
 */
public class DBUtil
{
    private static org.apache.log4j.Logger logger = org.apache.log4j.LogManager.
        getLogger("DBUtil");

    public static DBConnectionManager connMgr = DBConnectionManager
        .getInstance();

    public static BaseResultSet query(Connection conn, String strSql,
                                      PageManager page)
    {
        if (conn == null || Pub.empty(strSql))
            return null;
        PreparedStatement psCount = null;
        ResultSet rsCount = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            if (page == null)
            {
                page = new PageManager();
                page.setPageRows(100);
            }
            int cp = page.getCurrentPage();
            int pr = page.getPageRows();
            if (pr <= 0)
                pr = 100;
            int countP = page.getCountPage();
            int iCountRows = page.getCountRows();
            if (!Pub.empty(page.getFilter()))
                strSql += " where " + page.getFilter();
            logger.debug(strSql);
			String originalSQL = strSql;
            if (countP <= 0)
            {
                String strSqlCount = "select count(*) from (" + strSql + ")";
                psCount = conn.prepareStatement(strSqlCount);
                rsCount = psCount.executeQuery();
                if (rsCount.next())
                {
                    iCountRows = rsCount.getInt(1);
                }
                if (iCountRows > 0)
                {
                    countP = iCountRows / pr;
                    int mod = iCountRows % pr;
                    if (mod > 0)
                        countP++;
                    if (cp > countP)
                        cp = countP;
                    if (cp < 1)
                        cp = 1;
                }
                rsCount.close();
                rsCount = null;
                psCount.close();
                psCount = null;
            }
            page.setCurrentPage(cp);
            page.setCountPage(countP);
            page.setCountRows(iCountRows);
            page.setPageRows(pr);

            strSql = DBUtil.getSqlPage(strSql, page);
            logger.debug(strSql);
            ps = conn.prepareStatement(strSql, ResultSet.TYPE_FORWARD_ONLY,
                                       ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
           
            BaseResultSet bset = new BaseResultSet(rs, ps);
			bset.setOriginalSQL(originalSQL);
            bset.setPage(page);
            return bset;
        }
        catch (Exception e)
        {
    	    System.out.println(strSql);
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (rsCount != null)
                    rsCount.close();
                if (psCount != null)
                    psCount.close();
            }
            catch (Exception e)
            {
            }
            ;
        }
        return null;
    }

    // the first parameter is an OUTPUT parameter
    public static String callProcedure(Connection conn, String procName,
                                       Object[] para)
        throws Exception
    {
        CallableStatement proc = null;
        int len = 0;
        len = para == null ? 0:para.length;
        StringBuffer Statement = new StringBuffer("{ call " + procName + "(");
        for (int i = 0; i < len; i++)
        {
            if (i == len - 1)
            {
                Statement.append("?)}");
            }
            else
            {
                Statement.append("?,");
            }
        }
        try
        {
            proc = conn.prepareCall(Statement.toString());
            proc.registerOutParameter(1, Types.VARCHAR);
            for (int i = 0; i < len; i++)
            {
                if (para[i] == null)
                    proc.setNull(i + 1, Types.VARCHAR);
                else
                    proc.setObject(i + 1, para[i]);
            }
            proc.execute();
            String res = proc.getString(1);
            proc.close();
            proc = null;
            return res;
        }
        catch (SQLException ex)
        {
            if (proc != null)
                proc.close();
            throw ex;
        }
    }

    //types 1 : in ，  > 1 : out
    public static Collection callProcedure(Connection conn, String procName,
                                           int[] types, Object[] para)
        throws Exception
    {
        CallableStatement proc = null;
        int len = 0;
        len = para == null ? 0 : para.length;
        StringBuffer Statement = new StringBuffer("{ call " + procName + "(");
        for (int i = 0; i < len; i++)
        {
            if (i == len - 1)
            {
                Statement.append("?");
            }
            else
            {
                Statement.append("?,");
            }
        }
        Statement.append(")}");
        try
        {
            proc = conn.prepareCall(Statement.toString());
            if (len > 0)
            {
                if (types != null)
                {
                    for (int i = 0; i < types.length; i++)
                    {
                        if (types[i] > 1)
                            proc.registerOutParameter(i + 1, Types.VARCHAR);
                    }
                }
                for (int i = 0; i < len; i++)
                {
                    if (para[i] == null)
                        proc.setNull(i + 1, Types.VARCHAR);
                    else
                        proc.setObject(i + 1, para[i]);
                }
            }
            proc.execute();
            ArrayList res = null;
            if (len > 0)
            {
                if (types != null)
                {
                    for (int i = 0; i < types.length; i++)
                    {
                        if (types[i] > 1)
                        {
                            if (res == null)
                                res = new ArrayList();
                            res.add(proc.getString(i + 1));
                        }
                    }
                }
            }
            proc.close();
            proc = null;
            return res;
        }
        catch (Exception ex)
        {
            if (proc != null)
                proc.close();
            throw ex;
        }
    }

    public static void executeUpdate(String sql, Object[] objs)
        throws SQLException
    {
        Connection conn = DBUtil.getConnection();
        try
        {
            DBUtil.executeUpdate(conn, sql, objs);
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            try
            {
                if (conn != null)
                    conn.close();
            }
            catch (Exception E)
            {

            }
            conn = null;
        }
    }

    public static void executeUpdate(Connection conn, String sql, Object[] objs)
        throws SQLException
    {
        PreparedStatement stmt = null;
        try
        {
            stmt = conn.prepareStatement(sql);
            if (objs != null)
                for (int i = 0; i < objs.length; i++)
                {
                    if (objs[i] != null)
                    {
                        if (objs[i] instanceof java.util.Date
                            || objs[i] instanceof java.sql.Date)
                        {
                            java.sql.Timestamp date = new java.sql.Timestamp(
                                ( (Date) objs[i]).getTime());
                            stmt.setObject(i + 1, date);
                        }
                        else if (objs[i] instanceof java.sql.Timestamp)
                            stmt.setObject(i + 1, objs[i]);
                        else
                            stmt.setString(i + 1, objs[i].toString());
                    }
                    else
                    {
                        stmt.setNull(i + 1, java.sql.Types.VARCHAR);
                    }
                }
            stmt.execute();
            if (stmt != null)

                stmt.close();
        }
        catch (SQLException e)
        {
            if (stmt != null)
                stmt.close();
            stmt = null;
            throw e;
        }
    }

    public static int getConnectionCount()
    {
        return connMgr.getCounts();
    }

    public static Connection getConnection()
    {
        return connMgr.getDefaultConnection();
    }

    public static Connection getConnection(String user, String pass)
    {
        return connMgr.getConnectionFromDs("p3", user, pass);
    }

    public static Connection getConnection(String ds, String user, String pass)
    {
        return connMgr.getConnectionFromDs(ds, user, pass);
    }

    public static Connection getConnection(String ds)
    {
        return connMgr.getConnection(ds);
    }

    public static void releaseConnection()
    {
        connMgr.release();
    }

    public static List<Map<String, String>> queryReturnList(Connection conn, String sql) throws SQLException {
    	Statement st = null;
    	ResultSet rs = null;
    	ResultSetMetaData rmd = null;
    	List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
    	try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			rmd = rs.getMetaData();
			
			while (rs.next()) {
				Map<String, String> rsMap = new HashMap<String, String>();
				String columnKey = "";
				String value = "";
				for (int i = 0; i < rmd.getColumnCount(); i++) {
					columnKey = rmd.getColumnName(i + 1);
					value = rs.getString(columnKey);
					value = value == null ? "" : value;
					rsMap.put(columnKey, value);
				}
				rsList.add(rsMap);
			}
			
            rs.close();
            rs = null;
            st.close();
            st = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            if (st != null)
                st.close();
            st = null;
            if (conn != null)
                conn.close();
            conn = null;
		}
    	return rsList;
    }
    
    public static String[][] querySql(String sql, Object[] para)
        throws SQLException
    {
        if (sql == null)
            throw new SQLException("无效的SQL语句！");
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        ResultSetMetaData md = null;
        ArrayList aList = new ArrayList();
        int rows = 0, cols;
        try
        {
            conn = connMgr.getDefaultConnection();
            stmt = conn.prepareStatement(sql);
            if (para != null)
            {
                for (int i = 0; i < para.length; i++)
                {
                    stmt.setObject(i + 1, para[i]);
                }
            }
            rs = stmt.executeQuery();
            md = rs.getMetaData();
            cols = md.getColumnCount();
            while (rs.next())
            {
                String[] row = new String[cols];
                for (int i = 0; i < cols; i++)
                {
                    row[i] = rs.getString(i + 1);
                }
                aList.add(row);
            }
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
            throw e;
        }
        finally
        {
            if (stmt != null)
                stmt.close();
            stmt = null;
            if (conn != null)
                conn.close();
            conn = null;
        }
        rows = aList.size();
        if (rows == 0 || cols == 0)
        {
            aList.clear();
            aList = null;
            return null;
        }
        String[][] res = new String[rows][cols];
        for (int i = 0; i < rows; i++)
        {
            Object[] row = (Object[]) aList.toArray()[i];
            for (int j = 0; j < cols; j++)
            {
                if (row[j] == null)
                    res[i][j] = new String("");
                else
                    res[i][j] = new String(row[j].toString());
            }
        }
        aList.clear();
        aList = null;
        return res;
    }

    public static String[][] querySql(Connection conn, String sql)
        throws SQLException, Exception
    {
        if (sql == null)
            throw new Exception("无效的SQL语句！");
        if (conn == null)
            throw new Exception("获取数据库连接失败！");
        ResultSet rs = null;
        Statement stmt = null;
        ResultSetMetaData md = null;

        ArrayList aList = new ArrayList();
        int rows = 0, cols;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            md = rs.getMetaData();
            cols = md.getColumnCount();
            while (rs.next())
            {
                String[] row = new String[md.getColumnCount() + 1];
                for (int i = 0; i < md.getColumnCount(); i++)
                {
                    row[i] = rs.getString(i + 1);
                }
                aList.add(row);
            }

            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
            throw new SQLException("#71:" + e.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            throw e;
        }
        finally
        {
            if (stmt != null)
                stmt.close();
            stmt = null;
        }

        rows = aList.size();
        if (rows == 0 || cols == 0)
        {
            aList.clear();
            aList = null;
            return null;
        }
        String[][] res = new String[rows][cols];
        for (int i = 0; i < rows; i++)
        {
            Object[] row = (Object[]) aList.toArray()[i];
            for (int j = 0; j < cols; j++)
            {
                if (row[j] == null)
                    res[i][j] = new String("");
                else
                    res[i][j] = new String(row[j].toString());
            }
        }
        aList.clear();
        aList = null;
        return res;
    }

    public static String[][] querySql(String sql)
        throws SQLException,
        Exception
    {
        Connection conn = connMgr.getDefaultConnection();
        try
        {
            return DBUtil.querySql(conn, sql);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (conn != null)
            {
                conn.close();
                conn = null;
            }
        }
    }

    public static String[][] query(String sql)
    {
        Connection conn = connMgr.getDefaultConnection();
        try
        {
            return DBUtil.query(conn, sql);
        }
        catch (Exception e)
        {
            return null;
        }
        finally
        {
            try
            {
                if (conn != null)
                    conn.close();
                conn = null;
            }
            catch (Exception ex)
            {
            }
            conn = null;
        }
    }

    public static String[][] query(Connection conn, String sql)
    {
        if (sql == null)
            return null;
        if (conn == null)
            return null;
        ResultSet rs = null;
        Statement stmt = null;
        ResultSetMetaData md = null;
        ArrayList aList = new ArrayList();
        logger.debug(sql);
        int rows = 0, cols;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            md = rs.getMetaData();
            cols = md.getColumnCount();
            while (rs.next())
            {
                String[] row = new String[md.getColumnCount() + 1];
                for (int i = 0; i < md.getColumnCount(); i++)
                {
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
                e.printStackTrace(System.out);
                return null;
            }
            return null;
        }

        rows = aList.size();
        if (rows == 0 || cols == 0)
        {
            aList.clear();
            aList = null;
            return null;
        }
        String[][] res = new String[rows][cols];
        for (int i = 0; i < rows; i++)
        {
            Object[] row = (Object[]) aList.toArray()[i];
            for (int j = 0; j < cols; j++)
            {
                res[i][j] = (String) row[j];
            }
        }
        aList.clear();
        aList = null;
        return res;
    }

    public static boolean execSql(String sql)
        throws SQLException, Exception
    {
        Connection conn = connMgr.getDefaultConnection();
        try
        {
            boolean res = DBUtil.execSql(conn, sql);
            if (res)
                conn.commit();
            return res;
        }
        catch (Exception e)
        {
            if (conn != null)
                conn.rollback();
            throw e;
        }
        finally
        {
            conn.close();
        }
    }

    public static boolean execSql(Connection conn, String sql)
        throws SQLException, Exception
    {
        if (sql == null)
            return false;
        if (conn == null)
            return false;
        Statement stmt = null;
        boolean res = false;
        try
        {
            stmt = conn.createStatement();
            stmt.execute(sql);
            res = true;
            stmt.close();
            stmt = null;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            throw e;
        }
        finally
        {
            if (stmt != null)
                stmt.close();
            stmt = null;
        }
        return res;
    }

    public static boolean exec(String sql) throws Exception
    {
        Connection conn = null;
        try
        {
            conn = getConnection();
            boolean res = DBUtil.exec(conn,
                                      sql);
            if (res)
                conn.commit();
            return res;
        }
        catch (Exception e)
        {
            try
            {
                if (conn != null)
                    conn.rollback();
            }
            catch (Exception ee)
            {}
            e.printStackTrace(System.out);
            throw e;
        }
        finally
        {
            try
            {
                conn.close();
            }
            catch (Exception E)
            {}
            conn = null;
        }
    }
    public static boolean execUpdateSql(Connection conn, String sql) throws Exception
    {
        if (sql == null)
            return false;
        if (conn == null)
            return false;
        Statement stmt = null;
        boolean res = false;
        int count = 0;
        try
        {
            stmt = conn.createStatement();
            count = stmt.executeUpdate(sql);
            stmt.close();
            stmt = null;
            if(count > 0)
            	res = true;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            throw e;
        }
        finally
        {
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                    stmt = null;
                }
                catch (SQLException ex)
                {
                }
            }
        }
        return res;
    }
    public static boolean exec(Connection conn, String sql) throws Exception
    {
        if (sql == null)
            return false;
        if (conn == null)
            return false;
        Statement stmt = null;
        boolean res = false;
        try
        {
            stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
            stmt = null;
            res = true;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            throw e;
            //return res;
        }
        finally
        {
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                    stmt = null;
                }
                catch (SQLException ex)
                {
                }
            }
        }
        return res;
    }

    public static String getSequenceValue(String seqname) throws Exception
    {
        Connection conn = connMgr.getDefaultConnection();
        try
        {
            return DBUtil.getSequenceValue(seqname, conn);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            throw e;
            //return null;
        }
        finally
        {
            try
            {
                if (conn != null)
                    conn.close();
            }
            catch (Exception E)
            {

            }
            conn = null;
        }
    }

    public static String getSequenceValue(String seqname, Connection conn)
        throws SQLException
    {
        if (Pub.empty(seqname))
            return null;
        ResultSet rs = null;
        Statement stmt = null;
        String res = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select " + seqname + ".nextval from dual");
            if (rs.next())
            {
                res = rs.getString(1);
            }
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
            throw e;
        }
        return res;
    }

    public static String getSignalValue(Connection conn, String sql)  throws Exception
    {
        if (sql == null)
            return null;
        if (conn == null)
            return null;
        ResultSet rs = null;
        Statement stmt = null;
        String res = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                res = rs.getString(1);
            }
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            if (stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch (SQLException ex)
                {
                }
            }
            res = null;
            stmt = null;
            throw e;
        }
        return res;
    }

    public static String[][] querySql(Connection conn, String sql,
                                      Object[] para)
        throws SQLException
    {
        if (sql == null)
            throw new SQLException("无效的SQL语句！");
        if (conn == null)
            throw new SQLException("获取数据库连接失败！");
        ResultSet rs = null;
        PreparedStatement stmt = null;
        ResultSetMetaData md = null;
        ArrayList aList = new ArrayList();
        int rows = 0, cols;
        try
        {
            stmt = conn.prepareStatement(sql);
            if (para != null)
            {
                for (int i = 0; i < para.length; i++)
                {
                    stmt.setObject(i + 1, para[i]);
                }
            }
            rs = stmt.executeQuery();
            md = rs.getMetaData();
            cols = md.getColumnCount();
            while (rs.next())
            {
                String[] row = new String[cols];
                for (int i = 0; i < cols; i++)
                {
                    row[i] = rs.getString(i + 1);
                }
                aList.add(row);
            }
            rs.close();
            rs = null;
            stmt.close();
            stmt = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace(System.out);
            throw e;
        }
        finally
        {
            if (stmt != null)
                stmt.close();
            stmt = null;
        }
        rows = aList.size();
        if (rows == 0 || cols == 0)
        {
            aList.clear();
            aList = null;
            return null;
        }
        String[][] res = new String[rows][cols];
        for (int i = 0; i < rows; i++)
        {
            Object[] row = (Object[]) aList.toArray()[i];
            for (int j = 0; j < cols; j++)
            {
                if (row[j] == null)
                    res[i][j] = new String("");
                else
                    res[i][j] = new String(row[j].toString());
            }
        }
        aList.clear();
        aList = null;
        return res;
    }


    public static String getSqlPage(String sql, PageManager page)
    {
        try
        {
            int icur = page.getCurrentPage();
            int ipagenums = page.getPageRows();
//            sql = " select * from (select s.*,rownum idnum from (" + sql +
//                ") s " +
//                " where  rownum <=" + icur * ipagenums + " ) q where  idnum> " +
//                (icur - 1) * ipagenums;
            //mofify by zhangbr@ccthanking.com  2013-08-29
            sql = " select * from (select s.*,rownum idnum from (" + sql +
	            ") s " +
	            "    ) q where  idnum> " +
	            (icur - 1) * ipagenums +" and idnum <=" + icur * ipagenums ;
            return sql;
        }
        catch (Exception e)
        {
            return sql;
        }
    }

    /*
     * 获取服务器时间
     */
    public static String getServerTime(String fmt, Connection conn)
    {
        String time = null;
        // Create SQL:
        String sql = "";
        String[][] result = null;
        if (fmt == null)
        {
            sql =
                "Select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') tm from dual";
        }
        else
        {
            sql = "Select to_char(sysdate,'" + fmt + "') tm from dual";
        }
        try
        {
            result = DBUtil.query(conn, sql);
        }
        catch (Exception ex)
        {
            try
            {
                throw new Exception("提取系统时间时出错!" + ex.getMessage());
            }
            catch (Exception ex1)
            {
            }
        }
        // Get time:
        time = result[0][0];
        // Return:
        return time;
    }

    /*
     * 获取服务器时间
     */
    public static String getServerTime(String fmt)
    {

        Connection conn = DBUtil.getConnection();

        try
        {
            return getServerTime(fmt, conn);
        }
        catch (Exception e)
        {
            try
            {
                throw new Exception(e.getMessage());
            }
            catch (Exception ex1)
            {
            }
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException ex)
                {

                }
            }
        }
        return "";
    }

    //add by wuxp
    public static QuerySet executeQuery(String sql, Object[] paras)

        throws SQLException
    {
        int start = 1;
        int length = com.ccthanking.framework.Constants.MAX_RECORD_LIMITED;
        if (sql == null)
            throw new SQLException("无效的SQL语句！");
        Connection conn = connMgr.getDefaultConnection();

        QuerySet qs; //return value
        ArrayList dataObjs = new ArrayList();
        HashMap attrMap = new HashMap();
        HashMap columnType = new HashMap();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = conn.prepareStatement(sql);
            if (paras != null)
            {
                for (int i = 0; i < paras.length; i++)
                {
                    pstmt.setObject(i + 1, paras[i]);
                }
            }
            rs = pstmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int colNum = metaData.getColumnCount();

            for (int i = 0; i < colNum; i++)
            {
                attrMap.put(metaData.getColumnName(i + 1).toLowerCase(),
                            new Integer(i + 1));
                columnType.put(new Integer(i + 1),new Integer(metaData.getColumnType(i+1)));

            }

            boolean outOfRange = false;
            for (int i = 0; i < start; i++)
            {
                if (!rs.next())
                {
                    outOfRange = true;
                    break;
                }
            }

            if (!outOfRange)
            {
                int pos = 0;
                do
                {
                    if (length != -1 && length <= pos)
                        break;

                    ArrayList dataItems = new ArrayList();
                    for (int i = 0; i < colNum; i++)
                    {
                        dataItems.add(new DataItemImpl(rs.getObject(i + 1)));
                    }
                    dataObjs.add(new DataObjImpl(dataItems, attrMap));

                    pos++;
                }
                while (rs.next());
            }
            qs = new QuerySetImpl(sql, paras, dataObjs, attrMap,columnType);
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            if(rs != null) rs.close();
            rs = null;
            if(pstmt != null) pstmt.close();
            pstmt = null;
            if (conn != null)
                conn.close();
            conn = null;
        }
        return qs;
    }
    //add by wuxp
    public static QuerySet executeQuery(String sql, Object[] paras,Connection conn)
        throws SQLException
    {
        int start = 1;
        int length = com.ccthanking.framework.Constants.MAX_RECORD_LIMITED;
        if (sql == null)
            throw new SQLException("无效的SQL语句！");

        QuerySet qs; //return value
        ArrayList dataObjs = new ArrayList();
        HashMap attrMap = new HashMap();
        HashMap columnType = new HashMap();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = conn.prepareStatement(sql);
            if (paras != null)
            {
                for (int i = 0; i < paras.length; i++)
                {
                    pstmt.setObject(i + 1, paras[i]);
                }
            }
            rs = pstmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int colNum = metaData.getColumnCount();

            for (int i = 0; i < colNum; i++)
            {
                attrMap.put(metaData.getColumnName(i + 1).toLowerCase(),
                            new Integer(i + 1));
                columnType.put(new Integer(i + 1),new Integer(metaData.getColumnType(i+1)));
            }

            boolean outOfRange = false;
            for (int i = 0; i < start; i++)
            {
                if (!rs.next())
                {
                    outOfRange = true;
                    break;
                }
            }

            if (!outOfRange)
            {
                int pos = 0;
                do
                {
                    if (length != -1 && length <= pos)
                        break;

                    ArrayList dataItems = new ArrayList();
                    for (int i = 0; i < colNum; i++)
                    {
                        dataItems.add(new DataItemImpl(rs.getObject(i + 1)));
                    }
                    dataObjs.add(new DataObjImpl(dataItems, attrMap));

                    pos++;
                }
                while (rs.next());
            }
            qs = new QuerySetImpl(sql, paras, dataObjs, attrMap,columnType);
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            if(rs != null) rs.close();
            rs = null;
            if(pstmt != null) pstmt.close();
            pstmt = null;

        }
        return qs;
    }
    
    /**
	 * 关闭数据库连接
	 * @param conn
	 */
	public static void closeConnetion(Connection conn) {
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 回滚事务
	 * @param conn
	 */
	public static void rollbackConnetion(Connection conn) {
		if (null != conn) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static ResultSet exportQuery(Connection conn, String strSql, PageManager page) {
		if (conn == null || Pub.empty(strSql))
			return null;
		PreparedStatement psCount = null;
		ResultSet rsCount = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (page == null) {
				page = new PageManager();
				page.setPageRows(100);
			}
			int cp = page.getCurrentPage();
			int pr = page.getPageRows();
			if (pr <= 0)
				pr = 100;
			int countP = page.getCountPage();
			int iCountRows = page.getCountRows();
			if (!Pub.empty(page.getFilter()))
				strSql += " where " + page.getFilter();
			logger.debug(strSql);
			if (countP <= 0) {
				String strSqlCount = "select count(*) from (" + strSql + ")";
				psCount = conn.prepareStatement(strSqlCount);
				rsCount = psCount.executeQuery();
				if (rsCount.next()) {
					iCountRows = rsCount.getInt(1);
				}
				if (iCountRows > 0) {
					countP = iCountRows / pr;
					int mod = iCountRows % pr;
					if (mod > 0)
						countP++;
					if (cp > countP)
						cp = countP;
					if (cp < 1)
						cp = 1;
				}
				rsCount.close();
				rsCount = null;
				psCount.close();
				psCount = null;
			}
			page.setCurrentPage(cp);
			page.setCountPage(countP);
			page.setCountRows(iCountRows);
			page.setPageRows(pr);

			strSql = DBUtil.getSqlPage(strSql, page);
			logger.debug(strSql);
			ps = conn.prepareStatement(strSql, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();

			return rs;
		} catch (Exception e) {
			System.out.println(strSql);
			e.printStackTrace();
		} finally {
			try {
				if (rsCount != null)
					rsCount.close();
				if (psCount != null)
					psCount.close();
			} catch (Exception e) {
			}
		}
		return null;
	}
}
