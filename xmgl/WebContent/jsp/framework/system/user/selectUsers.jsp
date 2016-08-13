<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%@ page import="com.ccthanking.framework.util.Pub"%>
		
		<app:base />
		<%
			String str = request.getParameter("checkNodeString");
		    String str_name ="";
		    if(str!=null&&!"".equals(str)){
		    	if(str.indexOf(",")>-1){
		    		String[] s = str.split(",");
		    		for(int i=0;i<s.length;i++){
		    			str_name +=Pub.getUserNameByLoginId(s[i])+",";
		    		}
		    		if(str_name.length()>0){
		    			str_name = str_name.substring(0,str_name.length()-1);
		    		}
		    		
		    	}else{
		    		str_name = Pub.getUserNameByLoginId(str);
		    	}
		    }
			String callback = request.getParameter("callback");
			String type = request.getParameter("type");
			str = str==null || str==""?"":str;
			str_name = str_name==null || str_name==""?"":str_name;
			callback = callback==null ? "":callback;
			String dept = request.getParameter("dept");
			dept = dept==null ? "":dept;
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
					//	alert('1')
						 $('.selectAddBox tr.selectAddBox-active').removeClass('selectAddBox-active');
						 $(this).addClass('selectAddBox-active');
					},
					dblclick:function(){
						var rowJson = $(this).attr("rowJson");
						rowJson = eval("(" + rowJson +")");
						var $hs = $("#hs");
						var ul_obj = $hs.find("ul");
						var ul_html = "<ul class=\"unstyled\" account='"+rowJson.account+"'  username='"+rowJson.name+"'><li><i class=\"icon-user\"></i>"+rowJson.name+"</li></ul>";
						if(type=="single"){
							if(ul_obj.length>0){
								for(var i =0;i<ul_obj.length;i++){
									$(ul_obj[i]).remove();
								}
							}
							$hs.html(ul_html);
						}else{
							if(ul_obj.length>0){
								var nohas = true;
								for(var i =0;i<ul_obj.length;i++){
									var account = $(ul_obj[i]).attr("account");
									if(rowJson.account==account){
										nohas = false;
									}
								}
								if(nohas==true){
								  $(ul_obj[ul_obj.length-1]).after(ul_html);
								}
							}else{
								$hs.html(ul_html);
							}
						}
						$('.unstyled li').click(function(e) {
					        $('.unstyled li.selectAddBox-active').removeClass('selectAddBox-active');
							$(this).addClass('selectAddBox-active');
					    });
						$('.unstyled li').dblclick(function(e) {
                              $(this.parentElement).remove();
					    });
					}
				});
				
			}
		}
	});
}
			var controllername= "${pageContext.request.contextPath }/deptController.do";
			var checkNodeString = "<%=str %>";
			var checkNodeNameString = "<%=str_name %>";
			var dept = "<%=dept%>";
			var type = "<%=type%>";
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
			//点击保存按钮
			$(function() {
				$("#btnSave").click(function() {
					var $hs = $("#hs");
					var data = new Array();
					var ul_obj = $hs.find("ul");
					for(var i =0;i<ul_obj.size();i++){
						var obj = new Object();
						obj.ACCOUNT = ul_obj.eq(i).attr("account");
						obj.USERNAME = ul_obj.eq(i).attr("username");
						data.push(obj);
					}
					if(data.length==0){
						xAlert("提示信息","请选择人员！",'3');
						return
					}
					if(callback!=""){
						$(window).manhuaDialog.getParentObj().eval(callback+"("+JSON.stringify(data)+")");
					}else{
						$(window).manhuaDialog.setData(data);
						$(window).manhuaDialog.sendData();
					}
					$(window).manhuaDialog.close();
				});
				//清空按钮事件
				$("#btnClear").click(function(){
					var $hs = $("#hs");
					var ul_obj = $hs.find("ul");
					for(var i =0;i<ul_obj.size();i++){
						ul_obj.eq(i).remove();
					}
				});
				$("#btnCancel").click(function(){
					$(window).manhuaDialog.close();
				});
			});
			$(document).ready(function() {
				doInit();
				showSelect();
			});
			function doInit(){
					$.fn.zTree.init($("#myTree"), settingSingle);

			}
			function showSelect(){
				if(checkNodeString!=""){
					var ul_html  ="";
					checkNodeString = checkNodeString.split(",");
					checkNodeNameString = checkNodeNameString.split(",");
					if(checkNodeString.length>0){
						for(var i=0;i<checkNodeString.length;i++){
						  ul_html += "<ul class=\"unstyled\" account='"+checkNodeString[i]+"' username='"+checkNodeNameString[i]+"'><li><i class=\"icon-user\"></i>"+checkNodeNameString[i]+"</li></ul>";
						}
					}
					$("#hs").html(ul_html);
					$('.unstyled li').click(function(e) {
				        $('.unstyled li.selectAddBox-active').removeClass('selectAddBox-active');
						$(this).addClass('selectAddBox-active');
				    });
					$('.unstyled li').dblclick(function(e) {
                          $(this.parentElement).remove();
				    });
				}
			}
			
			
			
		</script>
<div class="selectAddContainer">
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span8">
                <div class="B-small-from-table-box">
                    <h4>人员列表：</h4>
                    <div class="selectAddBox">
                        <div class="container-fluid">
                            <div class="row-fluid">
                                <div class="span4">
                                <div align="center">
						<div class="zTreeDemoBackground left"
							style="height: 100%; width: 98%;">
							<ul id="myTree" class="ztree"
								style="height: 100%; width: 100%; border: 0; background-color: #FFFFFF; overflow: auto;">
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
            <div class="span1">
                <div class="selectAddBtnBox">
                    <button class="btn-block" disabled>双击-&gt;</button>
                    <button class="btn-block" disabled>双击&lt;-</button>
                </div>
               
            </div>
            <div class="span3">
                <div class="B-small-from-table-box">
                    <div class="selectAddRightBox">
                         <h4>已选择：</h4>
                    <div class="selectAddRight" id="hs">
                	
                    </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid text-center">
        	<button class="btn" id="btnSave">确定</button> 
        	<button class="btn" id="btnClear">清空 </button> 
        	<button class="btn" id="btnCancel">取消</button>
    </div>
            <p> </p>
            <p> </p>
</div>

</body>		
</html>