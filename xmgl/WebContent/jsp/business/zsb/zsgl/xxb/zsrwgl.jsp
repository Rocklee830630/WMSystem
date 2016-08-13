<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%
	User user=(User) request.getSession().getAttribute(Globals.USER_KEY);
	String account=user.getAccount();
%>
<app:base />
<title>拦标价维护</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
  var oTable1;
  var zdxxid,jmcqzl;
  var rowindex,rowValue,text,aa;
  var index;//全局行选变量
  var hang;
  var controllername= "${pageContext.request.contextPath }/zsb/xxb/xxbController.do";

  
  function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
  
	$(function() {
		setPageHeight();
		init();
		tr_select();
		var clean=$("#clean");
		clean.click(function(){
			$("#queryForm").clearFormResult();
			nd($("#ND"));
		});
		var btn = $("#query");
		btn.click(function() {
			//生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
			//alert("data:"+data);
			defaultJson.doQueryJsonList(controllername+"?queryXxb",data,DT1,null,false);
			index=$("DT1").getSelectedRowIndex();
				});
		
		//征收任务分配
		var rwfp=$("#rwfp");
		rwfp.click(function() {
			$(window).manhuaDialog({"title":"征收任务管理>任务分配","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xxb/rwfp.jsp","modal":"2"});
			/* if(index==-1){
				parent.$("body").manhuaDialog({"title":"征收任务管理>任务分配","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xxb/rwfp.jsp?index="+index,"modal":"2"});
			}else{
				parent.$("body").manhuaDialog({"title":"征收任务管理>任务分配","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xxb/rwfp.jsp?json="+json+"&index="+index,"modal":"2"});
			} */
			//parent.$("body").manhuaDialog({"title":"征地进度>征收任务分配","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xxb/rwfp.jsp?json="+json+"&index="+index,"modal":"2"});
		});
		//委托协议
		var wtxy=$("#wtxy");
		wtxy.click(function() {
			var row_index =	$("#DT1").getSelectedRowIndex();
			if(row_index ==-1) {
				requireSelectedOneRow();
			}  else {
				$(window).manhuaDialog({"title":"征收任务管理>委托协议","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xxb/wtxy.jsp","modal":"2"});
			}
		});
		//摸底信息
		var mdxx=$("#mdxx");
		mdxx.click(function() {
			var row_index =	$("#DT1").getSelectedRowIndex();
			if(row_index ==-1) {
				requireSelectedOneRow();
			}  else {
				$(window).manhuaDialog({"title":"征收任务管理>摸底信息","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xxb/mdxx.jsp","modal":"2"});
			 }	
		});
		//任务维护
		var rwwh=$("#rwwh");
		rwwh.click(function() {
			var row_index =	$("#DT1").getSelectedRowIndex();
			if(row_index ==-1) {
				requireSelectedOneRow();
			}  else {	
				$(window).manhuaDialog({"title":"征收任务管理>任务维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsgl/xxb/rwwh.jsp","modal":"1"});
			}
		});
		//征收进展
		var zsjz=$("#zsjz");
		zsjz.click(function() {
			var row_index =	$("#DT1").getSelectedRowIndex();
			if(row_index ==-1) {
				requireSelectedOneRow();
			}  else {
				var row_value=$("#DT1").getSelectedRow();
				row_value=convertJson.string2json1(row_value);
				var mdwcrq=$(row_value).attr("MDWCRQ");
				if(""==mdwcrq||null==mdwcrq){
					xFailMsg("请先录入征收摸底信息！","");
					return;
				}else{
					$(window).manhuaDialog({"title":"征收任务管理>征收进展","type":"text","content":"${pageContext.request.contextPath}/jsp/business/zsb/zsjd/zsjz.jsp","modal":"1"});
				}
			}
		});
		//按钮绑定事件（导出）
		$("#do_excel").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      printTabList("DT1","zs_rw.xls","XMBH,XMMC,QY,ZRDW,CQTHDSJ,GHXKZ,TDSYZ,MDWCRQ,LJJMZL,JMHS,LJQYZL,QYJS,LJZDMJ,ZMJ,QWTRQ,SJRQ,CDYJRQ,TDFWYJRQ,WTJFX","3,1");
			  }
		});
		//自动完成项目名称模糊查询
		showAutoComplete("XMMC",controllername+"?xmmcAutoQuery","getXmmcQueryCondition");
	});
	function tr_select(){
		$("#DT1").setSelect(-1);
		index=$("#DT1").getSelectedRowIndex();
	}
	function tr_click(obj){
		zdxxid=$(obj).attr("ZDXXID");
		id=$(obj).attr("GC_ZSB_XXB_ID");
		rowValue = $("#DT1").getSelectedRow();
		json=encodeURI(rowValue);
		index =	$("#DT1").getSelectedRowIndex();
	}
   function init() {
	   nd($("#ND"));
	   //g_bAlertWhenNoResult=false;	
	   var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	   //调用ajax插入
	   defaultJson.doQueryJsonList(controllername+"?queryXxb",data,DT1,null,true);
	   //g_bAlertWhenNoResult=true;
   };
   function ready(hang) {
	   if(hang==1){
			//g_bAlertWhenNoResult=false;	
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			var tempJson=convertJson.string2json1(data);
			var page=$("#DT1").getCurrentpagenum();
			tempJson.pages.currentpagenum=page;
			data = JSON.stringify(tempJson);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryXxb",data,DT1,null,true);
			//g_bAlertWhenNoResult=true;
			rowValue='';
			json='';
			index='';
			xSuccessMsg("操作成功","");
	   }else{
	   	   $("#queryForm").clearFormResult();
		   init();
		   /* $("#DT1").setSelect(hang);
		   index=$("#DT1").getSelectedRowIndex();
		   rowValue = $("#DT1").getSelectedRow();
		   json=encodeURI(rowValue); */
		   xSuccessMsg("操作成功","");
	   	}
   };
    
	//子页面修改，父页面查询
	function xgh(data)
	{
		//var index =	$("#DT1").getSelectedRowIndex();
		var subresultmsgobj = defaultJson.dealResultJson(data);
		var comprisesJson = $("#DT1").comprisesJson(subresultmsgobj,index);
		$("#DT1").updateResult(JSON.stringify(comprisesJson),DT1,index);
		$("#DT1").cancleSelected();
		$("#DT1").setSelect(index);
		xSuccessMsg("操作成功","");
	}
	
	//答疑弹出页回显修改
	function jzfk(xxbid)
	{	
		$.ajax(
		{
			   url : controllername+"?queryXxb",//此处定义后台controller类和方法
		         data : {xxbid:xxbid},    //此处为传入后台的数据，可以为json，可以为string，如果为json，那起结构必须和后台接收的bean一致或和bean的get方法名一致，例如｛id：1，name：2｝后台接收的bean方法至少包含String id,String name方法  如果为string，那么可以写为{portal: JSON.stringify(data)}, 后台接收的时候参数可以为String，名字必须和前台保持一致及定义为String portal
		         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
		         async : false,   //同步执行，即执行完ajax方法后才可以执行下面的函数，如果不设置则为异步执行，及ajax和其他函数是异步的，可能后面的代码执行完了，ajax还没执行
		         success : function(result) {
		         var resultmsg = result.msg; //返回成功事操作
		      	 //var index= $("#DT1").getSelectedRowIndex();
				 var subresultmsgobj1 = defaultJson.dealResultJson(resultmsg);
				 $("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
				 $("#DT1").setSelect(index);
		         },
		         error : function(result) {//返回失败操作
		           defaultJson.clearTxtXML();
		          }			
		});
		}
	
	//年度默认为征收任务表中的年度
	function nd(ndObj){
		ndObj.attr("src","T#GC_ZSB_XXB: distinct ND:ND:SFYX='1'");
		ndObj.attr("kind","dic");
		ndObj.html('');
		reloadSelectTableDic(ndObj);
		//setDefaultOption(ndObj,new Date().getFullYear());
		setDefaultNd("ND");
	}
	//生成项目名称查询条件
	function getXmmcQueryCondition(){
		var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"xmmc"}]}';
		var jsonData = eval('(' + initData + ')'); 
		//项目名称
		if("" != $("#XMMC").val()){
			var defineCondition = {"value": "%"+$("#XMMC").val()+"%","fieldname":"jhsj.XMMC","operation":"like","logic":"and"};
			jsonData.querycondition.conditions.push(defineCondition);
		}
	  //年度
		if("" != $("#ND").val()){
			var defineCondition = {"value": $("#ND").val(),"fieldname":"jhsj.ND","operation":"=","logic":"and"};
			jsonData.querycondition.conditions.push(defineCondition);
		}
		return JSON.stringify(jsonData);
	}
	
	//规划显示
	function showGh(obj){
		var mark=obj.MARK;
		var date_gh =obj.GHXKZ;
		var sf_gh=obj.SFGH;
		var gh_show=opera_sx(date_gh,sf_gh,mark);
		return gh_show;
	}
	//土地显示
	function showTd(obj){
		var mark=obj.MARK;
		var date_td =obj.TDSYZ;
		var sf_td=obj.SFTD;
		var td_show=opera_sx(date_td,sf_td,mark);
		return td_show;
	}
	//显示操作
	function opera_sx(date,sf,mark){
		var show;
		if(mark==null||mark==""){
			show='<div style="text-align:center"><i title="未办理" class=\"icon-yellow\"></i></div>';
		}else{
			if(sf==""||sf==null){
				show=date?date:('<div style="text-align:center"><i title="未办理" class=\"icon-yellow\"></i></div>');
			}else{
				show='<div style="text-align:center">—</div>';
			}
		}
		return show;
	}
	  //详细信息
    function rowView(aa){
        var obj=$("#DT1").getSelectedRowJsonByIndex(aa);//获取行对象
    	var id=convertJson.string2json1(obj).XMID;
    	$(window).manhuaDialog(xmscUrl(id));//调用公共方法,根据项目编号查询
    }
</script>
</head>
<body>
<app:dialogs/>
	<div class="container-fluid">
		<p></p>
	<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">征收任务管理<span class="pull-right">
      			<app:oPerm url="jsp/business/zsb/zsgl/xxb/rwfp.jsp">
	         		 <button id="rwfp" class="btn"  type="button">任务分配</button>
      			</app:oPerm>
        		<app:oPerm url="jsp/business/zsb/zsgl/xxb/wtxy.jsp">
	         		 <button id="wtxy" class="btn"  type="button">委托协议</button>
      			</app:oPerm>
         		<app:oPerm url="jsp/business/zsb/zsgl/xxb/mdxx.jsp">
	         		 <button id="mdxx" class="btn"  type="button">摸底信息</button>
      			</app:oPerm>
         		<app:oPerm url="jsp/business/zsb/zsjd/zsjz.jsp">
	         		 <button id="zsjz" class="btn"  type="button">征收进展</button>
      			</app:oPerm>
         		<app:oPerm url="jsp/business/zsb/zsgl/xxb/rwwh.jsp">
	         		 <button id="rwwh" class="btn"  type="button">任务维护</button>
      			</app:oPerm>
      				<button id="do_excel" class="btn"  type="button">导出</button>
          		</span></h4>
     <form method="post" id="queryForm" width="100%">
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">	
				<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" keep="true"/>
				<INPUT id="num" type="text" class="span12" kind="text" fieldname="xxb.fzr" value="<%=account %>" operation="=" keep="true"/>
			</TD>
        </TR>
        <!--可以再此处加入hidden域作为过滤条件 -->
        <tr>
        <!--公共的查询过滤条件 -->
         <th width="5%" class="right-border bottom-border text-right">年度</th>
         <td class="right-border bottom-border">
            <select class="span12 year" type="text" id="ND" name="ND" fieldname="jh.ND" kind="dic" src="XMNF" operation="=" defaultMemo="全部">
            </select>
          </td>
          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
         <td class="right-border bottom-border">
            <input class="span12 xmmc" type="text" id="XMMC" name="XMMC" fieldname="JH.XMMC" operation="like" autocomplete="off"/>
          </td>
         
        <th width="5%" class="right-border bottom-border text-right">区域</th>
         <td width="15%" class="right-border bottom-border">
            <select class="span12 10characters" name = "QQY" fieldname = "xxb.QY" operation="=" id="QY" kind="dic" src="QY" defaultMemo ="全部">
            </select>
          </td>
        <td width="50%" class="text-left bottom-border text-right">
			<button id="query" class="btn btn-link" type="button"><i class="icon-search"></i>查询</button>
	        <button id="clean" class="btn btn-link"   type="button"><i class="icon-trash"></i>清空</button>
		</td>	
        </tr>
      </table>
      </form>
		<div style="height:5px;"> </div>		
          	<div class= "overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" printFileName="征收任务管理">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
                    		<th fieldname="XMBH" maxlength="15" rowMerge="true" hasLink="true" linkFunction="rowView" rowspan="2" colindex=2>&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" rowspan="2" colindex=3 rowMerge="true" maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="QY" rowspan="2" colindex=4>&nbsp;区域&nbsp;</th>
							<th fieldname="ZRDW" rowspan="2" colindex=5 maxlength="15">&nbsp;负责单位&nbsp;</th>
							<th fieldname="XMBDDZ" rowspan="2" colindex=6 maxlength="15">&nbsp;项目地址&nbsp;</th>
							<!-- <th fieldname="GHSJ" tdalign="center" rowspan="2" colindex=5>用地规划<p></p>许可证&nbsp;</th>
							<th fieldname="TDSJ" tdalign="center" rowspan="2" colindex=6>&nbsp;土地使用证&nbsp;</th> -->
							<th fieldname="CQTHDSJ" tdalign="center" rowspan="2" colindex=7>拆迁图<p></p>取得时间&nbsp;</th>
							<th fieldname="GHXKZ" tdalign="center" rowspan="2" colindex=8 CustomFunction="showGh">用地规划<p></p>许可证&nbsp;</th>
							<th fieldname="TDSYZ" tdalign="center" rowspan="2" colindex=9 CustomFunction="showTd">&nbsp;土地使用证&nbsp;</th>
							<th colspan="2">摸底信息</th>
							<th colspan="2">拆迁居民数</th>
							<th colspan="2">拆迁企业数</th>
							<th colspan="2">征地面积</th>
							<th fieldname="QWTRQ" tdalign="center" rowspan="2" colindex=18>区委托协议签订<p></p>日期&nbsp;</th>
							<th fieldname="SJRQ" tdalign="center" rowspan="2" colindex=19>&nbsp;实施日期&nbsp;</th>
							<th fieldname="CDYJRQ" tdalign="center" rowspan="2" colindex=20>场地移交<p></p>日期&nbsp;</th>
							<th fieldname="TDFWYJRQ" tdalign="center" rowspan="2" colindex=21>土地房屋移交<p></p>日期&nbsp;</th>
							<th fieldname="WTJFX" rowspan="2" colindex=22 maxlength="15">&nbsp;问题及风险&nbsp;</th>
						</tr>
						<tr>
							<th fieldname="MDWCRQ" tdalign="center" colindex=10>完成日期&nbsp;</th>
							<th fieldname="MDFJ" tdalign="center" colindex=11 noprint="true">附件信息&nbsp;</th>
							<th fieldname="LJJMZL" tdalign="right" colindex=12>&nbsp;已完成&nbsp;</th>
							<th fieldname="JMHS" tdalign="right" colindex=13>&nbsp;总量&nbsp;</th>
							<th fieldname="LJQYZL" tdalign="right" colindex=14>&nbsp;已完成&nbsp;</th>
							<th fieldname="QYJS" tdalign="right" colindex=15>&nbsp;总量&nbsp;</th>
							<th fieldname="LJZDMJ" tdalign="right" colindex=16>&nbsp;已完成&nbsp;</th>
							<th fieldname="ZMJ" tdalign="right" colindex=17>&nbsp;总量&nbsp;</th>
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
			<input type="hidden" name="txtFilter"  order="desc" fieldname = "xxb.xmid,xxb.lrsj"	id = "txtFilter">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
			<input type="hidden" name="queryResult" id="queryResult"/>
		</FORM>
	</div>
</body>
</html>