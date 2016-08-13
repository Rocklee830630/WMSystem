package com.ccthanking.framework.spflow;

import java.util.*;
import javax.servlet.http.*;

import net.sf.json.JSONObject;

import org.apache.struts.action.*;

import com.ccthanking.framework.base.*;
import org.dom4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ccthanking.framework.dic.*;
import com.ccthanking.framework.util.*;

import java.sql.Connection;
import com.ccthanking.framework.*;
import com.ccthanking.framework.common.*;
import com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager;
import com.ccthanking.common.EventManager;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.model.requestJson;

import com.ccthanking.common.vo.*;

/**
@author zhangj <2009年7月22日>
处理流程信息
*/
@Controller
@RequestMapping("/TaskQuery")
public class ApTaskQueryAction
{
    //查询是否有多个审批流程
	
	@RequestMapping(params = "QueryOperation", method = RequestMethod.POST)
	public void QueryOperation(HttpServletRequest request,HttpServletResponse response)
	    throws Exception
	{
		User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
	    String ywlx = request.getParameter("ywlx");
	    String userRolelist = user.getRoleListString();
        String rolename=null;
        OrgDept dept = user.getOrgDept();
        Connection conn = DBUtil.getConnection();
        Object[] objs = new Object[2];
        objs[0] = ywlx;
        objs[1] = dept.getDeptID();
        
     //   String operationoid="";
        String codition ="";
        String newcond="";
	    int flag = 0;
	  
	    String condition="";
	    try
	    {
	    	 String sqlQuery =" select * from ap_ws_typz where ywlx= ? and deptid = ? ";
	            QuerySet qs = DBUtil.executeQuery(sqlQuery,objs,conn);
	            if (qs.getRowCount() > 0)
	            {
	                    for (int i = 0; i < qs.getRowCount(); i++)
	                    {
	                        rolename = qs.getString(i+1,"ROLENAME");
	                        if(userRolelist.indexOf(rolename)>=0)
	                        {
	                       // 	operationoid += qs.getString(i+1,"OPERATIONOID")+",";
	                        	codition += qs.getString(i+1,"CONDITION")+",";
	                        	
	                        }
	                    }
	                   newcond  = codition.substring(0, codition.length()-1);
	                
	           }
	     
	            if(!newcond.equals("")){
	            	  sqlQuery =" select * from ap_ws_typz where ywlx= ? and deptid = ? and condition not in ("+newcond+") ";
	            }
	            else{
	            	sqlQuery =" select * from ap_ws_typz where ywlx= ? and deptid = ?  ";
	            }
	            
/*	            while(true){
	            	 
	          	     dept = dept.getParent();
	         	      objs[1]=dept.getDeptID();
	         	    
	         	     qs = DBUtil.executeQuery(sqlQuery,objs,conn);
	         	    
	         	     if (qs.getRowCount() > 0)
		 	            {
		 	                    for (int i = 0; i < qs.getRowCount(); i++)
		 	                    {
		 	                        rolename = qs.getString(i+1,"ROLENAME");
		 	                        if(userRolelist.indexOf(rolename)>=0)
		 	                        {
		 	                      //  	operationoid += qs.getString(i+1,"OPERATIONOID")+",";
		 	                        	codition += qs.getString(i+1,"CONDITION")+",";
		 	                        }
		 	                    }
		 	                    if(!codition.equals("")){
		 	                   newcond  = codition.substring(0, codition.length()-1);
		 	                   }
		 	                   if(!newcond.equals("")){
		 		            	  sqlQuery =" select * from ap_ws_typz where ywlx= ? and deptid = ? and condition not in ("+newcond+") ";
		 		            }
		 		            else{
		 		            	sqlQuery =" select * from ap_ws_typz where ywlx= ? and deptid = ?  ";
		 		            }
		 	   	          
		 	           }
	         	      if(objs[1].equals("100000000000")) {
	         		      break;
	         	      }
	            }*/
	           
	          if (newcond.indexOf(",")>0)
	          {
	            flag=1;
	          }
	          else {
	        	   flag=0;
	        	   if(!newcond.equals("")){
	        	   condition = newcond;
	        	   }
	        }
	          JSONObject jo = new JSONObject();
	          jo.put("flag", flag);
	          jo.put("condition", condition);
	          
	        //String xmlStr = "<ROW><SUCCESS>"+flag+"</SUCCESS><CONDITION>"+condition+"</CONDITION></ROW>";
	        Pub.writeMessage(response, jo.toString(),"UTF-8");
	    }
	    catch (Exception e)
	    {
	        conn.rollback();
	        e.printStackTrace(System.out);

	    }
	    finally
	    {
	        if (conn != null)
	        {
	            conn.close();
	        }
	    }
	    //return null;

	}
	//查询当前可用的审批流程
	 public ActionForward QueryList(ActionMapping mapping, ActionForm form,
	            HttpServletRequest request, HttpServletResponse response)
	            throws Exception {

	        User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	        Document doc = RequestUtil.getDocument(request);
	        PageManager page = RequestUtil.getPageManager(doc);
	        String ywlx = request.getParameter("ywlx");
	     
	        if (page == null)
	            page = new PageManager();
	   
	        Document domresult = null;
	     
	        String userRolelist = user.getRoleListString();
	        String rolename=null;
	        OrgDept dept = user.getOrgDept();
	        Connection conn = DBUtil.getConnection();
	        Object[] objs = new Object[2];
	        objs[0] = ywlx;
	        objs[1] = dept.getDeptID();
	        
	        String operationoid="";
	        String codition ="";
	        String newcond="";
	        try {
	            conn.setAutoCommit(false);
	            String sqlQuery =" select * from za_zfba_jcxx_ws_typz where ywlx= ? and deptid = ? ";
	            QuerySet qs = DBUtil.executeQuery(sqlQuery,objs,conn);
	            if (qs.getRowCount() > 0)
 	            {
 	                    for (int i = 0; i < qs.getRowCount(); i++)
 	                    {
 	                        rolename = qs.getString(i+1,"ROLENAME");
 	                        if(userRolelist.indexOf(rolename)>=0)
 	                        {
 	                        	operationoid += qs.getString(i+1,"OPERATIONOID")+",";
 	                        	codition += qs.getString(i+1,"CONDITION")+",";
 	                        }
 	                    }
 	                   newcond  = codition.substring(0, codition.length()-1);
 	                
 	           }
	            if(!newcond.equals("")){
	            	  sqlQuery =" select * from za_zfba_jcxx_ws_typz where ywlx= ? and deptid = ? and condition not in ("+newcond+") ";
	            }
	            else{
	            	sqlQuery =" select * from za_zfba_jcxx_ws_typz where ywlx= ? and deptid = ?  ";
	            }
	            
	            while(true){
	            	 
	          	     dept = dept.getParent();
	         	      objs[1]=dept.getDeptID();
	         	    
	         	     qs = DBUtil.executeQuery(sqlQuery,objs,conn);
	         	    
	         	     if (qs.getRowCount() > 0)
		 	            {
		 	                    for (int i = 0; i < qs.getRowCount(); i++)
		 	                    {
		 	                        rolename = qs.getString(i+1,"ROLENAME");
		 	                        if(userRolelist.indexOf(rolename)>=0)
		 	                        {
		 	                        	operationoid += qs.getString(i+1,"OPERATIONOID")+",";
		 	                        	codition += qs.getString(i+1,"CONDITION")+",";
		 	                        }
		 	                    }
		 	                    if(!codition.equals("")){
		 	                   newcond  = codition.substring(0, codition.length()-1);
		 	                   }
		 	                   if(!newcond.equals("")){
		 		            	  sqlQuery =" select * from za_zfba_jcxx_ws_typz where ywlx= ? and deptid = ? and condition not in ("+newcond+") ";
		 		            }
		 		            else{
		 		            	sqlQuery =" select * from za_zfba_jcxx_ws_typz where ywlx= ? and deptid = ?  ";
		 		            }
		 	   	          
		 	           }
	         	      if(objs[1].equals("310000000000")) {
	         		      break;
	         	      }
	            }
	           
	            if(!operationoid.equals("")){
	            	operationoid = operationoid.substring(0, operationoid.length()-1);
	            }
	           // operationoid="43434,43436,85517,43054";
	            String sql = "select distinct t.condition,t3.memo ,t2.name  from za_zfba_jcxx_ws_typz t ,ap_processtype t2, ap_tasks t3 "
	            	+" where t.operationoid=t3.operationoid and t.operationoid=t2.operationoid and t.ywlx='"+ywlx+"'"
	            	+" and t.operationoid in ("+operationoid+")";
	            BaseResultSet bs = DBUtil.query(conn, sql,page);
	                    
	            domresult = bs.getDocument();
	            Pub.writeXmlDocument(response, domresult, "UTF-8");
	        }
	        catch (Exception e) {
	            conn.rollback();
	            e.printStackTrace(System.out);
	            Pub.writeXmlErrorMessage(response, e.getMessage());
	        }
	        finally {
	            if (conn != null)
	                conn.close();
	        }
	        return null;
	    }
//	查询当前部门定制的审批流程
	 public ActionForward QueryListALL(ActionMapping mapping, ActionForm form,
	            HttpServletRequest request, HttpServletResponse response)
	            throws Exception {

		    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	        Document doc = RequestUtil.getDocument(request);
	        QueryConditionList list = RequestUtil.getConditionList(doc);
	        PageManager page = RequestUtil.getPageManager(doc);
	        String orderFilter = RequestUtil.getOrderFilter(doc);
	        String condition = list == null ? "" : list.getConditionWhere();
	         //此处可以设置自定义的过滤条件
	        if (Pub.empty(condition))
	            condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
	        
	        condition += " and t.operationoid=t3.operationoid and t.operationoid=t2.operationoid and t.deptid='"+user.getDepartment()+"'   and t.ws_template_id=t4.ws_template_id";
	
	        if (page == null)
	            page = new PageManager();
	        page.setFilter(condition);
	        Document domresult = null;
	        Connection conn = DBUtil.getConnection();
	        try {
	            conn.setAutoCommit(false);
	            String sql = "select distinct t.condition,t3.memo ,t2.name,t.ywlx,t2.OPERATIONOID,t2.processtypeoid,t.rolename,t4.ws_template_id,t4.ws_template_name  from za_zfba_jcxx_ws_typz t ,ap_processtype t2, ap_tasks t3,ws_template t4 ";
	            BaseResultSet bs = DBUtil.query(conn, sql,page);
	                     //设置字典翻译定义
	                                  bs.setFieldDic("YWLX","YWLX");//强制措施类别

	            domresult = bs.getDocument();
	            Pub.writeXmlDocument(response, domresult, "UTF-8");
	        }
	        catch (Exception e) {
	            conn.rollback();
	            e.printStackTrace(System.out);
	            Pub.writeXmlErrorMessage(response, e.getMessage());
	        }
	        finally {
	            if (conn != null)
	                conn.close();
	        }
	        return null;
	    }
	  //查询审批流程以图形方式展示
		public ActionForward QueryJson(ActionMapping mapping, ActionForm form,
		                                HttpServletRequest request,
		                                HttpServletResponse response)
		    throws Exception
		{
			User user = (User)request.getSession().getAttribute(com.ccthanking.framework.Globals.USER_KEY);
		    String ywlx = request.getParameter("ywlx");
		    String condition=request.getParameter("condition");
		    String flowid=request.getParameter("flowid");
		    String opid=request.getParameter("opid");
		    String json = "{\"id\":null,\"name\":null,\"count\":43,\"nodes\":[],\"lines\":[]}";
		    String rolename =request.getParameter("rolename");
		    Connection conn = DBUtil.getConnection();
		    json=""; 
		    String fname = "";
		    String fmemo = "";
		    String frole = "";
		    String fid = "";
		    try
		    {
		       String sqljy= "select * from ap_processstep t where t.processtypeoid ='"+opid+"' order by t.stepsequence";
		     
		       QuerySet  qs = DBUtil.executeQuery(sqljy,null,conn);
		      int j=0;
		      json +="审批发起人:";
			   j++;
			   json +="审批发起人:";
			   j++;
			   json +=""+rolename+":";
			   j++;
			   String sql= "select STEPOID from AP_STEP t where t.ROLENAME ='"+rolename+"' ";
			     
		       QuerySet  qs1 = DBUtil.executeQuery(sql,null,conn);
		       if(qs1.getRowCount()>0){
		    	   json +=""+qs1.getString(1, "STEPOID")+":";
		       }
		       else{
			   json +=":";}
		       if(qs.getRowCount()>0){
		    	   for(int i=0; i<qs.getRowCount();i++){
		    		   fname = qs.getString(i+1, "NAME");
		    		   fmemo = qs.getString(i+1, "MEMO");
		    		   frole = qs.getString(i+1, "ROLENAME");
		    		   fid = qs.getString(i+1, "STEPOID");
		    		   if(fname==null){fname="";}
		    		   if(fmemo==null){fmemo="";}
		    		   if(frole==null){fname="";}
		    		   
		    			   j++;
		    			   json +=""+fname+":";
		    			   j++;
			    		   json +=""+fmemo+":";
			    		   j++;
			    		   json +=""+frole+":";
			    		   j++;
			    		   json +=""+fid+"";
		    		   
		    	   }
		       }
		      
		        String xmlStr = "<ROW><JSON>"+json+"</JSON></ROW>";
		        Pub.writeMessage(response, xmlStr,"UTF-8");
		    }
		    catch (Exception e)
		    {
		        conn.rollback();
		        e.printStackTrace(System.out);

		    }
		    finally
		    {
		        if (conn != null)
		        {
		            conn.close();
		        }
		    }
		    return null;

		}
		
//		查询文书
		 public ActionForward QueryListWS(ActionMapping mapping, ActionForm form,
		            HttpServletRequest request, HttpServletResponse response)
		            throws Exception {

			    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		        Document doc = RequestUtil.getDocument(request);
		        QueryConditionList list = RequestUtil.getConditionList(doc);
		        PageManager page = RequestUtil.getPageManager(doc);
		        String orderFilter = RequestUtil.getOrderFilter(doc);
		        String condition = list == null ? "" : list.getConditionWhere();
		         //此处可以设置自定义的过滤条件
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
		            String sql = "select *  from ws_template  ";
		            BaseResultSet bs = DBUtil.query(conn, sql,page);
		                     //设置字典翻译定义
		                     

		            domresult = bs.getDocument();
		            Pub.writeXmlDocument(response, domresult, "UTF-8");
		        }
		        catch (Exception e) {
		            conn.rollback();
		            e.printStackTrace(System.out);
		            Pub.writeXmlErrorMessage(response, e.getMessage());
		        }
		        finally {
		            if (conn != null)
		                conn.close();
		        }
		        return null;
		    }
//		流程修改
		 public ActionForward UpdateAp(ActionMapping mapping, ActionForm form,
		            HttpServletRequest request, HttpServletResponse response)
		            throws Exception {

			    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			    String ywlx = request.getParameter("ywlx");
			    String opid = request.getParameter("opid");
			    int condition = Integer.parseInt(request.getParameter("condition"));
			    String flowname =  request.getParameter("flowname");
			    String oprationoid = request.getParameter("flowid");
			    String wsid = request.getParameter("wsid");
			    String pstr =  request.getParameter("pstr");
			    String orolename =  request.getParameter("orolename");
		        Connection conn = DBUtil.getConnection();
		        try {
		            conn.setAutoCommit(false);
		            String[] temp_args = pstr.split(",");
		            String  sql = "delete ap_processstep where PROCESSTYPEOID = '"+opid+"'";
		            DBUtil.execSql(conn,sql);
		            sql = "delete AP_TASK_SPECIAL where YWLX= '"+ywlx+"' and OPERATIONOID='"+oprationoid+"'";
		            DBUtil.execSql(conn,sql);
		            if(condition==1 || (condition>=100 && condition<200)){condition=1; }
		            else if (condition==3 || (condition>=300 && condition<400)){condition=2;}
		            else if (condition==2 ||condition==4|| (condition>=400 && condition<500)){condition=3;}
		            else if (condition==5|| (condition>=500 && condition<600)){condition=4;}
		            else if (condition==6|| (condition>=600 && condition<700)){condition=5;}
		            else if (condition==7|| (condition>=700 && condition<800)){condition=6;}
		            else if (condition==8|| (condition>=800 && condition<900)){condition=7;}
		            else {condition=0;}
		            String seq = "";
		            String soid = "";
		            String sname = "";
		            String srole = "";
		            String sactor = "";
		            String preoid = "";
		            String nextoid = "";
		            String slevel = "";
		            String sdept = "";
		            String sevent = "";
		            String memo = "";
		            String apmemo = "";
		            int j=2;
		            for(int i=0;i<condition; i++ ){
		            	j++;
		            	sname = temp_args[j];
		            	j++;
		            	memo =  temp_args[j];
		            	j++;
		            	soid =  temp_args[j]; 
		            	seq = ""+(i+1);
		            	sql ="select ROLENAME,DEPTLEVEL,ACTOR from AP_STEP where STEPOID='"+soid+"'";
		            	 QuerySet qs = DBUtil.executeQuery(sql,null,conn);
		 	            if (qs.getRowCount() > 0){
		 	            	srole=qs.getString(1, "ROLENAME");
		 	            //	sactor=qs.getString(1, "ACTOR");
		 	            //	if(sactor==null){sactor="";}
		 	            	slevel=qs.getString(1, "DEPTLEVEL");
		 	            	if(apmemo.equals("")){apmemo= srole;}
		 	            	else apmemo += "->" +  srole;
		 	            }
		 	            if(j+3<temp_args.length+1){ 
		 	            	int np=j+3;
		 	            	nextoid=temp_args[np];
		 	            }
		 	            else {nextoid="";}
		            sql = "insert into ap_processstep cols(processtypeoid,stepsequence,"
		                +"stepoid,name,rolename,actor,state,precondition2,precondition3,"
		                +"precondition1,prestepoid,nextstepoid,deptlevel,application,"
		                +"memo,deptid,stepevent) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		            Object[] parastep = {opid,seq,soid,sname,srole,sactor,"1","0","0","0"
		                    ,preoid,nextoid,slevel,"hnpmi",memo,sdept,sevent};
		                DBUtil.executeUpdate(conn,sql,parastep);
		                
		            preoid = soid;
		            }
		            sql = " select ROLENAME from AP_STEP where STEPOID='"+temp_args[2]+"'";
		            QuerySet qs = DBUtil.executeQuery(sql,null,conn);
		            if(qs.getRowCount()>0){
		            srole=qs.getString(1, "ROLENAME");}
		            else {srole="";}
		            sql = "update ZA_ZFBA_JCXX_WS_TYPZ set ROLENAME='"+srole+"' , WS_TEMPLATE_ID='"+wsid+"' where ywlx='"+ywlx+"' and OPERATIONOID='"+oprationoid+"' and ROLENAME='"+orolename+"'";
		            DBUtil.executeUpdate(conn,sql,null);
		            sql = " update AP_TASKS set MEMO = '"+flowname+"' where OPERATIONOID = '"+oprationoid+"'";
		            DBUtil.executeUpdate(conn,sql,null);
		            sql = " update AP_PROCESSTYPE set NAME = '"+apmemo+"' where processtypeoid='"+opid+"'";
		            DBUtil.executeUpdate(conn,sql,null);
		            
		            conn.commit();
		        }
		        catch (Exception e) {
		            conn.rollback();
		            e.printStackTrace(System.out);
		            Pub.writeXmlErrorMessage(response, e.getMessage());
		        }
		        finally {
		            if (conn != null)
		                conn.close();
		        }
		        return null;
		    }
		 
//	新增流程
		 public ActionForward Insert(ActionMapping mapping, ActionForm form,
		            HttpServletRequest request, HttpServletResponse response)
		            throws Exception {

			    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			    String ywlx = request.getParameter("ywlx");
			    int condition = Integer.parseInt(request.getParameter("condition"));
			    String flowname =  request.getParameter("flowname");
			    String wsid = request.getParameter("wsid");
			    String pstr =  request.getParameter("pstr");
			    String rolename =  request.getParameter("rolename");
		        Connection conn = DBUtil.getConnection();
		        String isFlag = request.getParameter("isFlag");
		        String operationoid=request.getParameter("flowid");
		        if(isFlag==null) {isFlag="";}
		        try {
		            conn.setAutoCommit(false);
		            String[] temp_args = pstr.split(",");
		            
		            if(isFlag.equals("1")){
		            	 String  sql = "delete za_zfba_jcxx_ws_typz where ywlx ='"+ywlx+"' and operationoid='"+operationoid+"' and rolename='"+rolename+"' and condition='"+condition+"'";
				         DBUtil.execSql(conn,sql);
		            }
		            
		            String opid = DBUtil.getSequenceValue("ap_task_s", conn);
		            OrgDept dept = user.getOrgDept();
		            String fjdm = "",sjdm = "";
		            String level = dept.getDeptType();
		            String memo = flowname;
		            String sql = "insert into ap_tasks cols(operationoid,eventtype,memo,dwdm,fjdm,sjdm)"
		               +" values('"+opid+"','"+ywlx+"','"+memo+"','"+user.getDepartment()+"','"+fjdm+"','"+sjdm+"')";
		            DBUtil.execSql(conn,sql);
		            
		            String poid = DBUtil.getSequenceValue("ap_task_s",conn);
		            
		            String seq = "";
		            String soid = "";
		            String sname = "";
		            String srole = "";
		            String sactor = "";
		            String preoid = "";
		            String nextoid = "";
		            String slevel = "";
		            String sdept = "";
		            String sevent = "";
		            String apmemo = "";
		         //   if(condition==1){condition=2;}
		            if(condition==1 || (condition>=100 && condition<200)){condition=2; }
		            else if (condition==3 || (condition>=300 && condition<400)){condition=3;}
		            else if (condition==2 ||condition==4|| (condition>=400 && condition<500)){condition=4;}
		            else if (condition==5|| (condition>=500 && condition<600)){condition=5;}
		            else if (condition==6|| (condition>=600 && condition<700)){condition=6;}
		            else if (condition==7|| (condition>=700 && condition<800)){condition=7;}
		            else if (condition==8|| (condition>=800 && condition<900)){condition=8;}
		            else {condition=0;}
		            int j=2;
		            for(int i=0;i<condition-1; i++ ){
		            	j++;
		            	sname = temp_args[j];
		            	j++;
		            	memo =  temp_args[j];
		            	j++;
		            	soid =  temp_args[j]; 
		            	seq = ""+(i+1);
		            	sql ="select ROLENAME,DEPTLEVEL,ACTOR from AP_STEP where STEPOID='"+soid+"'";
		            	 QuerySet qs = DBUtil.executeQuery(sql,null,conn);
		 	            if (qs.getRowCount() > 0){
		 	            	srole=qs.getString(1, "ROLENAME");
		 	            //	sactor=qs.getString(1, "ACTOR");
		 	            //	if(sactor==null){sactor="";}
		 	            	slevel=qs.getString(1, "DEPTLEVEL");
		 	            	if(apmemo.equals("")){apmemo= srole;}
		 	            	else apmemo += "->" +  srole;
		 	            }
		 	            if(j+3<temp_args.length+1){ 
		 	            	int np=j+3;
		 	            	nextoid=temp_args[np];
		 	            }
		 	            else {nextoid="";}
		            sql = "insert into ap_processstep cols(processtypeoid,stepsequence,"
		                +"stepoid,name,rolename,actor,state,precondition2,precondition3,"
		                +"precondition1,prestepoid,nextstepoid,deptlevel,application,"
		                +"memo,deptid,stepevent) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		            Object[] parastep = {poid,seq,soid,sname,srole,sactor,"1","0","0","0"
		                    ,preoid,nextoid,slevel,"hnpmi",memo,sdept,sevent};
		                DBUtil.executeUpdate(conn,sql,parastep);
		                
		            preoid = soid;
		            }
		            
		            String pname = apmemo;
		            String pactor = "";
		            String pevent = "";
		            sql = "insert into ap_processtype cols(processtypeoid,name,"
		                +"operationoid,actor,createtime,state,precondition1,precondition2,"
		                +"precondition3,memo,processevent,processtype) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		            Object[] para = {poid,pname,opid,pactor,Pub.getCurrentDate(),"1"
		                ,"","","",pname,pevent,"1"};
		            DBUtil.executeUpdate(conn,sql,para);
		            sql = "select t.deptlevel from ap_step t where t.rolename='"+rolename+"'";
		            QuerySet qs = DBUtil.executeQuery(sql,null,conn);
	 	            if (qs.getRowCount() > 0){
	 	            	level=qs.getString(1, "DEPTLEVEL");
	 	            }
	 	            String con=""; 
	 	            if(condition==1 || condition==2) {con= " condition =1 or (condition>=100 and condition<200) "; }
				            else if (condition==3 )	 {con= " condition =3 or (condition>=300 and condition<400) "; }
				            else if (condition==4)   {con= " condition =4 or (condition>=400 and condition<500) "; }
				            else if (condition==5)   {con= " condition =5 or (condition>=500 and condition<600) "; }
				            else if (condition==6)   {con= " condition =6 or (condition>=600 and condition<700) ";}
				            else if (condition==7)   {con= " condition =7 or (condition>=700 and condition<800) ";}
				            else if (condition==8)   {con= " condition =8 or (condition>=800 and condition<900) ";}
		            else {condition=0;}
	 	            sql ="select max(CONDITION) CONDITION from ZA_ZFBA_JCXX_WS_TYPZ where YWLX='"+ywlx+"' and "+con;
	 	            qs = DBUtil.executeQuery(sql,null,conn);
	 	            if (qs.getRowCount() > 0){
	 	            	if(qs.getString(1, "CONDITION")!=null){
	 	            	int ncon=Integer.parseInt(qs.getString(1, "CONDITION"));
	 	            	if(ncon==1) {condition=100;}
	 	            	else if(ncon==2) {condition=200;}
	 	            	else if(ncon==3) {condition=300;}
	 	            	else if(ncon==4) {condition=400;}
	 	            	else if(ncon==5) {condition=500;}
	 	            	else if(ncon==6) {condition=600;}
	 	            	else if(ncon==7) {condition=700;}
	 	            	else if(ncon==8) {condition=800;}
	 	            	else {condition = ncon+1;}
	 	            	}
	 	            	else {
	 	            		if(condition==2)
	 	            		{
	 	            			condition=1;
	 	            		}
	 	            	}
	 	            }
		            sql = "insert into ZA_ZFBA_JCXX_WS_TYPZ cols(YWLX,WS_TEMPLATE_ID,"
		                +"OPERATIONOID,DEPT_LEVEL,DEPTID,ROLENAME,CONDITION) values(?,?,?,?,?,?,?)";
		            Object[] parastep = {ywlx,wsid,opid,level,user.getDepartment(),rolename,""+condition};
		                DBUtil.executeUpdate(conn,sql,parastep);
		           
		            conn.commit();
		        }
		        catch (Exception e) {
		            conn.rollback();
		            e.printStackTrace(System.out);
		          
		        }
		        finally {
		            if (conn != null)
		                conn.close();
		        }
		        return null;
		    }
		 
//	新增流程(从已有流程中选择)
		 public ActionForward InsertTypz(ActionMapping mapping, ActionForm form,
		            HttpServletRequest request, HttpServletResponse response)
		            throws Exception {

			    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			    String ywlx = request.getParameter("ywlx");
			    String oywlx = request.getParameter("oywlx");
			    String oprationoid = request.getParameter("flowid");
			    String rolename =  request.getParameter("rolename");
		        Connection conn = DBUtil.getConnection();
		        try {
		            conn.setAutoCommit(false);
		            String sql =  "select * from ZA_ZFBA_JCXX_WS_TYPZ "
		            	+" where ywlx='"+ywlx+"' and OPERATIONOID='"+oprationoid+"' and ROLENAME='"+rolename+"'";
		            QuerySet qs1 = DBUtil.executeQuery(sql,null,conn);
		            if(qs1.getRowCount()>0){
		            	Pub.writeXmlMessage(response,null,"该流程已经存在于当前业务中！","MESSAGE");
		            }
		            else {
			            sql = "select WS_TEMPLATE_ID,DEPT_LEVEL,DEPTID,CONDITION from ZA_ZFBA_JCXX_WS_TYPZ "
			            	+" where ywlx='"+oywlx+"' and OPERATIONOID='"+oprationoid+"' and ROLENAME='"+rolename+"'";
			            QuerySet qs = DBUtil.executeQuery(sql,null,conn);
			            
			            if(qs.getRowCount()>0){
			            	String WS_TEMPLATE_ID = qs.getString(1, "WS_TEMPLATE_ID");
			            	String CONDITION = qs.getString(1, "CONDITION");
			            	String DEPTID = qs.getString(1, "DEPTID");
			            	String DEPT_LEVEL = qs.getString(1, "DEPT_LEVEL");
			            	 String con="";
			            	 int newcon=0;
			            	 int condition=Integer.parseInt(CONDITION);
				 	            if(condition==1 ||condition==2 || (condition>=100 && condition<200)) {
				 	            	newcon=1;
				 	            	con= " condition =1 or (condition>=100 and condition<200) "; 
				 	            	}
				 	            else if (condition==3 ||(condition>=300 && condition<400)){
				 	            	newcon=3;
				 	            	con= " condition =3 or (condition>=300 and condition<400) "; 
				 	            	}
					            else if (condition==4 ||(condition>=400 && condition<500)) {
					            	newcon=4;
					            	con= " condition =4 or (condition>=400 and condition<500) "; 
					            	}
					            else if (condition==5 ||(condition>=500 && condition<600)) {
					            	newcon=5;
					            	con= " condition =5 or (condition>=500 and condition<600) "; 
					            	}
					            else if (condition==6 ||(condition>=600 && condition<700)) {
					            	newcon=6;
					            	con= " condition =6 or (condition>=600 and condition<700) ";
					            	}
					            else if (condition==7 ||(condition>=700 && condition<800)) {
					            	newcon=7;
					            	con= " condition =7 or (condition>=700 and condition<800) ";
					            	}
					            else if (condition==8 ||(condition>=800 && condition<900)) {
					            	newcon=8;
					            	con= " condition =8 or (condition>=800 and condition<900) ";
					            	}
					            else {
					            	condition=0;
					            	}
				 	            sql ="select max(CONDITION) CONDITION from ZA_ZFBA_JCXX_WS_TYPZ where YWLX='"+ywlx+"' and "+con;
				 	            qs = DBUtil.executeQuery(sql,null,conn);
				 	            if (qs.getRowCount() > 0){
				 	            	if(qs.getString(1, "CONDITION")!=null){
				 	            	int ncon=Integer.parseInt(qs.getString(1, "CONDITION"));
				 	            	if(ncon==1) {condition=100;}
				 	            	else if(ncon==2) {condition=200;}
				 	            	else if(ncon==3) {condition=300;}
				 	            	else if(ncon==4) {condition=400;}
				 	            	else if(ncon==5) {condition=500;}
				 	            	else if(ncon==6) {condition=600;}
				 	            	else if(ncon==7) {condition=700;}
				 	            	else if(ncon==8) {condition=800;}
				 	            	else {condition = ncon+1;}
				 	            	}
				 	            	else {
				 	            		
				 	            			condition=newcon;
				 	            		}
				 	            }
			            sql = "insert into ZA_ZFBA_JCXX_WS_TYPZ cols(YWLX,WS_TEMPLATE_ID,"
			                +"OPERATIONOID,DEPT_LEVEL,DEPTID,ROLENAME,CONDITION) values(?,?,?,?,?,?,?)";
			            Object[] parastep = {ywlx,WS_TEMPLATE_ID,oprationoid,DEPT_LEVEL,DEPTID,rolename,""+condition};
			                DBUtil.executeUpdate(conn,sql,parastep);
			                Pub.writeXmlMessage(response,null,"操作成功！","MESSAGE");
			            }
		            }
		        
		            conn.commit();
		        }
		        catch (Exception e) {
		            conn.rollback();
		            e.printStackTrace(System.out);
		            Pub.writeXmlErrorMessage(response, e.getMessage());
		        }
		        finally {
		            if (conn != null)
		                conn.close();
		        }
		        return null;
		    }
		 
//			删除
		 public ActionForward Delete(ActionMapping mapping, ActionForm form,
		            HttpServletRequest request, HttpServletResponse response)
		            throws Exception {

			    User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
			    String ywlx = request.getParameter("ywlx");
			    String operationoid = request.getParameter("flowid");
			    String rolename =  request.getParameter("rolename");
			    String condition =  request.getParameter("condition");
		        Connection conn = DBUtil.getConnection();
		        try {
		            conn.setAutoCommit(false);
		            String  sql = "delete za_zfba_jcxx_ws_typz where ywlx ='"+ywlx+"' and operationoid='"+operationoid+"' and rolename='"+rolename+"' and condition='"+condition+"'";
		            DBUtil.execSql(conn,sql);
		            Pub.writeXmlMessage(response,null,"操作成功！","MESSAGE");
		            conn.commit();
		          
		        }
		        catch (Exception e) {
		            conn.rollback();
		            e.printStackTrace(System.out);
		            Pub.writeXmlErrorMessage(response, e.getMessage());
		        }
		        finally {
		            if (conn != null)
		                conn.close();
		        }
		        return null;
		    }
}
