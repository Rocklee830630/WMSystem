<html>
<head>
<title>长春市政府投资建设项目管理中心——综合管控中心</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.wsdy.TemplateUtil"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<%
   	String tt= (String)request.getParameter("param");
   	tt=tt.replace('|','&');
   	String isEdit=(String)request.getParameter("isEdit");
   	String flaid = (String)request.getParameter("flaid");
  	String sql = "select accnname,acconame,t.accdir from TAC_ACCESSORY t where t.accdir = 'FlowApply' and t.accbelongid = "+flaid;
	String accname[][] = DBUtil.query(sql);
	
  	String sqlfle = "select t.accnname, t.acconame, "+
  	       			"(select e.dptname from TAB_DEPARTMENT e,TAB_EMPLOYEE f where e.dptid = f.empdptid and f.empid = d.flerempid)||'/'||d.flehempname as fjmc,t.accdir "+
  	     			" from TAC_ACCESSORY t, TAC_FLOWEXECUTE d "+
  	    			" where t.accbelongid = d.fleid "+
  	      			" and t.accdir = 'Flow' and d.fleflaid = "+flaid;
	String fleaccname[][] = DBUtil.query(sqlfle);
	
	String stepSql = "select fleid,to_char(flerdate,'YYYY-MM-DD HH24:MI:SS') AS flerdate,flerempname,to_char(flehdate,'YYYY-MM-DD HH24:MI:SS') AS flehdate,flehempname,fleparentid from TAC_FLOWEXECUTE t where t.fleflaid = "+flaid +" order by flestep ,flerdate";
	QuerySet qs = DBUtil.executeQuery(stepSql, null);
	TemplateUtil tu = new TemplateUtil();
%>
<script>
	function checkupFiles(m,n,o){
		var obj = $(document).find("#previewFileid");
		$("#previewMethod").val("history");
		$("#previewName").val(m);
		$("#previewType").val(n);
		$("#previewDir").val(o);
		window.open(encodeURI("${pageContext.request.contextPath }/jsp/file_upload/showPreview.jsp"));
	}
	

	//页面初始化
	$(function() {
		$("#tab0").click(function() {
			$("#example_query").removeAttr("disabled");
		});

		$("#tab1").click(function() {
			$("#example_query").attr("disabled","disabled");
		});
		
		$("#tab2").click(function() {
			$("#example_query").attr("disabled","disabled");
		});
	});
</script>
</head>
<body>
<div class="container-fluid">
    <p><!--占位用--></p>
    <div class="row-fluid">
        <div class="row-fluid">
        <p class="text-right tabsRightButtonP">
        <button id="example_query" class="btn btn-link" type="button" onclick="printDiv()"><i class="icon-print"></i>打印</button>
	    </p>
            <ul class="nav nav-tabs">
                <li class="active"><a href="#home" class="active" data-toggle="tab" id="tab0">&nbsp;&nbsp;表单&nbsp;&nbsp;</a></li>
                <li><a href="#attachedfile" data-toggle="tab" id="tab1">相关附件</a></li>
                <li><a href="#profile" data-toggle="tab" id="tab2">办理情况</a></li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="home" style="height:80%;overflow:hidden;">
                    <div class="windowOpenAuto" style="height:100%;overflow:auto;">


						<iframe id="iFrame" name="iFrame" width="100%" height="95%" src="<%=tt%>" FRAMEBORDER=0 SCROLLING= ></iframe>

                    </div>
                </div>
                <div class="tab-pane" id="attachedfile" style="height:80%;overflow:hidden;">
                	<div class="windowOpenAuto" style="height:100%;overflow:auto;">

					<p>流程申请附件:</p>
					<p style="margin-left:30px;">
						<%
							if(accname!=null&&accname.length>0)
							{
								
								for(int i=0;i<accname.length;i++)
								{
						%>
									<img src="old/MailHaveFile.gif" align="absmiddle">&nbsp;
									<a id="AccHrefi<%=i+1%>" style="CURSOR: hand; TEXT-DECORATION: none" href="javascript:void(0);" onclick="checkupFiles('<%=accname[i][0]%>','<%=accname[i][1]%>','<%=accname[i][2]%>')" target="_black"><%=accname[i][1]%></a><br>
						<%
								}
							}else
							{
						%>
								无附件！
						<%	}
						%>
					</p>
						<%
							if(fleaccname!=null&&fleaccname.length>0)
							{

									out.print("<p>"+fleaccname[0][2]+":</p>");
									
								out.print("<p style='margin-left:30px;'>");
								for(int i=0;i<fleaccname.length;i++)
								{
						%>
									
									<img src="old/MailHaveFile.gif" align="absmiddle">&nbsp;
									<a id="AccHrefj<%=i+1%>" style="CURSOR: hand; TEXT-DECORATION: none" href="javascript:void(0);" onclick="checkupFiles('<%=fleaccname[i][0]%>','<%=fleaccname[i][1]%>','<%=fleaccname[i][3]%>')" target="_black"><%=fleaccname[i][1]%></a><br>
						<%
								}
								out.print("</p>");
							}
						%>

				 </div>
				</div>
                <div class="tab-pane" id="profile" style="height:80%;overflow:hidden;">
					<div class="windowOpenAuto" style="height:100%;overflow:auto;">
						<table width="100%" class="table-hover table-activeTd B-table"	id="DT1" type="single" >
							<thead>
								<tr>
									<th width="5%" name="XH" id="_XH"><font style="font-size:12px;">序号</font></th>
									<th width="8%"><font style="font-size:12px;">送办人</font></th>
									<th width="12%"><font style="font-size:12px;">送办时间</font></th>
									<th width="8%"><font style="font-size:12px;">办理人</font></th>
									<th width="5%"><font style="font-size:12px;">办理状态</font></th>
									<th width="12%"><font style="font-size:12px;">办理时间</font></th>
									<th width="40%"><font style="font-size:12px;">办理结果</font></th>
								</tr>
							</thead>
							<tbody>
								
							<%
								if(qs.getRowCount()!=0)
								{
									
									QuerySet qs1 = null;
									for(int j=0;j<qs.getRowCount();j++)
									{
										String fleid = qs.getString(j+1, "fleid");
										String resultsql = " select "+
											      "(select to_char(flehdate, 'YYYY-MM-DD HH24:MI:SS')  from TAC_FLOWEXECUTE  where t.fleparentid = fleid) AS flerdate,"+
											       "(select e.flerempname from TAC_FLOWEXECUTE e where t.fleparentid = e.fleid) AS flerempname,"+
											       "to_char(t.flehdate, 'YYYY-MM-DD HH24:MI:SS') AS flehdate,"+
											       "t.flerempname as flehempname,t.fleparentid,d.ferffdcname,d.ferresult "+
											  "from TAC_FLOWEXECUTERESULT d,TAC_FLOWEXECUTE t "+
											 "where t.fleid = d.ferfleid(+) and t.fleid ="+fleid;
										qs1 = DBUtil.executeQuery(resultsql, null); 
																	
										
							%>
								<tr style="font-size:14px;">
									<td align="center"><%=j+1%></td>
									<td><%if(j==0){ ;} else{ out.print(qs1.getString(1, "flerempname"));}%></td>
									<td><%if(j==0){ ;} else{ out.print(qs1.getString(1, "flerdate"));}%></td>
									<td><%=qs1.getString(1, "flehempname")%></td>
									<td><%if(null==qs1.getString(1, "flehdate")) out.print("<font color='red'>未办理</font>"); else  out.print("<font color='green'>办完</font>");%></td>
									<td><%if(null!=qs1.getString(1, "flehdate")) out.print(qs1.getString(1, "flehdate"));%></td>
									<td>
										<%
											for(int k=0;k<qs1.getRowCount();k++)
											{
												if(k>0)
													out.println("<br>");
												out.print((qs1.getString(k+1, 6)==null)?"":"<b>"+(qs1.getString(k+1, 6)+":</b>"));
												out.print(tu.getValueFromQS(qs1,k+1,7));
												
											} 
											
										%>
									</td>
								</tr>

							<%
										
									}
									
								}
							%>
							<br>
							</tbody>
						</table>
				
					</div>
				</div>
            </div>
            <p></p>
            <div class="text-right" style="display:none">
                <button class="btn" onclick="printDiv()">打印表单</button>
                <button class="btn" onclick="window.close();">关闭</button>
            </div>
            <script>
		$('#myTab a').click(function (e) {
		  e.preventDefault();
		  $(this).tab('show');
		})
		function printDiv()
		{
			window.frames["iFrame"].printIframe();
			//$("#iFrame").printIframe();
			//document.getElementById("iFrame").window.print();
			//window.print();
		}
		</script> 
        </div>
    </div>
</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<input type="hidden" name="previewFileid" id="previewMethod">
				<input type="hidden" name="previewFileid" id="previewName">
				<input type="hidden" name="previewFileid" id="previewType">
				<input type="hidden" name="previewFileid" id="previewDir">
			</FORM>
		</div>
</body>
</html>