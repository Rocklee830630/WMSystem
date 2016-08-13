<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="java.sql.Connection"%>

<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<style type="text/css">
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
.table3 tr th {/* border-top: #000 solid 1px; border-bottom: #000 solid 1px; */}
</style>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/qqsx/bzgkController.do";
	$(function(){
		setDefaultNd();
		doInit();
		$("#printButton").click(function(){
			window.print();
		});
	});
	function doInit(){
		var nd = $("#ND").val();
		g_bAlertWhenNoResult = true;
		queryTongJiGaiKuang();
		queryShiJian();
		queryLx(nd);
		queryGh(nd);
		querySg(nd);
		queryTd(nd);
		//var data = combineQuery.getQueryCombineData(queryForm,frmPost,lxkyjz);
		//defaultJson.doQueryJsonList(controllername+"?queryPqInfo",data,lxkyjz);
	}
	function doSearch(m,n){
		var actionUrl = controllername + "?"+m;
		$.ajax({
			url : actionUrl,
			success: function(result){
				var resultmsgobj = convertJson.string2json1(result);
				var resultobj = convertJson.string2json1(resultmsgobj.msg);
				var len = resultobj.response.data.length;
				var showHtml='';
			}
		});
	}
//查询表单
function queryTongJiGaiKuang()
{

	var action1 = controllername + "?queryCount&nd="+$("#ND").val()+"";
	$.ajax({
		url : action1,
		//contentType:'application/json;charset=UTF-8',	    
		success: function(result)
		{
		 insertTable(result);
		}
	});
}
//查询时间
function queryShiJian()
{
	var action1 = controllername + "?queryDate&nd="+$("#ND").val();
	$.ajax({
		url : action1,
		//contentType:'application/json;charset=UTF-8',	    
		success: function(result)
		{
			 insertTable(result);
		}
	});
}
	//根据结果放入表格
	function insertTable(result)
	{
		var resultmsgobj = convertJson.string2json1(result);
		var resultobj = convertJson.string2json1(resultmsgobj.msg);
		var subresultmsgobj = resultobj.response.data[0];
	   $("span").each(function(i){
			var str = $(this).attr("bzfieldname");
			if(str!=''&&str!=undefined){
				if((str=="SG_DATE" || str=="TD_DATE"||str=="GH_DATE"||str=="LX_DATE")&&$(subresultmsgobj).attr(str)==""){
			    	$(this).html("<font style='width:80px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>");
				}else{
					var flag = $(this).attr("flag");
					if($(subresultmsgobj).attr(str)!==''&&$(subresultmsgobj).attr(str)!=undefined)
					if($(subresultmsgobj).attr(str)!=0){
						switch(flag){
							case 'bd':
								$(this).html('<a href="javascript:void(0);" onclick="_blankXmxxBdxx(\'070001\',\'BMJK_QQ_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							case 'xm':
								$(this).html('<a href="javascript:void(0);" onclick="_blankXmxx(\'070001\',\'BMJK_QQ_'+str+'\')">'+$(subresultmsgobj).attr(str)+'</a>');
								break;
							default:
								$(this).html($(subresultmsgobj).attr(str));
								break;
						}
					}else{
						$(this).html($(subresultmsgobj).attr(str));
					}
				}
			}
		});
	}
	//根据年度查询
	function queryND()
	{
		//alert($("#ND").val());
		doInit();
	}
	//
	function queryLx(nd)
	{
		var action1 = controllername + "?queryLx&nd="+nd;
		$.ajax({
			url : action1,
			//contentType:'application/json;charset=UTF-8',	    
			success: function(result)
			{
				addTable(result,"lxtable");
			}
		});
	}
	function queryGh(nd)
	{
		var action1 = controllername + "?queryGh&nd="+nd;
		$.ajax({
			url : action1,
			//contentType:'application/json;charset=UTF-8',	    
			success: function(result)
			{
				addTable(result,"ghTable");
			}
		});
	}
	function queryTd(nd)
	{
		var action1 = controllername + "?queryTd&nd="+nd;
		$.ajax({
			url : action1,
			//contentType:'application/json;charset=UTF-8',	    
			success: function(result)
			{
				addTable(result,"tdTable");
			}
		});
	}
	function querySg(nd)
	{
		var action1 = controllername + "?querySg&nd="+nd;
		$.ajax({
			url : action1,
			//contentType:'application/json;charset=UTF-8',	    
			success: function(result)
			{
				addTable(result,"sgTable");
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
		
		for(var i=0;i<len;i++){
			showHtml +="<tr>";
			$("#"+tableID+" tr th").each(function(j)
			{
				var subresultmsgobj = resultobj.response.data[i];
				var str = $(this).attr("bzfieldname");
				/* showHtml+="<td>"+$(subresultmsgobj).attr(str)+" </td>";	 */
					if(str=='LABEL')
							{
								showHtml+="<td>"+$(subresultmsgobj).attr(str)+" </td>";
							}
						else{
								showHtml+="<td style=\"text-align:center;\">"+$(subresultmsgobj).attr(str)+" </td>";	
							}
			});
			showHtml+="</tr>";
		}
		$("#"+tableID).append(showHtml);
	}
	

	// 计划标段数
	function _blankXmxxBdxx(ywlx, bmjkLx) {
		var nd = $("#ND").val();
		xmxx_bdxxView(ywlx, nd, bmjkLx);
	}
	
	// 项目
	function _blankXmxx(ywlx, bmjkLx) {
		var nd = $("#ND").val();
		xmxxView(ywlx, nd, bmjkLx);
	}
</script>
</head>
<body>
<div class="container-fluid">
           <span class="pull-right">
    	<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
    </span>
     <div class="B-small-from-table-auto" style="border:0px;">
				<h4 style="background: none; color: #333; border-bottom: #ccc solid 1px;padding-left:10px;">统计概况
						<select
							class="span2 year" style="width: 8%" id="ND" onchange="queryND()"
							name="ND" fieldname="ND" operation="=" defaultMemo="全部" kind="dic" 
							src="T#GC_JH_SJ: distinct ND as NDCODE:ND:SFYX='1' ORDER BY NDCODE asc">
						</select>
				</h4>
				<div class="container-fluid">
         <div class="row-fluid">
                <div class="span12">
               <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="25%">
				        <table width="100%"  id="TJGK" border="0" cellspacing="0" class="table1" cellpadding="0">
				            <tr>
				                <td colspan="2">计划项目/标段数：<span flag="xm" bzfieldname="XM_MAX" ></span>/<span flag="bd" bzfieldname="BD_MAX" ></span></td>
				                <td>手续办结项目/标段数：<span flag="xm" bzfieldname="XM_BJ"></span>[<span bzfieldname="XM_BJ_CENT_SV"></span>%]/<span flag="bd" bzfieldname="BD_BJ"></span>[<span bzfieldname="BD_BJ_CENT_SV"></span>%]</td>
				                <td>已反馈项目/标段数：<span flag="xm" bzfieldname="XM_FK"></span>[<span bzfieldname="XM_FK_CENT_SV"></span>%]/<span flag="bd" bzfieldname="BD_FK"></span>[<span bzfieldname="BD_FK_CENT_SV"></span>%]</td>
				                <td>严重拖期项目/标段数：<span flag="xm" bzfieldname="XM_TQ"></span>[<span bzfieldname="XM_TQ_CENT_SV"></span>%]/<span flag="bd" bzfieldname="BD_TQ"></span>[<span bzfieldname="BD_TQ_CENT_SV"></span>%]</td>
				            </tr>
				            <tr>
				           		<td width="5%">其中：</td>
				                <td width="17%">立项可研完成数：<span flag="xm" bzfieldname="LX" ></span>[<span bzfieldname="LX_CENT_SV" ></span>%]</td>
				                <td width="26%">土地审批完成数：<span flag="xm" bzfieldname="TD" ></span>[<span bzfieldname="TD_CENT_SV" ></span>%]</td>
				                <td width="26%">规划审批完成数：<span flag="xm" bzfieldname="GH" ></span>[<span bzfieldname="GH_CENT_SV" ></span>%]</td>
				                <td width="26%">施工许可完成数：<span flag="bd" bzfieldname="SG" ></span>[<span bzfieldname="SG_CENT_SV" ></span>%]</td>
				            </tr>
		                   <tr>
		                   		<td>&nbsp;</td>
				                <td>材料交接完成数：<span flag="xm" bzfieldname="CL" ></span>[<span bzfieldname="CL_CENT_SV" ></span>%]</td>
				                <td colspan="3"></td>
				            </tr>
				        </table></td>
				    </tr>
				</table>
                    <p>&nbsp;</p>
                </div>
              </div>
        </div>
    </div>
    <div class="B-small-from-table-auto" style="border:0px;">
    <h4 style="background:none; color:#333; border-bottom:#ccc solid 1px;padding-left:10px;">前期手续详情跟踪</h4>
        <div class="container-fluid">
            <div class="row-fluid">
              <div class="span6">
              <h4  align="center" style="background:none; color:#333;width: 560px;">立项可2222222222222研进展整体状况
              <font  size="2px" style="font-weight: normal;">最新反馈时间 :
              <span bzfieldname="LX_DATE" ></span></font></h4>
		       <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="100%"><table width="100%" border="0" cellpadding="0" id="lxtable" cellspacing="0" class="table3">
				            <tr style="border-bottom: #ccc solid 1px;border-top: #ccc solid 1px;">
				                <th bzfieldname="LABEL">&nbsp;</th>
				                <th  bzfieldname="XMJYSPF"><div style="text-align: center;">项目建议书</div></th>
				                <th   bzfieldname="HPPF"><div style="text-align: center;">环评批复</div></th>
				                <th  bzfieldname="TDYJH"><div style="text-align: center;">土地意见函</div></th>
				                <th  bzfieldname="GDZCTZXM"><div style="text-align: center;">固定资产投资项目节能审查</div></th>
				                <th  bzfieldname="KYPF"><div style="text-align: center;">可研批复</div></th>
				            </tr>
				        </table></td>
				    </tr>
				</table>
		      </div>
		      <div class="span6">
              <h4 align="center" style="background:none; color:#333;width: 560px;">土地审批进展整体状况
              <font size="2px" style="font-weight: normal;">最新反馈时间 :
              <span bzfieldname="TD_DATE" >
              </span></font></h4>
		       <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="25%"><table width="100%" border="0" cellpadding="0" id="tdTable" cellspacing="0" class="table3">
				           <tr  style="border-bottom: #ccc solid 1px;border-top: #ccc solid 1px;">
				                <th bzfieldname="LABEL">&nbsp;</th>
				                <th  bzfieldname="YDYS"><div style="text-align: center;">用地预审</div></th>
				                <th   bzfieldname="JTTDZD"><div style="text-align: center;">集体地证地</div></th>
				                <th  bzfieldname="GDSX"><div style="text-align: center;">供地手续</div></th>
				                <th  bzfieldname="TDDJ"><div style="text-align: center;">土地登记</div></th>
				                <th  bzfieldname="TDSYZ"><div style="text-align: center;">土地使用证</div></th>
				            </tr>
				        </table></td>
				    </tr>
				</table>
		      </div>
		       </div>
            </div>
		      <div class="container-fluid">
            <div class="row-fluid">
		     <div class="span6">
		     <div style="background:none; color:#333;width: 100%;border-top:#ccc solid 1px;"></div>
		     <p style="height: 20px"></p>
              <h4 align="center" style="background:none; color:#333;width: 560px;">规划许可进展整体状况
              <font size="2px" style="font-weight: normal;">最新反馈时间 :
              <span bzfieldname="GH_DATE" >
              </span>
              </font>
              </h4>
		       <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="100%"><table width="100%" border="0" cellpadding="0" id="ghTable" cellspacing="0" class="table3">
				            <tr  style="border-bottom: #ccc solid 1px;border-top: #ccc solid 1px;">
				                <th bzfieldname="LABEL">&nbsp;</th>
				                <th  bzfieldname="JSXMXZYJS"><div style="text-align: center;">选址意见书</div></th>
				                <th   bzfieldname="JSYDGHXKZ"><div style="text-align: center;">用地规划许可证</div></th>
				                <th  bzfieldname="JSGCGHXKZ"><div style="text-align: center;">工程规划许可证</div></th>
				            </tr>
				        </table>
				          <div style="background:none; color:#333;width: 100%;border-top:#ccc solid 1px;"></div>
				        </td>
				    </tr>
				</table>
		      </div>
		      <div class="span6">
		      <div style="background:none; color:#333;width: 100%;border-top:#ccc solid 1px;"></div>
		        <p style="height: 20px"></p>
              <h4 align="center"  style="background:none; color:#333;width: 560px;">施工许可进展整体状况
              <font size="2px" style="font-weight: normal;">最新反馈时间 :
              <span bzfieldname="SG_DATE" >
              </span>
              </font>
              </h4>
		       <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="">
				    <tr>
				        <td width="25%"><table width="100%" border="0" cellpadding="0" id="sgTable" cellspacing="0" class="table3">
				           <tr  style="border-bottom: #ccc solid 1px;border-top: #ccc solid 1px;">
				                <th bzfieldname="LABEL">&nbsp;</th>
				                <th  bzfieldname="BJ"><div style="text-align: center;">报建</div></th>
				                <th   bzfieldname="ZLJD"><div style="text-align: center;">质量监督</div></th>
				                <th  bzfieldname="STQ"><div style="text-align: center;">双拖欠</div></th>
				                <th  bzfieldname="ZFJC"><div style="text-align: center;">执行监察</div></th>
				                <th  bzfieldname="SGXK"><div style="text-align: center;">施工许可</div></th>
				                <th  bzfieldname="ZJGL"><div style="text-align: center;">造价管理</div></th>
				                <th  bzfieldname="QT"><div style="text-align: center;">其它</div></th>
				            </tr>
				        </table></td>
				    </tr>
				</table>
				  <div style="background:none; color:#333;width: 100%;border-top:#ccc solid 1px;"></div>
		      </div>
		      </div>
        </div>
    </div>
</div>
</body>
</html>