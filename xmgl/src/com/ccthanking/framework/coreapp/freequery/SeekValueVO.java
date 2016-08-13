//v20071212-1
package com.ccthanking.framework.coreapp.freequery;

public class SeekValueVO {
	
	private String strNum;    //
	private String strROp;    //¹ØÏµ²Ù×÷·û
	private String strLOp;    //Âß¼­²Ù×÷·û
	private String strVal;    //²éÑ¯ÄÚÈÝ
	private String strLSign;  //×óÀ¨ºÅ
	private String strRSign;  //ÓÒÀ¨ºÅ
	private String strFmt;
	
	public SeekValueVO(){
		
		strLSign = "";
		strRSign = "";
		strFmt = "";
	}
	
	public String getNum(){
		return strNum; 
	}
	
	public String getROp(){
		return strROp;
	}
	
	public String getLOp(){
		return strLOp;
	}
	
	public String getVal(){
		return strVal;
	}
	
	public String getLSign(){
		return strLSign;
	}
	
	public String getRSign(){
		return strRSign;
	}
	
	public String getFmt(){
		return strFmt;
	}
	
	public void setFmt(String p_Fmt){
		strFmt = p_Fmt; 
	}
	
	public void setNum(String p_Num){
		strNum = p_Num; 
	}
	
	public void setROp(String p_ROp){
		strROp = p_ROp;
	}
	
	public void setLOp(String p_LOp){
		strLOp = p_LOp;
	}
	
	public void setVal(String p_Val){
		strVal = p_Val;
	}
	
	public void setLSign(String p_LSign){
		strLSign = p_LSign;
	}
	
	public void setRSign(String p_RSign){
		strRSign = p_RSign;
	}

}
