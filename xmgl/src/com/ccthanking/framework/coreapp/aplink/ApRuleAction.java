package com.ccthanking.framework.coreapp.aplink;

import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.dom4j.*;
import org.dom4j.io.*;
import java.io.OutputStream;
import javax.servlet.ServletConfig;
import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import org.dom4j.Element;
import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.Constants;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager;
import com.ccthanking.framework.log.LogManager;

public class ApRuleAction
    extends BaseDispatchAction
{

  public ApRuleAction()
  {
  }

  public ActionForward getApRuleList(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws
      Exception
  {
    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    Document doc = RequestUtil.getDocument(request);
    QueryConditionList list = RequestUtil.getConditionList(doc);
    PageManager page = RequestUtil.getPageManager(doc);
    String condition = list == null ? "" : list.getConditionWhere();
    if (Pub.empty(condition))
      condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
    if (page == null)
      page = new PageManager();
    page.setFilter(condition);
    Document domresult = null;
    OutputStream os = null;
    Connection conn = DBUtil.getConnection();
    try {
      conn.setAutoCommit(false);
      String sql =
          "select rule_name,ywlx,operation_oid,unitid,pcsdm,fjdm,sjdm,ssxq,"
          +"value1,value2,value3,memo,rule_oid from approve_rule ";
      BaseResultSet bs = DBUtil.query(conn, sql,
          page);
      bs.setFieldDic("ywlx", "YWLX");
      bs.setFieldOrgDept("unitid");
      bs.setFieldOrgDept("pcsdm");
      bs.setFieldOrgDept("fjdm");
      bs.setFieldOrgDept("sjdm");
      bs.setFieldDic("ssxq","ssxq");
      domresult = bs.getDocument();
      os = response.getOutputStream();
      XMLWriter writer = new XMLWriter(os);
      writer.write(domresult);
      os.flush();
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
      domresult = WriteXmlMessage(domresult, "意外错误！！" + e.toString(),
                                  "ERRMESSAGE");
      if (os == null) {
        os = response.getOutputStream();
      }
      XMLWriter writer = new XMLWriter(os);
      writer.write(domresult);
      os.flush();
    }
    finally {
      if (conn != null)
        conn.close();
    }
    return null;
  }

  public ActionForward doSaveApRule(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws
      Exception
  {
    String ywlx = Pub.val(request, "ywlx");
    String opid = Pub.val(request, "opid");
    String dwdm = Pub.val(request, "dwdm");
    boolean isNew = Pub.empty(opid);
    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    Document doc = RequestUtil.getDocument(request);
    Document domresult = null;
    OutputStream os = null;
    Connection conn = DBUtil.getConnection();
    conn.setAutoCommit(false);
    try
    {
      Element root = doc.getRootElement();
      String rule_oid = root.element("RULE_OID").getText();
      if(!Pub.empty(rule_oid) && !rule_oid.equals(opid))
        throw new Exception("error get rule_oid");
      if(Pub.empty(rule_oid)) rule_oid = DBUtil.getSequenceValue("AP_TASK_S",conn);
      dwdm = root.element("UNITID") == null ? null : root.element("UNITID").getText();
      String pcsdm = root.element("PCSDM") == null ? null : root.element("PCSDM").getText();
      String rule_name = root.element("RULE_NAME") == null ? null :
          root.element("RULE_NAME").getText();
      String fjdm = root.element("FJDM") == null ? null :
          root.element("FJDM").getText();
      String sjdm = root.element("SJDM") == null ? null :
          root.element("SJDM").getText();
      String ssxq = root.element("SSXQ") == null ? null :
          root.element("SSXQ").getText();
      String value1 = root.element("VALUE1") == null ? null :
          root.element("VALUE1").getText();
      String value2 = root.element("VALUE2") == null ? null :
          root.element("VALUE2").getText();
      String value3 = root.element("VALUE3") == null ? null :
          root.element("VALUE3").getText();
      String memo = root.element("MEMO") == null ? null :
          root.element("MEMO").getText();
      String operation_oid = root.element("OPERATION_OID") == null ? null :
          root.element("OPERATION_OID").getText();
      String sql = null;
      String str = null;
      if(isNew)
      {
        sql = "insert into approve_rule cols(unitid,ywlx,pcsdm,fjdm,sjdm,"
            +"ssxq,value1,value2,value3,operation_oid,rule_name,memo,rule_oid) "
            + " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        str = "添加";
      }
      else
      {
        sql = "update approve_rule set unitid=?,ywlx=?,pcsdm=?,fjdm=?,sjdm=?,"
            +"ssxq=?,value1=?,value2=?,value3=?,operation_oid=?,rule_name=?,"
            +"memo=? where rule_oid=?";
        str = "修改";
      }
      Object[] para = {dwdm,ywlx,pcsdm,fjdm,sjdm,ssxq,value1,value2,value3,operation_oid,rule_name,memo,rule_oid};
      DBUtil.executeUpdate(conn, sql, para);
      conn.commit();
      if(isNew)
        LogManager.writeUpdateLog("规则管理->"+str+"规则","业务类型："+ywlx+" 部门:"+dwdm+" ID:"+rule_oid,str,LogManager.RESULT_SUCCESS,user);
      else
        LogManager.writeUpdateLog("规则管理->"+str+"规则",str,"业务类型："+ywlx+" 部门:"+dwdm+" ID:"+rule_oid,LogManager.RESULT_SUCCESS,user);
      domresult = WriteXmlMessage(domresult,"保存成功！","MESSAGE");
      os = response.getOutputStream();
      XMLWriter writer = new XMLWriter(os);
      writer.write(domresult);
      os.flush();
    }
    catch (Exception e)
    {
      if(conn != null) conn.rollback();
      domresult = WriteXmlMessage(domresult, "意外错误！\n" + e.getMessage(),
                                  "ERRMESSAGE");
      if (os == null) {
        os = response.getOutputStream();
      }
      XMLWriter writer = new XMLWriter(os);
      writer.write(domresult);
      os.flush();
    }
    finally {
      if (conn != null)
        conn.close();
    }
    return null;
  }

  public ActionForward doDeleteApRule(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
  {
    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    Document doc = RequestUtil.getDocument(request);
    Element root = doc.getRootElement();
    String ywlx = root.selectSingleNode("YWLX").getStringValue();
    String dwdm = root.selectSingleNode("UNITID").getStringValue();
    String opid = root.selectSingleNode("RULE_OID").getStringValue();
    OutputStream os = null;
    String res = null;
    Connection conn = DBUtil.getConnection();
    try
    {
      conn.setAutoCommit(false);
      DBUtil.execSql(conn,"delete approve_rule where RULE_OID='"+opid+"'");
      conn.commit();
      LogManager.writeUpdateLog("规则管理->删除规则","业务类型："+ywlx+" 部门:"+dwdm+" ID:"+opid,"删除",LogManager.RESULT_SUCCESS,user);
      doc = WriteXmlMessage(null, "规则已经删除！", "MESSAGE");
    }
    catch(Exception e)
    {
      conn.rollback();
      LogManager.writeUpdateLog("规则管理->删除规则","业务类型："+ywlx+" 部门:"+dwdm+" ID:"+opid,"删除",LogManager.RESULT_FAILURE,user);
      doc = WriteXmlMessage(null, "发生异常！\n" + res, "ERRMESSAGE");
    }
    finally
    {
      if(conn != null) conn.close();
      os = response.getOutputStream();
      XMLWriter writer = new XMLWriter(os);
      writer.write(doc);
      os.flush();
    }
    return null;
  }

  public Document WriteXmlMessage(Document doc, String strMessage,
                                  String strTagName)
  {
    try {
      Element root = null;
      if (doc == null) {
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
      else {
        root = doc.getRootElement();
      }
      if (root != null && strMessage != null && strTagName != null) {
        root.addElement(strTagName).addText(strMessage);
      }
    }
    catch (Exception e) {
      System.out.println("ZqzCzrkZqzEventAction.WriteXmlMessage(Document doc,String strMessage,String strTagName):" +
                         e.toString());
    }
    return doc;
  }
}
