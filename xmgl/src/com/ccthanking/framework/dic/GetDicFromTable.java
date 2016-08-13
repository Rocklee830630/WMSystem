package com.ccthanking.framework.dic;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.util.Pub;

public class GetDicFromTable extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public GetDicFromTable() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset="
				+ request.getCharacterEncoding());
		String vparam = request.getParameter("param");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String operate = request.getParameter("operate");

		if (vparam != null && vparam.indexOf("$") > 0) {
			while (vparam.indexOf("$") > 0) {
				vparam = vparam.substring(0, vparam.indexOf("$"))
						+ "%"
						+ vparam.substring(vparam.indexOf("$") + 1,
								vparam.length());
			}
		}
		String strTabname = null;
		String strCodeCol = null;
		String strValueCol = null;
		String swhere = null;
		String leafCol = null;
		String parentCol = null;
		String spell = null;
		String aspell = null;

		String[] temps = vparam.split(":");
		if (temps == null)
			throw new ServletException("错误的参数设置，无法获取字典内容！");
		strTabname = temps[0];
		strCodeCol = temps[1];
		strValueCol = temps[2];
		if (temps.length >= 4)
			swhere = temps[3];
		if (temps.length >= 5)
			leafCol = temps[4];
		if (temps.length >= 6)
			parentCol = temps[5];
		if (temps.length >= 7)
			spell = temps[6];
		if (temps.length >= 8)
			aspell = temps[7];

		String sql = null;
		if (strTabname != "" && strCodeCol != "" && strValueCol != "") {
			sql = "select " + strCodeCol + "," + strValueCol + ","
					+ (Pub.empty(leafCol) ? "1" : leafCol) + ","
					+ (Pub.empty(parentCol) ? "0" : parentCol) + ","
					+ (Pub.empty(spell) ? "''" : spell) + ","
					+ (Pub.empty(aspell) ? "''" : aspell) + " from "
					+ strTabname;
		} else
			throw new ServletException("错误的参数设置，无法获取字典内容！");
		if (!Pub.empty(swhere))
			sql += " where " + swhere;

		if (!Pub.empty(key)) {
			value = value == null ? "" : value;
			operate = operate == null ? "like" : operate;
			value = operate.trim().equalsIgnoreCase("like") ? "%" + value + "%"
					: value;
			sql += Pub.empty(swhere) ? " where " : " and ";
			sql += key + " " + operate + " '" + value + "'";
		}

		PreparedStatement ps = null;
		ResultSet res = null;
		Connection m_conn = null;
		ArrayList al = new ArrayList();
		try {
			m_conn = DBUtil.getConnection();
			ps = m_conn.prepareStatement(sql);
			res = ps.executeQuery();
			ResultSetMetaData rsmd = res.getMetaData();

			String[] temp = null;
			while (res.next()) {
				temp = new String[rsmd.getColumnCount()];
				for (int n = 0; n < temp.length; n++) {
					temp[n] = res.getString(n + 1);
				}
				al.add(temp);
			}
		} catch (Exception e) {
			throw new ServletException("获取字典内容失败！");
		} finally {
			try {
				if (res != null)
					res.close();
				if (ps != null)
					ps.close();
				if (m_conn != null)
					m_conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement("DATA");
		for (int n = 0; n < al.size(); n++) {
			Element resultRoot = root.addElement("R");
			resultRoot.addAttribute("c", ((String[]) al.get(n))[0]);
			resultRoot.addAttribute("t", ((String[]) al.get(n))[1]);
			if ((String[]) al.get(n) != null
					&& ((String[]) al.get(n))[4] != null)
				resultRoot.addAttribute("s", ((String[]) al.get(n))[4]);
			else
				resultRoot.addAttribute("s", "");
			if ((String[]) al.get(n) != null
					&& ((String[]) al.get(n))[5] != null)
				resultRoot.addAttribute("a", ((String[]) al.get(n))[5]);
			else
				resultRoot.addAttribute("a", "");
			// resultRoot.addAttribute("s",
			// SpellCache.getInstance().getSpell(((String[])al.get(n))[1]));
			// resultRoot.addAttribute("a",
			// SpellCache.getInstance().getAspell(((String[])al.get(n))[1]));
			resultRoot.addAttribute("a", "");
			resultRoot.addAttribute("l", ((String[]) al.get(n))[2]);
			resultRoot.addAttribute("p", ((String[]) al.get(n))[3]);
		}
		try {
			Pub.writeXmlDocument(response, doc, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}