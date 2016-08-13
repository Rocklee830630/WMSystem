package com.ccthanking.framework.base;

import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import oracle.jdbc.OraclePreparedStatement;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.log.log;

public class BaseDAO {
	
	private Connection _conn;

	public BaseDAO() {
	}

	public BaseDAO(Connection conn) {
		this._conn = conn;
	}

	public void setConnection(Connection conn) {
		this._conn = conn;
	}

	public Connection getConnection() {
		return this._conn;
	}

	public BaseVO[] getVOByCondition(BaseVO vo) throws Exception {
		return getVOByCondition(_conn, vo);
	}

	public static BaseVO[] getVOByCondition(Connection conn, BaseVO vo)
			throws Exception {
		ArrayList list = null;
		String sql = "select * from " + vo.getVOTableName() + " where ";
		String condstr = "";
		Enumeration enumer = vo.getFields();
		if (enumer != null) {
			while (enumer.hasMoreElements()) {
				String key = (String) enumer.nextElement();
				if (vo.isEmpty(key))
					continue;
				if (condstr.length() < 2) {
					condstr += key + "=?";
				} else {
					condstr += " and " + key + "=?";
				}
			}
		} else
			throw new Exception("BASE DAO:非法操作！#72");
		sql += condstr;
		sql += condstr.length() < 2 ? " rownum < 1000 " : " and rownum < 1000";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData md = null;
		try {
			log.getLogger("").info(sql);
			stmt = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			enumer = vo.getFields();
			int k = -1;
			while (enumer.hasMoreElements()) {
				String key = (String) enumer.nextElement();
				if (!vo.isEmpty(key))
					setParas(++k, stmt, key, vo);
			}
			rs = stmt.executeQuery();
			md = rs.getMetaData();
			int cols = md.getColumnCount();
			while (rs.next()) {
				BaseVO res = (BaseVO) vo.getClass().newInstance();
				if (list == null)
					list = new ArrayList();
				list.add(res);
				for (int i = 1; i <= md.getColumnCount(); i++) {
					String colname = md.getColumnName(i).toUpperCase();
					if (!res.isValidField(colname))
						continue;
					int coltype = md.getColumnType(i);
					if (coltype == java.sql.Types.DATE
							|| coltype == java.sql.Types.TIMESTAMP) {
						if (rs.getDate(i) != null)
							res.put(colname, rs.getDate(i));
					} else if (coltype == java.sql.Types.BLOB) {
						Blob dbBlob;
						dbBlob = rs.getBlob(i);
						if (dbBlob == null)
							continue;
						int length = (int) dbBlob.length();
						byte[] buffer = dbBlob.getBytes(1, length);
						res.put(colname, buffer);
					} else if (coltype == java.sql.Types.NULL) {
						// res.put(colname,null);
					} else {
						if (rs.getString(i) != null)
							res.put(colname, rs.getString(i));
					}
				}
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (Exception e) {
			com.ccthanking.framework.log.log.getLogger("").info(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			rs = null;
			if (stmt != null)
				stmt.close();
			stmt = null;
		}
		return list == null ? null : (BaseVO[]) list.toArray(new BaseVO[list
				.size()]);
	}

	public BaseVO getVOByPrimaryKey(BaseVO vo) throws Exception {
		return getVOByPrimaryKey(_conn, vo);
	}

	public static BaseVO getVOByPrimaryKey(Connection conn, BaseVO vo)
			throws Exception {
		BaseVO res = (BaseVO) vo.getClass().newInstance();
		String sql = "select * from " + vo.getVOTableName() + " where ";
		String condstr = "";
		String[] pks = vo.getPkFields();
		if (pks != null) {
			for (int i = 0; i < pks.length; i++) {
				if (vo.isEmpty(pks[i]))
					throw new Exception("BASE DAO:非法操作！#53");
				if (condstr.length() < 2) {
					condstr += pks[i] + "=?";
				} else {
					condstr += " and " + pks[i] + "=?";
				}
			}
		} else
			throw new Exception("BASE DAO:非法操作！#64");
		sql += condstr;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData md = null;
		try {
			log.getLogger("").info(sql);
			stmt = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			for (int i = 0; i < pks.length; i++) {
				String key = pks[i];
				setParas(i, stmt, key, vo);
			}
			rs = stmt.executeQuery();
			md = rs.getMetaData();
			int cols = md.getColumnCount();
			if (rs.next()) {
				for (int i = 1; i <= md.getColumnCount(); i++) {
					String colname = md.getColumnName(i).toUpperCase();
					if (!res.isValidField(colname))
						continue;
					int coltype = md.getColumnType(i);
					if (coltype == java.sql.Types.DATE
							|| coltype == java.sql.Types.TIMESTAMP) {
						if (rs.getDate(i) != null)
							res.put(colname, rs.getDate(i));
					} else if (coltype == java.sql.Types.BLOB) {
						Blob dbBlob;
						dbBlob = rs.getBlob(i);
						if (dbBlob == null)
							continue;
						int length = (int) dbBlob.length();
						byte[] buffer = dbBlob.getBytes(1, length);
						res.put(colname, buffer);
					} else if (coltype == java.sql.Types.NULL) {
						// res.put(colname,null);
					} else {
						if (rs.getString(i) != null)
							res.put(colname, rs.getString(i));
					}
				}
			} else
				return null;
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (Exception e) {
			com.ccthanking.framework.log.log.getLogger("").info(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			rs = null;
			if (stmt != null)
				stmt.close();
			stmt = null;
		}

		return res;
	}

	public boolean delete(BaseVO vo) throws Exception {
		return delete(_conn, vo);
	}

	public static boolean delete(Connection conn, BaseVO vo) throws Exception {
		if (conn == null || vo == null || vo.isEmpty())
			throw new Exception("BASE DAO:非法操作！#149");
		String[] pks = vo.getPkFields();
		String condstr = "";
		if (pks != null) {
			for (int i = 0; i < pks.length; i++) {
				if (vo.isEmpty(pks[i]))
					throw new Exception("BASE DAO:非法操作！#157");
				if (condstr.length() < 2) {
					condstr += pks[i] + "=?";
				} else {
					condstr += " and " + pks[i] + "=?";
				}
			}
		} else
			throw new Exception("BASE DAO:非法操作！#168");
		String sql = "delete from " + vo.getVOTableName() + " where " + condstr;
		PreparedStatement stmt = null;
		try {
			log.getLogger("").info(sql);
			stmt = conn.prepareStatement(sql);
			for (int j = 0; j < pks.length; j++) {
				String key = pks[j];
				setParas(j, stmt, key, vo);
			}
			stmt.execute();
			stmt.close();
			stmt = null;
		} catch (Exception e) {
			com.ccthanking.framework.log.log.getLogger("").info(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
			stmt = null;
		}
		return true;
	}

	public boolean update(BaseVO vo) throws Exception {
		return update(_conn, vo);
	}



	private static boolean updateByPartitions(Connection conn, BaseVO vo,
			String[] partitions, String[] pks) throws Exception {
		BaseVO old = getVOByPrimaryKey(conn, vo);
		old.setValueByChanged(vo);
		delete(conn, vo);
		insert(conn, old);
		return true;
	}



	public static boolean update(Connection conn, BaseVO vo) throws Exception {
		if (conn == null || vo == null || vo.isEmpty())
			throw new Exception("BASE DAO:非法操作！#216");
		String[] pks = vo.getPkFields();
		String condstr = "";
		if (pks != null) {
			for (int i = 0; i < pks.length; i++) {
				if (vo.isEmpty(pks[i]))
					throw new Exception("BASE DAO:非法操作！#224");
				if (condstr.length() < 2) {
					condstr += pks[i] + "=?";
				} else {
					condstr += " and " + pks[i] + "=?";
				}
			}
		} else
			throw new Exception("BASE DAO:非法操作！#235");
		String[] partitions = vo.getPartitionFields();
		if (partitions != null) {
			for (int i = 0; i < partitions.length; i++) {
				if (vo.isChanged(partitions[i])) {
					if (vo.isEmpty(partitions[i]))
						throw new Exception("BASE DAO:非法操作！#244");
					else
						// if(false)
						return updateByPartitions(conn, vo, partitions, pks);
				}
			}
		}
		String sql = "update " + vo.getVOTableName() + " set ";
		int i = 0;
		String vStr = "";
		ArrayList parasKey = new ArrayList();
		ArrayList blobsKey = new ArrayList();
		// add by wangzh for clob field 20070905
		ArrayList clobsKey = new ArrayList();
		Enumeration keys = vo.getFields();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (vo.isChanged(key)) {
				if (i > 0) {
					if (vo.isBlob(key)) {
						sql += "," + key + "=EMPTY_BLOB()";
						blobsKey.add(key);
					}
					// add by wangzh for clob field 20070905
					else if (vo.isClob(key)) {
						sql += "," + key + "=EMPTY_BLOB()";
						clobsKey.add(key);
					} else {
						sql += "," + key + "=?";
						parasKey.add(key);
					}
				} else {
					if (vo.isBlob(key)) {
						sql += key + "=EMPTY_BLOB()";
						blobsKey.add(key);
					}
					// add by wangzh for clob field 20070905
					else if (vo.isClob(key)) {
						sql += key + "=EMPTY_BLOB()";
						clobsKey.add(key);
					} else {
						sql += key + "=?";
						parasKey.add(key);
					}
				}
				i++;
			}
		}
		if (i == 0)
			throw new Exception("BASE DAO: 非法操作！#291");
		sql += " where " + condstr;
		Object[] paras = new Object[parasKey.size()];
		PreparedStatement stmt = null;
		try {
			log.getLogger("").info(sql);
			stmt = conn.prepareStatement(sql);
			for (i = 0; i < parasKey.size(); i++) {
				String key = (String) parasKey.get(i);
				setParas(i, stmt, key, vo);
			}
			for (int j = 0; j < pks.length; j++) {
				String key = pks[j];
				setParas(i, stmt, key, vo);
				i++;
			}
			stmt.execute();
			stmt.close();
			stmt = null;
			if (blobsKey.size() > 0)
				processBlobFields(conn, vo, blobsKey);
			// add by wangzh for clob field 20070905
			if (clobsKey.size() > 0)
				processClobFields(conn, vo, clobsKey);
		} catch (Exception e) {
			com.ccthanking.framework.log.log.getLogger("").info(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
			stmt = null;
		}
		return true;
	}

	public boolean insert(BaseVO vo) throws Exception {
		return insert(_conn, vo);
	}

	public static boolean insert(Connection conn, BaseVO vo) throws Exception {
		if (conn == null || vo == null)
			throw new Exception("BASE DAO:非法操作！#341");
		String sql = "insert into " + vo.getVOTableName() + " (";
		int i = 0;
		String vStr = "values(";
		ArrayList parasKey = new ArrayList();
		ArrayList blobsKey = new ArrayList();
		// add by wangzh for clob field 20070905
		ArrayList clobsKey = new ArrayList();
		Enumeration keys = vo.getFields();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (vo.isEmpty(key) && vo.isBindSequence(key)) {
				String sval = DBUtil.getSequenceValue(vo.getBindSequence(key),
						conn);
				vo.setInternal(key, sval);
			}
			if (!vo.isEmpty(key)) {
				if (i > 0) {
					if (vo.isBlob(key)) {
						sql += "," + key;
						vStr += ",EMPTY_BLOB()";
						blobsKey.add(key);
					}
					// add by wangzh for clob field 20070905
					else if (vo.isClob(key)) {
						sql += "," + key;
						vStr += ",EMPTY_BLOB()";
						clobsKey.add(key);
					} else {
						sql += "," + key;
						vStr += ",?";
						parasKey.add(key);
					}
				} else {
					if (vo.isBlob(key)) {
						sql += key;
						vStr += "EMPTY_BLOB()";
						blobsKey.add(key);
					} else if (vo.isClob(key)) {
						sql += key; // modified by songxb@2008-07-02 原代码为:sql +=
									// "," + key;
						vStr += "EMPTY_BLOB()"; // modified by songxb@2008-07-02
												// 原代码为:vStr += ",EMPTY_BLOB()";
						clobsKey.add(key);
					} else {
						sql += key;
						vStr += "?";
						parasKey.add(key);
					}
				}
				i++;
			}
		}
		if (i > 0)
			sql += ") " + vStr + ")";
		else
			throw new Exception("BASE DAO: 非法操作！#395");
		Object[] paras = new Object[parasKey.size()];
		PreparedStatement stmt = null;
		try {
			log.getLogger("").info(sql);
			stmt = conn.prepareStatement(sql);
			for (i = 0; i < parasKey.size(); i++) {
				String key = (String) parasKey.get(i);
				setParas(i, stmt, key, vo);
			}
			stmt.execute();
			stmt.close();
			stmt = null;
			if (blobsKey.size() > 0)
				processBlobFields(conn, vo, blobsKey);
			// add by wangzh for clob field 20070905
			if (clobsKey.size() > 0)
				processClobFields(conn, vo, clobsKey);
		} catch (Exception e) {
			com.ccthanking.framework.log.log.getLogger("").info(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (stmt != null)
				stmt.close();
			stmt = null;
		}
		return true;
	}

	/**
	 * processBlobField
	 * 
	 * @param conn
	 *            Connection
	 * @param vo
	 *            BaseVO
	 * @param blobsKey
	 *            ArrayList
	 */
	private static void setParas(int i, PreparedStatement stmt, String key,
			BaseVO vo) throws Exception {
		Object para = vo.get(key);
		if (para != null) {
			if (vo.isNchar(key)) {
				try {
					if (stmt instanceof com.ibm.ws.rsadapter.jdbc.WSJdbcPreparedStatement) {
						OraclePreparedStatement ostmt = (OraclePreparedStatement) ((com.ibm.ws.rsadapter.jdbc.WSJdbcPreparedStatement) stmt).pstmtImpl;
						ostmt.setFormOfUse(i + 1,
								oracle.jdbc.OraclePreparedStatement.FORM_NCHAR);
					} else {
						try {
							OraclePreparedStatement ostmt = (OraclePreparedStatement) stmt;
							ostmt.setFormOfUse(
									i + 1,
									oracle.jdbc.OraclePreparedStatement.FORM_NCHAR);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Throwable ee) {
					ee.printStackTrace(System.out);
				}
			}
			if (para instanceof java.util.Date || para instanceof java.sql.Date) {
				java.sql.Timestamp date = new java.sql.Timestamp(
						((java.util.Date) para).getTime());
				stmt.setObject(i + 1, date);
			} else if (para instanceof java.sql.Timestamp)
				stmt.setObject(i + 1, para);
			else
				stmt.setString(i + 1, para.toString());
		} else {
			stmt.setNull(i + 1, java.sql.Types.VARCHAR);
		}

	}

	// add by wangzh for clob field 20070905
	private static void processClobFields(Connection conn, BaseVO vo,
			ArrayList clobsKey) throws Exception {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = "select ";
			Iterator itor = clobsKey.iterator();
			while (itor.hasNext()) {
				sql += (String) itor.next() + ",";
			}
			sql += "1 from " + vo.getVOTableName() + " where ";
			Enumeration enumer = vo.getFields();
			ArrayList paralist = new ArrayList(3);
			while (enumer.hasMoreElements()) {
				String key = (String) enumer.nextElement();
				if (vo.isPk(key)) {
					sql += key + "=? and ";
					paralist.add(vo.getInternal(key));
				}
			}
			sql += "1=1 for update ";
			com.ccthanking.framework.log.log.getLogger("").info(sql);
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < paralist.size(); i++) {
				stmt.setObject(i + 1, paralist.get(i));
			}
			rs = stmt.executeQuery();
			if (!rs.next())
				return;
			for (int i = 0; i < clobsKey.size(); i++) {
				// if(vo.get(clobsKey.get(i)) == null) continue;
				// oracle.sql.CLOB dbclob = null;
				// dbclob = (oracle.sql.CLOB) rs.getBlob(i + 1);
				// BufferedWriter out = new
				// BufferedWriter(dbclob.getCharacterOutputStream());
				// BufferedReader in = new BufferedReader(new
				// java.io.StringReader((String)vo.get(clobsKey.get(i))));
				// int c;
				// while ((c=in.read())!=-1) {
				// out.write(c);
				// }
				// out.flush();
				// out.close();
				// in.close();
				if (vo.get(clobsKey.get(i)) == null)
					continue;
				oracle.sql.BLOB dbBlob = null;
				dbBlob = (oracle.sql.BLOB) rs.getBlob(i + 1);
				OutputStream os = dbBlob.getBinaryOutputStream();
				String t = (String) vo.get(clobsKey.get(i));
				byte[] b = t.getBytes("GBK");
				os.write(b);
				os.close();
			}
		} catch (Exception e) {
			com.ccthanking.framework.log.log.getLogger("").info(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			rs = null;
			if (stmt != null)
				stmt.close();
			stmt = null;
		}
	}

	private static void processBlobFields(Connection conn, BaseVO vo,
			ArrayList blobsKey) throws Exception {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = "select ";
			Iterator itor = blobsKey.iterator();
			while (itor.hasNext()) {
				sql += (String) itor.next() + ",";
			}
			sql += "1 from " + vo.getVOTableName() + " where ";
			Enumeration enumer = vo.getFields();
			ArrayList paralist = new ArrayList(3);
			while (enumer.hasMoreElements()) {
				String key = (String) enumer.nextElement();
				if (vo.isPk(key)) {
					sql += key + "=? and ";
					paralist.add(vo.getInternal(key));
				}
			}
			sql += "1=1 for update ";
			com.ccthanking.framework.log.log.getLogger("").info(sql);
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < paralist.size(); i++) {
				stmt.setObject(i + 1, paralist.get(i));
			}
			rs = stmt.executeQuery();
			if (!rs.next())
				return;
			for (int i = 0; i < blobsKey.size(); i++) {
				if (vo.get(blobsKey.get(i)) == null)
					continue;
				oracle.sql.BLOB dbBlob = null;
				dbBlob = (oracle.sql.BLOB) rs.getBlob(i + 1);
				OutputStream os = dbBlob.getBinaryOutputStream();
				os.write((byte[]) vo.get(blobsKey.get(i)));
				os.close();
			}
		} catch (Exception e) {
			com.ccthanking.framework.log.log.getLogger("").info(e);
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			rs = null;
			if (stmt != null)
				stmt.close();
			stmt = null;
		}
	}
	
	public static String comprisesResponseData(Connection conn, List<String> rowarrylist) throws Exception {
		
		String responseData = "{\"response\":{\"data\":[";
		
		for(int i=0;i<rowarrylist.size();i++){
			responseData += rowarrylist.get(i)+",";
		}
		
		responseData = responseData.substring(0,responseData.length()-1);
		
		responseData += "]}}";
		
		return responseData;
	}
	
	public static String stringToJson(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '/':
                sb.append("\\/");
                break;
            default:
                if (ch >= '\u0000' && ch <= '\u001F') {
                    String ss = Integer.toHexString(ch);
                    sb.append("\\u");
                    for (int k = 0; k < 4 - ss.length(); k++) {
                        sb.append('0');
                    }
                    sb.append(ss.toUpperCase());
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
      }
	
	/// <summary>
	/// Encodes a string to be represented as a string literal. The format
	/// is essentially a JSON string.
	/// 
	/// The string returned includes outer quotes 
	/// Example Output: "Hello \"Rick\"!\r\nRock on"
	/// </summary>
	/// <param name="s"></param>
	/// <returns></returns>
	public static String EncodeJsString(String s)
	{
	    StringBuilder sb = new StringBuilder();
//	    sb.append("\"");
//	    for (char c :  s)
//	    {
	    for (int j = 0; j < s.length(); j++) {
	    char c = s.charAt(j);
	        switch (c)
	        {
	            case '"':
	                sb.append("\\\"");
	                break;
	            case '\\':
	                sb.append("\\\\");
	                break;
	            case ' ':
	                sb.append("\\b");
	                break;
	            case '\f':
	                sb.append("\\f");
	                break;
	            case '\n':
	                sb.append("\\n");
	                break;
	            case '\r':
	                sb.append("\\r");
	                break;
	            case '\t':
	                sb.append("\\t");
	                break;
	            case '>':
	                sb.append("&gt;");
	                break;
		       case '<':
	                sb.append("&lt;");
	                break;
	                
	            default:
	                int i = (int)c;
	                if (c >= '\u0000' && c <= '\u001F') {
	                    String ss = Integer.toHexString(c);
	                    sb.append("\\u");
	                    for (int k = 0; k < 4 - ss.length(); k++) {
	                        sb.append('0');
	                    }
	                    sb.append(ss.toUpperCase());
	                }
	                else
	                {
	                    sb.append(c);
	                }
	                break;
	        }
	    }
//	    sb.append("\"");

	    return sb.toString();
	}
	
}
