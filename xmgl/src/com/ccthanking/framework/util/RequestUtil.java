package com.ccthanking.framework.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.PageManager;

public class RequestUtil {
	public RequestUtil() {
	}

	public static Log log = LogFactory.getLog(RequestUtil.class);

	public static Document getDocument(HttpServletRequest request) {
		try {
			SAXReader reader = new SAXReader();
			InputStream in = request.getInputStream();
			return reader.read(in);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	public static JSONObject getJSONObject(HttpServletRequest request) {
		StringBuffer json = new StringBuffer();
		String line = null;
		JSONObject obj = null;
		JSONObject res = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
			obj = JSONObject.fromObject(json.toString());
			res = obj.getJSONObject("msg");
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
		return res;
	}
	public static JSONObject setJSONValueByJSON(JSONObject jo,String jKey,JSONObject rq,String rKey){
		jo.put(jKey, !rq.containsKey(rKey)||rq.getString(rKey)==null?"":rq.getString(rKey));
		return jo;
	}
	public static PageManager getPageManager(Document doc) {
		Element root = (Element) doc.selectSingleNode("//QUERYCONDITION");
		if (root == null) {
			return null;
		}
		PageManager pm = new PageManager();
		String recordPerPage = root.attributeValue("recordsperpage");
		String pageNO = root.attributeValue("currentpagenum");
		String totalPage = root.attributeValue("totalpage");
		String countrows = root.attributeValue("countrows");
		pm.setCountPage(Pub.toInt(totalPage));
		pm.setCountRows(Pub.toInt(countrows));
		pm.setPageRows(Pub.toInt(recordPerPage) <= 0 ? Globals.DEFAULT_ROWS_PERPAGE
				: Pub.toInt(recordPerPage));
		pm.setCurrentPage(Pub.toInt(pageNO) <= 0 ? 1 : Pub.toInt(pageNO));
		return pm;
	}
	
	public static PageManager getPageManager(String json) {
		
		
		JSONObject querycondition = JSONObject.fromObject(json);
		JSONObject pages = null;
		PageManager pm = new PageManager();
		try {
			String pages_txt = querycondition.getString("pages");
			pages = JSONObject.fromObject(pages_txt);
			String recordPerPage = pages.getString("recordsperpage");
			String pageNO = pages.getString("currentpagenum");
			String totalPage = pages.getString("totalpage");
			String countrows = pages.getString("countrows");
			pm.setCountPage(Pub.toInt(totalPage));
			pm.setCountRows(Pub.toInt(countrows));
			pm.setPageRows(Pub.toInt(recordPerPage) <= 0 ? Globals.DEFAULT_ROWS_PERPAGE
					: Pub.toInt(recordPerPage));
			pm.setCurrentPage(Pub.toInt(pageNO) <= 0 ? 1 : Pub.toInt(pageNO));
		} catch (Exception e) {
			pm.setCountPage(0);
			pm.setCountRows(0);
			pm.setPageRows(Globals.DEFAULT_ROWS_PERPAGE);
			pm.setCurrentPage(1);
			
		}
		
		
		return pm;
	}
	/*
	 * json
	 */
	public static QueryConditionList getConditionList(String json){
		QueryConditionList qcList = new QueryConditionList();
		JSONObject querycondition = JSONObject.fromObject(json);
		String querycondition_txt = querycondition.getString("querycondition");
		//edit by cbl
		//转为对象
		JSONObject subcondition = JSONObject.fromObject(querycondition_txt);
		//获取子array串
		 querycondition_txt = subcondition.getString("conditions");
		//add end
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(querycondition_txt);
		Iterator iter = jsonArray.iterator();
		JSONObject condition = null;
		while (iter.hasNext())
		{
			condition = (JSONObject) iter.next();
			List<Object> arr = condition.names();
			Collection<Object> arra = condition.values();// 获取值
			String strFieldName = "";
			String strOperation = "";
			String strValue = "";
			String strFormat = "";
			String strKind = "";
			String strLogic = "";
			for (Object name : arr) {
				String key = name.toString().toUpperCase();
				String value = condition.getString(name.toString());
				if("FIELDNAME".equalsIgnoreCase(key))
				{
					strFieldName = value;
				}
				if("OPERATION".equalsIgnoreCase(key))
				{
					strOperation = value;
				}
				if("VALUE".equalsIgnoreCase(key))
				{
					strValue = value;
				}
				if("FIELDFORMAT".equalsIgnoreCase(key))
				{
					 strFormat = value == null ? "" : value;
					if (strFormat == null)
						strFormat = "";
				}
				if("FIELDTYPE".equalsIgnoreCase(key))
				{
					 strKind = value == null ? "" : value;
					if (strKind == null)
						strKind = "";
				}
				if("LOGIC".equalsIgnoreCase(key))
				{
				     strLogic = value == null ? "and" : value;
					if (strLogic == null)
						strLogic = "";
				}
			}
			qcList.addCondition(new QueryCondition(strFieldName,
					strOperation, strValue, strKind.trim(), strFormat
							.trim(), strLogic.trim()));
		}
		return qcList;
	}

	public static String getOrderFilter(String json){
		
		String res = "  ";
		try {
			
			JSONObject orders = JSONObject.fromObject(json);
			String orders_txt = orders.getString("orders");
			JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(orders_txt);
			if (jsonArray.size()>0) {
			    res = " order by ";
            }
			Iterator iter = jsonArray.iterator();
			JSONObject order = null;
			while (iter.hasNext())
			{
				order = (JSONObject) iter.next();
				List<Object> arr = order.names();
				Collection<Object> arra = order.values();// 获取值
				String strFieldName = "";
				String strType = "";
				for (Object name : arr) {
					String key = name.toString().toUpperCase();
					String value = order.getString(name.toString());
					if("FIELDNAME".equalsIgnoreCase(key))
					{
						strFieldName = value;
					}
					if("ORDER".equalsIgnoreCase(key))
					{
						strType = value;
					}
				}
				res +=" "+strFieldName +" "+strType+",";
			}
			res = res.substring(0,res.length()-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "";
		}
		return res;
	}


	public static QueryConditionList getConditionList(Document doc) {
		try {
			List list = doc.selectNodes("//CONDITIONS/CONDITION");
			Iterator iter = list.iterator();
			Element element = null;
			QueryConditionList qcList = new QueryConditionList();
			while (iter.hasNext()) {
				element = (Element) iter.next();
				String strFieldName = element.selectSingleNode("FIELDNAME")
						.getStringValue().trim();
				String strOperation = element.selectSingleNode("OPERATION")
						.getStringValue().trim();
				String strValue = element.selectSingleNode("VALUE")
						.getStringValue().trim();
				Node ndformat = element.selectSingleNode("FIELDFORMAT");
				String strFormat = ndformat == null ? "" : ndformat
						.getStringValue();
				if (strFormat == null)
					strFormat = "";
				Node ndkind = element.selectSingleNode("FIELDTYPE");
				String strKind = ndkind == null ? "" : ndkind.getStringValue();
				if (strKind == null)
					strKind = "";
				Node lgkind = element.selectSingleNode("LOGIC");
				String strLogic = lgkind == null ? "and" : lgkind
						.getStringValue();
				if (strLogic == null)
					strLogic = "";
				qcList.addCondition(new QueryCondition(strFieldName,
						strOperation, strValue, strKind.trim(), strFormat
								.trim(), strLogic.trim()));
			}
			list = doc.selectNodes("//CONDITIONS/EXPRESS/CONDITOIN");
			iter = list.iterator();
			while (iter.hasNext()) {
				element = (Element) iter.next();
				String strLogic = element.selectSingleNode("LOGIC").getText();
				String strLeft = element.selectSingleNode("LEFT").getText();
				String strValue = element.selectSingleNode("VALUE").getText();
				String strRight = element.selectSingleNode("RIGHT").getText();
				qcList.addExpCondition(new ExpCondition(strLogic, strLeft,
						strValue, strRight));
			}
			return qcList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getOrderFilter(Document doc) {
		try {
			String res = "";
			List list = doc.selectNodes("//ORDERS/ORDER");
			for (int i = 0; i < list.size(); i++) {
				Element order = (Element) list.get(i);
				if (order.attribute("type") != null
						&& order.attribute("type").getStringValue()
								.toLowerCase().equals("asc")) {
					if (res.length() > 1)
						res += ",";
					res += order.getText();
				} else if (order.attribute("type") != null
						&& order.attribute("type").getStringValue()
								.toLowerCase().equals("desc")) {
					if (res.length() > 1)
						res += ",";
					res += order.getText() + " desc ";
				}
			}
			if (res.length() > 1)
				res = " order by " + res;
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
}