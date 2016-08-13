<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%String treeid = request.getParameter("treeid"); %>
		<app:base />

		<title>实例页面-角色信息查询</title>

		<script type="text/javascript" charset="UTF-8">
	var json,rowindex,rowValue;
	var p_treeid = '<%=treeid%>';
	var controllername= "${pageContext.request.contextPath }/roleController.do";
  	//计算本页表格分页数
  	function setPageHeight() {
  		var getHeight=getDivStyleHeight();
  		var height = getHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight;
  		var pageNum = parseInt((height/pageTableOne)/2,10);
  		$("#DT1").attr("pageNum",pageNum);
  	}
	function doInit() {
		var roleType = getExistsNodeID();
		$("#QROLETYPE").val(roleType);
		g_bAlertWhenNoResult = false;
		doSearch();
        g_bAlertWhenNoResult = true;
	}
	function doSearch(){
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		defaultJson.doQueryJsonList(controllername+"?queryRole", data, DT1);
	}
	function getExistsNodeID(){
		var roleType = "";
		$.ajax({
			url:controllername + "?getExistsNodeID&parentID="+p_treeid,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				var res = result.msg;
				var tempJson = convertJson.string2json1(res);
				roleType = tempJson.TYPE;
				/**
				* not in条件由后台处理了，这段代码暂时无用
				var idStr = tempJson.NODEID;
				if(idStr.indexOf(",")!=-1){
					//idStr = "('"+idStr.replace(/\,/g,"','")+"')";
				}
				*/
				$("#QNODEID").val(tempJson.NODEID);//这个是查询条件
				$("#nodeIDExists").val(tempJson.NODEID);//这个是用于每次上移下移时使用的变量
			}
		});
		return roleType;
	}
	// 点击添加按钮 清空表单
	$(function() {
		setPageHeight();
		doInit();
		$("#insertBtn").click(function() {
			// 当添加用户信息时，解除ID为【ACCOUNT】的disabled属性
			$("#executeFrm").clearFormResult(); 
		});
		$("#query_clear").click(function() {
			$("#queryForm").clearFormResult();
		});
		$("#queryBtn").click(function() {
	        doSearch();
		});
		$("#btnFH").click(function(){
			var idStr = "";
			$("#DT2 tbody tr").each(function(i){
				var obj = $("#DT2").getRowJsonObjByIndex(i);
				idStr += obj.ROLE_ID+","
			});
			if(idStr==""){
				xInfoMsg('请至少选中一个角色！');
				return;
			}else{
				$("#nodeIDResult").val(idStr.substring(0,idStr.length-1));
				var data = new Object();
				data.NODEID = $("#nodeIDResult").val();
				data.PARENTID = p_treeid;
				var jsonObj = new Object();
				jsonObj.msg = JSON.stringify(data);
				$.ajax({
					url:controllername + "?doMoveInTreeNode&parentID="+p_treeid,
					data:jsonObj,
					dataType:"json",
					async:false,
					success:function(result){
						$(window).manhuaDialog.getParentObj().showSuccess();
						$(window).manhuaDialog.close();
					},
					error: function(result) {
						xInfoMsg('添加失败，请联系系统管理员！');
					}
				});
			}
			
		});
		//按钮绑定事件(全选)
		$("#all").click(function() {
			if($("#DT1 tr").length==1){
				xInfoMsg('没有可移动的角色！');
				return;
			}
			//判断是否有相同的项目
			$("#DT1 tbody tr").each(function(){
			var isinsert = true;
			var rowValue=convertJson.string2json1($(this).attr("rowJson"));
			var dtid = rowValue.ROLE_ID;
			$("#DT2 tbody tr").each(function(){
				var rowValue1=convertJson.string2json1($(this).attr("rowJson"));
				var dtid2=rowValue1.ROLE_ID;
				if(dtid==dtid2){
					isinsert = false;
					return
				}
			});
			if(isinsert){
				$("#DT2").insertResult(JSON.stringify(rowValue),DT2,1);
			}
			});
			$("#DT1").clearResult();
			trlen=$("#DT2 tr").length-1;
			var num=document.getElementsByTagName("font");
			num[0].innerHTML=trlen;
		});
	});
	//---------------------------------------
	//-行双击事件
	//---------------------------------------
	function tr_dbclick(obj,tableID){
		if(tableID=="DT1"){
			rowDown(obj);
		}else if(tableID=="DT2"){
			rowUp(obj)
		}
	}
	//行加下移按钮
	function doDwon(obj){
		return "<a href='javascript:void(0);' name='btnDownTable' title='点击下移' onclick='rowDown(this)' ><i class='icon-chevron-down'></i></a>";
	}
	//行加上移按钮
	function doUp(obj){
		return "<a href='javascript:void(0);' name='btnUpTable' title='点击下移' onclick='rowUp(this)' ><i class='icon-chevron-up'></i></a>";
	}
	//下移按钮事件
	function rowDown(obj){
		var rowIndex = $(event.target).closest("tr").index();
		$("#DT1").setSelect(rowIndex);
		var chongfu=chongfupanduan();
		if(chongfu==false){
			xInfoMsg('角色已添加！');
			return 
		}
		//var rowIndex = $(obj).parent().parent().index();
		var rowValue = $(event.target).closest("tr").attr("rowJson");
		$("#DT2").insertResult(rowValue,DT2,1);
		$("#DT1").removeResult(rowIndex);
		trlen = $("#DT2 tr").length-1;
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = trlen;
		getAllExistsNodeID();
	}
	//上移按钮事件
	function rowUp(obj){
		var rowIndex = $(event.target).closest("tr").index();
		$("#DT2").setSelect(rowIndex);
		var sycf=xiayichongfupanduan();
		var rowValue = $(event.target).closest("tr").attr("rowJson");
		if(sycf){
			$("#DT1").insertResult(rowValue,DT1,1);
		}
		$("#DT2").removeResult(rowIndex);
		trlen = $("#DT2 tr").length-1;
		var num = document.getElementsByTagName("font");
		num[0].innerHTML = trlen;
		getAllExistsNodeID();
	}
	function getAllExistsNodeID(){
		var idStr = "";
		$("#DT2 tbody tr").each(function(){
			var dt2id = convertJson.string2json1($(this).attr("rowJson")).ROLE_ID;
			idStr += dt2id+",";
		});
		idStr += $("#nodeIDExists").val();
		if(idStr.lastIndexOf(",")==idStr.length-1){
			idStr = idStr.substring(0,idStr.length-1);
		}
		$("#QNODEID").val(idStr);
		
		var data = combineQuery.getQueryCombineData(queryForm,frmPost, DT1);
		data = defaultJson.getQueryConditionWithNowPageNum(data,"DT1");
		defaultJson.doQueryJsonList(controllername+"?queryRole", data, DT1);
	}
	//下移重复判断
	function chongfupanduan(){
		var success =true;
		var json=$("#DT1").getSelectedRow();
		var odd = convertJson.string2json1(json);
		var id=$(odd).attr("ROLE_ID");
		$("#DT2 tbody tr").each(function(){
			var dt2id = convertJson.string2json1($(this).attr("rowJson")).ROLE_ID;
			if(id==dt2id){
				success=false;
			}
		});
		return success;
	}
	//上移重复判断
	function xiayichongfupanduan(){
		var success =true;
		var json=$("#DT2").getSelectedRow();
		var odd = convertJson.string2json1(json);
		var id=$(odd).attr("ROLE_ID");
		$("#DT1 tbody tr").each(function(){
			var dtid1 = convertJson.string2json1($(this).attr("rowJson")).GC_JH_SJ_ID;
			if(id==dtid1){
				success=false;
			}
		});
		return success;
	}
</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD>
									<input type="text" class="span12" kind="text"
										fieldname="rownum" value="1000" operation="<=">
								</TD>
								<th width="5%" class="right-border bottom-border text-right">
									已存在ID
								</th>
								<td width="10%" class="right-border bottom-border">
									<input class="span12" type="text" FIELDTYPE="text" placeholder="" name="ROLE_ID"
										fieldname="ROLE_ID" operation="not in" logic="and" id="QNODEID">
								</td>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>
								<th width="5%" class="right-border bottom-border text-right">
									角色名称
								</th>
								<td width="10%" class="right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="NAME"
										fieldname="NAME" operation="like" logic="and">
								</td>
								<th width="5%" class="right-border bottom-border">
									角色类别
								</th>
								<td width="10%" class="right-border bottom-border">
									<select name="QROLETYPE" fieldname="ROLETYPE" id="QROLETYPE"
										kind="dic" src="JSLB" operation="=" logic="and"
										defaultmemo="全部" disabled>

									</select>
								</td>
								<td width="40%" class="bottom-border text-right">
									<button id="queryBtn" class="btn btn-link" type="button">
										<i class="icon-search"></i>查询
									</button>
									<button id="query_clear" class="btn btn-link" type="button">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>

						</table>
					</form>

					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table width="100%" class="table-hover table-activeTd B-table"
							id="DT1" type="single">
							<thead>
								<tr>
									<th name="XH" id="_XH">
										&nbsp;#&nbsp;
									</th>
									<th fieldname="ROLE_ID" tdalign="center" CustomFunction="doDwon">&nbsp;操作&nbsp;</th>
									<th fieldname="NAME">
										&nbsp;角色名称&nbsp;
									</th>
									<th fieldname="ROLETYPE">
										&nbsp;角色类别&nbsp;
									</th>
									<th fieldname="S_MEMO">
										&nbsp;汉字描述&nbsp;
									</th>
									<th fieldname="GXSJ" tdalign="center">
										&nbsp;录入时间&nbsp;
									</th>
									<th fieldname="GXR">
										&nbsp;录入人&nbsp;
									</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
				<div align="center">
					<button id="all" class="btn" type="button" disabled>
						<i class="icon-plus"></i>&nbsp;全部下移
					</button>
					<button id="btnUp" class="btn" type="button" disabled>
						<i class="icon-chevron-up"></i>上移
					</button>
					<button id="btnDown" class="btn" type="button" disabled>
						<i class="icon-chevron-down"></i>下移
					</button>
					<button id="cannel" class="btn" type="button" disabled>
						<i class="icon-minus"></i>&nbsp;全部上移
					</button>
				</div>
				<div class="B-small-from-table-autoConcise">
					<h4>
						已选择项目列表&nbsp;&nbsp; 共
						<font
							style="margin-left: 5px; margin-right: 5px;; font-size: 28px; color: red;">0</font>个
						<span class="pull-right">
							<button id="btnFH" class="btn" type="button">
								确定
							</button> <!-- <button id="btnXdt" class="btn" type="button">下达</button> -->
						</span>

					</h4>
					<div style="height: 200px; overflow: auto;">
						<table class="table-hover table-activeTd B-table" id="DT2"
							width="100%" type="single" noPage="true" pageNum="1000">
							<thead>
								<tr>
									<th name="XH" id="_XH">
										&nbsp;#&nbsp;
									</th>
									<th fieldname="ROLE_ID" tdalign="center" CustomFunction="doUp">&nbsp;操作&nbsp;</th>
									<th fieldname="NAME">
										&nbsp;角色名称&nbsp;
									</th>
									<th fieldname="ROLETYPE">
										&nbsp;角色类别&nbsp;
									</th>
									<th fieldname="S_MEMO">
										&nbsp;汉字描述&nbsp;
									</th>
									<th fieldname="GXSJ" tdalign="center">
										&nbsp;录入时间&nbsp;
									</th>
									<th fieldname="GXR">
										&nbsp;录入人&nbsp;
									</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank" id="frmPost">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ"
					id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id="queryResult">
				<input type="hidden" name="nodeIDResult" id="nodeIDResult">
				<input type="hidden" name="nodeIDExists" id="nodeIDExists">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>