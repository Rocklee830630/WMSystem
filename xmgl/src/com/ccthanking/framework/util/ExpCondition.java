package com.ccthanking.framework.util;

public class ExpCondition {

	private String strLogic;
	private String strLeft;
	private String strRight;
	private String strValue;

	public ExpCondition() {
		strLogic = "";
		strLeft = "";
		strRight = "";
		strValue = "";
	}

	public ExpCondition(String p_Logic, String p_Left, String p_Value,
			String p_Right) {
		strLeft = p_Left;
		strLogic = p_Logic;
		strRight = p_Right;
		strValue = p_Value;
	}

	public String getLeftSign() {
		return strLeft;
	}

	public void setLeftSign(String p_Left) {
		strLeft = p_Left;
	}

	public String getStrLogic() {
		return strLogic;
	}

	public void setStrLogic(String p_Logic) {
		strLogic = p_Logic;
	}

	public String getRightSign() {
		return strRight;
	}

	public void setRightSign(String p_Right) {
		strRight = p_Right;
	}

	public String getItemValue() {
		return strValue;
	}

	public void setItemValue(String p_Value) {
		strValue = p_Value;
	}

}
