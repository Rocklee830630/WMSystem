<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
	<%
	request.setCharacterEncoding("utf-8");
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String department = user.getDepartment();
	String account=user.getAccount();
	String username = user.getName();
	%>
<script type="text/javascript" charset="utf-8">
   var controllername= "${pageContext.request.contextPath }/banGongHuiWenTiController.do";
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
	$(function() {
		
		setPageHeight();
		//查询
		var btn = $("#chaxun");
		btn.click(function()
				{  
					//生成json串
					var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
					//调用ajax插入
					defaultJson.doQueryJsonList(controllername+"?querybanGongHuiWen",data,DT1,null,false);
				});
		//新增
		var weihu = $("#weihu");
	 	weihu.click(function() 
	 			{
	 		     biaozhi=0;
	 		     $(window).manhuaDialog({"title":"新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghwtgladd.jsp","modal":"2"});
	            }); 
		//修改
	 	var xiugai = $("#xiugai");
	 	xiugai.click(function() 
	 			{
	 		      var index1 =	$("#DT1").getSelectedRowIndex();
			 		if(index1<0) 
					{
			 			requireSelectedOneRow();
					}
			 		else
			 		{
			 		 $("#resultXML").val($("#DT1").getSelectedRow());
			 		 $(window).manhuaDialog({"title":"修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghwtgledit.jsp","modal":"2"});
			 		}
			}); 
	 	//清空查询条件
	    var btn_clearQuery = $("#query_clear");
	    btn_clearQuery.click(function() 
	      {
	        $("#queryForm").clearFormResult();
	        getNd();
	      });
	    //导出
	    $("#btnExpExcel").click(function() 
	       {
	    	if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
	    	      printTabList("DT1","bghwt.xls","WTBT,ZT,ISJJ,JJSJ,FQR,FQSJ,XMMC,BDMC,SHYJ,BGHJL,BGHHF,DBCS","1,0"); 
	    	  }
	   	});
	    getNd();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?querybanGongHuiWen",data,DT1,null,true);
	});
function tianjiahang(data){
	var subresultmsgobj = defaultJson.dealResultJson(data);
	$("#DT1").insertResult(JSON.stringify(subresultmsgobj),DT1,1);
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(0);
	isedit(data);
    xSuccessMsg("操作成功","");
}
//子页面调用修改行
function xiugaihang(data)
{
	var index =	$("#DT1").getSelectedRowIndex();
	var subresultmsgobj = defaultJson.dealResultJson(data);
	var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,index);
	$("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,index);
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(index);
	isedit(data);
	xSuccessMsg("操作成功","");
}
//删除回调
function delhang()
{
	  var rowindex = $("#DT1").getSelectedRowIndex();
	  $("#DT1").removeResult(rowindex);
}
	//详细信息
	 function rowView(index){
	     var obj = $("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
	     var XMID = convertJson.string2json1(obj).XMID;
	     $(window).manhuaDialog(xmscUrl(XMID));//调用公共方法,根据项目编号查询
	 }
	
	//默认年度
	function getNd(){
			setDefaultNd("LRSJ");
	}
  //标段名称
  function doBdmc(obj){
	  var bd_name=obj.BDMC;
	  if(bd_name==null||bd_name==""){
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
  }
  //标段编号
    function doBdBH(obj){
	  var bd_name=obj.BDBH;
	  if(bd_name==null||bd_name==""){
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
  }
    //项目名称
    function doXMmc(obj){
  	  var xm_name=obj.XMMC;
  	  if(xm_name==null||xm_name==""){
  		  return '<div style="text-align:center">—</div>';
  	  }else{
  		  return xm_name;			  
  	  }
    }
    //项目编号
      function doXMBH(obj){
  	  var xm_name=obj.XMBH;
  	  if(xm_name==null||xm_name==""){
  		  return '<div style="text-align:center">—</div>';
  	  }else{
  		  return xm_name;			  
  	  }
    }
  //判断是否可以修改
  function isedit(data){
  	var resultmsgobj = convertJson.string2json1(data);
	var subresultmsgobj1 = resultmsgobj.response.data[0];
	var zt=$(subresultmsgobj1).attr("ZT");
	 if(zt=='0'){
		$("#xiugai").removeAttr("disabled"); 
	 }else{
		 $("#xiugai").attr("disabled",true);  
	 } 
  }
//获取行数据json串
	function tr_click(obj,tabListid)
	{
		var zt=obj.ZT;
		if(zt=='0') {
			$("#xiugai").removeAttr("disabled"); 
			$("#deleteBgh").removeAttr("disabled"); 
		} else if(zt=='1') {
			$("#xiugai").attr("disabled",true);
			$("#deleteBgh").removeAttr("disabled");
		} else {
			$("#xiugai").attr("disabled",true);
			$("#deleteBgh").attr("disabled",true);
		}
	}
	function doRandering(obj){
		var showHtml = "";
		showHtml = "<a href='javascript:void(0)' title='信息卡' onclick='openBGH("+obj.ZT+");'><i class='icon-file showXmxxkInfo' ></i></a>";
		return showHtml;
	}
	function openBGH(obj){
	    var index = $(event.target).closest("tr").index();
		$("#DT1").cancleSelected();
    	$("#DT1").setSelect(index);
		if($("#DT1").getSelectedRowIndex()==-1){
			requireSelectedOneRow();
			return;
		} else {
			$(window).manhuaDialog({"title":"会议中心","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/shytd.jsp","modal":"1"});
		}
	}
	
	function deleteBgh() {
		if($("#DT1").getSelectedRowIndex()==-1) {
			xAlert("提示信息",'请选择一条记录','3');
			return
	    }
		
		var data = Form2Json.formToJSON(queryForm);
		var data1 = defaultJson.packSaveJson(data);
		var line = $("#DT1").getSelectedRowJsonObj();
		/* alert(data);
		alert(JSON.stringify(data1));
		alert(JSON.stringify(line)); */
/* 		xConfirm("提示信息","是否确认删除！");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){ 
			defaultJson.doDeleteJson(controllername+"?deleteGongHuiWenTi",data1,null,"delhang"); 
		}); */
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <p></p>
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">部门会议中心
        <span class="pull-right"> 
         <app:oPerm url="/jsp/business/bgh/bghwtgladd.jsp">
        <button id="weihu" class="btn"  type="button">新增</button> 
         </app:oPerm>
          <app:oPerm url="/jsp/business/bgh/bghwtgledit.jsp">
		<button id="xiugai" class="btn"  type="button">修改</button>
		 </app:oPerm>
          <app:oPerm url="/jsp/business/bgh/deleteBgh.jsp">
		<!-- <button id="deleteBgh" class="btn" onclick="deleteBgh()" type="button">删除</button> -->
		 </app:oPerm>
		<!--   <button id="tjht_btn" class="btn" type="button">关联之前议题</button> -->
		 <button id="btnExpExcel" class="btn"  type="button">导出</button>
        </span>
      </h4>
     <form method="post"  id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display: none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			<INPUT id="num" type="text" class="span12" keep="true" kind="text" fieldname="rownum" value="1000" operation="<="/>
			<INPUT id="FQBM" type="text" class="span12" keep="true"  fieldname="a.FQBM" value="<%=department %>" operation="="/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
	          <th width="5%" class="right-border bottom-border text-right">年度</th>
	          <td width="8%" class="right-border bottom-border">
                 <select class="span12 year" id="LRSJ" name = "LRSJ" fieldname="to_char(a.LRSJ,'yyyy')"  defaultMemo="全部" operation="="  kind="dic" src="T#GC_BGH_WT:to_char(lrsj,'yyyy') :to_char(lrsj,'yyyy'):SFYX='1' group by to_char(lrsj,'yyyy') ">
	              </select>
	          </td>
	          <th width="5%" class="right-border bottom-border text-right" >第几次会议</th>
	          <td width="20%" class=" right-border bottom-border"> 
	          <select class="span12" id="HC" name = "HC" fieldname="C.HC"  defaultMemo="全部" operation="="  kind="dic" src="T#GC_BGH:HC:HC:SFYX='1'">
	           </select>
	          <th width="5%" class="right-border bottom-border text-right">状态</th>
	          <td width="10%" class=" right-border bottom-border">
	           <select class="span12" id="ZT" name = "ZT" fieldname="A.ZT"  defaultMemo="全部" operation="="  kind="dic" src="BGHZT">
              </select>
		      </td> 
	          <td  class="text-left bottom-border text-right">
	              <button id="chaxun" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
                  <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
             </td>
        </tr>
      </table>
      </form>
      <div style="height:5px;"> </div>
  <div class="overFlowX">                                
<table  width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="会议议题管理">
	<thead>
		<tr>
		    <th  name="XH" id="_XH"  >&nbsp;#&nbsp;</th>
		    <th fieldname="WTBT"  tdalign="center" CustomFunction="doRandering" >&nbsp;&nbsp;</th>
		    <th fieldname="WTBT"    maxlength="15" >&nbsp;标题&nbsp;</th>
		    <th fieldname="ZT"    >&nbsp;状态&nbsp;</th>
		    <th fieldname="ISJJ"   tdalign="center">&nbsp;解决&nbsp;</th>
		    <th fieldname="JJSJ"   tdalign="center"  >&nbsp;解决时间&nbsp;</th>
		    <th fieldname="FQR"     >&nbsp;发起人&nbsp;</th>
		    <th fieldname="FQSJ"     tdalign="center">&nbsp;发起日期&nbsp;</th>
		 	<th fieldname="XMMC"   Customfunction="doXMmc"    maxlength="15" >&nbsp;项目名称&nbsp;</th>
		 	<th fieldname="BDMC"   maxlength="15" Customfunction="doBdmc" >&nbsp;标段名称&nbsp;</th>
		 	<th fieldname="SHYJ"   maxlength="15"  >&nbsp;审核意见&nbsp;</th>
		 	<th fieldname="BGHJL"   maxlength="15"  >&nbsp;会议结论&nbsp;</th>
	 		<th fieldname="BGHHF"   maxlength="15"  >&nbsp;会议答复&nbsp;</th>
		 	<th fieldname="DBCS"   tdalign="right" >&nbsp;督办&nbsp;</th>
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
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" order="desc" fieldname="a.lrsj"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 	
 	<FORM name="deleteBghFrom" id="deleteBghFrom" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" order="desc" fieldname="a.fqsj,a.lrsj"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
</body>
</html>