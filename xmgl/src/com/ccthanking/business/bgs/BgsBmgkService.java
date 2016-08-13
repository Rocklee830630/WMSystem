package com.ccthanking.business.bgs;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface BgsBmgkService  {
	String TongChouJiHuaQuery(HttpServletRequest request,User user) throws Exception;

	String faWenQingKuang(HttpServletRequest request, User user) throws Exception;

	String shouWenQingKuang(HttpServletRequest request, User user) throws Exception;

	String liuChengQingKuang(HttpServletRequest request, User user) throws Exception;

	String cunZaiWenTiQingKuang(HttpServletRequest request, User user) throws Exception;

	String tongChouJiHuaQingKuang(HttpServletRequest request, User user) throws Exception;
}
