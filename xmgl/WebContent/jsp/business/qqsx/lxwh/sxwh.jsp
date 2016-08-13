<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base />
<title></title>
<%
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	String userDept = user.getDepartment();
	String userid = user.getAccount();
%>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/qqsx/lxky/sxfjController.do";
	var e_count;			//显示类型个数
	var s_count=0;			//应显示个数
	var mark;				//标记，是否进行限制
	var del_fjlb;			//删除标识之附件类型
	//获取父页面的值
	function do_value(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		var obj = convertJson.string2json1(rowValue);
		return obj;
	}
	
	$(function() {
 		set_Value();
		ready();
		//页面删除
		$("#del").click(function(){
			var index=$("#DT1").getSelectedRowIndex();
			if(index==-1){
				requireSelectedOneRow();
				return;
			}else{
				xConfirm("提示信息","是否要删除此条记录信息！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){
				var data = Form2Json.formToJSON(SxfjForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				defaultJson.doUpdateJson(controllername + "?deleteSxfj&fjlb="+del_fjlb, data1,DT1,"in_callback");
				});
			}
		});
		//页面查询
		$("#query").click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?querySxfj",data,DT1,null,false);
				});
		//保存按钮说明
		$("#save").click(function() {
			var data = Form2Json.formToJSON(SxfjForm);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			if($("#SxfjForm").validationButton()){
				var fjid=$("#FJID").val();
			if(fjid==null||fjid=="")
				{
					defaultJson.doInsertJson(controllername + "?insertSxfj&ywid="+$("#ywid").val(), data1,DT1,"in_callback");
				}else{
					defaultJson.doUpdateJson(controllername + "?updateSxfj", data1,DT1,"up_callback");
				}
			}else{
		  		defaultJson.clearTxtXML();
			}
				});
		//新增按钮，即表单的清除
		$("#new").click(function(){
			 	clearForm();
				});
		//反馈页面的链接按钮
		$("#feedback").click(function(){
			xConfirm("提示信息","是否进行部门反馈？");
			$('#ConfirmYesButton').unbind();
			$('#ConfirmYesButton').one("click",function(){
				$(window).manhuaDialog({"title":"立项可研手续>信息反馈","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/lxwh/xxfk.jsp","modal":"3"});
			});
		});
	});
	//插入、修改异步操作
	function in_callback(){
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.gengxinchaxun();
		ready();
	}
	//删除异步操作
	function up_callback(){
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.gengxinchaxun();
	}
    //清空表单 主要是ywid的删除 附件的删除 以及字典选择导致fjlb的变换 字典可用的判断
     function clearForm(){
    	$("#SxfjForm").clearFormResult();
		$("#ywid").val("");
		clearFileTab();
		$("#DT1").cancleSelected();
		change();
		//disDic();
		doUser();
	 	def_date();
    }
     //页面初始化赋值
    function set_Value(){
		var json=do_value();
		setValueByArr(json,["JHSJID","XMID","XMMC","SJWYBH"]);
		var jhsjid=$("#JHSJID").val();
		var sjwybh=$("#SJWYBH").val();
		$("#Q_SJWYBH").val(sjwybh);
		//不办理手续说明
		var bblsx=$(json).attr("BBLSX");
		mark=$(json).attr("XDKID");
		var sxArray = new Array();
		sxArray = bblsx.split(",");
		var sx="";
		$("input[type='checkbox']").each(function(){
			var check=true;
			for(var i=0;i<sxArray.length;i++){
				if($(this).val() == sxArray[i]){
					check=false;
					break;
					}
			}
			if(check){
				if($(this).attr('value')==undefined){
					return;
				}else{
 					s_count=Number(s_count)+1;
 					sx=sx?(sx+",'"+$(this).attr('value')+"'"):("'"+$(this).attr('value')+"'");
 				}
			}
			
		});
		$("#Q_FJLX").val(sx);
		$("#FJLX").attr("src","T#fs_dic_tree:dic_code:dic_value:parent_id='1000000000234' and dic_code in("+sx+")");
		$("#FJLX").html('');
		reloadSelectTableDic($("#FJLX"));
	}  
    
    //页面初始化查询 清空表单 获取行数 行数导致反馈按钮是否显示
    function ready() {
      	 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
   		defaultJson.doQueryJsonList(controllername+"?querySxfj",data,DT1,null,true);
   		getCounts();	//获取手续个数
   		clearForm();
   		xmxx();			//显示项目名称
   		doUser();		//默认用户
   		def_date();		//默认时间
      };
      //查询附件个数
    function getCounts() {
    	 var fjlx=$("#Q_FJLX").val();
    	 var sjwybh=$("#SJWYBH").val();
    	 $.ajax({
			url:controllername + "?getCounts&fjlx="+fjlx+"&sjwybh="+sjwybh,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				e_count = result.msg;
			}
    	 });
    	 return e_count;
      }; 
   	//字典选择不可用
    function disDic(){
		var fjlx=$("#FJLX").val();
		if(fjlx==""||fjlx==null){
			document.getElementById("FJLX").disabled=false;
		}else{
			document.getElementById("FJLX").disabled=true;
		}
    } 
    	//行选
    function tr_click(obj){
    	$("#SxfjForm").clearFormResult();
		$("#ywid").val("");
		$("#SxfjForm").setFormValues(obj);
		//字典选择不可用
		//disDic();
		
    	var fjlx=$("#FJLX").val();
    	del_fjlb=$("#FJLX").val();
    	$("#sxfj1").attr("fjlb",fjlx);
    	$("#sxfj").attr("fjlb",fjlx);
    	var jhsjid=$(obj).attr("JHSJID");
    	var a=$(obj).attr("GC_QQSX_SXFJ_ID");
    	
    	deleteFileData(jhsjid,"","","");
		setFileData(jhsjid,a,obj.SJBH,obj.YWLX,"0");
    	queryFileData("",a,"","");
	}
    //字典修改 fjlb变换
    function change(){
    	var fjlx=$("#FJLX").val();
    	$("#sxfj").attr("fjlb",fjlx);
    	$("#sxfj1").attr("fjlb",fjlx);
    }
   //显示项目信息
	function xmxx(){
	   var xmmc=$("#XMMC").val();
		var xm=document.getElementsByTagName("font");
			xm[0].innerHTML = "项目名称："+xmmc;	
	};
	//默认用户
    function doUser(){
    	var userid='<%=userid%>';
    	setDefaultOption($("#BLR"),userid);
    }
  //三级子页面调用一级页面，中间方法
	function do_parent(){
		var fuyemian=$(window).manhuaDialog.getParentObj();
		fuyemian.gengxinchaxun();
		successInfo("","");
	}
	//默认时间
	function def_date(){
		var today=getCurrentDate();
		var s_date='{"BLSJ":\"'+today+'\","BLSJ_SV":\"'+today+'\"}';	
		var date=convertJson.string2json1(s_date);
		$("#SxfjForm").setFormValues(date);
	}
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
      	<!-- <TR> -->
	        <TD><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true"></TD>
			<td><INPUT type="text" class="span12" kind="text" name="SJWYBH" fieldname="SJWYBH" id="Q_SJWYBH" operation="=" keep="true">
			</TD>
			<TD><INPUT type="text" class="span12" kind="text" id="Q_FJLX" keep="true"></TD>
        </TR>
      </table>
      </form>
    <div style="height:5px;"></div>
    <div class="overFlowX">
	<table class="table-hover table-activeTd B-table" id="DT1" width="100%" type="single" pageNum="5">
		<thead>
			<tr>
			<th name="XH" id="_XH" width="2%">&nbsp;#&nbsp;</th>
            <th fieldname="BLSJ" tdalign="center" width="10%">&nbsp;反馈时间&nbsp;</th>
            <th fieldname="BLR" width="10%">&nbsp;反馈人&nbsp;</th>
            <th fieldname="WHMC" maxlength="20" width="15%">&nbsp;文号名称&nbsp;</th>
            <th fieldname="FJLX" width="15%">&nbsp;手续类型&nbsp;</th>
            <th fieldname="CZWT" maxlength="40">&nbsp;存在问题&nbsp;</th>
			</tr>
		</thead>
	<tbody></tbody>
	</table>
	</div>
</div>
    <div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">立项可研信息&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       		<font class="title" style="color:gray;">&nbsp;</font>
      <span class="pull-right">
          <button id="new" name="new" class="btn"  type="button">新增</button>
          <button id="save" name="save" class="btn"  type="button">保存</button>
          <button id="del" name="del" class="btn"  type="button">删除</button>
          <button id="feedback" name="save" class="btn"  type="button">部门反馈</button>
       </span></h4>
     <form method="post" id="SxfjForm"  >
      <table class="B-table" width="100%">
         <TR  style="display:none;">
         <!-- <TR> -->
			<TD><input type="text" class="span12" kind="text" id="XMMC" fieldname="" keep="true" ></TD>
			<TD><input type="text" class="span12" kind="text" id="XMID" fieldname="XMID" keep="true" ></TD>
	        <TD><input type="text" class="span12" kind="text" id="JHSJID" fieldname="JHSJID" keep="true" ></TD>
	        <TD><input type="text" class="span12" kind="text" id="SJBH" fieldname="SJBH" keep="true" ></TD>
			 <TD><INPUT type="text" class="span12" kind="text" name="SJWYBH" fieldname="SJWYBH" id="SJWYBH"  keep="true"></TD>
			<!-- <TD><input type="text" class="span12" kind="text" id="BDMC" fieldname="" keep="true" ></TD> -->
			<TD><input class="span12" id="BBLSX" type="checkbox" kind="dic" src="LXKYFJLX" name ="BBLSX" ></TD>
	        <TD><input type="text" class="span12" kind="text" id="FJID" fieldname="GC_QQSX_SXFJ_ID" ></TD>
			<TD><input type="text" class="span12" kind="text"  id="ywid"></TD>
        </TR>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">选择手续</th>
          <td width="17%" class="right-border bottom-border">
          <select class="span12" id="FJLX" name="FJLX" fieldname ="FJLX" kind="dic" placeholder="必填" check-type="required" src="LXKYFJLX" onchange="change()">
            </select>
          </td>
          <td width="25%" class="right-border bottom-border" colspan="2"> 
          <th width="8%" class="right-border bottom-border text-right">文号名称</th>
          <td width="42%" class="right-border bottom-border" colspan="3"><input class="span12"  type="text" placeholder="必填"  check-type="required" fieldname="WHMC" name = "WHMC" check-type="maxlength" maxlength="200"></td>          
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">反馈人</th>
          <td width="17%"  class="right-border bottom-border">
          	<select class="span12 person" check-type="" fieldname="BLR" id="BLR" name = "BLR" kind="dic" src="T#FS_ORG_PERSON:ACCOUNT:NAME:DEPARTMENT='<%=userDept%>'  AND PERSON_KIND = '3' AND FLAG='1' order by sort">
          	</select>
          </td>
          <td width="25%" class="right-border bottom-border" colspan="2">	
          <th width="8%" class="right-border bottom-border text-right">反馈时间</th>
          <td width="17%" class="bottom-border">
            <input class="span12 date"  type="date" placeholder="必填"  check-type="required" fieldname="BLSJ" name = "BLSJ" >
          </td>
          <td width="25%" class="right-border bottom-border" colspan="2">
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">存在问题</th>
          <td width="92%"  class="right-border bottom-border" colspan="7">
          	<textarea rows="3" class="span12"  type="text" check-type="maxlength" fieldname="CZWT" id="CZWT" name = "CZWT" maxlength="4000" ></textarea>
          </td>
        </tr>
        <tr>
			<th width="8%" class="right-border bottom-border text-right">附件信息</th>
			   <td colspan="7" class="bottom-border right-border">
					<div>
						<span class="btn btn-fileUpload" id="sxfj" onclick="doSelectFile(this);" fjlb="">
								<i class="icon-plus"></i>
								<span>添加文件...</span>
						</span>
							<table role="presentation" class="table table-striped">
								<tbody id="sxfj1" fjlb="" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery">
						</tbody>
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
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
         <input type="hidden" name="queryResult" id ="queryResult">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>