<!DOCTYPE HTML>
<html lang="en">
  <head>
	<%@ page language="java" pageEncoding="UTF-8"%>
	<%@ taglib uri= "/tld/base.tld" prefix="app" %>
	<%@ page import="com.ccthanking.framework.common.User"%>
    <%@ page import="com.ccthanking.framework.Globals"%>
	<app:base />
    <title></title>
<%
String json =request.getParameter("json");
User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
String username = user.getName();
String bdmc =request.getParameter("bdmc");
%>		
</head>
  <script>
	var controllername= "${pageContext.request.contextPath }/sjgl/bgsf/sfController.do";
	//获取父页面的值
	var id;
	var json=<%=json%>;
	var xmmc,bdmc;
	var a;
	//页面赋值
	function setValue(){
		setValueByArr(json,["XMMC","BDMC","JHID","ND","XMID","BDID","JHSJID"]);
	    xmmc=$("#XMMC").val();
        bdmc=$("#BDMC").val();
        var jhsj=$("#JHSJID").val();
        $("#JHSJ").val(jhsj);
        xmxx();
	  }
	function ready() {
		setValue();
  		g_bAlertWhenNoResult=false;	
		var data = combineQuery.getQueryCombineData(queryForm1,frmPost1,DT11);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername + "?querySf",data,DT11);
		g_bAlertWhenNoResult=true;	
   };
    $(function() 
     {
    	//页面初始化（左侧默认查询）
    	ready();
			//左侧查询条件清空
			var cleabtn=$("#clean_btn");
				cleabtn.click(function(){
					$("#queryForm1").clearFormResult();
				});
	         //左侧保存
			var btn = $("#save");
			btn.click(function() {
				id=$("#ID").val();
				 if(id==null||id=="")
					{
					 	if($("#demoForm1").validationButton())
						{
			 				//生成json串
			 				var data = Form2Json.formToJSON(demoForm1);
			 				//组成保存json串格式
			 				var data1 = defaultJson.packSaveJson(data);
			 				defaultJson.doInsertJson(controllername+"?insertSf",data1,DT11);
			 				//$("#demoForm1").clearFormResult();
			 				$("#DT11").setSelect(0);
			 				var value=$("#DT11").getSelectedRow();
			 			    var temp=convertJson.string2json1(value);
			 			    $("#demoForm1").setFormValues(temp);
			 			    $('#ZLLJSD').val($(temp).attr("GC_SJ_BGSF_JS_ID"));
			 			    $("#demoForm2").setFormValues(temp);
			 			    $("#LQFS").val("");
			 				var fuyemian1=parent.$("body").manhuaDialog.getParentObj();
			 				fuyemian1.xiugaihang();
						}else{
					  		defaultJson.clearTxtXML();
						}
					}
				 else
					 {
			 		 	if($("#demoForm1").validationButton())
						{
			 		 		//生成json串
			 		 		var data = Form2Json.formToJSON(demoForm1);
			 				//组成保存json串格式
			 				var data1 = defaultJson.packSaveJson(data);
			 				//调用ajax插入
			 				defaultJson.doUpdateJson(controllername + "?updateSf", data1,DT11);
			 				var fuyemian1=parent.$("body").manhuaDialog.getParentObj();
			 				fuyemian1.xiugaihang();
						}else{
					  		defaultJson.clearTxtXML();
						}
					 }
			  });
			//左侧表单清空
			var clearbtn = $("#clear");
			clearbtn.click(function() {
				$("#demoForm1").clearFormResult();
				$("#DT11").cancleSelected();
				$('#ZLLJSD').val('');
				doquery();
			  });
	        //左侧查询
			var querybtn = $("#query");
			querybtn.click(function()
			  {		
	 				 var data = combineQuery.getQueryCombineData(queryForm1,frmPost1,DT11);
	 				//调用ajax插入
	 				defaultJson.doQueryJsonList(controllername + "?querySf",data,DT11);
					$('#ZLLJSD').val('');
					doquery();
					$("#demoForm1").clearFormResult();
			  });
			//右侧表单清空
			var newbtn = $("#clean");
			newbtn.click(function() {
				$("#demoForm2").clearFormResult();
				$("#DT2").cancleSelected();
				user();
    		});
			 //右侧保存
			var insertbtn = $("#insert");
			insertbtn.click(function() {
				var sfxxid=$('#LQID').val();
				var sfid=$('#ZLLJSD').val();
				if(sfid==null||sfid==""){
					requireSelectedOneRow();
					return;
				}
				 if(sfxxid==null||sfxxid=="")
					{
					 	if($("#demoForm2").validationButton())
						{
			 				//生成json串
			 				var data = Form2Json.formToJSON(demoForm2);
			 				//组成保存json串格式
			 				var data1 = defaultJson.packSaveJson(data);
			 					defaultJson.doInsertJson(controllername+"?insertSfxx",data1,DT2);
			 					$("#DT2").setSelect(0);
				 				var value=$("#DT2").getSelectedRow();
				 			    var temp=convertJson.string2json1(value);
				 			    $("#demoForm2").setFormValues(temp);
						}else{
					  		defaultJson.clearTxtXML();
						}
					}
				 else
					 {
			 		 	if($("#demoForm2").validationButton())
						{
			 		 		//生成json串
			 		 		var data = Form2Json.formToJSON(demoForm2);
			 				//组成保存json串格式
			 				//alert(data);
			 				var data1 = defaultJson.packSaveJson(data);
			 				//调用ajax插入
			 				defaultJson.doUpdateJson(controllername + "?updateSfxx", data1,DT2);
						}else{
					  		defaultJson.clearTxtXML();
						}
					 }
			  });
     });
    //行选
	function tr_click(obj,DT)
	{	
		if(DT=="DT11"){
		var hang=$("#DT11").getSelectedRow();
		if(hang=="-1"){
			$("#demoForm1").clearFormResult();
			$('#ZLLJSD').val('');
			doquery();
		}else{
			$('#ZLLJSD').val($(obj).attr("GC_SJ_BGSF_JS_ID"));
			$("#demoForm1").setFormValues(obj);
			$("#demoForm2").setFormValues(obj);
			$("#demoForm2").clearFormResult();
//			a=$("#DT11").getSelectedRowIndex();
			user();
			doquery();
			}
		}
		if(DT=="DT2"){
			var hang1=$("#DT2").getSelectedRow();
			if(hang1=="-1"){
				$("#demoForm2").clearFormResult();
			}else{
//			$("#DT11").setSelect(1);
			$("#demoForm2").setFormValues(obj);
			}
		}
	}
    //左侧行选执行右侧查询
    function doquery(){
    	var ZLLJSD=$('#ZLLJSD').val();
			g_bAlertWhenNoResult=false;	
			var data4 = combineQuery.getQueryCombineData(queryForm2,frmPost2,DT2);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername + "?querySfxx&ZLLJSD="+ZLLJSD,data4,DT2);
			g_bAlertWhenNoResult=true;
    }
	//默认办理人员
	function user(){
		var blr="<%= username%>";
		$("#BLR").val(blr);
	};
	//显示项目信息
	function xmxx(){
		var xm=document.getElementsByTagName("font");
		if (bdmc==null||bdmc==""){
			xm[0].innerHTML = xmmc;	
		}else{
			xm[0].innerHTML = xmmc;	
			xm[1].innerHTML = bdmc;
		}
	};
</script>

<body id="a">
<div class="row-fluid">
<div class="span7"><!-- 第一页开始 -->
<blockquote>
 <app:dialogs/>
<div class="container-fluid">
 <p></p>
 <!-- 第一页查询条件开始 -->
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
    <p></p>
     <%if("".equals(bdmc)) {%>
     		<h5 style="color:black;">当前项目为：<font class="title" style="color:red;">&nbsp;</font></h5>
     	<%}else{ %>
     		 <h5 style="color:black;">当前项目为：<font class="title" style="color:red;">&nbsp;</font>,<font class="title" style="color:red;">&nbsp;</font></h5>
     	<%} %>
    <form method="post" id="queryForm1"  >
      <table class="B-table" width="100%">
                               <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
      	<!-- <TR> -->
			<TD><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true"></TD>
			<TD><INPUT type="text" class="span12" kind="text" name="JHSJ" id="JHSJ" fieldname="JHSJID"  operation="=" keep="true"></TD>
		</TR>	
                               <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
	          <th width="5%" class="right-border bottom-border text-right" >报告类别</th>
	          <td width="10%" class="left-border bottom-border">
                  <select class="span12" id="BGLB" name = "BGLB" defaultMemo="全部"  fieldname="BGLB" kind="dic" src="BGLB" operation="=">
                  </select>
	          </td>
        	<td width="50%" class="text-left bottom-border text-right">
           	<button id="query" class="btn btn-link"  type="button" ><i class="icon-search"></i>查询</button>
           	<button id="clean_btn" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
      </table>
      </form>
    <div style="height:5px;"></div>
	<div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT11" type="single" pageNum="5">
                <thead>
                   <tr>
	                    <th name="XH" id="_XH" width="5%">&nbsp;#&nbsp;</th>
						<th fieldname="BGLB" tdalign="center" width="30%">&nbsp;报告类别&nbsp;</th>
						<th fieldname="JSRQ" tdalign="center" width="30%">&nbsp;接收日期&nbsp;</th>
						<th fieldname="FS" tdalign="right" width="15%" maxlength="10">&nbsp;接收份数&nbsp;</th>
						<th fieldname="JSR" width="20%">&nbsp;接收人&nbsp;</th>
                    </tr> 
                </thead>
             <tbody>
          </tbody>
      </table>
   </div>
</div>
</div>
<div style="height:5px;"></div>
 <!-- 第一页信息维护开始 -->
 <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">报告接收信息
          <span class="pull-right">
          <button id="save" class="btn"  type="button">保存</button>
          <button id="clear" class="btn"  type="button">清空</button> 
          </span>
       </h4>
     <form method="post" id="demoForm1"  >
      <table class="B-table" width="100%">
         <TR style="display:none;">
         <!-- <TR> -->
	        <TD>		<input type="text" class="span12" kind="text" id="JHID" name = "JHID"  fieldname="JHID" keep="true">
            </TD><TD>	<input type="text" class="span12" kind="text"  id="ID" name="ID" fieldname="GC_SJ_BGSF_JS_ID">
	        </TD><TD>	<input type="text" class="span12" kind="text" id="ND" name = "ND"  fieldname="ND" keep="true">
	        </TD><TD>	<input type="text" class="span12" kind="text" id="XMID" name = "XMID"  fieldname="XMID" keep="true">
	        </TD><TD>	<input type="text" class="span12" kind="text" id="BDID" name = "BDID"  fieldname="BDID" keep="true">
            </TD><TD>	<input type="text" class="span12" kind="text"  id="JHSJID" name="JHSJID" fieldname="JHSJID" keep="true">
            </TD><TD>	<input type="text" class="span12" kind="text"  id="XMMC" name="XMMC" keep="true">
			</TD><TD>	<input type="text" class="span12" kind="text"  id="BDMC" name="BDMC" keep="true">
             </TD>
        </TR>
        <TR style="display:none">
        	<th width="8%"></th><td width="17%"></td>
        	<th width="8%"></th><td width="17%"></td>
        	<th width="8%"></th><td width="17%"></td>
        	<th width="8%"></th><td width="17%"></td>
        </TR>
        <!-- <tr>
	          <th width="8%" class="right-border bottom-border">项目名称</th>
	          <td colspan="3" class="right-border bottom-border"><input class="span12" id="XMMC" type="text"  check-type="required"  readonly="readonly"  name = "XMMC" keep="true"></td>
	          <th width="8%" class="right-border bottom-border">标段名称</th>
	          <td width="30%" class="bottom-border"><input class="span12"  type="text"  id="BDMC" readonly="readonly" name = "BDMC" keep="true"></td>
        </tr> -->
        <tr>
	          <th colspan="1" class="right-border bottom-border">报告类别</th>
	          <td colspan="3" class="right-border bottom-border"><select class="span12" id="BGLB" check-type="required" name = "BGLB" fieldname = "BGLB"  kind="dic" src="BGLB"  placeholder="必填"></select></td>
	          <th colspan="1" class="right-border bottom-border">接收日期</th>
	          <td colspan="3" class="bottom-border"><input class="span12" type="date" id="JSRQ" check-type="required" name = "JSRQ" fieldname= "JSRQ" placeholder="必填"></td>
        </tr>
        <tr>
	          <th colspan="1" class="right-border bottom-border">接收份数</th>
	          <td colspan="3" class="right-border bottom-border"> <input class="span12" id="FS" type="text" check-type="number required" fieldname="FS" name ="FS"  placeholder="必填"></td>
	          <th colspan="1" class="right-border bottom-border">接收人</th>
	          <td colspan="3" class=" right-border bottom-border"><input class="span12" type="text" id="JSR" name = "JSR" fieldname= "JSR" check-type="maxlength" maxlength="30"></td>
        </tr>
         <tr>
         	<th colspan="1" class="right-border bottom-border">备注</th>
         	<td colspan="7" class=" right-border bottom-border" colspan="5"><textarea rows="2" class="span12" id="BZ" fieldname="BZ" check-type="maxlength" maxlength="4000"></textarea></td>
         </tr>
      </table>
      </form>
    </div>
  </div>
</div>
<div align="center">
 	<FORM name="frmPost1" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "lrsj"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML1">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
 	</FORM>
 </div>			
</blockquote>
</div><!-- 第一页结束 -->
<!-- 第二开始 -->
<div class="span5" style="height:100%;">
<blockquote>
<app:dialogs/>
<div class="container-fluid">
 <p></p>
 <h5 class="title">&nbsp;
 </h5>
 <!-- 第一页查询条件开始 -->
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
    <p></p>
    <form method="post" id="queryForm2"  >
      <table class="B-table">
                               <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true">
			</TD>
			<!-- <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text" name="ZLLJSD" id="ZLLJSD" fieldname="ZLLJSD" operation="="></TD> -->
        </TR>
                               <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
		<div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT2" type="single" pageNum="5">
                <thead>
                   <tr>
	                    <th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
						<th fieldname="LQBM" >&nbsp;领取部门&nbsp;</th>
						<th fieldname="LQR">&nbsp;领取人&nbsp;</th>
						<th fieldname="LQRQ" tdalign="center">&nbsp;领取时间&nbsp;</th>
						<th fieldname="FS" tdalign="right" >&nbsp;领取份数&nbsp;</th>
						<th fieldname="BLR">&nbsp;办理人&nbsp;</th>
                    </tr> 
                </thead>
             <tbody></tbody>
      </table>
   </div>
</div>
</div>
 <!-- 第 二页信息维护开始 -->
 <div style="height:5px;"></div>
    <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">报告领取信息
          <span class="pull-right">
          <button id="insert" class="btn"  type="button">保存</button>
          <button id="clean" class="btn"  type="button">清空</button> 
          </span>
       </h4>
     <form method="post" id="demoForm2"  >
      <table class="B-table" width="100%">
         <TR style="display:none;">
         <!-- <TR> -->
	        <TD>		<input type="text" class="span12" kind="text" id="JHID" name = "JHID"  fieldname="JHID" keep="true">
            </TD><TD>	<input type="text" class="span12" kind="text" id="LQID" name = "ID" fieldname="GC_SJ_XGZL_LQ_ID">
            </TD><TD>	<input type="text" class="span12" kind="text" id="JHSJID" name = "JHSJID" fieldname="JHSJID" keep="true">     
            </TD><TD>	<input type="text" class="span12" kind="text" id="ZLLJSD" name = "ZLLJSD" fieldname="ZLLJSD" keep="true">	         
	        </TD><TD>	<input type="text" class="span12" kind="text" id="ND" name = "ND"  fieldname="ND" keep="true">
	        </TD><TD>	<input type="text" class="span12" kind="text" id="XMID" name = "XMID"  fieldname="XMID" keep="true">
	        </TD><TD>	<input type="text" class="span12" kind="text" id="BDID" name = "BDID"  fieldname="BDID" keep="true">
	        </TD><TD>	<input type="text" class="span12" kind="text" id="XMMC" name = "XMMC"  fieldname="XMMC" keep="true">
	        </TD><TD>	<input type="text" class="span12" kind="text" id="BDMC" name = "BDMC"  fieldname="BDMC" keep="true">
	        </TD>
        </TR>
        <TR style="display:none">
        	<th width="8%"></th><td width="17%"></td> <th width="8%"></th><td width="17%"></td>
        	<th width="8%"></th><td width="17%"></td> <th width="8%"></th><td width="17%"></td>
        </TR>
        <tr>
	          <th colspan="1" class="right-border bottom-border">领取部门</th>
	          <td colspan="3" class="right-border bottom-border"><input class="span12"  type="text" check-type="maxlength required" id="LQBM" fieldname="LQBM"  name = "LQBM" maxlength="30" placeholder="必填"></td>
	          <th colspan="1" class="right-border bottom-border">领取人</th>
	          <td colspan="3" class="bottom-border"><input class="span12"  type="text" id="LQR" fieldname="LQR" name = "LQR" check-type="maxlength required" maxlength="30" placeholder="必填"></td>
        </tr>
        <tr>
	          <th colspan="1" class="right-border bottom-border">领取日期</th>
	          <td colspan="3" class="bottom-border"><input class="span12" type="date" id="LQRQ" check-type="required" name = "LQRQ" fieldname= "LQRQ" placeholder="必填"></td>
	          <th colspan="1" class="right-border bottom-border">领取份数</th>
	          <td colspan="3" class="right-border bottom-border"><input class="span12" type="text" check-type="number required" id="LQFS" fieldname="FS" name = "FS" placeholder="必填">
	          </td>
        </tr>
        <tr>
	          <th colspan="1" class="right-border bottom-border">办理人</th>
	          <td colspan="3" class="right-border bottom-border"><input class="span12"  type="text" check-type="" id="BLR" fieldname="BLR" name = "BLR" value="<%= username%>"></td>
	          <td colspan="4" class="right-border bottom-border">
        </tr>
      </table>
      </form>
    </div>
  </div>
</div>
  <div align="center">
 	<FORM name="frmPost2" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "lrsj"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML2">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
 	</FORM>
 </div>
</blockquote>
</div><!-- 第二页结束 -->
</div><!-- 最后一个div -->
</body>
</html>
