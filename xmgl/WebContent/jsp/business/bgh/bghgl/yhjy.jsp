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

input[type='text'] {
	vertical-align: middle;
	height: 20px;
	line-height: 16px;
	padding: 2px;
}
.table1 {
}
.table1 tr td,.table1 tr th {
	padding-right:4px;
	line-height: 1.5em;
	height:41px;
}
.table1 tr td.YtClass {
	font-size: 16px;
	font-weight: bold;
	padding-top: 15px;
}
.table1 tr td.tdTitle {
	text-align: right;
	font-size: 15px;
	font-weight: bold;
	
}
.table1 tr td.tdContent {
	font-size: 14px;
	padding-left: 4px;
	vertical-align: text-center;
	
}
.B-small-from-table-auto h4{padding:10px;}

.titleContentClass {
	text-align: left;
	font-size: 17px;
	padding:1px
}
.boldleft{
border-left:#ccc solid 2px;
}
.boldtop{
border-top:#ccc solid 2px;
}
.boldbottom{
border-bottom:#ccc solid 2px;
}
.boldright{
border-right:#ccc solid 2px;
}
.noboldleft{
border-left:#ccc solid 1px;
}
.noboldtop{
border-top:#ccc solid 1px;
}
.noboldbottom{
border-bottom:#ccc solid 1px;
}
.noboldright{
border-right:#ccc solid 1px;
}
.nobold{
border-right:#ccc solid 1px;
border-bottom:#ccc solid 1px;
border-top:#ccc solid 1px;
border-left:#ccc solid 1px;
}
</style>
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/banGongHuiController.do"; 
	$(function()
		{
			init();
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
function  init(){
	resizeBody();
	  var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#selectrow").val();
	  var odd=convertJson.string2json1(rowValue);
	  //将数据放入表单
	  $("#GC_BGH_ID").val(odd.id);
	  $("#HYSJ").html(odd.hysj);
	  $("#HC").html(odd.name);
	  $("#HYDD").html(odd.hydd);
	  $("#HYZC").html(odd.hyzc);
	  $("#HYZT").val(odd.hyzt);
	  $("#CHBM").html(odd.chbm);
	  if("1"!=odd.hyzt){
		  $(".isjy").remove();
	  }
}
//
	function queryHyjy()
	{
		var action1 = controllername + "?queryHyj&bghid="+$("#GC_BGH_ID").val()+"";
		$.ajax({
			url : action1,
			success: function(result){
				var msg = convertJson.string2json1(result).msg;
				if(msg=="0"){
					
				}else{
					bghwt(result);
				}
			}
		});
	}
//放入表格
	function bghwt(result)
	{
		$("#bghwt").remove();
		var resultmsgobj = convertJson.string2json1(result);
		var msgStr = resultmsgobj.msg.replace(/\\n/g,"<br/>");	//保留换行
		msgStr = msgStr.replace(/\\b/g,"&nbsp;");				//保留空格
		var resultobj = convertJson.string2json1(msgStr);
		var len = resultobj.response.data.length;
		for(var i=0;i<len;i++){
			
			var subresultmsgobj = resultobj.response.data[i];
			var WTBT=$(subresultmsgobj).attr("WTBT");
			var LRBM=$(subresultmsgobj).attr("FQBM_SV");
			var XWJJSJ=$(subresultmsgobj).attr("XWJJSJ");
			var XMMC=$(subresultmsgobj).attr("XMMC");
			var BDMC=$(subresultmsgobj).attr("BDMC");
			var WTMS=$(subresultmsgobj).attr("WTMS");
			var BGHHF=$(subresultmsgobj).attr("BGHHF");
			var BGHJL=$(subresultmsgobj).attr("BGHJL");
			var ZZBM=$(subresultmsgobj).attr("ZZBM_SV");
			var ZZR=$(subresultmsgobj).attr("ZZR_SV");
			var ZZZXLD=$(subresultmsgobj).attr("ZZZXLD_SV");
			var YQJJSJ=$(subresultmsgobj).attr("YQJJSJ");
			var ISJJ=$(subresultmsgobj).attr("ISJJ_SV");
			var DBCS=$(subresultmsgobj).attr("DBCS");
			var JJSJ=$(subresultmsgobj).attr("JJSJ");
			if(LRBM==undefined){
				LRBM="";
			}if(ZZBM==undefined){
				ZZBM="";
			}if(ZZR==undefined){
				ZZR="";
			}if(ISJJ==undefined){
				ISJJ="";
			}
			if(JJSJ==undefined){
				JJSJ="";
			}if(ZZZXLD==undefined){
				ZZZXLD="";
			}
			pingTable(WTBT,LRBM,XWJJSJ,XMMC,BDMC,WTMS,BGHHF,BGHJL,ZZBM,ZZR,ZZZXLD,YQJJSJ,ISJJ,DBCS,JJSJ,i);
		}
	}
function pingTable(a,b,c,m,n,d,e,f,g,o,h,i,j,k,l,hangshu)
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
		str=str.replace( 'HYDH' , e );
		str=str.replace( 'HYJL' , f );
		str=str.replace( 'ZZBM' , g );
		str=str.replace( 'ZZR' , h );
		str=str.replace( 'ZZZXLD' , o );
		str=str.replace( 'YQJJSJ' , i );
		str=str.replace( 'ISJJ' , j );
		str=str.replace( 'DBCS' , k );
		str=str.replace( 'JJSJ' , l );
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
<body  style="background-color: #F3F2EE;">
<div class="container-fluid" style="width:800px;margin-top:10px;">
    <span class="pull-right">
   	<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
    </span>
     <div class="B-small-from-table-auto" style="border:0px;" id="printDiv">
          <h3 style="background:none; color:#333;">
          &nbsp;
        </h3>
        <div class="container-fluid" id="tablelist">
       		 <input class="span12" id="GC_BGH_ID" type="hidden"   fieldname="GC_BGH_ID" name = "GC_BGH_ID">
			<input class="span12" id="HYZT" type="hidden"   fieldname="HYZT" name = "HYZT">
              <div class="row-fluid isjy"  >
                <div style="text-align: center;border-bottom:#ccc solid 4px;padding-bottom: 20px;margin-top:20px;">
                <span style="font-size: 30px;font-weight:bold;padding:1px;" id="HC"></span>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <img src="${pageContext.request.contextPath }/images/logo_jgzx.png"/>
                </div>
                <div class="titleContentClass" style="padding-top: 20px;padding-bottom: 20px;width: 100%">
                <div style="font-size:12pt;text-align: left;padding-left:150px;">
               							  会议时间：<span id="HYSJ" ></span>
                 &nbsp;&nbsp;&nbsp;&nbsp;会议地点：<span id="HYDD" ></span>
                 &nbsp;&nbsp;&nbsp;&nbsp;会议主持：<span id="HYZC" ></span><br>
                </div>
                <div style="font-size:12pt;text-align:left;;padding-left:150px;padding-top:10px;">
                 <font style="text-align: left;">参会：<span id="CHBM" ></span></font>
                </div>
              </div>
              </div>
         <div id="bghtables" class="ismoban" style="display: none">
                <div>
                		<div style="font-size:16pt;margin-top:25px;margin-bottom:15px;font-weight:bold;line-height:23pt;">WTGS、WTBT</div>
				          <table style="font-size:11pt;"  id="TJGK" class="table table-condensed">
				        	<thead>
				            <tr>
				                <td style="font-weight:bold;">发起部门：</td>
				                <td colspan="3">FQBM</td>
				                <td style="font-weight:bold;">希望解决时间：</td>
				                <td colspan="3">XWJJSJ</td>
				            </tr>
			              <tr>
				                <td width="15%" style="font-weight:bold;">涉及项目：</td>
				                <td width="35%" colspan="3">XMMC</td>
				                <td width="15%" style="font-weight:bold;">涉及标段：</td>
				                <td width="35%"  colspan="3" >BDMC</td>
				            </tr>
				            </thead>
				            <tbody>
				          	<tr>
				                <td height="50px" style="font-weight:bold;">议题描述：</td>
				                <td colspan="7">WTMS</td>
				            </tr>
				            <tr class="isjy">
				                <td height="50px" style="font-weight:bold;">会议结论：</td>
				                <td colspan="7">HYJL</td>
				            </tr>
				           	<tr class="isjy">
				                <td  height="50px" style="font-weight:bold;">会议答复：</td>
				                <td colspan="7">HYDH</td>
				            </tr>
				          
			               <tr class="isjy">
				                <td width="14%" style="font-weight:bold;">主责部门：</td>
				                <td width="9%">ZZBM</td>
				                <td width="14%" style="font-weight:bold;">主责人：</td>
				                <td width="9%" >ZZR</td>
				                <td width="14%" style="font-weight:bold;">主责中心领导：</td>
				                <td width="12%" >ZZZXLD</td>
				                <td width="14%"  style="font-weight:bold;">督办时限：</td>
				                <td width="14%" >YQJJSJ</td>
				           </tr>
			               <tr class="isjy">
			                	<td   style="font-weight:bold;">督办次数：</td>
				                <td >DBCS</td>
				                <td  style="font-weight:bold;">是否解决：</td>
				                <td >ISJJ</td>
				                <td  style="font-weight:bold;">解决时间：</td>
				                <td colspan="3">JJSJ</td>
				           </tr>
				           <tr><td colspan="8"></td></tr>
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