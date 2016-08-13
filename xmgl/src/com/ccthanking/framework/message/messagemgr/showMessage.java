package com.ccthanking.framework.message.messagemgr;

import java.sql.Connection;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.params.ParaManager;

public class showMessage {
	
	static private showMessage instance;

	public showMessage() {
	}

	// 弹出消息页面
	public boolean isShow(String userAccount, String userDept) throws Exception {
		String[][] bs = null;
		Connection conn = null, connM = null;
		try {
			conn = DBUtil.getConnection("p3");
			String condition = "";
			connM = DBUtil.getConnection("p3");
			String sqlM = "SELECT USERTO from MESSAGE_INFO";
			String[][] bss = DBUtil.query(connM, sqlM);
			String condition1 = "";
			if (bss == null)
				return false;
			for (int i = 0; i < bss.length; i++) {
				String arrTmp[] = bss[i][0].split(",");
				for (int j = 0; j < arrTmp.length; j++) {
					if (userAccount.equals(arrTmp[j])
							|| userDept.equals(arrTmp[j])) {
						condition1 += " USERTO='" + bss[i][0] + "' OR ";
						break;
					}
				}
			}
			if (!condition1.equals(""))
				condition = " (" + condition1 + " USERTO='allUser') "; // 用户或所属部门
			else
				condition += " 1<>'1'"; // 无返回值

			String sql = "SELECT OPID FROM MESSAGE_INFO WHERE " + condition;
			bs = DBUtil.query(conn, sql);
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
			if (connM != null)
				connM.close();
			connM = null;

		}
		if (bs == null)
			return false;
		else
			return true;
	}

	// 系统界面左部分公告
	public String[][] queryResult(String userAccount, String userDept)
			throws Exception {
		String[][] bs = null;
		Connection conn = null;
		try {
			String condition = "";
			Connection connM = DBUtil.getConnection("p3");
			String sqlM = "SELECT USERTO from MESSAGE_INFO";
			String[][] bss = DBUtil.query(connM, sqlM);
			String condition1 = "";
			try {
				for (int i = 0; i < bss.length; i++) {
					String arrTmp[] = bss[i][0].split(",");
					for (int j = 0; j < arrTmp.length; j++) {
						if (userAccount.equals(arrTmp[j])
								|| userDept.equals(arrTmp[j])) {
							condition1 += " USERTO='" + bss[i][0] + "' OR ";
							break;
						}
					}
				}
			} catch (Exception ex) {
				System.out.println("ex::" + ex);
			}
			if (!condition1.equals(""))
				condition = " (" + condition1 + " USERTO='allUser') "; // 用户或所属部门
			else
				condition += " 1='2'"; // 无返回值
			conn = DBUtil.getConnection("p3");
			String sql = "SELECT TITLE,OPID FROM MESSAGE_INFO WHERE "
					+ condition + "  AND ROWNUM<11 ORDER BY OPID DESC";
			bs = DBUtil.query(conn, sql);
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			if (conn != null)
				conn.close();
			conn = null;
		}
		return bs;
	}

	// 主界面左边显示
	public boolean leftShow() throws Exception {
		String isFlag = "";
		try {
			ParaManager paramanager = ParaManager.getInstance();
			isFlag = paramanager.getSysParameter("showMessage")
					.getSysParaConfigureParavalue1();
		} catch (Exception ex) {
			System.err.println(ex);
		}

		if (isFlag.equals("true"))
			return true;
		else
			return false;
	}

	// 主界面弹出显示
	public boolean popShow() throws Exception {
		String isFlag = "";
		try {
			ParaManager paramanager = ParaManager.getInstance();
			isFlag = paramanager.getSysParameter("showMessage")
					.getSysParaConfigureParavalue2();
		} catch (Exception ex) {
			System.err.println(ex);
		}

		if (isFlag.equals("true"))
			return true;
		else
			return false;
	}

	static synchronized public showMessage getInstance() {
		if (instance == null) {
			instance = new showMessage();
		}
		return instance;
	}

	public static void main(String[] args) {

	}

}