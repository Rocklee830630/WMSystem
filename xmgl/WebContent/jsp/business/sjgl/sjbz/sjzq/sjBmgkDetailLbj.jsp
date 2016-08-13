<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title></title>
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
		%>
		<script type="text/javascript" charset="utf-8">
	var g_nd = '<%=nd%>';
	var g_proKey = '<%=proKey%>';
	var json2,json;
	var rowindex,rowValue,text,aa;
	var jhsjid,lbjid,bdmc,zxgs;
	var controllername= "${pageContext.request.contextPath }/sjSjzqController.do";
  
  
	//计算本页表格分页数
	function setPageHeight(){
		var getHeight=getDivStyleHeight();
		var height = getHeight-pageTopHeight-pageTitle-getTableTh(2)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
  
	$(function() {
		setPageHeight();
		//页面初始化
		doInit();
		//查询方法
		var btn = $("#query");
		btn.click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryLbj",data,DT1,null,false);
			//
				});
		//维护页面链接
		var insert=$("#wh");
		insert.click(function() {
			$(window).manhuaDialog({"title":"拦标价管理>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/lbj/lbjwh.jsp","modal":"1"});
		});
		//清空查询条件
		var clear=$("#clear");
		clear.click(function(){
			$("#queryForm").clearFormResult();
			initCommonQueyPage();
			//$("#DT1").cancleSelected();
		});
		//导出Excel
		var excel=$("#excel");
		excel.click(function(){
			 if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      printTabList("DT1");
			  }
		});
		//新增
		var addlbj = $("#addlbj");
		addlbj.click(function() 
	 			{
			var index2= $("#DT1").getSelectedRowIndex();
			if(index2<0){
				requireSelectedOneRow();;
			}else{
				if(lbjid==null||lbjid=="")
				  {
				   $("#resultXML").val($("#DT1").getSelectedRow());
	 		       $(window).manhuaDialog({"title":"拦标价>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/lbj/lbjadd.jsp","modal":"2"});
				  } 
				else{
					xInfoMsg("拦标价已添加","");
					 return;
				    } 
			}
			}); 
		//新增委托咨询公司
		var WTZXGS = $("#WTZXGS");
		WTZXGS.click(function() 
	 			{
				$(window).manhuaDialog({"title":"拦标价>委托咨询公司","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/ztbxqwh.jsp","modal":"1"});
			}); 
		
	});
	function doInit(){
		doSearch();
	}
	function doSearch(){
		var condProKey = "";
		if(g_proKey!=null&&g_proKey!=""){
			condProKey = "&proKey="+g_proKey;
		}
		var condNd = "";
		if(g_nd!=null&&g_nd!=""){
			condNd = "&nd="+g_nd;
		}
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		defaultJson.doQueryJsonList(controllername+"?queryDrillingDataLbj"+condNd+condProKey,data,DT1,null,false);
	}
   //点击行获取数据
	function tr_click(obj,DT1){
	
		$("#LbjForm").setFormValues(obj);
		index= $("#DT1").getSelectedRowIndex();
		jhsjid=$(obj).attr("JHSJID");
		lbjid=$(obj).attr("GC_ZJB_LBJB_ID");
		//新增的数据串
		json2 = $("#DT1").getSelectedRowText();
		json2=encodeURI(json2);
		bdmc=$(obj).attr("BDMC");
		zxgs=$(obj).attr("ZXGS");
		//答疑的数据串
		json=JSON.stringify(obj);
		json=encodeURI(json);
		
	}
	//添加弹出页回显修改
	function xiugaihang(data)
	{
			var index =	$("#DT1").getSelectedRowIndex();
			var subresultmsgobj = defaultJson.dealResultJson(data);
			var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,index);
			$("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,index);
			$("#DT1").cancleSelected();
			$("#DT1").setSelect(index);
			lbjid=$(subresultmsgobj).attr("GC_ZJB_LBJB_ID");
			//alert(lbjid);
			json2 = $("#DT1").getSelectedRowText();
			//json串加密
			json2=encodeURI(json2);
			xSuccessMsg("操作成功","");
		 
	}
	//答疑弹出页回显修改
	function dayixiugaihang(jhsjid)
	{
		$.ajax(
		{
			   url : controllername+"?queryLbj",//此处定义后台controller类和方法
		         data : {jhsjid:jhsjid},    //此处为传入后台的数据，可以为json，可以为string，如果为json，那起结构必须和后台接收的bean一致或和bean的get方法名一致，例如｛id：1，name：2｝后台接收的bean方法至少包含String id,String name方法  如果为string，那么可以写为{portal: JSON.stringify(data)}, 后台接收的时候参数可以为String，名字必须和前台保持一致及定义为String portal
		         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
		         async : false,   //同步执行，即执行完ajax方法后才可以执行下面的函数，如果不设置则为异步执行，及ajax和其他函数是异步的，可能后面的代码执行完了，ajax还没执行
		         success : function(result) {
		         var resultmsg = result.msg; //返回成功事操作
		      	 var index= $("#DT1").getSelectedRowIndex();
				 var subresultmsgobj1 = defaultJson.dealResultJson(resultmsg);
				 $("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
				 $("#DT1").cancleSelected();
				 $("#DT1").setSelect(index);
				 lbjid=$(subresultmsgobj1).attr("GC_ZJB_LBJB_ID");
				//fym();
		         },
		         error : function(result) {//返回失败操作
		           // alert(result.msg);
		           defaultJson.clearTxtXML();
		          }			
		}		
		);
	}
	//子页面调用添加行
	function tianjiahang(data)
	{
		xSuccessMsg("操作成功","");
	}
	//详细信息
	function rowView(index){
		var obj = $("#DT1").getSelectedRowJsonByIndex(index);
		var id = convertJson.string2json1(obj).XMID;
		$(window).manhuaDialog(xmscUrl(id));
	}
	  //标段名称
	  function doBdmc(obj){
		  var bd_name=obj.BDMC;
		  if(bd_name==null||bd_name==""){
			  /* return '<div style="text-align:center"><abbr title=本条项目信息没有标段内容>—</abbr></div>'; */
			  return '<div style="text-align:center">—</div>';
		  }else{
			  return bd_name;			  
		  }
	  }
	  //标段编号
	    function doBdBH(obj){
		  var bd_name=obj.BDBH;
		  if(bd_name==null||bd_name==""){
			  return '<div style="text-align:center">—</div>';
		  }else{
			  return bd_name;			  
		  }
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
						<span class="pull-right">
							<button id="excel" class="btn" type="button">
								导出
							</button> </span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
									<INPUT id="num" type="text" class="span12" kind="text"
										fieldname="rownum" value="1000" operation="<=" keep="true" />
								</TD>
							</TR>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table width="100%" class="table-hover table-activeTd B-table"
							id="DT1" type="single" editable="0">
							<thead>
								<tr>
									<th name="XH" id="_XH" tdalign="">
										&nbsp;#&nbsp;
									</th>
									<th fieldname="XMBH" rowMerge="true" hasLink="true"
										linkFunction="rowView">
										&nbsp;项目编号&nbsp;
									</th>
									<th fieldname="XMMC" rowMerge="true" maxlength="15">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDBH" maxlength="15" Customfunction="doBdBH">
										&nbsp;标段编号&nbsp;
									</th>
									<th fieldname="BDMC" tdalign="" Customfunction="doBdmc"
										maxlength="15">
										&nbsp;标段名称&nbsp;
									</th>
									<th fieldname="CSBGBH" tdalign="">
										&nbsp;财审报告编号&nbsp;
									</th>
									<th fieldname="LRSJ" tdalign="center" class="wrap">
										<span style="width: 100px; display: block;">&nbsp;提需求时间
											<p></p>(招造价公司)</span>
									</th>
									<th fieldname="KBRQ" tdalign="center" class="wrap">
										<span style="width: 120px; display: block;">招标时间
											<p></p>(内部咨询公司)</span>
									</th>
									<th fieldname="ZXGS" tdalign="" maxlength="15">
										&nbsp;咨询公司&nbsp;
									</th>
									<th fieldname="TZJJSJ" tdalign="center" class="wrap">
										<span style="width: 100px; display: block;">&nbsp;与总工办交接图纸日期</span>
									</th>
									<th fieldname="ZXGSJ" tdalign="center" class="wrap">
										<span style="width: 100px; display: block;">&nbsp;发给咨询公司图纸日期</span>
									</th>
									<th fieldname="YWRQ" tdalign="center" class="wrap">
										<span style="width: 100px; display: block;">&nbsp;提出疑问日期</span>
									</th>
									<th fieldname="HFRQ" tdalign="center" class="wrap">
										<span style="width: 100px; display: block;">&nbsp;答疑日期</span>
									</th>
									<!-- <th fieldname="SGFAJS"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;施工方案接收日期</span></th>
							<th fieldname="ZBWJJS"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;招标文件接收日期</span></th> -->
									<th fieldname="ZXGSRQ" tdalign="center" class="wrap">
										<span style="width: 100px; display: block;">&nbsp;咨询公司
											<p></p>交造价组日期</span>
									</th>
									<th fieldname="JGBCSRQ" tdalign="center" class="wrap">
										<span style="width: 80px; display: block;">&nbsp;建管报财审日期</span>
									</th>
									<th fieldname="SBCSZ" tdalign="right" maxlength="15">
										&nbsp;上报财审值(元)&nbsp;
									</th>
									<th fieldname="CZSWRQ" tdalign="center" class="wrap">
										<span style="width: 100px; display: block;">&nbsp;财政审完日期</span>
									</th>
									<th fieldname="CSSDZ" tdalign="right" maxlength="15">
										&nbsp;财审审定值(元)&nbsp;
									</th>
									<th fieldname="SJZ" tdalign="right" maxlength="15">
										&nbsp;审减值(元)&nbsp;
									</th>
									<th fieldname="SJBFB" tdalign="right">
										&nbsp;审减百分比(%)&nbsp;
									</th>
									<th fieldname="ZDJ" tdalign="right" maxlength="15" class="wrap">
										<span style="width: 105px; display: block;">&nbsp;暂定金</span>
									</th>
									<th fieldname="BZ" tdalign="" maxlength="15">
										&nbsp;备注&nbsp;
									</th>
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
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id="queryResult" />
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="jhsj.xmbh,jhsj.xmbs,jhsj.pxh" id="txtFilter">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>