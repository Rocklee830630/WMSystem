package com.ccthanking.business.common_yw.kgtjgl;
import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;

public interface KgtjglService {

	String query(HttpServletRequest request,String json,User user) throws Exception;//普通查询
	String insert(HttpServletRequest reques,String json,User user) throws Exception;//
	String insert_tb(HttpServletRequest reques,String json,User user) throws Exception;//
}
