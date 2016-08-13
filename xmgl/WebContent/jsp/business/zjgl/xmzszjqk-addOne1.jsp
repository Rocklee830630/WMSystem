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
<title>项目征收资金情况-维护</title>
<%
	String type=request.getParameter("type");
	String qy = request.getParameter("qy");
	if(qy==null)
	    qy = "";
	//获取当前用户信息
	//String cDate = DateTimeUtil.getDate();
	
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	String userid = user.getAccount();
	String username = user.getName();
	String cDate = Pub.getDate("yyyy-MM-dd");
	String cYear = Pub.getDate("yyyy");
%>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/zjgl/gcZjglZszjXmzszjqkController.do";
var jbrID;
var type ="<%=type%>", qy="<%=qy%>";
var retMsg = "";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,xmzszjqkList);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?query",data,xmzszjqkList);

		var data1 = combineQuery.getQueryCombineData(queryForm,frmPost);	
		var data2 = {
			msg : data1
		};	
		//计算使用和拨付笔数
		$.ajax({	
			url : controllername+"?querySumJe",	
				data : data2,	
			cache : false,	
			async :	false,	
			dataType : "json",
			type : 'post',		
			success : function(response) {	
				$("#resultXML").val(response.msg);	
				var resultobj = defaultJson.dealResultJson(response.msg);	
				if(resultobj!=null&&resultobj.ZFJE!=null&&resultobj.ZFJE!=""){
					$("#xmzszjqkList").append('<tr><th align="center"><strong></strong></th><td align="center">合计</td><td align="center"></td><td align="center"></td><td align="center"></td><td align="center"></td><td align="right">'+resultobj.ZFJE_SV+'</td></tr>');
				}else{
					$("#xmzszjqkList").append('<tr><th align="center"><strong></strong></th><td align="center">合计</td><td align="center"></td><td align="center"></td><td align="center"></td><td align="center"></td><td align="right">-</td></tr>');
				}
			}
		});
	});
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		if($("#xmzszjqkForm").validationButton())
		{
    		//生成json串
    		var data = Form2Json.formToJSON(xmzszjqkForm);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax插入
		    if($("#ID").val() == "" || $("#ID").val() == null){
		    	var flag = defaultJson.doInsertJson(controllername + "?insert", data1, xmzszjqkList);
		    	if(flag){
		    		retMsg = "updateWhitoutMsg";
		    		var json=$("#resultXML").val();
		    		var tempJson=eval("("+json+")");
		    		var resultobj=tempJson.response.data[0];
		    		$("#xmzszjqkForm").setFormValues(resultobj);
		    	}
    		}else{
    			defaultJson.doUpdateJson(controllername + "?update", data1, xmzszjqkList);
    			retMsg = "updateWhitoutMsg";
    		}
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
	//按钮绑定事件（新增）
    $("#btnClear_Bins").click(function() {
    	
    	var idStr = $("#ID").val();
		if(idStr==""){
			return false;
		}
		
		xConfirm("信息确认","确定删除所选记录吗？ ");

    	
		$('#ConfirmYesButton').attr('click','');
		$('#ConfirmYesButton').one("click",function(){
			//生成json串
    		var data = Form2Json.formToJSON(xmzszjqkForm);
		  	//组成保存json串格式
		    var data1 = defaultJson.packSaveJson(data);
		  	//调用ajax删除
		    var flag = defaultJson.doInsertJson(controllername + "?delete", data1, xmzszjqkList);
		  //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,xmzszjqkList);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?query",data,xmzszjqkList);

			var data1 = combineQuery.getQueryCombineData(queryForm,frmPost);	
			var data2 = {
				msg : data1
			};	
			//计算使用和拨付笔数
			$.ajax({	
				url : controllername+"?querySumJe",	
					data : data2,	
				cache : false,	
				async :	false,	
				dataType : "json",
				type : 'post',		
				success : function(response) {	
					$("#resultXML").val(response.msg);	
					var resultobj = defaultJson.dealResultJson(response.msg);	
					if(resultobj!=null&&resultobj.ZFJE!=null&&resultobj.ZFJE!=""){
						$("#xmzszjqkList").append('<tr><th align="center"><strong></strong></th><td align="center">合计</td><td align="center"></td><td align="center"></td><td align="center"></td><td align="center"></td><td align="right">'+resultobj.ZFJE_SV+'</td></tr>');
					}else{
						$("#xmzszjqkList").append('<tr><th align="center"><strong></strong></th><td align="center">合计</td><td align="center"></td><td align="center"></td><td align="center"></td><td align="center"></td><td align="right">-</td></tr>');
					}
				}
			});
		});

        //$("#xmzszjqkForm").clearFormResult();
        //$("#xmzszjqkForm").cancleSelected();
        
        
    });
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
    });
	
    $("button[id^='blrBtn_']").bind("click", function(){
    	var valu = $(this).attr("value");
    	jbrID = valu;
    	var actorCode = $("#"+valu).attr("code");
		openUserTree('single',actorCode,'setBlr') ;
	});
	
});
//页面默认参数
function init(){
	if(qy!=""){
		$("#QJHSJID").val(qy);
	}
	
	//$("#QZFRQ").val("<%=cYear%>");
	$("#ZFRQ").val('<%=cDate%>');
	//获取父窗口行设置的条件
	
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xmzszjqkList);
	//调用ajax插入
	var flag = defaultJson.doQueryJsonList(controllername+"?query",data,xmzszjqkList, null, true);
	if(flag){
		var data1 = combineQuery.getQueryCombineData(queryForm,frmPost);	
		var data2 = {
			msg : data1
		};	
		//计算使用和拨付笔数
		$.ajax({
			url : controllername+"?querySumJe",	
				data : data2,	
			cache : false,	
			async :	false,	
			dataType : "json",
			type : 'post',		
			success : function(response) {	
				$("#resultXML").val(response.msg);	
				var resultobj = defaultJson.dealResultJson(response.msg);	
				if(resultobj!=null&&resultobj.ZFJE!=null&&resultobj.ZFJE!=""){
					$("#xmzszjqkList").append('<tr><th align="center"><strong></strong></th><td align="center">合计</td><td align="center"></td><td align="center"></td><td align="center"></td><td align="center"></td><td align="right">'+resultobj.ZFJE_SV+'</td></tr>');
				}else{
					$("#xmzszjqkList").append('<tr><th align="center"><strong></strong></th><td align="center">合计</td><td align="center"></td><td align="center"></td><td align="center"></td><td align="center"></td><td align="right">-</td></tr>');
				}
			}
		});
	}
	
	if(qy!="" && type=="insert"){
		//根据jhsjid (qy)  查询项目信息
		//$("#QJHSJID").val(qy);
		$.ajax({
			url : controllername+"?queryCommon&opttype=xmbd&qc_jhsjid="+qy,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(response) {
				var resultmsgobj  = convertJson.string2json2(response.msg);
				if(resultmsgobj.GC_JH_SJ_ID==""){
					$("#JHSJID").val("");
				}else{
					$("#JHSJID").val(resultmsgobj.GC_JH_SJ_ID);
				}
				if(resultmsgobj.XMMC==""){
					$("#XMMC").val("");
				}else{
					$("#XMMC").val(resultmsgobj.XMMC);
				}
				if(resultmsgobj.BDMC==""){
					$("#BDMC").val("");
				}else{
					$("#BDMC").val(resultmsgobj.BDMC);
				}
			}
		});
		
	}
}


//点击行事件
function tr_click(obj){
	var t = $("#xmzszjqkList").getTableRows();
	 if(t.length<=0)
	 {	
		 $("#btnClear_Bins").attr("disabled", true);
		 return;
	 }
	 $("#btnClear_Bins").attr("disabled", false);
	 $("#xmzszjqkForm").setFormValues(obj);
	 $("#ID").val(obj.ID);
}

//选中项目名称弹出页
function selectXm(){
	$(window).manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjgl/xmcx.jsp","modal":"2"});
}
//弹出区域回调
getWinData = function(data, tID){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#BDMC").val(JSON.parse(data).BDMC);
	$("#JHSJID").val(JSON.parse(data).GC_JH_SJ_ID);
};

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
	$("#"+jbrID).val(userName);
}
function doBD(obj){
	if(obj.BDMC==''){
		return '<div style="text-align:center">—</div>';
	}else{
		return obj.BDMC;
	}
}
function closeNowCloseFunction(){
	return retMsg;
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD class="right-border bottom-border">
<%--				<INPUT type="text" class="span12" kind="text" id="currXmid" name="t.xmid" fieldname="t.xmid" value="" operation="="/>--%>
					<input type="hidden" id="QJHSJID" name="JHSJID"  fieldname="JHSJID" operation="="/>
			</TD>
        </TR>
         <tr>
        	<!-- <th width="5%" class="right-border bottom-border text-right">年度</th>
	        <td class="right-border bottom-border" width="10%">
<%--	            <select class="span12" type="date" fieldtype="year" fieldformat="yyyy" id="QZFRQ" name="ZFRQ" fieldname="ZFRQ" operation="=" kind="dic" src="T#GC_ZJGL_ZSZJ_ZFZSB: distinct TO_CHAR(zfrq, 'yyyy') as zfrq:TO_CHAR(zfrq, 'yyyy') AS x:SFYX='1' order by zfrq"></select>--%>
	            <select class="span12" type="date" fieldtype="year" fieldformat="yyyy" id="QZFRQ" name="ZFRQ" fieldname="ZFRQ" operation="=" kind="dic" src="XMNF"  defaultMemo="-年度-">
	        </td>
	         -->
	        <th width="5%" class="right-border bottom-border text-right">区域</th>
						<td width="10%" class="right-border bottom-border">
							<select  id="QQY"  class="span12"  name="QY" fieldname="QY"  operation="=" kind="dic" src="QY"  defaultMemo="-全部-">
						</td>
	        <th width="7%" class="right-border bottom-border text-right disabledTh">项目名称</th>
			<td class="right-border bottom-border" width="10%">
				<input class="span12" type="text" name = "QXMMC" fieldname = "gjs.XMMC" operation="like" >
			</td>
			<th width="5%" class="right-border bottom-border text-right disabledTh">标段名称</th>
			<td class="right-border bottom-border" width="10%">
				<input class="span12" type="text" name = "BDMC" fieldname = "gjs.BDMC" operation="like" >
			</td>
			<th width="5%" class="right-border bottom-border  text-right">时间</th>
			<td width="32%" class="right-border bottom-border">
				<input id="ZFRQB" class="span5" type="date" fieldtype="date" autocomplete="off" placeholder="" name="ZFRQB" check-type="maxlength" maxlength="10" fieldname="ZFRQB" fieldformat="yyyy-MM-dd" operation=">="/>
				-
				<input id="ZFRQE" class="span5" type="date" fieldtype="date" autocomplete="off" placeholder="" name="ZFRQE" check-type="maxlength" maxlength="10" fieldname="ZFRQE" fieldformat="yyyy-MM-dd" operation="<="/>
			</td>
			<td class="text-left bottom-border text-right">
	        	<button id="btnQuery" class="btn btn-link"  type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-search"></i>查询</button>
	        	<button id="btnClear" class="btn btn-link" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;"><i class="icon-trash"></i>清空</button>
	        </td>
		</tr>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="xmzszjqkList" width="100%" type="single" pageNum="5" nopromptmsg="true">
		<thead>
			<tr>
 			    <th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
              	<th fieldname="XMBH" colindex=2 tdalign="center" maxlength="36">&nbsp;项目编号&nbsp;</th>
				<th fieldname="XMMC" colindex=3 tdalign="left" maxlength="36">&nbsp;项目名称&nbsp;</th>
				<th fieldname="QY" colindex=4 tdalign="center" maxlength="36">&nbsp;区域&nbsp;</th>
				<th fieldname="BDMC" colindex=5 tdalign="left" maxlength="40" CustomFunction="doBD">&nbsp;标段名称&nbsp;</th>
				<th fieldname="ZFRQ" colindex=6 tdalign="center" maxlength="10">&nbsp;支付日期&nbsp;</th>
				<th fieldname="ZFJE" colindex=7 tdalign="right" maxlength="17">&nbsp;支付金额(元)&nbsp;</th>
             </tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
	</div>
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">项目征收资金情况
      	<span class="pull-right">
      		<button id="btnClear_Bins" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;" disabled>删除</button>
	  		<button id="btnSave" class="btn" type="button" style="font-family:SimYou,Microsoft YaHei;font-weight:bold;">保存</button>
      	</span>
      </h4>
     <form method="post" id="xmzszjqkForm"  >
      <table class="B-table" width="100%" >
      <input type="hidden" id="ID" fieldname="ID" name = "ID"/></TD>
	  	<input type="hidden" id="GC_TCJH_XMXDK_ID" fieldname="XMID" name = "XMID"/></TD>
	  	<input type="hidden" id="JHSJID" name="JHSJID"  fieldname="JHSJID" >
        <tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
       	 	<td class="bottom-border right-border" width="23%">
         		<input class="span12" style="width:85%" id="XMMC" type="text" placeholder="必填" check-type="required" fieldname="XMMC" name = "XMMC"  disabled />
          		<button class="btn btn-link"  type="button" onclick="queryProjectWithBD()"><i class="icon-edit"></i></button>
       	 	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
			<td width="15%" class="right-border bottom-border">
				<input id="BDMC" class="span12" name="BDMC" fieldname="BDMC" type="text" disabled/>
			</td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">区域</th>
			<td width="17%" class="right-border bottom-border" colspan="3">
				<select  id="QY"  class="span12"  style="width:40%;" name="QY" fieldname="QY"  operation="=" kind="dic" src="QY"  defaultMemo="-全部-">
			</td>
		</tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">支付金额</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFJE" class="span12" keep="true" placeholder="必填" check-type="required" check-type="maxlength" maxlength="17" value=0 style="width:70%;text-align:right;" name="ZFJE" fieldname="ZFJE" type="number" min="0"/>&nbsp;<b>(元)</b>
			</td>
			<th width="8%" class="right-border bottom-border text-right">支付日期</th>
			<td width="17%" class="right-border bottom-border">
				<input id="ZFRQ" class="span7" type="date" check-type="maxlength" maxlength="10" name="ZFRQ" fieldname="ZFRQ"/>
			</td>
		</tr>
		<!-- <tr>
			<th width="8%" class="right-border bottom-border text-right">联系人</th>
			<td width="17%" class="right-border bottom-border">
				<input id="LXR" class="span12" style="width:80%;" placeholder="必填" maxlength="36" name="LXR" fieldname="LXR" type="text" />
				<button class="btn btn-link"  type="button" id="blrBtn_LXR" value="LXR"><abbr title="点击选择联系人"><i class="icon-edit"></i></abbr></button>
			</td>
			<th width="8%" class="right-border bottom-border text-right">联系方式</th>
			<td width="17%" class="right-border bottom-border">
				<input id="LXFS" class="span12"  maxlength="40" name="LXFS" fieldname="LXFS" type="text" />
			</td>
		</tr> -->
        <tr>
	        <th width="8%" class="right-border bottom-border text-right">备注</th>
	        <td colspan="3" class="bottom-border right-border" >
	        	<textarea class="span12" rows="2" id="BZ" check-type="maxlength" maxlength="4000" fieldname="BZ" name="BZ"></textarea>
	        </td>
        </tr>
      </table>
      </form>
    </div>
   </div>
  </div>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "t.LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>