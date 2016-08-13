<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>

<%
	String xmid= request.getParameter("xmid");
	String bdid= request.getParameter("bdid");
	//String GC_JH_SJ_ID= request.getParameter("GC_JH_SJ_ID");
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<app:base/>
<app:dialogs></app:dialogs>
<title>工程周报管理</title>
<script type="text/javascript" charset="utf-8">
 
var controllername= "${pageContext.request.contextPath }/gczb/gczbController.do";
var xmid,json,bdid,success,row_index,kssj_old,jssj_old;
$("#XMGLGS_tj").val('<%=deptId%>');
bdid='<%=bdid%>';
//初始化查询
$(document).ready(function(){
	query(true);
});




//查询
$(function() {
	var btn=$("#example_query");
	btn.click(function() {
		//生成json串
		query(false);
	});
	
	
	//清空查询表单
  	var btn_clearQuery=$("#query_clear");
  	btn_clearQuery.click(function() {
    $("#queryForm").clearFormResult();
  	setDefaultOption($("#ND"),new Date().getFullYear());
  	setDefaultOption($("#ND"),new Date().getFullYear());
      //其他处理放在下面
  });
	
    
	//删除周报信息
	var btn=$("#delete");
	btn.click(function()
	{
		//生成json串
		if(-1==$("#DT1").getSelectedRowIndex())
		{
			 requireSelectedOneRow();
			 return;
		}
	 	var data = Form2Json.formToJSON(demoForm);
		var data1 = defaultJson.packSaveJson(data);
		xConfirm("信息提示","删除后数据无法恢复！确定要删除吗？");
		$('#ConfirmYesButton').unbind();
		$('#ConfirmYesButton').one("click",function(){ 
			defaultJson.doDeleteJson(controllername+"?delete",data1,null,'delet_query');
		});		
	}
	);
 });


//删除回调函数
function delet_query()
{
	$("#demoForm").clearFormResult();
 	query(true);
	var parentmain=$(window).manhuaDialog.getParentObj();	
	parentmain.gs_feedback(); 	
}

//查询
function query(bool)
{
	var data=combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	if(bdid==null||bdid=="null"||bdid=='undefined'||bdid=='')
	{
		defaultJson.doQueryJsonList(controllername+"?query_tj&xmid="+'<%=xmid%>',data,DT1,null,bool);	
	}
	else
	{
		defaultJson.doQueryJsonList(controllername+"?query_tj&bdid="+'<%=bdid%>',data,DT1,null,bool);
	}		
}


//保存
$(function() {	
	var btn=$("#example_save");
	btn.click(function() {
		var KSSJ=$("#KSSJ").val();
		var JSSJ=$("#JSSJ").val();
	 	 if($("#demoForm").validationButton())
		{
		}else{
	 		requireFormMsg();
			return ;
		}
     	if(JSSJ<KSSJ)
    	{
    		xInfoMsg('结束时间不能早于开始时间！');
    		$("#JSSJ").focus();
    		return;
    	}
     	if(KSSJ!=kssj_old)
     	{
     		switch(sj(KSSJ,JSSJ,$("#JHSJID").val(),1,$("#GC_XMGLGS_ZBB_ID").val()))
     		{
     			case 'flag1':
    				xInfoMsg('开始时间重叠请重新录入！');
    				return;
    				break;	
     			case 'flag3':
    				xInfoMsg('时间区间重叠请重新录入！');
    				return;
    				break;	
     		}
     	}
		if(JSSJ!=jssj_old)
		{
			switch(sj(KSSJ,JSSJ,$("#JHSJID").val(),2,$("#GC_XMGLGS_ZBB_ID").val()))
			{
 			case 'flag2':
				xInfoMsg('结束时间重叠请重新录入！');
				return;
				break;	
 			case 'flag3':
				xInfoMsg('时间区间重叠请重新录入！');
				return;
				break;	
			
			}	
		}	
		//生成json串
		var data=Form2Json.formToJSON(demoForm);
		//组成保存json串格式
		var data1=defaultJson.packSaveJson(data);
		//通过判断id是否为空来判断是插入还是修改
		success=defaultJson.doInsertJson(controllername + "?update_zb&ywid="+$("#ywid").val(), data1,null,"update");
 	});
});


//校验时间是否重复
function sj(kssj,jssj,jhsjid,ksORjs,zbid)
{
	var isok='';
	var actionName=controllername+"?query_sj&kssj="+kssj+"&jssj="+jssj+"&jhsjid="+jhsjid+"&ksORjs="+ksORjs+"&zbid="+zbid;
	$.ajax({
			url : actionName,
			cache : false,
			async :	false,
			dataType : "json",  
			type : 'post',
			success : function(result) {
			isok=result.msg;
		} 
	});
  return isok;
}


//更新回调函数
function update()
{
	if(success==true)
	{
		var obj=$("#resultXML").val();
		gs_feedback();
		var parentmain=$(window).manhuaDialog.getParentObj();	
		parentmain.gs_feedback();
	}				
}


//清空
$(function() {	
	var btn=$("#example_ok");
	btn.click(function() {
		$("#demoForm").clearFormResult(); 
		bdid=null;
	});
});


//插入时列表行更新
function add(data)
{
	var subresultmsgobj1=defaultJson.dealResultJson(data);
	$("#DT1").insertResult(JSON.stringify(subresultmsgobj1),DT1,1);
	$("#DT1").cancleSelected();
	$("#DT1").setSelect(0);
}


//页刷新
function gs_feedback(){
	row_index=$("#DT1").getSelectedRowIndex();
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	var tempJson = convertJson.string2json1(data);
	var a=$("#DT1").getCurrentpagenum();
	tempJson.pages.currentpagenum=a;
	data = JSON.stringify(tempJson);
	if(bdid==null||bdid=="null"||bdid=='undefined')
	{
		defaultJson.doQueryJsonList(controllername+"?query_tj&xmid="+'<%=xmid%>',data,DT1,"gs_feedback_query",true);	
	}
	else
	{
		defaultJson.doQueryJsonList(controllername+"?query_tj&bdid="+'<%=bdid%>',data,DT1,"gs_feedback_query",true);
	}	
}


function gs_feedback_query()
{
	$("#DT1").setSelect(row_index);
	var json=$("#DT1").getSelectedRow();
}

//点击获取行对象
function tr_click(obj,tabListid){
	//alert(JSON.stringify(obj))
	var zbbid=obj.XCZP;
	kssj_old=obj.KSSJ;
	jssj_old=obj.JSSJ;
	//alert(kssj_old+"*****"+jssj_old)
	deleteFileData(obj.XCZP,"","","");
	setFileData(obj.XCZP,obj.JHSJID,obj.SJBH,obj.YWLX,"0");
	queryFileData(zbbid,"","","");
	$("#demoForm").setFormValues(obj);
	$("#GC_XMGLGS_ZBB_ID").val(zbbid);
}


//现场图片图标
function viewimg(obj){
	var isfj=obj.XCZP_SV;
	if(isfj!='' && isfj!=undefined)
	{
		return '<a href="javascript:void(0);"><img src=\"/xmgl/images/icon-annex.png\" title="现场照片" onclick="showPic(\''+obj.XCZP+'\')"></a>';
	}	
}


//现场照片
function showPic(ywid)
{
	$(window).manhuaDialog({"title":"现场照片","type":"text","content":g_sAppName +"/jsp/business/gcb/zbgl/viewimg.jsp?ywid="+ywid+"&lb=0311","modal":"2"});
}


//判断标段是否有值
function pdbd(obj){
	var bdid = obj.BDID;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
}


//判断标段编号是否有值
function pdbdbh(obj){
	var bdid = obj.BDID;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise">	
			<form method="post" id="queryForm"  >
      			<table class="B-table" width="100%">
      				<!--可以再此处加入hidden域作为过滤条件 -->
			      	<TR  style="display:none;">
				        <TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
							<input type="hidden" id="XMGLGS_tj" fieldname="xdk.XMGLGS" name="XMGLGS"  keep="true" operation="="/>
						</TD>
			        </TR>
        			<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<th width="3%" class="right-border bottom-border text-right">周报日期（起）</th>
						<td width="1%" class="right-border bottom-border" width="10%">
							<input class="span12 date" id="kssj" type="date" fieldtype="date" name="QKSSJ" fieldname="KSSJ" fieldformat="yyyyMMdd" operation=">="/>
						</td>
						<th width="3%" class="right-border bottom-border text-right">周报日期（止）</th>
						<td width="1%" class="right-border bottom-border" width="10%">
							<input class="span12 date" id="jssj" type="date" fieldtype="date" name="QJSSJ" fieldname="JSSJ" fieldformat="yyyyMMdd" operation="<="/>
						</td>
						<td width="30%" class="text-left bottom-border text-right">
							<button id="example_query" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
							<button id="query_clear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
						</td>
         			</tr>
      			</table>
      		</form>
			<div style="height:5px;"> </div>
			<div class="overFlowX"> 
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" pageNum="5" type="single" nopromptmsg="true">
	                <thead>
						<tr>
							<th  name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" rowspan="2" colindex=2>&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDBH" rowspan="2" colindex=4 CustomFunction="pdbdbh">&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC" rowspan="2" colindex=5 maxlength="15" CustomFunction="pdbd">&nbsp;标段名称&nbsp;</th>
							<th fieldname="XCZP" rowspan="2" colindex=6 tdalign="center" CustomFunction="viewimg">&nbsp;现场照片&nbsp;</th>
							<th fieldname="QTWJ" rowspan="2" colindex=7 tdalign="center">&nbsp;其他文件&nbsp;</th>							
 							<th fieldname="KSSJ" rowspan="2" colindex=8 tdalign="center">&nbsp;开始时间&nbsp;</th>
							<th fieldname="JSSJ" rowspan="2" colindex=9 tdalign="center">&nbsp;结束时间&nbsp;</th>
 							<th colspan="4">工程形象进度</th>
							<th fieldname="XZJH" maxlength="15" rowspan="2" colindex=14>&nbsp;下周计划&nbsp;</th>
							<th colspan="3">排迁形象进度</th>
							<th colspan="3">拆迁形象进度</th>
							<th colspan="3">周计量（万元）</th>
							<th colspan="5">存在问题及建议完成期限</th>
							<th fieldname="NOTE" rowspan="2" colindex=29 maxlength="15">&nbsp;备注&nbsp;</th>
						</tr>
						<tr>
							<th fieldname="BZJH" maxlength="15" colindex=10>&nbsp;本周计划&nbsp;</th>
							<th fieldname="BZWC" maxlength="15" colindex=11>&nbsp;本周完成&nbsp;</th>
							<th fieldname="BNWC" maxlength="15" colindex=12>&nbsp;本年完成&nbsp;</th>
							<th fieldname="LJWC" maxlength="15" colindex=13>&nbsp;累计完成&nbsp;</th>
							<th fieldname="GXMC" colindex=15 maxlength="15">&nbsp;管线名称&nbsp;</th>
							<th fieldname="PQWCSJ" colindex=16>&nbsp;完成时限&nbsp;</th>
							<th fieldname="PABZJZ" colindex=17 maxlength="15">&nbsp;本周进展&nbsp;</th>
							<th fieldname="CQWMC" colindex=18 maxlength="15">&nbsp;拆迁物名称&nbsp;</th>
							<th fieldname="CQWCSJ" colindex=19>&nbsp;完成时限&nbsp;</th>
							<th fieldname="CQBZJZ" colindex=20 maxlength="15">&nbsp;本周进展&nbsp;</th>
							<th fieldname="ZJLBZ" colindex=21 tdalign="right">&nbsp;本周完成&nbsp;</th>
							<th fieldname="ZJLND" colindex=22 tdalign="right">&nbsp;本年完成&nbsp;</th>
							<th fieldname="ZJLLJWC" colindex=23 tdalign="right">&nbsp;累计完成（含总工程量）&nbsp;</th>
							<th fieldname="QQWT" colindex=24 maxlength="15">&nbsp;前期问题&nbsp;</th>
							<th fieldname="HTZJWT" colindex=25 maxlength="15">&nbsp;合同、造价问题&nbsp;</th>
							<th fieldname="SJWT" colindex=26 maxlength="15">&nbsp;设计问题&nbsp;</th>
							<th fieldname="ZCWT" colindex=27 maxlength="15">&nbsp;征拆问题&nbsp;</th>
							<th fieldname="PQWT" colindex=28 maxlength="15">&nbsp;排迁问题&nbsp;</th>
						</tr>
	                </thead>
	                <tbody></tbody>
	           </table>
	        </div>     
			<div style="height:5px;"> </div>
			<h4 class="title">
	      		工程周报信息
	      		<span class="pull-right">
	          		<button id="example_save" class="btn" type="button">保存</button>
	          		<button id="delete" class="btn" type="button">删除</button>
	          	</span>
			</h4>
    		<form method="post" id="demoForm">
     			<table class="B-table" width="100%">
     				<input type="hidden" id="GC_XMGLGS_ZBB_ID" fieldname="GC_XMGLGS_ZBB_ID" name="GC_XMGLGS_ZBB_ID"/>
					<input type="hidden" id="HTID" fieldname="HTID" name="HTID"/>
					<input type="hidden" id="JHSJID" fieldname="JHSJID" name="JHSJID"/>
					<input type="hidden" id="BDID" fieldname="BDID" name="BDID"/>
					<input type="hidden" id="BDBH" fieldname="BDBH" name="BDBH"/>
					<input type="hidden" id="XDKID" fieldname="XDKID" name="XDKID"/>
					<input type="hidden" id="XMBH" fieldname="XMBH" name="XMBH"/>
					<input type="hidden" id="XMDZ" fieldname="XMDZ" name="XMDZ"/>
					<input type="hidden" id="YZDB" fieldname="YZDB" name="YZDB"/>
					<input type="hidden" id="SGDW" fieldname="SGDW" name="SGDW"/>
					<input type="hidden" id="JLDW" fieldname="JLDW" name="JLDW"/>
					<input type="hidden" id="XMGLGS" fieldname="XMGLGS" name="XMGLGS"/>
					<input type="hidden" id="ywid" fieldname="ywid" name="ywid"/>   			
					<tr>
						<th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
						<td width="42%" colspan="2" class="right-border bottom-border">
							<input class="span12" id="XMMC" type="text" placeholder="必填" check-type="required" fieldname="XMMC" name = "XMMC"  readonly />
						</td>
						<th width="8%" class="right-border bottom-border text-right disabledTh">标段名称</th>
						<td width="42%" colspan="2" class="bottom-border right-border">
							<input class="span12" id="BDMC" type="text" fieldname="BDMC" name="BDMC"  readonly />
						</td>
					</tr>
	        		<tr>
						<th class="right-border bottom-border text-right">开始时间</th>
						<td colspan="2" class="right-border bottom-border">
							<input class="span12 date" id="KSSJ" type="date" check-type="required" name="KSSJ" fieldname="KSSJ"/>
						</td>
						<th class="right-border bottom-border text-right">结束时间</th>
						<td colspan="2" class="bottom-border right-border">
							<input class="span12 date" id="JSSJ" type="date" check-type="required" name="JSSJ" fieldname="JSSJ"/>
						</td>
	        		</tr>
	        	</table>	
	      		<h4 id="zgxx" class="title">工程形象进度</h4>
				<table id="hfbd" class="B-table" width="100%">
	        		<tr>
	        			<th width="8%" class="right-border bottom-border text-right">本周计划</th>
	        			<td  class="right-border bottom-border" colspan="2">
	          				<textarea class="span12" rows="4" name ="BZJH"  fieldname="BZJH"></textarea>
	          			</td>
		        		<th width="8%" class="right-border bottom-border text-right">本周完成</th>
		        		<td class="right-border bottom-border" colspan="2">
	          				<textarea class="span12" rows="4" name ="BZWC"  fieldname="BZWC"></textarea>
	          			</td>
	       	 		</tr>
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right">本年完成</th>
				        <td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" rows="4" name ="BNWC"  fieldname="BNWC"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right">累计完成</th>
			          	<td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" rows="4" name ="LJWC"  fieldname="LJWC"></textarea>
			          	</td>
			        </tr>
			        <tr>
			        	<th class="right-border bottom-border text-right">下周计划</th>
			          	<td class="bottom-border right-border" colspan="5">
			          		<textarea class="span12" rows="4" name ="XZJH"  fieldname="XZJH"></textarea>
			          	</td>
			        </tr>
				</table> 
	      		<h4 id="zgxx" class="title">排迁形象进度</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right">管线名称</th>
			        	<td class="right-border bottom-border" width="23%">
			          		<textarea class="span12" rows="4" name ="GXMC"  fieldname="GXMC" check-type="maxlength"  maxlength="100"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right">完成时限</th>
				        <td class="right-border bottom-border"  width="23%">
			          		<input class="span12 date" type="date" name="PQWCSJ" fieldname="PQWCSJ"/>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right">本周进展</th>
			          	<td class="bottom-border right-border"  width="23%">
			          		<textarea class="span12" rows="4" name ="PABZJZ"  fieldname="PABZJZ"></textarea>
			          	</td>
			        </tr>
				</table>    
	      		<h4 id="zgxx" class="title">拆迁形象进度</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right">拆迁物名称</th>
			        	<td class="right-border bottom-border">
			          		<textarea class="span12" rows="4" name ="CQWMC"  fieldname="CQWMC" check-type="maxlength"  maxlength="1000"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right">完成时限</th>
				        <td class="right-border bottom-border">
			          		<input class="span12 date" type="date" name="CQWCSJ" fieldname="CQWCSJ"/>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right">本周进展</th>
			          	<td class="bottom-border right-border">
			          		<textarea class="span12" rows="4" name ="CQBZJZ"  fieldname="CQBZJZ"></textarea>
			          	</td>
			        </tr>
				</table>
	      		<h4 id="zgxx" class="title">周计量（万元）</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right">本周完成</th>
			        	<td width="27%" class="right-border bottom-border">
			          		<input class="span12" style="text-align:right" min=0 type="number" fieldname="ZJLBZ" name = "ZJLBZ" >
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right">本年完成</th>
				        <td width="26%" class="right-border bottom-border">
			          		<input class="span12" style="text-align:right" min=0 type="number" fieldname="ZJLND" name = "ZJLND" >
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right">累计完成（含工程量）</th>
			          	<td width="26%" class="bottom-border right-border">
			          		<input class="span12" style="text-align:right" type="number" min=0 fieldname="ZJLLJWC" name = "ZJLLJWC" >
			          	</td>
			        </tr>
				</table>
	      		<h4 id="zgxx" class="title">存在问题及建议完成期限</h4>
				<table id="hfbd" class="B-table" width="100%">
			        <tr>
			        	<th width="8%" class="right-border bottom-border text-right">前期问题</th>
			        	<td width="25%" class="right-border bottom-border">
			          		<textarea class="span12" rows="4" name ="QQWT"  fieldname="QQWT"  check-type="maxlength" maxlength="4000"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right">合同、造价问题</th>
				        <td width="25%" class="right-border bottom-border">
			          		<textarea class="span12" rows="4" name ="HTZJWT"  fieldname="HTZJWT"  check-type="maxlength" maxlength="4000"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right">设计问题</th>
				        <td width="25%" class="bottom-border right-border">
			          		<textarea class="span12" rows="4" name ="SJWT"  fieldname="SJWT" check-type="maxlength" maxlength="4000"></textarea>
			          	</td>
			        </tr>
			        <tr>
				        <th width="8%" class="right-border bottom-border text-right">征拆问题</th>
				        <td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" id="JSRW" rows="4" name ="ZCWT"  fieldname="ZCWT" check-type="maxlength" maxlength="4000"></textarea>
			          	</td>
				        <th width="8%" class="right-border bottom-border text-right">排迁问题</th>
			          	<td class="bottom-border right-border" colspan="2">
			          		<textarea class="span12" id="JSRW" rows="4" name ="PQWT"  fieldname="PQWT" check-type="maxlength" maxlength="4000"></textarea>
			          	</td>
			        </tr>
			        <tr>
			        	<th width="8%" class="right-border bottom-border  text-right">备注</th>
			          	<td class="bottom-border right-border" colspan="5">
			          		<textarea class="span12" rows="4" name ="NOTE"  fieldname="NOTE" check-type="maxlength" maxlength="4000"></textarea>
			          	</td>
			        </tr>
					<tr>
				       	<th class="right-border bottom-border text-right">现场照片</th>
				       	<td class="bottom-border right-border" colspan="5">
							<div>
								<span id="xczp" class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0311">
									<i class="icon-plus"></i>
									<span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody id="xczp" fjlb="0311" uploadOptions="type:image" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
								</table>
							</div>
						</td>
				     </tr>															
				    <tr>
				       	<th class="right-border bottom-border text-right text-right">其他文件</th>
				       	<td colspan="5" class="bottom-border right-border">
							<div>
								<span id="zbfj" class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0310">
									<i class="icon-plus"></i>
									<span>添加文件...</span>
								</span>
								<table role="presentation" class="table table-striped">
									<tbody id="zbfj" fjlb="0310" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
								</table>
							</div>
						</td>
				     </tr>															
   	 			</table>
     		</form>
		</div>
	</div>		
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>  
<div align="center">
	<FORM name="frmPost" method="post" style="display: none" target="_blank">
		<!--系统保留定义区域-->
		<input type="hidden" name="queryXML"/> 
		<input type="hidden" name="txtXML"/>
		<input type="hidden" name="txtFilter" order="desc" id="px" fieldname="xmbh,xmbs,bdbh,kssj desc,pxh"/>
		<input type="hidden" name="resultXML" id="resultXML"/>
		<input type="hidden" id="queryResult"/>
		<!--传递行数据用的隐藏域-->
		<input type="hidden" name="rowData"/>
	</FORM>
</div>
</body>
</html>