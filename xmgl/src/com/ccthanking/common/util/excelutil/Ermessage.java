package com.ccthanking.common.util.excelutil;

import java.util.ArrayList;

public class Ermessage {

	private ArrayList rowid;
	private ArrayList msg;
	private boolean isforward = false;
	//
	public ArrayList getMsg() {
		return msg;
	}
	public void setMsg(ArrayList msg) {
		this.msg = msg;
	}
	public ArrayList getRowid() {
		return rowid;
	}
	public void setRowid(ArrayList rowid) {
		this.rowid = rowid;
	}
	public boolean isIsforward() {
		return isforward;
	}
	public void setIsforward(boolean isforward) {
		this.isforward = isforward;
	}

	
}
