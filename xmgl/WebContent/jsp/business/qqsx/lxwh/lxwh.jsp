<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<app:base />
<title>作废---规划审批进度维护</title>
<%-- <script src="${pageContext.request.contextPath }/js/common/xWindow.js"></script> --%>
<script type="text/javascript" charset="utf-8">
  
  var controllername= "${pageContext.request.contextPath }/liXiangShouXuController.do";
  var biaozhi=null;
	$(function()
		{
			//查询
			var querybtn = $("#query");
			var whbtn= $("#wh");
			querybtn.click(function() 
					{
						//生成json串
						var data = combineQuery.getQueryCombineData(queryForm,frmPost,ghwhList);
						//调用ajax插入
						defaultJson.doQueryJsonList(controllername+"?queryliXiangShouXu",data,ghwhList);
					});
			 
			//保存
			    var savebtn=   $("#save");
	            savebtn.click(function() 
					{
			 				//生成json串
			 				if(biaozhi==''||biaozhi==null)
			 			{
			 				//插入
			 				
			 				if($("#demoForm").validationButton())
							{
				 		 		var data = Form2Json.formToJSON(demoForm);
				 				//组成保存json串格式
				 				var data1 = defaultJson.packSaveJson(data);
				 				var rowindex = $("#ghwhList").getSelectedRowIndex();
				 				
						 				var xuanzhong = '';
						 				var buxuanzhong = '';
						 				$("input[type='checkbox']").each(function()
						 					{
							 					if($(this).is(':checked')){
							 						xuanzhong =xuanzhong+$(this).attr('value')+',';
							 						
							 					}
							 					else
							 						{
							 						buxuanzhong=buxuanzhong+$(this).attr('value')+',';
							 						}
						 					});
				 				
				 				//调用ajax插入
				 				defaultJson.doUpdateJson(controllername + "?insert&buxuanzhong="+buxuanzhong+"&xuanzhong="+xuanzhong, data1,ghwhList);
				 				$("#ghwhList").updateResult(data,ghwhList,rowindex);
			 			     }
			 			  }
			 				else
			 				{
			 					//修改
						 		 	if($("#demoForm").validationButton())
									{
						 		 		//生成json串
						 		 		var data = Form2Json.formToJSON(demoForm);
						 				//组成保存json串格式
						 				var data1 = defaultJson.packSaveJson(data);
						 				
						 				
						 				var xuanzhongupdate = '';
						 				var buxuanzhongupdate = '';
						 				$("input[type='checkbox']").each(function()
						 					{
						 					    if($(this).is(':checked')){
						 						xuanzhongupdate =xuanzhongupdate+$(this).attr('value')+',';
						 						
							 					}
							 					else
							 						{
							 						buxuanzhongupdate=buxuanzhongupdate+$(this).attr('value')+',';
							 						}
						 					});
						 				
						 				//调用ajax插入
						 				defaultJson.doUpdateJson(controllername + "?update&buxuanzhongupdate="+buxuanzhongupdate+"&xuanzhongupdate="+xuanzhongupdate, data1,ghwhList);
									}
			 				}
				 				
							
					 });
	         
	            //维护
	            whbtn.click(function()
	            {
	            	if(biaozhi==''||biaozhi==null)
		 			{
	            		requireSelectedOneRow();
		 			}
	            	else
	            	{
	    			parent.$("body").manhuaDialog({"title":"","type":"text","content":"${pageContext.request.contextPath}/jsp/business/qqsx/lxwh/lxsxwh.jsp?id="+biaozhi,"modal":"2"});			
	            	}
	             });
	            
	      	   //清空
	            var btn_clearQuery = $("#query_clear");
	            btn_clearQuery.click(function() 
	              {
	            	$("#queryForm").clearFormResult(); 
	              });
	            //新增清空
	            var btn = $("#example_clear");
				btn.click(function() 
				{
					$("#demoForm").clearFormResult(); 
				});
	          //生成json串
				var data = combineQuery.getQueryCombineData(queryForm,frmPost,ghwhList);
				//调用ajax插入
				defaultJson.doQueryJsonList(controllername+"?queryliXiangShouXu",data,ghwhList);
	             
		});
	//选中行数据放入表单
	function tr_click(obj,tabListid)
	{
		biaozhi=$(obj).attr("GC_QQSX_LXKY_ID");
		//清空默认复选框
		$("input[type='checkbox']").prop("checked", false);
		if(biaozhi!='')
			{
			//有数据
				var lujing="${pageContext.request.contextPath }/liXiangShouXuController.do?queryBanLiXiangById";
				$.ajax({
			         url : lujing,//此处定义后台controller类和方法
			         data : {ywid:biaozhi},    //此处为传入后台的数据，可以为json，可以为string，如果为json，那起结构必须和后台接收的bean一致或和bean的get方法名一致，例如｛id：1，name：2｝后台接收的bean方法至少包含String id,String name方法
			                           // 如果为string，那么可以写为{portal: JSON.stringify(data)}, 后台接收的时候参数可以为String，名字必须和前台保持一致及定义为String portal
			         dataType : 'json',//此处定义返回值的类型为string，详见样例代码
			         async : false,   //同步执行，即执行完ajax方法后才可以执行下面的函数，如果不设置则为异步执行，及ajax和其他函数是异步的，可能后面的代码执行完了，ajax还没执行
			         success : function(result) 
			         {
				         var resultmsg = result.msg; //返回成功事操作
				   		//没有返回数据
				   		if(resultmsg==0)
				   			{
				   			
				   			}
				   		else
				   		{
					   		//将字符串转成对象
							var resultmsgobj = convertJson.string2json1(resultmsg);
							//获取对象的一个属性
							var rowDataJsons = resultmsgobj.response;
							 //有几个对象
							var ywids='';
							 //将附件类型ID拼成串
							for(var i=0;i<rowDataJsons.data.length;i++)
								  {
							        //获取第一个对象 
								    var subresultmsgobj = rowDataJsons.data[i];
								    var ywidss=subresultmsgobj.SXID;
							        ywids=ywids+ywidss+',';
								  }
							 //将串转化成数组
						     var bllx = new Array();
						     bllx = ywids.split(",");
						       //	alert(bllx);
						       //	alert("a"+$("#FJLX").find("input[type='checkbox']"));
						     //获取checkbox的数量
						     var changdu=$("input[type=checkbox][name='FJLX']").length;
						      //alert(changdu);
						     // alert("aaa:"+$("input[type=checkbox][name='FJLX']").eq(2).attr("value"));
						     for(var j=0;j<changdu;j++)
						    		{
						    		// alert("aaa:"+$("input[type=checkbox][name='FJLX']").eq(j).attr("value"));
									 	 for(var i=0;i<bllx.length-1;i++)
									 	  {
											if($("input[type=checkbox][name='FJLX']").eq(j).attr("value")== bllx[i])
											{
												
												$("input[type=checkbox][name='FJLX']").eq(j).prop("checked", true);
													break;
											}
										  } 
								   }
						     }
	                 },
			         error : function(result) 
			         {//返回失败操作
			        	 alert('失败');
			           // alert(result.msg);
			           defaultJson.clearTxtXML();
			 
			          }
	             });
		 }
		$("#demoForm").setFormValues(obj);
		//为文件上传需要的数据赋值
		$("#resultXML").val(JSON.stringify(obj));
	 }

</script>
</head>
<body>
<app:dialogs/>
<div class="container-fluid">
</p>
 <div class="row-fluid">
    <div class="B-small-from-table-box">
      <h4>信息查询  
      	<span class="pull-right">
      		<button id="query" class="btn btn-inverse"  type="button">查询</button>
      		 <button id="query_clear" class="btn btn-inverse" type="button">清空</button>
      		<button id="wh" class="btn btn-inverse" type="button">手续维护</button>
      	</span>
      </h4>
     <form method="post" id="queryForm"  >
      <table class="B-table">
      <!--可以再此处加入hidden域作为过滤条件 -->
      	<TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
				<INPUT type="text" class="span12" kind="text" keep="true"  fieldname="rownum"  value="1000" operation="<=" >
			</TD>
        </TR>
        <tr>
	        <th width="5%" class="right-border bottom-border text-right">项目年份</th>
	        <td width="10%" class="right-border bottom-border">
	           <select class="span12" id="ND" name = "ND" fieldname="xmxdk.ND"  kind="dic" src="XMNF" operation="=" logic = "and" >
	            </select>
	         </td>
	         <th width="5%" class="right-border bottom-border text-right" >项目类型</th>
	         <td width="10%" class="bottom-border">
	                 <select class="span12" id="XMLX" name = "XMLX"  fieldname="xmxdk.XMLX" kind="dic" src="XMLX" operation="=">
	                 </select>
	          </td>
	          <th width="5%" class="right-border bottom-border text-right">项目名称</th>
	          <td width="20%" class="bottom-border">
		          <input class="span12" type="text" placeholder="" name = "XMMC" fieldname="xmxdk.XMMC" operation="like" >
		       </td>
	           <th></th>
        </tr>
      </table>
      </form>
    </div>
  </div>
  <div class="row-fluid">
 <div class="B-small-from-table-auto">
  <h4>查询结果 
  </h4>
   <div class="overFlowX">  
	<table class="table-hover table-activeTd B-table" id="ghwhList" width="100%" type="single">

		<thead>
		<tr>
			<th  name="XH" id="_XH" rowspan="2" colindex=1 >&nbsp;序号&nbsp;</th>
            <th fieldname="XMMC" rowspan="2" colindex=2 maxlength="15">&nbsp;项目名称&nbsp;</th>
             <th colspan="7" >&nbsp;进展情况&nbsp;</th>
             <th fieldname="CZWT" rowspan="2" colindex=10>&nbsp;存在问题&nbsp;</th>
             <th fieldname="BBLYY" rowspan="2" colindex=11>&nbsp;不办理手续&nbsp;</th>
             <th fieldname="BBLYY" rowspan="2" colindex=12>&nbsp;不办理情况附件&nbsp;</th>
             <th fieldname="JBR" rowspan="2" tdalign="center" colindex=13>&nbsp;经办人&nbsp;</th>
              <th fieldname="BJSJ" rowspan="2" tdalign="center" colindex=14>&nbsp;办结时间&nbsp;</th>
        </tr>
        <tr>
             
            
            <th fieldname="JBR" colindex=3>&nbsp;项目建议书批复&nbsp;</th>
            <th fieldname="JBR" colindex=4>&nbsp;环评批复&nbsp;</th>
            <th fieldname="JBR" colindex=5>&nbsp;土地意见函&nbsp;</th>
            <th fieldname="JBR" colindex=6>&nbsp;建设项目选址意见书&nbsp;</th>
            <th fieldname="CZWT" colindex=7>&nbsp;建设项目选址意见书&nbsp;</th>
            <th fieldname="CZWT" colindex=8>&nbsp;固定资产投资项目节能审查&nbsp;</th>
            <th fieldname="BBLYY" colindex=9>&nbsp;可研批复&nbsp;</th>
		</tr>
		</thead>
	<tbody></tbody>
	</table>
	</div>
	</div>
</div>
    <div class="row-fluid">
    <div class="B-small-from-table-box">
      <h4>信息维护表单
         <span class="pull-right">
	          <button id="save" name="save" class="btn btn-inverse"  type="button">保存</button>
	          <button id="example_clear" class="btn btn-inverse"  type="button">新 增</button> 
          </span>
       </h4>
     <form method="post" action="${pageContext.request.contextPath }/insertdemo.xhtml" id="demoForm"  >
      <table class="B-table">
            
         <TR  style="display:none;">
	        <TD class="right-border bottom-border">
		         <input type="text" class="span12" kind="text"  fieldname="xdkid"  >
	      		 <input type="text" class="span12" kind="text"  fieldname="qqsxid" >
				 <input type="text" class="span12" kind="text"  fieldname="GC_QQSX_LXKY_ID" >
	        </TD>
			<TD class="right-border bottom-border"></TD>
        </TR>
        <tr>
          <th width="10%" class="right-border bottom-border text-right">项目名称</th>

          <td width="40%" colspan="2" class="right-border bottom-border"><input class="span12" readonly="readonly" placeholder="请选择项目" check-type="required" type="text"  fieldname="XMMC" name = "XMMC"></td>
          <th width="10" class="right-border bottom-border text-right"> 办结时间</th>
           <td width="42%" colspan="2" class="bottom-border"><input class="span12" type="date"  name = "BJSJ" fieldname= "BJSJ"></td>
        </tr>
        <tr>
        <th width="10%" class="right-border bottom-border text-right">存在问题</th>
          <td width="90%" colspan="5" class="bottom-border">
          <textarea rows="3" class="span12" id="CZWT" name = "CZWT" fieldname= "CZWT" maxlength="4000"  check-type="maxlength"></textarea>
          </td>
        </tr>
        <tr>
        <th width="12%" class="right-border bottom-border text-right">不办理手续</th>
        <td width="15%" class="right-border bottom-border">
            <label class="checkbox">项目建议书批复
            	<input type="checkbox" id="FJLX" name="FJLX" value="1000000000083" >
            </label>
          </td>
          <td width="15%" class="right-border bottom-border">
            <label class="checkbox">环评批复
            	<input type="checkbox" id="FJLX" name="FJLX" value="1000000000084" >
            </label>
          </td>
          <td width="15%" class="right-border bottom-border ">
            <label class="checkbox">土地意见函
                <input type="checkbox" id="FJLX" name="FJLX" value="1000000000085" >
            </label>
          </td>
            <td width=029%" class="right-border bottom-border ">
            <label class="checkbox">固定资产投资项目节能审查
                <input type="checkbox" id="FJLX" name="FJLX" value="1000000000086" >
            </label>
          </td>
           <td width="15%" class="right-border bottom-border ">
            <label class="checkbox">可研批复
                <input type="checkbox" id="FJLX" name="FJLX" value="1000000000087" >
            </label>
          </td>
          <th></th>
          <td></td>
        </tr>
        <tr>
          <th width="8%" class="right-border bottom-border text-right">不办理原因</th>
          <td width="42%" colspan="5" class="bottom-border">
          <textarea rows="3" class="span12" id="BBLYY" name = "BBLYY" fieldname= "BBLYY" maxlength="4000"  check-type="maxlength"></textarea>
          </td>
        </tr>
      </table>
      </form>
      <div style="height:350px;">
			<iframe
				src="${pageContext.request.contextPath}/jsp/file_upload/fileupload.jsp"
				id="menuiframe" width="100%" height="100%" frameborder="0">
			</iframe>
		</div>
    </div>
  </div>
</div>

  <div align="center">
 	<FORM name="frmPost" method="post" style="display:none" target="_blank">
		 <!--系统保留定义区域-->
         <input type="hidden" name="queryXML" id = "queryXML">
         <input type="hidden" name="txtXML" id = "txtXML">
         <input type="hidden" name="resultXML" id = "resultXML">
          <input type="hidden" name="txtFilter" order="desc" fieldname="xmxdk.XMBH,lxky.LRSJ"/>
       		 <!--传递行数据用的隐藏域-->
         <input type="hidden" name="rowData">
		
 	</FORM>
 </div>
</body>
</html>