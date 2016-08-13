package com.ccthanking.framework.spflow;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.ccthanking.common.vo.EventVO;
import com.ccthanking.framework.common.User;
//task = taskMgr.createApproveTask(conn, evo.getSjbh(),ywlx,desc + "，请审批。","","","",user,"17169");//分局呈请立案流程
public interface IDealSP
{
    /*
     String operationId  审批流程中的operationid
     String wsTemplateId 对应的文书模板编号
    */
    void dealSP(HttpServletRequest request, HttpServletResponse response,Connection conn,EventVO evo,String ywlx,String desc,User user,String type,JSONObject joList)throws java.lang.Exception;
}