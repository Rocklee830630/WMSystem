package com.ccthanking.framework.common;

/**
 * @auther xhb 
 */
public class Dict {
	/* 编号 */
	private String id;
	/* 父ID */
	private String parent_id;
	/* 字典码 */
	private String dic_code;
	/* 字典值 */
	private String dic_value;
	/* 字典值的拼音头 */
	private String dic_value_spell;
	/* 字典的层级 */
	private String dic_layer;
	/* 字典大类名称 */
	private String dic_name_value;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getDic_code() {
		return dic_code;
	}
	public void setDic_code(String dic_code) {
		this.dic_code = dic_code;
	}
	public String getDic_value() {
		return dic_value;
	}
	public void setDic_value(String dic_value) {
		this.dic_value = dic_value;
	}
	public String getDic_value_spell() {
		return dic_value_spell;
	}
	public void setDic_value_spell(String dic_value_spell) {
		this.dic_value_spell = dic_value_spell;
	}
	public String getDic_layer() {
		return dic_layer;
	}
	public void setDic_layer(String dic_layer) {
		this.dic_layer = dic_layer;
	}
	public String getDic_name_value() {
		return dic_name_value;
	}
	public void setDic_name_value(String dic_name_value) {
		this.dic_name_value = dic_name_value;
	}
}
