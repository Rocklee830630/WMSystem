<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>下达项目库-详细信息</title>
<%
	String id = request.getParameter("id");
%>

<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var id = '<%=id%>';
//页面初始化
$(function() {
	
	init();
	
});

//页面默认参数
function init(){
	var data = null;
	submit(controllername+"?queryById&id="+id,data,jcxxList);
}
function submit(actionName, data,tablistID){
	$.ajax({
		type : 'post',
		url : actionName,
		data : data,
		cache : false,
		dataType : "json",  
		async :	true,
		success : function(result) {
			var rowObj = convertJson.string2json1(result.msg).response.data[0];
			$("#jcxxForm").setFormValues(rowObj);
			$("#resultXML").val(JSON.stringify(rowObj));
			queryFileData(id,"","","");
		}
	});
}


//列表项<项目地址>加图标
function doDz(obj){
	var xmdz = obj.XMDZ;
	if(xmdz != ""){
		return '<a href="javascript:void(0);" onclick="selectDz()"><i title="查看" class="pull-right icon-map-marker"></a></i>';
	}
	
}

//点击项目地址图标
function selectDz(){
	window.open("${pageContext.request.contextPath }/jsp/business/jhb/xdxmk/img/earth.png");
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">项目信息</h4>
     <form method="post" id="jcxxForm">
      <table class="B-table" width="100%" id="jcxxList">
	  	<input type="hidden" id="GC_TCJH_XMXDK_ID" fieldname="GC_TCJH_XMXDK_ID" keep="true" name = "GC_TCJH_XMXDK_ID" value=""/></TD>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right disabledTh" >项目编号</th>
 	 		<td class="bottom-border right-border">
   				<input class="span12" id="XMBH"  type="text"  fieldname="XMBH" name = "XMBH" readonly>
 	 		</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
 	 		<td class="bottom-border right-border" colspan="3">
   				<input class="span12" id="XMMC"  type="text"  fieldname="XMMC" name = "XMMC" readonly>
 	 		</td>
 	 		
 			<th width="8%" class="right-border bottom-border text-right disabledTh">年度</th>
				<td class="bottom-border right-border">
    			<input class="span12" id="ND"  type="text" fieldname="ND" name = "ND" readonly />
  			</td>
        </tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">项目性质</th>
        	<td class="bottom-border right-border" width="17%">
          		<input class="span12 4characters" type="text" id="XJXJ"  fieldname="XJXJ" name="XJXJ" readonly>
          	</td>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">排序号</th>
			<td class="bottom-border right-border">
  				<input class="span12" id="PXH"  type="number" check-type="number" fieldname="PXH" style="text-align:right" name = "PXH" readonly>
  			</td>
          	<th width="8%" class="right-border bottom-border text-right disabledTh">项目类型</th>
            <td class="right-border bottom-border" width="17%">
	          	<input class="span12" type="text" id="XMLX" fieldname="XMLX" name="XMLX" readonly>
            </td>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">是否BT</th>
          	<td class="bottom-border right-border" width="17%">
	          	<input class="span12" type="text" id="ISBT"  fieldname="ISBT" name="ISBT" readonly>
         	</td>
         </tr>
         <tr id="tr_xjxdkxm">
        	<th width="8%" class="right-border bottom-border text-right disabledTh">续建项目</th>
          	<td class="bottom-border right-border" colspan="3">
	          	<input class="span12" type="text" id="XJ_XMID" fieldname="XJ_XMID" name="XJ_XMID" readonly>
         	</td>
         	<td class="bottom-border right-border" colspan="4">
         	</td>
        </tr>
         <tr>
        	<th width="8%" class="right-border bottom-border text-right disabledTh">项目地址</th>
          	<td class="bottom-border right-border" colspan="5">
	          	<input class="span12" style="width:85%" type="text" id="XMDZ"  fieldname="XMDZ" name="XMDZ" readonly>
	          	<button class="btn btn-link"  type="button" onclick="selectDz()"><i title="查看" class="icon-map-marker"></i></button>
         	</td>
         	<th width="8%" class="right-border bottom-border text-right disabledTh">稳定度</th>
        	<td class="bottom-border right-border" width="17%">
          		<input class="span12 4characters" type="text" id="WDD" fieldname="WDD" name="WDD" readonly>
          	</td>
        </tr>
		<tr>
			<th width="8%" class="right-border bottom-border text-right disabledTh">项目管理公司</th>
			<td class="bottom-border right-border">
				<input class="span12 4characters" type="text" id="XMGLGS" fieldname="XMGLGS" name="XMGLGS" readonly>
			</td>
			<th width="8%" class="right-border bottom-border text-right disabledTh">业主代表</th>
 	 		<td class="bottom-border right-border">
 	 			<input class="span12 person" type="text" id="YZDB" fieldname="YZDB" name="YZDB" readonly>
   				<!-- 
   				<input class="span12 person" id="YZDB" type="text"  fieldname="YZDB" name = "YZDB" />
   				<button class="btn btn-link"  type="button" onclick="openUserTree('single','','doCallback')"><i class="icon-edit"></i></button>
   				 -->
 	 		</td>
 	 		<td colspan="4"></td>
		</tr>
		<tr>
			<th class="right-border bottom-border text-left disabledTh"  colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;项目总体投资</th>
		</tr>
		<tr>
		  <th width="8%" class="right-border bottom-border text-right disabledTh">工程</th>
		  <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZTGC" type="number" onblur="countHj_ZT()" fieldname="ZTGC"  name = "ZTGC" min="0" disabled><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">征拆</th>
          <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZTZC" type="number" onblur="countHj_ZT()" fieldname="ZTZC"  name = "ZTZC" min="0" disabled><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right disabledTh">其他</th>
          <td class="bottom-border right-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZTQT" type="number" onblur="countHj_ZT()" fieldname="ZTQT"  name = "ZTQT" min="0" disabled><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">总投资额</th>
          <td class="right-border bottom-border" width="17%">
          <input class="span12" style="width:70%;text-align:right" id="ZTZTZE" type="number" fieldname="ZTZTZE" name = "ZTZTZE" readonly><b>（元）</b>
         </td>
		</tr>
		<tr>
			<th class="right-border bottom-border text-left disabledTh"  colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;项目年度投资</th>
		</tr>
        <tr>
         <th width="8%" class="right-border bottom-border text-right disabledTh">工程</th>
          <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="GC" type="number" onblur="countHj()" fieldname="GC"  name = "GC" min="0" disabled><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">征拆</th>
          <td class="right-border bottom-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="ZC" type="number" onblur="countHj()" fieldname="ZC"  name = "ZC" min="0" disabled><b>（元）</b>
         </td>
         <th width="8%" class="right-border bottom-border text-right disabledTh">其他</th>
          <td class="bottom-border right-border" width="17%">
          	<input class="span12" style="width:70%;text-align:right" id="QT" type="number" onblur="countHj()" fieldname="QT"  name = "QT" min="0" disabled><b>（元）</b>
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">总投资额</th>
	        <td class="right-border bottom-border" width="17%">
	          <input class="span12" style="width:70%;text-align:right" id="JHZTZE" type="number" fieldname="JHZTZE" name = "JHZTZE" readonly><b>（元）</b>
	        </td>
		</tr>
		<tr>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">年度建设目标</th>
	        <td colspan="7" class="bottom-border right-border" >
	        	<textarea class="span12" id="JSMB" rows="3" name ="JSMB"  fieldname="JSMB" maxlength="4000" disabled></textarea>
	        </td>
        </tr>
		<tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">建设任务</th>
          <td colspan="7" class="bottom-border right-border">
          	<textarea class="span12" id="JSRW" rows="3" name ="JSRW"  fieldname="JSRW" maxlength="4000" disabled></textarea>
          </td>
        </tr>
        <tr>
         <th width="8%" class="right-border bottom-border text-right disabledTh">建设内容及规模</th>
          <td colspan="7" class="bottom-border right-border">
          	<textarea class="span12" id="JSNR" rows="3" name ="JSNR"  fieldname="JSNR" maxlength="4000" disabled></textarea>
          </td>
        </tr>
        <tr>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">建设意义</th>
	        <td colspan="7" class="bottom-border right-border" >
	        	<textarea class="span12" id="JSYY" rows="3" name ="JSYY"  fieldname="JSYY" maxlength="4000" disabled></textarea>
	        </td>
        </tr>
        
        <tr>
	        <th width="8%" class="right-border bottom-border text-right disabledTh">备注</th>
	        <td colspan="7" class="bottom-border right-border" >
	        	<textarea class="span12" id="BZ" rows="3" name ="BZ"  fieldname="BZ" maxlength="4000" disabled></textarea>
	        </td>
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right disabledTh">附件信息</th>
        	<td colspan="7" class="bottom-border right-border">
				<div>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0035" class="files showFileTab" onlyView="true"
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
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "PXH,XMBH" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>

</html>