package com.ccthanking.framework.util;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.ccthanking.framework.common.BaseResultSet;

public final class ResponseUtil {
	public ResponseUtil() {
	}

	public static Log log = LogFactory.getLog(ResponseUtil.class);

	// send message xml to client .
	public static void writeMessageXml(HttpServletResponse response,
			String code, String msg) throws Exception {
		response.setContentType("text/html");
		Document domresult = DocumentFactory.getInstance().createDocument();
		Element root = domresult.addElement("RESPONSE");
		root.setAttributeValue("code", code);
		root.setAttributeValue("message", msg);
		OutputStream os = response.getOutputStream();
		XMLWriter writer = new XMLWriter(os);
		writer.write(domresult);
		os.flush();
	}

	/*
	 * add by wuxp 06/12/14 09/09 将结果集变化为xml的格式并且返回到前台
	 */
	public static void writeBaseResultSet2Xml(HttpServletResponse response,
			BaseResultSet result) throws Exception {
		Document domresult = result.getDocument();
		OutputStream os = response.getOutputStream();
		XMLWriter writer = new XMLWriter(os);
		writer.write(domresult);
		os.flush();
	}

	public static void writeDocumentXml(HttpServletResponse response,
			Document doc) throws Exception {
		OutputStream os = response.getOutputStream();
		XMLWriter writer = new XMLWriter(os);
		writer.write(doc);
		os.flush();
		return;
	}

	public static void writeCommand(HttpServletResponse response,
			String commandText) throws Exception {
		response.setContentType("text/html;charset=GBK");
		response.getWriter().print(commandText);
	}

	public static void findForward(HttpServletRequest request,
			HttpServletResponse response, String forward) throws Exception {

		String surl = request.getContextPath() + "/" + request.getServletPath()
				+ "?method=forward&action=" + forward;
		ResponseUtil.writeCommand(response, "FORWARD=" + surl);

	}

}