<!DOCTYPE html>
<html>
	<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
		<app:base />
		<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
		<script type="text/javascript" charset="utf-8">
   var controllername= "${pageContext.request.contextPath }/shenJiGuanliController.do";
   var controllername1= "${pageContext.request.contextPath }/jieSuanGuanliController.do";
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(3)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
	$(function() {
		 getNd();
		 query1();
		var chaxun = $("#chaxun");
		chaxun.click(function(){
			query1();
           });
	 	//清空查询条件
	    var btn_clearQuery = $("#query_clear");
	    btn_clearQuery.click(function() 
	      {
	        $("#queryForm").clearFormResult();
	        getNd();
	      });
		//纳入计划
		var nrjh_btn = $("#nrjh_btn");
		nrjh_btn.click(function(){
 		     $(window).manhuaDialog({"title":"审计管理>纳入项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/sjgl/nrjh.jsp","modal":"1"});
           });
		//审计维护
		var sjwh_btn = $("#sjwh_btn");
		sjwh_btn.click(function(){
 		     $(window).manhuaDialog({"title":"审计管理>审定信息维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/sjgl/sjwh.jsp","modal":"1"});
           }); 
		//计划维护
		var nrjh_btn = $("#jhwh_btn");
		nrjh_btn.click(function(){
			var sjnd=$("#SJND").val();
			if(""!=sjnd){
				 $(window).manhuaDialog({"title":"审计管理>计划维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/sjgl/jhxg.jsp","modal":"4"});
			}else{
				xAlert("提示信息",'请选择审计年度','3');
			}
 		     }); 
		//移除计划
		var ycjh=$("#ycjh_btn");
		ycjh.click(function()
		{
			
			var index =	$("#DT1").getSelectedRowIndex();
			if(index==-1){
				requireSelectedOneRow();
				return;
			}else{
				xConfirm("提示信息","是否确认移除！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){ 
					$.ajax( {
							     url : controllername+"?updateJSById&jsid="+$("#GC_ZJB_JSB_ID").val(),//此处定义后台controller类和方法
						         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
						         success : function(result) {
						         query();
						         },
						         error : function(result) {//返回失败操作
						           defaultJson.clearTxtXML();
						          }			
							});
				});
			}
		});
		//提交到合同 
		var tjht=$("#tjht_btn");
		tjht.click(function()
		{
			
			var index =	$("#DT1").getSelectedRowIndex();
		 	var data = Form2Json.formToJSON(htForm);
			var data1 = defaultJson.packSaveJson(data);
			if(index==-1){
				requireSelectedOneRow();
				return;
			}else{
				xConfirm("提示信息","是否确认提交！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){ 
					defaultJson.doUpdateJson(controllername1+"?updateHeTongZT",data1,DT1,null);
					$("#tjht_btn").attr("disabled","true");
				});
			}
		});
	    //导出
	    $("#btnExpExcel").click(function() 
	       {
	    	if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
	    	     // printTabList("DT1");
	    		printTabList("DT1","jsgl.xls","XMBH,XMMC,BDMC,ND,SGDW,JSZT,HTBH,HTMC,HTJE,TBR,TBRQ,TBJE,WTZXGS,YZSDRQ,YZSDJE,CSSDRQ,CSSDJE,CSBGBH,SJSDRQ,SJSDJE,SJBGBH,JSQK","3,0"); 

	    	  }
	   	});
	});
	 //标段名称
  function doBdmc(obj){
	  var bd_name=obj.BDMC;
	  if(bd_name==null||bd_name==""){
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
  function query(){
	  getNd();
	  var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
	  defaultJson.doQueryJsonList(controllername+"?querysjList",data,DT1,null,true);
  }
  function query1(){
	  var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
	  defaultJson.doQueryJsonList(controllername+"?querysjList",data,DT1,null,true);
  }
	//默认年度
	function getNd(){
			setDefaultNd("SJND");
	}
	//获取行数据json串
	function tr_click(obj,tabListid)
	{
		var jsbid=obj.GC_ZJB_JSB_ID;
		var sjsdje=obj.SJSDJE;
		var zt=obj.ZT;
		$("#GC_ZJB_JSB_ID").val(jsbid);
		if(zt!="1"&&sjsdje!=""){
			$("#ycjh_btn").removeAttr("disabled");
			$("#tjht_btn").removeAttr("disabled");
		}else{
			$("#tjht_btn").attr("disabled","true");
			$("#ycjh_btn").attr("disabled","true");
		}
	}
	//详细信息
	 function rowView(index){
	     var obj = $("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
	     var XMID = convertJson.string2json1(obj).XMID;
	     $(window).manhuaDialog(xmscUrl(XMID));//调用公共方法,根据项目编号查询
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
						审计管理
						<span class="pull-right"> 
							<button id="nrjh_btn" class="btn" type="button">
								纳入项目
							</button>
							<button id="jhwh_btn" class="btn" type="button">
								计划维护
							</button>
							<button id="sjwh_btn" class="btn" type="button">
								审定信息维护
							</button>
							<button id="tjht_btn" class="btn" type="button">
								提交到合同
							</button>
							<button id="ycjh_btn" class="btn" type="button">
								移出项目
							</button>
							<button id="btnExpExcel" class="btn" type="button">
								导出
							</button> 
						</span>
					</h4>
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
								<th width="5%" class="right-border bottom-border text-right">
									审计年度
								</th>
								<td width="8%" class="right-border bottom-border">
								    <select class="span12 year" id="SJND" name = "SJND" fieldname="SJND"  defaultMemo="全部" operation="="  kind="dic" src="T#GC_ZJB_JSB:SJND :SJND SJNDA:SFYX='1' AND SJND IS NOT NULL group by SJND ORDER BY SJND ">
	              					</select> 
								</td>
								<td class="text-left bottom-border text-right">
									<button id="chaxun" class="btn btn-link" type="button">
										<i class="icon-search"></i>查询
									</button>
									<button id="query_clear" class="btn btn-link" type="button">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
						</table>
					</form>
					<div style="height: 5px;">
					</div>
					<div class="overFlowX">
						<table width="100%" class="table-hover table-activeTd B-table"
							id="DT1" type="single" printFileName="审计管理">
							<thead>
								<tr>
									<th name="XH" id="_XH" rowspan="2" colindex=1>
										&nbsp;#&nbsp;
									</th>
									<th fieldname="XMBH" rowspan="2" rowMerge="true" colindex=2
										hasLink="true" linkFunction="rowView">
										&nbsp;项目编号&nbsp;
									</th>
									<th fieldname="XMMC" rowspan="2" rowMerge="true" colindex=3
										maxlength="15">
										&nbsp;项目名称&nbsp;
									</th>
									<th fieldname="BDBH" rowspan="2" colindex=4 maxlength="15"
										Customfunction="doBdBH">
										&nbsp;标段编号&nbsp;
									</th>
									<th fieldname="BDMC" rowspan="2" colindex=5 maxlength="15"
										Customfunction="doBdmc">
										&nbsp;标段名称&nbsp;
									</th>
									<th colspan="3">
										&nbsp;审计审定&nbsp;
									</th>
									<th fieldname="JSZT" rowspan="2" colindex=9>
										&nbsp;结算状态&nbsp;
									</th>
									<th fieldname="ND" rowspan="2" colindex=10 tdalign="center">
										&nbsp;施工年度&nbsp;
									</th>
									<th fieldname="SGDW" rowspan="2" maxlength="15" colindex=11>
										&nbsp;施工单位&nbsp;
									</th>
									<!-- <th fieldname="JLDW" rowspan="2" maxlength="15" colindex=6>&nbsp;监理单位&nbsp;</th> -->
									<th colspan="3">
										&nbsp;合同&nbsp;
									</th>
									<th colspan="3">
										&nbsp;上报值&nbsp;
									</th>
									<th colspan="3">
										&nbsp;业主审定&nbsp;
									</th>
									<th colspan="3">
										&nbsp;财审审定&nbsp;
									</th>
									<th colspan="4">
										&nbsp;差额值&nbsp;
									</th>
									<th fieldname="JSQK" rowspan="2" maxlength="15" colindex=28>
										&nbsp;结算情况&nbsp;
									</th>
								</tr>
								<tr>
									<th fieldname="SJSDRQ" colindex=6 tdalign="center">
										&nbsp;日期&nbsp;
									</th>
									<th fieldname="SJSDJE" colindex=7 tdalign="right"
										maxlength="15">
										金额(元)
										<br>
										E
									</th>
									<th fieldname="SJBGBH" colindex=8 maxlength="15">
										&nbsp;审计报告编号&nbsp;
									</th>
									<th fieldname="HTBH" maxlength="15" colindex=12>
										&nbsp;编号&nbsp;
									</th>
									<th fieldname="HTMC" maxlength="15" colindex=13>
										&nbsp;名称&nbsp;
									</th>
									<th fieldname="HTJE" maxlength="15" colindex=14 tdalign="right">
										金额（元）
										<br>
										A
									</th>
									<th fieldname="TBR" colindex=15 maxlength="15">
										&nbsp;提报人&nbsp;
									</th>
									<th fieldname="TBRQ" colindex=16 tdalign="center">
										&nbsp;日期&nbsp;
									</th>
									<th fieldname="TBJE" colindex=17 tdalign="right" maxlength="15">
										金额(元)
										<br>
										B
									</th>
									<th fieldname="WTZXGS" colindex=18 maxlength="15">
										&nbsp;委托咨询公司&nbsp;
									</th>
									<th fieldname="YZSDRQ" colindex=19 tdalign="center">
										&nbsp;日期&nbsp;
									</th>
									<th fieldname="YZSDJE" colindex=20 tdalign="right"
										maxlength="15">
										金额(元)
										<br>
										C
									</th>
									<th fieldname="CSSDRQ" colindex=21 tdalign="center">
										&nbsp;日期&nbsp;
									</th>
									<th fieldname="CSSDJE" colindex=22 tdalign="right"
										maxlength="15">
										金额(元)
										<br>
										D
									</th>
									<th fieldname="CSBGBH" colindex=23 maxlength="15">
										&nbsp;财审报告编号&nbsp;
									</th>
									<th fieldname="TBYZ" tdalign="center" noprint="true"
										colindex=24>
										上报值-审定值
										<br>
										B-C
									</th>
									<th fieldname="YZCS" tdalign="center" noprint="true"
										maxlength="15" colindex=25>
										审定值-财审值
										<br>
										C-D
									</th>
									<th fieldname="CSSJ" tdalign="center" noprint="true"
										maxlength="15" colindex=26>
										财审值-审计值
										<br>
										D-E
									</th>
									<th fieldname="TBSJ" tdalign="center" noprint="true"
										maxlength="15" colindex=27>
										上报值-审计值
										<br>
										B-E
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
		<form method="post" id="htForm" style="display: none">

				<table class="B-table" width="100%">
					<TR style="display: none">
						<!-- <TR> -->
						<TD>
							<INPUT class="span12" type="text" name="GC_ZJB_JSB_ID"  fieldname="GC_ZJB_JSB_ID" id="GC_ZJB_JSB_ID" />
							<INPUT class="span12" type="text" id="XQZT" name="XQZT" fieldname="ZT" value="1" keep="true" />
						</TD>
					</TR>
				</table>
			</form>
		<div align="center">
			<FORM name="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="txtFilter" order="asc"
					fieldname="g.xmbh,g.xmbs,g.pxh" />
				<input type="hidden" name="resultXML" id="resultXML">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">
				<input type="hidden" name="queryResult" id="queryResult">

			</FORM>
		</div>
	</body>
</html>