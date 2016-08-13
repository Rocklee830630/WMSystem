package com.ccthanking.framework.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class DialogsTag extends TagSupport {
	
	private String alertDialogs()
	{
		String text = "";
		text +="<div id=\"myAlert\" class=\"modal hide fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">\r\n";
		text +="<div class=\"modal-header\">\r\n";
		text +="<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\"></button>";
		text +="<h3 id=\"myAlertTitle\"></h3>";
		text +="</div>\r\n";
		text +="<div class=\"modal-body\">\r\n";
		text +="<p id=\"myAlertContent\"></p>\r\n";
		text +="</div>\r\n";
		text +="<div class=\"modal-footer\">\r\n";
		text +="<button data-dismiss=\"modal\" class=\"btn btn-inverse\" id=\"myAlertButton\">确定</button>\r\n";
		text +="</div>\r\n";
		text +="</div>\r\n";
		return text;
	}
	private String confirmDialogs()
	{
		String text = "";
		text +="<div id=\"myConfirm\" class=\"modal hide fade\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">\r\n";
		text +="<div class=\"modal-header\">\r\n";
		text +="<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\"></button>";
		text +="<h3 id=\"myConfirmTitle\"></h3>";
		text +="</div>\r\n";
		text +="<div class=\"modal-body\">\r\n";
		text +="<p id=\"myConfirmContent\"></p>\r\n";
		text +="</div>\r\n";
		text +="<div class=\"modal-footer\">\r\n";
		text +="<button class=\"btn\" data-dismiss=\"modal\" aria-hidden=\"true\" id=\"ConfirmCancleButton\">取消</button>\r\n";
		text +="<button data-dismiss=\"modal\" class=\"btn btn-inverse\" id=\"ConfirmYesButton\">确定</button>\r\n";
		text +="</div>\r\n";
		text +="</div>\r\n";
		return text;
	}
	
	
	
	private String getResult() throws JspException {

		StringBuffer sb = new StringBuffer();
		sb.append(alertDialogs()+confirmDialogs());
		String str = sb.toString();
		sb = null;
		return str;
	}
	
	
	public int doEndTag() throws JspTagException {
		try {
			// 获取页面输出流，并输出字符串
			String appPath = ((HttpServletRequest) pageContext.getRequest())
					.getContextPath();
			String base = "";
			base +="";
			pageContext.getOut().write( getResult());
		}
		// 捕捉异常
		catch (IOException ex) {
			// 抛出新异常
			// throw new JspTagException("错误"};
		} catch (JspException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 值回返
		return EVAL_PAGE;
	}

}
