<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title></title>
<script type="text/javascript" charset="utf-8">
  var json2,json;
  var rowindex,rowValue,text,aa;
  var jhsjid,lbjid,bdmc,zxgs;
  var controllername= "${pageContext.request.contextPath }/zjb/lbj/lbjController.do";
	//计算本页表格分页数
	function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(2)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
	$(function() {
		setPageHeight();
		//页面初始化
		ready();
		//查询方法
		var btn = $("#query");
		btn.click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryLbj",data,DT1,null,false);
			//
				});
		//清空查询条件
		var clear=$("#clear");
		clear.click(function(){
			$("#queryForm").clearFormResult();
			initCommonQueyPage();
		});
		//导出Excel
		var excel=$("#excel");
		excel.click(function(){
			 if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      printTabList("DT1");
			  }
		});
	});
	//初始化方法
    function ready() {
    	initCommonQueyPage();
   	 	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryLbj",data,DT1,null,true);
   };
	//详细信息
	function rowView(index){
		var obj = $("#DT1").getSelectedRowJsonByIndex(index);
		var id = convertJson.string2json1(obj).XMID;
		$(window).manhuaDialog(xmscUrl(id));
	}
	  //标段名称
	  function doBdmc(obj){
		  var bd_name=obj.BDMC;
		  if(bd_name==null||bd_name==""){
			  return '<div style="text-align:center">—</div>';
		  }else{
			  return bd_name;			  
		  }
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
	  //提报价
	  function doTBJ(obj){
	  	var fk=" &nbsp;";
		if('1'==obj.ISTBJ){
			if(obj.TBJ_SJ==""||obj.TBJ_SJ==null){
				if(""!=obj.SBCSZRQ){
					fk='<i title="未反馈" class=\"icon-yellow\"></i>';
				}
			}else{
				fk='<i title="已反馈" class=\"icon-green\"></i>';
			} 
 		}
		return fk;
	  }
	  //财审
	  function doCS(obj){
	  	var fk=" &nbsp;";
		if('1'==obj.ISCS){
			if(obj.CS_SJ==""||obj.CS_SJ==null){
				if(""!=obj.CSSDZRQ){
					fk='<i title="未反馈" class=\"icon-yellow\"></i>';
				}
			}else{
				fk='<i title="已反馈" class=\"icon-green\"></i>';
			} 
 		}
		return fk;
	  }
  //复选框
function cks(obj){
	  var hangshu=obj.IDNUM-1;
		 return "<input type='checkbox' colindex='"+hangshu+"' name='subBox' id='fuxuan' value='"+obj.GC_ZJB_LBJB_ID+"'>"; 
	}
//复选框全选取消
$(function() {
    $("#checkAll").click(function() {
         $('input[name="subBox"]').prop("checked",this.checked); 
     });
     var sub = $("input[name='subBox']");
     sub.click(function(){
         $("#checkAll").prop("checked",sub.length == $("input[name='subBox']:checked").length ? true : false);
     });
 });
 //修改状态
 function updateZT(zt){
	var num=0;
	$("[name=subBox]:checkbox:checked").each(function(){ 
		num++;
	});
	if(num==0)
	{
		requireSelectedOneRow();
		return;
	}
	xConfirm("提示信息","是否修改选中信息！");
	$('#ConfirmYesButton').unbind();
	$('#ConfirmYesButton').one("click",function(){ 
		var actionFlag = true;
		$("[name=subBox]:checkbox:checked").each(function(){ 
			var hang =$(this).closest("tr").find("th").text()-1;
			$("#DT1").setSelect(hang);
			//生成json串
			var data=$("#DT1").getSelectedRow();
			var obj=convertJson.string2json1(data);
			obj.ZJBZZT=zt;
			data=JSON.stringify(obj);
			//组成保存json串格式
			var data1 = defaultJson.packSaveJson(data);
			//调用ajax插入
			actionFlag = defaultJson.doUpdateJson(controllername + "?updateZt",data1, DT1);
			if(actionFlag==false){
				return false;
			}
		});
		if(actionFlag==false){
			xAlert("信息提示","更新失败！");
		}else{
			xAlert("信息提示","更新成功！");
		}
	 }); 
 }
</script>
</head>
<body>
<app:dialogs/>
	<div class="container-fluid">
		<p></p>
	<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">造价编制管理
      		<app:oPerm url="jsp/business/xmcbk/add.jsp?flag=true">
			</app:oPerm>
		
      		<span class="pull-right">
      			<button class="btn" onclick="updateZT(1)" >暂缓编制</button>
				<button class="btn" onclick="updateZT(2)" >具备条件</button>
				<button class="btn" onclick="updateZT(3)" >不需要编制</button>
        		 <button id="excel" class="btn"  type="button">导出</button>
       		</span></h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" keep="true"/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
        
        <!--公共的查询过滤条件 -->
         <jsp:include page="/jsp/business/common/commonQuery.jsp" flush="true">
         	<jsp:param name="prefix" value="jhsj"/> 
         </jsp:include>
        
         <td class=" bottom-border text-right">
           <button id="query" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           <button id="clear" class="btn btn-link"  type="button"><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
      </table>
      </form>
	  <div style="height:5px;"> </div>
        <div class= "overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1"  type="single" withoutAlertFlag="true" editable="0" printFileName="拦标价管理">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH"  tdalign="">&nbsp;#&nbsp;</th>
                   			<th  fieldname="XMBH"   tdalign="center" CustomFunction="cks">
	                     	<input type="checkbox" name="fuxuans"  id="checkAll" > 
	                     	</th>
                    		<th fieldname="XMBH"    hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC"  rowMerge="true" maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDBH"  maxlength="15" Customfunction="doBdBH" >&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC"   Customfunction="doBdmc" maxlength="15">&nbsp;标段名称&nbsp;</th>
							<th fieldname="XMBDDZ"   maxlength="15">&nbsp;项目地址&nbsp;</th>
							<th fieldname="ZJBZZT"   maxlength="15">&nbsp;具备拦标价&nbsp;</th>
							<th fieldname="SBCSZRQ" >&nbsp;&nbsp;上报值日期 &nbsp;&nbsp;</th>
							<th fieldname="ISTBJ" Customfunction="doTBJ" tdalign="center">&nbsp;&nbsp;是否反馈&nbsp;&nbsp;</th>
							<th fieldname="CSSDZRQ" >&nbsp;&nbsp;财审审定值日期 &nbsp;&nbsp;</th>
							<th fieldname="ISCS" Customfunction="doCS" tdalign="center">&nbsp;&nbsp;是否反馈&nbsp;&nbsp;</th>
							<th fieldname="CSBGBH" >&nbsp;财审报告编号&nbsp;</th>
							<th fieldname="LRSJ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;提需求时间<p></p>(招造价公司)</span></th>
							<th fieldname="KBRQ"  tdalign="center" class="wrap"><span style="width:120px;display:block;">招标时间<p></p>(内部咨询公司)</span></th>
							<th fieldname="ZXGS"  tdalign="" maxlength="15">&nbsp;咨询公司&nbsp;</th>
							<th fieldname="TZJJSJ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;与总工办交接图纸日期</span></th>
							<th fieldname="ZXGSJ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;发给咨询公司图纸日期</span></th>
							<th fieldname="YWRQ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;提出疑问日期</span></th>
							<th fieldname="HFRQ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;答疑日期</span></th>
							<th fieldname="ZXGSRQ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;咨询公司<p></p>交造价组日期</span></th>
							<th fieldname="JGBCSRQ"  tdalign="center" class="wrap"><span style="width:80px;display:block;">&nbsp;建管报财审日期</span></th>
							<th fieldname="SBCSZ"  tdalign="right" maxlength="15" >&nbsp;上报财审值(元)&nbsp;</th>
							<th fieldname="CZSWRQ" tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;财政审完日期</span></th>
							<th fieldname="CSSDZ" tdalign="right" maxlength="15"  >&nbsp;财审审定值(元)&nbsp;</th>
							<th fieldname="SJZ"  tdalign="right" maxlength="15" >&nbsp;审减值(元)&nbsp;</th>
							<th fieldname="SJBFB"  tdalign="right" >&nbsp;审减百分比(%)&nbsp;</th>
							<th fieldname="ZDJ"  tdalign="right" maxlength="15" class="wrap" ><span style="width:105px;display:block;">&nbsp;暂定金</span></th>
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
			<input type="hidden" name="queryResult" id = "queryResult" />
			<input type="hidden" name="txtFilter"  order="asc" fieldname = "jhsj.xmbh,jhsj.xmbs,jhsj.pxh" id = "txtFilter">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>