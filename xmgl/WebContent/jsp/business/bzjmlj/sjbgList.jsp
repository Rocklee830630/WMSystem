<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title></title>
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String flag = request.getParameter("flag");
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
		<script type="text/javascript" charset="utf-8">
			//请求路径，对应后台RequestMapping
			var controllername= "${pageContext.request.contextPath }/bzjk/bzjkCommonController.do";
			g_bAlertWhenNoResult = false;
			var g_nd = '<%=nd%>';
			var g_proKey = '<%=proKey%>';
			var flag='<%=flag%>';
			var g_tiaojian = '<%=tiaojian%>';
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
				doSearch();
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
				});
			 	//清空查询条件
			    var btn_clearQuery = $("#query_clear");
			    btn_clearQuery.click(function() 
			      {
			        $("#queryForm").clearFormResult();
			      });
			});
			//页面默认参数
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
				defaultJson.doQueryJsonList(controllername+"?querySJBGList"+condTiaojian+condNd+condProKey,data,DT1,null,false);
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
						<INPUT id="num" type="text" class="span12" keep="true" kind="text" fieldname="rownum" value="1000" operation="<="/>
						</TD>
					</TR>
					<tr>
					    <th width="5%" class="right-border bottom-border text-right">项目名称</th>
					          <td class="right-border bottom-border" width="15%">
					          	<input class="span12" type="text" placeholder="" name="QXMMC"
									fieldname="T.XMMC" operation="like" id="QXMMC" autocomplete="off"
									tablePrefix="A"/>
							  </td>
							    <th width="5%" class="right-border bottom-border text-right">标段名称</th>
					          <td class="right-border bottom-border" width="15%">
					          	<input class="span12" type="text" placeholder="" name="QXMMC"
									fieldname="T.BDMC" operation="like" id="BDMC"   />
							  </td>
							      <th width="5%" class="right-border bottom-border text-right">项目性质</th>
					          <td class="right-border bottom-border" width="10%">
					          	  <select class="span12" id="XJXJ" name = "XJXJ" fieldname="T.XMXZ"  defaultMemo="全部" operation="="  kind="dic" src="XMXZ">
					              </select>
							  </td>
							       <th width="5%" class="right-border bottom-border text-right">变更类别</th>
					          <td class="right-border bottom-border" width="10%">
					          	  <select class="span12" id="SJBG" name = "SJBG" fieldname="A.BGLB"  defaultMemo="全部" operation="="  kind="dic" src="BGLB2">
					              </select>
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
								    <th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
								    <th fieldname="XMMC"   maxlength="15">&nbsp;项目名称&nbsp;</th>
								    <th fieldname="BDMC"   maxlength="15">&nbsp;标段名称&nbsp;</th>
								    <th fieldname="XMDZ"   maxlength="15">&nbsp;项目地址&nbsp;</th>
								    <th fieldname="JBGRQ" tdalign="center" >&nbsp;变更日期&nbsp;</th>
								    <th fieldname="BGLB" tdalign="center" >&nbsp;变更类别&nbsp;</th>
							        <th fieldname="BGFY" tdalign="right" >&nbsp;变更费用&nbsp;</th>
						            <th fieldname="BGNR"  maxlength="15">&nbsp;变更内容&nbsp;</th>
						            <th fieldname="YZDB"  maxlength="15">&nbsp;业主代表&nbsp;</th>
									<th fieldname="SJDW"  maxlength="15">&nbsp;设计单位&nbsp;</th>
									<th fieldname="SGDW" maxlength="15">&nbsp;施工单位&nbsp;</th>
									<th fieldname="JLDW" maxlength="15">&nbsp;监理单位&nbsp;</th>
									<th fieldname="GS" >&nbsp;概算&nbsp;</th>
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