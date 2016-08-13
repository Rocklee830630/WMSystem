<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>实例页面-数据维护</title>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/qianQiShouXuController.do";
	$(function() {
		      //清空
				var btn = $("#example1");
				btn.click(function() {
					$("#demoForm").clearFormResult();
			
				  });
				  //获取父页面的值
				    var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
			        var odd=convertJson.string2json1(rowValue);
				  //将数据放入表单
				  $("#demoForm").setFormValues(odd);
				/*   var bdmc1=$(odd).attr("BDMC");
				   if(bdmc1=='')
					  {
			    	   $("#BDMC").val('此材料交接只针对项目');
			           $("#BDMC").attr("style","color:red;");
			           $("#BDMC").removeAttr("fieldname");
					  } */
	});
	$(function() {
		         //保存
				var btn = $("#example2");
				btn.click(function() {
					var biaozhi=$('#GC_ZGB_QQSX_ID').val();
					 if(biaozhi==null||biaozhi=="null"||biaozhi=='')
						{
						 	if($("#demoForm").validationButton())
							{
				 				//生成json串
				 		 		var data = Form2Json.formToJSON(demoForm);
				 				//组成保存json串格式
				 				var data1 = defaultJson.packSaveJson(data);
				 				//调用ajax插入
				 				defaultJson.doInsertJson(controllername + "?insert", data1,null,'addHuiDiao');
							}
						}
					 else
						 {
				 		 	if($("#demoForm").validationButton())
							{
				 		 		//生成json串
				 		 		var data = Form2Json.formToJSON(demoForm);
				 				//组成保存json串格式
				 				var data1 = defaultJson.packSaveJson(data);
				 				//调用ajax插入
				 				defaultJson.doUpdateJson(controllername + "?update", data1,null,'eidtHuiDiao');
							}
		 		 	
						 }
				});
		
	});
	function xuanzheXiangMu()
	{
		$(window).manhuaDialog({"title":"下达库>查询","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/qqsx/xiangmu.jsp","modal":"2"});
	}
	
	//弹出窗口回调函数
	getWinData = function(data){
		     $("#demoForm").setFormValues(data);
	};
	function addHuiDiao()
	{
		var obj = $("#resultXML").val();	
		var fuyemian=$(window).manhuaDialog.getParentObj();
        fuyemian.xiugaiahang(obj);
        $(window).manhuaDialog.close();
	}
	function eidtHuiDiao()
	{
		var obj = $("#resultXML").val();	
		var fuyemian=$(window).manhuaDialog.getParentObj();
        fuyemian.xiugaiahang(obj);
        $(window).manhuaDialog.close();
	}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <p></p>
    <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">前期手续信息
          <span class="pull-right">
          <button id="example2" class="btn"  type="button">保存</button>
           <!-- <button id="example1" class="btn"  type="button">清空</button>  -->
          </span>
       </h4>
     <form method="post" id="demoForm"  >
      <table class="B-table" width="100%">
         <TR  style="display: none;">
	        <TD class="right-border bottom-border">  
	           <input type="text" class="span12" kind="text" id="XDKID" name = "XDKID"  fieldname="XDKID"  >
               <input type="text" class="span12" kind="text" id="GC_ZGB_QQSX_ID" name="GC_ZGB_QQSX_ID" fieldname="GC_ZGB_QQSX_ID"  >
                <input type="text" class="span12" kind="text" id="JHSJID" name = "JHSJID"  fieldname="JHSJID"  >
                 <input type="text" class="span12" kind="text" id="SJWYBH" name = "SJWYBH"  fieldname="SJWYBH"  >
               <input type="text" class="span12" kind="text" id="ND" name="ND" fieldname="ND"  >
            </TD>
			<TD class="right-border bottom-border">
        </TR>
        <tr>
	          <th  class="right-border bottom-border disabledTh">项目名称</th>
	          <td  colspan="3"  class="right-border bottom-border"><input class="span12 xmmc"  type="text" disabled check-type="required"  placeholder="请选择项目"     check-type="" fieldname="XMMC" id="XMMC" name = "XMMC"></td>
        </tr>
        <tr>
        	  <th  class="right-border bottom-border disabledTh">标段名称</th>
	          <td colspan="3" class="right-border bottom-border"><input class="span12"  type="text" disabled  check-type="" fieldname="BDMC" id="BDMC" name = "BDMC"></td>
        </tr>
        <tr>
              <th width="10%" class="right-border bottom-border">交办单位</th>
	          <td width="40%" class="bottom-border"><input class="span12"  type="text"    fieldname="JBDW" maxlength="100" name = "JBDW">
	          </td>
	          <th width="10%" class="right-border bottom-border">交接人</th>
	          <td width="40%" class="right-border bottom-border">
	           <input class="span12"  type="text" check-type="" fieldname="JER" maxlength="100" name = "JER">
	          </td>
        </tr>
        <tr>
	          <th width="10%" class="right-border bottom-border">交接时间</th>
	          <td width="40%"  class="bottom-border"><input class="span12 date" type="date" check-type="required" name = "JJSJ" fieldname= "JJSJ"></td>
             <td  class="bottom-border" colspan="2">
        </tr>
             <tr>
	          <th width="10%" class="right-border bottom-border">交接材料说明</th>
	          <td width="40%" colspan="3" class="right-border bottom-border">
	           <textarea class="span12" id="JJCLMX" rows="2" name = "JJCLMX" maxlength="1000" fieldname="JJCLMX"></textarea>
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
         <input type="hidden" name="txtFilter" order="desc" fieldname="A.XMBH,A.LRSJ"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
          <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>

</body>
</html>