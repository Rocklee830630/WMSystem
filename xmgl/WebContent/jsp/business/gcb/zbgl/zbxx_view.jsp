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
<title>工程周报-详细</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gczb/gczbController.do";
var obj;
//页面初始化
$(function() {
	var pwindow =$(window).manhuaDialog.getParentObj();
	var rowValue = pwindow.document.getElementById("resultXML").value;
	var obj = convertJson.string2json1(rowValue);
	deleteFileData(obj.XCZP,"",obj.SJBH,obj.YWLX);
	setFileData(obj.XCZP,"",obj.SJBH,obj.YWLX,"0");
	queryFileData(obj.XCZP,"",obj.SJBH,obj.YWLX);
	$("#gczbForm").setFormValues(obj);
	
});

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
	    	<h4 class="title">工程周报信息
			</h4>
	     	<form method="post" id="gczbForm"  >
				<table class="B-table" width="100%">
					
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
						<td width="42%" colspan="2" class="right-border bottom-border">
							<input class="span12" id="XMMC" type="text" keep="true" fieldname="XMMC" name = "XMMC"  readonly />
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
						<td width="42%" colspan="2" class="bottom-border right-border">
							<input class="span12" id="BDMC" type="text" fieldname="BDMC" keep="true" name ="BDMC" readonly />
						</td>
						</tr>
	        		<tr>
						<th class="right-border bottom-border text-right">开始时间</th>
						<td class="right-border bottom-border" colspan="2">
							<input class="span12 date" id="KSSJ" type="text" fieldname="KSSJ" readonly/>
						</td>
						<th class="right-border bottom-border text-right">结束时间</th>
						<td class="bottom-border right-border" colspan="2">
							<input class="span12 date" id="JSSJ" type="text" fieldname="JSSJ" readonly/>
						</td>
	        		</tr>
	        	</table>	
	      		<h4 id="zgxx" class="title">工程形象进度</h4>
				<table id="hfbd" class="B-table" width="100%">
	        		<tr>
	        			<th width="8%" class="right-border bottom-border text-right"">本周计划</th>
	        			<td  class="right-border bottom-border" colspan="2">
	          				<textarea class="span12" rows="4" name ="BZJH"  fieldname="BZJH" readonly></textarea>
	          			</td>
		        		<th width="8%" class="right-border bottom-border text-right"">本周完成</th>
		        		<td class="right-border bottom-border" colspan="2">
	          				<textarea class="span12" rows="4" name ="BZWC"  fieldname="BZWC" readonly></textarea>
	          			</td>
	       	 		</tr>
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right">本年完成</th>
				        <td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" rows="4" name ="BNWC"  fieldname="BNWC" readonly></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">累计完成</th>
			          	<td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" rows="4" name ="LJWC"  fieldname="LJWC" readonly></textarea>
			          	</td>
			        </tr>
			        <tr>
			        	<th class="right-border bottom-border text-right"">下周计划</th>
			          	<td class="bottom-border right-border" colspan="5">
			          		<textarea class="span12" rows="4" name ="XZJH"  fieldname="XZJH" readonly></textarea>
			          	</td>
			        </tr>
				</table> 
	      		<h4 id="zgxx" class="title">排迁形象进度</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right">管线名称</th>
			        	<td class="right-border bottom-border" width="23%">
			          		<textarea class="span12" rows="4" name ="GXMC"  fieldname="GXMC" readonly></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">完成时限</th>
				        <td class="right-border bottom-border"  width="23%">
			          		<input class="span12 date" type="text" name="PQWCSJ" fieldname="PQWCSJ" readonly/>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">本周进展</th>
			          	<td class="bottom-border right-border"  width="23%">
			          		<textarea class="span12" rows="4" name ="PABZJZ"  fieldname="PABZJZ" readonly></textarea>
			          	</td>
			        </tr>
				</table>    
	      		<h4 id="zgxx" class="title">拆迁形象进度</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right"">拆迁物名称</th>
			        	<td class="right-border bottom-border">
			          		<textarea class="span12" rows="4" name ="CQWMC"  fieldname="CQWMC" readonly></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">完成时限</th>
				        <td class="right-border bottom-border">
			          		<input class="span12 date" type="text" name="CQWCSJ" fieldname="CQWCSJ" readonly/>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">本周进展</th>
			          	<td class="bottom-border right-border">
			          		<textarea class="span12" rows="4" name ="CQBZJZ"  fieldname="CQBZJZ" readonly></textarea>
			          	</td>
			        </tr>
				</table>
	      		<h4 id="zgxx" class="title">周计量（万元）</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right"">本周完成</th>
			        	<td width="12%" class="right-border bottom-border">
			          		<input class="span12" style="text-align:right" min=0 type="number" fieldname="ZJLBZ" name = "ZJLBZ" readonly>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">本年完成</th>
				        <td width="12%" class="right-border bottom-border">
			          		<input class="span12" style="text-align:right" min=0 type="number" fieldname="ZJLND" name = "ZJLND" readonly>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">累计完成（含工程量）</th>
			          	<td width="12%" class="bottom-border right-border">
			          		<input class="span12" style="text-align:right" type="number" min=0 fieldname="ZJLLJWC" name = "ZJLLJWC" readonly>
			          	</td>
			          	<td width="45%"></td>
			        </tr>
				</table>
	      		<h4 id="zgxx" class="title">存在问题及建议完成期限</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right"">前期问题</th>
			        	<td width="25%" class="right-border bottom-border">
			          		<textarea class="span12" rows="4" name ="QQWT"  fieldname="QQWT"  readonly></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">合同、造价问题</th>
				        <td width="25%" class="right-border bottom-border">
			          		<textarea class="span12" rows="4" name ="HTZJWT"  fieldname="HTZJWT"  readonly></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">设计问题</th>
				        <td width="25%" class="bottom-border right-border">
			          		<textarea class="span12" rows="4" name ="SJWT"  fieldname="SJWT" readonly></textarea>
			          	</td>
			        </tr>
			        <tr>
				        <th width="8%" class="right-border bottom-border text-right"">征拆问题</th>
				        <td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" id="JSRW" rows="4" name ="ZCWT"  fieldname="ZCWT" readonly></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right"">排迁问题</th>
			          	<td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" id="JSRW" rows="4" name ="PQWT"  fieldname="PQWT" readonly></textarea>
			          	</td>
			        </tr>
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right"">备注</th>
			          	<td class="bottom-border right-border" colspan="5">
			          		<textarea class="span12" rows="4" name ="NOTE"  fieldname="NOTE" readonly></textarea>
			          	</td>
			        </tr>
					<tr>
				       	<th class="right-border bottom-border text-right">现场照片</th>
				       	<td class="bottom-border right-border" colspan="5">
							<div>
								<table role="presentation" class="table table-striped">
									<tbody id="xczp" uploadOptions="type:image"  fjlb="0311" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery" onlyView="true"></tbody>
								</table>
							</div>
						</td>
				     </tr>															
					 <tr>
				       	<th class="right-border bottom-border text-right">其他文件</th>
				       	<td class="bottom-border right-border" colspan="5">
							<div>
								<table role="presentation" class="table table-striped">
									<tbody id="zbfj" fjlb="0310" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery" onlyView="true"></tbody>
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