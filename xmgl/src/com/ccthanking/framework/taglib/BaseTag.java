package com.ccthanking.framework.taglib;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.ccthanking.framework.Globals;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;


public class BaseTag extends TagSupport {
	
	private String sid;
	private String page;

	private static String result = null;
	
	public String baseConfPath = "/WEB-INF/tld/ScriptConfig.xml";

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getPage() {
		return (this.page);
	}

	public void setPage(String page) {
		this.page = page;
	}

	private String getResult() throws JspException {
		if (null == result) {
			result = getStr();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(result);
		String str = sb.toString();
		sb = null;
		return str;
	}

	private String getStr() throws JspException {
		
		//不进行缓存
		HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
		response.setHeader("Pragma","No-cache");
		response.setHeader("Cache-Control","no-cache");
		response.setDateHeader("Expires", 0);
		
		StringBuffer stringbuffer = new StringBuffer();
		File appConfig = null;
		SAXReader saxReader = null;
		Document document = null;
		try {

			// RightManager.getInstance().checkPageRight(pageContext);

			User user = (User) ((HttpServletRequest) pageContext.getRequest())
					.getSession().getAttribute(Globals.USER_KEY);

			
			String appPath = ((HttpServletRequest) pageContext.getRequest())
					.getContextPath();
			appPath = appPath.substring(1);
			String realPath = pageContext.getServletConfig()
					.getServletContext().getRealPath("/");
			appConfig = new File(realPath + baseConfPath);
			saxReader = new SAXReader();
			document = saxReader.read(appConfig);
			List cssList = document.selectNodes("//BaseConfig//LINK//LinkName");
			List jsList = document.selectNodes("//BaseConfig//JS//JSName");

			List pageList = document.selectNodes("//BaseConfig//META//CONTENT");
			Iterator pageit = pageList.iterator();
			while (pageit.hasNext()) {
				stringbuffer
						.append("<META  http-equiv=\"Page-Exit\" content=\""
								+ ((Node) pageit.next()).getText() + "\">\r\n");
			}

			Iterator cssit = cssList.iterator();
			String cssPath = null;

			while (cssit.hasNext()) {
				stringbuffer
						.append("<LINK type=\"text/css\" rel=\"stylesheet\" href=\"");
				Node cssNode = (Node) cssit.next();
				if (cssPath != null) {
					stringbuffer.append("/" + appPath + "/css/" + cssPath + "/"
							+ cssNode.getText());
				} else {
					stringbuffer.append("/" + appPath + "/css/"
							+ cssNode.getText());
				}
				stringbuffer.append("\"> </LINK>\n");
			}
			Iterator jsit = jsList.iterator();
			String tempjs = "";
			while (jsit.hasNext()) {
				stringbuffer.append("<script type=\"text/javascript\" src=\"");
				tempjs = ((Node) jsit.next()).getText();

				stringbuffer.append("/" + appPath + "/");
				stringbuffer.append(tempjs + "\"> </script>\r\n");
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new JspException(e.getMessage());
		} finally {
			document = null;
			saxReader = null;
			appConfig = null;
		}
		String str = stringbuffer.toString();
		stringbuffer = null;
		return str;
	}
	public int doEndTag() throws JspException {
		try {
			  /**
		      HttpSession session = pageContext.getSession();
		      if(!Pub.empty(sid) && !sid.equals(session.getId())) throw new Exception("无效的会话，请重新登录！");
		      User user = (User) session.getAttribute(Globals.USER_KEY);
		      if(user == null) throw new Exception("无效的会话，请重新登录！");
		      **/
			// 获取页面输出流，并输出字符串
			String appPath = ((HttpServletRequest) pageContext.getRequest())
					.getContextPath();
			String base = "";
			base +="";
			pageContext.getOut().write( getResult());
		}
		// 捕捉异常
		catch (Exception e) {
			  /**
		      HttpSession session = pageContext.getSession();
		      session.invalidate();
		      try {
		    	pageContext.getRequest().setAttribute("error","无效的会话，请重新登录!");
		        pageContext.forward("/jsp/framework/error/500.jsp");
		      }
		      catch (Exception ex) {
		        throw new JspException(ex.getMessage());
		      }
		      return (SKIP_PAGE);
		      **/
		    }
		// 值回返
		return EVAL_PAGE;
	}
}