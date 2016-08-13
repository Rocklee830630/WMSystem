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
import com.ccthanking.framework.common.OrgDept;
/**
 * @author leo leochou  oss@tom.com
 * @version 1.0
 */
public class ApAction
    extends BaseDispatchAction
{

  public ApAction()
  {
  }

  public ActionForward getApTaskList(ActionMapping mapping, ActionForm form,
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
    condition += " order by eventtype ";
    if (page == null)
      page = new PageManager();
    page.setFilter(condition);
    Document domresult = null;
    OutputStream os = null;
    Connection conn = DBUtil.getConnection();
    try {
      conn.setAutoCommit(false);
      String sql =
          "select memo,eventtype,operationoid,dwdm,fjdm,sjdm,(select PROCESSTYPE from AP_PROCESSTYPE where AP_PROCESSTYPE.OPERATIONOID = ap_tasks.operationoid) as processtype  from ap_tasks ";
      BaseResultSet bs = DBUtil.query(conn, sql,
          page);
      bs.setFieldDic("eventtype", "YWLX");
      bs.setFieldDic("processtype","LCLX");
      //bs.setFieldOrgDeptSimpleName("dwdm");
      bs.setFieldOrgDept("dwdm");
      bs.setFieldOrgDept("fjdm");
      bs.setFieldOrgDept("sjdm");
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

  public ActionForward createApTask(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws
      Exception
  {
    String ywlx = Pub.val(request, "ywlx");
    String opid = Pub.val(request, "opid");
    String dwdm = Pub.val(request, "dwdm");
    int steps = Pub.toInt(Pub.val(request, "steps"));
    boolean isNew = Pub.empty(opid);
    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    Document doc = RequestUtil.getDocument(request);
    Document domresult = null;
    OutputStream os = null;
    Connection conn = DBUtil.getConnection();
    conn.setAutoCommit(false);
    try
    {
      switch (steps)
      {
        case 1:
        {
          QueryConditionList list = RequestUtil.getConditionList(doc);
          PageManager page = RequestUtil.getPageManager(doc);
          String condition = list == null ? "" : list.getConditionWhere();
          if (Pub.empty(condition))
            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
          condition += " order by eventtype ";
          if (page == null)
            page = new PageManager();
          page.setFilter(condition);
          String sql =
              "select memo,eventtype,operationoid,dwdm,fjdm,sjdm from ap_tasks ";
          BaseResultSet bs = DBUtil.query(conn,
              sql, page);
          bs.setFieldDic("eventtype", "YWLX");
          bs.setFieldOrgDept("dwdm");
          bs.setFieldOrgDept("fjdm");
          bs.setFieldOrgDept("sjdm");
          domresult = bs.getDocument();
          List rows = domresult.selectNodes("//RESPONSE/RESULT/ROW");
          if(rows != null && rows.size() > 0)
          {
            if(isNew) throw new Exception("流程已经存在，不可以重复添加！\n请修改该流程，或删除后重新配置！");
            else
            {
              Element row = (Element) rows.get(0);
              opid = row.selectSingleNode("OPERATIONOID").getStringValue();
              sql = "select processtypeoid,name,operationoid,actor,createtime,"
                  + "state,precondition1,precondition2,precondition3,"
                  + "memo,processevent,processtype from ap_processtype where OPERATIONOID='"+opid+"'"
                  + " and state='1' order by to_number(PRECONDITION1)";
              //String[][] appt = DBUtil.querySql(conn,sql);
              bs = DBUtil.query(conn,sql,(PageManager)null);
              bs.setFieldDateFormat("createtime","yyyy-MM-dd HH:mm:ss");
              Document apdoc = bs.getDocument();
              rows = apdoc.selectNodes("//RESPONSE/RESULT/ROW");
              if(rows == null || rows.size()==0) break;
              Element aptype = row.addElement("PROCESSTYPE");
              for(int i=0;i<rows.size();i++)
              {
                Element aprow = (Element) rows.get(i);
                String pid = aprow.element("PROCESSTYPEOID").getStringValue();
                sql = "select processtypeoid,stepsequence,stepoid,name,rolename,"
                    +"actor,state,precondition2,precondition3,precondition1,"
                    +"prestepoid,nextstepoid,deptlevel,application,memo,"
                    +"deptid,stepevent from ap_processstep where PROCESSTYPEOID='"+pid+"'"
                    +" and state='1' order by STEPSEQUENCE";
                bs = DBUtil.query(conn,sql,(PageManager)null);
                bs.setFieldDic("deptlevel","BMJB");
                Document psdoc = bs.getDocument();
                List psrows = psdoc.selectNodes("//RESPONSE/RESULT/ROW");
                if(psrows != null && psrows.size()>0)
                {
                  Element sroot = aprow.addElement("STEPS");
                  for (int j = 0; j < psrows.size(); j++)
                  {
                    Element step = (Element) psrows.get(j);
                    sroot.add(step.createCopy());
                  }
                }
                aptype.add(aprow.createCopy());
              }
            }
          }
          break;
        }
        default:
          break;
      }
      //processtypeoid,name,operationoid,actor,createtime,state,precondition1,precondition2,precondition3,memo,processevent
      String sql = "select distinct processtypeoid,procname name,'"+opid+"' operationoid,actor,"
          +"'' createtime,state,precondition1,precondition2,precondition3,memo,"
          +"'' processevent from ap_process_templete order by processtypeoid";
      BaseResultSet bs = DBUtil.query(conn,sql,(PageManager)null);
      Document tmdoc = bs.getDocument();
      Element resultRoot = null;
      if(domresult == null)
      {
        domresult = DocumentFactory.getInstance().createDocument();
        Element root = domresult.addElement("RESPONSE");
        root.addAttribute("errorcode", "0");
        root.addAttribute("code", "0");
        resultRoot = root.addElement("RESULT");
      }
      List rows = tmdoc.selectNodes("//RESPONSE/RESULT/ROW");
      resultRoot = resultRoot==null?(Element) domresult.selectSingleNode("//RESPONSE/RESULT"):resultRoot;
      Element root = resultRoot.addElement("TEMPLETES");
      if(rows != null)
      {
        for(int i=0;i<rows.size();i++)
        {
          Element templete = (Element) rows.get(i);
          sql = "select processtypeoid,stepsequence,stepoid,stepname name,"
              +"rolename,actor,state,precondition2,precondition3,precondition1,"
              +"prestepoid,nextstepoid,deptlevel,application,memo,deptid,stepevent"
              +" from ap_process_templete where processtypeoid='"
              +templete.selectSingleNode("processtypeoid".toUpperCase()).getStringValue()+"'";
          Document stepsintempletedoc = DBUtil.query(conn,sql,(PageManager)null).getDocument();
          List steplist = stepsintempletedoc.selectNodes("//RESPONSE/RESULT/ROW");
          if(steplist != null)
          {
            Element stepelement = templete.addElement("STEPS");
            for(int j=0;j<steplist.size();j++)
            {
              Element step = (Element) steplist.get(j);
              stepelement.add(step.createCopy());
            }
          }
          root.add(templete.createCopy());
        }
      }
      sql = "select stepoid,name,rolename,deptlevel,deptid,actor,memo from ap_step";
      Document stepsDoc = DBUtil.query(conn,sql,(PageManager)null).getDocument();
      rows = stepsDoc.selectNodes("//RESPONSE/RESULT/ROW");
      root = resultRoot.addElement("ALLSTEPS");
      for(int i=0;i<rows.size();i++)
      {
        Element step = (Element) rows.get(i);
        root.add(step.createCopy());
      }
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
  public ActionForward createApProcess(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws
      Exception
  {
    String processoid = Pub.val(request, "processoid");
    int steps = 1;
    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    Document doc = RequestUtil.getDocument(request);
    Document domresult = null;
    OutputStream os = null;
    Connection conn = DBUtil.getConnection();
    conn.setAutoCommit(false);
    try
    {
      switch (steps)
      {
        case 1:
        {
             String  sql = "select processtypeoid,name,operationoid,actor,createtime,"
                  + "state,precondition1,precondition2,precondition3,"
                  + "memo,processevent,processtype from ap_processtype where PROCESSTYPEOID='"+processoid+"'"
                  + " and state='1' order by to_number(PRECONDITION1)";
              //String[][] appt = DBUtil.querySql(conn,sql);
              BaseResultSet bs = DBUtil.query(conn,sql,(PageManager)null);
              bs.setFieldDateFormat("createtime","yyyy-MM-dd HH:mm:ss");
              domresult = bs.getDocument();
              List rows = domresult.selectNodes("//RESPONSE/RESULT/ROW");
              if(rows == null || rows.size()==0) break;
              Element row = (Element) rows.get(0);
              Element aptype = row.addElement("PROCESSTYPE");
              for(int i=0;i<rows.size();i++)
              {
                Element aprow = (Element) rows.get(i);
                String pid = aprow.element("PROCESSTYPEOID").getStringValue();
                sql = "select processtypeoid,stepsequence,stepoid,name,rolename,"
                    +"actor,state,precondition2,precondition3,precondition1,"
                    +"prestepoid,nextstepoid,deptlevel,application,memo,"
                    +"deptid,stepevent from ap_processstep where PROCESSTYPEOID='"+pid+"'"
                    +" and state='1' order by STEPSEQUENCE";
                bs = DBUtil.query(conn,sql,(PageManager)null);
                bs.setFieldDic("deptlevel","BMJB");
                Document psdoc = bs.getDocument();
                List psrows = psdoc.selectNodes("//RESPONSE/RESULT/ROW");
                if(psrows != null && psrows.size()>0)
                {
                  Element sroot = aprow.addElement("STEPS");
                  for (int j = 0; j < psrows.size(); j++)
                  {
                    Element step = (Element) psrows.get(j);
                    sroot.add(step.createCopy());
                  }
                }
                aptype.add(aprow.createCopy());
              }
          break;
        }
        default:
          break;
      }
      //processtypeoid,name,operationoid,actor,createtime,state,precondition1,precondition2,precondition3,memo,processevent
      String sql = "select distinct processtypeoid,procname name,'"+processoid+"' operationoid,actor,"
          +"'' createtime,state,precondition1,precondition2,precondition3,memo,"
          +"'' processevent from ap_process_templete order by processtypeoid";
      BaseResultSet bs = DBUtil.query(conn,sql,(PageManager)null);
      Document tmdoc = bs.getDocument();
      Element resultRoot = null;
      if(domresult == null)
      {
        domresult = DocumentFactory.getInstance().createDocument();
        Element root = domresult.addElement("RESPONSE");
        root.addAttribute("errorcode", "0");
        root.addAttribute("code", "0");
        resultRoot = root.addElement("RESULT");
      }
      List rows = tmdoc.selectNodes("//RESPONSE/RESULT/ROW");
      resultRoot = resultRoot==null?(Element) domresult.selectSingleNode("//RESPONSE/RESULT"):resultRoot;
      Element root = resultRoot.addElement("TEMPLETES");
      if(rows != null)
      {
        for(int i=0;i<rows.size();i++)
        {
          Element templete = (Element) rows.get(i);
          sql = "select processtypeoid,stepsequence,stepoid,stepname name,"
              +"rolename,actor,state,precondition2,precondition3,precondition1,"
              +"prestepoid,nextstepoid,deptlevel,application,memo,deptid,stepevent"
              +" from ap_process_templete where processtypeoid='"
              +templete.selectSingleNode("processtypeoid".toUpperCase()).getStringValue()+"'";
          Document stepsintempletedoc = DBUtil.query(conn,sql,(PageManager)null).getDocument();
          List steplist = stepsintempletedoc.selectNodes("//RESPONSE/RESULT/ROW");
          if(steplist != null)
          {
            Element stepelement = templete.addElement("STEPS");
            for(int j=0;j<steplist.size();j++)
            {
              Element step = (Element) steplist.get(j);
              stepelement.add(step.createCopy());
            }
          }
          root.add(templete.createCopy());
        }
      }
      sql = "select stepoid,name,rolename,deptlevel,deptid,actor,memo from ap_step";
      Document stepsDoc = DBUtil.query(conn,sql,(PageManager)null).getDocument();
      rows = stepsDoc.selectNodes("//RESPONSE/RESULT/ROW");
      root = resultRoot.addElement("ALLSTEPS");
      for(int i=0;i<rows.size();i++)
      {
        Element step = (Element) rows.get(i);
        root.add(step.createCopy());
      }
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
  /**
   * 处理业务类型对应文书
   * @param ywlx
   * @param dwbh
   * @param wsTemplate
   * @param conn
   */

  public void saveApproveWs(String ywlx,String dwbh,String wsTemplate,String wsTemplateChange,Connection conn)
        throws Exception
    {

      if("true".equals(wsTemplateChange)){
          String delete_sql = "delete  ws_operationtypes where  unitid ='"+dwbh+"' And ywlx='"+ywlx+"'";
          DBUtil.execSql(conn,delete_sql);
          if(!Pub.empty(wsTemplate)&&!"null".equals(wsTemplate)){
              String[] wsTemplates = wsTemplate.split(",");
              for (int i = 0; i < wsTemplates.length; i++)
              {
                  String insert_sql =
                      "insert into ws_operationtypes (UNITID,WS_TEMPLATE_ID,YWLX) values('" +
                      dwbh + "','" + wsTemplates[i] + "','" + ywlx + "')";
                  DBUtil.execSql(conn, insert_sql);
              }
          }


      }

  }
  public ActionForward saveApTask(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws
      Exception
  {
    String ywlx = Pub.val(request, "ywlx");
    String opid = Pub.val(request, "opid");
    String dwdm = Pub.val(request, "dwdm");
    String lclx = Pub.val(request,"lclx");
    String wsTemplate = Pub.val(request,"wsTemplate");
    String wsTemplateChange = Pub.val(request,"wsTemplateChange");
    int steps = Pub.toInt(Pub.val(request, "steps"));
    boolean isNew = Pub.empty(opid);
    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    Document doc = RequestUtil.getDocument(request);
    Document domresult = null;
    OutputStream os = null;
    Connection conn = DBUtil.getConnection();
    conn.setAutoCommit(false);
    try
    {

       saveApproveWs(ywlx,dwdm,wsTemplate,wsTemplateChange,conn);

      String sql = null;
      if(isNew)
      {
        opid = DBUtil.getSequenceValue("ap_task_s", conn);
        OrgDept dept = OrgDeptManager.getInstance().getDeptByID(dwdm);
        String fjdm = "",sjdm = "";
        int level = Pub.toInt(dept.getDeptType());
        OrgDept parent = dept;
        for(int i=level;i>0;i--)
        {
            if(parent == null )
                break;

          parent = parent.getParent();
          if(parent != null)
          {
            switch(Pub.toInt(parent.getDeptType()))
            {
              case 2:
                sjdm = parent.getDeptID();
                if(sjdm == null) sjdm = "";
                break;
              case 3:
                fjdm = parent.getDeptID();
                if(fjdm == null) fjdm = "";
                break;
              default:
                break;
            }
          }
        }
        String memo = dept.getDeptName()+Pub.getDictValueByCode("ywlx",ywlx)+"审批规则";
        sql = "insert into ap_tasks cols(operationoid,eventtype,memo,dwdm,fjdm,sjdm)"
           +" values('"+opid+"','"+ywlx+"','"+memo+"','"+dwdm+"','"+fjdm+"','"+sjdm+"')";
        DBUtil.execSql(conn,sql);
      }
      else
      {
        sql = "delete ap_processstep where PROCESSTYPEOID in (select PROCESSTYPEOID"
            +" from ap_processtype where OPERATIONOID='"+opid+"')";
        DBUtil.execSql(conn,sql);
        sql = "delete ap_processtype where OPERATIONOID='"+opid+"'";
        DBUtil.execSql(conn,sql);
      }
      List procs = doc.selectNodes("//RESPONSE/RESULT/ROW/PROCESSTYPE/ROW");
      if(procs != null)
      {
        for(int i=0;i<procs.size();i++)
        {
          Element proc = (Element) procs.get(i);
          String poid = DBUtil.getSequenceValue("ap_task_s",conn);
          String pname = proc.element("NAME").getText();
          String pactor = proc.element("ACTOR").getText();
          String pevent = proc.element("PROCESSEVENT").getText();
          sql = "insert into ap_processtype cols(processtypeoid,name,"
              +"operationoid,actor,createtime,state,precondition1,precondition2,"
              +"precondition3,memo,processevent,processtype) values(?,?,?,?,?,?,?,?,?,?,?,?)";
          Object[] para = {poid,pname,opid,pactor,Pub.getCurrentDate(),"1"
              ,""+i,""+i,""+i,pname,pevent,lclx};
          DBUtil.executeUpdate(conn,sql,para);
          List steplist = proc.element("STEPS").selectNodes("ROW");
          if(steplist == null) continue;
          String preoid = null,nextoid = null;
          for(int j=0;j<steplist.size();j++)
          {
            Element step = (Element) steplist.get(j);
            if(j < steplist.size() -1) nextoid = ((Element)steplist.get(j+1)).element("STEPOID").getText();
            else nextoid = null;
            String seq = ""+(j+1);
            String soid = step.element("STEPOID").getText();
            String sname = step.element("NAME").getText();
            String srole = step.element("ROLENAME").getText();
            String sactor = step.element("ACTOR").getText();
            String sdept = step.element("DEPTID").getText();
            String slevel = step.element("DEPTLEVEL").getText();
            String sevent = step.element("STEPEVENT")==null?"":step.element("STEPEVENT").getText();
            sql = "insert into ap_processstep cols(processtypeoid,stepsequence,"
                +"stepoid,name,rolename,actor,state,precondition2,precondition3,"
                +"precondition1,prestepoid,nextstepoid,deptlevel,application,"
                +"memo,deptid,stepevent) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[] parastep = {poid,seq,soid,sname,srole,sactor,"1","0","0","0"
                ,preoid,nextoid,slevel,"hnpmi",null,sdept,sevent};
            DBUtil.executeUpdate(conn,sql,parastep);
            preoid = soid;
          }
        }
      }


      conn.commit();
      domresult = WriteXmlMessage(domresult, "保存成功！","MESSAGE");
      os = response.getOutputStream();
      XMLWriter writer = new XMLWriter(os);
      writer.write(domresult);
      os.flush();
    }
    catch (Exception e)
    {
      conn.rollback();
      e.printStackTrace(System.out);
      String strMe = handleError(e);

      domresult = WriteXmlMessage(domresult, strMe,
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
  public ActionForward saveApProcess(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws
      Exception
  {
    String processoid = Pub.val(request, "processoid");
    boolean isNew = Pub.empty(processoid);
    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    Document doc = RequestUtil.getDocument(request);
    Document domresult = null;
    OutputStream os = null;
    Connection conn = DBUtil.getConnection();
    conn.setAutoCommit(false);
    try
    {


      String sql = null;
      if(isNew)
      {
        processoid = DBUtil.getSequenceValue("ap_task_s", conn);
      }
      else
      {
        sql = "delete ap_processstep where PROCESSTYPEOID in (select PROCESSTYPEOID"
            +" from ap_processtype where OPERATIONOID='"+processoid+"')";
        DBUtil.execSql(conn,sql);
        sql = "delete ap_processtype where OPERATIONOID='"+processoid+"'";
        DBUtil.execSql(conn,sql);
      }
      List procs = doc.selectNodes("//RESPONSE/RESULT/ROW/PROCESSTYPE/ROW");
      if(procs != null)
      {
        for(int i=0;i<procs.size();i++)
        {
          Element proc = (Element) procs.get(i);
          String poid = DBUtil.getSequenceValue("ap_task_s",conn);
          String pname = proc.element("NAME").getText();
          String pactor = proc.element("ACTOR").getText();
          String pevent = proc.element("PROCESSEVENT").getText();
          sql = "insert into ap_processtype cols(processtypeoid,name,"
              +"operationoid,actor,createtime,state,precondition1,precondition2,"
              +"precondition3,memo,processevent,processtype) values(?,?,?,?,?,?,?,?,?,?,?,?)";
          Object[] para = {poid,pname,processoid,pactor,Pub.getCurrentDate(),"1"
              ,""+i,""+i,""+i,pname,pevent,"1"};//流程类型暂定为１　
          DBUtil.executeUpdate(conn,sql,para);
          List steplist = proc.element("STEPS").selectNodes("ROW");
          if(steplist == null) continue;
          String preoid = null,nextoid = null;
          for(int j=0;j<steplist.size();j++)
          {
            Element step = (Element) steplist.get(j);
            if(j < steplist.size() -1) nextoid = ((Element)steplist.get(j+1)).element("STEPOID").getText();
            else nextoid = null;
            String seq = ""+(j+1);
            String soid = step.element("STEPOID").getText();
            String sname = step.element("NAME").getText();
            String srole = step.element("ROLENAME").getText();
            String sactor = step.element("ACTOR").getText();
            String sdept = step.element("DEPTID").getText();
            String slevel = step.element("DEPTLEVEL").getText();
            String sevent = step.element("STEPEVENT")==null?"":step.element("STEPEVENT").getText();
            sql = "insert into ap_processstep cols(processtypeoid,stepsequence,"
                +"stepoid,name,rolename,actor,state,precondition2,precondition3,"
                +"precondition1,prestepoid,nextstepoid,deptlevel,application,"
                +"memo,deptid,stepevent) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[] parastep = {poid,seq,soid,sname,srole,sactor,"1","0","0","0"
                ,preoid,nextoid,slevel,"hnpmi",null,sdept,sevent};
            DBUtil.executeUpdate(conn,sql,parastep);
            preoid = soid;
          }
        }
      }


      conn.commit();
      domresult = WriteXmlMessage(domresult, "保存成功！","MESSAGE");
      os = response.getOutputStream();
      XMLWriter writer = new XMLWriter(os);
      writer.write(domresult);
      os.flush();
    }
    catch (Exception e)
    {
      conn.rollback();
      e.printStackTrace(System.out);
      String strMe = handleError(e);

      domresult = WriteXmlMessage(domresult, strMe,
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
  public ActionForward doDeleteApTask(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
  {
    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    Document doc = RequestUtil.getDocument(request);
    Element root = doc.getRootElement();
    String ywlx = doc.selectSingleNode("//DATAINFO//ROW//EVENTTYPE").getStringValue();
    String dwdm = root.selectSingleNode("//DATAINFO//ROW//DWDM").getStringValue();
    OutputStream os = null;
    String res = null;
    Connection conn = DBUtil.getConnection();
    try
    {
      conn.setAutoCommit(false);
      DBUtil.execSql(conn,"delete ap_tasks where EVENTTYPE='"+ywlx+"' and DWDM='"+dwdm+"'");
      conn.commit();
      LogManager.writeUpdateLog("流程管理->删除流程","业务类型："+ywlx+" 部门:"+dwdm,"删除",LogManager.RESULT_SUCCESS,user);
      doc = WriteXmlMessage(null, "流程已经删除！", "MESSAGE");
    }
    catch(Exception e)
    {
      conn.rollback();
      LogManager.writeUpdateLog("流程管理->删除流程","业务类型："+ywlx+" 部门:"+dwdm,"删除",LogManager.RESULT_FAILURE,user);
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
  public ActionForward doDeleteApProcess(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
  {
    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
    Document doc = RequestUtil.getDocument(request);
    Element root = doc.getRootElement();
    String processoid = Pub.val(request, "processoid");
    OutputStream os = null;
    String res = null;
    Connection conn = DBUtil.getConnection();
    try
    {
      conn.setAutoCommit(false);
      DBUtil.execSql(conn,"delete ap_processtype where PROCESSTYPEOID='"+processoid+"'" );
      DBUtil.execSql(conn,"delete ap_processstep where PROCESSTYPEOID='"+processoid+"'" );
      conn.commit();
      //LogManager.writeUpdateLog("流程管理->删除流程","业务类型："+ywlx+" 部门:"+dwdm,"删除",LogManager.RESULT_SUCCESS,user);
      doc = WriteXmlMessage(null, "流程已经删除！", "MESSAGE");
    }
    catch(Exception e)
    {
      conn.rollback();
      //LogManager.writeUpdateLog("流程管理->删除流程","业务类型："+ywlx+" 部门:"+dwdm,"删除",LogManager.RESULT_FAILURE,user);
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