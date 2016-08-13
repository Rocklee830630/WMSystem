package com.ccthanking.framework.params.UserPara;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;

public class UserParaConfigureDAO extends BaseDAO { /* 用户参数配置表 */
	private String INSERT = "";

	private String INSERT_LIST = "INSERT INTO USER_PARA_CONFIGURE(SN,APPTYPE,USERACCOUNT,USERNAME,USERID,USERLEVEL,APPLICATION,PARAKEY,PARANAME,PARAVALUE1,PARAVALUE2,PARAVALUE3,PARAVALUE4,MEMO";

	private String INSERT_VALUES = ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?";

	private String UPDATE = "UPDATE USER_PARA_CONFIGURE SET SN=? , APPTYPE=? , USERACCOUNT=? , USERNAME=? , USERID=? , USERLEVEL=? , APPLICATION=? , PARAKEY=? , PARANAME=? , PARAVALUE1=? , PARAVALUE2=? , PARAVALUE3=? , PARAVALUE4=? , MEMO=?";

	private String UPDATE_WHERE = " WHERE SN=?";

	private String DELETE = "DELETE  FROM USER_PARA_CONFIGURE WHERE SN=?";

	private String SELECT_BY_ID = "select SN,APPTYPE,USERACCOUNT,USERNAME,USERID,USERLEVEL,APPLICATION,PARAKEY,PARANAME,PARAVALUE1,PARAVALUE2,PARAVALUE3,PARAVALUE4,MEMO FROM USER_PARA_CONFIGURE WHERE SN=?";
	public Object selectById(String pk,Connection tempConn)throws SQLException {
    	PreparedStatement ps = null;
		ResultSet rs = null;
		UserParaConfigureVO  tempVO=null;
		try
		{
			ps = tempConn.prepareStatement(this.SELECT_BY_ID);
			ps.setString(1, pk);
			rs = ps.executeQuery();
			if(rs.next())
			{
				tempVO = new UserParaConfigureVO();
				tempVO.setUserParaConfigureSn(rs.getString(1));
				tempVO.setUserParaConfigureApptype(rs.getString(2));
				tempVO.setUserParaConfigureUseraccount(rs.getString(3));
				tempVO.setUserParaConfigureUsername(rs.getString(4));
				tempVO.setUserParaConfigureUserid(rs.getString(5));
				tempVO.setUserParaConfigureUserlevel(rs.getString(6));
				tempVO.setUserParaConfigureApplication(rs.getString(7));
				tempVO.setUserParaConfigureParakey(rs.getString(8));
				tempVO.setUserParaConfigureParaname(rs.getString(9));
				tempVO.setUserParaConfigureParavalue1(rs.getString(10));
				tempVO.setUserParaConfigureParavalue2(rs.getString(11));
				tempVO.setUserParaConfigureParavalue3(rs.getString(12));
				tempVO.setUserParaConfigureParavalue4(rs.getString(13));
				tempVO.setUserParaConfigureMemo(rs.getString(14));
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
		String strRes = "";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			UserParaConfigureVO tempVO = (UserParaConfigureVO) obj;

			ps= tempConn.prepareStatement("SELECT USERACCOUNT,PARAKEY FROM USER_PARA_CONFIGURE WHERE USERACCOUNT=? AND PARAKEY=?");
			ps.setString(1, tempVO.getUserParaConfigureUseraccount());
			ps.setString(2, tempVO.getUserParaConfigureParakey());
			rs = ps.executeQuery();
			if(rs.next())
			{
				return "主键重复";
			}


			this.INSERT = this.INSERT_LIST + this.INSERT_VALUES + ")";
			ps = tempConn.prepareStatement(this.INSERT);
			ps.setString(1, tempVO.getUserParaConfigureSn()); /* 参数序号 */
			ps.setString(2, tempVO.getUserParaConfigureApptype()); /* 参数类型 */
			ps.setString(3, tempVO.getUserParaConfigureUseraccount()); /* 用户账号 */
			ps.setString(4, tempVO.getUserParaConfigureUsername()); /* 用户名称 */
			ps.setString(5, tempVO.getUserParaConfigureUserid()); /* 用户编号 */
			ps.setString(6, tempVO.getUserParaConfigureUserlevel()); /* 用户级别 */
			ps.setString(7, tempVO.getUserParaConfigureApplication()); /* 应用名 */
			ps.setString(8, tempVO.getUserParaConfigureParakey()); /* 参数key */
			ps.setString(9, tempVO.getUserParaConfigureParaname()); /* 参数名称 */
			ps.setString(10, tempVO.getUserParaConfigureParavalue1()); /* 参数值1 */
			ps.setString(11, tempVO.getUserParaConfigureParavalue2()); /* 参数值2 */
			ps.setString(12, tempVO.getUserParaConfigureParavalue3()); /* 参数值3 */
			ps.setString(13, tempVO.getUserParaConfigureParavalue4()); /* 参数值4 */
			ps.setString(14, tempVO.getUserParaConfigureMemo()); /* 备注 */

			ps.executeUpdate();
			ps.close();
			ps = null;
		} catch (Exception e) {
			System.out.println("YhcsglUserParaConfigureDAO.insert:"
					+ e.toString());
			strRes = "";//strMessage[0];

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
		return strRes;

	}

	public String update(Object obj, Connection tempConn) throws SQLException {
		String strRes = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			UserParaConfigureVO tempVO = (UserParaConfigureVO) obj;
			this.UPDATE = this.UPDATE + this.UPDATE_WHERE;

			ps = tempConn.prepareStatement(this.UPDATE);

			ps.setString(1, tempVO.getUserParaConfigureSn()); /* 参数序号 */

			ps.setString(2, tempVO.getUserParaConfigureApptype()); /* 参数类型 */

			ps.setString(3, tempVO.getUserParaConfigureUseraccount()); /* 用户账号 */

			ps.setString(4, tempVO.getUserParaConfigureUsername()); /* 用户名称 */

			ps.setString(5, tempVO.getUserParaConfigureUserid()); /* 用户编号 */

			ps.setString(6, tempVO.getUserParaConfigureUserlevel()); /* 用户级别 */

			ps.setString(7, tempVO.getUserParaConfigureApplication()); /* 应用名 */

			ps.setString(8, tempVO.getUserParaConfigureParakey()); /* 参数key */

			ps.setString(9, tempVO.getUserParaConfigureParaname()); /* 参数名称 */

			ps.setString(10, tempVO.getUserParaConfigureParavalue1()); /* 参数值1 */

			ps.setString(11, tempVO.getUserParaConfigureParavalue2()); /* 参数值2 */

			ps.setString(12, tempVO.getUserParaConfigureParavalue3()); /* 参数值3 */

			ps.setString(13, tempVO.getUserParaConfigureParavalue4()); /* 参数值4 */

			ps.setString(14, tempVO.getUserParaConfigureMemo()); /* 备注 */

			ps.setString(15, tempVO.getUserParaConfigureSn()); /* 参数序号 */

			ps.executeUpdate();
			ps.close();
			ps = null;
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
			UserParaConfigureVO tempVO = (UserParaConfigureVO) obj;
			ps = tempConn.prepareStatement(this.DELETE);
			ps.setString(1, tempVO.getUserParaConfigureSn()); /* 参数序号 */
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			System.out.println("YhcsglUserParaConfigureDAO.delete:" + e);
			throw new SQLException(e.toString());
		} finally {

			try {
				if (ps != null) {
					ps.close();
					ps = null;
				}
			} catch (Exception e) {
				System.out.println("YhcsglUserParaConfigureDAO.delete:"
						+ e.toString());

			}
		}
		return strRes;

	}

}
