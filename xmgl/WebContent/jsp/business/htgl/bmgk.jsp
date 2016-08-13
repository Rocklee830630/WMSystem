<!DOCTYPE html>
<html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<head>
<title>招标部门概况</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/FixTable.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/bmgk/bmgk.css?version=20140415">
<style type="text/css">
body {font-size:14px;}
h2 {display:inline; line-height:2em;}
.stageTable .blankTD{
	width:3%;
	padding:0px;
}
.stageTable .contentTD{
	width:10%;
	padding:0px;
	height:100%;
}

.stageTable .blankTD3 {
	width:3%;
	padding:0px;
}

ul {
	list-style-type: none;
	margin-left: 0px;
}
body {font-size:14px;}
h2 {display:inline; line-height:2em;}
.table2 {
	border-left: #000 solid 1px;
	border-top: #000 solid 1px;
	margin:10px auto;
}
.marginBottom15px {margin-bottom:15px;}
.table2 tr td,.table2 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 1px;
	border-bottom: #000 solid 1px;
}
input[type='text'] {
	vertical-align: middle;
	height: 20px;
	line-height: 16px;
	padding: 2px;
}
.table1 {
}
.table1 tr td,.table1 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
}
.table3 {
}
.table3 tr td,.table3 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
}
.table4 tr td,.table4 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
}
.table4 tr {background-color:expression((this.sectionRowIndex%2==0)?"#E1F1F1":"#F0F0F0")}

</style>
<script type="text/javascript">			
var controllername = "${pageContext.request.contextPath }/bmgk/GcHtBmgkController.do";
var controllernamecommon = "${pageContext.request.contextPath}/bmgk/ZjBmgkController.do";
var p_chartNum=1;
$(function() {
	generateNd($("#ND"));
	setDefaultNd("ND");
	doInit();
    $("#ND").change(function() {
    	p_chartNum++;
    	doInit();
    });
	//打印按钮
	$("#printButton").click(function(){
		$(this).hide();
		window.print();
		$(this).show();
	});
});
function generateNd(ndObj){
	ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}
function doInit() {
	var nd=$("#ND").val();
	$(".ndText").text(nd);
	queryHtglTjgk(nd);
	queryHtglZbsg(nd);
	queryHtglZbjl(nd);
	queryZjbzTjgk(nd);
	queryZbzt(nd);
	queryZbglZbxqColumn2d(nd);
	queryHtglTable1(nd);
}

function queryHtglTjgk(nd) {
	var action1 = controllername + "?queryHtglTjgk&nd="+nd;
	$.ajax({
		url:		action1,
		success:	function(result) {
			insertTable(result,"HTGL_TJGK");
		}
	});
}

function queryHtglTable1(nd)
{
	var action1 = controllername + "?queryHtglBmlb&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result)
		{
			addTable2(result,"HtglListTableMore");
		}
	});
}
//根据结果放入表格
function insertTable(result,tableId) {
	var resultmsgobj = convertJson.string2json1(result);
	var resultobj = convertJson.string2json1(resultmsgobj.msg);
	var subresultmsgobj = resultobj.response.data[0];
	$("."+tableId+" span").each(function(i){
		var str = $(this).attr("bzfieldname");
		if(str!=''&&str!=undefined){
			if($(subresultmsgobj).attr(str+"_SV")!=undefined){
				var valueStr = $(subresultmsgobj).attr(str+"_SV");
				if($(this).attr("decimal")=="false"){
					//不需要小数的项，删掉小数点后的内容
					valueStr = valueStr.substring(0,valueStr.lastIndexOf("."));
				}else if($(this).attr("absl")=="true"){
					//是否要处理掉符号（取绝对值）
					if(valueStr.indexOf("-")==0){
						valueStr = valueStr.replace("-","");
					}
				}
		     	$(this).html(valueStr);
			}else{
				//$(this).html($(subresultmsgobj).attr(str));
				var flag = $(this).attr("flag");
				if($(subresultmsgobj).attr(str)!==''&&$(subresultmsgobj).attr(str)!=undefined){
					if($(subresultmsgobj).attr(str)!=0){
						switch(flag) {
							case 'hasLink_jgzxfz_dx' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**COMMON**jgzxfz_dx\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink_jgzxfz_zx' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**COMMON**jgzxfz_zx\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink__jgzxfz_xjdx' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**COMMON**jgzxfz_xjdx\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink__jgzxfz_xjzx' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**COMMON**jgzxfz_xjzx\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink__jgzxfz_xujdx' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\'CBK_DETAIL**COMMON**jgzxfz_xujdx\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLink__jgzxfz_xujzx' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**COMMON**jgzxfz_xujzx\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLinkTCJH' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetailJH(\'项目详细列表\',\'TCJh_DETAIL**ZTB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'hasLinkZBSJ' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetailZBSJ(\'项目详细列表\',\'LINK_ZBSJ_DETAIL**ZTB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'HTGL_DETAIL' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetailHtgl(\'项目详细列表\',\'HTGL_DETAIL**ZTB**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
							break;
							case 'hasLink' :
								$(this).html('<a href="javascript:void(0);" onclick="openDetail(\'项目详细列表\',\''+tableId+'_DETAIL**'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break; 
							default:
								$(this).html($(subresultmsgobj).attr(str));
								break;
						}
					}else{
						$(this).html($(subresultmsgobj).attr(str));
					}
				}else{
					$(this).html("");
				}
			}
		}
	});
}
function addTable(result,tableID)
{
	$("#"+tableID+" tr").each(function(i){
		if(i>0){
			$(this).empty()
		}
	});
	var resultmsgobj = convertJson.string2json1(result);
	var resultobj = convertJson.string2json1(resultmsgobj.msg);
	var len = resultobj.response.data.length;
	var showHtml='';
	var openWin;
	for(var i=0;i<len;i++){
		showHtml +="<tr>";
		$("#"+tableID+" tr th").each(function(j)
		{
			var subresultmsgobj = resultobj.response.data[i];
			var str = $(this).attr("bzfieldname");
			if(str=='BM') {
				showHtml+="<td class='mc' >"+$(subresultmsgobj).attr(str)+" </td>";
					
			}else{
			showHtml+="<td class='mc' style='text-align:center; '>"+$(subresultmsgobj).attr(str)+" </td>";
			} 
		});
		showHtml+="</tr>";
	}
	$("#"+tableID).append(showHtml);
	$($("#"+tableID+" tbody>tr:even")).addClass("tableTrEvenColor");
	$("#"+tableID).addClass("tableList");
	$($("#"+tableID+" thead tr:eq(0)")).addClass("tableTheadTr");
      
}
function addTable2(result,tableID)
{
	$("#"+tableID+" tr").each(function(i){
		if(i>1){
			$(this).empty()
		}
	});
	var resultmsgobj = convertJson.string2json1(result);
	var resultobj = convertJson.string2json1(resultmsgobj.msg);
	var len = resultobj.response.data.length;
	var showHtml='';
	var openWin;
	for(var i=0;i<len;i++){
		showHtml +="<tr>";
		$("#"+tableID+" tr").each(function(j)
		{
			var lie=0;
			if(j=='0'){
				$($("#"+tableID+" tr ")[j]).children().each(function(h){
					var subresultmsgobj = resultobj.response.data[i];
					var str = $(this).attr("bzfieldname");
					var flag=$(this).attr("colspan");
					if(flag>1){
						flag=Number(flag)+Number(lie);
						$($("#"+tableID+" tr ")[j+1]).children().each(function(k){
							if(lie<=k&&k<flag){
								lie++;
								var str = $(this).attr("bzfieldname");
								var lianjie=doLinkHT( $(subresultmsgobj) ,$(this).attr("bzfieldname"));
								//showHtml+="<td class='mc' style='text-align:center;'>"+lianjie+" </td>";
								if($(subresultmsgobj).attr(str)!=0 && k<2){
									showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailZbxq(\'项目详细列表\',\'ZBXQ_DETAIL**ZTB**'+str+'\',\''+$(subresultmsgobj).attr("ORDERNUM")+'\')">'+$(subresultmsgobj).attr(str)+'</a></td>';
								}else if($(subresultmsgobj).attr(str)!=0 && k>1){
									if(i==0){
										showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailHtgl(\'项目详细列表\',\'HTGL_DETAIL**ZTB**'+str+'\',\''+$(subresultmsgobj).attr("ORDERNUM")+'\',\'MULTI\')">'+$(subresultmsgobj).attr(str)+'</a></td>';
									}else if(i==1 || i==2){
										showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailHtgl(\'项目详细列表\',\'HTGL_DETAIL**ZTB**'+str+'\',\''+$(subresultmsgobj).attr("ORDERNUM")+'\',\'ZBSJ\')">'+$(subresultmsgobj).attr(str)+'</a></td>';
									}else if(i==3 || i==5 || i==6){
										showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailHtgl(\'项目详细列表\',\'HTGL_DETAIL**ZTB**'+str+'\',\''+$(subresultmsgobj).attr("ORDERNUM")+'\',\'HT\')">'+$(subresultmsgobj).attr(str)+'</a></td>';
									}else{
										showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailHtgl(\'项目详细列表\',\'HTGL_DETAIL**ZTB**'+str+'\',\''+$(subresultmsgobj).attr("ORDERNUM")+'\',\'COMMON\')">'+$(subresultmsgobj).attr(str)+'</a></td>';
									}
								}else{
									showHtml+="<td class='mc' style='text-align:center;'>"+$(subresultmsgobj).attr(str)+" </td>";
								}
							}
						});
					}else{
						if(str=='BM') {
							showHtml+="<td class='mc' >"+$(subresultmsgobj).attr(str)+" </td>";
						}else{
							//var lianjie=doLinkHT($(subresultmsgobj),$(this).attr("bzfieldname"));
							//showHtml+="<td class='mc' style='text-align:center;'>"+lianjie+" </td>";
							if(h>0 && h<4){
								showHtml+='<td class="mc" style="text-align:center;"><a href="javascript:void(0);" onclick="openDetailZbxq(\'项目详细列表\',\'ZBXQ_DETAIL**ZTB**'+str+'\',\''+$(subresultmsgobj).attr("ORDERNUM")+'\')">'+$(subresultmsgobj).attr(str)+'</a></td>';
							}else{
								showHtml+="<td class='mc' style='text-align:center;'>"+$(subresultmsgobj).attr(str)+" </td>";
							}
						}
					}
				}); 	
			}
		
		});
		showHtml+="</tr>";
	}
	$("#"+tableID).append(showHtml);
	$($("#"+tableID+" tbody>tr:even")).addClass("tableTrEvenColor");
	$("#"+tableID).addClass("tableList");
	$($("#"+tableID+" thead tr:eq(0)")).addClass("tableTheadTr");
}
//合同管理列表链接
function doLinkHT(obj,col){
	var flag=obj[0][col];
	var bm=obj[0].BM;
	if(flag==0){
		return '<div style="text-align:center">'+flag+'</div>';
	}else{
		return '<a href="javascript:void(0);" onclick="openDetailHTLB(\'项目详细列表\',\'HT_DETAIL**ZTB**HT\',\''+col+'\',\''+bm+'\')">'+flag+'</a>';			  
	}
}
function openDetailHTLB(title,name,tiaojian,bm){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/htList.jsp?nd="+nd+"&proKey="+name+"&tiaojian="+tiaojian+"&bm="+bm,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
//建管负责，新建续建等项目
function queryZjbzTjgk(nd){
	var action1 = controllernamecommon + "?queryZjbzTjgk&nd="+nd;
	$.ajax({
		url : action1,
		success: function(result){
			insertTable(result,"ZJBZ");
		}
	});
}

//施工监控
function queryHtglZbsg(nd) {
	var action1 = controllername + "?queryHtglZbsg&nd="+nd;
	$.ajax({
		url:		action1,
		success:	function(result) {
			insertTable(result,"ZBSG");
		}
	});
}
//监理监控
function queryHtglZbjl(nd) {
	var action1 = controllername + "?queryHtglZbjl&nd="+nd;
	$.ajax({
		url:		action1,
		success:	function(result) {
			insertTable(result,"ZBJL");			
		}
	});
}
function queryZbzt(nd)
{
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,zbzt);
	defaultJson.doQueryJsonList(controllername+"?queryZbzt&nd="+nd, data, zbzt, null, true);
	tableClass("zbzt");
}


function queryZbglZbxqColumn2d(nd){
	var action1 = controllername + "?queryZbglZbxqColumn2d&nd="+nd;
	$.ajax({
		url : action1,
		dataType:"json",
		async:false,
		success: function(result){
		 	 var myChart =  new FusionCharts("${pageContext.request.contextPath }/jsp/char/charts/MSStackedColumn2D.swf", "myChartID1", "100%", "300");  
		     myChart.setJSONData(result.msg);  
		     myChart.render("ZBGL_ZBXQ_Column2D"); 
		}
	});
}
function openDetailJH(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/tcjhList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
function openDetailZBSJ(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/zbsjList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
function openDetail(title,name){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/cbkList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
function tableClass(tableID){
	$("#"+tableID).removeClass();
	$($("#"+tableID+" tbody>tr:even")).addClass("tableTrEvenColor");
	$("#"+tableID).addClass("table3 tableList");
	$($("#"+tableID+" thead tr:eq(0)")).addClass("tableTheadTr");
	$($($("#"+tableID+" thead tr:eq(0)")).find("th")).addClass("tableTdAlign");
}
function showWtht(obj){
	return doLink(obj,'WTHT','5');
}
function showYtht(obj){
	return doLink(obj,'YTHT','6');
}
//链接方法
function doLink(obj,col,lie){
	var flag=obj[col];
	if(flag==0){
		return '<div style="text-align:center">'+flag+'</div>';
	}else{
		if(obj.ZBFS=='公开招标'){
			return '<a href="javascript:void(0);" onclick="openDetailXQLB(\'项目详细列表\',\'ZBXQ_DETAIL**ZTB**GKZB\', '+lie+')">'+flag+'</a>';			  
		}else{
			return '<a href="javascript:void(0);" onclick="openDetailXQLB(\'项目详细列表\',\'ZBXQ_DETAIL**ZTB**YJZB\','+lie+')">'+flag+'</a>';			  
		}
	}
}
function openDetailXQLB(title,name,tiaojian){
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/ztbxqList.jsp?nd="+nd+"&proKey="+name+"&tiaojian="+tiaojian,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
function openDetailZbxq(title,name,colNum){
	var nd = $("#ND").val();
	if(colNum!=undefined && colNum!="" && colNum!=null){
		name += "**"+colNum;
	}
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/ztbxqList.jsp?nd="+nd+"&proKey="+name+"&tiaojian=ZBHT","modal":"1"};
	$(window).manhuaDialog(xmsc);
}
function openDetailHtgl(title,name,colNum,flag){
	if(colNum!=undefined && colNum!="" && colNum!=null){
		name += "**"+colNum;
	}
	if(flag!=undefined && flag!="" && flag!=null){
		name +="**"+flag;
	}
	var nd = $("#ND").val();
	var  xmsc = {"title":title,"type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/gchtList.jsp?nd="+nd+"&proKey="+name,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
//招标需求柱形图
function zbxqcol(tiaojian,lie){
	var tiaojian1=tiaojian.replace(/%/g,"%25");
	var lie1=lie.replace(/%/g,"%25");
	var nd1=$("#ND").val();
	var proKey="ZBXQ_DETAIL**ZTB**ZBXQChartDiv";
	var condition = "nd="+nd1+"&tiaojian="+tiaojian1+"&proKey="+proKey+"&lie="+lie1;
	var  xmsc = {"title":"项目信息","type":"text","content":"${pageContext.request.contextPath }/jsp/business/bzjmlj/ztbxqList.jsp?"+condition,"modal":"1"};
	$(window).manhuaDialog(xmsc);
}
</script>
</head>
<body>
<app:dialogs />
	<div class="container-fluid" style="padding:0px">
	    <span class="pull-right">
	    	<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
	    </span>
    	<div class="" style="border: 0px;">
			<h4 class="bmjkTitleLine">
				招投标管理&nbsp;&nbsp;
				<select class="span2 year" style="width: 8%" id="ND" name="ND"  fieldname="ND" operation="=" noNullSelect ="true"></select> 
			</h4>
      		<div class="container-fluid" style="padding:0px 0px;">
        		<div class="row-fluid">
	          		<div class="span12 ZJBZ">
						<div class="bmjkTjgkBlock">
							<p>
							截至目前，<span class="ndText">2014</span>年建管中心负责<span flag="hasLink_jgzxfz_dx" bzfieldname="ZJBZ_TJGK_JGZX_DX"></span>项（<span flag="hasLink_jgzxfz_zx" bzfieldname="ZJBZ_TJGK_JGZX_ZX"></span>个子项）重点工程。
							其中，新建<span flag="hasLink__jgzxfz_xjdx" bzfieldname="ZJBZ_TJGK_XINJ_DX"></span>项（<span  flag="hasLink__jgzxfz_xjzx" bzfieldname="ZJBZ_TJGK_XINJ_ZX"></span>个子项），续建<span flag="hasLink__jgzxfz_xujdx" bzfieldname="ZJBZ_TJGK_XUJ_DX"></span>项（<span flag="hasLink__jgzxfz_xujzx" bzfieldname="ZJBZ_TJGK_XUJ_ZX"></span>个子项）。
							</p>
						</div>
	          		</div>
          			<div class="span12 ZBSG" style="margin-left:0px;min-width:960px;">
            			<table border="0" class="stageTable">
              				<tr>
				                <td rowspan="9" class="blankTD" style="border-right:5px solid #FFF;">
				                	<div style="color: #000;font-size: 22px;">施<br/><br/>工<br/><br/>招<br/><br/>标<br/></div>
				                </td>
				                <td class="line-H line-S blankTD" colspan=2>
				                	<div class="divTitle" style="width:80%;">年度计划</div>
									<div class="divContent" style="width:80%;">
										中心负责<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_TJGK_JGZX_ZX"></span>子项
									</div>
				                </td>
				                <td class="blankTD">
				                </td>
                				<td></td>
				                <td class="blankTD">
									<div class="containerDiv-upToRight">
								    	<div class="lineDiv">
								      		<div class="line" style="height:66px"></div>
								    	</div>
								    	<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		未提招标需求<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_WTZBXQ_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_WTZBXQ_BD"></span>标段
				                 	</div>
				                </td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
              				</tr>
              				<tr>
                				<td class="line-S blankTD">
                  					<div class="div-S" style="height:100%;"></div>
                				</td>
				                <td class="contentTD" rowspan="3">
				                  	<div class="block-blue">
				                  		建管中心<br/>招投标合同部负责<br>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_ZTBFZ_ZX">XX</span>子项
				                  	</div>
				                </td>
				                <td class="line-S blankTD">
								  	<div class="containerDiv-upToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-red">
				                  		暂缓招标<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_ZHZB_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_ZHZB_BD"></span>标段
				                  	</div>
				                </td>
				                <td class="line-S blankTD">
				                	<div class="div-S" style="height:50%;"></div>
								  	<div class="containerDiv-upToRight">
								    	<div class="lineDiv">
								      		<div class="line"></div>
								    	</div>
								    	<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		已提招标需求<br/>
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YTZBXQ_ZX"></span>子项
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YTZBXQ_BD"></span>标段
				                  	</div>
				                </td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
              				</tr>
              				<tr>
				                <td class="line-S blankTD">
								  	<div class="containerDiv-downToRight">
								    	<div class="lineDiv">
								      		<div class="line"></div>
								    	</div>
								   		 <div class="triangle-right"></div>
								  	</div>
				                  	<div class="div-S" style="height:100%;"></div>
				                </td>
				                <td class="line-H line-S blankTD">
				                  	<div class="div-H" style="width:50%;"></div>
				                  	<div class="div-S"></div>
				                </td>
                				<td></td>
				                <td class="line-S blankTD">
				                 	 <div class="div-S"></div>
				                </td>
								<td class="blankTD3">
								  	<div class="containerDiv-upToDown" style="height:20px;">
								    	<div class="lineDiv">
								      		<div class="line"></div>
								    	</div>
								    	<div class="triangle-up"></div>
								    	<div class="triangle-down"></div>
								  	</div>
								</td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
              				</tr>
              				<tr>
				                <td class="line-S blankTD">
				                	<div class="div-S" style="height:100%;"></div>
				                </td>
				                <td class="line-S blankTD">
								  	<div class="containerDiv-downToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		具备招标条件<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_JBZBTJ_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_JBZBTJ_BD"></span>标段
				                  	</div>
				                </td>
				                <td class="line-H line-S blankTD">
				                  	<div class="div-H" style="width:50%;"></div>
				                  	<div class="div-S" style="height:51%;"></div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		已报建<br/>
										<!-- <span bzfieldname="ZBGL_SGZB_YBJ_ZX"></span>子项 -->
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YBJ_ZX"></span>子项
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YBJ_BD"></span>标段
				                  	</div>
				                </td>
									<td class="blankTD3">
									  	<div class="containerDiv-upToRight">
									    	<div class="lineDiv">
									      		<div class="line"></div>
									    	</div>
									    	<div class="triangle-right"></div>
									  	</div>
									</td>
					                <td class="contentTD">
					                  	<div class="block-red">
					                  		未发招标公告<br/>
										<!-- 	<span bzfieldname=ZBGL_SGZB_GKZBWFZBGG_ZX></span>子项 -->
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBWFZBGG_ZX"></span>子项
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBWFZBGG_BD"></span>标段
					                  	</div>
					                </td>
									<td class="blankTD3">
									  	<div class="containerDiv-upToRight">
									    	<div class="lineDiv">
									      		<div class="line"></div>
									    	</div>
									    	<div class="triangle-right"></div>
									  	</div>
									</td>
					                <td class="contentTD">
					                  	<div class="block-red">
					                  		未开标<br/>
											<!-- <span bzfieldname="ZBGL_SGZB_GKZBWKB_ZX"></span>子项 -->
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBWKB_ZX"></span>子项
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBWKB_BD"></span>标段
					                  	</div>
					                </td>
					                <td></td>
					                <td></td>
									<td class="blankTD3">
									  	<div class="containerDiv-upToRight">
									    	<div class="lineDiv">
									      		<div class="line"></div>
									    	</div>
									    	<div class="triangle-right"></div>
									  	</div>
									</td>
					                <td class="contentTD">
					                  	<div class="block-red">
					                  		待签合同<br/>
											<!-- <span bzfieldname="ZBGL_SGZB_GKZBDQHT_ZX"></span>子项 -->
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBDQHT_ZX"></span>子项
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBDQHT_BD"></span>标段
					                  	</div>
					                </td>
              					</tr>
              					<tr>
					                <td class="line-S blankTD">
					                  	<div class="div-S" style="height:100%;"></div>
					                </td>
					                <td></td>
					                <td class="blankTD">
					                  	<div class="containerDiv-upToRight">
									    	<div class="lineDiv">
									      		<div class="line"></div>
									    	</div>
								  		</div>
					                </td>
					                <td class="line-H blankTD">
					                  	<div class="div-H" style="width:100%;"></div>
					                </td>
					                <td class="line-H blankTD">
					                  	<div class="div-H" style="width:100%;"></div>
					                </td>
					                <td class="line-H line-S blankTD" style="height:20px;">
					                  	<div class="div-H" style="width:53%;"></div>
					                  	<div class="div-S" style="height:50%;"></div>
					                </td>
					                <td class="line-S blankTD">
					                  	<div class="div-S"></div>
					                </td>
					                <td></td>
					                <td class="line-S blankTD">
					                  	<div class="div-S"></div>
					                </td>
					                <td></td>
					                <td></td>
					                <td></td>
					                <td class="line-S blankTD">
					                  	<div class="div-S"></div>
					                </td>
					                <td></td>
              					</tr>
              					<tr>
					                <td class="line-S blankTD">
					                  	<div class="div-S" style="height:100%;"></div>
					                </td>
					                <td></td>
					                <td class="line-S blankTD">
									  	<div class="containerDiv-downToRight">
									    	<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
					                    	<div class="div-S" style="top:89%;"></div>
									  	</div>
									</td>
					                <td class="contentTD">
					                  	<div class="block-blue">
					                  		公开招标<br/>
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZB_ZX"></span>子项
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZB_BD"></span>标段
					                  	</div>
					                </td>
					                <td class="line-S blankTD">
									  	<div class="containerDiv-leftToRight">
									    	<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
									  	</div>
					                </td>
					                <td class="contentTD">
					                  	<div class="divTitle">前置条件</div>
									  	<div class="divContent">
											已完成施工图<br/>
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YWSGT_ZX"></span>子项
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YWSGT_BD"></span>标段
									  	</div>
					                </td>
					                <td class="line-S blankTD">
					                  	<div class="div-S" style="height:50%;"></div>
									  	<div class="containerDiv-leftToRight">
									    	<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
									  	</div>
					                </td>
					                <td class="contentTD">
					                  	<div class="block-blue">
					                  		已发招标公告<br/>
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBYFZBGG_ZX"></span>子项
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBYFZBGG_BD"></span>标段
					                  	</div>
					                </td>
					                <td class="line-S blankTD">
					                  	<div class="div-S" style="height:50%;"></div>
									  	<div class="containerDiv-leftToRight">
									    	<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
									  	</div>
					                </td>
					                <td class="contentTD">
					                  	<div class="block-blue">
					                  		已开标<br/>
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBYKB_ZX"></span>子项
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBYKB_BD"></span>标段
					                  	</div>
					                </td>
					                <td class="line-S blankTD">
									 	 <div class="containerDiv-leftToRight">
									    	<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
									  	</div>
					                </td>
					                <td class="contentTD">
					                  	<div class="block-blue">
					                  		已发中标通知书<br/>
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBYFZBBTZS_ZX"></span>子项
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBYFZBBTZS_BD"></span>标段
					                  	</div>
					                </td>
					                <td class="line-S blankTD">
					                  	<div class="div-S" style="height:50%;"></div>
									  	<div class="containerDiv-leftToRight">
									    	<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
									  	</div>
					                </td>
					                <td class="contentTD">
					                  	<div class="block-blue">
					                  		已签合同<br/>
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBYQHT_ZX"></span>子项
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_GKZBYQHT_BD"></span>标段
					                  	</div>
					                </td>
								</tr>
				             	<tr>
				                	<td class="blankTD">
				                  		<div class="containerDiv-downToRight">
									   	 	<div class="lineDiv">
									      		<div class="line"></div>
									    	</div>
									    	<div class="triangle-right"></div>
									  	</div>
				                	</td>
				                	<td class="contentTD">
				                		<div class="block-blue">
					                  		其他单位负责<br>
					                  		<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_QTDWFZ_ZX">0</span>子项
					                  	</div>
				                	</td>
				                	<td class="line-S blankTD3">
				                  		<div class="div-S" style="height:100%"></div>
				               		 </td>
					                <td></td>
					                <td></td>
					                <td></td>
					                <td></td>
					                <td></td>
									<td class="blankTD3">
									  	<div class="containerDiv-upToRight">
									    	<div class="lineDiv">
									      		<div class="line"></div>
									    	</div>
									    	<div class="triangle-right"></div>
									  	</div>
									</td>
					                <td class="contentTD">
					                	<div class="block-red">
					                  		待签合同<br/>
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_DQHT_ZX"></span>子项
											<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_DQHT_BD"></span>标段
					                  	</div>
					                </td>
					                <td></td>
					                <td></td>
					                <td></td>
					                <td></td>
              					</tr>
             					<tr>
					                <td class="blankTD">
					                </td>
					                <td class="contentTD">
					                  	
					                </td>
					                <td class="line-S blankTD3">
					                  	<div class="div-S" style="height:100%"></div>
					                </td>
					                <td></td>
					                <td></td>
					                <td></td>
					                <td></td>
					                <td></td>
					                <td class="line-S blankTD3">
					                  	<div class="div-S" style="height:100%"></div>
					                </td>
					                <td></td>
					                <td></td>
					                <td></td>
					                <td></td>
					                <td></td>
								</tr>
					            <tr>
					                <td></td>
					                <td></td>
					                <td class="line-S blankTD">
									  	<div class="containerDiv-downToRight">
									    	<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
									  	</div>
					                </td>
					                <td class="contentTD">
					                  	<div class="block-blue">
					                  		随机抽取<br/>
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YJZB_ZX"></span>子项
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YJZB_BD"></span>标段
					                 	 </div>
					                </td>
					                <td class="line-S blankTD">
									  	<div class="containerDiv-leftToRight">
									    	<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
									  	</div>
					                </td>
					                <td class="contentTD">
					                  	<div class="block-blue">
					                  		已完成随机抽取<br/>
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YWYQZB_ZX"></span>子项
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YWYQZB_BD"></span>标段
					                  	</div>
					                </td>
					                <td class="line-S blankTD">
									  	<div class="containerDiv-leftToRight">
									    	<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
									  	</div>
					                </td>
					                <td class="contentTD">
					                  	<div class="block-blue">
					                  		已发中标通知书<br/>
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YFZBTZS_ZX"></span>子项
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YFZBTZS_BD"></span>标段
					                  	</div>
					                </td>
					                <td class="line-S blankTD">
					                  	<div class="div-S" style="height:50%;"></div>
									  	<div class="containerDiv-leftToRight">
									    	<div class="lineDiv">
												<div class="line"></div>
											</div>
											<div class="triangle-right"></div>
									  	</div>
					                </td>
					                <td class="contentTD">
					                  	<div class="block-blue">
					                  		已签合同<br/>
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YQHT_ZX"></span>子项
											<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_SGZB_YQHT_BD"></span>标段
					                  	</div>
					                </td>
					                <td></td>
					                <td></td>
					                <td></td>
					                <td></td>
              					</tr>
           				 </table>
					</div>
          			<div class="span12" style="margin-left:0px;border-bottom:1px solid RGB(135,142,141);height:2px;width:100%;"></div>                 
					<div class="span12 ZBJL" style="margin-left:0px;min-width:960px;">
          				<div style="height:20px;"></div>
           				<table border="0" class="stageTable">
             				<tr>
				                <td rowspan="9" class="blankTD" style="text-align: center;border-right:5px solid #FFF;">
				                  	<div style="color: #000;font-size: 22px">监<br/><br/>理<br/><br/>招<br/><br/>标<br/></div>
				                </td>
				                <td colspan=2 class="blankTD ZBSG">
				                  	<div class="divTitle" style="width:80%;">年度计划</div>
								  	<div class="divContent" style="width:80%;">
										中心负责<br/>
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_TJGK_JGZX_ZX"></span>子项
								  	</div>
				                </td>
				                <td class="blankTD">
				                </td>
				                <td></td>
				                <td class="blankTD">
								  	<div class="containerDiv-upToRight">
								    	<div class="lineDiv">
								      		<div class="line" style="height:66px"></div>
								    	</div>
								    	<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		未提招标需求<br/>
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_WTZBXQ_ZX"></span>子项
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_WTZBXQ_BD"></span>标段
				                  	</div>
				                </td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
							</tr>
             				<tr>
				                <td class="line-S blankTD">
				                  	<div class="div-S" style="height:100%;"></div>
				                </td>
				                <td class="contentTD" rowspan="3">
				                  	<div class="block-blue">
				                  		建管中心<br/>招投标合同部负责<br>
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_ZTBFZ_ZX"></span>子项
				                 	 </div>
				                </td>
				                <td class="line-S blankTD">
								  	<div class="containerDiv-upToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-red">
				                  		暂缓招标<br/>
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_ZHZB_ZX"></span>子项
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_ZHZB_BD"></span>标段
				                  	</div>
				                </td>
				                <td class="line-S blankTD">
				                  	<div class="div-S" style="height:50%;"></div>
								  	<div class="containerDiv-upToRight">
								    	<div class="lineDiv">
								      		<div class="line"></div>
								    	</div>
								    	<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		已提招标需求<br/>
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YTZBXQ_ZX"></span>子项
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YTZBXQ_BD"></span>标段
				                  	</div>
				                </td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
							</tr>
							<tr>
				                <td class="line-S blankTD">
									<div class="containerDiv-downToRight">
								    	<div class="lineDiv">
								      		<div class="line"></div>
								    	</div>
								    	<div class="triangle-right"></div>
								  	</div>
				                  	<div class="div-S" style="height:100%;"></div>
				                </td>
				                <td class="line-H line-S blankTD">
				                  	<div class="div-H" style="width:50%;"></div>
				                  	<div class="div-S"></div>
				                </td>
				                <td></td>
				                <td class="line-S blankTD">
				                  	<div class="div-S"></div>
				                </td>
								<td class="blankTD3">
								  	<div class="containerDiv-upToDown" style="height:20px;">
								    	<div class="lineDiv">
								      		<div class="line"></div>
								    	</div>
								    	<div class="triangle-up"></div>
								    	<div class="triangle-down"></div>
								  	</div>
								</td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				            </tr>
				            <tr>
				                <td class="line-S blankTD">
				                  	<div class="div-S" style="height:100%;"></div>
				                </td>
				                <td class="line-S blankTD">
								  	<div class="containerDiv-downToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		具备招标条件<br/>
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_JBZBTJ_ZX"></span>子项
										<span class="fieldValue-A"  flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_JBZBTJ_BD"></span>子项
				                  	</div>
				                </td>
				                <td class="line-H line-S blankTD">
				                  	<div class="div-H" style="width:50%;"></div>
				                  	<div class="div-S" style="height:51%;"></div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		已报建<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YBJ_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YBJ_BD"></span>标段
				                 	 </div>
				                </td>
								<td class="blankTD3">
								  	<div class="containerDiv-upToRight">
								   	 	<div class="lineDiv">
								     		<div class="line"></div>
								    	</div>
								   		<div class="triangle-right"></div>
								  	</div>
								</td>
				                <td class="contentTD">
				                  	<div class="block-red">
				                  		未发招标公告<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname=ZBGL_JLZB_GKZBWFZBGG_ZX></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBWFZBGG_BD"></span>标段
				                  	</div>
				                </td>
								<td class="blankTD3">
								  	<div class="containerDiv-upToRight">
								    	<div class="lineDiv">
								      		<div class="line"></div>
								    	</div>
								    	<div class="triangle-right"></div>
								  	</div>
								</td>
				                <td class="contentTD">
				                  	<div class="block-red">
				                  		未开标<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBWKB_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBWKB_BD"></span>标段
				                  	</div>
				                </td>
				                <td></td>
				                <td></td>
								<td class="blankTD3">
								  	<div class="containerDiv-upToRight">
								    	<div class="lineDiv">
								      		<div class="line"></div>
								    	</div>
								    	<div class="triangle-right"></div>
								  	</div>
								</td>
				                <td class="contentTD">
				                  	<div class="block-red">
				                  		待签合同<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBDQHT_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBDQHT_BD"></span>标段
				                  	</div>
				                </td>
             				</tr>
            				<tr>
				                <td class="line-S blankTD">
				                  	<div class="div-S" style="height:100%;"></div>
				                </td>
				                <td></td>
				                <td class="blankTD">
				                  	<div class="containerDiv-upToRight">
								    	<div class="lineDiv">
								      		<div class="line"></div>
								    	</div>
								  	</div>
				                </td>
				                <td class="line-H blankTD">
				                  	<div class="div-H" style="width:100%;"></div>
				                </td>
				                <td class="line-H blankTD">
				                  	<div class="div-H" style="width:100%;"></div>
				                </td>
				                <td class="line-H line-S blankTD" style="height:20px;">
				                  	<div class="div-H" style="width:53%;"></div>
				                  	<div class="div-S" style="height:50%;"></div>
				                </td>
				                <td class="line-S blankTD">
				                  	<div class="div-S"></div>
				                </td>
				                <td></td>
				                <td class="line-S blankTD">
				                  	<div class="div-S"></div>
				                </td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td class="line-S blankTD">
				                  	<div class="div-S"></div>
				                </td>
				                <td></td>
            				</tr>
							<tr>
				                <td class="line-S blankTD">
				                  	<div class="div-S" style="height:100%;"></div>
				                </td>
				                <td></td>
				                <td class="line-S blankTD">
								  	<div class="containerDiv-downToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
				                    	<div class="div-S" style="top:89%;"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		公开招标<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZB_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname=ZBGL_JLZB_GKZB_BD></span>标段
				                  	</div>
				                </td>
				                <td class="line-S blankTD">
								  	<div class="containerDiv-leftToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="divTitle">前置条件</div>
								  	<div class="divContent">
										已完成施工图<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YWSGT_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YWSGT_BD"></span>标段
								  	</div>
				                </td>
				                <td class="line-S blankTD">
				                  	<div class="div-S" style="height:50%;"></div>
							  		<div class="containerDiv-leftToRight">
							    		<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
							  		</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		已发招标公告<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBYFZBGG_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBYFZBGG_BD"></span>标段
				                 	 </div>
				                </td>
				                <td class="line-S blankTD">
				                  	<div class="div-S" style="height:50%;"></div>
								  	<div class="containerDiv-leftToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		已开标<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBYKB_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBYKB_BD"></span>标段
				                  	</div>
				                </td>
				                <td class="line-S blankTD">
								  	<div class="containerDiv-leftToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		已发中标通知书<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBYFZBBTZS_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBYFZBBTZS_BD"></span>标段
				                  	</div>
				                </td>
				                <td class="line-S blankTD">
				                  	<div class="div-S" style="height:50%;"></div>
							  		<div class="containerDiv-leftToRight">
							    		<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
							  		</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		已签合同<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBYQHT_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_GKZBYQHT_BD"></span>标段
				                 	 </div>
				                </td>
				            </tr>
				            <tr>
				                <td class="blankTD">
									<div class="containerDiv-downToRight">
								   		<div class="lineDiv">
								      		<div class="line"></div>
								    	</div>
								    	<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		其他单位负责<br>
					                  	<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_QTDWFZ_ZX"></span>子项
				                 	 </div>
				                </td>
				                <td class="line-S blankTD3">
				                  	<div class="div-S" style="height:100%"></div>
				                </td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
								<td class="blankTD3">
								  	<div class="containerDiv-upToRight">
								    	<div class="lineDiv">
								      		<div class="line"></div>
								    	</div>
								    	<div class="triangle-right"></div>
								  	</div>
								</td>
				                <td class="contentTD">
				                  	<div class="block-red">
				                  		待签合同<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_DQHT_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_DQHT_BD"></span>标段
				                  	</div>
				                </td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
							</tr>
							<tr>
				                <td class="blankTD">
				                </td>
				                <td>
				                </td>
				                <td class="line-S blankTD3">
				                  	<div class="div-S" style="height:100%"></div>
				                </td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td class="line-S blankTD3">
				                  	<div class="div-S" style="height:100%"></div>
				                </td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
             				</tr>
             				<tr>
				                <td></td>
				                <td></td>
				                <td class="line-S blankTD">
								  	<div class="containerDiv-downToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		随机抽取<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YJZB_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YJZB_BD"></span>标段
				                  	</div>
				                </td>
				                <td class="line-S blankTD">
								  	<div class="containerDiv-leftToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		已完成随机抽取<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YWYQZB_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YWYQZB_BD"></span>标段
				                  	</div>
				                </td>
				                <td class="line-S blankTD">
								  	<div class="containerDiv-leftToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		已发中标通知书<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YFZBTZS_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YFZBTZS_BD"></span>标段
				                  	</div>
				                </td>
				                <td class="line-S blankTD">
				                  	<div class="div-S" style="height:50%;"></div>
								  	<div class="containerDiv-leftToRight">
								    	<div class="lineDiv">
											<div class="line"></div>
										</div>
										<div class="triangle-right"></div>
								  	</div>
				                </td>
				                <td class="contentTD">
				                  	<div class="block-blue">
				                  		已签合同<br/>
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YQHT_ZX"></span>子项
										<span class="fieldValue-A" flag="hasLinkZBSJ" bzfieldname="ZBGL_JLZB_YQHT_BD"></span>标段
				                  	</div>
				                </td>
				                <td></td>
				                <td></td>
				                <td></td>
				                <td></td>
             				</tr>
          			 	</table>
          			</div>
          			<div class="span12 ZBJL" style="margin-left:0px;min-width:960px;">        			
          				<div style="height:20px;"></div>
           				<table border="0" class="stageTable">
             				<tr>
				                <td rowspan="5" style="text-align: center; width: 37px;" class="blankTD">
				                  	<div style="color: #000;font-size: 22px;">招<br/><br/>标<br/><br/>需<br/><br/>求<br/></div>
				                </td>
				            </tr>
				            <tr>
				            	<td>
				            		<div id="ZBGL_ZBXQ_Column2D"></div>
				            	</td>
				            </tr>
			                <tr>
				                <td>
									<div style="margin-left:20px;">		
									<div class="row-fluid">
										<div class="B-small-from-table-autoConcise">
						        			<div class="overFlowX"> 		
												<div style="height:20px;"></div>
									    			<table class="table-hover table-activeTd B-table" width="50%" border="0" 
									    				cellpadding="0" id="zbzt" cellspacing="0" nopage="true" pageNum="1000">
									    				<thead>
												            <tr  >
												                <th fieldname="ZBFS">招标方式</th>
												                <th fieldname="WTHT" tdalign="center" Customfunction="showWtht"><div style="text-align: center;">履行程序中</div></th>
												                <th fieldname="YTHT" tdalign="center" Customfunction="showYtht"><div style="text-align: center;">已完成</div></th>
												            </tr>
											            </thead>
											            <tbody>
											            </tbody>
											        </table>
												</div>
											</div>
										</div>
									</div>
								</td>
							</tr>
						</table>
					</div>
		    	</div>
      		</div>
    	</div> 	
	</div> 
	<div class="" style="border: 0px;">
	<div style="height:20px;"></div>
  		<h4 class="bmjkTitleLine">
			合同管理
  		</h4>
     		<div class="container-fluid" style="padding:0px 0px;">
       		<div class="row-fluid">
	          	<div class="span12 HTGL_TJGK">
	            	<div class="bmjkTjgkBlock">
	              		<p>
	              			截至目前，<span class="ndText"></span>年需签订合同<span flag="HTGL_DETAIL" bzfieldname="HTGL_TJGK_XQDHT" title="合同状态为已审批、审核中、履行中和已结算的合同">XX</span>个。
	              			其中，已签订合同<span flag="HTGL_DETAIL" bzfieldname="HTGL_TJGK_YQDHT" title="合同状态为履行中和已结算的合同">XX</span>个，
	              			待签订合同<span flag="HTGL_DETAIL" bzfieldname="HTGL_TJGK_DQDHT" title="合同状态为已审批和审核中的合同">XX</span>个。
	              		</p>
	            	</div>
	          	</div>
	        </div>
       		<div class="row-fluid">
	          	<div class="span8">
					<div class="B-small-from-table-autoConcise">
						<div class="overFlowX" style="height:100%; ">
								<table width="100%"  style="border-bottom: #ccc solid 1px;" align="center" cellpadding="0"
									cellspacing="0" class="">
									<tr>
										<td width="100%">
											<table width="100%" border="0" cellpadding="0" id="HtglListTableMore"
												cellspacing="0" class="table3">
												<thead>
												<tr
													style="border-bottom: #fff solid 1px; border-top: #ccc solid 1px;color: #fff;background:#36648B">
													<th bzfieldname="BM" rowspan="2">
														<div style="text-align: center;" >
															部门
														</div>
													</th>
													<th  bzfieldname ="HTGL_BMLB_ZBXQ"  rowspan="2">
														<div  style="text-align: center;">
															招标需求
														</div>
													</th>
													<th  bzfieldname ="KONG" colspan="2">
														<div  style="text-align: center;">
															 招标状态 
														</div>
													</th>
													<th  bzfieldname ="KONG" colspan="3">
														<div  style="text-align: center;">
															合同签订情况 
														</div>
													</th>
													
												</tr>
												<tr style="border-bottom: #FFF solid 1px; border-top: #ccc solid 1px;color:#fff;background:#36648B">
												<th bzfieldname="HTGL_BMLB_LXCXZ"  >
														<div style="text-align: center;">
															履行程序中
														</div>
													</th>
													<th bzfieldname="HTGL_BMLB_YWC"  >
														<div style="text-align: center;">
															已完成
														</div>
													</th>
													<th bzfieldname="HTGL_BMLB_YINGQDHT"  >
														<div style="text-align: center;">
															应签订合同（个）
														</div>
													</th>
													<th bzfieldname="HTGL_BMLB_YIQDHT"   >
														<div style="text-align: center;">
															已签订（个）
														</div>
													</th>
													<th bzfieldname="HTGL_BMLB_DQDHT"  >
														<div style="text-align: center;">
															待签订（个）
														</div>
													</th>
												</tr>
												</thead>
											</table>
										</td>
									</tr>
								</table>
							</div>
					</div>
	         	 </div>        
		        
       		</div>
     	</div>
	</div>
	<div>
		<form method="post" id="queryForm"></form>
		<FORM name="frmPost" method="post" style="display: none" target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML">
			<input type="hidden" name="txtXML" id="txtXML">
			<input type="hidden" name="ywid" id="ywid">
			<input type="hidden" name="txtFilter" order="asc" fieldname="" id="txtFilter">
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="queryResult" id = "queryResult">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>