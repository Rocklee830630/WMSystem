<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>项目选择</title>
		<%	String callBackFun = request.getParameter("callBack"); 
			callBackFun = "".equals(callBackFun)||callBackFun==null?"":callBackFun;
		%>
		<script
			src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
var controllername= "${pageContext.request.contextPath }/gcCjjhController.do";
var autocompleteXmmcController= "${pageContext.request.contextPath }/xmcbk/xmcbkwhController.do";
var g_callBackFun = '<%=callBackFun%>';
//计算本页表格分页数
function setPageHeight(){
	var height = getDivStyleHeight()-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#xdxmkList").attr("pageNum",pageNum);
}

//自定义的获取页面查询条件
function getQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
    return data;
}

function queryList(){
	//生成json串
	var data = getQueryCondition();
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryXmList",data,xdxmkList);
}

//页面初始化
$(function() {
	setPageHeight();
	initPage();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQuery","getQueryCondition");
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        //其他处理放在下面
        initPage();
    });
  	//按钮绑定事件（确定）
    $("#btnQd").click(function() {
    	if($("#xdxmkList").getSelectedRowIndex()==-1)
		 {
			xInfoMsg('请选择一条记录！',"");
		    return
		 }
        var rowValue = $("#xdxmkList").getSelectedRow();//获得选中行的json对象
        if(g_callBackFun!=""){
        	$(window).manhuaDialog.getParentObj().eval(g_callBackFun+"("+JSON.stringify(rowValue)+")");
        }else{
	        $(window).manhuaDialog.setData(rowValue);
			$(window).manhuaDialog.sendData();
		}
		$(window).manhuaDialog.close();
    });
});
function initPage(){
	$("#QueryND").attr("src","T#GC_TCJH_XMCBK: distinct ND:ND AS NND:SFYX='1' order by NND asc ");
	$("#QueryND").attr("kind","dic");
	$("#QueryND").html('');
	reloadSelectTableDic($("#QueryND"));
	setDefaultNd("QueryND");
}

//标段图标样式
function doBdmc(obj){
	if(obj.XMBS == "0"){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}

//标段编号图标样式
function doBdbh(obj){
	if(obj.BDBH == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
	}
}
//详细信息
function rowView(obj){
    //var obj_json=$("#DT1").getSelectedRowJsonByIndex(index);//获取行json串
	//var obj=convertJson.string2json1(obj_json);//获取行对象
	var id_xxxx=obj.GC_TCJH_XMCBK_ID;//取行对象<项目编号>
	var zt=obj.ISXD;
	var xdlx=obj.XDLX;
	var pcid=obj.PCID;
	var pch=obj.PCH;//取行对象<项目批次号>
	var showStr = "";
	if((obj.XMBM).substring(0,5)=="XXXXX"){
		showStr = "<abbr title='"+obj.XMBM+"'>"+obj.XMBH+"</abbr>";
	}else if(obj.XMBM==""){
		showStr = obj.XMBH;
	}else{
		showStr = "<abbr title='"+obj.XMBM+"'>"+(obj.XMBM).substring(0,14)+"</abbr>";
	}
	return "<a href='javascript:void(0);' onclick='showInfoCard(this);' id_xxxx='"+id_xxxx+"' pch='"+pch+"' zt='"+zt+"' xdlx='"+xdlx+"' pcid='"+pcid+"'>"+showStr+"</a>";
}
</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">

			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						项目信息
						<span class="pull-right">
							<button id="btnQd" class="btn" type="button">
								确定
							</button> </span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>

								<!--公共的查询过滤条件 -->
								<th width="5%" class="right-border bottom-border text-right">
									年度
								</th>
								<td class="right-border bottom-border" width="15%">
									<select class="span12" id="QueryND" name="QueryND"
										fieldname="t.ND" operation="=" defaultMemo="全部">
									</select>
									<input style="display: none;" class="span12" type="text"
										id="QXMSX" name="QXMSX" fieldname="t.XMSX" operation="=" />
									<input style="display: none;" class="span12" type="text"
										id="QISXF" name="QISXF" fieldname="t.ISXF" operation="="
										value="" keep="true" />
								</td>
								<th width="7%" class="right-border bottom-border text-right">
									项目编号
								</th>
								<td class="right-border bottom-border" width="15%">
									<input class="span12" type="text" placeholder="" name="XMBH"
										fieldname="t.XMBH" operation="like" id="XMBH">
								</td>
								<th width="7%" class="right-border bottom-border text-right">
									项目名称
								</th>
								<td class="right-border bottom-border" width="20%">
									<input class="span12" type="text" placeholder="" name="QXMMC"
										fieldname="t.XMMC" operation="like" id="QXMMC"
										autocomplete="off" tablePrefix="t">
								</td>

								<td class="text-left bottom-border text-right">
									<button id="btnQuery" class="btn btn-link" type="button">
										<i class="icon-search"></i>查询
									</button>
									<button id="btnClear" class="btn btn-link" type="button">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
						</table>
					</form>
					<div style="height: 5px;"></div>
					<div class="overFlowX">
						<table width="100%" class="table-hover table-activeTd B-table"
							id="xdxmkList" type="single">
							<thead>
								<tr>
									<th name="XH" id="_XH" style="width: 10px">
										&nbsp;#&nbsp;
									</th>
									<th fieldname="XMBH" tdalign="center" rowMerge="true" CustomFunction="rowView">
										&nbsp;项目编号&nbsp;
									</th>
									<th fieldname="XMMC" maxlength="20" rowMerge="true">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="XMDZ" maxlength="20">
										&nbsp;项目地址&nbsp;
									</th>
									<th fieldname="JSNR" maxlength="20">
										&nbsp;建设内容&nbsp;
									</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>

		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="t.xmbh,t.pxh" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
	</body>
</html>