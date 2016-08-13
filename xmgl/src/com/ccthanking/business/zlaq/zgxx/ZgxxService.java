package com.ccthanking.business.zlaq.zgxx;

import java.util.List;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;

public interface ZgxxService {
	
	String query_zg(String json,User user,String flag) throws Exception;//普通查询
	String insert_zg(String json,User user,String ywid) throws Exception ;//维护插入
	String update_zg(String json,User user,String ywid) throws Exception ;//维护修改
	String insert_hf(String json,User user,String ywid,String flag) throws Exception ;//回复插入
	String update_hf(String json,User user,String ywid,String flag) throws Exception ;//回复修改
	String insert_fc(String json,User user,String ywid) throws Exception ;//复查插入
	String update_fc(String json,User user,String ywid) throws Exception ;//复查修改
	List<autocomplete> xmmcAutoComplete(autocomplete json,User user)  throws Exception;//项目名称自动补全
}
