package com.ccthanking.framework.params.AppPara;

import java.io.OutputStream;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.ccthanking.framework.Constants;
import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.log.LogManager;
import com.ccthanking.framework.params.ParaManager;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;

public class ApParaAction extends BaseDispatchAction {

	public ApParaAction() {
	}

	public ActionForward getApParaList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String condition = list == null ? "" : list.getConditionWhere();
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Document domresult = null;
		OutputStream os = null;
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "select sn,operationtype,unitid,parakey,paraname,memo,apptype,"
					+ " orglevel,applicateion,paravalue1,paravalue2,paravalue3,paravalue4"
					+ " from fs_para_app_configure ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			bs.setFieldDic("operationtype", "YWLX");
			bs.setFieldOrgDept("unitid");
			domresult = bs.getDocument();
			os = response.getOutputStream();
			XMLWriter writer = new XMLWriter(os);
			writer.write(domresult);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			domresult = WriteXmlMessage(domresult, "意外错误！！" + e.toString(),
					"ERRMESSAGE");
			if (os == null) {
				os = response.getOutputStream();
			}
			XMLWriter writer = new XMLWriter(os);
			writer.write(domresult);
			os.flush();
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	public ActionForward doSaveApPara(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String opid = Pub.val(request, "paraoid");
		String key = Pub.val(request, "key");
		boolean isNew = Pub.empty(opid);
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		Document domresult = null;
		OutputStream os = null;
		Connection conn = DBUtil.getConnection("rkyw");
		conn.setAutoCommit(false);
		try {
			Element root = doc.getRootElement();
			String paraoid = root.element("SN").getText();
			if (!Pub.empty(paraoid) && !paraoid.equals(opid))
				throw new Exception("error get paraoid");
			if (Pub.empty(paraoid))
				paraoid = DBUtil.getSequenceValue("ap_para_configure_sn", conn);
			String ywlx = root.element("OPERATIONTYPE").getText();
			String dwdm = root.element("UNITID") == null ? null : root.element(
					"UNITID").getText();
			String paraname = root.element("PARANAME") == null ? null : root
					.element("PARANAME").getText();
			String value1 = root.element("PARAVALUE1") == null ? null : root
					.element("PARAVALUE1").getText();
			String value2 = root.element("PARAVALUE2") == null ? null : root
					.element("PARAVALUE2").getText();
			String value3 = root.element("PARAVALUE3") == null ? null : root
					.element("PARAVALUE3").getText();
			String value4 = root.element("PARAVALUE4") == null ? null : root
					.element("PARAVALUE4").getText();
			String memo = root.element("MEMO") == null ? null : root.element(
					"MEMO").getText();
			String apptype = root.element("APPTYPE") == null ? "1" : root
					.element("APPTYPE").getText();
			String orglevel = root.element("ORGLEVEL") == null ? null : root
					.element("ORGLEVEL").getText();
			String applicateion = root.element("APPLICATEION") == null ? "hnpmi"
					: root.element("APPLICATEION").getText();
			String sql = null;
			String str = null;
			if (isNew) {
				sql = "insert into fs_para_app_configure cols(apptype,operationtype,"
						+ " unitid,orglevel,applicateion,parakey,paraname,paravalue1,"
						+ " paravalue2,paravalue3,paravalue4,memo,sn) "
						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
				str = "添加";
			} else {
				sql = "update fs_para_app_configure set apptype=?,operationtype=?,unitid=?,"
						+ " orglevel=?,applicateion=?,parakey=?,paraname=?,paravalue1=?,"
						+ " paravalue2=?,paravalue3=?,paravalue4=?,memo=? where sn=?";
				str = "修改";
			}
			Object[] para = { apptype, ywlx, dwdm, orglevel, applicateion, key,
					paraname, value1, value2, value3, value4, memo, paraoid };
			DBUtil.executeUpdate(conn, sql, para);
			conn.commit();
			AppParaVO vo = ParaManager.getInstance().getAppParameter(paraoid);
			if (vo == null)
				vo = new AppParaVO();
			vo.setValue(paraoid, apptype, ywlx, dwdm, orglevel, applicateion,
					key, paraname, value1, value2, value3, value4, memo);
			ParaManager.getInstance().setAppParameter(vo);
			if (isNew)
				LogManager.writeUpdateLog("应用参数管理->" + str + "参数", "业务类型："
						+ ywlx + " 部门:" + dwdm + " ID:" + paraoid + " VALUE1:"
						+ value1 + " KEY:" + key, str,
						LogManager.RESULT_SUCCESS, user);
			else
				LogManager.writeUpdateLog("应用参数管理->" + str + "参数", str, "业务类型："
						+ ywlx + " 部门:" + dwdm + " ID:" + paraoid + " VALUE1:"
						+ value1 + " KEY:" + key, LogManager.RESULT_SUCCESS,
						user);
			domresult = WriteXmlMessage(domresult, "保存成功！", "MESSAGE");
			os = response.getOutputStream();
			XMLWriter writer = new XMLWriter(os);
			writer.write(domresult);
			os.flush();
		} catch (Exception e) {
			if (conn != null)
				conn.rollback();
			domresult = WriteXmlMessage(domresult, "意外错误！\n" + e.getMessage(),
					"ERRMESSAGE");
			if (os == null) {
				os = response.getOutputStream();
			}
			XMLWriter writer = new XMLWriter(os);
			writer.write(domresult);
			os.flush();
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

	public ActionForward doDeleteApPara(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		Document doc = RequestUtil.getDocument(request);
		Element root = doc.getRootElement();
		String opid = root.selectSingleNode("SN").getStringValue();
		String ywlx = root.selectSingleNode("OPERATIONTYPE").getStringValue();
		String dwdm = root.element("UNITID") == null ? null : root.element(
				"UNITID").getText();
		String key = root.element("PARAKEY") == null ? null : root.element(
				"PARAKEY").getText();
		String paraname = root.element("PARANAME") == null ? null : root
				.element("PARANAME").getText();
		String value1 = root.element("PARAVALUE1") == null ? null : root
				.element("PARAVALUE1").getText();
		String value2 = root.element("PARAVALUE2") == null ? null : root
				.element("PARAVALUE2").getText();
		String value3 = root.element("PARAVALUE3") == null ? null : root
				.element("PARAVALUE3").getText();
		String value4 = root.element("PARAVALUE4") == null ? null : root
				.element("PARAVALUE4").getText();
		String memo = root.element("MEMO") == null ? null : root
				.element("MEMO").getText();
		String apptype = root.element("APPTYPE") == null ? "1" : root.element(
				"APPTYPE").getText();
		String orglevel = root.element("ORGLEVEL") == null ? null : root
				.element("ORGLEVEL").getText();
		String applicateion = root.element("APPLICATEION") == null ? "hnpmi"
				: root.element("APPLICATEION").getText();

		OutputStream os = null;
		String res = null;
		Connection conn = DBUtil.getConnection("rkyw");
		try {
			conn.setAutoCommit(false);
			DBUtil.execSql(conn, "delete fs_para_app_configure where SN='" + opid
					+ "'");
			conn.commit();
			AppParaVO vo = ParaManager.getInstance().getAppParameter(opid);
			ParaManager.getInstance().deleteAppParameter(vo);
			LogManager.writeUpdateLog("应用参数管理->删除参数", "业务类型：" + ywlx + " 部门:"
					+ dwdm + " ID:" + opid, "删除", LogManager.RESULT_SUCCESS,
					user);
			doc = WriteXmlMessage(null, "参数已经删除！", "MESSAGE");
		} catch (Exception e) {
			conn.rollback();
			LogManager.writeUpdateLog("应用参数管理->删除参数", "业务类型：" + ywlx + " 部门:"
					+ dwdm + " ID:" + opid, "删除", LogManager.RESULT_FAILURE,
					user);
			doc = WriteXmlMessage(null, "发生异常！\n" + res, "ERRMESSAGE");
		} finally {
			if (conn != null)
				conn.close();
			os = response.getOutputStream();
			XMLWriter writer = new XMLWriter(os);
			writer.write(doc);
			os.flush();
		}
		return null;
	}

	public Document WriteXmlMessage(Document doc, String strMessage,
			String strTagName) {
		try {
			Element root = null;
			if (doc == null) {
				doc = DocumentFactory.getInstance().createDocument();

				root = doc.addElement("RESPONSE");
				root.addAttribute("errorcode", "0");
				root.addAttribute("code", "0");
				Element resultRoot = root.addElement("RESULT");

				resultRoot.addAttribute("currentpagenum", String.valueOf(1));
				resultRoot.addAttribute("recordsperpage", String.valueOf(12));

				resultRoot.addAttribute("totalpage", String.valueOf(0));
				resultRoot.addAttribute("countrows", String.valueOf(0));
			} else {
				root = doc.getRootElement();
			}
			if (root != null && strMessage != null && strTagName != null) {
				root.addElement(strTagName).addText(strMessage);
			}
		} catch (Exception e) {
			System.out
					.println("ZqzCzrkZqzEventAction.WriteXmlMessage(Document doc,String strMessage,String strTagName):"
							+ e.toString());
		}
		return doc;
	}
}
