

package com.ccthanking.framework.service;

import com.ccthanking.framework.common.User;
import javax.servlet.http.HttpServletRequest;


public interface MessageService extends BaseService {

	/**
	 * @author wangzh
	 * @memo
	 * @return string
	 * @throws Exception 
	 */
	String getMessage(User user) throws Exception;
	String getUserMessage(String msg ,User user) throws Exception;
	String changeZt(String msg ,User user,HttpServletRequest request) throws Exception;


}
