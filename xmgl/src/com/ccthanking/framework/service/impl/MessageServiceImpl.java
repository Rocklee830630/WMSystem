
package com.ccthanking.framework.service.impl;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccthanking.common.vo.XmxxVO;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.coreapp.orgmanage.MenuManager;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.MenuVo;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.message.messagemgr.MessageInfoVO;
import com.ccthanking.framework.service.MessageService;
import com.ccthanking.framework.util.Encipher;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

@Service
public class MessageServiceImpl implements MessageService {
	private User currentUser = null;
	

@Override
	public String getMessage (User user) throws Exception {
		
		String userid = user.getAccount();
		String json = "";
		int c = 0;
		String[][] messages = DBUtil.query("select TITLE,CONTENT from  fs_message_info where USERTO = '"+userid+"' and STATE = '1' and rownum<=5 order by OPTIME desc");
		String[][] mcount = DBUtil.query("select count(1) from  fs_message_info where USERTO = '"+userid+"' and STATE = '1'");
		if(messages!=null&&messages.length>0)
		{
			json +="{count:"+mcount[0][0]+",messages:[";
			for(int i = 0;i<messages.length;i++)
			{
				json +="{title:'"+messages[i][0]+"',content:'"+messages[i][1]+"'},";
			}
			json +="]}";
		}else{
			json = "{count:0}";
		}
		
		
		return json;
		
	}	
@Override
public String getUserMessage(String json,User user) throws Exception {
	Connection conn = DBUtil.getConnection();
	String domresult = "";
	try {

	QueryConditionList list = RequestUtil.getConditionList(json);
	PageManager page =  RequestUtil.getPageManager(json);
	String orderFilter = RequestUtil.getOrderFilter(json);
	String condition = list == null ? "" : list.getConditionWhere();
	
	condition +=" order by state desc , OPTIME desc";
	if (page == null)
		page = new PageManager();
		page.setFilter(condition);
		conn.setAutoCommit(false);
		String sql = "select opid, userfrom, userfromname, userto, usertoname, title, content, optime, sysmessage, emailmessage, smsmessage, deloper, deltime, state, accessory, linkurl, sjbh, ywlx from FS_MESSAGE_INFO";
		BaseResultSet bs = DBUtil.query(conn, sql, page);
		bs.setFieldDic("STATE", "XXZT");

		bs.setFieldDateFormat("optime", "yyyy-MM-dd HH:mm:ss");
		domresult = bs.getJson();
		
	} catch (Exception e) {
		e.printStackTrace(System.out);
	
	} finally {
		if (conn != null)
			conn.close();
	}
	return domresult;
}

	@Override
	public String changeZt(String json, User user, HttpServletRequest request)
			throws Exception {
		Connection conn = DBUtil.getConnection();
		String resultVO = null;
		MessageInfoVO vo = new MessageInfoVO();
		String opid = request.getParameter("opid");

		try {
			conn.setAutoCommit(false);
			vo.setOPID(opid);
            vo =(MessageInfoVO)(Pub.getVOByCondition(conn, vo))[0];
            vo.setSTATE("0");
			// 插入
			BaseDAO.update(conn, vo);
			resultVO = vo.getRowJson();
			conn.commit();
			
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace(System.out);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return resultVO;
	}

	@Override
	public Object findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List find(List ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int remove(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int remove(List ids) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Object bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Object bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
