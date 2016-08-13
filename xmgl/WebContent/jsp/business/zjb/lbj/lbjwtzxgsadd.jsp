<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<%
	String xx=request.getParameter("xx");
%>
<script type="text/javascript" charset="utf-8">
  var controllername= "${pageContext.request.contextPath }/zjb/lbj/lbjController.do";
  var tbl = null;
	$(function() {
		  //获取父页面的值
		  var a=<%=xx%>;
		  //将父页面的值转成json对象
		  var odd=convertJson.string2json1(a);
		  //将数据放入表单
		  $("#demoForm").setFormValues(odd);
		//保存按钮
		var btn = $("#example1");
		btn.click(function()
		  {
			id=$("#ID").val();
			if($("#demoForm").validationButton())
			{
				if(id==null||id==""){
					//生成json串
					//alert('aa');
	 				var data = Form2Json.formToJSON(demoForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
				 		defaultJson.doInsertJson(controllername + "?insertLbj",data1, null);
	 				var data2 = $("#frmPost").find("#resultXML").val();
	 			 	var fuyemian=$(window).manhuaDialog.getParentObj();
				    fuyemian.xiugaihang(data2);
				    $(window).manhuaDialog.close();
				}else{
					//alert('bb');
					//生成json串
	 				var data = Form2Json.formToJSON(demoForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
				 	defaultJson.doUpdateJson(controllername + "?updateLbj",data1, null);
	 				var data2 = $("#frmPost").find("#resultXML").val();
	 			 	var fuyemian=$(window).manhuaDialog.getParentObj();
				    fuyemian.xiugaihang(data2);
				    $(window).manhuaDialog.close();
					
				}
 			
			}
	 		 /* 	 if($("#demoForm").validationButton())
				{
	 		 		//生成json串
	 				var data = Form2Json.formToJSON(demoForm);
	 				//组成保存json串格式
	 				var data1 = defaultJson.packSaveJson(data);
	 				//调用ajax插入
 			 		defaultJson.doInsertJson(controllername + "?insertLbj",data1, null);
	 				var data2 = $("#frmPost").find("#resultXML").val();
	 			 	var fuyemian=$(window).manhuaDialog.getParentObj();
				    fuyemian.xiugaihang(data2);
				    $(window).manhuaDialog.close();
			
				} */
				else
				{
					return ;
				} 
	 		 
 		   });
	    //清空表单
		var btn1 = $("#example_clear");
		btn1.click(function() 
		{
			$("#demoForm").clearFormResult(); 
		});
	
	}
	);
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">造价咨询委托
  	<span class="pull-right">
				<!--  <button id="example1" class="btn"  type="button">保存</button> -->
          		<!--   <button id="fuzhixinzeng" class="btn btn-inverse"  type="button">复制新增</button>-->
          	    <!--  <button id="example_clear" class="btn"  type="button">清空</button> -->
				</span> 
  </h4>
 <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
      		<table class="B-table"  width="100%">
			<TR style="display:none;">
			<!-- <TR> -->
			<TD><INPUT class="span12" type="text" name="ID" fieldname="GC_ZJB_LBJB_ID" id="ID" />
				</TD><TD>	<INPUT class="span12"  keep="true" type="text" id="JHSJID" name="JHSJID" fieldname="JHSJID" />
				</TD><TD>	<INPUT class="span12"  keep="true" type="text" id="XMSX" name="XMSX" fieldname="XMSX" />
				</TD><TD>	<INPUT class="span12"  keep="true" type="text" id="ND" name="ND" fieldname="ND" />
				</TD><TD>	<INPUT class="span12"  keep="true" type="text" id="XMID" name="XMID" fieldname="XMID" />
				</TD><TD>	<INPUT class="span12"  keep="true" type="text" id="BDID" name="BDID" fieldname="BDID" />
				</TD><TD>	<INPUT class="span12"  keep="true" type="text" id="PXH" name="PXH" fieldname="PXH" />
				</TD>
			</TR>	
		<!-- 	<tr>
				<th width="10%" class="right-border bottom-border text-right">项目名称</th>
				<td width="40%" colspan="3" class="right-border bottom-border"><input keep="true" class="span12" type="text" placeholder="必填" readonly data-toggle="modal" check-type="required" id="XMMC" name="XMMC" fieldname="XMMC"/></td>
				<th width="10%" class="right-border bottom-border text-right">标段名称</th>
				<td width="40%" colspan="3" class="bottom-border"><input class="span12" keep="true" type="text" placeholder="" id="BDMC" name="BDMC" fieldname="BDMC" check-type="" readonly/></td>
			</tr> -->
			</table>
			<table class="B-table"  width="100%">
			<tr>
				<!-- -->
				<th width="16%" class="right-border bottom-border text-right">提交需求时间（招造价公司）</th>
				<td width="17%" class="bottom-border"><input class="span12" type="date" placeholder="" id="TXQSJ" name="TXQSJ" fieldname="TXQSJ" check-type="" maxlength=""></td>
				<th width="16%" class="right-border bottom-border text-right">招标时间（内部招咨询公司）</th>
				<td width="17%" class="right-border bottom-border"><input class="span12" type="date" placeholder="" id="ZBSJ" name="ZBSJ" fieldname="ZBSJ" check-type="" maxlength=""></td>
				<th width="7%" class="right-border bottom-border text-right">咨询公司</th>
				<td  class="bottom-border">
					<select class="span12" id="ZXGS" type="text" name = "ZXGS" maxlength="300" fieldname="ZXGS" kind="dic" check-type="maxlength" src="T#GC_DSFJG:JGMC:JGMC:JGLB='4'" >
					</select>
				</td>
			</tr>
			<tr>
				<!-- <th width="16%" class="right-border bottom-border text-right">与总工办交接图纸</th>
				<td width="17%" class="right-border bottom-border"><input class="span12" type="date" placeholder="" id="TZJJSJ" name="TZJJSJ" fieldname="TZJJSJ" check-type="" maxlength=""></td>
				<th width="16%" class="right-border bottom-border text-right">施工方案接收日期</th>
				<td width="17%" class="right-border bottom-border"><input class="span12" type="date" placeholder="" id="SGFAJS" name="SGFAJS" fieldname="SGFAJS" check-type="" maxlength=""></td>
				 -->
				<th width="16%" class="right-border bottom-border text-right">招标文件接收日期</th>
				<td width="17%" class="bottom-border"><input class="span12" type="date" placeholder="" id="ZBWJJS" name="ZBWJJS" fieldname="ZBWJJS" check-type="" maxlength=""></td>
			    <td colspan="4" class="right-border bottom-border">
			</tr>
		<!-- 	<tr>
			    <th  class="right-border bottom-border text-right">发给咨询公司图纸</th>
				<td  class="bottom-border"><input class="span12" type="date" placeholder="" id="ZXGSJ" name="ZXGSJ" fieldname="ZXGSJ"  check-type="" maxlength=""></td>						
				<th  class="right-border bottom-border text-right">咨询公司交造价组</th>
				<td  class="right-border bottom-border"><input class="span12" type="date" placeholder="" id="ZXGSRQ" name="ZXGSRQ" fieldname="ZXGSRQ" check-type="" maxlength=""></td>
			    <td  class="right-border bottom-border" colspan="2">
			</tr> -->
			</table>
		<!-- 	<h4 class="title">财审</h4>
			<table class="B-table"  width="100%">
			<tr>
				<th width="16%" class="right-border bottom-border text-right">建管报财审日期</th>
				<td width="17%" class="bottom-border"><input class="span12" type="date" placeholder="" id="JGBCSRQ" name="JGBCSRQ" fieldname="JGBCSRQ" check-type="" maxlength=""></td>
				<th width="16%" class="right-border bottom-border text-right">财政审完日期</th>
				<td width="17%" class="bottom-border"><input class="span12" type="date" placeholder="" id="CZSWRQ" name="CZSWRQ" fieldname="CZSWRQ" check-type="" maxlength=""></td>
			    <th width="16%" class="right-border bottom-border text-right">财审报告编号</th>
				<td width="17%" class="right-border bottom-border"><input class="span12" type="text" placeholder="" id="CSBGBH" name="CSBGBH" fieldname="CSBGBH" check-type="maxlength" maxlength="50"></td>
			</tr>
			<tr>
			    <th  class="right-border bottom-border text-right">上报财审值（元）</th>
				<td  class="right-border bottom-border"><input class="span12" style="text-align:right" type="number" placeholder="" id="SBCSZ" name="SBCSZ" fieldname="SBCSZ" check-type="maxlength number" maxlength="17" min="0"></td>
				<th  class="right-border bottom-border text-right">财审审定值（元）</th>
				<td  class="right-border bottom-border"><input class="span12" style="text-align:right" type="number" placeholder="" id="CSSDZ" name="CSSDZ" fieldname="CSSDZ" check-type="maxlength number" maxlength="17" min="0"></td>
				<th  class="right-border bottom-border text-right">审减值（元）</th>
				<td class="bottom-border"><input class="span12" style="text-align:right" type="number" placeholder="" id="SJZ" name="SJZ" fieldname="SJZ" check-type="maxlength number" maxlength="17" min="0"></td>
			</tr>
			<tr>	
				<th  class="right-border bottom-border text-right">审减百分比（%）</th>
				<td  class="right-border bottom-border"><input class="span12" style="text-align:right" type="number" placeholder="" id="SJBFB" name="SJBFB" fieldname="SJBFB" check-type="maxlength" maxlength="5" min="0" max="100"></td>
				<th  class="right-border bottom-border text-right">暂定金（元）</th>
				<td  class="bottom-border"><input class="span12" style="text-align:right" type="number" placeholder="" id="ZDJ" name="ZDJ" fieldname="ZDJ" check-type="maxlength number" maxlength="17" min="0"></td>
			    <td  class="right-border bottom-border" colspan="2">
			</tr>
			</table>
			<h4 ></h4>
			<table class="B-table"  width="100%">
			<tr>
				<th width="10%" class="right-border bottom-border text-right">备注</th>
				<td width="90%" colspan="7" class="bottom-border"><textarea class="span12" rows="3" id="BZ" name="BZ" fieldname="BZ" check-type="maxlength" maxlength="500"></textarea></td>
			</tr>
		</table> -->
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
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "XMNF"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>

</body>
</html>