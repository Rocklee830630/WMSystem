<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>前期手续综合信息</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script> 
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/qianQiShouXuController.do";
  var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do"; 
  var tbl = null;
  var json;
  var diaoyong;
  
//计算本页表格分页数
  function setPageHeight(){
  	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(3)-pageNumHeight;
  	var pageNum = parseInt(height/pageTableOne,10);
  	$("#DT1").attr("pageNum",pageNum);
  }
  
  
	$(function() {
		setPageHeight();
	     //查询
		var chaxun = $("#chaxun");
		chaxun.click(function() {
 		 	if($("#queryForm").validationButton())
			{
 				//组成保存json串格式
 				 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
 				//调用ajax插入
 				defaultJson.doQueryJsonList(controllername + "?queryQainQiShouXu",data,DT1,null,false);
			}
		  });
		//新增
		var weihu = $("#xinzeng");
	 	weihu.click(function() 
	 			{
	 		  var index1 =	$("#DT1").getSelectedRowIndex();
	 		 if(index1<0) 
				{
		 			requireSelectedOneRow();
				}
		 		else
		 		{
		 		 diaoyong="1";
		 		 $("#resultXML").val($("#DT1").getSelectedRow());
	 		     $(window).manhuaDialog({"title":"前期手续综合信息>材料交接","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/qqsx/qqsxadd.jsp","modal":"4"});
	            }
	 			}); 
	 	//修改
	 	var xiugai = $("#xiugai");
	 	xiugai.click(function() 
	 			{
	 		      var index1 =	$("#DT1").getSelectedRowIndex();
			 		if(index1<0) 
					{
			 			requireSelectedOneRow();
					}
			 		else
			 		{
		 			 $("#resultXML").val($("#DT1").getSelectedRow());
			 		 $(window).manhuaDialog({"title":"前期手续综合信息>手续办理设置","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/qqsx/tcjhfk.jsp","modal":"2"});
			 		}
			}); 
	 	//立项可研进展情况
	 	var xiugai = $("#lxkyjzqk");
	 	xiugai.click(function() 
	 			{
	 		      var index1 =	$("#DT1").getSelectedRowIndex();
	 		      var value = $("#DT1").getSelectedRow();
	 	    	  value=convertJson.string2json1(value);
			 		if(index1<0) 
					{
			 			requireSelectedOneRow();
					}
			 		else
			 		{
			 		 	$("#resultXML").val($("#DT1").getSelectedRow());
			 		 	$(window).manhuaDialog({"title":"前期手续综合信息>立项可研进展情况","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/qqsx/sxwhjzck.jsp?&dfl=0","modal":"2"});
			 		}
			}); 
	 	//土地审批进展情况
	 	var xiugai = $("#tdspjzqk");
	 	xiugai.click(function() 
	 			{
		          var index1 =	$("#DT1").getSelectedRowIndex();
	 		      var value = $("#DT1").getSelectedRow();
	 	    	  value=convertJson.string2json1(value);
			 		if(index1<0) 
					{
			 			requireSelectedOneRow();
					}
			 		else
			 		{
			 			 $("#resultXML").val($("#DT1").getSelectedRow());
			 			 $(window).manhuaDialog({"title":"前期手续综合信息>土地审批进展情况","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/qqsx/sxwhjzck.jsp?dfl=2","modal":"2"});
			 		}
			}); 
	 	//规划审批进展情况
	 	var xiugai = $("#ghspjzqk");
	 	xiugai.click(function() 
	 			{
		 	      var index1 =	$("#DT1").getSelectedRowIndex();
	 		      var value = $("#DT1").getSelectedRow();
	 	    	  value=convertJson.string2json1(value);
			 		if(index1<0) 
					{
			 			requireSelectedOneRow();
					}
			 		else
			 		{
			 			 $("#resultXML").val($("#DT1").getSelectedRow());
			 		 	$(window).manhuaDialog({"title":"前期手续综合信息>规划审批进展情况","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/qqsx/sxwhjzck.jsp?&dfl=1","modal":"2"});
			 		}
			}); 
	 	//施工许可进展情况
	 	var xiugai = $("#sgxkjzqk");
	 	xiugai.click(function() 
	 			{
		 	      var index1 =	$("#DT1").getSelectedRowIndex();
	 		      var value = $("#DT1").getSelectedRow();
	 	    	  value=convertJson.string2json1(value);
			 		if(index1<0) 
					{
			 			requireSelectedOneRow();
					}
			 		else
			 		{
			 			 $("#resultXML").val($("#DT1").getSelectedRow());
			 			 $(window).manhuaDialog({"title":"前期手续综合信息>施工许可进展情况","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/qqsx/sxwhjzck.jsp?dfl=3","modal":"2"});
			 		}
			}); 
	 	//查询条件清空
	 	var clear=$("#query_clear");
	 	clear.click(function(){
	 		$("#queryForm").clearFormResult();
	 		initCommonQueyPage();
	 	});
	 	//统筹计划反馈
	 	$("#tcjhsf").click(function(){
	 		if($("#DT1").getSelectedRowIndex()==-1){
		    	requireSelectedOneRow();
    	        return;
	    	}else{
		    	$("#resultXML").val($("#DT1").getSelectedRow());
		    	var tempJson = $("#DT1").getSelectedRowJsonObj();
		    	openJhfkPage(tempJson.JHSJID,"1003","queryForm","DT1",encodeURI(controllername+"?queryQainQiShouXu"),"2");
	    	}
	 	});
		//按钮绑定事件（导出）
		$("#excel").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      //printTabList("DT1");
			      printTabList("DT1","qqsx.xls","XMBH,XMMC,BDBH,BDMC,LXKYBJSJ,TDSPBJSJ,GHSPBJSJ,SGXKBJSJ,JBDW,JER,JJSJ,JJCLMX","4,1");
			  }
		});
	 	init();
	});
	
	function init()
	{
	     //默认调用
	     initCommonQueyPage();
		 var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername + "?queryQainQiShouXu",data,DT1,null,true); 
	}
 function tianjiahang(subresultmsgobj)
 {
	 var subresultmsgobj1 = defaultJson.dealResultJson(subresultmsgobj);
	 $("#DT1").insertResult(JSON.stringify(subresultmsgobj1),DT1,1);
	 $("#DT1").cancleSelected();
	 $("#DT1").setSelect(0);
	 rowValue = $("#DT1").getSelectedRowText();
	 json=encodeURI(rowValue);
	 xSuccessMsg("操作成功","");
 }
 function xiugaiahang(subresultmsgobj)
 {
	 var index= $("#DT1").getSelectedRowIndex();
	 var subresultmsgobj1 = defaultJson.dealResultJson(subresultmsgobj);
	 $("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
	 $("#DT1").setSelect(index);
	 rowValue = $("#DT1").getSelectedRowText();
	 json=encodeURI(rowValue);
	 xSuccessMsg("操作成功","");
	//bindEvent('xiugaiahang');
 }
 function xiugaiahang1(subresultmsgobj)
 {
	 var index= $("#DT1").getSelectedRowIndex();
	 var subresultmsgobj1 = defaultJson.dealResultJson(subresultmsgobj);
	 $("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
	 $("#DT1").setSelect(index);
	 rowValue = $("#DT1").getSelectedRowText();
	 json=encodeURI(rowValue);
	//bindEvent('xiugaiahang1');
 }
	function tr_click(obj,tabListid)
	{
		json = $("#DT1").getSelectedRowText();
		json=encodeURI(json);
	}
	//详细信息
	 function rowView(index){
	     var obj = $("#DT1").getSelectedRowJsonByIndex(index);//获取行对象
	     var xmbh = eval("("+obj+")").XDKID;//取行对象<项目编号>
	     $(window).manhuaDialog(xmscUrl(xmbh));//调用公共方法,根据项目编号查询
	 }
	function bindEvent(a){
		$(".showXmxxkInfo").unbind();
		$(".showXmxxkInfo").click(function() {
			$("#DT1").cancleSelected();
	    	var index = $(event.target).closest("tr").index();
	    	$("#DT1").setSelect(index);
			$(window).manhuaDialog({"title":"前期手续项目信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/qqsx/xmxxk.jsp","modal":"1"});
		});
	}
	//-----------------------------------
	//-打开信息卡
	//-----------------------------------
	function openXmxxkInfo(){
	    var index = $(event.target).closest("tr").index();
		$("#DT1").cancleSelected();
    	$("#DT1").setSelect(index);
		if($("#DT1").getSelectedRowIndex()==-1){
			requireSelectedOneRow();
			return;
		}else{
			var n = $(this).attr("jhsjid");
			$("#DT1").val($("#DT1").getSelectedRow());
			$(window).manhuaDialog({"title":"前期手续项目信息卡","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/qqsx/xmxxk.jsp","modal":"1"});
		}
	}
	//-----------------------------------
	//-生成信息卡图标
	//-----------------------------------
	function doRandering(obj){
		var showHtml = "";
		showHtml = "<a href='javascript:void(0)' title='前期手续信息卡' onclick='openXmxxkInfo();'><i class='icon-file showXmxxkInfo' jhsjid='"+obj.GC_JH_SJ_ID+"'></i></a>";
		return showHtml;
	}
	//反馈颜色判断	
	 function LXFKZT(obj)
		{
		 if(""!=obj.LXKYBJSJ)
		 {
		   if(obj.LXFKZT == "1")
			{
			 return '&nbsp;<i title="已反馈"  class="icon-green"></i>&nbsp;';
			}
		    else
			 {
			 return '&nbsp;<i title="未反馈" class="icon-red"></i>&nbsp;';
			 }
		  }
		 else{
			 return '&nbsp;';
		 }
		}
	//反馈颜色判断	
	 function TDFKZT(obj)
		{
		if(""!=obj.TDSPBJSJ)
			{
		 if(obj.TDFKZT == "1")
			{
			 return '&nbsp;<i title="已反馈"  class="icon-green"></i>&nbsp;';
			}
		 else
			 {
			 return '&nbsp;<i  title="未反馈" class="icon-red"></i>&nbsp;';
			 }
			}
		 else{
			 return '&nbsp;';
		 }
		}
	//反馈颜色判断	
	 function GHFKZT(obj)
		{
		 if(""!=obj.GHSPBJSJ)
			 {
			  if(obj.GHFKZT == "1")
				{
				 return '&nbsp;<i title="已反馈" class="icon-green"></i>&nbsp;';
				}
			  else
				 {
				 return '&nbsp;<i title="未反馈" class="icon-red"></i>&nbsp;';
				 }
			 }
		 else{
			 return '&nbsp;';
		 }
		}
	//反馈颜色判断	
	 function SGFKZT(obj)
		{
		 if(""!=obj.SGXKBJSJ)
		  {
			 if(obj.SGFKZT == "1")
				{
				 return '&nbsp;<i  title="已反馈" class="icon-green"></i>&nbsp;';
				}
			 else
				 {
				 return '&nbsp;<i title="未反馈" class="icon-red"></i>&nbsp;';
				 }
		 }
		 else{
			 return '&nbsp;';
		 }
		}
	function JHWCTX(obj)
	{
		if(obj.COLOR>0)
			{
			 return '&nbsp;<i title="超期" class="icon-red"></i>&nbsp;';
			}
		else
		{
			 return '&nbsp;<i title="正常" class="icon-green"></i>&nbsp;';
		}
	}	
	//标段判断
  function doBdmc(obj){
	  var bd_name=obj.BDMC;
	  if(bd_name==null||bd_name==""){
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
	  }
  }
	//获取父页面值
	function getValue(){
		var rowValue=$("#DT1").getSelectedRow();
		return rowValue;
	}
	  //标段编号
    function doBdBH(obj){
	  var bd_name=obj.BDBH;
	  if(bd_name==null||bd_name==""){
		  return '<div style="text-align:center">—</div>';
	  }else{
		  return bd_name;			  
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
      <h4 class="title">前期手续综合信息
	      <span class="pull-right">
	      <app:oPerm url="/jsp/business/qqsx/qqsx/sxwhjzck.jsp?lxky">
	       <button id="lxkyjzqk" class="btn"  type="button">立项可研进展情况</button> 
	       </app:oPerm>
	       <app:oPerm url="/jsp/business/qqsx/qqsx/sxwhjzck.jsp?tdsp">
	        <button id="tdspjzqk" class="btn"  type="button">土地审批进展情况</button> 
	        </app:oPerm>
	        <app:oPerm url="/jsp/business/qqsx/qqsx/sxwhjzck.jsp?ghsp">
	         <button id="ghspjzqk" class="btn"  type="button">规划审批进展情况</button> 
	         </app:oPerm>
	         <app:oPerm url="/jsp/business/qqsx/sgxk/sxwhjz.jsp?sgxk">
	          <button id="sgxkjzqk" class="btn"  type="button">施工许可进展情况</button>
	          </app:oPerm>
	          <app:oPerm url="/jsp/business/qqsx/qqsx/qqsxadd.jsp">  
	        <button id="xinzeng" class="btn"  type="button">材料交接</button> 
	        </app:oPerm>
	         <app:oPerm url="/jsp/business/jhfk/qqsx/jhfkMain.jsp">
            <button id="xiugai" class="btn"  type="button">手续办理设置</button>
            </app:oPerm>
             <app:oPerm url="/jsp/business/qqsx/qqsx/tcjhfk.jsp">
            <button id="tcjhsf" class="btn"  type="button">统筹计划反馈</button>
              </app:oPerm>
              <button id="excel" class="btn" type="button">导出</button>
	      </span>
      </h4>
      <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD><INPUT type="text" class="span12" keep="true" kind="text"  fieldname="rownum"  value="1000" operation="<=" ></TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
		     		<!--公共的查询过滤条件 -->
			<jsp:include page="/jsp/business/common/commonQuery.jsp" flush="true">
			   <jsp:param name="prefix" value="B" /></jsp:include>
	         <td  class="text-left bottom-border text-right">
           	 <button id="chaxun" class="btn btn-link"  type="button" ><i class="icon-search"></i>查询</button>
           	 <button id="query_clear" class="btn btn-link" type="button" ><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
      </table>
      </form>
    <div style="height:5px;"> </div>
    <div class="overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="前期手续综合信息">
                <thead>
                    <tr>
	                    <th  name="XH" id="_XH" rowspan="3" colindex=1 >&nbsp;#&nbsp;</th>
	                    <th fieldname="XMBH" rowspan="3"  noprint="true" colindex=2 tdalign="center" CustomFunction="doRandering">&nbsp;&nbsp;</th>
	                    <th fieldname="XMBH" rowspan="3" colindex=3  hasLink="true" linkFunction="rowView" rowMerge="true">&nbsp;项目编号&nbsp;</th>
						<th fieldname="XMMC"   rowspan="3" rowMerge="true"  colindex=4 maxlength="15">&nbsp;项目名称&nbsp;</th>
						<th fieldname="BDBH"  rowspan="3"  maxlength="15" colindex=5 Customfunction="doBdBH" >&nbsp;标段编号&nbsp;</th>
						<th fieldname="BDMC"   rowspan="3" colindex=6 maxlength="15" Customfunction="doBdmc" >&nbsp;标段名称&nbsp;</th>
						<th fieldname="XMBDDZ"   rowspan="3" colindex=7 maxlength="15" >&nbsp;项目地址&nbsp;</th>
						<th fieldname="COLOR"  CustomFunction="JHWCTX" tdalign="center" rowspan="3" colindex=8 noprint="true" >&nbsp;计划完成情况&nbsp;</th>
						<th  colspan="12" >&nbsp;手续进展情况&nbsp;</th>
						<th fieldname="JBDW"  rowspan="3" colindex=21 maxlength="15" >&nbsp;交办单位&nbsp;</th>
						<th fieldname="JER"  rowspan="3" colindex=22 tdalign="center" maxlength="15" >&nbsp;交接人&nbsp;</th>
						<th fieldname="JJSJ"  rowspan="3" colindex=23 tdalign="center">&nbsp;交接时间&nbsp;</th>
						<th fieldname="JJCLMX"  rowspan="3" colindex=24 maxlength="15" >&nbsp;交接材料说明&nbsp;</th>
					</tr>
					<tr>
						<th  colspan="3" >&nbsp;立项可研&nbsp;</th>
						<th  colspan="3" >&nbsp;土地审批&nbsp;</th>
						<th  colspan="3" >&nbsp;规划审批&nbsp;</th>
						<th  colspan="3" >&nbsp;施工许可&nbsp;</th>
					</tr>
					<tr>
					
						<th fieldname="LXKYFJ" colindex=9 tdalign="center" noprint="true">&nbsp;证照&nbsp;</th>
						<th fieldname="LXKYBJSJ" colindex=10 tdalign="center">&nbsp;时间&nbsp;</th>
						<th fieldname="LXFKZT" CustomFunction="LXFKZT" colindex=11 tdalign="center" noprint="true">&nbsp;反馈情况&nbsp;</th>
						<th fieldname="TTSPFJ" colindex=12 tdalign="center" noprint="true" >&nbsp;证照&nbsp;</th>
						<th fieldname="TDSPBJSJ" colindex=13 tdalign="center">&nbsp;时间&nbsp;</th>
						<th fieldname="TDFKZT"  CustomFunction="TDFKZT" colindex=14 tdalign="center" noprint="true">&nbsp;反馈情况&nbsp;</th>
						<th fieldname="GHSPFJ" colindex=15 tdalign="center" noprint="true">&nbsp;证照&nbsp;</th>
						<th fieldname="GHSPBJSJ" colindex=16 tdalign="center">&nbsp;时间&nbsp;</th>
						<th fieldname="GHFKZT" CustomFunction="GHFKZT" colindex=17 tdalign="center" noprint="true">&nbsp;反馈情况&nbsp;</th>
						<th fieldname="SGXKFJ" colindex=18 tdalign="center" noprint="true">&nbsp;证照&nbsp;</th>
						<th fieldname="SGXKBJSJ" colindex=19 tdalign="center">&nbsp;时间&nbsp;</th>
						<th fieldname="SGFKZT" CustomFunction="SGFKZT" colindex=20 tdalign="center" noprint="true" >&nbsp;反馈情况&nbsp;</th>
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
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
       <input type="hidden" name="txtFilter" order="asc" fieldname="B.xmbh,B.xmbs,B.pxh"/>
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
          <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
</body>
</html>