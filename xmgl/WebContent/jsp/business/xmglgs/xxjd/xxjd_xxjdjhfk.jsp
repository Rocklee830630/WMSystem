<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>形象进度管理-形象进度计划反馈</title>
<%
	//获取计划数据ID
	String jhfkId = request.getParameter("jhfkId");
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xxjd/xxjdController.do";
//页面初始化
$(function() {
	init();
	//按钮绑定事件(保存)
	$("#btnSave").click(function() {
		
		if($("#jhfkForm").validationButton())
		{
			 //生成json串
			var data = Form2Json.formToJSON(jhfkForm);
    		var data1 = addjson(convertJson.string2json1(data),"ZT_FK","0");
    		$(window).manhuaDialog.getParentObj().getWinData_jhfk(data1);
   			$(window).manhuaDialog.close(); 
		    /* $(window).manhuaDialog.setData(data1);
   			$(window).manhuaDialog.sendData();
   			$(window).manhuaDialog.close(); */
		   
		}else{
			requireFormMsg();
		  	return;
		}
	});
	//按钮绑定事件(提交)
	$("#btnTj").click(function() {
		
		if($("#jhfkForm").validationButton())
		{
    		//生成json串
			var data = Form2Json.formToJSON(jhfkForm);
    		var data1 = addjson(convertJson.string2json1(data),"ZT_FK","1");
    		$(window).manhuaDialog.getParentObj().getWinData_jhfk(data1);
   			$(window).manhuaDialog.close(); 
		   
		}else{
			requireFormMsg();
		  	return;
		}
	});
	
});
//页面默认参数
function init(){
	//getPensonByDep();
	
	
	var jhfkId = '<%=jhfkId%>';
	if(jhfkId != ""){
		var data = null;
		submit(controllername+"?queryJhbzByJhfkid&jhfkId="+jhfkId,data,jhfkList);
	}
	var userid = '<%=userid%>';
	$("#FKRID").val(userid);
}
function submit(actionName, data,tablistID){
	$.ajax({
		type : 'post',
		url : actionName,
		data : data,
		cache : false,
		dataType : "json",  
		async :	false,
		success : function(result) {
			var formObj = convertJson.string2json1(result.msg).response.data[0];
			$("#jhfkForm").setFormValues(formObj);
			//$("#BDMC").val(formObj.BDID_SV);
			$("#FKRQ").val('<%=sysdate %>');
			$("#resultXML").val(JSON.stringify(formObj));
		}
	});
}
//过滤反馈人
function getPensonByDep(){
	var depid = '<%=deptId %>';
	
	var src = "";
	$("#FKRID").attr('src',src);
	reloadSelectTableDic($("#FKRID"));
	
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">形象进度计划跟踪与编制
      	<span class="pull-right">
	  		<button id="btnSave" class="btn" type="button">保存</button>
	  		<button id="btnTj" class="btn" type="button">提交</button>
      	</span>
      </h4>
     <form method="post" id="jhfkForm"  >
      <table class="B-table" width="100%" id="jhfkList">
        <input type="hidden" id=GC_XMGLGS_XXJD_JHFK_ID fieldname="GC_XMGLGS_XXJD_JHFK_ID" name = "GC_XMGLGS_XXJD_JHFK_ID"/></TD>
        <input type="hidden" id=JHSJID fieldname="JHSJID" name = "JHSJID"/></TD>
        <input type="hidden" id=JHBZID fieldname="JHBZID" name = "JHBZID"/></TD>
        <tr>
			<th width="10%" class="right-border bottom-border text-right disabledTh">项目名称</th>
       	 	<td class="bottom-border right-border">
         		<input class="span12" id="XMMC" type="text" fieldname="XMMC" name = "XMMC" keep="true"  readonly />
       	 	</td>
         	<th width="10%" class="right-border bottom-border text-right disabledTh">标段名称</th>
       		<td class="bottom-border right-border">
         		<input class="span12" id="BDID" type="text" fieldname="BDID" name = "BDID" keep="true" readonly />
         	</td>
        </tr>
      </table>
      <p style="height:5px"></p>
      <table class="B-table" width="100%">
      	<tr>
			<th width="10%" class="right-border bottom-border text-right" colspan="2">反馈人</th>
			<td class="bottom-border" width="10%">
          		<select class="span12" id="FKRID" name = "FKRID"  fieldname = "FKRID" operation="=" kind="dic" src="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG='1' AND PERSON_KIND = '3' AND DEPARTMENT = '<%=deptId %>'" check-type="required">
            	</select>
          	</td>
          	<th width="12%" class="right-border bottom-border text-right">反馈日期</th>
			<td class="bottom-border" width="12%">
          		<input class="span12 date" id="FKRQ" type="date" name="FKRQ" fieldname="FKRQ"/>
          	</td>
          	<td class="bottom-border" width="56%">
          	</td>
		</tr>
      </table>
      <p style="height:5px"></p>
		<table id="T_xxjdjhbz" class="B-table" width="100%">
			<tr>
				<th class="right-border bottom-border" colspan="3" width="20%"></th>
				<th class="right-border bottom-border" width="12%">计划完成日期</th>
				<th class="right-border bottom-border"  width="12%">实际完成日期</th>
				<th class="right-border bottom-border" width="56%">存在问题</th>
			</tr>
			<tr>
	          <th width="5%" class="right-border bottom-border" rowspan="6">桥梁<br>部分</th>
	          <th class="right-border bottom-border" colspan="2" width="13%">桩基</th>
	          <td class="bottom-border" width="10%">
	          	<input class="span12" type="text" id="ZJ" fieldname="ZJ" name="ZJ" readonly>
	          </td>
	          <td class="bottom-border" width="10%">
	          	<input class="span12 date" id="ZJ_SJ" type="date" name="ZJ_SJ" fieldname="ZJ_SJ"/>
	          </td>
	          <td class="bottom-border">
	          	<input class="span12" type="text" id="ZJ_WT" fieldname="ZJ_WT" name="ZJ_WT" maxlength="4000">
	          </td>
	         
        	</tr>
        	<tr>
        		<th class="right-border bottom-border" colspan="2" width="13%">承台</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="CT" fieldname="CT" name="CT" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="CT_SJ" type="date" name="CT_SJ" fieldname="CT_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="CT_WT" fieldname="CT_WT" name="CT_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th class="right-border bottom-border" colspan="2" width="13%">墩柱</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="DZ" fieldname="DZ" name="DZ" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="DZ_SJ" type="date" name="DZ_SJ" fieldname="DZ_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="DZ_WT" fieldname="DZ_WT" name="DZ_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th class="right-border bottom-border" rowspan="2" width="5%">梁</th>
	          	<th class="right-border bottom-border" width="10%">主线梁</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="ZXL" fieldname="ZXL" name="ZXL" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="ZXL_SJ" type="date" name="ZXL_SJ" fieldname="ZXL_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="ZXL_WT" fieldname="ZXL_WT" name="ZXL_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th class="right-border bottom-border">匝道</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="ZD" fieldname="ZD" name="ZD" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="ZD_SJ" type="date" name="ZD_SJ" fieldname="ZD_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="ZD_WT" fieldname="ZD_WT" name="ZD_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th class="right-border bottom-border" colspan="2">其他附属</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="QTFS" fieldname="QTFS" name="QTFS" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="QTFS_SJ" type="date" name="QTFS_SJ" fieldname="QTFS_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="QTFS_WT" fieldname="QTFS_WT" name="QTFS_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border" rowspan="7">道路<br>部分</th>
        		<th class="right-border bottom-border" colspan="2" width="13%">土方回填</th>
	          	<td class="bottom-border" width="10%">
	          		<input class="span12" type="text" id="TFHT" fieldname="TFHT" name="TFHT" readonly>
	          	</td>
	          	<td class="bottom-border" width="10%">
	          		<input class="span12 date" id="TFHT_SJ" type="date" name="TFHT_SJ" fieldname="TFHT_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="TFHT_WT" fieldname="TFHT_WT" name="TFHT_WT" maxlength="4000">
		        </td>
        	</tr>
        	
        	<tr>
        		<th class="right-border bottom-border" colspan="2">洞内设备安装</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="DNSSAZ" fieldname="DNSSAZ" name="DNSSAZ" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="DNSSAZ_SJ" type="date" name="DNSSAZ_SJ" fieldname="DNSSAZ_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="DNSSAZ_WT" fieldname="DNSSAZ_WT" name="DNSSAZ_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th class="right-border bottom-border" colspan="2">洞内沥青铺装层</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="DNLQPZC" fieldname="DNLQPZC" name="DNLQPZC" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="DNLQPZC_SJ" type="date" name="DNLQPZC_SJ" fieldname="DNLQPZC_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="DNLQPZC_WT" fieldname="DNLQPZC_WT" name="DNLQPZC_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th class="right-border bottom-border" colspan="2">洞内附属设施及装修</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="DNFSSSJZX" fieldname="DNFSSSJZX" name="DNFSSSJZX" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="DNFSSSJZX_SJ" type="date" name="DNFSSSJZX_SJ" fieldname="DNFSSSJZX_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="DNFSSSJZX_WT" fieldname="DNFSSSJZX_WT" name="DNFSSSJZX_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th class="right-border bottom-border" colspan="2">辅道基层</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="FDJC" fieldname="FDJC" name="FDJC" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="FDJC_SJ" type="date" name="FDJC_SJ" fieldname="FDJC_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="FDJC_WT" fieldname="FDJC_WT" name="FDJC_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th class="right-border bottom-border" colspan="2">辅道面层</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="FDMC" fieldname="FDMC" name="FDMC" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="FDMC_SJ" type="date" name="FDMC_SJ" fieldname="FDMC_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="FDMC_WT" fieldname="FDMC_WT" name="FDMC_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th class="right-border bottom-border" colspan="2">方砖等道路附属</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="FZDDLFS" fieldname="FZDDLFS" name="FZDDLFS" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="FZDDLFS_SJ" type="date" name="FZDDLFS_SJ" fieldname="FZDDLFS_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="FZDDLFS_WT" fieldname="FZDDLFS_WT" name="FZDDLFS_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th width="5%" class="right-border bottom-border" rowspan="7">其他<br>工程<br>节点</th>
        		<th class="right-border bottom-border" colspan="2">维护桩基</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="WHZJ" fieldname="WHZJ" name="WHZJ" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="WHZJ_SJ" type="date" name="WHZJ_SJ" fieldname="WHZJ_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="WHZJ_WT" fieldname="WHZJ_WT" name="WHZJ_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th class="right-border bottom-border" colspan="2">止水帷幕</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="ZSWM" fieldname="ZSWM" name="ZSWM" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="ZSWM_SJ" type="date" name="ZSWM_SJ" fieldname="ZSWM_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="ZSWM_WT" fieldname="ZSWM_WT" name="ZSWM_WT" maxlength="4000">
		        </td>
        	</tr>
        	<tr>
        		<th class="right-border bottom-border" colspan="2">冠梁及混凝土支撑</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="GLJHNTZC" fieldname="GLJHNTZC" name="GLJHNTZC" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="GLJHNTZC_SJ" type="date" name="GLJHNTZC_SJ" fieldname="GLJHNTZC_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="GLJHNTZC_WT" fieldname="GLJHNTZC_WT" name="GLJHNTZC_WT" maxlength="4000">
		        </td>
        	 </tr>
        	 <tr>
        		<th class="right-border bottom-border" colspan="2">土方开挖、喷混及钢支撑安装</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="TFKW" fieldname="TFKW" name="TFKW" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="TFKW_SJ" type="date" name="TFKW_SJ" fieldname="TFKW_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="TFKW_WT" fieldname="TFKW_WT" name="TFKW_WT" maxlength="4000">
		        </td>
        	 </tr>
        	 <tr>
        		<th class="right-border bottom-border" colspan="2">主体底板</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="ZTDB" fieldname="ZTDB" name="ZTDB" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="ZTDB_SJ" type="date" name="ZTDB_SJ" fieldname="ZTDB_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="ZTDB_WT" fieldname="ZTDB_WT" name="ZTDB_WT" maxlength="4000">
		        </td>
        	 </tr>
        	 <tr>
        		<th class="right-border bottom-border" colspan="2">主体边墙及侧墙</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="ZTBQJCQ" fieldname="ZTBQJCQ" name="ZTBQJCQ" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="ZTBQJCQ_SJ" type="date" name="ZTBQJCQ_SJ" fieldname="ZTBQJCQ_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="ZTBQJCQ_WT" fieldname="ZTBQJCQ_WT" name="ZTBQJCQ_WT" maxlength="4000">
		        </td>
        	 </tr>
        	 <tr>
        		<th class="right-border bottom-border" colspan="2">主体顶板</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="ZTDCB" fieldname="ZTDCB" name="ZTDCB" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="ZTDCB_SJ" type="date" name="ZTDCB_SJ" fieldname="ZTDCB_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="ZTDCB_WT" fieldname="ZTDCB_WT" name="ZTDCB_WT" maxlength="4000">
		        </td>
        	 </tr>
        	 <tr>
        	 	<th width="5%" class="right-border bottom-border" rowspan="6">自定义<br>节点</th>
        		<th class="right-border bottom-border" colspan="2">节点1</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="JD1" fieldname="JD1" name="JD1" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="JD1_SJ" type="date" name="JD1_SJ" fieldname="JD1_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="JD1_WT" fieldname="JD1_WT" name="JD1_WT" maxlength="4000">
		        </td>
        	 </tr>
        	 <tr>
        		<th class="right-border bottom-border" colspan="2">节点2</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="JD2" fieldname="JD2" name="JD2" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="JD2_SJ" type="date" name="JD2_SJ" fieldname="JD2_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="JD2_WT" fieldname="JD2_WT" name="JD2_WT" maxlength="4000">
		        </td>
        	 </tr>
        	 <tr>
        		<th class="right-border bottom-border" colspan="2">节点3</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="JD3" fieldname="JD3" name="JD3" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="JD3_SJ" type="date" name="JD3_SJ" fieldname="JD3_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="JD3_WT" fieldname="JD3_WT" name="JD3_WT" maxlength="4000">
		        </td>
        	 </tr>
        	 <tr>
        		<th class="right-border bottom-border" colspan="2">节点4</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="JD4" fieldname="JD4" name="JD4" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="JD4_SJ" type="date" name="JD4_SJ" fieldname="JD4_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="JD4_WT" fieldname="JD4_WT" name="JD4_WT" maxlength="4000">
		        </td>
        	 </tr>
        	 <tr>
        		<th class="right-border bottom-border" colspan="2">节点5</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="JD5" fieldname="JD5" name="JD5" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="JD5_SJ" type="date" name="JD5_SJ" fieldname="JD5_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="JD5_WT" fieldname="JD5_WT" name="JD5_WT" maxlength="4000">
		        </td>
        	 </tr>
        	 <tr>
        		<th class="right-border bottom-border" colspan="2">节点6</th>
		        <td class="bottom-border" width="10%">
		        	<input class="span12" type="text" id="JD6" fieldname="JD6" name="JD6" readonly>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12 date" id="JD6_SJ" type="date" name="JD6_SJ" fieldname="JD6_SJ"/>
		        </td>
		        <td class="bottom-border" width="10%">
		          	<input class="span12" type="text" id="JD6_WT" fieldname="JD6_WT" name="JD6_WT" maxlength="4000">
		        </td>
        	 </tr>
        </table>
        <p style="height:5px"></p>
        <table class="B-table" width="100%">
        	<tr>
	          <th width="5%" class="right-border bottom-border">存在主要问题 </th>
	          <td class="right-border bottom-border" colspan="5">
	          	<textarea class="span12" id="CZZYWT" rows="3" name ="CZZYWT" fieldname="CZZYWT" maxlength="4000"></textarea>
	          </td>
        	</tr>
		</table>
      </form>
      <h4 id="xxjdjhbz" class="title">历史版本</h4>
      <table class="table-hover table-activeTd B-table" id="bgxxList" width="100%" type="single" pageNum="5">
		<thead>
			<tr>
				<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
				<th fieldname="FKRQ" tdalign="center" style="width:150px">&nbsp;反馈日期&nbsp;</th>
				<th fieldname="FKRXM" tdalign="center" style="width:150px">&nbsp;反馈人&nbsp;</th>
				<th fieldname="CZZYWT" maxlength="55">&nbsp;存在主要问题&nbsp;</th>
			</tr>
		</thead>
		<tbody>
           </tbody>
	</table>
    </div>
   </div>
  </div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ" id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>

</html>