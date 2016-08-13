<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
   var controllername= "${pageContext.request.contextPath }/ndxxgxController.do";
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(3)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
	$(function() {
		setPageHeight();
		//查询
		var btn = $("#chaxun");
		btn.click(function(){  
			init();
		});
		//新增
		var weihu = $("#weihu");
	 	weihu.click(function(){
		     $(window).manhuaDialog({"title":"年度信息共享>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/ndxxgx/ndxxgxwh.jsp","modal":"2"});
           }); 
	 	//修改
	 	var xiugai = $("#xiugai");
	 	xiugai.click(function(){
	      	var index1 = $("#DT1").getSelectedRowIndex();
	 		if(index1<0){
	 			requireSelectedOneRow();
			}else{
	 		 $("#resultXML").val($("#DT1").getSelectedRow());
	 		var obj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
			var id = obj.XTBG_XXZX_NDXXGX_ID;
	 		 $(window).manhuaDialog({"title":"年度信息共享>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/xtbg/ndxxgx/ndxxgxwh.jsp?id="+id,"modal":"2"});
	 		}
		}); 
	 	var deleteBut=$("#deleteBtn");
 		deleteBut.click(function(){
 			var index1 = $("#DT1").getSelectedRowIndex();
	 		if(index1<0){
	 			requireSelectedOneRow();
			}else{
			 	if($("#DT1").getSelectedRowIndex()==-1) {
					xAlert("提示信息",'请选择一条要操作的数据！','3');
				}else{
					xConfirm("提示信息","是否确认删除！");
					$('#ConfirmYesButton').unbind();
				 	$('#ConfirmYesButton').one("click",function(obj) {
						var data1 = $("#DT1").getSelectedRow();
						data1 = "{\"response\":{\"data\":[" + data1 + "]}}}";
						//return JSON.parse(data1);
						var data1 = {
							msg : data1
						}
						defaultJson.doUpdateJson(controllername + "?deleteNdxxgx", data1, null);
						 $("#DT1").removeResult(index1);
					});
				}
			}
		});
	 	//清空查询条件
	    var btn_clearQuery = $("#query_clear");
	    btn_clearQuery.click(function(){
	        $("#queryForm").clearFormResult();
	      });
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryNdxxgx",data,DT1,null,true);
	});
	//子页面调用修改行
	function xiugaihang(data){
		var index =	$("#DT1").getSelectedRowIndex();
		init();
		$("#DT1").setSelect(index);
		xSuccessMsg("操作成功","");
	}
	//子页面调用添加行
	function tianjiahang(data){
		init();
	}
	function init(){
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryNdxxgx",data,DT1,null,false);
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
  <p></p>
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">年度信息共享
        <span class="pull-right"> 
        <button id="weihu" class="btn"  type="button">新增</button> 
		<button id="xiugai" class="btn"  type="button">修改</button>
		<button id="deleteBtn" class="btn"  type="button" >删除</button>
        </span>
      </h4>
     <form method="post" action="${pageContext.request.contextPath }/insertdemo.do" id="queryForm"  >
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
	         <th width="5%" class="right-border bottom-border">标题</th>
				<td width="25%" class="right-border bottom-border">
					<input class="span12" type="text" id="NDXXBT" name="NDXXBT" fieldname="NDXXBT" operation="like" logic="and" >
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
<table  width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="结算管理">
	<thead>
		<tr>
		   	<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
			<th fieldname="NDXXBT"  maxlength="50">&nbsp;标题&nbsp;</th>
			<th fieldname="FBSJ"  tdalign="center">&nbsp;发布时间&nbsp;</th>
			<th fieldname="FBR" >&nbsp;发布人&nbsp;</th>
			<th fieldname="ISFB"  tdalign="center">&nbsp;是否发布&nbsp;</th>
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
         <input type="hidden" name="txtFilter" order="asc" fieldname="LRSJ"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
</body>
</html>