<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/ZhaotoubiaoController.do";
	var i=0;					//页面表单隐藏或显示按钮
	 var xmrowValues;
	$(function() {
		//选择需求
	 	$("#example1").click(function(){
			$(window).manhuaDialog({"title":"招投标管理>选择需求","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/xqmore.jsp","modal":"1"});
		});
	 	$("#bc_btn").click(function(){
	 	 	saveZhaoTouBiao();
	 	});
	});
	function saveZhaoTouBiao(){
		if(""==($("#ZTBXQID").val())){
			xInfoMsg('请选择招标需求！');
			return ;
		}
		if($("#demoForm2").validationButton()){
			//生成json串
			var data = Form2Json.formToJSON(demoForm2);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//调用ajax插入
			defaultJson.doInsertJson(controllername + "?insertZhaotoubiao&ZTBXQID="+$("#ZTBXQID").val(), data1,null,"addHuiDiao");
		}
	}
	function addHuiDiao()
	{
		var data3 = $("#frmPost").find("#resultXML").val();
		var fuyemian1=$(window).manhuaDialog.getParentObj();
		fuyemian1.tianjiahang(data3);
		$(window).manhuaDialog.close();
	}
	 //子页面调用
	function fanhuixiangm(rowValues,ids){
		//清空列表
		$("#DT2").find("tbody").children().remove();
		// alert(rowValues.length);
		xmrowValues=rowValues;
		for(var i=0;i<rowValues.length;i++){
			$("#DT2").insertResult(rowValues[i],DT2,1);
		}
		$("#ZTBXQID").val(ids);
	}
	function getArr(){
		return xmrowValues;
	}
	function xzxm(){
		$(window).manhuaDialog({"title" : "","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/gcb/cjdw/cjdw_Query_Add.jsp","modal":"4"});
	}
	//弹出窗口回调函数
	getWinData = function(data){
		var odd=convertJson.string2json1(data);
		$("#ZBDL").val(JSON.parse(data).DWMC);
		$("#ZBDL").attr("code",JSON.parse(data).GC_CJDW_ID);
		// document.getElementById('DSFJGID').code = JSON.parse(data).GC_CJDW_ID; 
	};
</script>  
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">需求信息
  	  <span class="pull-right">
  	  		<button id="example1" class="btn"  type="button">选择需求</button>
      </span>  
  </h4>

 <form method="post"  id="demoForm1"  >
     	<div class="overFlowX">                                
		<table  width="100%" class="table-hover table-activeTd B-table" id="DT2" type="single" >
			<thead>
				<tr>
				    <th  name="XH" id="_XH" >&nbsp;#&nbsp;</th>
				 	<th fieldname="GZMC"  maxlength="15" >&nbsp;工作名称&nbsp;</th>
				 	<th fieldname="GZNR"  maxlength="15" >&nbsp;工作内容&nbsp;</th>
				 	<th fieldname=ZBLX  maxlength="15" >&nbsp;招标类型&nbsp;</th>
				 	<th fieldname="JSYQ"  maxlength="15" >&nbsp;技术要求&nbsp;</th>
				 	<th fieldname="TBJFS"  maxlength="15" >&nbsp;投标报价方式&nbsp;</th>
				 	<th fieldname="YSE"  maxlength="15" >&nbsp;投资额&nbsp;</th>
				</tr>
			</thead>
			 <tbody>
		     </tbody>
		</table>
	</div>
	</form>

	<form method="post" id="demoForm2"  >
        		<h4 class="title">招标信息
        			<span class="pull-right">
				      <button id="bc_btn" class="btn"  type="button">保存</button>
                	</span>
        		</h4>
			<div style="height:5px;"></div>	
      		<table class="B-table" width="100%"  >
	      		<TR  style="display:none;">
				    <TD>
						<input class="span12"  id="GC_ZTB_SJ_ID"  type="text"  name = "GC_ZTB_SJ_ID" fieldname="GC_ZTB_SJ_ID">
						 <input type="text" id="ZTBXQID" name="ZTBXQID"/>
				    </TD>
	            </TR>
	            <tr>
      				<th width="8%" class="right-border bottom-border text-right">招投标名称</th>
          			<td width="42%" colspan=3 class="right-border bottom-border "  >
          				<input class="span12" id="ZTBMC" type="text" fieldname="ZTBMC" name = "ZTBMC">
          			</td>
         			<th width="8%" class="right-border bottom-border text-right">招标编号</th>
          			<td width="17%" class="right-border bottom-border" >
          				<input class="span12" type="text" id="ZBBH" check-type="required" placeholder="必填" fieldname="ZBBH" name="ZBBH">
          			</td>
          			<th width="8%" class="right-border bottom-border text-right">招标方式</th>
          			<td width="17%" class="right-border bottom-border" >
          				<select class="span12 4characters" id="ZBFS" type="text" fieldname="ZBFS" name="ZBFS" kind = "dic" src="ZBFS">
          				</select>
          			</td>
          		</tr>
         		<tr>
          			<th width="8%" class="right-border bottom-border text-right">招标代理</th>
          			<td width="42%" colspan=3 class="right-border bottom-border" >
          				<input style="width: 90%" class="span12" type="text" id="ZBDL" name="ZBDL" fieldname="ZBDL" maxlength="100"  disabled>
          				<a href="javascript:void(0)" title="请选择单位"><i class="icon-edit" onclick="xzxm();"></i></a>
          			</td>
          			<td class="right-border bottom-border" colspan=4></td>
          		</tr>
       		</table>
       		
      	</form>
     	</div>
 	</div>
</div>
 <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
 <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
  <script>

</script>
</body>
</html>