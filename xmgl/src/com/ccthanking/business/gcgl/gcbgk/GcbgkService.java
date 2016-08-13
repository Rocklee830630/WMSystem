package com.ccthanking.business.gcgl.gcbgk;

import com.ccthanking.framework.common.User;

public interface GcbgkService {
	String gcqs_zq_nd(String json,User user,String nd,String xmglgs) throws Exception;//年度洽商数
	String gcqs_zq_by(String json,User user,String nd,String xmglgs) throws Exception;//本月洽商数
}
