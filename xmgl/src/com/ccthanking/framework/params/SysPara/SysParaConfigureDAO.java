package com.ccthanking.framework.params.SysPara;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ccthanking.framework.base.BaseDAO;

public class SysParaConfigureDAO extends BaseDAO { /* 系统参数配置表 */

	private final String ENCODE = "UTF-8";

	private final String XML = "XML";

	private String INSERT = "";

	private String INSERT_LIST = "INSERT INTO FS_PARA_SYS_CONFIGURE(SN,APPTYPE,APPLICATION,PARAKEY,PARANAME,PARAVALUE1,PARAVALUE2,PARAVALUE3,PARAVALUE4,MEMO";

	private String INSERT_VALUES = ") VALUES(?,?,?,?,?,?,?,?,?,?";

	private String UPDATE = "UPDATE FS_PARA_SYS_CONFIGURE SET SN=? , APPTYPE=? , APPLICATION=? , PARAKEY=? , PARANAME=? , PARAVALUE1=? , PARAVALUE2=? , PARAVALUE3=? , PARAVALUE4=? , MEMO=?";

	private String UPDATE_WHERE = " WHERE SN=?";

	private String DELETE = "DELETE  FROM FS_PARA_SYS_CONFIGURE WHERE SN=?";

	private String SELECT_BY_ID = "select SN , APPTYPE , APPLICATION , PARAKEY , PARANAME , PARAVALUE1 , PARAVALUE2 , PARAVALUE3 , PARAVALUE4 , MEMO  FROM FS_PARA_SYS_CONFIGURE WHERE SN=?";

	private String[] sColId = { "SYS_PARA_CONFIGURE___SN",
			"SYS_PARA_CONFIGURE___APPTYPE",
			"SYS_PARA_CONFIGURE___APPLICATION",
			"SYS_PARA_CONFIGURE___PARAKEY", "SYS_PARA_CONFIGURE___PARANAME",
			"SYS_PARA_CONFIGURE___PARAVALUE1",
			"SYS_PARA_CONFIGURE___PARAVALUE2",
			"SYS_PARA_CONFIGURE___PARAVALUE3",
			"SYS_PARA_CONFIGURE___PARAVALUE4", "SYS_PARA_CONFIGURE___MEMO" };

	private String SELECT_ALL = "select SN , APPTYPE , APPLICATION , PARAKEY , PARANAME , PARAVALUE1 , PARAVALUE2 , PARAVALUE3 , PARAVALUE4 , MEMO from SYS_PARA_CONFIGURE";

	private String[] sColAll = { "SYS_PARA_CONFIGURE___SN",
			"SYS_PARA_CONFIGURE___APPTYPE",
			"SYS_PARA_CONFIGURE___APPLICATION",
			"SYS_PARA_CONFIGURE___PARAKEY", "SYS_PARA_CONFIGURE___PARANAME",
			"SYS_PARA_CONFIGURE___PARAVALUE1",
			"SYS_PARA_CONFIGURE___PARAVALUE2",
			"SYS_PARA_CONFIGURE___PARAVALUE3",
			"SYS_PARA_CONFIGURE___PARAVALUE4", "SYS_PARA_CONFIGURE___MEMO" };

	private String SELECT_COUNT = "select count(*) from SYS_PARA_CONFIGURE";

	private String MHBSELECT_ALL = "select SYS_PARA_CONFIGURE.SN , SYS_PARA_CONFIGURE.APPTYPE , SYS_PARA_CONFIGURE.APPLICATION , SYS_PARA_CONFIGURE.PARAKEY , SYS_PARA_CONFIGURE.PARANAME , SYS_PARA_CONFIGURE.PARAVALUE1 , SYS_PARA_CONFIGURE.PARAVALUE2 , SYS_PARA_CONFIGURE.PARAVALUE3 , SYS_PARA_CONFIGURE.PARAVALUE4 , SYS_PARA_CONFIGURE.MEMO from SYS_PARA_CONFIGURE";

	private String[] sMHBColAll = { "SYS_PARA_CONFIGURE___SN",
			"SYS_PARA_CONFIGURE___APPTYPE",
			"SYS_PARA_CONFIGURE___APPLICATION",
			"SYS_PARA_CONFIGURE___PARAKEY", "SYS_PARA_CONFIGURE___PARANAME",
			"SYS_PARA_CONFIGURE___PARAVALUE1",
			"SYS_PARA_CONFIGURE___PARAVALUE2",
			"SYS_PARA_CONFIGURE___PARAVALUE3",
			"SYS_PARA_CONFIGURE___PARAVALUE4", "SYS_PARA_CONFIGURE___MEMO" };

	private String EXISET = "select SN from FS_PARA_SYS_CONFIGURE where SN = ? ";

	private String[] strMessage = { "意料外的错误", "主键值重复", "未发现要修改的记录",
			"未发现要删除的记录", "", "", "", "", "", "", "", "", "", "", "", "" };

    public Object selectById(String pk,Connection tempConn)throws SQLException {
    	PreparedStatement ps = null;
		ResultSet rs = null;
		SysParaConfigureVO  tempVO=null;
		try
		{
			ps = tempConn.prepareStatement(this.SELECT_BY_ID);
			ps.setString(1, pk);
			rs = ps.executeQuery();
			if(rs.next())
			{
				tempVO = new SysParaConfigureVO();
				tempVO.setSysParaConfigureSn(rs.getString(1));
				tempVO.setSysParaConfigureApptype(rs.getString(2));
				tempVO.setSysParaConfigureApplicateion(rs.getString(3));
				tempVO.setSysParaConfigureParakey(rs.getString(4));
				tempVO.setSysParaConfigureParaname(rs.getString(5));
				tempVO.setSysParaConfigureParavalue1(rs.getString(6));
				tempVO.setSysParaConfigureParavalue2(rs.getString(7));
				tempVO.setSysParaConfigureParavalue3(rs.getString(8));
				tempVO.setSysParaConfigureParavalue4(rs.getString(9));
				tempVO.setSysParaConfigureMemo(rs.getString(9));
			}
		}
		catch(Exception e)
		{
			throw new SQLException(e.toString());
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				System.out.println("TestLi.insert:" + e.toString());

			}
			try {
				if (ps != null) {
					ps.close();
					ps = null;
				}
			} catch (Exception e) {
				System.out.println("TestLi.insert:" + e.toString());

			}
		}

		return tempVO;
    }

	public String insert(Object obj, Connection tempConn) throws SQLException {
		String pkvalue = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			SysParaConfigureVO tempVO = (SysParaConfigureVO) obj;
			pkvalue  =tempVO.getSysParaConfigureSn();

			ps = tempConn.prepareStatement("SELECT PARAKEY FROM FS_PARA_SYS_CONFIGURE WHERE PARAKEY=?");
			ps.setString(1, tempVO.getSysParaConfigureParakey());
			rs = ps.executeQuery();
			if(rs.next())
			{
				return "主键重复";
			}
			else rs.close();


			this.INSERT = this.INSERT_LIST + this.INSERT_VALUES + ")";
			ps = tempConn.prepareStatement(this.INSERT);
			ps.setString(1, tempVO.getSysParaConfigureSn()); /* 参数序号 */
			ps.setString(2, tempVO.getSysParaConfigureApptype()); /* 参数类型 */
			ps.setString(3, tempVO.getSysParaConfigureApplicateion()); /* 应用名 */
			ps.setString(4, tempVO.getSysParaConfigureParakey()); /* 参数key */
			ps.setString(5, tempVO.getSysParaConfigureParaname()); /* 参数名称 */
			ps.setString(6, tempVO.getSysParaConfigureParavalue1()); /* 参数值1 */
			ps.setString(7, tempVO.getSysParaConfigureParavalue2()); /* 参数值2 */
			ps.setString(8, tempVO.getSysParaConfigureParavalue3()); /* 参数值3 */
			ps.setString(9, tempVO.getSysParaConfigureParavalue4()); /* 参数值4 */
			ps.setString(10, tempVO.getSysParaConfigureMemo()); /* 备注 */
			ps.executeUpdate();
			ps.close();
			ps = null;
		} catch (Exception e) {

			throw new SQLException(e.toString());
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				System.out.println("TestLi.insert:" + e.toString());

			}
			try {
				if (ps != null) {
					ps.close();
					ps = null;
				}
			} catch (Exception e) {
				System.out.println("TestLi.insert:" + e.toString());

			}
		}
		return pkvalue;

	}

	public String update(Object obj, Connection tempConn) throws SQLException {
		String strRes = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			SysParaConfigureVO tempVO = (SysParaConfigureVO) obj;

			this.UPDATE = this.UPDATE + this.UPDATE_WHERE;

			ps = tempConn.prepareStatement(this.UPDATE);

			ps.setString(1, tempVO.getSysParaConfigureSn()); /* 参数序号 */

			ps.setString(2, tempVO.getSysParaConfigureApptype()); /* 参数类型 */

			ps.setString(3, tempVO.getSysParaConfigureApplicateion()); /* 应用名 */

			ps.setString(4, tempVO.getSysParaConfigureParakey()); /* 参数key */

			ps.setString(5, tempVO.getSysParaConfigureParaname()); /* 参数名称 */

			ps.setString(6, tempVO.getSysParaConfigureParavalue1()); /* 参数值1 */

			ps.setString(7, tempVO.getSysParaConfigureParavalue2()); /* 参数值2 */

			ps.setString(8, tempVO.getSysParaConfigureParavalue3()); /* 参数值3 */

			ps.setString(9, tempVO.getSysParaConfigureParavalue4()); /* 参数值4 */

			ps.setString(10, tempVO.getSysParaConfigureMemo()); /* 备注 */

			ps.setString(11, tempVO.getSysParaConfigureSn()); /* 参数序号 */

			ps.executeUpdate();
			ps.close();
			ps = null;
			boolean isInsert = false;

		} catch (Exception e) {
			System.out.println(e);
			throw new SQLException(e.toString());
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				System.out.println("TestLi.update:" + e.toString());

			}
			try {
				if (ps != null) {
					ps.close();
					ps = null;
				}
			} catch (Exception e) {
				System.out.println("TestLi.update:" + e.toString());

			}
		}
		return strRes;
	}

	public String delete(Object obj, Connection tempConn) throws SQLException {
		String strRes = "";
		PreparedStatement ps = null;
		try {

			SysParaConfigureVO tempVO = (SysParaConfigureVO) obj;

			String strPkValue = "";

			String strSysParaConfigureSnValue = tempVO.getSysParaConfigureSn(); /* 参数序号 */

			ps = tempConn.prepareStatement(this.DELETE);

			ps.setString(1, tempVO.getSysParaConfigureSn()); /* 参数序号 */

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			System.out.println("XtcsglSysParaConfigureDAO.delete:" + e);
			throw new SQLException(e.toString());
		} finally {

			try {
				if (ps != null) {
					ps.close();
					ps = null;
				}
			} catch (Exception e) {
				System.out.println("XtcsglSysParaConfigureDAO.delete:"
						+ e.toString());

			}
		}
		return strRes;

	}

}
