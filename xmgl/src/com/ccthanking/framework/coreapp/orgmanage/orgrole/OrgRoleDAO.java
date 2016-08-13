package com.ccthanking.framework.coreapp.orgmanage.orgrole;

import java.sql.*;

import com.ccthanking.framework.base.*;
import com.ccthanking.framework.common.*;

import org.dom4j.*;

public class OrgRoleDAO
    extends BaseDAO
{
    public static void insert(Object obj, Connection tempConn)
        throws Exception
    {
        PreparedStatement ps = null;
        try
        {
            RoleVO tempVO = (RoleVO) obj;
            String INSERT = "insert into FS_org_role cols(name,s_memo,level_name,deptid) "
                +"values(?,?,?,?)";
            ps = tempConn.prepareStatement(INSERT);
            ps.setString(1, tempVO.getName()); /* 角色名称 */
            ps.setString(2, tempVO.getMemo()); /* 备注 */
            ps.setString(3, tempVO.getLevelName()); /* 应用名称 */
            ps.setString(4, tempVO.getDeptId()); /* 创建者部门代码 */
            ps.executeUpdate();
            ps.close();
            ps = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            if(ps != null) ps.close();
            ps = null;
        }
    }

    public static void update(Object obj, Connection tempConn)
        throws Exception
    {
        PreparedStatement ps = null;
        try
        {
            RoleVO tempVO = (RoleVO) obj;
            String UPDATE = "update FS_org_role set name=?,s_memo=?,level_name=?,deptid=? where name=?";
            ps = tempConn.prepareStatement(UPDATE);
            ps.setString(1, tempVO.getName()); /* 角色名称 */
            ps.setString(2, tempVO.getMemo()); /* 备注 */
            ps.setString(3, tempVO.getLevelName()); /* 应用名称 */
            ps.setString(4, tempVO.getDeptId()); /* 级别名称 */
            ps.setString(5, tempVO.getName()); /* 角色标示 */
            ps.executeUpdate();
            ps.close();
            ps = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            if(ps != null) ps.close();
            ps = null;
        }
    }
}
