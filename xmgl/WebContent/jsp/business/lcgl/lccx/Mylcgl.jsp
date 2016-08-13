<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<script type="text/javascript" charset="utf-8">
  var controllername= '${pageContext.request.contextPath }/ProcessAction.do';
  var controllername_bl= "${pageContext.request.contextPath }/TaskAction.do";
  var tab_id = "DT2";

	//计算本页表格分页数
	function setPageHeight(){
		var getHeight=getDivStyleHeight();
		var height = getHeight-pageTopHeight-pageTab-pageQuery*2-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
		$("#DT2").attr("pageNum",pageNum);
		$("#DT3").attr("pageNum",pageNum);
	};
  
	$(function() {
		getNd();
		getNd2();
		getNd3();
		setPageHeight();
		$("#addOption").hide();
    	g_bAlertWhenNoResult=false;	
    	//查询我处理的流程
 		 $("#txtFilter").attr("fieldname","CREATETIME");
 		 $("#txtFilter").attr("order","desc");

  		 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	    var lczt =  $("#QRESULT  option:selected").val();
		defaultJson.doQueryJsonList(controllername+"?QueryMySplc&lczt="+lczt,data,DT1);
		
    	//查询我发起的流程
		 $("#txtFilter").attr("fieldname","CREATETIME");
 		 $("#txtFilter").attr("order","desc");

 		 data = combineQuery.getQueryCombineData(queryForm3,frmPost,DT3);
		 defaultJson.doQueryJsonList(controllername+"?mycllc",data,DT3);
		
		
		//查询我办理的流程
 		 $("#txtFilter").attr("fieldname","cjsj");
 		 $("#txtFilter").attr("order","desc");

		 data = combineQuery.getQueryCombineData(queryForm_bl,frmPost,DT2);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername_bl+"?getUserTask",data,DT2);
		
		g_bAlertWhenNoResult=true;
		//我处理的流程按钮响应函数
		var btn = $("#query");
		btn.click(function() {
			//生成json串
			$("#txtFilter").attr("fieldname","CREATETIME");
	 		 $("#txtFilter").attr("order","desc");
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
		    var lczt = $("#QRESULT  option:selected").val();
			var ywxl = $("#QVALUE3  option:selected").val();
			defaultJson.doQueryJsonList(controllername+"?QueryMySplc&lczt="+lczt+"&ywxl="+ywxl,data,DT1);
			//
				});
		var clean=$("#clean");
		clean.click(function(){
			$("#queryForm").clearFormResult();
			$("#QRESULT").get(0).options[0].selected=true;  
			//initCommonQueyPage();
			getNd2();
		});
		
		
		var btn = $("#query_fq");
		btn.click(function() {
			//生成json串
			$("#txtFilter").attr("fieldname","CREATETIME");
	 		 $("#txtFilter").attr("order","desc");
			var data = combineQuery.getQueryCombineData(queryForm3,frmPost,DT3);
			//调用ajax插入
			var dqblr = $("#DQBLR").val();
			defaultJson.doQueryJsonList(controllername+"?mycllc&dqblr="+dqblr,data,DT3);
			//
				});
		var clean=$("#clean_fq");
		clean.click(function(){
			$("#queryForm3").clearFormResult();
			//initCommonQueyPage();
			getNd3();
		});
		
		
		   //监听变化事件
	    $("#QCJDWDM").on("change", function(){
		   generatePc($("#QCJRID"));
	   }); 
	    $("#QCJDWDM1").on("change", function(){
			   generatePc1($("#QCJRID1"));
		   }); 
		var btn = $("#print_cl");
		btn.click(function() {
			
		 if(tab_id=="DT2"){
			 $("#txtFilter").attr("fieldname","cjsj");
	 		 $("#txtFilter").attr("order","desc");

			 data = combineQuery.getQueryCombineData(queryForm_bl,frmPost,DT2);
			 //调用ajax插入
			 defaultJson.doQueryJsonList(controllername_bl+"?getUserTask",data,DT2);
			 
		 }else if(tab_id =="DT1"){
			 	//查询我发起的流程
	 		 $("#txtFilter").attr("fieldname","CREATETIME");
	 		 $("#txtFilter").attr("order","desc");

	  		 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		     var lczt =  $("#QRESULT  option:selected").val();
			 defaultJson.doQueryJsonList(controllername+"?QueryMySplc&lczt="+lczt,data,DT1);
		 }else if(tab_id =="DT3"){
			 $("#txtFilter").attr("fieldname","CREATETIME");
	 		 $("#txtFilter").attr("order","desc");
	 		 data = combineQuery.getQueryCombineData(queryForm3,frmPost,DT3);
			 defaultJson.doQueryJsonList(controllername+"?mycllc",data,DT3);
		 }	   
			 var t = $("#"+tab_id).getTableRows();
			 if(t<=0)
			 {
				 xAlert("提示信息","请至少查询出一条记录！",'3');
				 return;
			 }
			 printTabList(tab_id);	         
		});
		   
		   
		
		//按钮绑定事件（查询）
		$("#btnQuery").click(function() {

			//生成json串
		    var lczt =  $("#QRESULT  option:selected").val();
	 		 $("#txtFilter").attr("fieldname","cjsj");
	 		 $("#txtFilter").attr("order","desc");
			var data = combineQuery.getQueryCombineData(queryForm_bl,frmPost,DT2);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername_bl+"?getUserTask",data,DT2);
		});	 
		//按钮绑定事件（清空）
	    $("#btnClear").click(function() {
	        $("#queryForm_bl").clearFormResult();
	        getNd();
	    });
		
		$('#tabPage0').on('show', function (e) {
			tab_id = "DT2";
			$("#addOption").hide();
		});
		$('#tabPage1').on('show', function (e) {
			tab_id = "DT1";
			$("#addOption").show();
		});
		$('#tabPage2').on('show', function (e) {
			tab_id = "DT3";
			$("#addOption").hide();
		});
		
	});
	
/*     $(document).ready(function() {

   }); */
    
  //详细信息
    function rowView(index){

        var obj=$("#DT1").getRowJsonObjByIndex(index);//获取行对象
    	//var xmbh=eval("("+obj+")").XMBH;//取行对象<项目编号>
    	var wsid,ywlx,sjbh,spzt,spbh ;
//     wsid = obj.WS_TEMPLATEID;
     ywlx = obj.VALUE3;
     sjbh = obj.EVENTID;
     spzt = obj.STATE;
     spbh = obj.PROCESSOID;

	 /*    var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid="+wsid+"|isEdit=1|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
	    wsActionURL = "/xmgl/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wsid+"&isEdit=0"+"&isview=1";
	  	wsActionURL = "/xmgl/jsp/business/lcgl/lccx/processDetail.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wsid+"&isEdit=0"+"&isview=1"+"&spbh="+spbh;
	    window.open(wsActionURL); */
	   	  	 var s = "/xmgl/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";   
	  	 $(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
       //  parent.$("body").manhuaDialog({"title":"流程查询>流程详细信息","type":"text","content":wsActionURL,"modal":"1"});
    	//parent.$("body").manhuaDialog(xmscUrl(xmbh));//调用公共方法,根据项目编号查询
    }
   function rowView_(index){

       var obj=$("#DT3").getRowJsonObjByIndex(index);//获取行对象
   	//var xmbh=eval("("+obj+")").XMBH;//取行对象<项目编号>
   	var wsid,ywlx,sjbh,spzt,spbh ;
//    wsid = obj.WS_TEMPLATEID;
    ywlx = obj.VALUE3;
    sjbh = obj.EVENTID;
    spzt = obj.STATE;
    spbh = obj.PROCESSOID;

	/*     var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid="+wsid+"|isEdit=1|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
	  //  wsActionURL = "/xmgl/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wsid+"&isEdit=0"+"&isview=1";
	  	    wsActionURL = "/xmgl/jsp/business/lcgl/lccx/processDetail.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wsid+"&isEdit=0"+"&isview=1"+"&spbh="+spbh;
	    //window.open(wsActionURL); */
	  	 var s = "/xmgl/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";     
	  	 $(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
       // parent.$("body").manhuaDialog({"title":"流程查询>流程详细信息","type":"text","content":wsActionURL,"modal":"1"});
   	//parent.$("body").manhuaDialog(xmscUrl(xmbh));//调用公共方法,根据项目编号查询
   }
   //办理流程链接响应函数
   function doDetail(i)
   {
   	var rowJson = $("#DT2").getSelectedRowJsonByIndex(i);
   	var rowObject = JSON.parse(rowJson);
   	var url =  '${pageContext.request.contextPath}/'+rowObject.LINKURL+'?taskid='+rowObject.ID+'&taskseq='+rowObject.SEQ+'&sjbh='+rowObject.SJBH+'&ywlx='+rowObject.YWLX+'&spbh='+rowObject.SPBH+'&rwlx='+rowObject.RWLX+"&isRead="+rowObject.XB;
   	$(window).manhuaDialog({"title":"审批信息","type":"text","content":url,"modal":"1"});  
   }
  function  procecessApply(){
		var url =  '${pageContext.request.contextPath}/jsp/business/lcgl/lcsq/lcsq.jsp';
	  $(window).manhuaDialog({"title":"流程申请","type":"text","content":url,"modal":"2"});  
  }
  function  lslccx(){
		var url =  '${pageContext.request.contextPath}/jsp/business/lcgl/lccx/lccx.jsp?byPerson=1';
	  $(window).manhuaDialog({"title":"历史流程查询","type":"text","content":url,"modal":"2"});  
  }

   function gengxinchaxun(obj)
   {
   		xAlertMsg(obj);
   		g_bAlertWhenNoResult=false;	
		 $("#txtFilter").attr("fieldname","CREATETIME");
 		 $("#txtFilter").attr("order","desc");

 		 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
	    var lczt =  $("#QRESULT  option:selected").val();
		defaultJson.doQueryJsonList(controllername+"?QueryMySplc&lczt="+lczt,data,DT1);
		
		
		 $("#txtFilter").attr("fieldname","cjsj");
 		 $("#txtFilter").attr("order","desc");

		 data = combineQuery.getQueryCombineData(queryForm_bl,frmPost,DT2);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername_bl+"?getUserTask",data,DT2);
		
		g_bAlertWhenNoResult=true;
   }
   //关闭事件
   function closeNowCloseFunction()
   {
	   try {
	      	window.parent.getProcess();
	      	var fuyemian=$(window).manhuaDialog.getParentObj();
			var s = fuyemian.Mylcgengxinchaxun;
		    if(s){
			   fuyemian.Mylcgengxinchaxun();
		    } else {
	      		var frameW = $(window).manhuaDialog.getFrameObj();
	      		var w = frameW.Mylcgengxinchaxun;
		      	if(w){
		      		frameW.Mylcgengxinchaxun();
		      	}
	       	}
	   } catch(e) {
	      	window.parent.getProcess();
	   }
   }
/*    $("#QCJDWDM").change(function(){
		alert(1);
	}); */

   
	 //人员查询
   function generatePc(ndObj) {
		// 默认查询所有人员
		var srcVal = "T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG = 1  AND PERSON_KIND = '3'";
		if($("#QCJDWDM").val()) {
			srcVal = "T#FS_ORG_PERSON:ACCOUNT:NAME:DEPARTMENT = "+$("#QCJDWDM").val()+ " AND FLAG=1  AND PERSON_KIND = '3' order by sort";
		}
   		ndObj.attr("src", srcVal);
		ndObj.attr("kind", "dic");
		ndObj.html('');
   		reloadSelectTableDic(ndObj);
   }
	
	function generatePc1(ndObj) {
		// 默认查询所有人员
		var srcVal = "T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG = 1  AND PERSON_KIND = '3'";
		if($("#QCJDWDM1").val()) {
			srcVal = "T#FS_ORG_PERSON:ACCOUNT:NAME:DEPARTMENT = "+$("#QCJDWDM1").val()+ " AND FLAG=1  AND PERSON_KIND = '3' order by sort";
		}
	   	ndObj.attr("src", srcVal);
			ndObj.attr("kind", "dic");
			ndObj.html('');
	   	reloadSelectTableDic(ndObj);
	}
	function  isRecallProcess(obj){
		var sfsy = obj.SFSY;
		if(sfsy&&sfsy!=""){
			return "否";
		}else{
			// obj.VALUE3是业务类型 add by xhb on 2014-03-10
			return "<a href=\"javascript:void(0)\" onclick=\"recallProcess('"+obj.PROCESSOID+"','"+obj.EVENTID+"','"+obj.VALUE3+"')\">是</a>";
		}
		
	}
	function prcCallback(){
		 $("#txtFilter").attr("fieldname","CREATETIME");
 		 $("#txtFilter").attr("order","desc");

 		var  data = combineQuery.getQueryCombineData(queryForm3,frmPost,DT3);
		 defaultJson.doQueryJsonList(controllername+"?mycllc",data,DT3);
	}
	function recallProcess(processoid,sjbh,ywlx){
		xConfirm("提示信息","是否确认撤回审批信息！");
		$('#ConfirmYesButton').attr('click','');
	 	$('#ConfirmYesButton').bind("click",function(){
	 		var stAction = "<%=request.getContextPath()%>/TaskBackAction.do?deleteProcess";
	 	    var data1 = "sjbh="+sjbh+"&spbh="+processoid+"&ywlx="+ywlx;
	 		$.ajax({
	 			type : 'post',
	 			url : stAction,
	 			data : data1,
	 			dataType : 'json',
	 			async :	false,
	 			success : function(result) {
	 				var status = result.status;
	 				var msg = result.Msg;
	 				if(status=="sucess"){
	 		  		  var rowindex = $("#DT3").getSelectedRowIndex();
	 				  $("#DT3").removeResult(rowindex);
	 				}
	 				xAlert("信息提示",msg);
	 			},
	 		    error : function(result) {
	 		    	 //alert(result);
	 			     //alert(result.msg);
	 		    	 xAlertMsg(result.Msg);
	 			}
	 		});
	 		
	 	});  
	}
function closeParentCloseFunction(obj){
     if(obj!="1"){
	   return;
     }
	 if(tab_id=="DT2"){
		 $("#txtFilter").attr("fieldname","cjsj");
		 $("#txtFilter").attr("order","desc");

		 data = combineQuery.getQueryCombineData(queryForm_bl,frmPost,DT2);
		 //调用ajax插入
		 defaultJson.doQueryJsonList(controllername_bl+"?getUserTask",data,DT2);
		 
	 }else if(tab_id =="DT1"){//待处理流程
		 	//查询我发起的流程
		 $("#txtFilter").attr("fieldname","CREATETIME");
		 $("#txtFilter").attr("order","desc");

 		 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	     var lczt =  $("#QRESULT  option:selected").val();
		 defaultJson.doQueryJsonList(controllername+"?QueryMySplc&lczt="+lczt,data,DT1);
	 }else if(tab_id =="DT3"){
		 $("#txtFilter").attr("fieldname","CREATETIME");
		 $("#txtFilter").attr("order","desc");
		 data = combineQuery.getQueryCombineData(queryForm3,frmPost,DT3);
		 defaultJson.doQueryJsonList(controllername+"?mycllc",data,DT3);
	 }
}

// 补充意见
function bcyj() {
	if($("#"+tab_id+"").getSelectedRowIndex()==-1) {
		requireFormMsg("请选择一条要操作的数据！");
		return;
	}
	var sjbh =	$("#"+tab_id+"").getSelectedRowJsonObj().EVENTID;
	var ywlx =	$("#"+tab_id+"").getSelectedRowJsonObj().VALUE3;
	var s = "${pageContext.request.contextPath}/jsp/business/lcgl/lccx/taskYjView.jsp?sjbh="+sjbh+"&ywlx="+ywlx;
	$(window).manhuaDialog({"title":"补充意见","type":"text","content":s,"modal":"2"});

}
//默认年度
function getNd(){
	setDefaultNd("qnd");
}
function getNd2(){
	setDefaultNd("qnd2");
}
function getNd3(){
	setDefaultNd("qnd3");
}
</script>
</head>
<body>
<app:dialogs/>
	<div style="height:2px;"></div>
	<div class="container-fluid">
	<div class="row-fluid">
	
	<p class="text-right tabsRightButtonP">
			<button class="btn success-b" id="addOption" onclick="bcyj()">补充意见</button>
			<button class="btn success-b" onClick="procecessApply()">流程申请</button>
			<button id="print_cl" class="btn"  type="button">导出</button>
			<button id="button_lslccx" class="btn"  type="button" onclick="lslccx()">历史查询</button>
	</p>
     <ul class="nav nav-tabs">
         <li class="active"><a href="#profile" data-toggle="tab" id="tabPage0"><b>待处理流程</b></a></li>
         <li><a href="#home" class="active" data-toggle="tab" id="tabPage1"><b>我处理的流程</b></a></li>
         <li><a href="#myprocess" class="active" data-toggle="tab" id="tabPage2"><b>我发起的流程</b></a></li>
     </ul>
 	<div class="tab-content" style="border-bottom:0px;border-left:0px;border-right:0px;">
 
   <!-- 我处理的流程 begin -->
  	<div class="tab-pane" id="home">
  	 <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
     <form method="post"  id="queryForm" width="100%" >
      <table class="B-table" style="width:100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="2000" operation="<=" keep="true"></TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
           <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="6%">
            <select class="span12 year" id="qnd2" name = "QND" defaultMemo="全部" fieldname = "to_char(CREATETIME,'yyyy')" operation="=" kind="dic" src="T#AP_PROCESSINFO: distinct TO_CHAR(CREATETIME,'yyyy') as JNRQ:TO_CHAR(CREATETIME,'yyyy') as x:1=1 ORDER BY JNRQ ASC">
            </select>
          </td>
          <th class="right-border bottom-border" width="8%">业务类型</th>
          <td class="right-border bottom-border" width="15%">
         	 <select class="span12 10characters" id="QVALUE3" name = "VALUE3" fieldname="VALUE3" kind="dic" src="T#ap_ws_typz t ,fs_dic_tree a:distinct(t.ywlx): a.dic_value:a.dic_code = t.ywlx and parent_id = '9000000000009' and sfyx='1'  order by t.ywlx" operation="=" defaultMemo="全部"></select>
          </td>
          <th class="right-border bottom-border" width="8%">流程状态</th>
          <td class="right-border bottom-border" width="15%">
         	 <select class="span12 4characters" id="QRESULT" name = "RESULT">
	         	 <option value="">全部</option>
	         	 <option value="0">办理中</option>
	         	 <option value="1">办理完成</option>
	         	 <option value="3">已中断</option>
         	 </select>
          </td>
                   
		  <th class="right-border bottom-border" width="8%">流程内容</th>
          <td class="right-border bottom-border" width="35%">
             <input class="span12"  type="text" id="MENO2" name = "MEMO2" fieldname="MEMO" operation="like"  >
          </td>
        
        </tr>
          
        <tr>
          
          <th class="right-border bottom-border">申请部门</th>
          <td class="right-border bottom-border" colspan="3">
         	 
         	 <select class="span12 department" type="text"  id="QCJDWDM" fieldname="CJDWDM" name="CJDWDM" operation="=" kind="dic"
										src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" defaultMemo="全部"></select>
          </td>
          <th class="right-border bottom-border">申请人</th>
          <td class="right-border bottom-border" colspan=2>
         	 <select class="span12 department" type="text"  id="QCJRID" fieldname="CJRID" name="CJRID" operation="=" defaultMemo="全部" kind = "dic" src ="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG = 1  AND PERSON_KIND = '3'"></select>
          </td>
        <td class="text-left bottom-border text-right">
							<button id="query" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	                        <button id="clean" class="btn btn-link"   type="button"><i class="icon-trash"></i>清空</button>
		</td>	
        </tr>
      </table>
      </form>
      <div style="height:5px;"> </div>		
            <table width="100%" class="table-hover table-activeTd B-table" style="border-top:0px;" id="DT1" type="single">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH" tdalign="center" style="width:10px;">&nbsp;#&nbsp;</th>
                    		<th fieldname="VALUE3" hasLink="true" linkFunction="rowView">&nbsp;业务名称 &nbsp;</th>
							<th fieldname="MEMO" maxlength="18">&nbsp;流程内容&nbsp;</th>
							<th fieldname="CJRID" >&nbsp;申请人&nbsp;</th>
							<th fieldname="CJDWDM" >&nbsp;申请部门&nbsp;</th>
							<th fieldname="BLSJ" tdalign="center">&nbsp;办理时间&nbsp;</th>
							<th fieldname="CREATETIME" tdalign="center">&nbsp;创建时间&nbsp;</th>
							<th fieldname="CLOSETIME" tdalign="center">&nbsp;结束时间&nbsp;</th>
							<th fieldname="RESULT" tdalign="center">&nbsp;状态&nbsp;</th>
						</tr>
					</thead>
					<tbody>
                	</tbody>
				</table>
			</div>
			</div>
						</div>
			   <!-- 我处理的流程 end -->
			   
			   
			   
			      <!-- 我发起的流程 begin -->
			      
  <div class="tab-pane" id="myprocess">
      <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
     <form method="post"  id="queryForm3" width="100%" >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="2000" operation="<=" keep="true"></TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
										<tr>
										   <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="6%">
            <select class="span12 year" id="qnd3" name = "QND" defaultMemo="全部" fieldname = "to_char(CREATETIME,'yyyy')" operation="=" kind="dic" src="T#AP_PROCESSINFO: distinct TO_CHAR(CREATETIME,'yyyy') as JNRQ:TO_CHAR(CREATETIME,'yyyy') as x:1=1 ORDER BY JNRQ ASC">
            </select>
          </td>
											<th style="width: 5%" class="right-border bottom-border">
												业务类型
											</th>
											<td style="width: 8%" class="right-border bottom-border">
												<select class="span12 10characters" id="QVALUE3"
													name="VALUE3" fieldname="VALUE3" kind="dic" src="T#ap_ws_typz t ,fs_dic_tree a:distinct(t.ywlx): a.dic_value:a.dic_code = t.ywlx and parent_id = '9000000000009' and sfyx='1'  order by t.ywlx"
													operation="=" defaultMemo="全部"></select>
											</td>
											<th style="width: 5%" class="right-border bottom-border">
												状态
											</th>
											<td style="width: 8%" class="right-border bottom-border">
												<select class="span12 10characters" id="RESULT_Q"
													name="RESULT_Q" fieldname="RESULT" kind="dic" src="LCZT"
													operation="=" defaultMemo="全部"></select>
											</td>

											<th class="right-border bottom-border text-right" width="5%">
												流程内容
											</th>
											<td class="bottom-border" width="30%" colspan=3>
												<input class="span12" type="text" id="MENO1" name="MEMO1"
													fieldname="MEMO" operation="like">
											</td>
										</tr>
										<tr>
											<th width="5%" class="right-border bottom-border">
												创建时间
											</th>
											<td width="8%" class="bottom-border">
												<input class="span12 date" type="date" placeholder=""
													id="CREATETIME_S" name="CREATETIME_S"
													fieldname="CREATETIME" operation=">=" fieldtype="date"
													fieldformat="YYYY-MM-DD">
											</td>
											<th width="2%" class="right-border bottom-border">
												至
											</th>
											<td width="8%" class="bottom-border">
												<input class="span12 date" type="date" placeholder=""
													id="CREATETIME_E" name="CREATETIME_E"
													fieldname="CREATETIME" operation="<=" fieldtype="date"
													fieldformat="YYYY-MM-DD">
											</td>
											<th width="5%" class="right-border bottom-border text-right">
												当前办理人
											</th>
											<td class="bottom-border" width="8%">
												<input class="span12" type="text" name="P.NAME"  fieldname="FUN_DQBLR(ap.EVENTID)" 
													id="DQBLR" operation="like">
											</td>
											<td width="18%" class="text-left bottom-border text-right" colspan=2>
												<button id="query_fq" class="btn btn-link" type="button">
													<i class="icon-search"></i>查询
												</button>
												<button id="clean_fq" class="btn btn-link" type="button">
													<i class="icon-trash"></i>清空
												</button>
											</td>
										</tr>
									</table>
      </form>
      <div style="height:5px;"> </div>		
            <table width="100%" class="table-hover table-activeTd B-table" style="border-top:0px;" id="DT3" type="single">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH" tdalign="center" style="width:10px;">&nbsp;#&nbsp;</th>
                    		<th fieldname="VALUE3" hasLink="true" linkFunction="rowView_">&nbsp;业务名称&nbsp;</th>
							<th fieldname="MEMO" maxlength="18">&nbsp;流程内容&nbsp;</th>
							<th fieldname="CJRID" >&nbsp;申请人&nbsp;</th>
							<th fieldname="CJDWDM" >&nbsp;申请部门&nbsp;</th>
							<th fieldname="CREATETIME" tdalign="center">&nbsp;创建时间&nbsp;</th>
							<th fieldname="CLOSETIME" tdalign="center">&nbsp;结束时间&nbsp;</th>
							<th fieldname="DQBLR" tdalign="left" maxlength=9>&nbsp;当前办理人&nbsp;</th>
							<th fieldname="RESULT" tdalign="center">&nbsp;状态&nbsp;</th>
							<th fieldname="SFSY" tdalign="center" CustomFunction="isRecallProcess">&nbsp;撤回&nbsp;</th>
							
						</tr>
					</thead>
					<tbody>
                	</tbody>
				</table>
			</div>
			</div>
						</div>
			   <!-- 我发起的流程 end -->
			
			   <!-- 我办理的流程 begin -->
			   
			
			<div class="tab-pane active" id="profile">

			    <div class="row-fluid">
     <div class="B-small-from-table-autoConcise">
     
     <form method="post" id="queryForm_bl">
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			<input  type="text" name = "rownum" fieldname = "rownum" operation="<=" value="2000">
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->

        <tr>
          <th width="5%" class="right-border bottom-border text-right">年度</th>
          <td class="right-border bottom-border" width="6%">
            <select class="span12 year" id="qnd" name = "QND" defaultMemo="全部" fieldname = "to_char(t.CJSJ,'yyyy')" operation="=" kind="dic" src="T#AP_TASK_SCHEDULE: distinct TO_CHAR(CJSJ,'yyyy') as JNRQ:TO_CHAR(CJSJ,'yyyy') as x: 1=1 ORDER BY JNRQ ASC">
            </select>
          </td>
          <th class="right-border bottom-border text-right" width="5%">申请部门</th>
          <td class="right-border bottom-border" >
         	 
         	 <select class="span12 department" type="text"  id="QCJDWDM1" fieldname="t.CJDWDM" name="CJDWDM" operation="=" kind="dic"
										src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" defaultMemo="全部"></select>
          </td>
          <th class="right-border bottom-border text-right" width="5%">上级办理人</th>
          <td class="right-border bottom-border" >
           <select class="span12 person" type="text"  id="QCJRID1" fieldname="t.CJRID" name="CJRID" operation="=" defaultMemo="全部" kind = "dic" src ="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG = 1  AND PERSON_KIND = '3'"></select>
          </td>
          <th class="right-border bottom-border text-right" width="5%">办理业务</th>
          <td class="right-border bottom-border" >
           <select class="span12 person" type="text" style="width:100%" id="YWLX" fieldname="YWLX" name="YWLX" operation="=" defaultMemo="全部" kind = "dic" src ="T#ap_ws_typz t ,fs_dic_tree a:distinct(t.ywlx): a.dic_value:a.dic_code = t.ywlx and parent_id = '9000000000009' and sfyx='1'  order by t.ywlx"></select>
          </td>
          
           <td class="right-border bottom-border text-right" width="20%" rowspan="2">
            <button id="btnQuery" class="btn btn-link"  type="button" ><i class="icon-search"></i>查询</button>
      		<button id="btnClear" class="btn btn-link" type="button" ><i class="icon-trash"></i>清空</button>
      		
          </td>
         </tr>
         <tr>
         <th class="right-border bottom-border text-right" width="5%">待办标题</th>
          <td class="right-border bottom-border" width="40%" colspan="3">
             <input class="span12"  type="text" id="MEMO" name = "MEMO" fieldname = "t.MEMO" operation="like"  >
          </td>
                     <th class="right-border bottom-border text-right" width="5%">申请时间</th>
          	<td  class="right-border bottom-border" >
			<input  class="date" type="date" name = "QCJSJGT" fieldname = "CJSJ" operation=">=" fieldtype="date" fieldformat="YYYY-MM-DD">
			</td>
			<th class="right-border bottom-border text-center" width="5%">至</th>
			<td class="right-border bottom-border" >
				<input class="date" type="date" name = "QCJSJLT" fieldname = "CJSJ" operation="<=" fieldtype="date" fieldformat="YYYY-MM-DD">
         	 </td>
         	 
         </tr>
      </table>
      </form>
      <div style="height:5px;"> </div>
	<table class="table-hover table-activeTd B-table" id="DT2"  width="100%" style="border-top:0px;" type="single" pageNum="8">
		<thead>
					<tr>
						<th name="XH" id="_XH" tdalign="center" noprint="true" style="width:10px;">&nbsp;#&nbsp;</th>
						<th fieldname="MEMO" tdalign="left" maxlength="40" hasLink="true" linkFunction="doDetail">&nbsp;待办标题&nbsp;</th>
						<th fieldname="YWLX"  tdalign="left" >&nbsp;办理业务&nbsp;</th>
						<th fieldname="CJRID" tdalign="center" maxlength="15">&nbsp;上级办理人&nbsp;</th>
						<th fieldname="SQR" tdalign="center" maxlength="15">&nbsp;申请人&nbsp;</th>
						<th fieldname="CREATETIME" tdalign="center" >&nbsp;申请时间&nbsp;</th>
					</tr>
		</thead>
		<tbody>
           </tbody>
	</table>
	</div>
</div>
		    </div>
		    <!-- 我办理的流程 end -->
		    
			</div>
			
			</div>
            
		  </div>
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none"
			target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML"> 
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="txtFilter"  order="desc" fieldname = "CREATETIME"	id = "txtFilter">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
			<input type="hidden" name="queryResult" id = "queryResult">
			
		</FORM>
	</div>
</body>
</html>