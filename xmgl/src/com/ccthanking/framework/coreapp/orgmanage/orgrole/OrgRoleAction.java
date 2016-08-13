package com.ccthanking.framework.coreapp.orgmanage.orgrole;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

import com.ccthanking.framework.Constants;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.*;
import com.ccthanking.framework.common.*;
import com.ccthanking.framework.common.cache.*;
import com.ccthanking.framework.coreapp.orgmanage.OrgRoleManager;
import com.ccthanking.framework.util.*;

import org.dom4j.*;
import java.io.*;
import org.dom4j.io.SAXReader;

public class OrgRoleAction
    extends BaseDispatchAction
{
    public OrgRoleAction()
    {
    }
    private static org.apache.log4j.Logger logger = org.apache.log4j.LogManager.
        getLogger("SysParaConfigureAction");
    public ActionForward insert(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
        throws Exception
    {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        HashMap tempData = new HashMap();
        insertParas(request, response, tempData);
        
		String checksql = "SELECT name FROM FS_org_role WHERE name = ?";
		Object[] paras = { tempData.get("NAME")};
		String[][] res = DBUtil.querySql(checksql, paras);
		if (res != null && res.length > 0){
			Pub.writeXmlInfoMessage(response,  "权限名称已存在，请重新添加！");
			return null;
		}
		//~

        RoleVO tempVO = new RoleVO();
        tempVO.setName( (String) tempData.get("NAME"));
        tempVO.setMemo( (String) tempData.get("S_MEMO"));
        tempVO.setLevelName( (String) tempData.get("LEVEL_NAME"));
        String strDeptId = user.getDepartment();
        tempVO.setDeptId(strDeptId);
        try
        {
            OrgRoleBO.insert(tempVO, request);
            Pub.writeXmlInfoSaveOK(response);
        }
        catch (Exception e)
        {
            logger.error(e);
            Pub.writeXmlErrorMessage(response, e.getMessage());
        }
        return null;
    }

    public ActionForward update(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
        throws Exception
    {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        HashMap tempData = new HashMap();
        insertParas(request, response, tempData);

        Role tempVO = OrgRoleManager.getInstance().getRole((String) tempData.get("HIDDENNAME"));
        tempVO.setMemo( (String) tempData.get("S_MEMO"));
        tempVO.setLevelName( (String) tempData.get("LEVEL_NAME"));
        /**
         * 暂不进行deptid的修改，防止更高级别部门人员修改后，原创建者无法查询到该角色
        String strDeptId = user.getDepartment();
        tempVO.setDeptId(strDeptId);
        **/
        try
        {
            OrgRoleBO.update(tempVO, request);
            Pub.writeXmlInfoSaveOK(response);
        }
        catch (Exception e)
        {
            logger.error(e);
            Pub.writeXmlErrorMessage(response, e.getMessage());
        }
        return null;
    }

//删除角色，同时删除与此角色映射的菜单和人员
    public ActionForward delete(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
        throws Exception
    {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        HashMap tempData = new HashMap();
        insertParas(request, response, tempData);
        String roleName = (String) tempData.get("NAME");
        Connection conn = DBUtil.getConnection();
        boolean res = true;
        try
        {
            conn.setAutoCommit(false);
            String sql = "delete FS_org_role_psn_map where role_name='" + roleName + "'";
            res = res & DBUtil.exec(conn, sql);
            sql = "delete FS_org_role_menu_map where role_name='" + roleName + "'";
            res = res & DBUtil.exec(conn, sql);
            sql = "delete FS_org_role where name='" + roleName + "'";
            res = res & DBUtil.exec(conn, sql);
            logger.info("角色 [" + roleName + "] 删除...");
            if(res) conn.commit();
            else throw new Exception("");
            CacheManager.broadcastChanges(CacheManager.CACHE_ROLE, roleName,
                                          CacheManager.DELETE);
            Pub.writeXmlInfoMessage(response, "操作成功！");
        }
        catch (Exception e)
        {
            conn.rollback();
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
        }
        finally
        {
            if (conn != null)
                conn.close();
            conn = null;
        }
        return null;
    }

    public ActionForward queryList(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
        throws Exception
    {
    	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition)){
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
        }
        condition += " and DEPTID like '"+ user.getOrgDept().getSsxq() + "%' ";
        condition += "  order by  NAME ";
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Connection conn = DBUtil.getConnection();
        try
        {
            conn.setAutoCommit(false);
            String sql = "select NAME,Name as HiddenName,LEVEL_NAME,DEPTID,S_MEMO from FS_ORG_ROLE ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            //bs.setFieldDic("LEVEL_NAME", "BMJB");
            bs.setFieldOrgDept("DEPTID");
            ResponseUtil.writeDocumentXml(response, bs.getDocument());
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, "意外错误!");
        }
        finally
        {
            if (conn != null)
                conn.close();
        }
        return null;
    }

    public void insertParas(HttpServletRequest request,
                            HttpServletResponse response, HashMap data)
        throws
        Exception
    {
        SAXReader reader = new SAXReader();
        InputStream in = request.getInputStream();
        Document doc = reader.read(in);

        List queryRoot = doc.selectNodes("//DATAINFO/ROW/*");
        for (int i = 0; i < queryRoot.size(); i++)
        {
            Element ele = (Element) queryRoot.get(i);
            if (ele.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            String nodeName = ele.getName();
            Object nodeValue = ele.getData();
            if (nodeName != null && !"".equals(nodeName))
            {
                data.put(nodeName, nodeValue);
            }
        }
        return;
    }

}