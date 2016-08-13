<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title>规划审批进度维护</title>
<%-- <script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script> --%>
<script type="text/javascript" charset="utf-8">
  
  var aa,qqsxid,sfbj,bblsx;
  var bb=1;
  var rowValue,json;
  var controllername= "${pageContext.request.contextPath }/qqsx/tdsp/tdspController.do";
	$(function() {
		var querybtn = $("#query");
		var whbtn= $("#wh");
		var savebtn= $("#save");
		var clear=$("#clear");
		var clean=$("#clean");
		var update=$("#update");
		clean.click(function(){
			$("#queryForm").clearFormResult();
			init();
		});
		clear.click(function(){
			$("#GhspForm").clearFormResult();
			$("input[type='checkbox']").prop("checked", false);
		});
		querybtn.click(function() {
				//生成json串
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				//调用ajax插入
				defaultJson.doQueryJsonList(controllername+"?queryTdsp",data,DT1);
				});
		savebtn.click(function() {
				if (qqsxid==""||qqsxid==null) {
					requireSelectedOneRow();
					return;
				} else {
				if(aa==""||aa==null){
					var data = Form2Json.formToJSON(GhspForm);
					//组成保存json串格式
					var data1 = defaultJson.packSaveJson(data);
				//调用ajax插入
				defaultJson.doUpdateJson(controllername + "?insertSgxk", data1,DT1);
				var obj = $("#frmPost").find("#resultXML").val();
				var resultmsgobj = convertJson.string2json1(obj);
				var subresultmsgobj = resultmsgobj.data[0];
				$("#ID").val($(subresultmsgobj).attr("GC_QQSX_TTSP_ID"));

				//$("#DT1").updateResult(data,DT1,rowindex);
				}else{
					var data = Form2Json.formToJSON(GhspForm);
					//组成保存json串格式
					var data1 = defaultJson.packSaveJson(data);
					//调用ajax插入
					defaultJson.doUpdateJson(controllername + "?updateTdsp", data1,DT1);
					}
				}
				});
		whbtn.click(function(){
			if(qqsxid==null||qqsxid==""){
				requireSelectedOneRow();
				return;
			}else{	
				parent.$("body").manhuaDialog({"title":"土地审批手续>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/tdsp/tdsxwh.jsp?id="+aa,"modal":"2"});	
					}		
				});
		update.click(function(){
			if(qqsxid==null||qqsxid==""){
				requireSelectedOneRow();
				return;
			}else{	
				parent.$("body").manhuaDialog({"title":"土地审批信息>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/tdsp/tdxxwh.jsp?json="+json,"modal":"2"});	
					}		
				});
	});
    $(document).ready(function() {
    	init();
    	g_bAlertWhenNoResult=false;	
      	 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
   		//调用ajax插入
   		defaultJson.doQueryJsonList(controllername+"?queryTdsp",data,DT1);
   		g_bAlertWhenNoResult=true;
      });
    function tr_click(obj,tabListid){
    	rowValue = $("#DT1").getSelectedRowText();
		json=encodeURI(rowValue);
    	$("input[type='checkbox']").prop("checked", false);
    	/* $("input[name='BBL']").prop("checked", false); */
    	aa=$(obj).attr("GC_QQSX_TTSP_ID");
    	qqsxid=$(obj).attr("QQSXID");
		$("#GhspForm").setFormValues(obj);
		//为上传文件是需要的字段赋值
		var SJBH=$(obj).attr("SJBH");
		var YWLX=$(obj).attr("YWLX");
		setFileData(aa,"",SJBH,YWLX);
		//查询附件信息
		queryFileData(aa,"","","");
	  //将数据放入表单
    	/* $("[name=BBL]:checkbox").each(function(){ 
    		this.checked=!this.checked; 
    		});
    		var str="";
    		$("[name=BBL]:checkbox:checked").each(function(){ 
    			str+=$(this).val()+","; 
    		});
    	alert(str); */
		/* setValueByArr(obj,["GC_QQSX_GHSP_ID","XDKID","QQSXID","SFBJ","BJSJ","BBLYY","XMMC","XMBH","CZWT"]); */
    	/* alert(JSON.stringify(obj)); */
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		//var rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		//var rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		$("#resultXML").val(JSON.stringify(obj));
	}
    function init(){
    	var d=new Date();
    	var year=d.getFullYear();
    	$("#ND").val(year);
    };
    function deleteDate(){
    	$("#BJSJ").val('');
    };
	/* getWinData = function(data)
	{
		index =	$("#DT1").getSelectedRowIndex();
		alert(index);
		var subresultmsgobj = defaultJson.dealResultJson(data);
		var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,index);
			$("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,index);
	}; */
	function update(data)
	{
		 var index= $("#DT1").getSelectedRowIndex();
		 var subresultmsgobj1 = defaultJson.dealResultJson(data);
		 $("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
		 $("#DT1").setSelect(index);
		 rowValue = $("#DT1").getSelectedRowText();
		 json=encodeURI(rowValue);
	}
	function gengxinchaxun()
	{
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?queryTdsp",data,DT1);
	}
	//详细信息
	function rowView(index){
		var obj = $("#xdxmkList").getSelectedRowJsonByIndex(index);
		var xmbh = eval("("+obj+")").XMBH;
		parent.$("body").manhuaDialog(xmscUrl(xmbh));
	}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">土地审批手续
      	<span class="pull-right">
      		<button id="update" class="btn" type="button">修改</button>
      		<button id="wh" class="btn" type="button">手续维护</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true">
			</TD>
        </TR>
        <tr>
          <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="10%">
          	<select class="span12" id="ND" name = "ND" fieldname = "qqsx.ND" operation="=" kind="dic" src="XMNF">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
          <td class="left-border bottom-border" width="20%">
           <input class="span12" type="text" placeholder="" name = "XMMC" fieldname = "qqsx.XMMC" operation="like" maxlength="15">
          </td>
          <td width="30%" class="text-left bottom-border text-right">
           <button id="query" type="button" class="btn btn-link" ><i class="icon-search"></i>查询</button>
           <button id="clean" type="button" class="btn btn-link"><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
      </table>
      </form>
   <div style="height:5px;"> </div>
  <div class= "overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" >
		<thead>
 			<tr>
			<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;&nbsp;序号&nbsp;&nbsp;</th>
            <th fieldname="XMMC" rowspan="2" colindex=2 maxlength="15">&nbsp;&nbsp;项目名称&nbsp;&nbsp;</th>
            <th colspan="6">&nbsp;&nbsp;进展情况&nbsp;&nbsp;</th>
            <th fieldname="CZWT" rowspan="2" colindex=9 maxlength="15">&nbsp;&nbsp;存在问题&nbsp;&nbsp;</th>
            <th fieldname="BBLYY" rowspan="2" colindex=10 maxlength="15">&nbsp;&nbsp;不办理手续原因&nbsp;&nbsp;</th>
            <th fieldname="JBR" rowspan="2" colindex=11>&nbsp;&nbsp;经办人&nbsp;&nbsp;</th>
            <th fieldname="BBLSX" rowspan="2" colindex=12>&nbsp;&nbsp;不办理手续&nbsp;&nbsp;</th>
            <th fieldname="BBLFJ" rowspan="2" colindex=13>&nbsp;&nbsp;不办理手续&nbsp;&nbsp;</th>
            </tr>
            <tr>
            <th fieldname="YDYS" colindex=3>&nbsp;&nbsp;用地预审&nbsp;&nbsp;</th>
            <th fieldname="JTTDZD" colindex=4>&nbsp;&nbsp;集体土地征地&nbsp;&nbsp;</th>
            <th fieldname="GDSX" colindex=5>&nbsp;&nbsp;供地手续&nbsp;&nbsp;</th>
            <th fieldname="TDDJ" colindex=6>&nbsp;&nbsp;土地登记&nbsp;&nbsp;</th>
            <th fieldname="TDSYZ" colindex=7>&nbsp;&nbsp;土地使用证&nbsp;&nbsp;</th>
            <th fieldname="BJSJ" colindex=8 tdalign="center">&nbsp;&nbsp;办结时间&nbsp;&nbsp;</th>
			</tr>
		</thead>
	<tbody></tbody>
	</table>
	</div>
	</div>
</div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
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