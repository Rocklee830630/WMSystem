package com.ccthanking.business.pqgl.service;

import javax.servlet.http.HttpServletRequest;

public interface PqglService {
	//查询项目信息
	String queryProjectInfo(HttpServletRequest request,String json) throws Exception;
	//查询排迁信息
	String queryPqInfo(HttpServletRequest request,String json) throws Exception;
	//更新计划反馈
	String doJhfk(HttpServletRequest request,String json) throws Exception;
	//更新业内情况
	String doYnqk(HttpServletRequest request,String json) throws Exception;
	//查询进展剩余数据
	String queryJzsy(HttpServletRequest request,String json) throws Exception;
	//插入进展剩余数据
	String insertJzsy(HttpServletRequest request,String json) throws Exception;
	//修改进展剩余数据
	String updateJzsy(HttpServletRequest request,String json) throws Exception;
	//查询问题解决数据
	String queryWtjj(HttpServletRequest request,String json) throws Exception;
	//插入问题解决数据
	String insertWtjj(HttpServletRequest request,String json) throws Exception;
	//更新问题解决数据
	String updateWtjj(HttpServletRequest request,String json) throws Exception;
	//更新排迁子项目数据
	String updatePqzxm(HttpServletRequest request,String json) throws Exception;
	//插入排迁子项目数据
	String insertPqzxm(HttpServletRequest request,String json) throws Exception;
	//查询排迁子项目
	String queryPqzxmByPk(HttpServletRequest request,String json) throws Exception;
	//查询进展反馈数据
	String queryJzfk(HttpServletRequest request,String json) throws Exception;
	//插入进展反馈表
	String insertJzfk(HttpServletRequest request,String json) throws Exception;
	//更新进展反馈表
	String updateJzfk(HttpServletRequest request,String json) throws Exception;
	//删除进展反馈表
	String deleteJzfk(HttpServletRequest request,String json) throws Exception;
	//计划反馈
	String doJhsjfk(HttpServletRequest request,String json) throws Exception;
	//获取计划反馈次数
	String getJhfkCounts(HttpServletRequest request,String json) throws Exception;
	//获取子项目个数
	String getPqzxmCounts(HttpServletRequest request,String json) throws Exception;
	//删除排迁子项目
	String deletePqzxm(HttpServletRequest request, String json) throws Exception;
	//导出
	String doCustomExportExcel(HttpServletRequest request,String json) throws Exception;
}
