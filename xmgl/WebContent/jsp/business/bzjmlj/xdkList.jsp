<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
	
		<app:base />
		<title></title>
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String tiaojian = request.getParameter("tiaojian");
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
		<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>
	<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/bzjk/bzjkCommonController.do";
			var controllername1= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
			g_bAlertWhenNoResult = false;
			var g_nd = '<%=nd%>';
			var g_proKey = '<%=proKey%>';
			var g_tiaojian = '<%=tiaojian%>';
			var btnNum = 0;
			var objRow;
			
			//计算本页表格分页数
			function setPageHeight(){
				var getHeight=getDivStyleHeight();
				var height = getHeight-pageTitle-pageQuery-getTableTh(4)-pageNumHeight;
				var pageNum = parseInt(height/pageTableOne,10);
				$("#DT1").attr("pageNum",pageNum);
			}
			
			//页面初始化
			$(function() {
				setPageHeight();
				//自动完成项目名称模糊查询
				showAutoComplete("QXMMC",controllername1+"?xmmcAutoCompleteToXmxdk","getXmmcQueryCondition"); 
				
				doSearch();
				//按钮绑定事件（导出EXCEL）
				$("#btnExp").click(function() {
					  if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
					      printTabList("DT1");
					  }
				});
				//按钮绑定事件（查询）
				$("#btnQuery").click(function() {
					doSearch();
				});
				//按钮绑定事件（清空）
			    $("#btnClear").click(function() {
			        $("#queryForm").clearFormResult();
			    });
			  
			});
			function doSearch(){
				var condProKey = "";
				if(g_proKey!=null&&g_proKey!=""){
					condProKey = "&proKey="+g_proKey;
				}
				var condNd = "";
				if(g_nd!=null&&g_nd!=""){
					condNd = "&nd="+g_nd;
				}
				var condTiaojian = "";
				if(g_tiaojian!=null&&g_tiaojian!=""){
					condTiaojian = "&tiaojian="+g_tiaojian;
				}
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryXDKList"+condTiaojian+condNd+condProKey,data,DT1,"queryAfter",false);
			}
			function tr_click(obj,tabId){
				var rowValue = $("#"+tabId).getSelectedRow();//获得选中行的json 字符串
				var tempJson = convertJson.string2json1(rowValue);//字符串转JSON对象
			}
			//项目名称自动模糊查询参数
			function getXmmcQueryCondition(){
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				return data;
			}
			//详细信息
			function rowView(index){
				var obj = $("#DT1").getSelectedRowJsonByIndex(index);
				var id = convertJson.string2json1(obj).XMID;
				$(window).manhuaDialog(xmscUrl(id));
			}
			//判断是否是项目
			function doBdmc(obj){
				  var bd_name=obj.BDMC;
				  if(bd_name==null||bd_name==""){
					  return '<div style="text-align:center">—</div>';
				  }else{
					  return bd_name;			  
				  }
			}
			//判断是否是项目
			function doBdbh(obj){
				  var bd_name=obj.BDBH;
				  if(bd_name==null||bd_name==""){
					  return '<div style="text-align:center">—</div>';
				  }else{
					  return bd_name;			  
				  }
			}

			//修改计划执行名称
			function rename(obj)
			{
				var isxd = obj.CJXMSX;
				if(isxd=='1')
				{
					return "年初计划";
				}
				else
				{
					return "追加计划";
				}	
			}
			
			function queryAfter(){
				var getHeight=getDivStyleHeight();
				getHeight=getHeight==0?611:getHeight;
			 	var height = getHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1);
				var pageNum = parseInt(height/pageTableOne,10);
				
				var rows = $("tbody tr" ,$("#DT1"));
				var tr_obj = rows.eq(0);
			    var t = $("#DT1").getTableRows();
			    var tr_height = $(tr_obj).height();
			    var d = t*(tr_height)+getTableTh(2)+20;
			     if(d<200) d = 150;
			    // 当高度大于400时，显示主页面列表的高度
			    if(d>400) d = height;
			    // 当没有数据的时候，只显示表头
			 	if(tr_height==null) d = getTableTh(2)+20;
			 	window_width = document.documentElement.clientWidth;//$("#allDiv").width()

			 	$("#DT1").fixTable({
					fixColumn: 2,//固定列数
					width:window_width-10,//显示宽度
					height:d//显示高度
				}); 
			}
			//列表项<稳定度>加图标
			function doWDD(obj){
				var wdd = obj.WDD;
				if(wdd != ""){
					if(wdd == "1"){
						return '<i title="'+obj.WDD_SV+'" class="icon-green"></i>';
					}else if(wdd == "2"){
						return '<i title="'+obj.WDD_SV+'" class="icon-yellow"></i>';
					}else if(wdd =="3"){
						return '<i title="'+obj.WDD_SV+'" class="icon-red"></i>';
					}
				}
			}
		</script>
	</head>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<h4 class="title">
						<span class="pull-right">
							<button id="btnExp" class="btn" type="button">
								导出
							</button>
						</span>
					</h4>
					<form method="post" id="queryForm">
						<table class="B-table" width="100%">
							<!--可以再此处加入hidden域作为过滤条件 -->
							<TR style="display: none;">
								<TD class="right-border bottom-border"></TD>
								<TD class="right-border bottom-border">
								</TD>
							</TR>
							    <tr>
				           <th width="5%" class="right-border bottom-border text-right">项目名称</th>
				          <td class="right-border bottom-border" width="15%">
				          	<input class="span12" type="text" placeholder="" name="QXMMC"
								fieldname="A.XMMC" operation="like" id="QXMMC" autocomplete="off"
								tablePrefix="A"/>
						  </td>
						      <th width="5%" class="right-border bottom-border text-right">项目性质</th>
				          <td class="right-border bottom-border" width="7%">
				          	  <select class="span12" id="XJXJ" name = "XJXJ" fieldname="A.XJXJ"  defaultMemo="全部" operation="="  kind="dic" src="XMXZ">
				              </select>
						  </td>
						       <th width="5%" class="right-border bottom-border text-right">项目类型</th>
				          <td class="right-border bottom-border" width="10%">
				          	  <select class="span12" id="XMLX" name = "XMLX" fieldname="A.XMLX"  defaultMemo="全部" operation="="  kind="dic" src="XMLX">
				              </select>
						  </td>
						   <th class="right-border bottom-border text-right">项目属性</th>
					          <td class="bottom-border">
					          	<select class="span12 2characters" name = "QXMSX" fieldname = "A.XMSX" defaultMemo="全部" operation="=" kind="dic" src="XMSX">
					            </select>
					          </td>
					          <th class="right-border bottom-border text-right">下达状态</th>
					          <td class="bottom-border">
					          	<select class="span12 3characters" name = "ISNATC" fieldname = "A.ISNATC" defaultMemo="全部" operation="=" kind="dic" src="XDZT">
					            </select>
					          </td>
					           <td class="text-left bottom-border text-right" rowspan="2" >
					           <button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
					           <button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
					          </td>
				          </tr>
				          <tr>
					        	<th class="right-border bottom-border text-right" width="5%">项目法人</th>
					          <td class="bottom-border" width="20%">
					          	<select class="span12 " name = "XMFR" fieldname = "CBK.XMFR" defaultMemo="全部" operation="=" kind="dic" src="XMFR">
					            </select>
					          </td>
					           <th class="right-border bottom-border text-right" width="4%">稳定度</th>
					          <td class="bottom-border" width="7%">
					          	<select class="span12  " name = "WDD" fieldname = "A.WDD" defaultMemo="全部" operation="=" kind="dic" src="WDD">
					            </select>
					          </td>
				         	<td colspan="6"></td>
				         </tr>
							<!--可以再此处加入hidden域作为过滤条件 -->
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" printFileName="项目详细列表">
							<thead>
			              	<tr>
								<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
								<!-- <th fieldname="XMBH" rowspan="2" colindex=2 hasLink="true" linkFunction="rowView">项目编号</th> -->
								<th fieldname="XMMC" rowspan="2" colindex=2  rowMerge="true" maxlength="15">&nbsp;项目名称&nbsp;</th>
								<th fieldname="PCH" rowspan="2" colindex=3 tdalign="center">计财批</th>
								<th fieldname="XMDZ" rowspan="2" colindex=4 maxlength="10">&nbsp;建设位置&nbsp;</th>	
								<th fieldname="XJXJ" rowspan="2" colindex=5 tdalign="center">&nbsp;项目性质&nbsp;</th>
								<th fieldname="JSMB" rowspan="2" colindex=6 maxlength="15">&nbsp;年度目标&nbsp;</th>
								<th fieldname="XMLX" rowspan="2" colindex=7 maxlength="15">&nbsp;项目类型&nbsp;</th>
								<th fieldname="ISNATC" rowspan="2" colindex=8 tdalign="center">&nbsp;是否下达&nbsp;</th>
								<th fieldname="XMFR" rowspan="2" colindex=9 maxlength="10">项目法人</th>
								<th fieldname="WDD" rowspan="2" colindex=10 tdalign="center" CustomFunction="doWDD">稳定度</th>
								<th fieldname="ISBT"  rowspan="2" colindex=11 tdalign="center">&nbsp;BT&nbsp;</th>
								<th fieldname="JSNR"  rowspan="2" colindex=12 maxlength="10" >&nbsp;建设内容及规模&nbsp;</th>
								<th fieldname="SGDW"  rowspan="2" colindex=13 tdalign="center"  >&nbsp;施工单位&nbsp;</th>
								<th fieldname="JLDW"  rowspan="2" colindex=14 tdalign="center"  >&nbsp;监理单位&nbsp;</th>
								<th fieldname="KGSJ_SJ"  rowspan="2" colindex=15 tdalign="center"  >&nbsp;开工日期&nbsp;</th>
								<th fieldname="WGSJ_SJ"  rowspan="2" colindex=16 maxlength="10" tdalign="center">&nbsp;完工日期&nbsp;</th>
								<th colspan="4">年度总投资额（元）</th>
								<th fieldname="BZ" rowspan="2" colindex=21 maxlength="15">备注</th>
						 </tr>
						<tr>
								<th fieldname="GC" colindex=17 tdalign="right">工程</th>
								<th fieldname="ZC" colindex=18 tdalign="right">征拆</th>
								<th fieldname="QT" colindex=19 tdalign="right">其他</th>
								<th fieldname="JHZTZE" colindex=20 tdalign="right">合计</th>
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
					fieldname="Z.XMID,Z.PXH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>