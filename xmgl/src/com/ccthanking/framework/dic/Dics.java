package com.ccthanking.framework.dic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.ccthanking.framework.common.DBUtil;
import com.ccthanking.framework.common.cache.Cache;
import com.ccthanking.framework.common.cache.CacheManager;
import com.ccthanking.framework.common.datasource.DBConnectionManager;
import com.ccthanking.framework.coreapp.orgmanage.SpellCache;
import com.ccthanking.framework.log.log;
import com.ccthanking.framework.plugin.AppInit;
import com.ccthanking.framework.util.Pub;

public class Dics implements Cache {
	
	private static Dics dics;
	private static HashMap hmDics = new HashMap();
	private static String strOutPath = "";
	private static Logger logger = log.getLogger(Dics.class);
	private static String strEncoding = "GBK";
	
	private static HashMap xmlDics = new HashMap();

	private Dics() {
		try {
			if (dics == null){
				init();
				allDicsIntoXml();
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			this.dics = null;
		}
	}

	public void reBuildMemory() throws Exception {
		if (hmDics != null) {
			hmDics.clear();
			hmDics = null;
			hmDics = new HashMap();
		}
		init();
		allDicsIntoXml();
		printDicToXml(AppInit.appPath + "/dic");
	}
	
	public static synchronized void allDicsIntoXml() {
		Connection conn = DBConnectionManager.getInstance()
				.getDefaultConnection();
		Statement sm = null;
		ResultSet rs = null;
		try {
			String querySql = "select id, parent_id, dic_layer,dic_code,dic_value, dic_layer_two,dic_value_spell,dic_value_aspell,dic_value2,dic_value3,IS_LEAF  from fs_dic_tree where dic_code is not null and sfyx='1' order by dic_layer,pxh,dic_code,dic_value";
			sm = conn.createStatement();
			rs = sm.executeQuery(querySql);
			ResultSetMetaData rsmd = rs.getMetaData();
			ArrayList al = new ArrayList();
			String[] temp = null;
			while (rs.next()) {
				temp = new String[rsmd.getColumnCount()];
				for (int n = 0; n < temp.length; n++) {
					temp[n] = rs.getString(n + 1);
				}
				al.add(temp);
			}
			
			TreeNode dic = new TreeNode();
			String[][] rootNode = getRootNode(al, "0");
			if (rootNode != null) {
				TreeNode root = null;
				for (int n = 0; n < rootNode.length; n++) {
					root = new TreeNode(rootNode[n][0], rootNode[n][1],
							rootNode[n][2], rootNode[n][3], rootNode[n][4],
							rootNode[n][5], rootNode[n][6], rootNode[n][7]);
					root.setDicValue2(rootNode[n][8]);
					root.setDicValue3(rootNode[n][9]);
					root.setIs_leaf(rootNode[n][10]);

					if (rootNode[n][6] != null) {
						xmlDics.put(rootNode[n][6].trim().toUpperCase(), root);
						addTreeNode(root, getChildNodes(al, root.getId()));
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static synchronized void addDic(ArrayList dicStrs, String name,
			String rootId) {
		try {
			TreeNode dic = new TreeNode();
			String[][] rootNode = getRootNode(dicStrs, rootId);
			if (rootNode != null) {
				TreeNode root = null;
				for (int n = 0; n < rootNode.length; n++) {
					root = new TreeNode(rootNode[n][0], rootNode[n][1],
							rootNode[n][2], rootNode[n][3], rootNode[n][4],
							rootNode[n][5], rootNode[n][6], rootNode[n][7]);
					root.setDicValue2(rootNode[n][8]);
					root.setDicValue3(rootNode[n][9]);
					root.setIs_leaf(rootNode[n][10]);

					if (rootNode[n][6] != null) {
						hmDics.put(rootNode[n][6].trim().toUpperCase(), root);
						addTreeNode(root, getChildNodes(dicStrs, root.getId()));
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static synchronized void addDic(ArrayList dicStrs, String name) {
		try {
			TreeNode dic = new TreeNode();
			hmDics.put(name.trim().toUpperCase(), dic);
			String[] temp = null;
			TreeNode tn = null;
			for (int n = 0; n < dicStrs.size(); n++) {
				temp = (String[]) dicStrs.get(n);
				tn = new TreeNode();
				tn.setDicCode(temp[0]);
				tn.setDicValue(temp[1]);
				dic.addChild(tn);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static synchronized void addTreeNode(TreeNode root, ArrayList dicStrs) {
		try {
			String[][] childs = getChildNode(dicStrs, root.getId());
			if (childs != null && childs.length > 0) {
				TreeNode child = null;
				for (int n = 0; n < childs.length; n++) {
					child = new TreeNode(childs[n][0], childs[n][1],
							childs[n][2], childs[n][3], childs[n][4],
							childs[n][5], childs[n][6], childs[n][7]);
					child.setDicValue2(childs[n][8]);
					child.setDicValue3(childs[n][9]);
					child.setIs_leaf(childs[n][10]);
					root.addChild(child);
					addTreeNodeTwo(child,
							getChildNodesTwo(dicStrs, child.getId()));
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static synchronized void addTreeNodeTwo(TreeNode root,
			ArrayList dicStrs) {
		try {
			String[][] childs = getChildNode(dicStrs, root.getId());
			if (childs != null && childs.length > 0) {
				TreeNode child = null;
				for (int n = 0; n < childs.length; n++) {
					child = new TreeNode(childs[n][0], childs[n][1],
							childs[n][2], childs[n][3], childs[n][4],
							childs[n][5], childs[n][6], childs[n][7]);
					child.setDicValue2(childs[n][8]);
					child.setDicValue3(childs[n][9]);
					child.setIs_leaf(childs[n][10]);
					root.addChild(child);
					addTreeNodeTwo(child, dicStrs);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static synchronized TreeNode[] getAllDics() {
		try {
			if (hmDics.values() != null) {
				TreeNode[] tnRes = new TreeNode[hmDics.values().size()];
				hmDics.values().toArray(tnRes);
				orderByDicValue(tnRes);
				return tnRes;
			}
		} catch (Exception e) {
			System.out.println("Dics.getAllDics:" + e.toString());
		}
		return null;
	}

	public static synchronized TreeNode[] orderByDicValue(TreeNode[] tn) {
		if (tn != null) {
			TreeNode fTemp = null;
			for (int n = 0; n < tn.length - 1; n++) {
				for (int m = n + 1; m < tn.length; m++) {

					if (isDa(tn[n].getDicCode(), tn[m].getDicCode())) {

						fTemp = tn[n];
						tn[n] = tn[m];
						tn[m] = fTemp;
					}
				}
			}
		}
		return tn;
	}

	public static synchronized boolean isDa(String str1, String str2) {
		if (str1 != null && str1.length() > 0) {
			if (str2 == null || str2.length() == 0) {
				return true;
			}
			if (str1.charAt(0) > str2.charAt(0)) {
				return true;
			}

		}
		return false;
	}

	private static synchronized String[][] getRootNode(ArrayList dicStrs,
			String rootId) {
		String[][] res = null;
		ArrayList alRes = new ArrayList();
		String[] temp = null;
		for (int n = 0; n < dicStrs.size(); n++) {
			temp = (String[]) dicStrs.get(n);
			if (temp != null && temp.length > 0 && temp[1] != null
					&& temp[1].trim().equals(rootId)) {
				alRes.add(temp);
			}
		}
		if (alRes.size() > 0) {
			res = new String[alRes.size()][((String[]) alRes.get(0)).length];
			alRes.toArray(res);
		}
		return res;
	}

	private static synchronized String[][] getChildNode(ArrayList dicStrs,
			String rootCode) {
		String[][] res = null;
		ArrayList alRes = new ArrayList();
		String[] temp = null;

		for (int n = 0; n < dicStrs.size(); n++) {
			temp = (String[]) dicStrs.get(n);

			if (temp != null && temp.length > 0 && temp[1] != null
					&& temp[1].trim().equals(rootCode)) {
				alRes.add(temp);
			}
		}
		if (alRes.size() > 0) {
			res = new String[alRes.size()][((String[]) alRes.get(0)).length];
			alRes.toArray(res);
		}
		return res;
	}

	private static synchronized ArrayList getChildNodes(ArrayList dicStrs,
			String rootCode) {
		ArrayList alRes = new ArrayList();
		String[] temp = null;
		for (int n = 0; n < dicStrs.size(); n++) {
			temp = (String[]) dicStrs.get(n);
			if (temp != null && temp.length > 0 && temp[2] != null
					&& temp[2].trim().startsWith(rootCode)) {
				alRes.add(temp);
			}
		}
		return alRes;
	}

	private static synchronized ArrayList getChildNodesTwo(ArrayList dicStrs,
			String rootCode) {
		ArrayList alRes = new ArrayList();
		String[] temp = null;

		for (int n = 0; n < dicStrs.size(); n++) {
			temp = (String[]) dicStrs.get(n);
			if (temp != null && temp.length > 0 && temp[5] != null
					&& temp[5].trim().startsWith(rootCode)) {
				alRes.add(temp);
			}
		}
		return alRes;
	}

	public static synchronized TreeNode getDicByName(String dicName) {
		if (dicName == null) {
			dicName = "";
		}
		return (TreeNode) hmDics.get(dicName.trim().toUpperCase());
	}

	public static synchronized TreeNode getDicByName(String dicName,
			String dicCode) {
		TreeNode res = null;
		if (dicName == null) {
			dicName = "";
		}
		res = (TreeNode) hmDics.get(dicName.trim().toUpperCase());
		if (res != null && dicCode != null && dicCode.trim().length() > 0) {
			res = res.getNodeByCode(dicCode);
		}
		return res;
	}

	public static synchronized String getTextByDicNameCode(String dicName,
			String dicCode) {
		TreeNode res = null;
		if (dicName == null) {
			dicName = "";
		}
		res = (TreeNode) hmDics.get(dicName.trim().toUpperCase());
		if (res != null && dicCode != null && dicCode.trim().length() > 0
				&& !dicCode.equals("null")) {
			res = res.getNodeByCode(dicCode);
			// add by guanchb 2007-07-01 start
			if (res == null)
				return "";
			return res.getDicValue();
			// add by guanchb 2007-07-01 end
		}

		// 2007-06-27 因为字典码为空返回字典名，所以修改
		// if (res != null)
		// {
		// return res.getDicValue();
		// }
		return "";
	}

	public static synchronized void saveToXmlDic(TreeNode TNode) {
		if (strOutPath != null && strOutPath.length() > 0) {
			saveToXmlDic(TNode, strOutPath);
		}
	}

	public static synchronized void saveToXmlDic(TreeNode TNode,
			String strOutputPath) {
		try {
			Document domresult = DocumentFactory.getInstance().createDocument();
			Element root = domresult.addElement("DATA");

			String strDicName = TNode.getDicValueSpell();
			File file = new File(strOutputPath);
			file.mkdirs();
			file = null;
			FileOutputStream fos = new FileOutputStream(strOutputPath + "/"
					+ strDicName.trim().toUpperCase() + ".xml");
			OutputStream os = fos;

			if (TNode != null && TNode.hasChild()) {
				TreeNode[] tns = TNode.getChilds();

				for (int n = 0; n < tns.length; n++) {
					Element resultRoot = root.addElement("R");
					resultRoot.addAttribute("c", tns[n].getDicCode());
					resultRoot.addAttribute("t", tns[n].getDicValue());
					String desc = tns[n].getDicValue();
					String spell = tns[n].getDicValueSpell();
					if (Pub.empty(spell))
						spell = SpellCache.getInstance().getSpell(desc);
					String aspell = tns[n].getDicValueASpell();
					if (Pub.empty(aspell))
						aspell = SpellCache.getInstance().getAspell(desc);
					String isleaf = tns[n].getIs_leaf();
					if (Pub.empty(isleaf))
						isleaf = "1";
					if (!isleaf.endsWith("1"))
						isleaf = "2";
					resultRoot.addAttribute("s", spell);
					resultRoot.addAttribute("a", aspell);
					resultRoot.addAttribute("l", isleaf);
					String parent = tns[n].getDicLayerTwo();
					if (!Pub.empty(parent))
						resultRoot.addAttribute("p", parent);
				}
			}
			OutputFormat of = new OutputFormat();
			of.setEncoding(strEncoding);
			of.setIndent(true);
			of.setNewlines(true);

			XMLWriter writer = new XMLWriter(os, of);

			writer.write(domresult);

			fos.close();
			os.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// 创建数据源字典文件
	public static void createDataSourceDic(String[] ds, String path) {
		TreeNode tnTemp = new TreeNode();
		tnTemp.setDicLayer("");
		tnTemp.setDicCode("datasource");
		tnTemp.setDicValue("datasource");
		tnTemp.setDicValueSpell("DATASOURCE");
		for (int i = 0; i < ds.length; i++) {
			TreeNode child = new TreeNode();
			child.setParent(tnTemp);
			child.setDicCode(ds[i]);
			child.setDicValue(ds[i]);
			tnTemp.addChild(child);
		}
		saveToXmlDic(tnTemp, path);

	}

	public static synchronized void printDicToXml(String strOutputPath) {
		try {
			java.util.Iterator it = xmlDics.keySet().iterator();
			String str = null;
			strOutPath = strOutputPath;
			TreeNode tnTemp = null;
			while (it.hasNext()) {
				str = (String) it.next();
				tnTemp = (TreeNode) xmlDics.get(str);
				logger.info("字典名：" + tnTemp.getDicValue() + "[" + str + "]");
				saveToXmlDic(tnTemp, strOutputPath);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static synchronized Dics getInstance() {
		if (dics == null) {
			dics = new Dics();
			CacheManager.register(CacheManager.CACHE_DIC, dics);
		}
		return dics;
	}

	private void init() throws Exception {
		Connection conn = DBConnectionManager.getInstance()
				.getDefaultConnection();
		Statement sm = null;
		ResultSet rs = null;
		try {
			//String querySql = "select id, parent_id, dic_layer,dic_code,dic_value, dic_layer_two,dic_value_spell,dic_value_aspell,dic_value2,dic_value3,IS_LEAF  from fs_dic_tree where dic_code is not null and sfyx = '1' order by dic_layer,dic_code,dic_value";
			String querySql = "select id, parent_id, dic_layer,dic_code,dic_value, dic_layer_two,dic_value_spell,dic_value_aspell,dic_value2,dic_value3,IS_LEAF  from fs_dic_tree where dic_code is not null order by dic_layer,pxh,dic_code,dic_value";
			sm = conn.createStatement();
			rs = sm.executeQuery(querySql);
			ResultSetMetaData rsmd = rs.getMetaData();
			ArrayList al = new ArrayList();
			String[] temp = null;
			while (rs.next()) {
				temp = new String[rsmd.getColumnCount()];
				for (int n = 0; n < temp.length; n++) {
					temp[n] = rs.getString(n + 1);
				}
				al.add(temp);
			}
			addDic(al, "DIC", "0");
		} catch (Exception e) {
			throw e;
		} finally {

			if (rs != null)
				try {
					rs.close();
				} catch (Exception E) {
					E.printStackTrace();
				}
			if (sm != null)
				try {
					sm.close();
				} catch (Exception E) {
					E.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception E) {
					E.printStackTrace();
				}
			rs = null;
			sm = null;
			conn = null;
		}
	}

	public static synchronized void addDicValue(String strDicId,
			String strDicParentId, TreeNode tnTemp) {
		try {
			if (strDicId != null && strDicParentId != null) {
				Iterator it = hmDics.values().iterator();
				TreeNode tnT = null;
				TreeNode tnTC = null;
				while ((it.hasNext())) {
					tnT = (TreeNode) it.next();
					if (tnT.getId().equals(strDicId)) {
						tnTC = tnT.getNodeById(strDicParentId);
						if (tnTC != null) {
							tnTC.addChild(tnTemp);
							saveToXmlDic(tnT);
						}
						break;
					}
				}
			} else {
			}
		} catch (Exception e) {
			System.out.println("Dics.addDicValue:" + e.toString());
		}
	}

	public static synchronized void updateDicValue(String strDicId,
			String strNodeId, TreeNode tnTemp) {
		try {
			if (strDicId != null && strNodeId != null) {
				Iterator it = hmDics.values().iterator();
				TreeNode tnT = null;
				TreeNode tnTC = null;
				while ((it.hasNext())) {
					tnT = (TreeNode) it.next();
					if (tnT.getId().equals(strDicId)) {
						tnTC = tnT.getNodeById(strNodeId);
						if (tnTC != null) {
							tnTC.setDicCode(tnTemp.getDicCode());
							tnTC.setDicValue(tnTemp.getDicValue());
							tnTC.setDicValueSpell(tnTemp.getDicValueSpell());
							tnTC.setDicValueASpell(tnTemp.getDicValueASpell());
							tnTC.setDicLayer(tnTemp.getDicLayer());
							tnTC.setDicLayerTwo(tnTemp.getDicLayerTwo());
							tnTC.setDicValue2(tnTemp.getDicValue2());
							tnTC.setDicValue3(tnTemp.getDicValue3());
							tnTC.setIs_leaf(tnTemp.getIs_leaf());
							saveToXmlDic(tnT);
						}
						break;
					}
				}

			} else {
			}
		} catch (Exception e) {
			System.out.println("Dics.addDicValue:" + e.toString());
		}
	}

	public static synchronized String getDicInSql(String strDicId,
			String strNodeId) {
		try {
			if (strDicId != null && strNodeId != null) {
				Iterator it = hmDics.values().iterator();
				TreeNode tnT = null;
				TreeNode tnTC = null;
				if (strDicId != null
						&& (strDicId.equals("0") || strDicId.length() == 0)) {
					strDicId = strNodeId;
				}
				while ((it.hasNext())) {
					tnT = (TreeNode) it.next();
					if (tnT.getId().equals(strDicId)) {
						tnTC = tnT.getNodeById(strNodeId);
						if (tnTC != null) {
							return tnTC.getInSql();
						}
						break;
					}
				}
			} else {
			}
		} catch (Exception e) {
			System.out.println("Dics.addDicValue:" + e.toString());
		}
		return "";
	}

	public static synchronized void addDic(TreeNode tnTemp) {
		try {
			if (tnTemp != null && tnTemp.getDicValueSpell() != null) {
				Iterator itKey = hmDics.keySet().iterator();
				while ((itKey.hasNext())) {
					if (((String) itKey.next()).equals(tnTemp
							.getDicValueSpell().trim().toUpperCase())) {
						System.out
								.println("Dics.addDic(TreeNode tnTemp):字典名称已经存在！！"
										+ tnTemp.getDicValueSpell());
						return;
					}
				}
				hmDics.put(tnTemp.getDicValueSpell().toUpperCase(), tnTemp);
				saveToXmlDic(tnTemp);
			}
		} catch (Exception e) {
			System.out.println("Dics.addDicValue:" + e.toString());
		}
	}

	public static synchronized void updateDic(String strDicId, TreeNode tnTemp) {
		try {
			if (strDicId != null) {
				Iterator it = hmDics.values().iterator();
				TreeNode tnT = null;

				while ((it.hasNext())) {
					tnT = (TreeNode) it.next();
					if (tnT.getId().equals(strDicId)) {
						tnT.setDicCode(tnTemp.getDicCode());
						tnT.setDicValue(tnTemp.getDicValue());
						tnT.setDicValueSpell(tnTemp.getDicValueSpell());
						tnT.setDicValueASpell(tnTemp.getDicValueASpell());
						tnT.setDicLayer(tnTemp.getDicLayer());
						tnT.setDicLayerTwo(tnTemp.getDicLayerTwo());
						tnT.setDicValue2(tnTemp.getDicValue2());
						tnT.setDicValue3(tnTemp.getDicValue3());
						tnT.setIs_leaf(tnTemp.getIs_leaf());
						saveToXmlDic(tnT);
						break;
					}
				}
			} else {
			}
		} catch (Exception e) {
			System.out.println("Dics.addDicValue:" + e.toString());
		}
	}

	public static TreeNode getTreeNode(String strDicId) {
		try {
			if (strDicId != null) {
				Iterator it = hmDics.values().iterator();
				TreeNode tnT = null;

				while ((it.hasNext())) {

					tnT = (TreeNode) it.next();

					// +">>>>:"+tnT.getId().equals(strDicId));
					if (tnT.getId().equals(strDicId)) {

						return tnT;
					} else {
						tnT = tnT.getNodeById(strDicId);
						if (tnT != null)
							return tnT;
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Dics.addDicValue:" + e.toString());
		}
		return null;
	}

	public static synchronized void delDic(String strDicId) {
		try {
			if (strDicId != null) {
				Iterator it = hmDics.values().iterator();
				TreeNode tnT = null;

				while ((it.hasNext())) {
					tnT = (TreeNode) it.next();
					if (tnT.getId().equals(strDicId)) {
						hmDics.remove(tnT.getDicValueSpell().toUpperCase());
						break;
					}
				}
			} else {

			}
		} catch (Exception e) {
			System.out.println("Dics.delDicValue:" + e.toString());
		}
	}

	public static synchronized void delDicValue(String strDicId,
			String strNodeId) {
		try {
			if (strDicId != null && strNodeId != null) {
				Iterator it = hmDics.values().iterator();
				TreeNode tnT = null;

				while ((it.hasNext())) {
					tnT = (TreeNode) it.next();
					if (tnT.getId().equals(strDicId)) {
						tnT.delChild(strNodeId);
						saveToXmlDic(tnT);
						break;
					}
				}
			} else {
			}
		} catch (Exception e) {
			System.out.println("Dics.delDicValue:" + e.toString());
		}
	}

	/*
	 * 将对应的字典类别导出为XML文件
	 */
	public static synchronized void exportDicTree2Xml(String filePath,
			String id, String kind) throws Exception {

		String filename = filePath + "/" + kind.toUpperCase() + ".xml";
		String sql = "select dic_code,dic_value,DIC_VALUE_SPELL,DIC_VALUE_ASPELL"
				+ ",IS_LEAF,DIC_LAYER_TWO from fs_dic_tree where parent_id='"
				+ id
				+ "' order by order by dic_layer, pxh,dic_code,dic_value";
		String[][] qs = DBUtil.query(sql);
		if (qs == null)
			throw new Exception(kind + " 字典信息不存在!");
		doImpl(filename, qs, filePath);

	}

	/**
	 * 存成xml文件
	 * 
	 * @param filename
	 * @param dic
	 * @throws java.lang.Exception
	 */
	private static void doImpl(String filename, String[][] dic, String filePath)
			throws Exception {
		FileOutputStream fos = null;
		try {
			Document domresult = DocumentFactory.getInstance().createDocument();
			Element root = domresult.addElement("DATA");
			File file = new File(filePath);
			file.mkdirs();
			file = null;
			fos = new FileOutputStream(filename);
			OutputStream os = fos;
			for (int i = 0; i < dic.length; i++) {
				String code = dic[i][0];
				String desc = dic[i][1];
				String spell = dic[i][2];
				if (Pub.empty(spell))
					spell = SpellCache.getInstance().getSpell(desc);
				String aspell = dic[i][3];
				if (Pub.empty(aspell))
					aspell = SpellCache.getInstance().getAspell(desc);
				String isleaf = dic[i][4];
				if (Pub.empty(isleaf))
					isleaf = "1";
				if (!isleaf.equals("1"))
					isleaf = "2";
				String parent = dic[i][5];
				Element resultRoot = root.addElement("R");
				resultRoot.addAttribute("c", code);
				resultRoot.addAttribute("t", desc);
				resultRoot.addAttribute("s", spell);
				resultRoot.addAttribute("a", aspell);
				resultRoot.addAttribute("l", isleaf);
				if (!Pub.empty(parent)) {
					resultRoot.addAttribute("p", parent);
				}
			}
			OutputFormat of = new OutputFormat();
			of.setEncoding(strEncoding);
			of.setIndent(true);
			of.setNewlines(true);
			XMLWriter writer = new XMLWriter(os, of);
			writer.write(domresult);
		} catch (Exception e) {
			throw e;
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	public void synchronize(String data, int action) throws Exception {
		if (action == CacheManager.ADD || action == CacheManager.UPDATE) { //
			this.add(data);
		} else if (action == CacheManager.DELETE) {
			this.delDic(data);
		}

	}

	// add by wuxp 此处代码为字典的同步处理
	private boolean add(String id) throws Exception {
		Connection conn = DBConnectionManager.getInstance()
				.getDefaultConnection();
		Statement sm = null;
		ResultSet rs = null;
		try {
			// 在内存中删除选中字典的所有节点
			String[] idArray = this.getTreeNode(id).getIdStr().split(",");
			String tempId = null;
			for (int i = 0; i < idArray.length; i++) {
				tempId = idArray[i];
				this.delDic(tempId);
			}
			String sql = "SELECT id,parent_id,dic_layer,dic_code,dic_value,dic_layer_two,dic_value_spell,dic_value_aspell,dic_value2,dic_value3,IS_LEAF FROM fs_DIC_TREE START WITH id='"
					+ id
					//+ "' CONNECT BY parent_id = PRIOR id order by dic_layer, dic_code";
					+ "' CONNECT BY parent_id = PRIOR id order by dic_layer, pxh,dic_code,dic_value";
			sm = conn.createStatement();
			rs = sm.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			ArrayList al = new ArrayList();
			String[] temp = null;
			while (rs.next()) {
				temp = new String[rsmd.getColumnCount()];
				for (int n = 0; n < temp.length; n++) {
					temp[n] = rs.getString(n + 1);
				}
				al.add(temp);
			}

			addDic(al, "DIC", "0");
		} catch (Exception e) {
			throw e;
		} finally {

			if (rs != null)
				try {
					rs.close();
				} catch (Exception E) {
					E.printStackTrace();
				}
			if (sm != null)
				try {
					sm.close();
				} catch (Exception E) {
					E.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception E) {
					E.printStackTrace();
				}
			rs = null;
			sm = null;
			conn = null;
		}

		return false;
	}
}
