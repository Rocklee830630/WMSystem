package com.ccthanking.common;

import com.ccthanking.framework.base.BaseVO;
import com.ccthanking.framework.common.User;
import com.ccthanking.framework.util.Pub;

/**
 * @author hongpeng.dong
 *
 */
public class BusinessUtil {

	/**
	 * 得到数据有效性过滤条件
	 * @param prefix
	 * @return
	 */
	public static String getSJYXCondition(String prefix){
		String pre = Pub.empty(prefix)?"":prefix+".";
		return " and " + pre + "sfyx = '1' ";
	}


	/**
	 * 预留的公共过滤条件
	 * @param prefix
	 * @return
	 */
	public static String getCommonCondition(User user,String prefix){
		return "";
	}


		/**
	 * 新增时写入相关公共字段
	 * add by hongpeng.dong at 2013-8-12
	 * @param vo
	 * @param user
	 * @return
		 * @throws Exception 
	 */
	public static void setInsertCommonFields(BaseVO vo,User user) throws Exception{
		setUpdateCommonFields(vo,user);
		vo.setInternal("LRR",user.getAccount());
		vo.setInternal("LRSJ",vo.getInternal("GXSJ"));
		vo.setInternal("LRBM",user.getDepartment());
		vo.setInternal("LRBMMC",user.getOrgDept().getDept_Name());
		vo.setInternal("SFYX","1");
	}

	/**
	 * 新增时写入相关公共字段
	 * add by hongpeng.dong at 2013-8-12
	 * @param vo
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public static void setUpdateCommonFields(BaseVO vo,User user) throws Exception{
		vo.setInternal("GXR",user.getAccount());
		vo.setInternal("GXSJ",Pub.getCurrentDate());
		vo.setInternal("GXBM",user.getDepartment());
		vo.setInternal("GXBMMC",user.getOrgDept().getDept_Name());
	}

	
}
