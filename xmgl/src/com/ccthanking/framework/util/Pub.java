package com.ccthanking.framework.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import oracle.jdbc.OraclePreparedStatement;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.OrgDept;
import com.ccthanking.framework.common.PageManager;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager;
import com.ccthanking.framework.coreapp.orgmanage.OrgRoleManager;
import com.ccthanking.framework.coreapp.orgmanage.UserManager;
import com.ccthanking.framework.dic.Dics;
import com.ccthanking.framework.dic.TreeNode;
import com.ccthanking.framework.params.ParaManager;
import com.ccthanking.framework.params.SysPara.SysParaConfigureVO;

/**
 * 公用函数类
 */
public class Pub {

	public static String username = "weblogic";

	public static String password = "weblogic";

	public static String ejb_url = "t3://localhost:7001";

	public static String ini_ctx = "weblogic.jndi.T3InitialContextFactory";

	public static Context ctx = null;

	public static Properties properties = null;

	public static final String PREFIX = "base";

	public static final int MAX_DEFAULT_CONDITIONS = 16;

	public static String getUserAccount(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		if (user != null && user.getAccount() != null)
			return user.getAccount();
		return "";
	}

	public static String getUserDepartment(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		if (user != null && user.getDepartment() != null)
			return user.getDepartment();
		return "";
	}
	public static String getUserDepartmentById(String userid) throws Exception {
		User user = UserManager.getInstance().getUserByLoginName(userid);
		if (user != null && user.getDepartment() != null)
			return user.getDepartment();
		return "";
	}

	public static String getUserName(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		if (user != null && user.getName() != null)
			return user.getName();
		return "";
	}

	public static String getUserSecrecyLevel(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
		if (user != null && user.getScretLevel() != null)
			return user.getScretLevel();
		return "";
	}

	public static String getDeptParentFjdm(String id) throws Exception {
		return Pub.getDeptParentFjdm(OrgDeptManager.getInstance().getDeptByID(
				id));
	}

	public static String getDeptParentFjdm(OrgDept dept) {
		try {
			int level = Pub.toInt(dept.getDeptType());
			if (level == 3)
				return dept.getDeptID();
			OrgDept parent = dept;
			for (int i = level; i > 2; i--) {
				parent = parent.getParent();
				if (parent != null) {
					switch (Pub.toInt(parent.getDeptType())) {
					case 3:
						return parent.getDeptID();
					default:
						break;
					}
				} else
					return null;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getDeptParentSjdm(String id) throws Exception {
		return Pub.getDeptParentSjdm(OrgDeptManager.getInstance().getDeptByID(
				id));
	}

	public static String getDeptParentSjdm(OrgDept dept) {
		try {
			int level = Pub.toInt(dept.getDeptType());
			if (level == 2)
				return dept.getDeptID();
			OrgDept parent = dept;
			for (int i = level; i > 1; i--) {
				parent = parent.getParent();
				if (parent != null) {
					switch (Pub.toInt(parent.getDeptType())) {
					case 2:
						return parent.getDeptID();
					default:
						break;
					}
				} else
					return null;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static Document setDomInfoMessage(Document doc, String message) {
		try {
			doc.getRootElement().addElement("MESSAGE").setText(message);
		} catch (Exception e) {
			//
		}
		return doc;
	}

	public static Document setDomErrMessage(Document doc, String message) {
		try {
			doc.getRootElement().addElement("ERRMESSAGE").setText(message);
		} catch (Exception e) {
			//
		}
		return doc;
	}

	public static Document setDomMessage(Document doc, String message,
			String tagName) {
		try {
			doc.getRootElement().addElement(tagName).setText(message);
		} catch (Exception e) {
			//
		}
		return doc;
	}

	public static Document setDomPage(Document doc, PageManager page) {
		try {
			Element element = (Element) doc.selectSingleNode("//RESULT");
			element.addAttribute("currentpagenum",
					String.valueOf(page.getCurrentPage()));
			element.addAttribute("recordsperpage",
					String.valueOf(page.getPageRows()));
			element.addAttribute("countrows",
					String.valueOf(page.getCountRows()));

			element.addAttribute("totalpage",
					String.valueOf(page.getCountPage()));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return doc;
		}
	}

	public static String getDeptNameByID(String id) {
		if (Pub.empty(id))
			return null;
		try {
			return OrgDeptManager.getInstance().getDeptByID(id).getDeptFullName();
		} catch (Exception e) {
			return null;
		}
	}

	public static String getDeptSimepleNameByID(String id) {
		if (Pub.empty(id))
			return null;
		try {
			return OrgDeptManager.getInstance().getDeptByID(id).getBmjc();
		} catch (Exception e) {
			return null;
		}
	}
	public static String getRoleNameByID(String role_id) {
		if (Pub.empty(role_id))
			return null;
		try {
			
			return OrgRoleManager.getInstance().getRole(role_id).getMemo();
		} catch (Exception e) {
			return null;
		}
	}

	public static String getDepttypeByID(String id) {
		if (Pub.empty(id))
			return null;
		try {
			return OrgDeptManager.getInstance().getDeptByID(id).getDeptType();
		} catch (Exception e) {
			return null;
		}
	}

	public static String getMUserNameById(String id) {
		try {
			String userNames = "";
			StringTokenizer t = null;
			if (!Pub.empty(id))
				t = new StringTokenizer(id, ",");
			while (t.hasMoreTokens()) {
				userNames += UserManager.getInstance()
						.getUserByLoginName((String) t.nextElement()).getName()
						+ ",";
			}
			if (userNames.length() > 0)
				userNames = userNames.substring(0, userNames.length() - 1);

			return userNames;
		} catch (Exception e) {
			return null;
		}
	}
	public static String[] getStringSplit(String value,String split){
		String[] s = null;
		ArrayList a = new ArrayList();
		StringTokenizer t = null;
		if (!Pub.empty(value))
			t = new StringTokenizer(value, split);
		while (t.hasMoreTokens()) {
			a.add((String) t.nextElement());
		}
		if(a.size()>0){
			s = new String [a.size()];
			for(int i =0;i<s.length;i++){
				s[i] = (String)a.get(i);
			}
		}
		return s;
	}

	public static String getUserNameByLoginId(String id) {
		try {
			return UserManager.getInstance().getUserByLoginNameFromNc(id).getName();
		} catch (Exception e) {
			return null;
		}
	}

	public static java.util.Date getCurrentDate() {
		// return Calendar.getInstance().getTime();
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyyyMMddHHmmss");
		try {
			return formatter.parse(DBUtil.getServerTime("YYYYMMDDHH24MISS"));
		} catch (ParseException e) {
			return Calendar.getInstance().getTime();
		}
	}

	public static String getDate(String s) {
		if (Pub.empty(s))
			return "";
		return new
		SimpleDateFormat(s).format(Calendar.getInstance().getTime()).toString();
		/**
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
				"yyyyMMddHHmmss");
		try {
			return new java.text.SimpleDateFormat(s).format(formatter
					.parse(DBUtil.getServerTime("YYYYMMDDHH24MISS")));
		} catch (ParseException e) {
			return new SimpleDateFormat(s).format(
					Calendar.getInstance().getTime()).toString();
		}
		**/
	}

	public static String getDate(String exp, java.util.Date date) {
		if (date == null)
			return "";
		return new SimpleDateFormat(exp).format(date).toString();
	}

	public static java.util.Date toDate(String exp, String str) { // yyyy-MM-dd
																	// HH:mm:ss
		try {
			return new java.text.SimpleDateFormat(exp).parse(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean empty(String s) {
		if (s == null || s.equals(""))
			return true;
		else
			return false;
	}
	
	//判断数据是否为空（暂时用于统计查询）
	public static boolean emptyArray(String s[][]) {
		if (null!= s && s.length > 0 && !Pub.empty(s.toString())){
			return true;
		}
		else
			return false;
	}

	public static String val(ServletRequest req, String para) {
		return Pub.str(req.getParameter(para));
	}

	public static String getVal(ServletRequest req, String para) {
		return req.getParameter(para);
	}

	public static String get(ServletRequest req, String para) {
		try {
			return new String(Pub.getVal(req, para).getBytes("ISO8859_1"),
					"UTF-8");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return "";
		}
	}

	public static String cat(String para) {
		return para.replaceAll("'", "''");
	}

	public static String unCat(String para) {
		return para.replaceAll("''", "'");
	}

	public static String catXml(String xml) {
		// return xml.replaceAll("'", "&apos;").replaceAll("\"", "&quot;")
		// .replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(
		// "&", "&amp;"); //20070912 michael.wang

		return xml.replaceAll("&", "&amp;").replaceAll("'", "&apos;")
				.replaceAll("\"", "&quot;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	public static String unCatXml(String xml) {
		return xml.replaceAll("&apos;", "'").replaceAll("&quot;", "\"")
				.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&amp;", "&");
	}

	public static double toNum(String s) {
		try {
			return Double.parseDouble(s.replaceAll(",", ""));
		} catch (Exception e) {
			// e.printStackTrace(System.out);
		}
		return 0;
	}

	public static int toIntRound(String s) {
		return (int) Math.round(Pub.toNum(s));
	}

	public static int toIntCeil(String s) {
		return (int) Math.ceil(Pub.toNum(s));
	}

	public static int toIntFloor(String s) {
		return (int) Math.floor(Pub.toNum(s));
	}

	public static int toInt(String s) {
		return (int) Pub.toNum(s);
	}

	public static String str(String s) {
		if (s == null || s.equals(""))
			return "";
		// Charset cs = Charset.forName("ISO8859_1");
		try {
			// return new String(cs.encode(s).array(),"GB2312");
			//return new String(s.getBytes("ISO8859_1"), "GB2312");
			return s;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;

	}

	public static String currency_format(String curr, int precision) {
		String prec = ".";
		for (int i = 0; i < precision; i++) {
			prec += "0";
		}
		if (prec.length() == 1)
			prec = "";
		Double dblNum = null;
		if (curr == null || curr == "")
			return "";
		try {
			dblNum = new Double(curr);
		} catch (Exception e) {
			return curr;
		}
		DecimalFormat nf = new DecimalFormat("#,##0" + prec);
		return nf.format(dblNum).toString();
	}

	public static String currency_format(String curr, String precision) {
		return currency_format(curr, Pub.toInt(precision));
	}

	public static String currency_format(String curr) {
		Double dblNum = null;
		if (curr == null || curr == "")
			return "";
		try {
			dblNum = new Double(curr);
		} catch (Exception e) {
			return curr;
		}
		DecimalFormat nf = new DecimalFormat("#,##0.00");
		return nf.format(dblNum).toString();
	}

	public static String currency_format(double curr) {
		Double dblNum = new Double(curr);
		DecimalFormat nf = new DecimalFormat("#,##0.00");
		return nf.format(dblNum).toString();
	}

	public static String currency_format(int curr) {
		Double dblNum = new Double(curr);
		DecimalFormat nf = new DecimalFormat("#,##0.00");
		return nf.format(dblNum).toString();
	}

	public static String currency_format(float curr) {
		Double dblNum = new Double(curr);
		DecimalFormat nf = new DecimalFormat("#,##0.00");
		return nf.format(dblNum).toString();
	}

	public static String getValueByCode(String dict, String code)
			throws Exception {
		if (Pub.empty(dict) || Pub.empty(code))
			return null;
		boolean table = true;
		if (Dics.getInstance() != null) {
			TreeNode tn = Dics.getDicByName(dict);
			if (tn != null) {
				if (code.indexOf(",") > 0) {
					String values = "";
					StringTokenizer t = t = new StringTokenizer(code, ",");
					while (t.hasMoreTokens()) {
						String code_ = (String) t.nextElement();
						tn = tn.getNodeByCode(code_);
						if (tn != null) {
							values += tn.getDicValue() + ",";
						} else {
							values += code_ + ",";
						}
						tn = Dics.getDicByName(dict);
					}
					if (values.length() > 0)
						values = values.substring(0, values.length() - 1);
					return values;
				}

				tn = tn.getNodeByCode(code);
				if (tn != null) {
					return tn.getDicValue();
				}
			} else
				table = true;
		}
		if (table) {
			// 系统，业务相关
			if (dict.toUpperCase().equals("ORG_DEPT")) { // 翻译组织机构
				return null;
			}

			else if (dict.toUpperCase().equals("EXT_YHID")) { // 翻译用户帐号
																// 帐号翻译 add by
																// taocz
																// 2008-02-25
				String[] dics = code.split(",");
				String strDic = "";
				if (dics.length == 1) {

					if (code != null)
						strDic = UserManager.getInstance().getUserByLoginName(
								code) == null ? code : UserManager
								.getInstance().getUserByLoginName(code)
								.getName();

				} else {
					String sv = "";
					String temp;
					for (int m = 0; m < dics.length; m++) {
						temp = UserManager.getInstance().getUserByLoginName(
								dics[m]) == null ? dics[m] : UserManager
								.getInstance().getUserByLoginName(dics[m])
								.getName();
						if (temp != null) {
							sv = sv + temp;
						}
						if (m < dics.length - 1)
							sv += ",";

					}

				}
				return strDic;
			}

			if (dict.equals("EXT_JLX")) {
				if (code != null) {
					String sql = "select jlxmc from ga_jcxx_dz_jlx  where jlxdm=?";
					Object[] paras = { code };
					String[][] list = null;
					try {
						list = DBUtil.querySql(sql, paras);
					} catch (Exception E) {
						System.out.println(E);
					}
					String sv = list == null ? code : list[0][0];
					return sv;
				}

			} else if (dict.equals("EXT_ZZJG")) {
				OrgDept dept = OrgDeptManager.getInstance().getDeptByID(code) == null ? null
						: OrgDeptManager.getInstance().getDeptByID(code);
				String strDic = dept == null ? code : dept.getDeptFullName();
				return strDic;

			}

		}
		return null;
	}

	public static String getDictValueByCode(String dict, String code) {
		if (Dics.getInstance() == null)
			return null;
		TreeNode tn = Dics.getDicByName(dict);
		if (tn != null) {
			tn = tn.getNodeByCode(code);
			if (tn != null) {
				return tn.getDicValue();
			}
		}
		return null;
	}

	public static String getDictAttribute2ByCode(String dict, String code) {
		if (Dics.getInstance() == null)
			return null;
		TreeNode tn = Dics.getDicByName(dict);
		if (tn != null) {
			tn = tn.getNodeByCode(code);
			if (tn != null) {
				return tn.getDicValue2();
			}
		}
		return null;
	}

	public static String getDictAttribute3ByCode(String dict, String code) {
		if (Dics.getInstance() == null)
			return null;
		TreeNode tn = Dics.getDicByName(dict);
		if (tn != null) {
			tn = tn.getNodeByCode(code);
			if (tn != null) {
				return tn.getDicValue3();
			}
		}
		return null;
	}

	/*
	 * 输出信息到前台,以便前台可以得到输出后的信息
	 */
	public static Document writeXmlMessage(HttpServletResponse response,
			String strMessage, String strTagName) throws Exception {
		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement("RESPONSE");
		if (root != null && strMessage != null && strTagName != null) {
			root.addElement(strTagName).addText(strMessage);
		}
		writeMessage(response, doc.asXML());
		return doc;
	}

	public static void writeMessage(HttpServletResponse response, String str,
			String encode) throws Exception {
		OutputStream os = response.getOutputStream();
		if (Pub.empty(encode))
			os.write(str.getBytes());
		else
			os.write(str.getBytes(encode));
		os.flush();
		os.close();
	}

	public static void writeXmlDocument(HttpServletResponse response,
			Document doc, String encode) throws Exception {
		writeMessage(response, doc.asXML(), encode);
	}

	public static void writeXmlDocument(HttpServletResponse response,
			Document doc) throws Exception {
		writeMessage(response, doc.asXML());
	}

	public static void writeMessage(HttpServletResponse response, String message)
			throws Exception {
		writeMessage(response, message, "UTF-8");
	}

	/*
	 * 提示删除成功信息
	 */
	public static Document writeXmlInfoDeleteOK(HttpServletResponse response)
			throws Exception {
		return writeXmlMessage(response, "删除成功!", "MESSAGE");
	}

	/*
	 * 提示修改成功信息
	 */
	public static Document writeXmlInfoUpdateOK(HttpServletResponse response)
			throws Exception {
		return writeXmlMessage(response, "修改成功!", "MESSAGE");
	}

	/*
	 * 提示保存成功信息
	 */
	public static Document writeXmlInfoSaveOK(HttpServletResponse response)
			throws Exception {
		return writeXmlMessage(response, "保存成功!", "MESSAGE");
	}

	/*
	 * 输出xml格式的一般提示信息
	 */
	public static Document writeXmlInfoMessage(HttpServletResponse response,
			String strMessage) throws Exception {
		return writeXmlMessage(response, strMessage, "MESSAGE");
	}

	/*
	 * 输出xml格式的错误信息
	 */
	public static Document writeXmlErrorMessage(HttpServletResponse response,
			String strMessage) throws Exception {
		return writeXmlMessage(response, strMessage, "ERRMESSAGE");
	}

	public static String decode(String encoded) {
		StringBuffer sb = new StringBuffer();
		int maxturns;
		if (encoded.length() % 3 == 0)
			maxturns = encoded.length();
		else
			maxturns = encoded.length() + (3 - (encoded.length() % 3));
		boolean skip;
		byte[] unenc = new byte[4];
		byte b;
		for (int i = 0, j = 0; i < maxturns; i++) {
			skip = false;
			if (i < encoded.length())
				b = (byte) encoded.charAt(i);
			else
				b = 0;
			if (b >= 65 && b < 91)
				unenc[j] = (byte) (b - 65);
			else if (b >= 97 && b < 123)
				unenc[j] = (byte) (b - 71);
			else if (b >= 48 && b < 58)
				unenc[j] = (byte) (b + 4);
			else if (b == '+')
				unenc[j] = 62;
			else if (b == '/')
				unenc[j] = 63;
			else if (b == '=')
				unenc[j] = 0;
			else {
				char c = (char) b;
				if (c == '\n' || c == '\r' || c == ' ' || c == '\t')
					skip = true;
				else
					;
			}
			if (!skip && ++j == 4) {
				int res = (unenc[0] << 18) + (unenc[1] << 12) + (unenc[2] << 6)
						+ unenc[3];
				byte c;
				int k = 16;
				while (k >= 0) {
					c = (byte) (res >> k);
					if (c > 0)
						sb.append((char) c);
					k -= 8;
				}
				j = 0;
				unenc[0] = 0;
				unenc[1] = 0;
				unenc[2] = 0;
				unenc[3] = 0;
			}
		}
		return sb.toString();
	}

	public static String encode(String plain) {
		if (plain.length() > 76)
			return null;
		int maxturns;
		StringBuffer sb = new StringBuffer();
		byte[] enc = new byte[3];
		boolean end = false;
		for (int i = 0, j = 0; !end; i++) {
			char _ch = plain.charAt(i);
			if (i == plain.length() - 1)
				end = true;
			enc[j++] = (byte) plain.charAt(i);
			if (j == 3 || end) {
				int res;
				// this is a bit inefficient at the end point
				// worth it for the small decrease in code size?
				res = (enc[0] << 16) + (enc[1] << 8) + enc[2];
				int b;
				int lowestbit = 18 - (j * 6);
				for (int toshift = 18; toshift >= lowestbit; toshift -= 6) {
					b = res >>> toshift;
					b &= 63;

					if (b >= 0 && b < 26)
						sb.append((char) (b + 65));
					if (b >= 26 && b < 52)
						sb.append((char) (b + 71));
					if (b >= 52 && b < 62)
						sb.append((char) (b - 4));
					if (b == 62)
						sb.append('+');
					if (b == 63)
						sb.append('/');
					if (sb.length() % 76 == 0)
						sb.append('\n');
				}
				// now set the end chars to be pad character if there
				// was less than integral input (ie: less than 24 bits)
				if (end) {
					if (j == 1)
						sb.append("==");
					if (j == 2)
						sb.append('=');
				}
				enc[0] = 0;
				enc[1] = 0;
				enc[2] = 0;
				j = 0;
			}
		}
		return sb.toString();
	}

	public static String toBase64(byte[] p_Buf) {
		return (new BASE64Encoder()).encode(p_Buf).replaceAll("\\s", "");
	}

	public static byte[] fromBase64(String p_Str) throws IOException {
		byte[] byteBuffer = new BASE64Decoder().decodeBuffer(p_Str);
		return byteBuffer;
	}

	public static Document writeXmlMessage(HttpServletResponse response,
			String strRowxml, String strMessage, String strTagName)
			throws Exception {
		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement("RESPONSE");
		root.addAttribute("errorcode", "0");
		root.addAttribute("code", "0");
		Element resultRoot = root.addElement("RESULT");
		resultRoot.addText("$");
		if (root != null && strMessage != null && strTagName != null) {


			root.addElement(strTagName).addText(strMessage);
		}
		String message = doc.asXML();
		int index = message.indexOf("$");
		message = message.substring(0, index) + strRowxml
				+ message.substring(index + 1, message.length());

		writeMessage(response, message, "UTF-8");
		return doc;
	}

	/**
	 * 可以将同时操作多个表的数据后返回的多个VO的rowXml封装在一起 前提是多个VO中没有不同意义的同名字段(否则建议将其修改成不同名)
	 * 
	 * @param rowXmlA
	 * @param rowXmlB
	 * @return a new XML
	 * @throws Exception
	 */
	public static String rowXmlUniter(String rowXmlA, String rowXmlB)
			throws Exception {
		return (new StringBuffer(128))
				.append("<ROW>")
				.append(rowXmlA.replaceAll("<ROW>", "")
						.replaceAll("</ROW>", ""))
				.append(rowXmlB.replaceAll("<ROW>", "")
						.replaceAll("</ROW>", "")).append("</ROW>").toString();
	}

	public static String CalID_15to18(String sId) {
		if (sId == null || sId.length() != 15) {
			return null;
		}
		return CalID_17to18(sId.substring(0, 6) + "19" + sId.substring(6));
	}

	public static String CalID_17to18(String sId) {
		int aW[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
		String aA[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		int aP[] = new int[17];
		int aB[] = new int[17];
		int i, iSum = 0;
		for (i = 0; i < 17; i++) {
			aP[i] = Integer.parseInt(sId.substring(i, i + 1));
		}
		for (i = 0; i < 17; i++) {
			aB[i] = aP[i] * aW[i];
			iSum += aB[i];
		}
		return sId + aA[iSum % 11];
	}

	public static String trimChar(String p_Source, char p_Char, int p_Mode) {
		// 1 odd
		// 2 even

		String bResult = "";

		if (!"".equals(p_Source)) {
			char[] arrSource = p_Source.toCharArray();
			int num = 0;
			for (int i = arrSource.length - 1; i >= 0; i--) {
				if (arrSource[i] == p_Char)
					num++;
				else
					break;
			}
			if (p_Mode == 1) {
				if (num > 0 && num % 2 == 0)
					num--;
			} else if (p_Mode == 2) {
				if (num % 2 == 1)
					num--;
			}
			bResult = p_Source.substring(0, arrSource.length - num);
		}

		return bResult;
	}

	public static void thinkCAString(HashMap hmCA, String strCA) {
		try {
			if (strCA != null && hmCA != null) {
				String[] strs = strCA.split(",");
				int num = -1;
				for (int n = 0; n < strs.length; n++) {
					num = strs[n].indexOf("=");
					if (num > 0) {
						if (hmCA.get(strs[n].substring(0, num).trim()
								.toUpperCase()) != null) { // 处理重复一次
							hmCA.put(strs[n].substring(0, num).trim()
									.toUpperCase()
									+ "2", strs[n].substring(num + 1));

						} else {
							hmCA.put(strs[n].substring(0, num).trim()
									.toUpperCase(), strs[n].substring(num + 1));
						}
					}
				}
			}
		} catch (Exception e) {
			System.out
					.println("CARole.thinkCAString(HashMap hmCA,String strCA)"
							+ e);
		}
	}
	
	/*
	 * sjl
	 * 计算百分比，取整
	 * 返回int类型
	 */
	public static int myPercent(int y,int z){
		if(y ==0){
			return 0;
		}
		if(z == 0){
			return 0;
		}
		double baiy=y*100.0;
		double baiz=z*1.0;
		double fen=baiy/baiz;
		BigDecimal bdl= new BigDecimal(Double.toString(fen)).setScale(0, BigDecimal.ROUND_HALF_UP);
		//NumberFormat nf   =   NumberFormat.getPercentInstance();     注释掉的也是一种方法
		//nf.setMinimumFractionDigits( 2 );        保留到小数点后几位
		//DecimalFormat df1 = new DecimalFormat("##.00%");    //##.00%   百分比格式，后面不足2位的用0补齐
		//baifenbi=nf.format(fen);   
		//baifenbi= df1.format(fen);  
		return Integer.parseInt(bdl.toString());
	}


	
	
	public static String getSysParameter(String para) {
		SysParaConfigureVO sysPara = null;
		String paraValue = "";
		para = para.toUpperCase();
		try {
			sysPara = (SysParaConfigureVO) ParaManager.getInstance()
					.getSysParameter(para);
			paraValue = sysPara.getSysParaConfigureParavalue2();
		} catch (Exception e) {
			System.out.println("获取系统参数：" + para + "时出错！");
			return "";
		}
		return paraValue;
	}

	public static String getSysParameter(String para, User user) {
		SysParaConfigureVO sysPara = null;
		String paraValue = "";
		para = para.toUpperCase();
		try {
			sysPara = (SysParaConfigureVO) ParaManager.getInstance()
					.getSysParameter(para);
			paraValue = sysPara.getSysParaConfigureParavalue2();
		} catch (Exception e) {
			System.out.println("获取系统参数：" + para + "时出错！");
			return "";
		}
		return paraValue;
	}

	public static BaseVO[] getVOByCondition(Connection conn, BaseVO vo)
			throws Exception {
		ArrayList list = null;
		String sql = "select * from " + vo.getVOTableName() + " where ";
		String condstr = "";
		Enumeration enumer = vo.getFields();
		if (enumer != null) {
			while (enumer.hasMoreElements()) {
				String key = (String) enumer.nextElement();

				if (vo.isEmpty(key))
					continue;
				if (condstr.length() < 2) {
					condstr += key + "=?";
				} else {
					condstr += " and " + key + "=?";
				}
			}
		} else
			throw new Exception("BASE DAO:非法操作！#72");
		sql += condstr;
		sql += condstr.length() < 2 ? " rownum < 1000 " : " and rownum < 1000";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData md = null;
		try {
			stmt = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			enumer = vo.getFields();
			int k = -1;
			while (enumer.hasMoreElements()) {
				String key = (String) enumer.nextElement();
				if (!vo.isEmpty(key))
					setParas(++k, stmt, key, vo);
			}
			rs = stmt.executeQuery();
			md = rs.getMetaData();
			int cols = md.getColumnCount();
			while (rs.next()) {
				BaseVO res = (BaseVO) vo.getClass().newInstance();
				if (list == null)
					list = new ArrayList();
				list.add(res);
				for (int i = 1; i <= md.getColumnCount(); i++) {
					String colname = md.getColumnName(i).toUpperCase();
					if (!res.isValidField(colname))
						continue;
					int coltype = md.getColumnType(i);
					if (coltype == java.sql.Types.DATE
							|| coltype == java.sql.Types.TIMESTAMP) {
						if (rs.getTimestamp(i) != null)
							res.put(colname, rs.getTimestamp(i));
					} else if (coltype == java.sql.Types.BLOB) {
						Blob dbBlob;
						dbBlob = rs.getBlob(i);
						if (dbBlob == null)
							continue;
						int length = (int) dbBlob.length();
						byte[] buffer = dbBlob.getBytes(1, length);
						res.put(colname, buffer);
					} else if (coltype == java.sql.Types.NULL) {
						// res.put(colname,null);
					} else {
						if (rs.getString(i) != null
								&& !"".equals(rs.getString(i)))
							res.put(colname, rs.getString(i));
					}
				}
			}
			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			rs = null;
			if (stmt != null)
				stmt.close();
			stmt = null;
		}
		return list == null ? null : (BaseVO[]) list.toArray(new BaseVO[list
				.size()]);
	}

	public static void setParas(int i, PreparedStatement stmt, String key,
			BaseVO vo) throws Exception {
		Object para = vo.get(key);
		if (para != null) {
			if (vo.isNchar(key)) {

				try {
					OraclePreparedStatement ostmt = (OraclePreparedStatement) stmt;
					ostmt.setFormOfUse(i + 1,
							oracle.jdbc.OraclePreparedStatement.FORM_NCHAR);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			if (para instanceof java.util.Date || para instanceof java.sql.Date) {
				java.sql.Timestamp date = new java.sql.Timestamp(
						((java.util.Date) para).getTime());
				stmt.setObject(i + 1, date);
			} else if (para instanceof java.sql.Timestamp)
				stmt.setObject(i + 1, para);
			else
				stmt.setString(i + 1, para.toString());
		} else {
			stmt.setNull(i + 1, java.sql.Types.VARCHAR);
		}
	}

	/**
	 * 得到数据表中最大ID，然后加1，用于vo获得主键
	 * @param tableName 表名
	 * @param idName	主键字段名
	 * @param num		位数，如果只是自增，传入0即可
	 * @return
	 */
	public static String getTableMaxId(String tableName, String idName, int num) {
		String result[][] = DBUtil.query("select max(to_number(" + idName
				+ ")) + 1 from " + tableName);
		String id = result[0][0];
		if (Pub.empty(id)) {
			id = "1";
		}
		int len = id.length();
		if(0 != num && num > len){
			for (int i = 0; i < num - len; i++) {
				id = "0" + id;
			}
		}
		return id;
	}
	
	
	/**
	 * 获得某年月下数据表的最大流水号
	 * 比对数据项为录入时间
	 * @param tableName
	 * @param num
	 * @return
	 */
	public static String getLshOnYearMonth(String tableName, int num){
		String result[][] = DBUtil.query("select count(*) + 1 from " + tableName + " where to_char(lrsj,'yyyyMM') = to_char(sysdate,'yyyyMM')");
		String id = result[0][0];
		int len = id.length();
		if(0 != num && num > len){
			for (int i = 0; i < num - len; i++) {
				id = "0" + id;
			}
		}
		return id;
	}
	/**
	 * 生成固定查询条件
	 * @param id	ID的值
	 * @param filename	ID的列名
	 * @return
	 */
	public static String makeQueryConditionByID(String id,String filename){
		JSONObject jso = new JSONObject();
		JSONObject json = new JSONObject();
		//内层查询条件		开始
        JSONArray jsa = new JSONArray();
        JSONObject jsono = new JSONObject();
        jsono.put("value",id);
        jsono.put("fieldname",filename);
        jsono.put("operation","=");
        jsono.put("logic","and");
        jsa.add(jsono);
        //内层查询条件		结束
        json.put("conditions", jsa);
        jso.put("querycondition", json);
        return jso.toString();
	}
	/**
	 * 数字转换为千分位函数
	 * @param 1000000
	 * @return 1000,000
	 */
	public static String NumberToThousand (String n){
		double d ;
		if(Pub.empty(n))
			return "";
		try {
			 d = Double.parseDouble(n);
		} catch (NumberFormatException e) {
			return n;
		}
		DecimalFormat df = new DecimalFormat("#,###.###");
		String m = df.format(d);
		return m;
	}
	
	/**
	 * 将字符串数字小数点向左移动4位，保留三位有效数字。
	 * @param n 
	 * @return
	 */
	public static String moveToleft3(String n) {
		String rs = null;
		try {
			BigDecimal b = new BigDecimal(n);
			DecimalFormat d = new DecimalFormat("#.###");
			rs = d.format(b.movePointLeft(4));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * 补小数点后两位
	 * @param n
	 * @return
	 */
	public static String NumberAddDec(String n){
		String m = "";
		if(!Pub.empty(n)){
			if(n.indexOf(".")>-1){
				StringTokenizer toke = new StringTokenizer(n, ".");
				ArrayList a = new ArrayList();
				while (toke.hasMoreTokens()) {
					String ss = (String) toke.nextElement();
					a.add(ss);
					
				}
				String t = (String)a.get(1);
				if(t.length()<2){
					int k = Integer.parseInt(t)+100;
					m = (String)a.get(0)+"."+(String)a.get(1)+"0";
				}else{
					m = n;
				}
				
			}else{
				return n+".00";
			}
		}
		return m;
		
	}
	/*
	 * 金钱数字统一格式处理——先转换为千分位 补小数点后两位
	 */
	public static String MoneyFormat(String n){
		String sv = "";
		sv = Pub.NumberToThousand(n);
		if(!Pub.empty(sv)){
			sv = Pub.NumberAddDec(sv);
		}
		return sv;
	}
	
	/*
	 * 小数类型格式化
	 * 如果是整数返回整数 ，如1  返回1
	 * 如果是小数那么保留一位小数，同时四舍五入 如1.1 返回1.1    1.15 返回1.2
	 */
	public static String DecimalsFormat(String n){
		
		String sv = "";
		//空处理
		if(Pub.empty(n)){
			return n;
		}
		//整数格式不变返回
		if(!Pub.empty(n)&&n.indexOf(".")==-1){
			return n;
		}
		double  b = Double.parseDouble(n); 
		DecimalFormat decimalFormat = new DecimalFormat(".#");
		decimalFormat.setRoundingMode(RoundingMode.HALF_UP);  
        double c =Double.parseDouble(decimalFormat.format(b)) ;
        String a = String.valueOf(c);
        if(a.substring(a.indexOf('.')+1).equals("0")){
        	a = a.substring(0,a.indexOf('.'));
        }
		return a;
		
	}
	
	
	public static String getNewsSql(String type,String id){
		String sql = "";
		if(Pub.empty(type)) type= "1";
		if(Pub.empty(id)) id= "";
		if("1".equals(type)){
			sql = "select ggbt,nr,fbr,TO_CHAR(fbsj,'yyyy\"年\"mm\"月\"dd\"日\"hh24\"时\"mi\"分\"') as fbsj,lrbmmc from xtbg_xxzx_ggtz where ggid ='"+id+"'";
		}else if("5".equals(type)){
			sql = "select NDXXBT,nr,fbr,TO_CHAR(fbsj,'yyyy\"年\"mm\"月\"dd\"日\"hh24\"时\"mi\"分\"') as fbsj,lrbmmc from XTBG_XXZX_NDXXGX where XTBG_XXZX_NDXXGX_ID='"+id+"'";
		}else{
			sql = "select xwbt,nr,fbr,TO_CHAR(fbsj,'yyyy\"年\"mm\"月\"dd\"日\"hh24\"时\"mi\"分\"') as fbsj,lrbmmc from xtbg_xxzx_zxxw where newsid='"+id+"'";
		}
		
		return sql;
		
	}
	/**
	 * 日期类型转换成毫秒数
	 * @param date
	 * @return
	 */
	public static Long Date2MS(Date date){
		
		if(date!=null){
			Long time = date.getTime();
			return time;
		}
		return null;
	}
	/** 
	 * 计算两个日期之间相差的天数 
	 * @param smdate 较小的时间
	 * @param bdate  较大的时间
	 * @return 相差天数
	 * @throws ParseException 
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	
	public static String getPropertiesSqlValue(String propertiesName, String keyName) throws UnsupportedEncodingException {
		Locale locale = Locale.getDefault();
		ResourceBundle resourceBundle = ResourceBundle.getBundle(propertiesName, locale);
		String sqlValue = "";
		if (keyName == null || keyName.equals("") || keyName.equals("null")) {
            return sqlValue;
        } else {
        	sqlValue =  new String(resourceBundle.getString(keyName).getBytes("ISO-8859-1"),"UTF-8");
        	return sqlValue;
        }
	}
	
	/*
	 * 去除内容的html格式标签
	 */
	 public static String splitAndFilterString(String input, int length) {  
	        if (input == null || input.trim().equals("")) {  
	            return "";  
	        }  
	        // 去掉所有html元素,  
	        String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(  "<[^>]*>", "");  
	        str = str.replaceAll("[(/>)<]", "");  
	        int len = str.length();  
	        if (len <= length) {  
	            return str;  
	        } else {  
	            str = str.substring(0, length);  
	            str += "...";  
	        }  
	        return str;  
	    } 
	 
	 private static final String _NUM = "0000000";
	 /**
	  * 生成编号，此编号格式为004
	  * @param num int 传入的流水号
	  * @param index int 要截取最后编号的长度
	  * @return
	  */
	 public static String formatSerialNumber(int num, int index) {
		 int length = String.valueOf(num).length() > index ? String.valueOf(num).length() : index;
		 String serialNumber = _NUM + num;
		 
		 serialNumber = serialNumber.substring(serialNumber.length()-length, serialNumber.length());
		 return serialNumber;
	 }
	  
	  /*
		 * 通过ywlx,user获取wsid,operationid
		 * 
		 */
		 public static void getFlowinf(Connection conn,String ywlx,String sjbh, User user,String condition) { 
		       String rs [] = null;
		       String rspz [][] = null;
		       try{
		    	   String roles = user.getRoleListString();
		    	   String gettypz = "";
		    	   if("".equals(condition)){
		    		   gettypz = "select ws_template_id,operationoid from AP_WS_TYPZ  where ywlx ='"+ywlx+"'";		    		   
		    	   }else{
		    		   gettypz = "select ws_template_id,operationoid from AP_WS_TYPZ  where ywlx ='"+ywlx+"' and condition = '"+condition+"'";		
		    	   }
		    	   rspz = DBUtil.query(conn, gettypz);
		    	   if(rspz!= null)
		    	   {
		    		   rs = new String[2];
		    		  if(rspz !=null)
		    		  {
		    			  rs[0] = rspz[0][0];
		    			  rs[1] = rspz[0][1];
		    		  }
		    			   
		    		   
		    		   //调用插入方法AP_PROCESSCONFIG
		    		   insertProcessconf(conn, ywlx, sjbh,rs,user);
		    	   }
		       }catch(Exception e)
		       {
		    	   e.printStackTrace();
		       }
		       
		    }
		 	/*
			 * 插入AP_PROCESSCONFIG表通用审批插入
			 * 
			 */
			 public static void insertProcessconf(Connection conn,String ywlx, String sjbh,String rs[],User user) {  
		
			       try{
			    	   String insert_sql =
			                    "insert into AP_PROCESSCONFIG (AP_PROCESS_ID,WS_TEMPLATEID,YWLX,SJBH,LRR,LRSJ,OPERATIONOID) values('" +
			                    		new RandomGUID().toString() + "','"+rs[0]+"','" + ywlx + "','"+sjbh+"','"+user.getAccount()+"',SYSDATE,'"+rs[1]+"')";
			                DBUtil.execSql(conn, insert_sql);
			       }catch(Exception e)
			       {
			    	   e.printStackTrace();
			       }
			       
			    }
			 /*
				 * 通过ywlx,sjbh获取流程描述信息
				 * 
				 */
				 public static String getProdesc(Connection conn,String ywlx,String sjbh) { 
				       String  [][] rs = null;
				       String tempdesc = "";
				       String getsql = "";
				       try{
				    	  if(!Pub.empty(ywlx)&& !Pub.empty(sjbh)){
				    		  if("300101".equals(ywlx)){//招投标需求
				    			  getsql = "select gzmc from GC_ZTB_XQ where sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("000001".equals(ywlx)){//工作联络单
				    			  getsql = "select t.domain_value from AP_PROCESS_WS t where t.fieldname = 'BT' and sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("200502".equals(ywlx)){//文件处理单
				    			  getsql = "select WJBT from XTBG_GWGL_SWGL t where  sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("200503".equals(ywlx)){//文件处理单
				    			  getsql = "select WJBT from XTBG_GWGL_FWGL t where  sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("700101".equals(ywlx)){//合同会签
				    			  getsql = "select HTMC from gc_htgl_ht t where  sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("020001".equals(ywlx)){//下达库审批
				    			  getsql = "SELECT SPMC FROM GC_TCJH_SP WHERE sjbh ='"+sjbh+"' and ywlx = '010007'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  } else if("010002".equals(ywlx)){//计财项目下达
				    			  getsql = "select SPMC from GC_TCJH_SP t where  sjbh ='"+sjbh+"' and ywlx = '010007'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("000002".equals(ywlx) || "000003".equals(ywlx)){//工作联络单、工作请示单
				    			  getsql = "select t.domain_value from AP_PROCESS_WS t where t.fieldname = 'BT' and sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("040413".equals(ywlx)){//读报信息处理卡
				    			  getsql = "select t.domain_value from AP_PROCESS_WS t where t.fieldname = 'Fld347' and sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("040414".equals(ywlx)){//招标公告
				    			  getsql = "select t.domain_value from AP_PROCESS_WS t where t.fieldname = 'Fld431' and sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("040415".equals(ywlx)){//招标文件
				    			  getsql = "select t.domain_value from AP_PROCESS_WS t where t.fieldname = 'Fld431' and sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("040417".equals(ywlx)){//工程甩项会签单
				    			  getsql = "select t.domain_value from AP_PROCESS_WS t where t.fieldname = 'xmmc' and sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("050001".equals(ywlx)){//工程洽商审核意见单
				    			  getsql = "select t.QSBT from GC_GCGL_GCQS t where sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }else if("040416".equals(ywlx)){//招标文件补遗文件会签单（新）
				    			  getsql = "select t.domain_value from AP_PROCESS_WS t where t.fieldname = 'Fld431' and sjbh ='"+sjbh+"' and ywlx = '"+ywlx+"'";
				    			  rs = DBUtil.querySql(conn, getsql);
				    			  if(rs != null){
				    				  tempdesc = rs[0][0];
				    			  }
				    		  }
				    	  }else{
				    		  tempdesc = "";
				    	  }
				       }catch(Exception e)
				       {
				    	   e.printStackTrace();
				       }
				      return  tempdesc;
				    }
				    /**
				     * 初始化json
				     * @param initJson
				     * @return
				     */
					public static JSONArray doInitJson(String initJson)
					{
						JSONObject response = JSONObject.fromObject(initJson);
						String response_txt = response.getString("response");
						JSONObject data = JSONObject.fromObject(response_txt);
						String data_txt = data.getString("data");
						JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(data_txt);
						return jsonArray;
					}
	 
	public static void main(String args[]) {
		
		System.out.println(getDate("yyyy"));
		System.out.println(NumberToThousand("1000000"));
		System.out.println(DecimalsFormat("1000000"));
		System.out.println(MoneyFormat("1000000"));
		System.out.println(DecimalsFormat("0.02397748"));
		
	}

	/**
	 * 将有结构的json字符串重新结合，再按原结构返回
	 * @param jsonList
	 * @return
	 */
	public static String toBaseResultSetJsonString(List<String> jsonList) {
		JSONObject domStringLastObj = null;
		for (int i = 0; i < jsonList.size(); i++) {
			String bsJson = jsonList.get(i);

			JSONObject domStringResponseObj = JSONObject.fromObject(bsJson);
			String domStringData = domStringResponseObj.getString("response");
			JSONObject domStringDataObj = JSONObject.fromObject(domStringData);
			String domStringArrayString = domStringDataObj.getString("data");
			JSONArray domStringArray = JSONArray.fromObject(domStringArrayString);
			String domStringLast = domStringArray.get(0).toString();
			
			if(domStringLastObj == null) {
				domStringLastObj = JSONObject.fromObject(domStringLast);
			} else {
				JSONObject notFirst = JSONObject.fromObject(domStringLast);
				Iterator<String> it = notFirst.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					String val = notFirst.getString(key);
					
					domStringLastObj.put(key, val);
				}
			}
			
		}
		JSONArray array = new JSONArray();
		array.add(domStringLastObj);
		JSONObject data = new JSONObject();
		data.put("data", array);
		JSONObject response = new JSONObject();
		response.put("response", data);
		return response.toString();
	}
	/**
	 * 
	 * @param conn	数据库连接
	 * @param id	表的主键值
	 * @param operation	操作。储备库：1，下达库：2，标段：3
	 * @throws Exception
	 */
	public static void doSynchronousXmbm(Connection conn,String id,String operation,String xmbm) throws Exception{
		String sql = "";
		String cbkID = "";
		String xdkID = "";
		String jhID = "";
		String arr[][] = null;
		String fullXMBM = "";
		String cbkBM = "";
		String xdkBM = "";
		if("1".equals(operation)){
			//储备库修改操作，要更新项目表、标段表、计划表。按照项目ID
			cbkID = id;
			cbkBM = xmbm;
			sql = "select GC_TCJH_XMXDK_ID,XDK_XMBM,ISNRTJ from GC_TCJH_XMXDK where XMCBK_ID='"+cbkID+"'";
			String cbksql = "select gc_tcjh_xmcbk_id, xmbh, xmmc, nd, isnrtj from gc_tcjh_xmcbk  where gc_tcjh_xmcbk_id= '"+cbkID+"'";
			arr = DBUtil.query(conn, sql);
			String cbk[][] = DBUtil.query(conn, cbksql);
			if(arr==null){
				//如果没有下达到下达库，那么后续的修改都不用做了
			}else{
				xdkID = arr[0][0];
				xdkBM = arr[0][1];
				//修改下达库
				sql = "update GC_TCJH_XMXDK t set t.XMBH='"+cbkBM+"'||t.XDK_XMBM,t.PRE_XMBM='"+cbkBM+"',t.ISNRTJ='"+cbk[0][4]+"' where XMCBK_ID='"+cbkID+"'";
				DBUtil.execSql(conn, sql);
				//同步计划的项目
				sql = "update GC_JH_SJ t set t.XMBH=(select XMBH from GC_TCJH_XMXDK where GC_TCJH_XMXDK_ID='"+xdkID+"'),t.ISNRTJ='"+cbk[0][4]+"' where XMID='"+xdkID+"' and XMBS='0'";
				DBUtil.execSql(conn, sql);
				//同步标段
				sql = "update GC_XMBD t set BDBM='"+cbkBM+xdkBM+"'||t.BD_XMBM,PRE_BDBM='"+cbkBM+xdkBM+"',t.ISNRTJ='"+cbk[0][4]+"' where XMID='"+xdkID+"'";
				DBUtil.execSql(conn, sql);
				//同步标段的项目
				sql = "update GC_JH_SJ t set XMBH=(select BDBM from GC_XMBD B where XMID='"+xdkID+"' and B.GC_XMBD_ID=t.BDID),t.ISNRTJ='"+cbk[0][4]+"' where XMID='"+xdkID+"' and XMBS='1' ";
				DBUtil.execSql(conn, sql);
			}
		}else if("2".equals(operation)){
			//下达库修改的时候，要同步标段表和计划表
			sql = "select C.GC_TCJH_XMCBK_ID,C.XMBM from GC_TCJH_XMCBK C,GC_TCJH_XMXDK X where C.GC_TCJH_XMCBK_ID=X.XMCBK_ID and X.GC_TCJH_XMXDK_ID='"+id+"'";
			xdkID = id;
			arr = DBUtil.query(conn, sql);
			if(arr==null){
				//如果数据不存在，那么也不需要修改后面的数据了
			}else{
				cbkBM = arr[0][1];
				xdkBM = xmbm;
				//同步计划的项目
				sql = "update GC_JH_SJ t set t.XMBH=(select XMBH from GC_TCJH_XMXDK where GC_TCJH_XMXDK_ID='"+xdkID+"') where XMID='"+xdkID+"' and XMBS='0'";
				DBUtil.execSql(conn, sql);
				//同步标段
				sql = "update GC_XMBD t set BDBM='"+cbkBM+xdkBM+"'||t.BD_XMBM,PRE_BDBM='"+cbkBM+xdkBM+"' where XMID='"+xdkID+"'";
				DBUtil.execSql(conn, sql);
				//同步标段的项目
				sql = "update GC_JH_SJ t set XMBH=(select BDBM from GC_XMBD B where XMID='"+xdkID+"' and B.GC_XMBD_ID=t.BDID) where XMID='"+xdkID+"' and XMBS='1'";
				DBUtil.execSql(conn, sql);
			}
		}else if("3".equals(operation)){
			//标段修改的时候，要同步计划表
//			sql = "select GC_XMBD_ID,BDBM from GC_XMBD where GC_XMBD_ID='"+id+"'" ;
			sql = "update GC_JH_SJ set XMBH='"+xmbm+"' where BDID='"+id+"'";
			DBUtil.execSql(conn, sql);
		}
	}
	public static String getPropertiesString(String propertyFileName,String key) {
    	Locale locale = Locale.getDefault();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(propertyFileName,locale);
        if (key == null || key.equals("") || key.equals("null")) {
            return "";
        }
        String result = "";
        try {
            result = new String(resourceBundle.getString(key).getBytes("iso-8859-1"),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}