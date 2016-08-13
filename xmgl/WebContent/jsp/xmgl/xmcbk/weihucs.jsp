<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>实例页面-数据维护</title>
<script type="text/javascript" charset="utf-8">
var controllername= "${pageContext.request.contextPath }/demo/DemoController.do";
  var tbl = null;
 
//新增
	$(function() {
		var btn = $("#example1");
		btn.click(function() {
			
 		 	if($("#demoForm").validationButton())
			{
 				//生成json串
 		 		var data = Form2Json.formToJSON(demoForm);
 				//组成保存json串格式
 				var data1 = defaultJson.packSaveJson(data);
 				//调用ajax插入
 				defaultJson.doInsertJson(controllername + "?insertdemo", data1,DT1);
			}

			
		});
		  btn = $("#print");
			btn.click(function() {
				 var t = $("#DT1").getTableRows();
				 if(t<=0)
				 {
					 xAlert("提示信息","请至少查询出一条记录！",'3');
					 return;
				 }
				 printTabList("DT1");
				
		       
			});
	});
//修改
	$(function() {
		var btn = $("#example2");
		btn.click(function() {
			 if($("#DT1").getSelectedRowIndex()==-1)
			 {
				 //xAlert("提示信息",'请选择一条记录');
				//return
			 }
 		 	if($("#demoForm").validationButton())
			{
 		 		//生成json串
 		 		var data = Form2Json.formToJSON(demoForm);
 				//组成保存json串格式
 				//alert(data);
 				var data1 = defaultJson.packSaveJson(data);
 				//调用ajax插入
 				defaultJson.doUpdateJson(controllername + "?updatedemo", data1,DT1);
			}
			
		});
	});
	//删除
	$(function() {
		var btn = $("#example3");
		btn.click(function() {
			 if($("#DT1").getSelectedRowIndex()==-1)
			 {
				xAlert("提示信息",'请选择一条记录','3');
				return
			 }
				xConfirm("提示信息","是否确认删除！");
				
				var dd = {id:1,name:2};//此处为入参
				$('#ConfirmYesButton').attr('click','');//.click( eval(function(){test(dd);}));
		});
				$('#ConfirmYesButton').one("click",function(){  
					alert('dddddd');
					doDeleteRow();
				}); 
		 
	});
	//查询
 	$(function() {
		var queryTable = $("#queryTable");
		queryTable.click(function() {
			        //生成json串
					var data = combineQuery.getQueryCombineData(queryForm,frmPost);
					//调用ajax插入
					defaultJson.doQueryJsonList(controllername+"?querydemo",data,DT1);
		});
	});
	
	function tr_click(obj,tabListid){
		//alert(tabListid);
		$("#demoForm").setFormValues(obj);
	//	  var next_tr = $("#"+tabListid).getTrObjByIndex(0);

		//    alert($(next_tr)[0].outerHTML);
		  // $(next_tr).find("td").eq(1).remove();
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		var rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		var rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
	}
	
    $(document).ready(function() {
    	
    });
    function doDeleteRow (obj){
        var data = $("#DT1").getSelectedRows();
        var data1 = defaultJson.packSaveJson(data);
        defaultJson.doDeleteJson(controllername+"?deletedemo",data1,DT1); 
    }
    function linkTest(index){
        //alert(obj.name);	
		alert("OK:"+index);
    }

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
 <div class="row-fluid">
 <div class="B-small-from-table-auto">
  <h4>示例数据采集页面<span class="pull-right">  
     <button id="example1" class="btn"  type="button">新 增</button> 
     <button id="example2" class="btn"  type="button">修 改</button>
     <button id="example3" class="btn"  type="button">删 除</button>
     <button id="print" class="btn"  type="button">列表打印</button> 
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
          <td width="35%" class="bottom-border">
            <select class="span12" id="XMNF" name = "XMNF" fieldname="XMNF" kind="dic" src="XMNF" operation="=">

            </select>
          </td>
          <td width="8%" class="text-left bottom-border">
           <button id="queryTable" class="btn btn-link"  type="button">查询</button>
          </td>
        </tr>
      </table>

     </form>
           <div style="height:5px;"> </div>
  <div class="LockingBox"> 
   <!--start 表头-->
                <div class="LockingBoxThead"> </div>
                <!--end 表头--> 
                <!--start 列头-->
                <div class="LockingBoxRow"> </div>
                <!--end 列头-->
            <table width="100%" class="table-hover table-activeTd B-table LockingTableRoot" id="DT1" type="single" editable="1">
                <thead>
                    <tr>
                    <th  name="XH" id="_XH" rowspan="2" colindex=1>序号</th>
					<th fieldname="XMMC" style="width:200px" rowspan="2" maxlength="5" colindex=2 align="center" hasLink="true" linkFunction="linkTest">项目名称</th>
					<th fieldname="XMNF" rowspan="2" colindex=3 rowMerge="true" type="dic" src="XMNF" defaultMemo="sdfdsf">项目年份</th>
					<th fieldname="XMFL" rowspan="2" colindex=4 >项目分类</th>
					<th fieldname="XMQZD" rowspan="2" colindex=5>项目起止点</th>
					<th  colspan="2">多表头1</th>
					<th  colspan="2">多表头2</th>
					<th fieldname="GS" rowspan="2" colindex=10>概算</th>
                    </tr>
                    <tr>
                    	<th fieldname="XMLY" colindex=6>项目类型</th>
						<th fieldname="JSBYS" colindex=7>项目必要性</th>
						<th fieldname="JSYY" maxlength="5" colindex=8>项目意义</th>
						<th fieldname="BZ" maxlength="5" colindex=9>备注</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            
        </div>
           <div style="height:5px;"> </div>

     <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
      <table class="B-table">
         <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><input type="text" class="span12" kind="text"  fieldname="ID"  ></TD>
        </TR>
        <tr>
          <th width="8%" class="right-border bottom-border">项目名称</th>

          <td width="42%" class="right-border bottom-border"><input class="span12"  type="text" placeholder="必填"  maxlength="10" check-type="required maxlength" fieldname="XMMC" name = "XMMC"></td>
          <th width="8%" class="right-border bottom-border">项目年份</th>
          <td width="42%" colspan="2" class="bottom-border">
          <input class="span12" type="checkbox" placeholder="" name = "XMNF"  kind="dic" src="XMNF" fieldname = "XMNF" >
            <!-- select class="span12" name = "XMNF" fieldname= "XMNF" kind="dic" src="XMNF">
            </select -->
          </td>

        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">项目分类</th>
          <td width="42%" class="right-border bottom-border">
            <select class="span12" name = "XMFL" fieldname= "XMFL" kind="dic" src="XMNF">
            </select>
          </td>
          <th width="8%" class="right-border bottom-border">项目起止点</th>
          <td width="42%" colspan="2" class="bottom-border"><input class="span12" type="date" placeholder="必填" check-type="required" name = "XMQZD" fieldname= "XMQZD"></td>

        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">概算</th>

          <td width="42%" class="right-border bottom-border"><input class="span12" type="number" check-type="number"  placeholder="" name = "GS" fieldname= "GS"></td>
          <th width="8%" class="right-border bottom-border">项目来源</th>

          <td width="42%" colspan="2" class="bottom-border"><input class="span12" type="text" placeholder="" name = "XMLY" fieldname= "XMLY"></td>

        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">建设必要性</th>

          <td width="92%" colspan="4" class="bottom-border"><textarea class="span12" rows="3" name = "JSBYS" fieldname= "JSBYS"></textarea></td>

        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">建设意义</th>

          <td width="92%" colspan="4" class="bottom-border"><textarea class="span12" rows="3" name = "JSYY" fieldname= "JSYY"></textarea></td>

        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border">备注</th>

          <td width="92%" colspan="4" class="bottom-border"><textarea class="span12" rows="3" name = "BZ" fieldname= "BZ"></textarea></td>

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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="queryResult" id = "queryResult">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>