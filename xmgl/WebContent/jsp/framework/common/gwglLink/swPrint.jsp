<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
	<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>


<%
	User user = (User) session.getAttribute(Globals.USER_KEY);
	String deptId = user.getDepartment();
   String tt= (String)request.getParameter("param");
   tt=tt.replace('|','&');

   String isEdit=(String)request.getParameter("isEdit");
   String isview=(String)request.getParameter("isview");
   String sjbh=(String)request.getParameter("sjbh");
   String ywlx=(String)request.getParameter("ywlx");
   String isReStart = (String)request.getParameter("isReStart");
   isReStart = isReStart == null ? "" : isReStart;
	
   String operationoid = (String)request.getParameter("operationoid");
   String processtype = (String)request.getParameter("processtype");
   String templateid="";
   templateid=(String)request.getParameter("temlateid");
   String spzt = request.getParameter("spzt");
   if(spzt == null || "".equals(spzt)) spzt = "9";
   
   String ywid = (String)request.getParameter("ywid");

 //  String ywid ="CE5B9114-E900-9B39-A81C-F390BE66A2B7";
  String condition = (String)request.getParameter("condition");
  if(Pub.empty(condition)){
	  condition = "";
  }
  
  // 办公室部门负责人
  String sql = "select fzr from fs_org_dept where row_id='"+deptId+"'";
  String[][] bmfzrRs = DBUtil.query(sql);
  String bmfzr = bmfzrRs == null ? "" : bmfzrRs[0][0];
  String fzrXm = Pub.getUserNameByLoginId(bmfzr);
%>
<head>

<title>打印处理</title>
<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script language="javascript" >

var ajbh=1;
var sjbh='<%=sjbh%>';
var ywlx='<%=ywlx%>';
var isReStart = '<%=isReStart%>';

var isedit='<%=isEdit%>';
var condition = '<%=condition%>';
function closeT(){

  window.close();
}

$(function() {
	$("#wsTemplateDiv").height(getDivStyleHeight());
	setFileData("","","<%=sjbh%>","<%=ywlx%>");
	queryFileData("","","<%=sjbh%>","<%=ywlx%>");
	// 默认是马主任，可给他们自选功能
	$("#ACTOR").val("<%=fzrXm %>");
	$("#ACTOR").attr("code","<%=bmfzr %>");
	$("#blrBtn").click(function(){
		var actorCode = $("#ACTOR").attr("code");
		selectUserTree('single',actorCode,'setBlr') ;
	});
});

function comfirmSave() {
	saveWs();
}

function saveWs() {

	var dbr = $("#ACTOR").attr("code");
	var dbrName = $("#ACTOR").val();

	if(!dbr||dbr==""||dbr==undefined) {
		requireFormMsg("请选择办理人!!");
		return;
	}
	
    var form1 = iFrame.wsTemplate;
    var data1 = null;
    if(form1) {
    	var json = Form2Json.formToJSON(form1);

    	if(json.length <= 2){
    		data1= defaultJson.packSaveJson('{"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=templateid%>"}');
    	} else {
    	  	json = json.replace('}',',"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=templateid%>"}');

    	   	data1 = defaultJson.packSaveJson(json);
    	}
    } else {
    	data1= defaultJson.packSaveJson('{"SJBH":"<%=sjbh%>","YWLX":"<%=ywlx%>","WSTEMPLATEID":"<%=templateid%>"}');
    }
    
    var wsActionURL = "/xmgl/PubWS.do?saveWSJson&isReStart="+isReStart;
	xConfirm("提示信息","是否确认发起审批?办理人："+dbrName);
    $('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
	$('#ConfirmYesButton').one("click",function() {
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
				if("0"==flag) {
					alert("文书保存出错！");
					return ;	
				} else {
					var sjbh="<%=sjbh%>";
					var ywlx="<%=ywlx%>";
					
					CQSPTS(ajbh,sjbh,ywlx,"",'',"",condition,'<%=operationoid%>',dbr,dbrName,'<%=isReStart%>');
				}
	
			},
		    error : function(result) {
		     	//alert(234);
		    }
		});

	});

}



function setBlr(data){
	var userId = "";
	var userName = "";
	for(var i=0;i<data.length;i++){
 	 var tempObj =data[i];
 	 if(i<data.length-1){
	  userId +=tempObj.ACCOUNT+",";
	  userName +=tempObj.USERNAME+",";
 	 }else{
 	  userId +=tempObj.ACCOUNT;
 	  userName +=tempObj.USERNAME; 
 	 }
	}
	$("#ACTOR").val(userName);
	$("#ACTOR").attr("code",userId);

}
function printDiv()
{
	window.frames["iFrame"].printIframe();
	
	
}

</script>
</head>
<body TOPMARGIN="0" LEFTMARGIN="0" style="overflow:visible" >
<app:dialogs/>
<div class="container-fluid">

    <div class="row-fluid" >
		<div class="B-small-from-table-autoConcise">
        	<div class="span7" style="height:80%;overflow:hidden;" id="wsTemplateDiv">
             	<iframe name="iFrame" width="100%" height="95%" src="<%=tt%>" FRAMEBORDER=0 SCROLLING= ></iframe>
			</div>
			
    		<div class="span5" style="height:80%;overflow:hidden;">

		    		<h4 class="title" style="width:100%">

						<span class="pull-right">

		             	<%if(isview==null || !("1".equals(isview))){ %>
		                	<button class="btn" id="yl"  onclick="comfirmSave()">&nbsp;&nbsp;提交&nbsp;&nbsp;</button>
		              	<%} %>
		              		<button id="example_query" class="btn btn-link" type="button" onclick="printDiv()"><i class="icon-print"></i>打印</button>
						</span>
					</h4>
           	       
                       <form method="post" action="" id="insertForm">
                       <div class="windowOpenAuto" style="height:90%;overflow:auto;">
                       <table class="B-table" width="100%">
                       		<tr style="display:<%if(!("4".equals(processtype))) out.println("none");%>">
								<th class="right-border bottom-border" style="font-size:14px;">办理人</th>
								<td class="bottom-border right-border"  colspan="7">
			         			<input class="span12"  type="text" check-type="required" fieldname="ACTOR" id="ACTOR" name ="ACTOR" style="width:75%" disabled="disabled" />
			         			<button class="btn btn-link"  type="button" id="blrBtn"><i class="icon-edit" title="点击选择办理人"></i></button>
			       	 			</td>
							</tr>
                       <tr>
						<th width="12%" class="right-border bottom-border text-right">附件信息</th>
						<td width="88%" colspan="7" class="bottom-border right-border">
							<div>
								<input type="hidden" name=ywid id="ywid" value="111"> 
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);"	fjlb="3999"> <i class="icon-plus"></i> <span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody  class="files showFileTab"
										data-toggle="modal-gallery" data-target="#modal-gallery" showLrr="true" deleteUser="<%=user.getAccount()%>" showSize="false" onlyView="true">
									</tbody>
								</table>
							</div>
						</td>
					   </tr>
                       </table>
                        </div>
                       </form>
                  
                   
				   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
              </div>
        </div>
    </div>
</div>

</body>

</html>