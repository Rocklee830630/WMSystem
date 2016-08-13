<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ page import="com.ccthanking.framework.common.QuerySet"%>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap-paginator.js"></script>

<title></title>
<%
User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
String userid =user.getAccount();
String type = request.getParameter("type") == null ? "1" : request.getParameter("type");
Date dt=new Date();
SimpleDateFormat matter1=new SimpleDateFormat("yyyy");
String date=matter1.format(dt);
%>
 <style type="text/css">
			.disabledA:hover {
				text-decoration: none;
				color: #AAAAAA;
				cursor: default
			}
			.disabledA{
				color: #AAAAAA;
				font-size:12px;
			}
			.nomalA:hover {
				text-decoration: underline;
				cursor: pointer
			}
			.nomalA{
				color: #333333;
				font-size:12px;
			}
</style>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername_tz= "${pageContext.request.contextPath }/ggtzController.do";
var controllername_xw= "${pageContext.request.contextPath }/zxxwController.do";
var userid = "<%=userid%>";
var type = "<%=type%>";

var nowDate =  "{\"logic\":\"and\",\"fieldname\":\"NF\",\"operation\":\"=\",\"value\":\""+<%=date%>+"\",\"fieldtype\":'',\"fieldformat\":''}";
var xwcshquerycondition='';
var ggcshquerycondition = "{\"logic\":\"and\",\"fieldname\":\"JSR_ACCOUNT\",\"operation\":\"=\",\"value\":\""+userid+"\",\"fieldtype\":'',\"fieldformat\":''}"
	+',{"logic":"and","fieldname":"GGLB","operation":"in","value":"GG,TZ","fieldtype":"text","fieldformat":""}';
//页面初始化
$(function() {
	//调用ajax插入
	var querydata = getQueryDateXw(nowDate);
	queryXw(querydata);
	querydata = getQueryDateGg(nowDate+","+ggcshquerycondition);
	queryGG(querydata);
	//初始化年度
	var date = new Date();
	var dqn = date.getFullYear();
	$("#xwx").attr("title",dqn+1);
	$("#xws").attr("title",dqn-1);
	$("#xwnd").attr("value",dqn);
	$("#ggx").attr("title",dqn+1);
	$("#ggs").attr("title",dqn-1);
	$("#ggnd").attr("value",dqn);
	//查询上一年和下一年有没有数据
	isndggQuery($("#ggs").attr("title"),"sn");
	isndggQuery($("#ggx").attr("title"),"xn");
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
//公告点击上一年下一年查询
function ndggQuery(obj){
	var querycondition = "";
	var nd = obj.title;
	$("#ggnd").attr("value",nd);
	$("#ggdqnd").html(nd);
	var dqn = $("#ggnd").attr("value");
	$("#ggx").attr("title",parseInt(dqn)+1);
	$("#ggs").attr("title",parseInt(dqn)-1);
	queryconditionNd = ggcshquerycondition+",{\"logic\":\"and\",\"fieldname\":\"NF\",\"operation\":\"=\",\"value\":\""+nd+"\",\"fieldtype\":'',\"fieldformat\":''}";
	//判断是否有快速查询
	var ggbt = $("#ggbtcx").val();
    if(ggbt!=null&&ggbt!=""){
    	querycondition = queryconditionNd+",{\"logic\":\"and\",\"fieldname\":\"GGBT\",\"operation\":\"like\",\"value\":\"%"+ggbt+"%\",\"fieldtype\":'',\"fieldformat\":''}";
    }else{
    	querycondition = queryconditionNd;
    }
	queryGG(getQueryDateGg(querycondition));
	isndggQuery($("#ggs").attr("title"),"sn");
	isndggQuery($("#ggx").attr("title"),"xn");

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
	var queryconditionND = "{\"logic\":\"and\",\"fieldname\":\"NF\",\"operation\":\"=\",\"value\":\""+nd+"\",\"fieldtype\":'',\"fieldformat\":''}";
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
//判断公告上一年下一年是否有数据
function isndggQuery(nd,sxnd){
	$("#ggnd").attr("value",nd);
	var querycondition = ggcshquerycondition+",{\"logic\":\"and\",\"fieldname\":\"NF\",\"operation\":\"=\",\"value\":\""+nd+"\",\"fieldtype\":'',\"fieldformat\":''}";
	var data=isqueryGG(getQueryDateGg(querycondition));
	
	if(data=='0')
	{
		if(sxnd=="sn")
		{
		 	 $("#ggs").removeAttr("onclick");
		  	 $("#ggs").attr("class","disabledA");
		}
		else{
			 $("#ggx").removeAttr("onclick");
			 $("#ggx").attr("class","disabledA");
		}
	}
	else{
			if(sxnd=="sn")
			{
				 $("#ggs").attr("onclick","ndggQuery(this)");
				 $("#ggs").attr("class","nomalA");
			}
			else{
				 $("#ggx").attr("class","nomalA");
				 $("#ggx").attr("onclick","ndggQuery(this)");
			}
	}
}
 //判断新闻下一年上一年是否有数据
function isndxwQuery(nd,sxnd){
	$("#xwnd").attr("value",nd);
	var querycondition = "{\"logic\":\"and\",\"fieldname\":\"NF\",\"operation\":\"=\",\"value\":\""+nd+"\",\"fieldtype\":'',\"fieldformat\":''}";
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
		url : controllername_xw + "?queryMoreXw" ,
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
//查询公告
function isqueryGG(data1){
	var data2='';
	var data = {
			msg : data1
	};
	$.ajax({
		url : controllername_tz + "?queryMoreGg" ,
		data : data,
		cache : false,
		async : false,
		dataType : 'json',
		success : function(response) {
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
		url : controllername_xw + "?queryMoreXw" ,
		data : data,
		cache : false, 
		async : false,
		dataType : 'json',
		success : function(response){
			
			if(response.msg==0){
			 var $thistab = $("#XW");
		     $("tbody tr",$thistab).remove();
		     $thistab.find("tbody").append("<tr><td colspan='6'>暂无新闻</td></tr>");
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
function queryGG(data1){
	var data = {
			msg : data1
	};
	$.ajax({
		url : controllername_tz + "?queryMoreGg" ,
		data : data,
		cache : false,
		async : false,
		dataType : 'json',
		success : function(response) {
			if(response.msg==0){
				var $thistab = $("#GG");
			    $("tbody tr",$thistab).remove();
			    $thistab.find("tbody").append("<tr><td colspan='6'>暂无公告</td></tr>");
			    ycpage("ggfy");
			    return;
			}
			xspage("ggfy");
			InsertTables(response.msg,GG,2);
			if(!GG.getAttribute("queryData")){
				GG.queryData = undefined;
				GG.setAttribute("queryData",data1);
			}else{
				GG.setAttribute("queryData",data1);
			}
			doQueryPage("ggfy",response.msg);
		}
	});
	
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
    	
    	for ( var j = 0; j < rowDataJsons.data.length; j++) {
        	if(type==1){//新闻
        		var imagehtml = "";
        		var ts = rowDataJsons.data[j]["TS"];
        		if(ts!=null&&ts<=7){
        			imagehtml = "&nbsp;<img src=\""+g_sAppName+"/images/new.gif\">";
        		}
        		strarr +="<tr>";	
        		strarr +="<td><a href=\"javascript:void(0)\" onclick=\"showXW('"+rowDataJsons.data[j]["NEWSID"]+"')\">"+rowDataJsons.data[j]["XWBT"]+"</a>"+imagehtml+"</td>";	
        		strarr +="<td align=\"center\" nowrap>"+rowDataJsons.data[j]["FBSJ_SN"]+"</td>";
                strarr +="</tr>";
        	}else{//公告
        		var yx = "";
        		var sfyx =  rowDataJsons.data[j]["SHZT"];
                if(sfyx == null){
              	  sfyx = "";
                }
                if("2"==sfyx){
              	  yx = "<span class=\"text-error\">[作废]</span>";
                }else{
                  yx = "[有效]"; 
                }
                var yd = "";
                var sfyd=rowDataJsons.data[j]["SFYD"];
                if(sfyd == null){
                	sfyd = "";
                  }
                if("0"==sfyd){
                	  yd = "<img src=\""+g_sAppName+"/images/listIcon.png\">";
                 }else{
                	 yd = "&nbsp;";
                }
           //     alert(rowDataJsons.data[j]["FJID_SV"]);
                var showFileIoc = rowDataJsons.data[j]["FJID_SV"] != '' ? '<img src=\"/xmgl/images/icon-annex.png\" alt=\"附件\" width=\"16\" height=\"16\">' : '';
        		strarr +="<tr>";
        		strarr +=" <td align=\"left\">"+yd+"</td>";
        		strarr +="<td><a href=\"javascript:void(0)\" onclick=\"showGG('"+rowDataJsons.data[j]["GGID"]+"','',this)\">"+yx+"&nbsp;&nbsp;["+rowDataJsons.data[j]["GGLB_SV"]+"]&nbsp;&nbsp;"+rowDataJsons.data[j]["GGBT"]+"&nbsp;" + showFileIoc + "</a></td>";
        		/* start 发布范围名称大于13位时，截取 */
        		if(rowDataJsons.data[j]["FBFWMC"].length >= 13) {
        			strarr +="<td align=\"center\" nowrap><abbr title=\""+rowDataJsons.data[j]["FBFWMC"]+"\"> "+rowDataJsons.data[j]["FBFWMC"].substring(0,13) +"...</abbr></td>";
        		} else {
        			strarr +="<td align=\"center\" nowrap>"+rowDataJsons.data[j]["FBFWMC"]+"</td>";
        		}
        		/* end 发布范围名称大于13位时，截取 */
        		strarr +="<td align=\"center\" nowrap>"+rowDataJsons.data[j]["FBBMMC"]+"</td>";	
        		strarr +="<td align=\"center\" nowrap>"+rowDataJsons.data[j]["FBR_SV"]+"</td>";	
        		strarr +="<td align=\"center\" nowrap>"+rowDataJsons.data[j]["FBSJ_SN"]+"</td>";	
        		strarr +="</tr>";	
        	}
        }
    }
    
    
    
    $thistab.find("tbody").append(strarr);//增加tbody内容
}


function showGG(id,ydcs,obj){
//	 var obj = $("#DT1").getRowJsonObjByIndex(index);
//	 var ggid = obj.GGID;
    var $tr = $(obj.parentElement.parentElement);
    var imgObj = $tr.find("img");
    if(imgObj.length>0){
    	var src_ = $(imgObj[0]).attr("src");
    	if(src_.indexOf("listIcon")>-1)
    	imgObj[0].remove();
    }
   
	readGG(id, ydcs);
	$(window).manhuaDialog({"title":"通知公告","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/newsDetail.jsp?type=1&id="+id+"&ydcs="+ydcs,"modal":"1"});
    window.parent.getMessages();

}
 function showXW(id){
//	 var obj = $("#DT2").getRowJsonObjByIndex(index);
//	 var ggid = obj.NEWSID;
	 $(window).manhuaDialog({"title":"中心新闻","type":"text","content":"${pageContext.request.contextPath}/jsp/framework/portal/newsDetail.jsp?type=2&id="+id,"modal":"1"});
 }


//记录阅读 
function readGG(ggid, ydcs) {
	$.ajax({
		url : controllername_tz + "?readMainGg&ggid="+ggid+"&ydcs="+ydcs ,
//		data : data,
		cache : false,
		async : false,
		dataType : 'json',
		success : function(response) {
			var result = eval("(" + response + ")");
		}
	});
}
</script>      
    
</head>

<body>
<app:dialogs/>
<div class="container-fluid">
    <p></p>
    <div class="row-fluid">
        <div class="row-fluid">
            <ul class="nav nav-tabs">
                <li id="newsId"><a href="#news" data-toggle="tab">新闻中心</a></li>
                <li id="annouceId"><a href="#annouce" class="active" data-toggle="tab">通知公告</a></li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane" id="annouce">
                    <div class="tabsRightButtonDiv"><div class="input-append inline pull-right">
                        <input id="ggbtcx" placeholder="快速查找" type="text">
                        <button class="btn" type="button"  onclick="doQueryGG('1');">搜索</button>
                    </div>
                    <div class="pull-right newsPN1">当前年：<span id="ggdqnd"></span>&nbsp;&nbsp;<B>|</B>&nbsp;&nbsp;<a   href="javascript:void(0)" onclick="ndggQuery(this)"  id="ggs">上一年</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javascript:void(0)" id="ggx" onclick="ndggQuery(this)">下一年</a>&nbsp;&nbsp;</div></div>
                    <div> 
                    <table cellpadding="0" cellspacing="0" width="100%" class="newsTable" id="GG">
                         <thead>
                        	<tr>
                            	<th width="20">&nbsp;</th>
                            	<th align="left">标题</th>
                            	<th width="160">发布范围</th>
                            	<th width="130">发布部门</th>
                            	<th width="56">发布人</th>
                            	<th width="100">发布时间</th>
                           	</tr>
                        </thead>
                        <tbody>
                         
                        </tbody>
                    </table>
                    </div>
                    <div id="ggfy"  class="pagination pagination-centered"></div>

                </div>
                <div class="tab-pane" id="news">
                    <div class="tabsRightButtonDiv"><div class="input-append inline pull-right">
                        <input id="xwbtcx" placeholder="快速查找" type="text">
                        <button class="btn" type="button"  onclick="doQueryXW('1');">搜索</button>
                    </div>
                    <div class="pull-right newsPN1">当前年：<span id="xwdqnd"></span>&nbsp;&nbsp;<B>|</B>&nbsp;&nbsp;<a href="javascript:void(0)"  id="xws" onclick="ndxwQuery(this)">上一年</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javascript:void(0)" id="xwx" onclick="ndxwQuery(this)">下一年</a>&nbsp;&nbsp;</div></div>
                    <table cellpadding="0" cellspacing="0" width="100%" class="newsTable" id="XW">
                        <thead>
                        	<tr>
                            	<th align="left">标题</th>
                            	<th width="100" align="center">发布时间</th>
                            </tr>
                        </thead>
                        <tbody>
                            
                          
                        </tbody>
                    </table>
                    <div  id="xwfy" class="pagination pagination-centered">
                      
                    </div>
                </div>
            </div>
            <script>
		$('#myTab a').click(function (e) {
		  e.preventDefault();
		  $(this).tab('show');
		})
		</script> 
        </div>
        <div style="height:5px;"> </div>
    </div>
</div>
<div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="queryResult" id = "queryResult">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="xwnd" id="xwnd" >
         <input type="hidden" name="ggnd" id="ggnd" >
         
		
 	</FORM>
 </div>
</body>
</body>
</html>