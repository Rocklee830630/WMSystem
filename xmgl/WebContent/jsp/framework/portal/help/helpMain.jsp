<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%@ page import="com.ccthanking.framework.common.User"%>
		<%@ page import="com.ccthanking.framework.Globals"%>
		<%@ page import="com.ccthanking.common.FjlbManager"%>
		<%	
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	String deptID = user.getDepartment();
	String fjlb = FjlbManager.FS_HELP_DOC;
%>
		<app:base />
		<script type="text/javascript" charset="UTF-8">
	var controllername= "${pageContext.request.contextPath }/fileUploadController.do";
	var g_ywid = '<%=deptID%>';
	var g_fjlb = '<%=fjlb%>';
	$(function(){
		doInit();
	});
	//页面初始化
	function doInit(){
		g_bAlertWhenNoResult = false;
		//queryInfoTable();
	}
	//------------------------------------
	//查询问题表格
	//------------------------------------
	function queryInfoTable(){
		$("#q_fjlb").val(g_fjlb);
		var ywidStr = g_ywid;
		if(ywidStr==""){
			ywidStr = "999999999999999999999999999999999";
		}else{
		}
		$("#q_ywid").val(ywidStr);
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,tabList);
		defaultJson.doQueryJsonList(controllername+"?queryInfoTable",data,tabList,null,true);
	}
	//------------------------------
	//-预览
	//------------------------------
	function doShowFile(obj){
		var showHtml = "";
		if(obj.FILENAME.length>10){
			showHtml +='<a href="javascript:void(0);" onclick="checkupFiles(\''+obj.FILEID+'\')"><i class="icon-zoom-in"></i> <abbr title="'+obj.FILENAME+'">'+obj.FILENAME.substring(0,10)+'</abbr></a>';
		}else{
			showHtml +='<a href="javascript:void(0);" onclick="checkupFiles(\''+obj.FILEID+'\')"><i class="icon-zoom-in"></i> '+obj.FILENAME+'</a>';
		}
		return showHtml;
	}
	//------------------------------
	//-下载
	//------------------------------
	function doDownloadFile(obj){
		var showHtml = "";
		showHtml +='<a href="${pageContext.request.contextPath }/fileUploadController.do?getFile&fileid='+obj.FILEID+'" download=""><i class="icon-download-alt"></i>下载</a>';
		return showHtml;
	}
	//计算本页表格分页数
	function setPageHeight() {
		var xHeight = parent.document.documentElement.clientHeight;
		var height = xHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(Math.abs(height)/pageTableOne,10);
		$("#tabList").attr("pageNum", pageNum);
	}
</script>
	</head>
	<body>
		<div class="container-fluid">

			<table class="table table-hover">
				<thead>
					<tr>
						<th>
							手册名称
						</th>
						<th>
							手册下载
						</th>
						<th>
							视频预览
						</th>
						<th>
							视频下载
						</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<a
								href="${pageContext.request.contextPath }/help/doc/公共功能用户手册.pdf"
								target="view_window"><i class="icon-file"> </i> 公共功能用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i>
							<a
								href="${pageContext.request.contextPath }/help/doc/公共功能用户手册.pdf"
								download="">[下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/主任界面视频.mp4"
								target="view_window"> 主任界面视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/主任界面视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<%if("100000000002".equals(deptID)){ %>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath }/help/doc/财务用户手册.pdf"
								target="view_window"><i class="icon-file"> </i>财务用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/财务用户手册.pdf"
								download="">[下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/财务部视频.mp4"
								target="view_window"> 财务部视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/财务部视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<%} %>
					<tr>
						<td>
							<a
								href="${pageContext.request.contextPath }/help/doc/工程部用户手册.pdf"
								target="view_window"><i class="icon-file"> </i> 工程部用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/工程部用户手册.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/工程部视频.mp4"
								target="view_window"> 工程部视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/工程部视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath }/help/doc/合同部手册.pdf"
								target="view_window"><i class="icon-file"> </i> 合同部用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/合同部手册.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/招投标合同部视频.mp4"
								target="view_window"> 招投标合同部视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/招投标合同部视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a
								href="${pageContext.request.contextPath }/help/doc/计财处用户手册.pdf"
								target="view_window"><i class="icon-file"> </i> 计财处用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/计财处用户手册.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/项目储备库视频.mp4"
								target="view_window"> 项目储备库视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/项目储备库视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath }/help/doc/排迁用户手册.pdf"
								target="view_window"><i class="icon-file"> </i> 排迁用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/排迁用户手册.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/排迁部视频.mp4"
								target="view_window"> 排迁组视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/排迁部视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath }/help/doc/前期手续.pdf"
								target="view_window"><i class="icon-file"> </i> 前期手续用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/前期手续.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/前期手续视频.mp4"
								target="view_window"> 前期手续视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/前期手续视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath }/help/doc/设计.pdf"
								target="view_window"><i class="icon-file"> </i> 设计用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/设计.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/设计部视频.mp4"
								target="view_window"> 设计视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/设计部视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath }/help/doc/项目管理公司.pdf"
								target="view_window"><i class="icon-file"> </i> 项目管理公司</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/项目管理公司.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/项目管理公司视频.mp4"
								target="view_window"> 项目管理公司视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/项目管理公司视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a
								href="${pageContext.request.contextPath }/help/doc/协同办公用户手册.pdf"
								target="view_window"><i class="icon-file"> </i> 协同办公用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/协同办公用户手册.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/OA视频.mp4"
								target="view_window"> 协同办公视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/OA视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath }/help/doc/造价用户手册.pdf"
								target="view_window"><i class="icon-file"> </i> 造价用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/造价用户手册.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/招投标合同部视频.mp4"
								target="view_window"> 招投标合同部视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/招投标合同部视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath }/help/doc/征收.pdf"
								target="view_window"><i class="icon-file"> </i> 征收用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/征收.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/征收部视频.mp4"
								target="view_window"> 征收视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/征收部视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a
								href="${pageContext.request.contextPath }/help/doc/质量安全部用户手册.pdf"
								target="view_window"><i class="icon-file"> </i> 质量安全部用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/质量安全部用户手册.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/质量安全部视频.mp4"
								target="view_window"> 质量安全部视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/质量安全部视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath }/help/doc/标段划分.pdf"
								target="view_window"><i class="icon-file"> </i> 标段划分用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/标段划分.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/标段划分管理视频.mp4"
								target="view_window"> 标段划分管理视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/标段划分管理视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath }/help/doc/参建单位.pdf"
								target="view_window"><i class="icon-file"> </i> 参建单位用户手册</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/参建单位.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/参建单位.mp4"
								target="view_window"> 参建单位视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/参建单位.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a
								href="${pageContext.request.contextPath }/help/doc/长春市城市基础设施建设项目群综合管控平台用户明细.xlsx"
								target="view_window"><i class="icon-file"> </i> 平台用户明细</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/长春市城市基础设施建设项目群综合管控平台用户明细.xlsx"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/部门综合视频.mp4"
								target="view_window"> 部门综合视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/部门综合视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
					<tr>
						<td>
							<a href="${pageContext.request.contextPath }/help/doc/问题中心.pdf"
								target="view_window"><i class="icon-file"> </i> 问题中心</a>
						</td>
						<td>
							<i class="icon-download"> </i><a
								href="${pageContext.request.contextPath }/help/doc/问题中心.pdf"
								download=""> [下载]</a>
						</td>
						<td>
							<i class="icon-play-circle"> </i><a
								href="${pageContext.request.contextPath }/help/video/问题提报视频.mp4"
								target="view_window"> 问题中心视频</a>
						</td>
						<td>
							<i class="icon-download"></i><a
								href="${pageContext.request.contextPath }/help/video/问题提报视频.mp4"
								download=""> [下载]</a>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

	</body>
</html>