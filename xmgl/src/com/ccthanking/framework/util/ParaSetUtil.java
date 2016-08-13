package com.ccthanking.framework.util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class ParaSetUtil {
	public ParaSetUtil() {
	}

	public static Log log = LogFactory.getLog(ParaSetUtil.class);

	public static Object convertResult2VO(java.sql.ResultSet result,
			java.lang.Class votype) throws Exception {
		java.lang.Class classobj = votype.getClass();
		Object tempVO = null;
		try {
			tempVO = classobj.newInstance();
			java.lang.reflect.Field[] fields = classobj.getFields();
			String value = null;
			for (int i = 0; i < fields.length; i++) {
				value = result.getString(fields[i].getName());
				fields[i].set(tempVO, value);
			}
		} catch (Exception E) {
			throw E;
		}
		return tempVO;
	}

	public static Object getVOInstance(HttpServletRequest request,
			java.lang.Class votype, String tablename) throws Exception {
		Object tempvo = votype.newInstance();
		// 获得字段列表
		Field[] fields = votype.getFields();
		SAXReader reader = new SAXReader();
		InputStream in = request.getInputStream();
		Document doc = reader.read(in);
		String fieldname = "";
		try {
			for (int i = 0; i < fields.length; i++) {
				fieldname = "//DATAINFO/ROW/" + (Pub.empty(tablename)?"":(tablename.toUpperCase() + "/"))
						+ fields[i].getName().toUpperCase();
				Node node = doc.selectSingleNode(fieldname);
				if (node != null) {
					fields[i].set(tempvo, node.getText());
				}
			}
		} catch (Exception E) {
			System.out.println(E);
			throw E;
		}
		return tempvo;
	}

	public static void insertParas(ServletConfig objServletConfig,
			HttpServletRequest request, HttpServletResponse response,
			HashMap data) throws Exception {
		SAXReader reader = new SAXReader();
		InputStream in = request.getInputStream();
		Document doc = reader.read(in);
		List queryRoot = doc.selectNodes("//DATAINFO/ROW/*");
		for (int i = 0; i < queryRoot.size(); i++) {
			Element ele = (Element) queryRoot.get(i);
			if (ele.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			String nodeName = ele.getName();
			Object nodeValue = ele.getData();
			if (nodeName != null && !"".equals(nodeName)) {
				data.put(nodeName, nodeValue);
			}
		}
		return;
	}

}