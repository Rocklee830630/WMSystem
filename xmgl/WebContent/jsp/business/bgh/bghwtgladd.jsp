<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base/>
	<%
	request.setCharacterEncoding("utf-8");
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String username = user.getName();
	String account=user.getAccount();
	String department = user.getDepartment();
	String bz=request.getParameter("bz");
	%>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/banGongHuiWenTiController.do";
	var tbl = null;
	var bz=<%=bz%>;
	var fuyemianfangfa=true;
	var xx;
	$(function() {
		if('1'==bz){
			$("#WTLX").val('1');
			$("#ZT").val('1');
			$("#but_tijiao").hide();
			$(".isbm").show();
		}else if('2'==bz){
			$("#WTLX").val('1');
			$("#ZT").val('2');
			$("#but_tijiao").hide();
			$(".isbm").show();
	 		var PARENT_ID =$($(window).manhuaDialog.getParentObj().document).find("#PARENT_ID").val();
			$("#BGHID").val(PARENT_ID);
		}else{
			$("#WTLX").val('0');
			$("#ZT").val('0');
			$(".isbm").hide();
		}
		//保存按钮
		var btn = $("#but_save");
		btn.click(function(){
			if($("#demoForm").validationButton()){
				//生成json串
				var data = Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//调用ajax插入
				if(fuyemianfangfa){
					defaultJson.doInsertJson(controllername + "?insertBanGongHuiWenTi&ywid="+$("#ywid").val(), data1,null,'addHuiDiao');
				}else{
					defaultJson.doInsertJson(controllername + "?updateBanGongHuiWenTi", data1,null,'editHuiDiao');
				}
			}
		});
		//提交
		var btn = $("#but_tijiao");
		btn.click(function(){
			if($("#demoForm").validationButton()){
				//生成json串
				var data = Form2Json.formToJSON(demoForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				//调用ajax插入
				if(fuyemianfangfa){
					xConfirm("提示信息","是否确认提交！提交后不能修改");
					$('#ConfirmYesButton').unbind();
					$('#ConfirmYesButton').one("click",function(){ 
						//生成json串
						$("#ZT").attr("code",1);
							var data = Form2Json.formToJSON(demoForm);
							//组成保存json串格式
							var data1 = defaultJson.packSaveJson(data);
							//调用ajax插入
							defaultJson.doInsertJson(controllername + "?insertBanGongHuiWenTi&ywid="+$("#ywid").val(), data1,null,'addHuiDiao');
						});
	 			 	}else{
		 			 	defaultJson.doInsertJson(controllername + "?updateBanGongHuiWenTi", data1,null,'editHuiDiao');
	 			 	}
				}
 		   });
	    //清空表单
		var btn = $("#example_clear");
		btn.click(function() 
		{
			$("#demoForm").clearFormResult(); 
			 clearFileTab();
		     $("#ywid").val("");
		});
		//选择项目
		$("#xmBtn").click(function(){
			queryProjectWithBD();
		});
		jieshouren();
		bumen();
	});
//回调函数
function addHuiDiao()
{
	
	var data2 = $("#frmPost").find("#resultXML").val();
	var fuyemian=$(window).manhuaDialog.getParentObj();
    fuyemian.tianjiahang(data2); 
    $(window).manhuaDialog.close();
}
function editHuiDiao()
{
	
	var data3 = $("#frmPost").find("#resultXML").val();
	var fuyemian1=$(window).manhuaDialog.getParentObj();
	fuyemian1.xiugaihang(data3);	
}
//选择项目回调
function getWinData(rowsArr){
		var data = convertJson.string2json1(rowsArr);
		$("#XMMC").val(data.XMMC);
		$("#BDMC").val(data.BDMC);
		$("#JHSJID").val(data.GC_JH_SJ_ID);
		$("#XMID").val(data.XMID);
		$("#BDID").val(data.BDID);
}
//默认接收人
function jieshouren()
{
  var account="<%= account%>";
  setDefaultOption($("#FQR"),account);
}
//默认当前部门
function bumen()
{
  var department="<%= department%>";
  setDefaultOption($("#FQBM"),department);
}
//动态获取发起人
function xuzheren(obj)
{
	var lqbm=$(obj).val();
	var src = "T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG='1'  AND PERSON_KIND = '3' AND DEPARTMENT = '"+lqbm+"'  order by sort ";
	$("#FQR").attr('src',src);
	reloadSelectTableDic($("#FQR"));
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
  				<!-- <button id="but_clear" class="btn"  type="button">清空</button> -->
				<button id="but_save" class="btn"  type="button">保存</button>
				<button id="but_tijiao" class="btn"  type="button">提交</button>
				</span> 
  </h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
     	<input class="span12" id="GC_BGH_WT_ID" type="hidden"   fieldname="GC_BGH_WT_ID" name = "GC_BGH_WT_ID">
     	<input class="span12" id="BGHID" type="hidden"   fieldname="BGHID" name = "BGHID">
      		<table class="B-table" width="100%"  >
      		                
	      			<TR style="display: none" >
				        <TD class="right-border bottom-border">
				            <input class="span12"  id="XMID"  type="text"  name = "XMID" fieldname="XMID">
							<input class="span12"  id="BDID"  type="text"  name = "BDID" fieldname="BDID">
							<input class="span12"  id="JHSJID"  type="text"  name = "JHSJID" fieldname="JHSJID">
							<input class="span12"  id="ZT"  type="text"  name = "ZT" fieldname="ZT">
							<input class="span12"  id="WTLX"  type="text"  name = "WTLX" fieldname="WTLX">
				        </TD>
	                </TR>
      			<tr>
      				<th  width="7%" class="right-border bottom-border text-right ">议题标题</th>
          			<td class="right-border bottom-border"  colspan="7">
          				<input class="span12"  check-type="required maxlength" maxlength="1000" placeholder="必填" type="text"  id="WTBT"  name = "WTBT" fieldname="WTBT">
          			</td>
          		</tr>
        		<tr>
          			<th class="right-border bottom-border text-right ">议题描述</th>
          			<td class="right-border bottom-border"  colspan="7">
          			<textarea rows="5" class="span12"  check-type="required maxlength" placeholder="必填" maxlength="4000" id="WTMS"  name = "WTMS" fieldname="WTMS"></textarea>
          			</td>
        		</tr>
        		<tr>
          			<th class="right-border bottom-border text-right disabledTh" width="7%">涉及项目</th>
            		<td  class="right-border bottom-border " colspan="3">
            			<input class="span12" disabled  type="text" placeholder="" check-type="" id="XMMC" name = "XMMC" fieldname="XMMC"></td>
        		    <th class="right-border bottom-border text-right disabledTh" width="7%">涉及标段</th>
        			<td  class="right-border bottom-border "  colspan="3">
        				<input class="span12" style="width:87%"  disabled id="BDMC" type="text" fieldname="BDMC" name = "BDMC">
          			  <button class="btn btn-link"  title="请选择项目" type="button" id="xmBtn" ><i class="icon-edit"></i></button></td>
        		</tr>
        		<tr>
        			<th class="right-border bottom-border text-right  isbm"  width="7%"> 发起部门</th>
            		<td class="right-border bottom-border isbm" width="18%">
            			 <select class="span12 department" kind="dic" check-type="required" id="FQBM" name = "FQBM" fieldname= "FQBM" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME"  onchange="xuzheren(this)"></select>
           			</td>
          			<th class="right-border bottom-border text-right " width="7%"> 发起人</th>
            		<td class="right-border bottom-border " width="18%">
            			<select class="span12 person" kind="dic" id="FQR" check-type="required" name = "FQR" fieldname= "FQR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1'  order by sort" ></select>
           			</td>
        		    <th class="right-border bottom-border text-right " width="7%">希望解决时间</th>
            		<td  class="right-border bottom-border " width="18%">
            			<input class="span12 date"  type="date"  check-type="" id="XWJJSJ" name = "XWJJSJ" fieldname="XWJJSJ">
           			</td>
        		    <th class="right-border bottom-border text-right " width="7%">是否公开</th>
            		<td  class="right-border bottom-border" width="18%">
            			<select class="span12 2characters" kind="dic" id="SFGK" check-type="required"
            				name="SFGK" fieldname="SFGK" src="SF" nonullselect="true"></select>
           			</td>
           			<td colspan="2"></td>
        		</tr>
       		  	<tr>
					<th width="8%" class="right-border bottom-border text-right">附件上传</th>
					<td colspan="8" class="bottom-border right-border">
						<div>
						<span class="btn btn-fileUpload" onclick="doSelectFile(this);" id="shangchuanID" fjlb="0071">
							<i class="icon-plus"></i>
								<span>添加文件...</span></span>
									<table role="presentation" class="table table-striped">
										<tbody fjlb="0071" id="shangchuanID1" class="files showFileTab"
											data-toggle="modal-gallery" data-target="#modal-gallery">
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