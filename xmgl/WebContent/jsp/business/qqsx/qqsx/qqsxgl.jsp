<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>作废--实例页面-数据维护</title>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/qianQiShouXuController.do";
  var tbl = null;
  var biaozhi;
	
	$(function() {
		      //新增
				var btn = $("#example1");
				btn.click(function() {
					$("#demoForm").clearFormResult();
			
				  });
		     //查询
				var chaxun = $("#chaxun");
				chaxun.click(function()
				  {
					
			 		 	if($("#queryForm").validationButton())
						{
			 				//生成json串
			 				//组成保存json串格式
			 				
			 				 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			 				//调用ajax插入
			 				
			 				defaultJson.doQueryJsonList(controllername + "?queryQainQiShouXu",data,DT1);
						}
				  });
				//清空
				   var btn_clearQuery = $("#query_clear");
				    btn_clearQuery.click(function()
				    {
				        $("#queryForm").clearFormResult();
				        //其他处理放在下面
				    });
			 //默认调用
				 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	 				//调用ajax插入
	 			 defaultJson.doQueryJsonList(controllername + "?queryQainQiShouXu",data,DT1); 
	});

	$(function() {
		         //保存
				var btn = $("#example2");
				btn.click(function() {
				   alert(biaozhi);
					 if(biaozhi==null||biaozhi=="null")
						{
						 	if($("#demoForm").validationButton())
							{
				 				//生成json串
				 		 		var data = Form2Json.formToJSON(demoForm);
				 				//组成保存json串格式
				 				var data1 = defaultJson.packSaveJson(data);
				 				var rowindex = $("#DT1").getSelectedRowIndex();
				 				//调用ajax插入
				 				defaultJson.doInsertJson(controllername + "?insert", data1,DT1);
				 				$("#DT1").updateResult(data,xdxmkList,rowindex);
							}
						}
					 else
						 {
				 		 	if($("#demoForm").validationButton())
							{
				 		 		//生成json串
				 		 		var data = Form2Json.formToJSON(demoForm);
				 				//组成保存json串格式
				 				//alert(data);
				 				var data1 = defaultJson.packSaveJson(data);
				 				//调用ajax插入
				 				defaultJson.doUpdateJson(controllername + "?update", data1,DT1);
							}
		 		 	
						 }
		 		
					
				});
	});
	function tr_click(obj,tabListid){
		
		$("#demoForm").setFormValues(obj);
		biaozhi=obj["GC_ZGB_QQSX_ID"];
		alert(biaozhi);
	}
	function xuanzheXiangMu()
	{
		parent.$("body").manhuaDialog({"title":"下达库>查询","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/qqsx/xiangmu.jsp","modal":"2"});
	}
	

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <p></p>

  <div class="row-fluid">
    <div class="B-small-from-table-box">
      <h4>信息查询
       <span class="pull-right"> 
         <button id="chaxun" class="btn btn-inverse"  type="button">查询</button>
         <button id="query_clear" class="btn btn-inverse" type="button">清空</button>
        </span>
      </h4>
         <form method="post" action="${pageContext.request.contextPath }/insertdemo.do" id="queryForm"  >
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" ></TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
	          <th width="5%" class="right-border bottom-border text-right">项目年份</th>
	          <td width="10%" class="right-border bottom-border">
	             <select class="span12" id="ND" name = "ND" fieldname="A.ND"  kind="dic" src="XMNF" operation="=" logic = "and" >
	             </select>
	          </td>
	          <th width="5%" class="right-border bottom-border text-right" >项目类型</th>
	          <td width="10%" class="bottom-border">
                  <select class="span12" id="XMLX" name = "XMLX"  fieldname="B.XMLX" kind="dic" src="XMLX" operation="=">
                  </select>
	          </td>
	          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
	          <td width="20%" class="bottom-border">
		          <input class="span12" type="text" placeholder="" name = "XMMC" fieldname="A.XMMC" operation="like" >
		      </td>
	          <td width="20%" class="bottom-border"></td>
        </tr>
      </table>
      </form>
    </div>
  </div>

 <div class="row-fluid">
 <div class="B-small-from-table-auto">
  <h4>查询结果</h4>
<div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
                <thead>
                <tr>
                    <th  name="XH" id="_XH" rowspan="3" colindex=1 >&nbsp;序号&nbsp;</th>
					<th fieldname="XMMC"   rowspan="3" maxlength="15" colindex=2>&nbsp;项目名称&nbsp;</th>
					<th fieldname="JBDW"  rowspan="3"  colindex=3>&nbsp;交办单位&nbsp;</th>
					<th fieldname="JER"  rowspan="3" tdalign="center"  colindex=4>&nbsp;交接人&nbsp;</th>
					<th fieldname="JJSJ"  rowspan="3" tdalign="center"  colindex=5>&nbsp;交接时间&nbsp;</th>
					<th fieldname="JJSJ"  rowspan="3" colindex=6>&nbsp;交接材料说明&nbsp;</th>
					<th  colspan="8" >&nbsp;手续进展情况&nbsp;</th>
				</tr>
				<tr>
					<th  colspan="2" >&nbsp;立项科研&nbsp;</th>
					<th  colspan="2" >&nbsp;土地&nbsp;</th>
					<th  colspan="2" >&nbsp;规划&nbsp;</th>
					<th  colspan="2" >&nbsp;施工许可&nbsp;</th>
				</tr>
					
				<tr>
					<th fieldname="LXSJ" tdalign="center"  colindex=7>&nbsp;立项科研证照&nbsp;</th>
					<th fieldname="LXSJ" tdalign="center"  colindex=8>&nbsp;立项科研时间&nbsp;</th>
					<th fieldname="TDSJ" tdalign="center"  colindex=9>&nbsp;土地证照&nbsp;</th>
					<th fieldname="TDSJ" tdalign="center"  colindex=10>&nbsp;土地时间&nbsp;</th>
					<th fieldname="GHSJ" tdalign="center"  colindex=11 >&nbsp;规划证照&nbsp;</th>
					<th fieldname="GHSJ" tdalign="center"  colindex=12>&nbsp;规划时间&nbsp;</th>
					<th fieldname="SGSJ" tdalign="center"  colindex=13>&nbsp;施工许可证照&nbsp;</th>
					<th fieldname="SGSJ" tdalign="center"  colindex=14>&nbsp;施工许可时间&nbsp;</th>
                 </tr>
                </thead>
             <tbody>
          </tbody>
      </table>
   </div>
</div>
</div>
    <div class="row-fluid">
    <div class="B-small-from-table-box">
      <h4>信息维护表单
          <span class="pull-right">
          <button id="example1" class="btn btn-inverse"  type="button">新 增</button> 
          <button id="example2" class="btn btn-inverse"  type="button">保存</button>
          </span>
       </h4>
     <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
      <table class="B-table">
         <TR  style="display:none;">
	        <TD class="right-border bottom-border">  
	           <input type="text" class="span12" kind="text" id="XDKID" name = "XDKID"  fieldname="XDKID"  >
               <input type="text" class="span12" kind="text" id="GC_ZGB_QQSX_ID" name="GC_ZGB_QQSX_ID" fieldname="GC_ZGB_QQSX_ID"  >
               <input type="text" class="span12" kind="text" id="JHID" name = "JHID"  fieldname="JHID"  >
               <input type="text" class="span12" kind="text" id="ND" name="ND" fieldname="ND"  >
               <input type="text" class="span12" kind="text" id="XMBH" name = "XMBH"  fieldname="XMBH"  >
            </TD>
			<TD class="right-border bottom-border">
        </TR>
        <tr>
	          <th width="10%" class="right-border bottom-border">项目名称</th>
	          <td width="40%" class="right-border bottom-border"><input class="span12"  type="text" disabled check-type="required"  placeholder="请选择项目"  onclick="xuanzheXiangMu()"    check-type="" fieldname="XMMC" id="XMMC" name = "XMMC"></td>
	          <th width="10%" class="right-border bottom-border">交办单位</th>
	          <td width="40%" class="bottom-border">
	           <input class="span12"  type="text"    fieldname="JBDW" maxlength="100" name = "JBDW">
	          </td>
        </tr>
        <tr>
	          <th width="10%" class="right-border bottom-border">交接人</th>
	          <td width="40%" class="right-border bottom-border">
	           <input class="span12"  type="text" check-type="" fieldname="JER" maxlength="100" name = "JER">
	          </td>
	          <th width="10%" class="right-border bottom-border">交接时间</th>
	          <td width="40%" colspan="2" class="bottom-border"><input class="span12" type="date"  name = "JJSJ" fieldname= "JJSJ"></td>
        </tr>
        
     

      </table>
      </form>
      			<div style="height: 550px;">
						<iframe
							src="${pageContext.request.contextPath}/jsp/file_upload/fileupload.jsp"
							id="menuiframe" width="100%" height="100%" frameborder="0"></iframe>
				</div>
      
      
    </div>
  </div>
</div>

  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" order="desc" fieldname="A.XMBH,A.LRSJ"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
 <script type="text/javascript">
	//弹出窗口回调函数
	getWinData = function(data){
		     $("#demoForm").setFormValues(data);
	};
</script>
</body>
</html>