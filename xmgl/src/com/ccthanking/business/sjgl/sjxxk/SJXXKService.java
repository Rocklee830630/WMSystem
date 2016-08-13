package com.ccthanking.business.sjgl.sjxxk;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;
import com.ccthanking.business.sjgl.sj.GcSjVO;

public interface SJXXKService 
{
	String xiangMuXinXi(User user,HttpServletRequest request);

	String queryTongCJH(String msg, User user,HttpServletRequest request);

	String queryJiHuaWanCheng(String msg, User user, HttpServletRequest request);

	String queryJianCeJianCe(String msg, User user, HttpServletRequest request);

	String queryJiaoJunGong(String msg, User user, HttpServletRequest request);

	String queryHeTongXinXi(String msg, User user, HttpServletRequest request);
}
