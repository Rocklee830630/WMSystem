<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<%
String num=request.getParameter("num");
%>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/ZhaoBiaoXuQiuController.do";
	var controllername_bl= "${pageContext.request.contextPath }/TaskAction.do";
	var json,XQID,XQZT;
	var delFlag = "true";
   
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
   
	$(function() {
		setPageHeight();
		setDefaultNd("QueryND");
		//新增
		var weihu = $("#weihu");
	 	weihu.click(function() 
	 			{
	 		     $(window).manhuaDialog({"title":"招标需求管理>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbxqwh.jsp","modal":"1"});
	            });
		//修改
		$("#xiugai").click(function(){
			var index1 = $("#DT1").getSelectedRowIndex();
	 		if(index1<0){
	 			requireSelectedOneRow();
	 			return;
			}else{
				$("#resultXML").val($("#DT1").getSelectedRow());
				$(window).manhuaDialog({"title":"招标需求管理>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbxqEdit.jsp?delFlag="+delFlag,"modal":"1"});
			}
		});
		//按钮绑定事件（导出）
		$("#btnExp").click(function() {
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
				printTabList("DT1");
			}
		});
		var sp_btn = $("#sp_btn");
		sp_btn.click(function() 
	 			{
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
			 			 //$("#DT1").cancleSelected();
			 		}
			 		
			 		
					/* var url =  '${pageContext.request.contextPath}/jsp/business/lcgl/lcsq/lcsq.jsp?sjbh='+sjbh;
					$(window).manhuaDialog({"title":"流程申请","type":"text","content":url,"modal":"2"});   */
				 });
		
		
		//提交
		 $("#tj_btn").click(function()
				{
			     $("#GC_ZTB_XQ_ID").val(XQID);
			     $("#XQZT").val("2");
				 var index1 =$("#DT1").getSelectedRowIndex();
		 		 if(index1<0) 
				 {
		 			requireSelectedOneRow();
		 			return;
				 }
	 		     //生成json串
			     var data = Form2Json.formToJSON(demoForm2);
			     //组成保存json串格式 
			     var data1 = defaultJson.packSaveJson(data);
		 	     defaultJson.doUpdateJson(controllername + "?updateZhaobiaoxuqiu", data1,DT1,null);
		 	     
			});
	 	//清空查询条件
	    var btn_clearQuery = $("#query_clear");
		btn_clearQuery.click(function() 
		{
	        $("#queryForm").clearFormResult();
	        getNd();
	        //  init();
	      });
		$("#btnZbxx").click(function(){
			if($("#DT1").getSelectedRowIndex()==-1){
				requireSelectedOneRow();
				return;
			}else{
				var tempJson = convertJson.string2json1($("#DT1").getSelectedRow());
				var id = tempJson.GC_ZTB_XQ_ID;
				var zblx = tempJson.ZBLX;
				var zbcs = getCountZbcs(id);
				zbcs = Number(zbcs);
				if(zbcs==1){
					$(window).manhuaDialog({"title":"招标需求管理>查看招标信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbgl_xx.jsp?xx="+id+"&zblx="+zblx,"modal":"1"});
				}else if(zbcs>1){
					$(window).manhuaDialog({"title":"招标需求管理>查看招标信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbxq/ztbList.jsp?xx="+id+"&zblx="+zblx,"modal":"4"});
				}else{
					xAlert("提示","无招标信息!","3");
				}
			}
		});
		$("#btnSubmit").click(function(){
			if($("#DT1").getSelectedRowIndex()==-1){
				requireSelectedOneRow();
				return;
			}else{
				var tempJson = convertJson.string2json1($("#DT1").getSelectedRow());
				var cqspFlag = getCqspFlag(tempJson.SJBH);
				var xqzt = tempJson.XQZT;
				if(xqzt!="1" && xqzt!="2"){
					xAlert("警告","该数据已经进入招标流程，不需要使用通过功能!","3");
					return;
				}else if(cqspFlag=="true"){
					xAlert("警告","该数据已经发起审批流程，不允许使用通过功能!","3");
					return;
				}else{
					xConfirm("提示信息","招标需求将跳过审批过程，是否继续？");
					$('#ConfirmYesButton').unbind();
					$('#ConfirmYesButton').one("click",function(){ 
						var id = tempJson.GC_ZTB_XQ_ID;
						defaultJson.doUpdateJson(controllername + "?doSubmit&id="+id, "",DT1,null);
					});
				}
			}
		});
	 });
	//--------------------------------------
	//-获取当前数据是否已经进行了“呈请审批”操作标志
	//--------------------------------------
	function getCqspFlag(n){
		var str = "";
		$.ajax({
			url:controllername_bl + "?getCqspFlag&sjbh="+n,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				str = result.msg;
			}
		});
		return str;
	}
	
	//--------------------------------------
	//-获取某个需求的招标次数（一个需求可以多次招标）
	//--------------------------------------
	function getCountZbcs(n){
		var str = "";
		$.ajax({
			url:controllername + "?getCountZbcs&xqid="+n,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				str = result.msg;
			}
		});
		return str;
	}
	function queryLcxx(){
		var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
		var str = "";
		$.ajax({
			url:controllername_bl + "?getFwLc&sjbh="+sjbh,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				str = result.msg;
			}
		});
		return str;
	}
	
	//获取行数据json串
	function tr_click(obj,tabListid)
	{
	    json = $("#DT1").getSelectedRowText();
	    XQID=obj.GC_ZTB_XQ_ID;
	    //XQZT=obj.XQZT;
		//json串加密
		json=encodeURI(json);
		//
		if(obj.XQZT != 1) {
			
			//$("#sp_btn").attr("disabled","disabled");
			if(obj.EVENTSJBH=="7"){
				//审批被退回的业务数据允许修改，但是不允许删除
				$("#sp_btn")[0].innerText = "审批退回信息";
				$("#xiugai").removeAttr("disabled");
				delFlag = "false";
			}else{
				$("#sp_btn")[0].innerText = "审批信息";
				$("#xiugai").attr("disabled","disabled");
				delFlag = "true";
			}
		} else {
			delFlag = "true";
			$("#sp_btn")[0].innerText = "呈请审批";
			//$("#sp_btn").removeAttr("disabled");
			$("#xiugai").removeAttr("disabled");
		}
		if(obj.XQZT=='5'||obj.XQZT=='6'){
			$("#btnZbxx").removeAttr("disabled","disabled");
		}else{
			$("#btnZbxx").attr("disabled","disabled");
		}
		//暂时放开 modified by hongpeng.dong at 2014.10.23
		$("#xiugai").removeAttr("disabled");
	}

	$(function() {
		init();
		//按钮绑定事件（查询）
		$("#chaxun").click(function() {
			getNd();
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryZhaobiaoxuqiu",data,DT1,null,false);
		});
	});
	//页面默认参数
	function init(){
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryZhaobiaoxuqiu",data,DT1,null,true);
	}
	//默认年度
	function getNd(){
	    	var d=new Date();
	    	var year=d.getFullYear();
	    	$("#LRSJ").val(year);
	}
	//子页面调用添加行
	function tianjiahang(data)
	{
		var subresultmsgobj = defaultJson.dealResultJson(data);
		$("#DT1").insertResult(JSON.stringify(subresultmsgobj),DT1,1);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(0);
		json = $("#DT1").getSelectedRowText();
		//json串加密
	    json=encodeURI(json);
		$("#xiugai").removeAttr("disabled");
		$("#btnZbxx").attr("disabled","disabled");
		$("#sp_btn")[0].innerText = "呈请审批";
	    xSuccessMsg("操作成功","");
	}
	//子页面调用修改行
	function xiugaihang(data)
	{
		var index =	$("#DT1").getSelectedRowIndex();
		var subresultmsgobj = defaultJson.dealResultJson(data);
		var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,index);
		$("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,index);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(index);
		$("#btnZbxx").removeAttr("disabled","disabled");
		$("#sp_btn")[0].innerText = "呈请审批";
		$("#btnZbxx").attr("disabled","disabled");
		json = $("#DT1").getSelectedRowText();
			//json串加密
		json=encodeURI(json);
		xSuccessMsg("操作成功","");
	}
	function doRanderingYuan(obj){
		var showHtml = "";
		if(obj.TBJFS=="1" || obj.TBJFS=="2"){
			showHtml = obj.YSE_SV;
		}else{
			showHtml = '<div style="text-align:center">—</div>';
		}
		return showHtml;
	}
	//状态颜色判断
	function docolor(obj)
	{
		var xqzt=obj.XQZT;
		if(xqzt=="1") 
		{
			return '<span class="label label-danger">'+obj.XQZT_SV+'</span>';
		}
		if(xqzt=="3")
		{
		  	return '<span class="label label-warning">'+obj.XQZT_SV+'</span>';
		}
		if(xqzt=="6")
		{
		 	return '<span class="label label-success">'+obj.XQZT_SV+'</span>';
		}
		else
			{
			return obj.XQZT_SV;
			}
		
	}
		function prcCallback()
	{
			var rowValue=$("#DT1").getSelectedRow();
			var rowindex=$("#DT1").getSelectedRowIndex();
			var odd=convertJson.string2json1(rowValue);
			$(odd).attr('XQZT_SV','会签审批');
			$(odd).attr('XQZT','2');
			$(odd).attr('EVENTSJBH_SV','审批中');
			$(odd).attr('EVENTSJBH','1');
			var aa=JSON.stringify(odd);
			$("#DT1").updateResult(aa,DT1,rowindex);
			$("#DT1").cancleSelected();
			$("#DT1").setSelect(rowindex);
			$("#sp_btn")[0].innerText = "审批信息";
			$("#xiugai").attr("disabled","disabled");
			json = $("#DT1").getSelectedRowText();
		   json=encodeURI(json);
	}
	//审批结束回调
		function gengxinchaxun(obj) {
		//	 init();
			var row_index=$("#DT1").getSelectedRowIndex();
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			var tempJson = convertJson.string2json1(data);
			var a=$("#DT1").getCurrentpagenum();
			tempJson.pages.currentpagenum=a;
			data = JSON.stringify(tempJson);
			defaultJson.doQueryJsonList(controllername+"?queryZhaobiaoxuqiu",data,DT1);
			$("#DT1").setSelect(row_index);
			successInfo("","");
			var selObj = $("#DT1").getSelectedRowJsonObj();//获得选中行的索引
			tr_click(selObj, DT1);
		}
		function doDelZtb(data,winObj){
			var data1 = defaultJson.packSaveJson(data);
			defaultJson.doDeleteJson(controllername+"?deleteZtbxq",data1,DT1,null);
			winObj.manhuaDialog.close();
		}
	//信息卡
	function doRandering(obj)
	  {
			var showHtml = "";
			showHtml = "<a href='javascript:void(0)' onclick='xinxika()' title='招标需求信息卡'><i class='icon-file showXmxxkInfo'></i></a>";
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
</script>     
</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<p></p>
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						部门招标需求
						<span class="pull-right">
						<app:oPerm url="/jsp/business/ztb/ztbxqwh.jsp?doSubmit">
						<button id="btnSubmit" class="btn" type="button">通过</button>&nbsp;&nbsp;
						</app:oPerm>

						<app:oPerm
								url="/jsp/business/ztb/ztbxqwh.jsp">
								<button id="weihu" class="btn" type="button">
									新增
								</button>
							</app:oPerm> <app:oPerm url="/jsp/business/ztb/ztbxqEdit.jsp">
								<button id="xiugai" class="btn" type="button">
									修改
								</button>
							</app:oPerm> <%-- <app:oPerm url="/jsp/business/zjb/jsgl/ZhaoBiaoXuQiuController.do?updateZhaobiaoxuqiu">
		 <button id="tj_btn" class="btn"  type="button">提交</button>
		 </app:oPerm> --%>
		 					
		 					<app:oPerm url="/jsp/framework/common/aplink/defaultArchivePage.jsp">
								<button id="sp_btn" class="btn" type="button">呈请审批</button>
							</app:oPerm>
							
							<app:oPerm url="/jsp/business/ztb/ztbgl_xx.jsp">
								<button id="btnZbxx" class="btn" type="button">招标信息</button>
							</app:oPerm>				
										
							<button id="btnExp" class="btn" type="button">
								导出
							</button> </span>
					</h4>
					<form method="post" id="demoForm2">
						<table class="B-table" id="DT" width="100%">
							<tr style="display: none;">
								<td>
									<input type="text" id="GC_ZTB_XQ_ID" name="GC_ZTB_XQ_ID"
										fieldname="GC_ZTB_XQ_ID" />
								</td>
								<td>
									<input type="text" id="XQZT" name="XQZT" fieldname="XQZT" />
								</td>
							</tr>
						</table>
					</form>
					<form method="post"
						action="${pageContext.request.contextPath }/insertdemo.do"
						id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT id="num" type="text" class="span12" keep="true"
										kind="text" fieldname="rownum" value="1000" operation="<=" />
								</TD>
							</TR>
							<!--可以再此处加入hidden域作为过滤条件 -->
							<tr>
								<th width="3%" class="right-border bottom-border text-right">年度</th>
								<td class="right-border bottom-border" width="5%">
									<select class="span12 year" id="QueryND" name="QueryND" kind="dic" src="T#GC_ZTB_XQ:distinct ND A:ND B:SFYX='1' and ND is not null order by ND asc"
										fieldname="X.ND" operation="=" defaultMemo="全部">
									</select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									工作名称
								</th>
								<td width="15%" class=" right-border bottom-border">
									<input class="span12" type="text" placeholder="" name="GZMC"
										fieldname="GZMC" operation="like" logic="and">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									招标类型
								</th>
								<td width="10%" class=" right-border bottom-border">
									<select class="span12 4characters" id="ZBLX" name="ZBLX"
										fieldname="ZBLX" defaultMemo="全部" kind="dic" src="ZBLX"
										operation="=">
									</select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									招标方式
								</th>
								<td width="10%" class=" right-border bottom-border">
									<select class="span12 4characters" id="ZBFS" name="ZBFS"
										fieldname="ZBFS" defaultMemo="全部" kind="dic" src="ZBFS"
										operation="=">
									</select>
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									状态
								</th>
								<td width="10%" class=" right-border bottom-border">
									<select class="span12 4characters" id="ZBFS" name="XQZT"
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
								<th width="5%" class="bottom-border text-right">
									项目名称
								</th>
								<td width="15%" class=" bottom-border" colspan="3">
									<input class="span12" type="text" placeholder="" name="GZMC"
										fieldname=" B.XMMC" operation="like" logic="and">
								</td>
							  	<td colspan="6"></td>
							</tr>
							
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
					<table width="100%" class="table-hover table-activeTd B-table"
						id="DT1" type="single" printFileName="部门招标需求">
						<thead>
							<tr>
								<th name="XH" id="_XH">
									&nbsp;#&nbsp;
								</th>
								<th fieldname="SFYX" maxlength="15" tdalign="center" CustomFunction="doRandering" noprint="true">
									&nbsp;&nbsp;
								</th>
								<th fieldname="XQZT" tdalign="center"  CustomFunction="docolor">
									&nbsp;需求状态&nbsp;
								</th>
								<th fieldname="EVENTSJBH" maxlength="15">
									&nbsp;审批状态&nbsp;
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
								<th fieldname="YSE" maxlength="15" tdalign="right"
									CustomFunction="doRanderingYuan">
									&nbsp;投资额(元)&nbsp;
								</th>
								<th fieldname="GZNR" maxlength="15">
									&nbsp;工作内容&nbsp;
								</th>
								<th fieldname="SXYQ" maxlength="15">
									&nbsp;时限要求&nbsp;
								</th>
								<th fieldname="QFBZ" maxlength="15">
									&nbsp;取费标准&nbsp;
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