<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title></title>
<script type="text/javascript" charset="utf-8">
  var json,json2,bdmc;
  var rowindex,rowValue,id,jhsjid,index;
  var controllername= "${pageContext.request.contextPath }/zjb/lbj/lbjController.do";
	$(function() {
		ready();
		//查询
		var btn = $("#query");
		btn.click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryLbj&IsNull=1",data,DT1,null,false);
			//清空下方表单
			$("#LbjForm").clearFormResult();
				});
		//添加方法
		var insert=$("#insert");
		insert.click(function(){
			id=$("#ID").val();
			if($("#LbjForm").validationButton())
			{
				if(""!=$("#CSSDZ").val()&&""==$("#SBCSZ").val())
				{
				xAlert("警告","请输入提标价","3");
				return;
				}
				var data = Form2Json.formToJSON(LbjForm);
				//组成保存json串格式
				var data1 = defaultJson.packSaveJson(data);
				if(id==null||id==""){
					defaultJson.doInsertJson(controllername + "?insertLbj",data1, DT1);
				}else{
					defaultJson.doUpdateJson(controllername + "?updateLbj",data1, DT1,'editHuiDiao');
				}
			}else{
		  		defaultJson.clearTxtXML();
			}
		});
		var date=$("#date");
		date.click(function() {
			id=$("#ID").val();
			sjid=$("#JHSJID").val();
			if(sjid==null||sjid==""){
				xInfoMsg("请先选择数据","");
			}else{
				if(id==null||id==""){
					xInfoMsg("请先录入相关内容","");
				}else{
				$(window).manhuaDialog({"title":"拦标价>答疑日期","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zjb/lbj/dyqk.jsp?json2="+json2+"&bdmc="+bdmc,"modal":"4"});
				}
			}
		});
		//清空查询条件
		var clear=$("#clear");
		clear.click(function(){
			$("#queryForm").clearFormResult();
			initCommonQueyPage();
		});
	});
		//行选
	function tr_click(obj){
		var hang=$("#DT1").getSelectedRowIndex();
		if(hang=="-1"){
			$("#LbjForm").clearFormResult();
		}else{
		$("#LbjForm").setFormValues(obj);
		index= $("#DT1").getSelectedRowIndex();
		jhsjid=$(obj).attr("JHSJID");
		json2=JSON.stringify(obj);
		json2=encodeURI(json2);
		bdmc=$(obj).attr("BDMC");
		}
		zjbzzt();
		sfxyscs();
		
	
	}
		//页面初始化
    function ready() {
    	initCommonQueyPage();
   	 	var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryLbj&IsNull=1",data,DT1,null,true);
   };
	//页面更新方法
	function update()
	{
		$("#DT1").setSelect(index);
	    var value=$("#DT1").getSelectedRow();
	    var temp=convertJson.string2json1(value);
	    $("#LbjForm").setFormValues(temp);
		json2=encodeURI(JSON.stringify(temp));
	}
	//弹出页回显修改
	function xiugaihang()
	{
		$.ajax(
		{
			   url : controllername+"?queryLbj",//此处定义后台controller类和方法
		         data : {jhsjid:jhsjid},    //此处为传入后台的数据，可以为json，可以为string，如果为json，那起结构必须和后台接收的bean一致或和bean的get方法名一致，例如｛id：1，name：2｝后台接收的bean方法至少包含String id,String name方法  如果为string，那么可以写为{portal: JSON.stringify(data)}, 后台接收的时候参数可以为String，名字必须和前台保持一致及定义为String portal
		         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
		         async : false,   //同步执行，即执行完ajax方法后才可以执行下面的函数，如果不设置则为异步执行，及ajax和其他函数是异步的，可能后面的代码执行完了，ajax还没执行
		         success : function(result) {
		         var resultmsg = result.msg; //返回成功事操作
		      	 var index= $("#DT1").getSelectedRowIndex();
				 var subresultmsgobj1 = defaultJson.dealResultJson(resultmsg);
				 $("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
				 $("#DT1").setSelect(index);
				fym();
		         },
		         error : function(result) {//返回失败操作
		           defaultJson.clearTxtXML();
		          }			
		}		
		);
	}
	function fym(){
		var fym=$(window).manhuaDialog.getParentObj();
		fym.ready();
	}
	//自动计算审减值 审减百分比
	function jssjz()
	{
		
		var sbcsz=$("#SBCSZ").val();
		var cssdz=$("#CSSDZ").val();
		$("#SJZ").val(sbcsz-cssdz);
		var sjz=$("#SJZ").val();
		var chazhi=Math.round((Math.abs(sjz/sbcsz* 10000))) / 100.00;
		$("#SJBFB").val(chazhi);
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
	  
//回调
function editHuiDiao()
{
	fym();
	update();
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
//判断是否需要送财审
function sfxyscs() {

	$("input[name=ISXYSCS]").each(function(){
		if(this.checked==true){
			var aa=this.value;
			if (aa == '0') {
				$("#csTable").clearFormResult();
				$("#JGBCSRQ").attr({disabled : 'true'});
				$("#CZSWRQ").attr({disabled : 'true'});
				$("#CSBGBH").attr({disabled : 'true'});
				/* $("#SBCSZ").attr({disabled : 'true'}); */
				$("#SBCSZRQ").attr({disabled : 'true'});
				$("#CSSDZ").attr({disabled : 'true'});
				$("#CSSDZRQ").attr({disabled : 'true'});
				$("#ZDJ").attr({disabled : 'true'});
				$("#CSSX option[value='3']").remove();
			} else {
				$("#JGBCSRQ").removeAttr("disabled");
				$("#CZSWRQ").removeAttr("disabled");
				$("#CSBGBH").removeAttr("disabled");
				/* $("#SBCSZ").removeAttr("disabled"); */
				$("#SBCSZRQ").removeAttr("disabled");
				$("#CSSDZ").removeAttr("disabled");
				$("#CSSDZRQ").removeAttr("disabled");
				$("#ZDJ").removeAttr("disabled");
				var cssxVal = $("#CSSX").val();
				reloadSelectTableDic($("#CSSX"));
				$("#CSSX").val(cssxVal);
			}
		}
	});
}
function zjbzzt(){
	if("1"==$("#ZJBZZT").val()){
		$("#csTable").clearFormResult();
		$("#SBCSZ").val('');
		$("#CSSX").val('');
		$("#JGBCSRQ").attr({disabled : 'true'});
		$("#CZSWRQ").attr({disabled : 'true'});
		$("#CSBGBH").attr({disabled : 'true'});
		$("#SBCSZ").attr({disabled : 'true'});
		$("#SBCSZRQ").attr({disabled : 'true'});
		$("#CSSDZ").attr({disabled : 'true'});
		$("#CSSDZRQ").attr({disabled : 'true'});
		$("#ZDJ").attr({disabled : 'true'});
		$("#CSSX").attr({disabled : 'true'});
		var cssxVal = $("#CSSX").val();
		reloadSelectTableDic($("#CSSX"));
		$("#CSSX").val(cssxVal);
		$("input[name=ISXYSCS]").attr("checked",false);
		$("input[name=ISXYSCS]").attr("disabled",true);
	}else{
		$("#JGBCSRQ").removeAttr("disabled");
		$("#CZSWRQ").removeAttr("disabled");
		$("#CSBGBH").removeAttr("disabled");
		$("#SBCSZ").removeAttr("disabled");
		$("#SBCSZRQ").removeAttr("disabled");
		$("#CSSDZ").removeAttr("disabled");
		$("#CSSDZRQ").removeAttr("disabled");
		$("#ZDJ").removeAttr("disabled");
		$("#CSSX").removeAttr("disabled");
		$("input[name=ISXYSCS]").removeAttr("disabled",true);
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
      <h4 class="title">拦标价管理</h4>
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
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" editable="0" pageNum="5">
                <thead>
                    <tr>	
                    	<th name="XH" id="_XH"  tdalign="">&nbsp;#&nbsp;</th>
                    		<th fieldname="XMBH"  rowMerge="true"  >&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC"  rowMerge="true"  maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="BDBH"  maxlength="15" Customfunction="doBdBH" >&nbsp;标段编号&nbsp;</th>
							<th fieldname="BDMC"  tdalign="" Customfunction="doBdmc"  maxlength="15">&nbsp;标段名称&nbsp;</th>
							<th fieldname="GYS"  tdalign="right"   >&nbsp;概预算(元)&nbsp;</th>
							<th fieldname="CSBGBH"  tdalign="">&nbsp;财审报告编号&nbsp;</th>
							<th fieldname="LRSJ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;提需求时间<p></p>(招造价公司)</span></th>
							<th fieldname="KBRQ"  tdalign="center" class="wrap"><span style="width:120px;display:block;">招标时间<p></p>(内部咨询公司)</span></th>
							<th fieldname="ZXGS"  tdalign="" maxlength="15">&nbsp;咨询公司&nbsp;</th>
							<th fieldname="TZJJSJ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;与总工办交接图纸日期</span></th>
							<th fieldname="ZXGSJ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;发给咨询公司图纸日期</span></th>
							<th fieldname="YWRQ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;提出疑问日期</span></th>
							<th fieldname="HFRQ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;答疑日期</span></th>
							<th fieldname="ZXGSRQ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;咨询公司<p></p>交造价组日期</span></th>
							<th fieldname="JGBCSRQ"  tdalign="center" class="wrap"><span style="width:80px;display:block;">&nbsp;建管报财审日期</span></th>
							<th fieldname="SBCSZ"  tdalign="right" maxlength="15" >&nbsp;提报价(元)&nbsp;</th>
							<th fieldname="CZSWRQ"  tdalign="center" class="wrap"><span style="width:100px;display:block;">&nbsp;财政审完日期</span></th>
							<th fieldname="CSSDZ"  tdalign="right" maxlength="15" >&nbsp;财审审定值(元)&nbsp;</th>
							<th fieldname="SJZ"  tdalign="right" maxlength="15" >&nbsp;审减值(元)&nbsp;</th>
							<th fieldname="SJBFB"  tdalign="right" >&nbsp;审减百分比(%)&nbsp;</th>
							<th fieldname="ZDJ"  tdalign="right" maxlength="15" class="wrap" ><span style="width:105px;display:block;">&nbsp;暂定金</span></th>
							<th fieldname="BZ"  tdalign="" maxlength="15">&nbsp;备注&nbsp;</th>
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
	<h4 class="title">项目信息
	<span class="pull-right">
		<button id="insert" class="btn" type="button">保 存</button>
	</span></h4>
	<form method="post" id="LbjForm" width="100%">
		<table class="B-table" width="100%">
			<TR style="display:none;">
			<!-- <TR> -->
				<TD><INPUT class="span12" type="text" name="ID" fieldname="GC_ZJB_LBJB_ID" id="ID" />
				</TD><TD>	<INPUT class="span12" type="text" id="JHSJID" name="JHSJID" fieldname="JHSJID" />
				</TD><TD>	<INPUT class="span12" type="text" id="XMSX" name="XMSX" fieldname="XMSX" />
				</TD><TD>	<INPUT class="span12" type="text" id="ND" name="ND" fieldname="ND" />
				</TD><TD>	<INPUT class="span12" type="text" id="XMID" name="XMID" fieldname="XMID" />
				</TD><TD>	<INPUT class="span12" type="text" id="BDID" name="BDID" fieldname="BDID" />
				</TD><TD>	<INPUT class="span12" type="text" id="PXH" name="PXH" fieldname="PXH" />
				</TD><TD>	<INPUT class="span12" type="text" id="SJWYBH" name="SJWYBH" fieldname="SJWYBH" />
				</TD>
			</TR>	
			<tr>
				<th  width="10%" class="right-border bottom-border text-right disabledTh">项目名称</th>
				<td  width="40%"   class="right-border bottom-border "><input class="span12 xmmc" type="text" placeholder="必填" disabled data-toggle="modal" check-type="required" id="XMMC" name="XMMC" fieldname="XMMC"/></td>
				<th  width="10%" class="right-border bottom-border text-right disabledTh">标段名称</th>
				<td  width="40%"   class="bottom-border"><input class="span12" type="text" placeholder="" id="BDMC" name="BDMC" fieldname="BDMC" check-type="" disabled/></td>
			</tr>
			<tr>
				<th class="right-border bottom-border text-right disabledTh"> 概预算 </th>
       			<td class="bottom-border"><input class="span12" style="width:65%;text-align:right" keep="true" type="text" placeholder="" id="GYS" name="GYS" fieldname="GYS"   disabled/>&nbsp;&nbsp;<b>(元)</b></td>
			 	<th class="right-border bottom-border text-right disabledTh">咨询公司</th>
				 <td  class="bottom-border"  >
			     <select class="span5 department " style="width: 80%" id="ZXGS" name = "ZXGS"  fieldname="ZXGS" kind="dic" src="T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX='4' " >
                  </select>
                  <a href="javascript:void(0)" title="点击选择单位"><i id="lydwSelect" selObj="ZXGS" dwlx="4" class="icon-edit" onclick="selectCjdw('lydwSelect');" isLxSelect="1"></i></a>
                  </td>
				<td  colspan="4"></td>
			</tr>
			</table>
			<h4 class="title">造价前期条件</h4>
			<table class="B-table"  width="100%">
			  <tr>
		        <th width="16%" class="right-border bottom-border text-right">其他部门负责</th>
		        <td width="17%" class="right-border bottom-border"><input class="span12" id="ISQTBMFZ" type="radio"    placeholder="" kind="dic" src="E#1=是:0=否" name = "ISQTBMFZ" fieldname="ISQTBMFZ" ></td>
		      	<td  colspan="5"></td>
		      </tr>
			<tr>
				<th width="16%" class="right-border bottom-border text-right">与总工办交接图纸</th>
				<td width="17%" class="right-border bottom-border"><input class="span12 date" type="date" placeholder="" id="TZJJSJ" name="TZJJSJ" fieldname="TZJJSJ" check-type="" maxlength=""></td>
			    <th width="16%" class="right-border bottom-border text-right">发给咨询公司图纸</th>
				<td width="17%" class="bottom-border"><input class="span12 date" type="date" placeholder="" id="ZXGSJ" name="ZXGSJ" fieldname="ZXGSJ"  check-type="" maxlength=""></td>						
				<th width="16%" class="right-border bottom-border text-right" colspan="2">咨询公司交造价组</th>
				<td width="17%" class="right-border bottom-border"><input class="span12 date" type="date" placeholder="" id="ZXGSRQ" name="ZXGSRQ" fieldname="ZXGSRQ" check-type="" maxlength=""></td>
			</tr>
			<tr>
				<th class="right-border bottom-border text-right">编制造价</th>
       			<td class=" bottom-border" >
					<select class="span8"  id="ZJBZZT" name = "ZJBZZT"  fieldname="ZJBZZT" kind="dic" src="ZJTJ"  onchange="zjbzzt()">
                  	</select>
                </td>
			 	<th class="right-border bottom-border text-right">是否需要送财审</th>
       			<td class=" bottom-border text-center" ><input class="span12" id="ISXYSCS" type="radio"    placeholder="" kind="dic" src="E#1=是:0=否" name = "ISXYSCS" fieldname="ISXYSCS" onclick="sfxyscs()"></td>
			 	<td class=" bottom-border text-left" >
				<select class="span12"  id="CSSX" name = "CSSX"  fieldname="CSSX" kind="dic" src="CSSX" >
                  </select></td>
                <th  class="right-border bottom-border text-right">提报价</th>
				<td  class="right-border bottom-border"><input class="span12" style="width:65%;text-align:right"  type="number" onkeyup="jssjz()" placeholder="" id="SBCSZ" name="SBCSZ" fieldname="SBCSZ" check-type="maxlength" maxlength="17" min="0">&nbsp;&nbsp;<b>(元)</b></td>
				
			</tr>
			</table>
			<h4 class="title">财审</h4>
			<table class="B-table" id="csTable"  width="100%">
			<tr>
				<th width="16%" class="right-border bottom-border text-right">建管报财审日期</th>
				<td width="17%" class="bottom-border"><input class="span12 date" type="date" placeholder="" id="JGBCSRQ" name="JGBCSRQ" fieldname="JGBCSRQ" check-type="" maxlength=""></td>
				<th width="16%" class="right-border bottom-border text-right">财审审完日期</th>
				<td width="17%" class="bottom-border"><input class="span12 date" type="date" placeholder="" id="CZSWRQ" name="CZSWRQ" fieldname="CZSWRQ" check-type="" maxlength=""></td>
			    <th width="16%" class="right-border bottom-border text-right">财审报告编号</th>
				<td width="17%" class="right-border bottom-border"><input class="span12" type="text" placeholder="" id="CSBGBH" name="CSBGBH" fieldname="CSBGBH" check-type="maxlength" maxlength="50"></td>
			</tr>
			<tr>
			   	<th  class="right-border bottom-border text-right">财审审定值</th>
				<td  class="right-border bottom-border"><input class="span12" style="width:65%;text-align:right" type="number" onkeyup="jssjz()"  placeholder="" id="CSSDZ" name="CSSDZ" fieldname="CSSDZ" check-type="maxlength" maxlength="17" min="0">&nbsp;&nbsp;<b>(元)</b></td>
				<th  class="right-border bottom-border text-right">审减值</th>
				<td class="bottom-border"><input class="span12" style="width:65%;text-align:right" type="number" disabled placeholder="" id="SJZ" name="SJZ" fieldname="SJZ" check-type="maxlength" maxlength="17" >&nbsp;&nbsp;<b>(元)</b></td>
				<th  class="right-border bottom-border text-right">审减百分比</th>
				<td  class="right-border bottom-border"><input class="span12" style="width:65%;text-align:right" type="number" disabled placeholder="" id="SJBFB" name="SJBFB" fieldname="SJBFB" check-type="maxlength" maxlength="17"  >&nbsp;&nbsp;<b>(%)</b></td>
			</tr>
			<tr>
				<th  class="right-border bottom-border text-right">暂定金</th>
				<td  class="bottom-border"><input class="span12" style="width:65%;text-align:right" type="number"  placeholder="" id="ZDJ" name="ZDJ" fieldname="ZDJ" check-type="maxlength" maxlength="17" min="0">&nbsp;&nbsp;<b>(元)</b></td>
				<td  class="right-border bottom-border" colspan="4">
			</tr>
			</table>
			<h4 ></h4>
			<table class="B-table"  width="100%">
			<tr>
				<th width="10%" class="right-border bottom-border text-right">备注</th>
				<td width="90%" colspan="7" class="bottom-border"><textarea class="span12" rows="3" id="BZ" name="BZ" fieldname="BZ" check-type="maxlength" maxlength="500"></textarea></td>
			</tr>
		</table>
		</form>
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