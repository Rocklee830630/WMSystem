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
String username = user.getName();
String department = user.getDepartment();
String account=user.getAccount();
Date d=new Date();//获取时间
SimpleDateFormat sam_date=new SimpleDateFormat("yyyy-MM-dd");		//转换格式
String today=sam_date.format(d);
%>		
</head>
  <script>
	var controllername= "${pageContext.request.contextPath }/sheJiBianGengService.do";
	var biaozhi;
	var SGDW;
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
		  SGDW=$(odd).attr("SGDW");
		  bg_defDate();
		  //判断项目是否有标段
		  if(bdmc1=='')
			  {
			  $("#BDMC").val('此变更管理只针对项目');
              $("#BDMC").attr("style","color:red;");
              $("#BDMC").removeAttr("fieldname");
			  }
		  jieshouren();
		  right_user();
		  left_defDate();
		  right_defDate();
		  deljs();
		  dellq();
		    //接收导出
		    $("#btnExpExcel").click(function() 
		       {
		    	if(exportRequireQuery($("#DT11"))){//该方法需传入表格的jquery对象
		    		  jieshouquery();
		    		  printTabList("DT11","sjbg.xls","XMBH,XMMC,BDMC,XMDZ,BGLB,BGBH,BGNR,SJY,SJDW,SGDW,JLDW,JBGRQ,BGFY","1,1"); 
		    		  
		    		 // printTabList("DT11","sjbg.xls","XMBH,XMMC,BDMC,SJDW,SGDW,JLDW,BGLLDJSRQ,BGLB,JBGRQ,BGBH,BGFY,SJY,JSRQ,FS,BGNR","1,1"); 
		    	  }
		   	});
		    //接收作废
		    $("#btnZF").click(function()
	    		{
		    	if($("#DT11").getSelectedRowIndex()==-1){
					requireSelectedOneRow();
					return;
				}else{
					var rowValue = $("#DT11").getSelectedRow();
					var tempJson = convertJson.string2json1(rowValue);
					tempJson["BGZT"]="0";
					xConfirm("提示信息","确认要作废吗？");
					$('#ConfirmYesButton').unbind();
					$('#ConfirmYesButton').one("click",function(){
						var data = JSON.stringify(tempJson);
			 			var data1 = defaultJson.packSaveJson(data);
			 			defaultJson.doUpdateJson(controllername + "?update", data1,DT11,"eidtHuiDiaoJS");
	 				});
				}
	    		});
	  });
	
    $(function() 
     {
    	     //默认查询
    	     jieshouquery();
	         //接收保存
			var btn = $("#example2");
			btn.click(function() {
			biaozhi=$("#GC_SJ_SJBG_JS2_ID").val();
			if($("#demoForm1").validationButton())
			{
				//生成json串
 		 		var data = Form2Json.formToJSON(demoForm1);
 				//组成保存json串格式
 				var data1 = defaultJson.packSaveJson(data);
				if(biaozhi=='')
				{
			 		$("#DT11").cancleSelected();
	 				//调用ajax插入
	 				defaultJson.doInsertJson(controllername + "?insert&ywid="+$("#ywid").val(), data1,DT11,'addHuiDiaoJS');
				}
			 else
				 {
	 				//调用ajax插入
	 				defaultJson.doUpdateJson(controllername + "?update", data1,DT11,'eidtHuiDiaoJS');
				}
 		 	
			 }
			  });
			//接收表单清空
			var btn = $("#example1");
			btn.click(function() {
				jsclear();
			  });
    });
  //默认接收查询 
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
		//判断点击的是那个列表
		if(DT11=="DT11")
		{
			var SJBGID=$(obj).attr("GC_SJ_SJBG_JS2_ID");
			$("#demoForm2").setFormValues(obj);
			$("#demoForm1").setFormValues(obj);
			//清空领取表单
			$('#ZLLJSD').val($(obj).attr("GC_SJ_SJBG_JS2_ID"));
			var ywid=$(obj).attr("GC_SJ_SJBG_JS2_ID");
			var SJBH=$(obj).attr("SJBH");
			var YWLX=$(obj).attr("YWLX");
			deleteFileData(ywid,"","","");
			setFileData(ywid,"",SJBH,YWLX,"0");
			//查询附件信息
			queryFileData(ywid,"","","");
		  	//清空领取表单
			$("#demoForm2").clearFormResult();
			//默认当前办理人
			//user();
			right_user();
			right_defDate();
			var data2 = combineQuery.getQueryCombineData(queryForm2,frmPost2,DT2);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername + "?queryAllShouQu&SJBGID="+SJBGID,data2,DT2,null,true);
		}
		//点击领取列表将数据放在领取表单
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
	         //领取保存
			var btn = $("#example4");
			btn.click(function() 
			{
				
				var ZLLJSD=$('#ZLLJSD').val();
				var LQID=$('#LQID').val();
				if(ZLLJSD=="")
				  {
					 requireSelectedOneRow();
					 return;
				  }
				 if($("#demoForm2").validationButton())
					{
						//生成json串
		 		 		var data = Form2Json.formToJSON(demoForm2);
		 				//组成保存json串格式
		 				var data1 = defaultJson.packSaveJson(data);
					 //判断添加还是修改
					 if(LQID==null||LQID=="null"||LQID=="")
						{
						
		 				//调用ajax插入
		 				defaultJson.doInsertJson(controllername + "?insertLingQu", data1,DT2,'addHuiDiaoLQ');
						}
				  	else{
		 				//调用ajax插入
		 				defaultJson.doUpdateJson(controllername + "?updateLingQu", data1,DT2,null);
						} 
			       }
					
			  });
	//领取清空表单
	var btn = $("#example3");
	btn.click(function() 
	 {
		$("#demoForm2").clearFormResult();
		$("#DT2").cancleSelected();
		//user();
		right_user();
		right_defDate();
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
//获取当前登录名
	function user()
	{
		var blr="<%= username%>";
		$("#BLR").val(blr);
	}	
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
	  setDefaultOption($("#SJY"),SGDW);
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
		var s_date='{"BGLLDJSRQ":\"'+'<%=today%>'+'\","BGLLDJSRQ_SV":\"'+'<%=today%>'+'\"}';	
		var date=convertJson.string2json1(s_date);
		$("#demoForm1").setFormValues(date);
	}
	//变更时间默认
	function bg_defDate(){
		var bgdate=$("#BGRQ").val();
		if(bgdate==null||bgdate=="")
		var s_date='{"JBGRQ":\"'+'<%=today%>'+'\","JBGRQ_SV":\"'+'<%=today%>'+'\"}';	
		var date=convertJson.string2json1(s_date);
		$("#demoForm1").setFormValues(date);
	}
	//右侧默认时间
	function right_defDate(){
		var s_date='{"LQRQ":\"'+'<%=today%>'+'\","LQRQ_SV":\"'+'<%=today%>'+'\"}';	
		var date=convertJson.string2json1(s_date);
		$("#demoForm2").setFormValues(date);
	}
	//编号颜色判断
		function doRandering(obj)
	{
		var bgzt=obj.BGZT;
		if(bgzt=="0")
		{
			return '<span class="label label-default" style="width: 70%" >'+obj.BGLLDJSRQ+'</span>';
		}
		else
			{
			return obj.BGLLDJSRQ;
			}
		
	}
	//接收回调函数
 function addHuiDiaoJS()
	{
	 	var data2 = $("#frmPost1").find("#resultXML").val();
	   // alert(data2);
	   	var fuyemian1=$(window).manhuaDialog.getParentObj();
		fuyemian1.xiugaihang();
		var resultmsgobj = convertJson.string2json1(data2);
		var subresultmsgobj = resultmsgobj.response.data[0];
		var aa=$(subresultmsgobj).attr("GC_SJ_SJBG_JS2_ID");
		
		var ywid=$(subresultmsgobj).attr("GC_SJ_SJBG_JS2_ID");
		var SJBH=$(subresultmsgobj).attr("SJBH");
		var YWLX=$(subresultmsgobj).attr("YWLX");
		
		setFileData(ywid,"",SJBH,YWLX);
		var bgzt=$(subresultmsgobj).attr("BGZT");
		$("#BGZT").val(bgzt);
		$("#GC_SJ_SJBG_JS2_ID").val(aa); 
		$('#ZLLJSD').val(aa);
	    $("#DT11").setSelect(0);	
	}
	function eidtHuiDiaoJS()
	{
		var fuyemian1=$(window).manhuaDialog.getParentObj();
		fuyemian1.xiugaihang();
	}
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
		var shanchu=$("#deljs");
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
		//清空接收表单
		$("#demoForm1").clearFormResult(); 
		//清空接收选中的列表
		$("#DT11").cancleSelected();
		//清空领取列表
		$("#DT2").find("tbody").children().remove();
		//清空领取的接收 ID
		$('#ZLLJSD').val('');
		//清空领取表单
		$("#demoForm2").clearFormResult();
		$("#DT2").cancleSelected();
		 //清空附件列表
		clearFileTab();
	    $("#ywid").val(""); 
		right_user();
		right_defDate();
		jieshouren();
		left_defDate();
		bg_defDate();
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
	//图纸接收日期同步变更日期
	function LLRQ()
	{
		$("#BGRQ").val($("#JSRQ").val());
	}
</script>

<body id="a">
<div class="row-fluid">
<div class="span7"><!-- 第一页开始 -->
<blockquote>
 <app:dialogs/>
<div class="container-fluid">

 <!-- 第一页查询条件开始 -->
  <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
     <p></p>
 <div>
 <h5 style="color: black;">
  <span class="pull-right"> 
   <button id="btnZF" class="btn"  type="button">作废</button>
   <button id="btnExpExcel" class="btn"  type="button">导出</button>
  </span>
 </h5>
</div>
    <form method="post"  id="queryForm1"  >
      <table class="B-table" width="100%">
                               <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text" keep="true" fieldname="rownum"  value="1000" operation="<=" ></TD>
        </TR>
                               <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
   <!-- 第一页查询结果开始 -->
 <div style="height:5px;"></div>
<div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT11" pageNum="5"  printFileName="变更管理" type="single">
                <thead>
                   <tr>
	                    <th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
						<th fieldname="BGLLDJSRQ" tdalign="center" CustomFunction="doRandering" >&nbsp;联络单接收日期&nbsp;</th>
						<th fieldname="BGLB" tdalign="center" >&nbsp;变更类别&nbsp;</th>
						<th fieldname="JBGRQ" tdalign="center" >&nbsp;变更日期&nbsp;</th>
						<th fieldname="BGBH" maxlength="15"  >&nbsp;变更编号&nbsp;</th>
						<th fieldname="SJY" maxlength="13">&nbsp;来源单位&nbsp;</th>
						<th fieldname="JSRQ" tdalign="center" >&nbsp;图纸接收日期&nbsp;</th>
						<th fieldname="FS" maxlength="3" tdalign="right">&nbsp;接收份数&nbsp;</th>
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
      <h4 class="title">设计变更信息
          <span class="pull-right">
            <button id="example1" class="btn"  type="button">新增</button> 
            <button id="example2" class="btn"  type="button">保存</button>
          	<button id="deljs" class="btn"  type="button">删除</button>
          </span>
       </h4>
     <form method="post"  id="demoForm1"  >
       
      <table class="B-table" width="100%">
         <TR  style="display:none;">
	        <TD class="right-border bottom-border">  
	            <input type="text" class="span12" kind="text" id="JHID" keep="true" name = "JHID"  fieldname="JHID"  >
	              <input type="text" class="span12" kind="text" id="JHSJID" keep="true" name = "JHSJID"  fieldname="JHSJID"  >
	              <input type="text" class="span12" kind="text" id="ND" keep="true" name = "ND"  fieldname="ND"  >
	              <input type="text" class="span12" kind="text" id="SJWYBH" keep="true" name = "SJWYBH"  fieldname="SJWYBH"  >
	              <input type="text" class="span12" kind="text" id=XMID keep="true" name = "XMID"  fieldname="XMID"  >
	              <input type="text" class="span12" kind="text" id="BDID" keep="true" name = "BDID"  fieldname="BDID"  >
	              <input type="text" class="span12" kind="text" id="XMBH" keep="true" name = "XMBH"  fieldname="XMBH"  >
	              <input type="text" class="span12" kind="text" id="BDBH" keep="true" name = "BDBH"  fieldname="BDBH"  >
                  <input type="text" class="span12" kind="text" id="GC_SJ_SJBG_JS2_ID" fieldname="GC_SJ_SJBG_JS2_ID"  >
                  <input type="text" class="span12" id="BGZT" name = "BGZT"  fieldname="BGZT"  >
             </TD>
			<TD class="right-border bottom-border"></TD>
        </TR>
      <tr>
	          <th width="10%" class="right-border bottom-border disabledTh">项目名称</th>
	          <td width="20%" class="right-border bottom-border"><input class="span12 xmmc" keep="true" id="XMMC" type="text"  check-type="required"  disabled fieldname="XMMC"  name = "XMMC"></td>
	          <th width="10%" class="right-border bottom-border disabledTh">标段名称</th>
	          <td width="20%" class="right-border  bottom-border"><input class="span12"  keep="true" type="text"  id="BDMC"  fieldname="BDMC" disabled name = "BDMC"></td>
	          
        </tr> 
        <tr>
	          <th width="10%" class="right-border bottom-border disabledTh">设计单位</th>
	          <td width="20%" class="right-border bottom-border"><input class="span12" keep="true" id="SJDW" type="text"    disabled fieldname="SJDW"  name = "SJDW"></td>
	          <th width="10%" class="right-border bottom-border disabledTh">施工单位</th>
	          <td width="20%" class="right-border  bottom-border"><input class="span12"  keep="true" type="text"  id="SGDW"  fieldname="SGDW" disabled name = "SGDW"></td>
        </tr> 
        <tr>
	          <th width="10%" class="right-border bottom-border disabledTh">监理单位</th>
	          <td width="20%" class="right-border bottom-border"><input class="span12" keep="true" id="JLDW" type="text"   disabled fieldname="JLDW"  name = "JLDW"></td>
              <td width="20%" colspan="2" class="right-border bottom-border">
        </tr> 
        <tr>
              <th width="8%" class="right-border bottom-border">联络单接收日期</th>
	          <td width="42%" class="right-border  bottom-border"><input class="span12 date" type="date" id="BGLLDJSRQ"  name = "BGLLDJSRQ" check-type="required"  placeholder="必填" fieldname= "BGLLDJSRQ"></td>
	          <th width="8%" class="right-border bottom-border">变更类别</th>
	          <td width="42%"  class=" right-border  bottom-border"><select class="span12 3characters " id="BGLB" name = "BGLB"   check-type="required" fieldname = "BGLB"  kind="dic" src="BGLB2"  ></select></td>
        </tr>
        <tr>
               <th  class="right-border bottom-border">变更日期</th>
	           <td  class=" right-border bottom-border"><input class="span12 date" type="date" id="BGRQ" disabled name = "JBGRQ" fieldname= "JBGRQ" check-type=""></td>
	           <th  class="right-border bottom-border">变更编号</th>
	           <td  class="right-border bottom-border"><input class="span12" id="" type="text"  maxlength="36"   fieldname="BGBH" name = "BGBH"></td>
        </tr>
        <tr>
               <th  class="right-border bottom-border">变更费用</th>
	           <td  class=" right-border bottom-border"><input class="span6" type="number" style="text-align: right;" id="" min="0" name = "BGFY" fieldname= "BGFY" >&nbsp;&nbsp;<b>(元)</td>
              <th class="right-border bottom-border">来源单位</th>
	          <td  class="right-border bottom-border"><select class="span3 department "  style="width: 80%" id="SJY" name = "SJY"  fieldname="SJY" kind="dic" src="T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX='1' or DWLX='3' " >
                  </select>
                  <a href="javascript:void(0)" title="点击选择单位"><i id="lydwSelect" selObj="SJY" dwlx="3" class="icon-edit" onclick="selectCjdw('lydwSelect');" isLxSelect="1"></i></a>
                  </td>
        </tr>
        <tr>
	          <th  class="right-border bottom-border">图纸接收日期</th>
              <td  class=" right-border bottom-border"><input class="span12 date" type="date" id="JSRQ"  onchange="LLRQ()"   name = "JSRQ" fieldname= "JSRQ" /></td>
              <th  class="right-border bottom-border">联络单发放日期</th>
              <td  class=" right-border bottom-border"><input class="span12 date" type="date"    id="LLDFFRQ"  name = "LLDFFRQ" fieldname= "LLDFFRQ" /></td>
         </tr>
         <tr>
           	  <th  class="right-border bottom-border">接收份数</th>
              <td  class=" right-border bottom-border"><input class="span6" type="number" min="0"  style="text-align:right"   id="FS" check-type="number" maxlength="30"  name = "FS" fieldname= "FS" value="12"/></td>
              <th  class="right-border bottom-border">接收人</th>
              <td  class=" right-border bottom-border">
              <select class="span12 person" id="JSR" name = "JSR"  fieldname="JSR" kind="dic" src="T#fs_org_person:ACCOUNT:NAME:FLAG='1'  AND PERSON_KIND = '3' AND DEPARTMENT = '<%=department%>'  order by sort" ></select>
              <%-- <input class="span12" type="text" id="" maxlength="30"  name = "JSR" fieldname= "JSR" value="<%= username%>"/>
               --%>
              </td>
         </tr>
         <tr>
              <th  class="right-border bottom-border">变更内容</th>
	          <td  colspan="3" class=" right-border bottom-border"> <textarea class="span12" maxlength="4000" rows="2" id="BGNR" name = "BGNR" fieldname= "BGNR" ></textarea></td>
         </tr>
         <tr>
		       	<th width="8%" class="right-border bottom-border text-right text-right">附件上传</th>
		       	<td colspan="3" class="bottom-border right-border">
					<div>
						<span class="btn btn-fileUpload" onclick="doSelectFile(this);" fjlb="0046">
							<i class="icon-plus"></i><span>添加文件...</span>
						</span>
						<table role="presentation" class="table table-striped">
							<tbody fjlb="0046" class="files showFileTab" data-toggle="modal-gallery" data-target="#modal-gallery"></tbody>
						</table>
					</div>
				</td>
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
         <input type="hidden" name="txtFilter" order="desc" fieldname="b.XMBH,a.LRSJ"/>
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
 <!-- 第二页查询结果开始 -->
<p></p>
 <h5 class="title">&nbsp;
      <span class="pull-right">
             <button id="TXBMLQ" class="btn"  onclick="sendMsg('信息推送','SJBGGL')" type="button">提醒部门领取</button> 
             <button id="DYLQD" class="btn"  type="button">打印领取单</button>
      </span>
 </h5>
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
<div style="height:5px;"></div>
<div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" pageNum="5" id="DT2" type="single">
                <thead>
                   <tr>
	                    <th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
	                    <th fieldname="LQBM" maxlength="15" >&nbsp;领取部门&nbsp;</th>
						<th fieldname="LQR" maxlength="15" >&nbsp;领取人&nbsp;</th>
						<th fieldname="LQRQ" tdalign="center" >&nbsp;领取日期&nbsp;</th>
						<th fieldname="FS"  maxlength="3" tdalign="right" >&nbsp;领取份数&nbsp;</th>
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
    <form method="post"  id="queryForm2"  >
      <table class="B-table">
                               <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true"></TD>
        </TR>
                               <!--可以再此处加入hidden域作为过滤条件 -->
      </table>
      </form>
    </div>
  </div>
    <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">领取信息
          <span class="pull-right">
           <button id="example3" class="btn"   type="button">新增</button> 
           <button id="example4" class="btn"  type="button">保存</button>
           <button id="dellq" class="btn"  type="button">删除</button>
          </span>
       </h4>
     <form method="post"  id="demoForm2"  >
      <table class="B-table" width="100%">
               
         <TR  style="display:none;">
                  
	        <TD class="right-border bottom-border"> 
	          <input type="text" class="span12" kind="text" id="ZLLJSD" keep="true" name = "ZLLJSD"  fieldname="ZLLJSD"  >
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
	          <td width="42%" class="right-border bottom-border">
	          <select class="span12" check-type="required"  name="LQBM" fieldname="LQBM" id="LQBM" kind="dic" src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME"  onchange="xuzheren(this)"></select>
	          </td>
	          <th width="8%" class="right-border bottom-border">领取人</th>
	          <td width="42%" class="bottom-border">
	            <select class="span12 person" id="LQR" name = "LQR" check-type="required" fieldname = "LQR" operation="=" kind="dic" src="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG='1'  AND PERSON_KIND = '3'  order by sort">
                </select>
	          </td>
        </tr>
        <tr>
               <th  class="right-border bottom-border">领取日期</th>
	          <td  class="bottom-border"><input class="span12 date"  type="date" id="LQRQ" check-type="required"  placeholder="必填"  name = "LQRQ" fieldname= "LQRQ"></td>
	          <th  class="right-border bottom-border">领取份数</th>
	          <td  class="right-border bottom-border"> <input class="span6"  style="text-align:right" type="number" min="0" id="LQFS" maxlength="30" fieldname="FS" placeholder="必填"   check-type="required number" name = "FS"> </td>
        </tr>
        <tr>
	          <th  class="right-border bottom-border">办理人</th>
	          <td  class="right-border bottom-border">
	          <select class="span12 person" kind="dic" id="BLR" name = "BLR" fieldname= "BLR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1'  order by sort" ></select>
	          </td>
               <td  colspan="2" class="right-border bottom-border">
        </tr>
      </table>
      </form>
    </div>
  </div>
</div>
<jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
   <div align="center">
 	<FORM name="frmPost2" method="post" style="display:none" target="_blank" id="frmPost2">
		 系统保留定义区域
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" order="desc" fieldname="LRSJ"/>
         <!-- <input type="hidden" name="resultXML" id = "resultXML"> -->
         <input type="hidden" name="ywid" id = "ywid" value="">
         <input type="hidden" name="queryResult" id = "queryResult" value="">
       		 传递行数据用的隐藏域
         <input type="hidden" name="rowData">
 	</FORM>
 </div> 
</blockquote>
</div><!-- 第二页结束 -->
</div><!-- 最后一个div -->
</body>
</html>