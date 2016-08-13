package com.ccthanking.common.util.excelutil;

import java.util.ArrayList;

/**
 * 管理Excel内容信息
 * 
 * @author cbl
 * 
 */
public class Content {
	/**
	 * 存储excel内容及内容的坐标
	 */
	private ArrayList content = new ArrayList();

	/**
	 * 返回excel的内容对象的集合
	 * 
	 * @return
	 */
	public ArrayList getContent() {
		return content;
	}

	/**
	 * 指定内容中的一行每一个单元格的文字，并指定单元格的横坐标，纵坐标
	 * 
	 * @param string
	 */
	public void addRow(Row row) {
		content.add(row);
	}

	/**
	 * 清空内容信息
	 * 
	 */
	public void initContent() {
		content.clear();
	}
}
