package com.ccthanking.framework.params.SysPara;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.Vector;

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
import com.ccthanking.framework.util.ParaSetUtil;
import com.ccthanking.framework.util.Pub;
import com.ccthanking.framework.util.QueryConditionList;
import com.ccthanking.framework.util.RequestUtil;
import com.ccthanking.framework.util.ResponseUtil;

public class SysParaConfigureAction extends BaseDispatchAction {

	public SysParaConfigureAction() {
	}

	private static org.apache.log4j.Logger logger = org.apache.log4j.LogManager
			.getLogger("SysParaConfigureAction");

	public Document WriteXmlMessage(Document doc, SysParaConfigureVO tempvo,
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
						Pub.str(tempvo.getSysParaConfigureSn()));
				row.addElement("PARAKEY").addText(
						Pub.str(tempvo.getSysParaConfigureParakey()));
				row.addElement("PARANAME").addText(
						Pub.str(tempvo.getSysParaConfigureParaname()));
				row.addElement("PARAVALUE1").addText(
						Pub.str(tempvo.getSysParaConfigureParavalue1()));
				row.addElement("PARAVALUE2").addText(
						Pub.str(tempvo.getSysParaConfigureParavalue2()));
				row.addElement("PARAVALUE3").addText(
						Pub.str(tempvo.getSysParaConfigureParavalue3()));
				row.addElement("PARAVALUE4").addText(
						Pub.str(tempvo.getSysParaConfigureParavalue4()));
				row.addElement("MEMO").addText(
						Pub.str(tempvo.getSysParaConfigureMemo()));
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
		SysParaConfigureBO tempBO = new SysParaConfigureBO(request.getSession());
		try {
			// 获取VO对象并且赋值给VO对象
			SysParaConfigureVO tempVO = (SysParaConfigureVO) ParaSetUtil
					.getVOInstance(request, SysParaConfigureVO.class,
							"SYS_PARA_CONFIGURE");
			String strMessage = tempBO.insert(tempVO, request);
			if (strMessage.length() > 0) {
				Pub.writeXmlErrorMessage(response, strMessage);
				return null;
			}
			tempVO = (SysParaConfigureVO) tempBO.selectById(strMessage);
			this.WriteXmlMessage(null, tempVO, response);

		} catch (Exception e) {
			logger.error(e);
			Pub.writeXmlErrorMessage(response, e.getMessage());
		}
		return null; // mapping.findForward("sucess");
	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SysParaConfigureVO tempVO = (SysParaConfigureVO) ParaSetUtil
				.getVOInstance(request, SysParaConfigureVO.class,
						null);
		SysParaConfigureBO tempBO = new SysParaConfigureBO(request.getSession());
		String strRes = "";
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
		String sn = (String) request.getParameter("SN");
		SysParaConfigureVO tempVO = new SysParaConfigureVO();
		String strRes = "";
		tempVO.setSysParaConfigureSn(sn);
		SysParaConfigureBO tempBO = new SysParaConfigureBO(request.getSession());
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

	public ActionForward getSysParamsList(ActionMapping mapping,
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
			String sql = "SELECT SN,PARAKEY,PARANAME,PARAVALUE1,PARAVALUE2,PARAVALUE3,PARAVALUE4,MEMO FROM FS_PARA_SYS_CONFIGURE ";
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
