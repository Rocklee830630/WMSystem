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
		<style>
		.myCellSpan{
			background-color:#FF8888 ;
			width:100%;
			height:100%;
			display:inline-block;
			margin:0;
			padding:0;
		}
		</style>
		<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/bzjk/bzjkCommonController.do";
			var controllername1= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
			g_bAlertWhenNoResult = false;
			var g_nd = '<%=nd%>';
			var g_proKey = '<%=proKey%>';
			var btnNum = 0;
			var objRow;
			
			//计算本页表格分页数
			function setPageHeight(){
				var getHeight=getDivStyleHeight();
				var height = getHeight-pageTopHeight-pageQuery-pageTitle-getTableTh(1)-pageNumHeight;
				var pageNum = parseInt(height/pageTableOne,10);
				$("#DT1").attr("pageNum",pageNum);
			}
			
			//页面初始化
			$(function() {
				setPageHeight();
				doInit();
				//按钮绑定事件（导出EXCEL）
				$("#btnExp").click(function() {
					  if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
					      printTabList("DT1");
					  }
				});
				//查询
				var btn = $("#chaxun");
				btn.click(function(){  
					doSearch();
					doPass();
				});
			 	//清空查询条件
			    var btn_clearQuery = $("#query_clear");
			    btn_clearQuery.click(function() 
			      {
			        $("#queryForm").clearFormResult();
			      });
			  //自动完成项目名称模糊查询
				showAutoComplete("QXMMC",controllername1+"?xmmcAutoCompleteToXmxdk","getXmmcQueryCondition"); 
		
			});
			//页面默认参数
			function doInit(){
				doSearch();
				doPass();
			}
			//项目名称自动模糊查询参数
			function getXmmcQueryCondition(){
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				return data;
			}
			function doSearch(){
				var condNd = "";
				if(g_nd!=null&&g_nd!=""){
					condNd = "&nd="+g_nd;
				}
				var condProKey = "";
				if(g_proKey!=null&&g_proKey!=""){
					condProKey = "&proKey="+g_proKey;
				}
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryList_gc_gcqs"+condNd+condProKey,data,DT1,null,false);
			}
			
			function tr_click(obj,tabId){
				var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
				var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
			}
			//详细信息
			function rowView(index){
				var obj = $("#DT1").getSelectedRowJsonByIndex(index);
				var id = convertJson.string2json1(obj).XMID;
				$(window).manhuaDialog(xmscUrl(id));
			}
			function doPass(){
				var condNd = "";
				if(g_nd!=null&&g_nd!=""){
					condNd = "&nd="+g_nd;
				}
				var condProKey = "";
				if(g_proKey!=null&&g_proKey!=""){
					condProKey = "&proKey="+g_proKey;
				}
				var data1 = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				var action1 = controllername+"?queryGcqsPass"+condNd+condProKey;
				var data = {
						msg : data1
					};
				$.ajax({
					url : action1,
					data:data,
					dataType:"json",
					type : 'post',
					async:false,
					success: function(result){
						var resultmsg = result.msg; //返回成功事操作\
				         var odd=convertJson.string2json1(resultmsg);
				         var zs=odd.response.data[0].ZS;
				         var pass=odd.response.data[0].PASS;
				         if(zs!=""&&zs!="0"&&pass!=""&&pass!="0"){
				        	 chazhi=Math.round((pass/zs* 1000))/10;
				        	 $("#tongGuoLv").html(chazhi+"%");
				         }else{
				        	 $("#tongGuoLv").html("0%");
				         }
					}
				});
			}
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title"><font  style="font-weight: normal;font-size: 11px;">当前数据洽商通过率:&nbsp;&nbsp;</font><font color="red" id ="tongGuoLv"></font>
						<span class="pull-right">
							<button id="btnExp" class="btn" type="button">
								导出
							</button>
						</span>
					</h4>
	<form method="post" id="queryForm">
		<table class="B-table" width="100%">
			<!--可以再此处加入hidden域作为过滤条件 -->
			   <tr>
							  <th width="5%" class="right-border bottom-border text-right">项目名称</th>
					          <td class="right-border bottom-border" width="15%">
					          	<input class="span12" type="text" placeholder="" name="QXMMC"
									fieldname="T.XMMC" operation="like" id="QXMMC" autocomplete="off"
									tablePrefix="T"/>
							  </td>
							    <th width="5%" class="right-border bottom-border text-right">标段名称</th>
					          <td class="right-border bottom-border" width="15%">
					          	<input class="span12" type="text" placeholder="" name="BDMC"
									fieldname="T.BDMC" operation="like" id="BDMC" />
							  </td>
						      <th width="5%" class="right-border bottom-border text-right">项目管理公司</th>
					          <td class="right-border bottom-border" width="10%">
					            <select class="span12" id="XMGLGS" name = "XMGLGS" fieldname="T.XMGLGS"  defaultMemo="全部" operation="="  kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC">
					             </select>
							  </td>
					  	      <th width="5%" class="right-border bottom-border text-right">洽商标题</th>
					          <td class="right-border bottom-border" width="15%">
					          	<input class="span12" type="text" placeholder="" name="QSBT"
									fieldname="QSBT" operation="like" id="QSBT" />
							  </td>
			       <td  class="text-left bottom-border text-right">
	              <button id="chaxun" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
                  <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
             </td>
			</tr>
		</table>
	</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" printFileName="项目详细列表">
							<thead>
				              <tr>
								<th name="XH" id="_XH" colindex=1>&nbsp;#&nbsp;</th>
									<th fieldname="XMMC" colindex=5 maxlength="15" tdalign="left">&nbsp;项目名称&nbsp;</th>
								<th fieldname="QSBT" colindex=2 maxlength="15" tdalign="left">&nbsp;洽商标题&nbsp;</th>
								<th fieldname="XMGLGS" colindex=11 maxlength="15" tdalign="left">&nbsp;项目管理公司&nbsp;</th>
								<th fieldname="BDMC" colindex=6 maxlength="15" tdalign="left">&nbsp;标段名称&nbsp;</th>
								<th fieldname="SJDWID" colindex=12 maxlength="15" tdalign="left">&nbsp;设计单位&nbsp;</th>
								<th fieldname="JLDWID" colindex=12 maxlength="15" tdalign="left">&nbsp;监理单位&nbsp;</th>
								<th fieldname="SGDWID" colindex=12 maxlength="15" tdalign="left">&nbsp;施工单位&nbsp;</th>
								<th fieldname="QSYY" colindex=4 maxlength="15" tdalign="left">&nbsp;洽商原因&nbsp;</th>
								<th fieldname="QSNR" colindex=4 maxlength="15" tdalign="left">&nbsp;洽商内容&nbsp;</th>
								<th fieldname="GSZJ" colindex=3 maxlength="15" tdalign="right">&nbsp;估算造价&nbsp;</th>
								<th fieldname="QSTCRQ" colindex=3 maxlength="15" tdalign="center">&nbsp;洽商提出日期&nbsp;</th>
								
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
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="t.XMID,t.PXH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	
	</body>
</html>