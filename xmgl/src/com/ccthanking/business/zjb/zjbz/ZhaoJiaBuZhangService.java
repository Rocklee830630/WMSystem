package com.ccthanking.business.zjb.zjbz;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;
import com.ccthanking.framework.service.BaseService;

public interface ZhaoJiaBuZhangService  {
	String lanBiaoJiaTongJi(HttpServletRequest request,User user, String nd);

	String shenJianLv(User user, String nd,HttpServletRequest request);

	/*String lanBiaoJiaNianDuTongJi(User user, String nd);*/

	String jieSuanTongJi(User user, String nd);

	String shenJiYuSongShen(User user, String nd);

	String caiShenYuShenJi(User user, String nd);

	String shenHeYuCaiShen(User user, String nd);

	String songShenYuShenHe(User user, String nd);

	String jieSuanNianDuTongJi(User user, String nd);

	String weiTuoZiXunGongSiTongJi(HttpServletRequest request,User user, String nd);

	String queryConditionLbj(String msg, User user, HttpServletRequest request);
}
