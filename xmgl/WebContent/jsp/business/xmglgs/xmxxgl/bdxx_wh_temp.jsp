<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<%
			String id = request.getParameter("id");
		%>
		<title>项目信息管理-标段信息维护</title>
		<script type="text/javascript" charset="utf-8">
var controllername = "${pageContext.request.contextPath }/xmxxController.do";
var id = '<%=id%>';
//页面初始化
$(function() {
	init();
  	//按钮绑定事件（保存）
    $("#btnSave").click(function() {
    	
    	if($("#bdxxForm").validationButton())
		{
    		var data = Form2Json.formToJSON(bdxxForm);
    		var data1 = addjson(convertJson.string2json1(data),"FLAG","BDXX");
		   	$(window).manhuaDialog.setData(data1);
		   	$(window).manhuaDialog.sendData();
		   	$(window).manhuaDialog.close();
		}else{
			requireFormMsg();
		  	return;
		}
    	
    });
	
});
//页面默认参数
function init(){
	$("#bdxxForm input[isReadOnly='0']").attr("disabled","true");
	$("#bdxxForm input[isReadOnly='1']").removeAttr("disabled");
	$("#bdxxForm th[isDisabledTh='1']").removeClass("disabledTh");
	submit(controllername+"?queryBdById&id="+id,null,bdxxList);
}
function submit(actionName, data,tablistID){
	$.ajax({
		type : 'post',
		url : actionName,
		data : data,
		cache : false,
		dataType : "json",  
		async :	false,
		success : function(result) {
			var rowObj = convertJson.string2json1(result.msg).response.data[0];
			//alert(JSON.stringify(substr)); 
			$("#bdxxForm").setFormValues(rowObj);
			//$("#resultXML").val(JSON.stringify(rowObj));
		}
	});
}
</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						标段信息
						<span class="pull-right">
							<button id="btnSave" class="btn" type="button">
								保存
							</button> </span>
					</h4>
					<form method="post" id="bdxxForm">
						<table class="B-table" width="100%" id="bdxxList">
							<tr style="display: none;">
								<td>

									<input type="hidden" id="GC_XMBD_ID" fieldname="GC_XMBD_ID"
										name="GC_XMBD_ID" />
								</td>
							</tr>
							<tr>
								<th width="5%" class="right-border bottom-border disabledTh" isDisabledTh="0">
									标段编号
								</th>
								<td class="right-border bottom-border" width="10%">
									<input class="span12" type="text" fieldname="BDBH" id="BDBH"
										name="BDBH" isReadOnly="0">
								</td>
								<th width="5%" class="right-border bottom-border disabledTh" isDisabledTh="0">
									标段名称
								</th>
								<td class="right-border bottom-border" colspan="3">
									<input class="span12" type="text" fieldname="BDMC" id="BDMC"
										name="BDMC" isReadOnly="0">
								</td>
							</tr>
						</table>
						<h5 id="sjdw" class="black">
							设计单位
						</h5>
						<table id="T_sjdw" class="B-table" width="100%">
							<tr>
								<th width="10%" class="right-border bottom-border disabledTh" isDisabledTh="0">
									单位名称
								</th>
								<td class="right-border bottom-border" colspan="3">
									<select class="span12 department" id="SJDW" name="SJDW"
										style="width: 95%" fieldname="SJDW" kind="dic"
										src="T#GC_CJDW:GC_CJDW_ID:DWMC:1=1"
										disabled="disabled">
									</select>
									<a href="javascript:void(0)" title="点击选择单位"><i
										id="sjdwSelect" selObj="SJDW" class="icon-edit"
										onclick="selectCjdw('sjdwSelect');" isLxSelect="1" dwlx="1"></i>
									</a>
								</td>
							</tr>
							<tr>
								<th width="10%" class="right-border bottom-border">
									设计负责人
								</th>
								<td class="bottom-border" width="15%">
									<input class="span12" type="text" name="SJDWFZR"
										maxlength="100" fieldname="SJDWFZR" />
								</td>
								<th width="10%" class="right-border bottom-border">
									联系电话
								</th>
								<td class="bottom-border">
									<input class="span12" type="text" name="SJDWFZRLXFS"
										maxlength="100" fieldname="SJDWFZRLXFS" style="width: 40%" />
								</td>
							</tr>
						</table>
						<h5 id="sgdw" class="black">
							施工单位
						</h5>
						<table id="T_sgdw" class="B-table" width="100%">
							<tr>
								<th width="10%" class="right-border bottom-border disabledTh" isDisabledTh="0">
									单位名称
								</th>
								<td class="right-border bottom-border" colspan="7">
									<select class="span12 department" id="SGDW" name="SGDW"
										style="width: 95%" fieldname="SGDW" kind="dic"
										src="T#GC_CJDW:GC_CJDW_ID:DWMC:1=1"
										disabled="disabled">
									</select>
									<a href="javascript:void(0)" title="点击选择单位"><i
										id="sgdwSelect" selObj="SGDW" class="icon-edit"
										onclick="selectCjdw('sgdwSelect');" isLxSelect="1" dwlx="3"></i>
									</a>
								</td>
							</tr>
							<tr>
								<th width="10%" class="right-border bottom-border">
									项目经理
								</th>
								<td class="bottom-border">
									<input class="span12" type="text" name="SGDWXMJL"
										maxlength="100" fieldname="SGDWXMJL" />
								</td>
								<th width="10%" class="right-border bottom-border">
									联系电话
								</th>
								<td class="bottom-border">
									<input class="span12" type="text" name="SGDWXMJLLXDH"
										maxlength="100" fieldname="SGDWXMJLLXDH" />
								</td>
								<th width="10%" class="right-border bottom-border">
									技术负责人
								</th>
								<td class="bottom-border">
									<input class="span12" type="text" name="SGDWJSFZR"
										maxlength="100" fieldname="SGDWJSFZR" />
								</td>
								<th width="10%" class="right-border bottom-border">
									联系电话
								</th>
								<td class="bottom-border">
									<input class="span12" type="text" name="SGDWJSFZRLXDH"
										maxlength="100" fieldname="SGDWJSFZRLXDH" />
								</td>
							</tr>
						</table>
						<h5 id="jldw" class="black">
							监理单位
						</h5>
						<table id="T_jldw" class="B-table" width="100%">
							<tr>
								<th width="10%" class="right-border bottom-border disabledTh" isDisabledTh="0">
									单位名称
								</th>
								<td class="right-border bottom-border" colspan="11">
									<select class="span12 department" id="JLDW" name="JLDW"
										style="width: 95%" fieldname="JLDW" kind="dic"
										src="T#GC_CJDW:GC_CJDW_ID:DWMC:1=1"
										disabled="disabled">
									</select>
									<a href="javascript:void(0)" title="点击选择单位"><i
										id="jldwSelect" selObj="JLDW" class="icon-edit"
										onclick="selectCjdw('jldwSelect');" isLxSelect="1" dwlx="2"></i>
									</a>
								</td>
							</tr>
							<tr>
								<th width="10%" class="right-border bottom-border">
									总监
								</th>
								<td class="bottom-border">
									<input class="span12" type="text" name="JLDWZJ" maxlength="100"
										fieldname="JLDWZJ" />
								</td>
								<th width="5%" class="right-border bottom-border">
									联系电话
								</th>
								<td class="bottom-border">
									<input class="span12" type="text" name="JLDWZJLXDH"
										maxlength="100" fieldname="JLDWZJLXDH" />
								</td>
								<th width="5%" class="right-border bottom-border">
									总监代表
								</th>
								<td class="bottom-border">
									<input class="span12" type="text" name="JLDWZJDB"
										maxlength="100" fieldname="JLDWZJDB" />
								</td>
								<th width="5%" class="right-border bottom-border">
									联系电话
								</th>
								<td class="bottom-border">
									<input class="span12" type="text" name="JLDWZJDBLXDH"
										maxlength="100" fieldname="JLDWZJDBLXDH" />
								</td>
								<th width="5%" class="right-border bottom-border">
									安全监理
								</th>
								<td class="bottom-border">
									<input class="span12" type="text" name="JLDWAQJL"
										maxlength="100" fieldname="JLDWAQJL" />
								</td>
								<th width="5%" class="right-border bottom-border">
									联系电话
								</th>
								<td class="bottom-border">
									<input class="span12" type="text" name="JLDWAQJLLXDH"
										maxlength="100" fieldname="JLDWAQJLLXDH" />
								</td>
							</tr>
						</table>
					</form>

				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="ywid" id="ywid" value="">
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="DESC" fieldname="bdbh"
					id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>