<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<%
String num=request.getParameter("num");
User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
String dept=user.getDepartment();

%>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/ZhaoBiaoXuQiuController.do";
	g_bAlertWhenNoResult = false;
	var json,XQID,XQZT;
   
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
   
	$(function() {
		setPageHeight();
		init();
		//按钮绑定事件（查询）
		$("#chaxun").click(function() {
			getNd();
			var str = "&xqzt='3','5','6'";
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryZhaobiaoxuqiu"+str,data,DT1,null,false);
		});
		//修改
		$("#xiugai").click(function(){
			var index1 = $("#DT1").getSelectedRowIndex();
	 		if(index1<0){
				//requireSelectedOneRow();
				//return;
				$("#resultXML").val("");
			}else{
				//json = $("#DT1").getSelectedRowText();
				//json串加密
				//json=encodeURI(json);
				$("#resultXML").val($("#DT1").getSelectedRow());
			}
			$(window).manhuaDialog({"title":"招标需求管理>启动招标","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbqdzb.jsp","modal":"1"});
	 	});
	 	//清空查询条件
		$("#query_clear").click(function(){
			$("#queryForm").clearFormResult();
			getNd();
			//  init();
		});
		//按钮绑定事件（导出）
		$("#btnExp").click(function() {
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
				printTabList("DT1");
			}
		});
		$("#blBtn").click(function(){
			$(window).manhuaDialog({"title":"招标需求管理>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbxqwh.jsp?blFlag=1","modal":"1"});
		});
		$("#btnSp").click(function(){
			var index1 =	$("#DT1").getSelectedRowIndex();
	 		if(index1<0) 
			{
	 			requireSelectedOneRow();
	 			return;
			}
	 		var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
	 		var ywlx = $("#DT1").getSelectedRowJsonObj().YWLX;
	 		var condition = "";
	 		var obj = $("#DT1").getSelectedRowJsonObj();
	 		var xqzt = obj.XQZT;
			if(obj.TGBZ=="1"){
				xAlert("警告","该数据是补录数据，没有审批信息！","3");
				return;
			}
	 		if(xqzt!=1){
	 		   // var wsActionURL = "${pageContext.request.contextPath}/PubWS.do?getXMLPrintAction|templateid=|isEdit=0|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
	 		   //  wsActionURL = "${pageContext.request.contextPath}/jsp/business/lcgl/lccx/processDetail.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid=&isEdit=0"+"&isview=1"+"&spbh=";
	 		    //window.open(wsActionURL);
	 		    if(obj.EVENTSJBH=="7"){
	 		    	var value=queryLcxx();
	 				var tempJson = convertJson.string2json1(value);
	 				var rowObject = tempJson.response.data[0];
	 			   	var url =  '${pageContext.request.contextPath}/'+rowObject.LINKURL+'?taskid='+rowObject.ID+'&taskseq='+rowObject.SEQ+'&sjbh='+rowObject.SJBH+'&ywlx='+rowObject.YWLX+'&spbh='+rowObject.SPBH+'&rwlx='+rowObject.RWLX+"&isRead="+rowObject.XB;
	 			   	$(window).manhuaDialog({"title":"审批信息","type":"text","content":url,"modal":"1"});

	 		    }else{
		 		    var isview = "1";
		 		    var isOver = getProIsover(sjbh,ywlx);
		 		    if(isOver =="1"){
		 		    	isview = "0";
		 		    }
		 			 var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview="+isview; 
		 		  	 $(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  

	 		    }
	 		}
	 		/* else if(xqzt == 7){
	 			 var isview = "1";
		 		    var isOver = getProIsover(sjbh,ywlx);
		 		    if(isOver =="1"){
		 		    	isview = "0";
		 		    }
		 			 var s = "${pageContext.request.contextPath}/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview="+isview; 
		 		  	 $(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
	 		} */
	 		else{
	 			createSPconf(sjbh,ywlx,condition);
	 		}
		 });
	});
	
	// add by xhb start
	function setBlr(data){
		var userId = "";
		var userName = "";
		for(var i=0;i<data.length;i++){
	 	 var tempObj =data[i];
	 	 if(i<data.length-1){
		  userId +=tempObj.DEPTID+",";
		  userName +=tempObj.DEPTNAME+",";
	 	 }else{
	 	  userId +=tempObj.DEPTID;
	 	  userName +=tempObj.DEPTNAME; 
	 	 }
		}
		$("#LRBM").val(userName);
		$("#LRBM").attr("code",userId);
	}
	//获取行数据json串
	function tr_click(obj,tabId){
		var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
		var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
		if(tempJson.XQZT!="3"){
			$("#xiugai").attr("disabled","true");
		}else{
			$("#xiugai").removeAttr("disabled");
		}
	}
	function Xzdw(){
		openDeptTree('single','','setBlr') ;
}
	//页面默认参数
	function init(){
		//生成json串
		doSearch();
	}
	//默认年度
	function getNd(){
    	var d=new Date();
    	var year=d.getFullYear();
    	$("#LRSJ").val(year);
	}
	function doSearch(){
		getNd();
		var str = "&xqzt='3','5','6'";
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryZhaobiaoxuqiu"+str,data,DT1,null,true);
	}
	//子页面调用修改行
	function xiugaihang(data,n){
		var str = "&xqzt='3','5','6'";
		var data1 = defaultJson.packSaveJson(data);
		defaultJson.doUpdateJson(controllername+"?doQdzb&ztbxqid="+n+str, data1,DT1);
		$("#xiugai").attr("disabled","true");
	}
	//信息卡
	function doRandering(obj)
	  {
			var showHtml = "";
			showHtml = "<a href='javascript:void(0)' onclick='xinxika()' title='招标需求信息卡'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.GC_JH_SJ_ID+"'></i></a>";
			return showHtml;
	  }
	function xinxika(){
		var index = $(event.target).closest("tr").index();
	    $("#DT1").cancleSelected();
	    $("#DT1").setSelect(index);
		json = $("#DT1").getSelectedRowText();
		//json串加密
		json=encodeURI(json);
		$("#resultXML").val($("#DT1").getSelectedRow());
		$(window).manhuaDialog({"title":"招标需求信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbxq/ztbxqxxk.jsp","modal":"1"});
	}
	function docolor(obj){
		var xqzt=obj.XQZT;
		if(xqzt=="1"){
			return '<span class="label label-danger">'+obj.XQZT_SV+'</span>';
		}else if(xqzt=="3"){
		  	return '<span class="label label-warning">'+obj.XQZT_SV+'</span>';
		}else if(xqzt=="6"){
		 	return '<span class="label label-success">'+obj.XQZT_SV+'</span>';
		}else{
			return obj.XQZT_SV;
		}
	}
	//控制投资额显示
	function showTze(obj){
		var showHtml = "";
		if(obj.TBJFS=="1" || obj.TBJFS=="2"){
			showHtml = obj.YSE_SV;
		}else{
			showHtml = '<div style="text-align:center">—</div>';
		}
		return showHtml;
	}
	//子页面调用添加行
	function tianjiahang(data){
		var subresultmsgobj = defaultJson.dealResultJson(data);
		$("#DT1").insertResult(JSON.stringify(subresultmsgobj),DT1,1);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(0);
		json = $("#DT1").getSelectedRowText();
		//json串加密
	    json=encodeURI(json);
	    xSuccessMsg("操作成功","");
	}
</script>     
</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						招标需求综合
						<span class="pull-right"> <%-- <app:oPerm url="/jsp/business/ztb/ztbxqzhXiugai.jsp"> --%>
							
							<app:oPerm url="/jsp/business/ztb/ztbxqwh.jsp?blFlag=1">
							<button id="blBtn" class="btn" type="button">
								补录招标需求
							</button>
							</app:oPerm>
							
							<app:oPerm url="/jsp/business/ztb/ztbqdzb.jsp">
							<button id="xiugai" class="btn" type="button">
								启动招标
							</button>
							</app:oPerm>
							<button id="btnSp" class="btn" type="button">审批信息</button>
							<button id="btnExp" class="btn" type="button">
								导出
							</button></span>
					</h4>
					<form method="post"
						action="${pageContext.request.contextPath }/insertdemo.do"
						id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>
								<th width="3%" class="right-border bottom-border text-right">
									单位
								</th>
								<td class="bottom-border right-border" width="20%">
									<select class="span12 department" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME: DEPT_PARANT_ROWID!='0'" fieldname="LRBM" id="LRBM" name="LRBM" operation="=" logic="and" defaultMemo="全部">
									</select>
								</td>
								<th class="right-border bottom-border text-right">
									工作名称
								</th>
								<td width="12%" class=" right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="GZMC"
										fieldname="GZMC" operation="like" logic="and">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									招标类型
								</th>
								<td class=" right-border bottom-border">
									<select class="span12 4characters" id="ZBLX" name="ZBLX"
										fieldname="ZBLX" defaultMemo="全部" kind="dic" src="ZBLX"
										operation="=">
									</select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									招标方式
								</th>
								<td class=" right-border bottom-border">
									<select class="span12 4characters" id="ZBFS" name="ZBFS"
										fieldname="ZBFS" defaultMemo="全部" kind="dic" src="ZBFS"
										operation="=">
									</select>
								</td>
								<th class="right-border bottom-border text-right">
									状态
								</th>
								<td class=" right-border bottom-border">
									<select class="span12 3characters" id="XQZT" name="XQZT"
										fieldname="XQZT" defaultMemo="全部" kind="dic" src="XQZT"
										operation="=">
									</select>
								</td>
								<td class="text-left bottom-border text-right" rowspan="2">
									<button id="chaxun" class="btn btn-link" type="button">
										<i class="icon-search"></i>查询
									</button>
									<button id="query_clear" class="btn btn-link" type="button">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
							<tr>
								<th class="right-border bottom-border text-right">
									项目名称
								</th>
								<td width="12%" class=" right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="XMMC"
										fieldname="XMMC" operation="like" logic="and">
								</td>
								<TD colspan="8"></TD>
							</tr>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
					<table width="100%" class="table-hover table-activeTd B-table"
						id="DT1" type="single" printFileName="招标需求综合">
						<thead>
							<tr>
								<th name="XH" id="_XH">
									&nbsp;#&nbsp;
								</th>
								<th fieldname="SFYX" maxlength="15" tdalign="center" CustomFunction="doRandering" noprint="true">
									&nbsp;&nbsp;
								</th>
								<th fieldname="XQZT" maxlength="15" tdalign="center" CustomFunction="docolor">
									&nbsp;状态&nbsp;
								</th>
								<th fieldname="GZMC" maxlength="15">
									&nbsp;工作名称&nbsp;
								</th>
								<th fieldname="ZBLX" maxlength="15" tdalign="center">
									&nbsp;招标类型&nbsp;
								</th>
								<th fieldname="ZBFS" maxlength="15" tdalign="center">
									&nbsp;招标方式&nbsp;
								</th>
								<th fieldname="TBJFS" maxlength="15" tdalign="center">
									&nbsp;投标报价方式&nbsp;
								</th>
								<th fieldname="YSE" maxlength="15" tdalign="right" CustomFunction="showTze">
									&nbsp;投资额(元)&nbsp;
								</th>
								<th fieldname="GZNR" maxlength="15">
									&nbsp;工作内容&nbsp;
								</th>
								<th fieldname="SXYQ" maxlength="15">
									&nbsp;时限要求&nbsp;
								</th>
								<th fieldname="JSYQ" maxlength="15">
									&nbsp;技术要求&nbsp;
								</th>
								<!-- 	<th fieldname="XQZT"  maxlength="15" tdalign="center">&nbsp;是否有效&nbsp;</th> -->
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
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
				<input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ" />
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
				<input type="hidden" name="queryResult" id="queryResult">

			</FORM>
		</div>
	</body>
</html>