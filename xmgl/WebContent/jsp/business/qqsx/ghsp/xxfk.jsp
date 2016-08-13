<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title></title>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/qqsx/ghsp/sxfjController.do";
  var sjwybh,jhsjid,bjsj;
	$(function() {
		setValue();
		var savebtn=$("#save");
		savebtn.click(function() {
				var data = Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				if($("#demoForm").validationButton()){
					defaultJson.doUpdateJson(controllername + "?feedbackQqsx&sjwybh="+sjwybh+"&jhsjid="+jhsjid, data1,null,"callback");
				}else{
			  		defaultJson.clearTxtXML();
				}
			});
	});
	//异步加载
	function callback(){
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.do_parent();
		$(window).manhuaDialog.close();
	}
	//一级页面值
	function setValue(){
		var fuyemian=$(window).manhuaDialog.getParentObj();
		var value= fuyemian.do_value();
		jhsjid=$(value).attr("JHSJID");
		sjwybh=$(value).attr("SJWYBH");
		bjsj=$(value).attr("BJSJ");
		getDate();
	}
      function getDate() {
		 var date;
     	 $.ajax({
 			url:controllername + "?getDate&sjwybh="+sjwybh,
 			data:"",
 			dataType:"json",
 			async:false,
 			success:function(result){
 				date = result.msg;
 			}
     	 });
     	 if(bjsj=="null"||bjsj==""){
     		var json_fz='{"GHSPBJSJ":\"'+date+'\","GHSPBJSJ_SV":\"'+date+'\"}';
    		var obj_fz=convertJson.string2json1(json_fz);
    		$("#demoForm").setFormValues(obj_fz);
     	 }else{
     		$("#GHSPBJSJ").val(bjsj);
     	 }
       };
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
      <h4 class="title">反馈信息
      <span class="pull-right">
          <button id="save" name="save" class="btn"  type="button">提交</button>
       </span></h4>
     <form method="post" id="demoForm"  >
      <table class="B-table" width="100%">
         <TR  style="display:none;">
         <!-- <TR> -->
	        <!-- <TD><input type="text" class="span12" kind="text"  fieldname="SJBH" id="SJBH" keep="true" ></TD> -->
        </TR>
        <tr>
          <th width="10%" class="right-border bottom-border text-right">办结时间</th>
          <td width="20%" class="bottom-border">
            <input class="span12 date"  type="date" placeholder="必填"  check-type="required" id="GHSPBJSJ" fieldname="GHSPBJSJ" fieldtype="date" name ="GHSPBJSJ" fieldformat="YYYY-MM-DD" /></td>
		  <td width="70%" class="bottom-border">
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
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>