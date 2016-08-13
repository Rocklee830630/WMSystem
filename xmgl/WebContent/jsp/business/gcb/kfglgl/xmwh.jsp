<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<title>开复工令-指令管理</title>
<%
	String id = request.getParameter("id");
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/kfgl/kfglController.do";
var id = "<%= id%>";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//获取复选框的值
		var kfg = "";
		$("input[type='checkbox']").each(function(){
			if($(this).is(':checked')){
				kfg += $(this).attr('value')+",";
			}
		});
		var kssj = $("#kssj").val();
		var jssj = $("#jssj").val();
		if(kssj > jssj){
			xInfoMsg('查询条件<填报时间>输入有误！',"");
			return;
		}
		
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,kfglList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryKfgById&kfg="+kfg,data,kfglList);
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        clearCkeckBox();
        //其他处理放在下面
    });
    //按钮绑定事件（新增）
    $("#btnInsert").click(function() {
    	$("#kfglForm").clearFormResult();
        clearFileTab();
        $("#ywid").val("");
        setDefaultInfo();
        $("#kfglList").cancleSelected();
        $("#btnSave").removeAttr("disabled");
        $("#btnDel").removeAttr("disabled");
        $("#sp_btn")[0].innerText = "呈请审批";
		releaseItem();
      	/* //禁用第一个
      	 $("input[name='KGFG']").each(function(){ 
			$(this).removeAttr("checked");
		 });  */
		
    });
  //按钮绑定事件（呈请审批）
    $("#sp_btn").click(function() {
    	if($("#kfglList").getSelectedRowIndex()==-1){
    		requireSelectedOneRow();
    		return;
    	}
    	var index1 =$("#kfglList").getSelectedRowIndex();
 	 /* 	if(index1<0) 
		{
 			requireSelectedOneRow();
 			return;
		} */
 		var obj = $("#kfglList").getSelectedRow();
 		var tempValue = convertJson.string2json1(obj);
 		var sjbh = tempValue.SJBH;
 		var ywlx = tempValue.YWLX;
 		var sjzt = tempValue.EVENTSJBH;
 		var kfgl = tempValue.KGFG;
 		if(sjzt!=0&&sjzt!=7){
 			var isview = "1";
 		    var isOver = getProIsover(sjbh,ywlx);
 		    if(isOver =="1"){
 		    	isview = "0";
 		    }
 			var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview="+isview;   
 		  	$(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
 		}else if(sjzt == 7){
 			var isview = "1";
 		    var isOver = getProIsover(sjbh,ywlx);
 		    if(isOver =="1"){
 		    	isview = "0";
 		    }
 			var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview="+isview;   
 		  	$(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
 		}else{
 			createSPconf(sjbh,ywlx,"");
 		}
    	
    });
  	//按钮绑定事件（保存）
    $("#btnSave").click(function() {
    	if($("#kfglForm").validationButton())
		{
    		if($('input:radio[name="KGFG"]:checked').val() == null){
    			xInfoMsg("请选择<开复工>","");
    			return;
    		}
    		//生成json串
    		var data = Form2Json.formToJSON(kfglForm);
 		  	//组成保存json串格式
 		    var data1 = defaultJson.packSaveJson(data);
    		if($("#GC_GCB_KFG_ID").val() == "" || $("#GC_GCB_KFG_ID").val() == null){
    			defaultJson.doInsertJson(controllername + "?insert&ywid="+$("#ywid").val(), data1,kfglList);
    			$("#kfglList").setSelect(0);
    			var data = $("#kfglList").getSelectedRowJsonByIndex(0);
    			var obj=convertJson.string2json1(data)
   				$("#sp_btn")[0].innerText = "呈请审批";
   				$("#sp_btn").attr("disabled","disabled");
    			$("#kfglForm").setFormValues(obj);
    			var parentmain=$(window).manhuaDialog.getParentObj();	
    			parentmain.gs_feedback();
    		}else{
    			defaultJson.doUpdateJson(controllername + "?insert&ywid="+$("#ywid").val(), data1,kfglList);
       			var parentmain=$(window).manhuaDialog.getParentObj();	
    			parentmain.gs_feedback();
    			var data=$("#kfglList").getSelectedRow();
    			var obj=convertJson.string2json1(data)
   				if(obj.EVENTSJBH==0)
   				{
       				$("#sp_btn")[0].innerText = "呈请审批";
       				$("#sp_btn").removeAttr("disabled");   					
   				}
   				else
   				{
       				$("#sp_btn")[0].innerText = "审批信息";
       				$("#sp_btn").removeAttr("disabled");   					   					
   				}	  				
    		}
		}else{
			requireFormMsg();
		  	return;
		}
    });
	
  	//按钮绑定事件（删除）
    $("#btnDel").click(function(){
    	if($("#kfglList").getSelectedRowIndex()==-1){
    		requireSelectedOneRow();
    		return;
    	}else{
   		 	var data = Form2Json.formToJSON(kfglForm);
   			var data1 = defaultJson.packSaveJson(data);
   			xConfirm("提示信息","是否确认删除！");
   			$('#ConfirmYesButton').unbind();
   			$('#ConfirmYesButton').one("click",function(){  
   				defaultJson.doDeleteJson(controllername+"?delete",data1,kfglList,null); 
   				$("#kfglForm").clearFormResult();
   				var parentmain=$(window).manhuaDialog.getParentObj();	
    			parentmain.queryList();
   			});   		
    	}
    });
});
//页面默认参数
function init(){
	$("#id").val(id);
	getPensonByDep();
	g_bAlertWhenNoResult = false;
	queryList();
	g_bAlertWhenNoResult = true;
	setFormDate();
	setDefaultInfo();
	_kgfg();
}
//初始化数据
function setDefaultInfo(){
	$("#TCJHSJID").val(id);
	//登陆用户ID
	$("#TBR").val("<%=userid %>");
	//登陆用户名称
	$("#TBRXM").val("<%=username %>");
	//登陆用户所在部门ID
	$("#TBDW").val("<%=deptId %>");
	//登陆用户所在部门名称
	$("#TBDWMC").val("<%=deptName %>");
	//登陆时间
	$("#BLSJ").val("<%=sysdate %>");
	//设计单位，暂定这样定义
	$("#JSDW").val("长春市政府投资建设项目管理中心");
}
//点击行事件
function tr_click(obj){
	$("#kfglForm").setFormValues(obj);
	_kgfg();
	if(obj.KGFG==0 || obj.KGFG==2 || obj.KGFG==1)
	{
		if(obj.EVENTSJBH == ""){//没有登记指令管理
			$("#btnSave").removeAttr("disabled");
			$("#btnDel").removeAttr("disabled");
			$("#sp_btn")[0].innerText = "呈请审批";
			$("#sp_btn").attr("disabled","disabled");
			deleteFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX);
			setFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX,"0");
			$("#fileTab").attr("onlyView","false");
			queryFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX);
		}else{
			if(obj.EVENTSJBH != 0&&obj.EVENTSJBH !=7) {
				deleteFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX);
				setFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX,"0");
				$("#fileTab").attr("onlyView","true");
				queryFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX);
				$("#sp_btn")[0].innerText = "审批信息";
				$("#btnSave").attr("disabled","disabled");
				$("#btnDel").attr("disabled","disabled");
				$("#sp_btn").removeAttr("disabled");
			}else if(obj.EVENTSJBH ==7){
				deleteFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX);
				setFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX,"0");
				$("#fileTab").attr("onlyView","true");
				queryFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX);
				$("#sp_btn")[0].innerText = "审批信息";
				$("#btnSave").removeAttr("disabled");
				$("#btnDel").attr("disabled","disabled");
				$("#sp_btn").removeAttr("disabled");
				lockItem();
			}else {
				deleteFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX);
				setFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX,"0");
				$("#fileTab").attr("onlyView","false");
				queryFileData(obj.GC_GCB_KFG_ID,"",obj.SJBH,obj.YWLX);
				$("#sp_btn")[0].innerText = "呈请审批";
				$("#sp_btn").removeAttr("disabled");
				$("#btnSave").removeAttr("disabled");
				$("#btnDel").removeAttr("disabled");
				releaseItem();
			}
		}	
	}else{
		$("#sp_btn")[0].innerText = "呈请审批";
		$("#sp_btn").attr("disabled","disabled");
		releaseItem();
	}	
}
function lockItem(){
	$("input[name='KGFG']").each(function(){
		$(this).attr("disabled","true");
	});
}
function releaseItem(){
	$("input[name='KGFG']").each(function(){
		$(this).removeAttr("disabled");
	});
}
//清空查询条件时，去掉选中状态
function clearCkeckBox(){
     $("input[type='checkbox']").each(function () { 
        $(this).attr("checked", false);
     }); 
}

//查询列表
function queryList(){
	var kfg = "";
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,kfglList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryKfgById&kfg="+kfg,data,kfglList);
}
//初始化页面，自动将该记录的业务信息绑定到表单上
function setFormDate(){
	var pwindow =$(window).manhuaDialog.getParentObj();
	var rowValue = pwindow.document.getElementById("resultXML").value;
	//var rowValue = $(parent.frames["menuiframe"].document).find("#resultXML").val();
	var tempJson = convertJson.string2json1(rowValue);
	$("#kfglForm").setFormValues(tempJson);
	$("#XDKID").val(tempJson.GC_TCJH_XMXDK_ID);
	$("#BDID").val(tempJson.BDID);
	/* if(tempJson.XMBS == "1"){
		$("#XMBH").val(tempJson.BDBH);
		$("#XMMC").val(tempJson.BDMC);
	} */
	if(tempJson.KGSJ != null && "" != tempJson.KGSJ ){
		$("#KGSJ").val(tempJson.KGSJ);
	}
	if(tempJson.KGSJ_SJ != null && "" != tempJson.KGSJ_SJ ){
		$("#SJKGSJ").val(tempJson.KGSJ_SJ);
	}
}
//过滤负责人
function getPensonByDep(){
	var depid = '<%=deptId %>';
	var src = "T#VIEW_YW_ORG_PERSON :ACCOUNT:NAME:PERSON_KIND = '3' AND DEPARTMENT = '"+depid+"'";
	$("#fzr").attr('src',src);
	reloadSelectTableDic($("#fzr"));
}
//审批结束回调
function gengxinchaxun(obj)
{
		xAlertMsg(obj);
}

function prcCallback()
{
	gs_feedback();
	$("#sp_btn")[0].innerText = "审批信息";
	$("#btnSave").attr("disabled","disabled");
	$("#btnDel").attr("disabled","disabled");
	//$("#btnXmwh").attr("disabled","disabled");
	$("#sp_btn").removeAttr("disabled");
}
//页刷新
function gs_feedback(){
	 	var row_index=$("#kfglList").getSelectedRowIndex();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,kfglList);
		var tempJson = convertJson.string2json1(data);
		var a=$("#kfglList").getCurrentpagenum();
		tempJson.pages.currentpagenum=a;
		data = JSON.stringify(tempJson);
		var kfg = "";
		defaultJson.doQueryJsonList(controllername+"?queryKfgById&kfg="+kfg,data,kfglList);
		$("#kfglList").setSelect(row_index);
		var json=$("#kfglList").getSelectedRow();
		json=encodeURI(json);
		/* $("#sp_btn")[0].innerText = "呈请审批";
		$("#sp_btn").removeAttr("disabled");
		$("#btnSave").removeAttr("disabled");	 */
}
//标段名称图标样式
function doBdmc(obj){
	if(obj.BDMC == ""){
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

function _kgfg() {
	var kgfg = $("input:radio[name=KGFG]:checked").val();
	if(kgfg == "2") {
		$("#SJTGSJ").attr("check-type","required");
		$("#SJTGSJ").removeAttr("disabled");
		$("#JHJGSJ").attr("disabled","disabled");
		$("#JHJGSJ").removeAttr("check-type");
	} else {
		$("#JHJGSJ").attr("check-type","required");
		$("#JHJGSJ").removeAttr("disabled");
		$("#SJTGSJ").attr("disabled","disabled");
		$("#SJTGSJ").removeAttr("check-type");
	}
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
  <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="text" id="id" name="TCJHSJID" fieldname="t3.TCJHSJID" keep="true" operation="="/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
    <div style="height:5px;"> </div>
	<table class="table-hover table-activeTd B-table" id="kfglList" width="100%" type="single" pageNum="5" nopromptmsg="true">
		<thead>
			<tr>
				<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
				<th fieldname="EVENTSJBH" tdalign="center">&nbsp;当前状态&nbsp;</th>
				<th fieldname="XMBH">&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>
				<th fieldname="BDBH" CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
				<th fieldname="BDMC" maxlength="15" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
				<th fieldname="KGFG" tdalign="center">&nbsp;开工/复工/停工&nbsp;</th>
				<th fieldname="JGNR" maxlength="15">&nbsp;结构内容&nbsp;</th>
				<th fieldname="BLSJ" tdalign="center">&nbsp;办理时间&nbsp;</th>
				<th fieldname="FJ">&nbsp;附件&nbsp;</th>
				<th fieldname="BZ" maxlength="15">&nbsp;备注&nbsp;</th>
			</tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
   </div>
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">开复工令信息
      	<span class="pull-right">
      		<button id="sp_btn" class="btn"  type="button">呈请审批</button>
      		<button id="btnInsert" class="btn" type="button">新增</button>
      		<button id="btnSave" class="btn" type="button">保存</button>
      		<button id="btnDel" class="btn" type="button">删除</button>
  		</span>
      </h4>
     <form method="post" id="kfglForm"  >
      <table class="B-table" width="100%">
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<input type="hidden" id="GC_GCB_KFG_ID" fieldname="GC_GCB_KFG_ID" name = "GC_GCB_KFG_ID"/>
				<input type="hidden" id="TCJHSJID" keep="true" fieldname="TCJHSJID" name = "TCJHSJID"/>
				<input type="hidden" id="XDKID" keep="true" fieldname="XDKID" name = "XDKID"/>
				<input type="hidden" id="BDID" keep="true" fieldname="BDID" name = "BDID"/>
      			<input type="hidden" id="TBR" keep="true" fieldname="TBR" name = "TBR"/>
      			<input type="hidden" id="TBRXM" keep="true" fieldname="TBRXM" name = "TBRXM"/>
      			<input type="hidden" id="TBDW" keep="true" fieldname="TBDW" name = "TBDW"/>
	      			   <!--  add by cbl start -->
				    	<input type="hidden" id="SJBH" fieldname="SJBH" name = SJBH/>
				      	<input type="hidden" id="YWLX" fieldname="YWLX" name = YWLX/>
				      <!--  add by cbl end -->
			</TD>
        </TR>
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">建设单位</th>
          <td class="right-border bottom-border" width="25%">
          	<input class="span12" id="JSDW" type="text" fieldname="JSDW" name = "JSDW"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">施工单位</th>
          <td class="bottom-border right-border"  width="25%">
          	<input class="span12" id="SGDW" type="text" fieldname="SGDW" keep="true" name = "SGDW"  readonly />
         </td>
         <th width="8%" class="right-border bottom-border text-right disabledTh">施工项目负责人</th>
          <td class="bottom-border right-border"  width="26%">
          	<input class="span12" id="FZR_SGDW" type="text" fieldname="FZR_SGDW" keep="true" name = "FZR_SGDW"  readonly />
         </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">设计单位</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="SJDW" type="text" fieldname="SJDW" keep="true" name = "SJDW"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">监理单位</th>
          <td class="bottom-border right-border">
          	<input class="span12" id="JLDW" type="text" fieldname="JLDW" keep="true" name = "JLDW"  readonly />
         </td>
         <th width="8%" class="right-border bottom-border text-right disabledTh">项目总监</th>
          <td class="bottom-border right-border">
          	<input class="span12" id="FZR_JLDW" type="text" fieldname="FZR_JLDW" keep="true" name = "FZR_JLDW"  readonly />
         </td>
        </tr>
		<tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">项目编号</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="XMBH" type="text" fieldname="XMBH" keep="true" name = "XMBH"  readonly />
          	
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
          <td class="bottom-border right-border">
          	<input class="span12" id="XMMC" type="text" fieldname="XMMC" keep="true" name = "XMMC"  readonly />
         </td>
         <th width="8%" class="right-border bottom-border text-right disabledTh">项目起止点</th>
          <td class="bottom-border right-border">
          	<input class="span12" id="XMDZ" type="text" fieldname="XMDZ" keep="true" name = "XMDZ"  readonly />
         </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">标段编号</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="BDBH" type="text" fieldname="BDBH" keep="true" name = "BDBH"  readonly />
          	
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
          <td class="bottom-border right-border">
          	<input class="span12" id="BDMC" type="text" fieldname="BDMC" keep="true" name = "BDMC"  readonly />
         </td>
         <th width="8%" class="right-border bottom-border text-right disabledTh">标段起止点</th>
          <td class="bottom-border right-border">
          	<input class="span12" id="BDDD" type="text" fieldname="BDDD" keep="true" name = "BDDD"  readonly />
         </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">项目类型</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="XMLX" type="text" fieldname="XMLX" keep="true" name = "XMLX" readonly />
          	
          </td>
         <th width="8%" class="right-border bottom-border text-right disabledTh">工程造价</th>
          <td class="bottom-border right-border">
          	<input class="span12" id="HTID" type="text" fieldname="HTID" keep="true" name = "HTID" readonly />
         </td>
         <td colspan="2"></td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">工程数量</th>
          <td class="bottom-border right-border">
          	<input class="span12" id="GCSL" type="text" fieldname="GCSL"  name = "GCSL"/>
         </td>
        </tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">开复工选择</th>
	        <td class="bottom-border" colspan="5">
            	<input class="span12" type="radio" id="KGFG" name = "KGFG" onclick="_kgfg()"  kind="dic" src="KFTGZT" fieldname = "KGFG" operation="=" defaultValue="0"> 
	        </td>
        </tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">结构内容</th>
	        <td class="bottom-border" colspan="5">
	        	<textarea class="span12" id="JGNR" rows="9" name ="JGNR" placeholder="必填" check-type="required" fieldname="JGNR" maxlength="4000"></textarea>
	        </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">备注</th>
          <td class="right-border bottom-border" colspan="5">
          	<textarea class="span12" id="BZ" rows="3" name ="BZ"  fieldname="BZ" maxlength="4000"></textarea>
          </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">计划开工时间</th>
          <td class="right-border bottom-border">
          	<input class="span12" id="KGSJ" type="text" keep="true" name="JHKGSJ" fieldname="JHKGSJ" disabled/>
          </td>
          <th width="8%" class="right-border bottom-border text-right">实际开工时间</th>
          <td class="bottom-border right-border">
          	<input class="span12" id="SJKGSJ" type="date" name="SJKGSJ" fieldname="SJKGSJ"/>
         </td>
         <th width="8%" class="right-border bottom-border text-right">计划竣工时间</th>
          <td class="bottom-border right-border">
          	<input class="span12" id="JHJGSJ" type="date" check-type="required" name="JHJGSJ" fieldname="JHJGSJ"/>
         </td>
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right">负责人</th>
          	<td class="bottom-border right-border">
          		<select class="span12 person" id="fzr" name = "FZR"  fieldname = "FZR" operation="=" kind="dic" src="">
            	</select>
         	</td>
          	<th width="8%" class="right-border bottom-border text-right">填报单位</th>
          	<td class="right-border bottom-border">
          		<input class="span12 department" id="TBDWMC" type="text" keep="true" fieldname="TBDWMC" name = "TBDWMC"  readonly />
          	</td>
          	<th width="8%" class="right-border bottom-border text-right">办理时间</th>
          	<td class="bottom-border right-border">
          		<input class="span12" id="BLSJ" type="date" name="BLSJ" fieldname="BLSJ" />
         	</td>
        </tr>
        
        <tr>
        	<th width="8%" class="right-border bottom-border text-right">实际停工时间</th>
          	<td class="bottom-border right-border">
          		<input class="span12" id="SJTGSJ" type="date" check-type="required" name="SJTGSJ" fieldname="SJTGSJ"/>
         	</td>
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right">附件
			</th>
        	<td colspan="5">
        		<div>
					<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0040">
						<i class="icon-plus"></i>
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0040" id="fileTab"
							class="files showFileTab" data-toggle="modal-gallery"
							data-target="#modal-gallery">
						</tbody>
					</table>
				</div>
				
			</td>
        </tr>
      </table>
      </form>
      	    
    </div>
  </div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
 	   <input type="hidden" name="ywid" id = "ywid" value="">
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="DESC" fieldname = "t3.LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>