package com.ccthanking.framework.common;

public class UserTemplate implements java.io.Serializable {
	
	public UserTemplate() {
	}

	public String topURL = null;
	public String submenuURL = null;
	public String childmenuURL = null;
	public String skipURL = null;
	public String pageareaURL = null;
	public String rowsLine = null;
	public String colsLine = null;
	public String cssFilePath = null;
	public String winColor = null;
	public String winCaretColor = null;
	public String winTitleColor = null;
	public String iColor = null;
	public String ImagePath = null;

	public String getImagePath() {
		return ImagePath;
	}

	public void setImagePath(String imagePath) {
		this.ImagePath = imagePath;
	}

	public void setSubmenuURL(String submenuURL) {
		this.submenuURL = submenuURL;
	}

	public String getSubmenuURL() {
		return submenuURL;
	}

	public String getChildmenuURL() {
		return childmenuURL;
	}

	public void setChildmenuURL(String childmenuURL) {
		this.childmenuURL = childmenuURL;
	}

	public String getPageareaURL() {
		return pageareaURL;
	}

	public void setPageareaURL(String pageareaURL) {
		this.pageareaURL = pageareaURL;
	}

	public String getSkipURL() {
		return skipURL;
	}

	public void setSkipURL(String skipURL) {
		this.skipURL = skipURL;
	}

	public String getTopURL() {
		return topURL;
	}

	public void setTopURL(String topURL) {
		this.topURL = topURL;
	}

	public String getRowsLine() {
		return rowsLine;
	}

	public String getColsLine() {
		return colsLine;
	}

	public void setColsLine(String colsLine) {
		this.colsLine = colsLine;
	}

	public void setRowsLine(String rowsLine) {
		this.rowsLine = rowsLine;
	}

	public String getCssFilePath() {
		return cssFilePath;
	}

	public void setCssFilePath(String cssFilePath) {
		this.cssFilePath = cssFilePath;
	}

	public String getWinCaretColor() {
		return winCaretColor;
	}

	public String getWinColor() {
		return winColor;
	}

	public String getWinTitleColor() {
		return winTitleColor;
	}

	public void setWinCaretColor(String winCaretColor) {
		this.winCaretColor = winCaretColor;
	}

	public void setWinColor(String winColor) {
		this.winColor = winColor;
	}

	public void setWinTitleColor(String winTitleColor) {
		this.winTitleColor = winTitleColor;
	}

	public String getIColor() {
		return iColor;
	}

	public void setIColor(String iColor) {
		this.iColor = iColor;
	}

}