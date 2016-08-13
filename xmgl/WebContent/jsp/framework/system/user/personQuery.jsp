<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>实例页面-用户信息查询</title>

<script type="text/javascript" charset="UTF-8">
 var id,json,rowindex,rowValue;
  var controllername= "${pageContext.request.contextPath }/userController.do";


	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
		//控制弹出表单的高度
		$("#formDiv").attr("style","height:"+(g_iHeight-200)+"px;min-height:350px;");
	}
	
	$(function() {
		$("#deleteBtn").hide();
		var btn = $("#queryBtn");
		btn.click(function() {
	        //生成json串
	        var str = "";
			$("#queryForm label input[name='FLAG']").each(function(i){
				if($(this).is(":checked")){
					str += $(this).val()+",";
				}
			});
			str = str.length==0?"":str.substring(0,str.length-1);
			$("#q_flag").val(str);
			var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryUser", data, DT1);
		});
	});
	
	//页面默认参数
	$(document).ready(function() { 
		setPageHeight();
        //生成json串
        g_bAlertWhenNoResult = false;
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryUser", data, DT1);
        g_bAlertWhenNoResult = true;
	}); 
	
	// 清除表单
	$(function() {
		$("#query_clear").click(function() {
	       $("#queryForm").clearFormResult();
		});
	});
	
	function tr_click(obj,tabListid) {
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.ACCOUNT;
		name = obj.NAME;
		$("#executeFrm").setFormValues(obj);
	}
	
	// 点击添加按钮 清空表单
	$(function() {
		var btn = $("#insertBtn");
		btn.click(function() {
			clearFileTab();
			$("#ywid").val("");
			// 当添加用户信息时，解除ID为【ACCOUNT】的disabled属性
			$("#myModalLabel").html("用户管理>新增");
			$("#ACCOUNT").removeAttr("disabled");
			$("#executeFrm").clearFormResult(); 
			$("#DT1").cancleSelected();
			id = "null";
		});
	});
	
	// 点击修改按钮
	$(function() {
		var btn = $("#updateBtn");
		btn.click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				//xAlert("提示信息",'请选择一条记录');
				requireSelectedOneRow();
				// 没有选中结果集行的时候，不显示弹出层，将【修改用户信息】按钮的【data-toggle】属性修改成空即可
				btn.attr("data-toggle","");
			} else {
				$("#myModalLabel").html("用户管理>修改");
				// 选中时，需要恢复【修改用户信息】按钮的【data-toggle】属性值
				btn.attr("data-toggle","modal");
				
				// 当修改用户信息时，由于【用户登录名ACCOUNT】是主键，所以不能修改。
				$("#ACCOUNT").attr("disabled","disabled");
			}
			clearFileTab();
			$("#ywid").val("");
			var rowValue = $("#DT1").getSelectedRow();
			var tempJson = convertJson.string2json1(rowValue);
			queryUserFile("0038",tempJson.ACCOUNT,"","");
			queryUserFile("0039",tempJson.ACCOUNT,"","");
			setFileData(tempJson.ACCOUNT,"","","");
		});
	});
	//----------------------------------------
	//-查询文件信息，并插入到表格(这个是用户和签名特殊要求的)
	//-@param ywid 业务ID，可以暂时理解为批次编号
	//-@param glid1 项目ID
	//-@param sjbh	事件编号
	//-@param ywlx	业务类型
	//----------------------------------------
	function queryUserFile(fjlb,glid1,sjbh,ywlx,url){
		var obj = new Object();
		obj.FSLB = fjlb;
		obj.ACCOUNT = glid1;
		var data = JSON.stringify(obj);
			var data1 = defaultJson.packSaveJson(data);
			$.ajax({
			url : controllername+"?queryUserFile",
			data : data1,
			cache : false,
			async :	false,
			dataType : "json",
			success : function(result) {
				//clearFileTab("query");
				insertFileTab(result.msg);
			}
		});
	}
	// 点击删除按钮
	$(function() {
		var btn = $("#deleteBtn");
		btn.click(function() {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
				//xAlert("提示信息",'请选择一条记录');
		 		requireSelectedOneRow();
			} else {
				xConfirm("提示信息","是否确认删除！");
				//生成json串
				var data = Form2Json.formToJSON(executeFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				$('#ConfirmYesButton').one("click",function(obj) {
					var success = defaultJson.doUpdateJson(controllername + "?executeUser&id="+id+"&update=3", data1, DT1);
					
					if(success == true) {
						$("#DT1").setSelect(rowindex);
				//		var dd = {id:1,name:2};//此处为入参
						var index = $("#DT1").getSelectedRowIndex();//获得选中行的索引
						var value = $("#DT1").getSelectedRow();//获得选中行的json对象

				        $("#DT1").removeResult(index);
					}
				});  
				
			} 
		});
	});
	// 点击密码初始化
	$(function() {
		var btn = $("#resetPw");
		btn.click(function() {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
		 		requireSelectedOneRow();
			} else {
				//生成json串
				var data = Form2Json.formToJSON(executeFrm);
				//组成保存json串格式
				var obj = convertJson.string2json1(rowValue);
				var account=obj.ACCOUNT;
				xConfirm("提示信息","是否确认密码重置！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){
					$.ajax({
						url : controllername+"?resetPw&account="+account,
						cache : false,
						async : false,
						dataType : 'json',
						success : function(response) {
							xSuccessMsg("密码重置成功","");
						}
					});
				});
			} 
		});
	});
	var sign;
	// 检测用户名是否重复
	$(function() {
		var userAccount = $("#ACCOUNT");
		userAccount.blur(function() {
			$.ajax({
				url : controllername+"?queryUnique&account="+$("#ACCOUNT").val(),
		//		data : data,
				cache : false,
				async : false,
				dataType : 'json',
				success : function(response) {
					var result = eval("(" + response + ")");
					sign = result.sign;
					//xAlert("提示信息", result.isUnique);
					xSuccessMsg(result.isUnique,"");
				}
			});
		});
	});
	
	// 点击保存按钮
	$(function() {
		var saveUserInfoBtn = $("#saveUserInfo");
		saveUserInfoBtn.click(function() {
			
			if(sign == 1) {
				 //xAlert("提示信息","登录用户名重复，请重新填写");
				 xFailMsg("登录用户名重复，请重新填写","");
				 return;
			}
			
			if($("#executeFrm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(executeFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				
				var success = false;
				if(id == null || id == "null" || id == "indefined") {
					success = defaultJson.doInsertJson(controllername + "?executeUser&id="+id+"&update=1", data1, DT1);
				} else {
					success = defaultJson.doUpdateJson(controllername + "?executeUser&id="+id+"&update=2", data1, DT1);
				}
				
				if(success == true) {
					$("#myModal").modal("hide");
				} else {
					//	失败
				}
				clearFileTab();
				$("#ywid").val("");
			}
		});
	});

	//父页面调用查询页面的方法
	$(function() {
		$("#awardRole").click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				 //xAlert("提示信息","请选择一条用户信息");
				 //requireSelectedOneRow();
				 xInfoMsg("请选择一条用户信息","");
				 return;
			}
			$(window).manhuaDialog({"title" : "用户管理>授予角色","type" : "text","content" : "${pageContext.request.contextPath}/jsp/framework/system/user/awardRole.jsp?account="+id+"&name="+name,"modal":"2"});
		});
	});
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">用户管理
			<span class="pull-right">
				<a href="#myModal" role="button" class="btn" data-toggle="modal" id="insertBtn">新增</a>
				<a href="#myModal" role="button" class="btn" data-toggle="modal" id="updateBtn">修改</a>
				<a href="#myModal" role="button" class="btn" id="deleteBtn">删除</a>
				<a href="#myModal" role="button" class="btn" id="awardRole">授予角色</a>
				<button id="resetPw" class="btn" type="button" name="resetPw" >重置密码</button>
			</span>
			</h4>
		<form method="post" id="queryForm"  >
		<table class="B-table" width="100%">
		<!--可以再此处加入hidden域作为过滤条件 -->
			<TR  style="display:none;">
				<TD>
					<input type="text" class="span12" kind="text"  fieldname="rownum" value="1000" operation="<=" >
				</TD>
				<th width="5%" class="right-border bottom-border text-right">
					人员状态
				</th>
				<td class="right-border bottom-border" width="15%">
					<input class="span12" type="text" name="FLAG"  FIELDTYPE="text"
						fieldname="FLAG" operation="in" id="q_flag" logic="and" >
				</td>
			</TR>
		<!--可以再此处加入hidden域作为过滤条件 -->
			<tr>
				<th class="right-border bottom-border text-right">登录名</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="account" fieldname="ACCOUNT" operation="like" logic="and" ></td>
				<th class="right-border bottom-border text-right">姓名</th>
				<td width="10%" class="right-border bottom-border">
					<input class="span12" type="text" placeholder="" name="name" fieldname="NAME" operation="like" logic="and" ></td>
					
		        <th class="right-border bottom-border text-right">性别</th>
				<td width="8%" class="right-border bottom-border">
					<select class="span12 2characters" name="SEX" fieldname="SEX" id="SEX" kind="dic" src="XB" operation="=" logic="and" defaultMemo="全部"></select></td>
				<th class="right-border bottom-border text-right">所在单位</th>
				<td class="bottom-border">
					<select class="span12 6characters" placeholder="" name="department" fieldname="DEPARTMENT" defaultMemo="全部" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME:ACTIVE_FLAG='1' ORDER BY SORT" operation="="  logic="and"></select></td>
				<th class="right-border bottom-border">
					人员状态
				</th>
				<td class="bottom-border">
					<label class="checkbox inline"><input type="checkbox" name="FLAG" value="1" title="有效" checked>有效</label>
					<label class="checkbox inline"><input type="checkbox" name="FLAG" value="0" title="无效">无效</label>
				</td>
				<td class="text-left bottom-border text-right">
					<button	id="queryBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
                    <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
	            </td>	
			</tr>
		</table>
		</form>
	
	<div style="height:5px;"> </div>		
	<div class="overFlowX"> 
		<table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
			<thead>
				<tr>
					<th name="XH" id="_XH" width="3%">#</th>
					<th fieldname="ACCOUNT" >&nbsp;登录名&nbsp;</th>
					<th fieldname="NAME" >&nbsp;用户姓名&nbsp;</th>
					<th fieldname="SEX" tdalign="center" width="5%">&nbsp;性别&nbsp;</th>
					<th fieldname="DEPARTMENT" >&nbsp;所在单位&nbsp;</th>
					<th fieldname="PERSON_KIND" >&nbsp;用户类别&nbsp;</th>
					<th fieldname="FLAG" >&nbsp;是否有效&nbsp;</th>
				</tr>
			</thead>
		    <tbody></tbody>
		</table>
		            
		</div>
		</div>
	</div>
</div>

<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML" id = "queryXML">
		<input type="hidden" name="txtXML" id = "txtXML">
		<input type="hidden" name="txtFilter"  order="asc" fieldname = "to_number(sort)"	id = "txtFilter">
		<input type="hidden" name="resultXML" id = "resultXML">
		<input type="hidden" name="queryResult" id = "queryResult">
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData">
	</FORM>
</div>

<!-- Modal -->
		<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header" style="background:#0866c6;">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					<i class="icon-remove icon-white"></i>
				</button>
				<h3 id="myModalLabel" style="color:white;">
					添加用户信息
				</h3>
			</div>
			<div class="modal-body" id="formDiv">
				<form method="post" id="executeFrm">
					<table class="B-table">
						<TR style="display:none;">
							<TD class="right-border bottom-border"></TD>
							<TD class="right-border bottom-border">
							<input type="text" name="ywid" id="ywid" fieldname="YWID">
								<!-- 	<input type="text" kind="text" fieldname="PASSWORD" name="PASSWORD" id="PASSWORD" value="123456"> -->
							</TD>
						</TR>
						<tr>
							<th width="30%" class="right-border bottom-border">
								用户登录名
							</th>
							<td class="right-border bottom-border">
								<input type="text" placeholder="必填" check-type="required"
									maxlength="32" fieldname="ACCOUNT" name="ACCOUNT" id="ACCOUNT">
							</td>
							<td rowspan=8 width="10px;"></td>
							<td rowspan=8 class="right-border bottom-border" align="center" valign="top">
								<div style="border-style: solid; border-width: 1px;border-color: #DDDDDD;">
									<table role="presentation" class="table table-striped" >
										<tbody fjlb="0038" uploadOptions="type:image" showType="type:image;count:1;width:100px;del:false;"
											class="files showFileTab" data-toggle="modal-gallery"
											data-target="#modal-gallery">
										</tbody>
									</table>
								</div>
								<div>
									<span class="btn btn-fileUpload" onclick="doSelectFile(this);"
										fjlb="0038"> <i class="icon-plus"></i>
										<span>更换头像...</span>
									</span>
								</div>
								<div style="border-style: solid; border-width: 1px;border-color: #DDDDDD;">
									<table role="presentation" class="table table-striped">
										<tbody fjlb="0039" showType="type:image;width:100px;"
											class="files showFileTab" data-toggle="modal-gallery"
											data-target="#modal-gallery">
										</tbody>
									</table>
								</div>
								<div>
									<span class="btn btn-fileUpload" onclick="doSelectFile(this);"
										fjlb="0039"> <i class="icon-plus"></i>
										<span>更换签名...</span>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border">
								用户姓名
							</th>
							<td class="right-border bottom-border">
								<input type="text" placeholder="必填" check-type="required"
									maxlength="32" fieldname="NAME" name="NAME" id="NAME">
							</td>
						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border">
								性 别
							</th>
							<td class="right-border bottom-border">
								<select type="text" placeholder="必填" check-type=""
									fieldname="SEX" name="SEX" id="SEX" kind="dic" src="XB"></select>
							</td>
						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border">
								所在单位
							</th>
							<td class="right-border bottom-border">
								<select type="text" placeholder="必填" check-type="required"
									fieldname="DEPARTMENT" name="DEPARTMENT" id="DEPARTMENT"
									kind="dic"
									src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME"></select>
							</td>
						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border">
								用户类别
							</th>
							<td class="right-border bottom-border">
								<select type="text" placeholder="必填" check-type="required"
									fieldname="PERSON_KIND" name="PERSON_KIND" id="PERSON_KIND"
									kind="dic" src="YHLB"></select>
							</td>

						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border">
								手机号码
							</th>
							<td class="right-border bottom-border">
								<input type="text" placeholder="" check-type="number"
									maxlength="32" fieldname="SJHM" name="SJHM" id="SJHM">
							</td>
						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border">
								身份证号
							</th>
							<td class="right-border bottom-border">
								<input type="text" placeholder="" 
									maxlength="18" fieldname="IDCARD" name="IDCARD" id="IDCARD">
							</td>
						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border">
								人员排序序号
							</th>
							<td class="right-border bottom-border">
								<input type="number" placeholder="" 
									maxlength="10" fieldname="SORT" name="SORT" id="SORT">
							</td>
						</tr>
						<tr>
							<th width="30%" class="right-border bottom-border">
								是否有效
							</th>
							<td class="right-border bottom-border">
								<select type="text" kind="dic" src="SF" placeholder="必填" check-type="required"
									fieldname="FLAG" name="FLAG"></select>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div class="modal-footer">
				<button class="btn" id="saveUserInfo">
					保存
				</button>
				<button class="btn" data-dismiss="modal" aria-hidden="true">
					关闭
				</button>
			</div>
		</div>
		<jsp:include page="/jsp/file_upload/fileupload_config.jsp"
			flush="true" />
</body>
</html>