<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<app:dialogs></app:dialogs>
<title>标段划分管理</title>
<script type="text/javascript" charset="utf-8">
 
var controllername= "${pageContext.request.contextPath }/jjg/jjgwhController.do";
var oTable1,id;


//初始化查询
$(document).ready(function(){
	//generateJhNdMc($("#ND"),null);
	initCommonQueyPage();
	g_bAlertWhenNoResult=false;
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?query_jjg",data,DT1);
	g_bAlertWhenNoResult=true;

});


//查询
$(function() {
	var btn=$("#example_query");
	btn.click(function() {
		//生成json串
		var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query_jjg",data,DT1);
	});
	//清空查询表单
    var btn_clearQuery=$("#query_clear");
    btn_clearQuery.click(function() {
        $("#queryForm").clearFormResult();
       // generateJhNdMc($("#ND"),null);
       initCommonQueyPage();
        document.getElementById('num').value='1000';
        //其他处理放在下面
    });
});


//复制新增
$(function() {	
	var btn=$("#example_copyadd");
	btn.click(function() {
		id=null;
        clearFileTab();
        $("#ywid").val("");
	});
});


//新增
$(function() {
    var btn_clearQuery=$("#query_add");
    btn_clearQuery.click(function() {
        $("#demoForm").clearFormResult();
      id=null;
        //其他处理放在下面
    });
});


//保存
$(function() {	
	var btn=$("#example_save");
	btn.click(function() {
	 	 if($("#demoForm").validationButton())
		{
		}else{
	 		requireFormMsg();
			return ;
		}   
		//生成json串
		var data=Form2Json.formToJSON(demoForm);
		//组成保存json串格式
		var data1=defaultJson.packSaveJson(data);
		//通过判断id是否为空来判断是插入还是修改
		if(id==null||id=='undefined')
		{
			var success=doInsertJson_jjg(controllername + "?insert_jjg&ywid="+$("#ywid").val(), data1,DT1);
			var obj=$("#queryResult").val();	
			var resultmsgobj=convertJson.string2json1(obj);
			var subresultmsgobj=resultmsgobj.response.data[0];
			$("#demoForm").setFormValues(subresultmsgobj);
			id=$(subresultmsgobj).attr("GC_SJGL_JJG_ID");
			document.getElementById('GC_SJGL_JJG_ID').value=id;
			document.getElementById('YWLX').value=$(subresultmsgobj).attr("YWLX");
			var subresultmsgobj1=defaultJson.dealResultJson(obj);
			$("#DT1").insertResult(JSON.stringify(subresultmsgobj1),DT1,1);
			$("#DT1").cancleSelected();
			$("#DT1").setSelect(0);
			var index1 =$("#DT1").getSelectedRowIndex();
			json=$("#DT1").getSelectedRowJsonByIndex(index1);
			var parentmain=parent.$("body").manhuaDialog.getParentObj();
			parentmain.query();
		}
		else
		{
			var success=doInsertJson_jjg(controllername + "?update_jjg", data1,DT1);
			var obj=$("#queryResult").val();
			var index= $("#DT1").getSelectedRowIndex();
			var subresultmsgobj1=defaultJson.dealResultJson(obj);
			$("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
			$("#DT1").cancleSelected();
			$("#DT1").setSelect(index);
			var parentmain=parent.$("body").manhuaDialog.getParentObj();
			parentmain.query();
		}				
  	});
});


//具体插入方法
doInsertJson_jjg=function(actionName, data1,tablistID) {
    var success =true;
	$.ajax({
		url : actionName,
		data : data1,
		dataType : 'json',
		async :	false,
		type : 'post',   
		success : function(result) {
		$("#queryResult").val(result.msg);
		var prompt=result.prompt;
 			if(!prompt){
 				prompt =g_prompt[0];
 			}
			xAlert("运行结果",prompt);
			defaultJson.clearTxtXML();
			success=true;
		},
	    error : function(result) {
	     	alert(result.msg);
		    defaultJson.clearTxtXML();
		    success=false;
		}
	});
	return success;
};


//点击获取行对象
function tr_click(obj,tabListid){
 	id=obj.GC_SJGL_JJG_ID;
 	//alert(id);
 	/*	document.getElementById('BDBH').value=obj.JBDBH;
	document.getElementById('XMBH').value=obj.XXMBH;
	 */
	//alert(JSON.stringify(obj));
	 alert(id);
	 setFileData(id,"","","");
	 queryFileData(id,"","","");
	$("#demoForm").setFormValues(obj);
}

$(function() {
	//清空表单
	var btn=$("#example_clear");
	btn.click(function()
	{
		$("#demoForm").clearFormResult(); 
		id=null;
	    clearFileTab();
	    $("#ywid").val("");
	});
	//弹出项目标段列表
	var xmBtn=$("#xmBtn");
	xmBtn.click(function()
	 {
		queryProjectWithBD();
		//$("body").manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"2"}); 
	 });

});
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<form method="post" id="queryForm">
				<table class="B-table">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
					  <jsp:include page="/jsp/business/common/commonQuery.jsp" flush="true">
				         	<jsp:param name="prefix" value="sj"/> 
				         </jsp:include>							
			            <td class="text-left bottom-border text-right">
							<button	id="example_query" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	                        	<button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
			            </td>																											
						<th></th>
					</tr>
				</table>
			</form>
			<div style="height:5px;"> </div>
			<div class="overFlowX"> 
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" pageNum="5">
	                <thead>
	                    <tr>
	                     	<th id="_XH" name="XH" tdalign="center">&nbsp;#&nbsp;</th>	
							<th fieldname="XMBH" rowspan="2" colindex=2 tdalign="center">&nbsp;项目编号&nbsp;</th> 
							<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
							<th fieldname="BDMC" maxlength="15">&nbsp;标段名称&nbsp;</th>
							<th fieldname="WGRQ" tdalign="center">&nbsp;完工时间&nbsp;</th>
							<th fieldname="XMGLGS">&nbsp;项目管理公司&nbsp;</th>
							<th fieldname="YZDB">&nbsp;业主代表&nbsp;</th>
				 			<th fieldname="SJDW">&nbsp;设计单位&nbsp;</th>
							<th fieldname="FZR_SJDW">&nbsp;设计人&nbsp;</th>
							<th fieldname="JLDW">&nbsp;监理单位&nbsp;</th>
							<th fieldname="FZR_JLDW">&nbsp;总监&nbsp;</th>
							<th fieldname="SGDW">&nbsp;施工单位&nbsp;</th>
							<th fieldname="FZR_SGDW">&nbsp;项目经理&nbsp;</th>
							<th fieldname="JSDW">&nbsp;接受单位&nbsp;</th>
							<th fieldname="JSR">&nbsp;接收人&nbsp;</th>
							<th fieldname="JGYSRQ" tdalign="center">&nbsp;交工竣工时间&nbsp;</th>
							<th fieldname="JGYSSJ" tdalign="center">&nbsp;竣工验收时间&nbsp;</th>
						</tr>
	                </thead> 
	              	<tbody></tbody>
	           </table>
	       </div>     
	      	<h4 class="title">
	      		交竣工信息
	      		<span class="pull-right">
	          		<button id="example_save" name="save" class="btn" type="button">保存</button>
	          		<button id="example_copyadd" class="btn" type="button">复制新增</button>
	          		<button id="query_add" name="new" class="btn" type="button">继续新增</button>
	          	</span>
			</h4>
    			<form method="post" id="demoForm">
     				<table class="B-table">
     				<TR  style="display:none;">
     				    <td>
	     					<input type="text" id="BDID" name="BDID" fieldname="BDID"/>
	     					<input type="text" id="XMID" name="XMID" fieldname="XMID"/>
	     					<input type="text" id="JHSJID" name="JHSJID" fieldname="JHSJID"/>
	     					<input type="text" id="GC_SJGL_JJG_ID" name="GC_SJGL_JJG_ID" fieldname="GC_SJGL_JJG_ID"/>
						    <input type="text" id="YWLX" fieldname="YWLX" name="YWLX"/>
						    <input type="text" id="ND" fieldname="ND" name="ND"/>
						    <input type="text" id="ywid"/>
					    </td>
					 </TR>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">项目名称</th>
						<td width="25%" class="right-border bottom-border">
							<input class="span12" type="text" placeholder="必填"  data-toggle="modal"   check-type="required" readOnly="true" id="XMMC" name="XMMC" fieldname="XMMC"/>
						    
						</td>
						<th width="8%" class="right-border bottom-border text-right">标段名称</th>
						<td width="25%" class="right-border bottom-border">
							<input class="span12" type="text" style="width:80%" id="BDMC" name="BDMC" fieldname="BDMC" disabled/>
							 <button class="btn btn-link"  title="请选择项目" type="button" id="xmBtn"><i class="icon-edit"></i></button>
						</td>
						<th width="8%" class="right-border bottom-border text-right">项目地址</th>
						<td width="25%" class="right-border bottom-border">
							<input id="XMBH" class="span12" type="text" name="XMDZ" id="XMDZ" check-type="maxlength" disabled maxlength="500" fieldname="XMDZ"/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">完工日期</th>
						<td width="25%" class="right-border bottom-border">
							<input class="span12" name ="WGRQ" id="WGRQ" fieldname="WGRQ" type="date" disabled/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">项目管理公司</th>
						<td width="25%" class="right-border bottom-border">
							<input id="XMMC" class="span12" type="text" name="XMGLGS" id="XMGLGS" check-type="maxlength" disabled maxlength="100" fieldname="XMGLGS"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">业主代表</th>
						<td width="25%" class="right-border bottom-border">
							<input id="YZDB" class="span12" type="text" name="YZDB" id="YZDB" check-type="maxlength" disabled maxlength="100" fieldname="YZDB"/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">设计单位</th>
						<td width="25%" class="right-border bottom-border">
							<input id="SJDW" class="span12" type="text" check-type="maxlength" maxlength="100" disabled fieldname="SJDW" name="SJDW" id="SJDW"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">设计人</th>
						<td width="25%" class="right-border bottom-border">
							<input id="SJR" class="span12" type="text" check-type="maxlength" maxlength="100" disabled fieldname="FZR_SJDW" name="FZR_SJDW" id="FZR_SJDW"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">监理单位</th>
						<td width="25%" class="right-border bottom-border">
							<input id="JLDW" class="span12" type="text" name="JLDW" id="JLDW" check-type="maxlength" disabled maxlength="100" fieldname="JLDW"/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">总监</th>
						<td width="25%" class="right-border bottom-border">
							<input id="FZR_JLDW" class="span12" type="text" name="FZR_JLDW" id="FZR_JLDW" disabled check-type="maxlength" maxlength="100" fieldname="FZR_JLDW"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">施工单位</th>
						<td width="25%" class="right-border bottom-border">
							<input id="SGDW" class="span12" type="text" name="SGDW" id="SGDW" check-type="maxlength" disabled maxlength="100" fieldname="SGDW"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">项目经理</th>
						<td width="25%" class="right-border bottom-border">
							<input id="FZR_SGDW" class="span12" type="text" name="FZR_SGDW" id="FZR_SGDW" check-type="maxlength" disabled maxlength="100" fieldname="FZR_SGDW"/>
						</td>
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">接收单位</th>
						<td width="25%" class="right-border bottom-border">
							<input id="JSDW" class="span12" type="text" name="JSDW" id="JSDW" check-type="maxlength" check-type="maxlength" maxlength="100" fieldname="JSDW"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">接收人</th>
						<td width="25%" class="right-border bottom-border">
							<input id="JSR" class="span12" type="text" name="JSR" id="JSR" check-type="maxlength" maxlength="100" fieldname="JSR"/>
						</td>
						<th width="8%" class="right-border bottom-border text-right">竣工验收日期</th>
						<td width="25%" class="right-border bottom-border">
							<input class="span12" name ="JGYSSJ" id="JGYSSJ" fieldname="JGYSSJ" type="date"/>
						</td>
					</tr>
					<tr>														
						<th width="8%" class="right-border bottom-border text-right">交工验收日期</th>
						<td width="25%" class="right-border bottom-border">
							<input class="span12" name ="JGYSRQ" id="JGYSRQ" fieldname="JGYSRQ" type="date"/>
						</td>	
						<th></th><th></th><th></th>					
					</tr>
					<tr>
						<th width="8%" class="right-border bottom-border text-right">工程规模</th>
						<td width="92%" colspan="5" class="bottom-border">
							<textarea class="span12" rows="2" id="JSNR" check-type="maxlength" maxlength="500" fieldname="JSNR" disabled name="JSNR"></textarea>
						</td>
					</tr>
					<tr>									
						<th width="8%" class="right-border bottom-border text-right">工程未竣工验收原因：</th>
						<td width="92%" colspan="5" class="bottom-border">
							<textarea class="span12" rows="2" id="WJGYSY" check-type="maxlength" maxlength="2000" fieldname="WJGYSY" name="WJGYSY"></textarea>
						</td>
					</tr>
					<tr>															
						<th width="8%" class="right-border bottom-border text-right">工程未交工验收原因：</th>
						<td width="92%" colspan="5" class="bottom-border">
							<textarea class="span12" rows="2" id="WJGYS" check-type="maxlength" maxlength="2000" fieldname="WJGYS" name="WJGYS"></textarea>
						</td>
					</tr>
			       <tr>
			        	<th width="8%" class="right-border bottom-border text-right text-right">交工相关附件</th>
			        	<td colspan="7" class="bottom-border right-border">
							<div>
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0028">
									<span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="0028" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
								</table>
							</div>
						</td>
			        </tr>				
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right">竣工相关附件</th>
			        	<td colspan="7" class="bottom-border right-border">
							<div>
								<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0029">
									<span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody fjlb="0029" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
								</table>
							</div>
						</td>
			        </tr>										
   	 			</table>
     			</form>
    		</div>
 		</div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
<table  id="DT2"></table>   				
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" fieldname="BDBH,LRSJ"/>
		<input type="hidden" name="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
</div>
<script type="text/javascript">
getWinData=function(data){
	//alert(data);
	//alert('aa');
	//var obj=econvertJson.string2json1(data);//字符串转JSON对象
	//$("#demoForm").setFormValues(data);
	var obj=eval("("+data+")");//字符串转JSON对象
	$("#demoForm").setFormValues(obj);
    $("#XMID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
	$("#BDID").val(JSON.parse(data).BDID);
	$("#ND").val(JSON.parse(data).ND);
	$("#JHSJID").val(JSON.parse(data).GC_JH_SJ_ID); 
	//setValueByArr(data,["BDID","GC_TCJH_XMXDK_ID","GC_JH_SJ_ID","ND"]);
};
</script>
</body>
</html>