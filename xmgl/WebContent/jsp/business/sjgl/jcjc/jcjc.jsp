<!DOCTYPE HTML>
<html lang="en">
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base />
<title></title>
<%
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String username = user.getName();
	String department = user.getDepartment();
	String userid= user.getAccount();
%>		
</head>
  <script>
	var controllername= "${pageContext.request.contextPath }/sjgl/jcjc/jcjcController.do";
	//获取父页面的值
	function do_value(){
		var pwindow =$(window).manhuaDialog.getParentObj();
		var rowValue = pwindow.$("#DT1").getSelectedRow();
		var obj = convertJson.string2json1(rowValue);
		return obj;
	}
	//页面赋值
	function setValue(){
		var json=do_value();
		setValueByArr(json,["XMMC","BDMC","JHID","ND","XMID","BDID","JHSJID","SJWYBH"]);
	    xmmc=$("#XMMC").val();
        bdmc=$("#BDMC").val();
        var sjwybh=$("#SJWYBH").val();
        $("#Q_SJWYBH").val(sjwybh);
		if(bdmc==''||bdmc==null)
		{
			$("#BDMC").val('所选内容不包含标段信息');
			$("#BDMC").attr("style","color:red;");
		}else{
			$("#BDMC").val(bdmc);
		}
		left_num();
		right_num();
	  }
	function ready() {
		left_user();
		right_user();
		setValue();
		left_defDate();
  		right_defDate();
  		left_query();
  		jsdel();
  		lqdel();
   };
   function left_query(){
		var data = combineQuery.getQueryCombineData(queryForm1,frmPost1,DT11);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername + "?queryJs",data,DT11,null,true);
   }
    $(function() 
     {
    	//页面初始化（左侧默认查询）
    	ready();
         //左侧保存
		$("#save").click(function() {
			id=$("#ID").val();
			 if(id==null||id=="")
				{
				 	if($("#demoForm1").validationButton())
					{
		 				var data = Form2Json.formToJSON(demoForm1);
		 				var data1 = defaultJson.packSaveJson(data);
		 				var ywid=$("#ywid").val();
		 				var json=do_value();
		 			    xmmc=$(json).attr("XMMC");
		 			    bdmc=$(json).attr("BDMC");
		 				defaultJson.doInsertJson(controllername+"?insertSf&ywid="+ywid+"&xmmc="+xmmc+"&bdmc="+bdmc,data1,DT11,"left_insert");
		 				/* left_insert(); */
					}else{
				  		defaultJson.clearTxtXML();
					}
				}else{
		 		 	if($("#demoForm1").validationButton())
					{
		 		 		var data = Form2Json.formToJSON(demoForm1);
		 				var data1 = defaultJson.packSaveJson(data);
		 				defaultJson.doUpdateJson(controllername + "?updateSf", data1,DT11,"left_update");
					}else{
				  		defaultJson.clearTxtXML();
					}
				 }
			  });
			//左侧表单清空
			$("#clear").click(function() {
				left_clear();
				right_clear();
			  });
			//右侧表单清空
			$("#clean").click(function() {
				right_clear();
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
			 				var data = Form2Json.formToJSON(demoForm2);
			 				var data1 = defaultJson.packSaveJson(data);
		 					defaultJson.doInsertJson(controllername+"?insertSfxx&ywid="+$("#ywid").val(),data1,DT2,"right_insert");
						}else{
					  		defaultJson.clearTxtXML();
						}
					}
				 else
					 {
			 		 	if($("#demoForm2").validationButton())
						{
			 		 		var data = Form2Json.formToJSON(demoForm2);
			 				var data1 = defaultJson.packSaveJson(data);
			 				defaultJson.doUpdateJson(controllername + "?updateSfxx", data1,DT2);
						}else{
					  		defaultJson.clearTxtXML();
						}
					 }
			  });
			//打印领取单
			$("#DYLQD").click(function() {
				if(-1 == $("#DT2").getSelectedRowIndex()){
					requireSelectedOneRow();
					return false;
				}
				$("#resultXML").val($("#DT2").getSelectedRow());
				$(window).manhuaDialog({"title":"打印领取单","type":"text","content":"${pageContext.request.contextPath}/jsp/business/sjgl/zlsf/dylqd.jsp","modal":"1"}); 
			  });
     });
    //右侧添加
    function right_insert(){
    	$("#DT2").setSelect(0);
		var value=$("#DT2").getSelectedRow();
	    var temp=convertJson.string2json1(value);
	    $("#demoForm2").setFormValues(temp);
    }
    //左侧更新
    function left_update(){
    	var fuyemian1=$(window).manhuaDialog.getParentObj();
		fuyemian1.gengxinchaxun();
    }
    //左侧添加行选
    function left_insert(){
    	$("#DT11").setSelect(0);
			var value=$("#DT11").getSelectedRow();
		    var temp=convertJson.string2json1(value);
		    
		    left_tr(temp);
		    
		 	var fuyemian1=$(window).manhuaDialog.getParentObj();
			fuyemian1.gengxinchaxun();
    }
	 //左侧默认数值
	function left_num(){
		$("#FS").val(12);
	}
	//右侧默认数值
	function right_num(){
		$("#LQFS").val(1);
	}
    //左侧清空方法
    function left_clear(){
    	$("#demoForm1").clearFormResult();
		$("#DT11").cancleSelected();
		$('#ZLLJSD').val('');
		$("#ywid").val('');
		right_query();
		clearFileTab();
		left_user();
		left_num();
		left_defDate();
    }
    //右侧清空方法
    function right_clear(){
    	$("#demoForm2").clearFormResult();
		$("#DT2").cancleSelected();
		right_user();
		right_num();
		right_defDate();
    }
    //左侧行选
    function left_tr(obj){
    	$('#ZLLJSD').val($(obj).attr("GC_SJ_BGSF_JS_ID"));
		$("#demoForm1").setFormValues(obj);
		$("#demoForm2").setFormValues(obj);
		$("#ywid").val("");
		$("#demoForm2").clearFormResult();

		right_query();
		right_user();
		right_num();
		right_defDate();
		
		$("#fj_a").attr("fjlb",$("#BGLB").val());
		$("#fj_b").attr("fjlb",$("#BGLB").val());
		var jhsjid=$(obj).attr("JHSJID");
    	var a=$(obj).attr("GC_SJ_BGSF_JS_ID");
    	deleteFileData(jhsjid,"","","");
    	
    	var sjbh=$(obj).attr("SJBH");
    	var ywlx=$(obj).attr("YWLX");
		setFileData(jhsjid,a,sjbh,ywlx,"0");
    	
    	queryFileData("",a,"","");
    }
    //左侧人员选择
    function left_user(){
    	var userid='<%=userid%>';
    	setDefaultOption($("#JSR"),userid);
    }
  //右侧人员选择
    function right_user(){
    	var userid='<%=userid%>';
    	setDefaultOption($("#BLR"),userid);
    }
    //行选
	function tr_click(obj,DT)
	{	
		if(DT=="DT11"){
			left_tr(obj);
		}
		if(DT=="DT2"){
			var lqr=obj.LQR;
			$("#demoForm2").setFormValues(obj);
			var dept=$("#LQBM").val();
			$("#LQR").attr("src","T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '"+dept+"'  AND PERSON_KIND = '3' AND FLAG='1' order by sort");
			$("#LQR").html('');
			reloadSelectTableDic($("#LQR"));
			$("#LQR").val(lqr);
			
		}
	}
    //左侧行选执行右侧查询
    function right_query(){
    		var ZLLJSD=$('#ZLLJSD').val();
			var data4 = combineQuery.getQueryCombineData(queryForm2,frmPost2,DT2);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername + "?querySfxx&ZLLJSD="+ZLLJSD,data4,DT2,null,true);
    }

    //显示项目信息
	function xmxx(){
		var xm=document.getElementsByTagName("font");
		if (bdmc==null||bdmc==""){
			xm[0].innerHTML = "本次只对项目进行操作";	
		}else{
			return;
		}
	};
	
	//部门选择人员
	function change(){
		var dept=$("#LQBM").val();
		$("#LQR").attr("src","T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '"+dept+"'  AND PERSON_KIND = '3' AND FLAG='1' order by sort");
		$("#LQR").html('');
		reloadSelectTableDic($("#LQR"));
}
	//左侧默认时间
	function left_defDate(){
		var today=getCurrentDate();
		var s_date='{"JSRQ":\"'+today+'\","JSRQ_SV":\"'+today+'\"}';	
		var date=convertJson.string2json1(s_date);
		$("#demoForm1").setFormValues(date);
	}
	//右侧默认时间
	function right_defDate(){
		var today=getCurrentDate();
		var s_date='{"LQRQ":\"'+today+'\","LQRQ_SV":\"'+today+'\"}';	
		var date=convertJson.string2json1(s_date);
		$("#demoForm2").setFormValues(date);
	}
	//接收删除
	function jsdel()
	{
		//删除
		var shanchu=$("#del");
		shanchu.click(function()
		{
			var index =	$("#DT11").getSelectedRowIndex();
				//生成json串
		 	var data = Form2Json.formToJSON(demoForm1);
			var data1 = defaultJson.packSaveJson(data);
			if(index==-1){
				requireSelectedOneRow();
				return;
			}else{
				xConfirm("提示信息","是否确认删除！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){ 
					defaultJson.doDeleteJson(controllername+"?deleteJS",data1,DT11,"delJSHuiDiao"); 
				});
			}
		}
		);
	}
	//删除回调
	function delJSHuiDiao()
	{
		left_clear();
		right_clear();
		var fuyemian1=$(window).manhuaDialog.getParentObj();
		fuyemian1.gengxinchaxun();
	}
	//领取删除
	function lqdel()
	{
		//删除
		var shanchu=$("#lqdel");
		shanchu.click(function()
		{
			var index =	$("#DT2").getSelectedRowIndex();
				//生成json串
		 	var data = Form2Json.formToJSON(demoForm2);
			var data1 = defaultJson.packSaveJson(data);
			if(index==-1){
				requireSelectedOneRow();
				return;
			}else{
				xConfirm("提示信息","是否确认删除！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){ 
					defaultJson.doDeleteJson(controllername+"?deleteLQ",data1,DT2,"delLQHuiDiao"); 
				});
			}
		}
		);
	}
	//删除领取回调
	function delLQHuiDiao()
	{
		right_clear();
	}
	function doFj(fjlb){
		$("#fj_a").attr("fjlb",fjlb);
		$("#fj_b").attr("fjlb",fjlb);
	}
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
     	<h5 style="color:black;"><font class="title" style="color:red;">&nbsp;</font></h5>
    <form method="post" id="queryForm1"  >
      <table class="B-table" width="100%">
                               <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
      	<!-- <TR> -->
			<TD><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true"></TD>
			<TD><INPUT type="text" class="span12" kind="text" name="SJWYBH" id="Q_SJWYBH" fieldname="SJWYBH" operation="=" keep="true"></TD>
		</TR>	
                               <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
    <div style="height:5px;"></div>
	<div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT11" type="single" pageNum="5">
                <thead>
                   <tr>
	                    <th name="XH" id="_XH" width="5%">&nbsp;#&nbsp;</th>
						<th fieldname="BGLB" tdalign="center" width="15%">&nbsp;检测类别&nbsp;</th>
						<th fieldname="JSRQ" tdalign="center" width="10%">&nbsp;接收日期&nbsp;</th>
						<th fieldname="FS" tdalign="right" width="15%" maxlength="10">&nbsp;接收份数&nbsp;</th>
						<th fieldname="JSR" width="20%">&nbsp;接收人&nbsp;</th>
						<th fieldname="BZ" maxlength="15">&nbsp;信息说明&nbsp;</th>
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
          <button id="clear" class="btn"  type="button">新增</button> 
          <button id="save" class="btn"  type="button">保存</button>
          <button id="del" class="btn"  type="button">删除</button>
          </span>
       </h4>
     <form method="post" id="demoForm1"  >
      <table class="B-table" width="100%">
         <TR style="display: none">
         <!-- <TR> -->
            <TD><input type="text" class="span12" kind="text"  id="ID" name="ID" fieldname="GC_SJ_BGSF_JS_ID">
	        </TD>
	        <TD><input type="text" class="span12" kind="text"  id="ywid">
	        </TD>
	        <TD><input type="text" class="span12" kind="text" id="XMID" name = "XMID"  fieldname="XMID" keep="true">
	        </TD>
            <TD><input type="text" class="span12" kind="text"  id="JHSJID" name="JHSJID" fieldname="JHSJID" keep="true">
             </TD>
              <TD><input type="text" class="span12" kind="text"  id="SJWYBH" name="SJWYBH" fieldname="SJWYBH" keep="true">
             </TD>
	        <TD><input type="text" class="span12" kind="text" id="BDID" name = "BDID"  fieldname="BDID" keep="true">
            </TD>
	        <TD><input type="text" class="span12" kind="text" id="JHID" name = "JHID"  fieldname="JHID" keep="true">
            </TD>
	        <TD><input type="text" class="span12" kind="text" id="ND" name = "ND"  fieldname="ND" keep="true">
	        </TD>
	        <TD><input type="text" class="span12" kind="text" id="SJBH" name = "SJBH"  fieldname="SJBH">
	        </TD>
        </TR>
        <tr>
	          <th width="8%" class="right-border bottom-border disabledTh">项目名称</th>
	          <td width="42%" class="right-border bottom-border"><input class="span12" id="XMMC" type="text"  disabled name = "XMMC" keep="true"></td>
	          <th width="8%" class="right-border bottom-border disabledTh">标段名称</th>
	          <td width="42%" class="right-border bottom-border"><input class="span12"  type="text"  id="BDMC" disabled name = "BDMC" keep="true"></td>
        </tr>
        <tr>
	          <th colspan="1" class="right-border bottom-border">检测类别</th>
	          <td colspan="1" class="right-border bottom-border"><select class="span12" id="BGLB" check-type="required" name = "BGLB" fieldname = "BGLB"  kind="dic" src="BGLB"  placeholder="必填" onchange="doFj(this.value)"></select></td>
	          <th colspan="1" class="right-border bottom-border">接收日期</th>
	          <td colspan="1" class="bottom-border"><input class="span12 date" type="date" id="JSRQ" check-type="required" name = "JSRQ" fieldname= "JSRQ" placeholder="必填"></td>
        </tr>
        <tr>
	          <th colspan="1" class="right-border bottom-border">接收份数</th>
	          <td colspan="1" class="right-border bottom-border"> <input class="span12" style="text-align:right" id="FS" type="number" check-type="required" fieldname="FS" name ="FS" min="0"></td>
	          <th colspan="1" class="right-border bottom-border">接收人</th>
	          <td colspan="1" class=" right-border bottom-border">
	          	<select class="span12 person" kind="dic" id="JSR" name = "JSR" fieldname= "JSR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1' order by sort" ></select>
	          </td>
        </tr>
         <tr>
         	<th colspan="1" class="right-border bottom-border">信息说明</th>
         	<td colspan="3" class=" right-border bottom-border" colspan="5">
         		<textarea rows="2" class="span12" id="BZ" fieldname="BZ" check-type="maxlength" maxlength="4000"></textarea>
         	</td>
         </tr>
         <tr>
	       	<th colspan="1" class="right-border bottom-border text-right text-right">附件上传</th>
	       	<td colspan="3" class="bottom-border right-border">
				<div>
					<span class="btn btn-fileUpload" onclick="doSelectFile(this);" id="fj_a" fjlb="">
						<i class="icon-plus"></i>
						<span>添加文件...</span>
					</span>
					<table role="presentation" class="table table-striped">
						<tbody id="fj_b" fjlb="" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery" rowNumbers="1"></tbody>
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
 	<FORM name="frmPost1" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "lrsj"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML1">
         <input type="hidden" name="queryResult" id ="queryResult">
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
	<span class="pull-right">
		<button id="TXBMLQ" onclick="sendMsg()" class="btn"  type="button">提醒部门领取</button> 
		<button id="DYLQD" class="btn"  type="button">打印领取单</button>
	</span>
 </h5>
 <!-- 第一页查询条件开始 -->
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
    <div style="height:5px;"></div>
    <form method="post" id="queryForm2"  >
      <table class="B-table">
                               <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
			<TD><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true">
			</TD>
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
          <button id="clean" class="btn"  type="button">新增</button> 
          <button id="insert" class="btn"  type="button">保存</button>
          <button id="lqdel" class="btn"  type="button">删除</button>
          </span>
       </h4>
     <form method="post" id="demoForm2"  >
      <table class="B-table" width="100%">
         <TR style="display:none;">
         <!-- <TR> -->
	        <TD>		<input type="text" class="span12" kind="text" id="JHID" name = "JHID"  fieldname="JHID" keep="true">
            </TD><TD>	<input type="text" class="span12" kind="text" id="LQID" name = "ID" fieldname="GC_SJ_XGZL_LQ_ID">
            </TD><TD>	<input type="text" class="span12" kind="text" id="SJBH" name = "SJBH" fieldname="SJBH">
            </TD><TD>	<input type="text" class="span12" kind="text" id="JHSJID" name = "JHSJID" fieldname="JHSJID" keep="true">     
            </TD><TD>	<input type="text" class="span12" kind="text" id="ZLLJSD" name = "ZLLJSD" fieldname="ZLLJSD" keep="true">	         
        </TD><TD>		<input type="text" class="span12" kind="text"  id="SJWYBH" name="SJWYBH" fieldname="SJWYBH" keep="true">
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
	          <td colspan="3" class="right-border bottom-border">
	          	<select class="span12 department "  kind="dic" id="LQBM" fieldname="LQBM"  name = "LQBM" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" onchange="change()" check-type="required" placeholder="必填">
	          	</select>
	          </td>
	          <th colspan="1" class="right-border bottom-border">领取人</th>
	          <td colspan="3" class="bottom-border">
	          	<select class="span12 person" kind="dic" id="LQR" fieldname="LQR" name = "LQR" src="T#fs_org_person:ACCOUNT:NAME:FLAG = '1' AND PERSON_KIND = '3' order by sort" check-type="required" placeholder="必填">
	          	</select>
	          </td>
        </tr>
        <tr>
	          <th colspan="1" class="right-border bottom-border">领取日期</th>
	          <td colspan="3" class="bottom-border"><input class="span12 date" type="date" id="LQRQ" check-type="required" name = "LQRQ" fieldname= "LQRQ" placeholder="必填"></td>
	          <th colspan="1" class="right-border bottom-border">领取份数</th>
	          <td colspan="3" class="right-border bottom-border"><input class="span12" style="text-align:right" type="number" check-type="required" id="LQFS" fieldname="FS" name = "FS" placeholder="必填" min="0">
	          </td>
        </tr>
        <tr>
	          <th colspan="1" class="right-border bottom-border">办理人</th>
	          <td colspan="3" class="right-border bottom-border">
	          	<select class="span12 person" kind="dic" id="BLR" name = "BLR" fieldname= "BLR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1' order by sort" ></select>
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname ="lrsj"	id ="txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML2">
         <input type="hidden" name="queryResult1" id ="queryResult1">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
 	</FORM>
 </div>
</blockquote>
</div><!-- 第二页结束 -->
</div><!-- 最后一个div -->
</body>
</html>