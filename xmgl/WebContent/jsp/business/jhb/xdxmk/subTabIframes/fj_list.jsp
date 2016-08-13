<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ page import="com.ccthanking.framework.common.DBUtil"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<%
			String id = request.getParameter("id");
			if (id == null || "".equals(id)) {
				id = "";
			}
			String fjlb = request.getParameter("fjlb");
			if (fjlb == null || "".equals(fjlb)) {
				fjlb = "999999";
			}
			String treeNodeID = request.getParameter("treeNodeID");
			String temp = "";
			if (fjlb.indexOf(",") > -1) {
				String[] fjlbs = fjlb.split(",");
				if (fjlbs != null && fjlbs.length > 0) {
					for (int i = 0; i < fjlbs.length; i++) {
						temp += "'" + fjlbs[i] + "',";
					}

					temp = temp.substring(0, temp.length() - 1);
				}
			} else {
				temp = "'" + fjlb + "'";
			}

			String fjname = request.getParameter("fjname");

			if (fjname == null || "".equals(fjname)) {
				fjname = "文档查看";
			}
			String sjwybhSql = "select SJWYBH from GC_JH_SJ where XMID='"+id+"'";
			String sjwybhArr[][] = DBUtil.query(sjwybhSql);
			String sjwybhStr = "";
			if(sjwybhArr!=null&& sjwybhArr.length!=0){
				for(int x=0;x<sjwybhArr.length;x++){
					sjwybhStr += "'"+sjwybhArr[x][0]+"',";
				}
			}
			String ywid = "";
			String htlx = "";
			if("410".equals(treeNodeID)){
				htlx = "SG";
			}else if("407".equals(treeNodeID)){
				htlx = "JL";
			}else if("417".equals(treeNodeID)){
				htlx = "QT";
			}else if("406".equals(treeNodeID)){
				htlx = "PQ";
			}else if("405".equals(treeNodeID)){
				htlx = "CQ";
			}else if("404".equals(treeNodeID)){
				htlx = "SJ";
			}else if("403".equals(treeNodeID)){
				htlx = "ZBDL";
			}else if("402".equals(treeNodeID)){
				htlx = "ZX";
			}else if("401".equals(treeNodeID)){
				htlx = "QQ";
			}
			if(!"".equals(htlx)){
				//410表示施工合同
				String jhSql = "select A.HTID from "+
					"(select distinct HTID from GC_HTGL_HTSJ "+
					"where JHSJID in (select GC_JH_SJ_ID from GC_JH_SJ where SJWYBH in ("+sjwybhStr.substring(0,sjwybhStr.length()-1)+") "+
					"and SFYX='1')) A,GC_HTGL_HT B where A.HTID=B.ID and B.HTLX='"+htlx+"' and B.SFYX='1'";
				String jhArr[][] = DBUtil.query(jhSql);
				if(jhArr!=null){
					for(int jhNum = 0;jhNum<jhArr.length;jhNum++){
						ywid += jhArr[jhNum][0]+",";
					}
					fjlb = "0702";
				}
			}else{
				if (!"".equals(id)) {
					if(sjwybhStr!="" && !"".equals(sjwybhStr)){
						sjwybhStr = " or WYBH in("+sjwybhStr.substring(0,sjwybhStr.length()-1)+")";
					}
					String[][] s = DBUtil
							.query("select ywid from fs_fileupload  where fjlb in("
									+ temp + ") and (glid3 = '" + id
									+ "' "+sjwybhStr+" ) and fjzt='1'");
					if (s != null && s.length > 0) {
						for (int i = 0; i < s.length; i++) {
							ywid += s[i][0] + ",";
						}
					}
	
				}
			}

		%>
		<app:base />
		<title>Insert title here</title>
		<script type="text/javascript">
			var controllername= "${pageContext.request.contextPath }/fileUploadController.do";
			var xmid = "<%=id%>";
			var g_fjlb = "<%=fjlb%>";
			var g_ywid = "<%=ywid%>";
			//页面初始化
			$(function() {
				setPageHeight();
				doInit();
			});
			function doInit(){
				$("#q_fjlb").val(g_fjlb);
				var ywidStr = "";
				if(g_ywid!=""){
					var arr = new Array();
					arr = g_ywid.split(",");
					for(var i=0;i<arr.length;i++){
						if(arr[i]!=""){
							ywidStr += arr[i]+",";
						}
					}
				}
				if(ywidStr==""){
					ywidStr = "999999999999999999999999999999999";
				}else{
					ywidStr = ywidStr.substring(0,ywidStr.length-1);
				}
				$("#q_ywid").val(ywidStr);
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryInfoTable",data,DT1,null,true);
			}
			//------------------------------
			//-预览
			//------------------------------
			function doShowFile(obj){
				var showHtml = "";
				if(obj.FILENAME.length>10){
					showHtml +='<a href="javascript:void(0);" onclick="checkupFiles(\''+obj.FILEID+'\')"><i class="icon-zoom-in"></i> <abbr title="'+obj.FILENAME+'">'+obj.FILENAME.substring(0,16)+'</abbr></a>';
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
			
			function setPageHeight(){
				var x_h = parent.document.documentElement.clientHeight;
		  		var height = x_h-pageTitle-getTableTh(1)-pageNumHeight-12;
		  		var pageNum = parseInt(height/pageTableOne,10);
		  		$("#DT1").attr("pageNum",pageNum);
		  	}
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span7" style="width: 100%">
					<div class="B-small-from-table-autoConcise">
						<h4 class="title"><%=fjname%>
						</h4>
						<form method="post" id="queryForm">
							<table class="B-table" width="100%">
								<!--可以再此处加入hidden域作为过滤条件 -->
								<TR style="display: none;">
									<TD class="right-border bottom-border"></TD>
									<TD class="right-border bottom-border">
									</TD>
									<th width="5%" class="right-border bottom-border text-right">
										业务ID
									</th>
									<td class="right-border bottom-border" width="15%">
										<input class="span12" type="text" name="YWID" fieldtype="text"
											fieldname="YWID" operation="in" id="q_ywid" >
									</td>
									<th width="5%" class="right-border bottom-border text-right">
										附件类别
									</th>
									<td class="right-border bottom-border" width="15%">
										<input class="span12" type="text" name="FJLB" 
											fieldname="FJLB" operation="=" id="q_fjlb" >
									</td>
								</TR>
								<!--可以再此处加入hidden域作为过滤条件 -->
							</table>
						</form>
						<div class="row-fluid">
							<div class="overFlowX">
								<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" pageNum="10">
									<thead>
										<tr>
											<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
											<th fieldname="FILENAME" tdalign="left" Customfunction="doShowFile">&nbsp;附件名称&nbsp;</th>
											<th fieldname="LRSJ" style="width: 20%" tdalign="center">&nbsp;上传时间&nbsp;</th>
											<th fieldname="LRR" style="width: 10%" maxlength="10">&nbsp;上传人&nbsp;</th>
											<th fieldname="LRBM" style="width: 20%" maxlength="10">&nbsp;上传部门&nbsp;</th>
											<th fieldname="FJZT" style="width: 8%" tdalign="center" Customfunction="doDownloadFile">&nbsp;下载&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
						<div style="display:none;">
						<jsp:include page="/jsp/file_upload/fileupload_config.jsp"
							flush="true" />
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
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<input type="hidden" name="txtFilter" order="desc"
					fieldname="LRSJ" id="txtFilter">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>