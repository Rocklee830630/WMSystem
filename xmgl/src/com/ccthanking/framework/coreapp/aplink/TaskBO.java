package com.ccthanking.framework.coreapp.aplink;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.io.OutputStream;

import net.sf.json.JSONObject;

import org.dom4j.*;
import org.dom4j.io.*;
import java.sql.Connection;
import com.ccthanking.framework.base.BaseBO;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RandomGUID;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.Constants;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.wsdy.PubWS;
import com.ccthanking.framework.coreapp.aplink.impl.*;
import java.sql.SQLException;

import com.ccthanking.common.EventManager;
import com.ccthanking.common.SequenceUtil;
import com.ccthanking.common.vo.*;
import com.ccthanking.framework.base.*;

public class TaskBO
    extends BaseBO {
  public TaskBO(User userObj) {
  }
  /**
   *
   * @param conn  数据连接
   * @param sjbh  事件编号
   * @param ywlx  业务类型
   * @param result 审批结果 1为同意,2为不同意
   * @param resultDscr  审批意见
   * @param spUser 审批人
   * @param isDealSpWs  是否处理文书
   * @param user 当前user对象
   */
  public void autodoApprove(Connection conn,String sjbh,String ywlx,String result,String resultDscr,String spUser,boolean isDealSpWs,User user, JSONObject jo){
//      String result = "1"; //审批结果为同意
      Process process =ProcessMgr.getProcessByEvent(conn,sjbh);//通过事件获得当前流程实例
      Step step = process.open();//获得当前运行节点
      if(step!=null){
          String taskId = step.getTaskOID();
          String taskSeq = step.getTaskSequence();
          TaskMgrBean task = new TaskMgrBean();
        try
        {

            TaskVO vo = task.doApproveTask(conn, taskId, taskSeq, result,
                                           resultDscr, null, "", "", spUser,
                                           user,jo);
            if(isDealSpWs){
                String fjbh ="";
                String sql_pub_blob = "select * from pub_blob a,ws_template b where a.ws_template_id = b.ws_template_id and a.SJBH='"+sjbh+"' and a.YWLX = '"+ywlx+"' order by is_sp_flag desc";
                QuerySet qs = DBUtil.executeQuery(sql_pub_blob,null);
                if(qs.getRowCount()>0){
                    for (int j = 0; j < qs.getRowCount(); j++)
                    {
                        if("1".equals(qs.getString(1+j,"IS_SP_FLAG"))){
                            fjbh =qs.getString(1+j,"FJBH");
                        }
                    }
                }
                if(!Pub.empty(fjbh)){
                    String approverole = step.getRole();
                    String approvelevel = String.valueOf(step.getDeptLevel());
                    String strResult = Pub.getDictValueByCode("SPJG",result)+":"+resultDscr;
                    doSpResult(conn,sjbh, ywlx,approverole,approvelevel, resultDscr, fjbh,null,"","", user);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
      }

  }

  public String doApprove(Document doc, User user) {
    Connection conn = null;
    try {
      List rows = doc.selectNodes("//DATAINFO/ROW");
      conn = DBUtil.getConnection();
      conn.setAutoCommit(false);
      Element node = null;
      for (int i = 0; i < rows.size(); i++) {
        node = (Element) rows.get(i);
        String id = node.attributeValue("ID");
        String seq = node.attributeValue("SEQ");
        String sjbh = node.attributeValue("SJBH");
        String ywlx = node.attributeValue("YWLX");
        String spbh = node.attributeValue("SPBH");
        String spjg = node.attributeValue("SPJG");
        String result = node.attributeValue("RESULT");
        String resultDscr = node.getText();
        String fjbh = node.attributeValue("FJBH");//文书附件编号
        String sfth = node.attributeValue("SFTH");//是否回退
        String spUser = node.attributeValue("SPUSER");
        JSONObject jo = null;
        if("1".equals(sfth)){
            result = "3";//退回
        }
        //流程外特送标示 特送人和部门
        String tsDwbh = node.attributeValue("TSDEPT");
        String tsPerson = node.attributeValue("TSPERSON");
        //流程内特送 特送节点
        String tsStep = node.attributeValue("TSSTEP");

        //获得盖章xml
        String aztXML = "";
        //获得签名xml
        String aztqmXML = "";

        if(tsDwbh!=null&&!"".equals(tsDwbh)&&"1".equals(result)){
        Process proc = ProcessMgr.getProcessByID(conn, spbh);
        Step step = proc.open();
        String name ="特送给";
        if(tsDwbh!=null)
            name +=Pub.getDeptNameByID(tsDwbh);
        if(tsPerson!=null){
            name +=Pub.getUserNameByLoginId(tsPerson);
        }
        name +="审批";
        Step nstep = proc.createStep(name,tsPerson,"",tsDwbh);//特送暂时不插入角色
        nstep.setTaskOID(id);
        nstep.setDeptLevel(Pub.toInt(OrgDeptManager.getInstance().getDeptByID(tsDwbh).getDeptType()));
        nstep.setStepOID(9999);
        Collection set = proc.getSteps();
        Iterator itor = set.iterator();
        int index =0;
        deleteStep(conn,step.getStepSequence(),proc.getProcessOID());
        Process proc_ts = ProcessMgr.getProcessByID(conn, spbh);//特送
        proc_ts.insertStep(step.getStepSequence()+1,nstep);
        }else if(tsStep!=null&&!"".equals(tsStep)&&"1".equals(result)){//流程内特送
            Process proc = ProcessMgr.getProcessByID(conn, spbh);
            Step step = proc.open();
            int inowStep = step.getStepSequence();
            int itsStep = Integer.parseInt(tsStep);

            //删除当前节点和特送节点之间的所有节点
            for(int j=inowStep;j<itsStep-1;j++){
                proc.deleteStep(j);
            }
        }

        //处理审批结果放在文书的某个域内
        if(!Pub.empty(fjbh)){
            Process proc = ProcessMgr.getProcessByID(conn, spbh);
            Step step = proc.open();
            String approverole = step.getRole();
            String approvelevel = String.valueOf(step.getDeptLevel());
            String strResult = Pub.getDictValueByCode("SPJG",result)+":"+resultDscr;
            doSpResult(conn,sjbh, ywlx,approverole,approvelevel, resultDscr, fjbh,aztXML,sfth,"", user);
        }

        TaskMgrBean task = new TaskMgrBean();
        TaskVO vo = task.doApproveTask(conn,id, seq, result, resultDscr, null,"","",spUser, user, jo);
        if (vo != null) {
          //操作成功，继续进行下一步审批 / 或审批不通过确认任务 / 或归档打印任务
          if (vo.getRWLX().equals(TaskVO.TASK_TYPE_PRINT)) {
            String sql = "";
            String dbdw = "";
            //-- add by guanchb -- 2008-04-01 -- start --
            //-- 审批通过后，需要额外的业务操作
            AfterSP asp = new AfterSP();
            asp.ywOp(conn, id, seq, sjbh, ywlx, spbh);
            //-- add by guanchb -- 2008-04-01 -- end --
            //审批结束后创建任务接口
            switch (Pub.toInt(vo.getYWLX())) {
              case 103:
              case 104:
                break;
              case 607:
                break;
              case 109:
                break;
              default:
                break;
            }
          }
          else if (vo.getRWLX().equals(TaskVO.TASK_TYPE_AFFIRM)) { //审批不通过确认任务

          }
          else if (vo.getRWLX().equals(TaskVO.TASK_TYPE_APPROVE)) { //审批中，返回下一审批任务

          }
        }
        }

      for (int i = 0; i < rows.size(); i++) {
        node = (Element) rows.get(i);
        String id = node.attributeValue("ID");
        String seq = node.attributeValue("SEQ");
        String sjbh = node.attributeValue("SJBH");
        String ywlx = node.attributeValue("YWLX");
        String spbh = node.attributeValue("SPBH");
        String spjg = node.attributeValue("SPJG");
        String result = node.attributeValue("RESULT");
        String resultDscr = node.getText();
        LogManager.writeUserLog(sjbh, ywlx, Globals.OPERATION_TYPE_APPROVE,
                                LogManager.RESULT_SUCCESS,
                                "审批成功！", user, "", "");

      }
      conn.commit();
    }
    catch (Exception e) {
      try {
        List rows = doc.selectNodes("//DATAINFO/ROW");
        Element node = null;
        for (int i = 0; i < rows.size(); i++) {
          node = (Element) rows.get(i);
          String id = node.attributeValue("ID");
          String seq = node.attributeValue("SEQ");
          String sjbh = node.attributeValue("SJBH");
          String ywlx = node.attributeValue("YWLX");
          String spbh = node.attributeValue("SPBH");
          String spjg = node.attributeValue("SPJG");
          String result = node.attributeValue("RESULT");
          String resultDscr = node.getText();
          LogManager.writeUserLog(sjbh, ywlx, Globals.OPERATION_TYPE_APPROVE,
                                  LogManager.RESULT_FAILURE,
                                  "审批失败！", user, "", "");
        }

      }
      catch (Exception ex1) {
      }
      try {
          conn.rollback();
      }
      catch (SQLException ex2) {
      }

      e.printStackTrace(System.out);
      return e.getMessage();
    }
    finally {
      if (conn != null) {
        try {
          conn.close();
        }
        catch (SQLException ex) {
          ex.printStackTrace(System.out);
          return "数据库连接错误！";
        }
      }
    }
    return null;
  }
  
  public String doApprove(JSONObject jo, User user) {
	    Connection conn = null;
	    try {
	      //List rows = doc.selectNodes("//DATAINFO/ROW");
	      conn = DBUtil.getConnection();
	      conn.setAutoCommit(false);
	      //Element node = null;
	      //for (int i = 0; i < rows.size(); i++) {
	      if(jo!=null)
	      {
	        //node = (Element) rows.get(i);
	        String id = (String)jo.get("ID");
	        String seq = (String)jo.get("SEQ");
	        String sjbh = (String)jo.get("SJBH");
	        String ywlx = (String)jo.get("YWLX");
	        String spbh = (String)jo.get("SPBH");
	        String spjg = (String)jo.get("SPJG");
	        String result = (String)jo.get("RESULT");
	        String resultDscr = (String)jo.get("resultDscr");
	        String fjbh = (String)jo.get("FJBH");//文书附件编号
	        String sfth = (String)jo.get("SFTH");//是否回退
	        String spUser = (String)jo.get("SPUSER");
	        String ccFlag = (String)jo.get("ccFlag");
	        String autodoApprove = (String)jo.get("autodoApprove");//是否自动越级
	        if("1".equals(sfth)){
	            result = "3";//退回
	        }
	        //流程外特送标示 特送人和部门
	        String tsDwbh = (String)jo.get("TSDEPT");
	        String tsPerson = (String)jo.get("TSPERSON");
	        //流程内特送 特送节点
	        String tsStep = (String)jo.get("TSSTEP");


	        String spFieldname = (String)jo.get("spFieldname");


	        if(tsDwbh!=null&&!"".equals(tsDwbh)&&"1".equals(result)){
	            String name ="特送给";
		        if(tsDwbh!=null)
		            name +=Pub.getDeptNameByID(tsDwbh);
		        if(tsPerson!=null){
		            name +=Pub.getUserNameByLoginId(tsPerson);
		        }
		        name +="审批";

	        }else if(tsStep!=null&&!"".equals(tsStep)&&"1".equals(result)){//流程内特送
	            Process proc = ProcessMgr.getProcessByID(conn, spbh);
	            Step step = proc.open();
	            int inowStep = step.getStepSequence();
	            int itsStep = Integer.parseInt(tsStep);

	            //删除当前节点和特送节点之间的所有节点
	            for(int j=inowStep;j<itsStep-1;j++){
	                proc.deleteStep(j);
	            }
	        }

	        //处理审批结果放在文书的某个域内
	        if(!Pub.empty(fjbh)){
	            Process proc = ProcessMgr.getProcessByID(conn, spbh);
	            Step step = proc.open();
	            String approverole = step.getRole();
	            String stepSequence = String.valueOf(step.getStepSequence());
	            
	            //String strResult = Pub.getDictValueByCode("SPJG",result)+":"+resultDscr;


	            	if("700101".equals(ywlx)) {
	            		int currentStep = step.getStepSequence();
	            		String name = step.getName();
	            		System.out.println(currentStep + " | " + name);
	    	            String oid = proc.getOperationOID();
	    	            
	    	            if ("43485".equals(oid)) {// 43485表示的是新的合同会签单，wsid为28，会签单会签顺序改变了
		            		if ("财务部负责人（落实核算主体）".equals(name)) {// 第六步骤是落实核算主题（甲方单位）
			            		hthqd(conn, "28", String.valueOf(currentStep), sjbh, ywlx, result, resultDscr, user);
							} else {
				            	doSpResult(conn,sjbh, ywlx,approverole,stepSequence, resultDscr, fjbh,spFieldname,sfth,autodoApprove, user);
							}
	    				} else if("43463".equals(oid)) {// 43463表示的是老的合同会签单，wsid为5
		            		if (currentStep == 2) {// 第二步骤是落实核算主题（甲方单位）
			            		hthqd(conn, "5", "2", sjbh, ywlx, result, resultDscr, user);
							} else {
				            	doSpResult(conn,sjbh, ywlx,approverole,stepSequence, resultDscr, fjbh,spFieldname,sfth,autodoApprove, user);
							}
						}
	            	} else {
		            	doSpResult(conn,sjbh, ywlx,approverole,stepSequence, resultDscr, fjbh,spFieldname,sfth,autodoApprove, user);
	            	}
	         //   	doSpResult(conn,sjbh, ywlx,approverole,stepSequence, resultDscr, fjbh,spFieldname,sfth,autodoApprove, user);

	        }

	        TaskMgrBean task = new TaskMgrBean();
	        TaskVO vo = task.doApproveTask(conn,id, seq, result, resultDscr, null,"","",spUser, user,jo);
	        if (vo != null) {
	          //操作成功，继续进行下一步审批 / 或审批不通过确认任务 / 或归档打印任务
	          if (vo.getRWLX().equals(TaskVO.TASK_TYPE_PRINT)) {
	            String sql = "";
	            String dbdw = "";
	            //-- add by guanchb -- 2008-04-01 -- start --
	            //-- 审批通过后，需要额外的业务操作
	            AfterSP asp = new AfterSP();
	            asp.ywOp(conn, id, seq, sjbh, ywlx, spbh);
	            //-- add by guanchb -- 2008-04-01 -- end --
	            //审批结束后创建任务接口
	            switch (Pub.toInt(vo.getYWLX())) {
	              case 103:
	              case 104:
	                break;
	              case 607:
	                break;
	              case 109:
	                break;
	              default:
	                break;
	            }
	          }
	          else if (vo.getRWLX().equals(TaskVO.TASK_TYPE_AFFIRM)) { //审批不通过确认任务

	          }
	          else if (vo.getRWLX().equals(TaskVO.TASK_TYPE_APPROVE)) { //审批中，返回下一审批任务

	          }
	        }


	      //for (int i = 0; i < rows.size(); i++) {
	      //  node = (Element) rows.get(i);

	        LogManager.writeUserLog(sjbh, ywlx, Globals.OPERATION_TYPE_APPROVE,
	                                LogManager.RESULT_SUCCESS,
	                                "审批成功！", user, "", "");

	      }
	      conn.commit();
	    }
	    catch (Exception e) {
	      try {
	        //List rows = doc.selectNodes("//DATAINFO/ROW");
	        //Element node = null;
	        //for (int i = 0; i < rows.size(); i++) {
	        //  node = (Element) rows.get(i);
	          String id = (String)jo.get("ID");
	          String seq = (String)jo.get("SEQ");
	          String sjbh = (String)jo.get("SJBH");
	          String ywlx = (String)jo.get("YWLX");
	          String spbh = (String)jo.get("SPBH");
	          String spjg = (String)jo.get("SPJG");
	          String result = (String)jo.get("RESULT");
	          String resultDscr = (String)jo.get("resultDscr");
	          LogManager.writeUserLog(sjbh, ywlx, Globals.OPERATION_TYPE_APPROVE,
	                                  LogManager.RESULT_FAILURE,
	                                  "审批失败！", user, "", "");
	        //}

	      }
	      catch (Exception ex1) {
	      }
	      try {
	          conn.rollback();
	      }
	      catch (SQLException ex2) {
	      }

	      e.printStackTrace(System.out);
	      return e.getMessage();
	    }
	    finally {
	      if (conn != null) {
	        try {
	          conn.close();
	        }
	        catch (SQLException ex) {
	          ex.printStackTrace(System.out);
	          return "数据库连接错误！";
	        }
	      }
	    }
	    return null;
	  }
  
  /**
   * 当合同会签单流程运行的时候
   * @param conn
   * @param sjbh String 事件编号
   * @param ywlx String 业务类型
   * @param result String 是否同意【// 1：同意   2：不同意  3：退回】
   * @param disagree String 意见。不同意的情况下才用
   * @param user User 当前登录人
   */
  private void hthqd(Connection conn, String wsid, String approvelevel, String sjbh, String ywlx, String result, String yj, User user) {
	  String domain_value = "";
	  if("3".equals(result)) {
		  domain_value = yj;
	  } else {
		  String sql = "select jfdw from gc_htgl_ht where sjbh='"+sjbh+"'";
		  String[][] jfdw = DBUtil.query(conn, sql);
		  domain_value = jfdw == null ? "" : jfdw[0][0];
		  // 融资主体和意见
		  domain_value += "\n"+yj;
	  }

      String id = new RandomGUID().toString();
      String insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM,SPYLB)"+
    		  	" values('"+id+"','5','"+wsid+"','','"+domain_value+"','','Fld467','','"+approvelevel+"','1','','"+sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"','') ";
      try {
		DBUtil.execUpdateSql(conn, insertSql);
      } catch (Exception e) {
		e.printStackTrace();
      }
  }

  public Document getApproveTaskList(Object obj, QueryConditionList list,
                                     PageManager page) throws Exception {
    String conditionAll = list == null ? "" : list.getConditionWhere();
    String condition="";
    String strXM="";
    if (!Pub.empty(conditionAll)) {
      condition=conditionAll.split(":")[0];
      if (conditionAll.split(":").length>1)
      {
        strXM=conditionAll.split(":")[1];
      }
    }
    else
    {
      condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
    }
    if (page == null) {
      page = new PageManager();
    }
    page.setFilter(condition);
    Document docRes = null;
    Connection conn = DBUtil.getConnection();
    try {
      conn.setAutoCommit(false);
      String sql =
          "select rownum,'['||id||'-'||seq||'] '||memo memo,nvl(result,'0') result,"
          + "RESULTDSCR,'wsprint' as wsprint,'splc' as splc,'' as gzxx,'' as qmxx,'0' as processtype,'ws_template_id' as ws_template_id,'ws_template_name' as ws_template_name,id,seq,sjbh,ywlx,rwzt,rwlx,spbh,"//增加伪列wsprint用于显示文书
          +
          " cjrxm,cjdwdm,cjsj,DBDWDM,dbrole,dbryid,spjg,spyj,spr,hh,'' as fjbh  from ap_task_schedule ";//增加伪列fjbh用于print文书
      BaseResultSet bset = DBUtil.query(conn, sql,page);
      bset.setFieldDic("result", "SPJG");
      bset.setFieldDic("spjg", "SPJG");
      bset.setFieldOrgDept("cjdwdm");
      docRes = bset.getDocument();
      List nodes = docRes.selectNodes("//RESPONSE/RESULT/ROW");
      if (nodes != null && nodes.size() > 0) {
        Element node = null;
        for (int i = 0; i < nodes.size(); i++) {
          node = (Element) nodes.get(i);
          String sjbh = node.selectSingleNode("SJBH").getText();
          String ywlx = node.selectSingleNode("YWLX").getText();
          String spbh = node.selectSingleNode("SPBH").getText();
          String str = null;
          //判断是否有文书
          String sql_pub_blob = "select * from pub_blob a,ws_template b where (a.ZFBS = '0' or a.ZFBS IS NULL) and a.ws_template_id = b.ws_template_id and a.SJBH='"+sjbh+"' and a.YWLX = '"+ywlx+"' order by is_sp_flag desc";
          QuerySet qs = DBUtil.executeQuery(sql_pub_blob,null);
          String sp_ws_template_id = "";//审批文书编号
          if(qs.getRowCount()>0){
              for (int j = 0; j < qs.getRowCount(); j++)
              {
                  if("1".equals(qs.getString(1+j,"IS_SP_FLAG"))){
                      sp_ws_template_id =qs.getString(1+j,"WS_TEMPLATE_ID");
                  }
              }
          }
          node.selectSingleNode("ROWNUM").setText(
              "<input type=\"radio\" sp_ws_template_id=\""+sp_ws_template_id+"\" sjbh=\""+sjbh+"\" ywlx=\""+ywlx+"\" onclick=\"onChecked(this)\"  name=\"selectCheckInfo\"" +
              " value=\"" + node.selectSingleNode("IDNUM").getStringValue() +
              "\">");
          ( (Element) node.selectSingleNode("RESULT")).setAttributeValue("sv",
              "<center>" +
              ( (Element) node.selectSingleNode("RESULT")).attributeValue("sv") +
              "</center>");
          if(qs.getRowCount()>0){
              String ws_template_id = "";
              String fjbh = "";
              String sp_fjbh = "";
              String ws_template_name = "";
              for(int j=0 ;j<qs.getRowCount();j++){
                  if("1".equals(qs.getString(1+j,"IS_SP_FLAG"))){
                      sp_fjbh = qs.getString(1+j,"FJBH");
                  }
                  ws_template_id +=qs.getString(1+j,"WS_TEMPLATE_ID")+",";
                  ws_template_name +=qs.getString(1+j,"FILENAME")+",";
                  fjbh +=qs.getString(1+j,"fjbh")+",";
              }
              if(ws_template_id.length()>0){
                  ws_template_id  = ws_template_id.substring(0,ws_template_id.length()-1);
              }
              if(ws_template_name.length()>0){
                  ws_template_name  = ws_template_name.substring(0,ws_template_name.length()-1);
              }
              if(fjbh.length()>0){
                  fjbh  = fjbh.substring(0,fjbh.length()-1);
              }
              str ="<a href=\"#\" onclick=\"print('"+fjbh+"','"+ws_template_id+"','"+sjbh+"','"+ywlx+"')\">查看文书</a>";
              node.selectSingleNode("WSPRINT").setText(str);
              node.selectSingleNode("WS_TEMPLATE_ID").setText(ws_template_id);
              node.selectSingleNode("WS_TEMPLATE_NAME").setText(ws_template_name);
              node.selectSingleNode("FJBH").setText(fjbh);
              //盖章信息判断
              //String[][] sfgz = DBUtil.query("select sfgz from ws_template where  WS_TEMPLATE_ID='"+sp_ws_template_id+"'");
              //if(sfgz!=null&&"1".equalsIgnoreCase(sfgz[0][0])){
                  Process proc = ProcessMgr.getProcessByID(conn, spbh);
                  Step step = proc.open();
                  String approverole = step.getRole();
                  String approvelevel = String.valueOf(step.getDeptLevel());
                  PubWS pubws = new PubWS();
                  Document doc= pubws.getDoc(sp_fjbh);
                  Element result = pubws.canGetAzt(doc,approverole,approvelevel,pubws.FIELDEXTEN_SEAL);
                  if(result!=null)
                  node.selectSingleNode("GZXX").setText("1");
                  else
                  node.selectSingleNode("GZXX").setText("0");

                  Element result_qm = pubws.canGetAzt(doc,approverole,approvelevel,pubws.FIELDEXTEN_WRITE);
                  if(result_qm!=null)
                    node.selectSingleNode("QMXX").setText("1");
                  else
                    node.selectSingleNode("QMXX").setText("0");

              //}
              ((Element)node.selectSingleNode("GZXX")).setAttributeValue("sv","未盖章");
          }else{
              ((Element)node.selectSingleNode("GZXX")).setAttributeValue("sv","未盖章");
              node.selectSingleNode("GZXX").setText("0");
              node.selectSingleNode("WSPRINT").setText("无审批文书");
          }
          //定义查看审批流程
          String splc ="<a href=\"#\" onclick=\"QuerySplc('"+sjbh+"','"+spbh+"')\">审批流程</a>";
          node.selectSingleNode("SPLC").setText(splc);
          //定义判断审批处理方式
          String sql_processtype = "Select a.processtype from ap_processinfo b,ap_processtype a Where a.PROCESSTYPEOID = b.PROCESSTYPEOID and b.PROCESSOID='"+spbh+"'";
          QuerySet qs_processtype = DBUtil.executeQuery(sql_processtype,null);
          if(qs_processtype.getRowCount()>0){
              node.selectSingleNode("PROCESSTYPE").setText(qs_processtype.getString(1,1));
          }



          //通过业务类型 组成审批显示信息字符串 begin
          ApproveDesc approveDesc = new ApproveDesc();
          // end
          String cjdwdm = node.selectSingleNode("CJDWDM").getText();
          str = approveDesc.getDesc(conn, sjbh, ywlx,cjdwdm);
          if (!Pub.empty(str)) {
              node.selectSingleNode("MEMO").setText(str);
          }
        }
      }
      conn.commit();
    }
    catch (Exception e) {
      conn.rollback();
      throw e;
    }
    finally {
      if (conn != null) {
        conn.close();
      }
    }
    return docRes;
  }
  /**
   * 处理审批文书意见接口函数
   * @param sjbh
   * @param ywlx
   * @param result
   * @param fjbh
   * @param user
   * @throws java.lang.Exception
   */
  public void doSpResult(Connection conn,String sjbh,String ywlx,String approverole,String stepSequence,String result,String fjbh,String spFieldname,String sfth,String autodoApprove,User user)
        throws Exception
    {
        PubWS pubws = new PubWS();
        String strfjbh = null;
        String strwsbh = null;
        if (fjbh != null) {
            String[] fjbhs = fjbh.split(",");
            for (int i = 0; i < fjbhs.length; i++) {
                String sql = "Select * From pub_blob a,ws_template b Where a.WS_TEMPLATE_ID = b.WS_TEMPLATE_ID And b.IS_SP_FLAG='1' and fjbh='"+fjbhs[i] + "'";
                QuerySet qs = DBUtil.executeQuery(sql, null);
                if (qs.getRowCount() > 0) {
                    strfjbh = fjbhs[i];
                    strwsbh = qs.getString(1,"WS_TEMPLATE_ID");
                    break;
                }
            }
        }

      if(ywlx!=null&&!"".equals(ywlx)){
          ApproveWsMind wsMind = new ApproveWsMind();
          if(strfjbh!=null){
              wsMind.ModifyApproveMind(conn,strwsbh, sjbh, ywlx, approverole,stepSequence, strfjbh, result,spFieldname,sfth,autodoApprove,user);
          }
      }

  }
  public Document getDisProveTaskList(Object obj, QueryConditionList list,
                                      PageManager page) throws Exception {
    String condition = list == null ? "" : list.getConditionWhere();
    if (Pub.empty(condition)) {
      condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
    }
    if (page == null) {
      page = new PageManager();
    }
    page.setFilter(condition);
    Document docRes = null;
    Connection conn = DBUtil.getConnection();
    try {
      conn.setAutoCommit(false);
      String sql =
          "select rownum,'['||id||'-'||seq||'] '||memo memo,nvl(result,'0') result,"
          + "RESULTDSCR,id,seq,sjbh,ywlx,rwzt,rwlx,spbh,"
          +
          " cjrxm,cjdwdm,cjsj,DBDWDM,dbrole,dbryid,spjg,spyj,spr from task_schedule ";
      BaseResultSet bset = DBUtil.query(conn, sql,page);
      bset.setFieldDic("result", "SPJG");
      bset.setFieldDic("spjg", "SPJG");
      bset.setFieldOrgDept("cjdwdm");
      docRes = bset.getDocument();
      List nodes = docRes.selectNodes("//RESPONSE/RESULT/ROW");
      if (nodes != null && nodes.size() > 0) {
        Element node = null;
        for (int i = 0; i < nodes.size(); i++) {
          node = (Element) nodes.get(i);
          node.selectSingleNode("ROWNUM").setText(
              "<input type=\"checkbox\"  checked=\"true\"  name=\"selectCheckInfo\"" +
              " value=\"" + node.selectSingleNode("IDNUM").getStringValue() +
              "\">");
          ( (Element) node.selectSingleNode("RESULT")).setAttributeValue("sv",
              "<center>" +
              ( (Element) node.selectSingleNode("RESULT")).attributeValue("sv") +
              "</center>");
          String sjbh = node.selectSingleNode("SJBH").getText();
          String ywlx = node.selectSingleNode("YWLX").getText();
          String str = null;
          switch (Pub.toInt(ywlx)) {
            case 101:

              break;
            case 0:
            default:
              break;
          }
          str += "<br>审批人：<font color=\"#bb0000\">" +
              node.selectSingleNode("SPR").getText() + "</font>&nbsp;&nbsp;";
          str += "审批结果：<font color=\"#bb0000\">" +
              node.element("SPJG").attributeValue("sv") + "</font>&nbsp;&nbsp;";
          str += "审批意见：<font color=\"#bb0000\">" +
              node.selectSingleNode("SPYJ").getText() + "</font>&nbsp;&nbsp;";
          if (!Pub.empty(str)) {
            node.selectSingleNode("MEMO").setText(str);
          }
        }
      }
      conn.commit();
    }
    catch (Exception e) {
      conn.rollback();
      throw e;
    }
    finally {
      if (conn != null) {
        conn.close();
      }
    }
    return docRes;
  }

  //删除节点，处理流程外特送
  public void deleteStep(Connection conn,int index,String ProcessOID) throws Exception{
      String sql = "update ap_processdetail set STATE=2"
          + " where PROCESSOID='" +ProcessOID
          + "' and STEPSEQUENCE > " + String.valueOf(index);
      DBUtil.execSql(conn, sql);
  }
  public static Process getProcess(Connection conn,String spbh)
        throws SQLException
    {
        Process proc = null;
        try {
            proc = ProcessMgr.getProcessByID(conn, spbh);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
        }
        return proc;

  }
  public static Collection getProcessSteps(String spbh)
        throws SQLException
    {
        Connection conn = DBUtil.getConnection();
        Collection coll = null ;
        Process proc = null;
        try {
            proc = ProcessMgr.getProcessByID(conn, spbh);
            coll = proc.getSteps();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return coll;

  }

  public static Collection getProcess(String spbh)
        throws SQLException
    {
	  Connection conn = DBUtil.getConnection();
      Collection coll = null ;
      Process proc = null;
      try {
          proc = ProcessMgr.getProcessByID(conn, spbh);
          coll = proc.getSteps();
      }
      catch (Exception ex) {
          ex.printStackTrace();
      }
      finally {
          if (conn != null) {
              conn.close();
          }
      }
      return coll;
  }
  
  		// 在发起或再次发起时，向文书表中添加意见。
  		public static void insertIntoWs(String ywlx, String sjbh, String mind, User user, Connection conn) throws SQLException {
  			if("040414".equals(ywlx)) {//招标公告
	            String id = new RandomGUID().toString();
				String insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
		        	" values('"+id+"','5','17','','"+mind+"','','Fld433','','','1','','"+sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"') ";
		        DBUtil.executeUpdate(conn, insertSql, null);
  			} else if("040415".equals(ywlx)) {//招标文件
	            String id = new RandomGUID().toString();
				String insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
						" values('"+id+"','5','18','','"+mind+"','','Fld433','','','1','','"+sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"') ";
		        DBUtil.executeUpdate(conn, insertSql, null);
  			} else if("300101".equals(ywlx)) {//招投标需求
	            String id = new RandomGUID().toString();
				String insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
						" values('"+id+"','5','4','','"+mind+"','','Fld1022','','','1','','"+sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"') ";
		    	DBUtil.executeUpdate(conn, insertSql, null);
  			} else if("040416".equals(ywlx)) {//招标文件补遗文件会签单（新）和招标文件一样
	            String id = new RandomGUID().toString();
				String insertSql = "insert into AP_PROCESS_WS(SPWSID,WSWH_FLAG,WS_TEMPLATE_ID,DOMAIN_TYPE,DOMAIN_VALUE,CODEPAGE,FIELDNAME,APPROVEROLE,APPROVELEVEL,CANEDIT,DOMAIN_STYLE,SJBH,YWLX,LRSJ,LRRID,LRRXM)"+
						" values('"+id+"','5','30','','"+mind+"','','Fld433','','','1','','"+sjbh+"','"+ywlx+"',SYSDATE,'"+user.getAccount()+"','"+user.getName()+"') ";
		        DBUtil.executeUpdate(conn, insertSql, null);
  			}
  		}

}