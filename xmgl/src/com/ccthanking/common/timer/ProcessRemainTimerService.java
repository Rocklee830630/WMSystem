package com.ccthanking.common.timer;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoader;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.QuerySet;
import com.ccthanking.framework.util.Pub;

public class ProcessRemainTimerService {
    private String para ; 
    
    /**
     * 审批办理超期提醒定时器
     * @throws Exception
     */
	public void ProcessRemainTimer() throws Exception{
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			ServletContext context = ContextLoader.getCurrentWebApplicationContext().getServletContext();
			showAllRemain(context,conn);
			conn.commit();
		} catch (Exception e) {
			// TODO: handle exception
			DBUtil.rollbackConnetion(conn);
			e.printStackTrace();
		}finally{
			DBUtil.closeConnetion(conn);
		}
	}
	/**
	 * 查询所有快要超期和已经超期但未办理的待办信息，并发chrome桌面提醒功能
	 * @param conn	数据库连接
	 * @throws Exception
	 */
	private void showAllRemain(ServletContext application,Connection conn) throws Exception{
		/**
		Date sysdate = Pub.getCurrentDate();
		if(conn==null){
			conn = DBUtil.getConnection();
		}
		String sql = "select * from ap_task_schedule t where rwzt='01' and rwlx=2 and sysdate -t.shedule_time >0 ";//超期办理
		QuerySet qs = DBUtil.executeQuery(sql, null,conn);
		if(qs.getRowCount()>0){
			for(int i =0;i<qs.getRowCount();i++)
			{
				 DwrService.remindToPerson(application, new String[] { "您待办理的审批业务:"+qs.getString(i+1, "MEMO")+" 已经超期，请及时处理", qs.getString(i+1, "DBRYID")});
			}
		}
		 sql = "select * from ap_task_schedule t where rwzt='01' and rwlx=2 and (t.shedule_time -sysdate)*24  >=4 ";//超期办理
		 qs = DBUtil.executeQuery(sql, null,conn);
		if(qs.getRowCount()>0){
			for(int i =0;i<qs.getRowCount();i++)
			{
				 DwrService.remindToPerson(application, new String[] { "您待办理的审批业务:"+qs.getString(i+1, "MEMO")+" 还有4个小时就要超期，请及时处理", qs.getString(i+1, "DBRYID")});
			}
		}
		
		**/
		
	}
	public String getPara() {
		return para;
	}
	public void setPara(String para) {
		this.para = para;
	}
}
