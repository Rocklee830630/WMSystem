<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>


<%
   String tt= (String)request.getParameter("param");
   tt=tt.replace('|','&');
   String isEdit=(String)request.getParameter("isEdit");
   String isview=(String)request.getParameter("isview");
   String sjbh=(String)request.getParameter("sjbh");
   String ywlx=(String)request.getParameter("sjbh");

   String templateid="";
   templateid=(String)request.getParameter("temlateid");
   String spzt = request.getParameter("spzt");
   if(spzt == null || "".equals(spzt)) spzt = "9";
   
   String ywid = (String)request.getParameter("ywid");

 //  String ywid ="CE5B9114-E900-9B39-A81C-F390BE66A2B7";
%>
<head>

<title>打印处理</title>
<app:base/>
<meta http-equiv="Content-Type" content="text/html" charset="utf-8"> 
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script language="javascript" >
$(function() {
	queryFileData("2dff495a-2c62-4d9d-a490-261759c5f037");
});
var isedit='<%=isEdit%>';

function pageSetup(){

  factory.printing.PageSetup();

}


function closeT(){

  window.close();
}


function saveWS(){

	/*
    var all = iFrame.document.getElementsByTagName("*");
    var len = all.length;
    var cn, el;
//alert('here 2');
	var json = '{';
	
    for (var i = 0; i < len; i++)
    {
        el = all[i];
        var oTagName = el.tagName.toLowerCase();
        
        if (oTagName!="textarea") continue;
        json += '"' + (el.id).replace("TextArea","") + '":' + JSON.stringify(el.value) + ',';
        //alert((el.id).replace("TextArea",""));
        //json += '"' + name + '":' + JSON.stringify(tmpstr) + ',';
 		//alert(el.id+"="+el.value);
    }
    json += '"' + '"SJBH":'+<%=sjbh%>+',"YWLX":'+<%=ywlx%>+'';
    //json = json.substring(0, json.length - 1) + '}';

    var wsActionURL = "/xmgl/PubWS.do?getPreviewOldXMLAction";
	$.ajax({
		url : wsActionURL,
		data : json,
		dataType : 'json',
		async :	false,
		type : 'post',
		//contentType:'application/json;charset=UTF-8',	    
		success : function(result) {

			if(result)
		       flag = result.flag;
			if("0"==flag)
			{
				alert("文书保存出错！");
				return ;	
			}

		},
	    error : function(result) {
	     	//alert(234);
	    }
	});*/
	var ajbh=1;
	var sjbh=1;
	var ywlx='040401';
	var ajmc='ceshi';
	CQSP(ajbh,sjbh,ywlx,"",ajmc,"",null,null);

}


</script>
</head>
<body TOPMARGIN="0" LEFTMARGIN="0" style="overflow:visible" >
<app:dialogs/>
<div class="container-fluid">
    <p><!--占位用--></p>
    <div class="row-fluid" >
        <div class="row-fluid">
        	<div class="span7" style="height:550px;overflow:hidden;">
             	<iframe name="iFrame" width="100%" height="90%" src="<%=tt%>" FRAMEBORDER=0 SCROLLING= ></iframe>
             	<div class="text-center">
             	<%if(isview==null || !("1".equals(isview))){ %>
                <button class="btn" id="yl"  onclick="saveWS()">提交</button>
              	<%} %>
            	</div>
			</div>
			
    		<div class="span5" >
           	      <div class="B-small-from-table-auto">
                       <h4>附件信息</h4>
                       <form method="post" action="" id="insertForm">
                       <table class="B-table" width="100%">
                       <tr>
						<th width="12%" class="right-border bottom-border text-right">附件信息</th>
						<td width="88%" class="bottom-border right-border">
							<div>
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);"
									fjlb="3999"> <i class="icon-plus"></i> <span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="3999" class="files showFileTab"
										data-toggle="modal-gallery" data-target="#modal-gallery">
									</tbody>
								</table>
							</div>
						</td>
					   </tr>
                       </table>
                       </form>
                   </div>
								<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
              </div>
        </div>
    </div>
</div>

<script type="text/javascript">
function insertFileTab(result){
	var files = eval('(' + result + ')');
	if(g_nameMaxlength==undefined){
		g_nameMaxlength=12;
	}
	var tab_width = 0;
	$(".showFileTab").each(function(i){
		tab_width = $(this).parent().parent().width();
		$(this).parent().addClass("nomargin");
		var fjlb = $(this).attr("fjlb");
		for(var i=0;i<files.length;i++){
			var showHtml = "";
			var file = files[i];
			//alert(file.fileid);
			//对关键字段赋值
			$("#ywid").val(file.ywid);
			var inputArr = $(".myKeyValueDiv input");
			for(var xx=0;xx<inputArr.length;xx++){
				if(inputArr[xx].getAttribute("cond")=="true"){
					if(inputArr[xx].getAttribute("condName")=="ywid"){
						inputArr[xx].value=file.ywid;
					}
				}
			}
			var number = 0;
			//如果table没有附件类别，那么插入所有数据，否则只插入附件类别相匹配的数据
			if(fjlb==undefined || fjlb==file.fjlb){
				if(tab_width<600){
					showHtml +="<table class='fu_blockTable_full' cellspacing='0' cellpadding='0'>";
				}else{
					showHtml +="<table class='fu_blockTable_half' cellspacing='0' cellpadding='0'>";
				}
				showHtml +="<tr class='template-download'>";
				/**停用缩略图功能
				showHtml +="<td class='preview'>";
				//如果不是图片，那么不使用gallery功能
				if(file.fileType.indexOf("image")!=-1){
					showHtml +="<a href='"+file.url+"' title='"+file.name+"' rel='gallery' download='"+file.name+"'><img src='"+file.thumbnail_url+"'></a>";
				}else{
					showHtml +="<a href='"+file.url+"' title='"+file.name+"'  download='"+file.name+"'><img src='"+file.thumbnail_url+"'></a>";
				}
				showHtml +="</td>";
				*/
				showHtml += "<td class='otherColumns' style='border:1;width:30px'><abbr title='"+file.name+"'>"+"superman"+"</abbr></td>";
				showHtml += "<td class='otherColumns' style='border:1;width:50px'><abbr title='"+file.name+"'>"+"2013-09-05 09:34:23"+"</abbr></td>";
				showHtml +="<td class='name' style='border:1;'>";
				//名字长度过长，折行处理
				if(file.name.length>g_nameMaxlength){
					//showHtml +="<a href='"+file.url+"' title='"+file.name+"' rel='gallery' download='"+file.name+"'>"+file.name.substring(0,file.name.length/2)+"<br/>"+file.name.substring((file.name.length/2),file.name.length)+"</a>"
					//showHtml +="<a href='"+file.url+"' title='"+file.name+"' fileid='"+file.fileid+"' rel='gallery' download='"+file.name+"'>"+file.name.substring(0,g_nameMaxlength)+"...</a>"
					showHtml +="<span class='my-showPreview-btn nameSpan' fileid='"+file.fileid+"'><abbr title='"+file.name+"'>"+file.name.substring(0,g_nameMaxlength)+"...</abbr></span>";
				}else{
					showHtml +="<span class='my-showPreview-btn nameSpan' fileid='"+file.fileid+"'>"+file.name+"</span>";
				}
				showHtml +="</td>";
			//	showHtml +="<td class='size ' style='border:0;'>"+file.size+"</td>";
				//showHtml +="<td colspan='2'></td>";
				if($(this).attr("onlyView")=="true"){
					//如果是只读表格，那么不需要删除按钮
					showHtml +="<td class='myPreview' style='border:1;text-align:center;'>";
				}else{
					showHtml +="<td class='delete ' style='border:1;'>";
					showHtml +="<button class='btn btn-link my-del-btn' data-type='"+file.delete_type+"' data-url='"+file.delete_url+"' >";
					showHtml +="<i class='icon-trash'></i>";
					showHtml +="<span>删除</span>";
					showHtml +="</button>&nbsp;";
					//showHtml +="<input type='checkbox' name='delete' value='1'>"
					showHtml +="</td>";
					showHtml +="<td class='myPreview' style='border:1;'>";
				}
				showHtml +="<a href='"+file.url+"' class='btn btn-link' fileid='"+file.fileid+"' download='"+file.name+"'>";
				showHtml +="<i class='icon-zoom-in'></i>";
				showHtml +="<span>下载</span>";
				showHtml +="</a>&nbsp;";
				showHtml +="</td>";
				showHtml +="</tr>";
				showHtml +="</table>";
				//showHtml +="</table>";
				//showHtml +="</td>";
				//showHtml +="<td class='fu_cellTd'>";
				//showHtml +="</td>";
				//showHtml +="</tr>";
				var showHtmlHead = "";
				showHtmlHead +="<tr class='fu_cellTr'>";
				showHtmlHead +="<td class='fu_cellTd' style='border:0px;'>";
				showHtmlHead +="<div class='p_container' style='border:0px;'>";
				showHtmlHead += showHtml;
				showHtmlHead +="</div>";
				showHtmlHead +="</td>";
				showHtmlHead +="</tr>";
				var templateObj = $(this).find(".fu_cellTr").find(".fu_cellTd").find(".p_container");//;
				if(templateObj.attr("class")!=undefined){
					templateObj.append(showHtml);
				}else{
					$(this).append(showHtmlHead);
				}
			}
		}
	});
}
</script>
</body>
</html>