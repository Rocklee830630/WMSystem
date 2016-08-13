package com.ccthanking.spflow;

import com.ccthanking.common.EventManager;
import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.util.*;

import java.sql.Connection;
import com.ccthanking.framework.common.*;
import com.ccthanking.framework.coreapp.aplink.TaskMgrBean;
import java.lang.reflect.*;

/**
 * 
 * @author
 * @des 审批归档调用类,误删
 */
public class ArchiveTaskBO
{

	public void operationTaskBack(String eventid, String ywlx, Object obj,
			Connection conn, int sjzt) throws Exception
	{

		 User user = (User) (obj);
		// TaskMgrBean bo = new TaskMgrBean();
		// PageManager page = new PageManager();
		try 
		{
			switch(sjzt)
			{
			  case 2: 
				  task(conn, ywlx, eventid,user,2);
				  break;
			  case 3:
				  task(conn, ywlx, eventid,user,3);
				  break;
			}
			
		} catch (Exception e) 
		{ 
			System.out.println(e.toString());
			throw new CustomException(e.getMessage());
		} 
	}

	// 方法列表
	private void task(Connection conn, String ywlx, String eventid,User user,int sjzt)
			throws Exception
	{

		Class[] parameterTypes = new Class[] { java.sql.Connection.class,
				java.lang.String.class,User.class };
		Method method = null;
		Field field = null;
		Boolean flag = new Boolean(true);
		String methodName = "";
		String classpath = "";
		switch(sjzt)
		{
		  case 2: 
			  classpath = "com.ccthanking.spflow.ArchiveMethodList";
			  break;
		  case 3:
			  classpath = "com.ccthanking.spflow.DisApproveMethodList";
			  break;
		}
		Class<?> methodClass = Class
				.forName(classpath);
		try 
		{
			field = methodClass.getField("YWLX" + ywlx);
			method = methodClass.getMethod(field.get("YWLX" + ywlx).toString(),
					parameterTypes);
		} catch (NoSuchFieldException e) 
		{
			
			method = methodClass.getMethod("defaultArchiveMethod",parameterTypes);
			//throw new CustomException("NoSuchFieldException:" + field);
		}
		try 
		{
			if (method != null) 
			{
				methodName = method.getName();
				flag = (Boolean) method.invoke(null, new Object[] { conn,
						eventid,user });
			}
            if(!flag.booleanValue())
            	throw new CustomException("归档方法错误！");
		} catch (Exception ex) 
		{
			if (!flag.booleanValue()) 
			{
				System.out.println(methodName + "抛出错误:" + ex.getMessage());
				throw new CustomException(methodName + "抛出错误:"
						+ ex.getMessage());
			} else
				ex.printStackTrace();
		}
	}

	/**
	 * 据说这些是通用可以做的事
	 * 
	 * @param eventID
	 * @param YWLX
	 * @param user
	 * @param conn
	 * @throws Exception
	 */
	public static void publicToDo(String eventID, String YWLX, User user,
			Connection conn) throws Exception {
		try {
			TaskMgrBean bo = new TaskMgrBean();
			EventVO event = EventManager.getEventByID(conn, eventID);
			bo.doPrintTask(conn, eventID, YWLX, user);
			event.setGdsj(Pub.getCurrentDate());
			EventManager.archiveEvent(conn, event, user);
			EventManager.setEventState(conn, eventID, "2");
		} catch (Exception e) {
			System.out.println("审批归档时出错:\n" + e.getMessage());
			throw new CustomException("审批归档时出错" + e.getMessage());
		}
	}
	
	/**
	 * 结束特送流程
	 */
	public static void publicToDo(String eventID, String YWLX, User user,
			Connection conn,String jsrYj) throws Exception {
		try {
			TaskMgrBean bo = new TaskMgrBean();
			EventVO event = EventManager.getEventByID(conn, eventID);
			bo.doPrintTask(conn, eventID, YWLX, jsrYj, user);
			event.setGdsj(Pub.getCurrentDate());
			EventManager.archiveEvent(conn, event, user);
			EventManager.setEventState(conn, eventID, "2");
		} catch (Exception e) {
			System.out.println("审批归档时出错:\n" + e.getMessage());
			throw new CustomException("审批归档时出错" + e.getMessage());
		}
	}
	
	
	/**
	 * 可以比较方便的多做一件事
	 * @param eventID
	 * @param YWLX
	 * @param user
	 * @param sql
	 * @param obj
	 * @param conn
	 * @throws Exception
	 */
	public static void customToDo(String eventID, String YWLX, User user,
			String sql, Object[] obj, Connection conn) throws Exception {
		try {
			publicToDo(eventID, YWLX, user, conn);
			if (sql != null)
				DBUtil.executeUpdate(conn, sql, obj);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 可以比较方便的多做一些事
	 * @param eventID
	 * @param YWLX
	 * @param user
	 * @param sql
	 * @param obj
	 * @param conn
	 * @throws Exception
	 */
	public static void customToDo(String eventID, String YWLX, User user,
			String[] sql, Object[][] obj, Connection conn) throws Exception {
		try {
			publicToDo(eventID, YWLX, user, conn);
			if (sql != null)
				for (int i = 0; i < sql.length; i++)
					if (sql[i] != null)
						DBUtil.executeUpdate(conn, sql[i], obj[i]);
					else
						continue;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
