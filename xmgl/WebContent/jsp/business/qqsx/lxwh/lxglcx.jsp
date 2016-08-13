<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title>规划审批进度维护</title>
<script src="${pageContext.request.contextPath }/js/common/bootstrap.autocomplete.js"></script>
<script type="text/javascript" charset="utf-8">
var autocompleteXmmcController= "${pageContext.request.contextPath }/tcjh/tcjhController.do"; 
  var controllername= "${pageContext.request.contextPath }/liXiangShouXuController.do";
  var biaozhi=null;
  var json;
	$(function() {
		getNd();
		//查询
		var querybtn = $("#query");
		querybtn.click(function() 
			{
				//生成json串
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,ghwhList);
				//调用ajax插入
				defaultJson.doQueryJsonList(controllername+"?queryliXiangShouXu",data,ghwhList);
			});
            //清空
            var btn_clearQuery = $("#query_clear");
            btn_clearQuery.click(function() {
                $("#queryForm").clearFormResult();
                //其他处理放在下面
                getNd();
            });
            //维护
            var whbtn= $("#wh");
            whbtn.click(function()
            		{
	            	   var index1 =	$("#ghwhList").getSelectedRowIndex();
				 		if(index1<0) 
							{
					 			requireSelectedOneRow();
					 			return;
							}
				 		else
				 		{
				 		 parent.$("body").manhuaDialog({"title":"立项科研>修改","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/lxwh/lxkyAdd.jsp?xx="+json,"modal":"1"});
				 		}
    				});
            //手续维护
            var sxwhbtn= $("#sxwh");
            sxwhbtn.click(function()
            {
          	   var index2 =	$("#ghwhList").getSelectedRowIndex();
		 		if(index2<0) 
					{
			 			requireSelectedOneRow();
			 			return;
					}
		 		else
		 		{
    			   parent.$("body").manhuaDialog({"title":"立项科研>手续维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/lxwh/lxsxwh.jsp?id="+biaozhi,"modal":"1"});			
		 		}
		 	});
            g_bAlertWhenNoResult=false;	
          //生成json串
			var data = combineQuery.getQueryCombineData(queryForm,frmPost,ghwhList);
			//调用ajax插入
			defaultJson.doQueryJsonList(controllername+"?queryliXiangShouXu",data,ghwhList);
			g_bAlertWhenNoResult=true;        
	});
	//单击行
	function tr_click(obj,tabListid)
	{
		biaozhi=$(obj).attr("GC_QQSX_LXKY_ID");
		json = $("#ghwhList").getSelectedRowText();
		json=encodeURI(json);
	}
	//子页面调用修改行
	function xiugaiahang(subresultmsgobj)
	{
		 var index= $("#ghwhList").getSelectedRowIndex();
		 var subresultmsgobj1 = defaultJson.dealResultJson(subresultmsgobj);
		 $("#ghwhList").updateResult(JSON.stringify(subresultmsgobj1),ghwhList,index);
		 $("#ghwhList").setSelect(index);
		 rowValue = $("#ghwhList").getSelectedRowText();
		 json=encodeURI(rowValue);
	}
	//子页面调用查询一条
	function gengxinchaxun(zymlxID)
	{
		$.ajax({
	         url : controllername+"?queryliXiangShouXu",//此处定义后台controller类和方法
	         data : {ywid:zymlxID},    //此处为传入后台的数据，可以为json，可以为string，如果为json，那起结构必须和后台接收的bean一致或和bean的get方法名一致，例如｛id：1，name：2｝后台接收的bean方法至少包含String id,String name方法  如果为string，那么可以写为{portal: JSON.stringify(data)}, 后台接收的时候参数可以为String，名字必须和前台保持一致及定义为String portal
	         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
	         async : false,   //同步执行，即执行完ajax方法后才可以执行下面的函数，如果不设置则为异步执行，及ajax和其他函数是异步的，可能后面的代码执行完了，ajax还没执行
	         success : function(result) {
	         var resultmsg = result.msg; //返回成功事操作
	      	 var index= $("#ghwhList").getSelectedRowIndex();
			 var subresultmsgobj1 = defaultJson.dealResultJson(resultmsg);
			 $("#ghwhList").updateResult(JSON.stringify(subresultmsgobj1),ghwhList,index);
			 $("#ghwhList").setSelect(index);
	         },
	         error : function(result) {//返回失败操作
	            alert(result.msg);
	           defaultJson.clearTxtXML();
	 
	          }
	});

	}
	//默认年度
	function getNd(){
		var d=new Date();
    	var year=d.getFullYear();
    	$("#ND").val(year);
	}
	//详细信息
	 function rowView(index){
	     var obj = $("#ghwhList").getSelectedRowJsonByIndex(index);//获取行对象
	 var xmbh = eval("("+obj+")").XDKID;//取行对象<项目编号>
	 parent.$("body").manhuaDialog(xmscUrl(xmbh));//调用公共方法,根据项目编号查询
	 }
	//自定义的获取页面查询条件
	 function xmmcAutoQuery(){
		    return combineQuery.getQueryCombineData(queryForm,frmPost,ghwhList);//同正常查询获取方法
		}
	 $(document).ready(function(){ 
		    showAutoComplete("QXMMC",autocompleteXmmcController+"?xmmcAutoQuery","xmmcAutoQuery"); 
		});
</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
<p></p>
 <div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
      <h4 class="title">立项可研手续  
      	<span class="pull-right">
      		<button id="wh" class="btn" type="button">修改</button>
      		<button id="sxwh" class="btn" type="button">手续维护</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD>
				<INPUT type="text" class="span12" kind="text" keep="true" fieldname="rownum"  value="1000" operation="<=" >
			</TD>
        </TR>
        <tr>
           <th width="5%" class="right-border bottom-border text-right">年度</th>
           <td width="10%" class="right-border bottom-border">
	           <select class="span12" id="ND" name = "ND" fieldname="xmxdk.ND" operation="=" kind="dic" src="XMNF" logic = "and" >
	           </select>
           </td>
          <!--  <th width="5%" class="right-border bottom-border text-right" >项目类型</th>
           <td width="10%" class="bottom-border">
                <select class="span12" id="XMLX" name = "XMLX"  fieldname="xmxdk.XMLX" kind="dic" src="XMLX" operation="=">
                </select>
           </td> -->
           <th width="5%" class="right-border bottom-border text-right">项目名称</th>
           <td width="20%" class="bottom-border">
	            <input class="span12" type="text" placeholder="" name = "XMMC" fieldname="xmxdk.XMMC" operation="like"  id="QXMMC" autocomplete="off" tablePrefix="xmxdk" >
	       </td>
           <td  class="text-left bottom-border text-right">
           <button id="query" class="btn btn-link"  type="button"><i class="icon-search"></i>查询</button>
           <button id="query_clear" class="btn btn-link"  type="button"><i class="icon-trash"></i>清空</button>
          </td>
        </tr>
      </table>
      </form>
    <div style="height:5px;"> </div>
   <div class="overFlowX">  
	<table class="table-hover table-activeTd B-table" id="ghwhList" width="100%">
		<thead>
		<tr>
			<th  name="XH" id="_XH" rowspan="2" colindex=1 >&nbsp;#&nbsp;</th>
			  <th fieldname="XMBH" rowspan="2" colindex=2 tdalign="center" hasLink="true" linkFunction="rowView">&nbsp;项目编号&nbsp;</th>
            <th fieldname="XMMC" rowspan="2" colindex=3 maxlength="15">&nbsp;项目名称&nbsp;</th>
                <th colspan="5" >&nbsp;进展情况&nbsp;</th>
                 <th fieldname="BJSJ" rowspan="2" colindex=9 tdalign="center">&nbsp;办结时间&nbsp;</th>
               <th fieldname="CZWT" rowspan="2" colindex=10 maxlength="15">&nbsp;存在问题&nbsp;</th>
               <th fieldname="BBLSX" rowspan="2" name="BBLSX" colindex=11>&nbsp;不办理手续&nbsp;</th>
               <th fieldname="BBLFJ" rowspan="2" colindex=12>&nbsp;不办理情况附件&nbsp;</th>
               <th fieldname="JBR" rowspan="2" colindex=13 tdalign="center">&nbsp;经办人&nbsp;</th>
             
            </tr>
            <tr>
              <th fieldname="XMJYSPF" colindex=4>&nbsp;项目建议书批复&nbsp;</th>
              <th fieldname="HPPF" colindex=5>&nbsp;环评批复&nbsp;</th>
              <th fieldname=TDYJH colindex=6>&nbsp;土地意见函&nbsp;</th>
              <th fieldname="GDZCTZXM" colindex=7>&nbsp;固定资产投资项目节能审查&nbsp;</th>
              <th fieldname="KYPF" colindex=8>&nbsp;可研批复&nbsp;</th>
               
		</tr>
		</thead>
	<tbody></tbody>
	</table>
	</div>
	</div>
</div>
</div>

  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="resultXML" id = "resultXML">
         <input type="hidden" name="txtFilter" order="desc" fieldname="xmxdk.XMBH,lxky.LRSJ"/>
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
          <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
</body>
</html>