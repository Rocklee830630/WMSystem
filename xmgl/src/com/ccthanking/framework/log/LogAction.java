package com.ccthanking.framework.log;

import java.util.*;
import java.lang.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.ccthanking.framework.Constants;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.*;
import com.ccthanking.framework.common.*;
import com.ccthanking.framework.dic.*;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.util.*;

import org.dom4j.*;

import java.sql.Connection;


import java.io.OutputStream;

public class LogAction
    extends BaseDispatchAction
{

    public LogAction()
    {
    }

    public ActionForward getOperationLogList(ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
        throws
        Exception
    {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc); //查询条件列表
        PageManager page = RequestUtil.getPageManager(doc); //页面条件列表
        String orderFilter = RequestUtil.getOrderFilter(doc); // 排序过滤
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
        condition += orderFilter;
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection("p3");
        try {
            conn.setAutoCommit(false);
            String sql = "select userid,username,userdeptid,operatetime,ywlx,OPERATETYPE,result,loginid,sjbh,month,memo,operateip from log_useroperation ";
            BaseResultSet bs = DBUtil.query(conn, sql, page);
            bs.setFieldDateFormat("operatetime", "yyyy-MM-dd HH:mm:ss");
            bs.setFieldDic("result", "DLZT");
            bs.setFieldOrgDept("userdeptid");
            bs.setFieldDic("ywlx", "ywlx");
            bs.setFieldDic("OPERATETYPE", "CZLX");
            domresult = bs.getDocument();
            Pub.writeXmlDocument(response, domresult);
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
        }
        finally {
            if (conn != null)
                conn.close();
        }
        return null;
    }

    public ActionForward getUpdateLogList(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response)
        throws
        Exception
    {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
        condition += RequestUtil.getOrderFilter(doc);
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection("p3");
        try {
            conn.setAutoCommit(false);
            String sql = "select loginid,userid,username,deptid,operatetime,datatype,beforevalue,aftervalue,result,operateip from log_sysdataupdate ";
            BaseResultSet bs = DBUtil.query(conn, sql,
                                            page);
            bs.setFieldDateFormat("operatetime", "yyyy-MM-dd HH:mm:ss");
            bs.setFieldDic("result", "DLZT");
            bs.setFieldOrgDept("deptid");
            domresult = bs.getDocument();
            Pub.writeXmlDocument(response, domresult);
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
        }
        finally {
            if (conn != null)
                conn.close();
        }
        return null;
    }

    public ActionForward getLoginLogList(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
        throws
        Exception
    {
        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String orderFilter = RequestUtil.getOrderFilter(doc);
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
        condition += orderFilter;
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection("p3");
        try {
            conn.setAutoCommit(false);
            String sql = "select loginid,userid,uername,userdeptid,loginip,logintime,loginstatus,logouttime,month from log_userlogin ";
            BaseResultSet bs = DBUtil.query(conn, sql,
                                            page);
            bs.setFieldDateFormat("logintime", "yyyy-MM-dd HH:mm:ss");
            bs.setFieldDateFormat("logouttime", "yyyy-MM-dd HH:mm:ss");
            bs.setFieldDic("loginstatus", "DLZT");
            bs.setFieldOrgDept("userdeptid");
            domresult = bs.getDocument();
            Pub.writeXmlDocument(response, domresult, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
        }
        finally {
            if (conn != null)
                conn.close();
        }
        return null;
    }

    public ActionForward getSysLogList(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request,
                                       HttpServletResponse response)
        throws
        Exception
    {
        Document doc = RequestUtil.getDocument(request);
        QueryConditionList list = RequestUtil.getConditionList(doc);
        PageManager page = RequestUtil.getPageManager(doc);
        String condition = list == null ? "" : list.getConditionWhere();
        if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
        condition += RequestUtil.getOrderFilter(doc);
        if (page == null)
            page = new PageManager();
        page.setFilter(condition);
        Document domresult = null;
        OutputStream os = null;
        Connection conn = DBUtil.getConnection("p3");
        try {
            conn.setAutoCommit(false);
            String sql =
                "select opid,operatetime,moduleid,memo,result from log_syslog ";
            BaseResultSet bs = DBUtil.query(conn, sql,
                                            page);
            bs.setFieldDateFormat("operatetime", "yyyy-MM-dd HH:mm:ss");
            bs.setFieldDic("result", "DLZT");
            domresult = bs.getDocument();
            Pub.writeXmlDocument(response, domresult);
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
        }
        finally {
            if (conn != null)
                conn.close();
        }
        return null;
    }
}
