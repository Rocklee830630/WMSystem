<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri= "/tld/base.tld" prefix="app" %>
<app:base/>
<%
	String xx=request.getParameter("xx");
    String zblx=request.getParameter("zblx");
	String cxlx = request.getParameter("cxlx")==null?"1":request.getParameter("cxlx");//定义一个查询类型，用于区分查询需求表(1)还是数据表(2)
%>
<script type="text/javascript" charset="utf-8">
	var controllername= "${pageContext.request.contextPath }/ZhaotoubiaoController.do";
	var controllername2= "${pageContext.request.contextPath }/ZhaoBiaoXuQiuController.do";
	
	g_bAlertWhenNoResult = false;
	var id='<%=xx%>';
	var i=0;					//页面表单隐藏或显示按钮
	var ZTBSJIDJE = "";
	var p_zblx = '<%=zblx%>';
	var p_cxlx = '<%=cxlx%>';
	$(function() {
		doInit();
		//获取父页面的值
		var odd = getZtbInfo(id,p_cxlx);
		//将数据放入表单
		$("#demoForm").setFormValues(odd);
		var ZTBSJID=$(odd).attr("GC_ZTB_SJ_ID");
		$("#ZBFS").attr("disabled","true");
		if(odd.ZBFS=="1"){
			$(".ggTab").show();
		}else{
			$(".ggTab").hide();
		}
		if(p_zblx=="11"){
			//设计招标
			$(".sjzbTh").show();
			$(".sgzbTh").hide();
			$(".jlzbTh").hide();
		}else if(p_zblx=="12"){
			//监理招标
			$(".sjzbTh").hide();
			$(".sgzbTh").hide();
			$(".jlzbTh").show();
		}else if(p_zblx=="13"){
			//施工招标
			$(".sjzbTh").hide();
			$(".sgzbTh").show();
			$(".jlzbTh").hide();
		}else{
			//其他全部是隐藏
			$(".sjzbTh").hide();
			$(".sgzbTh").hide();
			$(".jlzbTh").hide();
		}
		//判断报价方式
		//用户提出报价系数不需要判断了，一直显示就行了
		$(".bjxsTh").show();
		$(".bjxsThBlank").hide();
		/**
		var p_tbbjfs = $(odd).attr("TBJFS");
		if(p_tbbjfs=="3"){
			$(".bjxsTh").show();
			$(".bjxsThBlank").hide();
		}else{
			$(".bjxsTh").hide();
			$(".bjxsThBlank").show();
		}
		*/
		//查看附件
		setFileData(ZTBSJID,"","","");
		queryFileData(ZTBSJID,"","","");
		//
		 ZTBSJIDJE = ZTBSJID;
		//查询项目
		//生成json串
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		//调用ajax插入
		defaultJson.doQueryJsonList(controllername+"?queryZhaoBiaoXiangMu&ZTBSJID="+ZTBSJID,data,DT1,null,true);
		
	
		$("#aaa").hide();
		//价格划分维护页面
		var jghf=$("#jghf");
		jghf.click(function() {
			//$("#DT1").setSelect(0);
			$(window).manhuaDialog({"title":"招投标管理>维护","type":"text","content":"${pageContext.request.contextPath}/jsp/business/ztb/jghf.jsp?&ZTBSJID="+ZTBSJIDJE,"modal":"2"});
		});
		
		
		//项目信息
		var xmxx=$("#xmxx");
		xmxx.click(function(){
			$("#aaa").slideToggle("fast");
		});
	 	//保存
	 	$("#save").click(function()
			{
	 		  saveZhaoBiao(); 	
			}
	 	);
	 	
	});
	function doInit(){
		$("input").attr("disabled","true");
		$("select").attr("disabled","true");
		$("textarea").attr("disabled","true");
		$("form th").addClass("disabledTh");//所有表单中的th颜色反白
	}
	function getZtbInfo(m,n){
		var info = "";
		$.ajax({
			url:controllername2 + "?queryConditionZhaotoubiao&id="+m+"&cxlx="+n,
			data:"",
			dataType:"json",
			async:false,
			success:function(result){
				var res = result.msg;
				var tempJson = convertJson.string2json1(res);
				info = tempJson.response.data[0];
			}
		});
		return info;
	}
	function xzxm() {
		$(window).manhuaDialog({"title" : "","type" : "text","content" : "${pageContext.request.contextPath}/jsp/business/gcb/cjdw/cjdw_Query_Add.jsp","modal":"4"});
	}
	
	 //保存操作
	 function saveZhaoBiao()
	 {
		 if($("#demoForm").validationButton())
			{

			  //生成json串
			  var data = Form2Json.formToJSON(demoForm);
			  //组成保存json串格式
			  var data1 = defaultJson.packSaveJson(data);
			  //调用ajax插入
			  defaultJson.doUpdateJson(controllername + "?updateZhaotoubiao", data1,null,"updateHuiDiao");
			}
	 }
	function updateHuiDiao()
	{
		  var data2 = $("#frmPost").find("#resultXML").val();
		  var fuyemian=$(window).manhuaDialog.getParentObj();
		  fuyemian.xiugaihang(data2);
	}
	//弹出窗口回调函数
	getWinData = function(data){
	         var odd=convertJson.string2json1(data);
		       $("#DSFJGID").val(JSON.parse(data).DWMC);
		       $("#DSFJGID").attr("code",JSON.parse(data).GC_CJDW_ID);
		      // document.getElementById('DSFJGID').code = JSON.parse(data).GC_CJDW_ID; 
		     	
	};
/* 	function queryztb()
	{
		var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
		$.ajax(
		{
			   url : controllername+"?queryZhaotoubiao",//此处定义后台controller类和方法
		         data :data,    //此处为传入后台的数据，可以为json，可以为string，如果为json，那起结构必须和后台接收的bean一致或和bean的get方法名一致，例如｛id：1，name：2｝后台接收的bean方法至少包含String id,String name方法  如果为string，那么可以写为{portal: JSON.stringify(data)}, 后台接收的时候参数可以为String，名字必须和前台保持一致及定义为String portal
		         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
		         async : false,   //同步执行，即执行完ajax方法后才可以执行下面的函数，如果不设置则为异步执行，及ajax和其他函数是异步的，可能后面的代码执行完了，ajax还没执行
		         success : function(result) {
		         var resultmsg = result.msg; //返回成功事操作
		      	 var index= $("#DT1").getSelectedRowIndex();
				 var subresultmsgobj1 = defaultJson.dealResultJson(resultmsg);
				 $("#DT1").updateResult(JSON.stringify(subresultmsgobj1),DT1,index);
				 $("#DT1").setSelect(index);
		         },
		         error : function(result) {//返回失败操作
		           defaultJson.clearTxtXML();
		          }			
		}		
		);
	} */
	//判断是否是项目
	function doBdmc(obj){
		var bd_name=obj.BDMC;
		if(bd_name==null||bd_name==""){
			return '<div style="text-align:center">—</div>';
		}else{
			return bd_name;			  
		}
	}
	//判断是否是项目
	function doBdbh(obj){
		var bd_name=obj.BDBH;
		if(bd_name==null||bd_name==""){
			return '<div style="text-align:center">—</div>';
		}else{
		 	return bd_name;			  
		}
	}
	function doLbj(obj){
		var isxyscs=obj.ISXYSCS;
		if('0'==isxyscs){
			return  obj.SBCSZ;
		}else{
			return  obj.CSLBJ;
		}
	}
</script>   
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
 <div class="row-fluid">
<div class="B-small-from-table-autoConcise">
		  <h4 class="title">招标信息
		  	  <span class="pull-right">
				  <button id="xmxx" class="btn"  type="button">项目信息</button>
		      </span>  
		  </h4>
		  	<form method="post"  id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border"><input type="text" class="span12" kind="text"  fieldname="rownum"  value="1000" operation="<=" ></TD>
        </TR>
      </table>
      </form>
      <div class="B-small-from-table-autoConcise" id="aaa">
     	<!-- 台账展示信息_start -->
            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" type="single" 
            	noPage="true" editable="0" pageNum="1000" nopromptmsg="true">
                <thead>
                    <tr>	
                    		<th name="XH" id="_XH"  tdalign="">&nbsp;#&nbsp;</th>
							<th fieldname="XMBH" tdalign="left">&nbsp;项目编号&nbsp;</th>
							<th fieldname="XMMC" maxlength="15">&nbsp;项目名称&nbsp;</th>	
							<th fieldname="BDBH" maxlength="15" Customfunction="doBdbh">
								&nbsp;标段编号&nbsp;
							</th>
							<th fieldname="BDMC" maxlength="15" Customfunction="doBdmc">
								&nbsp;标段名称&nbsp;
							</th>					
							<th fieldname="XMLX" tdalign="center">&nbsp;项目类型&nbsp;</th>
							<th fieldname="XMSX" tdalign="center">&nbsp;项目属性&nbsp;</th>
							<!-- <th fieldname="ISBT" tdalign="center">&nbsp;BT&nbsp;</th> -->
							<th fieldname="XMDZ" maxlength="15">&nbsp;项目地址&nbsp;</th>
							<th fieldname="SBCSZ" tdalign="right" maxlength="20" Customfunction="doLbj">
											&nbsp;财审拦标价&nbsp;
							 </th>
						</tr>
					</thead>
					<tbody>
                	</tbody>
				</table>
		</div>
			<!-- 台账展示信息_end -->
			<form method="post" id="demoForm"  >	
			<div style="height:5px;"></div>	
      		<table class="B-table" width="100%"  >
	      		<TR  style="display:none;">
				    <TD>
						<input class="span12"  id="GC_ZTB_SJ_ID"  type="text"  name = "GC_ZTB_SJ_ID" fieldname="GC_ZTB_SJ_ID">

				    </TD>
	            </TR>
	             <tr>
      				<th width="8%" class="right-border bottom-border text-right">招投标名称</th>
          			<td width="42%"  class="right-border bottom-border "  >
          				<input class="span12" id="ZTBMC" type="text" fieldname="ZTBMC" name = "ZTBMC" maxlength="100">
          			</td>
          			<th width="8%" class="right-border bottom-border text-right">招标编号</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<input class="span12" type="text" id="ZBBH" check-type="required" placeholder="必填" fieldname="ZBBH" name="ZBBH" maxlength="100">
          			</td>
          			<th width="8%" class="right-border bottom-border text-right">招标方式</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<select class="span12" id="ZBFS" type="text" fieldname="ZBFS" name="ZBFS" kind = "dic" src="ZBFS">
          				</select>
          			</td>
          		</tr>
         		<tr>
         			<th width="8%"  class="right-border bottom-border text-right">招标代理</th>
          			<td width="42%" class="right-border bottom-border text-right" >
          				<input class="span12" type="text" id="ZBDL" name="ZBDL" fieldname="ZBDL" maxlength="100">
          			</td>	
          			<td width="50%" colspan=4 class="right-border bottom-border ">
          		</tr>
          		</table>
          		<h4 class="title ggTab">公告信息</h4>
        	<table class="B-table ggTab" width="100%">
          		<tr>
          			<th width="8%" class="right-border bottom-border text-right">公告发布媒体</th>
          			<td width="42%" colspan="7" class="right-border bottom-border text-right" colspan="7">
          				<input class="span12" type="text" id="GGFBMT" name="GGFBMT" fieldname="GGFBMT" maxlength="100">
          			</td>
          		</tr>
        	 	<tr>
          			<th width="8%" class="right-border bottom-border text-right">公告发布起始日期</th>
          			<td width="17%" class="right-border bottom-border text-right">
          				<input class="span12" type="date" id="GGFBQSRQ" name="GGFBQSRQ" fieldname="GGFBQSRQ">
          			</td>
          			<td width="25%" class="right-border bottom-border" colspan=2></td>
          			<th width="8%" class="right-border bottom-border text-right">公告发布结束日期</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<input class="span12" type="date" id="GGFBJSRQ" name="GGFBJSRQ" fieldname="GGFBJSRQ">
          			</td>
          			<td width="25%" class="right-border bottom-border" colspan=2></td>
          		</tr>
          		<tr>
          		<td width="100%" colspan="8" class="bottom-border">
          		<input class="span12" type="checkbox" placeholder="" name = "GGQK"  kind="dic" src="GGQK" fieldname = "GGQK" >
          		</td>
          		</tr>
        	</table> 
          		<h4 class="title">开标信息</h4>
        		<table class="B-table" width="100%">
        		<tr>
        			<th width="8%" class="right-border bottom-border text-right">开标时间</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<input class="span12" check-type="required" placeholder="必填" type="date" id="KBRQ"  name ="KBRQ" fieldname="KBRQ">
          			</td>
         			<th width="8%" class="right-border bottom-border text-right">付款方式</th>
          			<td width="17%" class="right-border bottom-border text-right">
          				<input class="span12" type="text" id="DZFS" name = "DZFS" fieldname="DZFS" check-type="maxlength" maxlength="300"/>
          			</td>
          			<td class="right-border bottom-border" colspan="4"></td>
          			</tr>
        		</table>
        		<h4 class="title">中标信息</h4>
        		<table class="B-table" width="100%">
          		<tr>
          			<th width="8%" class="right-border bottom-border text-right">中标通知书编号</th>
          			<td width="17%" class="right-border bottom-border text-right" colspan=3>
          				<input class="span12" type="text" id="ZBTZSBH" name="ZBTZSBH" fieldname="ZBTZSBH" maxlength="100">
          			</td>
          			<th width="8%" class="right-border bottom-border text-right">总中标价</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<input class="span9" type="number" min="0" id="ZZBJ" name="ZZBJ" fieldname="ZZBJ" style="text-align:right;">&nbsp;&nbsp;<b>(元)</b>
          			</td>
          			<th width="8%" class="right-border bottom-border text-right bjxsTh">报价系数</th>
          			<td width="17%" class="right-border bottom-border text-right bjxsTh" >
          				<input class="span12" type="number" min="0" id="BJXS" name="BJXS" fieldname="BJXS" style="text-align:right;">
          			</td>
          			<td width="50%" class="right-border bottom-border text-right bjxsThBlank" colspan=2>
					</td>
          		</tr>
          		
          		<!-- 监理招标信息_end -->
          		<tr>
          			<th width="8%" class="right-border bottom-border text-right">中标单位</th>
          			<td width="42%" class="right-border bottom-border" colspan=3>
          				<input class="span12" type="text" id="DSFJGID" name="DSFJGID" fieldname="DSFJGID" maxlength="100"  disabled>
          			</td>
          			<td class="right-border bottom-border" colspan=4></td>
          		</tr>
          		
          		<!-- 施工招标特有信息_start -->
          		<tr class="sgzbTh">
					<th class="right-border bottom-border text-right">项目经理</th>
          			<td class="right-border bottom-border text-right" >
          				<input class="span12" type="text" id="XMJL" name="XMJL" fieldname="XMJL" maxlength="100">
          			</td>
          			<th class="right-border bottom-border text-right">
						技术负责人
					</th>
					<td class="right-border bottom-border text-right">
						<input class="span12" type="text" id="JSFZR" name="JSFZR"
							fieldname="JSFZR" maxlength="100">
					</td>
          			<td class="right-border bottom-border" colspan="4"></td>         		
          		</tr>
          		<tr class="sgzbTh">
          			<th class="right-border bottom-border text-right">项目其他人员</th>
          			<td class="right-border bottom-border text-right" colspan="7">
          				<textarea rows="3" class="span12" type="text" id="XMQTRY" name="XMQTRY" fieldname="XMQTRY" maxlength="100"></textarea>
          			</td>
          		</tr>
          		<!-- 施工招标特有信息_end -->
          		
          		<!-- 监理招标特有信息_start -->
          		<tr class="jlzbTh">
					<th class="right-border bottom-border text-right">项目总监</th>
          			<td class="right-border bottom-border" >
          				<input class="span12" type="text" id="XMZJ" name="XMZJ" fieldname="XMZJ" maxlength="100">
          			</td>
          			<th class="right-border bottom-border text-right">
						总监代表
					</th>
					<td class="right-border bottom-border text-right">
						<input class="span12" type="text" id="ZJDB" name="ZJDB"
							fieldname="ZJDB" maxlength="100">
					</td>
					<th width="8%" class="right-border bottom-border text-right">
						安全监理
					</th>
					<td width="17%" class="right-border bottom-border text-right">
						<input class="span12" type="text" id="AQJL" name="AQJL"
							fieldname="AQJL" maxlength="100">
					</td>
          			<td width="25%" class="right-border bottom-border" colspan=2></td>         		
          		</tr>
          		<tr class="jlzbTh">
          			<th class="right-border bottom-border text-right">项目其他<br>监理人员</th>
          			<td class="right-border bottom-border text-right" colspan="7">
          				<textarea rows="3" class="span12" type="text" id="XMQTJLRY" name="XMQTJLRY" fieldname="XMQTJLRY" maxlength="100"></textarea>
          			</td>
          		</tr>
          		<!-- 监理招标特有信息_end -->
          		<tr>
          			<th class="right-border bottom-border text-right">备注</th>
          			<td class="right-border bottom-border text-right" colspan="7">
          				<textarea rows="3" class="span12" type="text" id="BZ" name="BZ" fieldname="BZ" maxlength="4000"></textarea>
          			</td>
          		</tr>
       		</table>
        	<h4 class="title sgzbTh">施工招标信息</h4>
        	<table class="B-table sgzbTh" width="100%">
       		   <!-- <tr>
          			<th width="8%" class="right-border bottom-border text-right">财审拦标价</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<input style="text-align:right;" class="span9" type="number" min="0" id="CSLBJ" name="CSLBJ" fieldname="CSLBJ" >&nbsp;&nbsp;<b>(元)</b>
          			</td>
          			<th width="8%" class="right-border bottom-border text-right">中标价比照财审拦标价降幅</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<input style="text-align:right;width: 90%"  class="span12" type="number" min="0" max="100" id="ZBJBZCS" name="ZBJBZCS" fieldname="ZBJBZCS" >%
          			</td>
          			<th width="8%" class="right-border bottom-border text-right">现场摇球降幅</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<input style="text-align:right;width: 90%" class="span12" type="number" min="0" max="100" id="XCYQJF" name="XCYQJF" fieldname="XCYQJF" >&nbsp;%
          			</td> 
          			<th width="8%" class="right-border bottom-border text-right">预留金</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<input style="text-align:right;"  class="span9" type="number" min="0" id="YLJ" name="YLJ" fieldname="YLJ" >&nbsp;&nbsp;<b>(元)</b>
          			</td>
       		   </tr>-->
       		   <tr>
          			<th class="right-border bottom-border text-right">应缴纳履约保证金</th>
          			<td class="right-border bottom-border text-right" >
          				 <input   style="text-align:right;" class="span9" type="number" min="0" id="YJNLYBZJ"   name="YJNLYBZJ" fieldname="YJNLYBZJ" >&nbsp;&nbsp;<b>(元)</b>
          			</td>
       		   		<td class="right-border bottom-border" width="70%" colspan=6></td>
       		   </tr>
        	</table>
        	<h4 class="title sjzbTh">设计招标区域</h4>
        	<table class="B-table sjzbTh" width="100%">
       		   <tr>
          			<th width="8%" class="right-border bottom-border text-right">折扣系数</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<input class="span12" type="number" min="0" id="ZKXS" name="ZKXS" fieldname="ZKXS" style="text-align:right;">
          			</td>
          			<th width="8%" class="right-border bottom-border text-right">计划中主体造价</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<input class="span12" type="number" min="0" id="JHZZTZJ" name="JHZZTZJ" fieldname="JHZZTZJ" style="text-align:right;">
          			</td>
          			<th width="8%" class="right-border bottom-border text-right">八折造价</th>
          			<td width="17%" class="right-border bottom-border text-right" >
          				<input class="span12" type="number" min="0" id="BZZJ" name="BZZJ" fieldname="BZZJ" style="text-align:right;">
          			</td>
          			   <th width="8%" class="right-border bottom-border text-right">
									负责人
								</th>
								<td width="17%" class="right-border bottom-border text-right">
									<input class="span12" type="text"  id="SJFZR"
										name="SJFZR"  fieldname="SJFZR">
								</td>
       		   </tr>
        	</table>
      	</form>
     	</div>
 	</div>
</div>
 <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true"/>
 <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank" id ="frmPost">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="txtFilter" 	id = "txtFilter">
         <input type="hidden" name="resultXML" id = "resultXML">
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
         <input type="hidden" name="queryResult" id = "queryResult">
		
 	</FORM>
 </div>
  <script>

</script>
</body>
</html>