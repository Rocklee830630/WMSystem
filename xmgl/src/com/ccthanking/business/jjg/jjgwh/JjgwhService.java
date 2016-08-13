package com.ccthanking.business.jjg.jjgwh;

import com.ccthanking.framework.common.User;

public interface JjgwhService {

	String query_jjg(String json,User user,String iswg,String tbjjg) throws Exception;//普通查询
	String query_xmxx(String json,User user) throws Exception;//项目信息查询
	String insert_jjg(String json,User user,String ywid) throws Exception ;//维护插入
	String update_jjg(String json,User user) throws Exception ;//维护修改
}
