package com.ccthanking.business.zsb.zsbmjk;

import java.sql.SQLException;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface ZengChaiBuZhangService  {

	String queryTongJiGaiKuang(User user,String nd) throws Exception;

	String juMingChaiQianJinZhan(User user,String nd);

	String moDiXiangQing(User user,String nd);

	String qiYeChaiQianJinZhan(User user,String nd);

	String zhengDiMianJiJinZhan(User user,String nd);

	String zhengChaiJinZhan(User user,String nd);
}
