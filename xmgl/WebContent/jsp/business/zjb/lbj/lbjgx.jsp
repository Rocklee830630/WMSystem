<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>

<app:base />
<title>拦标价信息</title>
<%
	String xx=request.getParameter("xx");
	String i=request.getParameter("i");
%>
<script type="text/javascript" charset="utf-8">
	var controllername = "${pageContext.request.contextPath }/zjb/lbj/lbjController.do";
	var tbl = null;
	var i=Number(<%=i%>);
	function xzxm() {
		$(window).manhuaDialog({"title":"拦标价>项目信息查找","type":"text","content":"${pageContext.request.contextPath}/jsp/business/gcb/kfglgl/xmcx.jsp","modal":"2"});
	}
	$(function() {
		var clean=$("#clean");
		var add=$("#add");
		add.click(function(){
			$("#ID").val('');
		});
		clean.click(function(){
			$("#LbjForm").clearFormResult(); 
		});
		var btn = $("#insert");
		btn.click(function() {
			var val=$("#XMMC").val();
			if(val==null||val==""){
				xAlert("插入失败","页面必填项为空！");
				return;				
			}else{
			var id=$("#ID").val();
			if(id==null||id==""){
				//生成json串
				var data = Form2Json.formToJSON(LbjForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				defaultJson.doInsertJson(controllername + "?insertLbj",data1, null);
				var obj = $("#frmPost").find("#resultXML").val();
				var resultmsgobj = convertJson.string2json1(obj);
				var subresultmsgobj = resultmsgobj.response.data[0];
				$("#ID").val($(subresultmsgobj).attr("GC_ZJB_LBJB_ID"));
				
 				var parentmain=$(window).manhuaDialog.getParentObj();			
 				parentmain.add(obj);
			}else{
				//生成json串
				var data = Form2Json.formToJSON(LbjForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				var success = defaultJson.doUpdateJson(controllername + "?updateLbj",data1, null);
				if(success ==true){
					var obj = $("#resultXML").val();	
					var resultmsgobj = convertJson.string2json1(obj);
					var subresultmsgobj = resultmsgobj.response.data[0];
	 				var parentmain=$(window).manhuaDialog.getParentObj();
	 				parentmain.update(obj);
	 				$(window).manhuaDialog.close();
			}
			}
			}
		});
		  var a=<%=xx%>;
		  var odd=convertJson.string2json1(a);
		  $("#LbjForm").setFormValues(odd);
	});
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid" hidden="ture">
			<div class="B-from-table-box">
				<table cellpadding="0" cellspacing="0" border="0" class="" hidden="ture" id="DT1" width="10%">
					<thead>
						<tr>
							
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="B-small-from-table-autoConcise">
			<h4 class="title">项目信息
			<span class="pull-right">
      		<%if(i.equals("1")) {%>
				<button id="insert" class="btn" type="button">保 存</button>
				<button id="clean" class="btn" type="button">清 空</button>
				<% }else{%>
				<button id="insert" class="btn" type="button">保 存</button>
				<button id="add" class="btn" type="button">复制新增</button>
				<button id="clean" class="btn" type="button">清 空</button>
				<%} %>
      		</span>
			</h4>
				<form method="post" id="LbjForm">
					<table class="B-table" width="100%">
						<TR style="display:none;">
						<!-- <TR> -->
							<TD><INPUT class="span12" type="text" name="ID" fieldname="GC_ZJB_LBJB_ID" id="ID" />
							</TD>
							<TD><INPUT class="span12" type="text" id="GC_JH_SJ_ID" name="GC_JH_SJ_ID" fieldname="JHID" />
							</TD>
							<TD><INPUT class="span12" type="text" id="XMSX" name="XMSX" fieldname="XMSX" />
							</TD>
							<TD><INPUT class="span12" type="text" id="ND" name="ND" fieldname="ND" />
							</TD>	
							<TD><INPUT class="span12" type="text" id="XMBH" name="XMBH" fieldname="XMBH" />
							</TD>
							<TD><INPUT class="span12" type="text" id="BDBH" name="BDBH" fieldname="BDBH" />
							</TD>
						</TR>	
						<tr>
							<th width="8%" class="right-border bottom-border text-right">项目名称</th>
							<td width="42%" colspan="3" class="right-border bottom-border"><input class="span12" type="text" placeholder="必填" onclick="xzxm();" data-toggle="modal" check-type="required" id="XMMC" name="XMMC" fieldname="XMMC"/></td>
							<th width="8%" class="right-border bottom-border text-right">标段名称</th>
							<td width="42%" colspan="3" class="bottom-border"><input class="span12" type="text" placeholder="" id="BDMC" name="BDMC" fieldname="BDMC" check-type="" disabled/></td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">财审报告编号</th>
							<td width="17%" class="right-border bottom-border"><input class="span12" type="text" placeholder="" id="CSBGBH" name="CSBGBH" fieldname="CSBGBH" check-type="maxlength" maxlength="50"></td>
							<th width="8%" class="right-border bottom-border text-right">提交需求时间（招造价公司）</th>
							<td width="17%" class="bottom-border"><input class="span12" type="date" placeholder="" id="TXQSJ" name="TXQSJ" fieldname="TXQSJ" check-type="" maxlength=""></td>
							<th width="8%" class="right-border bottom-border text-right">招标时间（内部招咨询公司）</th>
							<td width="17%" class="right-border bottom-border"><input class="span12" type="date" placeholder="" id="ZBSJ" name="ZBSJ" fieldname="ZBSJ" check-type="" maxlength=""></td>
							<th width="8%" class="right-border bottom-border text-right">咨询公司</th>
							<td width="17%" class="bottom-border">
								<select class="span12" id="ZXGS" type="text" name = "ZXGS" maxlength="300" fieldname="ZXGS" kind="dic" check-type="maxlength" src="T#GC_DSFJG:JGMC:JGMC:JGLB='4'" >
								</select>
							</td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">与总工办交接图纸日期</th>
							<td width="17%" class="right-border bottom-border"><input class="span12" type="date" placeholder="" id="TZJJSJ" name="TZJJSJ" fieldname="TZJJSJ" check-type="" maxlength=""></td>
							<th width="8%" class="right-border bottom-border text-right">发给咨询公司图纸日期</th>
							<td width="17%" class="bottom-border"><input class="span12" type="date" placeholder="" id="ZXGSJ" name="ZXGSJ" fieldname="ZXGSJ"  check-type="" maxlength=""></td>						
							<th width="8%" class="right-border bottom-border text-right">施工方案接收日期</th>
							<td width="17%" class="right-border bottom-border"><input class="span12" type="date" placeholder="" id="SGFAJS" name="SGFAJS" fieldname="SGFAJS" check-type="" maxlength=""></td>
							<th width="8%" class="right-border bottom-border text-right">招标文件接收日期</th>
							<td width="17%" class="bottom-border"><input class="span12" type="date" placeholder="" id="ZBWJJS" name="ZBWJJS" fieldname="ZBWJJS" check-type="" maxlength=""></td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">咨询公司交造价组日期</th>
							<td width="17%" class="right-border bottom-border"><input class="span12" type="date" placeholder="" id="ZXGSRQ" name="ZXGSRQ" fieldname="ZXGSRQ" check-type="" maxlength=""></td>
							<th width="8%" class="right-border bottom-border text-right">建管报财审日期</th>
							<td width="17%" class="bottom-border"><input class="span12" type="date" placeholder="" id="JGBCSRQ" name="JGBCSRQ" fieldname="JGBCSRQ" check-type="" maxlength=""></td>
							<th width="8%" class="right-border bottom-border text-right">上报财审值（元）</th>
							<td width="17%" class="right-border bottom-border"><input class="span12" style="text-align:right" type="number" placeholder="" id="SBCSZ" name="SBCSZ" fieldname="SBCSZ" check-type="maxlength number" maxlength="17"></td>
							<th width="8%" class="right-border bottom-border text-right">财政审完日期</th>
							<td width="17%" class="bottom-border"><input class="span12" type="date" placeholder="" id="CZSWRQ" name="CZSWRQ" fieldname="CZSWRQ" check-type="" maxlength=""></td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">财审审定值（元）</th>
							<td width="17%" class="right-border bottom-border"><input class="span12" style="text-align:right" type="number" placeholder="" id="CSSDZ" name="CSSDZ" fieldname="CSSDZ" check-type="maxlength number" maxlength="17"></td>
							<th width="8%" class="right-border bottom-border text-right">审减值（元）</th>
							<td width="17%" class="bottom-border"><input class="span12" style="text-align:right" type="number" placeholder="" id="SJZ" name="SJZ" fieldname="SJZ" check-type="maxlength number" maxlength="17"></td>
							<th width="8%" class="right-border bottom-border text-right">审减百分比（%）</th>
							<td width="17%" class="right-border bottom-border"><input class="span12" style="text-align:right" type="number" placeholder="" id="SJBFB" name="SJBFB" fieldname="SJBFB" check-type="maxlength number" maxlength="5"></td>
							<th width="8%" class="right-border bottom-border text-right">暂定金</th>
							<td width="17%" class="bottom-border"><input class="span12" style="text-align:right" type="number" placeholder="" id="ZDJ" name="ZDJ" fieldname="ZDJ" check-type="maxlength number" maxlength="17"></td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">提出疑问日期</th>
							<td width="42%" colspan="3" class="right-border bottom-border"><input class="span12" type="text" placeholder="" id="YWRQ" name="YWRQ" fieldname="YWRQ" check-type="maxlength" maxlength="100"></td>
							<th width="8%" class="right-border bottom-border text-right">答疑日期</th>
							<td width="42%" colspan="3" class="bottom-border"><input class="span12" type="text" placeholder="" id="DYRQ" name="DYRQ" fieldname="DYRQ"  check-type="maxlength" maxlength="100"></td>
						</tr>
						<tr>
							<th width="8%" class="right-border bottom-border text-right">备注</th>
							<td width="92%" colspan="7" class="bottom-border"><textarea class="span12" rows="3" id="BZ" name="BZ" fieldname="BZ" check-type="maxlength" maxlength="500"></textarea></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>

	<div align="center">
		<FORM name="frmPost" method="post" id="frmPost" style="display: none"
			target="_blank">
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML">
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ"	id = "txtFilter">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">

		</FORM>
	</div>
<!-- 	<script> -->
<%--   var a=<%=xx%>;
  var odd=convertJson.string2json1(a);
  var aa=$(odd).attr("ZXGS");
  alert(aa);
  $("#LbjForm").setFormValues(odd); --%>
  <!-- setValueByArr(a,["ID","XMMC","BDMC","CSBGBH","TXQSJ","ZBSJ","ZXGS","TZJJSJ","ZXGSJ","YWRQ","DYRQ","SGFAJS","ZBWJJS","ZXGSRQ","JGBCSRQ","SBCSZ","CZSWRQ","CSSDZ","SJZ","SJBFB","ZDJ","BZ"]);  -->
<!--   </script> -->
  	<script type="text/javascript">
	getWinData = function(data){
		var obj = eval("("+data+")");//字符串转JSON对象
		setValueByArr(obj,["XMMC","BDMC","GC_JH_SJ_ID","ND","XMBH","BDBH","XMSX"]);
//		$("#LbjForm").setFormValues(obj);
		document.getElementById("XMMC").disabled=true;
		document.getElementById("BDMC").disabled=true;
	};
	</script>
</body>
</html>