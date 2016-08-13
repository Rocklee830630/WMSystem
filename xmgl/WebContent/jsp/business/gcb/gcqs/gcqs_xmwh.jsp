<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>工程洽商-维护</title>
<%
	String type=request.getParameter("type");
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gcqs/gcqsController.do";
var flag;
var type ="<%=type%>";
//页面初始化
$(function() {
	init();
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#gcqsForm").clearFormResult();

    });
  	//按钮绑定事件（保存）
    $("#btnSave").click(function() {
    	
    	if($("#gcqsForm").validationButton())
		{
    		//生成json串
    		var ywid = $("#ywid").val();
    		//addjson();
    		var data = Form2Json.formToJSON(gcqsForm);
    		var data1 = addjson(convertJson.string2json1(data),"YWID",ywid);
		   	$(window).manhuaDialog.setData(data1);
		   	$(window).manhuaDialog.sendData();
		   	$(window).manhuaDialog.close();
		}else{
			requireFormMsg();
		  	return;
		}
    });
  	
  	//按钮绑定事件（删除）
    $("#btnDel").click(function(){
		xConfirm("提示信息","是否确认删除！");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){  
			var data = Form2Json.formToJSON(gcqsForm);
			$(window).manhuaDialog.getParentObj().getWinData_del(data);
   			$(window).manhuaDialog.close(); 
		});
    });
	
});
//页面默认参数
function init(){
	if(type == "insert"){
		$("#btnDel").hide();
		$("#BLRID").val("<%=userid %>");
		$("#BLRXM").val("<%=username %>");
		$("#QSTCRQ").val("<%=sysdate %>");
		
	}else if(type == "update"){
		$("#btnDel").show();
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.document.getElementById("resultXML").value;
		var obj = convertJson.string2json1(rowValue);
		deleteFileData(obj.GC_GCGL_GCQS_ID,"",obj.SJBH,obj.YWLX);
		setFileData(obj.GC_GCGL_GCQS_ID,"",obj.SJBH,obj.YWLX,"0");
		queryFileData(obj.GC_GCGL_GCQS_ID,"",obj.SJBH,obj.YWLX);
		$("#gcqsForm").setFormValues(obj);
		
	}
}

//选中项目名称弹出页
function selectXm(){
	$(window).manhuaDialog({"title":"项目列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/common/xmcx_xmglgs.jsp?xmglgs="+<%=deptId%>,"modal":"2"});
}

//弹出区域回调
getWinData = function(data){
	$("#XMID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#BDID").val(JSON.parse(data).BDID);
	$("#BDMC").val(JSON.parse(data).BDMC);
	$("#SJDW").val(JSON.parse(data).SJDW_SV);
	$("#SJDW").attr("code",JSON.parse(data).SJDW);
	$("#JLDW").val(JSON.parse(data).JLDW_SV);
	$("#JLDW").attr("code",JSON.parse(data).JLDW);
	$("#SGDW").val(JSON.parse(data).SGDW_SV);
	
	$("#SJDWFZR").val(JSON.parse(data).FZR_SJDW);
	$("#SJDWFZRLXFS").val(JSON.parse(data).LXFS_SJDW);
	$("#SGDWXMZJ").val(JSON.parse(data).SGDWXMJL);
	$("#SGDWXMZJLXFS").val(JSON.parse(data).SGDWXMJLLXDH);
	$("#JLDWXMJL").val(JSON.parse(data).JLDWZJ);
	$("#JLDWXMJLLXFS").val(JSON.parse(data).JLDWZJLXDH);
	
	$("#XMGLGS").attr("code",JSON.parse(data).XMGLGS);
	$("#XMGLGS").val(JSON.parse(data).XMGLGS_SV);
	$("#JHSJID").val(JSON.parse(data).GC_JH_SJ_ID);
	$("#BH").val(JSON.parse(data).XMBH);
	
};

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">工程洽商信息
      	<span class="pull-right">
      		<button id="btnSave" class="btn" type="button">保存</button>
      		<button id="btnDel" class="btn" type="button">删除</button>
  		</span>
      </h4>
     <form method="post" id="gcqsForm"  >
      <table class="B-table" width="100%" id="gcqsList">
      <input type="hidden" id="GC_GCGL_GCQS_ID" fieldname="GC_GCGL_GCQS_ID" name = "GC_GCGL_GCQS_ID"/>
      <input type="hidden" id="XMID" fieldname="XMID" name = XMID/>
      <input type="hidden" id="BDID" fieldname="BDID" name = BDID/>
      <input type="hidden" id="BLRID" fieldname="BLRID" name = BLRID/>
      <input type="hidden" id="BLRXM" fieldname="BLRXM" name = BLRXM/>
      <input type="hidden" id="JHSJID" fieldname="JHSJID" name = JHSJID/>
      <tr>
          <th width="8%" class="right-border bottom-border text-right">洽商标题</th>
          <td class="right-border bottom-border" colspan="3">
          	<input class="span12" id="QSBT" type="text" placeholder="必填" check-type="required" fieldname="QSBT" name = "QSBT"  />
          </td>
          <th width="8%" class="right-border bottom-border text-right">业主代表</th>
       	  <td class="right-border bottom-border" width="15%">
         	<input class="span12" id="YZDB" type="text" fieldname="YZDB" name = "YZDB"  maxlength="50" />
       	  </td>
      </tr>
      <tr>
        <th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
        <td class="right-border bottom-border" width="31%">
        	<input class="span12" id="XMMC" type="text" placeholder="必填" check-type="required" fieldname="XMMC" name = "XMMC"  readonly />
        </td>
        <th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
        <td class="right-border bottom-border" width="30%">
        	<input class="span12" style="width:70%" id="BDMC" type="text" fieldname="BDMC" name = "BDMC"  readonly />
       		<button class="btn btn-link"  type="button" onclick="selectXm()"><i title="选择项目" class="icon-edit"></i></button>
       </td>
        <th width="8%" class="right-border bottom-border text-right disabledTh">项目管理公司</th>
       	<td class="right-border bottom-border" width="15%">
         	<input class="span12" id="XMGLGS" type="text" fieldname="XMGLGS" name = "XMGLGS"  readonly />
       	</td>
      </tr>
     <tr>
       <th width="8%" class="right-border bottom-border text-right disabledTh">设计单位</th>
       <td class="right-border bottom-border">
       	<input class="span12" id="SJDW" type="text" fieldname="SJDW" name = "SJDW"  readonly />
       </td>
       <th width="8%" class="right-border bottom-border text-right disabledTh">负责人</th>
       	<td class="right-border bottom-border">
         	<input class="span12" id="SJDWFZR" type="text" fieldname="SJDWFZR" name = "SJDWFZR"  readonly />
       	</td>
       	<th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
       	<td class="right-border bottom-border">
         	<input class="span12" id="SJDWFZRLXFS" type="text" fieldname="SJDWFZRLXFS" name = "SJDWFZRLXFS"  readonly />
       	</td>
     </tr>
     <tr>
       <th width="8%" class="right-border bottom-border text-right disabledTh">监理单位</th>
       <td class="right-border bottom-border" >
        	<input class="span12" id="JLDW" type="text" fieldname="JLDW" name = "JLDW"  readonly />
       </td>
       <th width="8%" class="right-border bottom-border text-right disabledTh">总监</th>
       <td class="right-border bottom-border" >
        	<input class="span12" id="JLDWXMJL" type="text" fieldname="JLDWXMJL" name = "JLDWXMJL"  readonly />
       </td>
       <th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
       <td class="right-border bottom-border" >
        	<input class="span12" id="JLDWXMJLLXFS" type="text" fieldname="JLDWXMJLLXFS" name = "JLDWXMJLLXFS"  readonly />
       </td>
     </tr>
     <tr>
       <th width="8%" class="right-border bottom-border text-right disabledTh">施工单位</th>
       <td class="right-border bottom-border">
        	<input class="span12" id="SGDW" type="text" fieldname="SGDW" name = "SGDW"  readonly />
       </td>
       <th width="8%" class="right-border bottom-border text-right disabledTh">项目经理</th>
       <td class="right-border bottom-border">
        	<input class="span12" id="SGDWXMZJ" type="text" fieldname="SGDWXMZJ" name = "SGDWXMZJ"  readonly />
       </td>
       <th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
       <td class="right-border bottom-border">
        	<input class="span12" id="SGDWXMZJLXFS" type="text" fieldname="SGDWXMZJLXFS" name = "SGDWXMZJLXFS"  readonly />
       </td>
     </tr>
     <tr>
       <th width="8%" class="right-border bottom-border text-right">洽商提出日期</th>
       <td class="right-border bottom-border">
       	<input class="span12 date" id="QSTCRQ" type="date" name="QSTCRQ" check-type="required" fieldname="QSTCRQ"/>
       </td>
       <th width="8%" class="right-border bottom-border text-right">估算造价</th>
       <td class="right-border bottom-border">
       	<input class="span12" style="width:70%;text-align:right" id="GSZJ" type="number" fieldname="GSZJ" name = "GSZJ" ><b>（万元）</b>
       </td>
       <th width="8%" class="right-border bottom-border text-right">是否涉及拆迁</th>
       <td class="right-border bottom-border">
       	<select class="span12 3characters" id="SFSJPQ" kind="dic" src="SF" check-type="required" noNullSelect ="true" fieldname="SFSJPQ" name="SFSJPQ">
	    </select>
       </td>
     </tr>
     <tr>
       <th width="8%" class="right-border bottom-border text-right">洽商原因</th>
       <td class="right-border bottom-border" colspan="6">
       	<textarea class="span12" id="JSNR" rows="4" name ="QSYY" placeholder="必填" check-type="required" fieldname="QSYY" maxlength="4000"></textarea>
       </td>
     </tr>
     <tr>
       <th width="8%" class="right-border bottom-border text-right">洽商内容</th>
       <td class="right-border bottom-border" colspan="6">
       	<textarea class="span12" id="JSNR" rows="4" name ="QSNR" placeholder="必填" check-type="required" fieldname="QSNR" maxlength="4000"></textarea>
       </td>
     </tr>
     <tr>
	   	<th width="8%" class="right-border bottom-border text-right">附件信息</th>
	   	<td colspan="6" class="bottom-border right-border">
		<div>
			<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0530">
				<i class="icon-plus"></i>
				<span>添加文件...</span>
			</span>
			<table role="presentation" class="table table-striped">
				<tbody fjlb="0530" class="files showFileTab"
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
	<FORM id="frmPost" name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="ywid" id = "ywid" value="">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>