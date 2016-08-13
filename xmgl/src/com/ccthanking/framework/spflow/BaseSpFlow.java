package com.ccthanking.framework.spflow;

import java.sql.Connection;

import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.EventVO;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.aplink.TaskMgrBean;
import com.ccthanking.framework.coreapp.aplink.TaskVO;
import com.ccthanking.framework.util.Pub;


abstract class BaseSpFlow
{
	private String ywlx = "";
	private String sjbh = "";
	private String desc = "";
	//abstract public boolean beforeSpFlow();
	//abstract public boolean afterSpFlow();
	abstract public void customOperation();
	protected String create(Object obj) throws java.sql.SQLException
	{
		   String message = "";
		   User user = (User)obj;
		   EventVO evo = null; //事件对象
		   Connection conn = DBUtil.getConnection();
		   try
		   {
			 conn.setAutoCommit(false);
			 //if(beforeSpFlow() == false) return "";
			 evo = EventManager.getEventByID(getSJBH());
		     TaskMgrBean taskMgr = new TaskMgrBean(); //调用 任务管理器
	         String name = Pub.getDictValueByCode("YWLX",getYWLX()); //翻译业务类型为汉字描述
	         if("".equals(getDESC()))
	        	 desc = user.getOrgDept().getBmjc()+" ["+user.getName()+"] 办理 [" + name + "] 业务，请审批。";
	         TaskVO task = taskMgr.createApproveTask(conn,evo.getSjbh(),getYWLX(),getDESC(), user ); //创建审批任务
	         if(task==null) //根据返回的任务对象判断下一步的操作
	         { // 该业务不需要审批
	            EventManager.archiveEvent(conn,evo,user);
	         }
	         else
	         {
				 //可在此处生成文书
	             //taskMgr.createApproveWS(request,response,sjbh,ywlx,conn);
	             // 审批流已经发起
	         }
	         //if(afterSpFlow() == false) return "";
	         conn.commit();
		   }catch(Exception e)
		   {
			   conn.rollback();
	           e.printStackTrace(System.out);
	           //Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		   }finally
	       {
	           if (conn != null)
	               conn.close();
	           conn = null;
	       }
		  return message;
	}
	protected String archive(Object obj) throws Exception
	{
		String message = "";
        Connection conn = DBUtil.getConnection();
        try{

            User user = (User) obj;
            /*自定义业务begin */
            customOperation();
            /*end */
            EventVO event = EventManager.getEventByID(conn, getSJBH());
            event.setGdsj(Pub.getCurrentDate());
            EventManager.archiveEvent(conn, event, user);
            EventManager.setEventState(conn, getSJBH(), "2");
        }catch(Exception e){
            e.printStackTrace();
            throw e ;
        }
        finally{
            if(conn != null){
                conn.close();
            }
        }
		return message;
	}

	protected String back(Object obj) throws Exception
	{
		String message = "";
		Connection conn = DBUtil.getConnection();
        try{
            User user = (User) obj;
            /*自定义业务begin */
            customOperation();
            /*end */
            EventVO event = EventManager.getEventByID(conn, getSJBH());
            event.setGdsj(Pub.getCurrentDate());
            EventManager.archiveEvent(conn, event, user);
            EventManager.setEventState(conn, getSJBH(), "3");
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        finally{
            if(conn != null){
                conn.close();
            }
        }
		return message;
	}


   public void setYWLX(String ywlx)
   {
	   this.ywlx = ywlx;
   }
   public String getYWLX()
   {
	   return this.ywlx;
   }
   public void setSJBH(String sjbh)
   {
	   this.sjbh = sjbh;
   }
   public String getSJBH()
   {
	   return this.sjbh;
   }
   public void setDESC(String desc)
   {
	   this.desc = desc;
   }
   public String getDESC()
   {
	   return this.desc;
   }
}
