<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.lang.StringBuffer"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="java.sql.ResultSet"%>
<app:base/>
<title>项目手册</title>
<%
	String id = request.getParameter("id");
	Connection conn = DBUtil.getConnection();//定义连接
  	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String sql = null;
	String[][] results = null;
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var id = "<%=id%>";
var bmid = '<%=deptId%>';
//页面初始化
$(function() {
	init();
});

	
//页面默认参数
function init(){
	$("#id_jb").val(id);
	var data = null;
	submit(controllername+"?queryById&id="+id,data,jcxxList);
	getHtSum(controllername+"?queryHtSum&id="+id+"&bmid="+bmid+"&htlx=all","");

	//生成设计json串
	var data1 = combineQuery.getQueryCombineData(queryForm,frmPost,SJHTList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryHTSJ&id="+id+"&bmid="+bmid+"&htlx=SJ",data1,SJHTList, null, true);
	if($("#SJHTList tbody tr").size()==0){
		$("#SJHTList").hide();
	}else{
		getHtSum(controllername+"?queryHtSum&id="+id+"&bmid="+bmid+"&htlx=SJ","SJ");
	}
	//生成监理json串
	var data2 = combineQuery.getQueryCombineData(queryForm,frmPost,JLHTList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryHTSJ&id="+id+"&bmid="+bmid+"&htlx=JL",data2,JLHTList, null, true);
	if($("#JLHTList tbody tr").size()==0){
		$("#JLHTList").hide();
	}else{
		getHtSum(controllername+"?queryHtSum&id="+id+"&bmid="+bmid+"&htlx=JL","JL");
	}
	//生成施工json串
	var data3 = combineQuery.getQueryCombineData(queryForm,frmPost,SGHTList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryHTSJ&id="+id+"&bmid="+bmid+"&htlx=SG",data3,SGHTList, null, true);
	if($("#SGHTList tbody tr").size()==0){
		$("#SGHTList").hide();
	}else{
		getHtSum(controllername+"?queryHtSum&id="+id+"&bmid="+bmid+"&htlx=SG","SG");
	}
	//生成其他json串
	var data4 = combineQuery.getQueryCombineData(queryForm,frmPost	,QTHTList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryHTSJ&id="+id+"&bmid="+bmid+"&htlx=",data4,QTHTList, null, true);
	if($("#QTHTList tbody tr").size()==0){
		$("#QTHTList").hide();
	}else{
		getHtSum(controllername+"?queryHtSum&id="+id+"&bmid="+bmid+"&htlx=","QT");
	}

	
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
			var rowObj = convertJson.string2json1(result.msg).response.data[0];
			//$("#xmgyForm").setFormValues(rowObj);
			$("#XMMC").text(rowObj.XMMC);
			$("#XMGLGS").text(rowObj.XMGLGS_SV);
			$("#YZDB").text(rowObj.YZDB_SV);
			
			$("#LXFS_YZDB").text(rowObj.LXFS_YZDB);
			
			
			$("#resultXML").val(JSON.stringify(rowObj));
		}
	});
}

function getHtSum(url,tabType){
	
	//计算使用和拨付笔数
	$.ajax({	
		url : url,		
		cache : false,	
		async :	false,	
		dataType : "json",
		type : 'post',		
		success : function(response) {	
			//$("#resultXML").val(response.msg);	
			var resultobj = defaultJson.dealResultJson(response.msg);
			if(tabType==""){
				$("#HTS").text(resultobj.NUM);
				
				//$("#HTS").val(resultobj.NUM);
				if(resultobj.ZHTQDJ!="0"){
					//$("#JE").val(resultobj.ZHTQDJ+".00");
					$("#JE").text(resultobj.ZHTQDJ_SV);
				}
			}else if(tabType=="SJ"){
				if($("#SJHTList tbody tr").length>0){
					$("#SJHTList").append("<th></th><td align='center'>合计</td><td></td><td></td><td></td><td></td><td align='right'>"+resultobj.ZZXHTJ_SV+"</td><td></td><td></td><td></td><td></td><td></td>");
				}
			}else if(tabType=="JL"){
				if($("#JLHTList tbody tr").length>0){
					$("#JLHTList").append("<th></th><td align='center'>合计</td><td></td><td></td><td></td><td></td><td align='right'>"+resultobj.ZZXHTJ_SV+"</td><td></td><td></td><td></td><td></td><td></td>");
				}
			}else if(tabType=="SG"){
				if($("#SGHTList tbody tr").length>0){
					$("#SGHTList").append("<th></th><td align='center'>合计</td><td></td><td></td><td></td><td></td><td align='right'>"+resultobj.ZZXHTJ_SV+"</td><td></td><td></td><td></td><td></td><td></td>");
				}
			}else if(tabType=="QT"){
				if($("#QTHTList tbody tr").length>0){
					$("#QTHTList").append("<th></th><td align='center'>合计</td><td></td><td></td><td></td><td></td><td align='right'>"+resultobj.ZZXHTJ_SV+"</td><td></td><td></td><td></td><td></td><td></td>");
				}
			}
		}
	});
}
function doBD(obj){
	  var bd_name=obj.BDMC;
	  if(bd_name==null||bd_name==""){
		  /* return '<div style="text-align:center"><abbr title=本条项目信息没有标段内容>—</abbr></div>'; */
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
}
function xqxx(id,zblx){
	$(window).manhuaDialog({"title":"招标需求管理>查看招标信息","type":"text","content":"/xmgl/jsp/business/ztb/ztbgl_xx.jsp?xx="+id+"&zblx="+zblx,"modal":"1"});
}
function hqxx(sjbh,ywlx){
	var s = "/xmgl/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";   
 	$(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});
}
function doGZMC(obj){
	  var gzmc_name=obj.GZMC;
	  if(gzmc_name==null||gzmc_name==""){
		  /* return '<div style="text-align:center"><abbr title=本条项目信息没有标段内容>—</abbr></div>'; */
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return "<a href=javascript:xqxx('"+obj.GC_ZTB_XQ_ID+"','"+obj.ZBLX+"')>"+gzmc_name+"</a>";			  
	  }
}

function doHQ(obj){
	  var htzt=obj.EVENTSJBH;
	  if(htzt!='0'){
		  var sjbh = obj.SJBH;
		  var ywlx = obj.YWLX;
		  return "<a href=javascript:hqxx('"+sjbh+"','"+ywlx+"')><i title='审批信息' class='icon-file showXmxxkInfo'></a>";	
	  }else{
		return '<div style="text-align:center">—</div>';
	  }
		  		  
	  
}
function doZTBMC(obj){
	if(obj.ZTBMC==null||obj.ZTBMC==''){
		return '<div style="text-align:center">—</div>'
	}else{
		return obj.ZTBMC;
	}
}
function doZBFS(obj){
	if(obj.ZBFS==null||obj.ZBFS==''){
		return '<div style="text-align:center">—</div>'
	}else{
		return obj.ZBFS_SV;
	}
}
function doDLDW(obj){
	if(obj.DLDW==null||obj.DLDW==''){
		return '<div style="text-align:center">—</div>'
	}else{
		return obj.DLDW;
	}
}
function doKBRQ(obj){
	if(obj.KBRQ==null||obj.KBRQ==''){
		return '<div style="text-align:center">—</div>'
	}else{
		return obj.KBRQ;
	}
}
function doZBDW(obj){
	if(obj.ZBDW==null||obj.ZBDW==''){
		return '<div style="text-align:center">—</div>'
	}else{
		return obj.ZBDW;
	}
}
function doZZBJ(obj){
	if(obj.ZZBJ==null||obj.ZZBJ==''){
		return '<div style="text-align:center">—</div>'
	}else{
		return obj.ZZBJ_SV;
	}
}
</script>      
    
</head>
<body>
<app:dialogs/>
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <form method="post" id="queryForm"></form>
     <form method="post" id="xmgyForm">
     <h4 class="title">项目概要信息</h4>
      <table class="B-table c-table" width="100%" id="jcxxList">
      <input type="hidden" id="id_jb" name = "id_jb" fieldname="GC_TCJH_XMXDK_ID"/>
        <tr>
			<th width="5%" class="right-border bottom-border text-right disabledTh">项目名称</th>
			<td class="bottom-border right-border" colspan="3">
			  <span id="XMMC"></span>
			</td>
        </tr>
        <tr>
			<th width="5%" class="right-border bottom-border text-right disabledTh">业主单位</th>
			<td class="right-border bottom-border" colspan="3">
			  	<span id='XMGLGS'></span>
			</td>
        </tr>
        <tr>
			<th width="5%" class="right-border bottom-border text-right disabledTh">业主代表</th>
			<td class="bottom-border right-border">
				<span id='YZDB'></span>
			</td>
			<th width="5%" class="right-border bottom-border text-right disabledTh">联系方式</th>
			<td class="bottom-border right-border">
				<span id='LXFS_YZDB'></span>
			</td>
        </tr>
		<tr>
	        <th width="5%" class="right-border bottom-border text-right disabledTh">已签订（履行中）合同数</th>
	        <td class="right-border bottom-border">
	        	<span id='HTS'></span>
	        </td>
	        <th width="5%" class="right-border bottom-border text-right disabledTh">金额</th>
			<td class="bottom-border right-border">
				<span id='JE'></span>(元)
			</td>
         </tr>
       
        
        
      </table>
      </form>
      <h4 id="cjdw" class="title">设计类合同
      (
      <%
			String multiXmid = "select gc_tcjh_xmxdk_id from GC_TCJH_XMXDK xdk where xdk.xmwybh in " 
					+ "(select xmwybh from GC_TCJH_XMXDK x0 where x0.gc_tcjh_xmxdk_id='"+id+"')";
      		String htzt = " and ght.htzt>0 ";
      		sql = "select count(*) from (select distinct (ghh.id) FROM gc_htgl_ht ght　　left join gc_htgl_htsj ghh on ght.id = ghh.htid 　left "
	      	        +"join gc_jh_sj gjs on ghh.jhsjid = gjs.gc_jh_sj_id 　left "
	      	        +"join GC_TCJH_XMXDK t on t.gc_tcjh_xmxdk_id = gjs.xmid 　left "
	      	        +"join gc_ztb_sj gzs on ght.ztbid = gzs.gc_ztb_sj_id 　left "
	      	        +"join gc_ztb_xqsj_ys gzxs on gzs.GC_ZTB_SJ_ID = gzxs.ztbsjid "
	      	        +"left join gc_ztb_xq gzx on gzxs.ztbxqid = gzx.gc_ztb_xq_id "
     	        	+"left join GC_CJDW gc on gzs.zbdl = gc.gc_cjdw_id where ghh.xmid in ("+multiXmid+") and ght.htlx = 'SJ'" + htzt + ")";
      		results = DBUtil.query(conn, sql);
      		out.print(results[0][0]);
      %>
      )
      </h4>
      			<div class="">
			      <table class="table-hover table-activeTd B-table" id="SJHTList" width="100%" type="single" noPage="true" pageNum="1000">
						<thead>
							<tr>
								<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
								<th fieldname="HTDID" colindex=2 tdalign="center"  CustomFunction="doHQ">&nbsp;会签&nbsp;</th>
								<th fieldname="BDMC" colindex=3 style="width:15%" maxlength="10" CustomFunction="doBD">&nbsp;标段名称&nbsp;</th>
								<th fieldname="HTBM" rowmerge="true" colindex=4 tdalign="left" maxlength="10"  >&nbsp;合同编号&nbsp;</th>
								<th fieldname="HTZT" colindex=5 tdalign="center" maxlength="10">&nbsp;合同状态&nbsp;</th>
								<th fieldname="HTMC" rowmerge="true" colindex=6 tdalign="left" maxlength="27"  >&nbsp;合同名称&nbsp;</th>
								<th fieldname="ZZXHTJ" colindex=7 tdalign="right" maxlength="20">&nbsp;最新合同价(元)&nbsp;</th>
								<th fieldname="YFDW" colindex=8 width="150px;">&nbsp;乙方单位&nbsp;</th>
								<th fieldname="HTSJKSRQ" colindex=9 tdalign="center" width='85px' maxlength="20"  >&nbsp;开始日期&nbsp;</th>
								<th fieldname="HTJSRQ" colindex=10 tdalign="center" width='85px'  maxlength="20"  >&nbsp;结束日期&nbsp;</th>
								<th fieldname="DLDW" colindex=11 tdalign="left" maxlength="10"  CustomFunction="doDLDW">&nbsp;招标代理机构&nbsp;</th>
								<th fieldname="BJXS" colindex=7 tdalign="center" maxlength="10"  >&nbsp;报价&nbsp;<br>&nbsp;系数&nbsp;</th>
							</tr>
						</thead>
			             	<tbody></tbody>
					</table>
					</div>
      
      <h4 id="cjdw" class="title">监理类合同
      (
      <%
      		sql = "select count(*) from (select distinct (ghh.id) FROM gc_htgl_ht ght　　left join gc_htgl_htsj ghh on ght.id = ghh.htid 　left "
	      	        +"join gc_jh_sj gjs on ghh.jhsjid = gjs.gc_jh_sj_id 　left "
	      	        +"join GC_TCJH_XMXDK t on t.gc_tcjh_xmxdk_id = gjs.xmid 　left "
	      	        +"join gc_ztb_sj gzs on ght.ztbid = gzs.gc_ztb_sj_id 　left "
	      	        +"join gc_ztb_xqsj_ys gzxs on gzs.GC_ZTB_SJ_ID = gzxs.ztbsjid "
	      	        +"left join gc_ztb_xq gzx on gzxs.ztbxqid = gzx.gc_ztb_xq_id "
     	        	+"left join GC_CJDW gc on gzs.zbdl = gc.gc_cjdw_id where ghh.xmid in ("+multiXmid+") and ght.htlx = 'JL'" + htzt + ")";
      		results = DBUtil.query(conn, sql);
      		out.print(results[0][0]);
      %>
      )
      </h4>
      <div class="">
      <table class="table-hover table-activeTd B-table" id="JLHTList" width="100%" type="single" noPage="true" pageNum="1000">
			<thead>
			    <tr>
					<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
					<th fieldname="HTDID" colindex=2 tdalign="center"  CustomFunction="doHQ">&nbsp;会签&nbsp;</th>
					<th fieldname="BDMC" colindex=3 style="width:15%" maxlength="10" CustomFunction="doBD">&nbsp;标段名称&nbsp;</th>
					<th fieldname="HTBM" rowmerge="true" colindex=4 tdalign="left" maxlength="10"  >&nbsp;合同编号&nbsp;</th>
					<th fieldname="HTZT" colindex=5 tdalign="center" maxlength="10">&nbsp;合同状态&nbsp;</th>
					<th fieldname="HTMC" rowmerge="true" colindex=6 tdalign="left" maxlength="27"  >&nbsp;合同名称&nbsp;</th>
					<th fieldname="ZZXHTJ" colindex=7 tdalign="right" maxlength="20">&nbsp;最新合同价(元)&nbsp;</th>
					<th fieldname="YFDW" colindex=8 width="150px;">&nbsp;乙方单位&nbsp;</th>
					<th fieldname="HTSJKSRQ" colindex=9 tdalign="center" width='85px' maxlength="20"  >&nbsp;开始日期&nbsp;</th>
					<th fieldname="HTJSRQ" colindex=10 tdalign="center" width='85px'  maxlength="20"  >&nbsp;结束日期&nbsp;</th>
					<th fieldname="DLDW" colindex=11 tdalign="left" maxlength="10"  CustomFunction="doDLDW">&nbsp;招标代理机构&nbsp;</th>
					<th fieldname="BJXS" colindex=7 tdalign="center" maxlength="10"  >&nbsp;报价&nbsp;<br>&nbsp;系数&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
      </div>
        <h4 id="cjdw" class="title">施工类合同
        (
	      <%
	      		sql = "select count(*) from (select distinct (ghh.id) FROM gc_htgl_ht ght　　left join gc_htgl_htsj ghh on ght.id = ghh.htid 　left "
		      	        +"join gc_jh_sj gjs on ghh.jhsjid = gjs.gc_jh_sj_id 　left "
		      	        +"join GC_TCJH_XMXDK t on t.gc_tcjh_xmxdk_id = gjs.xmid 　left "
		      	        +"join gc_ztb_sj gzs on ght.ztbid = gzs.gc_ztb_sj_id 　left "
		      	        +"join gc_ztb_xqsj_ys gzxs on gzs.GC_ZTB_SJ_ID = gzxs.ztbsjid "
		      	        +"left join gc_ztb_xq gzx on gzxs.ztbxqid = gzx.gc_ztb_xq_id "
	     	        	+"left join GC_CJDW gc on gzs.zbdl = gc.gc_cjdw_id where ghh.xmid in ("+multiXmid+") and ght.htlx = 'SG'" + htzt + ")";
	      		results = DBUtil.query(conn, sql);
	      		out.print(results[0][0]);
	      %>
	      )
        </h4>
      <div class="">
      <table class="table-hover table-activeTd B-table" id="SGHTList" width="100%" type="single" noPage="true" pageNum="1000">
			<thead>
			   <tr>
					<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
					<th fieldname="HTDID" colindex=2 tdalign="center"  CustomFunction="doHQ">&nbsp;会签&nbsp;</th>
					<th fieldname="BDMC" colindex=3 style="width:15%" maxlength="10" CustomFunction="doBD">&nbsp;标段名称&nbsp;</th>
					<th fieldname="HTBM" rowmerge="true" colindex=4 tdalign="left" maxlength="10"  >&nbsp;合同编号&nbsp;</th>
					<th fieldname="HTZT" colindex=5 tdalign="center" maxlength="10">&nbsp;合同状态&nbsp;</th>
					<th fieldname="HTMC" rowmerge="true" colindex=6 tdalign="left" maxlength="27"  >&nbsp;合同名称&nbsp;</th>
					<th fieldname="ZZXHTJ" colindex=7 tdalign="right" maxlength="20">&nbsp;最新合同价(元)&nbsp;</th>
					<th fieldname="YFDW" colindex=8 width="150px;">&nbsp;乙方单位&nbsp;</th>
					<th fieldname="HTSJKSRQ" colindex=9 tdalign="center" width='85px' maxlength="20"  >&nbsp;开始日期&nbsp;</th>
					<th fieldname="HTJSRQ" colindex=10 tdalign="center" width='85px'  maxlength="20"  >&nbsp;结束日期&nbsp;</th>
					<th fieldname="DLDW" colindex=11 tdalign="left" maxlength="10"  CustomFunction="doDLDW">&nbsp;招标代理机构&nbsp;</th>
					<th fieldname="BJXS" colindex=7 tdalign="center" maxlength="10"  >&nbsp;报价&nbsp;<br>&nbsp;系数&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
    </div>
      <h4 id="cjdw" class="title">其他合同
      	(
	      <%
	      		sql = "select count(*) from (select distinct (ghh.id) FROM gc_htgl_ht ght　　left join gc_htgl_htsj ghh on ght.id = ghh.htid 　left "
	      	        +"join gc_jh_sj gjs on ghh.jhsjid = gjs.gc_jh_sj_id 　left "
	      	        +"join GC_TCJH_XMXDK t on t.gc_tcjh_xmxdk_id = gjs.xmid 　left "
	      	        +"join gc_ztb_sj gzs on ght.ztbid = gzs.gc_ztb_sj_id 　left "
	      	        +"join gc_ztb_xqsj_ys gzxs on gzs.GC_ZTB_SJ_ID = gzxs.ztbsjid "
	      	        +"left join gc_ztb_xq gzx on gzxs.ztbxqid = gzx.gc_ztb_xq_id "
     	        	+"left join GC_CJDW gc on gzs.zbdl = gc.gc_cjdw_id where t.gc_tcjh_xmxdk_id in ("+multiXmid+") and ght.htlx not in ('SJ','SG','JL')" + htzt + ")";
	      		results = DBUtil.query(conn, sql);
	      		out.print(results[0][0]);
	      %>
	     )
      </h4>
      <div class="">
      <table class="table-hover table-activeTd B-table" id="QTHTList" width="100%" type="single" noPage="true" pageNum="1000">
			<thead>
				<tr>
					<th  name="XH" id="_XH" style="width:10px" colindex=1 tdalign="center">&nbsp;#&nbsp;</th>
					<th fieldname="HTDID" colindex=2 tdalign="center"  CustomFunction="doHQ">&nbsp;会签&nbsp;</th>
					<th fieldname="BDMC" colindex=3 style="width:15%" maxlength="10" CustomFunction="doBD">&nbsp;标段名称&nbsp;</th>
					<th fieldname="HTBM" rowmerge="true" colindex=4 tdalign="left" maxlength="10"  width="150px;">&nbsp;合同编号&nbsp;</th>
					<th fieldname="HTZT" colindex=5 tdalign="center" maxlength="10">&nbsp;合同状态&nbsp;</th>
					<th fieldname="HTMC" rowmerge="true" colindex=6 tdalign="left" maxlength="9"  >&nbsp;合同名称&nbsp;</th>
					<th fieldname="ZZXHTJ" colindex=7 tdalign="right" maxlength="20">&nbsp;最新合同价(元)&nbsp;</th>
					<th fieldname="YFDW" colindex=8 width="150px;">&nbsp;乙方单位&nbsp;</th>
					<th fieldname="HTSJKSRQ" colindex=9 tdalign="center" width='85px' maxlength="20"  >&nbsp;开始日期&nbsp;</th>
					<th fieldname="HTJSRQ" colindex=10 tdalign="center" width='85px'  maxlength="20"  >&nbsp;结束日期&nbsp;</th>
					<th fieldname="DLDW" colindex=11 tdalign="left" maxlength="10"  CustomFunction="doDLDW">&nbsp;招标代理机构&nbsp;</th>
					<th fieldname="BJXS" colindex=7 tdalign="center" maxlength="10"  >&nbsp;报价&nbsp;<br>&nbsp;系数&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
		</div>
    </div>
 </div>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "ght.LRSJ"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>