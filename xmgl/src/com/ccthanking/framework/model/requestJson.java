package com.ccthanking.framework.model;

public class requestJson implements java.io.Serializable {

	private boolean success = false;// 是否成功
	private String msg = "";// 返回值信息
	private String prompt = "";// 提示信息
	private Object obj = null;// 其他信息

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt){
		this.prompt = prompt;
	}
	
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}