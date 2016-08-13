package com.ccthanking.framework.params.UserPara;

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

import com.ccthanking.framework.Constants;
import com.ccthanking.framework.base.BaseDispatchAction;
import com.ccthanking.framework.common.BaseResultSet;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;
import com.ccthanking.framework.util.ParaSetUtil;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.framework.util.ResponseUtil;

public class UserParaConfigureAction extends BaseDispatchAction {

	public UserParaConfigureAction() {
	}

	private static org.apache.log4j.Logger logger = org.apache.log4j.LogManager
			.getLogger("UserParaConfigureAction");

	public Document WriteXmlMessage(Document doc, UserParaConfigureVO tempvo,
			HttpServletResponse response) {
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
				Element row = resultRoot.addElement("ROW");
				row.addElement("SN").addText(
						Pub.str(tempvo.getUserParaConfigureSn()));
				row.addElement("USERACCOUNT").addText(
						Pub.str(tempvo.getUserParaConfigureUseraccount()));
				row.addElement("USERNAME").addText(
						Pub.str(tempvo.getUserParaConfigureUsername()));
				row.addElement("USERID").addText(
						Pub.str(tempvo.getUserParaConfigureUserid()));
				row.addElement("USERLEVEL").addText(
						Pub.str(tempvo.getUserParaConfigureUserlevel()));
				row.addElement("PARAKEY").addText(
						Pub.str(tempvo.getUserParaConfigureParakey()));
				row.addElement("PARANAME").addText(
						Pub.str(tempvo.getUserParaConfigureParaname()));
				row.addElement("PARAVALUE1").addText(
						Pub.str(tempvo.getUserParaConfigureParavalue1()));
				row.addElement("PARAVALUE2").addText(
						Pub.str(tempvo.getUserParaConfigureParavalue2()));
				row.addElement("PARAVALUE3").addText(
						Pub.str(tempvo.getUserParaConfigureParavalue3()));
				row.addElement("PARAVALUE4").addText(
						Pub.str(tempvo.getUserParaConfigureParavalue4()));
				row.addElement("MEMO").addText(
						Pub.str(tempvo.getUserParaConfigureMemo()));
				Element message = root.addElement("MESSAGE");
				message.addText("保存成功!");
			} else {
				root = doc.getRootElement();
			}

			OutputStream os = response.getOutputStream();
			os.write(doc.asXML().getBytes("GBK"));
			os.flush();

		} catch (Exception e) {
			System.out
					.println("ZqzCzrkZqzEventAction.WriteXmlMessage(Document doc,String strMessage,String strTagName):"
							+ e.toString());
		}
		return doc;
	}

	public ActionForward insert(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserParaConfigureVO tempVO = null;
		UserParaConfigureBO tempBO = new UserParaConfigureBO(
				request.getSession());
		String strMessage = null;
		try {
			tempVO = (UserParaConfigureVO) ParaSetUtil.getVOInstance(request,
					UserParaConfigureVO.class, "USER_PARA_CONFIGURE");
			strMessage = tempBO.insert(tempVO, request);
			if (strMessage.length() > 0) {
				Pub.writeXmlErrorMessage(response, strMessage);
			}
			tempVO = (UserParaConfigureVO) tempBO.selectById(tempVO
					.getUserParaConfigureSn());
			this.WriteXmlMessage(null, tempVO, response);

		} catch (Exception e) {
			Pub.writeXmlErrorMessage(response, "意外错误！！" + e.toString());
		}
		return null; // mapping.findForward("sucess");
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String strRes = "";
		UserParaConfigureVO tempVO = (UserParaConfigureVO) ParaSetUtil
				.getVOInstance(request, SysParaConfigureVO.class,
						"USER_PARA_CONFIGURE");

		UserParaConfigureBO tempBO = new UserParaConfigureBO(
				request.getSession());
		try {
			strRes = tempBO.update(tempVO, request);
			if (strRes.length() == 0) {
				Pub.writeXmlInfoUpdateOK(response);
			}
		} catch (Exception e) {
			Pub.writeXmlErrorMessage(response, e.getMessage());
			return mapping.findForward("faill");
		}
		return null; // mapping.findForward("sucess");
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String strRes = "";
		String SN = request.getParameter("SN");
		UserParaConfigureVO tempVO = new UserParaConfigureVO();
		tempVO.setUserParaConfigureSn(SN);
		UserParaConfigureBO tempBO = new UserParaConfigureBO(
				request.getSession());
		try {

			strRes = tempBO.delete(tempVO, request);
			if (strRes.length() == 0)
				Pub.writeXmlInfoDeleteOK(response);
		} catch (Exception e) {
			Pub.writeXmlErrorMessage(response, e.getMessage());
			return mapping.findForward("faill");
		}
		return null; // mapping.findForward("sucess");
	}

	public ActionForward getUserParamsList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Document doc = RequestUtil.getDocument(request);
		QueryConditionList list = RequestUtil.getConditionList(doc);
		PageManager page = RequestUtil.getPageManager(doc);
		String condition = list == null ? "" : list.getConditionWhere();
		if (Pub.empty(condition))
			condition = " rownum < " + Constants.MAX_RECORD_LIMITED;
		condition += " ORDER  BY SN ";
		if (page == null)
			page = new PageManager();
		page.setFilter(condition);
		Connection conn = DBUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "SELECT SN,USERACCOUNT,USERNAME,USERID,USERLEVEL,PARAKEY,PARANAME,PARAVALUE1,PARAVALUE2,PARAVALUE3,PARAVALUE4,MEMO FROM USER_PARA_CONFIGURE ";
			BaseResultSet bs = DBUtil.query(conn, sql, page);
			ResponseUtil.writeDocumentXml(response, bs.getDocument());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Pub.writeXmlErrorMessage(response, "意外错误!");
		} finally {
			if (conn != null)
				conn.close();
		}
		return null;
	}

}
