<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base/>
<app:dialogs></app:dialogs>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
var controllername= "${pageContext.request.contextPath }/bdhf/bdwhController.do";
var controllernameXM= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
var xmid,xmmc,GC_JH_SJ_ID,flag;

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#DT1").attr("pageNum",pageNum);
}

$(function() {
	setPageHeight();
	//自动完成项目名称模糊查询
	showAutoComplete("QXMMC",controllernameXM+"?xmmcAutoQuery","getXmmcQueryCondition");
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        getNd();
    });
  	//按钮绑定事件（新增）
    $("#btnInsert").click(function() {
    	if($("#DT1").getSelectedRowIndex()!=-1){	
    		var obj =$("#DT1").getSelectedRow();
    		var xmmc = convertJson.string2json1(obj).XMMC;
			var text=document.getElementsByTagName("font");
			text[0].innerHTML=xmmc;
			$("#myModal").modal("show");
    	}
    	else{
    		requireSelectedOneRow();
    	}	
    });
  	//按钮绑定事件（确定）
    $("#btnQd").click(function() {
    	var num=$("#num").val();
    	if(num==""){
    		xAlert("信息提示",'请输入标段数!','3');
    		return
    	}
    	$("#num").val('');
    	$("#myModal").modal("hide");
    	var obj =$("#DT1").getSelectedRow();
		var xmmc = convertJson.string2json1(obj).XMMC;
		var jhsjid = convertJson.string2json1(obj).GC_JH_SJ_ID;
		var xmglgs = convertJson.string2json1(obj).XMGLGS;
		var xmbh = convertJson.string2json1(obj).XMBH;
    	$("#xmmc").val(xmmc);
    	$(window).manhuaDialog({"title":"标段划分>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bdhf/addbd.jsp?GC_JH_SJ_ID="+jhsjid+"&xmbh="+xmbh+"&num="+num+"&xmglgs="+xmglgs,"modal":"1"});	
    });
  	//按钮绑定事件（维护）
    $("#btnWh").click(function() {
    	if($("#DT1").getSelectedRowIndex()!=-1){	
    		var obj =$("#DT1").getSelectedRow();
    		var xmid = convertJson.string2json1(obj).XMID;
    		var xmbs = convertJson.string2json1(obj).XMBS;
    		var jhsjid = convertJson.string2json1(obj).GC_JH_SJ_ID;
    		var id = "";
    		if(xmbs == "0"){
    			id = xmid;
    		}else{
    			id = convertJson.string2json1(obj).BDID;
    		}
    		$(window).manhuaDialog({"title":"标段划分>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bdhf/xgbd.jsp?xmid="+id+"&xmbs="+xmbs+"&jhsjid="+jhsjid,"modal":"1"});
    	}
    	else{
    		requireSelectedOneRow();
    	}	
    });
});

//项目名称自动模糊查询参数
function getXmmcQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	return data;
}


function init(){
	getNd();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
}
//查询列表
function queryList(){
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	defaultJson.doQueryJsonList(controllername+"?queryList",data,DT1,null,true);
}
//默认年度
function getNd(){
	generateFyjjhNdMc($("#qnd"),$("#JHID")); 
	setOptionSelectedIndex($("#JHID"));
}

//点击获取行对象
function tr_click(obj,tabListid){
	if(tabListid == "DT1"){
		if(obj.ISNOBDXM == '0'){
			$("#btnInsert").attr("disabled","disabled");
			$("#btnWh").removeAttr("disabled");
		}else if(obj.ISNOBDXM == '1' && obj.XMBS =='1'){
			$("#btnWh").removeAttr("disabled");
			$("#btnInsert").attr("disabled","disabled");
		}else{
			$("#btnInsert").removeAttr("disabled");
			$("#btnWh").attr("disabled","disabled");
		}
	}
}

//详细信息
function rowView(index){
    var obj=$("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
	var xmid=convertJson.string2json1(obj).XMID;//取行对象<项目编号>
	$(window).manhuaDialog(xmscUrl(xmid));//调用公共方法,根据项目编号查询
}

//行新增
function gsPar_feedback(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	data = defaultJson.getQueryConditionWithNowPageNum(data,"DT1");
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryList",data,DT1,null,true);
	xSuccessMsg("操作成功");
	$("#btnInsert").attr("disabled","disabled");
	$("#btnWh").removeAttr("disabled");
}

//<标段编号>样式
function doBDBH(obj){
	if(obj.BDBH == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDBH;
		/* var id = obj.BDID;
		return "<a href=\"javascript:void(0);\" onclick=\"showView('"+id+"')\">"+obj.BDBH+"</a>"; */
	}
}
/* function showView(id){
	$(window).manhuaDialog({"title":"标段划分>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bdhf/bdView.jsp?id="+id,"modal":"1"});	
} */
//<标段名称>样式
function doBDMC(obj){
	if(obj.BDMC == ""){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}

//<项目管理公司>样式
function doXMGLGS(obj){
	if(obj.XMGLGS == ""){
		if(obj.XMBS == '0'){
			return '<div style="text-align:center">—</div>';
		}else{
			return obj.XMGLGS1_SV;
		}
	}else{
		return obj.XMGLGS1_SV;
	}
}

</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header" style="background:#0866c6;">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-remove icon-white"></i></button>
			<h4 id="myModalLabel" style="color:white;">
				当前项目：<font style=" margin-left:5px; margin-right:5px;"></font>
			</h4>
		</div>
		<div class="modal-body">
			<table class="B-table">
				<tr>
					<th width="30%" class="right-border bottom-border">新增标段数</th>
					<td class="right-border bottom-border">
						<input type="number" name="bdnum" id="num" min="0"/>
					</td>
				</tr>
			</table>
		</div>
		<div class="modal-footer">
			<button id="btnQd" class="btn">确定</button>
			<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
		</div>
	</div>
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">
				项目标段  
				<span class="pull-right">  
					<app:oPerm url="jsp/business/bdhf/addbd.jsp">
						<button id="btnInsert" class="btn" title="已划分过标段的项目不能使用新增功能">新增</button>
					</app:oPerm>
					<app:oPerm url="jsp/business/bdhf/xgbd.jsp">
						<button id="btnWh" class="btn" title="维护操作针对标段">维护</button>
					</app:oPerm>
				</span>
			</h4>
			<form method="post" id="queryForm">
				<table class="B-table" width="100%">
					<!--可以再此处加入hidden域作为过滤条件 -->
					<TR style="display: none;">
						<TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
						</TD>
					</TR>
					<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
			          <th width="5%" class="right-border bottom-border text-right">年度</th>
			          <td class="right-border bottom-border" width="6%">
			            <select class="span12 year" id="qnd" name = "QND"  fieldname ="t.ND" defaultMemo="全部" operation="=">
			            </select>
			          </td>
			          <th width="5%" class="right-border bottom-border text-right">批次</th>
			          <td class="right-border bottom-border" width="9%">
			            <select class="span12 4characters" id="JHID" name = "QJHID"  defaultMemo="全部" fieldname = "t.JHID" operation="=">
			            </select>
			          </td>
			          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
			          <td class="right-border bottom-border" width="20%">
			          	<input class="span12" type="text" placeholder="" name="QXMMC"
							fieldname="t.XMMC" operation="like" id="QXMMC" autocomplete="off"
							tablePrefix="t"/>
					  </td>
		            <td class="text-left bottom-border text-right">
						<button	id="btnQuery" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
                        <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
		            </td>																				
				</tr>
			</table>
		</form>
			<div style="height:5px;"></div>								
			<div class="overFlowX"> 
				<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" printFileName="项目标段">
	                <thead>
	                    <tr>
							<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" hasLink="true" linkFunction="rowView" rowMerge="true">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" maxlength="15" rowMerge="true">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDBH" CustomFunction="doBDBH">&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC" maxlength="15" CustomFunction="doBDMC">&nbsp;标段名称&nbsp;</th>
							<th fieldname="XMGLGS" tdalign="center" CustomFunction="doXMGLGS">&nbsp;项目管理公司&nbsp;</th>
							<th fieldname="BDDD" maxlength="15" >&nbsp;项目地址&nbsp;</th>	
							<th fieldname="JSGM" maxlength="15" >&nbsp;建设内容及规模&nbsp;</th>						
							<th fieldname="GCZTFY" tdalign="right">&nbsp;工程主体费用(万元)&nbsp;</th>							
						</tr>
	                </thead> 
	              	<tbody></tbody>
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
		<input type="hidden" name="txtFilter" order="asc" fieldname="pxh,xmbh,xmbs,bdbh "/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
	<input type="hidden" name="xmmc" id="xmmc"/>
</div>
</body>
</html>