<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>

<title>实例页面-查询</title>

<script type="text/javascript" charset="utf-8">
function closeNowCloseFunction(){
		return "2";
}
 var oTable1;
  var controllername= "${pageContext.request.contextPath }/demo/DemoController.do";
	$(function() {
		var btn = $("#example1");
		btn.click(function() {
			var ss = $(window).manhuaDialog.getParentObj();
			$(window).manhuaDialog.changeUrl("title","${pageContext.request.contextPath}/jsp/xmgl/xmcbk/weihucs.jsp");
			$(window).manhuaDialog.close();
			//parent.$("body").manhuaDialog({"title":"示例查询1页面","type":"text","content":"${pageContext.request.contextPath}/jsp/xmgl/xmcbk/weihucs.jsp","modal":"1"});
			        //生成json串
					var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
					//调用ajax插入
					defaultJson.doQueryJsonList(controllername+"?querydemo",data,DT1);
		});
		var btn = $("#print");
		btn.click(function() {
			 var t = $("#DT1").getTableRows();
			 if(t<=0)
			 {
				 xAlert("提示信息","请至少查询出一条记录！");
				// return;
			 }
			
			
		  	 $(window).manhuaDialog({"title":"列表导出","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/print/TabListEXP.jsp?tabId=DT1","modal":"3"});
	         
		});
		var btn = $("#test");
		btn.click(function() {
		//	$(window).manhuaDialog({"title":"示例查询页面","type":"text","content":"${pageContext.request.contextPath}/jsp/xmgl/xmcbk/weihucx_open.jsp","modal":"2"});

			var parentobj =$(window).manhuaDialog.getParentObj(); 
			$(window).manhuaDialog.setData('saaaaaaaaaaaaaaaaaaaa');
			$(window).manhuaDialog.sendData();
			$(window).manhuaDialog.close();


		});
		
		
	});
	
	function closeNowCloseFunction(){
		
		return '12345';
	}
	
	
	//修改
	$(function() {
		var btn = $("#example2");
		btn.click(function() {
			var indexarry = new Array();
			indexarry = $("#DT1").getChangeRows();
				if(indexarry == "")
			 {
				 xAlert("提示信息",'请至少修改一条记录');
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
		//alert(JSON.strinify(obj));
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		var rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		var rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
	}
	function weihucx_open(){
		alert('2222222222222222222');
	}

	
</script>      
</head>
<body>
<app:dialogs/>
  <p></p>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="B-small-from-table-auto">
            <h4>查询条件<span class="pull-right">
               <button id="example2" class="btn btn-inverse"  type="button">批量修改</button>
          <button id="print" class="btn btn-inverse"  type="button">列表打印</button> 
                    <button id="test" class="btn btn-inverse"  type="button">测试</button> 
          
                </span></h4>

     <form method="post" action="${pageContext.request.contextPath }/insertdemo.do" id="queryForm"  >
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
            <select class="span12" id="XMNF" name = "XMNF" fieldname="XMNF" kind="dic" src="XMNF" operation="=">

            </select>
          </td>
          <td width="30%" class="text-left bottom-border">
           <button id="example1" class="btn btn-link"  type="button">查询</button>
          </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">项目分类</th>
          <td width="42%" class="right-border bottom-border">
            <select class="span12" name = "XMFL" fieldname = "XMFL" operation="=" kind="dic" src="T#fs_dic_tree:dic_code:dic_value:parent_id='1000000000004'">

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
      </table>
      </form>
      <div style="height:5px;"> </div>
   
           <table width="100%" class="table-hover table-activeTd B-table" id="DT1" editable="0" >
                <thead>
                    <tr>
                    <th  name="XH" id="_XH" rowspan="2" colindex=1>序号</th>
					<th fieldname="XMMC" style="width:100px" rowspan="2" maxlength="5" colindex=2 align="center">项目名称</th>
					<th fieldname="XMNF" style="width:100px" rowspan="2" colindex=3 align="right">项目年份</th>
					<th fieldname="XMFL" style="width:100px" rowspan="2" colindex=4 type="dic" src="XMNF">项目分类</th>
					<th fieldname="XMQZD" rowspan="2" colindex=5 type="date">项目起止点</th>
					<th  colspan="2">多表头1</th>
					<th  colspan="2">多表头2</th>
					<th fieldname="GS" rowspan="2" colindex=10 type="text">概算</th>
                    </tr>
                    <tr>
                    	<th fieldname="XMLY" colindex=6 type="text">项目来源</th>
						<th fieldname="JSBYS" colindex=7 type="text">项目必要性</th>
						<th fieldname="JSYY" maxlength="5" colindex=8  maxlength="5">项目意义</th>
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