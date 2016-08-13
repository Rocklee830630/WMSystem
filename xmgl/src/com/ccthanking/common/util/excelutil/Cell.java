/**
 * 
 */
package com.ccthanking.common.util.excelutil;

/**
 * 存储行每一个单元格信息
 * 
 * @author cbl
 * 
 */
public class Cell {

	/**
	 * 文字
	 */
	private String caption;

	/**
	 * 横坐标
	 */
	private int xPos;

	/**
	 * 纵坐标
	 */
	private int yPos;

	/**
	 * 获得文字
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * 设置文字
	 */
	public void setCaption(String string) {
		this.caption = string;
	}

	/**
	 * 获得横坐标
	 */
	public int getXPos() {
		return xPos;
	}

	/**
	 * 设置横坐标
	 */
	public void setXPos(int pos) {
		xPos = pos;
	}

	/**
	 * 获得纵坐标
	 */
	public int getYPos() {
		return yPos;
	}

	/**
	 * 设置纵坐标
	 */
	public void setYPos(int pos) {
		yPos = pos;
	}
}
