<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.common.OrgDept"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%
	//获取当前用户信息
	User user = (User) request.getSession().getAttribute(Globals.USER_KEY);
	OrgDept dept = user.getOrgDept();
	String deptId = dept.getDeptID();
	String userid = user.getAccount();
	String sysdate = Pub.getDate("yyyy-MM-dd");
	String info_text=(String)request.getAttribute("info_text");
%>

<app:base/>
<title>工程周报-工程周报管理</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
//请求路径，对应后台RequestMapping
var controllername= "${pageContext.request.contextPath }/gczb/gczbController.do";
var id,bdid,row_index;
var xmglgs = '<%=deptId%>';

//计算本页表格分页数
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#gczbList").attr("pageNum",pageNum);
}

//页面初始化
$(function() {
	hide();
	setPageHeight();
	init();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		var kssj = $("#kssj").val();
		var jssj = $("#jssj").val();
		if(kssj > jssj){
			xInfoMsg('<结束时间>不能早于<开始时间>！');
			return;
		}
		queryList();
	});
	//按钮绑定事件(新增)
	$("#btnInsert").click(function() {
		if($("#gczbList").getSelectedRowIndex()!=-1)
		{
			$("#resultXML").val($("#gczbList").getSelectedRow());
			$(window).manhuaDialog({"title":"工程周报管理>新增","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/zbgl/zbgl_xmwh.jsp","modal":"1"});
		}
		else
		{
			requireSelectedOneRow();
		}	
	});
	//按钮绑定事件(修改)
	$("#btnUpdate").click(function() {
		if($("#gczbList").getSelectedRowIndex()==-1)
		 {
			requireSelectedOneRow();
		    return
		 }
		if(bdid=='')
		{
			$(window).manhuaDialog({"title":"工程周报管理>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/zbgl/zbgl_update.jsp?xmid="+id,"modal":"1"});
		}
		else
		{
			$(window).manhuaDialog({"title":"工程周报管理>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/zbgl/zbgl_update.jsp?bdid="+bdid,"modal":"1"});
		}		
	});
	
	
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        setDefaultNd("ND");
        //setDefaultOption($("#ND"),new Date().getFullYear());
    });

    
	//按钮绑定事件（导出EXCEL）
	$("#btnExpExcel").click(function() {
		  if(exportRequireQuery($("#gczbList"))){//该方法需传入表格的jquery对象
		      printTabList("gczbList");
		  }
	});
	
	
    //按钮绑定事件（导出模板）
	$("#btnExp").click(function() {
		//if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			/* printTabList("DT1"); */
			//printTabList("DT1","pqrwgl.xls","XMBH,XMMC,BDMC,BDDD,GXLB,ZXMMC,PQDD,PQFA,ISJHWC,KGSJ_JH,KGSJ,WCSJ_JH,WCSJ,JZQK,SYGZL,CZWT,JJFA,HTZT,HTZT,HTZT,HTZT,HTZT,HTZT,HTZT,HTZT","2,1"); 
		//}
		$("#myModal").modal("show");
	//	$(window).manhuaDialog({"title":"导出","type":"text","content":g_sAppName+"/jsp/business/gcb/zbgl/zbgl_List_zbmbdc.jsp","modal":"3"});
	});
    
    $("#quxiao").click(function() {
		$("#myModal").modal("hide");
    });
    
    
	 $("#impPq").click(function() {
	    	$("#fileInput").trigger("click");
		
	    });
	 $("#fileInput").change(function() {
	    	$("#importExcel").attr("action",controllername+"?ExcelUpload");
	    	$("#importExcel").submit();
		});
	//自动完成项目名称模糊查询
	showAutoComplete("XMMC",controllername+"?xmmcAutoQuery","getXmmcQueryCondition"); 
});


//生成项目名称查询条件
function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"tcjh"}]}';
	var jsonData = convertJson.string2json1(initData); 
	//项目名称
	if("" != $("#XMMC").val()){
		var defineCondition = {"value": "%"+$("#XMMC").val()+"%","fieldname":"tcjh.XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	if("" != $("#XMGLGS").val()){
		var defineCondition = {"value": +xmglgs,"fieldname":"tcjh.XMGLGS","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}

	/*    	//开始时间
	if("" != $("#kssj").val()){
		var defineCondition = {"value": $("#kssj").val(),"fieldname":"KSSJ","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//结束时间
	if("" != $("#jssj").val()){
		var defineCondition = {"value": $("#jssj").val(),"fieldname":"JSSJ","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
 */ 	return JSON.stringify(jsonData);
}	


//页面默认参数
function init(){
	generateNd($("#ND"));
	setDefaultNd("ND");
	//setDefaultOption($("#ND"),new Date().getFullYear());
	generateNd($("#xmnd"));
	setDefaultOption($("#xmnd"),new Date().getFullYear());
	//g_bAlertWhenNoResult = false;
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gczbList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+xmglgs,data,gczbList,null,true);
	//g_bAlertWhenNoResult = true;
}


//年份查询
function generateNd(ndObj){
	ndObj.attr("src","T#GC_JH_SJ:distinct ND:ND as nnd:SFYX='1' order by nnd asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	ndObj.val(new Date().getFullYear());
}
 
//查询列表
function queryList(){
	//生成json串
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,gczbList);
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+xmglgs,data,gczbList,null,false);
}



//确定
function ok()
{
 	var nd=$("#xmnd").val();
	$("#saveNameExp").attr("href",controllername+"?toDsExcel&nd="+nd);
}



//弹出维护窗口
function OpenMiddleWindow_add(){
	
			
		
}

//详细信息
function rowView(index){
	var obj = $("#gczbList").getSelectedRowJsonByIndex(index);
	var xmbh = eval("("+obj+")").XMBH;
	parent.$("body").manhuaDialog(xmscUrl(xmbh));
}


//页刷新
function gs_feedback(){
	  	row_index=$("#gczbList").getSelectedRowIndex();
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,gczbList);
		var tempJson = convertJson.string2json1(data);
		var a=$("#gczbList").getCurrentpagenum();
		tempJson.pages.currentpagenum=a;
		data = JSON.stringify(tempJson);
		defaultJson.doQueryJsonList(controllername+"?query&xmglgs="+xmglgs,data,gczbList,"gs_feedback_query",true);
}


function gs_feedback_query()
{
	$("#gczbList").setSelect(row_index);
	var json=$("#gczbList").getSelectedRow();
	json=encodeURI(json);
	successInfo("","");
}

//行单击事件
function tr_click(obj){
	if(obj.ISNOBDXM == "0" &&  obj.XMBS == "0"){
		$("#btnInsert").attr("disabled","disabled");
	}else{
		$("#btnInsert").removeAttr("disabled");
	}
	id=obj.GC_TCJH_XMXDK_ID;
	bdid=obj.BDID;
}


//周报图标
function zblb(obj)
{
	var xmid=obj.GC_TCJH_XMXDK_ID;
	var bdid=obj.BDID;
	return '<a href="javascript:void(0);"><i title="周报列表" class="icon-align-justify" onclick="showLb(\''+xmid+'\',\''+bdid+'\')"></i></a>';
}


//周报列表展示界面
function showLb(xmid,bdid)
{
	$(window).manhuaDialog({"title":"工程周报>周报列表","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/zbgl/zblb.jsp?xmid="+xmid+"&bdid="+bdid,"modal":"2"});
 }


//详细信息
function rowView(index){
  	var obj=$("#gczbList").getSelectedRowJsonByIndex(index);//获取行对象
	var xdkid=convertJson.string2json1(obj).GC_TCJH_XMXDK_ID;//取行对象<项目编号>
	$(window).manhuaDialog(xmscUrl(xdkid));//调用公共方法,根据项目编号查询
} 


//判断标段是否有值
function pdbd(obj){
	var bdid = obj.BDID;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
}


//现场图片图标
function viewimg(obj){
	var isfj=obj.XCZP_SV;
	if(isfj!='' && isfj!=undefined)
	{
		return '<a href="javascript:void(0);"><img src=\"/xmgl/images/icon-annex.png\" title="现场照片" onclick="showPic(\''+obj.XCZP+'\')"></a>';
	}	
}


//现场照片
function showPic(ywid)
{
	$(window).manhuaDialog({"title":"现场照片","type":"text","content":g_sAppName +"/jsp/business/gcb/zbgl/viewimg.jsp?ywid="+ywid+"&lb=0311","modal":"2"});
}


//判断标段编号是否有值
function pdbdbh(obj){
	var bdid = obj.BDID;
	if(bdid=='undefinde'||bdid=='')
	{
		return '<div style="text-align:center">—</div>'
	}	
}


//本周完成
function copy_href_bzwc(index){
  	var obj=$("#gczbList").getSelectedRowJsonByIndex(index);//获取行对象
	var zbid=convertJson.string2json1(obj).XCZP;//获取周报id
	$(window).manhuaDialog({"title":"工程周报管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/zbgl/showtext.jsp?zbid="+zbid+"&zdname=bzwc&title=本周完成","modal":"1"});
 }

//累计完成
function copy_href_ljwc(index){
  	var obj=$("#gczbList").getSelectedRowJsonByIndex(index);//获取行对象
	var zbid=convertJson.string2json1(obj).XCZP;//获取周报id
	$(window).manhuaDialog({"title":"工程周报管理","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/zbgl/showtext.jsp?zbid="+zbid+"&zdname=ljwc&title=累计完成","modal":"1"});
}

function goback()
{
	$("#lb_info").show();
	$("#goback").hide();
	$("#dc_log").hide();
}
function hide()
{
	if(<%=info_text==null%>)
	{
		$("#goback").hide();
		$("#dc_log").hide();
		$("#lb_info").show();
	}
	else
	{
		$("#goback").show();
		$("#dc_log").show();
		$("#lb_info").hide();
		
	}	
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<p></p>
			<%
			if(info_text!=null&&!info_text.equals("false"))
			{
			%>
		<div  id="goback" style="position:absolute;margin-left:1000px; margin-top:15px;">
			<button class="btn" onclick="goback()" id="saveName">返回</button>
		</div>
		<div id="dc_log"  style=" margin-top:30px;">
			<table border="0" width="100%"  id="dc_info">
			<% 	
				String[] info = info_text.split(",");
			%>			
			<tr>
				<td width="100%" colspan="5" align="left"><%=info[info.length-7] %></td>
			</tr>
			<tr>
				<td height="10" colspan="5"></td>
			</tr>
			<%			
				
				for(int m=0;m<info.length-7;m=m+6)
				{
					if(info[m+5].equals("1"))
					{
			%>
				<tr>	
			<%}else{ %>
				<tr  style="background-color:green;">
			<%}%>		
				<td width="10%" align="center"><%=info[(m)] %></td>
				<td width="20%" align="center"><%=info[(m+1)] %></td>
				<td width="15%" align="left"><%=info[(m+2)] %></td>
				<td width="20%" align="center"><%=info[(m+3)] %></td>
				<td width="35%" align="left"><%=info[(m+4)] %></td>
			</tr>
			<% 
				}
			%>			
			</table>
		</div>
			<%}%>
		<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-body">
			<table class="B-table">
				<tr>
					<th width="50%" class="right-border bottom-border">项目年度</th>
					<td class="right-border bottom-border">
						<select	id="xmnd" class="span12 year" name="xmnd" fieldname="ND"  noNullSelect="true" defaultMemo="全部" operation="="></select>
					</td>
				</tr>
			</table>
		</div>
		<div class="modal-footer">
		<%-- ${pageContext.request.contextPath }/gczb/gczbController.do?toDsExcel&nd=2014 --%>
			<a href="#" role="button" class="btn" style="font-weight:800;" id="quxiao">取消</a>
			<a href="#" role="button" class="btn btn-inverse" onclick="ok()" style="font-weight:800;" id="saveNameExp" download="">确定</a>
			<!-- <button class="btn" onclick="ok()" id="saveName">确定</button> -->
			<!-- <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button> -->
		</div>
	</div>
	
	<div class="row-fluid"  id="lb_info" style="display:none;">
		<div class="B-small-from-table-autoConcise">
			<h4 class="title">工程周报管理
				<span class="pull-right">
					<app:oPerm url="jsp/business/gcb/zbgl/zbgl_xmwh.jsp">
						<button id="btnInsert" class="btn" type="button">新增</button>
					</app:oPerm>				
					<app:oPerm url="jsp/business/gcb/zbgl/zbgl_update.jsp">
						<button id="btnUpdate" class="btn" type="button">维护</button>
					</app:oPerm>
					<button class="btn" id="btnExpExcel">列表导出</button>
					<button id="btnExp" class="btn" type="button">周报模版导出</button>
					<button id="impPq" class="btn" type="button">周报信息导入</button>				
				</span>
			</h4>
			<form method="post" id="queryForm"  >
      			<table class="B-table" width="100%">
      				<!--可以再此处加入hidden域作为过滤条件 -->
			      	<TR  style="display:none;">
				        <TD class="right-border bottom-border"></TD>
						<TD class="right-border bottom-border">
							<INPUT type="text" class="span12" kind="text" id="num" fieldname="rownum" value="1000" operation="<="/>
						</TD>
			        </TR>
        			<!--可以再此处加入hidden域作为过滤条件 -->
					<tr>
						<th width="2%" class="right-border bottom-border text-right">年度</th>
						<td width="1%" class="right-border bottom-border">
							<select	id="ND" class="span12 year" name="ND" fieldname="ND" defaultMemo="全部" operation="="></select>
						</td>
						<th width="2%" class="right-border bottom-border text-right">项目名称</th>
						<td width="20%" class="right-border bottom-border">
							<input id="XMMC" class="span12" type="text" tablePrefix="tcjh" name = "QXMMC" fieldname = "tcjh.XMMC" operation="like"  autocomplete="off">
						</td>
						<td width="30%" class="text-left bottom-border text-right">
							<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
							<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
						</td>
         			</tr>
      			</table>
      		</form>
			<div style="height:5px;"> </div>
			<div class="overFlowX">
				<table class="table-hover table-activeTd B-table" id="gczbList" width="100%" type="single" pageNum="10" printFileName="工程周报管理">
					<thead>
						<tr>
							<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" rowspan="2" colindex=2 tdalign="center" noprint="true" CustomFunction="zblb"></th>
							<th fieldname="XMBH" rowspan="2" colindex=3 hasLink="true" linkFunction="rowView" rowMerge="true">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" rowspan="2" colindex=4 maxlength="15" rowMerge="true">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDBH" rowspan="2" colindex=5 CustomFunction="pdbdbh">&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC" rowspan="2" colindex=6 maxlength="15" CustomFunction="pdbd">&nbsp;标段名称&nbsp;</th>
							<th fieldname="XMBDDZ" rowspan="2" colindex=7 maxlength="15" >&nbsp;项目地址&nbsp;</th>
							<th fieldname="XCZP" rowspan="2" colindex=8 tdalign="center" noprint="true" CustomFunction="viewimg">&nbsp;现场照片&nbsp;</th>
							<th fieldname="QTWJ" rowspan="2" colindex=9 tdalign="center" noprint="true">&nbsp;其他文件&nbsp;</th>							
							<th fieldname="KSSJ" rowspan="2" colindex=10 tdalign="center">&nbsp;开始时间&nbsp;</th>
							<th fieldname="JSSJ" rowspan="2" colindex=11 tdalign="center">&nbsp;结束时间&nbsp;</th>
 							<th colspan="2">工程形象进度</th>
 							<th colspan="2">周计量（万元）</th>
 						</tr>
 						<tr>
							<th fieldname="BZWC" maxlength="15" colindex=12 hasLink="true" linkFunction="copy_href_bzwc" >&nbsp;本周完成&nbsp;</th>
							<th fieldname="LJWC" maxlength="15" colindex=13 hasLink="true" linkFunction="copy_href_ljwc" >&nbsp;累计完成&nbsp;</th>
							<th fieldname="ZJLND" colindex=14 tdalign="right">&nbsp;本年完成&nbsp;</th>
							<th fieldname="ZJLLJWC" colindex=15 tdalign="right">&nbsp;累计完成（含总工程量）&nbsp;</th> 							
 						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<div align="center">
	<FORM name="frmPost" method="post" style="display:none" target="_blank">
 	<!--系统保留定义区域-->
       <input type="hidden" name="queryXML" id = "queryXML">
       <input type="hidden" name="txtXML" id = "txtXML">
      <input type="hidden" name="txtFilter" order="asc" fieldname="xmbh,xmbs,pxh ,bdbh"/>
       <input type="hidden" name="resultXML" id = "resultXML">
     		 <!--传递行数据用的隐藏域-->
        <input type="hidden" name="rowData">
        <input type="hidden" name="queryResult" id="queryResult"/>
	</FORM>
	<form method="post" action="" id="loginForm" ></form>
	<form id="importExcel" action="" style="display: none" method="post" enctype="multipart/form-data">
		<input id="fileInput"  name="myFile" type="file" value="上传excel"/> <br/> 
	</form>	
</div>
</body>
</html>