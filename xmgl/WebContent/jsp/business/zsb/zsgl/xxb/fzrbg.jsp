<!DOCTYPE HTML>
<html lang="en">
  <head>
	<%@ page language="java" pageEncoding="UTF-8"%>
	<%@ taglib uri= "/tld/base.tld" prefix="app" %>
	<%@ page import="com.ccthanking.framework.common.User"%>
    <%@ page import="com.ccthanking.framework.Globals"%>
	<%@ page import="com.ccthanking.framework.common.Role"%>
	<%	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY); 
		String role = user.getRoleListString();
		String leader = "0";
		if(!"".equals(role)&&(role.indexOf("部长")!=-1 || role.indexOf("管理员")!=-1 || "superman".equals(user.getAccount()))){
			leader="1";//等于1表示是领导
		}
		String account=user.getAccount();
		String department=user.getDepartment();
	%>

	<app:base />
	<app:dialogs/>
	<style type="text/css">
		.myUl li p{
			font-size: 15px;
		}
		blockquote{
			margin-bottom:0px;
		}
	</style>
	<script src="${pageContext.request.contextPath }/js/common/FixTable.js"></script> 
	<script type="text/javascript" charset="utf-8">
	 var controllername= "${pageContext.request.contextPath }/zsb/xxb/xxbController.do";
		//获取父页面的值
		function do_value(){
			var pwindow =$(window).manhuaDialog.getParentObj();
			var rowValue = pwindow.$("#DT1").getSelectedRow();
			var obj = convertJson.string2json1(rowValue);
			return obj;
		}
		//填写表单
		function setvalue(){
			var value=do_value();
			$("#queryForm1").setFormValues(value);
			var xxbid=$(value).attr("GC_ZSB_XXB_ID");
			if(undefined!=xxbid&&''!=xxbid){
				$("#tabPage0").tab("show");
			}else{
				/* $("#XZXM").html('请先选择项目'); */
				$("#tabPage1").tab("show");
				$("#Updatesave").attr("disabled",true);
				$("#FZR2").attr("disabled",true);
			}
			
		}
		$(function() {
			init();
		});
		function init(){
			 setvalue();
			 //根据ID修改
			var savebtn=$("#Updatesave");
			savebtn.click(function(){
			if($("#queryForm1").validationButton()){
				//生成json串
				var data = Form2Json.formToJSON(queryForm1);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				var yfzr1=$("#YFZR").val();
				defaultJson.doUpdateJson(controllername+"?updateXXB&yfzr="+yfzr1,data1,null,"eidtHuiDiao");
			}
			});
			 //根据负责人修改
			var save=$("#save");
			save.click(function(){
			if($("#queryForm2").validationButton()){
				//生成json串
				var data = Form2Json.formToJSON(queryForm2);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				var yfzr1=$("#YFZR").val();
				defaultJson.doUpdateJson(controllername+"?updateXXB&yfzr="+yfzr1,data1,null,"callback");
			}
			});
		}
	//异步操作
	function callback(){
	//从resultXML获取修改的数据
	var fuyemian=$(window).manhuaDialog.getParentObj();
	fuyemian.init(); 
	$(window).manhuaDialog.close();
	}
	function eidtHuiDiao()
	{
		//从resultXML获取修改的数据
		var data2 = $("#resultXML").val();
		//返回数据
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.xgh(data2);
		$(window).manhuaDialog.close();
	}
  	</script>
  </head>
	<body id="a">
		<app:dialogs />
		<div class="row-fluid">
			<ul class="nav nav-tabs">
				<li class="active">
					<a href="#tab0" data-toggle="tab" id="tabPage0">按任务变更&nbsp;</a>
				</li>
				<li class="">
					<a href="#tab1" data-toggle="tab" id="tabPage1">按负责人变更</a>
				</li>
			</ul>
<div class="tab-content active">
<div class="tab-pane active" id="tab0">
<div class="span12">
<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
	<h4 class="title">征收任务信息
	         <font id="XZXM" style="color: red" size="3px"></font>
			<span class="pull-right">
         	 <button id="Updatesave" name="save" class="btn"  type="button">保存</button>
          	</span>
    </h4>
		<form method="post" action="${pageContext.request.contextPath }/insertdemo.do"  id="queryForm1">
	    <table class="B-table" width="100%">
	       <TR  style="display: none">
	       <!-- <TR> -->
				<TD><input type="text" class="span12" kind="text" id="XMID" fieldname="XMID" name="XMID" ></TD>
				<TD><input type="text" class="span12" kind="text" id="ND" fieldname="ND" name="ND" ></TD>
				<TD><input type="text" class="span12" kind="text" id="JHID" fieldname="JHID" name="JHID" ></TD>
				<TD><input type="text" class="span12" kind="text" id="JHSJID" fieldname="JHSJID" name="JHSJID" ></TD>
				<TD><input type="text" class="span12" kind="text" id="ZSXMID" fieldname="ZSXMID" name="ZSXMID" ></TD>
	        	<td><input type="text" name="gc_zsb_xxb_id" fieldname="GC_ZSB_XXB_ID" id="gc_zsb_xxb_id"/></td>
	        </TR>
			<tr>
				<th width="10%" colspan="1" class="right-border bottom-border text-right disabledTh">项目名称</th>
				<td width="60%" colspan="6" class="right-border bottom-border">
					<input class="span12" type="text" style="width:100%" data-toggle="modal" keep="true" placeholder="请选择项目" check-type="required" disabled id="XMMC" name="XMMC" fieldname="XMMC"/>
				</td>
				<th width="10%" colspan="1" class="right-border bottom-border text-right disabledTh">拆迁计划完成时间</th>
				<td width="20%" colspan="1" class="right-border bottom-border">
				<input class="span12" type="text" disabled fieldname="CQJHWCSJ" id="CQJHWCSJ" name="JHWCSJ"></td>
			</tr>
			<tr>
				<th width="10%" colspan="1" class="right-border bottom-border text-right disabledTh">区域选择</th>
				<td width="30%" colspan="3" class="right-border bottom-border">
					<!-- <select class="span12" id="S_QY" name="QY" check-type="required" disabled fieldname="QY" kind="dic" src="QY">
					</select> -->
					<input class="span12" type="text" disabled fieldname="QY" id="S_QY" name="QY">
				</td>
				<th width="10%" colspan="1" class="right-border bottom-border text-right disabledTh">责任单位</th>
				<td width="50%" colspan="4" class="right-border bottom-border"><input class="span12" type="text" disabled fieldname="ZRDW" name="ZRDW" placeholder="必填" check-type="required maxlength" maxlength="200"></td>	
			</tr>
			<tr>
				<th width="10%" class="right-border bottom-border text-right ">负责人</th>
				<td width="30%" colspan="3" class="right-border bottom-border">
					<select class="span12 " kind="dic" id="FZR2" name = "FZR" fieldname= "FZR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1' order by sort" check-type="required"></select>
				</td>
				<td  colspan="5"></td>
			</tr>
		</table>
			</form>
				</div>
			</div>
	</div>
</div>
<div class="tab-pane" id="tab1">
<div class="span12">
<div class="row-fluid">
<div class="B-small-from-table-autoConcise">
 	<h4 class="title">征收负责人信息<span class="pull-right">
          <button id="save" name="save" class="btn"  type="button">保存</button>
          </span>
    </h4>
	<form method="post" action="${pageContext.request.contextPath }/insertdemo.do"	id="queryForm2">
	  <table class="B-table" width="100%">
			<tr>
				<th width="10%" class="right-border bottom-border text-right">原负责人</th>
				<td width="30%" colspan="1" class="right-border bottom-border">
					<select class="span12 " kind="dic" id="YFZR" name = "YFZR" fieldname= "YFZR" src="T#fs_org_person:ACCOUNT:NAME:ACCOUNT in (select fzr from GC_ZSB_XXB )  AND PERSON_KIND = '3' AND FLAG='1' order by sort" check-type="required"></select>
				</td>
				<th width="10%" class="right-border bottom-border text-right">现负责人</th>
				<td width="30%" colspan="1" class="right-border bottom-border">
					<select class="span12 " kind="dic" id="FZR" name = "FZR" fieldname= "FZR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1' order by sort" check-type="required"></select>
				</td>
				<td width="20%"> </td>
			</tr>
		</table>
		</div>
	</div>
</div>
</div>
</div>
</div>
		<div align="center">
			<FORM name="frmPost" method="post"  style="display: none"  id ="frmPost"  target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="ywid" id="ywid">
				<input type="hidden" name="txtFilter" order="desc" fieldname="SJZT,LRSJ"
					id="txtFilter">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="queryResult" id = "queryResult">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
	</body>
</html>
