<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<title>项目选择</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
var controllername= "${pageContext.request.contextPath }/zsb/xmb/xmbController.do";
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do";


//自定义的获取页面查询条件
function getQueryCondition(){
	var data = combineQuery.getQueryCombineData(queryForm,frmPost,xdxmkList);
    return data;
}
//设置每页显示信息数量
function setPageHeight(){
	var height = g_iHeight-pageTopHeight-pageTitle-pageQuery-getTableTh(1)-pageNumHeight;
	var pageNum = parseInt(height/pageTableOne,10);
	$("#xdxmkList").attr("pageNum",pageNum);
}

function queryList(){
	//生成json串
	var data = getQueryCondition();
	//调用ajax插入
	defaultJson.doQueryJsonList(controllername+"?query",data,xdxmkList);
}

//页面初始化
$(function() {
	//setPageHeight();
	initPage($("#ND"));
	queryList();
	//按钮绑定事件（查询）
	$("#btnQuery").click(function() {
		queryList();
	});
	//按钮绑定事件（清空）
    $("#btnClear").click(function() {
        $("#queryForm").clearFormResult();
        //其他处理放在下面
        initPage($("#ND"));
    });
  	//按钮绑定事件（确定）
    $("#btnQd").click(function() {
    	if($("#xdxmkList").getSelectedRowIndex()==-1)
		 {
    		requireSelectedOneRow();
		    return;
		 }
        var rowValue = $("#xdxmkList").getSelectedRow();//获得选中行的json对象
        $(window).manhuaDialog.setData(rowValue);
        $(window).manhuaDialog.sendData();
        $(window).manhuaDialog.close();
    });
  	//两项查询
    showAutoComplete("XMMC",autocompleteXmmcController+"?xmmcAutoQuery","getXmmcQueryCondition");
});

function initPage(ndObj){
	ndObj.attr("src","T#GC_JH_SJ: distinct ND:ND as NND :SFYX='1' order by NND asc ");
	ndObj.attr("kind","dic");
	ndObj.html('');
	reloadSelectTableDic(ndObj);
	//setDefaultOption(ndObj,new Date().getFullYear());
	setDefaultNd("ND");
}

function getXmmcQueryCondition(){
	var initData = '{"querycondition":{"conditions":[]},"orders":[{"order":"desc","fieldname":"S.pxh"}]}';
	var jsonData = convertJson.string2json1(initData);
	//年度
	if("" != $("#ND").val()){
		var defineCondition = {"value": $("#ND").val(),"fieldname":"jhsj.ND","operation":"=","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//项目名称
	if("" != $("#XMMC").val()){
		var defineCondition = {"value": "%"+$("#XMMC").val()+"%","fieldname":"jhsj.XMMC","operation":"like","logic":"and"};
		jsonData.querycondition.conditions.push(defineCondition);
	}
	//是否拆迁
	var defineCondition = {"value": "1","fieldname":"jhsj.ISZC","operation":"=","logic":"and"};
	jsonData.querycondition.conditions.push(defineCondition);
	return JSON.stringify(jsonData);
}

</script>      
</head>
<body>
<app:dialogs/>
<div class="container-fluid">

  <p></p>
  <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">项目信息
      <span class="pull-right">
      	<button id="btnQd" class="btn" type="button">确定</button>
      </span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
         <tr>
         <th width="5%" class="right-border bottom-border text-right">年度</th>
         <td class="right-border bottom-border">
            <select class="span12 year" type="text" fieldname="jhsj.ND" id="ND" name="ND" kind="dic" src="XMNF" operation="=" defaultMemo="全部">
            </select>
          </td>
           <th width="5%" class="right-border bottom-border text-right">项目编号</th>
         <td class="right-border bottom-border">
            <input class="span12" type="text" fieldname="jhsj.XMBH" id="XMBH" name="XMBH" operation="like" />
          </td>
         <th width="5%" class="right-border bottom-border text-right">项目名称</th>
         <td class="right-border bottom-border">
            <input class="span12" type="text" fieldname="jhsj.XMMC" id="XMMC" name="XMMC" operation="like" autocomplete="off" tablePrefix="jhsj"/>
          </td>
          <td class="text-left bottom-border text-right">
        	<button id="btnQuery" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
        	<button id="btnClear" class="btn btn-link" type="button"><i class="icon-trash"></i>清空</button>
          </td>
         </tr>
      </table>
      </form>
<div style="height:5px;"> </div>
	<table width="100%" class="table-hover table-activeTd B-table" id="xdxmkList" type="single" pageNum="6">
	<thead>
		<tr>
			<th width="5%" name="XH" id="_XH" style="width:10px">&nbsp;#&nbsp;</th>
			<th fieldname="XMBH"  tdalign="center" maxlength="20" rowMerge="true">&nbsp;项目编号&nbsp;</th>
			<th fieldname="XMMC"  rowMerge="true" maxlength="15">&nbsp;项目名称&nbsp;</th>
			<th fieldname="CQJHWCSJ" tdalign="center">&nbsp;拆迁计划完成时间&nbsp;</th>
			<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
			<th fieldname="JSNR" maxlength="15">&nbsp;建设内容&nbsp;</th>
	  </tr>
	</thead>
	<tbody>
    </tbody>
</table>
</div>
    </div>
</div>

  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter"  order="desc" fieldname = "jhsj.pxh"	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 </FORM>
 </div>
</body>
</html>