<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ page import="com.ccthanking.framework.common.User"%>
		<%@ page import="com.ccthanking.framework.Globals"%>
		<%@ page import="java.util.Date"%>
		<%@ page import="java.text.SimpleDateFormat"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<script src="${pageContext.request.contextPath }/js/common/bootstrap-paginator.js"></script>
		<title></title>
		<%
			User user = (User) request.getSession().getAttribute(
					Globals.USER_KEY);
			String userid = user.getAccount();
			String type = request.getParameter("type") == null ? "1" : request
					.getParameter("type");
			Date dt = new Date();
			SimpleDateFormat matter1 = new SimpleDateFormat("yyyy");
			String date = matter1.format(dt);
		%>
		<style type="text/css">
		.disabledA:hover {
			text-decoration: none;
			color: #AAAAAA;
			cursor: default
		}
		
		.disabledA {
			color: #AAAAAA;
			font-size: 12px;
		}
		
		.nomalA:hover {
			text-decoration: underline;
			cursor: pointer
		}
		
		.nomalA {
			color: #333333;
			font-size: 12px;
		}
		</style>
		<script type="text/javascript" charset="utf-8">
		//请求路径，对应后台RequestMapping
		var controllername = "${pageContext.request.contextPath }/ndxxgxController.do";
		var controllername_tz = "${pageContext.request.contextPath }/ggtzController.do";
		var userid = "<%=userid%>";
		var type = "<%=type%>";
		
		var nowDate =  "{\"logic\":\"and\",\"fieldname\":\"to_char(FBSJ,'yyyy')\",\"operation\":\"=\",\"value\":\""+<%=date%>+"\",\"fieldtype\":'',\"fieldformat\":''}";
		var xwcshquerycondition='';
		var ggcshquerycondition = "{\"logic\":\"and\",\"fieldname\":\"JSR_ACCOUNT\",\"operation\":\"=\",\"value\":\""+userid+"\",\"fieldtype\":'',\"fieldformat\":''}"
			+',{"logic":"and","fieldname":"GGLB","operation":"in","value":"GG,TZ","fieldtype":"text","fieldformat":""}';
		//页面初始化
		$(function() {
			//调用ajax插入
			var querydata = getQueryDateXw(nowDate);
			queryXw(querydata);
			//querydata = getQueryDateGg(nowDate+","+ggcshquerycondition);
			//queryGG(querydata);//delete by zhangbr@ccthanking.com
			//初始化年度
			var date = new Date();
			var dqn = date.getFullYear();
			$("#xwx").attr("title",dqn+1);
			$("#xws").attr("title",dqn-1);
			$("#xwnd").attr("value",dqn);
			//查询上一年和下一年有没有数据
			isndxwQuery($("#xws").attr("title"),"sn");
			isndxwQuery($("#xwx").attr("title"),"xn");
			// type为1时，默认显示通知公告
			if(type == "1") {
				$("#annouceId").attr("class", "active");
				$("#annouce").attr("class", "tab-pane active");
			// type为2或者为空时，默认显示中心新闻
			} else {
				$("#newsId").attr("class", "active");
				$("#news").attr("class", "tab-pane active");
			}
			//赋值当前年
			$("#ggdqnd").html(dqn);
			$("#xwdqnd").html(dqn);
				
		});
		//查询新闻
		function doQueryXW(type){
			var querycondition = "";
			if(type==1){ //快速查询
				var xwbt = $("#xwbtcx").val();
			    if(xwbt!=null&&xwbt!=""){
			    	querycondition = "{\"logic\":\"and\",\"fieldname\":\"XWBT\",\"operation\":\"like\",\"value\":\"%"+xwbt+"%\",\"fieldtype\":'',\"fieldformat\":''}";
			    }else{
			    	querycondition = xwcshquerycondition;
			    }
			}
			queryXw(getQueryDateXw(querycondition));
		}
		//查询公告
		function doQueryGG(type){
			var querycondition = "";
			if(type==1){ //快速查询
				var ggbt = $("#ggbtcx").val();
			    if(ggbt!=null&&ggbt!=""){
			    	querycondition = ggcshquerycondition+",{\"logic\":\"and\",\"fieldname\":\"GGBT\",\"operation\":\"like\",\"value\":\"%"+ggbt+"%\",\"fieldtype\":'',\"fieldformat\":''}";
			    }else{
			    	querycondition = ggcshquerycondition;
			    }
			}
			queryGG(getQueryDateGg(querycondition));
		}
		
		function getQueryDateGg(querydata){
			var xHeight = parent.document.documentElement.clientHeight;
			var height = xHeight-20-30-30-50;//窗口高度-<p>标签高度-tab页高度-th高度-分页高度
			//alert(height)
			var pageNum = parseInt(height/pageTableOne,10);
			var json = "{querycondition: {conditions: [" +querydata+" ]},pages:{recordsperpage:"+pageNum+", currentpagenum:1, totalpage:0, countrows:0},orders:[{\"fieldname\":\"fbsj\",\"order\":\"desc\"}]}";
			return json;
		}
		function getQueryDateXw(querydata){
			var xHeight = parent.document.documentElement.clientHeight;
			var height = xHeight-20-30-30-50;//窗口高度-<p>标签高度-tab页高度-th高度-分页高度
			var pageNum = parseInt(height/pageTableOne,10);
			var json = "{querycondition: {conditions: [" +querydata+" ]},pages:{recordsperpage:"+pageNum+", currentpagenum:1, totalpage:0, countrows:0},orders:[{\"fieldname\":\"fbsj\",\"order\":\"desc\"},{\"fieldname\":\"lrsj\",\"order\":\"desc\"}]}";
			return json;
		}
		function doQueryPage(div_id,resultJson){
			var b = convertJson.string2json1(resultJson);
			var recordsperpage = b.pages.recordsperpage;
			var currentpagenum = b.pages.currentpagenum;
			var totalpage = b.pages.totalpage;
			 $('#'+div_id).children().remove();
			var countrows = b.pages.countrows;
			if(totalpage>1){
				var options = {
					currentPage: currentpagenum,
					totalPages: totalpage,
					alignment:'center',
					tooltipTitles: function (type, page, current) {
					       switch (type) {
					       case "first":
					           return "首页";
					       case "prev":
					           return "上一页";
					       case "next":
					           return "下一页";
					       case "last":
					           return "末页";
					       case "page":
					           return "当前页 " + page;
					       }
					   },
					   onPageClicked: function(e,originalEvent,type,page){
					   	var tableobj = null;
					   	if(div_id=="xwfy"){
					   		tableobj = document.getElementById("XW");
					   	}else{
					   		tableobj = document.getElementById("GG");
					   	}
					   	var queryData = tableobj.getAttribute("queryData");
					   	queryData = convertJson.string2json1(queryData);
					   	queryData.pages.currentpagenum = page;
					   	if(div_id=="xwfy"){
					   		queryXw(JSON.stringify(queryData));
					   	}else{
					   		queryGG(JSON.stringify(queryData));
					   	}
					}
		            
				};
				$('#'+div_id).bootstrapPaginator(options);
			}
		}
		//新闻点击上一年下一年查询
		function ndxwQuery(obj){
			var querycondition = "";
			var nd = obj.title;
			$("#xwdqnd").html(nd);
			$("#xwnd").attr("value",nd);
			var dqn = $("#xwnd").attr("value");
			$("#xwx").attr("title",parseInt(dqn)+1);
			$("#xws").attr("title",parseInt(dqn)-1);
			var queryconditionND = "{\"logic\":\"and\",\"fieldname\":\"to_char(FBSJ,'yyyy')\",\"operation\":\"=\",\"value\":\""+nd+"\",\"fieldtype\":'',\"fieldformat\":''}";
			//判断是否有快速查找
			var xwbt = $("#xwbtcx").val();
		    if(xwbt!=null&&xwbt!=""){
		    	querycondition = queryconditionND+",{\"logic\":\"and\",\"fieldname\":\"XWBT\",\"operation\":\"like\",\"value\":\"%"+xwbt+"%\",\"fieldtype\":'',\"fieldformat\":''}";
		    }else{
		    	querycondition = queryconditionND;
		    }
			queryXw(getQueryDateXw(querycondition));
			isndxwQuery($("#xws").attr("title"),"sn");
			isndxwQuery($("#xwx").attr("title"),"xn");
		}
		//判断新闻下一年上一年是否有数据
		function isndxwQuery(nd,sxnd){
			$("#xwnd").attr("value",nd);
			var querycondition = "{\"logic\":\"and\",\"fieldname\":\"to_char(FBSJ,'yyyy')\",\"operation\":\"=\",\"value\":\""+nd+"\",\"fieldtype\":'',\"fieldformat\":''}";
			var data=isqueryXw(getQueryDateXw(querycondition));
			if(data=='0')
				{
					if(sxnd=="sn")
					{
					  $("#xws").removeAttr("onclick");
					  $("#xws").attr("class","disabledA");
					}
					else{
						 $("#xwx").removeAttr("onclick");
						  $("#xwx").attr("class","disabledA");
					}
				}
			else{
					if(sxnd=="sn")
					{
						 $("#xws").attr("onclick","ndxwQuery(this)");
						 $("#xws").attr("class","nomalA");
					}
					else{
						 $("#xwx").attr("class","nomalA");
						 $("#xwx").attr("onclick","ndxwQuery(this)");
					}
			}
		}
		//查询新闻 
		function isqueryXw(data1){
			var data2='';
			var data = {
					msg : data1
			};
			$.ajax({
				url : controllername + "?queryMoreNdxxgx" ,
				data : data,
				cache : false, 
				async : false,
				dataType : 'json',
				success : function(response){
					data2= response.msg;
				}
			});
			return data2;	
		} 
		function queryXw(data1){
			var data = {
					msg : data1
			};
			$.ajax({
				url : controllername + "?queryMoreNdxxgx" ,
				data : data,
				cache : false, 
				async : false,
				dataType : 'json',
				success : function(response){
					if(response.msg==0){
						var $thistab = $("#XW");
						$("tbody tr",$thistab).remove();
						$thistab.find("tbody").append("<tr><td colspan='6'>暂无年度信息共享数据</td></tr>");
						ycpage("xwfy");
						return;
					}
					xspage("xwfy");
					InsertTables(response.msg,XW,1);
					if(!XW.getAttribute("queryData")){
						XW.queryData = undefined;
						XW.setAttribute("queryData",data1);
					}else{
						XW.setAttribute("queryData",data1);
					}
					doQueryPage("xwfy",response.msg);
				}
			});
		}
		function ycpage(id){
			 $('#'+id).hide();
		}
		function xspage(id){
		     $('#'+id).show();
		}
		function InsertTables(json ,tablistID,type){
			var $thistab = $("#"+tablistID.id);
			var b = convertJson.string2json1(json);
		    var rowDataJsons = b.response;
		    $("tbody tr",$thistab).remove();
		    var strarr = "";
		    if(rowDataJsons.data.length==0){
		    	strarr = "暂无信息";
		    }else{
		    	for(var j = 0; j < rowDataJsons.data.length; j++) {
		        	if(type==1){//新闻
		        		var imagehtml = "";
		        		var ts = rowDataJsons.data[j]["SFYD"];
		        		if(ts!=null&&ts==0){
		        			imagehtml = "&nbsp;<img src=\""+g_sAppName+"/images/new.gif\">";
		        		}else{
		        			imagehtml = "&nbsp;<img src=\""+g_sAppName+"/images/content_new_blank.png\">";
		        		}
		        		strarr +="<tr>";	
		        		strarr +="<td>"+imagehtml+"<a href=\"javascript:void(0)\" onclick=\"showNdxxgx('"+rowDataJsons.data[j]["XTBG_XXZX_NDXXGX_ID"]+"',this)\">"+rowDataJsons.data[j]["NDXXBT"]+"</a></td>";	
		        		strarr +="<td align=\"center\" nowrap>"+rowDataJsons.data[j]["FBSJ"]+"</td>";
		                strarr +="</tr>";
		        	}
		        }
		    }
		    $thistab.find("tbody").html(strarr);//增加tbody内容
		}
		function showNdxxgx(id,obj){
		    var $tr = $(obj.parentElement.parentElement);
		    var imgObj = $tr.find("img");
		    if(imgObj.length>0){
		    	var src_ = $(imgObj[0]).attr("src");
		    	if(src_.indexOf("new.gif")>-1){
		    		src_ = src_.replace("new.gif","content_new_blank.png");
		    		imgObj.attr("src",src_);
		    	}
		    }
			readGG(id,'1');
			$(window).manhuaDialog({"title":"年度信息共享","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/ndxxgxDetail.jsp?id="+id,"modal":"1"});
		}
		//记录阅读
		function readGG(ggid, ydcs) {
			$.ajax({
				url : controllername_tz + "?readMainGg&ggid="+ggid+"&ydcs="+ydcs ,
		//		data : data,
				cache : false,
				async : false,
				dataType : 'json',
				success : function(response){
					var result = eval("(" + response + ")");
				}
			});
		}
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="row-fluid">
					<ul class="nav nav-tabs">
						<li id="newsId" class="active">
							<a href="#ndxxgx" class="active" data-toggle="tab">年度信息共享</a>
						</li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane active" id="ndxxgx">
							<div class="tabsRightButtonDiv">
								<div class="input-append inline pull-right">
									<input id="xwbtcx" placeholder="快速查找" type="text">
									<button class="btn" type="button" onclick="doQueryXW('1');">
										搜索
									</button>
								</div>
								<div class="pull-right newsPN1">
									当前年：
									<span id="xwdqnd"></span>&nbsp;&nbsp;
									<B>|</B>&nbsp;&nbsp;
									<a href="javascript:void(0)" id="xws" onclick="ndxwQuery(this)">上一年</a>&nbsp;&nbsp;|&nbsp;&nbsp;
									<a href="javascript:void(0)" id="xwx" onclick="ndxwQuery(this)">下一年</a>&nbsp;&nbsp;
								</div>
							</div>
							<table cellpadding="0" cellspacing="0" width="100%"
								class="newsTable" id="XW">
								<thead>
									<tr>
										<th align="left">
											标题
										</th>
										<th width="100" align="center">
											发布时间
										</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<th align="left">
											标题
										</th>
										<th width="100" align="center">
											发布时间
										</th>
									</tr>
								</tbody>
							</table>
							<div id="xwfy" class="pagination pagination-centered">

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id="queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
				<input type="hidden" name="xwnd" id="xwnd">
				<input type="hidden" name="ggnd" id="ggnd">


			</FORM>
		</div>
	</body>
</html>