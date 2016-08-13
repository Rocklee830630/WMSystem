<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/shenJiGuanliController.do";
  
	$(function() {
		getNd();
		query();
		//保存按钮
		var btn = $("#example1");
		btn.click(function()
		  {
	 		 	 if($("#demoForm").validationButton())
				{
	 		 		//生成json串
	 				var data = Form2Json.formToJSON(demoForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
 			 		defaultJson.doUpdateJson(controllername + "?update_js", data1,DT1);
				}
 		   });
		var query1 = $("#query");
		query1.click(function(){
			query();
           });
	 	//清空查询条件
	    var clear = $("#clear");
	    clear.click(function() 
	      {
	        $("#queryForm").clearFormResult();
	        getNd();
	      });
		});
	//默认年度
	function getNd(){
			setDefaultNd("SJND");
	}
	  function query(){
		  var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
		  defaultJson.doQueryJsonList(controllername+"?querysjList",data,DT1,null,true);
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
	//标段名称
	function doBdmc(obj){
	 var bd_name=obj.BDMC;
	 if(bd_name==null||bd_name==""){
	  return '<div style="text-align:center">—</div>';
	 }else{
	  return bd_name;			  
	 }
	}
	//行选
	function tr_click(obj){
		var hang=$("#DT1").getSelectedRowIndex();
		if(hang=="-1"){
			$("#demoForm").clearFormResult();
		}else{
		$("#demoForm").setFormValues(obj);
		$("#weituo :radio").attr({disabled : 'true'});
		}
	}
	//自动计算审减值 审减百分比
	function jssjz()
	{
		
		var cssdje=$("#CSSDJE").val();
		var sjsdje=$("#SJSDJE").val();
		$("#SJZ").val(cssdje-sjsdje);
		/* var sjz=$("#SJZ").val();
		var chazhi=Math.round((Math.abs(sjz/sbcsz* 10000))) / 100.00;
		$("#SJBFB").val(chazhi); */
	}
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
	<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">审定信息维护</h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" keep="true"/>
			</TD>
        </TR>
        <tr>
		<th style="width:7%;" class="right-border bottom-border text-right">审计年度</th>
		<td style="width:7%;" width="15%" class="right-border bottom-border">
			<select class="span12 year" id="SJND" name = "SJND" fieldname="SJND"  defaultMemo="全部" operation="="  kind="dic" src="T#GC_ZJB_JSB:SJND :SJND:SFYX='1' AND SJND IS NOT NULL group by SJND ">
			</select> 
		</td>
        <!--公共的查询过滤条件 -->
         <td class=" bottom-border text-right">
           <button id="query" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           <button id="clear" class="btn btn-link"  type="button"><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
      </table>
      </form>
	  <div style="height:5px;"> </div>
        <div class= "overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" editable="0" pageNum="5">
                <thead>
                <tr>
				    <th  name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
				    <th fieldname="XMBH" rowspan="2" rowMerge="true" colindex=2  hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
				 	<th fieldname="XMMC" rowspan="2" rowMerge="true"  colindex=3 maxlength="15" >&nbsp;项目名称&nbsp;</th>
				 	<th fieldname="BDBH" rowspan="2" colindex=4 maxlength="15" Customfunction="doBdBH" >&nbsp;标段编号&nbsp;</th>
				 	<th fieldname="BDMC" rowspan="2" colindex=5 maxlength="15" Customfunction="doBdmc" >&nbsp;标段名称&nbsp;</th>
				 	<th fieldname="ND" rowspan="2" colindex=6 tdalign="center">&nbsp;施工年度&nbsp;</th>
				 	<th fieldname="SGDW" rowspan="2" maxlength="15" colindex=7>&nbsp;施工单位&nbsp;</th>
				 	<th fieldname="JSZT" rowspan="2"  colindex=8 >&nbsp;结算状态&nbsp;</th>
					<!-- <th fieldname="JLDW" rowspan="2" maxlength="15" colindex=6>&nbsp;监理单位&nbsp;</th> -->
					<th  colspan="3"  >&nbsp;合同&nbsp;</th>
					<th  colspan="3"  >&nbsp;上报值&nbsp;</th>
					<th  colspan="3"  >&nbsp;业主审定&nbsp;</th>
					<th  colspan="3"  >&nbsp;财审审定&nbsp;</th>
					<th  colspan="3" >&nbsp;审计审定&nbsp;</th>
					<th  colspan="4" >&nbsp;差额值&nbsp;</th>
				    <th fieldname="JSQK" rowspan="2" maxlength="15" colindex=28 >&nbsp;结算情况&nbsp;</th>
				</tr>
				<tr> 
					<th fieldname="HTBH"  maxlength="15" colindex=9>&nbsp;编号&nbsp;</th>
				 	<th fieldname="HTMC"  maxlength="15" colindex=10>&nbsp;名称&nbsp;</th>
					<th fieldname="HTJE"  maxlength="15" colindex=11 tdalign="right"   >金额（元）<br>A</th>
					<th fieldname="TBR" colindex=12 maxlength="15">&nbsp;提报人&nbsp;</th>
					<th fieldname="TBRQ" colindex=13 tdalign="center">&nbsp;日期&nbsp;</th>
					<th fieldname="TBJE" colindex=14 tdalign="right"  maxlength="15">金额(元)<br>B</th>
					<th fieldname="WTZXGS" colindex=15  maxlength="15">&nbsp;委托咨询公司&nbsp;</th>
					<th fieldname="YZSDRQ" colindex=16 tdalign="center">&nbsp;日期&nbsp;</th>
					<th fieldname="YZSDJE" colindex=17 tdalign="right"  maxlength="15" >金额(元)<br>C</th>
				    <th fieldname="CSSDRQ" colindex=18 tdalign="center">&nbsp;日期&nbsp;</th>
					<th fieldname="CSSDJE" colindex=19 tdalign="right"  maxlength="15"  >金额(元)<br>D</th>
					<th fieldname="CSBGBH" colindex=20  maxlength="15">&nbsp;财审报告编号&nbsp;</th>
					<th fieldname="SJSDRQ" colindex=21 tdalign="center">&nbsp;日期&nbsp;</th>
					<th fieldname="SJSDJE" colindex=22 tdalign="right"  maxlength="15" >金额(元)<br>E</th>
					<th fieldname="SJBGBH" colindex=23  maxlength="15">&nbsp;审计报告编号&nbsp;</th> 
				    <th fieldname="TBYZ"  tdalign="center" noprint="true" colindex=24 >上报值-审定值<br>B-C</th>
				    <th fieldname="YZCS"  tdalign="center" noprint="true" maxlength="15" colindex=25 >审定值-财审值<br>C-D</th>
					<th fieldname="CSSJ"  tdalign="center" noprint="true" maxlength="15" colindex=26 >财审值-审计值<br>D-E</th>
					<th fieldname="TBSJ"  tdalign="center" noprint="true" maxlength="15" colindex=27 >上报值-审计值<br>B-E</th>
				</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>
	</div>
	<div style="height:5px;"></div>
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">审计审定 
  		<span class="pull-right">
		 	<button id="example1" class="btn"  type="button">保存</button>
		</span> 
  </h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
     	<input class="span12" id="GC_ZJB_JSB_ID" type="hidden" placeholder=""  check-type=""  fieldname="GC_ZJB_JSB_ID" name = "GC_ZJB_JSB_ID">
      		
          		<table class="B-table" width="100%">
       			<tr>
          			<th width="8%" class="right-border bottom-border text-right">审计年度</th>
        			<td  width="10%" class="right-border bottom-border ">
        			    <select class="span12 year" id="SJND" name = "SJND"  disabled fieldname="SJND"  placeholder="必填"  check-type="required"     kind="dic" src="T#GC_ZJB_JSB:SJND :SJND:SFYX='1' AND SJND IS NOT NULL group by SJND ">
	              		</select> 
       				</td>
       				<th width="8%" class="right-border bottom-border text-right">咨询单位</th>
          			<td width="24%" class="right-border bottom-border ">
	          			<select  class="span12" id="SJZXGS" name = "SJZXGS" maxlength="100" fieldname="SJZXGS" kind="dic"  src="T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX = '4'" >
		                </select>
	                </td>
          			<th width="8%" class="right-border bottom-border text-right">金额</th>
          			<td width="17%" class="right-border bottom-border "><input class="span12" min="0" type="number" onkeyup="jssjz()" style="width:65%;text-align:right" placeholder=""    maxlength="17" id="SJSDJE" name = "SJSDJE" fieldname="SJSDJE">&nbsp;&nbsp;<b>(元)</td>
          			<th  width="8%" class="right-border bottom-border text-right">审减值</th>
					<td width="17%" class="bottom-border">
					<input class="span12" style="width:65%;text-align:right" type="number" disabled placeholder="" id="SJZ" name="SJZ" fieldname="SJZ" check-type="maxlength" maxlength="17" >&nbsp;&nbsp;<b>(元)</b></td>
          		
          		</tr>
          		<tr>
          		</tr>
          	<tr>
			<th width="8%" class="right-border bottom-border text-right">附件上传</th>
			<td colspan="8" class="bottom-border right-border">
				<div>
				<span class="btn btn-fileUpload" onclick="doSelectFile(this);" id="shangchuanID" fjlb="2025">
					<i class="icon-plus"></i>
						<span>添加文件...</span></span>
							<table role="presentation" class="table table-striped">
								<tbody fjlb="2025" id="shangchuanID1" class="files showFileTab"
									data-toggle="modal-gallery" data-target="#modal-gallery">
								</tbody>
							 </table>
				</div>
			  </td>
			</tr>
       		</table>
       		<h4 class="title">合同信息</h4>
      		<table class="B-table" width="100%"  >
      		                
	      			<TR  style="display:none;">
				        <TD class="right-border bottom-border">
				            <input class="span12"  id="XDKID"  type="text"  name = "XDKID" fieldname="XDKID">
							<input class="span12"  id="BDID"  type="text"  name = "BDID" fieldname="BDID">
							<input class="span12"  id="HTID"  type="text"  name = "HTID" fieldname="HTID">
							<input class="span12"  id="JHSJID"  type="text"  name = "JHSJID" fieldname="JHSJID">
				        </TD>
	                </TR>
      			<tr>
      				<th  width="8%" class="right-border bottom-border text-right disabledTh">合同名称</th>
          			<td   width="42%" class="right-border bottom-border "  ><input class="span12" style="width:90%"  disabled id="HTMC" type="text" placeholder="必填" fieldname="HTMC" check-type="required" name = "HTMC">
          			 </td>
          			<th  width="8%"  class="right-border bottom-border text-right disabledTh">合同编号</th>
          			<td width="17%" class="right-border bottom-border "><input width="2" class="span12"  id="HTBM"  type="text" disabled     name = "HTBM" fieldname="HTBM"></td>
         			<th width="8%" class="right-border bottom-border text-right disabledTh">合同金额</th>
          			<td width="17%" class="right-border bottom-border " ><input class="span12" min="0" id="HTQDJ" style="width:65%;text-align:right"   disabled type="number"   fieldname="HTQDJ" name = "HTQDJ">&nbsp;&nbsp;<b>(元)</td>
          		</tr>
        		<tr>
          			<th class="right-border bottom-border text-right disabledTh">项目名称</th>
          			<td class="right-border bottom-border " ><input class="span12 xmmc" disabled check-type="required" placeholder="必填" type="text"   id="XMMC"  name = "XMMC" fieldname="XMMC"></td>
         			<th class="right-border bottom-border text-right disabledTh">标段名称</th>
          			<td class="right-border bottom-border "><input class="span12" disabled type="text"   id="BDMC" name = "BDMC" fieldname="BDMC"></td>
          			<th class="right-border bottom-border text-right disabledTh">项目年份</th>
          			<td class="right-border bottom-border "><input class="span12" disabled id="ND" type="text"  name = "ND" fieldname="ND"></td>
        		</tr>
        		<tr>
          			<th class="right-border bottom-border text-right disabledTh">施工单位</th>
            		<td colspan="1" class="right-border bottom-border "><input class="span12" disabled  type="text" placeholder="" check-type="" id="SGDW" name = "SGDW" fieldname="SGDW"></td>
        		    <th class="right-border bottom-border text-right disabledTh">监理单位</th>
            		<td colspan="3" class="right-border bottom-border " ><input class="span12" disabled type="text" placeholder="" check-type="" id="JLDW" name = "JLDW" fieldname="JLDW"></td>
        		</tr>
        		</table>
        		<h4 class="title">上报值和业主审定</h4>
        		 <table class="B-table" width="100%">
       			<tr>
          			<th width="8%" class="right-border bottom-border text-right" >提报人</th>
          			<td width="17%" class="right-border bottom-border" ><input disabled class="span12"  type="text" maxlength="50"  id="TBR" name = "TBR" fieldname="TBR"></td>
          			<th width="8%" class="right-border bottom-border text-right">联系电话</th>
          			<td width="21%" class="right-border bottom-border" ><input disabled class="span12"  type="text" placeholder="" maxlength="15" check-type="number" id="TBRDH" name = "TBRDH" fieldname="TBRDH"></td>
          		    <th width="8%" class="right-border bottom-border text-right" >提报日期</th>
          			<td width="15%" class="right-border bottom-border"  ><input disabled class="span12 date"  type="date"  id="TBRQ"  name = "TBRQ" fieldname="TBRQ"></td>
         			<th width="8%" class="right-border bottom-border text-right" >提报金额</th>
          			<td width="15%" class="right-border bottom-border" ><input disabled min="0" class="span12"  type="number"  style="width:65%;text-align:right"  id="TBJE" name = "TBJE" fieldname="TBJE">&nbsp;&nbsp;<b>(元)</td>
          		</tr>
       			<tr id="weituo">
       			    <th class="right-border bottom-border text-right">是否需要委托</th>
         			<td class=" bottom-border text-center" ><input disabled class="span12" id="SFWT" type="radio"    placeholder="" kind="dic" src="E#1=是:0=否" name = "SFWT" fieldname="SFWT" onclick="sfwt()"></td>
          			<th class="right-border bottom-border text-right">咨询单位</th>
          			<td class="right-border bottom-border ">
	          			<select disabled class="span12" id="WTZXGS" name = "WTZXGS" maxlength="100" fieldname="WTZXGS" kind="dic"  src="T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX = '4'" >
		                </select>
          			<th class="right-border bottom-border text-right">审定日期</th>
          			<td class="right-border bottom-border "><input disabled class="span12 date"  type="date"   name = "YZSDRQ" id="YZSDRQ" fieldname="YZSDRQ"></td>
          			<th class="right-border bottom-border text-right">审定金额</th>
          			<td class="right-border bottom-border " ><input disabled class="span12" min="0"  type="number" style="width:65%;text-align:right"    id="YZSDJE" name = "YZSDJE" fieldname="YZSDJE">&nbsp;&nbsp;<b>(元)</td>
        		</tr>
        		<tr>
        			<th class="right-border bottom-border text-right">提报财审日期</th>
         			<td class=" bottom-border text-left" ><input disabled class="span12 date"  type="date"    name = "TBCSRQ" id="TBCSRQ" fieldname="TBCSRQ"></td>
         			<td colspan="6"></td>
        		</tr>
        		</table>
        		<h4 class="title">财审审定</h4>
          		<table class="B-table" width="100%">
       			<tr>
       			    <th width="8%" class="right-border bottom-border text-right">财审报告编号</th>
          			<td width="17%" class="right-border bottom-border "><input disabled class="span12"  type="text" placeholder=""  maxlength="36"  name = "CSBGBH" id="CSBGBH" fieldname="CSBGBH"></td>
          			<th width="8%" class="right-border bottom-border text-right">日期</th>
          			<td width="17%" class="right-border bottom-border "><input disabled class="span12 date"  type="date" placeholder=""  id="CSSDRQ"  name = "CSSDRQ" fieldname="CSSDRQ"></td>
          			<th width="8%" class="right-border bottom-border text-right">金额</th>
          			<td width="17%" class="right-border bottom-border "><input disabled class="span12 " min="0" type="number" style="width:65%;text-align:right" placeholder=""  id="CSSDJE"  name = "CSSDJE" fieldname="CSSDJE">&nbsp;&nbsp;<b>(元)</td>
          			<td class="right-border bottom-border" colspan="2" >
        		</tr>
        		</table>
        		
      	  </form>
     	</div>
 	</div>
</div>
   <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
 <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
		   <input type="hidden" name="ywid" id = "ywid" value="">
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname="g.xmbh,g.xmbs,g.pxh"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>