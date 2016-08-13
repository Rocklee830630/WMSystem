<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
<script type="text/javascript" charset="utf-8">
  var controllername= '${pageContext.request.contextPath }/ProcessAction.do';
  
  
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
	//	alert("g_iHeight:"+g_iHeight+"|pageTopHeight:"+pageTopHeight+"|pageTitle:"+pageTitle+"|pageQuery:"+pageQuery+"|pageNumHeight:"+pageNumHeight+"|getTableTh(1):"+getTableTh(1));
	//	alert(Math.abs(height) +"|" + pageTableOne + " | " + pageNum);
		$("#DT1").attr("pageNum",pageNum);
	}
  
	$(function() {
		setPageHeight();
    	g_bAlertWhenNoResult=false;	
  		 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
	
		defaultJson.doQueryJsonList(controllername+"?QueryAllSplc",data,DT1);
		
		g_bAlertWhenNoResult=true;
		
		var btn = $("#query");
		btn.click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			//alert("data:"+data);
			var dqblr = $("#DQBLR").val();
			defaultJson.doQueryJsonList(controllername+"?QueryAllSplc&dqblr="+dqblr,data,DT1);
			//
				});
		var clean=$("#clean");
		clean.click(function(){
			$("#queryForm").clearFormResult();
			initCommonQueyPage();
		});
		   //监听变化事件
	    $("#QCJDWDM").on("change", function(){

		   //generatePc($("#QCJRID"));
	   }); 
	    $("#changBlr").click(function(){
	    	if($("#DT1").getSelectedRowIndex()==-1)
			{
				xAlert("提示信息",'请选择一条记录','3');
				return
		    }
        	var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
        	var spbh = $("#DT1").getSelectedRowJsonObj().PROCESSOID;
        	var ywlx = $("#DT1").getSelectedRowJsonObj().VALUE3;
	    	$(window).manhuaDialog({"title":"修改办理人","type":"text","content":"${pageContext.request.contextPath }/jsp/framework/common/aplink/changBlr.jsp?sjbh="+sjbh+"&spbh="+spbh+"&ywlx="+ywlx,"modal":"2"});
		});   
	    
	    
	    $("#thfqr").click(function() {
        	if($("#DT1").getSelectedRowIndex()==-1)
			{
				xAlert("提示信息",'请选择一条记录','3');
				return
		    }
        	var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
        	var spbh = $("#DT1").getSelectedRowJsonObj().PROCESSOID;
        	var ywlx = $("#DT1").getSelectedRowJsonObj().VALUE3;
        	xConfirm("提示信息","是否确认退回给发起人！");
			$('#ConfirmYesButton').unbind();
    	 	$('#ConfirmYesButton').bind("click",function(){
    	 		 var stAction = "${pageContext.request.contextPath }/TaskAction.do?AdminReturnFqr";
    	 		var data1 = "id=&seq=&sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+spbh+"&spjg=3&sfth=1&spUser=";
    	 		//data1+= "&tsdept="+document.all('USERTODEPT').value+"&tsperson="+document.all('USERTOPERSON').value+"&tsstep="+document.all("USERSTEP").value;
    	 		data1+= "&result=3&resultDscr=管理员退回发起人";
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
    	 		  		  var rowindex = $("#DT1").getSelectedRowIndex();
    	 				  //$("#DT1").removeResult(rowindex);
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
		}); 
        
        $("#finishProcess").click(function() {
        	if($("#DT1").getSelectedRowIndex()==-1)
			{
				xAlert("提示信息",'请选择一条记录','3');
				return
		    }
        	var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
        	var spbh = $("#DT1").getSelectedRowJsonObj().PROCESSOID;
        	var ywlx = $("#DT1").getSelectedRowJsonObj().VALUE3;
        	xConfirm("提示信息","是否确认结束审批信息！");
			$('#ConfirmYesButton').unbind();
    	 	$('#ConfirmYesButton').bind("click",function(){
    	 		var stAction = "<%=request.getContextPath()%>/TaskBackAction.do?deleteProcess";
    	 	    var data1 = "sjbh="+sjbh+"&spbh="+spbh+"&ywlx="+ywlx+"&isgly=1";
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
    	 		  		  var rowindex = $("#DT1").getSelectedRowIndex();
    	 				  $("#DT1").removeResult(rowindex);
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
		}); 
        
        $("#jumpStep").click(function(){
        	if($("#DT1").getSelectedRowIndex()==-1)
			{
				xAlert("提示信息",'请选择一条记录','3');
				return
		    }
        	var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
        	var spbh = $("#DT1").getSelectedRowJsonObj().PROCESSOID;
        	var ywlx = $("#DT1").getSelectedRowJsonObj().VALUE3;

        	 var s = "/xmgl/jsp/framework/common/aplink/jumpStepPage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+spbh;   
    	  	 $(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"}); 
        });
		   
		
        $("#jumpStepTest").click(function() {
        	if($("#DT1").getSelectedRowIndex()==-1)
			{
				xAlert("提示信息",'请选择一条记录','3');
				return
		    }
        	var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
        	var processId = $("#DT1").getSelectedRowJsonObj().PROCESSOID;
        	var ywlx = $("#DT1").getSelectedRowJsonObj().VALUE3;
        	var memo = $("#DT1").getSelectedRowJsonObj().MEMO;
        	
        	var sjzt = $("#DT1").getSelectedRowJsonObj().EVENTSJBH;
        	if("0" == sjzt) {
        		xAlert("提示信息",'任务未发起，处于【登记】状态，无法越级','3');
    			return;
    		} else if("7" == sjzt) {
        		xAlert("提示信息",'任务未发起，处于【退回】状态，无法越级','3');
    			return;
    		} else if("8" == sjzt) {
        		xAlert("提示信息",'任务处于【中断】状态，无法越级','3');
    			return;
    		} else if("2" == sjzt) {
        		xAlert("提示信息",'任务已【通过】，无法越级','3');
    			return;
    		}

			if(ywlx == "000002") {
        		xAlert("提示信息",'此任务是【工作联络单（自由报送）】，无法越级','3');
    			return;
        	}
			if(ywlx == "200502") {
        		xAlert("提示信息",'此任务是【文件处理单（自由报送）】，无法越级','3');
    			return;
        	}

        	var s = "/xmgl/jsp/business/lcgl/lccx/processJumpPage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&processId="+processId;   
    	  	$(window).manhuaDialog({"title":memo,"type":"text","content":s,"modal":"1"});
        });
        
        /* 重启流程 */
        $("#restart").click(function() {
        	if($("#DT1").getSelectedRowIndex()==-1)
			{
				xAlert("提示信息",'请选择一条记录','3');
				return
		    }
        	var sjbh = $("#DT1").getSelectedRowJsonObj().SJBH;
        	var processId = $("#DT1").getSelectedRowJsonObj().PROCESSOID;
        	var ywlx = $("#DT1").getSelectedRowJsonObj().VALUE3;
        	var memo = $("#DT1").getSelectedRowJsonObj().MEMO;
        	var isgd = $("#DT1").getSelectedRowJsonObj().ISGD;
        	
        	var sjzt = $("#DT1").getSelectedRowJsonObj().EVENTSJBH;
        	if("2" != sjzt) {
        		xAlert("提示信息",'任务未通过，无法重启','3');
    			return;
    		}
        	if(ywlx == "700101") {
        		xAlert("提示信息",'此任务是【合同会签单】业务，禁止重启','3');
    			return;
        	}
        	if(ywlx == "700204") {
        		xAlert("提示信息",'此任务是【提请款申请】业务，禁止重启','3');
    			return;
        	}
			if(ywlx == "700201") {
        		xAlert("提示信息",'此任务是【履约保证金返还会签单】业务，禁止重启','3');
    			return;
        	}
			if(ywlx == "300101") {
        		xAlert("提示信息",'此任务是【招标需求】业务，禁止重启','3');
    			return;
        	}
			if(ywlx == "000002") {
        	 	xConfirm("提示信息","是否确认重启此工作联系单？");
    			$('#ConfirmYesButton').unbind();
    			$('#ConfirmYesButton').one("click",function() {
    		    	var data1 = "sjbh="+sjbh;
    		    	var url = "${pageContext.request.contextPath }/ProcessJumpAction.do?restart000002";
    		    
    		     	$.ajax({
    		 			type : 'post',
    		 			url : url,
    		 			data : data1,
    		 			dataType : 'json',
    		 			async :	false,
    		 			success : function(result) {
    		 				var obj = eval("("+result+")");
    			 		   	xAlert("信息提示",obj.msg);
    			 		   	
    			 		   	// 重启以后，对类表的一些信息进行及时更新 start
    			 		   	restartUpdateList(obj.cjrxm);
    			 		   	// 重启以后，对类表的一些信息进行及时更新 end
    		 			},
    		 			error : function() {
    		 				var obj = eval("("+result+")");
    			 		   	xAlert("信息提示",obj.msg);
    		 			}
    		 		});
    			});
    			return;
        	}
			if(ywlx == "200502") {
        		xAlert("提示信息",'此任务是【文件处理单[收文]（自由报送）】，不在此处重启','3');
    			return;
        	}

        	if(isgd == "0") {
        		xAlert("提示信息",'任务未归档，无法重启','3');
    			return;
        	}
        	var s = "/xmgl/jsp/business/lcgl/lccx/processRestartPage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&processId="+processId;   
    	  	$(window).manhuaDialog({"title":memo,"type":"text","content":s,"modal":"1"});
        });
	});
	
/*     $(document).ready(function() {

   }); */
    
  //详细信息
    function rowView(index){

        var obj=$("#DT1").getRowJsonObjByIndex(index);//获取行对象
    	//var xmbh=eval("("+obj+")").XMBH;//取行对象<项目编号>
    	var wsid,ywlx,sjbh,spzt,spbh,operationoid ;
     wsid = obj.WS_TEMPLATEID;
     ywlx = obj.VALUE3;
     sjbh = obj.SJBH;
     spzt = obj.STATE;
     spbh = obj.PROCESSOID;
     operationoid = obj.OPERATIONOID;
	wsid = getProwsid(ywlx,operationoid);
	    var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid="+wsid+"|isEdit=0|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
	    //wsActionURL = "/xmgl/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wsid+"&isEdit=0"+"&isview=1";
	     wsActionURL = "/xmgl/jsp/business/lcgl/lccx/processDetail.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wsid+"&isEdit=0"+"&isview=1"+"&spbh="+spbh;
	   //window.open(wsActionURL);
	         var s = "/xmgl/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";   
	   	  	 $(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
    }

   
/*    $("#QCJDWDM").change(function(){
		alert(1);
	}); */

   
	 //人员查询
   function generatePc(ndObj){

   	ndObj.attr("src", "T#FS_ORG_PERSON:ACCOUNT:NAME:DEPARTMENT = "+$("#QCJDWDM").val()+ " AND FLAG=1  AND PERSON_KIND = '3' order by sort");
		ndObj.attr("kind", "dic");
		ndObj.html('');
   	reloadSelectTableDic(ndObj);
   }
	function tr_click(obj, tabListid) {
		var spzt = obj.RESULT;
		if(spzt=="0"){//未完成
			$("#changBlr").removeAttr("disabled");
			$("#finishProcess").removeAttr("disabled");
			$("#jumpStep").removeAttr("disabled");
			$("#thfqr").removeAttr("disabled");

		}else if(spzt=="1"){//已完成
			$("#changBlr").attr("disabled","disabled");
			$("#finishProcess").attr("disabled","disabled");
			$("#jumpStep").attr("disabled","disabled");
			$("#thfqr").attr("disabled","disabled");
		}
		var ywlx = obj.VALUE3;
		if("000002"==ywlx||"000003"==ywlx){
			$("#thfqr").attr("disabled","disabled");
			$("#jumpStep").attr("disabled","disabled");
		}else{
			$("#thfqr").removeAttr("disabled");
			$("#jumpStep").removeAttr("disabled");
		}
		
	}
	function gengxinchaxun(obj)
	{
	   		xAlertMsg(obj);
	   		setPageHeight();
	    	g_bAlertWhenNoResult=false;	
	  		 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
		
			defaultJson.doQueryJsonList(controllername+"?QueryAllSplc",data,DT1);
			
			g_bAlertWhenNoResult=true;
	   		
	}
	
	function jumpUpdateList(blrxm) {
		// 退回/跳级以后，对类表的一些信息进行及时更新 
		var index = $("#DT1").getSelectedRowIndex();
		var rowObj = $("#DT1").getSelectedRowJsonObj();
		if(!blrxm) {
			rowObj.DQBLR = blrxm;
			$("#DT1").updateResult(JSON.stringify(rowObj),DT1,index);
		}
		$("#DT1").setSelect(index);
	}
	
	function restartUpdateList(blrxm) {
		// 重启以后，对类表的一些信息进行及时更新 
		var index = $("#DT1").getSelectedRowIndex();
		var rowObj = $("#DT1").getSelectedRowJsonObj();
		if(blrxm) {
			rowObj.RESULT_SV = "办理中";
			rowObj.RESULT = "0";
			rowObj.EVENTSJBH = "1";
			rowObj.DQBLR = blrxm;
			rowObj.CLOSETIME_SV = "";
			$("#DT1").updateResult(JSON.stringify(rowObj),DT1,index);
			tr_click(rowObj, "#DT1");
		}
		$("#DT1").setSelect(index);
	}
	function doSfcq(obj){
		var sfcq = obj.SFCQ;
		var sfcq_sv = obj.SFCQ_SV;
//		sfcq_sv = dealSpecialCharactor(sfcq_sv);
		if(sfcq==0){
			return '<div style="text-align:center"><i title="正常执行" class="icon-green"></i></div>';
		}else{
			return '<div style="text-align:center"><i title="'+sfcq_sv+'" class="icon-red"></i></div>';
		}
		
	}
	 
</script>
</head>
<body>
<app:dialogs/>
	<div class="container-fluid">
		<p></p>
	<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">流程查询 
		<span class="pull-right">  
	      <app:oPerm url="/jsp/business/lcgl/lccx/liuccx.jsp?yjthTask">
	      <button id="jumpStepTest" class="btn"  type="button">越级审批/退回审批</button>
	      </app:oPerm>
	      <app:oPerm url="/jsp/business/lcgl/lccx/liuccx.jsp?cqTask">
	      <button id="restart" class="btn"  type="button">重启审批</button>
	      </app:oPerm>
	      <app:oPerm url="/jsp/business/lcgl/lccx/liuccx.jsp?xgblr">
		  <button id="changBlr" class="btn"  type="button">修改办理人</button>
		  </app:oPerm>
	      <app:oPerm url="/jsp/business/lcgl/lccx/liuccx.jsp?yjsp">
	   <!--    <button id="jumpStep" class="btn"  type="button">越级审批</button> -->
		  </app:oPerm>
	      <app:oPerm url="/jsp/business/lcgl/lccx/liuccx.jsp?scsp">
          <button id="finishProcess" class="btn"  type="button">删除审批</button>
		  </app:oPerm>
		  <app:oPerm url="/jsp/business/lcgl/lccx/liuccx.jsp?thfqr">
		  <button id="thfqr" class="btn"  type="button" style="display:none;">退回发起人</button>
		  </app:oPerm>
        </span>
       </h4>
     <form method="post"  id="queryForm" width="100%" >
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="10000" operation="<=" keep="true"></TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
							<tr>
								<th width="5%" class="right-border bottom-border">
									业务类型
								</th>
								<td width="15%" class="right-border bottom-border">
									<select class="span12" id="QVALUE3" name="VALUE3"
										fieldname="VALUE3" kind="dic" src="T#ap_ws_typz t ,fs_dic_tree a:distinct(t.ywlx): a.dic_value:a.dic_code = t.ywlx and parent_id = '9000000000009' and sfyx='1' order by t.ywlx" operation="="
										defaultMemo="全部"></select>
								</td>
								<th width="5%" class="right-border bottom-border">
									申请部门
								</th>
								<td width="21%" class="right-border bottom-border" colspan=3>

									<select class="span12" type="text" id="QCJDWDM"
										fieldname="CJDWDM" name="CJDWDM" kind="dic"
										src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME"
										operation="=" defaultMemo="全部"></select>
								</td>
								<th class="right-border bottom-border" width="5%">
									流程内容
								</th>
								<td class="bottom-border" width="30%" colspan=3>
									<input class="span12" type="text" id="MENO2" name="MEMO2"
										fieldname="MEMO" operation="like">
								</td>
							</tr>
							<tr>

								<th width="5%" class="right-border bottom-border">
									流程状态
								</th>
								<td width="15%" class="right-border bottom-border">
									<select class="span12" id="QRESULT" name="RESULT"
										fieldname="RESULT" kind="dic" src="LCZT" operation="="
										defaultMemo="全部"></select>
								</td>
								<th width="5%" class="right-border bottom-border">
									是否超期
								</th>
								<td width="8%" class="right-border bottom-border">
									<select class="span12" id="SFCQ" name="SFCQ" fieldname="SFCQ"
										kind="dic" src="SF" operation="=" defaultMemo="全部"></select>
								</td>
								<th width="5%" class="right-border bottom-border">
									申请人
								</th>
								<td width="8%" class="right-border bottom-border">
									<input class="span12" type="text" id="QCJRID"
										fieldname="P.NAME" name="P.NAME" defaultMemo="全部"
										operation="like" kind="text">
								</td>
								<th width="5%" class="right-border bottom-border text-right">
									当前办理人
								</th>
								<td class="bottom-border" width="8%">
									<input class="span12" type="text" name="P.NAME" id="DQBLR" fieldname="FUN_DQBLR(ap.EVENTID)" operation="like">
								</td>
								<td class="text-left bottom-border text-right" colspan=2>
									<button id="query" class="btn btn-link" type="button">
										<i class="icon-search"></i>查询
									</button>
									<button id="clean" class="btn btn-link" type="button">
										<i class="icon-trash"></i>清空
									</button>
								</td>
							</tr>
						</table>
      </form>
      <div style="height:5px;"> </div>		
          	<div class= "overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH">&nbsp;#&nbsp;</th>
                    		<th fieldname="VALUE3" hasLink="true" linkFunction="rowView">&nbsp;业务类型&nbsp;</th>
                    		<th fieldname="SFCQ" CustomFunction="doSfcq" noprint="true">&nbsp;是否超期&nbsp;</th>
							<th fieldname="MEMO" maxlength="17">&nbsp;流程内容&nbsp;</th>
							<th fieldname="CJRID" >&nbsp;申请人&nbsp;</th>
							<th fieldname="CJDWDM" >&nbsp;申请部门&nbsp;</th>
							<th fieldname="CREATETIME" tdalign="center">&nbsp;创建时间&nbsp;</th>
							<th fieldname="CLOSETIME" tdalign="center">&nbsp;结束时间&nbsp;</th>
							<th fieldname="DQBLR" tdalign="left" maxlength=10>&nbsp;当前办理人&nbsp;</th>
							<th fieldname="RESULT">&nbsp;状态&nbsp;</th>
						</tr>
					</thead>
					<tbody>
                	</tbody>
				</table>
				</div>
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
		</FORM>
	</div>
</body>
</html>