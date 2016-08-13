<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>

<%@ page import="java.util.*"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.lang.StringBuffer"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="com.ccthanking.framework.plugin.AppInit"%>
<%@ page import="java.math.*"%>
<head>
<app:base/>
<title>长春市政府投资建设项目管理中心——综合管控中心</title>
<%
	String id = request.getParameter("id");
	Connection conn = DBUtil.getConnection();//定义连接
	StringBuffer sbSql = null;//sql语句字符串
	String sql = "";//查询参数字符串
	String result = "";//用于记录统计数
    //取当前年
    String year = Pub.getDate("yyyy", new Date());
%>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css"> 
.table1 {
	border-left: #000 solid 1px;
	border-top: #000 solid 1px;
	margin:3px 0;
}
.marginBottom15px {margin-bottom:15px;}
.table1 th {background:#b8cce4;}
.table1 td,.table1 th {
	line-height: 1.5em;
	padding: 2px;
	border-right: #000 solid 1px;
	border-bottom: #000 solid 1px;
}
.table2 {
	border-left: #000 solid 1px;
}
.table2 td,.table2 th {
	line-height: 1.5em;
	padding: 2px;
	border-right: #000 solid 1px;
	border-bottom: #000 solid 1px;
}
 
input[type='text'] {
	vertical-align: middle;
	height: 20px;
	line-height: 16px;
	padding: 2px;
}
.tableHeader {border-bottom:#6095c9 solid 3px; margin-top:25px; margin-bottom:5px;}
.tableHeader th {color:#000; padding:4px;}
.tableHeader .active {background:#6095c9;}
.tableHeader .tabs {background:#ccc;}
.table2Header {border-bottom:#6095c9 solid 3px; margin-top:25px;}
.table2Header th {color:#000; padding:4px;}
.table2Header .active {background:#6095c9;}
.tableHeader a {color:#000;}
.table2Header .tabs {background:#ccc;}
 
.tableTdBorder {border-bottom:#000 solid 1px; padding:3px 0;}
 
.bdinfoTabs {width:800px; height:29px; border-bottom: #6095c9 solid 3px; margin:0; padding:0; list-style:none; margin-top: 25px; margin-bottom: 5px;}
.bdinfoTabs li {width:32.3%; margin-right:1%; height:29px; line-height:29px; text-indent:1em; float:left; background:#ddd;}
.bdinfoTabs li.ui-tabs-active {background:#6095c9;}
.bdinfoTabs li a {color:#000; display:block; height:29px; text-decoration:none;}
.bdinfoTabs li a:hover {background:#6095c9;}
</style>
<script src="${pageContext.request.contextPath }/js/base/jquery-ui.js"></script>
<script>
  $(function() {
    $( "#tabs" ).tabs();
  });
  </script>
 <script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var id = "<%=id%>";
//页面初始化
$(function() {
	init();
		
});

	
//页面默认参数
function init(){
	//alert(id);
	$("#id_jb").val(id);
	var a = defaultJson.doQueryJsonList(controllername+"?queryById&id="+id,null,jcxxList);
	submit(controllername+"?queryById&id="+id,data,jcxxList);
	
}
function submit(actionName, data,tablistID){
	$.ajax({
		type : 'post',
		url : actionName,
		data : data,
		cache : false,
		dataType : "json",  
		async :	false,
		success : function(result) {
			var rowObj = convertJson.string2json1(result.msg).response.data[0];
			//alert(JSON.stringify(substr)); 
			$("#jcxxForm").setFormValues(rowObj);
			$("#resultXML").val(JSON.stringify(rowObj));
		}
	});
}
</script>
</head>
<body>
<p></p>
<div class="container-fluid">
    <div class="row-fluid">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab1" class="active" data-toggle="tab">项目基本信息</a></li>
            <li><a href="#tab2" data-toggle="tab">项目资金信息</a></li>
            <li><a href="#tab3" data-toggle="tab">项目进度</a></li>
            <li><a href="#tab4" data-toggle="tab">招投标信息</a></li>
            <li><a href="#tab5" data-toggle="tab">合同信息</a></li>
            <li><a href="#tab6" data-toggle="tab">项目存在问题</a></li>
            <li><a href="#tab7" data-toggle="tab">工程文档</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab1">
            	<table width="800" border="0" cellpadding="0" cellspacing="0" class="tableHeader">
                    <tr>
                        <th width="30%" align="left" class="active">项目基本信息</th>
                        <th width="3%">&nbsp;</th>
                        <th width="30%">&nbsp;</th>
                        <th width="3%">&nbsp;</th>
                        <th width="30%">&nbsp;</th>
                        <th width="3%">&nbsp;</th>
                    </tr>
                </table>
                <%
	sbSql = new StringBuffer();
	sbSql.append("SELECT ");
	sbSql.append("XMBH, XMMC, XMDZ, JHZTZE, ND, JSMB, JSZT,LXFS_GLGS,LXFS_YZDB,LXFS_SJDW,FZR_SJDW, ");
	sbSql.append("(SELECT DIC_VALUE FROM FS_DIC_TREE WHERE DIC_NAME_CODE = 'XMLX' AND dic_code = T.XMLX)AS XMLX,");
	sbSql.append("(SELECT DEPT_NAME FROM VIEW_YW_ORG_DEPT WHERE ROW_ID = T.XMGLGS)AS XMGLGS,");
	sbSql.append("(SELECT NAME FROM FS_ORG_PERSON WHERE ACCOUNT = T.YZDB)AS YZDB,");
	sbSql.append("(SELECT NAME FROM FS_ORG_PERSON WHERE ACCOUNT = T.FZR_GLGS)AS FZR_GLGS ");
	sbSql.append("FROM GC_TCJH_XMXDK T WHERE GC_TCJH_XMXDK_ID = '"+id+"'");
	sql = sbSql.toString();
	BaseResultSet bs = DBUtil.query(conn, sql, null);
	ResultSet rs = bs.getResultSet();
	String XMBH = "";
	String XMMC = "";
	String XMDZ = "";
	String JHZTZE = "";
	String XMLX = "";
	String ND = "";
	String JSMB = "";
	String XMGLGS = "";
	String JSZT = "";
	String FZR_GLGS = "";
	String LXFS_GLGS = "";
	String YZDB = "";
	String LXFS_YZDB = "";
	String LXFS_SJDW = "";
	String FZR_SJDW = "";
	while(rs.next()){
		XMBH = Pub.empty(rs.getString("XMBH")) ? "":rs.getString("XMBH");//项目编号
		XMMC = Pub.empty(rs.getString("XMMC")) ? "":rs.getString("XMMC");//项目名称
		XMDZ = Pub.empty(rs.getString("XMDZ"))  ? "":rs.getString("XMDZ");//项目地址
		JHZTZE = Pub.empty(rs.getString("JHZTZE"))  ? "":rs.getString("JHZTZE");//总投资额
		XMLX = Pub.empty(rs.getString("XMLX")) ? "":rs.getString("XMLX");//项目类型
		ND = Pub.empty(rs.getString("ND")) ? "":rs.getString("ND");//年度
		JSMB = Pub.empty(rs.getString("JSMB"))  ? "":rs.getString("JSMB");//建设目标
		XMGLGS = Pub.empty(rs.getString("XMGLGS"))? "":rs.getString("XMGLGS");//项目管理公司
		JSZT = Pub.empty(rs.getString("JSZT")) ? "":rs.getString("JSZT");//建设主体
		FZR_GLGS = Pub.empty(rs.getString("FZR_GLGS")) ? "":rs.getString("FZR_GLGS");//项目管理公司负责人
		LXFS_GLGS = Pub.empty(rs.getString("LXFS_GLGS")) ? "":rs.getString("LXFS_GLGS");//项目管理公司负责人联系电话
		YZDB = Pub.empty(rs.getString("YZDB")) ? "":rs.getString("YZDB");//业主代表
		LXFS_YZDB = Pub.empty(rs.getString("LXFS_YZDB")) ? "":rs.getString("LXFS_YZDB");//业主代表联系电话
		FZR_SJDW = Pub.empty(rs.getString("FZR_SJDW")) ? "":rs.getString("FZR_SJDW");//设计单位负责人
		LXFS_SJDW = Pub.empty(rs.getString("LXFS_SJDW")) ? "":rs.getString("LXFS_SJDW");//设计单位负责人联系电话
	}
%> 
                <table width="800" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="15%">项目编号</td>
                        <td class="tableTdBorder"><%=XMBH %></td>
                    </tr>
                    <tr>
                        <td>项目名称</td>
                        <td class="tableTdBorder"><%=XMMC %></td>
                    </tr>
                    <tr>
                        <td>项目地址</td>
                        <td class="tableTdBorder"><%=XMDZ %></td>
                    </tr>
                    <tr>
                        <td>项目类型</td>
                        <td class="tableTdBorder"><%=XMLX %></td>
                    </tr>
                    <tr>
                        <td>长度</td>
                        <td class="tableTdBorder">&nbsp;</td>
                    </tr>
                    <tr>
                        <td>总投资</td>
                        <td class="tableTdBorder"><%=JHZTZE %></td>
                    </tr>
                    <tr>
                        <td>开工年度</td>
                        <td class="tableTdBorder"><%=ND %></td>
                    </tr>
                    <tr>
                        <td>年度建设目标</td>
                        <td class="tableTdBorder"><%=JSMB %></td>
                    </tr>
                    <tr>
                        <td>项目管理公司</td>
                        <td class="tableTdBorder"><%=XMGLGS %></td>
                    </tr>
                    <tr>
                        <td>建设主体</td>
                        <td class="tableTdBorder"><%=JSZT %></td>
                    </tr>
                    <tr>
                        <td>项目建设阶段</td>
                        <td class="tableTdBorder">&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellpadding="0" cellspacing="0" class="tableHeader">
                    <tr>
                        <th width="33%" align="left" class="active">参建单位</th>
                        <th width="3%">&nbsp;</th>
                        <th width="30%">&nbsp;</th>
                        <th width="3%">&nbsp;</th>
                        <th width="30%">&nbsp;</th>
                        <th width="3%">&nbsp;</th>
                    </tr>
                </table>
                <table width="800" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="15%">建管中心工程部</td>
                        <td>&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellpadding="0" cellspacing="0" class="table1 marginBottom15px">
                    <tr>
                        <th width="20%">职务</th>
                        <th width="200">联系人</th>
                        <th>联系方式</th>
                    </tr>
                    <tr>
                        <td>业主代表</td>
                        <td><%=YZDB %></td>
                        <td><%=LXFS_YZDB %></td>
                    </tr>
                </table>
                <table width="800" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="15%">项目部</td>
                        <td class="tableTdBorder">&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellpadding="0" cellspacing="0" class="table1 marginBottom15px">
                    <tr>
                        <th width="20%" bgcolor="#b8cce4">职务</th>
                        <th bgcolor="#b8cce4" width="200">联系人</th>
                        <th bgcolor="#b8cce4">联系方式</th>
                    </tr>
                    <tr>
                        <td>项目负责人</td>
                        <td><%=FZR_GLGS %></td>
                        <td><%=LXFS_GLGS %></td>
                    </tr>
                </table>
                <table width="800" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="15%">设计单位</td>
                        <td class="tableTdBorder">&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellpadding="0" cellspacing="0" class="table1">
                    <tr>
                        <th width="20%">职务</th>
                        <th width="200">联系人</th>
                        <th>联系方式</th>
                    </tr>
                    <tr>
                        <td>项目负责人</td>
                        <td><%=FZR_SJDW %></td>
                        <td><%=LXFS_SJDW %></td>
                    </tr>
                </table>
                <div id="tabs">
                
                <ul class="bdinfoTabs">
    				<li><a href="#bdinfo-1">标段信息1</a></li>
				    <li><a href="#bdinfo-2">标段信息2</a></li>
				    <li><a href="#bdinfo-3">标段信息3</a></li>
  				</ul>
                <div id="bdinfo-1"><table width="800" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="15%">标段名称1</td>
                        <td class="tableTdBorder">111111</td>
                    </tr>
                    <tr>
                        <td width="15%">施工总承包单位</td>
                        <td class="tableTdBorder">&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellpadding="0" cellspacing="0" class="table1 marginBottom15px">
                    <tr>
                        <th width="20%" bgcolor="#b8cce4">职务</th>
                        <th bgcolor="#b8cce4">联系人</th>
                        <th bgcolor="#b8cce4">联系方式</th>
                    </tr>
                    <tr>
                        <td>项目经理</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>技术负责人</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="15%">监理单位</td>
                        <td class="tableTdBorder">&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellpadding="0" cellspacing="0" class="table1 marginBottom15px">
                    <tr>
                        <th width="20%" bgcolor="#b8cce4">职务</th>
                        <th bgcolor="#b8cce4">联系人</th>
                        <th bgcolor="#b8cce4">联系方式</th>
                    </tr>
                    <tr>
                        <td>总监理工程师</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>总监理工程师代表</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>安全监理工程师</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                </table></div>
                <div id="bdinfo-2"><table width="800" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="15%">标段名称2</td>
                        <td class="tableTdBorder">22222222</td>
                    </tr>
                    <tr>
                        <td width="15%">施工总承包单位</td>
                        <td class="tableTdBorder">&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellpadding="0" cellspacing="0" class="table1 marginBottom15px">
                    <tr>
                        <th width="20%" bgcolor="#b8cce4">职务</th>
                        <th bgcolor="#b8cce4">联系人</th>
                        <th bgcolor="#b8cce4">联系方式</th>
                    </tr>
                    <tr>
                        <td>项目经理</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>技术负责人</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="15%">监理单位</td>
                        <td class="tableTdBorder">&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellpadding="0" cellspacing="0" class="table1 marginBottom15px">
                    <tr>
                        <th width="20%" bgcolor="#b8cce4">职务</th>
                        <th bgcolor="#b8cce4">联系人</th>
                        <th bgcolor="#b8cce4">联系方式</th>
                    </tr>
                    <tr>
                        <td>总监理工程师</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>总监理工程师代表</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>安全监理工程师</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                </table></div><div id="bdinfo-3"><table width="800" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="15%">标段名称3</td>
                        <td class="tableTdBorder">333333</td>
                    </tr>
                    <tr>
                        <td width="15%">施工总承包单位</td>
                        <td class="tableTdBorder">&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellpadding="0" cellspacing="0" class="table1 marginBottom15px">
                    <tr>
                        <th width="20%" bgcolor="#b8cce4">职务</th>
                        <th bgcolor="#b8cce4">联系人</th>
                        <th bgcolor="#b8cce4">联系方式</th>
                    </tr>
                    <tr>
                        <td>项目经理</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>技术负责人</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="15%">监理单位</td>
                        <td class="tableTdBorder">&nbsp;</td>
                    </tr>
                </table>
                <table width="800" border="0" cellpadding="0" cellspacing="0" class="table1 marginBottom15px">
                    <tr>
                        <th width="20%" bgcolor="#b8cce4">职务</th>
                        <th bgcolor="#b8cce4">联系人</th>
                        <th bgcolor="#b8cce4">联系方式</th>
                    </tr>
                    <tr>
                        <td>总监理工程师</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>总监理工程师代表</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td>安全监理工程师</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                </table></div>
                </div>
                <table width="800" border="0" cellpadding="0" cellspacing="0" class="tableHeader">
                    <tr>
                        <th width="33%" align="left" class="active">相关图纸</th>
                        <th width="3%">&nbsp;</th>
                        <th width="30%">&nbsp;</th>
                        <th width="3%">&nbsp;</th>
                        <th width="30%">&nbsp;</th>
                        <th width="3%">&nbsp;</th>
                    </tr>
                </table>
            </div>
            <div class="tab-pane" id="tab2"><table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">资金基本信息</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th width="20%" align="left">资金来源</th>
        <th colspan="3">&nbsp;</th>
    </tr>
    <tr>
        <th align="left">项目总投资(万元)</th>
        <td width="30%">&nbsp;</td>
        <th width="20%" align="left">到位资金(万元) </th>
        <td width="30%">&nbsp;</td>
    </tr>
    <tr>
        <th align="left">累计进度(万元) </th>
        <td colspan="3">&nbsp;</td>
    </tr>
    <tr>
        <th align="left">累计付款(万元) </th>
        <td>&nbsp;</td>
        <th align="left">累计计划付款(万元) </th>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">决算价(万元) </th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">投资完成情况</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th width="20%" align="left">计划投资</th>
        <th>&nbsp;</th>
        <th align="left">完成投资</th>
        <th>&nbsp;</th>
    </tr>
    <tr>
        <th align="left"> 工程总投资</th>
        <td width="30%">&nbsp;</td>
        <th width="20%" align="left"> 工程总投资</th>
        <td width="30%">&nbsp;</td>
    </tr>
    <tr>
        <th align="left"> 征地拆迁总投资</th>
        <td>&nbsp;</td>
        <th align="left">征地拆迁总投资</th>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left"> 其他投资</th>
        <td>&nbsp;</td>
        <th align="left"> 其他投资</th>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">资金支付情况</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>&nbsp;</th>
        <th>合同数</th>
        <th>合同金额</th>
        <th>已支付</th>
        <th>支付率</th>
    </tr>
    <tr>
        <th align="left"> 工程</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left"> 征地拆迁</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left"> 其他</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table></div>
            <div class="tab-pane" id="tab3"><table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">前期</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>&nbsp;</th>
        <th width="16%">计划完成时间</th>
        <th width="16%">实际完成时间</th>
        <th width="16%">完成情况反馈</th>
        <th width="16%">反馈时间</th>
        <th width="16%">存在问题</th>
    </tr>
    <tr>
        <th align="left">规划</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">土地</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">方案稳定</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">报建</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">施工许可</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">融资</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>&nbsp;</th>
        <th width="16%">计划完成时间</th>
        <th width="16%">实际完成时间</th>
        <th width="16%">完成情况反馈</th>
        <th width="16%">反馈时间</th>
        <th width="16%">存在问题</th>
    </tr>
    <tr>
        <th align="left">融资推介</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">签订融资合同</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">资金到位</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">可研批复</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">规划意见函</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">设计</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>&nbsp;</th>
        <th width="16%">计划完成时间</th>
        <th width="16%">实际完成时间</th>
        <th width="16%">完成情况反馈</th>
        <th width="16%">反馈时间</th>
        <th width="16%">存在问题</th>
    </tr>
    <tr>
        <th align="left">初步设计</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">施工图设计</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">拆迁图</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">排迁图</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">造价招标</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>&nbsp;</th>
        <th width="16%">计划完成时间</th>
        <th width="16%">实际完成时间</th>
        <th width="16%">完成情况反馈</th>
        <th width="16%">反馈时间</th>
        <th width="16%">存在问题</th>
    </tr>
    <tr>
        <td>拦标价及财审</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>设计招标</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>监理招标</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>施工招标</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">征地拆排迁</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>&nbsp;</th>
        <th width="16%">计划完成时间</th>
        <th width="16%">实际完成时间</th>
        <th width="16%">完成情况反馈</th>
        <th width="16%">反馈时间</th>
        <th width="16%">存在问题</th>
    </tr>
    <tr>
        <th align="left">征地拆迁</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">排迁</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">施工</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>&nbsp;</th>
        <th width="16%">计划完成时间</th>
        <th width="16%">实际完成时间</th>
        <th width="16%">完成情况反馈</th>
        <th width="16%">反馈时间</th>
        <th width="16%">存在问题</th>
    </tr>
    <tr>
        <th align="left">开复工</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">完工</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">竣工移交</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>&nbsp;</th>
        <th width="16%">计划完成时间</th>
        <th width="16%">实际完成时间</th>
        <th width="16%">完成情况反馈</th>
        <th width="16%">反馈时间</th>
        <th width="16%">存在问题</th>
    </tr>
    <tr>
        <th align="left">竣工验收</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <th align="left">移交</th>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table></div>
            <div class="tab-pane" id="tab4"><table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">设计招标</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>序号</th>
        <th>招标方式</th>
        <th>招标标段</th>
        <th>中标通知书编号</th>
        <th>招投标名称</th>
        <th>开标日期</th>
        <th>中标价(元)</th>
        <th>中标单位</th>
    </tr>
    <tr>
        <td align="center">1</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">监理招标</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>序号</th>
        <th>招标方式</th>
        <th>招标标段</th>
        <th>中标通知书编号</th>
        <th>招投标名称</th>
        <th>开标日期</th>
        <th>中标价(元)</th>
        <th>中标单位</th>
    </tr>
    <tr>
        <td align="center">1</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">施工招标</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>序号</th>
        <th>招标方式</th>
        <th>招标标段</th>
        <th>中标通知书编号</th>
        <th>招投标名称</th>
        <th>开标日期</th>
        <th>中标价(元)</th>
        <th>中标单位</th>
    </tr>
    <tr>
        <td align="center">1</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">其他招标</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>序号</th>
        <th>招标方式</th>
        <th>招标标段</th>
        <th>中标通知书编号</th>
        <th>招投标名称</th>
        <th>开标日期</th>
        <th>中标价(元)</th>
        <th>中标单位</th>
    </tr>
    <tr>
        <td align="center">1</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">3</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">4</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">5</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">6</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table></div>
            <div class="tab-pane" id="tab5"><table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">设计合同</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>序号</th>
        <th>合同编码</th>
        <th>合同名称</th>
        <th>签订日期</th>
        <th>签订价</th>
        <th>合同支付</th>
        <th>完成投资</th>
        <th>变更金额</th>
        <th>乙方单位</th>
        <th>备注</th>
    </tr>
    <tr>
        <td align="center">1</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">施工合同</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>序号</th>
        <th>合同编码</th>
        <th>合同名称</th>
        <th>签订日期</th>
        <th>签订价</th>
        <th>合同支付</th>
        <th>完成投资</th>
        <th>变更金额</th>
        <th>乙方单位</th>
        <th>备注</th>
    </tr>
    <tr>
        <td align="center">1</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">监理合同</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>序号</th>
        <th>合同编码</th>
        <th>合同名称</th>
        <th>签订日期</th>
        <th>签订价</th>
        <th>合同支付</th>
        <th>完成投资</th>
        <th>变更金额</th>
        <th>乙方单位</th>
        <th>备注</th>
    </tr>
    <tr>
        <td align="center">1</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">其他合同</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>序号</th>
        <th>合同编码</th>
        <th>合同名称</th>
        <th>合同类型</th>
        <th>签订日期</th>
        <th>签订价</th>
        <th>合同支付</th>
        <th>完成投资</th>
        <th>变更金额</th>
        <th>乙方单位</th>
        <th>备注</th>
    </tr>
    <tr>
        <td align="center">1</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">3</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">4</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">5</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">6</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">7</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">8</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
</table></div>
            <div class="tab-pane" id="tab6"><table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">未解决问题</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>序号</th>
        <th>问题等级</th>
        <th>提交日期</th>
        <th>问题描述</th>
        <th>提出部门</th>
        <th>提出人</th>
        <th>最后处理日期</th>
    </tr>
    <tr>
        <td align="center">1</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">已解决问题</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th>序号</th>
        <th>问题等级</th>
        <th>提交日期</th>
        <th>问题描述</th>
        <th>提出部门</th>
        <th>提出人</th>
        <th>处理日期</th>
        <th>处理人</th>
    </tr>
    <tr>
        <td align="center">1</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table></div>
            <div class="tab-pane" id="tab7"><table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">前期阶段文件</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <td align="center">　</td>
        <td width="13%" align="center">　</td>
        <td width="5%" align="center">　</td>
        <td width="35%" align="center">　</td>
        <td>文件名</td>
        <td>上传时间</td>
        <td>上传人</td>
    </tr>
    <tr>
        <td rowspan="28" align="center">1</td>
        <td rowspan="28" align="center">报建资料</td>
        <td align="center">1</td>
        <td align="center">项目建议书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td align="center">可行性研究报告</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">3</td>
        <td align="center">建设用地批准书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">4</td>
        <td align="center">建设用地规划许可证</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">5</td>
        <td align="center">建设用地规划设计条件（要点）</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">6</td>
        <td align="center">划地红线图（批准图纸）</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">7</td>
        <td align="center">建设用地征地、拆迁文件</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">8</td>
        <td align="center">规划方案批准（批准图纸）</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">9</td>
        <td align="center">初步设计文件批复</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">10</td>
        <td align="center">项目概算及批复</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">11</td>
        <td align="center">总平面图及施工图（规划部门批准）</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">12</td>
        <td align="center">建筑设计防火审核意见书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">13</td>
        <td align="center">建筑工程防空地下室审批表</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">14</td>
        <td align="center">防雷设施审核意见书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">15</td>
        <td align="center">涉外建设项目国家安全事项审查意见表</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">16</td>
        <td align="center">设计卫生审查认可书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">17</td>
        <td align="center">客户供电方案审批表（附图纸、方案）</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">18</td>
        <td align="center">环境影响报告及批复</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">19</td>
        <td align="center">燃气报审及批复</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">20</td>
        <td align="center">建设工程规划许可证</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">21</td>
        <td align="center">工程勘察地质报告</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">22</td>
        <td align="center">施工图设计文件审查意见</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">23</td>
        <td align="center">施工图</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">24</td>
        <td align="center">项目预算书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">25</td>
        <td align="center">年度建设计划</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">26</td>
        <td align="center">招标项目核准表</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">27</td>
        <td align="center">其他文件资料</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">28</td>
        <td align="center">建筑工程施工许可证</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td rowspan="5" align="center">2</td>
        <td rowspan="5" align="center">招投标资料</td>
        <td align="center">1</td>
        <td align="center">监理招投标资料</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td align="center">勘察设计招投标资料</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">3</td>
        <td align="center">咨询类招投标资料</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">4</td>
        <td align="center">其他招投标资料</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">5</td>
        <td align="center">（05）施工招标资料</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td rowspan="2" align="center">3</td>
        <td rowspan="2" align="center">前期其它资料</td>
        <td align="center">1</td>
        <td align="center">来往文件</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td align="center">会议纪要</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">实施阶段文件</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <td align="center">　</td>
        <td width="13%" align="center">　</td>
        <td width="5%" align="center" valign="top">&nbsp;</td>
        <td width="35%" align="center">　</td>
        <td>文件名</td>
        <td>上传时间</td>
        <td>上传人</td>
    </tr>
    <tr>
        <td align="center">1</td>
        <td align="center">勘察设计合同</td>
        <td align="center">1</td>
        <td align="center">合同文件</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">3</td>
        <td align="center">咨询合同</td>
        <td align="center">1</td>
        <td align="center">合同文件</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">4</td>
        <td align="center">测量合同</td>
        <td align="center">1</td>
        <td align="center">合同文件</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">5</td>
        <td align="center">监理合同</td>
        <td align="center">1</td>
        <td align="center">合同文件</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td rowspan="39" align="center">6</td>
        <td rowspan="39" align="center">施工合同</td>
        <td rowspan="7" align="center">1</td>
        <td rowspan="7" align="center">合同书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td align="center">招标文件</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">3</td>
        <td align="center">招标图</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">4</td>
        <td align="center">投标报价书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">5</td>
        <td align="center">中标通知书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">6</td>
        <td align="center">标底</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">7</td>
        <td align="center">施工审图会议纪要</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">8</td>
        <td align="center">质量监督注册登记表</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">9</td>
        <td align="center">安全监督注册登记表</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">10</td>
        <td align="center">工程开工申报表</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">11</td>
        <td align="center">施工噪声排放许可证</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">12</td>
        <td align="center">建筑施工许可证</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">13</td>
        <td align="center">规划定点标桩、验线、测量记录册</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">14</td>
        <td align="center">环境保护设施(合格)证明书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">15</td>
        <td align="center">人防工程建设(合格)证明书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">16</td>
        <td align="center">建筑电气工程电气装置检测报告</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">17</td>
        <td align="center">消防验收证明书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">18</td>
        <td align="center">电梯验收合格证</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">19</td>
        <td align="center">其他专项验收证明</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">20</td>
        <td align="center">工程有关照片及声像资料</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">21</td>
        <td align="center">竣工验收报告</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">22</td>
        <td align="center">竣工图</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">23</td>
        <td align="center">工程质量验收申请表</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">24</td>
        <td align="center">勘察文件质量检查报告</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">25</td>
        <td align="center">设计文件质量检查报告</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">26</td>
        <td align="center">工程质量保修书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">27</td>
        <td align="center">工程档案预验收合格证</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">28</td>
        <td align="center">工程规划验收图纸</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">29</td>
        <td align="center">测绘验收测量记录册</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">30</td>
        <td align="center">工程规划验收合格证</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">31</td>
        <td align="center">验工验收备案表</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">32</td>
        <td align="center">工程保修验收表</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">33</td>
        <td align="center">结算书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">7</td>
        <td align="center">采购合同</td>
        <td align="center">1</td>
        <td align="center">合同文件</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">8</td>
        <td align="center">其他合同</td>
        <td align="center">1</td>
        <td align="center">合同文件</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td rowspan="4" align="center">9</td>
        <td rowspan="4" align="center">施工阶段其它资料</td>
        <td align="center">1</td>
        <td align="center">往来文件</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td align="center">会议纪要</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">3</td>
        <td align="center">设计变更</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">4</td>
        <td align="center">其他资料</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2Header">
    <tr>
        <th width="33%" align="left" class="active">后期管理文件</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
        <th width="30%">&nbsp;</th>
        <th width="3%">&nbsp;</th>
    </tr>
</table>
<table width="800" border="0" cellpadding="0" cellspacing="0" class="table2 marginBottom15px">
    <tr>
        <th align="center">　</th>
        <th width="13%" align="center">　</th>
        <th width="5%" align="center" valign="top">&nbsp;</th>
        <th width="35%" align="center">　</th>
        <th>文件名</th>
        <th>上传时间</th>
        <th>上传人</th>
    </tr>
    <tr>
        <td rowspan="2" align="center">1</td>
        <td rowspan="2" align="center">工程验收</td>
        <td align="center">1</td>
        <td align="center">验收相关报告</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td align="center">验收证书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td rowspan="2" align="center">2</td>
        <td rowspan="2" align="center">总结报告</td>
        <td align="center">1</td>
        <td align="center">工程概况表</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">2</td>
        <td align="center">项目总结报告</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">3</td>
        <td align="center">工程移交</td>
        <td align="center">1</td>
        <td align="center">工程移交书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">4</td>
        <td align="center">工程决算</td>
        <td align="center">1</td>
        <td align="center">财务决算报告</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">5</td>
        <td align="center">工程保修</td>
        <td align="center">1</td>
        <td align="center">保修记录</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td rowspan="2" align="center">6</td>
        <td rowspan="2" align="center">工程审计</td>
        <td align="center">1</td>
        <td align="center">审计报告</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
    <tr>
        <td align="center">1</td>
        <td align="center">产权移交书</td>
        <td>　</td>
        <td>　</td>
        <td>　</td>
    </tr>
</table></div>
        </div>
    </div>
</div>
</body>
</html>

