<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<jsp:include page="/jsp/framework/common/spFlow/spwsJs.jsp" flush="true"/>
	<%
			String ywlx = request.getParameter("ywlx");
			String bmjkLx = request.getParameter("bmjkLx");
			String nd = request.getParameter("nd");
			String deptid = request.getParameter("deptid");
	%>
<script type="text/javascript" charset="utf-8">
  var controllername= '${pageContext.request.contextPath }/bmjkCommonController.do';
  var ywlx = "<%=ywlx %>";
  var bmjkLx = "<%=bmjkLx%>";
  var nd = "<%=nd%>";
  var deptid = "<%=deptid%>";
	//计算本页表格分页数
	function setPageHeight(){
		var getHeight=getDivStyleHeight();
		var height = getHeight-pageTopHeight-pageTitle-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
	$(function() {
		setPageHeight();
    	g_bAlertWhenNoResult=false;	
  		 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?QueryAllSplc&ywlx="+ywlx+"&bmjkLx="+bmjkLx+"&nd="+nd+"&deptid="+deptid,data,DT1);
		g_bAlertWhenNoResult=true;
	});
	
    
  //详细信息
    function rowView(index){

        var obj=$("#DT1").getRowJsonObjByIndex(index);//获取行对象
    	var wsid,ywlx,sjbh,spzt,spbh,operationoid ;
     wsid = obj.WS_TEMPLATEID;
     ywlx = obj.VALUE3;
     sjbh = obj.EVENTID;
     spzt = obj.STATE;
     spbh = obj.PROCESSOID;
     operationoid = obj.OPERATIONOID;
	wsid = getProwsid(ywlx,operationoid);
	    var wsActionURL = "/xmgl/PubWS.do?getXMLPrintAction|templateid="+wsid+"|isEdit=0|ywlx="+ywlx+"|sjbh="+sjbh+"|rowid="+Math.random()+".mht";
	     wsActionURL = "/xmgl/jsp/business/lcgl/lccx/processDetail.jsp?param="+wsActionURL+"&sjbh="+sjbh+"&ywlx="+ywlx+"&temlateid="+wsid+"&isEdit=0"+"&isview=1"+"&spbh="+spbh;
	         var s = "/xmgl/jsp/framework/common/aplink/defaultArchivePage.jsp?sjbh="+sjbh+"&ywlx="+ywlx+"&spbh="+sjbh+"&isview=1";   
	   	  	 $(window).manhuaDialog({"title":"流程信息","type":"text","content":s,"modal":"1"});  
    }


   
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
		if("000002"==ywlx){
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
       </h4>
     <form method="post"  id="queryForm" width="100%" >
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><INPUT type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" keep="true"></TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
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