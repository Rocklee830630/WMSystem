<html>
<head>
<title>长春市政府投资建设项目管理中心——综合管控中心</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.wsdy.TemplateUtil"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>

<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>

<%
String tt= (String)request.getParameter("param");
if(tt!=null&&!"".equals(tt))
tt=tt.replace('|','&');

String isEdit=(String)request.getParameter("isEdit");
String isview=(String)request.getParameter("isview");
String sjbh=(String)request.getParameter("sjbh");
String ywlx=(String)request.getParameter("ywlx");

String templateid="";
templateid=(String)request.getParameter("temlateid");
String spzt = request.getParameter("spzt");
if(spzt == null || "".equals(spzt)) 
	spzt = "9";

String ywid = (String)request.getParameter("ywid");
String spbh = (String)request.getParameter("spbh");
if(Pub.empty(templateid)||Pub.empty(templateid)){
	String [][] s = DBUtil.query("select t.ws_templateid,a.processoid from ap_processconfig t,ap_processinfo a where t.sjbh = a.eventid and t.sjbh ='"+sjbh+"'");
	if(s!=null&&s.length>0){
		spbh = s[0][1];
		templateid = s[0][0];
		tt="/xmgl/PubWS.do?getXMLPrintAction&templateid="+templateid+"&isEdit=0&ywlx="+ywlx+"&sjbh="+sjbh+"&rowid="+System.currentTimeMillis()+".mht";
	}
}
String[][] t = DBUtil.query("select PROCESSTYPE from ap_processinfo a,ap_processtype b where a.PROCESSTYPEOID = b.PROCESSTYPEOID and a.PROCESSOID='"+spbh+"'");
String processtype = "";
if(t!=null&&!"".equals(t[0][0])){
	processtype = t[0][0];
}
String flowchart_jsp = "spFlowView_gd";
if("1".equals(processtype)){
	
	flowchart_jsp = "spFlowView_gd";
}
%>

<script language="javascript" >

var isedit='<%=isEdit%>';

function closeT(){

  window.close();
}

$(function() {
    var sjbh = "<%=sjbh%>";
    var spbh = "<%=spbh%>";
    document.getElementById("spFlowView").src = "<%=request.getContextPath()%>/jsp/framework/common/aplink/<%=flowchart_jsp%>.jsp?sjbh="+sjbh+"&spbh="+spbh;
	setFileData("","","<%=sjbh%>","<%=ywlx%>");
	queryFileData("","","<%=sjbh%>","<%=ywlx%>");
});


function saveWS(){

    var form1 = iFrame.wsTemplate;
    var json = Form2Json.formToJSON(form1);
	json = json.replace('}',',"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=templateid%>"}');
	var data1 = defaultJson.packSaveJson(json);

    var wsActionURL = "/xmgl/PubWS.do?saveWSJson";
	$.ajax({
		url : wsActionURL,
		data : data1,
		dataType : 'json',
		async :	false,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {

			if(result)
		       flag = result.msg;
			if("0"==flag)
			{
				alert("文书保存出错！");
				return ;	
			}else
			{
				var ajbh=1;
				var sjbh="<%=sjbh%>";
				var ywlx="<%=ywlx%>";
				var ajmc='ceshi';
				CQSP(ajbh,sjbh,ywlx,"",ajmc,"",null,null);
				
			}
		},
	    error : function(result) {
	     	//alert(234);
	    }
	});

}

function printDiv()
{
	//window.frames["iFrame"].printIframe();
	document.getElementById("iFrame").printIframe();
}

</script>

</head>
<body>
<div class="container-fluid">
    <p><!--占位用--></p>
    <div class="row-fluid">
        <div class="row-fluid">
            <ul class="nav nav-tabs">
                <li class="active"><a href="#home" class="active" data-toggle="tab">&nbsp;&nbsp;表单&nbsp;&nbsp;</a></li>
                <li><a href="#attachedfile" data-toggle="tab">相关附件</a></li>
                <li><a href="#profile" data-toggle="tab">办理情况</a></li>
            </ul>

            <div class="tab-content">
                <div class="tab-pane active" id="home">
                            <div class="text-left">
            <button class="btn" onclick="printDiv()">打印表单</button>
            </div>
                    <div class="windowOpenAuto" style="height:80%;overflow:hidden;">
	                <%
	        			if("200503".equals(ywlx)) {
	    			%>
	    			<iframe name="iFrame" width="100%" height="95%" src="/xmgl/jsp/framework/common/gwglLink/fwWs.jsp?sjbh=<%=sjbh %>" FRAMEBORDER=0 SCROLLING= ></iframe>
	    			<%
	        			} else {
	        		%>
	        		<iframe id="iFrame" name="iFrame" width="100%" height="100%" src="<%=tt%>" FRAMEBORDER=0 SCROLLING=0 ></iframe>
	        		<%
	        			}
	        		%>


					

                    </div>
                </div>
                <div class="tab-pane" id="attachedfile" style="height:80%;overflow:hidden;">

					<table class="B-table" width="100%">
                       <tr>

						<td width="100%" colspan="7" class="bottom-border right-border">
							<div>

								<table role="presentation" class="table table-striped">
									<tbody  class="files showFileTab"
										data-toggle="modal-gallery" data-target="#modal-gallery" onlyView="true">
									</tbody>
								</table>
							</div>
						</td>
					   </tr>
                       </table>
				   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
					
				
				</div>
                <div class="tab-pane" id="profile" style="height:80%;overflow:hidden;">

		        <div class="windowOpenAuto" >
     				  <iframe id="spFlowView" style="width:100%;height:100%;border:0px;scrolling:auto">
     				  </iframe>

				</div>
				</div>
				
            </div>

            <p></p>
           
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

</body>
</html>