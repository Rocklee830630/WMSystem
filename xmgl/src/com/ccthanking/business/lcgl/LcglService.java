package com.ccthanking.business.lcgl;

import javax.servlet.http.HttpServletRequest;

import com.ccthanking.framework.common.User;

public interface LcglService {

	public String queryLcInfo(HttpServletRequest request,String json) throws Exception;
	public String queryLcInfo(HttpServletRequest request,String json,String defineCondition) throws Exception;
	public String queryFlowApplyByPerson(HttpServletRequest request,String json,User user) throws Exception;
	public String createSP(HttpServletRequest request,String json) throws Exception;
	public String queryProcessType(HttpServletRequest request,String json) throws Exception;	
	public String queryProcessStep(HttpServletRequest request,String json) throws Exception;
	public String saveProcess(HttpServletRequest request,String json,User user) throws Exception;
	public String deleteProcess(String json,User user) throws Exception;
	public String queryProcessWs(HttpServletRequest request,String processoid) throws Exception;
	public String saveApwstypz(HttpServletRequest request,String json,User user) throws Exception;
	public String[] queryProcessTask(HttpServletRequest request,String processoid) throws Exception;




}
