<!DOCTYPE HTML>
<html lang="en">
  <head>
	<%@ page language="java" pageEncoding="UTF-8"%>
	<%@ taglib uri= "/tld/base.tld" prefix="app" %>
	<%@ page import="com.ccthanking.framework.common.User"%>
    <%@ page import="com.ccthanking.framework.Globals"%>
	<app:base />
	<app:dialogs/>
<%
request.setCharacterEncoding("utf-8");
User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
String department = user.getDepartment();
String account=user.getAccount();
String username = user.getName();
%>		
</head>
<style type="text/css">
table {
	border-left: #ddd solid 1px;
	border-top: #ddd solid 1px;
}
td, th {
	line-height: 1.8em;
	padding: 5px;
	border-right: #ddd solid 1px;
	border-bottom: #ddd solid 1px;
	text-align:center;
}
input[type='text'] {
	vertical-align: middle;
	height: 20px;
	line-height: 16px;
	padding: 2px;
	margin-left:2px;
}
</style>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/qqsx/xmxxkController.do";
	$(function(){
		setPageInfo();
		setPageTimeInfo();
		pageInfoOpera();
	});
	//显示时间：个手续的计划时间与办结时间/实际完成时间
	function setPageTimeInfo(){
		var obj=getValue();
		//规划许可证时间，大分类 
		$("#gcxkz_jh").html(obj.JHGCXKZ_SV);
		$("#1_sj").html(obj.GHSPBJSJ_SV);
		$("#1_time").html(obj.GHSPBJSJ_SV);
		//立项可研时间，大分类：0
		$("#kypf_jh").html(obj.JHKYPF_SV);
		$("#0_sj").html(obj.LXKYBJSJ_SV);
		$("#0_time").html(obj.LXKYBJSJ_SV);
		//土地审批时间，大分类
		$("#hpjds_jh").html(obj.JHHPJDS_SV);
		$("#2_sj").html(obj.TDSPBJSJ_SV);
		$("#2_time").html(obj.TDSPBJSJ_SV);
		//施工许可时间，大分类
		$("#sgxk_jh").html(obj.JHSGXK_SV);
		$("#3_sj").html(obj.SGXKBJSJ_SV);
		$("#3_time").html(obj.SGXKBJSJ_SV);
	}
	//页面信息处理
	function pageInfoOpera(){
		var obj=getValue();
		//立项信息
		var lxbblsx=obj.LXKYBBLSX;
		showPageInfo(lxbblsx,"0",5);
		//规划信息
		var ghbblsx=obj.GHSPBBLSX;
		showPageInfo(ghbblsx,"1",3);
		//土地信息
		var tdbblsx=obj.TDSPBBLSX;
		showPageInfo(tdbblsx,"2",5);
		//施工信息
		var sgbblsx=obj.SGXKBBLSX;
		showPageInfo(sgbblsx,"3",8);
	}
	//dfl：大分类，手续存在问题需要显示的；
	//bblsx:不办理手续，方法传值得来；
	//time：后台查询一次获得，不需要传值
	//count：如果多种小手续都不办理，办结时间与存在问题都不显示
	//no：不办理手续的个数，与count连用
	function showPageInfo(bblsx,dfl,count){
		var jhsjid=$("#JHSJID").val();
		var sjwybh=$("#SJWYBH").val();
		var sxxx=querySxxx();
		var fjxx=queryFjxx();
		var no=0;
		var sxArray=new Array();
		sxArray=bblsx.split(",");
		$("[name="+dfl+"]:checkbox").each(function(){ 
 			var check=true;
 			for(var i=0;i<sxArray.length;i++){
 				if($(this).val() == sxArray[i]){
 					check=false;
 					break;
 					}
 			}
 			var lb=$(this).attr('value');
 			if(check){
 				if(lb==undefined){
 					return;
 				}else{
 					var value=$(sxxx).attr(lb+"_SV");
 					var exist=$(fjxx).attr(lb);
 					if(exist==0){
 						$("#"+lb).html();
 					}else{
 						$("#"+lb).html('<a href="javascript:void(0);" onclick="showAllFilesWithWybh(\''+sjwybh+'\',\''+lb+'\')"><img src="/xmgl/images/icon-annex.png" title="点击查看附件"></a>');
 					}
					//$("#"+$(this).attr('value')).html('<a href="javascript:void(0);" onclick="showAllFiles(\''+jhsjid+'\',\''+$(this).attr('value')+'\')"><img src="/xmgl/images/icon-annex.png" title="点击查看附件"></a>');
					$("#"+lb+"_SV").html(value);
 				}
 			}else{
				no+=1;
 				$("#"+lb).html('—');
 				$("#"+lb+"_SV").html('—');
 			}
 		});
		$("#"+dfl).html($(sxxx).attr(dfl));
		if(no==count){
			$("#"+dfl+"_sj").html("—");
			$("#"+dfl+"_time").html("—");
			$("#"+dfl).html("—");
		}
	}
	
	//获取父页面行选值
	function getValue(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		var obj = convertJson.string2json1(rowValue);
		return obj;
	}
	//查询手续信息
	function querySxxx(){
		var jhsjid=$("#JHSJID").val();
		var sjwybh=$("#SJWYBH").val();
		var res = "";
		$.ajax({
			url:controllername + "?querySxxx&sjwybh="+sjwybh,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				res = result.msg;
			}
		});
		var tempJson = convertJson.string2json1(res);
		var str = tempJson.response.data[0];
		return str;
	}
	//查询附件信息
	function queryFjxx(){
		var sjwybh=$("#SJWYBH").val();
		var res = "";
		$.ajax({
			url:controllername + "?exist&sjwybh="+sjwybh,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				res = result.msg;
			}
		});
		var tempJson = convertJson.string2json1(res);
		var str = tempJson.response.data[0];
		return str;
	}
	//页面赋值
	function setPageInfo(){
		var obj=getValue();
		setValueByArr(obj,["XMMC","JBDW","JJSJ","JJCLMX","JER","JHSJID","SJWYBH"]);
		jhsjid=$("#JHSJID").val();
	}
</script>
<body>
<app:dialogs />
<div class="container-fluid">
	<div class="row-fluid">
		<div class="B-small-from-table-autoConcise" style="width:100%">
			<h4 class="title">统筹计划信息
				<!-- <span class="pull-right">
					<button id="btnExp" class="btn" type="button"> 导出 </button>
				</span> -->
			</h4>
			<form method="post" action="" id="showForm">
				<table class="B-table" width="100%">
					<TR style="display:none">
						<td><input class="span12" type="checkbox" kind="dic" src="LXKYFJLX" name ="0" >
						</td>
						<td><input class="span12" type="checkbox" kind="dic" src=GHSX name ="1" >
						</td>
						<td><input class="span12" type="checkbox" kind="dic" src="TDSPSX" name ="2" >
						</td>
						<td><input class="span12" type="checkbox" kind="dic" src="SGXKSX" name ="3" >
						</td>
					</TR>
					<tr style="display:none">
						<td><input class="span12" type="text" name="JHSJID" id="JHSJID" keep="true">
						</td>
						<td><input class="span12" type="text" name="SJWYBH" id="SJWYBH" keep="true">
						</td>
					</tr>
					<tr>
						<th width="10%" class="right-border bottom-border disabledTh">项目名称</th>
						<td width="90%" class="right-border bottom-border" colspan=9 style="text-align:left;">
							<input class="span12" type="text" name="XMMC" id="XMMC" disabled style="width:40%">
						</td>
					</tr>
					<tr>
						<th width="10%" class="right-border bottom-border disabledTh">交办单位</th>
						<td width="30%" class="right-border bottom-border" colspan=3>
							<input class="span12" type="text" name="JBDW" id="JBDW" disabled>
						</td>
						<th width="10%" class="right-border bottom-border disabledTh">交接时间</th>
						<td width="20%" class="right-border bottom-border" colspan=2>
							<input class="span12" type="text" name="JJSJ" id="JJSJ" disabled>
						</td>
						<th width="10%" class="right-border bottom-border disabledTh">交接人</th>
						<td width="20%" class="right-border bottom-border" colspan=2>
							<input class="span12" type="text" name="JER" id="JER" disabled>
						</td>
					</tr>
					<tr>
						<th width="10%" class="right-border bottom-border disabledTh">交接材料说明</th>
						<td width="90%" class="right-border bottom-border" colspan=9>
							<textarea rowsapn=3 class="span12" type="text" name="JJCLMX" id="JJCLMX" disabled></textarea>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display: none"
		target="_blank">
		系统保留定义区域
		<input type="hidden" name="queryXML" id="queryXML">
		<input type="hidden" name="txtXML" id="txtXML">
		<input type="hidden" name="resultXML" id="resultXML">
		<input type="hidden" name="queryResult" id="queryResult">
		传递行数据用的隐藏域
		<input type="hidden" name="rowData">
	</FORM>
</div>
<h4>统筹计划完成情况</h4>
<table width="100%" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td width="10%"></td>
        <td width="20%">计划完成</td>
        <td width="20%">实际完成</td>
        <td width="10%"></td>
        <td width="20%">计划完成</td>
        <td width="20%">实际完成</td>
    </tr>
    <tr>
        <td >可研批复</td>
        <td id="kypf_jh"></td>
        <td id="0_sj"></td>
        <td >规划审批</td>
        <td id="gcxkz_jh"></td>
        <td id="1_sj"></td>
    </tr>
    <tr>
        <td >土地审批</td>
        <td id="hpjds_jh"></td>
        <td id="2_sj"></td>
        <td >施工许可</td>
        <td id="sgxk_jh"></td>
        <td id="3_sj"></td>
    </tr>
</table>
<h4>立项可研进展</h4>
<table width="100%" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td width="20%"></td>
        <td width="10%">证照</td>
        <td width="10%">反馈日期</td>
        <td width="10%">办结时间</td>
        <td width="50%">存在问题</td>
    </tr>
    <tr>
        <td >项目建议书批复</td>
        <td id="2020"></td>
        <td id="2020_SV"></td>
        <td id="0_time" rowspan='5'></td>
        <td id="0" rowspan='5' style="text-align:left;overflow :hidden;"></td>
    </tr>
    <tr>
        <td >环评批复</td>
        <td id="2021"></td>
        <td id="2021_SV"></td>
    </tr>
    <tr>
        <td >土地意见函</td>
        <td id="2022"></td>
        <td id="2022_SV"></td>
    </tr>
    <tr>
        <td >固定资产投资项目节能审查</td>
        <td id="2023"></td>
        <td id="2023_SV"></td>
    </tr>
    <tr>
        <td >可研批复</td>
        <td id="2024"></td>
        <td id="2024_SV"></td>
    </tr>
</table>

<h4>土地审批进展</h4>
<table width="100%" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td width="20%"></td>
        <td width="10%">证照</td>
        <td width="10%">反馈日期</td>
        <td width="10%">办结时间</td>
        <td width="50%">存在问题</td>
    </tr>
    <tr>
        <td >用地预审</td>
        <td id="0010"></td>
        <td id="0010_SV"></td>
        <td id="2_time" rowspan='8'></td>
        <td id="2" rowspan='8' style="text-align:left;overflow :hidden;"></td>
    </tr>
    <tr>
        <td >集体土地征地</td>
        <td id="0011"></td>
        <td id="0011_SV"></td>
    </tr>
    <tr>
        <td >供地手续</td>
        <td id="0012"></td>
        <td id="0012_SV"></td>
    </tr>
    <tr>
        <td >土地登记</td>
        <td id="0013"></td>
        <td id="0013_SV"></td>
    </tr>
    <tr>
        <td >土地使用证</td>
        <td id="0014"></td>
        <td id="0014_SV"></td>
    </tr>
    <tr>
        <td >建设用地批准书</td>
        <td id="0023"></td>
        <td id="0023_SV"></td>
    </tr>
    <tr>
        <td >征地批复</td>
        <td id="0024"></td>
        <td id="0024_SV"></td>
    </tr>
    <tr>
        <td >划拨决定书</td>
        <td id="0025"></td>
        <td id="0025_SV"></td>
    </tr>
</table>

<h4>规划审批进展</h4>
<table width="100%" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td width="20%"></td>
        <td width="10%">证照</td>
        <td width="10%">反馈日期</td>
        <td width="10%">办结时间</td>
        <td width="50%">存在问题</td>
    </tr>
    <tr>
        <td >建设选址项目意见书</td>
        <td id="0007"></td>
        <td id="0007_SV"></td>
        <td id="1_time" rowspan='3'></td>
        <td id="1" rowspan='3' style="text-align:left;overflow :hidden;"></td>
    </tr>
    <tr>
        <td >建设用地规划许可证</td>
        <td id="0008"></td>
        <td id="0008_SV"></td>
    </tr>
    <tr>
        <td >建设工程规划许可证</td>
        <td id="0009"></td>
        <td id="0009_SV"></td>
    </tr>
</table>
	
<h4>施工许可进展</h4>
<table width="100%" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td width="20%"></td>
        <td width="10%">证照</td>
        <td width="10%">反馈日期</td>
        <td width="10%">办结时间</td>
        <td width="50%">存在问题</td>
    </tr>
    <tr>
        <td >报建</td>
        <td id="0015"></td>
        <td id="0015_SV"></td>
        <td id="3_time" rowspan='8'></td>
        <td id="3" rowspan='8' style="text-align:left;overflow :hidden;"></td>
    </tr>
    <tr>
        <td >其他手续</td>
        <td id="0016"></td>
        <td id="0016_SV"></td>
    </tr>
    <tr>
        <td >质量监督</td>
        <td id="0017"></td>
        <td id="0017_SV"></td>
    </tr>
    <tr>
        <td >安全监督</td>
        <td id="0018"></td>
        <td id="0018_SV"></td>
    </tr>
    <tr>
        <td >造价管理</td>
        <td id="0019"></td>
        <td id="0019_SV"></td>
    </tr>
    <tr>
        <td >双拖欠</td>
        <td id="0020"></td>
        <td id="0020_SV"></td>
    </tr>
    <tr>
        <td >执法监察</td>
        <td id="0021"></td>
        <td id="0021_SV"></td>
    </tr>
    <tr>
        <td >施工许可</td>
        <td id="0022"></td>
        <td id="0022_SV"></td>
    </tr>
</table>	
</body>
</html>
