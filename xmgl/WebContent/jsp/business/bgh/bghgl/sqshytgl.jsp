<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
   var controllername= "${pageContext.request.contextPath }/banGongHuiWenTiController.do";
	//计算本页表格分页数
	function setPageHeight(){
		var getHeight=getDivStyleHeight();
		var height = getHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
	$(function() {
		init();
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
	 		     $(window).manhuaDialog({"title":"上会议题>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghwtgladd.jsp?bz=1","modal":"2"});
	            }); 
		//审核意见
	 	var xiugai = $("#shenhe");
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
			 		 $(window).manhuaDialog({"title":"审核意见","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/bghwtshedit.jsp","modal":"2"});
			 		}
			}); 
	 	//清空查询条件
	    var btn_clearQuery = $("#query_clear");
	    btn_clearQuery.click(function() 
		      {
			        $("#queryForm").clearFormResult();
			        getZT();
		      });
	  //议题排序
		var ytpx = $("#ytpx_btn");
		ytpx.click(function() 
	 			{
	 		     $(window).manhuaDialog({"title":"议题排序","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghwtpx.jsp","modal":"2"});
	            }); 
	    //导入办公会
	    $("#drbgh_btn").click(function() 
	       {
	    	 init();
	    	 if(0>=$("#DT1").getTableRows())
	    		 {
	    			 xInfoMsg('请先添加议题！',null);
    			 }else{
   					 $(window).manhuaDialog({"title":"维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/bghadd.jsp","modal":"2"});
	    		 }
	    		 });
	    //议题打印
    	$("#ytdy_btn").click(function() {
		 	$(window).manhuaDialog({"title":"议题打印","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/ytdy.jsp","modal":"1"});
	 		});
    	//议题修改
	 	var xiugai = $("#edityt");
	 	xiugai.click(function() 
 			{
 		      	var index1 =	$("#DT1").getSelectedRowIndex();
		 		if(index1<0){
		 			requireSelectedOneRow();
				}else{
		 		 $("#resultXML").val($("#DT1").getSelectedRow());
		 		 $(window).manhuaDialog({"title":"修改议题","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghwtgledit.jsp?isbumen=0","modal":"2"});
		 		}
			}); 
	});
 function init(){
	 getZT();
	 setPageHeight();
	 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
	 defaultJson.doQueryJsonList(controllername+"?querybanGongHuiWen",data,DT1,null,true);
 } 
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
	
	//默认状态
	function getZT(){
			$("#ZT").val('1');
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
		 if(zt=='0'){
			$("#xiugai").removeAttr("disabled"); 
		 }else{
			 $("#xiugai").attr("disabled",true);  
		 } 
	}
	function doRandering(obj){
		var showHtml = "";
		showHtml = "<a href='javascript:void(0)' title='信息卡' onclick='openBGH();'><i class='icon-file showXmxxkInfo' ></i></a>";
		return showHtml;
	}
	function openBGH(){
	    var index = $(event.target).closest("tr").index();
		$("#DT1").cancleSelected();
    	$("#DT1").setSelect(index);
		if($("#DT1").getSelectedRowIndex()==-1){
			requireSelectedOneRow();
			return;
		}else{
			$(window).manhuaDialog({"title":"会议中心","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/shytd.jsp","modal":"1"});
		}
	}
	function initfuyemian()
	{
		init();
		var fuyemian=$(window).manhuaDialog.getParentObj();
	    fuyemian.closeParent(); 
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <p></p>
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">
        <span class="pull-right"> 
        <button id="shenhe" class="btn"  type="button">审核意见</button> 
		<button id="weihu" class="btn"  type="button">新增议题</button>
		<button id="edityt" class="btn"  type="button">修改议题</button> 
		<button id="ytdy_btn" class="btn" type="button">议题打印</button>
		<button id="ytpx_btn" class="btn" type="button">议题排序</button>
		<button id="drbgh_btn" class="btn"  type="button">导入会议</button>
        </span>
      </h4>
     <form method="post"  id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			<INPUT id="num" type="text" class="span12" keep="true" kind="text" fieldname="rownum" value="1000" operation="<="/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
	          <th width="5%" class="right-border bottom-border text-right">部门</th>
	          <td width="8%" class="right-border bottom-border">
                 <select class="span12 department" id="FQBM" name = "FQBM" fieldname="FQBM"  defaultMemo="全部" operation="="  kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME ">
	              </select>
	          </td>
	          <th width="5%" class="right-border bottom-border text-right">类型</th>
	          <td width="10%" class=" right-border bottom-border">
	           <select class="span12" id="WTLX" name = "WTLX" fieldname="A.WTLX"  defaultMemo="全部" operation="="  kind="dic" src="LX">
              </select>
		      </td>
		        <th width="5%" class="right-border bottom-border text-right">状态</th>
	          <td width="10%" class=" right-border bottom-border">
	           <select class="span12" id="ZT" name = "ZT" fieldname="A.ZT"  noNullSelect ="true" operation="="  kind="dic" src="T#FS_DIC_TREE:DIC_CODE:DIC_VALUE:PARENT_ID='9990000000339'  AND DIC_CODE IN ('1','3')">
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
		    <th fieldname="WTBT" maxlength="15" >&nbsp;标题&nbsp;</th>
		    <th fieldname="WTLX">&nbsp;类型&nbsp;</th>
		     <th fieldname="ZT">&nbsp;状态&nbsp;</th>
		    <th fieldname="XWJJSJ" tdalign="center"  >&nbsp;希望解决时间&nbsp;</th>
		 	<th fieldname="XMMC" Customfunction="doXMmc"    maxlength="15" >&nbsp;项目名称&nbsp;</th>
		 	<th fieldname="BDMC" maxlength="15" Customfunction="doBdmc" >&nbsp;标段名称&nbsp;</th>
		 	<th fieldname="FQR">&nbsp;发起人&nbsp;</th>
		 	<th fieldname="FQSJ" tdalign="center">&nbsp;发起日期&nbsp;</th>
		 <!-- 	<th fieldname="XUHAO" tdalign="right"  >&nbsp;排序号&nbsp;</th> -->
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
         <input type="hidden" name="txtFilter" order="desc" fieldname="a.xuhao,a.lrsj"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
 	</FORM>
 </div>
</body>
</html>