<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />

<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/zsb/xmb/xmbController.do";
  var countcontroller= "${pageContext.request.contextPath }/pqgl/pqglController.do";
  //获取父页面行选值方法
  function do_value(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		var obj = convertJson.string2json1(rowValue);
		return obj;
	}
  function setvalue(){
	  var json=do_value();
	  setValueByArr(json,["JHSJID","ND","JHID","XMID","GC_ZSB_XMB_ID","SJBH"]);
	  $("#JHSJ").val($("#JHSJID").val());
	  $("#XmbForm").setFormValues(json);
	  var sfcq_sj=$(json).attr('SFCQ_SJ');
	  var sfcq_jh=$(json).attr('SFCQ_JH');
	  sfcq_sj?($("#SFCQ").val(sfcq_sj)):$("#SFCQ").val(sfcq_jh);
	  if(json.SFCQ_JH=='0'){
		  document.getElementById("CDSJSJ").disabled=true;
	  }
  }
  var id,xmkid,xmmc;
	$(function() {
		setvalue();		//页面初始化赋值
		var feedback=$("#feedback");
		feedback.click(function(){
			if($("#XmbForm").validationButton()){
				var counts = getJhfkCounts();
				if(Number(counts)>=1){
						xConfirm("提示信息","您已经对该计划进行过反馈了，再次反馈将覆盖之前的数据，是否继续？");
						$('#ConfirmYesButton').unbind();
						$('#ConfirmYesButton').one("click",function(){
						do_feedback();
					});
				}else{
						do_feedback();
				}
				}else{
			  		defaultJson.clearTxtXML();
				}
		});
		
		var savebtn=   $("#save");
		savebtn.click(function(){
			$("#SJWCRQ").attr("check-type","");
			if($("#XmbForm").validationButton()){
			//生成json串
			var data = Form2Json.formToJSON(XmbForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			var id=$("#GC_ZSB_XMB_ID").val();
			if(id==null||id==""){
				defaultJson.doInsertJson(controllername+"?insertXmb",data1,null,"callback");
			}else{
				defaultJson.doUpdateJson(controllername+"?updateXmb",data1,null,"callback");
			}
			}else{
		  		defaultJson.clearTxtXML();
			}
		});
			});
	//一步操作
	function callback(){
		//从resultXML获取修改的数据
		var data2 = $("#frmPost").find("#resultXML").val();
		//返回数据
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.xgh(data2);
		$(window).manhuaDialog.close();
	}
    //页面反馈方法
    function do_feedback(){
    	//生成json串
		var data = Form2Json.formToJSON(XmbForm);
		//组成保存json串格式
		var data1 = defaultJson.packSaveJson(data);
    	defaultJson.doInsertJson(controllername+"?doJhsjfk",data1,null,"callback");	
    }
    //页面功能禁止功能的添加
    function rows(){
    	var rows=$("#DT1").getTableRows();
    	if(rows>0){
    		document.getElementById("SFCQ").disabled=true;
    	}
    }
    /**
	*	获取计划反馈次数
	*/
	function getJhfkCounts(){
		var n = $("#JHSJID").val();
		var ywlx=100001;
		var str = "";
		$.ajax({
			url:countcontroller + "?getJhfkCounts&jhsjid="+n+"&ywlx="+ywlx,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				str = result.msg;
			}
		});
		return str;
	}
    //获取计划拆迁状态
    function getZt(){
    	var jhsjid=$("#JHSJID").val();
    	var zt="";
    	$.ajax({
    		url:controllername+"?getZt&jhsjid="+jhsjid,
    		data:"",
    		dataType:"json",
    		async:false,
    		success:function(result){
    			zt=result.msg;
    		}
    	});
    	return zt;
    }
	//进展情况
	function jzqk()
	{
		var id=$("#JHSJID").val();
	 	$(window).manhuaDialog({"title":"征地拆迁>进展情况","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xmb/jdxxck.jsp?jhsjid="+id,"modal":"2"});
	}
</script>
</head>
	<body>
	<app:dialogs />
		<div class="container-fluid">
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
				<h4 class="title">反馈信息
					<span class="pull-right">
						<button id="save" name="save" class="btn" type="button">保存</button>
						<!-- <button id="feedback" name="feedback" id="feedback" class="btn" type="button">提交</button>  -->
					</span>
				</h4>
		<form method="post" id="XmbForm">
			<table class="B-table" width="100%">
				<tr style="display: none">
					<td><input class="span12" type="text" id="SJBH" fieldname="SJBH" name="SJBH" keep="true"></td>
					<td><input class="span12" type="text" id="GC_ZSB_XMB_ID" fieldname="GC_ZSB_XMB_ID" name="GC_ZSB_XMB_ID" keep="true"></td>
					<td><input class="span12" type="text" id="XMID" fieldname="XMID" name="XMID" keep="true"></td>
					<td><input class="span12" type="text" id="JHSJID" fieldname="JHSJID" name="JHSJID" keep="true"></td>
					<td><input class="span12" type="text" id="JHID" fieldname="JHID" name="JHID" keep="true"></td>
					<td><input class="span12" type="text" id="ND" fieldname="ND" name="ND" keep="true"></td>
				</tr>
				<tr>
					<th id="cdyj" width="8%" class="right-border bottom-border text-right">场地移交时间</th>
					<td width="17%" class="left-border bottom-border">
						<input class="span12 date" type="date" fieldname="CDSJSJ" name="CDSJSJ" id="CDSJSJ"/>
					</td>
					<td  class="right-border bottom-border" colspan="2">
					<a href='javascript:void(0)' onclick="jzqk()"><i class="icon-list"></i>查看进展情况</a>
					</td>
				</tr>
			</table>
		</form>
				</div>
			</div>
		</div>
		<jsp:include page="/jsp/file_upload/fileupload_config.jsp"
			flush="true" />
		<div align="center">
			<FORM name="frmPost" id="frmPost" method="post" style="display: none"
				target="_blank">
				<!--系统保留定义区域-->
				<input type="hidden" name="queryXML" id="queryXML">
				<input type="hidden" name="txtXML" id="txtXML">
				<input type="hidden" name="resultXML" id="resultXML">
				<input type="hidden" name="txtFilter" order="desc" fieldname="xxb.LRSJ" id="txtFilter">
				<!--传递行数据用的隐藏域-->
				<input type="hidden" name="rowData">

			</FORM>
		</div>
	</body>
</html>