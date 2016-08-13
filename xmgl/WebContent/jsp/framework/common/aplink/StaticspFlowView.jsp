<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.coreapp.aplink.*"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%

	User user = (User) session.getAttribute(Globals.USER_KEY);
	String userName = user.getName();
	String processtypeoid = request.getParameter("operationoid");
	//获取AP_PROCESSTYPE表中的processtypeoid值
	Connection conn = DBUtil.getConnection();

	try {
		com.ccthanking.framework.coreapp.aplink.Process process = null;
		if (!Pub.empty(processtypeoid)) {
			process = ProcessTypeMgr.getProcessByID(conn,
					processtypeoid);
			//
			Collection set = process.getSteps();
			int length = set.size();
			Iterator itor = set.iterator();
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
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="windowOpenAuto">
					<div class="ProcessYBox">
						<div class="ProcessYInfo Process">
							<h5	style="margin-top: 0px; padding-bottom: 0px; padding-top: 0px;">流程申请</h5>
							<b>申请人：</b><%=userName%>
						</div>
						<%
							int t = 0;
									while (itor.hasNext()) {
										//处理用户单位编码范围的方法
										Step step = (Step) itor.next();

										String title = step.getName();
										String days = step.getShedule_days();
										String isMS = step.getIsMS();
										String isMS_memo ="";
										String isCC = step.getIsCC();
										String actors = step.getActor();
										String ccactors = step.getCcActor();
										String roleName = step.getRole();
										
										if(!Pub.empty(roleName)){
											roleName = Pub.getRoleNameByID(roleName);
										}
										String zs = "";
										String cs = "";
										if(isMS!=null){
											isMS_memo = Pub.getDictValueByCode("SPJDBSFS", isMS);
										}
										
										if("1".equals(isMS))
										{
										 if(zs.indexOf(",")<0)
										 {
										 	zs = Pub.getUserNameByLoginId(actors);
										 }else
										 {
											String res[] = actors.split(",");
											if(res!=null)
											{
												for(int i=0;i<res.length;i++)
												{
													zs += Pub.getUserNameByLoginId(res[i])+"，";//("+res[i]+")
												}
												zs = zs.substring(0,zs.length()-1);
											}
										 }
								    	}
										
										if("1".equals(isCC))
										{
											String res[] = ccactors.split(",");
											if(res!=null)
											{
												for(int i=0;i<res.length;i++)
												{
													cs += Pub.getUserNameByLoginId(res[i])+"，";//("+res[i]+")
												}
												cs = cs.substring(0,cs.length()-1);
											}
										}


						%>

						<div style="text-align:center">
							<i class="icon-arrow-down" align="center"></i>
						</div>
						<div class="ProcessYInfo Process">
							<h5	style="margin-top: 0px; padding-bottom: 0px; padding-top: 0px;"><%=title%></h5>
							<b>报送方式：</b><span><%=isMS_memo==null?"":isMS_memo%></span><br>
							<b>主送：</b><span><%=zs==""?roleName:zs%></span><br>
							<%if(cs!=null&&cs.length()>0){ %><b>抄送：</b><%=cs==""?"":cs%><br><%} %>
							<b>时限：</b><%=days==null?"":days%>天
						</div>
					<%
						}
									t++;

						}
							
						} catch (Exception ex) {
							ex.printStackTrace();
						} finally {
							if (conn != null) {
								conn.close();
							}
						}
					%>
				</div>
			</div>
		</div>
	</div>


</BODY>
</HTML>