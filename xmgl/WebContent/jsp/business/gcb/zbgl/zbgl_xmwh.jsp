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
<title>工程周报-维护</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gczb/gczbController.do";
var obj;
//页面初始化
$(function() {
	var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#resultXML").val();
	obj=convertJson.string2json1(rowValue);
	$("#gczbForm").setFormValues(obj);
	 $("#gczbForm").clearFormResult();
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#gczbForm").clearFormResult();

    });
  	//按钮绑定事件（保存）
    $("#btnSave").click(function() {
    	
    	if($("#gczbForm").validationButton())
		{
    		var KSSJ = $("#KSSJ").val();
    		var JSSJ = $("#JSSJ").val();
    		if(KSSJ > JSSJ){
    			xInfoMsg('结束时间不能早于开始时间！');
    			return;
    		}
    		switch(ksjs_sj(KSSJ,JSSJ,obj.GC_JH_SJ_ID))
    		{
    		case 'flag1':
				xInfoMsg('开始时间重叠请重新录入！');
				return;
				break;
    		case 'flag2':
				xInfoMsg('结束时间重叠请重新录入！');
				return;
				break;
    		case 'flag3':
				xInfoMsg('时间区间重叠请重新录入！');
				return;
				break;
    		}
    		//生成json串
			$("#XDKID").val(obj.GC_TCJH_XMXDK_ID);
			//$("#HTID").val(obj.ID);
			$("#JHSJID").val(obj.GC_JH_SJ_ID);
    		var data = Form2Json.formToJSON(gczbForm);
    		var data1=defaultJson.packSaveJson(data);
    		success=defaultJson.doInsertJson(controllername + "?insert&ywid="+$("#ywid").val(), data1,null,"insert");
 		}else{
			requireFormMsg();
		  	return;
		}
    });
	
});


//校验时间是否重复
function ksjs_sj(kssj,jssj,jhsjid)
{
	var isok='';
	var actionName=controllername+"?query_ksjs_sj&kssj="+kssj+"&jssj="+jssj+"&jhsjid="+jhsjid;
	$.ajax({
			url : actionName,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(result) {
			isok=result.msg;
		} 
	});
    return isok;
}


//插入回调函数
function insert()
{
	var parentmain=$(window).manhuaDialog.getParentObj();	
	parentmain.gs_feedback();    		
	$(window).manhuaDialog.close();	
}


//选中项目名称弹出页
function selectXm(){
	$("body").manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"1"});
}


//弹出区域回调
getWinData = function(data){
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#XMBH").val(JSON.parse(data).XMBH);
	$("#BDBH").val(JSON.parse(data).BDBH);
	$("#BDMC").val(JSON.parse(data).BDMC);
	$("#BDMC").val(JSON.parse(data).BDMC);
	$("#QZD").val(JSON.parse(data).XMDZ);
	$("#YZDB").val(JSON.parse(data).YZDB);
	$("#SGDW").val(JSON.parse(data).SGDW);
	$("#JLDW").val(JSON.parse(data).JLDW);
};
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
	    	<h4 class="title">工程周报信息
		      	<span class="pull-right">
		      		<button id="btnSave" class="btn" type="button">保存</button>
		  		</span>
			</h4>
	     	<form method="post" id="gczbForm"  >
				<table class="B-table" width="100%">
					<input type="hidden" id="HTID" fieldname="HTID" name="HTID" keep="true"/>
					<input type="hidden" id="JHSJID" fieldname="JHSJID" name="JHSJID" keep="true"/>
					<input type="hidden" id="BDID" fieldname="BDID" name="BDID" keep="true"/>
					<input type="hidden" id="BDBH" fieldname="BDBH" name="BDBH" keep="true"/>
					<input type="hidden" id="XDKID" fieldname="XDKID" name="XDKID" keep="true"/>
					<input type="hidden" id="XMBH" fieldname="XMBH" name="XMBH" keep="true"/>
			        <input type="hidden" id="SJBH" fieldname="SJBH" name="SJBH" keep="true"/>
					<input type="hidden" id="XMGLGS" fieldname="XMGLGS" name="XMGLGS" keep="true"/>
					<input type="hidden" id="ywid" fieldname="ywid" name="ywid"/>
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
						<td width="42%" colspan="2" class="right-border bottom-border">
							<input class="span12" id="XMMC" type="text" placeholder="必填" check-type="required" keep="true" fieldname="XMMC" name = "XMMC"  readonly />
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
						<td width="42%" colspan="2" class="bottom-border right-border">
							<input class="span12" id="BDMC" type="text" fieldname="BDMC" keep="true" name ="BDMC" readonly />
						</td>
						</tr>
	        		<tr>
						<th class="right-border bottom-border text-right">开始时间</th>
						<td class="right-border bottom-border" colspan="2">
							<input class="span12 date" id="KSSJ" type="date" check-type="required" name="KSSJ" fieldname="KSSJ"/>
						</td>
						<th class="right-border bottom-border text-right">结束时间</th>
						<td class="bottom-border right-border" colspan="2">
							<input class="span12 date" id="JSSJ" type="date" check-type="required" name="JSSJ" fieldname="JSSJ"/>
						</td>
	        		</tr>
	        	</table>	
	      		<h4 id="zgxx" class="title">工程形象进度</h4>
				<table id="hfbd" class="B-table" width="100%">
	        		<tr>
	        			<th width="8%" class="right-border bottom-border text-right"">本周计划</th>
	        			<td  class="right-border bottom-border" colspan="2">
	          				<textarea class="span12" rows="4" name ="BZJH"  fieldname="BZJH"></textarea>
	          			</td>
		        		<th width="8%" class="right-border bottom-border text-right"">本周完成</th>
		        		<td class="right-border bottom-border" colspan="2">
	          				<textarea class="span12" rows="4" name ="BZWC"  fieldname="BZWC"></textarea>
	          			</td>
	       	 		</tr>
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right"">本年完成</th>
				        <td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" rows="4" name ="BNWC"  fieldname="BNWC"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">累计完成</th>
			          	<td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" rows="4" name ="LJWC"  fieldname="LJWC"></textarea>
			          	</td>
			        </tr>
			        <tr>
			        	<th class="right-border bottom-border text-right"">下周计划</th>
			          	<td class="bottom-border right-border" colspan="5">
			          		<textarea class="span12" rows="4" name ="XZJH"  fieldname="XZJH"></textarea>
			          	</td>
			        </tr>
				</table> 
	      		<h4 id="zgxx" class="title">排迁形象进度</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right"">管线名称</th>
			        	<td class="right-border bottom-border" width="23%">
			          		<textarea class="span12" rows="4" name ="GXMC"  fieldname="GXMC"  check-type="maxlength" maxlength="100"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">完成时限</th>
				        <td class="right-border bottom-border"  width="23%">
			          		<input class="span12 date" type="date" name="PQWCSJ" fieldname="PQWCSJ"/>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">本周进展</th>
			          	<td class="bottom-border right-border"  width="23%">
			          		<textarea class="span12" rows="4" name ="PABZJZ"  fieldname="PABZJZ"></textarea>
			          	</td>
			        </tr>
				</table>    
	      		<h4 id="zgxx" class="title">拆迁形象进度</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right"">拆迁物名称</th>
			        	<td class="right-border bottom-border">
			          		<textarea class="span12" rows="4" name ="CQWMC"  fieldname="CQWMC"  check-type="maxlength" maxlength="1000"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">完成时限</th>
				        <td class="right-border bottom-border">
			          		<input class="span12 date" type="date" name="CQWCSJ" fieldname="CQWCSJ"/>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">本周进展</th>
			          	<td class="bottom-border right-border">
			          		<textarea class="span12" rows="4" name ="CQBZJZ"  fieldname="CQBZJZ"></textarea>
			          	</td>
			        </tr>
				</table>
	      		<h4 id="zgxx" class="title">周计量（万元）</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right"">本周完成</th>
			        	<td width="12%" class="right-border bottom-border">
			          		<input class="span12" style="text-align:right" min=0 type="number" fieldname="ZJLBZ" name = "ZJLBZ" >
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">本年完成</th>
				        <td width="12%" class="right-border bottom-border">
			          		<input class="span12" style="text-align:right" min=0 type="number" fieldname="ZJLND" name = "ZJLND" >
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">累计完成（含工程量）</th>
			          	<td width="12%" class="bottom-border right-border">
			          		<input class="span12" style="text-align:right" type="number" min=0 fieldname="ZJLLJWC" name = "ZJLLJWC" >
			          	</td>
			          	<td width="45%"></td>
			        </tr>
				</table>
	      		<h4 id="zgxx" class="title">存在问题及建议完成期限</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right"">前期问题</th>
			        	<td width="25%" class="right-border bottom-border">
			          		<textarea class="span12" rows="4" name ="QQWT"  fieldname="QQWT"  check-type="maxlength" maxlength="4000"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">合同、造价问题</th>
				        <td width="25%" class="right-border bottom-border">
			          		<textarea class="span12" rows="4" name ="HTZJWT"  fieldname="HTZJWT"  check-type="maxlength" maxlength="4000"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">设计问题</th>
				        <td width="25%" class="bottom-border right-border">
			          		<textarea class="span12" rows="4" name ="SJWT"  fieldname="SJWT" check-type="maxlength" maxlength="4000"></textarea>
			          	</td>
			        </tr>
			        <tr>
				        <th width="8%" class="right-border bottom-border text-right"">征拆问题</th>
				        <td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" id="JSRW" rows="4" name ="ZCWT"  fieldname="ZCWT" check-type="maxlength" maxlength="4000"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">排迁问题</th>
			          	<td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" id="JSRW" rows="4" name ="PQWT"  fieldname="PQWT" check-type="maxlength" maxlength="4000"></textarea>
			          	</td>
			        </tr>
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right"">备注</th>
			          	<td class="bottom-border right-border" colspan="5">
			          		<textarea class="span12" rows="4" name ="NOTE"  fieldname="NOTE" maxlength="4000"></textarea>
			          	</td>
			        </tr>
					<tr>
				       	<th class="right-border bottom-border text-right">现场照片</th>
				       	<td class="bottom-border right-border" colspan="5">
							<div>
								<span id="xczp" class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0311">
									<i class="icon-plus"></i>
									<span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody id="xczp" uploadOptions="type:image"  fjlb="0311" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
								</table>
							</div>
						</td>
				     </tr>															
					 <tr>
				       	<th class="right-border bottom-border text-right">其他文件</th>
				       	<td class="bottom-border right-border" colspan="5">
							<div>
								<span id="zbfj" class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0310">
									<i class="icon-plus"></i>
									<span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody id="zbfj" fjlb="0310" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
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
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>