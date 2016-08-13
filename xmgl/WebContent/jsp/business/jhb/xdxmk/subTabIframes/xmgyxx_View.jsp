<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.lang.StringBuffer"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.common.*"%>
<%@ page import="com.ccthanking.framework.util.*"%>
<%@ page import="java.sql.ResultSet"%>
<app:base/>
<title>项目手册</title>
<%
	String id = request.getParameter("id");
	Connection conn = DBUtil.getConnection();//定义连接
	StringBuffer sbSql = null;//sql语句字符串
	String sql = "";
	BaseResultSet bs = null;
  	ResultSet rs = null;
  	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String deptName = dept.getDept_Name();
%>
<style type="text/css">
.inputCss 
{
	border-style:none;
	background:#FFF;
	box-shadow:none;
}

</style>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/xdxmk/xdxmkController.do";
var id = "<%=id%>";
var bmid = '<%=deptId%>';
//页面初始化
$(function() {
	init();
	
});

	
//页面默认参数
function init(){
	//alert(id);
	$("#id_jb").val(id);
	//生成json串
	var data = null;
	//调用ajax插入
	//var a = defaultJson.doQueryJsonList(controllername+"?queryById&id="+id,data,jcxxList);
	submit(controllername+"?queryById&id="+id,data,jcxxList);

	
}
function submit(actionName, data,tablistID){
	$.ajax({
		type : 'post',
		url : actionName,
		data : data,
		cache : false,
		dataType : "json",  
		async :	true,
		success : function(result) {
			var rowObj = convertJson.string2json1(result.msg).response.data[0];
			$("#jcxxForm").setFormValues(rowObj);
			//$("input[type='text']").addClass("inputCss");
			if(rowObj.BDID != ""){
				$("#cjdw").hide();
				$("#cj_sjdw").hide();
				$("#x_t_sjdw").hide();
				$("#cj_sgdw").hide();
				$("#x_t_sgdw").hide();
				$("#cj_jldw").hide();
				$("#x_t_jldw").hide();
				$("#bdxx").show();	
				$("#title_bdxx").show();
				$("#t_bdxx").show();
			}else{
				$("#cjdw").show();
				$("#cj_sjdw").show();
				$("#x_t_sjdw").show();
				$("#cj_sgdw").show();
				$("#x_t_sgdw").show();
				$("#cj_jldw").show();
				$("#x_t_jldw").show();
				$("#bdxx").hide();	
				$("#title_bdxx").hide();
				$("#t_bdxx").hide();
			}
			if(rowObj.XJXJ == 1){
				$("#xj_xmid").show();
				var val = rowObj.XJ_XMID;
				var text = "";
				var nd = "";
				if(val == ""){
					text = "暂无关联项目";
					$("#xj_xmid").text(text);
				}else{
					nd = getNdByXjxm(val);
					text = "  "+nd+"年项目信息 ";
					$("#xj_xmid_text").text(text);
				}
				
			}else{
				$("#xj_xmid").hide();
			}
			$("#resultXML").val(JSON.stringify(rowObj));
			queryFileData(id,"","","");
		}
	});
}
//点击项目地址图标
function selectDz(){
	window.open("${pageContext.request.contextPath }/jsp/business/jhb/xdxmk/img/earth.png");
}
//点击续建项目超链接
function showXjXdkxm(obj){
	var xjid = $("#xj_xmid_val").val();
	$(window).manhuaDialog(xmscUrl(xjid));
	//$(window).manhuaDialog({"title":"项目手册131131","type":"text","content":g_sAppName+"/jsp/business/jhb/xdxmk/rowView.jsp?id="+xjid,"model":"2"});
}
//续建项目查询对应年度
function getNdByXjxm(id){
	var nd = "";
	$.ajax({
		type : 'post',
		url : controllername+"?queryById&id="+id,
		data : null,
		cache : false,
		dataType : "json",  
		async :	false,
		success : function(result) {
			var rowObj = convertJson.string2json1(result.msg).response.data[0];
			nd = rowObj.ND;
		}
	});
	return nd;
}
</script>      
    
</head>
<body>
<app:dialogs/>
<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="jcxxForm"  >
     <h4 class="title">项目概要信息</h4>
        <span class="pull-right">
     	</span>
      <table class="B-table" width="100%" id="jcxxList">
      <input type="hidden" id="id_jb" name = "id_jb" fieldname="GC_TCJH_XMXDK_ID"/>
        <tr>
			<th width="5%" class="right-border bottom-border text-right">项目编号</th>
			<td class="right-border bottom-border" colspan=3>
				<input class="span12" type="text" fieldname="XMBH" style="border-style:none;background:#FFF;box-shadow:none;"  name = "XMBH" disabled>
			</td>
        	<th width="5%" class="right-border bottom-border text-right">项目年度</th>
			<td class="bottom-border right-border">
			  <input class="span12" type="text" fieldname="ND" name = "ND" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
			<td class="bottom-border right-border" colspan = "2" rowspan = "4"  style="text-align: center">
			  	<%-- <a	href="javascript:openTwoDimensionCode('<%=id%>');" > --%>
     			<img src='${pageContext.request.contextPath }/fileUploadController.do?getFileEwm&id=<%=id%>'    alt="项目二维码" style="width:180px;" />
     			<!-- </a> -->
			</td>
        </tr>
         <tr>
			<th width="5%" class="right-border bottom-border text-right">项目名称</th>
			<td class="bottom-border right-border" colspan="3">
			  <input class="span12" type="text" fieldname="XMMC" name = "XMMC" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
			<th width="5%" class="right-border bottom-border text-right">项目管理公司</th>
       		<td class="bottom-border right-border">
       			<input class="span12" type="text" fieldname="XMGLGS" name = "XMGLGS" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
      		</td>
        </tr>
        <tr>
			<th width="5%" class="right-border bottom-border text-right">项目属性</th>
			<td class="right-border bottom-border">
			  	<input class="span12" type="text" fieldname="XMSX" name = "XMXS" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
			<th width="5%" class="right-border bottom-border text-right">项目类型</th>
		       <td class="bottom-border right-border">
		       	<input class="span12" type="text" fieldname="XMLX" name = "XMLX" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
         	<th width="5%" class="right-border bottom-border text-right">是否BT</th>
         	<td class="right-border bottom-border">
	          	<input class="span12" type="text" fieldname="ISBT" name = "ISBT" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
	        </td>
	        
        </tr>
        <tr>
			<th width="5%" class="right-border bottom-border text-right">项目性质</th>
		       <td class="bottom-border right-border">
		       	<input class="span12" id="XJXJ" type="text" fieldname="XJXJ" name = "XJXJ" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
			<td id="xj_xmid" class="bottom-border right-border" colspan="2">
				<input type ="hidden" id="xj_xmid_val" fieldname="XJ_XMID" name="XJ_XMID"/>
				<a id="xj_xmid_text" href="javascript:void(0);" onclick="showXjXdkxm()"></a>
			</td>
			
        </tr>
		<tr>
	        <th width="5%" class="right-border bottom-border text-right">项目地址</th>
	        <td class="right-border bottom-border" colspan="7" >
	        	<input class="span12" style="width:95%;border-style:none;background:#FFF;box-shadow:none;" type="text" fieldname="XMDZ" name = "XMDZ" readOnly />
	        	<button class="btn btn-link"  type="button" onclick="selectDz()"><i title="查看" class="icon-map-marker"></i></button>
	        </td>
         </tr>
        <tr>
			<th width="5%" class="right-border bottom-border text-right">建设内容及规模</th>
			<td class="bottom-border right-border" colspan="7" >
				<textarea class="span12" rows="3" name ="JSNR" fieldname="JSNR" style="border-style:none;background:#FFF;box-shadow:none;" disabled></textarea>
			</td>
        </tr>
        <tr>
        	<th width="5%" class="right-border bottom-border text-right">年度建设任务</th>
			<td class="bottom-border right-border">
				<textarea class="span12" rows="3" name ="JSMB"  fieldname="JSRW" style="border-style:none;background:#FFF;box-shadow:none;" readOnly></textarea>
			</td>
			<th width="5%" class="right-border bottom-border text-right">年度建设目标</th>
			<td class="bottom-border right-border">
				<textarea class="span12" rows="3" name ="JSMB"  fieldname="JSMB" style="border-style:none;background:#FFF;box-shadow:none;" readOnly></textarea>
			</td>
			<th width="5%" class="right-border bottom-border text-right">建设主体</th>
			<td class="bottom-border right-border" colspan="3" >
				<textarea class="span12" rows="3" name ="JSZT"  fieldname="JSZT" style="border-style:none;background:#FFF;box-shadow:none;" readOnly></textarea>
			</td>
		</tr>
		<tr>
			<th width="5%" class="right-border bottom-border text-right">业主代表</th>
			<td class="bottom-border right-border">
				<input class="span12" type="text" fieldname="YZDB" name = "YZDB" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
			<th width="5%" class="right-border bottom-border text-right">联系方式</th>
			<td class="bottom-border right-border">
				<input class="span12" type="text" fieldname="LXFS_YZDB" name = "LXFS_YZDB" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
			<th width="5%" class="right-border bottom-border text-right">项目管理公司负责人</th>
			<td class="bottom-border right-border">
				<input class="span12" type="text" fieldname="FZR_GLGS" name = "FZR_GLGS" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
			<th width="5%" class="right-border bottom-border text-right">联系方式</th>
			<td class="bottom-border right-border">
				<input class="span12" type="text" fieldname="LXFS_GLGS" name = "LXFS_GLGS" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
        </tr>
        
      </table>
      <br>
      <h4 id="cjdw" class="title">参建单位概要信息</h4>
      <h5 id="cj_sjdw" class="black">设计单位</h5>
      <table id="x_t_sjdw" class="B-table" width="100%">
      	<tr>
        	<th width="8%" class="right-border bottom-border text-right">单位名称</th>
			<td class="bottom-border right-border" width="42%">
				<input class="span12" type="text" fieldname="SJDW" name = "SJDW" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
        	<th width="8%" class="right-border bottom-border text-right">设计负责人</th>
			<td class="bottom-border right-border" width="17%">
				<input class="span12" type="text" fieldname="FZR_SJDW" name = "FZR_JLDW" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
			<th width="8%" class="right-border bottom-border text-right">联系电话</th>
			<td class="bottom-border right-border" width="17%">
				<input class="span12" type="text" fieldname="LXFS_SJDW" name = "LXFS_JLDW" style="border-style:none;background:#FFF;box-shadow:none;" readOnly/>
			</td>
        </tr>
      </table>
      <h5 id="cj_sgdw" class="black">施工单位</h5>
      <table id="x_t_sgdw" class="B-table" width="100%">
        <tr>
          <th width="8%" class="right-border bottom-border text-right" rowspan="2">单位名称 </th>
          <td class="right-border bottom-border" width="42%" rowspan="2">
          	<input class="span12"  type="text" fieldname="SGDW" name = "SGDW" style="border-style:none;background:#FFF;box-shadow:none;" readonly />
          </td>
          <th width="8%" class="right-border bottom-border text-right">项目经理</th>
          <td class="bottom-border" width="17%">
          	<input class="span12" type="text" name = "SGDWXMJL" fieldname= "SGDWXMJL" style="border-style:none;background:#FFF;box-shadow:none;" readOnly />
          </td>
          <th width="8%" class="right-border bottom-border text-right">联系电话</th>
          <td class="bottom-border" width="17%">
          	<input class="span12" type="text" name = "SGDWXMJLLXDH" fieldname= "SGDWXMJLLXDH" style="border-style:none;background:#FFF;box-shadow:none;" readOnly />
          </td>
       	</tr>
       	<tr>
       	  <th width="8%" class="right-border bottom-border text-right">技术负责人</th>
          <td class="bottom-border" width="17%">
          	<input class="span12" type="text" name = "SGDWJSFZR" fieldname= "SGDWJSFZR" style="border-style:none;background:#FFF;box-shadow:none;" readOnly />
          </td>
          <th width="8%" class="right-border bottom-border text-right">联系电话</th>
          <td class="bottom-border" width="17%">
          	<input class="span12" type="text" name = "SGDWJSFZRLXDH"  fieldname= "SGDWJSFZRLXDH" style="border-style:none;background:#FFF;box-shadow:none;" readOnly />
          </td>
       	</tr>
       </table>
       <h5 id="cj_jldw" class="black">监理单位</h5>
    <table id="x_t_jldw" class="B-table" width="100%">
     <tr>
         <th width="8%" class="right-border bottom-border text-right">单位名称 </th>
         <td class="right-border bottom-border" colspan="3" width="42%">
         	<input class="span12"  type="text" fieldname="JLDW" name = "JLDW" style="border-style:none;background:#FFF;box-shadow:none;" readonly />
         </td>
         <th width="8%" class="right-border bottom-border text-right">总监</th>
         <td  class="bottom-border" width="17%">
         	<input class="span12" type="text" name = "JLDWZJ"  fieldname= "JLDWZJ" style="border-style:none;background:#FFF;box-shadow:none;" readonly/>
         	</td>
         <th width="8%" class="right-border bottom-border text-right">联系电话</th>
         <td class="bottom-border" width="17%">
         	<input class="span12" type="text" name = "JLDWZJLXDH" fieldname= "JLDWZJLXDH" style="border-style:none;background:#FFF;box-shadow:none;" readonly/>
         </td>
      	</tr>
      	<tr>
      	 <th width="8%" class="right-border bottom-border text-right">总监代表</th>
         <td  class="bottom-border" width="17%">
         	<input class="span12" type="text" name = "JLDWZJDB"  fieldname= "JLDWZJDB" style="border-style:none;background:#FFF;box-shadow:none;" readonly/>
         	</td>
         <th width="8%" class="right-border bottom-border text-right">联系电话</th>
         <td class="bottom-border" width="17%">
         	<input class="span12" type="text" name = "JLDWZJDBLXDH" fieldname= "JLDWZJDBLXDH" style="border-style:none;background:#FFF;box-shadow:none;" readonly/>
         </td>
         <th width="8%" class="right-border bottom-border text-right">安全监理</th>
         <td  class="bottom-border" width="17%">
         	<input class="span12" type="text" name = "JLDWAQJL" fieldname= "JLDWAQJL" style="border-style:none;background:#FFF;box-shadow:none;" readonly/>
         	</td>
         <th width="8%" class="right-border bottom-border text-right">联系电话</th>
         <td class="bottom-border" width="17%">
         	<input class="span12" type="text" name = "JLDWAQJLLXDH" fieldname= "JLDWAQJLLXDH" style="border-style:none;background:#FFF;box-shadow:none;" readonly/>
         </td>
      	</tr>
  </table>
  		<%
  		String bdCount="";
      	sbSql = new StringBuffer();
      	sbSql.append("SELECT COUNT(GC_XMBD_ID) FROM GC_XMBD WHERE SFYX ='1' AND XMID = '"+id+"'");
	  	sql = sbSql.toString();
	  	String[][] bdCountArray = DBUtil.query(conn, sql);
	       if(Pub.emptyArray(bdCountArray)){
	    	   bdCount = bdCountArray[0][0].toString();
	       }else{
	    	   bdCount = "0";
	       }
      %>
      
  		<h4 id="bdxx" class="title">标段概要信息（共<%=bdCount %>个标段）
  		</h4>
      <%
      	sbSql = new StringBuffer();
      	sbSql.append("SELECT ");
	  	sbSql.append("T.BDMC||'|'||T.BDBH AS BDMC,T.SGDWFZRLXFS,T.JLDWFZRLXFS,T.SJDWFZRLXFS,T.SGDWFZR,T.JLDWFZR,T.SJDWFZR,T.SGDWXMJL,T.SGDWXMJLLXDH,T.SGDWJSFZR,T.SGDWJSFZRLXDH,T.JLDWZJ,T.JLDWZJLXDH,T.JLDWZJDB,T.JLDWZJDBLXDH,T.JLDWAQJL,T.JLDWAQJLLXDH, ");
	  	sbSql.append("(SELECT DWMC FROM GC_CJDW WHERE GC_CJDW_ID = T.SJDW)AS SJDW,");
	  	sbSql.append("(SELECT DWMC FROM GC_CJDW WHERE GC_CJDW_ID = T.SGDW)AS SGDW,");
	  	sbSql.append("(SELECT DWMC FROM GC_CJDW WHERE GC_CJDW_ID = T.JLDW)AS JLDW");
	  	sbSql.append(" FROM ");
	  	sbSql.append("GC_XMBD T ");
	  	sbSql.append("WHERE T.XMID = '"+id+"'");
	  	sql = sbSql.toString();
	  	bs = DBUtil.query(conn, sql, null);
	  	rs = bs.getResultSet();
	  	while(rs.next()){
      %>
      
      <h5 id="title_bdxx" class="black"><%=Pub.empty(rs.getString("BDMC")) ? "":rs.getString("BDMC") %></h5>
      <table id="t_bdxx" class="B-table" width="100%">
      	<tr>
      		<th width="8%" class="right-border bottom-border text-right">设计单位</th>
			<td class="bottom-border right-border" colspan="3" width="42%">
				<%=Pub.empty(rs.getString("SJDW")) ? "":rs.getString("SJDW") %>
			</td>
			<th width="8%" class="right-border bottom-border text-right">设计负责人</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("SJDWFZR")) ? "":rs.getString("SJDWFZR") %>
			</td>
			<th width="8%" class="right-border bottom-border text-right">联系方式</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("SJDWFZRLXFS")) ? "":rs.getString("SJDWFZRLXFS") %>
			</td>
      	</tr>
      	<tr>
      		<th width="8%" class="right-border bottom-border text-right" rowspan="2">施工单位</th>
			<td class="bottom-border right-border" colspan="3" rowspan="2" width="45%">
				<%=Pub.empty(rs.getString("SGDW")) ? "":rs.getString("SGDW") %>
			</td>
			<th width="8%" class="right-border bottom-border text-right">项目经理</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("SGDWXMJL")) ? "":rs.getString("SGDWXMJL") %>
			</td>
			<th width="8%" class="right-border bottom-border text-right">联系方式</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("SGDWXMJLLXDH")) ? "":rs.getString("SGDWXMJLLXDH") %>
			</td>
      	</tr>
      	<tr>
      		<th width="8%" class="right-border bottom-border text-right">技术负责人</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("SGDWJSFZR")) ? "":rs.getString("SGDWJSFZR") %>
			</td>
			<th width="8%" class="right-border bottom-border text-right">联系方式</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("SGDWJSFZRLXDH")) ? "":rs.getString("SGDWJSFZRLXDH") %>
			</td>
      	</tr>
      	<tr>
      		<th width="8%" class="right-border bottom-border text-right">监理单位</th>
			<td class="bottom-border right-border" width="17%" colspan="3">
				<%=Pub.empty(rs.getString("JLDW")) ? "":rs.getString("JLDW") %>
			</td>
			<th width="8%" class="right-border bottom-border text-right">总监</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("JLDWZJ")) ? "":rs.getString("JLDWZJ") %>
			</td>
			<th width="8%" class="right-border bottom-border text-right">联系方式</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("JLDWZJLXDH")) ? "":rs.getString("JLDWZJLXDH") %>
			</td>
      	</tr>
      	<tr>
      		<th width="8%" class="right-border bottom-border text-right">总监代表</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("JLDWZJDB")) ? "":rs.getString("JLDWZJDB") %>
			</td>
      		<th width="8%" class="right-border bottom-border text-right">联系方式</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("JLDWZJDBLXDH")) ? "":rs.getString("JLDWZJDBLXDH") %>
			</td>
			<th width="8%" class="right-border bottom-border text-right">安全监理</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("JLDWAQJL")) ? "":rs.getString("JLDWAQJL") %>
			</td>
			<th width="8%" class="right-border bottom-border text-right">联系方式</th>
			<td class="bottom-border right-border" width="17%">
				<%=Pub.empty(rs.getString("JLDWAQJLLXDH")) ? "":rs.getString("JLDWAQJLLXDH") %>
			</td>
      	</tr>
      </table>
      <br>
      <% } %>
      <h4 id="cjdw" class="title">项目方案图</h4>
      <table id="x_t_xmfat" width="100%" style="border:0">
      	<tr>
			<td align="center">
				<div>
					<table role="presentation" class="table table-striped" style="border-left:0;border-top:0;align:center">
						<tbody fjlb="0035" showType="type:image;width:200px;height:200px;attr:multi;noborder:true;maxnum:2;i-align:center;preview:true;"
							class="files showFileTab" data-toggle="modal-gallery"
							data-target="#modal-gallery">
						</tbody>
					</table>
				</div>
			</td>
        </tr>
      </table>
      </form>
    </div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>