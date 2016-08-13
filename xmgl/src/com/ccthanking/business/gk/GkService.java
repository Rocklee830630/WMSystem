package com.ccthanking.business.gk;

import java.util.List;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.model.autocomplete;

public interface GkService {
	String query_jhgk(String json,User user) throws Exception;//计划概况查询
	String query_tcjh(String json,User user) throws Exception;//统筹计划概况查询
	String query_jcjh(String json,User user) throws Exception;//统筹计划概况查询
}
