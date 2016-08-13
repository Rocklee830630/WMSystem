<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.*"%>
<%@ page import="com.ccthanking.framework.coreapp.orgmanage.OrgDeptManager"%>
<%@ page import="com.ccthanking.framework.common.*"%>

<%
	String sjbh = request.getParameter("sjbh");
	String spbh = request.getParameter("spbh");
	boolean isTs = false;
	String sql = "select rwlx,ywlx,to_char(cjsj,'YYYY-MM-DD HH24:MI:SS'),cjdwdm,cjrxm,to_char(wcsj,'YYYY-MM-DD HH24:MI:SS'),wcdwdm,wcrxm,result,dbdwdm from ap_task_schedule t where t.sjbh='"
			+ sjbh + "' order by cjsj";
	String[][] taskList = DBUtil.query(sql);
	//通过审批编号判断是否为特送审批
	if (!Pub.empty(sjbh)) {

		String typesql = "select b.processtype from AP_PROCESSINFO a,ap_processtype b where a.processtypeoid = b.processtypeoid and a.eventid = '"
				+ sjbh + "'";
		String[][] rstaskList = DBUtil.query(typesql);

		if (rstaskList != null && rstaskList[0][0].equals("4")) {
			isTs = true;
		}
	}
	String rwlx = Pub.getDictValueByCode("YWLX", taskList[0][1]);
	String slsj = taskList[0][2];
	String slr = taskList[0][4];
	String pcs = Pub.getDeptNameByID(taskList[0][3]);
	Collection set = TaskBO.getProcessSteps(spbh);
	int length = set.size();
	Iterator itor = set.iterator();
	//
	String spsj = "";

	String spdw = "";

	String spr = "";
	String spjg = "";
	String fillcolor = "ProcessYInfo Process";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<head>
<TITLE>审批流程图示</TITLE>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<BODY>

<p></p>
<%
	if (isTs) {
%>
<div class="container-fluid">
    <div class="row-fluid">
		<div class="span12">
        	<div class="windowOpenAuto">
                <div class="ProcessYBox">
                <%
                	for (int j = 0; j < taskList.length; j++) {
                			if (j == 0) {
                				spsj = taskList[j][5];

								
								spdw = Pub.getDeptNameByID(taskList[j][6]);
								if(Pub.empty(spdw)){
									spdw = "";
								}
								
								// getDeptByID
								spr = taskList[j][7];

                %>
                			<div class="ProcessYSuccess Process">
							<h5 style="margin-top:0px;padding-bottom: 0px;padding-top:0px;"><%=rwlx%></h5>
							申请时间：<%=slsj%><br> 申请部门：<%=pcs%><br> 申请人：<%=slr%>
								</div>
						
							<%
							} else {
										spsj = taskList[j][5];

								
										spdw = Pub.getDeptNameByID(taskList[j][6]);
										if(Pub.empty(spdw)){
											spdw = "";
										}
										
										// getDeptByID
										spr = taskList[j][7];

									}
                		if(!"".equals(spsj))
                		{
                			fillcolor = "ProcessYSuccess Process";
                		}
						%>
                		<div class="next">
			下一步<i class="icon-hand-down"></i>
		</div>
		
		<div  class="<%=fillcolor%>"></div>
			<h5 style="margin-top:0px;padding-bottom: 0px;padding-top:0px;"><%=rwlx%></h5>
			审批时间：<%=spsj%><BR /> 审批部门：<%=spdw%><BR /> 审批 人：<%=spr%><BR />
					
            </div>
                 <%
                 	}
                 %>
        </div>
    </div>
</div>
</div>
<%
	}
%>
</BODY>
</HTML>