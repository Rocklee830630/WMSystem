<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%
	User user=(User) request.getSession().getAttribute(Globals.USER_KEY);
	String account=user.getAccount();
	String department=user.getDepartment();
%>
<app:base />
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/zsb/xxb/xxbController.do";
	var qy="";			//区域字段
	$(function() {
		defPerson();
		var savebtn=$("#save");
		savebtn.click(function(){
		if($("#XmbForm").validationButton()){
			//生成json串
			var data = Form2Json.formToJSON(XmbForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			defaultJson.doInsertJson(controllername+"?insertXxb",data1,null,"callback");
			/* hang=0;
			
			var fuyemian=$(window).manhuaDialog.getParentObj();
			fuyemian.ready(hang);
			$(window).manhuaDialog.close(); */
				//更新主页面记录
				/* var fuyemian=parent.$("body").manhuaDialog.getParentObj();
				fuyemian.updateTable($("#xmwhList")); */
		}else{
	  		defaultJson.clearTxtXML();
			}
		});
			});
	//异步操作
	function callback(){
		hang=0;
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.ready(hang);
		$(window).manhuaDialog.close();
	}
	//弹出项目列表
	function xzxm() {
		$(window).manhuaDialog({"title" : "项目信息","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xxb/xmcx.jsp","modal":"2"});
	}
	
	//默认字典项
	function getSrc(){
		generateSrc($("#S_QY")); 
	}
	/**
	 * 查询项目区域
	 */
	function generateSrc(qyObj){
		init();
		if(qy==null||qy==""){
			qyObj.attr("src","QY");
			qy="";
		}else{
			qyObj.attr("src","T#fs_dic_tree:dic_code:dic_value:parent_id='1000000000243' and dic_code in("+qy+")");
			//qyObj.attr("kind","dic");
			qyObj.html('');
			reloadSelectTableDic(qyObj);
			qy="";
		}
	}
	//将查询出的结果转化成''引起的字段
	function init(){
		var x_qy=$("#QY").val();
		if(x_qy==null||x_qy==""){
			return qy;
		}else{
		var qyArray = new Array();
		qyArray = x_qy.split(",");
			for(var i=0;i<qyArray.length;i++){
				if(i==qyArray.length-1){
					qy=qy+"'"+qyArray[i]+"'";
				}else{
					qy=qy+"'"+qyArray[i]+"',";	
				}
			}
		}
}
	function defPerson(){
		var userid='<%=account%>';
    	setDefaultOption($("#FZR"),userid);
	}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">征收任务信息<span class="pull-right">
          <button id="save" name="save" class="btn"  type="button">保存</button>
          </span></h4>
     <form method="post" id="XmbForm"  >
      <table class="B-table" width="100%">
       <TR  style="display:none;">
       <!-- <TR> -->
			<TD><input type="text" class="span12" kind="text" id="QY" name="X_QY"></TD>
       		<!-- <TD><input type="text" class="span12" kind="text" id="gc_zsb_xxb_id" fieldname="gc_zsb_xxb_id" name="gc_zsb_xxb_id" ></TD> -->
			<TD><input type="text" class="span12" kind="text" id="XMID" fieldname="XMID" name="XMID" ></TD>
			<TD><input type="text" class="span12" kind="text" id="ND" fieldname="ND" name="ND" ></TD>
			<TD><input type="text" class="span12" kind="text" id="JHID" fieldname="JHID" name="JHID" ></TD>
			<TD><input type="text" class="span12" kind="text" id="JHSJID" fieldname="JHSJID" name="JHSJID" ></TD>
			<TD><input type="text" class="span12" kind="text" id="ZSXMID" fieldname="ZSXMID" name="ZSXMID" ></TD>
        </TR>
			<tr>
				<th width="10%" colspan="1" class="right-border bottom-border text-right disabledTh">项目名称</th>
				<td width="60%" colspan="6" class="right-border bottom-border">
					<input class="span12" type="text" style="width:90%" data-toggle="modal" keep="true" placeholder="必填" check-type="required" disabled id="XMMC" name="XMMC" fieldname="XMMC"/>
						<a href="javascript:void(0)"><i width="10%" class="icon-edit" title="请选择项目" onclick="xzxm();"></i></a>	
				</td>
				<th width="10%" colspan="1" class="right-border bottom-border text-right disabledTh">拆迁计划完成时间</th>
				<td width="20%" colspan="1" class="right-border bottom-border"><input class="span12" type="text" disabled fieldname="CQJHWCSJ" id="CQJHWCSJ" name="JHWCSJ"></td>
			</tr>
			<tr>
				<th width="10%" colspan="1" class="right-border bottom-border text-right">区域选择</th>
				<td width="20%" colspan="2" class="right-border bottom-border">
					<select class="span12" id="S_QY" name="QY" check-type="required" fieldname="QY" kind="dic" src="QY">
					<!-- <select class="span12" id="S_QY" name="QY" fieldname="QY"> -->
					</select>
				</td>
				<th width="10%" class="right-border bottom-border text-right">负责人</th>
				<td width="10%" colspan="1" class="right-border bottom-border">
					<select class="span12 person" kind="dic" id="FZR" name = "FZR" fieldname= "FZR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1' order by sort" check-type="required"></select>
				</td>
				<th width="10%" colspan="1" class="right-border bottom-border text-right">责任单位</th>
				<td width="40%" colspan="4" class="right-border bottom-border"><input class="span12" type="text" fieldname="ZRDW" name="ZRDW" placeholder="必填" check-type="required maxlength" maxlength="200"></td>	
			</tr>
		</table>
      </form>
    </div>
  </div>
</div>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="resultXML" id = "resultXML">
<!--          <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter"> -->
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
 	</FORM>
 </div>
<script type="text/javascript">
//将列表中的数据赋到表单中
getWinData=function(data){
	var tempJson=convertJson.string2json1(data);
	setValueByArr(tempJson,["XMID","ND","JHID","XMMC","JHSJID","ZSXMID","CQJHWCSJ","QY"]);
	//getSrc();
	//$("#S_QY").attr("check-type","required");
};
</script>
</body>
</html>