<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<app:base />
<title></title>
<%
	String jhsjid=request.getParameter("jhsjid");
	String ywbid=request.getParameter("ywbid");
	String sjsj=request.getParameter("bjsj");
	String bblsx=request.getParameter("bblsx");
	String xmmc=request.getParameter("xmmc");
	String sfck=request.getParameter("sfck");
	//String json=request.getParameter("tempValue");
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	String userDept = user.getDepartment();
	String username = user.getName();
%>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/qqsx/ghsp/sxfjController.do";
	var jhsjid="<%=jhsjid%>";
	var ywbid="<%=ywbid%>";
	var sjsj="<%=sjsj%>";
	var bblsx="<%=bblsx%>";
	var xmmc="<%=xmmc%>";
	var username="<%=username%>";
	<%-- var json="<%=json%>"; --%>
	var e_count;			//显示类型个数
	var s_count=0;			//应显示个数
 /* 	function setValue(){
		json=JSON.stringify(json);
		alert(json);
		return;
		setValueByArr(json,["XMMC","JHSJID","YWBID","BJSJ"]);
	} */
	$(function() {
		//setValue();
		ready();
		var querybtn = $("#query");
		var savebtn=   $("#save");
		var newbtn=$("#new");
		//页面查询
		querybtn.click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?querySxfj",data,DT1);
				});
	});
    //页面初始化查询 清空表单 获取行数 行数导致反馈按钮是否显示
    function ready() {
    	g_bAlertWhenNoResult=false;	
      	 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
   		//调用ajax插入
   		defaultJson.doQueryJsonList(controllername+"?querySxfj",data,DT1);
   		g_bAlertWhenNoResult=true;
   		clearForm();
   		init();			
   		getCounts();	//获取手续个数
   		xmxx();			//显示项目名称
      };
     //查询附件个数
    function getCounts() {
    	 var fjlx=$("#Q_FJLX").val();
    	 var ywbid=$("#YWBID").val();
    	 $.ajax({
			url:controllername + "?getCounts&fjlx="+fjlx+"&jhsjid="+jhsjid,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				e_count = result.msg;
			}
    	 });
    	 return e_count;
      }; 
    function tr_click(obj){
    	$("#SxfjForm").clearFormResult();
		$("#ywid").val("");
		$("#SxfjForm").setFormValues(obj);
		//字典选择不可用
		disDic();
    	var fjlx=$("#FJLX").val();
    	$("#sxfj1").attr("fjlb",fjlx);
    	$("#sxfj").attr("fjlb",fjlx);
    	a=$(obj).attr("GC_QQSX_SXFJ_ID");
		setFileData(jhsjid,a,obj.SJBH,obj.YWLX);
    	queryFileData("",a,"","");
	}
    //字典修改 fjlb变换
    function change(){
    	var fjlx=$("#FJLX").val();
    	$("#sxfj").attr("fjlb",fjlx);
    	$("#sxfj1").attr("fjlb",fjlx);
    }
    
  //页面默认参数
    function init(){
	  	if(Number(s_count)>0){
	  		return s_count;
	  	}else{
    		$("[name=BBLSX]:checkbox").attr("checked",true);
    		var sxArray = new Array();
    		sxArray = bblsx.split(",");
    		$("input[type='checkbox']").each(function(){
    			for(var i=0;i<sxArray.length;i++){
    				if($(this).val() == sxArray[i]){
    					$(this).attr("checked",false);
    					break;
    				}
    			}
    		}); 
    	 var sx="";
    	 $("input[type='checkbox']").each(function(){
 			if($(this).is(':checked')){
 				if($(this).attr('value')!=undefined){
 					
 					s_count=Number(s_count)+1;
	 				
 					if(sx==""||sx==null){
						sx="'"+$(this).attr('value')+"'";
	 				}else{
	 					sx=sx+",'"+$(this).attr('value')+"'";
	 				}
 				}
 			}
 		});
    	$("#Q_FJLX").val(sx); 
    	 //输入框显示
		$("#FJLX").attr("src","T#fs_dic_tree:dic_code:dic_value:parent_id='1000000000102' and dic_code in("+sx+")");
		$("#FJLX").html('');
		reloadSelectTableDic($("#FJLX"));
	  	}
  }
   //显示项目信息
	function xmxx(){
		var xm=document.getElementsByTagName("font");
			xm[0].innerHTML = "项目名称："+xmmc;	
	};

</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
    <!-- <h4 class="title">规划手续信息
      <span class="pull-right">
          <button id="feedback" name="save" class="btn"  type="button">反馈</button>
          <button id="clear" name="clear" class="btn"  type="button">新增</button>
       </span></h4> -->
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
      	<!-- <TR> -->
	        <TD><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true"></TD>
			<TD><INPUT type="text" class="span12" kind="text"  fieldname="YWBID" id="YWBID" value="<%=ywbid%>" operation="=" keep="true"></TD>
			<TD><input class="span12" type="text" id="Q_FJLX" keep="true"></TD>			
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
            <th fieldname="WHMC" maxlength="15" width="15%">&nbsp;文号名称&nbsp;</th>
            <th fieldname="FJLX" width="15%">&nbsp;手续类型&nbsp;</th>
            <th fieldname="CZWT" maxlength="30">&nbsp;存在问题&nbsp;</th>
			</tr>
		</thead>
	<tbody></tbody>
	</table>
	</div>
</div>
    <div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">规划手续信息&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       		<font class="title" style="color:gray;">&nbsp;</font>
      <span class="pull-right">
      <%if(sfck!="1"){ %>
          <button id="save" name="save" class="btn"  type="button">保存</button>
          <button id="new" name="new" class="btn"  type="button">新增</button>
          <button id="del" name="del" class="btn"  type="button">删除</button>
          <button id="feedback" name="save" class="btn"  type="button">反馈</button>
          <%}%>
       </span></h4>
     <form method="post" id="SxfjForm"  >
      <table class="B-table" width="100%">
         <TR  style="display:none;">
         <!-- <TR> -->
			<TD><input class="span12" id="BBLSX" type="checkbox" kind="dic" src="GHSX" name ="BBLSX" ></TD>
	        <TD><input type="text" class="span12" kind="text" id="FJID" fieldname="GC_QQSX_SXFJ_ID" ></TD>
	        <TD><input type="text" class="span12" kind="text"  fieldname="JHSJID" value="<%=jhsjid %>" keep="true" ></TD>
			<TD><input type="text" class="span12" kind="text"  fieldname="YWBID" value="<%=ywbid %>" keep="true" ></TD>
			<!-- <TD><input type="text" class="span12" kind="text" id="JHSJID" fieldname="JHSJID" keep="true" ></TD>
			<TD><input type="text" class="span12" kind="text" id="YWBID" fieldname="YWBID" keep="true" ></TD>
			<TD><input type="text" class="span12" kind="text" id="XMMC" keep="true" ></TD>
			<TD><input type="text" class="span12" kind="text" id="BJSJ" keep="true" ></TD> -->
			<TD><input type="text" class="span12" kind="text"  id="ywid"></TD>
        </TR>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">选择手续</th>
          <td width="17%" class="right-border bottom-border">
          <select class="span12" id="FJLX" name = "FJLX" fieldname = "FJLX" kind="dic" placeholder="必填" check-type="required" src="GHSX" onchange="change()">
            </select>
          </td>
          <td width="25%" class="right-border bottom-border" colspan="2"> 
          <th width="8%" class="right-border bottom-border text-right">文号名称</th>
          <td width="42%" class="right-border bottom-border" colspan="3"><input class="span12"  type="text" placeholder="必填"  check-type="required" fieldname="WHMC" name = "WHMC" check-type="maxlength" maxlength="200"></td>          
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">反馈人</th>
          <td width="17%"  class="right-border bottom-border">
          	<select class="span12" check-type="" fieldname="BLR" id="BLR" name = "BLR" kind="dic" src="T#FS_ORG_PERSON:NAME:NAME:DEPARTMENT='<%=userDept%>'  AND PERSON_KIND = '3' AND FLAG='1'" defaultMemo="<%=username %>">
          	</select>
          </td>
          <td width="25%" class="right-border bottom-border" colspan="2">	
          <th width="8%" class="right-border bottom-border text-right">反馈时间</th>
          <td width="17%" class="bottom-border">
            <input class="span12"  type="date" placeholder="必填"  check-type="required" fieldname="BLSJ" name = "BLSJ" >
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
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>