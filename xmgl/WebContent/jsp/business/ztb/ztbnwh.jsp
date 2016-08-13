<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
 <script type="text/javascript">
 var controllername= "${pageContext.request.contextPath }/ZhaoBiaoXuQiuController.do";
  $(function()
 {	//保存
	 $("#example2").click(function()
		{
			 if($("#DT1").getTableRows()==0){
				 xAlertMsg("没有项目被选中,请选择项目后保存!");
				 return;
			 }
			 //获取DT1中的行记录的主要值
			 if($("#demoForm").validationButton())
			{
				 
 				//生成json串
 		 		var data = Form2Json.formToJSON(demoForm);
 				//组成保存json串格式
 				var data1 = defaultJson.packSaveJson(data);
 				//调用ajax插入
 				defaultJson.doInsertJson(controllername + "?insertdemo", data1,DT1,null);
			}
		});
	
 }); 
 $(function() {
		//新增
		var weihu = $("#example1");
	 	weihu.click(function() 
	 			{
	 		$(window).manhuaDialog({"title":"项目管理>选择项目","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/xmmore.jsp","modal":"1"});
	            }); 
	});
 
//弹出区域回调
 getWinData = function(data){
 	
	alert(data);
 	for(var i=0;i<data.length;i++){
 		$("#DT1").insertResult(data[i],DT1,1);
 	}
 	
 };
 </script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
 <div class="B-small-from-table-autoConcise">
  <h4 class="title">项目列表
 
  	<span class="pull-right">
				 <button id="example1" class="btn"  type="button">选择项目</button>
				
	</span> 
  </h4>
	  <div class="overFlowX">                                
		<table  width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" >
			<thead>
				<tr>
				    <th  name="XH" id="_XH" >&nbsp;#&nbsp;</th>
				    <th fieldname="XMBH"   hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
				 	<th fieldname="XMMC"  maxlength="15" >&nbsp;项目名称&nbsp;</th>
				 	<th fieldname="BDMC"  maxlength="15" >&nbsp;标段名称&nbsp;</th>
				 	<th fieldname="BDMC"  maxlength="15" >&nbsp;项目管理公司&nbsp;</th>
				</tr>
			</thead>
			 <tbody>
		     </tbody>
		</table>
	</div>
        		<h4 class="title">招标信息
        			<span class="pull-right">
				      <button id="example2" class="btn"  type="button">保存</button>
				       <button id="example3" class="btn"  type="button">提交</button>
                	</span>
        		</h4>
        		 <form method="post"  id="demoForm"  >
        		 <table class="B-table" width="100%">
       			<tr>
          			<th  class="right-border bottom-border text-right" width="8%">工作名称</th>
          			<td class="right-border bottom-border text-right" colspan="3"  width="17%" ><input class="span12"  type="text" maxlength="100"  id="GZMC" name = "GZMC" fieldname="GZMC"></td>
		            <td class="right-border bottom-border text-right" colspan="4">
                </tr>
                 <tr >
                   <th class="right-border bottom-border text-right" width="8%">招标类型</th>
          			<td class="right-border bottom-border "  width="17%" ><select class="span12" id="ZBLX" name = "ZBLX" fieldname="ZBLX" kind="dic" src="ZBLX" operation="=">
			 	</select></td>
       			    <th class="right-border bottom-border text-right" width="8%">投标报价方式</th>
         			<td class=" bottom-border " width="17%">
         			    <select class="span8" id="TBJFS" name = "TBJFS" maxlength="100" fieldname="TBJFS" kind="dic"  src="T#GC_DSFJG:JGMC:JGMC:JGLB='4'" >
		                </select>
		            </td>
          			<th class="right-border bottom-border text-right " width="8%">招标方式</th>
          			<td class="right-border bottom-border " width="17%">
	          			<select class="span12" id="ZBFS" name = "ZBFS" fieldname="ZBFS" kind="dic" src="ZBFS" operation="=">
			  	</select>
		            </td>
          				<th width="8%" class="right-border bottom-border text-right">
							投资额
						</th>
						<td width="17%" class="bottom-border">
							<input class="span10" type="number" name="YSE" style="text-align:right;"
								placeholder="必填" check-type="required" fieldname="YSE">&nbsp;元
						</td>
        		</tr>
           
          	    <tr>
          			<th  class="right-border bottom-border text-right">工作内容</th>
          			<td  class="right-border bottom-border text-right" colspan="3"><textarea fieldname="GZNR" id="GZNR"  name="GZNR" class="span12" rows="2" maxlength="4000" ></textarea></td>
          		    <th  class="right-border bottom-border text-right"  width="8%">投标人资质、业绩要求</th>
          			<td  class="right-border bottom-border text-right"  colspan="3"><textarea fieldname="ZZYJYQ" id="ZZYJYQ"  name="ZZYJYQ" class="span12" rows="2" maxlength="4000"></textarea></td>
         		</tr>
       			<tr >	
         			<th  class="right-border bottom-border text-right" >时限要求</th>
          			<td  class="right-border bottom-border text-right" colspan="3" ><textarea fieldname="SXYQ" id="SXYQ"  name="SXYQ" class="span12" rows="2" maxlength="4000"></textarea></td>
       			    <th class="right-border bottom-border text-right" >技术要求</th>
         			<td class=" bottom-border text-center" colspan="3"><textarea fieldname="JSYQ" id="JSYQ"  name="JSYQ" class="span12" rows="2" maxlength="4000"></textarea></td>
          		</tr>
       			<tr >
          			<th class="right-border bottom-border text-right">成果或目标要求</th>
          			<td class="right-border bottom-border text-right" colspan="3">
	          		<textarea fieldname="CGMBYQ" id="CGMBYQ"  name="CGMBYQ" maxlength="4000" class="span12" rows="2" ></textarea>
          			<th class="right-border bottom-border text-right">配备人员要求</th>
          			<td class="right-border bottom-border text-right" colspan="3"><textarea fieldname="PBRYYQ" id="PBRYYQ"  name="PBRYYQ" class="span12" rows="2" maxlength="4000"></textarea></td>
          		</tr>
       			<tr >
          			<th class="right-border bottom-border text-right">配备的设备要求</th>
          			<td class="right-border bottom-border text-right" colspan="3" ><textarea fieldname="PBSBYQ" id="PBSBYQ"  name="PBSBYQ" class="span12" rows="2" maxlength="4000"></textarea></td>
        		    <th class="right-border bottom-border text-right">其它要求</th>
          			<td class="right-border bottom-border" colspan="3"><textarea fieldname="QTYQ" id="QTYQ"  name="QTYQ" class="span12" rows="2" maxlength="4000"></textarea></td>
        		</tr>
        		<tr>
                   <th class="right-border bottom-border text-right" width="8%">需求单位经办人</th>
          			<td class="right-border bottom-border "  width="17%" ><input class="span12" id="XQDWJBR" name = "XQDWJBR" fieldname="XQDWJBR" type="text" >
			 	</td>
       			    <th class="right-border bottom-border text-right" width="8%">需求单位经办人办理时间</th>
         			<td class=" bottom-border " width="17%">
         			    <input class="span8" id="XQDWJBRSJ" name = "XQDWJBRSJ" maxlength="20" fieldname="XQDWJBRSJ" type="date"   >
		               
		            </td>
          			<th class="right-border bottom-border text-right " width="8%">需求单位负责人</th>
          			<td class="right-border bottom-border " width="17%">
	          			<input class="span12" id="XQDWFZR" name = "XQDWFZR" fieldname="XQDWFZR" type="text" >
			  	
		            </td>
          				<th width="8%" class="right-border bottom-border text-right">
							需求单位负责人办理时间
						</th>
						<td width="17%" class="bottom-border">
							<input class="span10" type="date" id = "XQDWFZRSJ" name="XQDWFZRSJ" 
								 fieldname="XQDWFZRSJ">
						</td>
        		</tr>
        		<tr>
                   <th class="right-border bottom-border text-right" width="8%">招标部经办人</th>
          			<td class="right-border bottom-border "  width="17%" ><input class="span12" id="ZBBJBR" name = "ZBBJBR" fieldname="ZBBJBR" type="text" >
			 		</td>
       			    <th class="right-border bottom-border text-right" width="8%">招标部负责人</th>
         			<td class=" bottom-border " width="17%">
         			    <input class="span8" id="ZBBFZR" name = "ZBBFZR" maxlength="20" fieldname="ZBBFZR" type="text"  >
		               
		            </td>
		             <td class="right-border bottom-border text-right" colspan="4">
		            </tr>
        		</table>
      	  </form>
     	</div>
 	</div>
</div>
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
 <script type="text/javascript">
 </script>
</body>
</html>