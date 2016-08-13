package com.ccthanking.common.util.excelutil;

import java.util.ArrayList;

public class Row {
	/**
	 * 存储标题及标题的坐标
	 */
	private ArrayList row = new ArrayList();

	/**
	 * 返回标题信息对象的集合
	 */
	public ArrayList getRow() {
		return row;
	}

	/**
	 * 指定标题行每一个单元格的文字，单元格的横纵坐标取默认值，横坐标按顺序依次递增，纵坐标取0
	 * 
	 * @param string
	 */
	public void addCell(String string) {
		if (row != null && !row.isEmpty()) {
			addCell(string, row.size());
		} else {
			addCell(string, 0);
		}
	}

	/**
	 * 指定标题行每一个单元格的文字，并指定单元格的横坐标，纵坐标取0
	 * 
	 * @param string
	 * @param xPos
	 */
	public void addCell(String string, int xPos) {
		addCell(string, xPos, 0);
	}

	/**
	 * 指定标题行每一个单元格的文字，并指定单元格的横坐标，纵坐标
	 * 
	 * @param string
	 * @param xPos
	 * @param yPos
	 */
	public void addCell(String string, int xPos, int yPos) {
		Cell cell = new Cell();
		cell.setCaption(string);
		cell.setYPos(yPos);
		cell.setXPos(xPos);
		row.add(cell);
	}

	/**
	 * 清空标题信息
	 * 
	 */
	public void initRow() {
		row.clear();
	}
}
