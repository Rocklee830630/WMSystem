<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.message.comet.DwrService"%>

<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>


<title>实例页面-查询</title>

<script type="text/javascript" charset="utf-8">
 var oTable1;
  var controllername= "${pageContext.request.contextPath }/demo/DemoController.do";
  var window_width;
    function testt(){
    	//alert(DT1.outerHTML);
    }
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
    	$("#matchInfo").val(userName);
    	$("#matchInfo").attr("code",userId);

    }
    function queryd(){
    	var rows = $("tbody tr" ,$("#DT1"));
    	var tr_obj = rows.eq(0);
         var t = $("#DT1").getTableRows();
         var tr_height = $(tr_obj).height();
         var d = t*tr_height;
         if(d<200) d = 200;
         if(d>500) d = 500;
         window_width = window.screen.availWidth;//$("#allDiv").width()
       //  alert(window_width)
    	 $("#DT1").fixTable({
    			fixColumn: 4,//固定列数
    			width:window_width-60,//显示宽度
    			height:d//显示高度
    		});
    }
	$(function() {
		window_width = $(document.body).width();
		$("#DT1").width(window_width);
		//alert($(window).width()); //浏览器当前窗口可视区域宽度 
		//alert($(document).width());//浏览器当前窗口文档对象宽度 
		//alert($(document.body).width());//浏览器当前窗口文档body的高度 
		
		
		var btn = $("#w_open");
		btn.click(function() {
			$(window).manhuaDialog({"title":"示例查询页面","type":"text","content":"${pageContext.request.contextPath}/jsp/xmgl/xmcbk/bbxx.jsp","modal":"1"});
		});
		var btn = $("#goBtn");
		btn.click(function() {
			var actorCode = $("#matchInfo").attr("code");
			selectUserTree('single',actorCode,'setBlr') ;
			
		});
		var clean=$("#clean");
		clean.click(function(){
			$("#queryForm").clearFormResult();

		});
		var btn = $("#example1");
		btn.click(function() {	
			        //生成json串
					var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
					//调用ajax插入
					var success =  defaultJson.doQueryJsonList(controllername+"?querydemo",data,DT1,"queryd","false");
					
					//temp();

		});
		var btn = $("#print");
		btn.click(function() {
		 	  clearLockTable(DT1);

			var rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引

			$("#DT1").removeResult(rowindex);
			  modifyLockTable(DT1);
			
		//	 return;
			 var t = $("#DT1").getTableRows();
			 if(t<=0)
			 {
				 xAlert("提示信息","请至少查询出一条记录！",'3');
				 return;
			 }
			 printTabList("DT1");
			
	         
		});
		//自定义导出
		var btn = $("#example3");
		btn.click(function() {	
			 var t = $("#DT1").getTableRows();
			 if(t<=0)
			 {
				 xAlert("提示信息","请至少查询出一条记录！",'3');
				 return;
			 }
			printCustomTabList("DT1",controllername+"?toDsExcel");
			      //  window.open(controllername+"?toDcExcel");

		});
		
	});
	
	//修改
	$(function() {
		var btn = $("#example2");
		btn.click(function() {
			var indexarry = new Array();
			indexarry = $("#DT1").getChangeRows();
				if(indexarry == "")
			 {
				 xAlert("提示信息",'请至少修改一条记录','3');
				return
			 }
			//获取表格表头的数组,按照表格显示的顺序
			var tharrays = new Array();
			var comprisesData;
			tharrays = $("#DT1").getTableThArrays();
 			if(tharrays != null){
 				//组成json串,此串为修改行所属
 				 comprisesData = $("#DT1").comprisesData(indexarry,tharrays);
				//console.log("comprisesData:"+JSON.stringify(comprisesData));
				//调用ajax插入
				defaultJson.doUpdateBatchJson(controllername + "?updatebatchdemo", comprisesData,DT1,indexarry);
			}

			
		});
	});
	
	function tr_click(obj,tabListid){
		//$("#queryForm").setFormValues(obj);
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		var rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		var rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
	}
	
	
	

	function doTest(obj){
		var xmly = obj.XMLY;
		if(xmly==""){
		//	return "<i class=\"icon-remove\"></i>";
		}else{
		//	return "<i class=\"icon-download-alt\" onclick=\"alert('1')\"></i>";
		}
		
	}
	
	function temp(){
		var d = $("#DT1").find('td').eq(0);
		alert(d.length);
		 alert(d.style.height);
		var windowHeight = parseInt($(window).height());
		alert(windowHeight);
		alert($("#DT1").offset().top);
		//计算组织 pageNum = (浏览器高-固定头高)/每行高度
		var pageNum = Math.ceil((windowHeight-$("#DT1").offset().top)/25); 
		alert(pageNum);
		
		$("#DT1").attr("pageNum",pageNum);
	}
	
	$(document).ready(function(){
		showAutoComplete("matchInfo",controllername+"?returnauto","{\"dd\":\"s dfds\",\"aa\":\"sdfds\"}");
		
		
	});
    function pageTest(tableid){
    //	alert(tableid);
    }
    function getWinData(data){
    //	alert(data)
    }
    function closeParentCloseFunction(obj){
    //	alert(obj);
    }
    function successInfo () {
		xConfirm("信息提示","是否保存为新版本？");

    	$('.success-b-open').slideDown('fast').delay(2000).slideUp('fast');
    }
	
</script>      
</head>
<body>
<app:dialogs/>
  <p></p>
<div class="container-fluid">
  <div class="row-fluid">
<div class="alert alert-info success-b-open">
    	<button type="button" class="close" data-dismiss="alert">&times;</button>
  		<h4>查询结果</h4>
        这里还可以再加一些关于此次操作的一些提示或摘要等，如果没有可以不加！
</div>
       <div class="B-small-from-table-auto">
       <h4 class="title">示例查询界面<span class="pull-right"> 
           <button class="btn success-b" onClick="successInfo ()">操作成功按钮</button>
          <button id="w_open" class="btn"  type="button">全屏弹出样式</button>
          <button id="example2" class="btn"  type="button">批量修改</button>
          <button id="example3" class="btn"  type="button">自定义导出</button>
          
          <button id="print" class="btn btn-link" type="button"><i class="icon-print"></i>列表打印</button>
                          </span></h4>
     <form method="post" action="${pageContext.request.contextPath }/insertdemo.do" id="queryForm">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" ></TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
          <th width="8%" class="right-border bottom-border">项目名称</th>
          <td width="42%" class="right-border bottom-border"><input class="span12" type="text" placeholder="" name = "XMMC" fieldname = "XMMC" operation="like" logic = "and" ></td>
          <th width="8%" class="right-border bottom-border">项目年份</th>
          <td width="40%" class="bottom-border">
            <input class="span12" type="radio" placeholder="" defaultValue="2013" lineType="upline" name = "XMNF" id="XMNF"  kind="dic" src="XMNF" fieldname = "XMNF" operation="=" value="sdf">
            <!-- select class="span12" id="XMNF" name = "XMNF" fieldname="XMNF" kind="dic" src="XMNF" operation="=">

            </select -->
          </td>
          <td width="30%" class="text-left bottom-border">
           <button id="example1" class="btn btn-link"  type="button">查询</button>
           <button id="clean" class="btn btn-link"   type="button"><i class="icon-trash"></i>清空</button>
           
          </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">项目分类</th>
          <td width="42%" class="right-border bottom-border">
            <select class="span12" name = "XMFL"  fieldname = "XMFL" operation="="  kind="dic" src="T#fs_dic_tree:dic_code:dic_value:parent_id='1000000000004'">

            </select>
          </td>
          <th width="8%" class="right-border bottom-border">项目起止点</th>
          <td width="42%" colspan="2" class="bottom-border"><input class="span12" type="date" placeholder="" name = "XMQZD" fieldname = "XMQZD" operation=">=" fieldtype="date" fieldformat="YYYY-MM-DD"></td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">概算</th>
          <td width="42%" class="right-border bottom-border"><input class="span12" type="text" placeholder="" name = "GS" fieldname = "GS" operation="=" ></td>
          <th width="8%" class="right-border bottom-border">项目来源</th>
          <td width="42%" colspan="2" class="bottom-border"><input class="span12" type="text" placeholder="" name = "XMLY" fieldname = "XMLY" operation="="></td>
        </tr>
         <tr>
          <th width="8%" class="right-border bottom-border">测试</th>
          <td width="42%" class="right-border bottom-border" colspan=3>
          <input type="text" class="input-xlarge" name="matchInfo" id="matchInfo" placeholder="输入城市中文名、全拼、简拼、编码" autocomplete="off">
          <button class="btn" type="button" id="goBtn">Go!</button></td>
        </tr>
      </table>
      </form>
      <div style="height:5px;"> </div>

           <table width="1000" class="table-hover table-activeTd B-table" id="DT1"  editable="1" editable="0" pageNum="10"  pagingFunction="pageTest">
                <thead>
                    <tr>
                    <th  name="XH" id="_XH" rowspan="2" colindex=1>序号</th>
					<th fieldname="XMMC" style="width:100px" rowspan="2" maxlength="5" colindex=2 tdalign="center" noprint="true">项目名称</th>
					<th fieldname="XMNF" style="width:100px" rowspan="2" maxlength="5" colindex=3 tdalign="center" rowMerge="true">项目年份</th>
					<th fieldname="XMFL" style="width:100px" rowspan="2" colindex=4 type="dic" noNullSelect="true" src="T#fs_dic_tree:dic_code:dic_value:parent_id='1000000000004'">项目分类</th>
					<th fieldname="XMQZD" rowspan="2" colindex=5 type="date">项目起止点</th>
					<th  colspan="2">多表头1</th>
					<th  colspan="2">多表头2</th>
					<th fieldname="GS" rowspan="2" colindex=10 type="text">概算</th>
                    </tr>
                    <tr>
                    	<th fieldname="XMLY" colindex=6 type="dic" src="SF" CustomFunction="doTest" tdalign="center">项目来源</th>
						<th fieldname="JSBYS" colindex=7 type="text">项目必要性</th>
						<th fieldname="JSYY" colindex=8  >项目意义</th>
						<th fieldname="BZ" maxlength="5" colindex=9 type="text">备注</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            

           <div class="pagination">
		  
		</div>
		
</div>
</div>
    
</div>

  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="queryResult" id = "queryResult">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 </FORM>
 </div>
</body>
</html>