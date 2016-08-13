<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>下达项目库-维护</title>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>
		
		
<script type="text/javascript" charset="utf-8">

//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
	//页面初始化
	$(function() {
		//按钮绑定事件(查询)
		$("#btnQuery").click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?query",data,xdxmkList);
		});
		//按钮绑定事件(保存)
		$("#btnSave").click(function() {
			//生成json串
			
			if($("#xdxmkForm").validationButton())
			{
				//生成json串
				var data = Form2Json.formToJSON(xdxmkForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//调用ajax插入
				var rowindex = $("#xdxmkList").getSelectedRowIndex();
				defaultJson.doInsertJson(controllername + "?insert", data1,xdxmkList);
				$("#xdxmkList").updateResult(data,xdxmkList,rowindex);
				alert("保存成功!");
			}else{
				alert("请输入必填项!");
				return ;
			}
			
		});

		//按钮绑定事件(下达统筹计划)
		$("#btnXdtcjh").click(function() {
			location.href = "${pageContext.request.contextPath}/jsp/business/jhb/xdxmk/aXmxdtc.jsp";
		});
		
	});
	//行点击事件
	function tr_click(obj){
		setValueByArr(obj,
		["ID","XMBH","XMMC","XMLX","XMSX","XMGLGS","XMFR","QY","ISBT","XMDZ","JHZTZE","GC","ZC","QT","JSNR","JSYY","JSRW","JSBYX","JSZT"]
		);
		//为上传文件是需要的字段赋值
		setFileData("26F1BD08-C994-1C96-7D5A-03106A74D8DB",obj.ID,obj.SJBH,obj.YWLX);
		//查询附件信息
		queryFileData("148319F6-1EF6-8D3C-525D-C220F6D349A2","","","");
		
		//不再使用resultXML
		//这里通过obj.ID的方式为项目编号重新赋值，允许有多个值，以逗号分隔
		//obj.ID="1986482F-0C12-2C8F-1A8D-BEE3B4045293,DEECF14C-5805-B5AF-B40E-CDF622BCF895";
		//$("#resultXML").val(JSON.stringify(obj));
		
		//不再使用ifram方式
		//var url = "${pageContext.request.contextPath}/jsp/file_upload/fileupload.jsp";
		//$("#fileUploadFrame").attr("src",url);//用刷新iframe的方式加载附件信息
	}
	

</script>      
    
</head>
	<body>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-from-table-box">
					<form method="post" id="queryForm">
						<table class="B-table">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT type="text" class="span12" kind="text"
										fieldname="rownum" value="1000" operation="<=">
									<INPUT type="text" class="span12" kind="text" fieldname="SFYX"
										value="0" operation="=">
								</TD>
							</TR>
							<tr>
								<th width="5%" class="right-border bottom-border text-right">
									项目年份
								</th>
								<td class="right-border bottom-border">
									<select class="span12" id="xmnf" name="QND" fieldname="ND"
										operation="=" kind="dic" src="XMNF">
									</select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									项目类型
								</th>
								<td class="right-border bottom-border">
									<select class="span12" name="QXMLX" fieldname="XMLX"
										operation="=" kind="dic" src="XMLX">
									</select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									项目名称
								</th>
								<td class="right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="QXMMC"
										fieldname="XMMC" operation="like">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									项目属性
								</th>
								<td class="right-border bottom-border">
									<select class="span12" name="QXMSX" fieldname="XMSX"
										operation="=" kind="dic" src="XMSX">
									</select>
								</td>
								<td class="bottom-border">
									<button id="btnQuery" class="btn btn-primary" type="button">
										查询
									</button>
								</td>
								<td class="bottom-border" align="right">
									<button id="btnXdtcjh" class="btn btn-primary" type="button">
										下达统筹计划
									</button>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
			<div class="row-fluid">
				<div class="B-from-table-box">
					<h4>
						查询结果
					</h4>
					<div class="overFlowX">
						<table class="table table-hover table-activeTd" id="xdxmkList"
							width="100%">
							<thead>
								<tr>
									<th fieldname="XMMC">
										项目名称
									</th>
									<th fieldname="ISNATC">
										项目状态
									</th>
									<th fieldname="XMLX">
										项目类型
									</th>
									<th fieldname="XMGLGS">
										项目管理公司
									</th>
									<th fieldname="XMSX">
										项目属性
									</th>
									<th fieldname="ISBT">
										BT
									</th>
									<th fieldname="XMDZ">
										项目地址
									</th>
									<th fieldname="JHZTZE">
										总投资额
									</th>
									<th fieldname="QY">
										项目来源
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
					<form method="post" id="xdxmkForm">
						<table class="B-table" width="100%">
							<input type="hidden" id="ID" fieldname="ID" name="ID" />
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									项目编号
								</th>
								<td class="right-border bottom-border" colspan="2">
									<input class="span12" id="XMBH" type="text" placeholder="必填"
										check-type="required" fieldname="XMBH" name="XMBH"
										maxlength="36">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									项目名称
								</th>
								<td class="bottom-border right-border" colspan="2">
									<input class="span12" id="XMMC" type="text" placeholder="必填"
										check-type="required" fieldname="XMMC" name="XMMC"
										maxlength="100">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									项目类型
								</th>
								<td class="right-border bottom-border" colspan="2">
									<select class="span12" id="XMLX" kind="dic" src="XMLX"
										check-type="required" fieldname="XMLX" name="XMLX">
									</select>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									项目属性
								</th>
								<td class="bottom-border right-border" colspan="2">
									<select class="span12" id="XMSX" kind="dic" src="XMSX"
										check-type="required" fieldname="XMSX" name="XMSX">
									</select>
								</td>
								<th width="12%" class="right-border bottom-border text-right">
									项目管理公司
								</th>
								<td class="right-border bottom-border" colspan="2">
									<input class="span12" id="XMGLGS" placeholder="必填"
										check-type="required" name="XMGLGS" fieldname="XMGLGS"
										maxlength="100" />
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									项目法人
								</th>
								<td class="bottom-border right-border" colspan="2">
									<input class="span12" id="XMFR" type="text" placeholder="必填"
										check-type="required" fieldname="XMFR" name="XMFR"
										maxlength="100">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									项目来源
								</th>
								<td class="right-border bottom-border" colspan="2">
									<input class="span12" id="QY" type="text" placeholder="必填"
										check-type="required" fieldname="QY" name="QY" maxlength="100">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									是否BT
								</th>
								<td class="bottom-border right-border" colspan="2">
									<select class="span12" id="ISBT" kind="dic" src="SF"
										check-type="required" fieldname="ISBT" name="ISBT">
									</select>
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									项目地址
								</th>
								<td class="bottom-border right-border" colspan="2">
									<input class="span12" id="XMDZ" check-type="required"
										placeholder="必填" fieldname="XMDZ" name="XMDZ" maxlength="500">
								<td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									计划总投资额
								</th>
								<td class="right-border bottom-border">
									<input class="span12" id="JHZTZE" type="text" placeholder="必填"
										check-type="required" fieldname="JHZTZE" name="JHZTZE"
										maxlength="17">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									工程计划投资
								</th>
								<td class="right-border bottom-border">
									<input class="span12" id="GC" type="text" placeholder="必填"
										check-type="required" fieldname="GC" name="GC" maxlength="17">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									征拆计划投资
								</th>
								<td class="right-border bottom-border">
									<input class="span12" id="ZC" type="text" placeholder="必填"
										check-type="required" fieldname="ZC" name="ZC" maxlength="17">
								</td>
								<th width="8%" class="right-border bottom-border text-right">
									其他计划投资资
								</th>
								<td class="bottom-border right-border" colspan="2">
									<input class="span12" id="QT" type="text" placeholder="必填"
										check-type="required" fieldname="QT" name="QT" maxlength="17">
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									建设内容
								</th>
								<td colspan="8" class="bottom-border right-border">
									<textarea class="span12" id="JSNR" rows="3" name="JSNR"
										placeholder="必填" check-type="required" fieldname="JSNR"
										maxlength="500"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									建设意义
								</th>
								<td colspan="8" class="bottom-border right-border">
									<textarea class="span12" id="JSYY" rows="3" name="JSYY"
										placeholder="必填" check-type="required" fieldname="JSYY"
										maxlength="500"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									建设任务
								</th>
								<td colspan="8" class="bottom-border right-border">
									<textarea class="span12" id="JSRW" rows="3" name="JSRW"
										placeholder="必填" check-type="required" fieldname="JSRW"
										maxlength="500"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									建设必要性
								</th>
								<td colspan="8" class="bottom-border right-border">
									<textarea class="span12" id="JSBYX" rows="3" name="JSBYX"
										placeholder="必填" check-type="required" fieldname="JSBYX"
										maxlength="500"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									建设主体
								</th>
								<td colspan="8" class="bottom-border right-border">
									<textarea class="span12" id="JSZT" rows="3" name="JSZT"
										placeholder="必填" check-type="required" fieldname="JSZT"
										maxlength="100"></textarea>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									预算送审单
								</th>
								<td colspan="8" class="bottom-border right-border">
									<div>
										<span class="btn btn-success" onclick="doSelectFile(this);" fjlb="0001">
											<i class="icon-plus icon-white"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0001" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									工程预算审定表
								</th>
								<td colspan="8" class="bottom-border right-border">
									<div>
										<span class="btn btn-success" onclick="doSelectFile(this);" fjlb="0002">
											<i class="icon-plus icon-white"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody fjlb="0002" class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<th width="8%" class="right-border bottom-border text-right">
									全部附件
								</th>
								<td colspan="8" class="bottom-border right-border">
									<div>
										<span class="btn btn-success" onclick="doSelectFile(this);" fjlb="">
											<i class="icon-plus icon-white"></i>
											<span>添加文件...</span>
										</span>
										<table role="presentation" class="table table-striped">
											<tbody class="files showFileTab"
												data-toggle="modal-gallery" data-target="#modal-gallery">
											</tbody>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="8" class="bottom-border right-border">
									<button id="btnSave" class="btn btn-primary" type="button">
										保存
									</button>
								</td>
							</tr>
						</table>
					</form>
					
				</div>
				
			</div>
		</div>
		<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="desc" fieldname="XMNF"
					id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
	</body>
</html>