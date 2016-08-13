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
<% 
	String lujing = request.getParameter("lujing");
	String mingchen = request.getParameter("mingchen");
	String tiaojian = request.getParameter("tiaojian");
	String nd = request.getParameter("nd");
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
  var controllername= "${pageContext.request.contextPath }/zsxxController.do";
  var lujing = "<%=lujing %>";
  var mingchen = "<%=mingchen%>";
  var nd = "<%=nd%>";
  var tiaojian = "<%=tiaojian%>";
  
  function setPageHeight(){
		var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
		var pageNum = parseInt(height/pageTableOne,10);
		$("#DT1").attr("pageNum",pageNum);
	}
  
	$(function() {
		setPageHeight();
		init();
		//按钮绑定事件（导出）
		$("#do_excel").click(function(){
			if(exportRequireQuery($("#DT1"))){//该方法需传入表格的jquery对象
			      printTabList("DT1","zs_rw.xls","XMBH,XMMC,QY,ZRDW,CQTHDSJ,GHXKZ,TDSYZ,MDWCRQ,LJJMZL,JMHS,LJQYZL,QYJS,LJZDMJ,ZMJ,QWTRQ,SJRQ,CDYJRQ,TDFWYJRQ,WTJFX","3,1");
			  }
		});
	});
   function init() {
	   var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
	   //调用ajax插入
	   defaultJson.doQueryJsonList(controllername+"?queryZsxx&lujing="+lujing+"&mingchen="+mingchen+"&nd="+nd+"&tiaojian="+tiaojian,data,DT1,null,true);
   };
    
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
        </tr>
      </table>
      </form>
		<div style="height:5px;"> </div>		
          	<div class= "overFlowX">
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH" rowspan="2" colindex=1>&nbsp;#&nbsp;</th>
                    		<th fieldname="XMBH" maxlength="15" rowMerge="true" hasLink="true" linkFunction="rowView" rowspan="2" colindex=2>&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" rowspan="2" colindex=3 rowMerge="true" maxlength="15">&nbsp;项目名称&nbsp;</th>
							<th fieldname="QY" rowspan="2" colindex=4>&nbsp;区域&nbsp;</th>
							<th fieldname="ZRDW" rowspan="2" colindex=5 maxlength="15">&nbsp;负责单位&nbsp;</th>
							<!-- <th fieldname="GHSJ" tdalign="center" rowspan="2" colindex=5>用地规划<p></p>许可证&nbsp;</th>
							<th fieldname="TDSJ" tdalign="center" rowspan="2" colindex=6>&nbsp;土地使用证&nbsp;</th> -->
							<th fieldname="CQTHDSJ" tdalign="center" rowspan="2" colindex=6>拆迁图<p></p>取得时间&nbsp;</th>
							<th fieldname="GHXKZ" tdalign="center" rowspan="2" colindex=7 CustomFunction="showGh">用地规划<p></p>许可证&nbsp;</th>
							<th fieldname="TDSYZ" tdalign="center" rowspan="2" colindex=8 CustomFunction="showTd">&nbsp;土地使用证&nbsp;</th>
							<th colspan="2">摸底信息</th>
							<th colspan="2">拆迁居民数</th>
							<th colspan="2">拆迁企业数</th>
							<th colspan="2">征地面积</th>
							<th fieldname="QWTRQ" tdalign="center" rowspan="2" colindex=17>区委托协议签订<p></p>日期&nbsp;</th>
							<th fieldname="SJRQ" tdalign="center" rowspan="2" colindex=18>&nbsp;实施日期&nbsp;</th>
							<th fieldname="CDYJRQ" tdalign="center" rowspan="2" colindex=19>场地移交<p></p>日期&nbsp;</th>
							<th fieldname="TDFWYJRQ" tdalign="center" rowspan="2" colindex=20>土地房屋移交<p></p>日期&nbsp;</th>
							<th fieldname="WTJFX" rowspan="2" colindex=21 maxlength="15">&nbsp;问题及风险&nbsp;</th>
						</tr>
						<tr>
							<th fieldname="MDWCRQ" tdalign="center" colindex=9>完成日期&nbsp;</th>
							<th fieldname="MDFJ" tdalign="center" colindex=10 noprint="true">附件信息&nbsp;</th>
							<th fieldname="LJJMZL" tdalign="right" colindex=11>&nbsp;已完成&nbsp;</th>
							<th fieldname="JMHS" tdalign="right" colindex=12>&nbsp;总量&nbsp;</th>
							<th fieldname="LJQYZL" tdalign="right" colindex=13>&nbsp;已完成&nbsp;</th>
							<th fieldname="QYJS" tdalign="right" colindex=14>&nbsp;总量&nbsp;</th>
							<th fieldname="LJZDMJ" tdalign="right" colindex=15>&nbsp;已完成&nbsp;</th>
							<th fieldname="ZMJ" tdalign="right" colindex=16>&nbsp;总量&nbsp;</th>
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