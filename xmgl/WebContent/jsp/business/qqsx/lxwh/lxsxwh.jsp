<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title>规划审批进度维护</title>
<%
	String id=request.getParameter("id");
%>
<%-- <script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script> --%>
<script type="text/javascript" charset="utf-8">
  var a;
  var controllername= "${pageContext.request.contextPath }/lxsxfj/liXiangShouxuFjController.do";
  var id="<%=id%>";
	$(function() {
		var querybtn = $("#query");
		var newbtn= $("#new");
		var savebtn=   $("#save");
	    
		//查询
		querybtn.click(function() 
				{
					 //生成json串
					var data = combineQuery.getQueryCombineData(queryForm,frmPost,sxfjList); 
					//调用ajax插入
					defaultJson.doQueryJsonList(controllername+"?querySxfj",data,sxfjList); 
				});
		//保存
		savebtn.click(function() 
				{
					if($("#SxfjForm").validationButton())
					{
						var data = Form2Json.formToJSON(SxfjForm);
						/* var a=$(obj).attr("ID"); */
						//组成保存json串格式
						var data1 = defaultJson.packSaveJson(data);
						if(a==null||a=="null")
							{
			 				    //调用ajax插入
								defaultJson.doInsertJson(controllername + "?insertSxfj&ywid="+$("#ywid").val(), data1,sxfjList);
								//返回数据
								var fuyemian=parent.$("body").manhuaDialog.getParentObj();
								fuyemian.gengxinchaxun(id);
							}else
							{
								defaultJson.doUpdateJson(controllername + "?updateSxfj", data1,sxfjList);
								var fuyemian=parent.$("body").manhuaDialog.getParentObj();
								fuyemian.gengxinchaxun(id);
							}
					}
				});
		//新增
		newbtn.click(function()
				{
			      $("#SxfjForm").clearFormResult();
			      //a=null判断是添加 
			       a=null;
			       clearFileTab();
			       $("#ywid").val("");
		        });
		
		//清空
		 var btn_clearQuery = $("#query_clear");
		    btn_clearQuery.click(function()
		    	{
		           $("#queryForm").clearFormResult();
		        });
	});
	
	    $(document).ready(function() 
	       {
	    	   g_bAlertWhenNoResult=false;	
		        var data = combineQuery.getQueryCombineData(queryForm,frmPost,sxfjList);
		   		//调用ajax插入
		   		defaultJson.doQueryJsonList(controllername+"?querySxfj",data,sxfjList);
		   		g_bAlertWhenNoResult=true;
	      });
    
    function tr_click(obj,tabListid)
    {
    	a=$(obj).attr("GC_QQSX_SXFJ_ID");
    	var SJBH=$(obj).attr("SJBH");
		var YWLX=$(obj).attr("YWLX");
		var x= Number(obj.FJLX);
		switch(x){
		case 0:
			$("#shangchuanID").attr("fjlb","2020");
			$("#shangchuanID1").attr("fjlb","2020");
			break;
		case 1:
			$("#shangchuanID").attr("fjlb","2021");
			$("#shangchuanID1").attr("fjlb","2021");
			break;
		case 2:
			$("#shangchuanID").attr("fjlb","2022");
			$("#shangchuanID1").attr("fjlb","2022");
			break;
		case 3:
			$("#shangchuanID").attr("fjlb","2023");
			$("#shangchuanID1").attr("fjlb","2023");
			break;
		case 4:
			$("#shangchuanID").attr("fjlb","2024");
			$("#shangchuanID1").attr("fjlb","2024");
			break;
		default:
			break;
		}
		setFileData(id,a,SJBH,YWLX);
    	queryFileData("",a,"","");
	    //将数据放入表单
		$("#SxfjForm").setFormValues(obj);
	}

    function shangchuanbianhao(aa)
    {
    	var xiala = $("#FJLX1");
    	xiala.find("option").each(function() {
    			if(this.selected)
    			{
    				$('#shangchuanID').attr("fjlb","202"+this.value);
    				$('#shangchuanID1').attr("fjlb","202"+this.value);
    			}
    	});
    }
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <p></p>
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD>
				<INPUT type="text" class="span12" kind="text" keep="true" fieldname="rownum"  value="1000" operation="<=" >
				<INPUT type="text" class="span12" kind="text" keep="true" fieldname="YWBID"  value="<%=id%>" operation="=" >
			</TD>
        </TR>
        <tr>
	          <th width="5%" class="right-border bottom-border text-right">选择手续</th>
	          <td class="right-border bottom-border" width="18%">
	          	<select class="span12" id="FJLX" name = "FJLX" defaultMemo="全部" fieldname = "FJLX" operation="=" kind="dic" src="LXKYFJLX">
	            </select>
	          </td>
	          <td  class="text-left bottom-border text-right">
           <button id="query" class="btn btn-link"  type="button" ><i class="icon-search"></i>查询</button>
           <button id="query_clear" class="btn btn-link" type="button" ><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="sxfjList" width="100%" pageNum=5  type="single">
		<thead>
			<tr>
				<th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
	            <th fieldname="WHMC" maxlength="15">&nbsp;文号名称&nbsp;</th>
	            <th fieldname="FJLX">&nbsp;手续类型&nbsp;</th>
	            <th fieldname="BLR" tdalign="center">&nbsp;办理人&nbsp;</th>
	            <th fieldname="BLSJ" tdalign="center">&nbsp;办理时间&nbsp;</th>
			</tr>
		</thead>
	<tbody></tbody>
	</table>
	</div>
</div>
    <div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">立项可研手续信息维护
      <span class="pull-right">
          <button id="save" name="save" class="btn"  type="button">保存</button>
            <button id="new" name="new" class="btn"  type="button">清空</button>
      </span></h4>
     <form method="post" id="SxfjForm"  >
      <table class="B-table" width="100%">
         <TR  style="display:none;">
         <TD>
           <input type="text" id="ywid"/>
	        <TD> <input type="text" class="span12" kind="text"  fieldname="GC_QQSX_SXFJ_ID"  ></TD>
			<TD><input type="text" class="span12" keep="true" kind="text"  fieldname="YWBID" value="<%=id %>"  ></TD>
		 </TD>	
        </TR>
        <tr>
	          <th width="8%" class="right-border bottom-border text-right">选择手续</th>
	          <td class="right-border bottom-border" width="42%">
		          <select class="span12" id="FJLX1" name = "FJLX1"  onchange="shangchuanbianhao(this)" check-type="required" fieldname = "FJLX"  kind="dic" src="LXKYFJLX">
		          </select>
	          </td>
	          <th width="8%" class="right-border bottom-border text-right">文号名称</th>
	          <td width="42%"  class=" bottom-border">
	          	  <input class="span12"  type="text" placeholder="必填"  check-type="required" maxlength="200"  fieldname="WHMC" name = "WHMC"></td>
        </tr>
        <tr>
	          <th width="8%" class="right-border bottom-border text-right">办理人</th>
	          <td width="42%"  class="right-border bottom-border">
	          	<input class="span12"  type="text" placeholder="必填" check-type="required" maxlength="20"  check-type="" fieldname="BLR" name = "BLR"></td>
	          <th width="8%" class="right-border bottom-border text-right">办理时间</th>
	          <td width="42%" class="bottom-border">
	            <input class="span12"  type="date" placeholder="必填"  check-type="required" check-type="" fieldname="BLSJ" name = "BLSJ"></td>
        </tr>
         <tr>
			<th width="8%" class="right-border bottom-border text-right">附件上传</th>
			<td colspan="5" class="bottom-border right-border">
				<div>
				<span class="btn btn-fileUpload" onclick="doSelectFile(this);" id="shangchuanID" fjlb="2000">
					<i class="icon-plus"></i>
						<span>添加文件...</span></span>
							<table role="presentation" class="table table-striped">
								<tbody fjlb="2000" id="shangchuanID1" class="files showFileTab"
									data-toggle="modal-gallery" data-target="#modal-gallery">
								</tbody>
							 </table>
				</div>
			  </td>
			</tr>
      </table>
      </form> 
    </div>
  </div>
</div>
  <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>

  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="resultXML" id = "resultXML">
           <input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ"/>
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
          <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
</body>
</html>