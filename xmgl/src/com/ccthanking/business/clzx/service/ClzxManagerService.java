package com.ccthanking.business.clzx.service;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.ccthanking.framework.common.User;

/**
 * @author xiahongbo
 * @date 2014-9-2
 */
public interface ClzxManagerService {
	/**
	 * 用于创建任务，暂时不准备抛出异常
	 * @param ywcode
	 * @param filter
	 * @param user
	 */
	String insert_clzx(HttpServletRequest request,User user) throws Exception ;//
	public String[][] queryYwry(String id);
	public abstract String queryClzxgl();
	public void createTask(String ywcode,String filter,User user,Connection conn);
	public void achieveTask(String ywbid,String filter,User user,Connection conn);
	public void achieveTaskByJhfk(String ywbid,JSONObject json,HttpServletRequest request,Connection conn);
	public String queryYwlx(String json,HttpServletRequest request);
	
	/**
	 * 查询业务时间节点的的列表
	 * @param request
	 * @return
	 */
	public String queryList(String json, HttpServletRequest request);
	
	/**
	 * 查询首页处理中心图像的数量
	 * @param request
	 * @return
	 */
	public String queryCount(HttpServletRequest request);
	
	/**
	 * 查询首页处理中心的图像是否显示。结果为0不显示，结果不为0显示
	 * @param request
	 * @return
	 */
	public String queryIsShowClzx(HttpServletRequest request);
	public String plfkJh(HttpServletRequest request);
	String queryListLS(String msg, HttpServletRequest request);
}
