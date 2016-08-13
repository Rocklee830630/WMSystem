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
	<app:dialogs/>
<%
request.setCharacterEncoding("utf-8");
User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
String department = user.getDepartment();
String account=user.getAccount();
String username = user.getName();
Date d=new Date();//获取时间
SimpleDateFormat sam_date=new SimpleDateFormat("yyyy-MM-dd");		//转换格式
String today=sam_date.format(d);
%>		
</head>
  <script>
	var controllername= "${pageContext.request.contextPath }/ziLiaoShouFaController.do";
	var biaozhi; 
	//获取父页面的值
	  //将数据放入表单
	  $(function(){
		  var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#DT1").getSelectedRow();
		  var odd=convertJson.string2json1(rowValue);
		  $("#demoForm1").setFormValues(odd);
		  $("#demoForm2").setFormValues(odd);
		  var xmmc1=$(odd).attr("XMMC");
		  var bdmc1=$(odd).attr("BDMC");
		  $("#XMMC").val(xmmc1);
		  $("#BDMC").val(bdmc1);
		  //判断是否有标段名称
	       if(bdmc1=='')
			  {
	    	   $("#BDMC").val('此设计文件管理只针对项目');
	           $("#BDMC").attr("style","color:red;");
	           $("#BDMC").removeAttr("fieldname");
			  }
	       jieshouren();
	       right_user();
		   left_defDate();
		   right_defDate();
		   dellq();
		   deljs();
	  });
	
    $(function() 
     {
	 	    jieshouquery();
	 	    right_user();
	 	    initLydw();
	         //保存
			var btn = $("#example2");
			btn.click(function() {
			biaozhi=$("#GC_SJ_ZLSF_JS_ID").val();
			if($("#demoForm1").validationButton()){
			//生成json串
	 		var data = Form2Json.formToJSON(demoForm1);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			if(biaozhi==''){
	 				defaultJson.doInsertJson(controllername + "?insert", data1,DT11,'addHuiDiaoJS');
				}else{
	 				defaultJson.doUpdateJson(controllername + "?update", data1,DT11,'editHuiDiaoJS');
				}}
			  });
			//接收表单清空
			var btn = $("#example1");
			btn.click(function() {
				jsclear();
			  });
			//资料类别改变event
			$("#TZLB").change(function() {
				$("#LYDW").clearFormResult(); 
				var zllb = $("#TZLB").val();
				changeLydw(zllb);
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
		   	//接收导出
		    $("#btnExpExcel").click(function() 
		       {
		    	 jSLQquery();
		    	 if(exportRequireQuery($("#EXP"))){//该方法需传入表格的jquery对象
		    		  printTabList("EXP","sjwj.xls","XMMC,BDMC,TZLB,JSRQ,LQRQ,LQBM,LQR","1,1"); 
		    	  }
		   	});
    });
    
    //初始化来源单位字典
    function initLydw(){
    	$("#LYDW").attr('src','T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX=\'6\' or DWLX=\'1\'');
    	$("#LYDW").attr('disabled','disabled');
    	$("#lydwSelect").hide();
		reloadSelectTableDic($("#LYDW"));
    }
    function changeLydw(zllb){
		var lydw = '1';
		switch(zllb){
			case '':
				lydw = 'lydw';
				break;
			case '0'://规划条件
				lydw = '6';
				break;
			default:
				lydw = '1';
		}
		var src = "T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX = '"+lydw+"'";
		$("#LYDW").attr('src',src);
		if(lydw!='lydw'){
			$("#lydwSelect").attr('dwlx',''+lydw+'');
			$("#LYDW").removeAttr('disabled');
			$("#lydwSelect").show();
			reloadSelectTableDic($("#LYDW"));
		}else{
	    	$("#LYDW").attr('disabled','disabled');
	    	$("#lydwSelect").hide();
		}
    }
    
    //默认查询 
	function jieshouquery()
	{
		  var sjwybh=$('#SJWYBH').val();
		  var nd=$('#ND').val();
		  var data = combineQuery.getQueryCombineData(queryForm1,frmPost1,DT11);
			//调用ajax插入
		  defaultJson.doQueryJsonList(controllername + "?queryAllLList&sjwybh="+sjwybh+"&nd="+nd,data,DT11,null,true);
	}  
	function tr_click(obj,DT11)
	{
		
		if(DT11=="DT11")
		{
			var ZLLJSDID=$(obj).attr("GC_SJ_ZLSF_JS_ID");
			changeLydw(obj.TZLB);
			$("#demoForm2").setFormValues(obj);
			$("#demoForm1").setFormValues(obj);
			//清空领取表单
			$('#ZLLJSD').val($(obj).attr("GC_SJ_ZLSF_JS_ID"));
			$("#demoForm2").clearFormResult();
			//默认当前办理人
			right_user();
			right_defDate();
			var data = combineQuery.getQueryCombineData(queryForm2,frmPost2,DT2);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername + "?queryAllShouQu&ZLLJSDID="+ZLLJSDID,data,DT2,null,true);
		}
		if(DT11=="DT2")
		{
			$("#demoForm2").setFormValues(obj);
			var lqr=obj.LQR;
			var dept=$("#LQBM").val();
			$("#LQR").attr("src","T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '"+dept+"'  AND PERSON_KIND = '3' AND FLAG='1' order by sort");
			$("#LQR").html('');
			reloadSelectTableDic($("#LQR"));
			$("#LQR").val(lqr);
		}
	}	  
	  
	  
	//领取操作
	  $(function() 
     {
	         //保存
			var btn = $("#example4");
			btn.click(function() {
			var ZLLJSD=$('#ZLLJSD').val();
			var LQID=$('#LQID').val();
			 if(ZLLJSD==null||ZLLJSD=="null"||ZLLJSD==""){
					 requireSelectedOneRow();
					 return;
				}
			 if($("#demoForm2").validationButton()){
					//生成json串
	 		 		var data = Form2Json.formToJSON(demoForm2);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
				 if(LQID==null||LQID=="null"||LQID==""){
		 				defaultJson.doInsertJson(controllername + "?insertLingQu", data1,DT2,'addHuiDiaoLQ');
					}else{
	 					defaultJson.doUpdateJson(controllername + "?updateLingQu", data1,DT2);
						} 
		        }
				});
			//新增
			var btn = $("#example3");
			btn.click(function() {
				$("#demoForm2").clearFormResult(); 
				$("#DT2").cancleSelected();
				right_user();
				right_defDate();
			  });
      });
//动态获取领取人
	function xuzheren(obj)
	{
		var lqbm=$(obj).val();
		var src = "T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG='1'  AND PERSON_KIND = '3' AND DEPARTMENT = '"+lqbm+"'  order by sort ";
		$("#LQR").attr('src',src);
		reloadSelectTableDic($("#LQR"));
	}
 //默认接收人
   function jieshouren()
  {
	  var account="<%= account%>";
	  setDefaultOption($("#JSR"),account);
	  $("#FS").val('12');
  }
   //右侧人员选择
   function right_user(){
   	var account='<%=account%>';
   	setDefaultOption($("#BLR"),account);
   	$("#LQFS").val('1');
   }
	//左侧默认时间
	function left_defDate(){
		var s_date='{"JSRQ":\"'+'<%=today%>'+'\","JSRQ_SV":\"'+'<%=today%>'+'\"}';	
		var date=convertJson.string2json1(s_date);
		$("#demoForm1").setFormValues(date);
	}
	//右侧默认时间
	function right_defDate(){
		var s_date='{"LQRQ":\"'+'<%=today%>'+'\","LQRQ_SV":\"'+'<%=today%>'+'\"}';	
		var date=convertJson.string2json1(s_date);
		$("#demoForm2").setFormValues(date);
	}
	//接收回调函数
	function addHuiDiaoJS()
	{
		var fuyemian1=$(window).manhuaDialog.getParentObj();
		fuyemian1.xiugaihang();
		//获取返回的ID
	    var data2 = $("#frmPost1").find("#resultXML").val();
		var resultmsgobj = convertJson.string2json1(data2);
		var subresultmsgobj = resultmsgobj.response.data[0];
		var zlsfid=$(subresultmsgobj).attr("GC_SJ_ZLSF_JS_ID");
		$("#GC_SJ_ZLSF_JS_ID").val(zlsfid); 
		$('#ZLLJSD').val(zlsfid);
	    $("#DT11").setSelect(0);
	}
	function editHuiDiaoJS()
	{
		var fuyemian1=$(window).manhuaDialog.getParentObj();
		fuyemian1.xiugaihang();
	}
	//领取回调
	function addHuiDiaoLQ()
	{
		var data2 = $("#frmPost1").find("#resultXML").val();
		var resultmsgobj = convertJson.string2json1(data2);
		var subresultmsgobj = resultmsgobj.response.data[0];
		var aa=$(subresultmsgobj).attr("GC_SJ_XGZL_LQ_ID");
		$("#LQID").val(aa); 
	    $("#DT2").setSelect(0);
	}
	//接收删除
	function deljs()
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
	//接收回调
	function delJSHuiDiao()
	{
		var fuyemian1=$(window).manhuaDialog.getParentObj();
		fuyemian1.xiugaihang();
		jsclear();
	}
	function jsclear()
	{
		$("#demoForm1").clearFormResult(); 
		$("#DT11").cancleSelected();
		//清空领取
		$("#DT2").find("tbody").children().remove();
		//清空领取的接收 ID
		$('#ZLLJSD').val('');
		//清空领取表单
		$("#demoForm2").clearFormResult();
		$("#DT2").cancleSelected();
		//user();
		right_user();
		jieshouren();
		initLydw();
		right_defDate();
		left_defDate();
	}
	//领取删除
	function dellq()
	{
		//删除
		var shanchu=$("#dellq");
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
	//接收回调
	function delLQHuiDiao()
	{
		$("#demoForm2").clearFormResult(); 
		$("#DT2").cancleSelected();
		right_user();
		right_defDate();
	}
	//导出查询
	function jSLQquery()
	{
   		var sjwybh=$('#SJWYBH').val();
   		var nd=$('#ND').val();
		var data = combineQuery.getQueryCombineData(queryForm1,frmPost1,EXP);
     	//调用ajax插入
     	defaultJson.doQueryJsonList(controllername + "?queryJSLQ&sjwybh="+sjwybh+"&nd="+nd,data,EXP,null,true);
	}
</script>
<body id="a">
<div class="row-fluid">
<div class="span7"><!-- 第一页开始 -->
<blockquote>
 <app:dialogs/>
<div class="container-fluid">
<div class="row-fluid">
 <!-- 第一页查询条件开始 -->
 <div class="B-small-from-table-autoConcise">
  <p></p>
     <h5 style="color: black;"><div id="XSXMMC" ></div></h5>
    <form method="post"  id="queryForm1"  >
      <table class="B-table" width="100%">
                               <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text" keep="true" fieldname="rownum"  value="1000" operation="<=" ></TD>
        </TR>
      </table>
      </form>
   <!-- 第一页查询结果开始 -->
    <div style="height:5px;"></div>
    <div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT11" pageNum="5" type="single">
                <thead>
                   <tr>
	                    <th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
						<th fieldname="TZLB" >&nbsp;资料类别&nbsp;</th>
						<th fieldname="SJY" maxlength="15">&nbsp;来源单位&nbsp;</th>
						<th fieldname="JSRQ" tdalign="center">&nbsp;接收日期&nbsp;</th>
						<th fieldname="FS" tdalign="right"  maxlength="3">&nbsp;接收份数&nbsp;</th>
						<th fieldname="JSR"  >&nbsp;接收人&nbsp;</th>
                    </tr> 
                </thead>
             <tbody>
          </tbody>
      </table>
   </div>
</div>
 <!-- 第一页信息维护开始 -->
	<div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">资料信息
          <span class="pull-right">
           <button id="example1" class="btn"  type="button">新增</button> 
           <button id="example2" class="btn"  type="button">保存</button>
           <button id="del" class="btn"  type="button">删除</button>
          </span>
       </h4>
     <form method="post"  id="demoForm1"  >
      <table class="B-table"  width="100%">
         <TR  style="display:none;">
	        <TD class="right-border bottom-border">  
	              <input type="text" class="span12" kind="text" id="JHID" keep="true" name = "JHID"  fieldname="JHID"  >
	              <input type="text" class="span12" kind="text" id="JHSJID" keep="true" name = "JHSJID"  fieldname="JHSJID"  >
	              <input type="text" class="span12" kind="text" id="SJWYBH" keep="true" name = "SJWYBH"  fieldname="SJWYBH"  >
	              <input type="text" class="span12" kind="text" id="ND" keep="true" name = "ND"  fieldname="ND"  >
	              <input type="text" class="span12" kind="text" id=XMID keep="true" name = "XMID"  fieldname="XMID"  >
	              <input type="text" class="span12" kind="text" id="BDID" keep="true" name = "BDID"  fieldname="BDID"  >
	              <input type="text" class="span12" kind="text" id="XMBH" keep="true" name = "XMBH"  fieldname="XMBH"  >
	              <input type="text" class="span12" kind="text" id="BDBH" keep="true" name = "BDBH"  fieldname="BDBH"  >
                  <input type="text" class="span12" kind="text" id="GC_SJ_ZLSF_JS_ID" fieldname="GC_SJ_ZLSF_JS_ID"  >
             </TD>
			<TD class="right-border bottom-border"></TD>
        </TR>
       <tr>
	          <th width="5%" class="right-border bottom-border disabledTh">项目名称</th>
	          <td width="42%" class="right-border bottom-border"><input class="span12 xmmc" id="XMMC" keep="true" type="text"  check-type="required" disabled placeholder="请选择项目" fieldname="XMMC"  name = "XMMC"></td>
	          <th width="8%" class="right-border bottom-border disabledTh">标段名称</th>
	          <td width="25%" class="bottom-border"><input class="span12"  type="text"  id="BDMC" keep="true"  fieldname="BDMC" disabled name = "BDMC"></td>
        </tr> 
        <tr>
              <th width="8%" class="right-border bottom-border">资料类别</th>
	          <td width="42%"  class="bottom-border"><select class="span12 7characters " id="TZLB" check-type="required" name = "TZLB" fieldname = "TZLB"  kind="dic" src="TZLB"  ></select></td>
	          <th width="8%" class="right-border bottom-border">来源单位</th>
	          <td width="42%" class="right-border bottom-border">
	              <select class="span12 department" id="LYDW" name = "SJY" style="width: 80%"  fieldname="SJY" kind="dic" check-type="required"  src="" >
                  </select>
                  <a href="javascript:void(0)" title="点击选择单位"><i id="lydwSelect" selObj="LYDW"   class="icon-edit" onclick="selectCjdw('lydwSelect');" isLxSelect="1"></i></a>
	          </td>
	         
        </tr>
        <tr>
	          <th  class="right-border bottom-border">接收日期</th>
	           <td  class="bottom-border"><input class="span12 date" type="date" id="" check-type="required" name = "JSRQ" fieldname= "JSRQ"></td>
               <th  class="right-border bottom-border">接收份数</th>
	           <td  class="right-border bottom-border"> <input class="span6" id="FS"   style="text-align:right" type="number" maxlength="30" check-type="required number"  placeholder="必填" fieldname="FS" name = "FS" value="12"></td>
        </tr>
        <tr>
              <th  class="right-border bottom-border">接收人</th>
	          <td  class=" right-border bottom-border">
	          <select class="span12 person" id="JSR" name = "JSR"  fieldname="JSR" kind="dic" src="T#fs_org_person:ACCOUNT:NAME:FLAG='1'  AND PERSON_KIND = '3' AND DEPARTMENT = '<%=department%>'  order by sort " > </select>
	          </td>
	          <td colspan="2"  class="bottom-border" ></td>
        </tr>
       <tr>
              <th  class="right-border bottom-border">备注</th>
              <td colspan="3"  class=" right-border bottom-border">
              <textarea class="span12"  id=""  name = "BZ" maxlength="4000" fieldname= "BZ" rows="2" cols=""></textarea>
         </tr>
      </table>
      </form>
    </div>
  </div>
</div>
<div align="center">
 	<FORM name="frmPost1" method="post" style="display:none" target="_blank" id="frmPost1">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
        <input type="hidden" name="txtFilter" order="desc" fieldname="XMBH,LRSJ"/>
         <input type="hidden" name="resultXML" id = "resultXML">
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
     		<button id="btnExpExcel"  class="btn"  type="button">导出</button> 
             <button id="TXBMLQ" onclick="sendMsg('信息推送','SJWJGL')" class="btn"  type="button">提醒部门领取</button> 
             <button id="DYLQD" class="btn"  type="button">打印领取单</button>
      </span>
 </h5>
 <!-- 第二页查询结果开始 -->
 <p></p>
    <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
    <form method="post"  id="queryForm2"  >
      <table class="B-table" width="100%">
                               <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true"></TD>
        </TR>
      </table>
      </form>
  <div style="height:5px;"></div>
    <div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT2" pageNum="5" type="single">
                <thead>
                   <tr>
	                    <th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
						<th fieldname="LQBM"  maxlength="15">&nbsp;领取部门&nbsp;</th>
						<th fieldname="LQR">&nbsp;领取人&nbsp;</th>
						<th fieldname="LQRQ" tdalign="center">&nbsp;领取日期&nbsp;</th>
						<th fieldname="FS" tdalign="right">&nbsp;领取份数&nbsp;</th>
                    </tr> 
                </thead>
             <tbody>
          </tbody>
      </table>
        <table width="100%"  style="display: none" class="table-hover table-activeTd B-table" id="EXP" noPage="true"  printFileName="设计文件"  type="single">
                <thead>
                   <tr>
	                    <th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
	                    <th fieldname="LQBM" maxlength="15" >&nbsp;领取部门&nbsp;</th>
                    </tr> 
                </thead>
             <tbody>
          	</tbody>
     	</table>
   </div>
</div>
 <!-- 第 二页信息维护开始 -->
   <div style="height:5px;"></div>
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">领取信息
          <span class="pull-right">
             <button id="example3" class="btn"  type="button">新增</button> 
             <button id="example4" class="btn"  type="button">保存</button>
             <button id="dellq" class="btn"  type="button">删除</button>
          </span>
       </h4>
     <form method="post"  id="demoForm2"  >
      <table class="B-table">
                 
         <TR  style="display:none;">
                  
	        <TD class="right-border bottom-border">  
	              <input type="text" class="span12" kind="text" id="ZLLJSD" name = "ZLLJSD" keep="true" fieldname="ZLLJSD"  >
                  <input type="text" class="span12" kind="text"  name = "JHID" keep="true" fieldname="JHID"  >
	              <input type="text" class="span12" kind="text" id="JHSJID" keep="true" name = "JHSJID"  fieldname="JHSJID"  >
	              <input type="text" class="span12" kind="text"  name = "ND" keep="true" fieldname="ND"  >
	              <input type="text" class="span12" kind="text"  name = "XMID" keep="true"  fieldname="XMID"  >
	              <input type="text" class="span12" kind="text"  name = "BDID" keep="true" fieldname="BDID"  > 
                  <input type="text" class="span12" kind="text" id="LQID" name = "GC_SJ_XGZL_LQ_ID" fieldname="GC_SJ_XGZL_LQ_ID"  > 
	         </TD>
			<TD class="right-border bottom-border"></TD>
        </TR>
        <tr>
	          <th width="8%" class="right-border bottom-border">领取部门</th>
	          <td width="52%" class="right-border bottom-border">
	           <select class="span12 department" check-type="required"  name="LQBM" fieldname="LQBM" id="LQBM" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME"  onchange="xuzheren(this)"></select>
	          </td>
	          <th width="8%" class="right-border bottom-border">领取人</th>
	          <td width="32%" class="bottom-border">
	           <select class="span12 person" id="LQR" name = "LQR" check-type="required" fieldname = "LQR" operation="=" kind="dic" src="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG='1'  AND PERSON_KIND = '3'  order by sort">
                </select>
	          </td>
        </tr>
        <tr>
         <th  class="right-border bottom-border">领取日期</th>
	          <td class="bottom-border"><input class="span12 date" type="date" id="LQRQ"  check-type="required" name = "LQRQ" fieldname= "LQRQ"></td>
	          <th  class="right-border bottom-border">领取份数</th>
	          <td class="right-border bottom-border">
	           <input class="span6"  type="number" id="LQFS" fieldname="FS" maxlength="30"    style="text-align:right" placeholder="必填" check-type="required number" name = "FS">
	          </td>
        </tr>
        <tr>
	          <th  class="right-border bottom-border">办理人</th>
	          <td class="right-border bottom-border">
	          <select class="span12 person" kind="dic" id="BLR" name = "BLR" fieldname= "BLR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1'  order by sort" ></select>
	          </td>
               <td  colspan="2" class="right-border bottom-border">
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
        <input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ"/>
        <!--  <input type="hidden" name="resultXML" id = "resultXML"> -->
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
 	</FORM>
 </div>
</blockquote>
</div><!-- 第二页结束 -->
</div><!-- 最后一个div -->
</body>
</html>
