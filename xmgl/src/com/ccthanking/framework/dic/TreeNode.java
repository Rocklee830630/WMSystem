package com.ccthanking.framework.dic;

import java.sql.SQLException;
import java.util.ArrayList;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ccthanking.framework.common.DBUtil;

/**
 * @version 1.0
 */
public class TreeNode {
	
	TreeNode parent = null;
	ArrayList childs = new ArrayList();
	String code = ""; // 字典码
	String text = ""; // 字典值
	String strDicLayerTwo = ""; //
	String id = ""; //
	String parentId = ""; //
	String strDicLayer = ""; //
	String strDicValueSpell = ""; // 字典值的拼音头
	String strDicValueASpell = ""; // 字典值的全拼
	String dicValue2 = ""; // 保留未使用
	String dicValue3 = ""; // 保留未使用
	String strDicNameValue = "";
	String strDicNameCode = "";
	String strDicNode = "";
	String strDicYwlb = "";
	String strDicSyfw = "";
	String strDicBmff = "";
	String strDicDmgl = "";
	String strIs_leaf = "1";
	int iType = 0;
	boolean hasChild = false;

	public TreeNode() {

	}

	public TreeNode(String id, String parentId, String strDicLayer,
			String strDicCode, String strDicValue, String strDicLayerTwo,
			String strDicValueSpell, String strDicValueASpell) {
		this.code = strDicCode;
		this.text = strDicValue;
		this.strDicLayerTwo = strDicLayerTwo;
		this.id = id;
		this.parentId = parentId;
		this.strDicLayer = strDicLayer;
		this.strDicValueSpell = strDicValueSpell;
		this.strDicValueASpell = strDicValueASpell;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public TreeNode getParent() {
		return this.parent;
	}

	public void addChild(TreeNode child) {
		this.childs.add(child);
		hasChild = true;
	}

	public boolean delChild(TreeNode child) {
		boolean bRes = false;
		try {
			TreeNode tnTemp = null;
			for (int n = 0; n < childs.size(); n++) {
				tnTemp = (TreeNode) childs.get(n);
				if (tnTemp != null && tnTemp.equals(child)) {
					childs.remove(tnTemp);
					if (childs.size() == 0) {
						this.hasChild = false;
					}
					break;
				} else {
					tnTemp.delChild(child);
				}
			}
		} catch (Exception e) {
			System.out.println("TreeNode.delChild:" + e.toString());
		}
		return bRes;
	}

	public String getInSql() {
		String strRes = "";
		TreeNode tnTemp = null;
		strRes = "'" + this.id + "'";
		String str = "";
		for (int n = 0; n < childs.size(); n++) {
			tnTemp = (TreeNode) childs.get(n);
			str = tnTemp.getInSql();
			if (str != null && str.length() > 0) {
				strRes += "," + str;
			}
		}
		return strRes;
	}

	public Element getElement() {
		Element resEle = null;
		TreeNode tnTemp = null;
		resEle = DocumentHelper.createElement("DICNODE");
		resEle.addAttribute("ID", this.getId());
		resEle.addAttribute("PARENT_ID", this.getParentId());
		resEle.addAttribute("DIC_LAYER", this.getDicLayer());
		resEle.addAttribute("DIC_CODE", this.getDicCode());
		resEle.addAttribute("DIC_VALUE", this.getDicValue());
		resEle.addAttribute("DIC_LAYER_TWO", this.getDicLayerTwo());
		// resEle.addAttribute("DIC_VALUE_SPELL", this.getDicValueSpell());
		// resEle.addAttribute("DIC_VALUE_ASPELL", this.getDicValueASpell());
		// resEle.addAttribute("DIC_VALUE2", this.getDicValue2());
		// resEle.addAttribute("DIC_VALUE3", this.getDicValue3());
		resEle.addAttribute("IS_LEAF", this.getIs_leaf());
		Element ele = null;
		for (int n = 0; n < childs.size(); n++) {
			tnTemp = (TreeNode) childs.get(n);
			ele = tnTemp.getElement();
			if (ele != null) {
				resEle.add(ele);
			}
		}
		return resEle;
	}

	public boolean delChild(String strId) {
		boolean bRes = false;
		try {
			TreeNode tnTemp = null;
			for (int n = 0; n < childs.size(); n++) {
				tnTemp = (TreeNode) childs.get(n);
				if (tnTemp != null && tnTemp.id.equals(strId)) {
					childs.remove(tnTemp);
					if (childs.size() == 0) {
						this.hasChild = false;
					}
					break;
				} else {
					tnTemp.delChild(strId);
				}
			}
		} catch (Exception e) {
			System.out.println("TreeNode.delChild:" + e.toString());
		}
		return bRes;
	}

	public String getIdStr() {
		String strRes = "";
		TreeNode tnTemp = null;
		strRes = this.id;
		String str = "";
		for (int n = 0; n < childs.size(); n++) {
			tnTemp = (TreeNode) childs.get(n);
			str = tnTemp.getIdStr();
			if (str != null && str.length() > 0) {
				strRes += "," + str;
			}
		}
		return strRes;
	}

	public TreeNode[] getChilds() {
		TreeNode[] res = new TreeNode[this.childs.size()];
		this.childs.toArray(res);
		// orderByDicValue(res);
		return res;
	}

	public void setDicCode(String code) {
		this.code = code;
	}

	public String getDicCode() {
		if (this.code == null) {
			return "";
		}
		return this.code;
	}

	public void setDicValue(String text) {
		this.text = text;
	}

	public String getDicValue() {
		if (this.text == null) {
			return "";
		}
		return this.text;
	}

	public String getDicValue2() {
		return this.dicValue2;
	}

	public String getDicValue3() {
		return this.dicValue3;
	}

	public void setDicValue2(String value) {
		this.dicValue2 = value;
	}

	public void setDicValue3(String value) {
		this.dicValue3 = value;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		if (this.id == null) {
			return "";
		}
		return this.id;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentId() {
		if (this.parentId == null) {
			return "";
		}
		return this.parentId;
	}

	public void setDicLayer(String strDicLayer) {
		this.strDicLayer = strDicLayer;
	}

	public String getDicLayer() {
		if (this.strDicLayer == null) {
			return "";
		}
		return this.strDicLayer;
	}

	public void setDicLayerTwo(String strDicLayerTwo) {
		this.strDicLayerTwo = strDicLayerTwo;
	}

	public String getDicLayerTwo() {
		if (this.strDicLayerTwo == null) {
			return "";
		}
		return this.strDicLayerTwo;
	}

	public void setDicValueSpell(String strDicValueSpell) {
		this.strDicValueSpell = strDicValueSpell;
	}

	public String getDicValueSpell() {
		if (this.strDicValueSpell == null) {
			return "";
		}
		return this.strDicValueSpell;
	}

	public void setDicValueASpell(String strDicValueASpell) {
		this.strDicValueASpell = strDicValueASpell;
	}

	public String getDicValueASpell() {
		if (this.strDicValueASpell == null) {
			return "";
		}
		return this.strDicValueASpell;
	}

	/*
	 * modify by liuyang 增加业务字段
	 * dic_name_value,dic_name_code,dic_node,dic_ywlb,dic_syfw,dic_bmff,dic_dmgl
	 * start
	 */
	public void setDicNameValue(String strDicNameValue) {
		this.strDicNameValue = strDicNameValue;
	}

	public String getDicNameValue() {
		if (this.strDicNameValue == null) {
			return "";
		}
		return this.strDicNameValue;
	}

	public void setDicNameCode(String strDicNameCode) {
		this.strDicNameCode = strDicNameCode;
	}

	public String getDicNameCode() {
		if (this.strDicNameCode == null) {
			return "";
		}
		return this.strDicNameCode;
	}

	public void setDicNode(String strDicNode) {
		this.strDicNode = strDicNode;
	}

	public String getDicNode() {
		if (this.strDicNode == null) {
			return "";
		}
		return this.strDicNode;
	}

	public void setDicYwlb(String strDicYwlb) {
		this.strDicYwlb = strDicYwlb;
	}

	public String getDicYwlb() {
		if (this.strDicYwlb == null) {
			return "";
		}
		return this.strDicYwlb;
	}

	public void setDicSyfw(String strDicSyfw) {
		this.strDicSyfw = strDicSyfw;
	}

	public String getDicSyfw() {
		if (this.strDicSyfw == null) {
			return "";
		}
		return this.strDicSyfw;
	}

	public void setDicBmff(String strDicBmff) {
		this.strDicBmff = strDicBmff;
	}

	public String getDicBmff() {
		if (this.strDicBmff == null) {
			return "";
		}
		return this.strDicBmff;
	}

	public void setDicDmgl(String strDicDmgl) {
		this.strDicDmgl = strDicDmgl;
	}

	public String getDicDmgl() {
		if (this.strDicDmgl == null) {
			return "";
		}
		return this.strDicDmgl;
	}

	public void setType(int type) {
		this.iType = type;
	}

	public int getType() {
		return this.iType;
	}

	/*
	 * end modify by liuyang
	 */
	// modify by wangzh 增加 字段IS_LEAF 是否叶节点 begin
	public void setIs_leaf(String Is_leaf) {
		this.strIs_leaf = Is_leaf;
	}

	public String getIs_leaf() {
		return this.strIs_leaf;
	}

	// modify by wangzh 增加 字段IS_LEAF 是否叶节点 end

	public TreeNode getNodeByCode(String strCode) {
		// add by hongpeng_dong at 2009.12.22 解决如果字典翻译不了出现空指针错误的问题
		//modify by zhangbr@ccthanking.com 此处加上strCode空值的判断，如果strCode为空，肯定无法翻译，那么也没必要执行下去了
		if (null == this.code || ("".equals(strCode)||strCode==null)) {
			return null;
		}
		TreeNode res = null;
//		System.out.println("CODE:"+this.code+"|StrCODE:"+strCode);
		if (this.code.equals(strCode)) {
			return this;
		} else {
			for (int n = 0; n < this.childs.size(); n++) {
				res = ((TreeNode) childs.get(n)).getNodeByCode(strCode);
				if (res != null) {
					return res;
				}
			}
		}
		
		//已删除的字典翻译（临时处理）
		//add by hongpeng.dong at 2013.10.26
		if(null == res){
			String sql = "select id, parent_id, dic_layer,dic_code,dic_value, dic_layer_two,dic_value_spell,dic_value_aspell,dic_value2,dic_value3,IS_LEAF from fs_dic_tree where parent_id = ? and dic_code = ?";
			Object[] paras = new Object[2];
			paras[0] = this.id;
			paras[1] = strCode;
			String result[][];
			try {
				result = DBUtil.querySql(sql, paras);
			} catch (SQLException e) {
				result = null;
			}
			if(null != result && result.length > 0){
				res = new TreeNode();
				res.setDicCode(strCode);
				res.setParent(this);
				res.setDicValue(result[0][4]);
			}
		}
		
		return res;
	}

	public TreeNode getNodeById(String strId) {
		TreeNode res = null;
		if (this.id.equals(strId)) {
			return this;
		} else {
			for (int n = 0; n < this.childs.size(); n++) {
				res = ((TreeNode) childs.get(n)).getNodeById(strId);
				if (res != null) {
					return res;
				}
			}
		}
		return res;
	}

	public boolean equals(TreeNode tnPar) {
		if (tnPar != null && this.id.equals(tnPar.id)) {
			return true;
		}
		return false;
	}

	public boolean hasChild() {
		return this.hasChild;
	}

	public TreeNode[] orderByDicValue(TreeNode[] tn) {
		if (tn != null) {
			TreeNode fTemp = null;
			for (int n = 0; n < tn.length - 1; n++) {
				for (int m = n + 1; m < tn.length; m++) {

					if (isDa(tn[n].getDicValue(), tn[m].getDicValue())) {
						fTemp = tn[n];
						tn[n] = tn[m];
						tn[m] = fTemp;
					}
				}
			}
		}
		return tn;
	}

	public TreeNode[] orderByDicCode(TreeNode[] tn) {
		if (tn != null) {
			TreeNode fTemp = null;
			for (int n = 0; n < tn.length - 1; n++) {
				for (int m = n + 1; m < tn.length; m++) {
					try {
						if (Integer.parseInt(tn[n].getDicCode()) > Integer
								.parseInt(tn[m].getDicCode())) { // 如果码值是数字
							fTemp = tn[n];
							tn[n] = tn[m];
							tn[m] = fTemp;
						}

					} catch (Exception e) {
						if (isDa(tn[n].getDicCode(), tn[m].getDicCode())) {
							fTemp = tn[n];
							tn[n] = tn[m];
							tn[m] = fTemp;
						}

					}
				}
			}
		}
		return tn;
	}

	public boolean isDa(String str1, String str2) {
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
}