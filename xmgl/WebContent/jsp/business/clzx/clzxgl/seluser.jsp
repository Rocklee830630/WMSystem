<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%@ page import="com.ccthanking.framework.util.Pub"%>
		
		<app:base />
		<%
		    String str_name ="";
			String str="";
			String dept="";
			String callback ="";
		%>
		<title>用户选择界面</title>

		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/demo.css"
			type="text/css">
		<link rel="stylesheet"
			href="${pageContext.request.contextPath }/css/ztree_css/zTreeStyle.css"
			type="text/css">
		<script type="text/javascript"
			src="${pageContext.request.contextPath }/js/ztree/jquery.ztree.all-3.5.min.js"></script>
		<style type="text/css">
			div#rMenu {
				position: absolute;
				visibility: hidden;
				top: 0;
				background-color: #555;
				text-align: left;
				padding: 2px;
			}
			
			div#rMenu ul li {
				margin: 1px 0;
				padding: 0 5px;
				cursor: pointer;
				list-style: none outside none;
				background-color: #DFDFDF;
			}
		</style>
	</head>
	<body>
<body>
<script type="text/javascript">
//生成部门人员列表
function query(event, treeId, treeNode){
	var dept = treeNode.id;
	$.ajax({
		url: "${pageContext.request.contextPath }/userController.do?loadDeptUser&dept="+dept,
		data : "",
		dataType : 'json',
		async :	true,
		type : 'post',
		success : function(result) {
			var $tab = $("#DT1");
			var tr_obj = $tab.find("tr");
			for(var i =1;i<tr_obj.length;i++){
				tr_obj[i].remove();
			}			
			if(result&&result.length>0){
			    var t_html = "";
				for(var i=0;i<result.length;i++){
					var obj = result[i];
					t_html +="<tr rowJson='"+JSON.stringify(result[i])+"'><td><i class=\"icon-user\"></i>"+obj.account+"</td><td>"+obj.name+"</td><td></td></tr>";
				}
				$(tr_obj[0]).after(t_html);
				var tabTab = $("#DT1").find('tr').bind({
					click:function(){
						 $('.selectAddBox tr.selectAddBox-active').removeClass('selectAddBox-active');
						 $(this).addClass('selectAddBox-active');
					},
					dblclick:function(){
						var rowJson = $(this).attr("rowJson");
						var a=rowJson;
						rowJson = eval("(" + rowJson +")");
						var div_obj = $("#frame1",parent.document.body).contents().find("#nameall").find("div");
						var table_obj = $("#frame1",parent.document.body).contents().find("#nameall").find("table");
						
						var div_title='<table class="B-table"  style="width: 100%">	<th colspan="10"><span class="pull-left" style="font-size:16px;">长春市政府投资建设项目管理中心</span><button style="float:right;"class="btn" id="btnSave">保存11</button> </th></table>';
						var div_html = "<div style=\'border:1px solid #DDDDDD;margin-right:5px;margin-top:10px;width: 30%;height:18%;float:left;text-align:center;\' account='"+rowJson.account+"'  username='"+rowJson.name+"'><font style=\'font-size:14px;color:#000000;\'>"+rowJson.name+"</font><i style=\'float:right;\'class=\'icon-remove\''></i></div>";
						if(div_obj.length>0)
						{
							var nohas = true;
							for(var i =0;i<div_obj.length;i++)
							{
								var account = $(div_obj[i]).attr("account");
								if(rowJson.account==account)
								{
									nohas = false;
								}
							}
							if(nohas==true)
							{			
							  $(div_obj[div_obj.length-1]).after(div_html);
							}
						}
						else
						{
							$(table_obj[table_obj.length-1]).after(div_html);
						}
						$("#frame1",parent.document.body).contents().find("#nameall").find("i").click(function(e) {
		                    $(this.parentElement).remove();
					    });
					}
				});
				
			}
		}
	});
}
var controllername= "${pageContext.request.contextPath }/deptController.do";
var dept = "<%=dept%>";
var callback = "<%=callback%>";
var settingSingle = {

	async: {
		enable: true,
		url: controllername + "?loadAllDept&dept="+dept,
		autoParam: ["id"],
		dataFilter: function (treeId, parentNode, responseData) {
            return responseData;
        }
	},
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parent_Id",
		}
	},
	view:{
		showLine :false
	},
	callback: {
		onClick  : query
	}
};

$(document).ready(function() {
	doInit();
});
function doInit(){
	$.fn.zTree.init($("#myTree"), settingSingle);
	setPageHeight();
}
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle;
	$("#myTree").css("height",g_iHeight-pageQuery-18);
};
	
</script>
<div class="selectAddContainer" style="height: 100%;width:100%;">
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span8" style="height: 100%;width:100%;">
                <div class="B-small-from-table-box">
                    <h4>人员列表：</h4>
                    <div class="selectAddBox" style="height: 100%;overflow: hidden;">
                        <div class="container-fluid">
                            <div class="row-fluid">
                                <div class="span4">
	                                <div align="center">
										<div class="zTreeDemoBackground left" style="height: 100%; width: 100%;">
											<ul id="myTree" class="ztree" style=" border: 0; background-color: #FFFFFF; overflow: hidden;">
												<img alt="请稍后，正在加载数据……"
													src="${pageContext.request.contextPath }/img/loading.gif" />
											</ul>
										</div>
						         	</div>
                                </div>
                                <div class="span8 selectAddBorderLeft">
                                   <table class="table" id="DT1">
                                		<tr>
                                        <th>帐号</th>
                                        <th>姓名</th>
                                        <th>岗位</th>
                                        </tr>
                                </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

</body>		
</html>