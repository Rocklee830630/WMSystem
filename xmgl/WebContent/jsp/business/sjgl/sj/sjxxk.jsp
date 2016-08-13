<!DOCTYPE html>
<html>
<head>
		<%@ page language="java" pageEncoding="UTF-8"%>
		<%@ page import="com.ccthanking.framework.common.User"%>
		<%@ page import="com.ccthanking.framework.Globals"%>
		<%@ taglib uri="/tld/base.tld" prefix="app"%>
	<%
	String jhsjid = request.getParameter("jhsjid");
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String username = user.getName();
	String department = user.getDepartment();
%>
	<app:base />
</head>
	
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/sjgl/sj/sjxxkController.do";
var bdid,xmid,jhsjid,department,nd,sjwybh;
//统计概况
$(function() {
	var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#DT1").getSelectedRow();
	var odd=convertJson.string2json1(rowValue);
	   bdid=odd.BDID;
	   xmid=odd.XMID;
	   nd=odd.ND;
	   sjwybh=odd.SJWYBH;
	 
	 department='<%=department%>';
	var action1 = controllername + "?xiangMuXinXi&sjwybh="+sjwybh+"&nd="+nd;
	$.ajax({
		url : action1,
		async:false,
		success: function(result)
		{
			var resultmsgobj = convertJson.string2json1(result);
			var resultobj = convertJson.string2json1(resultmsgobj.msg);
			var subresultmsgobj = resultobj.response.data[0];
			 $("#showForm").setFormValues(subresultmsgobj);
		}
	});
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryTongCJH&sjwybh="+sjwybh+"&nd="+nd,data,DT2,"jhwcqk",true);
});
function jhwcqk()
{
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryJiHuaWanCheng&sjwybh="+sjwybh+"&nd="+nd,data,DT3,"jcjc",true);
}

function jcjc()
{
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryJianCeJianCe&sjwybh="+sjwybh+"&nd="+nd,data,DT4,"jjg",true);
}
function jjg()
{
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryJiaoJunGong&sjwybh="+sjwybh+"&nd="+nd,data,DT5,"htxx",true);
}
function htxx()
{
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?queryHeTongXinXi&sjwybh="+sjwybh+"&nd="+nd+"&department="+department+"",data,DT6,null,true);
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
function doBg(obj){
	  var bg=obj.BG;
	  bg=bg?bg:('—');
	  return bg;
}
//交工时间判断
function queryJGSJ(obj)
	{
	    if(obj.JGYSSJ!="")
	    	{
				if(obj.JGYSRQ == "")
				{
					return '—';
				}
	    	}
	}
</script>
	<body>
		<app:dialogs />
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="B-small-from-table-autoConcise">
					<div class="B-small-from-table-autoConcise" style="width:100%">
						<h4 class="title">
							项目信息
						</h4>
					  <form method="post" action="" id="queryForm"  >
					      <table class="B-table" width="100%">
					      <!--可以再此处加入hidden域作为过滤条件 -->
					      	<TR  style="display:none;">
								<TD class="right-border bottom-border">
								<INPUT id="num" type="text" class="span12" keep="true" kind="text" fieldname="rownum" value="1000" operation="<="/>
								</TD>
					        </TR>
					      </table>
					     </form>
						<form method="post" action="" id="showForm">
							<table class="B-table" id="DT1" width="70%">
								<!-- 这里需要一个隐藏域，存放比如：问题编号,接收人账号，接收单位等信息 -->
								<tr>
									<th width="8%" class="right-border bottom-border disabledTh ">
										项目名称
									</th>
									<td width="17%" colspan="7" class="right-border bottom-border" >
										<input  disabled class="span12" type="text" name="XMMC" fieldname="XMMC" >
									</td>
								</tr>
								<tr>
									<th width="8%" class="right-border bottom-border disabledTh">
										设计招标时间
								   	</th>
									<td width="17%"  class="right-border bottom-border" >
									<input class="span12" disabled type="text" name="KBRQ" fieldname="KBRQ" >
									</td>
									<th width="8%" class="right-border bottom-border disabledTh">设计单位</th>
									<td class="right-border bottom-border" width="17%">
										<input class="span12"  disabled type="text" name="SJDW" fieldname="SJDW" >
									</td>
								</tr>
								<tr>
									<th width="8%" class="right-border bottom-border disabledTh">设计负责人</th>
									<td class="right-border bottom-border" width="17%">
										<input class="span12" disabled type="text" name="FZR_SJDW" fieldname="FZR_SJDW" >
									</td>
									<th width="8%" class="right-border bottom-border disabledTh">联系方式</th>
									<td class="right-border bottom-border" width="17%">
										<input class="span12" disabled type="text" name="LXFS_SJDW" fieldname="LXFS_SJDW" >
									</td>
								</tr>
							</table>
						</form>
					</div>
		<div>
			<h4 class="title">
				统筹计划完成情况
			</h4>
		</div>
		  <div class="overFlowX">
						<table class="table-hover table-activeTd B-table" id="DT2" width="70%" type="single" pageNum="1000" noPage="true">
							<thead>
								<tr>
									<th fieldname="MC" tdalign ="center" id="MC" >
										&nbsp;#&nbsp;
									</th>
									<th  fieldname="WCSJ" tdalign ="center">	&nbsp;计划完成&nbsp;
									</th>
									<th fieldname="SJSJ"  tdalign ="center">&nbsp;实际完成&nbsp;
									</th>
									<th fieldname="MC1" tdalign ="center" id="MC1" >&nbsp;#&nbsp;
									</th>
									<th  fieldname="WCSJ1" tdalign ="center">&nbsp;计划完成&nbsp;
									</th>
									<th fieldname="SJSJ1"  tdalign ="center">&nbsp;实际完成&nbsp;
									</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						</div>
		<div>
			<h4 class="title">
				设计完成情况
			</h4>
		</div>
		<div class="overFlowX">
			<table class="table-hover table-activeTd B-table" id="DT3" width="100%" type="single"  noPage="true" pageNum="5">
							<thead>
				<tr>
					<th name="XH" id="_XH" rowspan="2" colindex=1>
						&nbsp;#&nbsp;
					</th>
					<th fieldname="BDMC" maxlength="15" rowspan="2" colindex=2 Customfunction="doBdmc">
						&nbsp;标段名称&nbsp;
					</th>
					<th colspan="2">
						&nbsp;概算&nbsp;
					</th>
					<th >&nbsp;拆迁图&nbsp;
					</th>
					<th >&nbsp;排迁图&nbsp;
					</th>
					<th colspan="2">&nbsp;施工图&nbsp;
					</th>
					<th fieldname="BG" tdalign="center" rowspan="2" colindex=9 CustomFunction="doBg">&nbsp;变更&nbsp;
					</th>
				</tr>
				<tr>
					<th fieldname="CBSJPF" colindex=3 tdalign="center">&nbsp;完成时间&nbsp;</th>
					<th fieldname="GS" colindex=4 tdalign="right">&nbsp;概算&nbsp;</th>
					<th fieldname="WCSJ_CQT" colindex=5 tdalign="center">&nbsp;时间&nbsp;</th>
					<th fieldname="WCSJ_PQT" colindex=6 tdalign="center">&nbsp;时间&nbsp;</th>
					<th fieldname="WCSJ_SGT_SSB" tdalign="center" colindex=7>&nbsp;送审版&nbsp;</th>
					<th fieldname="WCSJ_SGT_ZSB" colindex=8 tdalign="center">&nbsp;正式版&nbsp;</th>
				</tr>
				</thead>
				<tbody>
				</tbody>
				</table>
			</div>
		<div>
			<h4 class="title">
				监测检测
			</h4>
		</div>
		<div class="overFlowX">
			<table class="table-hover table-activeTd B-table" id="DT4" width="100%" type="single"  pageNum="5">
			<thead>
				<tr>
					<th name="XH" id="_XH" rowspan="2" colindex=1 width="3%">
						&nbsp;#&nbsp;
					</th>
					<th fieldname="BDMC" maxlength="15" rowspan="2" colindex=2 width="15%">
						&nbsp;标段名称&nbsp;
					</th>
		            <th colspan="2">&nbsp;桩基检测&nbsp;</th>
		            <th colspan="2">&nbsp;焊缝探伤检测&nbsp;</th>
		            <th colspan="2">&nbsp;动静载试验检测&nbsp;</th>
	            </tr>
	            <tr>
		            <th fieldname="ZJJC" colindex=3 tdalign="center" noprint="true">&nbsp;检测信息&nbsp;</th>
		            <th fieldname="ZJJCRQ" colindex=4 tdalign="center" >&nbsp;接收日期&nbsp;</th>
		            <th fieldname="HFTSJC" colindex=5 tdalign="center" noprint="true">&nbsp;检测信息&nbsp;</th>
		            <th fieldname="HFTSJCRQ" colindex=6 tdalign="center" >&nbsp;接收日期&nbsp;</th>
		            <th fieldname="DJZSYJC" colindex=7 tdalign="center" noprint="true">&nbsp;检测信息&nbsp;</th>
		            <th fieldname="DJZSYJCRQ" colindex=8 tdalign="center" >&nbsp;接收日期&nbsp;</th>
				</tr>
				</thead>
				<tbody>
				</tbody>
				</table>
			</div>
			<div>
			<h4 class="title">
				交工竣工
			</h4>
		</div>
		<div class="overFlowX">
			<table class="table-hover table-activeTd B-table" id="DT5" width="100%" type="single" noPage="true" pageNum="1000">
			<thead>
				    <tr>
	                     	<th id="_XH" name="XH" rowspan="2" tdalign="center" rowMerge="true" colindex=1>&nbsp;#&nbsp;</th>	
							<th fieldname="BDMC" rowspan="2" colindex=2    maxlength="15">&nbsp;标段名称&nbsp;</th>
							<th fieldname="XMGLGS" rowspan="2" colindex=3  >&nbsp;项目管理公司&nbsp;</th>
							<th fieldname="WGRQ" rowspan="2" colindex=4 tdalign="center">&nbsp;完工时间&nbsp;</th>
							<th  colspan="3"  >&nbsp;交工&nbsp;</th>
			                <th  colspan="3"  >&nbsp;竣工&nbsp;</th>
			               </tr>
			                <tr>
			                <th fieldname="JGYSRQ"  colindex=5 tdalign="center" CustomFunction="queryJGSJ" >&nbsp;交工时间&nbsp;</th>
							<th fieldname="JIAOGJSDW" colindex=6 maxlength="10">&nbsp;接收单位&nbsp;</th>
							<th fieldname="JIAOGJSR"  colindex=7 maxlength="10">&nbsp;接收人&nbsp;</th>
							<th fieldname="JGYSSJ"  colindex=8 tdalign="center" >&nbsp;竣工时间&nbsp;</th>
							<th fieldname="JUNGJSDW" colindex=9 maxlength="10">&nbsp;参加单位&nbsp;</th>
							<th fieldname="JUNGJSR"  colindex=10 maxlength="10">&nbsp;参加人&nbsp;</th>
					</tr>
			</thead>
				<tbody>
				</tbody>
				</table>
			</div>
			
			<div>
			<h4 class="title">
				部门合同
			</h4>
		</div>
		<div class="overFlowX">
			<table class="table-hover table-activeTd B-table" id="DT6" width="100%" type="single"  pageNum="5">
			<thead>
				<tr>
					<th name="XH" id="_XH"  >&nbsp;#&nbsp;
					<th fieldname="HTBM"  >&nbsp;合同编号&nbsp;</th>
					<th  fieldname="HTMC" maxlength="15">&nbsp;合同名称&nbsp;</th>
					<th  fieldname="HTLX"  maxlength="15">&nbsp;合同类型&nbsp;</th>
					<th  fieldname="HTZT"  maxlength="15">&nbsp;合同状态&nbsp;</th>
					<th  fieldname="YFDW"  maxlength="15">&nbsp;乙方单位&nbsp;</th>
					<th  fieldname="HTQDJ"  tdalign="right"  maxlength="15">&nbsp;合同额&nbsp;</th>
					<th  fieldname="ZFJE"  tdalign="right" maxlength="15">&nbsp;已支付&nbsp;</th>
					<th  fieldname="ZFL"  tdalign="right" maxlength="15">&nbsp;支付率<P>(%)&nbsp;</th>
					<th  fieldname="WZF"  tdalign="right" maxlength="15">&nbsp;未支付&nbsp;</th>
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
					<input type="hidden" name="txtFilter" order="asc" fieldname="jhsj.xmbh, jhsj.xmbs, jhsj.pxh"
						id="txtFilter">
					<input type="hidden" name="resultXML" id="resultXML">
					<input type="hidden" name="queryResult" id="queryResult">
					<!--传递行数据用的隐藏域-->
					<input type="hidden" name="rowData">
				</FORM>
			</div>
	</body>
</html>
