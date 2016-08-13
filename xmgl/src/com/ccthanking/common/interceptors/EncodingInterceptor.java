package com.ccthanking.common.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.stereotype.Repository;  

import com.ccthanking.framework.Globals;

@Repository  
public class EncodingInterceptor extends HandlerInterceptorAdapter {

	public static String getRequestPath(HttpServletRequest request) {
		String requestPath = request.getRequestURI() + "?" + request.getQueryString();
		if (requestPath.indexOf("&") > -1) {// 去掉其他参数
			requestPath = requestPath.substring(0, requestPath.indexOf("&"));
		}
		requestPath = requestPath.substring(request.getContextPath().length());// 去掉项目路径
		return requestPath;
	}
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		
		    String requestPath = getRequestPath(request);
		    if(requestPath.indexOf("userController.do?login")>-1)
		    {
		    	return true;
		    }
		    
		    Object obj = request.getSession().getAttribute(Globals.USER_KEY);  
	        if(obj==null){  
	            request.getRequestDispatcher("/index.jsp").forward(request, response);   
	        return false;  
	        }  
	        else{  
	            return super.preHandle(request, response, object);  
	        }  
		
	}

}

