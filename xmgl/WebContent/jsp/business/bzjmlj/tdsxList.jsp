<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<title></title>
		<%	String nd = request.getParameter("nd");
			String proKey = request.getParameter("proKey");
			String sxbh = request.getParameter("sxbh");
			String sxmc = request.getParameter("sxmc");
			String bllx = request.getParameter("bllx");	
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
			var sxbh='<%=sxbh%>';
			var sxmc='<%=sxmc%>';
			var bllx='<%=bllx%>';
			
			//计算本页表格分页数
			function setPageHeight(){
				var getHeight=getDivStyleHeight();
				var height = getHeight-pageTopHeight-pageTitle-getTableTh(4)-pageNumHeight;
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
				//按钮绑定事件（查询）
				$("#btnQuery").click(function() {
					 doInit();
				});
				//按钮绑定事件（清空）
			    $("#btnClear").click(function() {
			        $("#queryForm").clearFormResult();
			        $("#SGJLDW").val('');
			    });
				//自动完成项目名称模糊查询
				showAutoComplete("QXMMC",controllername1+"?xmmcAutoCompleteToXmxdk","getXmmcQueryCondition"); 
			});
			//项目名称自动模糊查询参数
			function getXmmcQueryCondition(){
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				return data;
			}
			//页面默认参数
			function doInit(){
				doSearchQq();
			}
			function doSearchQq(){
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
				var condNd = "";
				if(g_nd!=null&&g_nd!=""){
					condNd = "&nd="+g_nd;
				}
				
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
				defaultJson.doQueryJsonList(controllername+"?queryList_tdsx"+condNd+condSxbh+condSxmc+condBllx,data,DT1,null,false);
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
				/* 用地预审 */
			 	//附件显示
			 	function doFj1(obj){
			 		var bblsx=obj.BBLSX;
			 		var nr=obj.YDYS;
			 		var sxlx='0010';
			 		var fk=doOpera(nr,bblsx,sxlx);
			 		return fk;
			 	}
			 	//时间显示
			 	function doDa1(obj){
			 		var bblsx=obj.BBLSX;
			 		var nr=obj.YDYSFK;
			 		var sxlx='0010';
			 		var fk=doOpera(nr,bblsx,sxlx);
			 		return fk;
			 	}
			 	/* 集体土地征地 */
			 	//附件显示
			 	function doFj2(obj){
			 		var bblsx=obj.BBLSX;
			 		var nr=obj.JTTDZD;
			 		var sxlx='0011';
			 		var fk=doOpera(nr,bblsx,sxlx);
			 		return fk;
			 	}
			 	//时间显示
			 	function doDa2(obj){
			 		var bblsx=obj.BBLSX;
			 		var nr=obj.ZDJDFK;
			 		var sxlx='0011';
			 		var fk=doOpera(nr,bblsx,sxlx);
			 		return fk;
			 	}
			 	/* 工地手续 */
			 	//附件显示
			 	function doFj3(obj){
			 		var bblsx=obj.BBLSX;
			 		var nr=obj.GDSX;
			 		var sxlx='0012';
			 		var fk=doOpera(nr,bblsx,sxlx);
			 		return fk;
			 	}
			 	//时间显示
			 	function doDa3(obj){
			 		var bblsx=obj.BBLSX;
			 		var nr=obj.GDSXFK;
			 		var sxlx='0012';
			 		var fk=doOpera(nr,bblsx,sxlx);
			 		return fk;
			 	}
			 	/* 土地登记 */
			 	//附件显示
			 	function doFj4(obj){
			 		var bblsx=obj.BBLSX;
			 		var nr=obj.TDDJ;
			 		var sxlx='0013';
			 		var fk=doOpera(nr,bblsx,sxlx);
			 		return fk;
			 	}
			 	//时间显示
			 	function doDa4(obj){
			 		var bblsx=obj.BBLSX;
			 		var nr=obj.TDDJFK;
			 		var sxlx='0013';
			 		var fk=doOpera(nr,bblsx,sxlx);
			 		return fk;
			 	}
			 	/* 土地使用证 */
			 	//附件显示
			 	function doFj5(obj){
			 		var bblsx=obj.BBLSX;
			 		var nr=obj.TDSYZ;
			 		var sxlx='0014';
			 		var fk=doOpera(nr,bblsx,sxlx);
			 		return fk;
			 	}
			 	//时间显示
			 	function doDa5(obj){
			 		var bblsx=obj.BBLSX;
			 		var nr=obj.TDSYFK;
			 		var sxlx='0014';
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
							  <th width="5%" class="right-border bottom-border text-right">项目名称</th>
					          <td class="right-border bottom-border" width="15%">
					          	<input class="span12" type="text" placeholder="" name="QXMMC"
									fieldname="JHSJ.XMMC" operation="like" id="QXMMC" autocomplete="off"
									tablePrefix="JHSJ"/>
							  </td>
							     <td class="text-left bottom-border text-right">
					           		<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
					           		<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
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
								<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
								<th fieldname="XMBH" id="E_XMHB" maxlength="15" hasLink="true" linkFunction="rowView" rowspan="2" colindex=2>&nbsp;项目编号&nbsp;</th>
					            <th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15">&nbsp;项目名称&nbsp;</th>
					            <th fieldname="XMBDDZ" rowspan="2" colindex=4 maxlength="15">&nbsp;项目地址&nbsp;</th>
					            <th colspan="10">&nbsp;进度&nbsp;</th>
					            <th rowspan="2" fieldname="BJSJ" colindex=15 tdalign="center">&nbsp;办结时间&nbsp;</th>
					            <th rowspan="2" fieldname="CZWT" colindex=16 maxlength="15">&nbsp;存在问题&nbsp;</th>
					            </tr>
					            <tr>
					            <th fieldname="YDYS" colindex=5 tdalign="center" CustomFunction="doFj1" noprint="true">&nbsp;用地预审&nbsp;</th>
					            <th fieldname="YDYSFK" colindex=6 tdalign="center" CustomFunction="doDa1">&nbsp;反馈日期&nbsp;</th>
					            <th fieldname="JTTDZD" colindex=7 tdalign="center" CustomFunction="doFj2" noprint="true">&nbsp;集体土地征地&nbsp;</th>
					            <th fieldname="ZDJDFK" colindex=8 tdalign="center" CustomFunction="doDa2">&nbsp;反馈日期&nbsp;</th>
					            <th fieldname="GDSX" colindex=9 tdalign="center" CustomFunction="doFj3" noprint="true">&nbsp;供地手续&nbsp;</th>
					            <th fieldname="GDSXFK" colindex=10 tdalign="center" CustomFunction="doDa3">&nbsp;反馈日期&nbsp;</th>
					            <th fieldname="TDDJ" colindex=11 tdalign="center" CustomFunction="doFj4" noprint="true">&nbsp;土地登记&nbsp;</th>
					            <th fieldname="TDDJFK" colindex=12 tdalign="center" CustomFunction="doDa4">&nbsp;反馈日期&nbsp;</th>
					            <th fieldname="TDSYZ" colindex=13 tdalign="center" CustomFunction="doFj5" noprint="true">&nbsp;土地使用证&nbsp;</th>
					            <th fieldname="TDSYFK" colindex=14 tdalign="center" CustomFunction="doDa5">&nbsp;反馈日期&nbsp;</th>
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