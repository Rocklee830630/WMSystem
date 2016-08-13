package com.ccthanking.framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

public class securityFilter implements Filter {

	/**
	 * 错误页面
	 */
	private String errorPage;
	private String validSessionMsg = "无效的会话，请重新登录!";

	/**
	 * 不用过滤的URL
	 */
	private String[] notFilterURL;

	private FilterConfig filterConfig = null;

	public void init(FilterConfig filterConfig) throws ServletException {

		this.filterConfig = filterConfig;
		if (filterConfig != null) {
			errorPage = filterConfig.getInitParameter("error_page");
			String allURLStr = filterConfig.getInitParameter("allow_url");
			if (allURLStr != null) {
				allURLStr = allURLStr.replaceAll("\t*\n*", "");
				notFilterURL = allURLStr.split(",");
			}
			if (notFilterURL == null) {
				notFilterURL = new String[0];
			}
		}

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException, IOException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		// Get relevant URI.
		String URI = httpServletRequest.getRequestURI();
		String paras = StringUtils.isBlank(httpServletRequest.getQueryString())?"":"?"+httpServletRequest.getQueryString();
		if(URI.endsWith(".do")){
			URI = URI+paras;
		}

		/**
		if (URI.indexOf(".do") < 0) {
			chain.doFilter(request, response);
			return;
		}
		**/
		URI = URI.replaceFirst(httpServletRequest.getContextPath(), "");

		// 判断是否需要过滤
		if (!needFilter(URI)) {
			chain.doFilter(request, response);
			return;
		}

		// 判断Session是否过期
		HttpSession session = httpServletRequest.getSession();
		if (null == session || null == session.getAttribute(
				com.ccthanking.framework.Globals.USER_KEY)) {
			if(httpServletRequest.getHeader("x-requested-with")!=null
					&& httpServletRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
				httpServletResponse.setHeader("sessionStatus","sessionOut");
				httpServletResponse.setStatus(600);
			}else if(URI.indexOf(".do") < 0){
				returnLogin(request, response,
						validSessionMsg, errorPage);
			}else{
				returnLogin(request, response,
						validSessionMsg, httpServletRequest.getContextPath() + errorPage);
			}
			return;
		} 
		chain.doFilter(request, response);
	}

	/**
	 * 判断是否需要过滤
	 * @param uri
	 * @param sessionData
	 * @return
	 */
	private boolean needFilter(String uri) {
		for (int i = 0; i < notFilterURL.length; i++) { // 不用过滤的url
			if (notFilterURL[i].equals(uri))
				return false;
		}
		return true;
	}

	public void destroy() {
	}
	
	private void returnLogin ( ServletRequest request,
			ServletResponse response, String errorString, String forwardPage )
			throws ServletException, IOException
	{
		request.setAttribute("error","无效的会话，请重新登录!");
		request.getRequestDispatcher(forwardPage).forward(request, response);
	}
	
}