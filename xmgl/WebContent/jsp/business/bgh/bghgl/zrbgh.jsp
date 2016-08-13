<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath }/jsp/char/charts/FusionCharts.js"></script>
<%@ page import="com.ccthanking.framework.common.DBUtil"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="com.ccthanking.framework.util.Pub"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<%
	String sysyf = Pub.getDate("MM");
	String sysnf = Pub.getDate("yyyy");
	String type= request.getParameter("type")==null?"":request.getParameter("type");
	String id = request.getParameter("id")==null?"":request.getParameter("id");
	String hysj = request.getParameter("hysj")==null?"":request.getParameter("hysj");
	String hysj_year = "";
	String hysj_month = "";
	if(hysj!=""){
		hysj_year = hysj.substring(0,hysj.indexOf("年"));
		hysj_month = "00"+hysj.substring(hysj.indexOf("年")+1,hysj.indexOf("月"));
		hysj_month = hysj_month.substring(hysj_month.length()-2,hysj_month.length());
	}
%>
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
var date="<%=sysyf%>";
var sysnf="<%=sysnf%>";
var p_type = "<%=type%>";
var p_id = "<%=id%>";
var p_hysj_year = "<%=hysj_year%>";
var p_hysj_month = "<%=hysj_month%>";
	//页面高度
	function resizeBody(){
		if(window.screen.width>800){
			try{
				document.body.style.marginLeft = ((window.screen.width-800)/2 - 15)+"px";
			}catch(e){}
		}else{
			document.body.style.marginLeft = "0px";
		}
	}
	//页面加载
	$(function() {
		getNd();
		if(p_type==""){//空表示从快捷入口进入的
			generateYF($("#YF"));
			queryZXHC(1);
		}else if(p_type=="2"){//2表示从首页列表标题超链接进入的
			//alert(p_hysj_year+"|"+p_hysj_month);
			$(".checkbox").hide();
			$("#ND").val(p_hysj_year);
			$("#YF").val(p_hysj_month);
			$("#HCID").val(p_id);
			generateYF($("#YF"));
			generatePc($("#HCID"));
			queryZXHC("2");
		}
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
	function  init(){
		queryZXHC(2);
	}
	//查询会次
	function queryZXHC(zx)//1是查询最新2 是根据下拉
	{
		var tiaojian="";
		if("2"==zx){
			tiaojian=$("#HCID").val();
		}
		var action1 = controllername + "?queryZXHC&bghid="+tiaojian+"";
		$.ajax({
			url : action1,
			success: function(result)
			{
				var resultmsgobj = convertJson.string2json1(result);
				if(resultmsgobj.msg=="0"){
					$("#tablelist").hide();
				}else{
					$("#tablelist").show();
					var msgStr = resultmsgobj.msg.replace(/\\n/g,"<br/>");	//保留换行
					msgStr = msgStr.replace(/\\b/g,"&nbsp;");				//保留空格
					var resultobj = convertJson.string2json1(msgStr);
					if('0'==resultobj){
						return ;
					}
					var subresultmsgobj = resultobj.response.data[0];
					$("#GC_BGH_ID").val($(subresultmsgobj).attr("GC_BGH_ID"));
					$("#HYSJ").html($(subresultmsgobj).attr("HYSJ"));
					$("#HC").html($(subresultmsgobj).attr("HC"));
					$("#HYDD").html($(subresultmsgobj).attr("HYDD"));
					$("#HYZC").html($(subresultmsgobj).attr("HYZC"));
					$("#HYZC").html($(subresultmsgobj).attr("HYZC"));
					$("#CHBM").html($(subresultmsgobj).attr("CHBM"));
					setDefaultOption($("#HCID"),$(subresultmsgobj).attr("GC_BGH_ID"));
					iswc($(subresultmsgobj).attr("HYZT"));
				}
			}
		});
	}
	//判断是否完成
	function iswc(zt){
	 	  if("1"!=zt){
			  $(".isjy").hide();
		  }else{
			  $(".isjy").show();  
		  }
	 	  //查询会议下的议题
		queryHyjy(); 
	}
	//批次切换查询
	function queryHC(obj){
		var hc=$(obj).val();
		if(""==hc){
			//清空表单
			$(".zengjiatable").remove();
			$(".isjy").hide();
			xAlert("提示信息",'请选择会次','3');
		}else{
			init();
		}
	}
	//默认年度
	function getNd(){
		setDefaultNd("ND");
	} 
	//月份下拉查询
	function generateYF(pcObj){
		var year=$("#ND").val();
		var yuefen="T#GC_BGH:distinct to_char(lrsj,'MM') : to_char(lrsj,'MM'):TO_CHAR(LRSJ,'yyyy')='"+year+"' order by to_char(lrsj,'MM')";
		pcObj.attr("src",yuefen);
		pcObj.attr("kind","dic");
		reloadSelectTableDic(pcObj);
		setDefaultYf(pcObj);
		generatePc($("#HCID"));
	}
	//会次下拉查询
	function generatePc(pcObj){
		var year=$("#ND").val();
		var yuefen="T#GC_BGH A,(select count(GC_BGH_WT_ID) WTSL,BGHID from GC_BGH_WT where SFGK='1' and SFYX='1' group by BGHID) B:";
		yuefen += "GC_BGH_ID:HC:TO_CHAR(A.LRSJ,'yyyy-MM')='"+year+"-"+$("#YF").val()+"' and A.sfyx='1' and A.GC_BGH_ID=B.BGHID and B.WTSL>0 order by A.hysj asc";
		pcObj.attr("src",yuefen);
		pcObj.attr("kind","dic");
		pcObj.html('');
		reloadSelectTableDic(pcObj);
		setDefaultPc(pcObj);
	}
	//年份切换
	function queryYear(){
		generateYF($("#YF"));
		queryYue($("#YF"));
	}
	//月份切换
	function queryYue(obj){
		//清空表单
		$(".zengjiatable").remove();
		$(".isjy").hide();
		generatePc($("#HCID"));
	}
	//默认月份
	function setDefaultYf(n){
		var obj = n;
		reloadSelectTableDic(obj);
		var sysYear = p_hysj_month==""?12:p_hysj_month;//如果有传入到页面的月份，那么使用传入的月份，不然从12月开始
		var len = obj.find('option').length;
		//遍历select，如果选项中没有当前月，那么月份减1，直到找出存在的月份选项
		var existYear = "";
		for(var x=sysYear;x>0;x--){
			var hasValueFlag = false;
			obj.find("option").each(function(){
				if($(this).text()==x){
					hasValueFlag=true;
					return false;
				}
			});
			if(hasValueFlag==true){
				existYear=x;
				break;
			}
		}
		var existYear1=existYear+"";
		if("1"==existYear1.length){
			existYear1="0"+existYear1;
		}
		obj.val(existYear1);
	}
	//设置“会次”默认值
	function setDefaultPc(n){
		var obj = n;
		if(p_id==""){
			//do nothing
			//如果没有传入的ID值，那么不需要设置默认选中项
		}else{
			//如果有传入的ID值，那么使该会次被选中
			obj.val(p_id);
		}
	}
	//查询会议纪要
	function queryHyjy()
	{
		var action1 = controllername + "?queryHyj&bghid="+$("#HCID").val()+"";
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
		$(".zengjiatable").remove();
		var resultmsgobj = convertJson.string2json1(result);
		var msgStr = resultmsgobj.msg.replace(/\\n/g,"<br/>");	//保留换行
		msgStr = msgStr.replace(/\\b/g,"&nbsp;");				//保留空格
		var resultobj = convertJson.string2json1(msgStr);
		if('0'==resultobj){
			xAlert("警告","会议下没有议题","3"); 
			return ;
		}
		var len = resultobj.response.data.length;
		for(var i=0;i<len;i++){
			
			var subresultmsgobj = resultobj.response.data[i];
			var WTBT=$(subresultmsgobj).attr("WTBT");
			var LRBM=$(subresultmsgobj).attr("FQBM_SV");
			var XWJJSJ=$(subresultmsgobj).attr("XWJJSJ");
			var WTMS=$(subresultmsgobj).attr("WTMS");
			var XMMC=$(subresultmsgobj).attr("XMMC");
			var BDMC=$(subresultmsgobj).attr("BDMC");
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
			str=str.replace( 'WTMS' , d );
			str=str.replace( 'HYDH' , e );
			str=str.replace( 'XMMC' , m );
			str=str.replace( 'BDMC' , n );
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
			str=str.replace( 'ismoban"' , 'zengjiatable" ');
		 $("#tablelist").append(str); 
	}
	function queryMore(){
		$(window).manhuaDialog({"title":"会议中心","type":"text","content":"${pageContext.request.contextPath}/jsp/business/bgh/bghgl/zrbghgd.jsp","modal":"1"});
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
<style>

</style>
</head>
<body style="background-color: #F3F2EE;">
<div class="container-fluid" style="width:800px;margin-top:10px;">
     <div class="B-small-from-table-auto" style="border:0px;" id="printDiv">
        <h3 id="queryH">
        	<label class="checkbox inline" style="white-space:nowrap;">
        		<span style="font-size: 14px;padding-bottom:15px;vertical-align: text-center;">年度&nbsp;</span>
	        		<select id="ND" style="width: 13%;" class="span2" name="ND" onchange="queryYear(this)"  noNullSelect ="true" src="T#GC_BGH: DISTINCT TO_CHAR(LRSJ,'yyyy') ND:TO_CHAR(LRSJ,'yyyy') :1=1 ORDER BY ND"   operation="=" ></select>
	        	<span style="font-size: 14px;padding-bottom:15px;vertical-align: text-center;">月份&nbsp;</span>
	        		<select id="YF"  class="span1" name="YF" onchange="queryYue(this)"  noNullSelect ="true" fieldname="YF" operation="=" ></select>
		   		<span style="font-size: 14px;padding-bottom:15px;vertical-align: text-center;">会次&nbsp;</span>
		   			<select id="HCID"  class="span3" name="HCID" onchange="queryHC(this)" fieldname="HCID"  operation="=" ></select>
	         	<a href="javascript:void(0);" onclick="queryMore()" style="padding: 10px"><font >查询更多</font></a>
        	</label>
	    <span class="pull-right">
			   	<button id="printButton" class="btn btn-link" type="button"><i class="icon-print"></i>打印</button>
	    </span>
        </h3>
        <div class="container-fluid" id="tablelist">
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
              </div></div>
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
</div></div>
</body>
</html>