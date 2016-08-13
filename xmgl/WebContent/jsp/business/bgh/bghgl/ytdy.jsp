<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="java.sql.Connection"%>

<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<style type="text/css">
body {font-size:14px;}
h2 {display:inline; line-height:2em;}
.table2 {
	border-left: #000 solid 1px;
	border-top: #000 solid 1px;
	margin:10px auto;
}
.marginBottom15px {margin-bottom:15px;}
.table2 tr td,.table2 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 1px;
	border-bottom: #000 solid 1px;
}
input[type='text'] {
	vertical-align: middle;
	height: 20px;
	line-height: 16px;
	padding: 2px;
}
.table1 {
}
.table1 tr td,.table1 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
}
.table3 {
}
.table3 tr td,.table3 tr th {
	line-height: 1.5em;
	padding: 4px;
	border-right: #000 solid 0;
	border-bottom: #000 solid 0;
}
.table3 tr th {/* border-top: #000 solid 1px; border-bottom: #000 solid 1px; */}
.B-small-from-table-auto h4{padding:10px;}
</style>
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/banGongHuiController.do"; 
	$(function()
		{
			queryHyjy();
			$("#printButton").click(function(){
				$(this).hide();
				$("#queryH").hide();
				document.body.style.marginLeft = "0px";
				window.print();
				$("#queryH").show();
				$(this).show();
				resizeBody();
			});
			resizeBody();
		});
	function resizeBody(){
		if(window.screen.width>800){
			try{
				document.body.style.marginLeft = ((window.screen.width-800)/2 - 15)+"px";
			}catch(e){}
		}else{
			document.body.style.marginLeft = "0px";
		}
	}
	function queryHyjy()
	{
		var action1 = controllername + "?queryBGHWT";
		$.ajax({
			url : action1,
			success: function(result)
			{
				bghwt(result);
			}
		});
	}
//放入表格
	function bghwt(result)
	{
		var resultmsgobj = convertJson.string2json1(result);
		var msgStr = resultmsgobj.msg.replace(/\\n/g,"<br/>");	//保留换行
		msgStr = msgStr.replace(/\\b/g,"&nbsp;");				//保留空格
		var resultobj = convertJson.string2json1(msgStr);
		if(resultobj=='0'){
			$("#HC").html('没有打印的议题');
			return ;
		}
		var len = resultobj.response.data.length;
		for(var i=0;i<len;i++){
			
			var subresultmsgobj = resultobj.response.data[i];
			var WTBT=$(subresultmsgobj).attr("WTBT");
			var XMMC=$(subresultmsgobj).attr("XMMC");
			var BDMC=$(subresultmsgobj).attr("BDMC");
			var LRBM=$(subresultmsgobj).attr("FQBM_SV");
			var XWJJSJ=$(subresultmsgobj).attr("XWJJSJ");
			var WTMS=$(subresultmsgobj).attr("WTMS");
			if(LRBM==undefined){
				LRBM="";
			}
			pingTable(WTBT,LRBM,XWJJSJ,XMMC,BDMC,WTMS,i);
		}
	}
function pingTable(a,b,c,m,n,d,hangshu)
{
	//大小写转换
    var result=show(hangshu+1);
    if(result.length!='2'||result=="一十"){
    	var r=result.substring(0,1);
    	if(r=="一"){
    		result=result.substring(1,result.length);
    	}
    } 
	var str=document.getElementById("bghtables").outerHTML;
	
	str=str.replace( 'WTGS' , result );
		str=str.replace( 'WTBT' , a );
		str=str.replace( 'FQBM' , b );
		str=str.replace( 'XWJJSJ' , c );
		str=str.replace( 'XMMC' , m );
		str=str.replace( 'BDMC' , n );
		str=str.replace( 'WTMS' , d );
		str=str.replace( 'bghtables' , 'tables'+hangshu );
		str=str.replace( 'style="display: none"' , '');
	 $("#tablelist").append(str); 
}
function show(hangshu){
	var text=hangshu;
	text=parseInt(text);
	var num=/[0-9]{1,}/;
	text=text.toString();
	if(text!=""){
	if(!num.exec(text))
	{
	}else if(text.length<13){
	{
	for(var i=0;i<text.length;i++){
	  var s=text.charAt(i);
	  var l=text.length; 
	  var y,w;
	  var k=[" ","十","佰","仟","万","十","佰","仟","亿","十","佰","仟"];
	  var n=["0","一","二","三","四","五","六","七","八","九"];
	  s=parseInt(s);
	  a=n[s];
	  w=a+k[l-i-1];
	  y=y+w;
	  }
	  q=y.substr(9);
	  var o=q.charAt(0);
	  var ls=q.length;
	  var p=q.charAt(ls-2);
	  if(o=="0"){
	   }
	  else if(p=="0"){
	    for (var i=ls;i>0;i--){
	     var ss=q.charAt(i);
	     if(ss=="0"){
	      q=q.substring(i,0);
	      }
	     }
	    }//p=="零"
	  else{
	   }
	 
	return q;
	}//有效数字
	}//小于13位
	else{}
	}//不为空
	else{}
	}
</script>
</head>
<body style="background-color: #F3F2EE;">
<div class="container-fluid" style="width:800px;margin-top:10px;">
    <span class="pull-right">
   	<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
    </span>
     <div class="B-small-from-table-auto" style="border:0px;" id="printDiv">
        <h3 id="queryH" class="isjy">
        <span  style="width: 20%;padding-left:33%; ">&nbsp;</span><span style="width: 80%" id="HC"></span>
        </h3>
        <div class="container-fluid" id="tablelist">
       		 <input class="span12" id="GC_BGH_ID" type="hidden"   fieldname="GC_BGH_ID" name = "GC_BGH_ID">
			<input class="span12" id="HYZT" type="hidden"   fieldname="HYZT" name = "HYZT">
         <div class="row-fluid ismoban" id="bghtables" style="display: none">
                <div class="span12">
                <div style="font-size:16pt;margin-top:25px;margin-bottom:15px;font-weight:bold;line-height:23pt;">WTGS、WTBT</div>
				        <table style="font-size:11pt;"  id="TJGK" class="table table-condensed">
				            <thead>
				              <tr>
				                <td style="font-weight:bold;">发起部门：</td>
				                <td>FQBM</td>
				                <td style="font-weight:bold;">希望解决时间：</td>
				                <td colspan="3">XWJJSJ</td>
				            </tr>
				              <tr>
				                <td width="15%" style="font-weight:bold;">涉及项目：</td>
				                <td width="18%" >XMMC</td>
				                <td width="15%" style="font-weight:bold;">涉及标段：</td>
				                <td width="18%"  colspan="3" >BDMC</td>
				            </tr>
				          	 </thead>
				            <tbody>
				          	<tr>
				                <td height="50px" style="font-weight:bold;">议题描述：</td>
				                <td colspan="5">WTMS</td>
				            </tr>
				            <tr><td colspan="6"></td></tr>
				             </tbody>
				        </table>
				         <div style="height:25px;"></div>
                </div>
              </div>
        </div>
    </div>
</div>
</body>
</html>