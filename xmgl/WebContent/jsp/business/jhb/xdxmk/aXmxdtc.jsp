<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>按项目下达统筹</title>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var id ="",index=-1,trlen;
//页面初始化

$(function() {
	init();
	//按钮绑定事件(查询)
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,xdxmkList);
	});
	//按钮绑定事件(下达统筹计划)
	$("#btnXdtcjh").click(function() {
		var ids = "";
		if($("#xdtcjhxmList").getTableRows()==0){
			xAlert("提示信息",'没有可下达的项目！');
			return;
		}
		if(!$("#jhForm").validationButton()){
			requireFormMsg();
			return;
		}
		$("#xdtcjhxmList tbody tr").each(function(){
			//存行数据
			var value = convertJson.string2json1($(this).attr("rowJson")).GC_TCJH_XMXDK_ID;
			ids+=value+",";
		});
		
		xConfirm("提示信息","确认将该批项目进行下达吗？");
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){  
			var ywid = $("#ywid").val();
			var formData = Form2Json.formToJSON(jhForm);
			var formData1 = defaultJson.packSaveJson(formData);
			var success = doInsertJh(controllername + "?aPcxd&ids="+ids+"&ywid="+ywid, formData1);
			if(success){
				xAlert("提示信息",'操作成功！');
				$("#xdtcjhxmList").clearResult();
				clearFileTab();
				$("#ywid").val("");
				var num = document.getElementsByTagName("font");
				num[0].innerHTML = 0;
			}else{
				xAlert("提示信息",'操作失败！');
			}
 			
			//getSelectedTDs("xdxmkList");
		});  
		
	});
	//按钮绑定事件(左移)
	$("#btnLeft").click(function() {
		var rowindex = $("#xdtcjhxmList").getSelectedRowIndex();//获得选中行的索引
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		var rowValue = $("#xdtcjhxmList").getSelectedRow();//获得选中行的json对象
		$("#xdxmkList").insertResult(rowValue,xdxmkList,1);
		$("#xdtcjhxmList").removeResult(rowindex);
		trlen = $("#xdtcjhxmList tr").length-1;
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = trlen;
	});
	//按钮绑定事件(右移)
	$("#btnRight").click(function() {
		var rowindex = $("#xdxmkList").getSelectedRowIndex();//获得选中行的索引
		
		if(rowindex==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		var rowValue = $("#xdxmkList").getSelectedRow();//获得选中行的json对象
		$("#xdtcjhxmList").insertResult(rowValue,xdtcjhxmList,1);
		$("#xdxmkList").removeResult(rowindex);
		trlen = $("#xdtcjhxmList tr").length-1;
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = trlen;
	});
	//按钮绑定事件(全选)
	$("#btnAll").click(function() {
		$("#xdxmkList tbody tr").each(function(){

			var rowValue = convertJson.string2json1($(this).attr("rowJson"));//$("#DT1").getSelectedRowIndex();//获得选中行的索引
			$("#xdtcjhxmList").insertResult(JSON.stringify(rowValue),xdtcjhxmList,1);
		});
		$("#xdxmkList").clearResult();
		trlen = $("#xdtcjhxmList tr").length-1;
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = trlen;
	});
	//按钮绑定事件(取消)
	$("#btnCannel").click(function() {
		$("#xdtcjhxmList tbody tr").each(function(){
			var rowValue = convertJson.string2json1($(this).attr("rowJson"));//$("#DT1").getSelectedRowIndex();//获得选中行的索引
			$("#xdxmkList").insertResult(JSON.stringify(rowValue),xdxmkList,1);
		});
		$("#xdtcjhxmList").clearResult();
		trlen = $("#xdtcjhxmList tr").length-1;
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = trlen;
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
});
//页面默认参数
function init(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,xdxmkList);
}	
//按项目下发(业务操作包括，更新下达库数据，新增计划主体数据，新增统筹计划数据)
doInsertJh = function(actionName, data1) {
    var success  = true;
	$.ajax({
		type : 'post',
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	false,
		success : function(result) {
			$("#resultXML").val(result.msg);
			$("#jhForm").clearFormResult();
			success = true;
		},
	    error : function(result) {
		     	alert(result.msg);
			    defaultJson.clearTxtXML();
			    success = false;
		}
	});
	 return success;
};
//详细信息
function rowView(index){
	var obj = $("#xdxmkList").getSelectedRowJsonByIndex(index);
	var id = eval("("+obj+")").GC_TCJH_XMXDK_ID;
	$("body").manhuaDialog({"title":"项目下达库>项目信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/rowView.jsp?id="+id,"modal":"1"});
}
//默认年度
function getNd(){
	//项目年份默认当前年
	var d = new Date();
	var year = d.getFullYear();
	var qnd = $("#qnd");
	var t = 0;
	qnd.find("option").each(function() {
		if(this.value == year){
			return false;
		}
		t++;
	});
	if(t){
		qnd.get(0).selectedIndex=t;  
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <div class="row-fluid" style="display:none">
    <div class="B-small-from-table-box">
      <h4>信息查询
      	<span class="pull-right">
      		<button id="btnQuery" class="btn btn-inverse"  type="button">查询</button>
      		<button id="btnClear" class="btn btn-inverse" type="button">清空</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
          <tr>
          <th width="5%" class="right-border bottom-border text-right">项目年份</th>
          <td class="right-border bottom-border" width="10%">
          	<select class="span12" id="qnd" name = "QND" check-type="required" fieldname = "ND" operation="=" kind="dic" src="T#GC_CBK_ND:ND:ND:"">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目类型</th>
          <td class="right-border bottom-border" width="10%">
            <select class="span12" name = "QXMLX" fieldname = "XMLX" operation="=" kind="dic" src="XMLX">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
          <td class="right-border bottom-border" width="30%">
           <input class="span12" type="text" placeholder="" name = "QXMMC" fieldname = "XMMC" operation="like" >
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目属性</th>
          <td class="right-border bottom-border" width="10%">
          	<select class="span12" name = "QXMSX" fieldname = "XMSX" operation="=" kind="dic" src="XMSX">
            </select>
          </td>
          <th></th>
          <td></td>
          
         </tr>
      </table>
      </form>
    </div>
  </div>
 <div class="row-fluid">
	<div class="span5">
		<div class="B-small-from-table-autoConcise">
		  <h4 class="title">下达项目库 </h4>
			<table class="table-hover table-activeTd B-table" id="xdxmkList" width="100%" type="single" noPage="true" pageNum="1000">
				<thead>
					<tr>
						<th name="XH" id="_XH" style="width:10px" tdalign="center">&nbsp;#&nbsp;</th>
						<th fieldname="XMBH" tdalign="center">&nbsp;项目编号&nbsp;</th>
						<th fieldname="ISNATC" style="width:60px" tdalign="center" >&nbsp;已下达&nbsp;</th>
						<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>
	<div class="span2">
		<div align="center">
			<br></br><br></br><br></br>
			<button id="btnRight" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-chevron-right"></i>&nbsp;右 移</button>
			<br></br>
			<button id="btnLeft" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-chevron-left"></i>&nbsp;左 移</button>
			<br></br>
			<button id="btnAll" class="btn"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-forward"></i>&nbsp;全 选</button>
			<br></br>
			<button id="btnCannel" class="btn"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-backward"></i>&nbsp;&nbsp;取 消</button>
			
		</div>
	</div>
	<div class="span5">
		<div class="B-small-from-table-autoConcise">
		  <h4 class="title">待下达统筹计划列表&nbsp;&nbsp; 共<font  style=" margin-left:5px; margin-right:5px;;font-size:28px;color:red;">0</font>个</h4>
			<table class="table-hover table-activeTd B-table" id="xdtcjhxmList" width="100%" type="single" pageNum="1000">
				<thead>
					<tr>
						<th name="XH" id="_XH" style="width:10px" tdalign="center">&nbsp;#&nbsp;</th>
						<th fieldname="XMBH" tdalign="center">&nbsp;项目编号&nbsp;</th>
						<th fieldname="ISNATC" style="width:60px" tdalign="center" >&nbsp;已下达&nbsp;</th>
						<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
		<br></br>
		<div class="B-small-from-table-autoConcise">
	   <h4 class="title">统筹计划
	 	<span class="pull-right">
	 		<button id="btnXdtcjh" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">下达统筹计划</button>
	 	</span>
	 	</h4>
	    <form method="post" id="jhForm"  >
	     <table class="B-table">
		     <tr>
		     	<th width="8%" class="right-border bottom-border text-right">统筹名称</th>
		         <td class="bottom-border right-border" colspan="3">
		         	<input class="span12" type="text" placeholder="必填" check-type="required" fieldname="JHMC" name = "JHMC" maxlength="100">
		         </td>
		     </tr>
		     <tr>
		         <th width="8%" class="right-border bottom-border text-right">统筹批次</th>
		         <td class="right-border bottom-border">
		         	<select class="span12" name = "JHPCH" check-type="required" fieldname = "JHPCH" operation="=" kind="dic" src="PCH">
		           </select>
		         </td>
		         <th width="8%" class="right-border bottom-border text-right">下达日期</th>
		         <td class="right-border bottom-border">
		         	<input class="span12" type="date" placeholder="" check-type="required" name = "LRSJ" fieldname="LRSJ">
		         </td>
		     </tr>
		     <tr>
        	<th width="8%" class="right-border bottom-border text-right">附件信息</th>
        	<td colspan="3" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0002">
						<i class="icon-plus"></i>
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0002" class="files showFileTab"
							data-toggle="modal-gallery" data-target="#modal-gallery">
						</tbody>
					</table>
				</div>
			</td>
        </tr>
	     </table>
	     </form>
	   </div>
	</div>
 	</div>
 

</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMBH,LRSJ"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="ywid" id = "ywid" value="">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 </FORM>
 </div>
</body>
</html>