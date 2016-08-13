<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>实例页面-数据维护</title>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/userController.do";
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
 				//alert(data);
 				var data1 = defaultJson.packSaveJson(data);
 				//调用ajax插入
 				defaultJson.doInsertJson(controllername + "?insertdemo", data1,DT1);
			}else{
				defaultJson.clearTxtXML();
			}

			
		});
	});
//修改
	$(function() {
		var btn = $("#example2");
		btn.click(function() {
			 if($("#DT1").getSelectedRowIndex()==-1)
			 {
				alert('请选择一条记录');
				return
			 }
			
 		 	
 		 		//生成json串
 		 		var data = Form2Json.formToJSON(demoForm);
 				//组成保存json串格式
 				//alert(data);
 				var data1 = defaultJson.packSaveJson(data);
 				//调用ajax插入
 				defaultJson.doUpdateJson(controllername + "?updatedemo", data1,DT1);
			
			
			var rowindex = $("#DT1").getSelectedRowIndex();
			//alert(rowindex);
			//
			var respdata = "{data:[{TBSJ:'20130727164321',TBSJ_SV:'2013-07-27',JSBYS:'bbb',XMQZD:'20130727000000',TBR:'superman',XMFL:'2014',ZHUX:'',BZ:'',SJBH:'2013072700000285',YWLX:'ywlx',SJMJ:'',XMMC:'123',GXDW:'',TBDW:'100000000000',JSYY:'',XMLY:'aa',XMNF:2014,ID:'3C65B499-32AB-7BA5-3027-F8980B553DC6',GXR:'',GS:'123',GXSJ:''}]}";
			$("#DT1").updateResult(respdata,DT1,rowindex);
		});
	});
	function tr_click(obj){
		$("#demoForm").setFormValues(obj);

		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		var rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		var rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
	}
	
    $(document).ready(function() {


    	
      });
</script>      
</head>
	<body>
		<div class="container-fluid">
			<div class="page-header">
				<h3>
					维护-新增
				</h3>
			</div>
			<div class="row-fluid">
				<div class="B-from-table-box">
					<h4>
						结果列表
					</h4>
					<div class="overFlowX">
						<table width="100%" class="table table-hover table-activeTd"
							id="DT1">
							<thead>
								<tr>
									<th type="single" name="XH"></th>
									<th fieldname="XMMC" style="width: 200px">
										项目名称
									</th>
									<th fieldname="XMNF">
										项目年份
									</th>
									<th fieldname="XMFL">
										项目分类
									</th>
									<th fieldname="XMQZD">
										项目起止点
									</th>
									<th fieldname="GS">
										概算
									</th>
									<th fieldname="XMLY">
										项目类型
									</th>
									<th fieldname="JSBYS">
										项目必要性
									</th>
									<th fieldname="JSYY">
										项目意义
									</th>
									<th fieldname="BZ">
										备注
									</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>

					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="B-from-table-box">
					<h4>
						信息采集
					</h4>
					<form method="post"
						action="${pageContext.request.contextPath }/insertdemo.do"
						id="demoForm">
						<table class="B-table">
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<input type="text" class="span12" kind="text" fieldname="ID">
								</TD>
							</TR>
							<tr>
								<th width="8%" class="right-border bottom-border">
									项目名称
								</th>

								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" placeholder="必填"
										check-type="required" fieldname="XMMC" name="XMMC">
								</td>
								<th width="8%" class="right-border bottom-border">
									项目年份
								</th>
								<td width="25%" class="bottom-border">
									<select class="span12" name="XMNF" fieldname="XMNF" kind="dic"
										src="XMNF">
									</select>
								</td>
								<td width="17%" class="text-left bottom-border">
									<label class="checkbox">
										应急、特殊项目
										<input type="checkbox" value="1">
									</label>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									项目分类
								</th>
								<td width="42%" class="right-border bottom-border">
									<select class="span12" name="XMFL" fieldname="XMFL" kind="dic"
										src="XMNF">
									</select>
								</td>
								<th width="8%" class="right-border bottom-border">
									项目起止点
								</th>
								<td width="42%" colspan="2" class="bottom-border">
									<input class="span12" type="date" placeholder="必填"
										check-type="required" name="XMQZD" fieldname="XMQZD">
								</td>

							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									概算
								</th>

								<td width="42%" class="right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="GS"
										fieldname="GS">
								</td>
								<th width="8%" class="right-border bottom-border">
									项目来源
								</th>

								<td width="42%" colspan="2" class="bottom-border">
									<input class="span12" type="text" placeholder="" name="XMLY"
										fieldname="XMLY">
								</td>

							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									建设必要性
								</th>

								<td width="92%" colspan="4" class="bottom-border">
									<textarea class="span12" rows="3" name="JSBYS"
										fieldname="JSBYS"></textarea>
								</td>

							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									建设意义
								</th>

								<td width="92%" colspan="4" class="bottom-border">
									<textarea class="span12" rows="3" name="JSYY" fieldname="JSYY"></textarea>
								</td>

							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									备注
								</th>

								<td width="92%" colspan="4" class="bottom-border">
									<textarea class="span12" rows="3" name="BZ" fieldname="BZ"></textarea>
								</td>

							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border">
									上传附件
								</th>
								<td width="92%" colspan="4" class="bottom-border">
									<div>
										<span class="btn btn-success" onclick="doSelectFile();">
											<i class="icon-plus icon-white"></i> <span>添加文件...</span> </span>
										<table role="presentation" class="table table-striped">
											<tbody id="" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<td class="bottom-border">
									<button id="example1" class="btn btn-primary" type="button">
										新 增
									</button>
								</td>
								<td class="bottom-border">
									<button id="example2" class="btn btn-primary" type="button">
										修 改
									</button>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
		<jsp:include page="/jsp/file_upload/fileupload_config.jsp"
			flush="true" />

		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="desc" fieldname="XMNF"
					id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="ywid" id="ywid">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
	</body>
</html>