<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script>

<title>个人通讯录管理</title>

<script type="text/javascript" charset="UTF-8">
var id,json,rowindex,rowValue;
var publicId,publicRowIndex;
  var controllername= "${pageContext.request.contextPath }/txlController.do";
	$(function() {
		var btn = $("#queryPrivateBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryForm, frmPrivatePost, DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryPrivateTxl", data, DT1);
		});
	});

	// 查询公共联系人
	$(function() {
		var btn = $("#queryPublicBtn");
		btn.click(function() {
	        //生成json串
			var data = combineQuery.getQueryCombineData(queryPublicForm, frmPublicPost, DT2);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryPublicTxl", data, DT2);
		});
	});
	
	function tr_click(obj,tabListid){
		//$("#queryForm").setFormValues(obj);
		
		//obj为行数据的json 对象，可以通过obj.XMMC获得选中行的项目名称
		rowindex = $("#DT1").getSelectedRowIndex();//获得选中行的索引
		rowValue = $("#DT1").getSelectedRow();//获得选中行的json对象
		id = obj.TXID;
		$("#executeTxlFrm").setFormValues(obj);
		
		publicId = obj.TXID;
		publicRowIndex = $("#DT2").getSelectedRowIndex();//获得选中行的索引
		$("#executePublicTxlFrm").setFormValues(obj);
	}

	// 点击添加按钮 清空表单（个人）
	$(function() {
		var btn = $("#insertBtn");
		btn.click(function() {
			$("#executeTxlFrm").clearFormResult(); 
			id = "null";
		});
	});
	
	// 点击添加按钮 清空表单（公共）
	$(function() {
		var btn = $("#insertPublicBtn");
		btn.click(function() {
			$("#executePublicTxlFrm").clearFormResult(); 
			publicId = "null";
		});
	});
	
	// 点击修改按钮（个人）
	$(function() {
		var btn = $("#updateBtn");
		btn.click(function() {
			if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条记录');
				// 没有选中结果集行的时候，不显示弹出层，将【修改用户信息】按钮的【data-toggle】属性修改成空即可
				btn.attr("data-toggle","");
			} else {
				// 选中时，需要恢复【修改信息】按钮的【data-toggle】属性值
				btn.attr("data-toggle","modal");
				
			}
		});
	});
	
	// 点击修改按钮（公共）
	$(function() {
		var btn = $("#updatePublicBtn");
		btn.click(function() {
			if($("#DT2").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条记录');
				// 没有选中结果集行的时候，不显示弹出层，将【修改用户信息】按钮的【data-toggle】属性修改成空即可
				btn.attr("data-toggle","");
			} else {
				// 选中时，需要恢复【修改信息】按钮的【data-toggle】属性值
				btn.attr("data-toggle","modal");
				
			}
		});
	});
	

	// 点击删除按钮
	$(function() {
		var btn = $("#deleteBtn");
		btn.click(function() {
		 	if($("#DT1").getSelectedRowIndex()==-1) {
				xAlert("提示信息",'请选择一条记录');
			} else {
				xConfirm("提示信息","是否确认删除！");
				//生成json串
				var data = Form2Json.formToJSON(executeTxlFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				$('#ConfirmYesButton').one("click",function(obj) {
					var success = defaultJson.doUpdateJson(controllername + "?executePrivateTxl&id="+id+"&exeSign=3", data1, DT1);
					
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
	
	// 点击保存按钮（个人）
	$(function() {
		var saveUserInfoBtn = $("#saveUserInfo");
		saveUserInfoBtn.click(function() {
			if($("#executeTxlFrm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(executeTxlFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				
				var success = false;
				if(id == null || id == "null" || id == "indefined") {
					success = defaultJson.doInsertJson(controllername + "?executePrivateTxl&id="+publicId+"&exeSign=1", data1, DT1);
				} else {
					success = defaultJson.doUpdateJson(controllername + "?executePrivateTxl&id="+publicId+"&exeSign=2", data1, DT1);
				}
				
				if(success == true) {
					$("#myModal").modal("hide");
				} else {
					//	失败
				}
			}
		});
	});
	
	// 点击保存按钮（公共）
	$(function() {
		var saveUserInfoBtn = $("#savePublicUserInfo");
		saveUserInfoBtn.click(function() {
			if($("#executePublicTxlFrm").validationButton()) {
				//生成json串
				var data = Form2Json.formToJSON(executePublicTxlFrm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//通过判断id是否为空来判断是插入还是修改
				
				var success = false;
				if(id == null || id == "null" || id == "indefined") {
					success = defaultJson.doInsertJson(controllername + "?executePublicTxl&id="+id+"&exeSign=1", data1, DT2);
				} else {
					success = defaultJson.doUpdateJson(controllername + "?executePublicTxl&id="+id+"&exeSign=2", data1, DT2);
				}
				
				if(success == true) {
					$("#publicModal").modal("hide");
				} else {
					//	失败
				}
			}
		});
	});
	
</script>      
</head>
<body>
<app:dialogs/>
<p></p>
<ul class="nav nav-tabs" id="myTab">
  <li class="active"><a href="#home" data-toggle="tab">个人通讯录</a></li>
  <li><a href="#profile" data-toggle="tab">公共通讯录</a></li>
  <li><a href="#messages" data-toggle="tab">内部通讯录</a></li>
</ul>

<div class="tab-content">
	<div class="tab-pane active" id="home">
	
		<div class="container-fluid">
		  <div class="row-fluid">
		    <div class="B-small-from-table-auto">
		      <h4>个人通讯录查询条件  <span class="pull-right">  <button id="queryPrivateBtn" class="btn btn-inverse"  type="button">查询</button>
		      </span></h4>
		     <form method="post" id="queryForm"  >
		      <table class="B-table">
		      <!--可以再此处加入hidden域作为过滤条件 -->
		      	<TR  style="display:none;">
			        <TD class="right-border bottom-border"></TD>
					<TD class="right-border bottom-border">
						<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
					</TD>
		        </TR>
		        <!--可以再此处加入hidden域作为过滤条件 -->
		        <tr>
		          <th width="8%" class="right-border bottom-border">姓名</th>
		          <td width="42%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="XM" fieldname="XM" operation="like" logic="and" ></td>
		          <th width="8%" class="right-border bottom-border">性别</th>
		          <td width="42%" class="bottom-border">
		          	<input class="span12" type="text" placeholder="" name="XB" fieldname="XB" operation="=" logic="and" ></td>
		          <td width="17%" class="text-left bottom-border">
		          </td>
		        </tr>
		        <tr>
		          <th width="8%" class="right-border bottom-border">职位</th>
		          <td width="42%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="ZW" fieldname="ZW" operation="=" logic="and"></td>
		          <th width="8%" class="right-border bottom-border">工作电话</th>
		          <td width="42%" colspan="2" class="bottom-border">
		          	<input class="span12" type="text" placeholder="" name="GZDH" fieldname="GZDH" operation="like"  logic="and"></td>
		        </tr>
		        <tr>
		          <th width="8%" class="right-border bottom-border">手机号码</th>
		          <td width="42%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="SJHM" fieldname="SJHM" operation="like" logic="and"></td>
		         
		        </tr>
		      </table>
		      </form>
		    </div>
		  </div>
		 <div class="row-fluid">
		      <div class="B-small-from-table-auto">
		       <h4>结果列表
					<span class="pull-right">
						<a href="#myModal" role="button" class="btn btn-inverse" data-toggle="modal" id="insertBtn">添加联系人信息</a>
						<a href="#myModal" role="button" class="btn btn-inverse" data-toggle="modal" id="updateBtn">修改联系人信息</a>
						<a href="#myModal" role="button" class="btn btn-inverse" id="deleteBtn">删除联系人信息</a>
					</span>
			   </h4>
		           <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
		                <thead>
		                    <tr>
		                    <th name="XH" id="_XH">序号</th>
		                    <th fieldname="XM">姓名</th>
							<th fieldname="NC" >昵称</th>
							<th fieldname="XB" kind="dic" >性别</th>
							<th fieldname="CSRQ"  fieldformat="YYYY-MM-DD" colindex=4>出生日期</th>
							<th fieldname="DW" >单位</th>
							<th fieldname="ZW" >职位</th>
							<th fieldname="GZDH" >工作电话</th>
							<th fieldname="YX" >邮箱</th>
							<th fieldname="SJHM" >手机号码</th>
		                    </tr>
		                </thead>
		                <tbody>
		                </tbody>
		            </table>
		            
		</div>
		</div>
		    
		</div>
		
		 <div align="center">
			<FORM name="frmPrivatePost" method="post" style="display:none" target="_blank" id ="frmPrivatePost">
			 <!--系统保留定义区域-->
		        <input type="hidden" name="queryXML" id = "queryXML">
		        <input type="hidden" name="txtXML" id = "txtXML">
		        <input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" id="txtFilter">
		        <input type="hidden" name="resultXML" id = "resultXML">
		        <input type="hidden" name="queryResult" id = "queryResult">
		      		 <!--传递行数据用的隐藏域-->
		        <input type="hidden" name="rowData">
			
			</FORM>
		</div>
	</div>
	 
	<div class="tab-pane" id="profile">
	
		<div class="container-fluid">
		  <div class="row-fluid">
		    <div class="B-small-from-table-auto">
		      <h4>公共通讯录查询条件  <span class="pull-right">  <button id="queryPublicBtn" class="btn btn-inverse"  type="button">查询</button>
		      </span></h4>
		     <form method="post" id="queryPublicForm"  >
		      <table class="B-table">
		      <!--可以再此处加入hidden域作为过滤条件 -->
		      	<TR  style="display:none;">
			        <TD class="right-border bottom-border"></TD>
					<TD class="right-border bottom-border">
						<input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" >
					</TD>
		        </TR>
		        <!--可以再此处加入hidden域作为过滤条件 -->
		        <tr>
		          <th width="8%" class="right-border bottom-border">姓名</th>
		          <td width="42%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="XM" fieldname="XM" operation="like" logic="and" ></td>
		          <th width="8%" class="right-border bottom-border">性别</th>
		          <td width="42%" class="bottom-border">
		          	<input class="span12" type="text" placeholder="" name="XB" fieldname="XB" operation="=" logic="and" ></td>
		          <td width="17%" class="text-left bottom-border">
		          </td>
		        </tr>
		        <tr>
		          <th width="8%" class="right-border bottom-border">职位</th>
		          <td width="42%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="ZW" fieldname="ZW" operation="=" logic="and"></td>
		          <th width="8%" class="right-border bottom-border">工作电话</th>
		          <td width="42%" colspan="2" class="bottom-border">
		          	<input class="span12" type="text" placeholder="" name="GZDH" fieldname="GZDH" operation="like"  logic="and"></td>
		        </tr>
		        <tr>
		          <th width="8%" class="right-border bottom-border">手机号码</th>
		          <td width="42%" class="right-border bottom-border">
		          	<input class="span12" type="text" placeholder="" name="SJHM" fieldname="SJHM" operation="like" logic="and"></td>
		         
		        </tr>
		      </table>
		      </form>
		    </div>
		  </div>
		 <div class="row-fluid">
		      <div class="B-small-from-table-auto">
		       <h4>结果列表
					<span class="pull-right">
						<a href="#publicModal" role="button" class="btn btn-inverse" data-toggle="modal" id="insertPublicBtn">添加联系人信息</a>
						<a href="#publicModal" role="button" class="btn btn-inverse" data-toggle="modal" id="updatePublicBtn">修改联系人信息</a>
						<a href="#publicModal" role="button" class="btn btn-inverse" id="deletePublicBtn">删除联系人信息</a>
					</span>
			   </h4>
		           <table width="100%" class="table-hover table-activeTd B-table" id="DT2" type="single">
		                <thead>
		                    <tr>
		                    <th name="XH" id="_XH">序号</th>
		                    <th fieldname="XM">姓名</th>
							<th fieldname="NC" >昵称</th>
							<th fieldname="XB" kind="dic" >性别</th>
							<th fieldname="CSRQ"  fieldformat="YYYY-MM-DD" colindex=4>出生日期</th>
							<th fieldname="DW" >单位</th>
							<th fieldname="ZW" >职位</th>
							<th fieldname="GZDH" >工作电话</th>
							<th fieldname="YX" >邮箱</th>
							<th fieldname="SJHM" >手机号码</th>
		                    </tr>
		                </thead>
		                <tbody>
		                </tbody>
		            </table>
		            
		</div>
		</div>
		    
		</div>
		
		 <div align="center">
			<FORM name="frmPublicPost" method="post" style="display:none" target="_blank" id ="frmPublicPost">
			 <!--系统保留定义区域-->
		        <input type="hidden" name="queryXML" id = "queryXML">
		        <input type="hidden" name="txtXML" id = "txtXML">
		        <input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" id="txtFilter">
		        <input type="hidden" name="resultXML" id = "resultXML">
		        <input type="hidden" name="queryResult" id = "queryResult">
		      		 <!--传递行数据用的隐藏域-->
		        <input type="hidden" name="rowData">
			
			</FORM>
		</div>
	</div>
	
	<div class="tab-pane" id="messages">...messages</div>
</div>
<!-- Modal -->
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="myModalLabel">添加联系人信息1</h3>
  </div>
  <div class="modal-body">
    <form method="post" id="executeTxlFrm"  >
      <table class="B-table" >
         <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			 	<input type="text" kind="text" fieldname="TXID" name="TXID" id="TXID">
			</TD>
        </TR>
        <tr>
          <th width="30%" class="right-border bottom-border">姓名</th>
          <td class="right-border bottom-border">
          	<input type="text" placeholder="必填"  check-type="required" fieldname="XM" name="XM" id="XM"></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">昵称</th>
          <td class="right-border bottom-border">
          	<input type="text" placeholder="必填" fieldname="NC" name="NC" id="NC"></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">性别</th>
          <td class="right-border bottom-border">
          	<select type="text" placeholder="必填" fieldname="XB" name="XB" id="XB" kind="dic" src="XB"></select></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">出生日期</th>
          <td class="right-border bottom-border">
          	<input type="date" placeholder="必填" fieldname="CSRQ" name="CSRQ" id="CSRQ" fieldtype="date" fieldformat="YYYY-MM-DD" /></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">单位</th>
          <td class="right-border bottom-border">
          	<!-- <select type="text" placeholder="必填"  check-type="required" fieldname="DW" name="DW" id="DW" ></select> -->
          	<input type="text" placeholder="必填" fieldname="DW" name="DW" id="DW" />
          </td>
        </tr>
      </table>
    </form>
  </div>
  <div class="modal-footer">
    <button class="btn" id="saveUserInfo">保存</button>
    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
  </div>
</div>



<div id="publicModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="myModalLabel">添加联系人信息2</h3>
  </div>
  <div class="modal-body">
    <form method="post" id="executePublicTxlFrm"  >
      <table class="B-table" >
         <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			 	<input type="text" kind="text" fieldname="TXID" name="TXID" id="TXID">
			</TD>
        </TR>
        <tr>
          <th width="30%" class="right-border bottom-border">姓名</th>
          <td class="right-border bottom-border">
          	<input type="text" placeholder="必填"  check-type="required" fieldname="XM" name="XM" id="XM"></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">昵称</th>
          <td class="right-border bottom-border">
          	<input type="text" placeholder="必填" fieldname="NC" name="NC" id="NC"></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">性别</th>
          <td class="right-border bottom-border">
          	<select type="text" placeholder="必填" fieldname="XB" name="XB" id="XB" kind="dic" src="XB"></select></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">出生日期</th>
          <td class="right-border bottom-border">
          	<input type="date" placeholder="必填" fieldname="CSRQ" name="CSRQ" id="CSRQ" fieldtype="date" fieldformat="YYYY-MM-DD" /></td>
        </tr>
        <tr>
          <th width="30%" class="right-border bottom-border">单位</th>
          <td class="right-border bottom-border">
          	<!-- <select type="text" placeholder="必填"  check-type="required" fieldname="DW" name="DW" id="DW" ></select> -->
          	<input type="text" placeholder="必填" fieldname="DW" name="DW" id="DW" />
          </td>
        </tr>
      </table>
    </form>
  </div>
  <div class="modal-footer">
    <button class="btn" id="savePublicUserInfo">保存</button>
    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
  </div>
</div>

</body>
</html>