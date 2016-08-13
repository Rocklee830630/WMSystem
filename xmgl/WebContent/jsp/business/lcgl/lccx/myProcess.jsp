<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<script type="text/javascript" charset="utf-8">
  var controllername= '${pageContext.request.contextPath }/ProcessAction.do';
	$(function() {
		
    	g_bAlertWhenNoResult=false;	
  		 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
	    var lczt =  $("#QRESULT  option:selected").val();
		defaultJson.doQueryJsonList(controllername+"?QueryMySplc&lczt="+lczt,data,DT1);
		
		g_bAlertWhenNoResult=true;
		
		var btn = $("#query");
		btn.click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
		    var lczt =  $("#QRESULT  option:selected").val();
			defaultJson.doQueryJsonList(controllername+"?QueryMySplc&lczt="+lczt,data,DT1);
			//
				});
		var clean=$("#clean");
		clean.click(function(){
			$("#queryForm").clearFormResult();
			initCommonQueyPage();
		});
		   //监听变化事件
	    $("#QCJDWDM").on("change", function(){

		   generatePc($("#QCJRID"));
	   }); 
	});
	
/*     $(document).ready(function() {

   }); */
    
  //详细信息
    function rowView(index){

        var obj=$("#DT1").getRowJsonObjByIndex(index);//获取行对象
    	//var xmbh=eval("("+obj+")").XMBH;//取行对象<项目编号>
    	var wsid,ywlx,sjbh,spzt,spbh ;
     wsid = obj.WS_TEMPLATEID;
     ywlx = obj.VALUE3;
     sjbh = obj.EVENTID;
     spzt = obj.STATE;
     spbh = obj.PROCESSOID;
    	
	    var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid="+wsid+"|isEdit=0|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
	    wsActionURL = "/xmgl/jsp/business/lcgl/lccx/processDetail.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wsid+"&isEdit=0"+"&isview=1"+"&spbh="+spbh;
		//    wsActionURL = "/xmgl/jsp/framework/print/pubPrint.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wsid+"&isEdit=0"+"&isview=1";
	   // window.open(wsActionURL,"","width=800px,height=600px,toolar=0,menubar=0,scrollbars=1,status=0,resizable=1,screenX=0,screenY=0");
         $(window).manhuaDialog({"title":"流程查询>流程详细信息","type":"text","content":wsActionURL,"modal":"1"});
    }
   

   
/*    $("#QCJDWDM").change(function(){
		alert(1);
	}); */

   
	 //人员查询
   function generatePc(ndObj){
     	ndObj.attr("src", "T#FS_ORG_PERSON:ACCOUNT:NAME:DEPARTMENT = "+$("#QCJDWDM").val()+ " AND FLAG=1  AND PERSON_KIND = '3'");
		ndObj.attr("kind", "dic");
		ndObj.html('');
   	    reloadSelectTableDic(ndObj);
   }
	 
</script>
</head>
<body>
<app:dialogs/>
	<div class="container-fluid">
		<p></p>
	<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">
				流程查询 
	 </h4>
     <form method="post"  id="queryForm" width="100%" >
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true"></TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
          <th width="8%" class="right-border bottom-border">业务类型</th>
          <td width="10%" class="right-border bottom-border">
         	 <select class="span12" id="QVALUE3" name = "VALUE3" fieldname="VALUE3" kind="dic" src="YWLX" operation="=" defaultMemo="全部">
          </td>
          <th width="8%" class="right-border bottom-border">申请部门</th>
          <td width="10%" class="right-border bottom-border">
         	 
         	 <select class="span12" type="text"  id="QCJDWDM" fieldname="CJDWDM" name="CJDWDM" kind="dic"
										src="T#VIEW_YW_ORG_DEPT:ROW_ID:DEPT_NAME" defaultMemo="全部"></select>
          </td>
          <th width="8%" class="right-border bottom-border">申请人</th>
          <td width="10%" class="right-border bottom-border">
         	 <select class="span12" type="text"  id="QCJRID" fieldname="CJRID" name="CJRID" defaultMemo="全部" kind = "dic" src ="T#FS_ORG_PERSON:ACCOUNT:NAME:FLAG = 1  AND PERSON_KIND = '3'"></select>
          </td>
          <th width="8%" class="right-border bottom-border">流程状态</th>
          <td width="10%" class="right-border bottom-border">
         	 <select class="span12" id="QRESULT" name = "RESULT">
         	 <option value="">全部</option>
         	 <option value="0">办理中</option>
         	 <option value="1">办理完成</option>
         	 <option value="2">我发起的</option>
          </td>
        <td class="text-left bottom-border text-right">
							<button id="query" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	                        <button id="clean" class="btn btn-link"   type="button"><i class="icon-trash"></i>清空</button>
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
							<th fieldname="MEMO" >&nbsp;流程内容&nbsp;</th>
							<th fieldname="CJRID" >&nbsp;申请人&nbsp;</th>
							<th fieldname="CJDWDM" >&nbsp;申请部门&nbsp;</th>
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