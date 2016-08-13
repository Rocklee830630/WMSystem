package com.ccthanking.business.zjgl.service;

import java.util.HashMap;

import com.ccthanking.framework.common.User;

public interface GcZjglBmgkService {
	String queryBmZList(String json, User user, HashMap map)throws Exception;//总部门支付
	String queryBmList(String json, User user, HashMap map)throws Exception;//年度部门支付
	String queryPCZList(String json, User user, HashMap map)throws Exception;//总录入时间
	String queryPCList(String json, User user, HashMap map)throws Exception;//年份录入时间
	String queryZSZJList(String json, User user, HashMap map)throws Exception;//总征收资金
	String queryZZSZJList(String msg, User user, HashMap map)throws Exception;//年份征收资金
	String queryNFLYBZJList(String msg, User user, HashMap map)throws Exception;//年份履约保证金
	String queryZLYBZJList(String msg, User user, HashMap map)throws Exception;//总履约保证金
	String queryXJNFLYBZJList(String msg, User user, HashMap map)throws Exception;//年份履约保证金（现金）
	String queryBHNFLYBZJList(String msg, User user, HashMap map)throws Exception;//年份履约保证金（保函）
	String queryZXJLYBZJList(String msg, User user, HashMap map)throws Exception;//总履约保证金（现金）
	String queryZBHLYBZJList(String msg, User user, HashMap map)throws Exception;//总履约保证金（保函）
	
	String queryLJFHLVBZJList(String msg, User user, HashMap map)throws Exception;//累计返还履约保证金
	String queryNFLJFHLVBZJList(String msg, User user, HashMap map)throws Exception;//年份累计返还履约保证金


	String queryCLJFHLVBZJList(String msg, User user, HashMap map)throws Exception;//余返还履约保证金

	
	String queryYSXList(String msg, User user, HashMap map)throws Exception;//履约保证金保函已失效
	String queryJJSXList(String msg, User user, HashMap map)throws Exception;//履约保证金保函即将失效
	
	String querySYJEList(String msg, User user, HashMap map)throws Exception;//征收资金使用金额
	String queryNDSYJEList(String msg, User user, HashMap map)throws Exception;//征收资金年度使用金额
	
	
	String queryLJBFYEList(String msg, User user, HashMap map)throws Exception;//累计拨付余额
	String queryBMPXList(String msg, User user, HashMap map)throws Exception;//根据部门查询
	
	/**
     * 根据条件查询记录.
     * 
     * @param json
     * @param user
     * @return
     * @throws Exception
     * @since v1.00
     */
    String queryQyList(String json, User user, HashMap map) throws Exception;
}
