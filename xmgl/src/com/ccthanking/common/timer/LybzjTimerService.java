package com.ccthanking.common.timer;

import java.sql.Connection;
import java.util.Date;

import com.ccthanking.business.wttb.vo.WttbInfoVO;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.message.messagemgr.PushMessage;
import com.ccthanking.framework.message.messagemgr.sendMessage;
import com.ccthanking.framework.util.Pub;

public class LybzjTimerService {
	
	 /**
     * lybzj到期提醒
     * @throws Exception
     */
	public void lybzjTx() throws Exception{
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			queryLybzj(conn);
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
	 * 查询一个月后即将到期的履约保证金
	 * @param conn
	 * @throws Exception
	 */
	public void queryLybzj(Connection conn) throws Exception{
		
		
		String QUERY_LYBZJ_DQXX = " SELECT gzl.ID, gzl.htid, gzl.sjbh, to_char(gzl.BHYXQZ,'yyyy/MM/dd'), gzl.ywlx, gzl.je, gjs.xmmc, gjs.bdmc , gc.dwmc " 
						+" FROM GC_ZJGL_LYBZJ gzl left join gc_jh_sj gjs on gjs.gc_jh_sj_id = gzl.jhsjid "
						+" left join GC_CJDW gc on gc.gc_cjdw_id = gzl.jndw "
						+" where gzl.jnfs = 'BH' and to_char(gzl.BHYXQZ,'yyyy/MM/dd') = to_char(current_date+30,'yyyy/MM/dd') ";
		String[][] lybzjResult = DBUtil.query(conn, QUERY_LYBZJ_DQXX);
		
		String[] person = queryPerson(conn, PushMessage.LYBZJ_BHTX);
		String msg ="";
		for(int i=0; i<lybzjResult.length; i++){
			msg = "交纳单位："+lybzjResult[i][8]+" 项目："+lybzjResult[i][6]+" 标段："+lybzjResult[i][7]+"。履约保证金保函将于"+lybzjResult[i][3]+"日到期！";
			//PushMessage.push(request, PushMessage.ZJB_LBJ, msg, null, lybzjResult[i][2], lybzjResult[i][4], "保函到期提醒");
			for (int j = 0; j < person.length; j++) {
				if(queryMessageExist(conn,person[i],lybzjResult[i][2])){
					sendMessage.sendMessageToPerson(conn, "保函到期提醒", msg, "superman", person[i], false, false, false, null, null, lybzjResult[i][2], lybzjResult[i][4]);
				}
			}
		}
	}
	
	/**
	 * 获取推送用户
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public String[] queryPerson(Connection conn, String operator_no) throws Exception{
		String[] person = null;
		String sql = " select fpp.userid from FS_PUSHINFO_PSN fpp where fpp.operator_no='"+operator_no+"' ";
		String[][] personResult = DBUtil.query(conn, sql);
		
		person = new String[personResult.length];
		for (int i = 0; i < personResult.length; i++) {
			person[i] = personResult[i][0]; 
		}
		
		return person;
	}
	
	/**
	 * 查看是否已通知
	 * @param conn
	 * @param uname
	 * @param sjbh
	 * @return
	 * @throws Exception
	 */
	public boolean queryMessageExist(Connection conn,String uname,String sjbh) throws Exception{
		boolean flag = false;
		String sql = " select * from FS_MESSAGE_INFO fm where fm.userto = '"+uname+"' and fm.sjbh = '"+sjbh+"' ";
		String[][] messageResult = DBUtil.query(conn, sql);
		
		if(messageResult.length==0){
			flag = true;
		}
		return flag;
	}
}
