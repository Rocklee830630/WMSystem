<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String prefix = request.getParameter("prefix");
	boolean isPre = (null == prefix || "".equals(prefix));
	String proPrefix = isPre?"":prefix;
	prefix = isPre?"":(prefix+".");
%>
<th width="30" class="right-border bottom-border text-right">年度</th>
<td class="right-border bottom-border" width="8%"><select
	class="span12 year" id="QueryND" name="QueryND" fieldname="<%=prefix%>ND" operation="="
	defaultMemo="全部">
	<input style="display:none;" class="span12" type="text" id="QXMSX" name="QXMSX" fieldname="<%=prefix%>XMSX" operation="="/>
	<input style="display:none;" class="span12" type="text" id="QISXF" name="QISXF" fieldname="<%=prefix%>ISXF" operation="=" value="" keep="true"/>
</select></td>
<th width="50" class="right-border bottom-border text-right">全部项目</th>
<td class="right-border bottom-border text-center" width="70"><label
	class="checkbox inline" style="font-weight: bold;"> <input
		type="checkbox" id="QuerySyfyj" name="QuerySyfyj" class="text-right" />非应急
</label></td>
<td class="right-border bottom-border text-center" width="50"><label
	class="checkbox inline" style="font-weight: bold;"> <input
		type="checkbox" id="QuerySyyj" name="QuerySyyj" class="text-right" />应急
</label></td>
<th width="3%" class="right-border bottom-border text-right">批次</th>
<td class="right-border bottom-border"><select
	class="span12 4characters" id="QueryPc" name="QueryPc" fieldname="<%=prefix%>JHID"
	operation="=" defaultMemo="全部">
</select></td>
<th width="6%" class="right-border bottom-border text-right">项目名称</th>
<td class="right-border bottom-border"><input
	class="span12 xmmc" type="text" placeholder="" name="QXMMC"
	fieldname="<%=prefix%>XMMC" operation="like" id="QXMMC" autocomplete="off"
	tablePrefix="<%=proPrefix%>"></td>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>	
<script type="text/javascript" charset="utf-8">
$(function() {
	//自动完成项目名称模糊查询
	var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";
	showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQuery","getXmmcQueryCondition"); 
	initCommonQueyPage();

	//checkbox绑定事件(所有批次)
    $("#QuerySyfyj").click(function() {
    	if(this.checked){
    		makePcAll();
    	}
    	combineYjFyj();
    });
  	//checkbox绑定事件(所有应急)
    $("#QuerySyyj").click(function() {
    	if(this.checked){
    		makePcAll();
    	}
    	combineYjFyj();
    });
    //批次选定事件
    $("#QueryPc").change(function() {
    	if("" != $(this).val()){
    		$("#QuerySyfyj").attr("checked",false);
    		$("#QuerySyyj").attr("checked",false);
    	}
    	combineYjFyj();
    });
	
})


	function clearYjFyj(){
		$("#QuerySyfyj").attr("checked",false);
		$("#QuerySyyj").attr("checked",false);
		makePcAll();
		combineYjFyj();
	}
	
	function initCommonQueyPage(){
		clearYjFyj();
        generateJhNdMc($("#QueryND"),$("#QueryPc")); 
	}
	/**
	 * 根据全部应急还是全部非应急的选择，在原有的页面查询条件基础上增加查询条件
	 * @param data 页面原有的查询条件
	 * @param prefix 计划数据表的查询自定义前缀 
	 */
	function combineYjFyj(){
		var xmsx = "1";//正常
		if($("#QuerySyyj").is(':checked') && $("#QuerySyfyj").is(':checked')){
			xmsx = "";
		}else if(!$("#QuerySyyj").is(':checked') && !$("#QuerySyfyj").is(':checked')){
			xmsx = "";
		}else if($("#QuerySyyj").is(':checked')){
			xmsx = "2";
		}
		/**
		var defineCondition = {"value": xflx,"fieldname":"xflx","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
		return JSON.stringify(jsonData);
		**/
		$("#QXMSX").val(xmsx);
	}
	
	/**
	 * 业务通用年度和计划名称的级联查询
	 * ndObj 年度的jquery对象
	 * mcObj 计划名称jquery对象
	 */
	function generateJhNdMc(ndObj,mcObj){
		ndObj.attr("src","T#GC_JH_SJ: distinct ND as NDCODE:ND:SFYX='1' ORDER BY NDCODE asc");
		ndObj.attr("kind","dic");
		ndObj.html('');
		reloadSelectTableDic(ndObj);
		//ndObj.val(new Date().getFullYear());
		//setDefaultOption(ndObj,new Date().getFullYear());
		//modify by zhangbr@ccthanking.com	修改年度默认条件
		setDefaultNd("QueryND");
		if(mcObj){
			mcObj.attr("kind","dic");
			mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and isxf = '1' ORDER BY jhpch ASC");
			mcObj.html('');
			reloadSelectTableDic(mcObj);
			ndObj.change(function() {
				mcObj.html('');
				if(!ndObj.val().length){
				}
				mcObj.attr("src","T#GC_JH_ZT:GC_JH_ZT_ID:jhpch:SFYX='1' AND ND='" + ndObj.val()+"' and isxf = '1' ORDER BY jhpch ASC");
				reloadSelectTableDic(mcObj);
			});
		}
	}


	function makePcAll(){
		$("#QueryPc").val('');
	}
	
	
	function getXmmcQueryCondition(){
		var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"<%=prefix%>pxh"}]}';
		var jsonData = eval('(' + initData + ')'); 
		//年度
		if("" != $("#QueryND").val()){
			var defineCondition = {"value": $("#QueryND").val(),"fieldname":"<%=prefix%>ND","operation":"=","logic":"and"};
			jsonData.querycondition.conditions.push(defineCondition);
		}
		//批次
		if("" != $("#QueryPc").val()){
			var defineCondition = {"value": $("#QueryPc").val(),"fieldname":"<%=prefix%>JHID","operation":"=","logic":"and"};
			jsonData.querycondition.conditions.push(defineCondition);
		}
		//项目名称
		if("" != $("#QXMMC").val()){
			var defineCondition = {"value": "%"+$("#QXMMC").val()+"%","fieldname":"<%=prefix%>XMMC","operation":"like","logic":"and"};
			jsonData.querycondition.conditions.push(defineCondition);
		}
		//下发类型
		if("" != $("#QXMSX").val()){
			var defineCondition = {"value": $("#QXMSX").val(),"fieldname":"<%=prefix%>XMSX","operation":"=","logic":"and"};
			jsonData.querycondition.conditions.push(defineCondition);
		}
		//是否下发
		if("" != $("#QISXF").val()){
			var defineCondition = {"value": $("#QISXF").val(),"fieldname":"<%=prefix%>ISXF","operation":"=","logic":"and"};
			jsonData.querycondition.conditions.push(defineCondition);
		}
		return JSON.stringify(jsonData);
	}
	
</script>
