<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title>insertDemo</title>

		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/demo.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/zTreeStyle.css"
			type="text/css">
		<script type="text/javascript"
			src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.core-3.5.min.js"></script>
		<style type="text/css">
div#rMenu {
	position: absolute;
	visibility: hidden;
	top: 0px;
	/**background-color: #555;**/
	background-color: #DFDFDF;
	text-align: left;
	padding: 2px;
	width:150px;
}

div#rMenu ul li {
	margin: 5px 2px 5px 2px;
	padding: 0 5px;
	cursor: pointer;
	list-style: none outside none;
	background-color: #DFDFDF;
	color:blue;
}

th {
	font-weight: bold;
}

th {
	display: table-cell;
	vertical-align: inherit;
}

table {
	border-collapse: collapse;
	border-spacing: 0;
}

table {
	border-spacing: 2px;
	border-color: gray;
	font-family:Microsoft YaHei,"Helvetica Neue", Helvetica, Arial, sans-serif;
}

</style>
	</head>
	<body>
		<app:dialogs />

		<div class="container-fluid">
			<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-header" style="background: #0866c6;">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">
						<i class="icon-remove icon-white"></i>
					</button>
					<h3 id="myModalLabel" style="color: white;">
						请输入项目分类名称
					</h3>
				</div>
				<div class="modal-body">
					<table class="B-table">
						<tr>
							<th width="30%" class="right-border bottom-border">
								项目分类名称
							</th>
							<td class="right-border bottom-border">
								<input type="text" placeholder="" id="processname">
							</td>
						</tr>
					</table>
				</div>
				<div class="modal-footer">
					<button class="btn" id="saveName">
						确定
					</button>
					<button class="btn" data-dismiss="modal" aria-hidden="true">
						关闭
					</button>
				</div>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span4">
				<div class=" left">
					<span style="font-size:10px;">选中要操作的节点右键，即可操作菜单</span>
						<select class="span2 year" style="width: 17%" id="ND" name="ND"  fieldname="ND" operation="=" noNullSelect ="true"></select>
					<span class="pull-right"> <!-- 	<button class="btn" id="refreshMenuCache">清除菜单缓存</button> -->
						<button class="btn" id="btnAddRoot">
							新增根节点
						</button> 
						
					</span>
					<ul id="menuTree" class="ztree" style="width: 420px; height: 500px">
						<img alt="请稍后，正在加载数据……"
							src="${pageContext.request.contextPath }/img/loading.gif" />
					</ul>
				</div>
				<div id="rMenu">
					<ul>
						<li id="m_add" onclick="addXm();">
							增加子节点
						</li>
						<li id="m_add" onclick="editXm();">
							修改节点
						</li>
						<li id="m_del" onclick="deleteXm();">
							删除节点
						</li>
					</ul>
				</div>
			</div>
			<div class="span8">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title" id="h4">
						项目信息
					</h4>
     <form method="post" action="${pageContext.request.contextPath }/insertdemo.do" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			<INPUT id="GC_CJJH_ID" type="text" class="span12" keep="true" kind="text" fieldname="GC_CJJH_ID"  operation="="/>
			</TD>
        </TR>
      </table>
      </form>
      <div style="height:5px;"> </div>
  <div class="overFlowX">                                
<table  width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="结算管理">
	<thead>
		<tr>
		    <th  name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
		    <th fieldname="XH">&nbsp;序号&nbsp;</th>
		    <th fieldname="MXS" maxlength="20">&nbsp;项目数&nbsp;</th>
		    <th fieldname="ZXSM" maxlength="20">&nbsp;子项数目&nbsp;</th>
		 	<th fieldname="XMMC" maxlength="20">&nbsp;项目名称&nbsp;</th>
		 	<th fieldname="ND" tdalign="center">&nbsp;施工年度&nbsp;</th>
		 	<th fieldname="XMDZ" maxlength="20">&nbsp;建设位置&nbsp;</th>
		</tr>
	</thead>
	 <tbody>
     </tbody>
</table>
</div>
</div>
</div>
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" order="asc" fieldname=""/>
         <input type="hidden" name="resultXML" id = "resultXML">
          <input type="hidden" name="resultEdit" id = "resultEdit">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/gcCjjhController.do";
var nd = "";
var menuTreeJson="";
var g_defaultOpenNodeID = "";
var zTree,rMenu,menuTreeJson,operatorSign;
var setting="";
$(document).ready(function() {
	generateNd($("#ND"));
	setDefaultNd("ND");	
	//alert($("#ND").val())
	set();
	$.fn.zTree.init($("#menuTree"), setting);
	zTree = $.fn.zTree.getZTreeObj("menuTree");
	rMenu = $("#rMenu");
});

function generateNd(ndObj){
	ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}
function set()	{
	setting = {
		async: {
			enable: true,
			url: getAsyncUrl(),
			autoParam: ["id"],
			dataFilter: function (treeId, parentNode, responseData) {
	            return responseData;
	        }
		},
		view: {
			dblClickExpand: true,
			nameIsHTML: true
		},
		data: {
			simpleData: {
				enable: true,
				idKey: "id",
				pIdKey: "parentId"
			}
		},
		callback: {
			onClick  :queryByID ,
			onRightClick  : showMenuValue
		}
	};
}
//点击查询列表
function queryByID(event, treeId, treeNode){
	isXm();
	menuTreeJson=treeNode;
	doSetFormValue(treeNode);
	queryXMbyId(treeNode.id);
}
//显示项目信息
function isXm(){
	$("#insertForm").hide();
	$("#btnSave").hide();
	$("#btnAdd").show();
	$("#btnEdit").show();
	$("#btnDelete").show();
	$("#DT1").show();
}
//点击查询项目
function queryXMbyId(cjjhid){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryXMbyId&cjjhid="+cjjhid,data,DT1,null,true);
}
	//点击保存按钮
	$(function() {
		$("#XMSX").val("1");
		$("#btnSave").click(function() {
			if($("#PARENTID").val()=="") {
				xInfoMsg("请选择一个父节点进行添加","");
				return;
			}
		});
		$('#ND').change(function(){
			nd = $("#ND").val();
			freshMenu();
		});
		$("#btnAddRoot").click(function(){
			$("#NODELEVEL").val("0");
			$("#PARENTID").val("root");
		});
		$("#btnAddRoot").click(function (){
			hideRMenu();
			//页面信息清空，方便开始输入栏目信息
			$(window).manhuaDialog({"title":"新增根节点","type":"text","content":"${pageContext.request.contextPath}/jsp/business/cjjcssxmjsjh/cjjcAddRoot.jsp","modal":"3"});				
		});
	});
	//节点添加回调
	function jiedianhuiadd(redata,flag){
		var resultmsgobj = convertJson.string2json1(redata);
		var resJson = resultmsgobj.response.data[0];
		var ids="";
		if("1"==resJson.ISXM){
			if("1"==flag){
				ids=resJson.PARENTID;	
			}else{
				ids=resJson.GC_CJJH_ID;
			}
			queryXMbyId(ids);
		}
		setting.async.url = getAsyncUrl()+"&id="+resJson.GC_CJJH_ID;
		$.fn.zTree.init($("#menuTree"), setting);
		setting.async.url = getAsyncUrl();
		xAlert("提示信息","操作成功！");
	}
	//根节点添加回调
	function rootadd(redata){
		var resultmsgobj = convertJson.string2json1(redata);
		var resJson = resultmsgobj.response.data[0];
		generateNd($("#ND"));
		setDefaultNd("ND");	
		$("#ND").val(resJson.ND);		
		freshMenu();
		xAlert("提示信息","操作成功！");
	}
	function getAsyncUrl(){
		nd=$("#ND").val();
		return controllername + "?getProjectfl"+g_defaultOpenNodeID+"&nd="+nd;
	}
	function getXmData(obj){
		var tempJson = convertJson.string2json1(obj);
		$("#insertForm input").each(function(){
			var elem = $(this);
			if(elem.attr("fillValue")!=undefined && elem.attr("fillValue")!=""){
				elem.val(tempJson[elem.attr("fillValue")]);
			}
		});
	}
	//----------------------
	//-左侧树点击时，刷新表单数据
	//-
	//----------------------
	function doSetFormValue(obj){
		$("#insertForm").clearFormResult();
		$.ajax({
			url:controllername + "?getNodeDetail&id="+obj.id,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				var resultmsg = result.msg;
				if(resultmsg=="0"){
					xInfoMsg('无数据！',null);
					return;
				}else{
					var resultmsgobj = convertJson.string2json1(resultmsg);
					var resJson = resultmsgobj.response.data[0];
					$("#resultEdit").val(JSON.stringify(resJson));
				}
			}
		});
	}
	//----------------------
	//选择项目
	//----------------------
	function openXmList(callBack){
		if(callBack!=undefined && callBack!=""){
			callBack = "?callBack="+callBack;
		}
		$(window).manhuaDialog({"title":"项目列表","type":"text","content": "${pageContext.request.contextPath}/jsp/business/cjjcssxmjsjh/xmcx.jsp"+callBack,"modal":"4"});
	}
/**
 * 点击右键时，获取的菜单信息
 */
function showMenuValue(event, treeId, treeNode) {
	queryByID(event, treeId, treeNode);
	menuTreeJson = treeNode;
	if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
		// 取消选中节点
		zTree.cancelSelectedNode();
		showRMenu("root", event.clientX-20, event.clientY+10);
	} else if (treeNode && !treeNode.noR) {
		// 设置选中节点
		zTree.selectNode(treeNode);
		showRMenu("node", event.clientX-20, event.clientY+10);
	}
}

/**
 * 显示右键按钮
 * type	 
 * x	右键时所在的x坐标
 * y	右键时所在的y坐标
 */
function showRMenu(type, x, y) {
//	alert("showRMenu : " + type);
	$("#rMenu ul").show();
		if (type=="root") {
		$("#m_add").hide();
		$("#m_imp").hide();
		$("#m_reset").hide();
		$("#m_del").hide();
	} else {
		var ly = menuTreeJson.level;
		// 根节点只能添加
		$("#m_add").show();
		$("#m_del").show();
	}
	rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

	$("body").bind("mousedown", onBodyMouseDown);
}

/**
 * 隐藏右键按钮
 */
function hideRMenu() {
	if (rMenu) rMenu.css({"visibility": "hidden"});
	$("body").unbind("mousedown", onBodyMouseDown);
}

function onBodyMouseDown(event) {
	if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
		rMenu.css({"visibility" : "hidden"});
	}
}


/**
 * 添加按钮方法
 */
function addXm() {
	hideRMenu();
	//页面信息清空，方便开始输入栏目信息
	$(window).manhuaDialog({"title":"新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/cjjcssxmjsjh/cjjcXmAdd.jsp","modal":"2"});
}
/**
 * 修改节点
 */
function editXm() {
	hideRMenu();
	doSetFormValue(menuTreeJson);
	if(menuTreeJson.level=='0')
	{
		$(window).manhuaDialog({"title":"修改根节点","type":"text","content":"${pageContext.request.contextPath}/jsp/business/cjjcssxmjsjh/cjjcUpdateRoot.jsp","modal":"3"});
	}
	else
	{
		$(window).manhuaDialog({"title":"修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/cjjcssxmjsjh/cjjcXmEdit.jsp","modal":"2"});
	}	
}
function deleteXm(){
	hideRMenu();
	/* isJd(); */
	var flag = hasChild();
	if(flag){
		//如果flag为真，那么表示当前节点下还有子节点，不能删除
		xInfoMsg('本节点下还存在子节点，不允许删除！');
		return;
	}
	var a=JSON.stringify(menuTreeJson)	
	xConfirm("提示信息","是否确认删除节点："+menuTreeJson.name);
	$('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').one("click",function(){
		$.ajax({
			url : controllername+"?doDeleteNode&id="+menuTreeJson.id,
			cache : false,
			async : false,
			dataType : 'json',
			type:"post",
			success : function(response) {
				var msg = response.msg;
				if(msg=="1"){
					xAlert("操作成功");
					$.fn.zTree.init($("#menuTree"), setting);
					if(menuTreeJson.level=='0')
					{
						generateNd($("#ND"));
						setDefaultNd("ND");	
						freshMenu();
					}
				}else{
					xInfoMsg('操作失败！');
				}
			}
		});
	});
}
//是否有子节点
function hasChild(){
	var flag = false;
	var treeObj = $.fn.zTree.getZTreeObj("menuTree");
	var nodes = treeObj.getSelectedNodes();
	var obj = nodes[0];
	if(obj!=null&&obj.children!=null&&obj.children.length!=null&&obj.children.length>0){
		flag = true;
	}
	return flag;
}

function freshMenu() {
	set();
	$.fn.zTree.init($("#menuTree"), setting);
	hideRMenu();
}
function closeParentCloseFunction(){
	//$.fn.zTree.init($("#menuTree"), setting);
	hideRMenu();
}

//年份查询
function generateNd(ndObj){
	ndObj.attr("src","T#GC_CJJH:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}
// 检测菜单信息是否重复
var sign;
$(function() {
	var menuName = $("#NAME");
	menuName.blur(function() {
		$.ajax({
			url : controllername+"?queryUnique&menuName="+menuName.val(),
			cache : false,
			async : false,
			dataType : 'json',
			type:"post",
			success : function(response) {
				var result = eval("(" + response + ")");
				// 设置添加时得校验标识，1表示数据重复
				sign = result.sign;
				//xAlert("提示信息", result.isUnique);
				xInfoMsg(result.isUnique,"");
			}
		});
	});
});


</script>

	</body>
</html>