<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<app:base/>
<title>工程甩项-维护</title>
<%
	String type=request.getParameter("type");
	String fqlx=request.getParameter("fqlx");
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
	String userid = user.getAccount();
	String username = user.getName();
	String sysdate = Pub.getDate("yyyy-MM-dd");
%>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gcsx/gcsxController.do";
var type ="<%=type%>";
var fqlx = '<%=fqlx%>';
//页面初始化
$(function() {
	init();
	
  	//按钮绑定事件（保存）
    $("#btnSave").click(function() {
    	
    	if($("#gcsxForm").validationButton())
		{
    		//生成json串
    		var ywid = $("#ywid").val();
    		//addjson();
    		var data = Form2Json.formToJSON(gcsxForm);
    		var data1 = addjson(convertJson.string2json1(data),"YWID",ywid);
		   	$(window).manhuaDialog.setData(data1);
		   	$(window).manhuaDialog.sendData();
		   	$(window).manhuaDialog.close();
		}else{
			requireFormMsg();
		  	return;
		}
    });
  	//项目管理公司change事件
    $("#XMGLGS").change(function() {
    	getPensonByDep($("#XMGLGS").val());//重新初始化业主代表
    });
	
});
//页面默认参数
function init(){
	setFqlx();
	getPensonByDep(<%=deptId %>);
	if(type == "insert"){
		$("#SXSQRQ").val("<%=sysdate %>");
	}else if(type == "update"){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.document.getElementById("resultXML").value;
		var obj = convertJson.string2json1(rowValue);
		deleteFileData(obj.GC_GCGL_GCSX_ID,"",obj.SJBH,obj.YWLX);
		setFileData(obj.GC_GCGL_GCSX_ID,"",obj.SJBH,obj.YWLX,"0");
		queryFileData(obj.GC_GCGL_GCSX_ID,"",obj.SJBH,obj.YWLX);
		$("#gcsxForm").setFormValues(obj);
	    getPensonByDep($("#XMGLGS").val());
		$("#XMGLGSYZDB").val(obj.XMGLGSYZDB);
		
	}
}
function setFqlx(){
	$("#FQLX").val(fqlx);
	if(fqlx == 1){
		$("#XMGLGS").attr("disabled",true);
	}else{
		$("#XMGLGS").attr("disabled",false);
	}
	
}

//选中项目名称弹出页
function selectXm(){
	if(fqlx == 1){
		$(window).manhuaDialog({"title":"项目列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/common/xmcx_xmglgs.jsp?xmglgs="+<%=deptId%>,"modal":"2"});
	}else{
		$(window).manhuaDialog({"title":"项目列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/jhb/tcjh/common/xmcx_xmglgs.jsp?xmglgs=","modal":"2"});
		//$(window).manhuaDialog({"title":"项目列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"2"});
	}
}
//弹出区域回调
getWinData = function(data){
	$("#XMID").val(JSON.parse(data).GC_TCJH_XMXDK_ID);
	$("#XMMC").val(JSON.parse(data).XMMC);
	$("#BDID").val(JSON.parse(data).BDID);
	$("#BDMC").val(JSON.parse(data).BDMC);
	$("#JLDW").val(JSON.parse(data).JLDW_SV);//监理单位
	$("#JLDW").attr("code",JSON.parse(data).JLDW);//监理单位ID
	$("#SGDW").val(JSON.parse(data).SGDW_SV);//施工单位
	$("#SGDW").attr("code",JSON.parse(data).SGDW);//施工单位ID
	$("#XMJL").val(JSON.parse(data).FZR_GLGS_SV);//项目经理
	$("#ND").val(JSON.parse(data).ND);//年度
	$("#JHSJID").val(JSON.parse(data).GC_JH_SJ_ID);//计划数据ID
	
	$("#XMGLGS").val(JSON.parse(data).XMGLGS);//项目管理公司
	getPensonByDep($("#XMGLGS").val());//重新初始化业主代表
	$("#GCKGRQ").val(JSON.parse(data).KGSJ_SJ);//工程开工实际时间
	
	$("#GCBYZDB").val(JSON.parse(data).YZDB);//业主代表
	
	$("#GCBYZDBLXFS").val(JSON.parse(data).YZDBLXFS);//业主代表联系方式
	
	$("#SGDWXMJL").val(JSON.parse(data).SGDWXMJL);
	$("#SGDWXMJLLXDH").val(JSON.parse(data).SGDWXMJLLXDH);
	$("#JLDWZJ").val(JSON.parse(data).JLDWZJ);
	$("#JLDWZJLXDH").val(JSON.parse(data).JLDWZJLXDH);
	
};
//过滤负责人
function getPensonByDep(depid){
	var src = "T#VIEW_YW_ORG_PERSON :ACCOUNT:NAME:PERSON_KIND = '3' AND DEPARTMENT = '"+depid+"'";
//	$("#XMGLGSYZDB").attr('src',src);
//	reloadSelectTableDic($("#XMGLGSYZDB"));
}
</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">工程甩项信息
      	<span class="pull-right">
      		<button id="btnSave" class="btn" type="button">保存</button>
  		</span>
      </h4>
     <form method="post" id="gcsxForm"  >
      <table class="B-table" width="100%" id="gcsxList">
      <input type="hidden" id="GC_GCGL_GCSX_ID" fieldname="GC_GCGL_GCSX_ID" name = "GC_GCGL_GCSX_ID"/>
      <input type="hidden" id="XMID" fieldname="XMID" name = XMID/>
      <input type="hidden" id="BDID" fieldname="BDID" name = BDID/>
      <input type="hidden" id="JHSJID" fieldname="JHSJID" name = JHSJID/>
    	<!--  add by cbl start -->
    	<input type="hidden" id="SJBH" fieldname="SJBH" name = SJBH/>
      	<input type="hidden" id="YWLX" fieldname="YWLX" name = YWLX/>
      	<input type="hidden" id="FQLX" fieldname="FQLX" name = FQLX/>
      <!--  add by cbl end -->
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">项目名称</th>
          <td class="right-border bottom-border" width="30%">
          	<input class="span12" id="XMMC" type="text" placeholder="必填" check-type="required" fieldname="XMMC" name = "XMMC"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right">标段名称</th>
          <td class="right-border bottom-border" width="30">
          	<input class="span12" style="width:70%" id="BDMC" type="text" fieldname="BDMC" name = "BDMC"  readonly />
         	<button class="btn btn-link"  type="button" onclick="selectXm()"><i title="选择项目" class="icon-edit"></i></button>
         </td>
         <th width="8%" class="right-border bottom-border text-right disabledTh">项目年度</th>
         <td class="right-border bottom-border" width="16%">
          	<input class="span12" id="ND" type="text" fieldname="ND" name = "ND"  readonly />
         </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">甩项名称</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" placeholder="必填" check-type="required" maxlength="200" id="SXMC" type="text" fieldname="SXMC" name = "SXMC" />
          </td>
          <th width="8%" class="right-border bottom-border text-right">工程部业主代表</th>
          <td class="right-border bottom-border" width="26%">
          	<select class="span12 person" id="GCBYZDB" kind="dic" src="T#VIEW_YW_ORG_PERSON:ACCOUNT:NAME:PERSON_KIND = '3' and DEPARTMENT in (select row_id from VIEW_YW_ORG_DEPT where EXTEND1='2')" fieldname="GCBYZDB" name="GCBYZDB">
	        </select>
          </td>
          <th width="8%" class="right-border bottom-border text-right">联系方式</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" id="GCBYZDBLXFS" type="text" fieldname="GCBYZDBLXFS" name = "GCBYZDBLXFS" />
          </td>
          
        </tr>
        <tr>
        	<th width="8%" class="right-border bottom-border text-right">项目管理公司</th>
          	<td class="right-border bottom-border">
	           	<select class="span12 4characters" id="XMGLGS" kind="dic" src="T#VIEW_YW_XMGLGS:ROW_ID:BMJC" fieldname="XMGLGS" name="XMGLGS">
	     		</select>
          	</td>
          	<th width="8%" class="right-border bottom-border text-right">业主代表</th>
          	<td class="bottom-border right-border">
	            <input class="span12" id="XMGLGSYZDB" name = "XMGLGSYZDB"  fieldname = "XMGLGSYZDB" operation="=" >
          	</td>
          	<th width="8%" class="right-border bottom-border text-right">联系方式</th>
          	<td class="bottom-border right-border">
	            <input class="span12" id="XMGLGSYZDBLXFS" type="text" fieldname="XMGLGSYZDBLXFS" name = "XMGLGSYZDBLXFS" />
          	</td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">施工单位</th>
          <td class="right-border bottom-border" width="26%">
          	<input class="span12" id="SGDW" type="text" fieldname="SGDW" name = "SGDW"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">项目经理</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" id="SGDWXMJL" type="text" fieldname="SGDWXMJL" name = "SGDWXMJL"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" id="SGDWXMJLLXDH" type="text" fieldname="SGDWXMJLLXDH" name = "SGDWXMJLLXDH"  readonly />
          </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right disabledTh">监理单位</th>
          <td class="right-border bottom-border" width="26%">
          	<input class="span12" id="JLDW" type="text" fieldname="JLDW" name = "JLDW"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">项目总监</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" id="JLDWZJ" type="text" fieldname="JLDWZJ" name = "JLDWZJ"  readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right disabledTh">联系方式</th>
          <td class="right-border bottom-border" width="26%">
           	<input class="span12" id="JLDWZJLXDH" type="text" fieldname="JLDWZJLXDH" name = "JLDWZJLXDH"  readonly />
          </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">工程开工日期</th>
          <td class="right-border bottom-border">
          	<input class="span12 date" id="GCKGRQ" type="date" name="GCKGRQ" fieldname="GCKGRQ"/>
          </td>
          <th width="8%" class="right-border bottom-border text-right">甩项申请日期</th>
          <td class="right-border bottom-border">
          	<input class="span12 date" id="SXSQRQ" type="date" name="SXSQRQ" check-type="required" fieldname="SXSQRQ"/>
          </td>
          <th width="8%" class="right-border bottom-border text-right">是否涉及拆迁</th>
	       <td class="right-border bottom-border">
	       	<select class="span12 3characters" id="SFSJPQ" kind="dic" src="SF" check-type="required" noNullSelect ="true" fieldname="SFSJPQ" name="SFSJPQ">
		    </select>
	      </td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">甩项申请内容</th>
          <td class="right-border bottom-border" colspan="6">
          	<textarea class="span12" id="SXSQNR" rows="4" name ="SXSQNR" placeholder="必填" check-type="required" fieldname="SXSQNR" maxlength="1000"></textarea>
          </td>
        </tr>
        <tr>
	     	<th width="8%" class="right-border bottom-border text-right">附件信息</th>
	     	<td colspan="6" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0070">
						<i class="icon-plus"></i>
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody fjlb="0070" class="files showFileTab"
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
	<FORM id="frmPost" name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
       <input type="hidden" name="resultXML" id = "resultXML">
       <input type="hidden" name="ywid" id = "ywid" value="">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
	</FORM>
</div>
</body>
</html>