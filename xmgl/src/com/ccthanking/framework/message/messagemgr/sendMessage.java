package com.ccthanking.framework.message.messagemgr;

import java.sql.Connection;
import java.util.Date;

import com.ccthanking.framework.base.BaseDAO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.RandomGUID;

public class sendMessage {
	
	public sendMessage() {
	}

	public static void sendMessageToDept(Connection conn, String strTitle,
			String strContent, String strUserfrom, String strDept,
			boolean isSys, boolean isMail, boolean isSms, String fjbhs)
			throws Exception {
		sendMessageToDept(conn, strTitle, strContent, strUserfrom, strDept,
				isSys, isMail, isSms, fjbhs, "", "", "");
	}

	/**
	 * 
	 * @param strTitle消息标题
	 * @param strContent消息内容
	 * @param strUserfrom发信人
	 * @param strDept
	 *            收信单位
	 * @param isSys
	 *            是否消息发送
	 * @param isMail是否mail发送
	 * @param isSms是否短信发送
	 * @param fjbhs所传附件编号
	 */
	public static void sendMessageToDept(Connection conn, String strTitle,
			String strContent, String strUserfrom, String strDept,
			boolean isSys, boolean isMail, boolean isSms, String fjbhs,
			String linkurl, String sjbh, String ywlx) throws Exception {

		String[][] usersAccount = DBUtil.query(conn,
				"select account from org_person where DEPARTMENT = '" + strDept
						+ "' and FLAG='1'");
		if (usersAccount != null) {
			for (int i = 0; i < usersAccount.length; i++) {
				sendMessageToPerson(conn, strTitle, strContent, strUserfrom,
						usersAccount[i][0], isSys, isMail, isSms, fjbhs,
						linkurl, sjbh, ywlx);
			}
		}
	}

	public static void sendMessageToPerson(Connection conn, String strTitle,
			String strContent, String strUserfrom, String strUserto,
			boolean isSys, boolean isMail, boolean isSms, String fjbhs)
			throws Exception {
		sendMessageToPerson(conn, strTitle, strContent, strUserfrom, strUserto,
				isSys, isMail, isSms, fjbhs, "", "", "");
	}

	/**
	 * 
	 * @param strTitle消息标题
	 * @param strContent消息内容
	 * @param strUserfrom发信人
	 * @param strUserto
	 *            收信人
	 * @param isSys
	 *            是否消息发送
	 * @param isMail是否mail发送
	 * @param isSms是否短信发送
	 * @param fjbhs所传附件编号
	 */
	public static MessageInfoVO sendMessageToPerson(Connection conn, String strTitle,
			String strContent, String strUserfrom, String strUserto,
			boolean isSys, boolean isMail, boolean isSms, String fjbhs,
			String linkurl, String sjbh, String ywlx, String oid) throws Exception {
		MessageInfoVO vo = new MessageInfoVO();
		// 发信人
		User userFrom = UserManager.getInstance().getUserByLoginNameFromNc(strUserfrom);
		// 收信人
		User userTo = UserManager.getInstance().getUserByLoginNameFromNc(strUserto);
		if(userTo==null){
			//-----------------------------------------------
			//add by zhangbr@ccthanking.com
			//如果获取不到收信人，就不能往下执行了，不然会报错
			return null;
		}
		vo.setUSERFROM(strUserfrom); // 发送人编号
		vo.setUSERFROMNAME(userFrom.getName()); // 发送人姓名
		vo.setUSERTO(strUserto); // 收信人编号
		vo.setUSERTONAME(userTo.getName()); // 收信人姓名
		vo.setTITLE(strTitle); // 消息标题
		vo.setCONTENT(strContent); // 消息内容
		if (isSys) {
			vo.setSYSMESSAGE("1"); // 系统消息
		}
		if (isMail) {
			vo.setEMAILMESSAGE("1"); // 邮件
		}
		if (isSms) {
			vo.setSMSMESSAGE("1"); // 短信
		}

		vo.setOPTIME(new Date()); // 发送时间
		vo.setSTATE("1"); // 正常状态
		if (!Pub.empty(fjbhs)) {
			vo.setACCESSORY(fjbhs);
		}
		if (!Pub.empty(linkurl)) {
			vo.setLINKURL(linkurl);
		}
		if (!Pub.empty(sjbh)) {
			vo.setSJBH(sjbh);
		}
		if (!Pub.empty(ywlx)) {
			vo.setYWLX(ywlx);
		}
		if (!Pub.empty(oid)) {
			vo.setOPERATOR_NO(oid);
		}
		if (isMail) {
		}
		if (isSys) {
			// 短信方式暂放
		}

		BaseDAO dao = new BaseDAO();
		//这里原值是使用oracle序列
		//现在改为使用java生成的编号
//		vo.setOPID(DBUtil.getSequenceValue("MESSAGE_INFO_OPID", conn));
		vo.setOPID(new RandomGUID().toString());
		dao.insert(conn, vo);
		return vo;
	}
	
	public static void sendMessageToPerson(Connection conn, String strTitle,
			String strContent, String strUserfrom, String strUserto,
			boolean isSys, boolean isMail, boolean isSms, String fjbhs,
			String linkurl, String sjbh, String ywlx) throws Exception {
		sendMessageToPerson(conn, strTitle, strContent, strUserfrom, strUserto, isSys, isMail, isSms, fjbhs, linkurl, sjbh, ywlx, null);
	}
}