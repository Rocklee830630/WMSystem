package com.ccthanking.framework.coreapp.aplink.ApProcessTemplete;
import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import com.ccthanking.framework.base.*;
import org.dom4j.*;
import com.ccthanking.framework.dic.*;
import com.ccthanking.framework.util.*;
import java.sql.Connection;
import com.ccthanking.framework.*;
import com.ccthanking.framework.common.*;
import org.dom4j.*;
import org.dom4j.io.*;
import java.io.OutputStream;
import javax.servlet.ServletConfig;
import java.io.*;

public class ApProcessTempleteAction
    extends BaseDispatchAction {
  //处理环节定义插入

 	public ActionForward Insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

            User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
            Document doc = RequestUtil.getDocument(request);
        Connection conn = DBUtil.getConnection();
        try
           {
               conn.setAutoCommit(false);
               String sql = "Select ap_task_s.Nextval From dual";
               String processID = DBUtil.query(sql)[0][0];

               ArrayList array = getVO(processID,doc);
               BaseDAO dao = new BaseDAO();
               //删除原有模版

               for(int i=0;i<array.size();i++){
                ApProcessTempleteVO vo = ( ApProcessTempleteVO)array.get(i);
                dao.insert(conn,vo);
               }
               conn.commit();
               Pub.writeXmlInfoMessage(response,"操作成功！");
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
           }
           return null;

	}
    public ActionForward QueryTemplate(ActionMapping mapping, ActionForm form,
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

  //处理环节定义查询

	public ActionForward QueryList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

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
        Connection conn = DBUtil.getConnection();
        try {
            conn.setAutoCommit(false);
            String sql = "select ACTOR , APPLICATION , DEPTID , DEPTLEVEL , MEMO , NEXTSTEPOID , PRECONDITION1 , PRECONDITION2 , PRECONDITION3 , PRESTEPOID , PROCESSTYPEOID , PROCNAME , ROLENAME , STATE , STEPEVENT , STEPNAME , STEPOID , STEPSEQUENCE from AP_PROCESS_TEMPLETE ";
            BaseResultSet bs = DBUtil.query(conn, sql,page);
             		//设置字典翻译定义
					  		//设置时间的显示格式
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
 //处理环节定义修改
    public ArrayList getVO(String processID ,Document doc){
        List list = doc.selectNodes("/DATAINFO/STEPS/ROW");
        ArrayList array = new ArrayList();
        java.util.Iterator iterator = list.iterator();
        String preoid = "",nextoid = "";
        String name ="";

        for(int i=0;i<list.size();i++){
            Element step = (Element) list.get(i);
            String stepName = step.element("NAME").getText();
            name+=stepName+"->";
        }
        if(name.length()>0){
            name = name.substring(0,name.length()-2);
            name +="审批模版";
        }
        for(int i=0;i<list.size();i++){
            ApProcessTempleteVO vo = new ApProcessTempleteVO();

            Element step = (Element) list.get(i);
            if(i < list.size() -1) nextoid = ((Element)list.get(i+1)).element("STEPOID").getText();
            else nextoid = "";
            if(i >0) preoid = ((Element)list.get(i-1)).element("STEPOID").getText();
            else preoid = "";
            vo.setProcesstypeoid(processID);
            vo.setStepsequence(String.valueOf(i+1));
            vo.setStepoid(step.element("STEPOID").getText());
            vo.setProcname(name);
            vo.setStepname(step.element("NAME").getText());
            vo.setRolename(step.element("ROLENAME").getText());
            vo.setActor(step.element("ACTOR").getText());
            vo.setState("1");
            vo.setPrecondition1("1");
            vo.setPrecondition2("0");
            vo.setPrecondition3("0");
            vo.setPrestepoid(preoid);
            vo.setNextstepoid(nextoid);
            vo.setDeptid(step.element("DEPTID").getText());
            vo.setDeptlevel(step.element("DEPTLEVEL").getText());
            array.add(vo);
        }

        return array;

    }

	public ActionForward Update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

             User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
             Document doc = RequestUtil.getDocument(request);
	     Connection conn = DBUtil.getConnection();
	     try
	        {
	            conn.setAutoCommit(false);
	            Element data = doc.getRootElement();
                Element eprocessID = data.element("PROCESSTYPEOID");
                String processID = eprocessID.getText();
                Element ename = data.element("PROCNAME");
                String name = ename.getText();
                ArrayList array = getVO(processID,doc);
                BaseDAO dao = new BaseDAO();
                //删除原有模版
                String sql = "delete ap_process_templete where PROCESSTYPEOID ='"+processID+"'";
                DBUtil.execSql(conn,sql);

                for(int i=0;i<array.size();i++){
                 ApProcessTempleteVO vo = ( ApProcessTempleteVO)array.get(i);
                 dao.insert(conn,vo);
                }
	            conn.commit();
	            Pub.writeXmlInfoMessage(response,"操作成功！");
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
	        }
	        return null;
	}
   //处理环节定义删除

    public ActionForward Delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

             User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
             Document doc = RequestUtil.getDocument(request);
         Connection conn = DBUtil.getConnection();
         try
            {
                conn.setAutoCommit(false);
                Element data = doc.getRootElement();
                Element eprocessID = data.element("PROCESSTYPEOID");
                String processID = eprocessID.getText();
                String sql = "delete ap_process_templete where PROCESSTYPEOID ='"+processID+"'";
                DBUtil.execSql(conn,sql);
                conn.commit();
                Pub.writeXmlInfoMessage(response,"操作成功！");
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
            }
            return null;
    }

}
