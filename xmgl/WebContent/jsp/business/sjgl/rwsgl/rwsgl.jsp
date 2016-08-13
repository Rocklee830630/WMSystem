<!DOCTYPE html>
<html>
<head>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tld/base.tld" prefix="app"%>
<%@ page import="com.ccthanking.framework.common.User"%>
<%@ page import="com.ccthanking.framework.Globals"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat"%>
<app:base />
	<%
	request.setCharacterEncoding("utf-8");
	User user = (User)  request.getSession().getAttribute(Globals.USER_KEY);
	String department = user.getDepartment();
	String account=user.getAccount();
	String username = user.getName();
	Date d=new Date();//获取时间
	SimpleDateFormat sam_date=new SimpleDateFormat("yyyy-MM-dd");		//转换格式
	String today=sam_date.format(d);
	%>
<title></title>
</head>
<script type="text/javascript">
var controllername= "${pageContext.request.contextPath }/renWuShuController.do";
$(function() 
  {
		jingbanren();
		init();
		//任务书类型改变event
		$("#RWSLX").change(function() {
			var rwslb = $("#RWSLX").val();
			changeRWSLB(rwslb);
		  });
		//任务书类型改变event
		$("#LB").change(function() {
			var lb = $("#LB").val();
			changeLB(lb);
		  });
		//添加方法
		var insert=$("#insert");
		insert.click(function(){
			if($("#demoForm1").validationButton())
			{
			var id=$("#GC_SJGL_RWSGL_ID").val();
			var data = Form2Json.formToJSON(demoForm1);
			var data1 = defaultJson.packSaveJson(data);
			if(id==null||id==""){
				defaultJson.doInsertJson(controllername + "?insertRenWuShu&ywid="+$("#ywid").val()+"&fjbh="+$("#LB").val(),data1, DT1,'addHuiDiao');
			}else{
				defaultJson.doUpdateJson(controllername + "?updateRenWuShu&fjbh="+$("#LB").val(),data1, DT1,'editHuiDiao');
			}
			}else{
		  		defaultJson.clearTxtXML();
			}
		});
		//删除
		var del=$("#del");
		del.click(function(){
			var index =	$("#DT1").getSelectedRowIndex();
			//生成json串
		 	var data = Form2Json.formToJSON(demoForm1);
			var data1 = defaultJson.packSaveJson(data);
			if(index==-1){
				requireSelectedOneRow();
				return;
			}else{
				xConfirm("提示信息","是否确认删除！");
				$('#ConfirmYesButton').unbind();
				$('#ConfirmYesButton').one("click",function(){ 
					defaultJson.doDeleteJson(controllername+"?deleteRenWuShu",data1,DT1,"deleteHuiDiao"); 
				});
		}
		});
		//清空
		var example1=$("#example1");
		example1.click(function(){
			clear();
		});
  });
	//回调函数
	function addHuiDiao()
	{
		var fuyemian1=$(window).manhuaDialog.getParentObj();
		fuyemian1.xiugaihang();
		//获取返回的ID
	    var data2 = $("#frmPost").find("#resultXML").val();
		var resultmsgobj = convertJson.string2json1(data2);
		var subresultmsgobj = resultmsgobj.response.data[0];
		var rwsglid=$(subresultmsgobj).attr("GC_SJGL_RWSGL_ID");
		$("#GC_SJGL_RWSGL_ID").val(rwsglid); 
	    $("#DT1").setSelect(0);
	}
	function editHuiDiao()
	{
		var fuyemian1=$(window).manhuaDialog.getParentObj();
		fuyemian1.xiugaihang();
	}
   function deleteHuiDiao()
   {
	  	 clear();
		 var fuyemian=$(window).manhuaDialog.getParentObj();
		 fuyemian.xiugaihang();
   }
   function clear()
   {
	  	 $("#demoForm1").clearFormResult();
		 $("#DT1").cancleSelected();
		 clearFileTab();
	     $("#ywid").val("");
	     var rwslb = $("#RWSLX").val();
		 changeRWSLB(rwslb);
		 jingbanren();
   }
	//行选
	function tr_click(obj){
		//类别判断
		var rwslb = obj.RWSLX;
		changeRWSLB(rwslb);
		//附件编号
		var lb=obj.LB;
		
		changeLB(lb);
		var hang=$("#DT1").getSelectedRow();
		if(hang=="-1"){
			$("#demoForm1").clearFormResult();
		}else{
			$("#demoForm1").setFormValues(obj);
		}
		//为上传文件是需要的字段赋值
	    var ywid=$(obj).attr("GC_SJGL_RWSGL_ID");
		var SJBH=$(obj).attr("SJBH");
		var YWLX=$(obj).attr("YWLX");
		deleteFileData(ywid,"","","");
		setFileData(ywid,"",SJBH,YWLX,"0");
		//查询附件信息
		queryFileData(ywid,"","","");
	}
  function init()
  {
	  var rowValue =$($(window).manhuaDialog.getParentObj().document).find("#DT1").getSelectedRow();
	  var odd=convertJson.string2json1(rowValue);
	  $("#demoForm1").setFormValues(odd);
	  var xmmc1=$(odd).attr("XMMC");
	  var bdmc1=$(odd).attr("BDMC");
	  $("#XMMC").val(xmmc1);
	  $("#BDMC").val(bdmc1);
	  //判断是否有标段名称
       if(bdmc1=='')
		  {
    	   $("#BDMC").val('此设计文件管理只针对项目');
           $("#BDMC").attr("style","color:red;");
           $("#BDMC").removeAttr("fieldname");
		  }
       queryRenWuShu();
  }
  //默认查询 
	function queryRenWuShu()
	{
		  var sjwybh=$('#SJWYBH').val();
		  $("#Q_SJWYBH").val(sjwybh);
		  var nd=$('#ND').val();
		  $("#Q_ND").val(nd);
		  var data = combineQuery.getQueryCombineData(queryForm,frmPost,DT1);
			//调用ajax插入
		  defaultJson.doQueryJsonList(controllername + "?queryRenWuShu",data,DT1,null,true);
	}  
   //附件类别
	function changeLB(lb)
	{
	   if(lb!="")
		   {
			$("#shangchuanID").attr("fjlb",lb);
			$("#shangchuanID1").attr("fjlb",lb);
			
		   }
	}
	//任务书类别
	function changeRWSLB(rwslb){
		var lb = '1';
		var src='';
		switch(rwslb){
			case '2':
				lb = 'SJRWS';
				 src = "T#FS_DIC_TREE:DIC_CODE:DIC_VALUE:PARENT_ID ='9990000000158'  and sfyx='1' and DIC_CODE like '10%' ";
				break;
			case '1':
				lb = 'KCRWS';
				src = "T#FS_DIC_TREE:DIC_CODE:DIC_VALUE:PARENT_ID ='9990000000158'  and sfyx='1' and DIC_CODE like '00%' ";
				break;
			default:
				lb = 'lb';
		}
		
		if(lb!='lb')
			{
			$("#LB").attr('src',src);
			$("#LB").removeAttr('disabled');
			reloadSelectTableDic($("#LB"));
			}
		else{
	    	$("#LB").attr('disabled','disabled');
		}
    }
	 //默认接收人
	   function jingbanren()
	  {
		  var account="<%= account%>";
		  setDefaultOption($("#JBR"),account);
	  }
</script>
<body>
<app:dialogs />
	<div class="container-fluid">
	<div class="row-fluid">
    <div class="B-small-from-table-autoConcise">
     <form method="post" id="queryForm"  >
      <table class="B-table" width="100%">
      <!--可以再此处加入hidden域作为过滤条件 -->
      <TR  style="display:none;">
	        <TD class="right-border bottom-border"></TD>
			<TD class="right-border bottom-border">
			<INPUT id="num" type="text" class="span12" kind="text" fieldname="rownum" value="1000" operation="<=" keep="true"/>
			<INPUT class="span12" type="text" id="Q_SJWYBH" fieldname="jhsj.SJWYBH" operation="=" keep="true"/>
			<INPUT class="span12" type="text" id="Q_ND" fieldname="jhsj.ND" operation="=" keep="true"/>
			</TD>
        </TR>
      </table>
      </form>
	    <div style="height:5px;"></div>
	    <div class="overFlowX">
	            <table width="100%" class="table-hover table-activeTd B-table" id="DT1" pageNum="5" type="single" nopromptmsg="true">
	                <thead>
	                   <tr>
		                    <th  name="XH" id="_XH">&nbsp;#&nbsp;</th>
							<th fieldname="RWSLX" >&nbsp;任务书类型&nbsp;</th>
							<th fieldname="LB">&nbsp;类别&nbsp;</th>
							<th fieldname="JSDW"  maxlength="15">&nbsp;接收单位&nbsp;</th>
							<th fieldname="FSSJ" tdalign="center">&nbsp;发送时间&nbsp;</th>
							<th fieldname="JBR"  >&nbsp;经办人&nbsp;</th>
	                    </tr> 
	            	    </thead>
	            	 <tbody>
	     	     </tbody>
	     	 </table>
	 	  </div>
		</div>
	</div>
		<div style="height:5px;"></div>
		<div class="row-fluid">
	<div class="B-small-from-table-autoConcise">
      <h4 class="title">任务书信息
          <span class="pull-right">
           <button id="example1" class="btn"  type="button">新增</button> 
           <button id="insert" class="btn"  type="button">保存</button>
           <button id="del" class="btn"  type="button">删除</button>
          </span>
       </h4>
     <form method="post"  id="demoForm1"  >
      <table class="B-table" width="100%">
         <TR  style="display: none">
	        <TD class="right-border bottom-border">  
	              <input type="text" class="span12" kind="text" id="JHSJID" keep="true" name = "JHSJID"  fieldname="JHSJID"  >
	              <input type="text" class="span12" kind="text" id="SJWYBH" keep="true" name = "SJWYBH"  fieldname="SJWYBH"  >
	               <input type="text" class="span12" kind="text" id="ND" keep="true" name = "ND"  fieldname="ND"  >
	              <input type="text" class="span12" kind="text" id=XMID keep="true" name = "XMID"  fieldname="XMID"  >
	              <input type="text" class="span12" kind="text" id="BDID" keep="true" name = "BDID"  fieldname="BDID"  >
                  <input type="text" class="span12" kind="text" id="GC_SJGL_RWSGL_ID" fieldname="GC_SJGL_RWSGL_ID"  >
             </TD>
			<TD class="right-border bottom-border"></TD>
        </TR>
       <tr>
	          <th  class="right-border bottom-border disabledTh">项目名称</th>
	          <td  class="right-border bottom-border"><input class="span12 xmmc" id="XMMC" keep="true" type="text"  check-type="required" disabled   fieldname="XMMC"  name = "XMMC"></td>
	          <th  class="right-border bottom-border disabledTh">标段名称</th>
	          <td   class="bottom-border"><input class="span12"  type="text"  id="BDMC" keep="true"  fieldname="BDMC" disabled name = "BDMC"></td>
        </tr> 
        <tr>
              <th width="10%" class="right-border bottom-border">任务书类型</th>
	          <td width="40%"  class="bottom-border">
	          		<select class="span4 5characters " id="RWSLX" check-type="required" name = "RWSLX" fieldname = "RWSLX"  kind="dic" src="RWSLX"  ></select></td>
	          <th width="10%" class="right-border bottom-border">类别</th>
	          <td width="40%" class="right-border bottom-border">
	              <select class="span6 8characters" id="LB" name = "LB" disabled  fieldname="LB" kind="dic" check-type="required"  src="" >
                  </select>
	          </td>
        </tr>
        <tr>
         <th width="7%" class="right-border bottom-border">接收单位</th>
	           <td  width="18%"  class="bottom-border">
	          	  <select class="span3 department " style="width: 80%" id="JSDW" name = "JSDW"  fieldname="JSDW" kind="dic" src="T#GC_CJDW:GC_CJDW_ID:DWMC:DWLX='1' " >
                  </select>
                   <a href="javascript:void(0)" title="点击选择单位"><i id="lydwSelect" selObj="JSDW" dwlx="1" class="icon-edit" onclick="selectCjdw('lydwSelect');" isLxSelect="1"></i></a>
                  </td>
               <th width="7%" class="right-border bottom-border">发送时间</th>
	           <td   width="18%"  class="right-border bottom-border"> 
	           <input class="span6 date" id="FSSJ"     type="date" check-type="required"   fieldname="FSSJ" name = "FSSJ" ></td>
        </tr>
        <tr>
         <th width="7%" class="right-border bottom-border">经办人</th>
        <td  width="18%"  class="bottom-border">
	           <select class="span12 person" kind="dic" id="JBR" name = "JBR" fieldname= "JBR" src="T#fs_org_person:ACCOUNT:NAME:DEPARTMENT= '<%=department%>'  AND PERSON_KIND = '3' AND FLAG='1'  order by sort" ></select>
        </tr>
       	<tr>
			<th width="8%" class="right-border bottom-border text-right">附件</th>
			<td colspan="5" class="bottom-border right-border">
				<div>
				<span class="btn btn-fileUpload" onclick="doSelectFile(this);" id="shangchuanID" fjlb="">
					<i class="icon-plus"></i>
						<span>添加文件...</span></span>
							<table role="presentation" class="table table-striped">
								<tbody fjlb="" id="shangchuanID1" class="files showFileTab"
									data-toggle="modal-gallery" data-target="#modal-gallery">
								</tbody>
							 </table>
				</div>
			  </td>
			</tr>
      </table>
      </form>
      </div>
    </div>
    </div>
	  <jsp:include page="/jsp/file_upload/fileupload_config.jsp" flush="true" />
	<div align="center">
		<FORM name="frmPost" method="post" style="display: none" target="_blank" id="frmPost" >
			<!--系统保留定义区域-->
			<input type="hidden" name="queryXML" id="queryXML"> 
			<input type="hidden" name="txtXML" id="txtXML"> 
			<input type="hidden" name="resultXML" id="resultXML">
			<input type="hidden" name="queryResult" id = "queryResult" />
			<input type="hidden" name="txtFilter"  order="desc" fieldname = "LRSJ" id = "txtFilter">
			<input type="text" name="ywid" id = "ywid" value="">
			<!--传递行数据用的隐藏域-->
			<input type="hidden" name="rowData">
		</FORM>
	</div>
</body>
</html>