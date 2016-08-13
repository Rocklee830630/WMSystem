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
			String sxbh = request.getParameter("sxbh");
			String sxmc = request.getParameter("sxmc");
			String bllx = request.getParameter("bllx");	
			String sgflag = request.getParameter("sgflag");
			String tiaojian = request.getParameter("tiaojian");
			String iskg = request.getParameter("iskg");
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
			var controllername= "${pageContext.request.contextPath }/bzjkjm/QqBzjkCommonController.do";
			var controllername1= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
			g_bAlertWhenNoResult = false;
			var g_nd = '<%=nd%>';
			var g_proKey = '<%=proKey%>';
			var iskg='<%=iskg%>';
			var sxbh='<%=sxbh%>';
			var sxmc='<%=sxmc%>';
			var bllx='<%=bllx%>';
			var sgflag='<%=sgflag%>';
			var g_tiaojian = '<%=tiaojian%>';
			var btnNum = 0;
			var objRow;
			
			//计算本页表格分页数
			function setPageHeight(){
				var getHeight=getDivStyleHeight();
		  		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
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
				
				var btn = $("#queryBtn");
				btn.click(function() {
					doSearchQqSg();
				});

				$("#query_clear").click(function() {
			       $("#queryForm").clearFormResult();
				});

				//自动完成项目名称模糊查询
				showAutoComplete("QXMMC",controllername1+"?xmmcAutoCompleteToXmxdk","getXmmcQueryCondition");
			});

			//项目名称自动模糊查询参数
			function getXmmcQueryCondition(){
				var data = combineQuery.getQueryCombineData(queryForm,frmPost_auto,DT1);
				return data;
			}
			
			//页面默认参数
			function doInit() {
				doSearchQqSg();
			}
			function doSearchQqSg(){
				var condSxbh = "";
				if(sxbh!=null&&sxbh!=""){
					condSxbh = "&sxbh="+sxbh;
				}
				var condSxmc = "";
				if(sxmc!=null&&sxmc!=""){
					condSxmc = "&sxmc="+sxmc;
				}			
				var condBllx = "";
				if(bllx!=null&&bllx!=""){
					condBllx = "&bllx="+bllx;
				}				
				var condSgflag = "";
				if(sgflag!=null&&sgflag!=""){
					condSgflag = "&sgflag="+sgflag;
				}
				var condNd = "";
				if(g_nd!=null&&g_nd!=""){
					condNd = "&nd="+g_nd;
				}
				var conIskg = "";
				if(iskg!=null&&iskg!=""){
					conIskg = "&iskg="+iskg;
				}
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryListQqsg"+condNd+conIskg+condSxbh+condSxmc+condBllx+condSgflag,data,DT1,null,false);
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

			  //标段名称
			  function doBdmc(obj){
				  var bd_name=obj.BDMC;
				  if(bd_name==null||bd_name==""){
					  return '<div style="text-align:center">—</div>';
				  }else{
					  return bd_name;			  
				  }
			  }
			//标段名称
			  function doBdbh(obj){
				  var bd_bh=obj.BDBH;
				  if(bd_bh==null||bd_bh==""){
					  return '<div style="text-align:center">—</div>';
				  }else{
					  return bd_bh;			  
				  }
			  }
			/* 报建 */
		 	//附件显示
		 	function doFj1(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.BJ;
		 		var sxlx='0015';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	//时间显示
		 	function doDa1(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.BJFK;
		 		var sxlx='0015';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	/* 其他手续 */
		 	//附件显示
		 	function doFj2(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.QTSX;
		 		var sxlx='0016';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	//时间显示
		 	function doDa2(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.QTSXFK;
		 		var sxlx='0016';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	/* 质量监督 */
		 	//附件显示
		 	function doFj3(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.ZLJD;
		 		var sxlx='0017';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	//时间显示
		 	function doDa3(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.ZLJDFK;
		 		var sxlx='0017';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	/* 安全监督 */
		 	//附件显示
		 	function doFj4(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.AQJD;
		 		var sxlx='0018';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	//时间显示
		 	function doDa4(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.AQJDFK;
		 		var sxlx='0018';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	/* 造价管理 */
		 	//附件显示
		 	function doFj5(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.ZJGL;
		 		var sxlx='0019';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	//时间显示
		 	function doDa5(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.ZJGLFK;
		 		var sxlx='0019';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	
		 	/* 双拖欠 */
		 	//附件显示
		 	function doFj6(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.STQ;
		 		var sxlx='0020';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	//时间显示
		 	function doDa6(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.STQFK;
		 		var sxlx='0020';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	/* 执法监察 */
		 	//附件显示
		 	function doFj7(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.ZFJC;
		 		var sxlx='0021';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	//时间显示
		 	function doDa7(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.ZFJCFK;
		 		var sxlx='0021';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	/* 施工许可 */
		 	//附件显示
		 	function doFj8(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.SGXK;
		 		var sxlx='0022';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	//时间显示
		 	function doDa8(obj){
		 		var bblsx=obj.BBLSX;
		 		var nr=obj.SGXKFK;
		 		var sxlx='0022';
		 		var fk=doOpera(nr,bblsx,sxlx);
		 		return fk;
		 	}
		 	
		 	
		 	//显示方法
		 	function doOpera(nr,bblsx,sxlx){
		 		var point="";
		 		var check=false;
				if(bblsx==null||bblsx==""){
					return point;
				}else{
			 		var sxArray = new Array();
					sxArray = bblsx.split(",");
					for(var x=0;x<sxArray.length;x++){
						if(sxArray[x]==sxlx){
							check=true;
							break;
						}else{
							continue;
						}	
					}
				}
				if(check){
					point ='<div style="text-align:center">—</div>';
				}
				return point;
		 	} 
			//详细信息
			function rowView(index){
				var obj = $("#DT1").getSelectedRowJsonByIndex(index);
				var id = convertJson.string2json1(obj).XMID;
				$(window).manhuaDialog(xmscUrl(id));
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
							<tr>
								<th width="5%" class="right-border bottom-border">项目名称</th>
								<td width="15%" class="right-border bottom-border">
									<input class="span12" type="text" placeholder="" id="QXMMC" name="QXMMC" fieldname="XMMC" operation="like" logic="and"></td>
								<th width="5%" class="right-border bottom-border">项目标段</th>
								<td width="15%" class="right-border bottom-border">
									<input class="span12" type="text" placeholder="" id="BDMC" name="BDMC" fieldname="BDMC" operation="like" logic="and">
								</td>
								<th width="5%" class="right-border bottom-border">项目标志</th>
								<td width="15%" class="bottom-border">
									<label class="checkbox inline">
										<input type="checkbox" name="XMBZ" fieldname="XMBS" value="0" operation="=" logic="and" title="项目标志" checked>只查项目
									</label>
								</td>
								<td width="13%"  class="text-left bottom-border text-right" rowspan="2">
									<button	id="queryBtn" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
				                    <button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
					            </td>
							</tr>
						</table>
					</form>
					<div style="height: 5px;"></div>
					<div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" printFileName="项目详细列表">
							<thead>
					 			<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
									<th fieldname="XMBH" id="E_XMHB" maxlength="15" rowMerge="true" hasLink="true" linkFunction="rowView" rowspan="2" colindex=2>&nbsp;项目编号&nbsp;</th>
						            <th fieldname="XMMC" id="XMMC" rowmerge="true" colindex=3 maxlength="15" rowspan=2>&nbsp;项目名称&nbsp;</th>
						            <th fieldname="BDBH" id="BDBH" maxlength="15" rowspan="2" colindex=4 CustomFunction="doBdbh">&nbsp;标段编号&nbsp;</th>
						            <th fieldname="BDMC" id="BDMC" rowspan="2" colindex=5 maxlength="15" CustomFunction="doBdmc">&nbsp;标段名称&nbsp;</th>
						            <th fieldname="XMBDDZ" id="BDMC" rowspan="2" colindex=6 maxlength="15" >&nbsp;项目地址&nbsp;</th>
						            <th colspan="16">&nbsp;进度&nbsp;</th>
						            <th rowspan="2" fieldname="BJSJ" colindex=23 tdalign="center">&nbsp;办结时间&nbsp;</th>
						            <th rowspan="2" fieldname="CZWT" colindex=24 maxlength="15">&nbsp;存在问题&nbsp;</th>
					            </tr>
					            <tr>
						            <th fieldname="BJ" colindex=7 tdalign="center" CustomFunction="doFj1">&nbsp;报建&nbsp;</th>
						            <th fieldname="BJFK" colindex=8 tdalign="center" CustomFunction="doDa1">&nbsp;反馈日期&nbsp;</th>
						            <th fieldname="QTSX" colindex=9 tdalign="center" CustomFunction="doFj2">&nbsp;其他手续&nbsp;</th>
						            <th fieldname="QTSXFK" colindex=10 tdalign="center" CustomFunction="doDa2">&nbsp;反馈日期&nbsp;</th>
						            <th fieldname="ZLJD" colindex=11 tdalign="center" CustomFunction="doFj3">&nbsp;质量监督&nbsp;</th>
						            <th fieldname="ZLJDFK" colindex=12 tdalign="center" CustomFunction="doDa3">&nbsp;反馈日期&nbsp;</th>
						            <th fieldname="AQJD" colindex=13 tdalign="center" CustomFunction="doFj4">&nbsp;安全监督&nbsp;</th>
						            <th fieldname="AQJDFK" colindex=14 tdalign="center" CustomFunction="doDa4">&nbsp;反馈日期&nbsp;</th>
						            <th fieldname="ZJGL" colindex=15 tdalign="center" CustomFunction="doFj5">&nbsp;造价管理&nbsp;</th>
						            <th fieldname="ZJGLFK" colindex=16 tdalign="center" CustomFunction="doDa5">&nbsp;反馈日期&nbsp;</th>
						            <th fieldname="STQ" colindex=17 tdalign="center" CustomFunction="doFj6">&nbsp;双拖欠&nbsp;</th>
						            <th fieldname="STQFK" colindex=18 tdalign="center" CustomFunction="doDa6">&nbsp;反馈日期&nbsp;</th>
						            <th fieldname="ZFJC" colindex=19 tdalign="center" CustomFunction="doFj7">&nbsp;执法监察&nbsp;</th>
						            <th fieldname="ZFJCFK" colindex=20 tdalign="center" CustomFunction="doDa7">&nbsp;反馈日期&nbsp;</th>
						            <th fieldname="SGXK" colindex=21  tdalign="center"CustomFunction="doFj8">&nbsp;施工许可&nbsp;</th>
						            <th fieldname="SGXKFK" colindex=22 tdalign="center" CustomFunction="doDa8">&nbsp;反馈日期&nbsp;</th>
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
				<input type="hidden" name="txtFilter" order="asc" fieldname = "JHSJ.XMBH,JHSJ.XMBS,JHSJ.PXH" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
		
		
		<div align="center">
			<FORM name="frmPost_auto" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="txtFilter" order="asc" fieldname = "" id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
			</FORM>
		</div>
	</body>
</html>