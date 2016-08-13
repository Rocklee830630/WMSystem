<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%
	String GC_JH_SJ_ID=request.getParameter("GC_JH_SJ_ID");
	String num=request.getParameter("num");
	String xmglgs = request.getParameter("xmglgs");
	String xmbh = request.getParameter("xmbh");
%>
<app:base/>
<app:dialogs></app:dialogs>
<title>标段划分管理</title>
<script type="text/javascript" charset="utf-8">
 
var controllername= "${pageContext.request.contextPath }/bdhf/bdwhController.do";
var num_id,success;
var GC_JH_SJ_ID='<%=GC_JH_SJ_ID%>';
var xmglgs = '<%=xmglgs %>';
var g_xmbh = '<%=xmbh%>';
$(function() {
	var xmmc =$($(window).manhuaDialog.getParentObj().document).find("#xmmc").val();
	var num=document.getElementsByTagName("font");
	num[0].innerHTML=xmmc;
	addbg('<%=num%>');
	//按钮绑定事件（新增）
    $("#example_save").click(function() {
    	if($("#DT1").validationButton())
		{	
	 		var data = '';
	 		var a = '';
			 for(var n=0;n<num_id;n++)
			{
				a=Form2Json.formToJSON(document.getElementById("bd"+n));
				data=a+','+data;
			}	 
			data = data.substring(0, data.length - 1);
	 		var data1=defaultJson.packSaveJson(data);
	 		success=defaultJson.doInsertJson(controllername + "?insertdemo&jhsjid="+GC_JH_SJ_ID, data1,null,"insert");
		}else{
	 		requireFormMsg();
			return ;
		}
    });
	
});


//插入回调函数
function insert()
{
	if(success==true)
	{
		var parentmain=$(window).manhuaDialog.getParentObj();	
		parentmain.gsPar_feedback();
		//parentmain.successinfo();
		$(window).manhuaDialog.close();
	}					
}


//生成表单
function addbg(num){
	num_id=num;
	for(var i=0;i<num;i++){
		var demoform='bd'+i;
		$("#addtbody").append('<tr><form id=\"'+demoform+'\"><th align=center><strong>'+parseInt(i+1)+'</strong></th><td class="right-border bottom-border"><input class="span12" type="text" placeholder="必填" data-toggle="modal" style="min-width:300px;" check-type="required maxlength" maxlength="100" name="PRE_BDBM" fieldname="PRE_BDBM" value="'+g_xmbh+'"  form = "'+demoform+'" disabled/></td><td class="right-border bottom-border"><input class="span12" type="text" placeholder="必填" data-toggle="modal" style="min-width:100px;" check-type="required maxlength" maxlength="100" name="BD_XMBM" fieldname="BD_XMBM" form = "'+demoform+'"/></td><td class=\"right-border bottom-border\"><input class=\"span12\" type=\"text\" placeholder=\"必填\" data-toggle=\"modal\" check-type=\"required maxlength\" maxlength=\"36\" name=\"BDBH\" fieldname=\"BDBH\" form = \"'+demoform+'\"/></td><td class=\"right-border bottom-border\"><input class=\"span12\" type=\"text\" name=\"BDMC\" placeholder=\"必填\" check-type=\"required maxlength\" maxlength=\"200\" fieldname=\"BDMC\" form = \"'+demoform+'\" /></td><td class=\"right-border bottom-border\"><input id=\"QDZH'+i+'\"class=\"span12\" type=\"text\" name=\"QDZH\" check-type=\"maxlength\" maxlength=\"100\"fieldname=\"QDZH\" form = \"'+demoform+'\"/></td><td class=\"right-border bottom-border\"><input id=\"ZDZH'+i+'\"class=\"span12\" type=\"text\" name=\"ZDZH\" check-type=\"maxlength\" maxlength=\"100\"fieldname=\"ZDZH\" form = \"'+demoform+'\"/></td><td class=\"right-border bottom-border\"><input id=\"BDDD'+i+'\" class=\"span12\" type=\"text\" name=\"BDDD\" onfocus=\"qsd('+i+')\" check-type=\"maxlength\" maxlength=\"100\"fieldname=\"BDDD\"form = \"'+demoform+'\"/></td><td class=\"right-border bottom-border\"><select  class=\"span12\" type= \"select-one\" id = \"XMGLGS'+i+'\" name = \"XMGLGS\" fieldname= \"XMGLGS\" kind=\"dic\" src=\"\" form = \"'+demoform+'\"></select></td><td class=\"right-border bottom-border\"><input class=\"span12\" type=\"text\" name=\"JSGM\"  check-type=\"maxlength\" maxlength=\"500\" fieldname=\"JSGM\" form = \"'+demoform+'\" /></td><td class=\"right-border bottom-border\"><input  class=\"span12\" type=\"number\" name=\"CD\" min=\"0\" style=\"text-align:right;\" fieldname=\"CD\"form = \"'+demoform+'\"/></td><td class=\"right-border bottom-border\"><input class=\"span12\" type=\"number\" name=\"KD\" min=\"0\" style=\"text-align:right;\" fieldname=\"KD\"form = \"'+demoform+'\"/></td><td class=\"right-border bottom-border\"><input class=\"span12\" type=\"number\" fieldname=\"GJ\" min=\"0\" name=\"GJ\" style=\"text-align:right;\"form = \"'+demoform+'\"/></td><td class=\"right-border bottom-border\"><input class=\"span12\" type=\"number\" fieldname=\"MJ\" name=\"MJ\" min=\"0\" style=\"text-align:right;\"form = \"'+demoform+'\"/></td><td class=\"right-border bottom-border\"><input class=\"span12\" type=\"number\" fieldname=\"GCZTFY\" min=\"0\" style=\"text-align:right;\" name=\"GCZTFY\"form = \"'+demoform+'\"/></td><td class=\"right-border bottom-border\"><input type="text" class=\"span12\" check-type=\"maxlength\" maxlength=\"4000\" fieldname=\"BZ\" name=\"BZ\"form = \"'+demoform+'\"/><input type="hidden" class=\"span12\"  fieldname=\"GC_JH_SJ_ID\" value=\"'+GC_JH_SJ_ID+'\" name=\"GC_JH_SJ_ID\"form = \"'+demoform+'\"/></td></form></tr>');
		generateFyjjhNdMc($("#XMGLGS"+i));
		setDefaultOption($("#XMGLGS"+i),xmglgs);
	}	
}
//重新加载表选字典值
function generateFyjjhNdMc(obj){
	obj.attr("src","T#VIEW_YW_XMGLGS:ROW_ID:BMJC");
	obj.html('');
	reloadSelectTableDic(obj);
}

//拼接起始点
function qsd(n)
{	
	if($("#BDDD"+n).val()=='')
	{
		if($("#QDZH"+n).val()!=''&&$("#ZDZH"+n).val()!='')
		{
	 		var a=$("#QDZH"+n).val();
	 		var b=$("#ZDZH"+n).val();
	 		var c=a+'—'+b;
	 		$("#BDDD"+n).val($("#QDZH"+n).val()+'—'+$("#ZDZH"+n).val());			
		}	
	}	
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">			
			<h4 class="title">
				当前项目：<font style=" margin-left:5px; margin-right:5px;color:gray;"></font>
				<span class="pull-right">  
					<button class="btn" id="example_save">保存</button> 
				</span>
			</h4>
			<div class="overFlowX"> 
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" noPage="true" pageNum="1000" editable="1">
	                <thead id="bd">
	                    <tr>
	                     	<th id="_XH" name="XH" tdalign="center">&nbsp;#&nbsp;</th>
							<th fieldname="PRE_BDBM" type="text">&nbsp;编码前缀&nbsp;</th>
							<th fieldname="BD_XMBM" type="text">&nbsp;标段编码&nbsp;</th>
							<th fieldname="BDBH" type="text">&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC" maxlength="15" type="text">&nbsp;标段名称&nbsp;</th>
							<th fieldname="QDZH" type="text" tdalign="right">&nbsp;起点(桩号)&nbsp;</th>
							<th fieldname="ZDZH" type="text" tdalign="right">&nbsp;终点(桩号)&nbsp;</th>
						    <th fieldname="BDDD" type="text" tdalign="right">&nbsp;起止点&nbsp;</th>
						    <th fieldname="XMGLGS" type="dic" tdalign="right" >&nbsp;项目管理公司&nbsp;</th>
						    <th fieldname="JSGM" type="text">&nbsp;建设内容及规模&nbsp;</th>
							<th fieldname="CD" type="number">&nbsp;长度&nbsp;</th>
							<th fieldname="KD" type="number" tdalign="right">&nbsp;宽度&nbsp;</th>
							<th fieldname="GJ" type="number" tdalign="right">&nbsp;管径&nbsp;</th>
							<th fieldname="MJ" type="number" tdalign="right">&nbsp;面积&nbsp;</th>
							<th fieldname="GCZTFY" type="number" tdalign="right">&nbsp;工程主体费用（万元）&nbsp;</th>
							<th fieldname="BZ" type="text">&nbsp;备注&nbsp;</th>
						</tr>
	                </thead>
	                <tbody id = "addtbody"></tbody>
	           </table>
	       </div>     
		</div>
	</div>		
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" id="px" fieldname="XMBH,LRSJ"/>
		<input type="hidden" name="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
	<form method="post" id="queryForm">
		<table class="B-table">
			<!--可以再此处加入hidden域作为过滤条件 -->
			<TR style="display: none;">
				<TD class="right-border bottom-border"></TD>
				<TD class="right-border bottom-border">
					<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
				</TD>
			</TR>
		</table>
	</form>
</div>
</body>
</html>