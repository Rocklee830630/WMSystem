package com.ccthanking.framework.spflow.DealSP;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.ApWsTypzVO;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.aplink.TaskMgrBean;
import com.ccthanking.framework.coreapp.aplink.TaskVO;
import com.ccthanking.framework.spflow.IDealSP;
import com.ccthanking.framework.spflow.WsConfigManager;
import com.ccthanking.framework.util.Pub;


public class DefaultDealSP implements IDealSP
{
    public void dealSP(HttpServletRequest request, HttpServletResponse response, Connection conn, 
    		EventVO evo, String ywlx, String desc, User user,String type,JSONObject joList) throws java.lang.Exception
    {
         TaskMgrBean taskMgr = new TaskMgrBean();
         TaskVO task = null;
         String dbr = request.getParameter("dbr");
         String dbdw = request.getParameter("dbdw");
         String ywbh = (String) request.getParameter("ywbh");// 案件编号
         if(ywbh==null) ywbh ="";
         String title = (String) request.getParameter("title");// 标题
         String condition = (String) request.getParameter("condition");// 条件
         String operationoid = (String) request.getParameter("operationoid");
         String mind = (String)request.getParameter("mind");

         ApWsTypzVO typzvo= WsConfigManager.getDefaultConfig(ywlx,user,condition);
         if(typzvo != null)
         {
        	 //判发起是否为特送审批dbdw有值即为特送审批，否则为普通审批
        	 if(operationoid==null||operationoid.length()<=0)
        		  operationoid = typzvo.getOperationoid();
        	 boolean isTs = false;
        	 if(!Pub.empty(operationoid))
        	 {
        		 String sql = " select a.processtype from ap_processtype a  where a.operationoid = '"+operationoid+"'";
        		 String rs[][] = DBUtil.query(conn, sql);
        		 if(rs!=null&& "4".equals(rs[0][0]))
        		 {
        			 isTs = true;
        		 }
        	 }
        	 //
        	 if(isTs){//特送处理
        		 
        		 if(dbr.indexOf(",")==-1) { // 表示发起时发送一个人
        			 taskMgr.createApproveTaskTs(conn, evo.getSjbh(), ywlx, desc + "，请办理。", dbdw, "", dbr, user,operationoid,ywbh,title,ywlx,""); // 
        		 } else { // 表示发起时发送多人
        			 taskMgr.createApproveMultiTaskTs(conn, evo.getSjbh(), ywlx, desc + "，请办理。", dbdw, "", dbr, user, operationoid);
        		 }
        	 }else{
                 task = taskMgr.createApproveTask(conn, evo.getSjbh(), ywlx, desc + "，请办理。", dbdw, "", dbr, user,operationoid,ywbh,title,ywlx,mind); // 
					if (task == null)
					{ // 该业务不需要审批
						EventManager.archiveEvent(conn, evo, user);
					}
					else
					{
						//if(typzvo.getWs_template_id().equals("0")||typzvo.getWs_template_id()==null)
						//{
							//taskMgr.createApproveWS(request, response, evo.getSjbh(), ywlx, conn);
						//}
						//else 
							//taskMgr.createApproveWS(request, response, evo.getSjbh(), ywlx, conn, typzvo.getWs_template_id());
					} 
        	 }
         }
         else
         {
             task = taskMgr.createApproveTask(conn, evo.getSjbh(), ywlx, desc + "，请办理。", "", "", dbr,false, user,ywbh,title,ywlx,mind);

//                task = taskMgr.createApproveTask(conn, evo.getSjbh(),ywlx, desc + "，请办理。","","",dbr,false, user);
                if (task == null) { // 该业务不需要审批
                    EventManager.archiveEvent(conn, evo, user);
                } else {
                    //taskMgr.createApproveWS(request, response, evo.getSjbh(), ywlx, conn);
                }
         }

     }
}