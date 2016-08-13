<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/gcCjjhController.do";
	$(function() {
		init();
		var rowValue =$(window).manhuaDialog.getParentObj().menuTreeJson;
		$("#PARENTID").val(rowValue.id);
		$("#ND").val(rowValue.nd);
		$("#XMSX").val(rowValue.xmsx);
		$("#NODELEVEL").val(Number(rowValue.level)+1);
		//保存按钮
		var btn = $("#example1");
		btn.click(function(){
	 	 if($("#demoForm").validationButton()){
		 		//生成json串
				var data = Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//调用ajax插入
		 		defaultJson.doInsertJson(controllername + "?saveInfo", data1,null,'addHuiDiao');
				}
	 	 });
		//添加关联按钮事件
		$("#addXmBtn").click(function(){
			openXmList("getXmData");
		});
		//取消关联按钮事件
		$("#delXmBtn").click(function(){
			if($("#XMID").val()!=""){
				$("#XMID").val("");
				xAlert("提示信息","操作成功");
			}else{
				xInfoMsg('本条数据未关联任何项目！');
			}
			ctrlLabelStyle();
		});
   });
	//默认选中项目
	function init(){
		$("input[name=ISXM]").each(function()
	     {
		  	if(this.value=='1'){
			  this.checked=true;
	    	}
		});
		$("input[name='ZRBM']").each(function(){
			$(this).attr("checked",false);
			$(this).attr("disabled","true");
		});
		$("input[name=DXZX]").each(function()
			     {
				  	if(this.value=='1'){
					  this.checked=true;
			    	}
				});	
	}
	function  xuanzhexm(){
		$("input[name=ISXM]").each(function(){
			if(this.checked==true){
				var aa=this.value;
				if (aa == '0') {
					$("#addXmBtn").hide(); 
					$("#XMID").val('');
					$("#XMMC").removeAttr("disabled");
					$("input[name='ZRBM']").each(function(){
						$(this).removeAttr("disabled");
					}); 
					$("#SSXZ").attr("check-type","");
					$("#JWXM").attr("check-type","");
					$("#XMXZ").attr("check-type","");
					$("#TZLX").attr("check-type","");
					$("#demoForm").validation();
				} else {
					$("#addXmBtn").show();
					$("#XMMC").val('');
					$("#XMID").val('');
					$("#XMMC").attr("disabled","disabled");
					$("input[name='ZRBM']").each(function(){
						$(this).attr("checked",false);
						$(this).attr("disabled","true");
					}); 
					$("#SSXZ").attr("check-type","required");
					$("#JWXM").attr("check-type","required");
					$("#XMXZ").attr("check-type","required");
					$("#TZLX").attr("check-type","required");
					$("#demoForm").validation();
				}
			}
			});
	}
	//----------------------
	//选择项目
	//----------------------
	function openXmList(callBack){
		if(callBack!=undefined && callBack!=""){
			callBack = "?callBack="+callBack;
		}
		$(window).manhuaDialog({"title":"项目列表","type":"text","content": "${pageContext.request.contextPath}/jsp/business/cjjcssxmjsjh/xmcx.jsp"+callBack,"modal":"2"});
	}
	//控制lebel样式
	function ctrlLabelStyle(){
		if($("#XMID").val()!=""){
			$("#gljgxm").text("已关联建管项目");
			$("#gljgxm").addClass("label-info");
		}else{
			$("#gljgxm").text("未关联建管项目");
			$("#gljgxm").removeClass("label-info");
		}
	}
	function getXmData(obj){
		var tempJson = convertJson.string2json1(obj);
		$("#demoForm input").each(function(){
			var elem = $(this);
			if(elem.attr("fillValue")!=undefined && elem.attr("fillValue")!=""){
				elem.val(tempJson[elem.attr("fillValue")]);
			}
		});
		ctrlLabelStyle();
	}
//回调函数
function addHuiDiao()
{
	var data2 = $("#frmPost").find("#resultXML").val();
	var fuyemian=$(window).manhuaDialog.getParentObj();
	fuyemian.jiedianhuiadd(data2,1);
    $(window).manhuaDialog.close();
}
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">
  	<span class="pull-right">
		<button id="example1" class="btn"  type="button">保存</button>
    </span> 
  </h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
      		<table style="width: 100%" class="B-table">
				<!--可以再此处加入hidden域作为过滤条件 -->
				<TR style="display: none;">
					<TD class="right-border bottom-border"></TD>
					<TD class="right-border bottom-border">
						<input type="text" fieldname="LEVELNO" id="LEVELNO" name="LEVELNO" />
					</TD>
					<th>父节点</th>
					<td>
						<input type="text" disabled check-type="required" id="PARENTID" name="PARENTID" fieldname="PARENTID">
					</td>
					<th>项目ID</th>
					<td >
						<input type="text" disabled id="XMID" name="XMID" fieldname="XMID" fillValue="XMID">
					</td>
					<th >主键</th>
						<td >
							<input type="text" disabled id="GC_CJJH_ID" name="GC_CJJH_ID" fieldname="GC_CJJH_ID">
						</td>
				</TR>
				<!--可以再此处加入hidden域作为过滤条件 -->
				<tr style="display:none;">
				<!-- 	<th width="20%">建委项目</th>
					<td width="40%">
						<input type="text" id="JWXM" name="JWXM" fieldname="JWXM">
					</td> -->
					<th >委办项目</th>
					<td>
						<input type="text" id="WBXM" name="WBXM" fieldname="WBXM">
					</td>
				</tr>
				<tr>
				 <th width="5%">节点类型</th>
         			<td  width="1%" class="right-border bottom-border"><input  id="ISXM" type="radio"    placeholder="" kind="dic" src="E#1=项目:0=节点" name = "ISXM" fieldname="ISXM" onclick="xuanzhexm()"></td>
					<th width="5%" class="right-border bottom-border">大项子项</th>
					<td  width="30%" class="right-border bottom-border">
						<input  id="DXZX" type="checkbox"    placeholder="" kind="dic" src="E#1=大项:0=子项" name = "DXZX" fieldname="DXZX">
					</td>
					<th width="5%" class="right-border bottom-border">建委项目</th>
					<td  width="10%" class="right-border bottom-border">
						<select kind="dic" src="SF" class="span12 4characters"  check-type="required" id="JWXM" name="JWXM" fieldname="JWXM"></select>
					</td>
				</tr>
				<tr>
				<th width="5%" class="right-border bottom-border">项目性质</th>
					<td  width="10%" class="right-border bottom-border">
						<select kind="dic" src="XMXZ" class="span12 4characters"  check-type="required" id="XMXZ" name="XMXZ" fieldname="XMXZ"></select>
					</td>
					<th width="5%" class="right-border bottom-border">投资类型</th>
					<td  width="10%" class="right-border bottom-border">
						<select kind="dic" src="TZLX" class="span12 4characters"  check-type="required" id="TZLX" name="TZLX" fieldname="TZLX"></select>
					</td>
					<th width="5%" class="right-border bottom-border">实施性质</th>
					<td  width="10%" class="right-border bottom-border">
						<select kind="dic" src="XMSX" class="span12 4characters"  check-type="required" 
							id="SSXZ" name="SSXZ" fieldname="SSXZ"></select>
					</td>
				</tr>
				<tr>
					<th>年度</th>
					<td>
						<input type="text" name="ND" fieldname="ND" id="ND" placeholder="必填" check-type="required">
					</td>
					<th>项目名称</th>
					<td colspan=3>
						<input class="span10" type="text" id="XMMC" name="XMMC" style="width:90%" fieldname="XMMC" disabled placeholder="必填" check-type="required" fillValue="XMMC">
						<button class="btn btn-link"  title="请选择项目" type="button" id="addXmBtn"><i class="icon-edit"></i></button>
						<!-- <span id="gljgxm" class="label label-info">关联建管项目</span>
						<button class="btn btn-link" style="padding: 4px 0px 4px 0px;" type="button" id="addXmBtn" title="添加关联">
							<i class="icon-plus"></i>
						</button>
						<button class="btn btn-link" style="padding: 4px 0px 4px 0px;" type="button" id="delXmBtn" title="取消关联">
							<i class="icon-minus"></i>
						</button> -->
					</td>
				</tr>
				<tr>
					<th>项目数</th>
					<td>
						<input type="text" id="MXS" name="MXS" fieldname="MXS">
					</td>
					<th>子项数目</th>
					<td>
						<input type="text" id="ZXSM" name="ZXSM" fieldname="ZXSM">
					</td>
					<th width="5%" class="right-border bottom-border">项目属性</th>
					<td width="10%" class="right-border bottom-border">
						<select kind="dic" src="CJXMSX" class="span12 4characters"  check-type="required" id="XMSX" name="XMSX" fieldname="XMSX"></select>
					</td>
				</tr>
				<tr>
					<th class="right-border bottom-border">责任部门</th>
					<td  class="right-border bottom-border" colspan="5">
						<input class="span12" id="ZRBM" type="checkbox" kind="dic" src="ZRBM"  name="ZRBM" fieldname="ZRBM">
					</td>
				</tr>
				<tr>
					<th >序号<span style="font-size:10px;font-weight:normal">（用于列表显示，允许用字符）</span>
					</th>
					<td >
						<input type="text" id="XH" name="XH" fieldname="XH">
					</td>
					<th >排序号<span style="font-size:10px;font-weight:normal">（用于数据排序）</span>
					</th>
					<td>
						<input type="number" id="PXH" name="PXH" fieldname="PXH" 
							placeholder="必填" check-type="required maxlength" maxlength="2" style="text-align:right;">
					</td>
					<th>节点等级</th>
					<td>
						<input type="number" name="NODELEVEL" fieldname="NODELEVEL" id="NODELEVEL"  placeholder="必填" check-type="required" style="text-align:right;">
					</td>
				</tr>
			</table>
      	  </form>
     	</div>
 	</div>
</div>
<div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
		   <input type="hidden" name="ywid" id = "ywid" value="">
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>

</body>
</html>